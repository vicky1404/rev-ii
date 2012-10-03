package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

public class RevRelacionDetalleEeffPK implements Serializable {
    private BigDecimal idColumna;
    private BigDecimal idCuenta;
    private BigDecimal idFecu;
    private BigDecimal idFila;
    private BigDecimal idGrilla;
    private BigDecimal idPeriodo;

    public RevRelacionDetalleEeffPK() {
    }

    public RevRelacionDetalleEeffPK(BigDecimal idColumna, BigDecimal idCuenta, BigDecimal idFecu, BigDecimal idFila,
                                    BigDecimal idGrilla, BigDecimal idPeriodo) {
        this.idColumna = idColumna;
        this.idCuenta = idCuenta;
        this.idFecu = idFecu;
        this.idFila = idFila;
        this.idGrilla = idGrilla;
        this.idPeriodo = idPeriodo;
    }

    public boolean equals(Object other) {
        if (other instanceof RevRelacionDetalleEeffPK) {
            final RevRelacionDetalleEeffPK otherRevRelacionDetalleEeffPK = (RevRelacionDetalleEeffPK)other;
            final boolean areEqual =
                (otherRevRelacionDetalleEeffPK.idColumna.equals(idColumna) && otherRevRelacionDetalleEeffPK.idCuenta.equals(idCuenta) &&
                 otherRevRelacionDetalleEeffPK.idFecu.equals(idFecu) &&
                 otherRevRelacionDetalleEeffPK.idFila.equals(idFila) &&
                 otherRevRelacionDetalleEeffPK.idGrilla.equals(idGrilla) &&
                 otherRevRelacionDetalleEeffPK.idPeriodo.equals(idPeriodo));
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

    public BigDecimal getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(BigDecimal idPeriodo) {
        this.idPeriodo = idPeriodo;
    }
}
