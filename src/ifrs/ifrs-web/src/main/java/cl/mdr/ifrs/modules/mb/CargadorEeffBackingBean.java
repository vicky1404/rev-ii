package cl.mdr.ifrs.modules.mb;


 

 
import java.util.ArrayList;
import java.util.List;
 
import java.util.Map;
 
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
import cl.mdr.ifrs.ejb.entity.EstadoFinanciero;
import cl.mdr.ifrs.ejb.entity.Periodo;
import cl.mdr.ifrs.ejb.entity.TipoEstadoEeff;
import cl.mdr.ifrs.ejb.entity.VersionEeff;
import cl.mdr.ifrs.exceptions.EstadoFinancieroException;
import cl.mdr.ifrs.vo.CargadorEeffVO;

 
@ManagedBean(name="cargadorEeff") 
@ViewScoped
public class CargadorEeffBackingBean extends AbstractBackingBean {
   
   
   
    private transient Logger logger = Logger.getLogger(CargadorEeffBackingBean.class);   
    
    private List<VersionEeff> versionEeffList;
    private transient FileUpload richInputFile;
    private transient UploadedFile uploadedFile;
    private Map<Long, EstadoFinanciero> eeffMap;
    private Periodo periodo;
    VersionEeff versionEeff;
    private int sizeEeff =0;
    private int sizeEeffDetalle =0;
    private boolean renderedTablaResultado;
    private boolean renderTableRel = false;
    private CargadorEeffVO cargadorVO;
 
    public CargadorEeffBackingBean() {
    }
   
    @PostConstruct
    public void cargarPeriodo(){
        try{
           
            periodo = getFacadeService().getPeriodoService().findMaxPeriodoObj();
           
            versionEeffList = getFacadeService().getEstadoFinancieroService().getVersionEeffFindByPeriodo(periodo.getIdPeriodo());
            
            this.setRenderedTablaResultado(Boolean.FALSE);
           
        }catch(Exception e){
            logger.error(e);
            addErrorMessage("Error al buscar los periodos");
        }
    }
   
    /*public void buscarVersionEEFF(ActionEvent event){
       
        try{ 
            Long periodoLong = Long.valueOf(getFiltro().getPeriodo().getAnioPeriodo().concat(getFiltro().getPeriodo().getMesPeriodo()));
            Periodo periodo = getFacade().getMantenedoresTipoService().findByPeriodo(periodoLong);
            getFiltro().setPeriodo(periodo);
            renderVersiones = true;
        }catch(Exception e){
            agregarErrorMessage("Error al buscar las versiones para el periodo seleccionado");
            e.printStackTrace();
        }
       
    }*/
    
    private void init(){
    	
        versionEeff = null;
        renderTableRel = false;
        cargadorVO = null;
    }
   
    public String procesarArchivo(FileUploadEvent event) {
try {
            
            if(getUploadedFile() == null){
                init();
                addErrorMessage("Seleccione o Actualice el archivo que desea cargar");
                return null;
            }
            if(getUploadedFile().getInputstream() == null){
                init();
                addErrorMessage("Seleccione o Actualice el archivo que desea cargar");
                return null;
            }
            
            versionEeff = new VersionEeff();
            
            TipoEstadoEeff tipoEstadoEeff = getFacadeService().getEstadoFinancieroService().getTipoEstadoEeffById(TipoEstadoEeffEnum.INGRESADO.getKey());
            cargadorVO = getFacadeService().getCargadorEeffService().leerEeff(getUploadedFile().getInputstream());
            EeffUtil.setVersionEeffToEeffList(cargadorVO.getEeffList(), versionEeff);
            getFacadeService().getCargadorEeffService().validarNuevoEeff(cargadorVO.getEeffList(), periodo.getIdPeriodo(),cargadorVO);
            
            String emailFrom = PropertyManager.getInstance().getMessage("cargador_mail_from");
            String emailHost = PropertyManager.getInstance().getMessage("mail_host");
            String subject = PropertyManager.getInstance().getMessage("cargador_subject");
            
            getFacadeService().getCargadorEeffService().sendMailEeff(cargadorVO.getUsuarioGrupoList(), emailFrom, subject, emailHost);
            
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
            versionEeff.setPeriodo(periodo);
            versionEeff.setEstadoFinancieroList(cargadorVO.getEeffList());
            
        } catch (EstadoFinancieroException e) {
            
            addErrorMessage("El archivo presenta los siguiente errores : ");
            
            for(String str : e.getDetailErrors())
            	addErrorMessage(str);
        
        } catch (Exception e) {
            logger.error("error al procesar archivo excel ",e);
            addErrorMessage("Error al procesar el archivo");
        }
        return null;
    }
   
    
    public void guardarListener(ActionEvent event){
        try{
           
        	if(versionEeff!=null && cargadorVO.getEeffList()!=null)
                getFacadeService().getEstadoFinancieroService().persisVersionEeff(versionEeff);
            else{
                init();
                addErrorMessage("Debe cargar información antes de guardar");
                return;
            }
            
            versionEeffList = getFacadeService().getEstadoFinancieroService().getVersionEeffFindByPeriodo(periodo.getIdPeriodo());
            
            addInfoMessage("Se ha almacenado correctamente los estados financieros");
            addInfoMessage("Registros Cabecera :" + cargadorVO.getCatidadEeffProcesado());
            addInfoMessage("Registros Detalle  :" + cargadorVO.getCatidadEeffDetProcesado());
            
            init();
           
        }catch(Exception e){
            logger.error("error al guardar eeff", e);
            addErrorMessage("Error al guardar información");
        }
    }
   
    public void archivoEstructuraValidator(FacesContext facesContext, UIComponent uIComponent, Object object){
 
        try {
            setUploadedFile(GeneradorDisenoHelper.archivoEstructuraValidator(facesContext, (UploadedFile)object, (FileUpload) uIComponent));
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            addErrorMessage(PropertyManager.getInstance().getMessage("Error al procesar archivo"));
        }
    }
   
    
 
    public void setVersionEeffList(List<VersionEeff> versionEeffList) {
        this.versionEeffList = versionEeffList;
    }
 
    public List<VersionEeff> getVersionEeffList() {
        return versionEeffList;
    }
 
    public void setRichInputFile(FileUpload richInputFile) {
        this.richInputFile = richInputFile;
    }
 
    public FileUpload getRichInputFile() {
        return richInputFile;
    }
 
    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }
 
    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }
 
    public Periodo getPeriodo() {
        return periodo;
    }
 
    public int getSizeEeff() {
        return sizeEeff;
    }
 
    public int getSizeEeffDetalle() {
        return sizeEeffDetalle;
    }

	public boolean isRenderedTablaResultado() {
		return renderedTablaResultado;
	}

	public void setRenderedTablaResultado(boolean renderedTablaResultado) {
		this.renderedTablaResultado = renderedTablaResultado;
	}

	public boolean isRenderTableRel() {
		return renderTableRel;
	}

	public void setRenderTableRel(boolean renderTableRel) {
		this.renderTableRel = renderTableRel;
	}

	public CargadorEeffVO getCargadorVO() {
		return cargadorVO;
	}

	public void setCargadorVO(CargadorEeffVO cargadorVO) {
		this.cargadorVO = cargadorVO;
	}
}
