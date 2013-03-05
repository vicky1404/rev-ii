package cl.bicevida.revelaciones.common.mb;


import static ch.lambdaj.Lambda.index;
import static ch.lambdaj.Lambda.on;

import cl.bicevida.revelaciones.common.filtros.Filtro;
import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.revelaciones.ejb.entity.CaratulaFecu;
import cl.bicevida.revelaciones.ejb.entity.DatoCaratulaFecu;
import cl.bicevida.revelaciones.ejb.entity.Periodo;

import java.io.Serializable;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import javax.ejb.EJBException;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;

import javax.faces.validator.ValidatorException;

import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;

import oracle.adf.view.rich.context.AdfFacesContext;

import org.apache.log4j.Logger;


public class CaratulaFecuBackingBean extends SoporteBackingBean implements Serializable {    
    private transient Logger logger = Logger.getLogger(CaratulaFecuBackingBean.class);
    @SuppressWarnings("compatibility:-7446856244184267319")
    private static final long serialVersionUID = 7388265230878845608L;
    
    private CaratulaFecu caratulaFecu;
    private List<DatoCaratulaFecu> datoCaratulaFecuList;
    private Map<String, DatoCaratulaFecu> datoCaratulaFecuMap; 
    private transient RichPanelGroupLayout panelInformacionCaratula;
    private boolean renderPanelCaratula;
    
    public CaratulaFecuBackingBean() {
        super();
    }
    
    @PostConstruct
    void init(){
        this.setPeriodoActual();        
        this.cargarCaratula(super.getFiltro().getPeriodo());
    }
    
    private void setPeriodoActual(){
        super.getFiltro().setPeriodo(new Periodo(super.getComponenteBackingBean().getPeriodoActual()));
    }
    
    public void cargarCaratula(Periodo periodo){
        try {
            logger.info("Cargando los datos de la caratula para el periodo: "+periodo.getPeriodoFormat());
            this.setCaratulaFecu(super.getFacade().getCaratulaFecuService().findByPeriodo(periodo));            
            datoCaratulaFecuMap = new LinkedHashMap<String, DatoCaratulaFecu>();
            datoCaratulaFecuMap = index(this.getCaratulaFecu().getDatoCaratulaFecuList(), on(DatoCaratulaFecu.class).getNombreDatoCaratulaFecu().getIdNombreDato());            
            this.setRenderPanelCaratula(Boolean.TRUE);
            
        } catch (EJBException e) {            
            super.agregarWarnMessage("No se han encontrado datos de la portada para este período");
            this.setRenderPanelCaratula(Boolean.FALSE);
            logger.error(e);
        } catch (Exception e) {
            super.agregarErrorMessage("Se ha producido un error al cargar los datos de la portada");
            logger.error(e);
        }
    }
    
    public void guardarAction(ActionEvent actionEvent){
        try {
            super.getFacade().getCaratulaFecuService().mergeDatosCaratula(this.getCaratulaFecu().getDatoCaratulaFecuList());
            super.agregarSuccesMessage("Se han guardado los datos de la portada con éxito");
        } catch (Exception e) {
            super.agregarErrorMessage("Se ha producido un error al guardar los datos de la portada");
        }
    }
    
    public String buscarAction(){        
        try {            
            this.limpiarAction();
            this.cargarCaratula(super.getFiltroPeriodo());             
        } catch (EJBException e) {
            super.agregarWarnMessage("No se han encontrado datos de la portada para este período");
            logger.error(e);
        } catch (Exception e) {
            super.agregarErrorMessage("Se ha producido un error al cargar los datos de la portada");
            logger.error(e);
        }   
        return null;
    }
    
    public String limpiarAction(){
        Filtro filtroPaso = super.getFiltro();
        filtroPaso.getPeriodo().setPeriodo(null);
        return null;
    }
    
    public void setDatoCaratulaFecuList(List<DatoCaratulaFecu> datoCaratulaFecuList) {
        this.datoCaratulaFecuList = datoCaratulaFecuList;
    }

    public List<DatoCaratulaFecu> getDatoCaratulaFecuList() {
        return datoCaratulaFecuList;
    }

    public void setCaratulaFecu(CaratulaFecu caratulaFecu) {
        this.caratulaFecu = caratulaFecu;
    }

    public CaratulaFecu getCaratulaFecu() {
        return caratulaFecu;
    }
 
    public void setDatoCaratulaFecuMap(Map<String, DatoCaratulaFecu> datoCaratulaFecuMap) {
        this.datoCaratulaFecuMap = datoCaratulaFecuMap;
    }

    public Map<String, DatoCaratulaFecu> getDatoCaratulaFecuMap() {
        return datoCaratulaFecuMap;
    }

    public void setPanelInformacionCaratula(RichPanelGroupLayout panelInformacionCaratula) {
        this.panelInformacionCaratula = panelInformacionCaratula;
    }

    public RichPanelGroupLayout getPanelInformacionCaratula() {
        return panelInformacionCaratula;
    }

    public void setRenderPanelCaratula(boolean renderPanelCaratula) {
        this.renderPanelCaratula = renderPanelCaratula;
    }

    public boolean isRenderPanelCaratula() {
        return renderPanelCaratula;
    }

    public void validarRangoFecha(javax.faces.context.FacesContext facesContext, UIComponent uIComponent, Object object) { 
        String strFechaDesde = this.getDatoCaratulaFecuMap().get("FECHA_INICIO").getValor();
        String strFechaHasta = (String)object;    
        
        Date fechaDesde = Util.getDate(strFechaDesde);
        Date fechaHasta = Util.getDate(strFechaHasta);
        
        if(fechaDesde.after(fechaHasta)){
            FacesMessage message = new FacesMessage();
            message.setDetail("La fecha de Término no puede ser inferior a la fecha de Inicio");
            message.setSummary("Rango de fecha inválido");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);        
            throw new ValidatorException(message);
        }
        
        
    }
}
