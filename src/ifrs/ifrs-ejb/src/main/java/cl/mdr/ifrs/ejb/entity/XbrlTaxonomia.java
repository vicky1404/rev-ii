package cl.mdr.ifrs.ejb.entity;

import java.io.Serializable;
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

import com.google.gson.annotations.Expose;

import cl.mdr.ifrs.ejb.common.Constantes;

@Entity
@NamedQueries( { @NamedQuery(name = XbrlTaxonomia.FIND_TAXONOMIAS_BY_FILTRO , query = "select o from XbrlTaxonomia o") })
@Table(name = Constantes.XBRL_TAXONOMIA)
public class XbrlTaxonomia implements Serializable {
    
    public static final String FIND_TAXONOMIAS_BY_FILTRO = "XbrlTaxonomia.findAllByFiltro";
    
    private static final long serialVersionUID = -6395869128445641501L;
    
    @Id
    @Column(name = "ID_TAXONOMIA", nullable = false)
    @GeneratedValue(generator="ID_GEN_XBRL_TAXONOMIA")
    @SequenceGenerator(name="ID_GEN_XBRL_TAXONOMIA", sequenceName = "SEQ_XBRL_TAXONOMIA" ,allocationSize = 1)
    @Expose
    private Long idTaxonomia;
    
    @Column(length = 1024)
    @Expose
    private String nombre;
    
    @Column(name = "NOMBRE_ARCHIVO", length = 1024)
    @Expose
    private String nombreArchivo;
    
    @Column(name = "TOP_TAXONOMY", length = 1024)
    @Expose
    private String topTaxonomy;
    
    @Column(length = 1024)
    @Expose
    private String uri;
    
    @Expose
    private Long vigente;
    
    @Column(name = "USUARIO_CREACION", length = 256, updatable = false)
    @Expose
    private String usuarioCreacion;
    
    @Column(name = "USUARIO_EDICION", length = 256)
    @Expose
    private String usuarioEdicion;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_CREACION", updatable = false)    
    private Date fechaCreacion;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_EDICION")    
    private Date fechaEdicion;
    
    @Temporal(TemporalType.DATE)
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fechaCreacion == null) ? 0 : fechaCreacion.hashCode());
		result = prime * result
				+ ((fechaEdicion == null) ? 0 : fechaEdicion.hashCode());
		result = prime * result
				+ ((fechaTaxonomia == null) ? 0 : fechaTaxonomia.hashCode());
		result = prime * result
				+ ((idTaxonomia == null) ? 0 : idTaxonomia.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result
				+ ((nombreArchivo == null) ? 0 : nombreArchivo.hashCode());
		result = prime * result
				+ ((topTaxonomy == null) ? 0 : topTaxonomy.hashCode());
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
		result = prime * result
				+ ((usuarioCreacion == null) ? 0 : usuarioCreacion.hashCode());
		result = prime * result
				+ ((usuarioEdicion == null) ? 0 : usuarioEdicion.hashCode());
		result = prime * result + ((vigente == null) ? 0 : vigente.hashCode());
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
		XbrlTaxonomia other = (XbrlTaxonomia) obj;
		if (fechaCreacion == null) {
			if (other.fechaCreacion != null)
				return false;
		} else if (!fechaCreacion.equals(other.fechaCreacion))
			return false;
		if (fechaEdicion == null) {
			if (other.fechaEdicion != null)
				return false;
		} else if (!fechaEdicion.equals(other.fechaEdicion))
			return false;
		if (fechaTaxonomia == null) {
			if (other.fechaTaxonomia != null)
				return false;
		} else if (!fechaTaxonomia.equals(other.fechaTaxonomia))
			return false;
		if (idTaxonomia == null) {
			if (other.idTaxonomia != null)
				return false;
		} else if (!idTaxonomia.equals(other.idTaxonomia))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (nombreArchivo == null) {
			if (other.nombreArchivo != null)
				return false;
		} else if (!nombreArchivo.equals(other.nombreArchivo))
			return false;
		if (topTaxonomy == null) {
			if (other.topTaxonomy != null)
				return false;
		} else if (!topTaxonomy.equals(other.topTaxonomy))
			return false;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		if (usuarioCreacion == null) {
			if (other.usuarioCreacion != null)
				return false;
		} else if (!usuarioCreacion.equals(other.usuarioCreacion))
			return false;
		if (usuarioEdicion == null) {
			if (other.usuarioEdicion != null)
				return false;
		} else if (!usuarioEdicion.equals(other.usuarioEdicion))
			return false;
		if (vigente == null) {
			if (other.vigente != null)
				return false;
		} else if (!vigente.equals(other.vigente))
			return false;
		return true;
	}
    
    
}
