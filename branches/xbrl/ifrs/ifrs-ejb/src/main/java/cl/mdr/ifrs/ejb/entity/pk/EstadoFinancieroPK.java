package cl.mdr.ifrs.ejb.entity.pk;

import java.io.Serializable;

import java.lang.Long;


public class EstadoFinancieroPK implements Serializable {
    
    private Long idFecu;
    private Long idVersionEeff;

    public EstadoFinancieroPK() {
    }

    public EstadoFinancieroPK(Long idFecu, Long idVersionEeff) {
        this.idFecu = idFecu;
        this.idVersionEeff = idVersionEeff;
    }

    public boolean equals(Object other) {
        if (other instanceof EstadoFinancieroPK) {
            final EstadoFinancieroPK otherEstadoFinancieroPK = (EstadoFinancieroPK)other;
            final boolean areEqual =
                (otherEstadoFinancieroPK.idFecu.equals(idFecu) && otherEstadoFinancieroPK.idVersionEeff.equals(idVersionEeff));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public Long getIdFecu() {
        return idFecu;
    }

    public void setIdFecu(Long idFecu) {
        this.idFecu = idFecu;
    }

    public Long getIdVersionEeff() {
        return idVersionEeff;
    }

    public void setIdVersionEeff(Long idVersionEeff) {
        this.idVersionEeff = idVersionEeff;
    }
}
