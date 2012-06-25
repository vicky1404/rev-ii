package cl.mdr.ifrs.ejb.dummy.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the IFRS_PERIODO database table.
 * 
 */
@Entity
@Table(name="IFRS_PERIODO")
public class Periodo implements Serializable {	
	private static final long serialVersionUID = 6745837738835804567L;

	@Id
	@Column(name="ID_PERIODO")
	private Long idPeriodo;

	private Long periodo;

	//bi-directional many-to-one association to HistorialReporte
	@OneToMany(mappedBy="ifrsPeriodo")
	private List<HistorialReporte> historialReportes;

	//bi-directional many-to-one association to EstadoPeriodo
    @ManyToOne
	@JoinColumn(name="ID_ESTADO_PERIODO")
	private EstadoPeriodo estadoPeriodo;

	//bi-directional many-to-one association to RelacionDetalleEeff
	@OneToMany(mappedBy="periodo")
	private List<RelacionDetalleEeff> relacionDetalleEeffs;

	//bi-directional many-to-one association to RelacionEeff
	@OneToMany(mappedBy="periodo")
	private List<RelacionEeff> relacionEeffs;

	//bi-directional many-to-one association to Version
	@OneToMany(mappedBy="periodo")
	private List<Version> versiones;

	//bi-directional many-to-one association to VersionEeff
	@OneToMany(mappedBy="ifrsPeriodo")
	private List<VersionEeff> versionEeffs;

    public Periodo() {
    }

	public Long getIdPeriodo() {
		return idPeriodo;
	}

	public void setIdPeriodo(Long idPeriodo) {
		this.idPeriodo = idPeriodo;
	}

	public Long getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Long periodo) {
		this.periodo = periodo;
	}

	public List<HistorialReporte> getHistorialReportes() {
		return historialReportes;
	}

	public void setHistorialReportes(List<HistorialReporte> historialReportes) {
		this.historialReportes = historialReportes;
	}

	public EstadoPeriodo getEstadoPeriodo() {
		return estadoPeriodo;
	}

	public void setEstadoPeriodo(EstadoPeriodo estadoPeriodo) {
		this.estadoPeriodo = estadoPeriodo;
	}

	public List<RelacionDetalleEeff> getRelacionDetalleEeffs() {
		return relacionDetalleEeffs;
	}

	public void setRelacionDetalleEeffs(
			List<RelacionDetalleEeff> relacionDetalleEeffs) {
		this.relacionDetalleEeffs = relacionDetalleEeffs;
	}

	public List<RelacionEeff> getRelacionEeffs() {
		return relacionEeffs;
	}

	public void setRelacionEeffs(List<RelacionEeff> relacionEeffs) {
		this.relacionEeffs = relacionEeffs;
	}

	public List<Version> getVersiones() {
		return versiones;
	}

	public void setVersiones(List<Version> versiones) {
		this.versiones = versiones;
	}

	public List<VersionEeff> getVersionEeffs() {
		return versionEeffs;
	}

	public void setVersionEeffs(List<VersionEeff> versionEeffs) {
		this.versionEeffs = versionEeffs;
	}

	
	
}