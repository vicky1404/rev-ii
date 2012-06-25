package cl.mdr.ifrs.ejb.dummy.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the IFRS_CODIGO_FECU database table.
 * 
 */
@Entity
@Table(name="IFRS_CODIGO_FECU")
public class CodigoFecu implements Serializable {
	private static final long serialVersionUID = -1362406369117385093L;

	@Id
	@Column(name="ID_FECU")
	private Long idFecu;

	private String descripcion;

	private Long vigencia;

	//bi-directional many-to-one association to Eeff
	@OneToMany(mappedBy="ifrsCodigoFecu")
	private List<Eeff> ifrsEeffs;

	//bi-directional many-to-one association to RelacionEeff
	@OneToMany(mappedBy="codigoFecu")
	private List<RelacionEeff> relacionEeffs;

    public CodigoFecu() {
    }

	public Long getIdFecu() {
		return idFecu;
	}

	public void setIdFecu(Long idFecu) {
		this.idFecu = idFecu;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Long getVigencia() {
		return vigencia;
	}

	public void setVigencia(Long vigencia) {
		this.vigencia = vigencia;
	}

	public List<Eeff> getIfrsEeffs() {
		return ifrsEeffs;
	}

	public void setIfrsEeffs(List<Eeff> ifrsEeffs) {
		this.ifrsEeffs = ifrsEeffs;
	}

	public List<RelacionEeff> getRelacionEeffs() {
		return relacionEeffs;
	}

	public void setRelacionEeffs(List<RelacionEeff> relacionEeffs) {
		this.relacionEeffs = relacionEeffs;
	}

	
	
}