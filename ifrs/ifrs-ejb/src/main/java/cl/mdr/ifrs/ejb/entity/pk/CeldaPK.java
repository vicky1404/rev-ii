package cl.mdr.ifrs.ejb.entity.pk;

import java.io.Serializable;

import java.lang.Long;

public class CeldaPK implements Serializable {
    private Long idColumna;
    private Long idFila;
    private Long idGrilla;

    public CeldaPK() {
    }

    public CeldaPK(Long idColumna, Long idFila, Long idGrilla) {
        this.idColumna = idColumna;
        this.idFila = idFila;
        this.idGrilla = idGrilla;
    }

    public boolean equals(Object other) {
        if (other instanceof CeldaPK) {
            final CeldaPK otherCeldaNotaPK = (CeldaPK)other;
            final boolean areEqual =
                (otherCeldaNotaPK.idColumna.equals(idColumna) && otherCeldaNotaPK.idFila.equals(idFila) &&
                 otherCeldaNotaPK.idGrilla.equals(idGrilla));
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

    public Long getIdFila() {
        return idFila;
    }

    public void setIdFila(Long idFila) {
        this.idFila = idFila;
    }

    public Long getIdGrilla() {
        return idGrilla;
    }

    public void setIdGrilla(Long idGrilla) {
        this.idGrilla = idGrilla;
    }
}
