package cl.mdr.ifrs.ejb.dummy.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the IFRS_CELDA database table.
 * 
 */
@Entity
@Table(name="IFRS_CELDA")
public class Celda implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private CeldaPK id;

	@Column(name="CHILD_HORIZONTAL")
	private Long childHorizontal;

	@Column(name="CHILD_VERTICAL")
	private Long childVertical;

	private String formula;

	@Column(name="PARENT_HORIZONTAL")
	private Long parentHorizontal;

	@Column(name="PARENT_VERTICAL")
	private Long parentVertical;

	private String valor;

	//bi-directional many-to-one association to Columna
    @ManyToOne
	@JoinColumns({
		@JoinColumn(name="ID_COLUMNA", referencedColumnName="ID_COLUMNA"),
		@JoinColumn(name="ID_GRILLA", referencedColumnName="ID_GRILLA")
		})
	private Columna columna;

	//bi-directional many-to-one association to TipoCelda
    @ManyToOne
	@JoinColumn(name="ID_TIPO_CELDA")
	private TipoCelda tipoCelda;

	//bi-directional many-to-one association to TipoDato
    @ManyToOne
	@JoinColumn(name="ID_TIPO_DATO")
	private TipoDato tipoDato;

	//bi-directional many-to-one association to RelacionDetalleEeff
	@OneToMany(mappedBy="celda")
	private List<RelacionDetalleEeff> relacionDetalleEeffs;

	//bi-directional many-to-one association to RelacionEeff
	@OneToMany(mappedBy="celda")
	private List<RelacionEeff> relacionEeffs;

    public Celda() {
    }

	public CeldaPK getId() {
		return id;
	}

	public void setId(CeldaPK id) {
		this.id = id;
	}

	public Long getChildHorizontal() {
		return childHorizontal;
	}

	public void setChildHorizontal(Long childHorizontal) {
		this.childHorizontal = childHorizontal;
	}

	public Long getChildVertical() {
		return childVertical;
	}

	public void setChildVertical(Long childVertical) {
		this.childVertical = childVertical;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public Long getParentHorizontal() {
		return parentHorizontal;
	}

	public void setParentHorizontal(Long parentHorizontal) {
		this.parentHorizontal = parentHorizontal;
	}

	public Long getParentVertical() {
		return parentVertical;
	}

	public void setParentVertical(Long parentVertical) {
		this.parentVertical = parentVertical;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public Columna getColumna() {
		return columna;
	}

	public void setColumna(Columna columna) {
		this.columna = columna;
	}

	public TipoCelda getTipoCelda() {
		return tipoCelda;
	}

	public void setTipoCelda(TipoCelda tipoCelda) {
		this.tipoCelda = tipoCelda;
	}

	public TipoDato getTipoDato() {
		return tipoDato;
	}

	public void setTipoDato(TipoDato tipoDato) {
		this.tipoDato = tipoDato;
	}

	public List<RelacionDetalleEeff> getRelacionDetalleEeffs() {
		return relacionDetalleEeffs;
	}

	public void setRelacionDetalleEeffs(
			List<RelacionDetalleEeff> relacionDetalleEeffs) {
		this.relacionDetalleEeffs = relacionDetalleEeffs;
	}

	public List<RelacionEeff> getRelacionEeffs() {
		return relacionEeffs;
	}

	public void setRelacionEeffs(List<RelacionEeff> relacionEeffs) {
		this.relacionEeffs = relacionEeffs;
	}
    
    

	
	
}