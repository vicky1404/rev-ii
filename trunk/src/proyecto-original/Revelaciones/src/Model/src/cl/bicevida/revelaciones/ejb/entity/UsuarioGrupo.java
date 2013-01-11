package cl.bicevida.revelaciones.ejb.entity;


import cl.bicevida.revelaciones.ejb.common.Constantes;
import cl.bicevida.revelaciones.ejb.cross.EeffUtil;
import cl.bicevida.revelaciones.ejb.entity.pk.UsuarioGrupoPK;

import java.io.Serializable;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@NamedQueries({
    @NamedQuery(name = UsuarioGrupo.FIND_ALL , query = "select o from UsuarioGrupo o"),
    @NamedQuery(name = UsuarioGrupo.FIND_USUARIOS_DISTINCT_ALL , query = "select distinct(o.usuarioOid) from UsuarioGrupo o order by o.usuarioOid"),
    @NamedQuery(name = UsuarioGrupo.FIND_GRUPOS_BY_USUARIO , query = "select o.grupo from UsuarioGrupo o where o.usuarioOid =:usuarioOid"),
    @NamedQuery(name = UsuarioGrupo.DELETE_BY_USUARIO , query = "delete from UsuarioGrupo o where upper(o.usuarioOid) =:usuarioOid"),
    @NamedQuery(name = UsuarioGrupo.FIND_BY_ID_CATALOGO , query = "select distinct new cl.bicevida.revelaciones.ejb.entity.UsuarioGrupo(ug.usuarioOid) from CatalogoGrupo cg, Grupo g, UsuarioGrupo ug where cg.idCatalogo = :idCatalogo and g.idGrupo = cg.idGrupoAcceso and ug.idGrupo = g.idGrupo order by ug.usuarioOid")
    //@NamedQuery(name = UsuarioGrupo.FIND_BY_ID_CATALOGO , query = "select o from UsuarioGrupo o where o.usuarioOid in ( select distinct ug.usuarioOid from CatalogoGrupo cg, Grupo g, UsuarioGrupo ug where cg.idCatalogo = :idCatalogo and g.idGrupo = cg.idGrupoAcceso and ug.idGrupo = g.idGrupo )  order by o.usuarioOid")
    //@NamedQuery(name = UsuarioGrupo.FIND_BY_ID_CATALOGO , query = "select distinct ug.usuarioOid, ug.email from CatalogoGrupo cg, Grupo g, UsuarioGrupo ug where cg.idCatalogo = :idCatalogo and g.idGrupo = cg.idGrupoAcceso and ug.idGrupo = g.idGrupo order by ug.usuarioOid")
})
@Table(name = Constantes.REV_USUARIO_GRUPO)
@IdClass(UsuarioGrupoPK.class)
public class UsuarioGrupo implements Serializable {
    @SuppressWarnings("compatibility:2990813962204104457")
    private static final long serialVersionUID = 6253639174056647085L;
    
    public static final String FIND_ALL = "UsuarioGrupo.findAll";
    public static final String FIND_USUARIOS_DISTINCT_ALL = "UsuarioGrupo.findUsuariosDistinctAll";
    public static final String FIND_GRUPOS_BY_USUARIO = "UsuarioGrupo.findGruposByUsuario";
    public static final String DELETE_BY_USUARIO = "UsuarioGrupo.deleteByUsuario";
    public static final String FIND_BY_ID_CATALOGO = "UsuarioGrupo.findByIdCatalogo";
    
    @Id
    @Column(name="ID_GRUPO_ACCESO",nullable = false,insertable = false,updatable = false)
    private String idGrupo;
    
    @Id
    @Column(name="USUARIO_OID", nullable = false, length = 256)
    private String usuarioOid;
    
    @ManyToOne
    @JoinColumn(name = "ID_GRUPO_ACCESO")
    private Grupo grupo;
    
    @Transient
    private List<Catalogo> catalogoAsociadoList;
    
    @Transient
    private StringBuilder contenidoMail;
    
    @Transient
    private String email;
 
    public UsuarioGrupo() {
    }

    public UsuarioGrupo(String idGrupo, String usuarioOid, Grupo grupo) {
        super();
        this.idGrupo = idGrupo;
        this.usuarioOid = usuarioOid;
        this.grupo = grupo;
    }
    
    public UsuarioGrupo(String usuarioOid) {
        this.usuarioOid = usuarioOid;
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

    public void setContenidoMail(StringBuilder contenidoMail) {
        this.contenidoMail = contenidoMail;
    }

    public StringBuilder getContenidoMail() {
        return contenidoMail;
    }

    public void setCatalogoAsociadoList(List<Catalogo> catalogoAsociadoList) {
        this.catalogoAsociadoList = catalogoAsociadoList;
    }

    public List<Catalogo> getCatalogoAsociadoList() {
        return catalogoAsociadoList;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
