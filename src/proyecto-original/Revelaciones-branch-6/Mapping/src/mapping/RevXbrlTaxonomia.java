package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Date;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({
  @NamedQuery(name = "RevXbrlTaxonomia.findAll", query = "select o from RevXbrlTaxonomia o")
})
@Table(name = "REV_XBRL_TAXONOMIA")
public class RevXbrlTaxonomia implements Serializable {
    @Temporal(TemporalType.DATE)
    @Column(name="FECHA_CREACION")
    private Date fechaCreacion;
    @Temporal(TemporalType.DATE)
    @Column(name="FECHA_EDICION")
    private Date fechaEdicion;
    @Temporal(TemporalType.DATE)
    @Column(name="FECHA_TAXONOMIA")
    private Date fechaTaxonomia;
    @Id
    @Column(name="ID_TAXONOMIA", nullable = false)
    private BigDecimal idTaxonomia;
    @Column(length = 1024)
    private String nombre;
    @Column(name="NOMBRE_ARCHIVO", length = 1024)
    private String nombreArchivo;
    @Column(name="TOP_TAXONOMY", length = 1024)
    private String topTaxonomy;
    @Column(length = 1024)
    private String uri;
    @Column(name="USUARIO_CREACION", length = 256)
    private String usuarioCreacion;
    @Column(name="USUARIO_EDICION", length = 256)
    private String usuarioEdicion;
    private BigDecimal vigente;
    @OneToMany(mappedBy = "revXbrlTaxonomia")
    private List<RevXbrlConceptoCodigoFecu> revXbrlConceptoCodigoFecuList2;
    @OneToMany(mappedBy = "revXbrlTaxonomia1")
    private List<RevXbrlConceptoCelda> revXbrlConceptoCeldaList3;

    public RevXbrlTaxonomia() {
    }

    public RevXbrlTaxonomia(Date fechaCreacion, Date fechaEdicion, Date fechaTaxonomia, BigDecimal idTaxonomia,
                            String nombre, String nombreArchivo, String topTaxonomy, String uri,
                            String usuarioCreacion, String usuarioEdicion, BigDecimal vigente) {
        this.fechaCreacion = fechaCreacion;
        this.fechaEdicion = fechaEdicion;
        this.fechaTaxonomia = fechaTaxonomia;
        this.idTaxonomia = idTaxonomia;
        this.nombre = nombre;
        this.nombreArchivo = nombreArchivo;
        this.topTaxonomy = topTaxonomy;
        this.uri = uri;
        this.usuarioCreacion = usuarioCreacion;
        this.usuarioEdicion = usuarioEdicion;
        this.vigente = vigente;
    }


    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaEdicion() {
        return fechaEdicion;
    }

    public void setFechaEdicion(Date fechaEdicion) {
        this.fechaEdicion = fechaEdicion;
    }

    public Date getFechaTaxonomia() {
        return fechaTaxonomia;
    }

    public void setFechaTaxonomia(Date fechaTaxonomia) {
        this.fechaTaxonomia = fechaTaxonomia;
    }

    public BigDecimal getIdTaxonomia() {
        return idTaxonomia;
    }

    public void setIdTaxonomia(BigDecimal idTaxonomia) {
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

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public String getUsuarioEdicion() {
        return usuarioEdicion;
    }

    public void setUsuarioEdicion(String usuarioEdicion) {
        this.usuarioEdicion = usuarioEdicion;
    }

    public BigDecimal getVigente() {
        return vigente;
    }

    public void setVigente(BigDecimal vigente) {
        this.vigente = vigente;
    }

    public List<RevXbrlConceptoCodigoFecu> getRevXbrlConceptoCodigoFecuList2() {
        return revXbrlConceptoCodigoFecuList2;
    }

    public void setRevXbrlConceptoCodigoFecuList2(List<RevXbrlConceptoCodigoFecu> revXbrlConceptoCodigoFecuList2) {
        this.revXbrlConceptoCodigoFecuList2 = revXbrlConceptoCodigoFecuList2;
    }

    public RevXbrlConceptoCodigoFecu addRevXbrlConceptoCodigoFecu(RevXbrlConceptoCodigoFecu revXbrlConceptoCodigoFecu) {
        getRevXbrlConceptoCodigoFecuList2().add(revXbrlConceptoCodigoFecu);
        revXbrlConceptoCodigoFecu.setRevXbrlTaxonomia(this);
        return revXbrlConceptoCodigoFecu;
    }

    public RevXbrlConceptoCodigoFecu removeRevXbrlConceptoCodigoFecu(RevXbrlConceptoCodigoFecu revXbrlConceptoCodigoFecu) {
        getRevXbrlConceptoCodigoFecuList2().remove(revXbrlConceptoCodigoFecu);
        revXbrlConceptoCodigoFecu.setRevXbrlTaxonomia(null);
        return revXbrlConceptoCodigoFecu;
    }

    public List<RevXbrlConceptoCelda> getRevXbrlConceptoCeldaList3() {
        return revXbrlConceptoCeldaList3;
    }

    public void setRevXbrlConceptoCeldaList3(List<RevXbrlConceptoCelda> revXbrlConceptoCeldaList3) {
        this.revXbrlConceptoCeldaList3 = revXbrlConceptoCeldaList3;
    }

    public RevXbrlConceptoCelda addRevXbrlConceptoCelda(RevXbrlConceptoCelda revXbrlConceptoCelda) {
        getRevXbrlConceptoCeldaList3().add(revXbrlConceptoCelda);
        revXbrlConceptoCelda.setRevXbrlTaxonomia1(this);
        return revXbrlConceptoCelda;
    }

    public RevXbrlConceptoCelda removeRevXbrlConceptoCelda(RevXbrlConceptoCelda revXbrlConceptoCelda) {
        getRevXbrlConceptoCeldaList3().remove(revXbrlConceptoCelda);
        revXbrlConceptoCelda.setRevXbrlTaxonomia1(null);
        return revXbrlConceptoCelda;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("fechaCreacion=");
        buffer.append(getFechaCreacion());
        buffer.append(',');
        buffer.append("fechaEdicion=");
        buffer.append(getFechaEdicion());
        buffer.append(',');
        buffer.append("fechaTaxonomia=");
        buffer.append(getFechaTaxonomia());
        buffer.append(',');
        buffer.append("idTaxonomia=");
        buffer.append(getIdTaxonomia());
        buffer.append(',');
        buffer.append("nombre=");
        buffer.append(getNombre());
        buffer.append(',');
        buffer.append("nombreArchivo=");
        buffer.append(getNombreArchivo());
        buffer.append(',');
        buffer.append("topTaxonomy=");
        buffer.append(getTopTaxonomy());
        buffer.append(',');
        buffer.append("uri=");
        buffer.append(getUri());
        buffer.append(',');
        buffer.append("usuarioCreacion=");
        buffer.append(getUsuarioCreacion());
        buffer.append(',');
        buffer.append("usuarioEdicion=");
        buffer.append(getUsuarioEdicion());
        buffer.append(',');
        buffer.append("vigente=");
        buffer.append(getVigente());
        buffer.append(']');
        return buffer.toString();
    }
}
