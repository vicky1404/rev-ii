package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

public class RevXbrlConceptoCeldaPK implements Serializable {
    private BigDecimal idColumna;
    private String idConceptoXbrl;
    private BigDecimal idFila;
    private BigDecimal idGrilla;

    public RevXbrlConceptoCeldaPK() {
    }

    public RevXbrlConceptoCeldaPK(BigDecimal idColumna, String idConceptoXbrl, BigDecimal idFila,
                                  BigDecimal idGrilla) {
        this.idColumna = idColumna;
        this.idConceptoXbrl = idConceptoXbrl;
        this.idFila = idFila;
        this.idGrilla = idGrilla;
    }

    public boolean equals(Object other) {
        if (other instanceof RevXbrlConceptoCeldaPK) {
            final RevXbrlConceptoCeldaPK otherRevXbrlConceptoCeldaPK = (RevXbrlConceptoCeldaPK)other;
            final boolean areEqual =
                (otherRevXbrlConceptoCeldaPK.idColumna.equals(idColumna) && otherRevXbrlConceptoCeldaPK.idConceptoXbrl.equals(idConceptoXbrl) &&
                 otherRevXbrlConceptoCeldaPK.idFila.equals(idFila) &&
                 otherRevXbrlConceptoCeldaPK.idGrilla.equals(idGrilla));
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

    public String getIdConceptoXbrl() {
        return idConceptoXbrl;
    }

    public void setIdConceptoXbrl(String idConceptoXbrl) {
        this.idConceptoXbrl = idConceptoXbrl;
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
}
