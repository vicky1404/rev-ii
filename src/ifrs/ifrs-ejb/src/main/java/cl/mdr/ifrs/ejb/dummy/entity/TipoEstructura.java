package cl.mdr.ifrs.ejb.dummy.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the IFRS_TIPO_ESTRUCTURA database table.
 * 
 */
@Entity
@Table(name="IFRS_TIPO_ESTRUCTURA")
public class TipoEstructura implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_TIPO_ESTRUCTURA")
	private Long idTipoEstructura;

	private String nombre;

	//bi-directional many-to-one association to Estructura
	@OneToMany(mappedBy="tipoEstructura")
	private List<Estructura> estructuras;

    public TipoEstructura() {
    }

	public Long getIdTipoEstructura() {
		return idTipoEstructura;
	}

	public void setIdTipoEstructura(Long idTipoEstructura) {
		this.idTipoEstructura = idTipoEstructura;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Estructura> getEstructuras() {
		return estructuras;
	}

	public void setEstructuras(List<Estructura> estructuras) {
		this.estructuras = estructuras;
	}

	
	
}