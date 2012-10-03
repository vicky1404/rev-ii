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
@NamedQueries( { @NamedQuery(name = "RevRelacionDetalleEeff.findAll",
                             query = "select o from RevRelacionDetalleEeff o") })
@Table(name = "REV_RELACION_DETALLE_EEFF")
@IdClass(RevRelacionDetalleEeffPK.class)
public class RevRelacionDetalleEeff implements Serializable {
    @Id
    @Column(name = "ID_COLUMNA", nullable = false, insertable = false, updatable = false)
    private BigDecimal idColumna;
    @Id
    @Column(name = "ID_CUENTA", nullable = false, insertable = false, updatable = false)
    private BigDecimal idCuenta;
    @Id
    @Column(name = "ID_FECU", nullable = false)
    private BigDecimal idFecu;
    @Id
    @Column(name = "ID_FILA", nullable = false, insertable = false, updatable = false)
    private BigDecimal idFila;
    @Id
    @Column(name = "ID_GRILLA", nullable = false, insertable = false, updatable = false)
    private BigDecimal idGrilla;
    @Id
    @Column(name = "ID_PERIODO", nullable = false, insertable = false, updatable = false)
    private BigDecimal idPeriodo;
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
    @JoinColumns({
    @JoinColumn(name = "ID_COLUMNA", referencedColumnName = "ID_COLUMNA"),
    @JoinColumn(name = "ID_GRILLA", referencedColumnName = "ID_GRILLA"),
    @JoinColumn(name = "ID_FILA", referencedColumnName = "ID_FILA")
    })
    private RevCelda revCelda2;
    @ManyToOne
    @JoinColumn(name = "ID_PERIODO")
    private RevPeriodo revPeriodo3;
    @ManyToOne
    @JoinColumn(name = "ID_CUENTA")
    private RevCuentaContable revCuentaContable1;

    public RevRelacionDetalleEeff() {
    }

    public RevRelacionDetalleEeff(RevCelda revCelda2, RevCuentaContable revCuentaContable1, BigDecimal idFecu, RevPeriodo revPeriodo3, BigDecimal montoEbs,
                                  BigDecimal montoMiles, BigDecimal montoPesos, BigDecimal montoPesosMil,
                                  BigDecimal montoReclasificacion) {
        this.revCelda2 = revCelda2;
        this.revCuentaContable1 = revCuentaContable1;
        this.idFecu = idFecu;
        this.revPeriodo3 = revPeriodo3;
        this.montoEbs = montoEbs;
        this.montoMiles = montoMiles;
        this.montoPesos = montoPesos;
        this.montoPesosMil = montoPesosMil;
        this.montoReclasificacion = montoReclasificacion;
    }


    public BigDecimal getIdColumna() {
        return idColumna;
    }

    public void setIdColumna(BigDecimal idColumna) {
        this.idColumna = idColumna;
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

    public BigDecimal getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(BigDecimal idPeriodo) {
        this.idPeriodo = idPeriodo;
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

    public RevCelda getRevCelda2() {
        return revCelda2;
    }

    public void setRevCelda2(RevCelda revCelda2) {
        this.revCelda2 = revCelda2;
        if (revCelda2 != null) {
            this.idFila = revCelda2.getIdFila();
            this.idGrilla = revCelda2.getIdGrilla();
            this.idColumna = revCelda2.getIdColumna();
        }
    }

    public RevPeriodo getRevPeriodo3() {
        return revPeriodo3;
    }

    public void setRevPeriodo3(RevPeriodo revPeriodo3) {
        this.revPeriodo3 = revPeriodo3;
        if (revPeriodo3 != null) {
            this.idPeriodo = revPeriodo3.getIdPeriodo();
        }
    }

    public RevCuentaContable getRevCuentaContable1() {
        return revCuentaContable1;
    }

    public void setRevCuentaContable1(RevCuentaContable revCuentaContable1) {
        this.revCuentaContable1 = revCuentaContable1;
        if (revCuentaContable1 != null) {
            this.idCuenta = revCuentaContable1.getIdCuenta();
        }
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idColumna=");
        buffer.append(getIdColumna());
        buffer.append(',');
        buffer.append("idCuenta=");
        buffer.append(getIdCuenta());
        buffer.append(',');
        buffer.append("idFecu=");
        buffer.append(getIdFecu());
        buffer.append(',');
        buffer.append("idFila=");
        buffer.append(getIdFila());
        buffer.append(',');
        buffer.append("idGrilla=");
        buffer.append(getIdGrilla());
        buffer.append(',');
        buffer.append("idPeriodo=");
        buffer.append(getIdPeriodo());
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
