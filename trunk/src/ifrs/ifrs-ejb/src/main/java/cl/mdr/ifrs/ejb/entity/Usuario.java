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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Email;


/**
 * The persistent class for the IFRS_USUARIO database table.
 * 
 */
@Entity
@NamedQueries( { 
    @NamedQuery(name = Usuario.AUTHENTICATE_USER , query = " select u from Usuario u left join fetch u.grupos " +
    													   " where u.nombreUsuario =:nombreUsuario ")
})    
@Table(name="IFRS_USUARIO")
public class Usuario implements Serializable {
	private static final long serialVersionUID = -2179335821042652705L;
	
	public static final String AUTHENTICATE_USER = "Usuario.authenticateUser";

	@Id
	@Column(name="NOMBRE_USUARIO")
	@NotNull
    @Size(min = 1, max = 255)
    @Pattern(regexp = "[A-Za-z  .]*", message = "Puede contener solo letras y puntos")
	private String nombreUsuario;
	
	@Email
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
    
    @NotNull
    @Size(min = 1, max = 255)
	private String password;

	private Long vigente;
	
	//bi-directional many-to-one association to Rol
    @ManyToOne
	@JoinColumn(name="ID_ROL")
	private Rol rol;

	//bi-directional many-to-one association to HistorialReporte
	@OneToMany(mappedBy="usuario", fetch = FetchType.LAZY)
	private List<HistorialReporte> historialReportes;

	//bi-directional many-to-one association to HistorialVersion
	@OneToMany(mappedBy="usuario", fetch = FetchType.LAZY)
	private List<HistorialVersion> historialVersiones;

	//bi-directional many-to-many association to Grupo
	@Fetch(FetchMode.JOIN)
    @ManyToMany(fetch = FetchType.LAZY) 
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
    
	public Usuario(String nombreUsuario) {
		super();
		this.nombreUsuario = nombreUsuario;
	}
	
	



	public Usuario(String nombreUsuario, String email, Date fechaActualizacion,
				   Date fechaCreacion, Date fechaUltimoAcceso, String password,
				   Long vigente, List<Grupo> grupos) {
		super();
		this.nombreUsuario = nombreUsuario;
		this.email = email;
		this.fechaActualizacion = fechaActualizacion;
		this.fechaCreacion = fechaCreacion;
		this.fechaUltimoAcceso = fechaUltimoAcceso;
		this.password = password;
		this.vigente = vigente;
		this.grupos = grupos;
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

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	
	
}