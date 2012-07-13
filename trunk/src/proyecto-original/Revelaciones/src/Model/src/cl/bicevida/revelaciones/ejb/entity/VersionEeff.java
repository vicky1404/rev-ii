package cl.bicevida.revelaciones.ejb.entity;


import java.io.Serializable;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@NamedQueries( { @NamedQuery(name = VersionEeff.FIND_ALL, query = "select o from VersionEeff o"),
                 @NamedQuery(name = VersionEeff.FIND_BY_PERIOD, query = "select o from VersionEeff o where o.periodo.idPeriodo = :idPeriodo order by o.version"),
                 @NamedQuery(name = VersionEeff.FIND_VIGENTE_BY_PERIOD, query = "select o from VersionEeff o where o.periodo.idPeriodo = :idPeriodo and o.vigencia = 1"),
                 @NamedQuery(name = VersionEeff.FIN_MAX_VERSION_BY_PERIODO, query = "select max(o.version) from VersionEeff o where o.periodo.idPeriodo = :idPeriodo"),
                 @NamedQuery(name = VersionEeff.UPDATE_VIGENCIA_BY_PERIODO, query = "update VersionEeff o set o.vigencia = 0 where o.periodo.idPeriodo = :idPeriodo")})
@Table(name = "REV_VERSION_EEFF")
public class VersionEeff implements Serializable {
    
    public static final String FIND_ALL = "VersionEeff.findAll";
    public static final String FIND_BY_PERIOD = "VersionEeff.findByPeriod";
    public static final String FIND_VIGENTE_BY_PERIOD = "VersionEeff.findVigenteByPeriod";
    public static final String FIN_MAX_VERSION_BY_PERIODO = "VersionEeff.findByMaxVersionByPeriodo";
    public static final String UPDATE_VIGENCIA_BY_PERIODO = "VersionEeff.updateVigenciaByPeriodo";
    
    @Id    
    @GeneratedValue(generator="ID_GEN_VERSION_EEFF")
    @SequenceGenerator(name="ID_GEN_VERSION_EEFF", sequenceName = "SEQ_VERSION_EEFF" ,allocationSize = 1)
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
    
    @OneToMany(mappedBy = "versionEeff", cascade = CascadeType.PERSIST)
    private List<EstadoFinanciero> estadoFinancieroList;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PERIODO")
    private Periodo periodo;

    public VersionEeff() {
    }

    public VersionEeff(TipoEstadoEeff estadoEeff, Periodo periodo, Long idVersionEeff, String usuario,
                       Long version, Long vigencia) {
        this.tipoEstadoEeff = estadoEeff;
        this.periodo = periodo;
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

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public void setEstadoFinancieroList(List<EstadoFinanciero> estadoFinancieroList) {
        this.estadoFinancieroList = estadoFinancieroList;
    }

    public List<EstadoFinanciero> getEstadoFinancieroList() {
        return estadoFinancieroList;
    }
}
