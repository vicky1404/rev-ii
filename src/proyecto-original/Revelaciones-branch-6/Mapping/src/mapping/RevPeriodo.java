package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "RevPeriodo.findAll", query = "select o from RevPeriodo o") })
@Table(name = "REV_PERIODO")
public class RevPeriodo implements Serializable {
    @Id
    @Column(name = "ID_PERIODO", nullable = false)
    private BigDecimal idPeriodo;
    @Column(nullable = false)
    private BigDecimal periodo;
    @OneToMany(mappedBy = "revPeriodo")
    private List<RevHistorialReporte> revHistorialReporteList;
    @OneToMany(mappedBy = "revPeriodo1")
    private List<RevRelacionEeff> revRelacionEeffList;
    @ManyToOne
    @JoinColumn(name = "ID_ESTADO_PERIODO")
    private RevEstadoPeriodo revEstadoPeriodo;
    @OneToMany(mappedBy = "revPeriodo2")
    private List<RevVersionPeriodo> revVersionPeriodoList2;
    @OneToMany(mappedBy = "revPeriodo3")
    private List<RevRelacionDetalleEeff> revRelacionDetalleEeffList3;
    @OneToMany(mappedBy = "revPeriodo4")
    private List<RevVersionEeff> revVersionEeffList1;

    public RevPeriodo() {
    }

    public RevPeriodo(RevEstadoPeriodo revEstadoPeriodo, BigDecimal idPeriodo, BigDecimal periodo) {
        this.revEstadoPeriodo = revEstadoPeriodo;
        this.idPeriodo = idPeriodo;
        this.periodo = periodo;
    }


    public BigDecimal getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(BigDecimal idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public BigDecimal getPeriodo() {
        return periodo;
    }

    public void setPeriodo(BigDecimal periodo) {
        this.periodo = periodo;
    }

    public List<RevHistorialReporte> getRevHistorialReporteList() {
        return revHistorialReporteList;
    }

    public void setRevHistorialReporteList(List<RevHistorialReporte> revHistorialReporteList) {
        this.revHistorialReporteList = revHistorialReporteList;
    }

    public RevHistorialReporte addRevHistorialReporte(RevHistorialReporte revHistorialReporte) {
        getRevHistorialReporteList().add(revHistorialReporte);
        revHistorialReporte.setRevPeriodo(this);
        return revHistorialReporte;
    }

    public RevHistorialReporte removeRevHistorialReporte(RevHistorialReporte revHistorialReporte) {
        getRevHistorialReporteList().remove(revHistorialReporte);
        revHistorialReporte.setRevPeriodo(null);
        return revHistorialReporte;
    }

    public List<RevRelacionEeff> getRevRelacionEeffList() {
        return revRelacionEeffList;
    }

    public void setRevRelacionEeffList(List<RevRelacionEeff> revRelacionEeffList) {
        this.revRelacionEeffList = revRelacionEeffList;
    }

    public RevRelacionEeff addRevRelacionEeff(RevRelacionEeff revRelacionEeff) {
        getRevRelacionEeffList().add(revRelacionEeff);
        revRelacionEeff.setRevPeriodo1(this);
        return revRelacionEeff;
    }

    public RevRelacionEeff removeRevRelacionEeff(RevRelacionEeff revRelacionEeff) {
        getRevRelacionEeffList().remove(revRelacionEeff);
        revRelacionEeff.setRevPeriodo1(null);
        return revRelacionEeff;
    }

    public RevEstadoPeriodo getRevEstadoPeriodo() {
        return revEstadoPeriodo;
    }

    public void setRevEstadoPeriodo(RevEstadoPeriodo revEstadoPeriodo) {
        this.revEstadoPeriodo = revEstadoPeriodo;
    }

    public List<RevVersionPeriodo> getRevVersionPeriodoList2() {
        return revVersionPeriodoList2;
    }

    public void setRevVersionPeriodoList2(List<RevVersionPeriodo> revVersionPeriodoList2) {
        this.revVersionPeriodoList2 = revVersionPeriodoList2;
    }

    public RevVersionPeriodo addRevVersionPeriodo(RevVersionPeriodo revVersionPeriodo) {
        getRevVersionPeriodoList2().add(revVersionPeriodo);
        revVersionPeriodo.setRevPeriodo2(this);
        return revVersionPeriodo;
    }

    public RevVersionPeriodo removeRevVersionPeriodo(RevVersionPeriodo revVersionPeriodo) {
        getRevVersionPeriodoList2().remove(revVersionPeriodo);
        revVersionPeriodo.setRevPeriodo2(null);
        return revVersionPeriodo;
    }

    public List<RevRelacionDetalleEeff> getRevRelacionDetalleEeffList3() {
        return revRelacionDetalleEeffList3;
    }

    public void setRevRelacionDetalleEeffList3(List<RevRelacionDetalleEeff> revRelacionDetalleEeffList3) {
        this.revRelacionDetalleEeffList3 = revRelacionDetalleEeffList3;
    }

    public RevRelacionDetalleEeff addRevRelacionDetalleEeff(RevRelacionDetalleEeff revRelacionDetalleEeff) {
        getRevRelacionDetalleEeffList3().add(revRelacionDetalleEeff);
        revRelacionDetalleEeff.setRevPeriodo3(this);
        return revRelacionDetalleEeff;
    }

    public RevRelacionDetalleEeff removeRevRelacionDetalleEeff(RevRelacionDetalleEeff revRelacionDetalleEeff) {
        getRevRelacionDetalleEeffList3().remove(revRelacionDetalleEeff);
        revRelacionDetalleEeff.setRevPeriodo3(null);
        return revRelacionDetalleEeff;
    }

    public List<RevVersionEeff> getRevVersionEeffList1() {
        return revVersionEeffList1;
    }

    public void setRevVersionEeffList1(List<RevVersionEeff> revVersionEeffList1) {
        this.revVersionEeffList1 = revVersionEeffList1;
    }

    public RevVersionEeff addRevVersionEeff(RevVersionEeff revVersionEeff) {
        getRevVersionEeffList1().add(revVersionEeff);
        revVersionEeff.setRevPeriodo4(this);
        return revVersionEeff;
    }

    public RevVersionEeff removeRevVersionEeff(RevVersionEeff revVersionEeff) {
        getRevVersionEeffList1().remove(revVersionEeff);
        revVersionEeff.setRevPeriodo4(null);
        return revVersionEeff;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idPeriodo=");
        buffer.append(getIdPeriodo());
        buffer.append(',');
        buffer.append("periodo=");
        buffer.append(getPeriodo());
        buffer.append(',');
        buffer.append(']');
        return buffer.toString();
    }
}
