package cl.mdr.ifrs.ejb.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import cl.mdr.ifrs.ejb.common.Constantes;

import com.google.gson.annotations.Expose;


@Entity
@NamedQueries( { @NamedQuery(name = Version.VERSION_FIND_ALL, query = "select o from Version o"),
                 @NamedQuery(name = Version.VERSION_FIND_ALL_VIGENTE, query = "select o from Version o, Catalogo c where c.idCatalogo = o.catalogo.idCatalogo and c.vigencia = 1 and o.vigencia = 1 and c.empresa.idRut = :idRut order by o.fechaCreacion, o.version"),
                 @NamedQuery(name = Version.VERSION_FIND_ALL_NO_VIGENTE, query = "select o from Version o where o.vigencia = 0"),
                 @NamedQuery(name = Version.VERSION_FIND_NO_VIGENTE, query = "select o from Version o where o.vigencia = 0 and o.catalogo = :catalogo"),
                 @NamedQuery(name = Version.VERSION_FIND_VIGENTE, query = "select o from Version o where o.vigencia = 1 and o.catalogo = :catalogo "),
                 @NamedQuery(name = Version.FIND_VIGENTE_SIN_CERRAR, query = "select o from Version o where o.vigencia = 1 and o.estado.idEstado <> 4 and o.periodoEmpresa.idPeriodo = :idPeriodo and o.periodoEmpresa.idRut = :idRut order by o.catalogo.empresa.idRut, o.catalogo.orden"),
                 @NamedQuery(name = Version.VERSION_FIND_ALL_BY_CATALOGO, query = "select o from Version o where o.catalogo = :catalogo order by o.vigencia, o.version, o.fechaCreacion"),
                 @NamedQuery(name = Version.VERSION_FIND_ALL_BY_ID_CATALOGO, query = "select o from Version o where o.catalogo.idCatalogo = :idCatalogo order by o.vigencia, o.version, o.fechaCreacion"),
                 @NamedQuery(name = Version.VERSION_FIND_BY_VERSION, query = "select o from Version o where o = :version"),
                 
                 @NamedQuery(name = Version.VERSION_FIND_ULTIMO_VERSION_BY_PERIODO, 
                 			 query = " select o from Version o " +
                 			 		 " where o.idVersion in ( select max(v.idVersion) " +
                 			 		 " from Version v, PeriodoEmpresa p, CatalogoGrupo cg, UsuarioGrupo ug, GrupoEmpresa ge " +
                 			 		 " where ug.grupo = cg.grupo " +
                 			 		 " and ug.grupo = ge.grupo " +
                                     " and v.catalogo.empresa.idRut = :rutEmpresa "+
                 			 		 " and p.idPeriodo = v.periodoEmpresa.idPeriodo " +
                 			 		 " and p.idPeriodo = :periodo " +
                 			 		 " and (:usuario is null or ug.nombreUsuario = :usuario) " +
                 			 		 " and (:tipoCuadro is null or v.catalogo.tipoCuadro.idTipoCuadro = :tipoCuadro) " +
                 			 		 " and (:vigente is null or v.vigencia = :vigente) " +
                 			 		 " and v.catalogo.vigencia = 1 group by v.catalogo.idCatalogo) " +
                 			 		 " order by o.catalogo.orden"),
                 
                  /*RDV@NamedQuery(name = Version.FIND_ULTIMA_VERSION_VIGENTE, 
                 			 query = " select ve " +
                 			 		 " from Version ve, UsuarioGrupo ug, CatalogoGrupo cg join fetch ve.estructuraList" +
                 			 		 " where ve.catalogo.idCatalogo = :idCatalogo " +
                 			 		 " and ve.vigencia = 1 " +
                 			 		 " and ve.catalogo.idCatalogo = cg.catalogo.idCatalogo " +
                 			 		 " and ug.nombreUsuario = :usuario " +
                 			 		 " and cg.idGrupoAcceso = ug.idGrupo " +
                 			 		 " and ve.periodoEmpresa.idPeriodo = :idPeriodo " +
                 			 		 " and ve.periodoEmpresa.idRut = :idRut "),*/              
		 		@NamedQuery(name = Version.FIND_ULTIMA_VERSION_VIGENTE, 
	            			 query = " select ve " +
	            			 		 " from Version ve " +
	            			 		 " where " +
	            			 		 " ve.catalogo.idCatalogo = :idCatalogo " +
	            			 		 " and ve.vigencia = 1 " +
	            			 		 " and ve.periodoEmpresa.idPeriodo = :idPeriodo " +
	            			 		 " and ve.periodoEmpresa.idRut = :idRut "),
                        			 		 
                 @NamedQuery(name = Version.VERSION_FIND_BY_FILTRO,
                            query = " select distinct v " +
                            		" from Version v, CatalogoGrupo cg, UsuarioGrupo ug, GrupoEmpresa ge join fetch v.estructuraList where " +
                                    " v.catalogo.idCatalogo = cg.idCatalogo " + 
                                    " and ug.grupo = cg.grupo " +
                                    " and ug.grupo = ge.grupo " +
                                    " and v.periodoEmpresa.idRut = :idRut" +
                                    " and (ug.nombreUsuario = :usuario or :usuario is null) " +
                                    " and (v.catalogo.tipoCuadro.idTipoCuadro = :tipoCuadro or :tipoCuadro is null) " +
                                    " and (v.catalogo.idCatalogo = :catalogo or :catalogo is null) " +
                                    " and (v.periodoEmpresa.idPeriodo = :periodo or :periodo is null) " +
                                    " and (v.estado.idEstado = :estado or :estado is null) " +
                                    " and (v.vigencia = :vigente or :vigente is null) "+
                                    " and v.catalogo.vigencia = 1"),
                                    //" order by v.catalogo.tipoCuadro.nombre , " +
                                    //" v.catalogo.orden asc")
                @NamedQuery(name = Version.VERSION_DETALLE_FIND_BY_FILTRO,
	                query = " select distinct v " +
	                		" from Version v left join fetch v.historialVersionList, CatalogoGrupo cg, UsuarioGrupo ug, GrupoEmpresa ge where " +
	                        " v.catalogo.idCatalogo = cg.idCatalogo " + 
	                        " and ug.grupo = cg.grupo " +
	                        " and ug.grupo = ge.grupo " +
	                        " and v.periodoEmpresa.idRut = :idRut" +
	                        " and (ug.nombreUsuario = :usuario or :usuario is null) " +
	                        " and (v.catalogo.tipoCuadro.idTipoCuadro = :tipoCuadro or :tipoCuadro is null) " +
	                        " and (v.catalogo.idCatalogo = :catalogo or :catalogo is null) " +
	                        " and (v.periodoEmpresa.idPeriodo = :periodo or :periodo is null) " +
	                        " and (v.estado.idEstado = :estado or :estado is null) " +
	                        " and (v.vigencia = :vigente or :vigente is null) "+
	                        " and v.catalogo.vigencia = 1"),
                                    
                 @NamedQuery(name = Version.VERSION_FIND_BY_ID_CATALOGO_PERIODO_EMPRESA, 
                 			 query = " select o from Version o " +
                 			 		 " where o.catalogo.idCatalogo = :idCatalogo " +
                 			 		 " and o.periodoEmpresa.idPeriodo = :idPeriodo " +
                 			 		 " and o.periodoEmpresa.idRut = :idRut " +
                 			 		 " order by o.version desc"),
                 @NamedQuery(name = Version.VERSION_FIND_BY_ID_ESTRUCTURA, 
                 query = "select o.version from Estructura o where o.idEstructura = :idEstructura")
                 })
                 
@Table(name = Constantes.VERSION)
public class Version implements Serializable {
	
	private static final long serialVersionUID = -8305833693336452475L;
    
    public static final String VERSION_FIND_ALL = "Version.findAll";
    public static final String VERSION_FIND_ALL_VIGENTE = "Version.findAllVigente";
    public static final String VERSION_FIND_ALL_NO_VIGENTE = "Version.findAllNoVigente";
    public static final String VERSION_FIND_NO_VIGENTE = "Version.findNoVigente";
    public static final String VERSION_FIND_VIGENTE = "Version.findVigente";
    public static final String VERSION_FIND_ALL_BY_CATALOGO = "Version.findAllByCatalogo";
    public static final String VERSION_FIND_ALL_BY_ID_CATALOGO = "Version.findAllByIdCatalogo";
    public static final String VERSION_FIND_BY_VERSION = "Version.findByVersion";
    public static final String VERSION_FIND_ULTIMO_VERSION_BY_PERIODO = "Version.findUltimoVersionByPeriodo";
    public static final String FIND_ULTIMA_VERSION_VIGENTE = "Version.findUltimaVersionVigente";
    public static final String VERSION_FIND_BY_FILTRO = "Version.findByFiltro";
    public static final String VERSION_DETALLE_FIND_BY_FILTRO = "Version.findDetalleByFiltro";
    public static final String VERSION_FIND_BY_ID_CATALOGO_PERIODO_EMPRESA = "Version.findByIdCatalogoIdPeriodoEmpresa";
    public static final String FIND_VIGENTE_SIN_CERRAR = "Version.findVigenteSinCerrar";
    public static final String VERSION_FIND_BY_ID_ESTRUCTURA = "Version.findByIdEstructura";
    
    @Id
    //@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID_VERSION", nullable = false)
    @Expose
    private Long idVersion;
    
    @Column(nullable = false)
    @Expose
    private Long version;
    
    @Column(nullable = false)
    @Expose
    private Long vigencia;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_CREACION", nullable = false)
    @Expose
    private Date fechaCreacion;
    
    @Expose
    private String comentario;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns( { @JoinColumn(name = "ID_PERIODO", referencedColumnName = "ID_PERIODO"),
			        @JoinColumn(name = "ID_RUT", referencedColumnName = "ID_RUT")})
    @Expose
    private PeriodoEmpresa periodoEmpresa;

	@Fetch(FetchMode.JOIN)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_CATALOGO")
	@Expose
    private Catalogo catalogo;
    
    @Fetch(FetchMode.JOIN)
    @ManyToOne
    @JoinColumn(name = "ID_ESTADO_CUADRO")
    @Expose
    private EstadoCuadro estado;
    
    //@Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "version", targetEntity = Estructura.class, fetch = FetchType.LAZY, orphanRemoval=true)
    private List<Estructura> estructuraList;
        
    //@Fetch(FetchMode.SUBSELECT)
  	@OneToMany(mappedBy="version", fetch=FetchType.LAZY,orphanRemoval=true)
  	private List<HistorialVersion> historialVersionList;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_ULTIMO_PROCESO", nullable = false)
    @Expose
    private Date fechaUltimoProceso;
    
    @Expose
    private String usuario;
    
    @Column(nullable = false, name = "DATOS_MODIFICADOS")
    private Long datosModificados;
    
    @Transient
    @Expose
    private boolean editable;
    
    @Transient
    @Expose
    private boolean estadoCambiado;
    
    @Column(name = "VALIDADO_EEFF")
    private Long validadoEeff;
    
    

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
    
    public PeriodoEmpresa getPeriodoEmpresa() {
		return periodoEmpresa;
	}

	public void setPeriodoEmpresa(PeriodoEmpresa periodoEmpresa) {
		this.periodoEmpresa = periodoEmpresa;
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

	public List<HistorialVersion> getHistorialVersionList() {
		return historialVersionList;
	}

	public void setHistorialVersioList(List<HistorialVersion> historialVersionList) {
		this.historialVersionList = historialVersionList;
	}

	public boolean isEstadoCambiado() {
		return estadoCambiado;
	}

	public void setEstadoCambiado(boolean estadoCambiado) {
		this.estadoCambiado = estadoCambiado;
	}

	public Long getDatosModificados() {
		return datosModificados;
	}

	public void setDatosModificados(Long datosModificados) {
		this.datosModificados = datosModificados;
	}
	
	public Long getValidadoEeff() {
		return validadoEeff;
	}

	public void setValidadoEeff(Long validadoEeff) {
		this.validadoEeff = validadoEeff;
	}
}