package cl.mdr.ifrs.ejb.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the IFRS_USUARIO database table.
 * 
 */
@Entity
@Table(name="IFRS_USUARIO")
public class Usuario implements Serializable {
	private static final long serialVersionUID = -2179335821042652705L;

	@Id
	@Column(name="NOMBRE_USUARIO")
	private String nombreUsuario;

	private String email;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="FECHA_ACTUALIZACION")
	private Date fechaActualizacion;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="FECHA_CREACION")
	private Date fechaCreacion;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="FECHA_ULTIMO_ACCESO")
	private Date fechaUltimoAcceso;

	private String password;

	private Long vigente;

	//bi-directional many-to-one association to HistorialReporte
	@OneToMany(mappedBy="usuario", fetch = FetchType.LAZY)
	private List<HistorialReporte> historialReportes;

	//bi-directional many-to-one association to HistorialVersion
	@OneToMany(mappedBy="usuario", fetch = FetchType.LAZY)
	private List<HistorialVersion> historialVersiones;

	//bi-directional many-to-many association to Grupo
    @ManyToMany
	@JoinTable(
		name="IFRS_USUARIO_GRUPO"
		, joinColumns={
			@JoinColumn(name="NOMBRE_USUARIO")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_GRUPO_ACCESO")
			}
		)
	private List<Grupo> grupos;

    public Usuario() {
    }

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getFechaActualizacion() {
		return fechaActualizacion;
	}

	public void setFechaActualizacion(Date fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaUltimoAcceso() {
		return fechaUltimoAcceso;
	}

	public void setFechaUltimoAcceso(Date fechaUltimoAcceso) {
		this.fechaUltimoAcceso = fechaUltimoAcceso;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getVigente() {
		return vigente;
	}

	public void setVigente(Long vigente) {
		this.vigente = vigente;
	}

	public List<HistorialReporte> getHistorialReportes() {
		return historialReportes;
	}

	public void setHistorialReportes(List<HistorialReporte> historialReportes) {
		this.historialReportes = historialReportes;
	}

	public List<HistorialVersion> getHistorialVersiones() {
		return historialVersiones;
	}

	public void setHistorialVersiones(List<HistorialVersion> historialVersiones) {
		this.historialVersiones = historialVersiones;
	}

	public List<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}

	
	
}