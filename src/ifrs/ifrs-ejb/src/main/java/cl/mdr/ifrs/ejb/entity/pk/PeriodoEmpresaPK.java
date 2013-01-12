package cl.mdr.ifrs.ejb.entity.pk;

import java.io.Serializable;



/**
 * The primary key class for the IFRS_PERIODO_EMPRESA database table.
 * 
 */
public class PeriodoEmpresaPK implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2476298956371983247L;
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
		return idPeriodo;
	}

	public void setIdPeriodo(long idPeriodo) {
		this.idPeriodo = idPeriodo;
	}

	public long getIdRut() {
		return idRut;
	}

	public void setIdRut(long idRut) {
		this.idRut = idRut;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (idPeriodo ^ (idPeriodo >>> 32));
		result = prime * result + (int) (idRut ^ (idRut >>> 32));
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
		PeriodoEmpresaPK other = (PeriodoEmpresaPK) obj;
		if (idPeriodo != other.idPeriodo)
			return false;
		if (idRut != other.idRut)
			return false;
		return true;
	}

	
	
}