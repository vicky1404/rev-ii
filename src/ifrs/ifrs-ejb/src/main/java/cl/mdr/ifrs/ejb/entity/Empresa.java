package cl.mdr.ifrs.ejb.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import cl.mdr.ifrs.ejb.common.Constantes;


/**
 * The persistent class for the IFRS_EMPRESA database table.
 * 
 */
@Entity
@Table(name=Constantes.EMPRESA)
public class Empresa implements Serializable {
	private static final long serialVersionUID = 2904448285907525542L;

	@Id
	private Long rut;

	private String dv;

	private String giro;

	private String nombre;

	@Column(name="RAZON_SOCIAL")
	private String razonSocial;

	//bi-directional many-to-one association to Catalogo
	@OneToMany(mappedBy="empresa")
	private List<Catalogo> catalogos;

	//bi-directional many-to-many association to Grupo
	@ManyToMany(mappedBy="empresas")
	private List<Grupo> grupos;

    public Empresa() {
    }

	public Long getRut() {
		return rut;
	}

	public void setRut(Long rut) {
		this.rut = rut;
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

    
	
	
}