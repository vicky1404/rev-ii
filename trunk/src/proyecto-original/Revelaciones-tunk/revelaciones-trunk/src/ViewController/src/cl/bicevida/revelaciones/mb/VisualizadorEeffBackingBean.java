package cl.bicevida.revelaciones.mb;


import cl.bicevida.revelaciones.common.mb.SoporteBackingBean;
import cl.bicevida.revelaciones.common.util.PropertyManager;
import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.revelaciones.ejb.entity.DetalleEeff;
import cl.bicevida.revelaciones.ejb.entity.EstadoFinanciero;
import cl.bicevida.revelaciones.ejb.entity.Periodo;
import cl.bicevida.revelaciones.ejb.entity.VersionEeff;

import java.text.MessageFormat;

import java.util.List;

import javax.annotation.PostConstruct;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;


public class VisualizadorEeffBackingBean extends SoporteBackingBean{
    
    private transient Logger logger = Logger.getLogger(CargadorEeffBackingBean.class);    
    
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
    private Long busqueda;
    
    public VisualizadorEeffBackingBean() {
        
    }
    
    @PostConstruct
    public void init(){
        try{
            
            periodo = getFacade().getPeriodoService().findMaxPeriodoObj();
            mesPeriodo = periodo.getMesPeriodo();
            anioPeriodo = periodo.getAnioPeriodo();
            versionEeffList = getFacade().getEstadoFinancieroService().getVersionEeffFindByPeriodo(periodo.getIdPeriodo());
            getFiltro().setPeriodo(periodo);

        }catch(Exception e){
            logger.error(e);
            agregarErrorMessage(PropertyManager.getInstance().getMessage("visualizador_error_busc_periodo"));
        }
    }
    
    public void buscarVersionEeff(ActionEvent event){
        versionEeffList = null;
        eeffList = null;
        renderFalse();
        try{
            Long anioMes = Util.getLong(anioPeriodo.concat(mesPeriodo), 0L);
            periodo = getFacade().getMantenedoresTipoService().findByPeriodo(anioMes);
            getFiltro().setPeriodo(periodo);
        }catch(Exception e){
            agregarWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("periodo_busqueda_sin_resultado_periodo"), mesPeriodo, anioPeriodo));
            logger.error(e.getMessage(), e);
            return;
        }
        try{            
            versionEeffList = getFacade().getEstadoFinancieroService().getVersionEeffFindByPeriodo(periodo.getIdPeriodo());
        }catch(Exception e){
            logger.error(e);
            agregarErrorMessage(PropertyManager.getInstance().getMessage("visualizador_buscar_versiones"));
        }
    }
    
    public void cargarEeff(ActionEvent event){
        try{
            versionEeff = (VersionEeff)event.getComponent().getAttributes().get("versionEeff");
            eeffList = getFacade().getEstadoFinancieroService().getEeffByVersion(versionEeff.getIdVersionEeff());
            renderEeffList = true;
            renderEeffDetList = false;
        }catch(Exception e){
            logger.error(e.getMessage() , e);
            agregarErrorMessage(PropertyManager.getInstance().getMessage("visualizador_carga_eeff"));
        }
    }
    
    
    
    public void buscarPorFiltro(ActionEvent event){
        if(versionEeff==null)
            agregarWarnMessage(PropertyManager.getInstance().getMessage("visualizador_seleccionar_version"));
        else if(busqueda==null){
            agregarWarnMessage(PropertyManager.getInstance().getMessage("visualizador_seleccionar_filtro"));
        }else if(busqueda.equals(1L))
            buscarPorFecu();
        else if(busqueda.equals(2L))
            buscarPorCuenta();
    }
    
    private void buscarPorFecu(){
        try{
            Long idFecu = null;
            try{
                idFecu = Long.valueOf(filtroTablaEeff.replaceAll("[.]", ""));
            }catch(Exception e){
                agregarErrorMessage(PropertyManager.getInstance().getMessage("visualizador_fecu_invalido"));
                return;
            }
            eeffList = getFacade().getEstadoFinancieroService().getEeffByLikeFecu(versionEeff.getIdVersionEeff(), idFecu);
            renderEeffList = true;
            renderEeffDetList = false;
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            agregarErrorMessage(PropertyManager.getInstance().getMessage("visualizador_error_filtro_fecu"));
        }
    }
    
    private void buscarPorCuenta(){
        try{
            Long cuenta = null;
            try{
                cuenta = Long.valueOf(filtroTablaEeff);
            }catch(Exception e){
                agregarErrorMessage(PropertyManager.getInstance().getMessage("visualizador_cuenta_invalido"));
                return;
            }
            
            eeffDetList = getFacade().getEstadoFinancieroService().getEeffByLikeCuenta(versionEeff.getIdVersionEeff(), cuenta);
            
            renderEeffList = false;
            renderEeffDetList = true;
            
        }catch(Exception e){
            logger.error(e);
            agregarErrorMessage(PropertyManager.getInstance().getMessage("visualizador_error_cuenta_filtro"));
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

    public void setBusqueda(Long busqueda) {
        this.busqueda = busqueda;
    }

    public Long getBusqueda() {
        return busqueda;
    }
}
