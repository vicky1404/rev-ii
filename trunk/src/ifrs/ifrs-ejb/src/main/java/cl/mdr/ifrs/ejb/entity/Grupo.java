package cl.mdr.ifrs.ejb.entity;


import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
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
    @NamedQuery(name = Grupo.FIND_ALL , query = "select o from Grupo o order by o.areaNegocio.nombre asc, o.nombre asc") 
})
@Table(name="IFRS_GRUPO")
public class Grupo implements Serializable {
	private static final long serialVersionUID = 73024457862453354L;

	public static final String FIND_ALL = "Grupo.findAll";

	@Id
	@Column(name="ID_GRUPO_ACCESO")
	private String idGrupoAcceso;

	@Column(name="ACCESO_BLOQUEADO")
	private Long accesoBloqueado;

	private String nombre;

	//bi-directional many-to-many association to Catalogo
    @ManyToMany
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
    @ManyToMany
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
	@ManyToMany(mappedBy="grupoList")
	private List<Menu> menus;

	//bi-directional many-to-many association to Usuario
	@ManyToMany(mappedBy="grupos")
	private List<Usuario> usuarios;

    public Grupo() {
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
