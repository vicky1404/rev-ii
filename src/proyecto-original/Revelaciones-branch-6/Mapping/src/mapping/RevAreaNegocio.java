package mapping;

import java.io.Serializable;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "RevAreaNegocio.findAll", query = "select o from RevAreaNegocio o") })
@Table(name = "REV_AREA_NEGOCIO")
public class RevAreaNegocio implements Serializable {
    @Id
    @Column(name = "ID_AREA_NEGOCIO", nullable = false, length = 3)
    private String idAreaNegocio;
    @Column(length = 256)
    private String nombre;
    @OneToMany(mappedBy = "revAreaNegocio")
    private List<RevGrupo> revGrupoList1;

    public RevAreaNegocio() {
    }

    public RevAreaNegocio(String idAreaNegocio, String nombre) {
        this.idAreaNegocio = idAreaNegocio;
        this.nombre = nombre;
    }


    public String getIdAreaNegocio() {
        return idAreaNegocio;
    }

    public void setIdAreaNegocio(String idAreaNegocio) {
        this.idAreaNegocio = idAreaNegocio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<RevGrupo> getRevGrupoList1() {
        return revGrupoList1;
    }

    public void setRevGrupoList1(List<RevGrupo> revGrupoList1) {
        this.revGrupoList1 = revGrupoList1;
    }

    public RevGrupo addRevGrupo(RevGrupo revGrupo) {
        getRevGrupoList1().add(revGrupo);
        revGrupo.setRevAreaNegocio(this);
        return revGrupo;
    }

    public RevGrupo removeRevGrupo(RevGrupo revGrupo) {
        getRevGrupoList1().remove(revGrupo);
        revGrupo.setRevAreaNegocio(null);
        return revGrupo;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idAreaNegocio=");
        buffer.append(getIdAreaNegocio());
        buffer.append(',');
        buffer.append("nombre=");
        buffer.append(getNombre());
        buffer.append(',');
        buffer.append(']');
        return buffer.toString();
    }
}
