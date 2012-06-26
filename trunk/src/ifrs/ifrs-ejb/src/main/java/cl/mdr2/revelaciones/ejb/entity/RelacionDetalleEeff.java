package cl.bicevida.revelaciones.ejb.entity;

import cl.bicevida.revelaciones.ejb.common.Constantes;
import cl.bicevida.revelaciones.ejb.entity.pk.RelacionDetalleEeffPK;

import java.io.Serializable;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@NamedQueries( { @NamedQuery(name = "RelacionDetalleEeff1.findAll", query = "select o from RelacionDetalleEeff o") })
@Table(name = Constantes.RELACION_DETALLE_EEFF)
@IdClass(RelacionDetalleEeffPK.class)

public class RelacionDetalleEeff implements Serializable {
    
    @SuppressWarnings("compatibility:7150370316002379901")
    private static final long serialVersionUID = -2121624962103986848L;
    
    @Id
    @Column(name = "ID_CUENTA", nullable = false)
    private Long idCuenta;
    
    @Id
    @Column(name = "ID_FECU", nullable = false)
    private Long idFecu;
    
    @Id
    @Column(name = "ID_PERIODO", nullable = false)
    private Long idPeriodo;
    
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
    
    @ManyToOne
    @JoinColumns( { @JoinColumn(name = "ID_COLUMNA", referencedColumnName = "ID_COLUMNA"),
                    @JoinColumn(name = "ID_GRILLA", referencedColumnName = "ID_GRILLA"),
                    @JoinColumn(name = "ID_FILA", referencedColumnName = "ID_FILA") })
    private Celda celda5;
    

    public RelacionDetalleEeff() {
    }

    public RelacionDetalleEeff(String descripcionCuenta,
                                BigDecimal ebs, Celda celda5, Long idCuenta, 
                                BigDecimal montoMiles, BigDecimal montoPesos, BigDecimal reclasificacion) {
        this.descripcionCuenta = descripcionCuenta;
        this.montoEbs = ebs;
        this.celda5 = celda5;
        this.idCuenta = idCuenta;
        this.montoMiles = montoMiles;
        this.montoPesos = montoPesos;
        this.montoReclasificacion = reclasificacion;
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
    
    public void copyDetalleEeff(final DetalleEeff detalleEeff, final Celda celda,final Periodo periodo){
        
        this.idCuenta = detalleEeff.getIdCuenta();
        this.idFecu = detalleEeff.getIdFecu();
        this.idPeriodo = periodo.getIdPeriodo();
        this.descripcionCuenta = detalleEeff.getDescripcionCuenta();
        this.montoEbs = detalleEeff.getMontoEbs();
        this.montoMiles = detalleEeff.getMontoMiles();
        this.montoPesos = detalleEeff.getMontoPesos();
        this.montoReclasificacion = detalleEeff.getMontoReclasificacion();
        this.celda5 = celda;
        
    }
}
