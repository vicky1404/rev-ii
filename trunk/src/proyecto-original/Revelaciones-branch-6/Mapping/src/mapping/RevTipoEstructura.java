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
@NamedQueries( { @NamedQuery(name = "RevTipoEstructura.findAll", query = "select o from RevTipoEstructura o") })
@Table(name = "REV_TIPO_ESTRUCTURA")
public class RevTipoEstructura implements Serializable {
    @Id
    @Column(name = "ID_TIPO_ESTRUCTURA", nullable = false)
    private BigDecimal idTipoEstructura;
    @Column(nullable = false, length = 64)
    private String nombre;
    @OneToMany(mappedBy = "revTipoEstructura")
    private List<RevEstructura> revEstructuraList;

    public RevTipoEstructura() {
    }

    public RevTipoEstructura(BigDecimal idTipoEstructura, String nombre) {
        this.idTipoEstructura = idTipoEstructura;
        this.nombre = nombre;
    }


    public BigDecimal getIdTipoEstructura() {
        return idTipoEstructura;
    }

    public void setIdTipoEstructura(BigDecimal idTipoEstructura) {
        this.idTipoEstructura = idTipoEstructura;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<RevEstructura> getRevEstructuraList() {
        return revEstructuraList;
    }

    public void setRevEstructuraList(List<RevEstructura> revEstructuraList) {
        this.revEstructuraList = revEstructuraList;
    }

    public RevEstructura addRevEstructura(RevEstructura revEstructura) {
        getRevEstructuraList().add(revEstructura);
        revEstructura.setRevTipoEstructura(this);
        return revEstructura;
    }

    public RevEstructura removeRevEstructura(RevEstructura revEstructura) {
        getRevEstructuraList().remove(revEstructura);
        revEstructura.setRevTipoEstructura(null);
        return revEstructura;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idTipoEstructura=");
        buffer.append(getIdTipoEstructura());
        buffer.append(',');
        buffer.append("nombre=");
        buffer.append(getNombre());
        buffer.append(',');
        buffer.append(']');
        return buffer.toString();
    }
}
