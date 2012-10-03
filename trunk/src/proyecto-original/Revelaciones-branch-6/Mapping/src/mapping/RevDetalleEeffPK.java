package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

public class RevDetalleEeffPK implements Serializable {
    private BigDecimal idCuenta;
    private BigDecimal idFecu;
    private BigDecimal idVersionEeff;

    public RevDetalleEeffPK() {
    }

    public RevDetalleEeffPK(BigDecimal idCuenta, BigDecimal idFecu, BigDecimal idVersionEeff) {
        this.idCuenta = idCuenta;
        this.idFecu = idFecu;
        this.idVersionEeff = idVersionEeff;
    }

    public boolean equals(Object other) {
        if (other instanceof RevDetalleEeffPK) {
            final RevDetalleEeffPK otherRevDetalleEeffPK = (RevDetalleEeffPK)other;
            final boolean areEqual =
                (otherRevDetalleEeffPK.idCuenta.equals(idCuenta) && otherRevDetalleEeffPK.idFecu.equals(idFecu) &&
                 otherRevDetalleEeffPK.idVersionEeff.equals(idVersionEeff));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public BigDecimal getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(BigDecimal idCuenta) {
        this.idCuenta = idCuenta;
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
