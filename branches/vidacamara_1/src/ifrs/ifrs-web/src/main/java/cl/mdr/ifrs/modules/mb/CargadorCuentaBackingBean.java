package cl.mdr.ifrs.modules.mb;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

//import oracle.adf.view.rich.component.rich.input.RichInputFile;

import org.apache.log4j.Logger;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.util.GeneradorDisenoHelper;
import cl.mdr.ifrs.cross.util.PropertyManager;
//import org.apache.myfaces.trinidad.model.UploadedFile;
import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.CuentaContable;
import cl.mdr.ifrs.ejb.entity.Periodo;
import cl.mdr.ifrs.exceptions.EstadoFinancieroException;

import org.primefaces.component.fileupload.FileUpload;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 * Manejador para las cargas de las cuentas contables.
 * @author mdr tech ltda.
 * @since 13/01/2013
 */

@ManagedBean(name="cargadorCuentaBackingBean") 
@ViewScoped
public class CargadorCuentaBackingBean extends AbstractBackingBean{


    private transient Logger logger = Logger.getLogger(CargadorCuentaBackingBean.class);    
    
    private List<CuentaContable> cuentaEditarList;
    private Periodo periodo;
    private List<CuentaContable> cuentaList;
    
    private transient FileUpload richInputFile;
    private transient UploadedFile uploadedFile;
    
    public CargadorCuentaBackingBean() {
    }
    
    @PostConstruct
    private void init(){
        try{
            periodo = getFacadeService().getPeriodoService().findMaxPeriodoObj();
            cuentaEditarList = getFacadeService().getEstadoFinancieroService().getCuentaContableAll();
        }catch(Exception e){
            logger.error(e);
            addErrorMessage(PropertyManager.getInstance().getMessage("eeff_error_periodo"));
        }
    }
    
    public String procesarArchivo(FileUploadEvent event){
        
        try{
            
            this.cuentaList = null;
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
            
            this.cuentaList = getFacadeService().getFecuService().leerCuenta(getUploadedFile().getInputstream());
            
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
        
        this.cuentaList = null;
        
        try{
            if(Util.esListaValida(cuentaEditarList)){
                getFacadeService().getFecuService().mergeCuenta(cuentaEditarList);
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
            if(Util.esListaValida(cuentaList)){
                getFacadeService().getFecuService().persistCuenta(cuentaList);
                this.cuentaList = null;
                this.cuentaEditarList = getFacadeService().getEstadoFinancieroService().getCuentaContableAll();
                addInfoMessage("Se ha almacenado correctamente la información");
            }else{
                addWarnMessage("Se debe cargar Excel con cuentas nuevas");
            }
        }catch(Exception e){
            addErrorMessage("Se ha producido un error al insertar");
            logger.error("Error al insertar cuenta",e);
        }
        
        
    }
 
    
    
 
    public String agregarCuenta(){
        
        List<CuentaContable> cuentaTempList = new ArrayList<CuentaContable>();
        cuentaTempList.add(new CuentaContable(true));
        cuentaTempList.addAll(this.cuentaEditarList);
        this.cuentaEditarList = cuentaTempList;
        
        return null;
    }
    
    
    public void archivoEstructuraValidator(FacesContext facesContext, UIComponent uIComponent, Object object){
        try {
            setUploadedFile(GeneradorDisenoHelper.archivoEstructuraValidator(facesContext, (UploadedFile)object, (FileUpload) uIComponent));
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

   

    public void setCuentaEditarList(List<CuentaContable> cuentaEditarList) {
        this.cuentaEditarList = cuentaEditarList;
    }

    public List<CuentaContable> getCuentaEditarList() {
        return cuentaEditarList;
    }

    public void setCuentaList(List<CuentaContable> cuentaList) {
        this.cuentaList = cuentaList;
    }

    public List<CuentaContable> getCuentaList() {
        return cuentaList;
    }

	public FileUpload getRichInputFile() {
		return richInputFile;
	}

	public void setRichInputFile(FileUpload richInputFile) {
		this.richInputFile = richInputFile;
	}
}
