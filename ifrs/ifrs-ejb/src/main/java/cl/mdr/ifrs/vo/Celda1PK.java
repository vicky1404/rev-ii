package cl.mdr.ifrs.vo;

import java.io.Serializable;

import java.math.BigDecimal;

public class Celda1PK implements Serializable {
    private BigDecimal idColumna;
    private BigDecimal idFila;
    private BigDecimal idGrilla;

    public Celda1PK() {
    }

    public Celda1PK(BigDecimal idColumna, BigDecimal idFila, BigDecimal idGrilla) {
        this.idColumna = idColumna;
        this.idFila = idFila;
        this.idGrilla = idGrilla;
    }

    public boolean equals(Object other) {
        if (other instanceof Celda1PK) {
            final Celda1PK otherCelda1PK = (Celda1PK)other;
            final boolean areEqual =
                (otherCelda1PK.idColumna.equals(idColumna) && otherCelda1PK.idFila.equals(idFila) &&
                 otherCelda1PK.idGrilla.equals(idGrilla));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public BigDecimal getIdColumna() {
        return idColumna;
    }

    public void setIdColumna(BigDecimal idColumna) {
        this.idColumna = idColumna;
    }

    public BigDecimal getIdFila() {
        return idFila;
    }

    public void setIdFila(BigDecimal idFila) {
        this.idFila = idFila;
    }

    public BigDecimal getIdGrilla() {
        return idGrilla;
    }

    public void setIdGrilla(BigDecimal idGrilla) {
        this.idGrilla = idGrilla;
    }
}
