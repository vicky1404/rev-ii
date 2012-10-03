package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

public class RevVersionPeriodoPK implements Serializable {
    private BigDecimal idPeriodo;
    private BigDecimal idVersion;

    public RevVersionPeriodoPK() {
    }

    public RevVersionPeriodoPK(BigDecimal idPeriodo, BigDecimal idVersion) {
        this.idPeriodo = idPeriodo;
        this.idVersion = idVersion;
    }

    public boolean equals(Object other) {
        if (other instanceof RevVersionPeriodoPK) {
            final RevVersionPeriodoPK otherRevVersionPeriodoPK = (RevVersionPeriodoPK)other;
            final boolean areEqual =
                (otherRevVersionPeriodoPK.idPeriodo.equals(idPeriodo) && otherRevVersionPeriodoPK.idVersion.equals(idVersion));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public BigDecimal getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(BigDecimal idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public BigDecimal getIdVersion() {
        return idVersion;
    }

    public void setIdVersion(BigDecimal idVersion) {
        this.idVersion = idVersion;
    }
}
