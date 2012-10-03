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
@NamedQueries( { @NamedQuery(name = "RevXbrlConceptoCodigoFecu.findAll",
                             query = "select o from RevXbrlConceptoCodigoFecu o") })
@Table(name = "REV_XBRL_CONCEPTO_CODIGO_FECU")
@IdClass(RevXbrlConceptoCodigoFecuPK.class)
public class RevXbrlConceptoCodigoFecu implements Serializable {
    @Id
    @Column(name = "ID_CONCEPTO_XBRL", nullable = false, length = 1024)
    private String idConceptoXbrl;
    @Id
    @Column(name = "ID_FECU", nullable = false, insertable = false, updatable = false)
    private BigDecimal idFecu;
    @Id
    @Column(name = "ID_VERSION_EEFF", nullable = false, insertable = false, updatable = false)
    private BigDecimal idVersionEeff;
    @ManyToOne
    @JoinColumns({
    @JoinColumn(name = "ID_FECU", referencedColumnName = "ID_FECU"),
    @JoinColumn(name = "ID_VERSION_EEFF", referencedColumnName = "ID_VERSION_EEFF")
    })
    private RevEeff revEeff1;
    @ManyToOne
    @JoinColumn(name = "ID_TAXONOMIA")
    private RevXbrlTaxonomia revXbrlTaxonomia;

    public RevXbrlConceptoCodigoFecu() {
    }

    public RevXbrlConceptoCodigoFecu(String idConceptoXbrl, RevEeff revEeff1, RevXbrlTaxonomia revXbrlTaxonomia) {
        this.idConceptoXbrl = idConceptoXbrl;
        this.revEeff1 = revEeff1;
        this.revXbrlTaxonomia = revXbrlTaxonomia;
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

    public RevEeff getRevEeff1() {
        return revEeff1;
    }

    public void setRevEeff1(RevEeff revEeff1) {
        this.revEeff1 = revEeff1;
        if (revEeff1 != null) {
            this.idVersionEeff = revEeff1.getIdVersionEeff();
            this.idFecu = revEeff1.getIdFecu();
        }
    }

    public RevXbrlTaxonomia getRevXbrlTaxonomia() {
        return revXbrlTaxonomia;
    }

    public void setRevXbrlTaxonomia(RevXbrlTaxonomia revXbrlTaxonomia) {
        this.revXbrlTaxonomia = revXbrlTaxonomia;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idConceptoXbrl=");
        buffer.append(getIdConceptoXbrl());
        buffer.append(',');
        buffer.append("idFecu=");
        buffer.append(getIdFecu());
        buffer.append(',');
        buffer.append("idVersionEeff=");
        buffer.append(getIdVersionEeff());
        buffer.append(',');
        buffer.append(']');
        return buffer.toString();
    }
}
