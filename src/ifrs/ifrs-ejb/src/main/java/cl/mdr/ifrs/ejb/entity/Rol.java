package cl.mdr.ifrs.ejb.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.google.gson.annotations.Expose;

import java.util.List;


/**
 * The persistent class for the IFRS_ROL database table.
 * 
 */
@Entity
@NamedQueries( { 
	@NamedQuery(name = Rol.FIND_ALL , query = "select r from Rol r" )
})

@Table(name="IFRS_ROL")
public class Rol implements Serializable {	
	private static final long serialVersionUID = 6411167555823652099L;
	
	public static final String FIND_ALL = "Rol.findAll";

	@Id
	@Expose
	@Column(name="ID_ROL")
	private String idRol;

	@Column(name="NOMBRE_ROL")
	@Expose
	private String nombreRol;

	//bi-directional many-to-one association to Usuario
	@OneToMany(mappedBy="rol")
	private List<Usuario> usuarios;

    public Rol() {
    }

	public String getIdRol() {
		return this.idRol;
	}

	public void setIdRol(String idRol) {
		this.idRol = idRol;
	}

	public String getNombreRol() {
		return this.nombreRol;
	}

	public void setNombreRol(String nombreRol) {
		this.nombreRol = nombreRol;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	
	
}