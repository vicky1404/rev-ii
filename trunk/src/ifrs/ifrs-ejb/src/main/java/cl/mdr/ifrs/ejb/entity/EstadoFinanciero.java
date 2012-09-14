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
import javax.persistence.Transient;

import com.google.gson.annotations.Expose;

import cl.mdr.ifrs.ejb.common.Constantes;
import cl.mdr.ifrs.ejb.cross.EeffUtil;
import cl.mdr.ifrs.ejb.entity.pk.EstadoFinancieroPK;

@Entity
@NamedQueries( { @NamedQuery(name = EstadoFinanciero.FIND_ALL, query = "select o from EstadoFinanciero o"),
    @NamedQuery(name = EstadoFinanciero.FIND_VIGENTE_BY_PERIODO_EMPRESA, query = "select o from EstadoFinanciero o, VersionEeff v, CodigoFecu f where f.idFecu = o.idFecu and v.idVersionEeff = o.idVersionEeff and v.periodoEmpresa.idPeriodo = :idPeriodo and v.periodoEmpresa.idRut = :idRut and v.vigencia = 1 order by o.idFecu desc"),
    @NamedQuery(name = EstadoFinanciero.FIND_BY_VERSION, query = "select o from EstadoFinanciero o where o.idVersionEeff = :idVersionEeff order by o.idFecu"),
    @NamedQuery(name = EstadoFinanciero.FIND_EAGER_BY_VERSION, query = "select distinct o from EstadoFinanciero o join fetch o.detalleEeffList4 where o.idVersionEeff = :idVersionEeff order by o.idFecu"),
    @NamedQuery(name = EstadoFinanciero.FIND_BY_LIKE_FECU, query = "select distinct o from EstadoFinanciero o join fetch o.detalleEeffList4 where o.idVersionEeff = :idVersionEeff and str(o.idFecu) like :likeFecu order by o.idFecu")})
@Table(name = Constantes.EEFF)
@IdClass(EstadoFinancieroPK.class)
public class EstadoFinanciero implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 138435343926279767L;
	public static final String FIND_ALL = "EstadoFinanciero.findAll";
    public static final String FIND_VIGENTE_BY_PERIODO_EMPRESA = "EstadoFinanciero.findVigenteByPeriodo";
    public static final String FIND_BY_VERSION = "EstadoFinanciero.findByVersion";
    public static final String FIND_EAGER_BY_VERSION = "EstadoFinanciero.findEagerByVersion";
    public static final String FIND_BY_LIKE_FECU = "EstadoFinanciero.findByLikeFecu";
    public static final String FIND_BY_LIKE_CUENTA= "EstadoFinanciero.findByLikeCuenta";
    
    
    @Id
    @Column(name = "ID_FECU", nullable = false, insertable = false, updatable = false)
    @Expose
    private Long idFecu;
    
    @Id
    @Column(name = "ID_VERSION_EEFF", nullable = false, insertable = false, updatable = false)
    @Expose
    private Long idVersionEeff;
    
    @Column(name = "MONTO_TOTAL")
    @Expose
    private BigDecimal montoTotal;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_VERSION_EEFF")
    @Expose
    private VersionEeff versionEeff;      
    
    @OneToMany(mappedBy = "estadoFinanciero1", cascade = CascadeType.PERSIST,fetch = FetchType.LAZY )
    private List<DetalleEeff> detalleEeffList4;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_FECU")
    @Expose
    private CodigoFecu codigoFecu;
    
    @Transient
    private BigDecimal montoTotalNuevo;
    
    @Transient
    private boolean selectedForMapping;

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
    
    public String getFecuFormat(){
        if(idFecu!=null)
            return EeffUtil.formatFecu(idFecu);
        else
            return "";
    }

    public void setMontoTotalNuevo(BigDecimal montoTotalNuevo) {
        this.montoTotalNuevo = montoTotalNuevo;
    }

    public BigDecimal getMontoTotalNuevo() {
        return montoTotalNuevo;
    }

    public void setSelectedForMapping(boolean selectedForMapping) {
        this.selectedForMapping = selectedForMapping;
    }

    public boolean isSelectedForMapping() {
        return selectedForMapping;
    }
    
    @Override
   	public int hashCode() {
   		final int prime = 31;
   		int result = 1;
   		result = prime * result + ((idFecu == null) ? 0 : idFecu.hashCode());
   		result = prime * result
   				+ ((idVersionEeff == null) ? 0 : idVersionEeff.hashCode());
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
   		EstadoFinanciero other = (EstadoFinanciero) obj;
   		if (idFecu == null) {
   			if (other.idFecu != null)
   				return false;
   		} else if (!idFecu.equals(other.idFecu))
   			return false;
   		if (idVersionEeff == null) {
   			if (other.idVersionEeff != null)
   				return false;
   		} else if (!idVersionEeff.equals(other.idVersionEeff))
   			return false;
   		return true;
   	}
}

