package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "RevEeff.findAll", query = "select o from RevEeff o") })
@Table(name = "REV_EEFF")
@IdClass(RevEeffPK.class)
public class RevEeff implements Serializable {
    @Id
    @Column(name = "ID_FECU", nullable = false, insertable = false, updatable = false)
    private BigDecimal idFecu;
    @Id
    @Column(name = "ID_VERSION_EEFF", nullable = false, insertable = false, updatable = false)
    private BigDecimal idVersionEeff;
    @Column(name = "MONTO_TOTAL")
    private BigDecimal montoTotal;
    @ManyToOne
    @JoinColumn(name = "ID_VERSION_EEFF")
    private RevVersionEeff revVersionEeff;
    @OneToMany(mappedBy = "revEeff1")
    private List<RevXbrlConceptoCodigoFecu> revXbrlConceptoCodigoFecuList1;
    @ManyToOne
    @JoinColumn(name = "ID_FECU")
    private RevCodigoFecu revCodigoFecu;
    @OneToMany(mappedBy = "revEeff3")
    private List<RevDetalleEeff> revDetalleEeffList2;

    public RevEeff() {
    }

    public RevEeff(RevCodigoFecu revCodigoFecu, RevVersionEeff revVersionEeff, BigDecimal montoTotal) {
        this.revCodigoFecu = revCodigoFecu;
        this.revVersionEeff = revVersionEeff;
        this.montoTotal = montoTotal;
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

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public RevVersionEeff getRevVersionEeff() {
        return revVersionEeff;
    }

    public void setRevVersionEeff(RevVersionEeff revVersionEeff) {
        this.revVersionEeff = revVersionEeff;
        if (revVersionEeff != null) {
            this.idVersionEeff = revVersionEeff.getIdVersionEeff();
        }
    }

    public List<RevXbrlConceptoCodigoFecu> getRevXbrlConceptoCodigoFecuList1() {
        return revXbrlConceptoCodigoFecuList1;
    }

    public void setRevXbrlConceptoCodigoFecuList1(List<RevXbrlConceptoCodigoFecu> revXbrlConceptoCodigoFecuList1) {
        this.revXbrlConceptoCodigoFecuList1 = revXbrlConceptoCodigoFecuList1;
    }

    public RevXbrlConceptoCodigoFecu addRevXbrlConceptoCodigoFecu(RevXbrlConceptoCodigoFecu revXbrlConceptoCodigoFecu) {
        getRevXbrlConceptoCodigoFecuList1().add(revXbrlConceptoCodigoFecu);
        revXbrlConceptoCodigoFecu.setRevEeff1(this);
        return revXbrlConceptoCodigoFecu;
    }

    public RevXbrlConceptoCodigoFecu removeRevXbrlConceptoCodigoFecu(RevXbrlConceptoCodigoFecu revXbrlConceptoCodigoFecu) {
        getRevXbrlConceptoCodigoFecuList1().remove(revXbrlConceptoCodigoFecu);
        revXbrlConceptoCodigoFecu.setRevEeff1(null);
        return revXbrlConceptoCodigoFecu;
    }

    public RevCodigoFecu getRevCodigoFecu() {
        return revCodigoFecu;
    }

    public void setRevCodigoFecu(RevCodigoFecu revCodigoFecu) {
        this.revCodigoFecu = revCodigoFecu;
        if (revCodigoFecu != null) {
            this.idFecu = revCodigoFecu.getIdFecu();
        }
    }

    public List<RevDetalleEeff> getRevDetalleEeffList2() {
        return revDetalleEeffList2;
    }

    public void setRevDetalleEeffList2(List<RevDetalleEeff> revDetalleEeffList2) {
        this.revDetalleEeffList2 = revDetalleEeffList2;
    }

    public RevDetalleEeff addRevDetalleEeff(RevDetalleEeff revDetalleEeff) {
        getRevDetalleEeffList2().add(revDetalleEeff);
        revDetalleEeff.setRevEeff3(this);
        return revDetalleEeff;
    }

    public RevDetalleEeff removeRevDetalleEeff(RevDetalleEeff revDetalleEeff) {
        getRevDetalleEeffList2().remove(revDetalleEeff);
        revDetalleEeff.setRevEeff3(null);
        return revDetalleEeff;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idFecu=");
        buffer.append(getIdFecu());
        buffer.append(',');
        buffer.append("idVersionEeff=");
        buffer.append(getIdVersionEeff());
        buffer.append(',');
        buffer.append("montoTotal=");
        buffer.append(getMontoTotal());
        buffer.append(',');
        buffer.append(']');
        return buffer.toString();
    }
}
