package cl.mdr.ifrs.ejb.entity.pk;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the IFRS_PERIODO_EMPRESA database table.
 * 
 */
@Embeddable
public class PeriodoEmpresaPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ID_PERIODO")
	private long idPeriodo;

	@Column(name="ID_RUT")
	private long idRut;

    public PeriodoEmpresaPK() {
    }
	public long getIdPeriodo() {
		return this.idPeriodo;
	}
	public void setIdPeriodo(long idPeriodo) {
		this.idPeriodo = idPeriodo;
	}
	public long getIdRut() {
		return this.idRut;
	}
	public void setIdRut(long idRut) {
		this.idRut = idRut;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof PeriodoEmpresaPK)) {
			return false;
		}
		PeriodoEmpresaPK castOther = (PeriodoEmpresaPK)other;
		return 
			(this.idPeriodo == castOther.idPeriodo)
			&& (this.idRut == castOther.idRut);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.idPeriodo ^ (this.idPeriodo >>> 32)));
		hash = hash * prime + ((int) (this.idRut ^ (this.idRut >>> 32)));
		
		return hash;
    }
}