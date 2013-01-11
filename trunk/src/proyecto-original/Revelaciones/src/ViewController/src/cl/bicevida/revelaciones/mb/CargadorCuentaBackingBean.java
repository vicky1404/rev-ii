package cl.bicevida.revelaciones.mb;


import cl.bicevida.revelaciones.common.mb.SoporteBackingBean;
import cl.bicevida.revelaciones.common.util.GeneradorDisenoHelper;
import cl.bicevida.revelaciones.common.util.PropertyManager;
import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.revelaciones.ejb.entity.CodigoFecu;
import cl.bicevida.revelaciones.ejb.entity.CuentaContable;
import cl.bicevida.revelaciones.ejb.entity.Periodo;
import cl.bicevida.revelaciones.exceptions.EstadoFinancieroException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import oracle.adf.view.rich.component.rich.input.RichInputFile;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.model.UploadedFile;


public class CargadorCuentaBackingBean extends SoporteBackingBean{

    @SuppressWarnings("compatibility:-3316758757392848676")
    private static final long serialVersionUID = -5732294012637739798L;
    private transient Logger logger = Logger.getLogger(CargadorCuentaBackingBean.class);    
    
    private List<CuentaContable> cuentaEditarList;
    private Periodo periodo;
    private List<CuentaContable> cuentaList;
    
    private transient RichInputFile richInputFile; 
    private transient UploadedFile uploadedFile;
    
    public CargadorCuentaBackingBean() {
    }
    
    @PostConstruct
    private void init(){
        try{
            periodo = getFacade().getPeriodoService().findMaxPeriodoObj();
            cuentaEditarList = getFacade().getEstadoFinancieroService().getCuentaContableAll();
        }catch(Exception e){
            logger.error(e);
            agregarErrorMessage(PropertyManager.getInstance().getMessage("eeff_error_periodo"));
        }
    }
    
    public String procesarArchivo(){
        
        try{
            
            this.cuentaList = null;
            
            if(getUploadedFile() == null){
                init();
                agregarErrorMessage(PropertyManager.getInstance().getMessage("eeff_actualizar_archivo"));
                return null;
            }
            if(getUploadedFile().getInputStream() == null){
                init();
                agregarErrorMessage(PropertyManager.getInstance().getMessage("eeff_actualizar_archivo"));
                return null;
            }
            
            this.cuentaList = getFacade().getFecuService().leerCuenta(getUploadedFile().getInputStream());
            
        } catch (EstadoFinancieroException e) {
            
            agregarErrorMessage(PropertyManager.getInstance().getMessage("eeff_archivo_error"));
            
            for(String str : e.getDetailErrors())
                agregarErrorMessage(str);
        
        } catch (Exception e) {
            logger.error("error al procesar archivo excel ",e);
            agregarErrorMessage(PropertyManager.getInstance().getMessage("eeff_error_procesar_archivo"));
        }
        
        return null;
    }
    
    
    public void editarListener(ActionEvent event){
        
        this.cuentaList = null;
        
        try{
            if(Util.esListaValida(cuentaEditarList)){
                getFacade().getFecuService().mergeCuenta(cuentaEditarList);
                agregarSuccesMessage("Se ha editado correctamente la información");
            }else{
                agregarWarnMessage("No hay información para editar");
            }
        }catch(Exception e){
            agregarErrorMessage("Se ha producido un error al editar");
            logger.error("Error al al editar fecu",e);
        }
        
    }
    
    public void guardarListener(ActionEvent event){
        
        try{
            if(Util.esListaValida(cuentaList)){
                getFacade().getFecuService().persistCuenta(cuentaList);
                this.cuentaList = null;
                this.cuentaEditarList = getFacade().getEstadoFinancieroService().getCuentaContableAll();
                agregarSuccesMessage("Se ha almacenado correctamente la información");
            }else{
                agregarWarnMessage("Se debe cargar Excel con cuentas nuevas");
            }
        }catch(Exception e){
            agregarErrorMessage("Se ha producido un error al insertar");
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
            setUploadedFile(GeneradorDisenoHelper.archivoEstructuraValidator(facesContext, (UploadedFile)object, (RichInputFile)uIComponent));
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            agregarErrorMessage(PropertyManager.getInstance().getMessage("eeff_error_procesar_archivo"));
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

    public void setRichInputFile(RichInputFile richInputFile) {
        this.richInputFile = richInputFile;
    }

    public RichInputFile getRichInputFile() {
        return richInputFile;
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
}
