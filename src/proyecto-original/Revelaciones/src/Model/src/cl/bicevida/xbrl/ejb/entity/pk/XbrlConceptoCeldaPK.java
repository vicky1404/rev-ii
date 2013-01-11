package cl.bicevida.xbrl.ejb.entity.pk;

import java.io.Serializable;

public class XbrlConceptoCeldaPK implements Serializable {
    private static final long serialVersionUID = 3674306144882102968L;
    
    private Long idColumna;
    private String idConceptoXbrl;
    private Long idFila;
    private Long idGrilla;

    public XbrlConceptoCeldaPK() {
        super();
    }

    public XbrlConceptoCeldaPK(Long idColumna, String idConceptoXbrl, Long idFila, Long idGrilla) {
        this.idColumna = idColumna;
        this.idConceptoXbrl = idConceptoXbrl;
        this.idFila = idFila;
        this.idGrilla = idGrilla;
    }

    public boolean equals(Object other) {
        if (other instanceof XbrlConceptoCeldaPK) {
            final XbrlConceptoCeldaPK otherRevXbrlConceptoCeldaPK = (XbrlConceptoCeldaPK)other;
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

    public Long getIdColumna() {
        return idColumna;
    }

    public void setIdColumna(Long idColumna) {
        this.idColumna = idColumna;
    }

    public String getIdConceptoXbrl() {
        return idConceptoXbrl;
    }

    public void setIdConceptoXbrl(String idConceptoXbrl) {
        this.idConceptoXbrl = idConceptoXbrl;
    }

    public Long getIdFila() {
        return idFila;
    }

    public void setIdFila(Long idFila) {
        this.idFila = idFila;
    }

    public Long getIdGrilla() {
        return idGrilla;
    }

    public void setIdGrilla(Long idGrilla) {
        this.idGrilla = idGrilla;
    }
}
