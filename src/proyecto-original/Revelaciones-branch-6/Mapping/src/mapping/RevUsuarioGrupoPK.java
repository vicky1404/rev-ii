package mapping;

import java.io.Serializable;

public class RevUsuarioGrupoPK implements Serializable {
    private String idGrupoAcceso;
    private String usuarioOid;

    public RevUsuarioGrupoPK() {
    }

    public RevUsuarioGrupoPK(String idGrupoAcceso, String usuarioOid) {
        this.idGrupoAcceso = idGrupoAcceso;
        this.usuarioOid = usuarioOid;
    }

    public boolean equals(Object other) {
        if (other instanceof RevUsuarioGrupoPK) {
            final RevUsuarioGrupoPK otherRevUsuarioGrupoPK = (RevUsuarioGrupoPK)other;
            final boolean areEqual =
                (otherRevUsuarioGrupoPK.idGrupoAcceso.equals(idGrupoAcceso) && otherRevUsuarioGrupoPK.usuarioOid.equals(usuarioOid));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
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
}
