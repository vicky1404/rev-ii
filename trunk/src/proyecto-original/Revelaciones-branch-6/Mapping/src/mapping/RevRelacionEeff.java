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
@NamedQueries( { @NamedQuery(name = "RevRelacionEeff.findAll", query = "select o from RevRelacionEeff o") })
@Table(name = "REV_RELACION_EEFF")
@IdClass(RevRelacionEeffPK.class)
public class RevRelacionEeff implements Serializable {
    @Id
    @Column(name = "ID_COLUMNA", nullable = false, insertable = false, updatable = false)
    private BigDecimal idColumna;
    @Id
    @Column(name = "ID_FECU", nullable = false, insertable = false, updatable = false)
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
    @Column(name = "MONTO_TOTAL")
    private BigDecimal montoTotal;
    @ManyToOne
    @JoinColumn(name = "ID_PERIODO")
    private RevPeriodo revPeriodo1;
    @ManyToOne
    @JoinColumns({
    @JoinColumn(name = "ID_COLUMNA", referencedColumnName = "ID_COLUMNA"),
    @JoinColumn(name = "ID_GRILLA", referencedColumnName = "ID_GRILLA"),
    @JoinColumn(name = "ID_FILA", referencedColumnName = "ID_FILA")
    })
    private RevCelda revCelda8;
    @ManyToOne
    @JoinColumn(name = "ID_FECU")
    private RevCodigoFecu revCodigoFecu1;

    public RevRelacionEeff() {
    }

    public RevRelacionEeff(RevCelda revCelda8, RevCodigoFecu revCodigoFecu1, RevPeriodo revPeriodo1, BigDecimal montoTotal) {
        this.revCelda8 = revCelda8;
        this.revCodigoFecu1 = revCodigoFecu1;
        this.revPeriodo1 = revPeriodo1;
        this.montoTotal = montoTotal;
    }


    public BigDecimal getIdColumna() {
        return idColumna;
    }

    public void setIdColumna(BigDecimal idColumna) {
        this.idColumna = idColumna;
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

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public RevPeriodo getRevPeriodo1() {
        return revPeriodo1;
    }

    public void setRevPeriodo1(RevPeriodo revPeriodo1) {
        this.revPeriodo1 = revPeriodo1;
        if (revPeriodo1 != null) {
            this.idPeriodo = revPeriodo1.getIdPeriodo();
        }
    }

    public RevCelda getRevCelda8() {
        return revCelda8;
    }

    public void setRevCelda8(RevCelda revCelda8) {
        this.revCelda8 = revCelda8;
        if (revCelda8 != null) {
            this.idFila = revCelda8.getIdFila();
            this.idGrilla = revCelda8.getIdGrilla();
            this.idColumna = revCelda8.getIdColumna();
        }
    }

    public RevCodigoFecu getRevCodigoFecu1() {
        return revCodigoFecu1;
    }

    public void setRevCodigoFecu1(RevCodigoFecu revCodigoFecu1) {
        this.revCodigoFecu1 = revCodigoFecu1;
        if (revCodigoFecu1 != null) {
            this.idFecu = revCodigoFecu1.getIdFecu();
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
        buffer.append("montoTotal=");
        buffer.append(getMontoTotal());
        buffer.append(',');
        buffer.append(']');
        return buffer.toString();
    }
}
