package cl.mdr.ifrs.ejb.entity;


import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the IFRS_GRUPO database table.
 * 
 */
@Entity
@NamedQueries( { 
    @NamedQuery(name = Grupo.FIND_ALL , query = " select new cl.mdr.ifrs.ejb.entity.Grupo(o.idGrupoAcceso, o.nombre, o.accesoBloqueado, o.areaNegocio) " +
    											" from Grupo o order by o.areaNegocio asc, o.nombre asc"),
	@NamedQuery(name = Grupo.FIND_BY_ID , query = " select new cl.mdr.ifrs.ejb.entity.Grupo(o.idGrupoAcceso, o.nombre, o.accesoBloqueado) " +
												  " from Grupo o where o.idGrupoAcceso =:idGrupoAcceso")
})
@Table(name="IFRS_GRUPO")
public class Grupo implements Serializable {
	private static final long serialVersionUID = 73024457862453354L;

	public static final String FIND_ALL = "Grupo.findAll";
	public static final String FIND_BY_ID = "Grupo.findById";

	@Id
	@Column(name="ID_GRUPO_ACCESO")
	private String idGrupoAcceso;

	@Column(name="ACCESO_BLOQUEADO")
	private Long accesoBloqueado;

	private String nombre;

	//bi-directional many-to-many association to Catalogo
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name="IFRS_CATALOGO_GRUPO"
		, joinColumns={
			@JoinColumn(name="ID_GRUPO_ACCESO")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_CATALOGO")
			}
		)
	private List<Catalogo> catalogos;

	//bi-directional many-to-one association to AreaNegocio    
    @ManyToOne
	@JoinColumn(name="ID_AREA_NEGOCIO")
	private AreaNegocio areaNegocio;

	//bi-directional many-to-many association to Empresa
    @ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name="IFRS_GRUPO_EMPRESA"
		, joinColumns={
			@JoinColumn(name="ID_GRUPO_ACCESO")
			}
		, inverseJoinColumns={
			@JoinColumn(name="RUT")
			}
		)
	private List<Empresa> empresas;

	//bi-directional many-to-many association to Menu     
    @ManyToMany(fetch = FetchType.LAZY)    
	@JoinTable(
		name="IFRS_MENU_GRUPO"
		, joinColumns={
			@JoinColumn(name="ID_GRUPO_ACCESO")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_MENU")
			}
		)
	private List<Menu> menus;

	//bi-directional many-to-many association to Usuario
	@ManyToMany(mappedBy="grupos", fetch = FetchType.LAZY)
	private List<Usuario> usuarios;

    
	public Grupo() {
    }
	
	

	public Grupo(String idGrupoAcceso) {
		super();
		this.idGrupoAcceso = idGrupoAcceso;
	}
	
	



	public Grupo(String idGrupoAcceso, String nombre, Long accesoBloqueado) {
		super();
		this.idGrupoAcceso = idGrupoAcceso;
		this.nombre = nombre;
		this.accesoBloqueado = accesoBloqueado;		
	}
	
	



	public Grupo(String idGrupoAcceso, String nombre, Long accesoBloqueado, AreaNegocio areaNegocio) {
		super();
		this.idGrupoAcceso = idGrupoAcceso;
		this.nombre = nombre;
		this.accesoBloqueado = accesoBloqueado;	
		this.areaNegocio = areaNegocio;
	}



	public String getIdGrupoAcceso() {
		return idGrupoAcceso;
	}

	public void setIdGrupoAcceso(String idGrupoAcceso) {
		this.idGrupoAcceso = idGrupoAcceso;
	}

	public Long getAccesoBloqueado() {
		return accesoBloqueado;
	}

	public void setAccesoBloqueado(Long accesoBloqueado) {
		this.accesoBloqueado = accesoBloqueado;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Catalogo> getCatalogos() {
		return catalogos;
	}

	public void setCatalogos(List<Catalogo> catalogos) {
		this.catalogos = catalogos;
	}

	public AreaNegocio getAreaNegocio() {
		return areaNegocio;
	}

	public void setAreaNegocio(AreaNegocio areaNegocio) {
		this.areaNegocio = areaNegocio;
	}

	public List<Empresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}

	public List<Menu> getMenus() {
		return menus;
	}

	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	
	
}
