package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

public class RevRelacionEeffPK implements Serializable {
    private BigDecimal idColumna;
    private BigDecimal idFecu;
    private BigDecimal idFila;
    private BigDecimal idGrilla;
    private BigDecimal idPeriodo;

    public RevRelacionEeffPK() {
    }

    public RevRelacionEeffPK(BigDecimal idColumna, BigDecimal idFecu, BigDecimal idFila, BigDecimal idGrilla,
                             BigDecimal idPeriodo) {
        this.idColumna = idColumna;
        this.idFecu = idFecu;
        this.idFila = idFila;
        this.idGrilla = idGrilla;
        this.idPeriodo = idPeriodo;
    }

    public boolean equals(Object other) {
        if (other instanceof RevRelacionEeffPK) {
            final RevRelacionEeffPK otherRevRelacionEeffPK = (RevRelacionEeffPK)other;
            final boolean areEqual =
                (otherRevRelacionEeffPK.idColumna.equals(idColumna) && otherRevRelacionEeffPK.idFecu.equals(idFecu) &&
                 otherRevRelacionEeffPK.idFila.equals(idFila) && otherRevRelacionEeffPK.idGrilla.equals(idGrilla) &&
                 otherRevRelacionEeffPK.idPeriodo.equals(idPeriodo));
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
