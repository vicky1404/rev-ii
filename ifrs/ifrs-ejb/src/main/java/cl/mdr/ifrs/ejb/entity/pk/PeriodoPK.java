package cl.mdr.ifrs.ejb.entity.pk;

import java.io.Serializable;

import java.lang.Long;

public class PeriodoPK implements Serializable {
    private Long idPeriodo;
    private Long idVersion;

    public PeriodoPK() {
    }

    public PeriodoPK(Long idPeriodo, Long idVersion) {
        this.idPeriodo = idPeriodo;
        this.idVersion = idVersion;
    }

    public boolean equals(Object other) {
        if (other instanceof PeriodoPK) {
            final PeriodoPK otherPeriodoNotaPK = (PeriodoPK)other;
            final boolean areEqual =
                (otherPeriodoNotaPK.idPeriodo.equals(idPeriodo) && otherPeriodoNotaPK.idVersion.equals(idVersion));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public Long getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(Long idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public Long getIdVersion() {
        return idVersion;
    }

    public void setIdVersion(Long idVersion) {
        this.idVersion = idVersion;
    }
}
