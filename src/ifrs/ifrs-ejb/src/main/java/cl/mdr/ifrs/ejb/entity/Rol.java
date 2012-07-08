package cl.mdr.ifrs.ejb.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;


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

	
	
}