package cl.mdr.ifrs.ejb.dummy.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the IFRS_RELACION_DETALLE_EEFF database table.
 * 
 */
@Embeddable
public class RelacionDetalleEeffPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ID_FECU")
	private long idFecu;

	@Column(name="ID_CUENTA")
	private long idCuenta;

	@Column(name="ID_PERIODO")
	private long idPeriodo;

    public RelacionDetalleEeffPK() {
    }
	public long getIdFecu() {
		return this.idFecu;
	}
	public void setIdFecu(long idFecu) {
		this.idFecu = idFecu;
	}
	public long getIdCuenta() {
		return this.idCuenta;
	}
	public void setIdCuenta(long idCuenta) {
		this.idCuenta = idCuenta;
	}
	public long getIdPeriodo() {
		return this.idPeriodo;
	}
	public void setIdPeriodo(long idPeriodo) {
		this.idPeriodo = idPeriodo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof RelacionDetalleEeffPK)) {
			return false;
		}
		RelacionDetalleEeffPK castOther = (RelacionDetalleEeffPK)other;
		return 
			(this.idFecu == castOther.idFecu)
			&& (this.idCuenta == castOther.idCuenta)
			&& (this.idPeriodo == castOther.idPeriodo);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.idFecu ^ (this.idFecu >>> 32)));
		hash = hash * prime + ((int) (this.idCuenta ^ (this.idCuenta >>> 32)));
		hash = hash * prime + ((int) (this.idPeriodo ^ (this.idPeriodo >>> 32)));
		
		return hash;
    }
}