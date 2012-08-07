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

import cl.mdr.ifrs.ejb.common.Constantes;
import cl.mdr.ifrs.ejb.cross.EeffUtil;
import cl.mdr.ifrs.ejb.entity.pk.RelacionDetalleEeffPK;


@Entity
@NamedQueries( { @NamedQuery(name = "RelacionDetalleEeff1.findAll", query = "select o from RelacionDetalleEeff o") })
@Table(name = Constantes.RELACION_DETALLE_EEFF)
@IdClass(RelacionDetalleEeffPK.class)

public class RelacionDetalleEeff implements Serializable {
    
	
   
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -4991893502991008001L;
	
	public static final String FIND_ALL = "RelacionDetalleEeff.findAll";
    public static final String FIND_BY_PERIODO_FECU_CUENTA = "RelacionDetalleEeff.findByPeriodoFecuCuenta";
    public static final String FIND_BY_PERIODO = "RelacionDetalleEeff.findByPeriodo";
    public static final String DELETE_BY_CELDA = "RelacionDetalleEeff.deleteByCelda";
    
    @Id
    @Column(name = "ID_CUENTA", nullable = false)
    private Long idCuenta;
    
    @Id
    @Column(name = "ID_FECU", nullable = false)
    private Long idFecu;
    
    @Id
    @Column(name = "ID_PERIODO", nullable = false, insertable = false, updatable = false)
    private Long idPeriodo;
    
    @Id
    @Column(name = "ID_GRILLA", nullable = false, insertable = false, updatable = false)
    private Long idGrilla;
    
    @Id
    @Column(name = "ID_COLUMNA", nullable = false, insertable = false, updatable = false)
    private Long idColumna;
    
    @Id
    @Column(name = "ID_FILA", nullable = false, insertable = false, updatable = false)
    private Long idFila;
    
    @Id
    @Column(name = "ID_RUT", nullable = false, insertable = false, updatable = false)
    private Long idRut;
    
    @Column(name = "DESCRIPCION_CUENTA", length = 256)
    private String descripcionCuenta;
    
    @Column(name = "MONTO_EBS", length = 256)
    private BigDecimal montoEbs;
        
    @Column(name = "MONTO_MILES")
    private BigDecimal montoMiles;
    
    @Column(name = "MONTO_PESOS")
    private BigDecimal montoPesos;
    
    @Column(name = "MONTO_RECLASIFICACION")
    private BigDecimal montoReclasificacion;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns( { @JoinColumn(name = "ID_PERIODO", referencedColumnName = "ID_PERIODO"),
			        @JoinColumn(name = "ID_RUT", referencedColumnName = "ID_RUT")})
    private PeriodoEmpresa periodoEmpresa;
    
   
    
    @ManyToOne
    @JoinColumns( { @JoinColumn(name = "ID_COLUMNA", referencedColumnName = "ID_COLUMNA"),
                    @JoinColumn(name = "ID_GRILLA", referencedColumnName = "ID_GRILLA"),
                    @JoinColumn(name = "ID_FILA", referencedColumnName = "ID_FILA") })
    private Celda celda5;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PERIODO")
    private Periodo periodo;
    

    public RelacionDetalleEeff() {
    }

    public String getDescripcionCuenta() {
        return descripcionCuenta;
    }

    public void setDescripcionCuenta(String descripcionCuenta) {
        this.descripcionCuenta = descripcionCuenta;
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

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Periodo getPeriodo() {
        return periodo;
    }
    
    public String getFecuFormat(){
        if(idFecu!=null)
            return EeffUtil.formatFecu(idFecu);
        else
            return "";
    }
    
    public void copyDetalleEeff(final DetalleEeff detalleEeff, final Celda celda,final PeriodoEmpresa periodoEmpresa){
        
        this.idCuenta = detalleEeff.getIdCuenta();
        this.idFecu = detalleEeff.getIdFecu();
        if(periodoEmpresa != null){
        	this.idPeriodo = periodoEmpresa.getIdPeriodo();
        	this.idRut = periodoEmpresa.getIdRut();
        }
        this.periodoEmpresa = periodoEmpresa;
        this.descripcionCuenta = detalleEeff.getDescripcionCuenta();
        this.montoEbs = detalleEeff.getMontoEbs();
        this.montoMiles = detalleEeff.getMontoMiles();
        this.montoPesos = detalleEeff.getMontoPesos();
        this.montoReclasificacion = detalleEeff.getMontoReclasificacion();
        this.celda5 = celda;
        
    }

	public PeriodoEmpresa getPeriodoEmpresa() {
		return periodoEmpresa;
	}

	public void setPeriodoEmpresa(PeriodoEmpresa periodoEmpresa) {
		this.periodoEmpresa = periodoEmpresa;
	}

	public Long getIdRut() {
		return idRut;
	}

	public void setIdRut(Long idRut) {
		this.idRut = idRut;
	}
}
