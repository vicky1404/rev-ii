package cl.mdr.ifrs.ejb.dummy.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the IFRS_AREA_NEGOCIO database table.
 * 
 */
@Entity
@Table(name="IFRS_AREA_NEGOCIO")
public class AreaNegocio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_AREA_NEGOCIO")
	private String idAreaNegocio;

	private String nombre;

	//bi-directional many-to-one association to Grupo
	@OneToMany(mappedBy="areaNegocio")
	private List<Grupo> grupos;

    public AreaNegocio() {
    }

	public String getIdAreaNegocio() {
		return idAreaNegocio;
	}

	public void setIdAreaNegocio(String idAreaNegocio) {
		this.idAreaNegocio = idAreaNegocio;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}

	
	
}