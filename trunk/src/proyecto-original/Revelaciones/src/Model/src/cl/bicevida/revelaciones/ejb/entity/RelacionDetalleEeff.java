package cl.bicevida.revelaciones.ejb.entity;

import cl.bicevida.revelaciones.ejb.cross.EeffUtil;
import cl.bicevida.revelaciones.ejb.entity.pk.RelacionDetalleEeffPK;

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


@Entity
@NamedQueries( { @NamedQuery(name = RelacionDetalleEeff.FIND_ALL, query = "select o from RelacionDetalleEeff o"),
                 @NamedQuery(name = RelacionDetalleEeff.FIND_BY_PERIODO, query = "select o from RelacionDetalleEeff o where o.periodo.idPeriodo = :idPeriodo order by o.idCuenta"),
                 @NamedQuery(name = RelacionDetalleEeff.FIND_BY_PERIODO_FECU_CUENTA, query = "select o from RelacionDetalleEeff o where o.periodo.idPeriodo = :idPeriodo and o.idFecu = :idFecu and o.idCuenta = :idCuenta"),
                 @NamedQuery(name = RelacionDetalleEeff.DELETE_BY_CELDA, query = "delete from RelacionDetalleEeff o where o.celda5 = :celda"),
                 @NamedQuery(name = RelacionDetalleEeff.DELETE_BY_GRILLA_PERIODO, query = "delete from RelacionDetalleEeff o where o.idGrilla = :idGrilla and o.idPeriodo = :idPeriodo"),
                 @NamedQuery(name = RelacionDetalleEeff.DELETE_BY_FECU_CUENTA_PERIODO, query = "delete from RelacionDetalleEeff o where o.idFecu = :idFecu and o.idCuenta = :idCuenta and o.idPeriodo = :idPeriodo"),
                 @NamedQuery(name = RelacionDetalleEeff.UPDATE_MONTO_BY_FECU_CUENTA_PERIODO, query = "update RelacionDetalleEeff o set o.montoMilesValidarMapeo = :montoTotal where o.idFecu = :idFecu and o.idCuenta = :idCuenta and o.idPeriodo = :idPeriodo ")})
@Table(name = "REV_RELACION_DETALLE_EEFF")
@IdClass(RelacionDetalleEeffPK.class)
public class RelacionDetalleEeff implements Serializable {
    
    
    public static final String FIND_ALL = "RelacionDetalleEeff.findAll";
    public static final String FIND_BY_PERIODO_FECU_CUENTA = "RelacionDetalleEeff.findByPeriodoFecuCuenta";
    public static final String FIND_BY_PERIODO = "RelacionDetalleEeff.findByPeriodo";
    public static final String DELETE_BY_CELDA = "RelacionDetalleEeff.deleteByCelda";
    public static final String DELETE_BY_GRILLA_PERIODO = "RelacionDetalleEeff.deleteByGrillaPeriodo";
    public static final String DELETE_BY_FECU_CUENTA_PERIODO = "RelacionDetalleEeff.deleteByCuentaFecuPeriodo";
    public static final String UPDATE_MONTO_BY_FECU_CUENTA_PERIODO = "RelacionDetalleEeff.updateMontoByFecuCuentaPeriodo";
    
    @SuppressWarnings("compatibility:-4464908447263165812")
    private static final long serialVersionUID = 8260228090213209786L;

    @Id
    @Column(name = "ID_CUENTA", nullable = false, insertable = false, updatable = false)
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
    
    @Column(name = "MONTO_EBS", length = 256)
    private BigDecimal montoEbs;
        
    @Column(name = "MONTO_MILES")
    private BigDecimal montoMilesValidarMapeo;
    
    @Column(name = "MONTO_PESOS")
    private BigDecimal montoPesos;
    
    @Column(name = "MONTO_RECLASIFICACION")
    private BigDecimal montoReclasificacion;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_CUENTA")
    private CuentaContable cuentaContable;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns( { @JoinColumn(name = "ID_COLUMNA", referencedColumnName = "ID_COLUMNA"),
                    @JoinColumn(name = "ID_GRILLA", referencedColumnName = "ID_GRILLA"),
                    @JoinColumn(name = "ID_FILA", referencedColumnName = "ID_FILA") })
    private Celda celda5;
    
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PERIODO")
    private Periodo periodo;
    
    @Column(name = "MONTO_PESOS_MIL")
    private BigDecimal montoXBRL;
    
    @Transient
    private BigDecimal montoMilesValidarMapeoNuevo;

    public RelacionDetalleEeff() {
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
    
    public void setCuentaContable(CuentaContable cuentaContable) {
        this.cuentaContable = cuentaContable;
    }

    public CuentaContable getCuentaContable() {
        return cuentaContable;
    }

    public void setMontoMilesValidarMapeo(BigDecimal montoMilesValidarMapeo) {
        this.montoMilesValidarMapeo = montoMilesValidarMapeo;
    }

    public BigDecimal getMontoMilesValidarMapeo() {
        return montoMilesValidarMapeo;
    }

    public void setMontoXBRL(BigDecimal montoXBRL) {
        this.montoXBRL = montoXBRL;
    }

    public BigDecimal getMontoXBRL() {
        return montoXBRL;
    }

    public void setMontoMilesValidarMapeoNuevo(BigDecimal montoMilesValidarMapeoNuevo) {
        this.montoMilesValidarMapeoNuevo = montoMilesValidarMapeoNuevo;
    }

    public BigDecimal getMontoMilesValidarMapeoNuevo() {
        return montoMilesValidarMapeoNuevo;
    }
    
    
    public void copyDetalleEeff(final DetalleEeff detalleEeff, final Celda celda,final Periodo periodo){
        
        this.idCuenta = detalleEeff.getIdCuenta();
        this.idFecu = detalleEeff.getIdFecu();
        this.idPeriodo = periodo.getIdPeriodo();
        this.periodo = periodo;
        this.cuentaContable = detalleEeff.getCuentaContable();
        this.montoEbs = detalleEeff.getMontoEbs();
        this.montoMilesValidarMapeo = detalleEeff.getMontoMilesValidarMapeo();
        this.montoPesos = detalleEeff.getMontoPesos();
        this.montoReclasificacion = detalleEeff.getMontoReclasificacion();
        this.montoXBRL = detalleEeff.getMontoXBRL();
        this.idGrilla = celda.getIdGrilla();
        this.idColumna = celda.getIdColumna();
        this.idFila = celda.getIdFila();
        this.celda5 = celda;
        
    }
}
