package cl.mdr.ifrs.ejb.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the EXFIDA_PARAMETRO database table.
 * 
 */
@Embeddable
public class ParametroPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ID_TIPO_PARAMETRO", insertable = false, updatable = false)
	private long idTipoParametro;

	private String nombre;

    public ParametroPK() {
    }
	public long getIdTipoParametro() {
		return this.idTipoParametro;
	}
	public void setIdTipoParametro(long idTipoParametro) {
		this.idTipoParametro = idTipoParametro;
	}
	public String getNombre() {
		return this.nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ParametroPK)) {
			return false;
		}
		ParametroPK castOther = (ParametroPK)other;
		return 
			(this.idTipoParametro == castOther.idTipoParametro)
			&& this.nombre.equals(castOther.nombre);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.idTipoParametro ^ (this.idTipoParametro >>> 32)));
		hash = hash * prime + this.nombre.hashCode();
		
		return hash;
    }
}