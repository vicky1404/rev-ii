package cl.mdr.ifrs.ejb.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import cl.mdr.ifrs.ejb.common.Constantes;

import com.google.gson.annotations.Expose;


@Entity
@NamedQueries( { @NamedQuery(name = VersionEeff.FIND_ALL, query = "select o from VersionEeff o"),
    @NamedQuery(name = VersionEeff.FIND_BY_PERIODO_EMPRESA, query = "select o from VersionEeff o where o.periodoEmpresa.idPeriodo = :idPeriodo and o.periodoEmpresa.idRut = :idRut order by o.version"),
    @NamedQuery(name = VersionEeff.FIND_VIGENTE_BY_PERIODO_EMPRESA, query = "select o from VersionEeff o where o.periodoEmpresa.idPeriodo = :idPeriodo and o.periodoEmpresa.idRut = :idRut and  o.vigencia = 1"),
    
    @NamedQuery(name = VersionEeff.FIN_MAX_VERSION_BY_PERIODO_EMPRESA, 
                query =" select " +
                		" max(o.version) " +
                		" from VersionEeff o " +
                		" where " +
                		" o.periodoEmpresa.idPeriodo = :idPeriodo and " +
                		" o.periodoEmpresa.idRut = :idRut"),
    		
    @NamedQuery(name = VersionEeff.UPDATE_VIGENCIA_BY_PERIODO_EMPRESA, 
                query = " update VersionEeff set vigencia = :vigencia where periodoEmpresa.idPeriodo = :idPeriodo and periodoEmpresa.idRut = :idRut")
})
@Table(name = Constantes.VERSION_EEFF)
public class VersionEeff implements Serializable {
	private static final long serialVersionUID = 3871781069569025001L;
	
	public static final String FIND_ALL = "VersionEeff.findAll";
    public static final String FIND_BY_PERIODO_EMPRESA = "VersionEeff.findByPeriodEmpresa";
    public static final String FIND_VIGENTE_BY_PERIODO_EMPRESA = "VersionEeff.findVigenteByPeriodoEmpresa";
    public static final String FIN_MAX_VERSION_BY_PERIODO_EMPRESA = "VersionEeff.findByMaxVersionByPeriodoEmpresa";
    public static final String UPDATE_VIGENCIA_BY_PERIODO_EMPRESA = "VersionEeff.updateVigenciaByPeriodoEmpresa";
    
    @Id   
    @Column(name = "ID_VERSION_EEFF", nullable = false)
    private Long idVersionEeff;
    
    @Column(length = 256)
    private String usuario;
    
    @Column(nullable = false)
    private Long version;
    
    @Column(nullable = false)
    private Long vigencia;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ESTADO_EEFF")
    private TipoEstadoEeff tipoEstadoEeff;
    
    @OneToMany(mappedBy = "versionEeff", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<EstadoFinanciero> estadoFinancieroList;
    
    @Expose
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns( { @JoinColumn(name = "ID_PERIODO", referencedColumnName = "ID_PERIODO"),
			        @JoinColumn(name = "ID_RUT", referencedColumnName = "ID_RUT")})
	private PeriodoEmpresa periodoEmpresa;

    
	public VersionEeff() {
    }

    public VersionEeff(TipoEstadoEeff estadoEeff, PeriodoEmpresa periodoEmpresa, Long idVersionEeff, String usuario,
                       Long version, Long vigencia) {
        this.tipoEstadoEeff = estadoEeff;
        this.periodoEmpresa = periodoEmpresa;
        this.idVersionEeff = idVersionEeff;
        this.usuario = usuario;
        this.version = version;
        this.vigencia = vigencia;
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

}
