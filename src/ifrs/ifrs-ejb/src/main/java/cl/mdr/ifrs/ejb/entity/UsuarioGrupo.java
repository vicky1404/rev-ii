package cl.mdr.ifrs.ejb.entity;


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

import cl.mdr.ifrs.ejb.common.Constantes;
import cl.mdr.ifrs.ejb.entity.pk.UsuarioGrupoPK;


@Entity
@NamedQueries({
    @NamedQuery(name = UsuarioGrupo.FIND_ALL , query = "select o from UsuarioGrupo o"),
    @NamedQuery(name = UsuarioGrupo.FIND_USUARIOS_DISTINCT_ALL , query = "select distinct(o.usuarioOid) from UsuarioGrupo o order by o.usuarioOid"),
    @NamedQuery(name = UsuarioGrupo.FIND_GRUPOS_BY_USUARIO , query = "select o.grupo from UsuarioGrupo o where o.usuarioOid =:usuarioOid"),
    @NamedQuery(name = UsuarioGrupo.DELETE_BY_USUARIO , query = "delete from UsuarioGrupo o where upper(o.usuarioOid) =:usuarioOid")
})
@Table(name = Constantes.USUARIO_GRUPO)
@IdClass(UsuarioGrupoPK.class)
public class UsuarioGrupo implements Serializable {
    private static final long serialVersionUID = 6253639174056647085L;
    
    public static final String FIND_ALL = "UsuarioGrupo.findAll";
    public static final String FIND_USUARIOS_DISTINCT_ALL = "UsuarioGrupo.findUsuariosDistinctAll";
    public static final String FIND_GRUPOS_BY_USUARIO = "UsuarioGrupo.findGruposByUsuario";
    public static final String DELETE_BY_USUARIO = "UsuarioGrupo.deleteByUsuario";
    
    @Id
    @Column(name="ID_GRUPO_ACCESO",nullable = false,insertable = false,updatable = false)
    private String idGrupo;
    
    @Id
    @Column(name="USUARIO_OID", nullable = false, length = 256)
    private String usuarioOid;
    
    @ManyToOne
    @JoinColumn(name = "ID_GRUPO_ACCESO")
    private Grupo grupo; 
 
 
    public UsuarioGrupo() {
    }

    public UsuarioGrupo(String idGrupo, String usuarioOid, Grupo grupo) {
        super();
        this.idGrupo = idGrupo;
        this.usuarioOid = usuarioOid;
        this.grupo = grupo;
    }

    public String getUsuarioOid() {
        return usuarioOid;
    }

    public void setUsuarioOid(String usuarioOid) {
        this.usuarioOid = usuarioOid;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public void setIdGrupo(String idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getIdGrupo() {
        return idGrupo;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');                
        buffer.append("usuarioOid=");
        buffer.append(getUsuarioOid());
        buffer.append(']');
        return buffer.toString();
    }
}
