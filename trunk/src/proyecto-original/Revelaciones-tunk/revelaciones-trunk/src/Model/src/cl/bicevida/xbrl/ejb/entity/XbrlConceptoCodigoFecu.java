package cl.bicevida.xbrl.ejb.entity;

import cl.bicevida.revelaciones.ejb.entity.EstadoFinanciero;
import cl.bicevida.xbrl.ejb.entity.pk.XbrlConceptoCodigoFecuPK;

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
@NamedQueries( { @NamedQuery(name = "XbrlConceptoCodigoFecu.findAll",
                             query = "select o from XbrlConceptoCodigoFecu o") })
@Table(name = "REV_XBRL_CONCEPTO_CODIGO_FECU")
@IdClass(XbrlConceptoCodigoFecuPK.class)
public class XbrlConceptoCodigoFecu implements Serializable {
    @SuppressWarnings("compatibility:7605053560837114639")
    private static final long serialVersionUID = -4354847256495280086L;
    @Id
    @Column(name = "ID_CONCEPTO_XBRL", nullable = false, length = 1024)
    private String idConceptoXbrl;
    @Id
    @Column(name = "ID_FECU", nullable = false, insertable = false, updatable = false)
    private Long idFecu;
    @Id
    @Column(name = "ID_VERSION_EEFF", nullable = false, insertable = false, updatable = false)
    private Long idVersionEeff;
    
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "ID_FECU", referencedColumnName = "ID_FECU"),
        @JoinColumn(name = "ID_VERSION_EEFF", referencedColumnName = "ID_VERSION_EEFF")
    })
    private EstadoFinanciero estadoFinanciero;
    
    @ManyToOne
    @JoinColumn(name = "ID_TAXONOMIA")
    private XbrlTaxonomia xbrlTaxonomia;

    public XbrlConceptoCodigoFecu() {
        super();
    }

    public XbrlConceptoCodigoFecu(String idConceptoXbrl, EstadoFinanciero estadoFinanciero, XbrlTaxonomia xbrlTaxonomia) {
        super();
        this.idConceptoXbrl = idConceptoXbrl;
        this.estadoFinanciero = estadoFinanciero;
        this.xbrlTaxonomia = xbrlTaxonomia;
    }


    public void setIdConceptoXbrl(String idConceptoXbrl) {
        this.idConceptoXbrl = idConceptoXbrl;
    }

    public String getIdConceptoXbrl() {
        return idConceptoXbrl;
    }

    public void setIdFecu(Long idFecu) {
        this.idFecu = idFecu;
    }

    public Long getIdFecu() {
        return idFecu;
    }

    public void setIdVersionEeff(Long idVersionEeff) {
        this.idVersionEeff = idVersionEeff;
    }

    public Long getIdVersionEeff() {
        return idVersionEeff;
    }

    public void setEstadoFinanciero(EstadoFinanciero estadoFinanciero) {
        this.estadoFinanciero = estadoFinanciero;
    }

    public EstadoFinanciero getEstadoFinanciero() {
        return estadoFinanciero;
    }

    public void setXbrlTaxonomia(XbrlTaxonomia xbrlTaxonomia) {
        this.xbrlTaxonomia = xbrlTaxonomia;
    }

    public XbrlTaxonomia getXbrlTaxonomia() {
        return xbrlTaxonomia;
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
