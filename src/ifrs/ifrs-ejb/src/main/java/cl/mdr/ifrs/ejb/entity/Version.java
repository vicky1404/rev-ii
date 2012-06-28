package cl.mdr.ifrs.ejb.entity;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import cl.mdr.ifrs.ejb.common.Constantes;


@Entity
@NamedQueries( { @NamedQuery(name = Version.VERSION_FIND_ALL, query = "select o from Version o"),
                 @NamedQuery(name = Version.VERSION_FIND_ALL_VIGENTE, query = "select o from Version o, Catalogo c where c.idCatalogo = o.catalogo.idCatalogo and c.vigencia = 1 and o.vigencia = 1 order by o.fechaCreacion, o.version"),
                 @NamedQuery(name = Version.VERSION_FIND_ALL_NO_VIGENTE, query = "select o from Version o where o.vigencia = 0"),
                 @NamedQuery(name = Version.VERSION_FIND_NO_VIGENTE, query = "select o from Version o where o.vigencia = 0 and o.catalogo = :catalogo"),
                 @NamedQuery(name = Version.VERSION_FIND_VIGENTE, query = "select o from Version o where o.vigencia = 1 and o.catalogo = :catalogo "),
                 @NamedQuery(name = Version.VERSION_FIND_ALL_BY_CATALOGO, query = "select o from Version o where o.catalogo = :catalogo order by o.vigencia, o.version, o.fechaCreacion"),
                 @NamedQuery(name = Version.VERSION_FIND_BY_VERSION, query = "select o from Version o where o = :version"),
                 @NamedQuery(name = Version.VERSION_FIND_ULTIMO_VERSION_BY_PERIODO, query = "select o from Version o where o.idVersion in ( select max(v.idVersion) from Version v, Periodo p, CatalogoGrupo cg, UsuarioGrupo ug where ug.grupo = cg.grupo and p.idPeriodo = v.periodo.idPeriodo and p.periodo = :periodo and (:usuario is null or ug.nombreUsuario = :usuario) and (:tipoCuadro is null or v.catalogo.tipoCuadro.idTipoCuadro = :tipoCuadro) and (:vigente is null or v.vigencia = :vigente) and v.catalogo.vigencia = 1 group by v.catalogo.idCatalogo) order by o.catalogo.orden"),
                 @NamedQuery(name = Version.FIND_ULTIMA_VERSION_VIGENTE, query = "select ve from Version ve, UsuarioGrupo ug, CatalogoGrupo cg where ve.catalogo.idCatalogo = :idCatalogo and ve.vigencia = 1 and ve.catalogo.idCatalogo = cg.catalogo.idCatalogo and ug.nombreUsuario = :usuario and cg.idGrupoAcceso = ug.idGrupo and ve.periodo.idPeriodo = :idPeriodo"),
                 @NamedQuery(name = Version.VERSION_FIND_BY_FILTRO,
                            query = " select distinct v from Version v , CatalogoGrupo cg, UsuarioGrupo ug where " +
                                    " v.catalogo.idCatalogo = cg.idCatalogo " + 
                                    " and ug.grupo = cg.grupo " +
                                    " and (:usuario is null or ug.nombreUsuario = :usuario) " +
                                    " and (:tipoCuadro is null or v.catalogo.tipoCuadro.idTipoCuadro = :tipoCuadro) " +
                                    " and (:periodo is null or v.periodo.idPeriodo = :periodo) " +
                                    " and (:estado is null or v.estado.idEstado = :estado) " +
                                    " and (:vigente is null or v.vigencia = :vigente) "+
                                    " and v.catalogo.vigencia = 1 "+
                                    " order by v.catalogo.tipoCuadro.nombre , " +
                                    " v.catalogo.orden asc") 
                 })
@Table(name = Constantes.VERSION)
public class Version implements Serializable {
    
    public static final String VERSION_FIND_ALL = "Version.findAll";
    public static final String VERSION_FIND_ALL_VIGENTE = "Version.findAllVigente";
    public static final String VERSION_FIND_ALL_NO_VIGENTE = "Version.findAllNoVigente";
    public static final String VERSION_FIND_NO_VIGENTE = "Version.findNoVigente";
    public static final String VERSION_FIND_VIGENTE = "Version.findVigente";
    public static final String VERSION_FIND_ALL_BY_CATALOGO = "Version.findAllByCatalogo";
    public static final String VERSION_FIND_BY_VERSION = "Version.findByVersion";
    public static final String VERSION_FIND_ULTIMO_VERSION_BY_PERIODO = "Version.findUltimoVersionByPeriodo";
    public static final String FIND_ULTIMA_VERSION_VIGENTE = "Version.findUltimaVersionVigente";
    public static final String VERSION_FIND_BY_FILTRO = "Version.findByFiltro";
    
    private static final long serialVersionUID = -8305833693336452475L;
    
    @Id
    @GeneratedValue(generator="ID_GEN_VERSION")
    @SequenceGenerator(name="ID_GEN_VERSION", sequenceName = "SEQ_VERSION" ,allocationSize = 1)
    @Column(name = "ID_VERSION", nullable = false)
    private Long idVersion;
    
    @Column(nullable = false)
    private Long version;
    
    @Column(nullable = false)
    private Long vigencia;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_CREACION", nullable = false)
    private Date fechaCreacion;
    
    private String comentario;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PERIODO")
    private Periodo periodo;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_CATALOGO")
    private Catalogo catalogo;
    
    @ManyToOne
    @JoinColumn(name = "ID_ESTADO_CUADRO")
    private EstadoCuadro estado;
    
    @OneToMany(mappedBy = "version", targetEntity = Estructura.class)
    private List<Estructura> estructuraList;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_ULTIMO_PROCESO", nullable = false)
    private Date fechaUltimoProceso;
    
    private String usuario;
    
    @Transient
    private boolean editable;

    public Version() {
    }

    public Version(Long idVersion) {
        this.idVersion = idVersion;
    }
    
    public Version(Long version, boolean editable, Date fechaCreacion) {
        this.version = version;
        this.editable = editable;
        this.fechaCreacion = fechaCreacion;
    }

    public Version(Catalogo catalogo, Long idVersion, Long version, Long vigencia) {
        this.catalogo = catalogo;
        this.idVersion = idVersion;
        this.version = version;
        this.vigencia = vigencia;
    }

    public Version(Long version, boolean editable, Date fechaCreacion, Long vigencia) {
        this.version = version;
        this.editable = editable;
        this.fechaCreacion = fechaCreacion;
        this.vigencia = vigencia;
    }

    public Long getIdVersion() {
        return idVersion;
    }

    public void setIdVersion(Long idVersion) {
        this.idVersion = idVersion;
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

    public Catalogo getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(Catalogo catalogoNota) {
        this.catalogo = catalogoNota;
    }

    public List<Estructura> getEstructuraList() {
        return estructuraList;
    }

    public void setEstructuraList(List<Estructura> estructuraList) {
        this.estructuraList = estructuraList;
    }

    public Estructura addEstructura(Estructura estructura) {
        getEstructuraList().add(estructura);
        estructura.setVersion(this);
        return estructura;
    }

    public Estructura removeEstructura(Estructura estructura) {
        getEstructuraList().remove(estructura);
        estructura.setVersion(null);
        return estructura;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idVersion=");
        buffer.append(getIdVersion());
        buffer.append(',');
        buffer.append("version=");
        buffer.append(getVersion());
        buffer.append(',');
        buffer.append("vigencia=");
        buffer.append(getVigencia());
        buffer.append(']');
        return buffer.toString();
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setEstado(EstadoCuadro estado) {
        this.estado = estado;
    }

    public EstadoCuadro getEstado() {
        return estado;
    }

    public void setFechaUltimoProceso(Date fechaUltimoProceso) {
        this.fechaUltimoProceso = fechaUltimoProceso;
    }

    public Date getFechaUltimoProceso() {
        return fechaUltimoProceso;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUsuario() {
        return usuario;
    }
}
