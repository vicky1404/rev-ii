package cl.mdr.ifrs.ejb.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import cl.mdr.ifrs.ejb.common.Constantes;

import com.google.gson.annotations.Expose;


/**
 * The persistent class for the IFRS_EMPRESA database table.
 * 
 */
@Entity
@NamedQueries({ 
			@NamedQuery(name = Empresa.EMPRESA_FIND_ALL,  query = " select new cl.mdr.ifrs.ejb.entity.Empresa(o.idRut, o.dv, o.giro, o.nombre, o.razonSocial) " +
																  " from Empresa o order by o.razonSocial"),
			@NamedQuery(name = Empresa.EMPRESA_FIND_BY_ID, query = " select e " +
					  											   " from Empresa e left join fetch e.grupos where e.idRut = :rut" +
					  											   " order by e.razonSocial")
})
@Table(name=Constantes.EMPRESA)
public class Empresa implements Serializable {
	
	

	public static final String EMPRESA_FIND_ALL = "Empresa.findAll";
	public static final String EMPRESA_FIND_BY_ID = "Empresa.findById";
	
	private static final long serialVersionUID = 2904448285907525542L;

	@Id
	@Column(name = "ID_RUT")
	@Expose
	private Long idRut;

	@Expose	
	private String dv;

	@Expose
	private String giro;

	@Expose
	private String nombre;

	
	@Column(name="RAZON_SOCIAL")
	@Expose
	private String razonSocial;

	@OneToMany(mappedBy="empresa")	
	private List<Catalogo> catalogos;
	
		
	@ManyToMany(mappedBy="empresas")
	private List<Grupo> grupos;

    public Empresa() {
    }
    
    public Empresa(Long idRut) {
    	super();
    	this.idRut = idRut;
    }

	public Empresa(Long idRut, String dv, String giro, String nombre, String razonSocial) {
		super();
		this.idRut = idRut;
		this.dv = dv;
		this.giro = giro;
		this.nombre = nombre;
		this.razonSocial = razonSocial;
	}

	public Long getIdRut() {
		return idRut;
	}

	public void setIdRut(Long idRut) {
		this.idRut = idRut;
	}

	public String getDv() {
		return dv;
	}

	public void setDv(String dv) {
		this.dv = dv;
	}

	public String getGiro() {
		return giro;
	}

	public void setGiro(String giro) {
		this.giro = giro;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public List<Catalogo> getCatalogos() {
		return catalogos;
	}

	public void setCatalogos(List<Catalogo> catalogos) {
		this.catalogos = catalogos;
	}

	public List<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}

    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idRut == null) ? 0 : idRut.hashCode());
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
		Empresa other = (Empresa) obj;
		if (idRut == null) {
			if (other.idRut != null)
				return false;
		} else if (!idRut.equals(other.idRut))
			return false;
		return true;
	}
	
}