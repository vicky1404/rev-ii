package cl.mdr.ifrs.ejb.entity.pk;

import java.io.Serializable;

public class UsuarioGrupoPK implements Serializable{
    @SuppressWarnings("compatibility:3897190556614757237")
    private static final long serialVersionUID = -7917216190081291215L;
    public String idGrupo;
    public String usuarioOid;

    public UsuarioGrupoPK(){
    }

    public UsuarioGrupoPK(String idGrupo,String usuarioOid){
        this.idGrupo = idGrupo;
        this.usuarioOid = usuarioOid;
    }

    public boolean equals(Object other){
        if(other instanceof UsuarioGrupoPK){
            final UsuarioGrupoPK otherUsuarioGrupoPK=(UsuarioGrupoPK) other;
            final boolean areEqual=
                (otherUsuarioGrupoPK.idGrupo.equals(idGrupo) && otherUsuarioGrupoPK.usuarioOid.equals(usuarioOid));
            return areEqual;
        }
        return false;
    }

    public int hashCode(){
        return super.hashCode();
    }
}
