package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

public class RevXbrlConceptoCodigoFecuPK implements Serializable {
    private String idConceptoXbrl;
    private BigDecimal idFecu;
    private BigDecimal idVersionEeff;

    public RevXbrlConceptoCodigoFecuPK() {
    }

    public RevXbrlConceptoCodigoFecuPK(String idConceptoXbrl, BigDecimal idFecu, BigDecimal idVersionEeff) {
        this.idConceptoXbrl = idConceptoXbrl;
        this.idFecu = idFecu;
        this.idVersionEeff = idVersionEeff;
    }

    public boolean equals(Object other) {
        if (other instanceof RevXbrlConceptoCodigoFecuPK) {
            final RevXbrlConceptoCodigoFecuPK otherRevXbrlConceptoCodigoFecuPK = (RevXbrlConceptoCodigoFecuPK)other;
            final boolean areEqual =
                (otherRevXbrlConceptoCodigoFecuPK.idConceptoXbrl.equals(idConceptoXbrl) && otherRevXbrlConceptoCodigoFecuPK.idFecu.equals(idFecu) &&
                 otherRevXbrlConceptoCodigoFecuPK.idVersionEeff.equals(idVersionEeff));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String getIdConceptoXbrl() {
        return idConceptoXbrl;
    }

    public void setIdConceptoXbrl(String idConceptoXbrl) {
        this.idConceptoXbrl = idConceptoXbrl;
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
