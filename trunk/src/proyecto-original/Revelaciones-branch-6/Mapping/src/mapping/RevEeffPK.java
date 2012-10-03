package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

public class RevEeffPK implements Serializable {
    private BigDecimal idFecu;
    private BigDecimal idVersionEeff;

    public RevEeffPK() {
    }

    public RevEeffPK(BigDecimal idFecu, BigDecimal idVersionEeff) {
        this.idFecu = idFecu;
        this.idVersionEeff = idVersionEeff;
    }

    public boolean equals(Object other) {
        if (other instanceof RevEeffPK) {
            final RevEeffPK otherRevEeffPK = (RevEeffPK)other;
            final boolean areEqual =
                (otherRevEeffPK.idFecu.equals(idFecu) && otherRevEeffPK.idVersionEeff.equals(idVersionEeff));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public BigDecimal getIdFecu() {
        return idFecu;
    }

    public void setIdFecu(BigDecimal idFecu) {
        this.idFecu = idFecu;
    }

    public BigDecimal getIdVersionEeff() {
        return idVersionEeff;
    }

    public void setIdVersionEeff(BigDecimal idVersionEeff) {
        this.idVersionEeff = idVersionEeff;
    }
}
