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
    @NamedQuery(name = RelacionDetalleEeff.FIND_BY_PERIODO_EMPRESA, query = "select o from RelacionDetalleEeff o where o.periodoEmpresa.idPeriodo = :idPeriodo and o.periodoEmpresa.idRut = :idRut  order by o.idCuenta"),
    @NamedQuery(name = RelacionDetalleEeff.FIND_BY_PERIODO_FECU_CUENTA, query = "select o from RelacionDetalleEeff o where o.periodoEmpresa.idPeriodo = :idPeriodo and o.periodoEmpresa.idRut = :idRut and o.idFecu = :idFecu and o.idCuenta = :idCuenta"),
    @NamedQuery(name = RelacionDetalleEeff.DELETE_BY_CELDA, query = "delete from RelacionDetalleEeff o where o.celda5 = :celda"),
    @NamedQuery(name = RelacionDetalleEeff.DELETE_BY_GRILLA_PERIODO_EMPRESA, query = "delete from RelacionDetalleEeff o where o.idGrilla = :idGrilla and o.idPeriodo = :idPeriodo and o.idRut = :idRut"),
    @NamedQuery(name = RelacionDetalleEeff.FIND_BY_CELDA, query = "select o from RelacionDetalleEeff o where o.celda5 = :celda"),
    @NamedQuery(name = RelacionDetalleEeff.FIND_BY_GRILLA, query = "select o from RelacionDetalleEeff o where o.idGrilla = :idGrilla order by o.idColumna, o.idFila")})

@Table(name = Constantes.RELACION_DETALLE_EEFF)
@IdClass(RelacionDetalleEeffPK.class)
public class RelacionDetalleEeff implements Serializable {

    public static final String FIND_ALL = "RelacionDetalleEeff.findAll";
    public static final String FIND_BY_PERIODO_FECU_CUENTA = "RelacionDetalleEeff.findByPeriodoFecuCuenta";
    public static final String FIND_BY_PERIODO_EMPRESA = "RelacionDetalleEeff.findByPeriodo";
    public static final String DELETE_BY_CELDA = "RelacionDetalleEeff.deleteByCelda";
    public static final String DELETE_BY_GRILLA_PERIODO_EMPRESA = "RelacionDetalleEeff.deleteByGrillaPeriodo";
    public static final String FIND_BY_CELDA = "RelacionDetalleEeff.findByCelda";
    public static final String FIND_BY_GRILLA = "RelacionDetalleEeff.findByGrilla";
    
    @Id
    @Column(name = "ID_CUENTA", nullable = false, insertable = false, updatable = false)
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
    
    @Id
    @Column(name = "ID_GRILLA", nullable = false, insertable = false, updatable = false)
    @Expose
    private Long idGrilla;
    
    @Id
    @Column(name = "ID_COLUMNA", nullable = false, insertable = false, updatable = false)
    @Expose
    private Long idColumna;
    
    @Id
    @Column(name = "ID_FILA", nullable = false, insertable = false, updatable = false)
    @Expose
    private Long idFila;
            
    @Column(name = "MONTO_MILES")
    @Expose
    private BigDecimal montoMilesValidarMapeo;
    
    @Column(name = "MONTO_PESOS")
    @Expose
    private BigDecimal montoPesos;
        
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_CUENTA")
    @Expose
    private CuentaContable cuentaContable;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns( { @JoinColumn(name = "ID_COLUMNA", referencedColumnName = "ID_COLUMNA"),
                    @JoinColumn(name = "ID_GRILLA", referencedColumnName = "ID_GRILLA"),
                    @JoinColumn(name = "ID_FILA", referencedColumnName = "ID_FILA") })
    @Expose
    private Celda celda5;
    
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns( { @JoinColumn(name = "ID_PERIODO", referencedColumnName = "ID_PERIODO"),
			        @JoinColumn(name = "ID_RUT", referencedColumnName = "ID_RUT")})
    @Expose
    private PeriodoEmpresa periodoEmpresa;
    
    @Column(name = "MONTO_PESOS_MIL")
    @Expose
    private BigDecimal montoXBRL;
    
    @Transient
    @Expose
    private BigDecimal montoMilesValidarMapeoNuevo;

    public RelacionDetalleEeff() {
    }

 
    public Long getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(Long idCuenta) {
        this.idCuenta = idCuenta;
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


   
    public BigDecimal getMontoPesos() {
        return montoPesos;
    }

    public void setMontoPesos(BigDecimal montoPesos) {
        this.montoPesos = montoPesos;
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
    

    public void setIdGrilla(Long idGrilla) {
        this.idGrilla = idGrilla;
    }

    public Long getIdGrilla() {
        return idGrilla;
    }

    public void setIdColumna(Long idColumna) {
        this.idColumna = idColumna;
    }

    public Long getIdColumna() {
        return idColumna;
    }

    public void setIdFila(Long idFila) {
        this.idFila = idFila;
    }

    public Long getIdFila() {
        return idFila;
    }
    
    public String getFecuFormat(){
        if(idFecu!=null)
            return EeffUtil.formatFecu(idFecu);
        else
            return "";
    }

   
    
    public void setCuentaContable(CuentaContable cuentaContable) {
        this.cuentaContable = cuentaContable;
    }

    public CuentaContable getCuentaContable() {
        return cuentaContable;
    }
    
    public void copyDetalleEeff(final DetalleEeff detalleEeff, final Celda celda,final PeriodoEmpresa periodoEmpresa){
        
        this.idCuenta = detalleEeff.getIdCuenta();
        this.idFecu = detalleEeff.getIdFecu();
        if(periodoEmpresa != null){
        	this.idPeriodo = periodoEmpresa.getIdPeriodo();
        	this.idRut = periodoEmpresa.getIdRut();
        }
        this.periodoEmpresa = periodoEmpresa;
        this.cuentaContable = detalleEeff.getCuentaContable();
       
        this.montoMilesValidarMapeo = detalleEeff.getMontoMilesValidarMapeo();
        this.montoPesos = detalleEeff.getMontoPesos();
      
        this.montoXBRL = detalleEeff.getMontoXBRL();
        this.idGrilla = celda.getIdGrilla();
        this.idColumna = celda.getIdColumna();
        this.idFila = celda.getIdFila();
        this.celda5 = celda;
    }
    

	public BigDecimal getMontoMilesValidarMapeo() {
		return montoMilesValidarMapeo;
	}

	public void setMontoMilesValidarMapeo(BigDecimal montoMilesValidarMapeo) {
		this.montoMilesValidarMapeo = montoMilesValidarMapeo;
	}

	public BigDecimal getMontoXBRL() {
		return montoXBRL;
	}

	public void setMontoXBRL(BigDecimal montoXBRL) {
		this.montoXBRL = montoXBRL;
	}

	public BigDecimal getMontoMilesValidarMapeoNuevo() {
		return montoMilesValidarMapeoNuevo;
	}

	public void setMontoMilesValidarMapeoNuevo(
			BigDecimal montoMilesValidarMapeoNuevo) {
		this.montoMilesValidarMapeoNuevo = montoMilesValidarMapeoNuevo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		return true;
	}
    
    

}
