package cl.mdr.ifrs.ejb.dummy.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the IFRS_EEFF database table.
 * 
 */
@Entity
@Table(name="IFRS_EEFF")
public class Eeff implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private EeffPK id;

	@Column(name="MONTO_TOTAL")
	private BigDecimal montoTotal;

	//bi-directional many-to-one association to DetalleEeff
	@OneToMany(mappedBy="ifrsEeff")
	private List<DetalleEeff> ifrsDetalleEeffs;

	//bi-directional many-to-one association to CodigoFecu
    @ManyToOne
	@JoinColumn(name="ID_FECU")
	private CodigoFecu ifrsCodigoFecu;

	//bi-directional many-to-one association to VersionEeff
    @ManyToOne
	@JoinColumn(name="ID_VERSION_EEFF")
	private VersionEeff ifrsVersionEeff;

    public Eeff() {
    }

	public EeffPK getId() {
		return this.id;
	}

	public void setId(EeffPK id) {
		this.id = id;
	}
	
	public BigDecimal getMontoTotal() {
		return this.montoTotal;
	}

	public void setMontoTotal(BigDecimal montoTotal) {
		this.montoTotal = montoTotal;
	}

	public List<DetalleEeff> getIfrsDetalleEeffs() {
		return this.ifrsDetalleEeffs;
	}

	public void setIfrsDetalleEeffs(List<DetalleEeff> ifrsDetalleEeffs) {
		this.ifrsDetalleEeffs = ifrsDetalleEeffs;
	}
	
	public CodigoFecu getIfrsCodigoFecu() {
		return this.ifrsCodigoFecu;
	}

	public void setIfrsCodigoFecu(CodigoFecu ifrsCodigoFecu) {
		this.ifrsCodigoFecu = ifrsCodigoFecu;
	}
	
	public VersionEeff getIfrsVersionEeff() {
		return this.ifrsVersionEeff;
	}

	public void setIfrsVersionEeff(VersionEeff ifrsVersionEeff) {
		this.ifrsVersionEeff = ifrsVersionEeff;
	}
	
}