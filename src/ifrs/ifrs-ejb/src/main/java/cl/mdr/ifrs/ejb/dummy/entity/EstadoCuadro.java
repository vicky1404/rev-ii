package cl.mdr.ifrs.ejb.dummy.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the IFRS_ESTADO_CUADRO database table.
 * 
 */
@Entity
@Table(name="IFRS_ESTADO_CUADRO")
public class EstadoCuadro implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_ESTADO_CUADRO")
	private long idEstadoCuadro;

	private String nombre;

	//bi-directional many-to-one association to HistorialVersion
	@OneToMany(mappedBy="ifrsEstadoCuadro")
	private List<HistorialVersion> ifrsHistorialVersions;

    public EstadoCuadro() {
    }

	public long getIdEstadoCuadro() {
		return this.idEstadoCuadro;
	}

	public void setIdEstadoCuadro(long idEstadoCuadro) {
		this.idEstadoCuadro = idEstadoCuadro;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<HistorialVersion> getIfrsHistorialVersions() {
		return this.ifrsHistorialVersions;
	}

	public void setIfrsHistorialVersions(List<HistorialVersion> ifrsHistorialVersions) {
		this.ifrsHistorialVersions = ifrsHistorialVersions;
	}
	
}