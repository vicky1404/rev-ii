package cl.mdr.ifrs.ejb.entity.pk;

import java.io.Serializable;

/**
 * The primary key class for the IFRS_PERIODO_EMPRESA database table.
 * 
 */
public class PeriodoEmpresaPK implements Serializable {
	private static final long serialVersionUID = -4530787021415326858L;
	private long idPeriodo;
	private long idRut;
	
    public PeriodoEmpresaPK(long idPeriodo, long idRut) {
		super();
		this.idPeriodo = idPeriodo;
		this.idRut = idRut;
	}
    
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