package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "RevTipoOperacion.findAll", query = "select o from RevTipoOperacion o") })
@Table(name = "REV_TIPO_OPERACION")
public class RevTipoOperacion implements Serializable {
    @Id
    @Column(name = "ID_TIPO_OPERACION", nullable = false)
    private BigDecimal idTipoOperacion;
    @Column(nullable = false, length = 128)
    private String nombre;

    public RevTipoOperacion() {
    }

    public RevTipoOperacion(BigDecimal idTipoOperacion, String nombre) {
        this.idTipoOperacion = idTipoOperacion;
        this.nombre = nombre;
    }


    public BigDecimal getIdTipoOperacion() {
        return idTipoOperacion;
    }

    public void setIdTipoOperacion(BigDecimal idTipoOperacion) {
        this.idTipoOperacion = idTipoOperacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idTipoOperacion=");
        buffer.append(getIdTipoOperacion());
        buffer.append(',');
        buffer.append("nombre=");
        buffer.append(getNombre());
        buffer.append(']');
        return buffer.toString();
    }
}
