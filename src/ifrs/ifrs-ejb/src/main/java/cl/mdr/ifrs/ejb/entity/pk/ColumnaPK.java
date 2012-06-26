package cl.mdr.ifrs.ejb.entity.pk;

import java.io.Serializable;

import java.lang.Long;

public class ColumnaPK implements Serializable {
    private Long idColumna;
    private Long idGrilla;

    public ColumnaPK() {
    }

    public ColumnaPK(Long idColumna, Long idGrilla) {
        this.idColumna = idColumna;
        this.idGrilla = idGrilla;
    }

    public boolean equals(Object other) {
        if (other instanceof ColumnaPK) {
            final ColumnaPK otherColumnaNotaPK = (ColumnaPK)other;
            final boolean areEqual =
                (otherColumnaNotaPK.idColumna.equals(idColumna) && otherColumnaNotaPK.idGrilla.equals(idGrilla));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public Long getIdColumna() {
        return idColumna;
    }

    public void setIdColumna(Long idColumna) {
        this.idColumna = idColumna;
    }

    public Long getIdGrilla() {
        return idGrilla;
    }

    public void setIdGrilla(Long idGrilla) {
        this.idGrilla = idGrilla;
    }
}
