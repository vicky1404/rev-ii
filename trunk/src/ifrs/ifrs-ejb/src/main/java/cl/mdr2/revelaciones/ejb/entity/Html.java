package cl.bicevida.revelaciones.ejb.entity;


import cl.bicevida.revelaciones.ejb.common.Constantes;
import cl.bicevida.revelaciones.ejb.cross.Util;

import java.io.Serializable;

import java.lang.Long;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@NamedQueries( { @NamedQuery(name = "Html.findAll", query = "select o from Html o") })
@Table(name = Constantes.HTML)
public class Html implements Serializable {
    private byte[] contenido;
    @Id
    @Column(name = "ID_HTML", nullable = false)
    private Long idHtml;
    @Column(length = 256)
    private String titulo;
    @ManyToOne(targetEntity = Estructura.class)
    @JoinColumn(name = "ID_HTML", insertable = false, updatable = false)
    private Estructura estructura;
    @Transient
    private String contenidoStr;

    public Html() {
    }

    public Html(Estructura estructura, Long idHtml, String titulo) {
        this.estructura = estructura;
        this.idHtml = idHtml;
        this.titulo = titulo;
    }


    public byte[] getContenido() {
        return contenido;
    }
    
    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }
    
    public Long getIdHtml() {
        return idHtml;
    }

    public void setIdHtml(Long idHtml) {
        this.idHtml = idHtml;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Estructura getEstructura() {
        return estructura;
    }

    public void setEstructura(Estructura estructura) {
        this.estructura = estructura;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("contenido=");
        buffer.append(getContenido());
        buffer.append(',');
        buffer.append("idHtml=");
        buffer.append(getIdHtml());
        buffer.append(',');
        buffer.append("titulo=");
        buffer.append(getTitulo());
        buffer.append(']');
        return buffer.toString();
    }

    public void setContenidoStr(String contenidoStr) {
        this.contenidoStr = contenidoStr;
        this.setContenido(Util.getBytes(Util.clearHtml(contenidoStr.replaceAll("<p>&nbsp;</p>\n", "").replaceAll("<p>", "<p style=\"font-family: Arial; font-size: 15px; color: #222;\">\n").replaceAll("border=\"0\"", "border=\"1\""))));
    }

    public String getContenidoStr() {
        return Util.htmlBytesToString(getContenido());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Html)) {
            return false;
        }
        final Html other = (Html)object;
        if (!(idHtml == null ? other.idHtml == null : idHtml.equals(other.idHtml))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = 1;
        result = PRIME * result + ((idHtml == null) ? 0 : idHtml.hashCode());
        return result;
    }
}
