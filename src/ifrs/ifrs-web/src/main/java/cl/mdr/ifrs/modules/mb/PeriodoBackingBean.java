package cl.mdr.ifrs.modules.mb;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;
import org.primefaces.model.chart.PieChartModel;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.util.PropertyManager;
import cl.mdr.ifrs.ejb.common.EstadoCuadroEnum;
import cl.mdr.ifrs.ejb.common.MensajePeriodoEnum;
import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.Empresa;
import cl.mdr.ifrs.ejb.entity.EstadoPeriodo;
import cl.mdr.ifrs.ejb.entity.PeriodoEmpresa;
import cl.mdr.ifrs.ejb.entity.Version;

@ManagedBean(name ="periodoBackingBean")
@ViewScoped
public class PeriodoBackingBean extends AbstractBackingBean{
	
    private transient Logger logger = Logger.getLogger(PeriodoBackingBean.class);
    private PeriodoEmpresa periodoEmpresa;
    private List<Version> versionSinCerrarList;
    private boolean renderTableVersion = false;
    private PieChartModel estadoCuadroChart;
    public int renderCharNumber = 0;

	@PostConstruct
    private void init(){
		
		if(isSelectedEmpresa()){
			periodoEmpresa = getFacadeService().getPeriodoService().getMaxPeriodoEmpresaByEmpresa(getFiltroBackingBean().getEmpresa().getIdRut());
		}
    }
    
    
    public String abrirPeriodo() {
    	
    	if(!isSelectedEmpresa())
    		return null;
        
        try {
            
        	List<PeriodoEmpresa> periodoEmpresaList = getFacadeService().getPeriodoService().getMaxPeriodoEmpresaNoCerrado();
            
        	if(Util.esListaValida(periodoEmpresaList)){
        		
        		addWarnMessage("No se puede abrir período, las siguientes empresas no han cerrado el período");
        	
	        	for(PeriodoEmpresa periodoE : periodoEmpresaList){
	        		if (!periodoE.getEstadoPeriodo().getIdEstadoPeriodo().equals(EstadoPeriodo.ESTADO_CERRADO)){
	        			addWarnMessage("Empresa : " + periodoE.getEmpresa().getNombre());
	        		}
	        	}
	        	
        	}else{
                
            	int keyMessage = getFacadeService().getPeriodoService().abrirPeriodo(this.getNombreUsuario());
                
            	if (keyMessage == 0){
            		getComponenteBackingBean().setPeriodoList(null);
                    addInfoMessage(PropertyManager.getInstance().getMessage("abrir_periodo_correcto"));
                    //addInfoMessage(MensajePeriodoEnum.CerrarPeriodo.getMensajeByKey(keyMessage).getValue());
                    return null;
	            } else {
	            	//addErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_aplicacion_error"));
	            	addErrorMessage(MensajePeriodoEnum.AbrirPeriodo.getMensajeByKey(keyMessage).getValue());
                    logger.error(MensajePeriodoEnum.AbrirPeriodo.getMensajeByKey(keyMessage).getValue());
                    return null;
	            }

            }
            
        } catch (Exception e) {
            addErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_aplicacion_error"));
            logger.error(e.getMessage(), e);
        }
        
        return null;
    }
    
    
    public String cerrarPeriodo(){
        
    	if(!isSelectedEmpresa())
    		return null;
    	
        try {
        	
        	periodoEmpresa = getFacadeService().getPeriodoService().getMaxPeriodoEmpresaByEmpresa(getFiltroBackingBean().getEmpresa().getIdRut());
            
        	if (!periodoEmpresa.getEstadoPeriodo().getIdEstadoPeriodo().equals(EstadoPeriodo.ESTADO_CERRADO)){
        		
		    	versionSinCerrarList = getFacadeService().getVersionService().findVersionVigenteSinCerrar(periodoEmpresa);
		    
		        if (!Util.esListaValida(versionSinCerrarList)){
		        	
		        	renderTableVersion = false;
		                               
		            int keyMessage = this.getFacadeService().getPeriodoService().cerrarPeriodo(periodoEmpresa, getNombreUsuario());
		            
		            if (keyMessage == 1 || keyMessage == 2){
		                    addInfoMessage(MensajePeriodoEnum.CerrarPeriodo.getMensajeByKey(keyMessage).getValue());
		                    return null;
		            } else {
		                    addErrorMessage(PropertyManager.getInstance().getMessage("cerrar_periodo_no_update"));
		                    logger.error(MensajePeriodoEnum.CerrarPeriodo.getMensajeByKey(keyMessage).getValue());
		                    return null;
		            }
		            
		        }else renderTableVersion = true;
            
        	}else addWarnMessage(PropertyManager.getInstance().getMessage("cerrar_periodo_mensaje_error_periodo_ya_esta_cerrado"));
            
        } catch (Exception e) {
        	addErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_aplicacion_error"));
            logger.error(e.getMessage(), e);  
        }
        
        return null;
    }
    
    public void displayEstadoChartAction(){
    	renderCharNumber = 1;
    	estadoCuadroChart = new PieChartModel();  			  
    	estadoCuadroChart.set(EstadoCuadroEnum.INICIADO.getValue(), select(versionSinCerrarList ,having(on(Version.class).getEstado().getIdEstado(), equalTo(EstadoCuadroEnum.INICIADO.getKey()))).size() );  
    	estadoCuadroChart.set(EstadoCuadroEnum.MODIFICADO.getValue(), select(versionSinCerrarList ,having(on(Version.class).getEstado().getIdEstado(), equalTo(EstadoCuadroEnum.MODIFICADO.getKey()))).size());  
    	estadoCuadroChart.set(EstadoCuadroEnum.POR_APROBAR.getValue(), select(versionSinCerrarList ,having(on(Version.class).getEstado().getIdEstado(), equalTo(EstadoCuadroEnum.POR_APROBAR.getKey()))).size());  
    	estadoCuadroChart.set(EstadoCuadroEnum.APROBADO.getValue(), select(versionSinCerrarList ,having(on(Version.class).getEstado().getIdEstado(), equalTo(EstadoCuadroEnum.APROBADO.getKey()))).size());  
		estadoCuadroChart.set(EstadoCuadroEnum.CERRADO.getValue(), select(versionSinCerrarList ,having(on(Version.class).getEstado().getIdEstado(), equalTo(EstadoCuadroEnum.CERRADO.getKey()))).size());
		estadoCuadroChart.set(EstadoCuadroEnum.CONTINGENCIA.getValue(), select(versionSinCerrarList ,having(on(Version.class).getEstado().getIdEstado(), equalTo(EstadoCuadroEnum.CONTINGENCIA.getKey()))).size());
		displayPopUp("popUpEstadoChart", "form_cerrar_periodo");
    }
    
    public void displayEmpresaChartAction(){
    	
    	renderCharNumber = 2;
    	estadoCuadroChart = new PieChartModel();
    	List<Empresa> empresaList = getFacadeService().getEmpresaService().findAll();
    	
    	for(Empresa empresa : empresaList){
	    	estadoCuadroChart.set(empresa.getNombre(), select(versionSinCerrarList ,having(on(Version.class).getCatalogo().getEmpresa().getIdRut(), equalTo(empresa.getIdRut()))).size() );
    	}
    	
    	displayPopUp("popUpEmpresaChart", "form_cerrar_periodo");
    }


	public PeriodoEmpresa getPeriodoEmpresa() {
		return periodoEmpresa;
	}


	public void setPeriodoEmpresa(PeriodoEmpresa periodoEmpresa) {
		this.periodoEmpresa = periodoEmpresa;
	}


	public List<Version> getVersionSinCerrarList() {
		return versionSinCerrarList;
	}


	public void setVersionSinCerrarList(List<Version> versionSinCerrarList) {
		this.versionSinCerrarList = versionSinCerrarList;
	}


	public boolean isRenderTableVersion() {
		return renderTableVersion;
	}


	public void setRenderTableVersion(boolean renderTableVersion) {
		this.renderTableVersion = renderTableVersion;
	}
	
	public PieChartModel getEstadoCuadroChart() {
		return estadoCuadroChart;
	}


	public void setEstadoCuadroChart(PieChartModel estadoCuadroChart) {
		this.estadoCuadroChart = estadoCuadroChart;
	}


	public int getRenderCharNumber() {
		return renderCharNumber;
	}


	public void setRenderCharNumber(int renderCharNumber) {
		this.renderCharNumber = renderCharNumber;
	}

}
