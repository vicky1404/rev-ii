package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

public class RevSubCeldaPK implements Serializable {
    private BigDecimal idGrilla;
    private BigDecimal idSubColumna;
    private BigDecimal idSubFila;
    private BigDecimal idSubGrilla;

    public RevSubCeldaPK() {
    }

    public RevSubCeldaPK(BigDecimal idGrilla, BigDecimal idSubColumna, BigDecimal idSubFila, BigDecimal idSubGrilla) {
        this.idGrilla = idGrilla;
        this.idSubColumna = idSubColumna;
        this.idSubFila = idSubFila;
        this.idSubGrilla = idSubGrilla;
    }

    public boolean equals(Object other) {
        if (other instanceof RevSubCeldaPK) {
            final RevSubCeldaPK otherRevSubCeldaPK = (RevSubCeldaPK)other;
            final boolean areEqual =
                (otherRevSubCeldaPK.idGrilla.equals(idGrilla) && otherRevSubCeldaPK.idSubColumna.equals(idSubColumna) &&
                 otherRevSubCeldaPK.idSubFila.equals(idSubFila) && otherRevSubCeldaPK.idSubGrilla.equals(idSubGrilla));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public BigDecimal getIdGrilla() {
        return idGrilla;
    }

    public void setIdGrilla(BigDecimal idGrilla) {
        this.idGrilla = idGrilla;
    }

    public BigDecimal getIdSubColumna() {
        return idSubColumna;
    }

    public void setIdSubColumna(BigDecimal idSubColumna) {
        this.idSubColumna = idSubColumna;
    }

    public BigDecimal getIdSubFila() {
        return idSubFila;
    }

    public void setIdSubFila(BigDecimal idSubFila) {
        this.idSubFila = idSubFila;
    }

    public BigDecimal getIdSubGrilla() {
        return idSubGrilla;
    }

    public void setIdSubGrilla(BigDecimal idSubGrilla) {
        this.idSubGrilla = idSubGrilla;
    }
}
