package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "RevCodigoFecu.findAll", query = "select o from RevCodigoFecu o") })
@Table(name = "REV_CODIGO_FECU")
public class RevCodigoFecu implements Serializable {
    @Column(nullable = false, length = 256)
    private String descripcion;
    @Id
    @Column(name = "ID_FECU", nullable = false)
    private BigDecimal idFecu;
    @Column(nullable = false)
    private BigDecimal vigencia;
    @OneToMany(mappedBy = "revCodigoFecu")
    private List<RevEeff> revEeffList1;
    @OneToMany(mappedBy = "revCodigoFecu1")
    private List<RevRelacionEeff> revRelacionEeffList4;

    public RevCodigoFecu() {
    }

    public RevCodigoFecu(String descripcion, BigDecimal idFecu, BigDecimal vigencia) {
        this.descripcion = descripcion;
        this.idFecu = idFecu;
        this.vigencia = vigencia;
    }


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getIdFecu() {
        return idFecu;
    }

    public void setIdFecu(BigDecimal idFecu) {
        this.idFecu = idFecu;
    }

    public BigDecimal getVigencia() {
        return vigencia;
    }

    public void setVigencia(BigDecimal vigencia) {
        this.vigencia = vigencia;
    }

    public List<RevEeff> getRevEeffList1() {
        return revEeffList1;
    }

    public void setRevEeffList1(List<RevEeff> revEeffList1) {
        this.revEeffList1 = revEeffList1;
    }

    public RevEeff addRevEeff(RevEeff revEeff) {
        getRevEeffList1().add(revEeff);
        revEeff.setRevCodigoFecu(this);
        return revEeff;
    }

    public RevEeff removeRevEeff(RevEeff revEeff) {
        getRevEeffList1().remove(revEeff);
        revEeff.setRevCodigoFecu(null);
        return revEeff;
    }

    public List<RevRelacionEeff> getRevRelacionEeffList4() {
        return revRelacionEeffList4;
    }

    public void setRevRelacionEeffList4(List<RevRelacionEeff> revRelacionEeffList4) {
        this.revRelacionEeffList4 = revRelacionEeffList4;
    }

    public RevRelacionEeff addRevRelacionEeff(RevRelacionEeff revRelacionEeff) {
        getRevRelacionEeffList4().add(revRelacionEeff);
        revRelacionEeff.setRevCodigoFecu1(this);
        return revRelacionEeff;
    }

    public RevRelacionEeff removeRevRelacionEeff(RevRelacionEeff revRelacionEeff) {
        getRevRelacionEeffList4().remove(revRelacionEeff);
        revRelacionEeff.setRevCodigoFecu1(null);
        return revRelacionEeff;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("descripcion=");
        buffer.append(getDescripcion());
        buffer.append(',');
        buffer.append("idFecu=");
        buffer.append(getIdFecu());
        buffer.append(',');
        buffer.append("vigencia=");
        buffer.append(getVigencia());
        buffer.append(']');
        return buffer.toString();
    }
}
