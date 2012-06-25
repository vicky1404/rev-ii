package cl.mdr.ifrs.ejb.dummy.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the IFRS_EEFF database table.
 * 
 */
@Embeddable
public class EeffPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ID_FECU")
	private long idFecu;

	@Column(name="ID_VERSION_EEFF")
	private long idVersionEeff;

    public EeffPK() {
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
		if (!(other instanceof EeffPK)) {
			return false;
		}
		EeffPK castOther = (EeffPK)other;
		return 
			(this.idFecu == castOther.idFecu)
			&& (this.idVersionEeff == castOther.idVersionEeff);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.idFecu ^ (this.idFecu >>> 32)));
		hash = hash * prime + ((int) (this.idVersionEeff ^ (this.idVersionEeff >>> 32)));
		
		return hash;
    }
}