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
@NamedQueries( { @NamedQuery(name = "RevGrupoOid.findAll", query = "select o from RevGrupoOid o") })
@Table(name = "REV_GRUPO_OID")
public class RevGrupoOid implements Serializable {
    @Id
    @Column(name = "ID_GRUPO_OID", nullable = false, length = 128)
    private String idGrupoOid;
    @Column(length = 512)
    private String nombre;
    @OneToMany(mappedBy = "revGrupoOid")
    private List<RevGrupo> revGrupoList;

    public RevGrupoOid() {
    }

    public RevGrupoOid(String idGrupoOid, String nombre) {
        this.idGrupoOid = idGrupoOid;
        this.nombre = nombre;
    }


    public String getIdGrupoOid() {
        return idGrupoOid;
    }

    public void setIdGrupoOid(String idGrupoOid) {
        this.idGrupoOid = idGrupoOid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<RevGrupo> getRevGrupoList() {
        return revGrupoList;
    }

    public void setRevGrupoList(List<RevGrupo> revGrupoList) {
        this.revGrupoList = revGrupoList;
    }

    public RevGrupo addRevGrupo(RevGrupo revGrupo) {
        getRevGrupoList().add(revGrupo);
        revGrupo.setRevGrupoOid(this);
        return revGrupo;
    }

    public RevGrupo removeRevGrupo(RevGrupo revGrupo) {
        getRevGrupoList().remove(revGrupo);
        revGrupo.setRevGrupoOid(null);
        return revGrupo;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idGrupoOid=");
        buffer.append(getIdGrupoOid());
        buffer.append(',');
        buffer.append("nombre=");
        buffer.append(getNombre());
        buffer.append(',');
        buffer.append(']');
        return buffer.toString();
    }
}
