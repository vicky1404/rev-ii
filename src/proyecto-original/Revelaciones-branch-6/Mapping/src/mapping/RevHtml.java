package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "RevHtml.findAll", query = "select o from RevHtml o") })
@Table(name = "REV_HTML")
public class RevHtml implements Serializable {
    private byte[] contenido;
    @Id
    @Column(name = "ID_HTML", nullable = false, insertable = false, updatable = false)
    private BigDecimal idHtml;
    @Column(length = 256)
    private String titulo;
    @ManyToOne
    @JoinColumn(name = "ID_HTML")
    private RevEstructura revEstructura;

    public RevHtml() {
    }

    public RevHtml(RevEstructura revEstructura, String titulo) {
        this.revEstructura = revEstructura;
        this.titulo = titulo;
    }


    public byte[] getContenido() {
        return contenido;
    }

    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }

    public BigDecimal getIdHtml() {
        return idHtml;
    }

    public void setIdHtml(BigDecimal idHtml) {
        this.idHtml = idHtml;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public RevEstructura getRevEstructura() {
        return revEstructura;
    }

    public void setRevEstructura(RevEstructura revEstructura) {
        this.revEstructura = revEstructura;
        if (revEstructura != null) {
            this.idHtml = revEstructura.getIdEstructura();
        }
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
}
