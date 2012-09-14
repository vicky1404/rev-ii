package cl.bicevida.revelaciones.ejb.entity;


import cl.bicevida.revelaciones.ejb.common.Constantes;
import cl.bicevida.revelaciones.ejb.entity.pk.PeriodoPK;

import java.io.Serializable;

import java.util.Date;
import java.util.List;

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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


@Entity
@NamedQueries({  @NamedQuery(name = VersionPeriodo.VERSION_PERIODO_FIND_ALL, query = "select o from VersionPeriodo o order by o.fechaCreacion, o.version.version"),
                 @NamedQuery(name = VersionPeriodo.VERSION_PERIODO_FIND_ALL_BY_PERIODO, query = "select o from VersionPeriodo o where o.periodo = :periodo order by o.fechaCreacion, o.version.version"),
                 @NamedQuery(name = VersionPeriodo.VERSION_PERIODO_FIND_ALL_BY_PERIODO_NOTA , query = "select o from VersionPeriodo o where o.periodo = :periodo and o.version.catalogo = :catalogo order by o.version.vigencia desc, o.version.version desc"),
                 @NamedQuery(name = VersionPeriodo.VERSION_PERIODO_FIND_ALL_BY_PERIODO_NOTA_VIGENTE , query = "select o from VersionPeriodo o where o.periodo = :periodo and o.version.catalogo = :catalogo and o.version.vigencia = 1 order by o.version.version desc"),
                 @NamedQuery(name = VersionPeriodo.VERSION_PERIODO_FIND_BY_ID, query = "select o from VersionPeriodo o where o.idPeriodo = :idPeriodo and o.idVersion = :idVersion"),
                 @NamedQuery(name = VersionPeriodo.VERSION_PERIODO_FIND_BY_FILTRO,
                            query = " select distinct vp from VersionPeriodo vp , CatalogoGrupo cg, UsuarioGrupo ug where " +
                                    " vp.version.catalogo.idCatalogo = cg.idCatalogo " + 
                                    " and ug.grupo = cg.grupo " +
                                    " and (:usuario is null or ug.usuarioOid = :usuario) " +
                                    " and (:tipoCuadro is null or vp.version.catalogo.tipoCuadro.idTipoCuadro = :tipoCuadro) " +
                                    " and (:periodo is null or vp.periodo.idPeriodo = :periodo) " +
                                    " and (:estado is null or vp.estado.idEstado = :estado) " +
                                    " and (:vigente is null or vp.version.vigencia = :vigente) "+
                                    " and vp.version.catalogo.vigencia = 1 "+
                                    " order by vp.version.catalogo.tipoCuadro.nombre , " +
                                    " vp.version.catalogo.orden asc ") 
})
@Table(name = Constantes.REV_VERSION_PERIODO)
@IdClass(PeriodoPK.class)
public class VersionPeriodo implements Serializable {
    @SuppressWarnings("compatibility:1614747039701603103")
    private static final long serialVersionUID = 9089883612703095353L;
    
    public static final String VERSION_PERIODO_FIND_ALL = "VersionPeriodo.findAll";
    public static final String VERSION_PERIODO_FIND_ALL_BY_PERIODO = "VersionPeriodo.findAllByPeriodo";
    public static final String VERSION_PERIODO_FIND_ALL_BY_PERIODO_NOTA = "VersionPeriodo.findAllByPeriodoNota";
    public static final String VERSION_PERIODO_FIND_ALL_BY_PERIODO_NOTA_VIGENTE = "VersionPeriodo.findAllByPeriodoNotaVigente";
    public static final String VERSION_PERIODO_FIND_BY_FILTRO = "VersionPeriodo.findByFiltro";
    public static final String VERSION_PERIODO_FIND_BY_ID = "VersionPeriodo.findById";
    
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_CREACION", nullable = false)
    private Date fechaCreacion;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_ULTIMO_PROCESO", nullable = false)
    private Date fechaUltimoProceso;
    
    @Id
    @Column(name = "ID_PERIODO", nullable = false, insertable = false, updatable = false)
    private Long idPeriodo;
    
    @Id
    @Column(name = "ID_VERSION", nullable = false, insertable = false, updatable = false)
    private Long idVersion;
    
    @Column(nullable = false, length = 256)
    private String usuario;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_VERSION")
    private Version version;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PERIODO")
    private Periodo periodo;
    
    @ManyToOne
    @JoinColumn(name = "ID_ESTADO_CUADRO")
    private EstadoCuadro estado;
    
    @OneToMany(mappedBy = "versionPeriodo" , targetEntity = HistorialVersionPeriodo.class, fetch = FetchType.EAGER)
    @OrderBy("fechaProceso DESC")
    private List<HistorialVersionPeriodo> historialVersionPeriodoList;
    
    @Transient
    private String comentario;

    public VersionPeriodo() {
    }

    public VersionPeriodo(Date fechaCreacion, EstadoCuadro estado, Periodo periodo, Version version, String usuario) {
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.periodo = periodo;
        this.version = version;
        this.usuario = usuario;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }


    public Long getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(Long idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public Long getIdVersion() {
        return idVersion;
    }

    public void setIdVersion(Long idVersion) {
        this.idVersion = idVersion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
        if (version != null) {
            this.idVersion = version.getIdVersion();
        }
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
        if (periodo != null) {
            this.idPeriodo = periodo.getIdPeriodo();
        }
    }

    public EstadoCuadro getEstado() {
        return estado;
    }

    public void setEstado(EstadoCuadro estado) {
        this.estado = estado;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setHistorialVersionPeriodoList(List<HistorialVersionPeriodo> historialVersionPeriodoList) {
        this.historialVersionPeriodoList = historialVersionPeriodoList;
    }

    public List<HistorialVersionPeriodo> getHistorialVersionPeriodoList() {
        return historialVersionPeriodoList;
    }
    
    public void setFechaUltimoProceso(Date fechaUltimoProceso) {
        this.fechaUltimoProceso = fechaUltimoProceso;
    }

    public Date getFechaUltimoProceso() {
        return fechaUltimoProceso;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("fechaCreacion=");
        buffer.append(getFechaCreacion());
        buffer.append(',');
        buffer.append("usuario=");
        buffer.append(getUsuario());
        buffer.append(',');
        buffer.append(']');
        return buffer.toString();
    }

    
}
