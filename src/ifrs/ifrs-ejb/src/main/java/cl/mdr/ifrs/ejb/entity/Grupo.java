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

import com.google.gson.annotations.Expose;


/**
 * The persistent class for the IFRS_GRUPO database table.
 * 
 */
@Entity
@NamedQueries( { 
    @NamedQuery(name = Grupo.FIND_ALL , query = " select new cl.mdr.ifrs.ejb.entity.Grupo(o.idGrupoAcceso, o.nombre, o.accesoBloqueado, o.areaNegocio) " +
    											" from Grupo o order by o.nombre asc"),
	@NamedQuery(name = Grupo.FIND_BY_ID , query = " select new cl.mdr.ifrs.ejb.entity.Grupo(o.idGrupoAcceso, o.nombre, o.accesoBloqueado) " +
												  " from Grupo o where o.idGrupoAcceso =:idGrupoAcceso"),
	@NamedQuery(name = Grupo.FIND_BY_AREA_NEGOCIO ,	query = " select g from Grupo g where g.areaNegocio.idAreaNegocio = :idAreaNegocio "),
														  		
    @NamedQuery(name = Grupo.FIND_BY_FILTRO ,query = " select o " +
    											  	 " from Grupo o  " +
    											  	 " where 1 = 1 " +    											  	 
    											  	 " and (o.areaNegocio.empresa.idRut =:rutEmpresa or o.areaNegocio.empresa.idRut is null or :rutEmpresa is null)" +
    											  	 " and (o.areaNegocio.idAreaNegocio =:areaNegocio or :areaNegocio is null) " +
    											  	 " order by o.nombre asc , o.areaNegocio.nombre asc")
})
@Table(name="IFRS_GRUPO")
public class Grupo implements Serializable {
	private static final long serialVersionUID = 73024457862453354L;

	public static final String FIND_ALL = "Grupo.findAll";
	public static final String FIND_BY_ID = "Grupo.findById";
	public static final String FIND_BY_FILTRO = "Grupo.findByFiltro";
	public static final String FIND_BY_AREA_NEGOCIO = "Grupo.findByAreaNegocio";

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
			@JoinColumn(name="ID_RUT")
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
	
	@Column(name = "VIGENTE")
	@Expose
    private Long vigente;

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



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((accesoBloqueado == null) ? 0 : accesoBloqueado.hashCode());
		result = prime * result
				+ ((areaNegocio == null) ? 0 : areaNegocio.hashCode());
		result = prime * result
				+ ((catalogos == null) ? 0 : catalogos.hashCode());
		result = prime * result
				+ ((empresas == null) ? 0 : empresas.hashCode());
		result = prime * result
				+ ((idGrupoAcceso == null) ? 0 : idGrupoAcceso.hashCode());
		result = prime * result + ((menus == null) ? 0 : menus.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result
				+ ((usuarios == null) ? 0 : usuarios.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Grupo other = (Grupo) obj;
		if (accesoBloqueado == null) {
			if (other.accesoBloqueado != null)
				return false;
		} else if (!accesoBloqueado.equals(other.accesoBloqueado))
			return false;
		if (areaNegocio == null) {
			if (other.areaNegocio != null)
				return false;
		} else if (!areaNegocio.equals(other.areaNegocio))
			return false;
		if (catalogos == null) {
			if (other.catalogos != null)
				return false;
		} else if (!catalogos.equals(other.catalogos))
			return false;
		if (empresas == null) {
			if (other.empresas != null)
				return false;
		} else if (!empresas.equals(other.empresas))
			return false;
		if (idGrupoAcceso == null) {
			if (other.idGrupoAcceso != null)
				return false;
		} else if (!idGrupoAcceso.equals(other.idGrupoAcceso))
			return false;
		if (menus == null) {
			if (other.menus != null)
				return false;
		} else if (!menus.equals(other.menus))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (usuarios == null) {
			if (other.usuarios != null)
				return false;
		} else if (!usuarios.equals(other.usuarios))
			return false;
		return true;
	}

	public Long getVigente() {
		return vigente;
	}

	public void setVigente(Long vigente) {
		this.vigente = vigente;
	}
	
	
	
	
}
