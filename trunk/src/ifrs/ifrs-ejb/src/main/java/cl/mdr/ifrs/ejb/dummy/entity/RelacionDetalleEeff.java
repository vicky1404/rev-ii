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
 * The persistent class for the IFRS_RELACION_DETALLE_EEFF database table.
 * 
 */
@Entity
@Table(name="IFRS_RELACION_DETALLE_EEFF")
public class RelacionDetalleEeff implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RelacionDetalleEeffPK id;

	@Column(name="DESCRIPCION_CUENTA")
	private String descripcionCuenta;

	@Column(name="MONTO_EBS")
	private Long montoEbs;

	@Column(name="MONTO_MILES")
	private Long montoMiles;

	@Column(name="MONTO_PESOS")
	private Long montoPesos;

	private Long reclasificacion;

	//bi-directional many-to-one association to Celda
    @ManyToOne
	@JoinColumns({
		@JoinColumn(name="ID_COLUMNA", referencedColumnName="ID_COLUMNA"),
		@JoinColumn(name="ID_FILA", referencedColumnName="ID_FILA"),
		@JoinColumn(name="ID_GRILLA", referencedColumnName="ID_GRILLA")
		})
	private Celda celda;

	//bi-directional many-to-one association to Periodo
    @ManyToOne
	@JoinColumn(name="ID_PERIODO")
	private Periodo periodo;

    public RelacionDetalleEeff() {
    }

	public RelacionDetalleEeffPK getId() {
		return id;
	}

	public void setId(RelacionDetalleEeffPK id) {
		this.id = id;
	}

	public String getDescripcionCuenta() {
		return descripcionCuenta;
	}

	public void setDescripcionCuenta(String descripcionCuenta) {
		this.descripcionCuenta = descripcionCuenta;
	}

	public Long getMontoEbs() {
		return montoEbs;
	}

	public void setMontoEbs(Long montoEbs) {
		this.montoEbs = montoEbs;
	}

	public Long getMontoMiles() {
		return montoMiles;
	}

	public void setMontoMiles(Long montoMiles) {
		this.montoMiles = montoMiles;
	}

	public Long getMontoPesos() {
		return montoPesos;
	}

	public void setMontoPesos(Long montoPesos) {
		this.montoPesos = montoPesos;
	}

	public Long getReclasificacion() {
		return reclasificacion;
	}

	public void setReclasificacion(Long reclasificacion) {
		this.reclasificacion = reclasificacion;
	}

	public Celda getCelda() {
		return celda;
	}

	public void setCelda(Celda celda) {
		this.celda = celda;
	}

	public Periodo getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}
    
    

	
	
}