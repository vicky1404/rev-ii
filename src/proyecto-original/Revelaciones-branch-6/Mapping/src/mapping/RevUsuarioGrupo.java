package mapping;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "RevUsuarioGrupo.findAll", query = "select o from RevUsuarioGrupo o") })
@Table(name = "REV_USUARIO_GRUPO")
@IdClass(RevUsuarioGrupoPK.class)
public class RevUsuarioGrupo implements Serializable {
    @Id
    @Column(name = "ID_GRUPO_ACCESO", nullable = false, length = 128, insertable = false, updatable = false)
    private String idGrupoAcceso;
    @Id
    @Column(name = "USUARIO_OID", nullable = false, length = 256)
    private String usuarioOid;
    @ManyToOne
    @JoinColumn(name = "ID_GRUPO_ACCESO")
    private RevGrupo revGrupo2;

    public RevUsuarioGrupo() {
    }

    public RevUsuarioGrupo(RevGrupo revGrupo2, String usuarioOid) {
        this.revGrupo2 = revGrupo2;
        this.usuarioOid = usuarioOid;
    }


    public String getIdGrupoAcceso() {
        return idGrupoAcceso;
    }

    public void setIdGrupoAcceso(String idGrupoAcceso) {
        this.idGrupoAcceso = idGrupoAcceso;
    }

    public String getUsuarioOid() {
        return usuarioOid;
    }

    public void setUsuarioOid(String usuarioOid) {
        this.usuarioOid = usuarioOid;
    }

    public RevGrupo getRevGrupo2() {
        return revGrupo2;
    }

    public void setRevGrupo2(RevGrupo revGrupo2) {
        this.revGrupo2 = revGrupo2;
        if (revGrupo2 != null) {
            this.idGrupoAcceso = revGrupo2.getIdGrupoAcceso();
        }
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idGrupoAcceso=");
        buffer.append(getIdGrupoAcceso());
        buffer.append(',');
        buffer.append("usuarioOid=");
        buffer.append(getUsuarioOid());
        buffer.append(']');
        return buffer.toString();
    }
}
