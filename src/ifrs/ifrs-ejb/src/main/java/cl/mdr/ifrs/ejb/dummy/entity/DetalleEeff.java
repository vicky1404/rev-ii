package cl.mdr.ifrs.ejb.dummy.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the IFRS_DETALLE_EEFF database table.
 * 
 */
@Entity
@Table(name="IFRS_DETALLE_EEFF")
public class DetalleEeff implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private DetalleEeffPK id;

	@Column(name="DESCRIPCION_CUENTA")
	private String descripcionCuenta;

	@Column(name="MONTO_EBS")
	private BigDecimal montoEbs;

	@Column(name="MONTO_MILES")
	private BigDecimal montoMiles;

	@Column(name="MONTO_PESOS")
	private BigDecimal montoPesos;

	private BigDecimal reclasificacion;

	//bi-directional many-to-one association to Eeff
    @ManyToOne
	@JoinColumns({
		@JoinColumn(name="ID_FECU", referencedColumnName="ID_FECU"),
		@JoinColumn(name="ID_VERSION_EEFF", referencedColumnName="ID_VERSION_EEFF")
		})
	private Eeff ifrsEeff;

    public DetalleEeff() {
    }

	public DetalleEeffPK getId() {
		return this.id;
	}

	public void setId(DetalleEeffPK id) {
		this.id = id;
	}
	
	public String getDescripcionCuenta() {
		return this.descripcionCuenta;
	}

	public void setDescripcionCuenta(String descripcionCuenta) {
		this.descripcionCuenta = descripcionCuenta;
	}

	public BigDecimal getMontoEbs() {
		return this.montoEbs;
	}

	public void setMontoEbs(BigDecimal montoEbs) {
		this.montoEbs = montoEbs;
	}

	public BigDecimal getMontoMiles() {
		return this.montoMiles;
	}

	public void setMontoMiles(BigDecimal montoMiles) {
		this.montoMiles = montoMiles;
	}

	public BigDecimal getMontoPesos() {
		return this.montoPesos;
	}

	public void setMontoPesos(BigDecimal montoPesos) {
		this.montoPesos = montoPesos;
	}

	public BigDecimal getReclasificacion() {
		return this.reclasificacion;
	}

	public void setReclasificacion(BigDecimal reclasificacion) {
		this.reclasificacion = reclasificacion;
	}

	public Eeff getIfrsEeff() {
		return this.ifrsEeff;
	}

	public void setIfrsEeff(Eeff ifrsEeff) {
		this.ifrsEeff = ifrsEeff;
	}
	
}