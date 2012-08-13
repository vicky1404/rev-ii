package cl.mdr.ifrs.ejb.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

import cl.mdr.ifrs.ejb.common.Constantes;

@Entity
@NamedQueries( { @NamedQuery(name = VersionEeff.FIND_ALL, query = "select o from VersionEeff o"),
                 @NamedQuery(name = VersionEeff.FIND_BY_PERIOD, query = "select o from VersionEeff o where o.periodoEmpresa.idPeriodo = :idPeriodo order by o.version"),
                 @NamedQuery(name = VersionEeff.FIND_VIGENTE_BY_PERIOD, query = "select o from VersionEeff o where o.periodoEmpresa.idPeriodo = :idPeriodo and o.vigencia = 1"),
                 
                 @NamedQuery(name = VersionEeff.FIN_MAX_VERSION_BY_PERIODO_EMPRESA, 
			                 query =" select " +
			                 		" max(o.version) " +
			                 		" from VersionEeff o " +
			                 		" where " +
			                 		" o.periodoEmpresa.idPeriodo = :idPeriodo and " +
			                 		" o.periodoEmpresa.idRut = :idRut"),
                 		
                 @NamedQuery(name = VersionEeff.UPDATE_VIGENCIA_BY_PERIODO_EMPRESA, 
			                 query = " update VersionEeff o " +
			                 		 " set o.vigencia = 0 " +
			                 		 " where o.periodoEmpresa.idPeriodo = :idPeriodo " +
			                 		 " and o.periodoEmpresa.idRut = :idRut")
})

@Table(name = Constantes.VERSION_EEFF)
public class VersionEeff implements Serializable {
	private static final long serialVersionUID = -6581463781943497902L;
	public static final String FIND_ALL = "VersionEeff.findAll";
    public static final String FIND_BY_PERIOD = "VersionEeff.findByPeriod";
    public static final String FIND_VIGENTE_BY_PERIOD = "VersionEeff.findVigenteByPeriod";
    public static final String FIN_MAX_VERSION_BY_PERIODO_EMPRESA = "VersionEeff.findByMaxVersionByPeriodoEmpresa";
    public static final String UPDATE_VIGENCIA_BY_PERIODO_EMPRESA = "VersionEeff.updateVigenciaByPeriodoEmpresa";
    
    @Id    
    @GeneratedValue(generator="ID_GEN_VERSION_EEFF")
    @SequenceGenerator(name="ID_GEN_VERSION_EEFF", sequenceName = "SEQ_VERSION_EEFF" ,allocationSize = 1)
    @Column(name = "ID_VERSION_EEFF", nullable = false)
    @Expose
    private Long idVersionEeff;
    
    @Column(length = 256)
    @Expose
    private String usuario;
    
    @Column(nullable = false)
    @Expose
    private Long version;
    
    @Column(nullable = false)
    @Expose
    private Long vigencia;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ESTADO_EEFF")
    @Expose
    private TipoEstadoEeff tipoEstadoEeff;
    
    @OneToMany(mappedBy = "versionEeff", cascade = CascadeType.PERSIST)
    private List<EstadoFinanciero> estadoFinancieroList;

	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns( { @JoinColumn(name = "ID_PERIODO", referencedColumnName = "ID_PERIODO"),
			        @JoinColumn(name = "ID_RUT", referencedColumnName = "ID_RUT")})
	@Expose
	private PeriodoEmpresa periodoEmpresa;

    public VersionEeff() {
    }


    public Long getIdVersionEeff() {
        return idVersionEeff;
    }

    public void setIdVersionEeff(Long idVersionEeff) {
        this.idVersionEeff = idVersionEeff;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getVigencia() {
        return vigencia;
    }

    public void setVigencia(Long vigencia) {
        this.vigencia = vigencia;
    }

    public TipoEstadoEeff getTipoEstadoEeff() {
        return tipoEstadoEeff;
    }

    public void setTipoEstadoEeff(TipoEstadoEeff estadoEeff) {
        this.tipoEstadoEeff = estadoEeff;
    }

    public void setEstadoFinancieroList(List<EstadoFinanciero> estadoFinancieroList) {
        this.estadoFinancieroList = estadoFinancieroList;
    }

    public List<EstadoFinanciero> getEstadoFinancieroList() {
        return estadoFinancieroList;
    }
    
    public PeriodoEmpresa getPeriodoEmpresa() {
		return periodoEmpresa;
	}


	public void setPeriodoEmpresa(PeriodoEmpresa periodoEmpresa) {
		this.periodoEmpresa = periodoEmpresa;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		VersionEeff other = (VersionEeff) obj;
		if (idVersionEeff == null) {
			if (other.idVersionEeff != null)
				return false;
		} else if (!idVersionEeff.equals(other.idVersionEeff))
			return false;
		return true;
	}
}
