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
@NamedQueries( { @NamedQuery(name = "RevCuentaContable.findAll", query = "select o from RevCuentaContable o") })
@Table(name = "REV_CUENTA_CONTABLE")
public class RevCuentaContable implements Serializable {
    @Column(nullable = false, length = 256)
    private String descripcion;
    @Id
    @Column(name = "ID_CUENTA", nullable = false)
    private BigDecimal idCuenta;
    private BigDecimal vigencia;
    @OneToMany(mappedBy = "revCuentaContable")
    private List<RevDetalleEeff> revDetalleEeffList;
    @OneToMany(mappedBy = "revCuentaContable1")
    private List<RevRelacionDetalleEeff> revRelacionDetalleEeffList4;

    public RevCuentaContable() {
    }

    public RevCuentaContable(String descripcion, BigDecimal idCuenta, BigDecimal vigencia) {
        this.descripcion = descripcion;
        this.idCuenta = idCuenta;
        this.vigencia = vigencia;
    }


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(BigDecimal idCuenta) {
        this.idCuenta = idCuenta;
    }

    public BigDecimal getVigencia() {
        return vigencia;
    }

    public void setVigencia(BigDecimal vigencia) {
        this.vigencia = vigencia;
    }

    public List<RevDetalleEeff> getRevDetalleEeffList() {
        return revDetalleEeffList;
    }

    public void setRevDetalleEeffList(List<RevDetalleEeff> revDetalleEeffList) {
        this.revDetalleEeffList = revDetalleEeffList;
    }

    public RevDetalleEeff addRevDetalleEeff(RevDetalleEeff revDetalleEeff) {
        getRevDetalleEeffList().add(revDetalleEeff);
        revDetalleEeff.setRevCuentaContable(this);
        return revDetalleEeff;
    }

    public RevDetalleEeff removeRevDetalleEeff(RevDetalleEeff revDetalleEeff) {
        getRevDetalleEeffList().remove(revDetalleEeff);
        revDetalleEeff.setRevCuentaContable(null);
        return revDetalleEeff;
    }

    public List<RevRelacionDetalleEeff> getRevRelacionDetalleEeffList4() {
        return revRelacionDetalleEeffList4;
    }

    public void setRevRelacionDetalleEeffList4(List<RevRelacionDetalleEeff> revRelacionDetalleEeffList4) {
        this.revRelacionDetalleEeffList4 = revRelacionDetalleEeffList4;
    }

    public RevRelacionDetalleEeff addRevRelacionDetalleEeff(RevRelacionDetalleEeff revRelacionDetalleEeff) {
        getRevRelacionDetalleEeffList4().add(revRelacionDetalleEeff);
        revRelacionDetalleEeff.setRevCuentaContable1(this);
        return revRelacionDetalleEeff;
    }

    public RevRelacionDetalleEeff removeRevRelacionDetalleEeff(RevRelacionDetalleEeff revRelacionDetalleEeff) {
        getRevRelacionDetalleEeffList4().remove(revRelacionDetalleEeff);
        revRelacionDetalleEeff.setRevCuentaContable1(null);
        return revRelacionDetalleEeff;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("descripcion=");
        buffer.append(getDescripcion());
        buffer.append(',');
        buffer.append("idCuenta=");
        buffer.append(getIdCuenta());
        buffer.append(',');
        buffer.append("vigencia=");
        buffer.append(getVigencia());
        buffer.append(']');
        return buffer.toString();
    }
}
