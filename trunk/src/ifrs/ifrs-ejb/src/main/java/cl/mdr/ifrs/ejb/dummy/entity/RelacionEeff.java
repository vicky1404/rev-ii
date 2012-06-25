package cl.mdr.ifrs.ejb.dummy.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * The persistent class for the IFRS_RELACION_EEFF database table.
 * 
 */
@Entity
@Table(name="IFRS_RELACION_EEFF")
public class RelacionEeff implements Serializable {
	private static final long serialVersionUID = 3956196409969521180L;

	@EmbeddedId
	private RelacionEeffPK id;

	@Column(name="MONTO_TOTAL")
	private Long montoTotal;

	//bi-directional many-to-one association to Celda
    @ManyToOne
	@JoinColumns({
		@JoinColumn(name="ID_COLUMNA", referencedColumnName="ID_COLUMNA"),
		@JoinColumn(name="ID_FILA", referencedColumnName="ID_FILA"),
		@JoinColumn(name="ID_GRILLA", referencedColumnName="ID_GRILLA")
		})
	private Celda celda;

	//bi-directional many-to-one association to CodigoFecu
    @ManyToOne
	@JoinColumn(name="ID_FECU")
	private CodigoFecu codigoFecu;

	//bi-directional many-to-one association to Periodo
    @ManyToOne
	@JoinColumn(name="ID_PERIODO")
	private Periodo periodo;

    public RelacionEeff() {
    }

	public RelacionEeffPK getId() {
		return id;
	}

	public void setId(RelacionEeffPK id) {
		this.id = id;
	}

	public Long getMontoTotal() {
		return montoTotal;
	}

	public void setMontoTotal(Long montoTotal) {
		this.montoTotal = montoTotal;
	}

	public Celda getCelda() {
		return celda;
	}

	public void setCelda(Celda celda) {
		this.celda = celda;
	}

	public CodigoFecu getCodigoFecu() {
		return codigoFecu;
	}

	public void setCodigoFecu(CodigoFecu codigoFecu) {
		this.codigoFecu = codigoFecu;
	}

	public Periodo getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}
    
    

	
	
}