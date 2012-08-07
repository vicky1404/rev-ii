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
   
    public String procesarArchivo(FileUploadEvent event) {
        try {
           
            if(event.getFile()  == null){
                addErrorMessage("Seleccione o Actualice el archivo que desea cargar");
                return null;
            }
            if(event.getFile().getInputstream()  == null){
                addErrorMessage("Seleccione o Actualice el archivo que desea cargar");
                return null;
            }
            versionEeff = new VersionEeff();
            TipoEstadoEeff tipoEstadoEeff = getFacadeService().getEstadoFinancieroService().getTipoEstadoEeffById(TipoEstadoEeffEnum.INGRESADO.getKey());
            eeffMap = getFacadeService().getCargadorEeffService().leerEeff(event.getFile().getInputstream());
            List<EstadoFinanciero> eeffList = new ArrayList<EstadoFinanciero>();
           
            versionEeff.setTipoEstadoEeff(tipoEstadoEeff);
            versionEeff.setUsuario(getNombreUsuario());
            versionEeff.setVigencia(1L);
            versionEeff.setPeriodo(periodo);
           
            for(EstadoFinanciero eeff : eeffMap.values()){
               
                if(eeff.getDetalleEeffList4()!=null)
                    sizeEeffDetalle += eeff.getDetalleEeffList4().size();
               
                eeff.setVersionEeff(versionEeff);
               
                eeffList.add(eeff);
            }
           
            sizeEeff = eeffList.size();
           
            versionEeff.setEstadoFinancieroList(eeffList);
           
        } catch (EstadoFinancieroException e) {
           
        	if (e.getDetailErrors().size() > 0){
            addErrorMessage("El archivo presenta los siguiente errores : ");
           
            for(String str : e.getDetailErrors())
            	addErrorMessage(str);
        	}
        	
        	if (e.getMessage().length() > 0){
        		addErrorMessage("El archivo tiene el siguiente error : " + e.getMessage());        		
        	}
            
            
           
       
        } catch (Exception e) {
            logger.error("error al procesar archivo excel ",e);
            addErrorMessage("Error al procesar el archivo");
        }
        return null;
    }
   
    
    public void guardarListener(ActionEvent event){
        try{
           
            if(versionEeff!=null && eeffMap!=null){
                getFacadeService().getEstadoFinancieroService().persisVersionEeff(versionEeff);
           
            versionEeffList = getFacadeService().getEstadoFinancieroService().getVersionEeffFindByPeriodo(periodo.getIdPeriodo());
           
            addInfoMessage("Se ha almacenado correctamente los estados financieros");
            addInfoMessage("Registros Cabecera :" + sizeEeff);
            addInfoMessage("Registros Detalle  :" + sizeEeffDetalle);
           
            versionEeff = null;
            sizeEeff = 0;
            sizeEeffDetalle = 0;
            eeffMap = null;
            
            this.setRenderedTablaResultado(Boolean.TRUE);
            }
            addWarnMessage("No hay nada que guardar");
           
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
}
