package cl.bicevida.xbrl.navegador.filtro;

import java.io.Serializable;

import java.util.Date;

/**
 * Clase filtro para busqueda de Mantenedor de Taxonimias
 */
public class FiltroTaxonomia implements Serializable {
    private static final long serialVersionUID = -4202309419422847611L;
    
    private Date fechaDesde;
    private Date fechaHasta;
    private Long vigente;
    
    public FiltroTaxonomia() {
        super();
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
}
