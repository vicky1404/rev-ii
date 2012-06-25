package cl.mdr.ifrs.ejb.dummy.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * The persistent class for the IFRS_AGRUPACION_COLUMNA database table.
 * 
 */
@Entity
@Table(name="IFRS_AGRUPACION_COLUMNA")
public class AgrupacionColumna implements Serializable {
	private static final long serialVersionUID = 3920165734167871798L;

	@EmbeddedId
	private AgrupacionColumnaPK id;

	private Long ancho;

	private Long grupo;

	private String titulo;

	//bi-directional many-to-one association to Columna
    @ManyToOne
	@JoinColumns({
		@JoinColumn(name="ID_COLUMNA", referencedColumnName="ID_COLUMNA"),
		@JoinColumn(name="ID_GRILLA", referencedColumnName="ID_GRILLA")
		})
	private Columna columna;

    public AgrupacionColumna() {
    }

	public AgrupacionColumnaPK getId() {
		return id;
	}

	public void setId(AgrupacionColumnaPK id) {
		this.id = id;
	}

	public Long getAncho() {
		return ancho;
	}

	public void setAncho(Long ancho) {
		this.ancho = ancho;
	}

	public Long getGrupo() {
		return grupo;
	}

	public void setGrupo(Long grupo) {
		this.grupo = grupo;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Columna getColumna() {
		return columna;
	}

	public void setColumna(Columna columna) {
		this.columna = columna;
	}
    
    
	
	
}