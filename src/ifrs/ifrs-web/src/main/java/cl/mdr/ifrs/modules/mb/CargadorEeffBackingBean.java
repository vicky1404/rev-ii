package cl.mdr.ifrs.modules.mb;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;
import org.primefaces.component.fileupload.FileUpload;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.util.GeneradorDisenoHelper;
import cl.mdr.ifrs.cross.util.PropertyManager;
import cl.mdr.ifrs.ejb.common.TipoEstadoEeffEnum;
import cl.mdr.ifrs.ejb.cross.EeffUtil;
import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.Periodo;
import cl.mdr.ifrs.ejb.entity.PeriodoEmpresa;
import cl.mdr.ifrs.ejb.entity.TipoEstadoEeff;
import cl.mdr.ifrs.ejb.entity.VersionEeff;
import cl.mdr.ifrs.exceptions.EstadoFinancieroException;
import cl.mdr.ifrs.vo.CargadorEeffVO;


/**
 * Manejador para las cargas de estados financieros.
 * @author mdr tech ltda.
 * @since 28/08/2012
 */


@ManagedBean(name="cargadorEeff") 
@ViewScoped
public class CargadorEeffBackingBean extends AbstractBackingBean {
    
    private transient Logger logger = Logger.getLogger(CargadorEeffBackingBean.class);    
    
    private List<VersionEeff> versionEeffList;
    private transient FileUpload richInputFile;
    private transient UploadedFile uploadedFile;
    private PeriodoEmpresa periodoEmpresa;
    private VersionEeff versionEeff;
    private CargadorEeffVO cargadorVO;
    private boolean renderTableRel = false;

    public CargadorEeffBackingBean() {
    }
    
    @PostConstruct
    public void cargar(){
        try{
            
        	periodoEmpresa = getFacadeService().getPeriodoService().getMaxPeriodoEmpresaByEmpresa(getFiltroBackingBean().getEmpresa().getIdRut());
            versionEeffList = getFacadeService().getEstadoFinancieroService().getVersionEeffFindByPeriodo(periodoEmpresa.getIdPeriodo(), periodoEmpresa.getIdRut());
            
        }catch(Exception e){
            logger.error(e);
            addErrorMessage(PropertyManager.getInstance().getMessage("carga_eeff_error_buscar_periodo"));
        }
    }
    
    private void init(){
        versionEeff = null;
        renderTableRel = false;
        cargadorVO = null;
    }
    
    public String procesarArchivo(FileUploadEvent event) {
        try {
        	
        	this.setUploadedFile(event.getFile()); 
            
            if(getUploadedFile() == null){
                init();
                addErrorMessage(PropertyManager.getInstance().getMessage("carga_eeff_seleccione_actualice_archivo"));
                return null;
            }
            /*if(getUploadedFile().getInputStream() == null){
                init();
                addErrorMessage(PropertyManager.getInstance().getMessage("carga_eeff_seleccione_actualice_archivo"));
                return null;
            }*/
            
            versionEeff = new VersionEeff();
            TipoEstadoEeff tipoEstadoEeff = getFacadeService().getEstadoFinancieroService().getTipoEstadoEeffById(TipoEstadoEeffEnum.INGRESADO.getKey());
            cargadorVO = getFacadeService().getCargadorEeffService().leerEeff(getUploadedFile().getInputstream());
            EeffUtil.setVersionEeffToEeffList(cargadorVO.getEeffList(), versionEeff);
            getFacadeService().getCargadorEeffService().validarNuevoEeff(cargadorVO.getEeffList(), periodoEmpresa, cargadorVO);

            if(
                Util.esListaValida(cargadorVO.getEeffDescuadreList())||
                Util.esListaValida(cargadorVO.getEeffDetDescuadreList()) ||
                Util.esListaValida(cargadorVO.getEeffBorradoList()) ||
                Util.esListaValida(cargadorVO.getEeffDetBorradoList()) ||            
                Util.esListaValida(cargadorVO.getRelEeffDescuadreList()) ||
                Util.esListaValida(cargadorVO.getRelEeffDetDescuadreList()) ||
                Util.esListaValida(cargadorVO.getRelEeffBorradoList()) ||
                Util.esListaValida(cargadorVO.getRelEeffDetBorradoList())
            ) renderTableRel = true;
            
            versionEeff.setTipoEstadoEeff(tipoEstadoEeff);
            versionEeff.setUsuario(getNombreUsuario());
            versionEeff.setVigencia(1L);
            versionEeff.setPeriodoEmpresa(periodoEmpresa);
            versionEeff.setEstadoFinancieroList(cargadorVO.getEeffList());
            
        } catch (EstadoFinancieroException e) {
            
        	addErrorMessage(PropertyManager.getInstance().getMessage("carga_eeff_archivos_presenta_siguientes_errores"));
            
            for(String str : e.getDetailErrors())
                addErrorMessage(str);
        
        } catch (Exception e) {
        	logger.error(PropertyManager.getInstance().getMessage("carga_eeff_error_procesar_archivo_excel"),e);
            addErrorMessage(PropertyManager.getInstance().getMessage("carga_eeff_error_procesar_archivo_excel"));
        }
        return null;
    }
    
    
    public void guardarListener(ActionEvent event){
        try{
            
            if(versionEeff!=null && cargadorVO.getEeffList()!=null)
                getFacadeService().getEstadoFinancieroService().persisVersionEeff(versionEeff);
            else{
                init();
                addErrorMessage(PropertyManager.getInstance().getMessage("carga_eeff_error_debe_argar_info_antes_guardar"));
                return;
            }
            
            versionEeffList = getFacadeService().getEstadoFinancieroService().getVersionEeffFindByPeriodo(periodoEmpresa.getIdPeriodo(), periodoEmpresa.getIdRut());
            
            
            
            
	           
	            getFacadeService().getCargadorEeffService().sendMailEeff(cargadorVO.getUsuarioGrupoList());
	            
	            addInfoMessage(PropertyManager.getInstance().getMessage("eeff_mensaje_guardar_ok"));
	            addInfoMessage(PropertyManager.getInstance().getMessage("eeff_codigo_fecu_procesados") + cargadorVO.getCatidadEeffProcesado());
	            addInfoMessage(PropertyManager.getInstance().getMessage("eeff_cuentas_procesadas") + cargadorVO.getCatidadEeffDetProcesado());
            
         
            
            addInfoMessage(PropertyManager.getInstance().getMessage("carga_eeff_guardar_correcto"));
            
            init();
            
        }catch(Exception e){
        	logger.error(PropertyManager.getInstance().getMessage("carga_eeff_error_guardar_informacion"), e);
            addErrorMessage(PropertyManager.getInstance().getMessage("carga_eeff_error_guardar_informacion"));
        }
    }
    
    public void archivoEstructuraValidator(FacesContext facesContext, UIComponent uIComponent, Object object){
    	 
        try {
            setUploadedFile(GeneradorDisenoHelper.archivoEstructuraValidator(facesContext, (UploadedFile)object, (FileUpload) uIComponent));
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            addErrorMessage(PropertyManager.getInstance().getMessage("carga_eeff_error_procesar_archivo"));
        }
    }

    public void setVersionEeffList(List<VersionEeff> versionEeffList) {
        this.versionEeffList = versionEeffList;
    }

    public List<VersionEeff> getVersionEeffList() {
        return versionEeffList;
    }

    public FileUpload getRichInputFile() {
		return richInputFile;
	}

	public void setRichInputFile(FileUpload richInputFile) {
		this.richInputFile = richInputFile;
	}

	public PeriodoEmpresa getPeriodoEmpresa() {
		return periodoEmpresa;
	}

	public void setPeriodoEmpresa(PeriodoEmpresa periodoEmpresa) {
		this.periodoEmpresa = periodoEmpresa;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setCargadorVO(CargadorEeffVO cargadorVO) {
        this.cargadorVO = cargadorVO;
    }

    public CargadorEeffVO getCargadorVO() {
        return cargadorVO;
    }

    public boolean isRenderTableRel() {
        return renderTableRel;
    }
}