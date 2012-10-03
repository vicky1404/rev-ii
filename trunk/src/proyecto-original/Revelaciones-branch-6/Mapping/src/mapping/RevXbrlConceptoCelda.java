package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "RevXbrlConceptoCelda.findAll", query = "select o from RevXbrlConceptoCelda o") })
@Table(name = "REV_XBRL_CONCEPTO_CELDA")
@IdClass(RevXbrlConceptoCeldaPK.class)
public class RevXbrlConceptoCelda implements Serializable {
    @Id
    @Column(name = "ID_COLUMNA", nullable = false, insertable = false, updatable = false)
    private BigDecimal idColumna;
    @Id
    @Column(name = "ID_CONCEPTO_XBRL", nullable = false, length = 1024)
    private String idConceptoXbrl;
    @Id
    @Column(name = "ID_FILA", nullable = false, insertable = false, updatable = false)
    private BigDecimal idFila;
    @Id
    @Column(name = "ID_GRILLA", nullable = false, insertable = false, updatable = false)
    private BigDecimal idGrilla;
    @ManyToOne
    @JoinColumns({
    @JoinColumn(name = "ID_COLUMNA", referencedColumnName = "ID_COLUMNA"),
    @JoinColumn(name = "ID_GRILLA", referencedColumnName = "ID_GRILLA"),
    @JoinColumn(name = "ID_FILA", referencedColumnName = "ID_FILA")
    })
    private RevCelda revCelda5;
    @ManyToOne
    @JoinColumn(name = "ID_TAXONOMIA")
    private RevXbrlTaxonomia revXbrlTaxonomia1;

    public RevXbrlConceptoCelda() {
    }

    public RevXbrlConceptoCelda(RevCelda revCelda5, String idConceptoXbrl, RevXbrlTaxonomia revXbrlTaxonomia1) {
        this.revCelda5 = revCelda5;
        this.idConceptoXbrl = idConceptoXbrl;
        this.revXbrlTaxonomia1 = revXbrlTaxonomia1;
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


    public RevCelda getRevCelda5() {
        return revCelda5;
    }

    public void setRevCelda5(RevCelda revCelda5) {
        this.revCelda5 = revCelda5;
        if (revCelda5 != null) {
            this.idFila = revCelda5.getIdFila();
            this.idGrilla = revCelda5.getIdGrilla();
            this.idColumna = revCelda5.getIdColumna();
        }
    }

    public RevXbrlTaxonomia getRevXbrlTaxonomia1() {
        return revXbrlTaxonomia1;
    }

    public void setRevXbrlTaxonomia1(RevXbrlTaxonomia revXbrlTaxonomia1) {
        this.revXbrlTaxonomia1 = revXbrlTaxonomia1;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idColumna=");
        buffer.append(getIdColumna());
        buffer.append(',');
        buffer.append("idConceptoXbrl=");
        buffer.append(getIdConceptoXbrl());
        buffer.append(',');
        buffer.append("idFila=");
        buffer.append(getIdFila());
        buffer.append(',');
        buffer.append("idGrilla=");
        buffer.append(getIdGrilla());
        buffer.append(',');
        buffer.append(']');
        return buffer.toString();
    }
}
