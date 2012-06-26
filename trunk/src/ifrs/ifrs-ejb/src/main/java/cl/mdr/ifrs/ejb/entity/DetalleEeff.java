package cl.mdr.ifrs.ejb.entity;

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

import cl.mdr.ifrs.ejb.common.Constantes;
import cl.mdr.ifrs.ejb.entity.pk.DetalleEeffPK;


@Entity
@IdClass(DetalleEeffPK.class)
@NamedQueries( { @NamedQuery(name = DetalleEeff.FIND_ALL, query = "select o from DetalleEeff o"),
                 @NamedQuery(name = DetalleEeff.FIND_BY_EEFF, query = "select o from DetalleEeff o where o.estadoFinanciero1 = :eeff"),
                 @NamedQuery(name = DetalleEeff.FIND_BY_VERSION, query = "select o from DetalleEeff o where o.idVersionEeff = :idVersionEeff")})
@Table(name = Constantes.DETALLE_EEFF)
public class DetalleEeff implements Serializable {
	private static final long serialVersionUID = -5505476462941937517L;
	public static final String FIND_ALL = "DetalleEeff.findAll";
    public static final String FIND_BY_EEFF = "DetalleEeff.findByEeff";
    public static final String FIND_BY_VERSION = "DetalleEeff.findByVersion";
    
    @Id
    @Column(name = "ID_CUENTA", nullable = false)
    private Long idCuenta;
    
    @Id
    @Column(name = "ID_FECU", nullable = false, insertable = false, updatable = false)
    private Long idFecu;
    
    @Id
    @Column(name = "ID_VERSION_EEFF", nullable = false, insertable = false, updatable = false)
    private Long idVersionEeff;
    
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
    @JoinColumns( { @JoinColumn(name = "ID_FECU", referencedColumnName = "ID_FECU"),
                    @JoinColumn(name = "ID_VERSION_EEFF", referencedColumnName = "ID_VERSION_EEFF") })
    private EstadoFinanciero estadoFinanciero1;

    public DetalleEeff() {
    }

    public DetalleEeff(String descripcionCuenta, String descripcionFecu, BigDecimal ebs,
                       Long idCuenta, EstadoFinanciero estadoFinanciero1, BigDecimal montoMiles,
                       BigDecimal montoPesos, BigDecimal reclasificacion) {
        this.descripcionCuenta = descripcionCuenta;
        this.montoEbs = ebs;
        this.idCuenta = idCuenta;
        this.estadoFinanciero1 = estadoFinanciero1;
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
}
