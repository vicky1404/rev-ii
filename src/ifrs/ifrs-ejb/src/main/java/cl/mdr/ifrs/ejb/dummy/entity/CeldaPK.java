package cl.mdr.ifrs.ejb.dummy.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the IFRS_CELDA database table.
 * 
 */
@Embeddable
public class CeldaPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ID_COLUMNA")
	private long idColumna;

	@Column(name="ID_GRILLA")
	private long idGrilla;

	@Column(name="ID_FILA")
	private long idFila;

    public CeldaPK() {
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
	public long getIdFila() {
		return this.idFila;
	}
	public void setIdFila(long idFila) {
		this.idFila = idFila;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof CeldaPK)) {
			return false;
		}
		CeldaPK castOther = (CeldaPK)other;
		return 
			(this.idColumna == castOther.idColumna)
			&& (this.idGrilla == castOther.idGrilla)
			&& (this.idFila == castOther.idFila);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.idColumna ^ (this.idColumna >>> 32)));
		hash = hash * prime + ((int) (this.idGrilla ^ (this.idGrilla >>> 32)));
		hash = hash * prime + ((int) (this.idFila ^ (this.idFila >>> 32)));
		
		return hash;
    }
}