package cl.mdr.ifrs.modules.mb;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.util.GeneradorDisenoHelper;
import cl.mdr.ifrs.cross.util.PropertyManager;
import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.CodigoFecu;
import cl.mdr.ifrs.ejb.entity.Periodo;
import cl.mdr.ifrs.exceptions.EstadoFinancieroException;

import org.primefaces.component.fileupload.FileUpload;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;


/**
 * Manejador para las cargas de los codigos fecu.
 * @author mdr tech ltda.
 * @since 13/01/2013
 */
@ManagedBean(name="cargadorFecuBackingBean") 
@ViewScoped
public class CargadorFecuBackingBean extends AbstractBackingBean{


    private transient Logger logger = Logger.getLogger(CargadorFecuBackingBean.class);    
    
    private List<CodigoFecu> fecuEditarList;
    private Periodo periodo;
    private List<CodigoFecu> fecuList;
    
    private transient FileUpload richInputFile;
    private transient UploadedFile uploadedFile;
    
    public CargadorFecuBackingBean() {
    }
    
    @PostConstruct
    private void init(){
        try{
            periodo = getFacadeService().getPeriodoService().findMaxPeriodoObj();
            fecuEditarList = getFacadeService().getEstadoFinancieroService().getCodigoFecuAll();
        }catch(Exception e){
            logger.error(e);
            addErrorMessage(PropertyManager.getInstance().getMessage("eeff_error_periodo"));
        }
    }
    
    public String procesarArchivo(FileUploadEvent event){
        
        try{
            
            this.fecuList = null;
            this.setUploadedFile(event.getFile()); 
            if(getUploadedFile() == null){
                init();
                addErrorMessage(PropertyManager.getInstance().getMessage("eeff_actualizar_archivo"));
                return null;
            }
            if(getUploadedFile().getInputstream() == null){
                init();
                addErrorMessage(PropertyManager.getInstance().getMessage("eeff_actualizar_archivo"));
                return null;
            }
            
            this.fecuList = getFacadeService().getFecuService().leerFECU(getUploadedFile().getInputstream());
            
        } catch (EstadoFinancieroException e) {
            
            addErrorMessage(PropertyManager.getInstance().getMessage("eeff_archivo_error"));
            
            for(String str : e.getDetailErrors())
                addErrorMessage(str);
        
        } catch (Exception e) {
            logger.error("error al procesar archivo excel ",e);
            addErrorMessage(PropertyManager.getInstance().getMessage("eeff_error_procesar_archivo"));
        }
        
        return null;
    }
    
    
    public void editarListener(ActionEvent event){
        
        this.fecuList = null;
        
        try{
            if(Util.esListaValida(fecuEditarList)){
                getFacadeService().getFecuService().mergeFecu(fecuEditarList);
                addInfoMessage("Se ha editado correctamente la información");
            }else{
                addWarnMessage("No hay información para editar");
            }
        }catch(Exception e){
            addErrorMessage("Se ha producido un error al editar");
            logger.error("Error al al editar fecu",e);
        }
        
    }
    
    public void guardarListener(ActionEvent event){
        
        try{
            if(Util.esListaValida(fecuList)){
                getFacadeService().getFecuService().persistFecu(fecuList);
                this.fecuList = null;
                this.fecuEditarList = getFacadeService().getEstadoFinancieroService().getCodigoFecuAll();
                addInfoMessage("Se ha almacenado correctamente la información");
            }else{
                addWarnMessage("Se debe cargar Excel con código FECUs nuevos");
            }
        }catch(Exception e){
            addErrorMessage("Se ha producido un error al insertar");
            logger.error("Error al insertar fecu",e);
        }
        
    }
    
    public String agregarFecu(){
        
        List<CodigoFecu> feuTempList = new ArrayList<CodigoFecu>();
        feuTempList.add(new CodigoFecu(true));
        feuTempList.addAll(this.fecuEditarList);
        this.fecuEditarList = feuTempList;
        
        return null;
    }
    
    
    public void archivoEstructuraValidator(FacesContext facesContext, UIComponent uIComponent, Object object){
        try {
            setUploadedFile(GeneradorDisenoHelper.archivoEstructuraValidator(facesContext, (UploadedFile)object, (FileUpload)uIComponent));
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            addErrorMessage(PropertyManager.getInstance().getMessage("eeff_error_procesar_archivo"));
        }
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }


    public void setFecuList(List<CodigoFecu> fecuList) {
        this.fecuList = fecuList;
    }

    public List<CodigoFecu> getFecuList() {
        return fecuList;
    }

    public void setFecuEditarList(List<CodigoFecu> fecuEditarList) {
        this.fecuEditarList = fecuEditarList;
    }

    public List<CodigoFecu> getFecuEditarList() {
        return fecuEditarList;
    }

	public FileUpload getRichInputFile() {
		return richInputFile;
	}

	public void setRichInputFile(FileUpload richInputFile) {
		this.richInputFile = richInputFile;
	}
}
