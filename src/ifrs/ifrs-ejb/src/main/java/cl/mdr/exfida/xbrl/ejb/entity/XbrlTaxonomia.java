package cl.mdr.exfida.xbrl.ejb.entity;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity 
@NamedQueries( { @NamedQuery(name = XbrlTaxonomia.FIND_TAXONOMIAS_BY_FILTRO , query = "select o from XbrlTaxonomia o where 1 = 1 and (:vigente is null or o.vigente = :vigente) and (:fechaDesde is null or o.fechaCreacion between :fechaDesde and :fechaHasta)") })
@Table(name = "IFRS_XBRL_TAXONOMIA")
public class XbrlTaxonomia implements Serializable {
    
    public static final String FIND_TAXONOMIAS_BY_FILTRO = "XbrlTaxonomia.findAllByFiltro";
    
    private static final long serialVersionUID = -6395869128445641501L;
    
    @Id
    @Column(name = "ID_TAXONOMIA", nullable = false)
    @GeneratedValue(generator="ID_GEN_XBRL_TAXONOMIA")
    @SequenceGenerator(name="ID_GEN_XBRL_TAXONOMIA", sequenceName = "SEQ_XBRL_TAXONOMIA" ,allocationSize = 1)
    private Long idTaxonomia;
    
    @Column(length = 1024)
    private String nombre;
    
    @Column(name = "NOMBRE_ARCHIVO", length = 1024)
    private String nombreArchivo;
    
    @Column(name = "TOP_TAXONOMY", length = 1024)
    private String topTaxonomy;
    
    @Column(length = 1024)
    private String uri;
    
    private Long vigente;
    
    @Column(name = "USUARIO_CREACION", length = 256, updatable = false)
    private String usuarioCreacion;
    
    @Column(name = "USUARIO_EDICION", length = 256)
    private String usuarioEdicion;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_CREACION", updatable = false)
    private Date fechaCreacion;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_EDICION")
    private Date fechaEdicion;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_TAXONOMIA")
    private Date fechaTaxonomia;

    public XbrlTaxonomia() {
        super();
    }

    public XbrlTaxonomia(Long vigente, Date fechaCreacion, Date fechaTaxonomia, String usuarioCreacion) {
        super();
        this.vigente = vigente;
        this.fechaCreacion = fechaCreacion;
        this.fechaTaxonomia = fechaTaxonomia;
        this.usuarioCreacion = usuarioCreacion;
    }

    public Long getIdTaxonomia() {
        return idTaxonomia;
    }

    public void setIdTaxonomia(Long idTaxonomia) {
        this.idTaxonomia = idTaxonomia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getTopTaxonomy() {
        return topTaxonomy;
    }

    public void setTopTaxonomy(String topTaxonomy) {
        this.topTaxonomy = topTaxonomy;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Long getVigente() {
        return vigente;
    }

    public void setVigente(Long vigente) {
        this.vigente = vigente;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioEdicion(String usuarioEdicion) {
        this.usuarioEdicion = usuarioEdicion;
    }

    public String getUsuarioEdicion() {
        return usuarioEdicion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaEdicion(Date fechaEdicion) {
        this.fechaEdicion = fechaEdicion;
    }

    public Date getFechaEdicion() {
        return fechaEdicion;
    }

    public void setFechaTaxonomia(Date fechaTaxonomia) {
        this.fechaTaxonomia = fechaTaxonomia;
    }

    public Date getFechaTaxonomia() {
        return fechaTaxonomia;
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof XbrlTaxonomia)) {
            return false;
        }
        final XbrlTaxonomia other = (XbrlTaxonomia)object;
        if (!(idTaxonomia == null ? other.idTaxonomia == null : idTaxonomia.equals(other.idTaxonomia))) {
            return false;
        }
        if (!(fechaCreacion == null ? other.fechaCreacion == null : fechaCreacion.equals(other.fechaCreacion))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = 1;
        result = PRIME * result + ((idTaxonomia == null) ? 0 : idTaxonomia.hashCode());
        result = PRIME * result + ((fechaCreacion == null) ? 0 : fechaCreacion.hashCode());
        return result;
    }
}
