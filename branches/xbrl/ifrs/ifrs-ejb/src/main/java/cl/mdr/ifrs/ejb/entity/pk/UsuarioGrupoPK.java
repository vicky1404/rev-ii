package cl.mdr.ifrs.ejb.entity.pk;

import java.io.Serializable;

public class UsuarioGrupoPK implements Serializable{
    private static final long serialVersionUID = -7917216190081291215L;
    public String idGrupo;
    public String nombreUsuario;

    public UsuarioGrupoPK(){
    }

    public UsuarioGrupoPK(String idGrupo,String nombreUsuario){
        this.idGrupo = idGrupo;
        this.nombreUsuario = nombreUsuario;
    }

    public boolean equals(Object other){
        if(other instanceof UsuarioGrupoPK){
            final UsuarioGrupoPK otherUsuarioGrupoPK=(UsuarioGrupoPK) other;
            final boolean areEqual=
                (otherUsuarioGrupoPK.idGrupo.equals(idGrupo) && otherUsuarioGrupoPK.nombreUsuario.equals(nombreUsuario));
            return areEqual;
        }
        return false;
    }

    public int hashCode(){
        return super.hashCode();
    }
}
