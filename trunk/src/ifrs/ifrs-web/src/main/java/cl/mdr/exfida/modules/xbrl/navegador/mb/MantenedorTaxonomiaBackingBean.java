package cl.mdr.exfida.modules.xbrl.navegador.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;
import org.primefaces.component.calendar.Calendar;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.ejb.common.VigenciaEnum;
import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.XbrlTaxonomia;

public class MantenedorTaxonomiaBackingBean extends AbstractBackingBean implements Serializable {
    private final transient Logger logger = Logger.getLogger(this.getClass().getName());    
    private static final long serialVersionUID = -8983975627912345590L;
            
    private List<XbrlTaxonomia> xbrlTaxonomias;
    
    private Date fechaDesde;
    private Date fechaHasta;
    private Long vigente;
    
    private transient Calendar inputFechaDesde;
    private transient Calendar inputFechaHasta;
        
        
    public MantenedorTaxonomiaBackingBean() {
        super();
    }

    /**    
     */
    public String buscarTaxonimiaAction() {
        try {
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
            }                        
            this.setXbrlTaxonomias(super.getFacadeService().getTaxonomyLoaderService().findTaxonomiasByFiltro( this.getFechaDesde(), this.getFechaHasta(), this.getVigente() ));
        } catch (Exception e) {
            super.addErrorMessage("Se ha producido un error al Cargar las Taxonom�as Disponibles.");
            logger.error(e);
        } 
        return null;
    }
           
    /**
     * @param event
     */
    public void agregarFilaAction(ActionEvent event){
        List<XbrlTaxonomia> xbrlTaxonomiasTemp = new ArrayList<XbrlTaxonomia>();
        xbrlTaxonomiasTemp.add(new XbrlTaxonomia(VigenciaEnum.VIGENTE.getKey(), new Date(), new Date(), this.getNombreUsuario()));
        xbrlTaxonomiasTemp.addAll(xbrlTaxonomias);
        xbrlTaxonomias = xbrlTaxonomiasTemp;        
    }

    /**
     * @return
     */
    public String guardarAction(){
        try {            
            super.getFacadeService().getTaxonomyLoaderService().mergeTaxonomias(this.getXbrlTaxonomias(), super.getNombreUsuario());            
            super.addInfoMessage("Se han guardado los datos de la Taxonom�a con �xito");
            this.setXbrlTaxonomias(null);
        } catch (Exception e) {
            logger.error(e);
            super.addErrorMessage("Se ha producido un error al guardar la Taxonom�a");
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

    public void setInputFechaDesde(Calendar inputFechaDesde) {
        this.inputFechaDesde = inputFechaDesde;
    }

    public Calendar getInputFechaDesde() {
        return inputFechaDesde;
    }

    public void setInputFechaHasta(Calendar inputFechaHasta) {
        this.inputFechaHasta = inputFechaHasta;
    }

    public Calendar getInputFechaHasta() {
        return inputFechaHasta;
    }
}
