package cl.mdr.ifrs.ejb.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.gson.annotations.Expose;

import cl.mdr.ifrs.ejb.common.Constantes;
import cl.mdr.ifrs.ejb.cross.EeffUtil;
import cl.mdr.ifrs.ejb.entity.pk.RelacionDetalleEeffPK;


@Entity
@NamedQueries( { @NamedQuery(name = RelacionDetalleEeff.FIND_ALL, query = "select o from RelacionDetalleEeff o"),
    @NamedQuery(name = RelacionDetalleEeff.FIND_BY_PERIODO, query = "select o from RelacionDetalleEeff o where o.periodoEmpresa.periodo.idPeriodo = :idPeriodo order by o.idCuenta"),
    @NamedQuery(name = RelacionDetalleEeff.FIND_BY_PERIODO_FECU_CUENTA, query = "select o from RelacionDetalleEeff o where o.periodoEmpresa.periodo.idPeriodo = :idPeriodo and o.idFecu = :idFecu and o.idCuenta = :idCuenta"),
    @NamedQuery(name = RelacionDetalleEeff.DELETE_BY_CELDA, query = "delete from RelacionDetalleEeff o where o.celda5 = :celda"),
    @NamedQuery(name = RelacionDetalleEeff.DELETE_BY_GRILLA_PERIODO, query = "delete from RelacionDetalleEeff o where o.idGrilla = :idGrilla and o.idPeriodo = :idPeriodo")})

@Table(name = Constantes.RELACION_DETALLE_EEFF)
@IdClass(RelacionDetalleEeffPK.class)

public class RelacionDetalleEeff implements Serializable {

	public static final String FIND_ALL = "RelacionDetalleEeff.findAll";
    public static final String FIND_BY_PERIODO_FECU_CUENTA = "RelacionDetalleEeff.findByPeriodoFecuCuenta";
    public static final String FIND_BY_PERIODO = "RelacionDetalleEeff.findByPeriodo";
    public static final String DELETE_BY_CELDA = "RelacionDetalleEeff.deleteByCelda";
    public static final String DELETE_BY_GRILLA_PERIODO = "RelacionDetalleEeff.deleteByGrillaPeriodo";
	   
private static final long serialVersionUID = -2121624962103986848L;
    
    @Id
    @Column(name = "ID_CUENTA", nullable = false)
    @Expose
    private Long idCuenta;
    
    @Id
    @Column(name = "ID_FECU", nullable = false)
    @Expose
    private Long idFecu;
    
    @Id
    @Column(name = "ID_PERIODO", nullable = false, insertable = false, updatable = false)
    @Expose
    private Long idPeriodo;
    
    @Id
    @Column(name = "ID_RUT", nullable = false, insertable = false, updatable = false)    
    @Expose
    private Long idRut;
    
	@Column(name = "ID_GRILLA", insertable = false, updatable = false)
	@Expose
    private Long idGrilla;

	
	@Column(name = "ID_FILA", insertable = false, updatable = false)
	@Expose
    private Long idFila;
	
	@Column(name = "ID_COLUMNA", insertable = false, updatable = false)
	@Expose
    private Long idColumna;

    @Column(name = "MONTO_EBS", length = 256)
    @Expose
    private BigDecimal montoEbs;
        
    @Column(name = "MONTO_MILES")
    @Expose
    private BigDecimal montoMiles;
    
    @Column(name = "MONTO_PESOS")
    @Expose
    private BigDecimal montoPesos;
    
    @Column(name = "MONTO_RECLASIFICACION")
    @Expose
    private BigDecimal montoReclasificacion;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns( { @JoinColumn(name = "ID_PERIODO", referencedColumnName = "ID_PERIODO"),
			        @JoinColumn(name = "ID_RUT", referencedColumnName = "ID_RUT")})
    @Expose
    private PeriodoEmpresa periodoEmpresa;
    
    @ManyToOne
    @JoinColumns( { @JoinColumn(name = "ID_COLUMNA", referencedColumnName = "ID_COLUMNA"),
                    @JoinColumn(name = "ID_GRILLA", referencedColumnName = "ID_GRILLA"),
                    @JoinColumn(name = "ID_FILA", referencedColumnName = "ID_FILA") })
    @Expose
    private Celda celda5;
    
    @Transient
    private BigDecimal montoPesosNuevo;
    
    @ManyToOne
    @JoinColumn(name = "ID_CUENTA", insertable = false, updatable = false)
    private CuentaContable cuentaContable;

    public RelacionDetalleEeff() {
    }

    public RelacionDetalleEeff(BigDecimal ebs, Celda celda5, Long idCuenta, 
                                BigDecimal montoMiles, BigDecimal montoPesos, BigDecimal reclasificacion) {
        
        this.montoEbs = ebs;
        this.celda5 = celda5;
        this.idCuenta = idCuenta;
        this.montoMiles = montoMiles;
        this.montoPesos = montoPesos;
        this.montoReclasificacion = reclasificacion;
    }
    
    public Long getIdRut() {
		return idRut;
	}

	public void setIdRut(Long idRut) {
		this.idRut = idRut;
	}

	public PeriodoEmpresa getPeriodoEmpresa() {
		return periodoEmpresa;
	}

	public void setPeriodoEmpresa(PeriodoEmpresa periodoEmpresa) {
		if(periodoEmpresa != null){
        	this.idPeriodo = periodoEmpresa.getIdPeriodo();
        	this.idRut = periodoEmpresa.getIdRut();
        }
		this.periodoEmpresa = periodoEmpresa;
	}

    public BigDecimal getMontoEbs() {
        return montoEbs;
    }

    public void setMontoEbs(BigDecimal ebs) {
        this.montoEbs = ebs;
    }


    public Long getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(Long idCuenta) {
        this.idCuenta = idCuenta;
    }


    public BigDecimal getMontoMiles() {
        return montoMiles;
    }

    public void setMontoMiles(BigDecimal montoMiles) {
        this.montoMiles = montoMiles;
    }

    public BigDecimal getMontoPesos() {
        return montoPesos;
    }

    public void setMontoPesos(BigDecimal montoPesos) {
        this.montoPesos = montoPesos;
    }

    public BigDecimal getMontoReclasificacion() {
        return montoReclasificacion;
    }

    public void setMontoReclasificacion(BigDecimal reclasificacion) {
        this.montoReclasificacion = reclasificacion;
    }

    public Celda getCelda5() {
        return celda5;
    }

    public void setCelda5(Celda celda5) {
        this.celda5 = celda5;
    }

    public void setIdFecu(Long idFecu) {
        this.idFecu = idFecu;
    }

    public Long getIdFecu() {
        return idFecu;
    }

    public void setIdPeriodo(Long idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public Long getIdPeriodo() {
        return idPeriodo;
    }
    
    public void copyDetalleEeff(final DetalleEeff detalleEeff, final Celda celda,final PeriodoEmpresa periodoEmpresa){
        
        this.idCuenta = detalleEeff.getIdCuenta();
        this.idFecu = detalleEeff.getIdFecu();
        if(periodoEmpresa != null){
        	this.idPeriodo = periodoEmpresa.getIdPeriodo();
        	this.idRut = periodoEmpresa.getIdRut();
        }
        this.periodoEmpresa = periodoEmpresa;        
        this.montoEbs = detalleEeff.getMontoEbs();
        this.montoMiles = detalleEeff.getMontoMiles();
        this.montoPesos = detalleEeff.getMontoPesos();
        this.montoReclasificacion = detalleEeff.getMontoReclasificacion();
        this.celda5 = celda;
        
    }

	
	
	public String getFecuFormat(){
		
		if (idFecu != null){
			return EeffUtil.formatFecu(idFecu);			
		} else {
			return "";	
		}
		
	}

	

	public Long getIdGrilla() {
		return idGrilla;
	}

	public void setIdGrilla(Long idGrilla) {
		this.idGrilla = idGrilla;
	}

	public Long getIdFila() {
		return idFila;
	}

	public void setIdFila(Long idFila) {
		this.idFila = idFila;
	}

	public Long getIdColumna() {
		return idColumna;
	}

	public void setIdColumna(Long idColumna) {
		this.idColumna = idColumna;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((celda5 == null) ? 0 : celda5.hashCode());
		result = prime * result
				+ ((idColumna == null) ? 0 : idColumna.hashCode());
		result = prime * result
				+ ((idCuenta == null) ? 0 : idCuenta.hashCode());
		result = prime * result + ((idFecu == null) ? 0 : idFecu.hashCode());
		result = prime * result + ((idFila == null) ? 0 : idFila.hashCode());
		result = prime * result
				+ ((idGrilla == null) ? 0 : idGrilla.hashCode());
		result = prime * result
				+ ((idPeriodo == null) ? 0 : idPeriodo.hashCode());
		result = prime * result + ((idRut == null) ? 0 : idRut.hashCode());
		result = prime * result
				+ ((montoEbs == null) ? 0 : montoEbs.hashCode());
		result = prime * result
				+ ((montoMiles == null) ? 0 : montoMiles.hashCode());
		result = prime * result
				+ ((montoPesos == null) ? 0 : montoPesos.hashCode());
		result = prime
				* result
				+ ((montoReclasificacion == null) ? 0 : montoReclasificacion
						.hashCode());
		result = prime * result
				+ ((periodoEmpresa == null) ? 0 : periodoEmpresa.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RelacionDetalleEeff other = (RelacionDetalleEeff) obj;
		if (celda5 == null) {
			if (other.celda5 != null)
				return false;
		} else if (!celda5.equals(other.celda5))
			return false;
		if (idColumna == null) {
			if (other.idColumna != null)
				return false;
		} else if (!idColumna.equals(other.idColumna))
			return false;
		if (idCuenta == null) {
			if (other.idCuenta != null)
				return false;
		} else if (!idCuenta.equals(other.idCuenta))
			return false;
		if (idFecu == null) {
			if (other.idFecu != null)
				return false;
		} else if (!idFecu.equals(other.idFecu))
			return false;
		if (idFila == null) {
			if (other.idFila != null)
				return false;
		} else if (!idFila.equals(other.idFila))
			return false;
		if (idGrilla == null) {
			if (other.idGrilla != null)
				return false;
		} else if (!idGrilla.equals(other.idGrilla))
			return false;
		if (idPeriodo == null) {
			if (other.idPeriodo != null)
				return false;
		} else if (!idPeriodo.equals(other.idPeriodo))
			return false;
		if (idRut == null) {
			if (other.idRut != null)
				return false;
		} else if (!idRut.equals(other.idRut))
			return false;
		if (montoEbs == null) {
			if (other.montoEbs != null)
				return false;
		} else if (!montoEbs.equals(other.montoEbs))
			return false;
		if (montoMiles == null) {
			if (other.montoMiles != null)
				return false;
		} else if (!montoMiles.equals(other.montoMiles))
			return false;
		if (montoPesos == null) {
			if (other.montoPesos != null)
				return false;
		} else if (!montoPesos.equals(other.montoPesos))
			return false;
		if (montoReclasificacion == null) {
			if (other.montoReclasificacion != null)
				return false;
		} else if (!montoReclasificacion.equals(other.montoReclasificacion))
			return false;
		if (periodoEmpresa == null) {
			if (other.periodoEmpresa != null)
				return false;
		} else if (!periodoEmpresa.equals(other.periodoEmpresa))
			return false;
		return true;
	}

	public BigDecimal getMontoPesosNuevo() {
		return montoPesosNuevo;
	}

	public void setMontoPesosNuevo(BigDecimal montoPesosNuevo) {
		this.montoPesosNuevo = montoPesosNuevo;
	}

	public CuentaContable getCuentaContable() {
		return cuentaContable;
	}

	public void setCuentaContable(CuentaContable cuentaContable) {
		this.cuentaContable = cuentaContable;
	}
	
}
