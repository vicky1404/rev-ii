package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

public class RevCeldaPK implements Serializable {
    private BigDecimal idColumna;
    private BigDecimal idFila;
    private BigDecimal idGrilla;

    public RevCeldaPK() {
    }

    public RevCeldaPK(BigDecimal idColumna, BigDecimal idFila, BigDecimal idGrilla) {
        this.idColumna = idColumna;
        this.idFila = idFila;
        this.idGrilla = idGrilla;
    }

    public boolean equals(Object other) {
        if (other instanceof RevCeldaPK) {
            final RevCeldaPK otherRevCeldaPK = (RevCeldaPK)other;
            final boolean areEqual =
                (otherRevCeldaPK.idColumna.equals(idColumna) && otherRevCeldaPK.idFila.equals(idFila) &&
                 otherRevCeldaPK.idGrilla.equals(idGrilla));
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
