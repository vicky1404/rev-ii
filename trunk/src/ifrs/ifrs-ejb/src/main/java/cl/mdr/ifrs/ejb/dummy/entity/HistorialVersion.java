package cl.mdr.ifrs.ejb.dummy.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the IFRS_HISTORIAL_VERSION database table.
 * 
 */
@Entity
@Table(name="IFRS_HISTORIAL_VERSION")
public class HistorialVersion implements Serializable {
	private static final long serialVersionUID = 4827045173970105396L;

	@Id
	@Column(name="ID_HISTORIAL")
	private Long idHistorial;

	private String comentario;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="FECHA_PROCESO")
	private Date fechaProceso;

	@Column(name="IP_USUARIO")
	private String ipUsuario;

	//bi-directional many-to-one association to EstadoCuadro
    @ManyToOne
	@JoinColumn(name="ID_ESTADO_CUADRO")
	private EstadoCuadro ifrsEstadoCuadro;

	//bi-directional many-to-one association to Usuario
    @ManyToOne
	@JoinColumn(name="NOMBRE_USUARIO")
	private Usuario usuario;

	//bi-directional many-to-one association to Version
    @ManyToOne
	@JoinColumn(name="ID_VERSION")
	private Version version;

    public HistorialVersion() {
    }

	public Long getIdHistorial() {
		return idHistorial;
	}

	public void setIdHistorial(Long idHistorial) {
		this.idHistorial = idHistorial;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Date getFechaProceso() {
		return fechaProceso;
	}

	public void setFechaProceso(Date fechaProceso) {
		this.fechaProceso = fechaProceso;
	}

	public String getIpUsuario() {
		return ipUsuario;
	}

	public void setIpUsuario(String ipUsuario) {
		this.ipUsuario = ipUsuario;
	}

	public EstadoCuadro getIfrsEstadoCuadro() {
		return ifrsEstadoCuadro;
	}

	public void setIfrsEstadoCuadro(EstadoCuadro ifrsEstadoCuadro) {
		this.ifrsEstadoCuadro = ifrsEstadoCuadro;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Version getVersion() {
		return version;
	}

	public void setVersion(Version version) {
		this.version = version;
	}

	
	
}