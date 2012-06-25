package cl.mdr.ifrs.ejb.dummy.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the IFRS_VERSION database table.
 * 
 */
@Entity
@Table(name="IFRS_VERSION")
public class Version implements Serializable {	
	private static final long serialVersionUID = 4036774345169298370L;

	@Id
	@Column(name="ID_VERSION")
	private Long idVersion;

	private String comentario;

    @Temporal(TemporalType.DATE)
	@Column(name="FECHA_CREACION")
	private Date fechaCreacion;

	@Column(name="VERSION")
	private Long version;

	private Long vigencia;

	//bi-directional many-to-one association to Estructura
	@OneToMany(mappedBy="version")
	private List<Estructura> estructuras;

	//bi-directional many-to-one association to HistorialVersion
	@OneToMany(mappedBy="version")
	private List<HistorialVersion> historialVersiones;

	//bi-directional many-to-one association to Catalogo
    @ManyToOne
	@JoinColumn(name="ID_CATALOGO")
	private Catalogo catalogo;

	//bi-directional many-to-one association to Periodo
    @ManyToOne
	@JoinColumn(name="ID_PERIODO")
	private Periodo periodo;

    public Version() {
    }

	public Long getIdVersion() {
		return idVersion;
	}

	public void setIdVersion(Long idVersion) {
		this.idVersion = idVersion;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Long getVigencia() {
		return vigencia;
	}

	public void setVigencia(Long vigencia) {
		this.vigencia = vigencia;
	}

	public List<Estructura> getEstructuras() {
		return estructuras;
	}

	public void setEstructuras(List<Estructura> estructuras) {
		this.estructuras = estructuras;
	}

	public List<HistorialVersion> getHistorialVersiones() {
		return historialVersiones;
	}

	public void setHistorialVersiones(List<HistorialVersion> historialVersiones) {
		this.historialVersiones = historialVersiones;
	}

	public Catalogo getCatalogo() {
		return catalogo;
	}

	public void setCatalogo(Catalogo catalogo) {
		this.catalogo = catalogo;
	}

	public Periodo getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}

    
	
	
}