package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

public class RevSubAgrupacionColumnaPK implements Serializable {
    private BigDecimal idGrilla;
    private BigDecimal idNivel;
    private BigDecimal idSubColumna;
    private BigDecimal idSubGrilla;

    public RevSubAgrupacionColumnaPK() {
    }

    public RevSubAgrupacionColumnaPK(BigDecimal idGrilla, BigDecimal idNivel, BigDecimal idSubColumna,
                                     BigDecimal idSubGrilla) {
        this.idGrilla = idGrilla;
        this.idNivel = idNivel;
        this.idSubColumna = idSubColumna;
        this.idSubGrilla = idSubGrilla;
    }

    public boolean equals(Object other) {
        if (other instanceof RevSubAgrupacionColumnaPK) {
            final RevSubAgrupacionColumnaPK otherRevSubAgrupacionColumnaPK = (RevSubAgrupacionColumnaPK)other;
            final boolean areEqual =
                (otherRevSubAgrupacionColumnaPK.idGrilla.equals(idGrilla) && otherRevSubAgrupacionColumnaPK.idNivel.equals(idNivel) &&
                 otherRevSubAgrupacionColumnaPK.idSubColumna.equals(idSubColumna) &&
                 otherRevSubAgrupacionColumnaPK.idSubGrilla.equals(idSubGrilla));
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

    public BigDecimal getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(BigDecimal idNivel) {
        this.idNivel = idNivel;
    }

    public BigDecimal getIdSubColumna() {
        return idSubColumna;
    }

    public void setIdSubColumna(BigDecimal idSubColumna) {
        this.idSubColumna = idSubColumna;
    }

    public BigDecimal getIdSubGrilla() {
        return idSubGrilla;
    }

    public void setIdSubGrilla(BigDecimal idSubGrilla) {
        this.idSubGrilla = idSubGrilla;
    }
}
