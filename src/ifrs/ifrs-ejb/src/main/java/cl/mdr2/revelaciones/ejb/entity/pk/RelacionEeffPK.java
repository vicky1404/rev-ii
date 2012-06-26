package cl.bicevida.revelaciones.ejb.entity.pk;

import java.io.Serializable;

import java.lang.Long;


public class RelacionEeffPK implements Serializable {
    
    private Long idFecu;
    private Long idPeriodo;

    public RelacionEeffPK() {
    }

    public RelacionEeffPK(Long idFecu, Long idPeriodo) {
        this.idFecu = idFecu;
        this.idPeriodo = idPeriodo;
    }

    public boolean equals(Object other) {
        if (other instanceof RelacionEeffPK) {
            final RelacionEeffPK otherRelacionEeff1PK = (RelacionEeffPK)other;
            final boolean areEqual =
                (otherRelacionEeff1PK.idFecu.equals(idFecu) && otherRelacionEeff1PK.idPeriodo.equals(idPeriodo));
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

    public Long getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(Long idPeriodo) {
        this.idPeriodo = idPeriodo;
    }
}
