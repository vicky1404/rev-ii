package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

public class RevSubGrillaPK implements Serializable {
    private BigDecimal idGrilla;
    private BigDecimal idSubGrilla;

    public RevSubGrillaPK() {
    }

    public RevSubGrillaPK(BigDecimal idGrilla, BigDecimal idSubGrilla) {
        this.idGrilla = idGrilla;
        this.idSubGrilla = idSubGrilla;
    }

    public boolean equals(Object other) {
        if (other instanceof RevSubGrillaPK) {
            final RevSubGrillaPK otherRevSubGrillaPK = (RevSubGrillaPK)other;
            final boolean areEqual =
                (otherRevSubGrillaPK.idGrilla.equals(idGrilla) && otherRevSubGrillaPK.idSubGrilla.equals(idSubGrilla));
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

    public BigDecimal getIdSubGrilla() {
        return idSubGrilla;
    }

    public void setIdSubGrilla(BigDecimal idSubGrilla) {
        this.idSubGrilla = idSubGrilla;
    }
}
