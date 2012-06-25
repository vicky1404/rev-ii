package cl.mdr.ifrs.ejb.dummy.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 * The persistent class for the IFRS_GRILLA database table.
 * 
 */
@Entity
@Table(name="IFRS_GRILLA")
public class Grilla implements Serializable {	
	private static final long serialVersionUID = -310349560514771349L;

	@Id
	@Column(name="ID_GRILLA")
	private Long idGrilla;

	@Column(name="TIPO_FORMULA")
	private Long tipoFormula;

	private String titulo;

	//bi-directional many-to-one association to Columna
	@OneToMany(mappedBy="grilla")
	private List<Columna> columnas;

	//bi-directional one-to-one association to Estructura
	@OneToOne
	@JoinColumn(name="ID_GRILLA")
	private Estructura estructura;

    public Grilla() {
    }

	public Long getIdGrilla() {
		return idGrilla;
	}

	public void setIdGrilla(Long idGrilla) {
		this.idGrilla = idGrilla;
	}

	public Long getTipoFormula() {
		return tipoFormula;
	}

	public void setTipoFormula(Long tipoFormula) {
		this.tipoFormula = tipoFormula;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public List<Columna> getColumnas() {
		return columnas;
	}

	public void setColumnas(List<Columna> columnas) {
		this.columnas = columnas;
	}

	public Estructura getEstructura() {
		return estructura;
	}

	public void setEstructura(Estructura estructura) {
		this.estructura = estructura;
	}
    
    

	
	
}