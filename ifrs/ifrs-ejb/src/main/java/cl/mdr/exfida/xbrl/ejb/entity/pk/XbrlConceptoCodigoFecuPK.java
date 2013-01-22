package cl.mdr.exfida.xbrl.ejb.entity.pk;

import java.io.Serializable;

public class XbrlConceptoCodigoFecuPK implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 4441798283624250485L;
	
	private String idConceptoXbrl;
    private Long idFecu;
    private Long idVersionEeff;

    public XbrlConceptoCodigoFecuPK() {
    }

    public XbrlConceptoCodigoFecuPK(String idConceptoXbrl, Long idFecu, Long idVersionEeff) {
        this.idConceptoXbrl = idConceptoXbrl;
        this.idFecu = idFecu;
        this.idVersionEeff = idVersionEeff;
    }

    public boolean equals(Object other) {
        if (other instanceof XbrlConceptoCodigoFecuPK) {
            final XbrlConceptoCodigoFecuPK otherRevXbrlConceptoCodigoFecuPK = (XbrlConceptoCodigoFecuPK)other;
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

    public Long getIdFecu() {
        return idFecu;
    }

    public void setIdFecu(Long idFecu) {
        this.idFecu = idFecu;
    }

    public Long getIdVersionEeff() {
        return idVersionEeff;
    }

    public void setIdVersionEeff(Long idVersionEeff) {
        this.idVersionEeff = idVersionEeff;
    }
}
