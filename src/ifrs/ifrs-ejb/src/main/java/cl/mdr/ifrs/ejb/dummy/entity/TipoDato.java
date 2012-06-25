package cl.mdr.ifrs.ejb.dummy.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the IFRS_TIPO_DATO database table.
 * 
 */
@Entity
@Table(name="IFRS_TIPO_DATO")
public class TipoDato implements Serializable {
	private static final long serialVersionUID = -7230636505955932386L;

	@Id
	@Column(name="ID_TIPO_DATO")
	private long idTipoDato;

	private String nombre;

	@Column(name="NOMBRE_CLASE")
	private String nombreClase;

	//bi-directional many-to-one association to Celda
	@OneToMany(mappedBy="tipoDato")
	private List<Celda> celdas;

    public TipoDato() {
    }

	public long getIdTipoDato() {
		return idTipoDato;
	}

	public void setIdTipoDato(long idTipoDato) {
		this.idTipoDato = idTipoDato;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombreClase() {
		return nombreClase;
	}

	public void setNombreClase(String nombreClase) {
		this.nombreClase = nombreClase;
	}

	public List<Celda> getCeldas() {
		return celdas;
	}

	public void setCeldas(List<Celda> celdas) {
		this.celdas = celdas;
	}

	
	
}