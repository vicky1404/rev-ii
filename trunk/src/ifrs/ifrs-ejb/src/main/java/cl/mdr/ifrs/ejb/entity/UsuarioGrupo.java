package cl.mdr.ifrs.ejb.entity;


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

import cl.mdr.ifrs.ejb.common.Constantes;
import cl.mdr.ifrs.ejb.entity.pk.UsuarioGrupoPK;


@Entity
@NamedQueries({
    @NamedQuery(name = UsuarioGrupo.FIND_ALL , query = "select o from UsuarioGrupo o"),
    @NamedQuery(name = UsuarioGrupo.FIND_USUARIOS_DISTINCT_ALL , query = "select distinct(o.nombreUsuario) from UsuarioGrupo o order by o.nombreUsuario"),
    @NamedQuery(name = UsuarioGrupo.FIND_GRUPOS_BY_USUARIO , query = "select o.grupo from UsuarioGrupo o where o.nombreUsuario =:nombreUsuario"),
    @NamedQuery(name = UsuarioGrupo.DELETE_BY_USUARIO , query = "delete from UsuarioGrupo o where upper(o.nombreUsuario) =:usuario"),
    @NamedQuery(name = UsuarioGrupo.DELETE_BY_GRUPO , query = "delete from UsuarioGrupo o where o.grupo =:grupo"),
    @NamedQuery(name = UsuarioGrupo.FIND_BY_ID_CATALOGO , query = "select distinct new cl.mdr.ifrs.ejb.entity.UsuarioGrupo(ug.nombreUsuario) from CatalogoGrupo cg, Grupo g, UsuarioGrupo ug where cg.idCatalogo = :idCatalogo and g.idGrupoAcceso = cg.idGrupoAcceso and ug.idGrupo = g.idGrupoAcceso order by ug.nombreUsuario")
})
@Table(name = Constantes.USUARIO_GRUPO)
@IdClass(UsuarioGrupoPK.class)
public class UsuarioGrupo implements Serializable {
    private static final long serialVersionUID = 6253639174056647085L;
    
    public static final String FIND_ALL = "UsuarioGrupo.findAll";
    public static final String FIND_USUARIOS_DISTINCT_ALL = "UsuarioGrupo.findUsuariosDistinctAll";
    public static final String FIND_GRUPOS_BY_USUARIO = "UsuarioGrupo.findGruposByUsuario";
    public static final String DELETE_BY_USUARIO = "UsuarioGrupo.deleteByUsuario";
    public static final String DELETE_BY_GRUPO = "UsuarioGrupo.deleteByGrupo";
    public static final String FIND_BY_ID_CATALOGO = "UsuarioGrupo.findByIdCatalogo";
    
    @Id
    @Column(name="ID_GRUPO_ACCESO",nullable = false, insertable = false,updatable = false)
    private String idGrupo;
    
    @Id
    @Column(name="NOMBRE_USUARIO", nullable = false, insertable = false,updatable = false)
    private String nombreUsuario;
    
    @ManyToOne
    @JoinColumn(name = "ID_GRUPO_ACCESO")
    private Grupo grupo; 
    
    @ManyToOne
    @JoinColumn(name = "NOMBRE_USUARIO")
    private Usuario usuario; 
    
    @Transient
    private List<Catalogo> catalogoAsociadoList;
    
    @Transient
    private StringBuilder contenidoMail;
    
    
 
    public List<Catalogo> getCatalogoAsociadoList() {
		return catalogoAsociadoList;
	}


	public void setCatalogoAsociadoList(List<Catalogo> catalogoAsociadoList) {
		this.catalogoAsociadoList = catalogoAsociadoList;
	}

 
    public StringBuilder getContenidoMail() {
		return contenidoMail;
	}


	public void setContenidoMail(StringBuilder contenidoMail) {
		this.contenidoMail = contenidoMail;
	}


	public UsuarioGrupo() {
    }

    
    public UsuarioGrupo(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }


	public String getIdGrupo() {
		return idGrupo;
	}



	public void setIdGrupo(String idGrupo) {
		this.idGrupo = idGrupo;
	}



	public String getNombreUsuario() {
		return nombreUsuario;
	}



	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}



	public Grupo getGrupo() {
		return grupo;
	}



	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}



	public Usuario getUsuario() {
		return usuario;
	}



	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

    
}
