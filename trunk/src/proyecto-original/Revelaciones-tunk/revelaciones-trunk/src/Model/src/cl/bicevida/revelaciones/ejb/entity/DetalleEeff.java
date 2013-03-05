package cl.bicevida.revelaciones.ejb.entity;

import cl.bicevida.revelaciones.ejb.cross.EeffUtil;
import cl.bicevida.revelaciones.ejb.entity.pk.CeldaPK;

import cl.bicevida.revelaciones.ejb.entity.pk.DetalleEeffPK;

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
@IdClass(DetalleEeffPK.class)
@NamedQueries( { @NamedQuery(name = DetalleEeff.FIND_ALL, query = "select o from DetalleEeff o"),
                 @NamedQuery(name = DetalleEeff.FIND_BY_EEFF, query = "select o from DetalleEeff o where o.estadoFinanciero1 = :eeff"),
                 @NamedQuery(name = DetalleEeff.FIND_BY_VERSION, query = "select o from DetalleEeff o where o.idVersionEeff = :idVersionEeff"),
                 @NamedQuery(name = DetalleEeff.FIND_BY_LIKE_CUENTA, query = "select o from DetalleEeff o where o.estadoFinanciero1.idVersionEeff = :idVersionEeff and o.idCuenta like :likeCuenta order by o.idFecu")})
@Table(name = "REV_DETALLE_EEFF")
public class DetalleEeff implements Serializable {
    
    public static final String FIND_ALL = "DetalleEeff.findAll";
    public static final String FIND_BY_EEFF = "DetalleEeff.findByEeff";
    public static final String FIND_BY_VERSION = "DetalleEeff.findByVersion";
    public static final String FIND_BY_LIKE_CUENTA = "DetalleEeff.findByLikeCuenta";
    
    
    @Id
    @Column(name = "ID_CUENTA", nullable = false, insertable = false, updatable = false)
    private Long idCuenta;
    
    @Id
    @Column(name = "ID_FECU", nullable = false, insertable = false, updatable = false)
    private Long idFecu;
    
    @Id
    @Column(name = "ID_VERSION_EEFF", nullable = false, insertable = false, updatable = false)
    private Long idVersionEeff;
    
    @Column(name = "MONTO_EBS", length = 256)
    private BigDecimal montoEbs;
    
    @Column(name = "MONTO_PESOS")
    private BigDecimal montoPesos;
    
    @Column(name = "MONTO_MILES")
    private BigDecimal montoMilesValidarMapeo;
    
    @Column(name = "MONTO_PESOS_MIL")
    private BigDecimal montoXBRL;
    
    @Column(name = "MONTO_RECLASIFICACION")
    private BigDecimal montoReclasificacion;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_CUENTA")
    private CuentaContable cuentaContable;
    
    @ManyToOne
    @JoinColumns( { @JoinColumn(name = "ID_FECU", referencedColumnName = "ID_FECU"),
                    @JoinColumn(name = "ID_VERSION_EEFF", referencedColumnName = "ID_VERSION_EEFF") })
    private EstadoFinanciero estadoFinanciero1;
    
    @Transient
    private BigDecimal montoMilesValidarMapeoNuevo;
    
    @Transient
    private boolean selectedForMapping;

    public DetalleEeff() {
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

    public EstadoFinanciero getEstadoFinanciero1() {
        return estadoFinanciero1;
    }

    public void setEstadoFinanciero1(EstadoFinanciero estadoFinanciero1) {
        if(estadoFinanciero1!=null){
            idFecu = estadoFinanciero1.getIdFecu();
            idVersionEeff = estadoFinanciero1.getIdVersionEeff();
        }
        this.estadoFinanciero1 = estadoFinanciero1;
    }

    public void setIdFecu(Long idFecu) {
        this.idFecu = idFecu;
    }

    public Long getIdFecu() {
        return idFecu;
    }

    public void setIdVersionEeff(Long idVersionEeff) {
        this.idVersionEeff = idVersionEeff;
    }

    public Long getIdVersionEeff() {
        return idVersionEeff;
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

    public void setSelectedForMapping(boolean selectedForMapping) {
        this.selectedForMapping = selectedForMapping;
    }

    public boolean isSelectedForMapping() {
        return selectedForMapping;
    }

    public void setMontoXBRL(BigDecimal montoXBRL) {
        this.montoXBRL = montoXBRL;
    }

    public BigDecimal getMontoXBRL() {
        return montoXBRL;
    }

    public void setMontoMilesValidarMapeo(BigDecimal montoMilesValidarMapeo) {
        this.montoMilesValidarMapeo = montoMilesValidarMapeo;
    }

    public BigDecimal getMontoMilesValidarMapeo() {
        return montoMilesValidarMapeo;
    }

    public void setMontoMilesValidarMapeoNuevo(BigDecimal montoMilesValidarMapeoNuevo) {
        this.montoMilesValidarMapeoNuevo = montoMilesValidarMapeoNuevo;
    }

    public BigDecimal getMontoMilesValidarMapeoNuevo() {
        return montoMilesValidarMapeoNuevo;
    }
}
