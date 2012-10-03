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
@NamedQueries( { @NamedQuery(name = "RevEstadoCuadro.findAll", query = "select o from RevEstadoCuadro o") })
@Table(name = "REV_ESTADO_CUADRO")
public class RevEstadoCuadro implements Serializable {
    @Id
    @Column(name = "ID_ESTADO_CUADRO", nullable = false)
    private BigDecimal idEstadoCuadro;
    @Column(nullable = false, length = 128)
    private String nombre;
    @OneToMany(mappedBy = "revEstadoCuadro")
    private List<RevHistorialVersion> revHistorialVersionList;
    @OneToMany(mappedBy = "revEstadoCuadro1")
    private List<RevVersionPeriodo> revVersionPeriodoList1;

    public RevEstadoCuadro() {
    }

    public RevEstadoCuadro(BigDecimal idEstadoCuadro, String nombre) {
        this.idEstadoCuadro = idEstadoCuadro;
        this.nombre = nombre;
    }


    public BigDecimal getIdEstadoCuadro() {
        return idEstadoCuadro;
    }

    public void setIdEstadoCuadro(BigDecimal idEstadoCuadro) {
        this.idEstadoCuadro = idEstadoCuadro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<RevHistorialVersion> getRevHistorialVersionList() {
        return revHistorialVersionList;
    }

    public void setRevHistorialVersionList(List<RevHistorialVersion> revHistorialVersionList) {
        this.revHistorialVersionList = revHistorialVersionList;
    }

    public RevHistorialVersion addRevHistorialVersion(RevHistorialVersion revHistorialVersion) {
        getRevHistorialVersionList().add(revHistorialVersion);
        revHistorialVersion.setRevEstadoCuadro(this);
        return revHistorialVersion;
    }

    public RevHistorialVersion removeRevHistorialVersion(RevHistorialVersion revHistorialVersion) {
        getRevHistorialVersionList().remove(revHistorialVersion);
        revHistorialVersion.setRevEstadoCuadro(null);
        return revHistorialVersion;
    }

    public List<RevVersionPeriodo> getRevVersionPeriodoList1() {
        return revVersionPeriodoList1;
    }

    public void setRevVersionPeriodoList1(List<RevVersionPeriodo> revVersionPeriodoList1) {
        this.revVersionPeriodoList1 = revVersionPeriodoList1;
    }

    public RevVersionPeriodo addRevVersionPeriodo(RevVersionPeriodo revVersionPeriodo) {
        getRevVersionPeriodoList1().add(revVersionPeriodo);
        revVersionPeriodo.setRevEstadoCuadro1(this);
        return revVersionPeriodo;
    }

    public RevVersionPeriodo removeRevVersionPeriodo(RevVersionPeriodo revVersionPeriodo) {
        getRevVersionPeriodoList1().remove(revVersionPeriodo);
        revVersionPeriodo.setRevEstadoCuadro1(null);
        return revVersionPeriodo;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idEstadoCuadro=");
        buffer.append(getIdEstadoCuadro());
        buffer.append(',');
        buffer.append("nombre=");
        buffer.append(getNombre());
        buffer.append(',');
        buffer.append(']');
        return buffer.toString();
    }
}
