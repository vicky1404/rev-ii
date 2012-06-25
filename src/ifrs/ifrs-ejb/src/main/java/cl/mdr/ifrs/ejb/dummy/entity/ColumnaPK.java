package cl.mdr.ifrs.ejb.dummy.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the IFRS_COLUMNA database table.
 * 
 */
@Embeddable
public class ColumnaPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ID_COLUMNA")
	private long idColumna;

	@Column(name="ID_GRILLA")
	private long idGrilla;

    public ColumnaPK() {
    }
	public long getIdColumna() {
		return this.idColumna;
	}
	public void setIdColumna(long idColumna) {
		this.idColumna = idColumna;
	}
	public long getIdGrilla() {
		return this.idGrilla;
	}
	public void setIdGrilla(long idGrilla) {
		this.idGrilla = idGrilla;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ColumnaPK)) {
			return false;
		}
		ColumnaPK castOther = (ColumnaPK)other;
		return 
			(this.idColumna == castOther.idColumna)
			&& (this.idGrilla == castOther.idGrilla);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.idColumna ^ (this.idColumna >>> 32)));
		hash = hash * prime + ((int) (this.idGrilla ^ (this.idGrilla >>> 32)));
		
		return hash;
    }
}