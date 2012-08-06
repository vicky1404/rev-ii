package cl.mdr.ifrs.ejb.entity;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import cl.mdr.ifrs.ejb.common.Constantes;
import cl.mdr.ifrs.ejb.entity.pk.EstadoFinancieroPK;


@Entity
@NamedQueries( { @NamedQuery(name = EstadoFinanciero.FIND_ALL, query = "select o from EstadoFinanciero o"),
                 @NamedQuery(name = EstadoFinanciero.FIND_VIGENTE_BY_PERIODO, query = "select o from EstadoFinanciero o, VersionEeff v where v.idVersionEeff = o.idVersionEeff and v.periodoEmpresa.idPeriodo = :idPeriodo and v.vigencia = 1"),
                 @NamedQuery(name = EstadoFinanciero.FIND_BY_VERSION, query = "select o from EstadoFinanciero o where o.idVersionEeff = :idVersionEeff")})
@Table(name = Constantes.EEFF)
@IdClass(EstadoFinancieroPK.class)
public class EstadoFinanciero implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 7036236916516486290L;
	public static final String FIND_ALL = "EstadoFinanciero.findAll";
    public static final String FIND_VIGENTE_BY_PERIODO = "EstadoFinanciero.findVigenteByPeriodo";
    public static final String FIND_BY_VERSION = "EstadoFinanciero.findByVersion";
    
    
    @Id
    @Column(name = "ID_FECU", nullable = false, insertable = false, updatable = false)
    private Long idFecu;
    
    @Id
    @Column(name = "ID_VERSION_EEFF", nullable = false, insertable = false, updatable = false)
    private Long idVersionEeff;
    
    @Column(name = "MONTO_TOTAL")
    private BigDecimal montoTotal;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_VERSION_EEFF")
    private VersionEeff versionEeff;
    
    @OneToMany(mappedBy = "estadoFinanciero1", cascade = CascadeType.PERSIST,fetch = FetchType.LAZY )
    private List<DetalleEeff> detalleEeffList4;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_FECU")
    private CodigoFecu codigoFecu;

    public EstadoFinanciero() {
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

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setVersionEeff(VersionEeff versionEeff) {
        if(versionEeff!=null){
            idVersionEeff = versionEeff.getIdVersionEeff();
        }
        this.versionEeff = versionEeff;
    }

    public VersionEeff getVersionEeff() {
        return versionEeff;
    }

    public void setDetalleEeffList4(List<DetalleEeff> detalleEeffList4) {
        this.detalleEeffList4 = detalleEeffList4;
    }

    public List<DetalleEeff> getDetalleEeffList4() {
        return detalleEeffList4;
    }

    public void setCodigoFecu(CodigoFecu codigoFecu) {
        
        if(codigoFecu!=null)
            this.idFecu = codigoFecu.getIdFecu();
        
        this.codigoFecu = codigoFecu;
    }

    public CodigoFecu getCodigoFecu() {
        return codigoFecu;
    }
}
