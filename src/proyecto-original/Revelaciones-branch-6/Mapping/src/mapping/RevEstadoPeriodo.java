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
@NamedQueries( { @NamedQuery(name = "RevEstadoPeriodo.findAll", query = "select o from RevEstadoPeriodo o") })
@Table(name = "REV_ESTADO_PERIODO")
public class RevEstadoPeriodo implements Serializable {
    @Id
    @Column(name = "ID_ESTADO_PERIODO", nullable = false)
    private BigDecimal idEstadoPeriodo;
    @Column(nullable = false, length = 128)
    private String nombre;
    @OneToMany(mappedBy = "revEstadoPeriodo")
    private List<RevPeriodo> revPeriodoList;

    public RevEstadoPeriodo() {
    }

    public RevEstadoPeriodo(BigDecimal idEstadoPeriodo, String nombre) {
        this.idEstadoPeriodo = idEstadoPeriodo;
        this.nombre = nombre;
    }


    public BigDecimal getIdEstadoPeriodo() {
        return idEstadoPeriodo;
    }

    public void setIdEstadoPeriodo(BigDecimal idEstadoPeriodo) {
        this.idEstadoPeriodo = idEstadoPeriodo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<RevPeriodo> getRevPeriodoList() {
        return revPeriodoList;
    }

    public void setRevPeriodoList(List<RevPeriodo> revPeriodoList) {
        this.revPeriodoList = revPeriodoList;
    }

    public RevPeriodo addRevPeriodo(RevPeriodo revPeriodo) {
        getRevPeriodoList().add(revPeriodo);
        revPeriodo.setRevEstadoPeriodo(this);
        return revPeriodo;
    }

    public RevPeriodo removeRevPeriodo(RevPeriodo revPeriodo) {
        getRevPeriodoList().remove(revPeriodo);
        revPeriodo.setRevEstadoPeriodo(null);
        return revPeriodo;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idEstadoPeriodo=");
        buffer.append(getIdEstadoPeriodo());
        buffer.append(',');
        buffer.append("nombre=");
        buffer.append(getNombre());
        buffer.append(',');
        buffer.append(']');
        return buffer.toString();
    }
}
