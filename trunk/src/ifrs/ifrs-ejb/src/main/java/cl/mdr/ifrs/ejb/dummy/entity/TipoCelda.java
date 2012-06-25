package cl.mdr.ifrs.ejb.dummy.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the IFRS_TIPO_CELDA database table.
 * 
 */
@Entity
@Table(name="IFRS_TIPO_CELDA")
public class TipoCelda implements Serializable {	
	private static final long serialVersionUID = -8267007561684859540L;

	@Id
	@Column(name="ID_TIPO_CELDA")
	private Long idTipoCelda;

	private String nombre;

	//bi-directional many-to-one association to Celda
	@OneToMany(mappedBy="tipoCelda")
	private List<Celda> celdas;

    public TipoCelda() {
    }

	public Long getIdTipoCelda() {
		return idTipoCelda;
	}

	public void setIdTipoCelda(Long idTipoCelda) {
		this.idTipoCelda = idTipoCelda;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Celda> getCeldas() {
		return celdas;
	}

	public void setCeldas(List<Celda> celdas) {
		this.celdas = celdas;
	}

	
	
}