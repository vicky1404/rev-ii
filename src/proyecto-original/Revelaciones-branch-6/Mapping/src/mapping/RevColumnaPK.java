package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

public class RevColumnaPK implements Serializable {
    private BigDecimal idColumna;
    private BigDecimal idGrilla;

    public RevColumnaPK() {
    }

    public RevColumnaPK(BigDecimal idColumna, BigDecimal idGrilla) {
        this.idColumna = idColumna;
        this.idGrilla = idGrilla;
    }

    public boolean equals(Object other) {
        if (other instanceof RevColumnaPK) {
            final RevColumnaPK otherRevColumnaPK = (RevColumnaPK)other;
            final boolean areEqual =
                (otherRevColumnaPK.idColumna.equals(idColumna) && otherRevColumnaPK.idGrilla.equals(idGrilla));
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
}
