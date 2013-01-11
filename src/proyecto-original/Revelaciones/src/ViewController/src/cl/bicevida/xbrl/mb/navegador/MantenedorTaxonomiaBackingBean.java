package cl.bicevida.xbrl.mb.navegador;

import cl.bicevida.revelaciones.common.mb.SoporteBackingBean;

import cl.bicevida.revelaciones.ejb.common.VigenciaEnum;
import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.xbrl.ejb.entity.XbrlTaxonomia;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import javax.faces.event.ValueChangeEvent;

import oracle.adf.view.rich.component.rich.input.RichInputDate;

import org.apache.log4j.Logger;

/**
 * Clase BackingBean que controla las funcionalidades del mantenedor de Taxonomías XBRL
 * @author rodrigo.reyes@bicevida.cl
 * @link http://cl.linkedin.com/in/rreyesc
 */
public class MantenedorTaxonomiaBackingBean extends SoporteBackingBean implements Serializable {
    private final transient Logger logger = Logger.getLogger(this.getClass().getName());
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = -8983975627912345590L;
            
    private List<XbrlTaxonomia> xbrlTaxonomias;
    
    private Date fechaDesde;
    private Date fechaHasta;
    private Long vigente;
    
    private transient RichInputDate inputFechaDesde;
    private transient RichInputDate inputFechaHasta;
        
    public MantenedorTaxonomiaBackingBean() {
        super();
    }

    /**    
     */
    public String buscarTaxonimiaAction() {
        try {            
            /*
            if(this.fechaHasta != null || this.fechaDesde != null){
                if(this.fechaHasta != null && this.fechaDesde == null){
                    FacesMessage message = new FacesMessage();
                    message.setSummary("El rango de fecha ingresado no es valido.");
                    message.setDetail("La fecha desde no puede estar vacio.");
                    message.setSeverity(FacesMessage.SEVERITY_ERROR);
                    this.getInputFechaDesde().setValid(Boolean.FALSE);
                    this.setFechaDesde(null);
                    super.getFacesContext().addMessage(this.getInputFechaDesde().getClientId( super.getFacesContext() ), message);   
                    return null;
                }
                
                if(this.fechaDesde != null && this.fechaHasta == null){
                    FacesMessage message = new FacesMessage();
                    message.setSummary("El rango de fecha ingresado no es valido.");
                    message.setDetail("La fecha hasta no puede estar vacio.");
                    message.setSeverity(FacesMessage.SEVERITY_ERROR);
                    this.getInputFechaHasta().setValid(Boolean.FALSE);
                    this.setFechaHasta(null);
                    super.getFacesContext().addMessage(this.getInputFechaHasta().getClientId( super.getFacesContext() ), message);   
                    return null;
                }
                
                if(this.getFechaHasta().before(this.getFechaDesde())){
                    FacesMessage message = new FacesMessage();
                    message.setSummary("El rango de fecha ingresado no es valido.");
                    message.setDetail("La fecha hasta no puede ser menor a "+Util.getString(this.getFechaDesde()));
                    message.setSeverity(FacesMessage.SEVERITY_ERROR);  
                    this.getInputFechaHasta().setValid(Boolean.FALSE);
                    this.setFechaHasta(null);
                    super.getFacesContext().addMessage(this.getInputFechaHasta().getClientId( super.getFacesContext() ), message);                                  
                    return null;
                }
            } */                       
            this.setXbrlTaxonomias(super.getFacade().getTaxonomyLoaderService().findTaxonomiasByFiltro( this.getFechaDesde(), this.getFechaHasta(), this.getVigente() ));
        } catch (Exception e) {
            super.agregarErrorMessage("Se ha producido un error al Cargar las Taxonomías Disponibles.");
            logger.error(e);
        } 
        return null;
    }
           
    /**
     * @param event
     */
    public void agregarFilaAction(ActionEvent event){
        List<XbrlTaxonomia> xbrlTaxonomiasTemp = new ArrayList<XbrlTaxonomia>();
        if(xbrlTaxonomias == null){
            xbrlTaxonomias = new ArrayList<XbrlTaxonomia>();
        }
        xbrlTaxonomiasTemp.add(new XbrlTaxonomia(VigenciaEnum.VIGENTE.getKey(), new Date(), new Date(), this.getNombreUsuario()));
        xbrlTaxonomiasTemp.addAll(xbrlTaxonomias);
        xbrlTaxonomias = xbrlTaxonomiasTemp;        
    }

    /**
     * @return
     */
    public String guardarAction(){
        try {
            if(xbrlTaxonomias == null){
                super.agregarWarnMessage("No existen registros de Taxonomías para guardar");
                return null;
            }
            super.getFacade().getTaxonomyLoaderService().mergeTaxonomias(this.getXbrlTaxonomias(), super.getNombreUsuario());            
            super.agregarSuccesMessage("Se han guardado los datos de la Taxonomía con éxito");
            this.setXbrlTaxonomias(super.getFacade().getTaxonomyLoaderService().findTaxonomiasByFiltro( this.getFechaDesde(), this.getFechaHasta(), this.getVigente() ));
        } catch (Exception e) {
            logger.error(e);
            super.agregarErrorMessage("Se ha producido un error al guardar la Taxonomía");
        }
        return null;
    }
    
    public List<XbrlTaxonomia> getXbrlTaxonomias() {              
        return xbrlTaxonomias;
    }
    
    public void setXbrlTaxonomias(List<XbrlTaxonomia> xbrlTaxonomias) {
        this.xbrlTaxonomias = xbrlTaxonomias;
    }
    
    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setVigente(Long vigente) {
        this.vigente = vigente;
    }

    public Long getVigente() {
        return vigente;
    }

    public void setInputFechaDesde(RichInputDate inputFechaDesde) {
        this.inputFechaDesde = inputFechaDesde;
    }

    public RichInputDate getInputFechaDesde() {
        return inputFechaDesde;
    }

    public void setInputFechaHasta(RichInputDate inputFechaHasta) {
        this.inputFechaHasta = inputFechaHasta;
    }

    public RichInputDate getInputFechaHasta() {
        return inputFechaHasta;
    }
}
