package cl.mdr.ifrs.modules.mb;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.util.PropertyManager;
import cl.mdr.ifrs.ejb.common.EstadoCuadroEnum;
import cl.mdr.ifrs.ejb.entity.Periodo;
import cl.mdr.ifrs.ejb.entity.Version;

@ManagedBean(name ="periodoBackingBean")
@ViewScoped
public class PeriodoBackingBean extends AbstractBackingBean{
	
    private transient Logger logger = Logger.getLogger(PeriodoBackingBean.class);
	
    public String cerrarPeriodo(){
        
        int error = 0;
    
        try {
                List<Version> versionesVigentesList = getListaVersionesVigentes();
                Long periodo = this.getUltimoPeriodo();
            
                    if (this.cuadrosCerrados(periodo, versionesVigentesList)){
                    
                        Periodo per = this.getFacadeService().getPeriodoService().findPeriodoByPeriodo(periodo); //Obtiene el periodo que se esta cerrando.
                        
                        if (!per.getEstadoPeriodo().getIdEstadoPeriodo().equals(1L)){ //Pregunta si el estado del periodo obtenido esta cerrado.
                            
                                error = this.getFacadeService().getPeriodoService().cerrarPeriodo(this.getNombreUsuario(), periodo);
                                
                                if (error == 0){
                                        addInfoMessage(PropertyManager.getInstance().getMessage("cerrar_periodo_mensaje_periodo_cerrado"));
                                        return null;
                                } else {
                                        addErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_aplicacion_error"));
                                        return null;
                                    }
                                
                        } else {
                            addWarnMessage(PropertyManager.getInstance().getMessage("cerrar_periodo_mensaje_error_periodo_ya_esta_cerrado"));
                            }
                        
                    } else {
                            addWarnMessage(getCuadrosAbiertos(periodo, versionesVigentesList));
                    }
            
        } catch (Exception e) {
            logger.error(e.getMessage(), e); 
            addWarnMessage(e.getMessage() + " - " + e.getCause()); 
        }
        
        return null;
    }
        
	private Long getUltimoPeriodo() throws Exception {
	        return this.getFacadeService().getPeriodoService().findMaxPeriodo();
	}
	
	private List<Version> getListaVersionesVigentes(){
	    return this.getFacadeService().getVersionService().findAllVersionVigente(); //Lista de versiones vigentes
	}
	
    private boolean cuadrosCerrados(Long periodo, List<Version> versionesVigentesList){
        boolean abrir = true;
        
                for (Version version : versionesVigentesList){
            		if(version.getPeriodo().getPeriodo().equals(periodo)){
            			if (!version.getEstado().getIdEstado().equals(EstadoCuadroEnum.CERRADO.getKey()) ){ //Todos los cuadros deben estar cerrados
                            abrir = false;
                     }  
            		}
                }
        
        return abrir;
    }
    
    private String getCuadrosAbiertos(Long periodo, List<Version> versionesVigentesList){
        
        StringBuffer notasAbiertas = new StringBuffer();
        
            for (Version version : versionesVigentesList){
                if (version.getPeriodo().getPeriodo().equals(periodo)){
                    if (!version.getEstado().getIdEstado().equals(EstadoCuadroEnum.CERRADO.getKey()) ){ //Todos los cuadros deben estar cerrados
                           if (notasAbiertas.length() > 0){
                                   notasAbiertas.append(" , ");
                           } else {
                                   notasAbiertas.append(PropertyManager.getInstance().getMessage("cerrar_periodo_mensaje_error_cuadros_cerradas"));
                               }
                            notasAbiertas.append(version.getCatalogo().getNombre());
                    }
                }  
            }
        
            return notasAbiertas.toString();
    }

}
