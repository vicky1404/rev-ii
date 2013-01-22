package cl.mdr.ifrs.ejb.entity.pk;

import java.io.Serializable;

import java.lang.Long;


public class RelacionEeffPK implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -6506132959434868875L;
	private Long idFecu;
	private Long idPeriodo;
	private Long idRut;
	private Long idGrilla;
	private Long idColumna;
	private Long idFila;

    public RelacionEeffPK() {
    }


	public RelacionEeffPK(Long idFecu, Long idPeriodo, Long idRut, Long idGrilla, Long idColumna, Long idFila) {
		this.idFecu = idFecu;
		this.idPeriodo = idPeriodo;
		this.idRut = idRut;
		this.idGrilla = idGrilla;
		this.idColumna = idColumna;
		this.idFila = idFila;
	}

	public Long getIdFecu() {
		return idFecu;
	}

	public void setIdFecu(Long idFecu) {
		this.idFecu = idFecu;
	}

	public Long getIdPeriodo() {
		return idPeriodo;
	}

	public void setIdPeriodo(Long idPeriodo) {
		this.idPeriodo = idPeriodo;
	}

	public Long getIdRut() {
		return idRut;
	}

	public void setIdRut(Long idRut) {
		this.idRut = idRut;
	}

	public Long getIdGrilla() {
		return idGrilla;
	}

	public void setIdGrilla(Long idGrilla) {
		this.idGrilla = idGrilla;
	}

	public Long getIdColumna() {
		return idColumna;
	}

	public void setIdColumna(Long idColumna) {
		this.idColumna = idColumna;
	}

	public Long getIdFila() {
		return idFila;
	}

	public void setIdFila(Long idFila) {
		this.idFila = idFila;
	}

  
}
