package cl.bicevida.revelaciones.mb;


import cl.bicevida.revelaciones.common.mb.SoporteBackingBean;
import cl.bicevida.revelaciones.common.util.PropertyManager;
import cl.bicevida.revelaciones.ejb.common.EstadoCuadroEnum;
import cl.bicevida.revelaciones.ejb.entity.Periodo;
import cl.bicevida.revelaciones.ejb.entity.Version;
import cl.bicevida.revelaciones.ejb.entity.VersionPeriodo;

import java.io.Serializable;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.List;

import oracle.adf.view.rich.event.DialogEvent;

import org.apache.log4j.Logger;


public class AbrirPeriodoBackingBean extends SoporteBackingBean implements Serializable{

    @SuppressWarnings("compatibility:-1515786930698466238")
    private static final long serialVersionUID = 3595928840245930338L;
    private transient Logger logger = Logger.getLogger(AbrirPeriodoBackingBean.class);
    private static final int ERROR_CERRAR_PERIODO = -1000;

    public AbrirPeriodoBackingBean() {
        super();
    }
    
    public void confirmCerrarPeriodo(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.ok)) {
            this.cerrarPeriodo();
        }        
    }
    
    public void confirmAbrirPeriodo(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.ok)) {
            this.abrirPeriodo();
        }        
    }
    
    
    public String cerrarPeriodo(){
        
        int error = 0;
    
        try {
                List<Version> versionesVigentesList = this.getListaVersionesVigentes();
                Long periodo = this.getUltimoPeriodo();
            
                    if (this.cuadrosCerrados(periodo, versionesVigentesList)){
                    
                        Periodo per = this.getFacade().getPeriodoService().findPeriodoByPeriodo(periodo); //Obtiene el periodo que se esta cerrando.
                        
                        if (!per.getEstadoPeriodo().getIdEstadoPeriodo().equals(1L)){ //Pregunta si el estado del periodo obtenido esta cerrado.
                            
                                error = this.getFacade().getPeriodoService().cerrarPeriodo(this.getNombreUsuario(), periodo);
                                
                                if (error == 0){
                                        super.agregarSuccesMessage(PropertyManager.getInstance().getMessage("general_mensaje_periodo_cerrado"));
                                        return null;
                                } else {
                                        super.agregarErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_aplicacion_error"));
                                        return null;
                                    }
                                
                        } else {
                            super.agregarWarnMessage(PropertyManager.getInstance().getMessage("general_mensaje_error_periodo_ya_esta_cerrado"));
                            }
                        
                    } else {
                            super.agregarWarnMessage(this.getCuadrosAbiertos(periodo, versionesVigentesList));
                    }
            
        } catch (Exception e) {
            logger.error(e.getMessage(), e); 
            super.agregarWarnMessage(e.getMessage() + " - " + e.getCause()); 
        }
        
        return null;
    }
    
    
    public String abrirPeriodo() {
        
        int error = 0;
        
        try {
            
                Long periodo = this.getPeriodoCerradoActual();
            
            if (periodo != null && periodo > 0){                    
                        error = this.getFacade().getPeriodoService().abrirPeriodo(this.getNombreUsuario());                        
                        if (error == 0){
                            super.agregarSuccesMessage(PropertyManager.getInstance().getMessage("general_mensaje_periodo_abierto"));
                            super.getComponenteBackingBean().setPeriodoActual(null);
                            return null;
                        }else if (error == ERROR_CERRAR_PERIODO){
                            super.agregarErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_error_debe_cerrar_periodo"));
                            return null;
                        } else {
                            super.agregarErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_aplicacion_error"));
                            return null;
                        }

            } else {
                    super.agregarErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_error_debe_cerrar_periodo"));
                }
            
        } catch (Exception e) {
            logger.error(e.getMessage(), e);            
            super.agregarErrorMessage(e.getMessage() + " - " + e.getCause()); 
        }
        
        return null;
    }
    
    
    private Long getPeriodoCerradoActual() throws Exception {
            return this.getFacade().getPeriodoService().findMaxPeriodoCerrado();
    }
    
    private Long getUltimoPeriodo() throws Exception {
            return this.getFacade().getPeriodoService().findMaxPeriodo();
    }
    
    public String getPeriodoActual() {
    
        String retorno = ""; 

        try {
            
            String periodoStr = String.valueOf(this.getUltimoPeriodo());
            if (periodoStr != null && periodoStr.length() > 0 ){
                    retorno = periodoStr.substring(0, 4);
                    retorno = retorno + "-";
                    retorno = retorno + periodoStr.substring(4, periodoStr.length());
            } else {
                    retorno = PropertyManager.getInstance().getMessage("general_mensaje_error_periodo_no_existe");
                }
            
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return retorno;
        }
    
    public String getProximoPeriodo() {
    
        String retorno = "";
        

        try {
            
            String periodoStr = this.getPeriodoActual();
            
            if (periodoStr != null && periodoStr.length() > 0){
                
                    Calendar c1 = Calendar.getInstance(); 
                    String[] fecha = periodoStr.split("-");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                    c1.set(Integer.parseInt(fecha[0]), Integer.parseInt(fecha[1])-1, 1);
                    c1.add(Calendar.MONTH, 3);
                    retorno = sdf.format(c1.getTime());
            }
            
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return retorno;
        }
    
    
    private List<Version> getListaVersionesVigentes(){
            return this.getFacade().getVersionService().findAllVersionVigente(); //Lista de versiones vigentes
    }
    
    private boolean cuadrosCerrados(Long periodo, List<Version> versionesVigentesList){
        boolean abrir = true;
        
                for (Version version : versionesVigentesList){
                    //if (version.getCatalogo().getTipoCuadro().getIdTipoCuadro().equals(1L)){ //Si la version es una nota (Nota ; TipoCuadro = 1 )
                        for (VersionPeriodo versionPeriodo : version.getVersionPeriodoList()){
                            if (versionPeriodo.getPeriodo().getPeriodo().equals(periodo)){
                                if (!versionPeriodo.getEstado().getIdEstado().equals(EstadoCuadroEnum.CERRADO.getKey()) ){ //Todos los cuadros deben estar cerrados
                                       abrir = false;
                                }  
                            }
                        }
                    //}  
                }
        
        return abrir;
    }
    
    private String getCuadrosAbiertos(Long periodo, List<Version> versionesVigentesList){
        
        StringBuffer notasAbiertas = new StringBuffer();
        
            for (Version version : versionesVigentesList){
                //if (version.getCatalogo().getTipoCuadro().getIdTipoCuadro().equals(1L)){ //Si la version es una nota (Nota ; TipoCuadro = 1 )
                    for (VersionPeriodo versionPeriodo : version.getVersionPeriodoList()){
                        if (versionPeriodo.getPeriodo().getPeriodo().equals(periodo)){
                            if (!versionPeriodo.getEstado().getIdEstado().equals(EstadoCuadroEnum.CERRADO.getKey()) ){ //Todos los cuadros deben estar cerrados
                                   if (notasAbiertas.length() > 0){
                                           notasAbiertas.append(" , ");
                                   } else {
                                           notasAbiertas.append(PropertyManager.getInstance().getMessage("general_mensaje_error_cuadros_cerradas"));
                                       }
                                    notasAbiertas.append(version.getCatalogo().getNombre());
                            }
                         }  
                     }
                 //}
            }
        
            return notasAbiertas.toString();
        
        }
    
    
    
}
