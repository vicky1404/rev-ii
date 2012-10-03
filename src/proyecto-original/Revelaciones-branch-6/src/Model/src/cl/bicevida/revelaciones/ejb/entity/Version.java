package cl.bicevida.revelaciones.ejb.entity;


import cl.bicevida.revelaciones.ejb.common.Constantes;
import cl.bicevida.revelaciones.ejb.common.TipoEstructuraEnum;

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


@Entity
@NamedQueries( { @NamedQuery(name = Version.VERSION_FIND_ALL, query = "select o from Version o"),
                 @NamedQuery(name = Version.VERSION_FIND_ALL_VIGENTE, query = "select o from Version o, Catalogo c where c.idCatalogo = o.catalogo.idCatalogo and c.vigencia = 1 and o.vigencia = 1 order by o.fechaCreacion, o.version"),
                 @NamedQuery(name = Version.VERSION_FIND_ALL_NO_VIGENTE, query = "select o from Version o where o.vigencia = 0"),
                 @NamedQuery(name = Version.VERSION_FIND_NO_VIGENTE, query = "select o from Version o where o.vigencia = 0 and o.catalogo = :catalogo"),
                 @NamedQuery(name = Version.VERSION_FIND_VIGENTE, query = "select o from Version o join fetch o.versionPeriodoList where o.vigencia = 1 and o.catalogo = :catalogo "),
                 @NamedQuery(name = Version.VERSION_FIND_ALL_BY_CATALOGO, query = "select o from Version o where o.catalogo = :catalogo order by o.vigencia, o.version, o.fechaCreacion"),
                 @NamedQuery(name = Version.VERSION_FIND_BY_VERSION, query = "select o from Version o where o = :version"),
                 @NamedQuery(name = Version.VERSION_FIND_ULTIMO_VERSION_BY_PERIODO, query = "select o from Version o where o.idVersion in ( select max(v.idVersion) from Version v, VersionPeriodo vp, Periodo p, CatalogoGrupo cg, UsuarioGrupo ug where ug.grupo = cg.grupo and vp.version.idVersion = v.idVersion and p.idPeriodo = vp.periodo.idPeriodo and p.periodo = :periodo and (:usuario is null or ug.usuarioOid = :usuario) and (:tipoCuadro is null or vp.version.catalogo.tipoCuadro.idTipoCuadro = :tipoCuadro) and (:vigente is null or vp.version.vigencia = :vigente) and v.catalogo.vigencia = 1 group by v.catalogo.idCatalogo) order by o.catalogo.orden"),
                 @NamedQuery(name = Version.FIND_ULTIMA_VERSION_VIGENTE, query = "select v from Version v where v.idVersion = (select max(ve.idVersion) from Version ve, VersionPeriodo vp, UsuarioGrupo ug, CatalogoGrupo cg where ve.catalogo.idCatalogo = :idCatalogo and ve.vigencia = 1 and ve.catalogo.idCatalogo = cg.catalogo.idCatalogo and ug.usuarioOid = :usuario and cg.idGrupoAcceso = ug.idGrupo and vp.idVersion = ve.idVersion and vp.idPeriodo = :idPeriodo)")
                 
                 })
@Table(name = Constantes.REV_VERSION)
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
    private static final Integer CANT_GRUPOS_DEFAULT = 2;
        
    @SuppressWarnings("compatibility")
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
    
    @OneToMany(mappedBy = "version")
    private List<VersionPeriodo> versionPeriodoList;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_CATALOGO")
    private Catalogo catalogo;
    
    @OneToMany(mappedBy = "version", targetEntity = Estructura.class)
    private List<Estructura> estructuraList;
    
    @Column(name = "VALIDADO_EEFF")
    private Long validadoEeff;
    
    @Transient
    private boolean editable;
    @Column(name = "CANT_GRUPOS")
    private Integer cantidadGrupos;
    @Transient
    private boolean desagregado;
    @Transient
    private boolean contieneGrillas;

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

    public List<VersionPeriodo> getVersionPeriodoList() {
        return versionPeriodoList;
    }

    public void setVersionPeriodoList(List<VersionPeriodo> versionPeriodoList) {
        this.versionPeriodoList = versionPeriodoList;
    }

    public VersionPeriodo addPeriodo(VersionPeriodo versionPeriodo) {
        getVersionPeriodoList().add(versionPeriodo);
        versionPeriodo.setVersion(this);
        return versionPeriodo;
    }

    public VersionPeriodo removePeriodo(VersionPeriodo versionPeriodo) {
        getVersionPeriodoList().remove(versionPeriodo);
        versionPeriodo.setVersion(null);
        return versionPeriodo;
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

    public void setCantidadGrupos(Integer cantidadGrupos) {
        this.cantidadGrupos = cantidadGrupos;
    }
    public Integer getCantidadGrupos() {
        
        if (cantidadGrupos == null){
            
                cantidadGrupos = CANT_GRUPOS_DEFAULT;
            }
        
        return cantidadGrupos;
    }
    public void setValidadoEeff(Long validadoEeff) {
        this.validadoEeff = validadoEeff;
    }

    public Long getValidadoEeff() {
        return validadoEeff;
    }
	
    public void setDesagregado(boolean desagregado) {
        this.desagregado = desagregado;
    }
    
    public boolean isDesagregado() {
        boolean respuesta = Boolean.FALSE;
        if (this.getEstructuraList() != null){
        for (Estructura estructura : this.getEstructuraList()){
            if (estructura.getTipoEstructura().getIdTipoEstructura().equals(TipoEstructuraEnum.GRILLA.getKey())){
                for (Grilla grilla : estructura.getGrillaList()){
                    if (grilla != null && grilla.getSubGrillaList() != null && grilla.getSubGrillaList().size() > 0){
                            respuesta = Boolean.TRUE;
                            break;
                        }
                    }
                }
            }
        }
        return respuesta;
    }
    
    public void setContieneGrillas(boolean contieneGrillas) {
        this.contieneGrillas = contieneGrillas;
    }
    
    public boolean isContieneGrillas() {
        boolean respuesta = Boolean.FALSE;
        for (Estructura estructura : this.estructuraList){
            if (estructura.getGrillaList() != null && estructura.getGrillaList().size() > 0){
                    respuesta = Boolean.TRUE;
                    break;
                }
        }
        return respuesta;
    }
}
