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
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Email;

import com.google.common.base.Strings;


/**
 * The persistent class for the IFRS_USUARIO database table.
 */
@Entity
@NamedQueries( { 
    @NamedQuery(name = Usuario.AUTHENTICATE_USER , query = " select u from Usuario u left join fetch u.grupos " +
    													   " where u.nombreUsuario =:nombreUsuario "),
    @NamedQuery(name = Usuario.FIND_BY_FILTRO , query = " select u from Usuario u " +
    													" left join fetch u.grupos " +
    													" where 1 = 1 " +
    													" and (:nombreUsuario is null or upper(u.nombreUsuario) like :nombreUsuario) " +
    													" and (:nombre is null or upper(u.nombre) like :nombre) " +
    													" and (:apellidoPaterno is null or upper(u.apellidoPaterno) like :apellidoPaterno)" +
    													" and (:apellidoMaterno is null or upper(u.apellidoMaterno) like :apellidoMaterno)" +
    													" and (:rol is null or u.rol.idRol = :rol) ORDER BY u.nombreUsuario ASC"),
    													
	@NamedQuery(name = Usuario.FIND_BY_NOMBRE , query =  " select u from Usuario u " +
														 " left join fetch u.grupos " +														
														 " where upper(u.nombreUsuario) like :nombreUsuario " +
														 " ORDER BY u.nombreUsuario ASC")
})    
@Table(name="IFRS_USUARIO")
public class Usuario implements Serializable {
	private static final long serialVersionUID = -2179335821042652705L;
	
	public static final String AUTHENTICATE_USER = "Usuario.authenticateUser";
	public static final String FIND_BY_FILTRO = "Usuario.findByFiltro";
	public static final String FIND_BY_NOMBRE = "Usuario.findByNombre";

	@Id
	@Column(name="NOMBRE_USUARIO")	
    @Pattern(regexp = "[A-Za-z  .]*", message = "Puede contener solo letras y puntos")
	private String nombreUsuario;
	
	@Column(name="NOMBRE")	
    @Pattern(regexp = "[A-Za-z  ]*", message = "Puede contener solo letras")
	private String nombre;
	
	@Column(name="APELLIDO_PATERNO")	
    @Pattern(regexp = "[A-Za-z  ]*", message = "Puede contener solo letras")
	private String apellidoPaterno;
	
	@Column(name="APELLIDO_MATERNO")	
    @Pattern(regexp = "[A-Za-z  ]*", message = "Puede contener solo letras")
	private String apellidoMaterno;
	
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
           
	private String password;

	private Long vigente;
	
	@Column(name="CAMBIAR_PASSWORD")
	private Long cambiarPassword;
	
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
	
	

	public Usuario(String nombreUsuario, String nombre, String apellidoPaterno,
			String apellidoMaterno, String email, String password, Date fechaActualizacion,
			Date fechaCreacion, Date fechaUltimoAcceso, Long vigente, Rol rol) {
		super();
		this.nombreUsuario = nombreUsuario;
		this.nombre = nombre;
		this.apellidoPaterno = apellidoPaterno;
		this.apellidoMaterno = apellidoMaterno;
		this.email = email;
		this.password = password;
		this.fechaActualizacion = fechaActualizacion;
		this.fechaCreacion = fechaCreacion;
		this.fechaUltimoAcceso = fechaUltimoAcceso;
		this.vigente = vigente;
		this.rol = rol;
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
		if(!Strings.isNullOrEmpty(email)){
			email = email.toLowerCase();
		}
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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidoPaterno() {
		return apellidoPaterno;
	}

	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}

	public String getApellidoMaterno() {
		return apellidoMaterno;
	}

	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((apellidoMaterno == null) ? 0 : apellidoMaterno.hashCode());
		result = prime * result
				+ ((apellidoPaterno == null) ? 0 : apellidoPaterno.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime
				* result
				+ ((fechaActualizacion == null) ? 0 : fechaActualizacion
						.hashCode());
		result = prime * result
				+ ((fechaCreacion == null) ? 0 : fechaCreacion.hashCode());
		result = prime
				* result
				+ ((fechaUltimoAcceso == null) ? 0 : fechaUltimoAcceso
						.hashCode());
		result = prime * result + ((grupos == null) ? 0 : grupos.hashCode());
		result = prime
				* result
				+ ((historialReportes == null) ? 0 : historialReportes
						.hashCode());
		result = prime
				* result
				+ ((historialVersiones == null) ? 0 : historialVersiones
						.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result
				+ ((nombreUsuario == null) ? 0 : nombreUsuario.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((rol == null) ? 0 : rol.hashCode());
		result = prime * result + ((vigente == null) ? 0 : vigente.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (apellidoMaterno == null) {
			if (other.apellidoMaterno != null)
				return false;
		} else if (!apellidoMaterno.equals(other.apellidoMaterno))
			return false;
		if (apellidoPaterno == null) {
			if (other.apellidoPaterno != null)
				return false;
		} else if (!apellidoPaterno.equals(other.apellidoPaterno))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (fechaActualizacion == null) {
			if (other.fechaActualizacion != null)
				return false;
		} else if (!fechaActualizacion.equals(other.fechaActualizacion))
			return false;
		if (fechaCreacion == null) {
			if (other.fechaCreacion != null)
				return false;
		} else if (!fechaCreacion.equals(other.fechaCreacion))
			return false;
		if (fechaUltimoAcceso == null) {
			if (other.fechaUltimoAcceso != null)
				return false;
		} else if (!fechaUltimoAcceso.equals(other.fechaUltimoAcceso))
			return false;
		if (grupos == null) {
			if (other.grupos != null)
				return false;
		} else if (!grupos.equals(other.grupos))
			return false;
		if (historialReportes == null) {
			if (other.historialReportes != null)
				return false;
		} else if (!historialReportes.equals(other.historialReportes))
			return false;
		if (historialVersiones == null) {
			if (other.historialVersiones != null)
				return false;
		} else if (!historialVersiones.equals(other.historialVersiones))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (nombreUsuario == null) {
			if (other.nombreUsuario != null)
				return false;
		} else if (!nombreUsuario.equals(other.nombreUsuario))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (rol == null) {
			if (other.rol != null)
				return false;
		} else if (!rol.equals(other.rol))
			return false;
		if (vigente == null) {
			if (other.vigente != null)
				return false;
		} else if (!vigente.equals(other.vigente))
			return false;
		return true;
	}

	/**
	 * @return the cambiarPassword
	 */
	public Long getCambiarPassword() {
		return cambiarPassword;
	}

	/**
	 * @param cambiarPassword the cambiarPassword to set
	 */
	public void setCambiarPassword(Long cambiarPassword) {
		this.cambiarPassword = cambiarPassword;
	}

	
	
	
}