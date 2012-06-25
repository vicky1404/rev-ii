package cl.mdr.ifrs.ejb.dummy.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the IFRS_DETALLE_EEFF database table.
 * 
 */
@Embeddable
public class DetalleEeffPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ID_CUENTA")
	private long idCuenta;

	@Column(name="ID_FECU")
	private long idFecu;

	@Column(name="ID_VERSION_EEFF")
	private long idVersionEeff;

    public DetalleEeffPK() {
    }
	public long getIdCuenta() {
		return this.idCuenta;
	}
	public void setIdCuenta(long idCuenta) {
		this.idCuenta = idCuenta;
	}
	public long getIdFecu() {
		return this.idFecu;
	}
	public void setIdFecu(long idFecu) {
		this.idFecu = idFecu;
	}
	public long getIdVersionEeff() {
		return this.idVersionEeff;
	}
	public void setIdVersionEeff(long idVersionEeff) {
		this.idVersionEeff = idVersionEeff;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof DetalleEeffPK)) {
			return false;
		}
		DetalleEeffPK castOther = (DetalleEeffPK)other;
		return 
			(this.idCuenta == castOther.idCuenta)
			&& (this.idFecu == castOther.idFecu)
			&& (this.idVersionEeff == castOther.idVersionEeff);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.idCuenta ^ (this.idCuenta >>> 32)));
		hash = hash * prime + ((int) (this.idFecu ^ (this.idFecu >>> 32)));
		hash = hash * prime + ((int) (this.idVersionEeff ^ (this.idVersionEeff >>> 32)));
		
		return hash;
    }
}