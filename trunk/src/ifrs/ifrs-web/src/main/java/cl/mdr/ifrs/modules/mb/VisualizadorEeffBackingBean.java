package cl.mdr.ifrs.modules.mb;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.DetalleEeff;
import cl.mdr.ifrs.ejb.entity.EstadoFinanciero;
import cl.mdr.ifrs.ejb.entity.Periodo;
import cl.mdr.ifrs.ejb.entity.VersionEeff;

/**
 * Clase controladora para el modulo web Visualización de Estados Financieros
 * @author MDR Technology
 * @since 11/08/2012
 *
 */
@ManagedBean(name ="visualizador")
@ViewScoped
public class VisualizadorEeffBackingBean extends AbstractBackingBean{
	
private transient Logger logger = Logger.getLogger(VisualizadorEeffBackingBean.class);    
    
    private Periodo periodo;
    private VersionEeff versionEeff;
    private List<VersionEeff> versionEeffList;
    private List<EstadoFinanciero> eeffList;
    private List<DetalleEeff> eeffDetList;
    private String filtroTablaEeff;
    private boolean renderEeffList = false;
    private boolean renderEeffDetList = false;
    private String mesPeriodo;
    private String anioPeriodo;
    
    public VisualizadorEeffBackingBean() {
        
    }
    
    @PostConstruct
    public void init(){
        try{
            
            periodo = getFacadeService().getPeriodoService().findMaxPeriodoObj();
            mesPeriodo = periodo.getMesPeriodo();
            anioPeriodo = periodo.getAnioPeriodo();
            versionEeffList = getFacadeService().getEstadoFinancieroService().getVersionEeffFindByPeriodo(periodo.getIdPeriodo());
            getFiltroPeriodoEmpresa().setPeriodo(periodo);

        }catch(Exception e){
            logger.error(e);
            addErrorMessage("Error al buscar el periodo vigente");
        }
    }
    
    public void buscarVersionEeff(ActionEvent event){
        versionEeffList = null;
        eeffList = null;
        renderFalse();
        try{
            Long anioMes = Util.getLong(anioPeriodo.concat(mesPeriodo), 0L);
            periodo = getFacadeService().getMantenedoresTipoService().findByPeriodo(anioMes);
            getFiltroPeriodoEmpresa().setPeriodo(periodo);
            versionEeffList = getFacadeService().getEstadoFinancieroService().getVersionEeffFindByPeriodo(periodo.getIdPeriodo());
        }catch(Exception e){
            logger.error(e);
            addErrorMessage("Error al buscar las versiones");
        }
    }
    
    public void cargarEeff(ActionEvent event){
        try{
            versionEeff = (VersionEeff)event.getComponent().getAttributes().get("versionEeff");
            eeffList = getFacadeService().getEstadoFinancieroService().getEeffByVersion(versionEeff.getIdVersionEeff());
            renderEeffList = true;
            renderEeffDetList = false;
        }catch(Exception e){
            logger.error("Error al buscar version" , e);
            addErrorMessage("Error al cargar Estados Financieros por Versión");
        }
    }
    
    public void buscar(ActionEvent event){
    	
    	if (versionEeff == null){
    		
    		super.addErrorMessage("Seleccione una versión antes de continuar");
    		
    	}
    	
    	this.buscarFiltroFecu(event);
    	
    }
    
    private void buscarFiltroFecu(ActionEvent event){
        try{
            Long idFecu = null;
            try{
                idFecu = Long.valueOf(filtroTablaEeff.replaceAll("[.]", ""));
            }catch(Exception e){
                addErrorMessage("Debe ingresar un Código de Fecu Válido");
                return;
            }
            
            eeffList = getFacadeService().getEstadoFinancieroService().getEeffByLikeFecu(versionEeff.getIdVersionEeff(), idFecu);
            renderEeffList = true;
            renderEeffDetList = false;
        }catch(Exception e){
        	e.printStackTrace();
            logger.error(e);
            addErrorMessage("Error al buscar los estados financieros por el filtro");
        }
    }
    
    private void buscarFiltroCuenta(ActionEvent event){
        try{
            Long cuenta = null;
            try{
                cuenta = Long.valueOf(filtroTablaEeff);
            }catch(Exception e){
                addErrorMessage("Debe ingresar un código de cuenta válido");
                return;
            }
            
            eeffDetList = getFacadeService().getEstadoFinancieroService().getEeffByLikeCuenta(versionEeff.getIdVersionEeff(), cuenta);
            
            renderEeffList = false;
            renderEeffDetList = true;
            
        }catch(Exception e){
            logger.error(e);
            addErrorMessage("Error al buscar los estados financieros por el filtro");
        }
    }


    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setVersionEeff(VersionEeff versionEeff) {
        this.versionEeff = versionEeff;
    }

    public VersionEeff getVersionEeff() {
        return versionEeff;
    }

    public void setVersionEeffList(List<VersionEeff> versionEeffList) {
        this.versionEeffList = versionEeffList;
    }

    public List<VersionEeff> getVersionEeffList() {
        return versionEeffList;
    }

    public void setEeffList(List<EstadoFinanciero> eeffList) {
        this.eeffList = eeffList;
    }

    public List<EstadoFinanciero> getEeffList() {
        return eeffList;
    }

    public void setFiltroTablaEeff(String filtroTablaEeff) {
        this.filtroTablaEeff = filtroTablaEeff;
    }

    public String getFiltroTablaEeff() {
        return filtroTablaEeff;
    }

    public boolean isRenderEeffDetList() {
        return renderEeffDetList;
    }
    
    private void renderFalse(){
        renderEeffList = false;
        renderEeffDetList = false;
    }

    public void setEeffDetList(List<DetalleEeff> eeffDetList) {
        this.eeffDetList = eeffDetList;
    }

    public List<DetalleEeff> getEeffDetList() {
        return eeffDetList;
    }

    public boolean isRenderEeffList() {
        return renderEeffList;
    }

    public void setMesPeriodo(String mesPeriodo) {
        this.mesPeriodo = mesPeriodo;
    }

    public String getMesPeriodo() {
        return mesPeriodo;
    }

    public void setAnioPeriodo(String anioPeriodo) {
        this.anioPeriodo = anioPeriodo;
    }

    public String getAnioPeriodo() {
        return anioPeriodo;
    }

}
