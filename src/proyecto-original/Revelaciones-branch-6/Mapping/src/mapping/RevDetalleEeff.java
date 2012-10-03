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
@NamedQueries( { @NamedQuery(name = "RevDetalleEeff.findAll", query = "select o from RevDetalleEeff o") })
@Table(name = "REV_DETALLE_EEFF")
@IdClass(RevDetalleEeffPK.class)
public class RevDetalleEeff implements Serializable {
    @Id
    @Column(name = "ID_CUENTA", nullable = false, insertable = false, updatable = false)
    private BigDecimal idCuenta;
    @Id
    @Column(name = "ID_FECU", nullable = false, insertable = false, updatable = false)
    private BigDecimal idFecu;
    @Id
    @Column(name = "ID_VERSION_EEFF", nullable = false, insertable = false, updatable = false)
    private BigDecimal idVersionEeff;
    @Column(name = "MONTO_EBS")
    private BigDecimal montoEbs;
    @Column(name = "MONTO_MILES")
    private BigDecimal montoMiles;
    @Column(name = "MONTO_PESOS")
    private BigDecimal montoPesos;
    @Column(name = "MONTO_PESOS_MIL")
    private BigDecimal montoPesosMil;
    @Column(name = "MONTO_RECLASIFICACION")
    private BigDecimal montoReclasificacion;
    @ManyToOne
    @JoinColumn(name = "ID_CUENTA")
    private RevCuentaContable revCuentaContable;
    @ManyToOne
    @JoinColumns({
    @JoinColumn(name = "ID_FECU", referencedColumnName = "ID_FECU"),
    @JoinColumn(name = "ID_VERSION_EEFF", referencedColumnName = "ID_VERSION_EEFF")
    })
    private RevEeff revEeff3;

    public RevDetalleEeff() {
    }

    public RevDetalleEeff(RevCuentaContable revCuentaContable, RevEeff revEeff3, BigDecimal montoEbs,
                          BigDecimal montoMiles, BigDecimal montoPesos, BigDecimal montoPesosMil,
                          BigDecimal montoReclasificacion) {
        this.revCuentaContable = revCuentaContable;
        this.revEeff3 = revEeff3;
        this.montoEbs = montoEbs;
        this.montoMiles = montoMiles;
        this.montoPesos = montoPesos;
        this.montoPesosMil = montoPesosMil;
        this.montoReclasificacion = montoReclasificacion;
    }


    public BigDecimal getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(BigDecimal idCuenta) {
        this.idCuenta = idCuenta;
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

    public BigDecimal getMontoEbs() {
        return montoEbs;
    }

    public void setMontoEbs(BigDecimal montoEbs) {
        this.montoEbs = montoEbs;
    }

    public BigDecimal getMontoMiles() {
        return montoMiles;
    }

    public void setMontoMiles(BigDecimal montoMiles) {
        this.montoMiles = montoMiles;
    }

    public BigDecimal getMontoPesos() {
        return montoPesos;
    }

    public void setMontoPesos(BigDecimal montoPesos) {
        this.montoPesos = montoPesos;
    }

    public BigDecimal getMontoPesosMil() {
        return montoPesosMil;
    }

    public void setMontoPesosMil(BigDecimal montoPesosMil) {
        this.montoPesosMil = montoPesosMil;
    }

    public BigDecimal getMontoReclasificacion() {
        return montoReclasificacion;
    }

    public void setMontoReclasificacion(BigDecimal montoReclasificacion) {
        this.montoReclasificacion = montoReclasificacion;
    }

    public RevCuentaContable getRevCuentaContable() {
        return revCuentaContable;
    }

    public void setRevCuentaContable(RevCuentaContable revCuentaContable) {
        this.revCuentaContable = revCuentaContable;
        if (revCuentaContable != null) {
            this.idCuenta = revCuentaContable.getIdCuenta();
        }
    }

    public RevEeff getRevEeff3() {
        return revEeff3;
    }

    public void setRevEeff3(RevEeff revEeff3) {
        this.revEeff3 = revEeff3;
        if (revEeff3 != null) {
            this.idVersionEeff = revEeff3.getIdVersionEeff();
            this.idFecu = revEeff3.getIdFecu();
        }
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idCuenta=");
        buffer.append(getIdCuenta());
        buffer.append(',');
        buffer.append("idFecu=");
        buffer.append(getIdFecu());
        buffer.append(',');
        buffer.append("idVersionEeff=");
        buffer.append(getIdVersionEeff());
        buffer.append(',');
        buffer.append("montoEbs=");
        buffer.append(getMontoEbs());
        buffer.append(',');
        buffer.append("montoMiles=");
        buffer.append(getMontoMiles());
        buffer.append(',');
        buffer.append("montoPesos=");
        buffer.append(getMontoPesos());
        buffer.append(',');
        buffer.append("montoPesosMil=");
        buffer.append(getMontoPesosMil());
        buffer.append(',');
        buffer.append("montoReclasificacion=");
        buffer.append(getMontoReclasificacion());
        buffer.append(',');
        buffer.append(']');
        return buffer.toString();
    }
}
