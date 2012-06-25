package cl.mdr.ifrs.ejb.dummy.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the IFRS_TIPO_ESTADO_EEFF database table.
 * 
 */
@Entity
@Table(name="IFRS_TIPO_ESTADO_EEFF")
public class TipoEstadoEeff implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_ESTADO_EEFF")
	private long idEstadoEeff;

	private String nombre;

	private BigDecimal vigente;

	//bi-directional many-to-one association to VersionEeff
	@OneToMany(mappedBy="ifrsTipoEstadoEeff")
	private List<VersionEeff> ifrsVersionEeffs;

    public TipoEstadoEeff() {
    }

	public long getIdEstadoEeff() {
		return this.idEstadoEeff;
	}

	public void setIdEstadoEeff(long idEstadoEeff) {
		this.idEstadoEeff = idEstadoEeff;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public BigDecimal getVigente() {
		return this.vigente;
	}

	public void setVigente(BigDecimal vigente) {
		this.vigente = vigente;
	}

	public List<VersionEeff> getIfrsVersionEeffs() {
		return this.ifrsVersionEeffs;
	}

	public void setIfrsVersionEeffs(List<VersionEeff> ifrsVersionEeffs) {
		this.ifrsVersionEeffs = ifrsVersionEeffs;
	}
	
}