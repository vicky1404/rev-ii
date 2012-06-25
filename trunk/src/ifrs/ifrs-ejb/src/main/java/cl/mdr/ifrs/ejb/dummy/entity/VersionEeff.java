package cl.mdr.ifrs.ejb.dummy.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the IFRS_VERSION_EEFF database table.
 * 
 */
@Entity
@Table(name="IFRS_VERSION_EEFF")
public class VersionEeff implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_VERSION_EEFF")
	private long idVersionEeff;

    @Temporal( TemporalType.DATE)
	private Date fecha;

	private String usuario;

	@Column(name="\"VERSION\"")
	private BigDecimal version;

	private BigDecimal vigencia;

	//bi-directional many-to-one association to Eeff
	@OneToMany(mappedBy="ifrsVersionEeff")
	private List<Eeff> ifrsEeffs;

	//bi-directional many-to-one association to Periodo
    @ManyToOne
	@JoinColumn(name="ID_PERIODO")
	private Periodo ifrsPeriodo;

	//bi-directional many-to-one association to TipoEstadoEeff
    @ManyToOne
	@JoinColumn(name="ID_ESTADO_EEFF")
	private TipoEstadoEeff ifrsTipoEstadoEeff;

    public VersionEeff() {
    }

	public long getIdVersionEeff() {
		return this.idVersionEeff;
	}

	public void setIdVersionEeff(long idVersionEeff) {
		this.idVersionEeff = idVersionEeff;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public BigDecimal getVersion() {
		return this.version;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}

	public BigDecimal getVigencia() {
		return this.vigencia;
	}

	public void setVigencia(BigDecimal vigencia) {
		this.vigencia = vigencia;
	}

	public List<Eeff> getIfrsEeffs() {
		return this.ifrsEeffs;
	}

	public void setIfrsEeffs(List<Eeff> ifrsEeffs) {
		this.ifrsEeffs = ifrsEeffs;
	}
	
	public Periodo getIfrsPeriodo() {
		return this.ifrsPeriodo;
	}

	public void setIfrsPeriodo(Periodo ifrsPeriodo) {
		this.ifrsPeriodo = ifrsPeriodo;
	}
	
	public TipoEstadoEeff getIfrsTipoEstadoEeff() {
		return this.ifrsTipoEstadoEeff;
	}

	public void setIfrsTipoEstadoEeff(TipoEstadoEeff ifrsTipoEstadoEeff) {
		this.ifrsTipoEstadoEeff = ifrsTipoEstadoEeff;
	}
	
}