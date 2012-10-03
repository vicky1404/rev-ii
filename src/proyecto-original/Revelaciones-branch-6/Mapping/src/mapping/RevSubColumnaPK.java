package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

public class RevSubColumnaPK implements Serializable {
    private BigDecimal idGrilla;
    private BigDecimal idSubColumna;
    private BigDecimal idSubGrilla;

    public RevSubColumnaPK() {
    }

    public RevSubColumnaPK(BigDecimal idGrilla, BigDecimal idSubColumna, BigDecimal idSubGrilla) {
        this.idGrilla = idGrilla;
        this.idSubColumna = idSubColumna;
        this.idSubGrilla = idSubGrilla;
    }

    public boolean equals(Object other) {
        if (other instanceof RevSubColumnaPK) {
            final RevSubColumnaPK otherRevSubColumnaPK = (RevSubColumnaPK)other;
            final boolean areEqual =
                (otherRevSubColumnaPK.idGrilla.equals(idGrilla) && otherRevSubColumnaPK.idSubColumna.equals(idSubColumna) &&
                 otherRevSubColumnaPK.idSubGrilla.equals(idSubGrilla));
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

    public BigDecimal getIdSubGrilla() {
        return idSubGrilla;
    }

    public void setIdSubGrilla(BigDecimal idSubGrilla) {
        this.idSubGrilla = idSubGrilla;
    }
}
