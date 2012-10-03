package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

public class RevAgrupacionColumnaPK implements Serializable {
    private BigDecimal idColumna;
    private BigDecimal idGrilla;
    private BigDecimal idNivel;

    public RevAgrupacionColumnaPK() {
    }

    public RevAgrupacionColumnaPK(BigDecimal idColumna, BigDecimal idGrilla, BigDecimal idNivel) {
        this.idColumna = idColumna;
        this.idGrilla = idGrilla;
        this.idNivel = idNivel;
    }

    public boolean equals(Object other) {
        if (other instanceof RevAgrupacionColumnaPK) {
            final RevAgrupacionColumnaPK otherRevAgrupacionColumnaPK = (RevAgrupacionColumnaPK)other;
            final boolean areEqual =
                (otherRevAgrupacionColumnaPK.idColumna.equals(idColumna) && otherRevAgrupacionColumnaPK.idGrilla.equals(idGrilla) &&
                 otherRevAgrupacionColumnaPK.idNivel.equals(idNivel));
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
}
