package cl.mdr.ifrs.ejb.dummy.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the IFRS_COLUMNA database table.
 * 
 */
@Entity
@Table(name="IFRS_COLUMNA")
public class Columna implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ColumnaPK id;

	private Long ancho;

	private Long orden;

	@Column(name="ROW_HEADER")
	private Long rowHeader;

	@Column(name="TITULO_COLUMNA")
	private String tituloColumna;

	//bi-directional many-to-one association to AgrupacionColumna
	@OneToMany(mappedBy="columna")
	private List<AgrupacionColumna> agrupacionColumnas;

	//bi-directional many-to-one association to Celda
	@OneToMany(mappedBy="columna")
	private List<Celda> celdas;

	//bi-directional many-to-one association to Grilla
    @ManyToOne
	@JoinColumn(name="ID_GRILLA")
	private Grilla grilla;

    public Columna() {
    }

	public ColumnaPK getId() {
		return id;
	}

	public void setId(ColumnaPK id) {
		this.id = id;
	}

	public Long getAncho() {
		return ancho;
	}

	public void setAncho(Long ancho) {
		this.ancho = ancho;
	}

	public Long getOrden() {
		return orden;
	}

	public void setOrden(Long orden) {
		this.orden = orden;
	}

	public Long getRowHeader() {
		return rowHeader;
	}

	public void setRowHeader(Long rowHeader) {
		this.rowHeader = rowHeader;
	}

	public String getTituloColumna() {
		return tituloColumna;
	}

	public void setTituloColumna(String tituloColumna) {
		this.tituloColumna = tituloColumna;
	}

	public List<AgrupacionColumna> getAgrupacionColumnas() {
		return agrupacionColumnas;
	}

	public void setAgrupacionColumnas(List<AgrupacionColumna> agrupacionColumnas) {
		this.agrupacionColumnas = agrupacionColumnas;
	}

	public List<Celda> getCeldas() {
		return celdas;
	}

	public void setCeldas(List<Celda> celdas) {
		this.celdas = celdas;
	}

	public Grilla getGrilla() {
		return grilla;
	}

	public void setGrilla(Grilla grilla) {
		this.grilla = grilla;
	}
    
    

	
	
}