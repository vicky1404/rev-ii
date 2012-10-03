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
@NamedQueries( { @NamedQuery(name = "RevTexto.findAll", query = "select o from RevTexto o") })
@Table(name = "REV_TEXTO")
public class RevTexto implements Serializable {
    @Id
    @Column(name = "ID_TEXTO", nullable = false, insertable = false, updatable = false)
    private BigDecimal idTexto;
    @Column(nullable = false)
    private BigDecimal negrita;
    @Column(length = 4000)
    private String texto;
    @ManyToOne
    @JoinColumn(name = "ID_TEXTO")
    private RevEstructura revEstructura1;

    public RevTexto() {
    }

    public RevTexto(RevEstructura revEstructura1, BigDecimal negrita, String texto) {
        this.revEstructura1 = revEstructura1;
        this.negrita = negrita;
        this.texto = texto;
    }


    public BigDecimal getIdTexto() {
        return idTexto;
    }

    public void setIdTexto(BigDecimal idTexto) {
        this.idTexto = idTexto;
    }

    public BigDecimal getNegrita() {
        return negrita;
    }

    public void setNegrita(BigDecimal negrita) {
        this.negrita = negrita;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public RevEstructura getRevEstructura1() {
        return revEstructura1;
    }

    public void setRevEstructura1(RevEstructura revEstructura1) {
        this.revEstructura1 = revEstructura1;
        if (revEstructura1 != null) {
            this.idTexto = revEstructura1.getIdEstructura();
        }
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idTexto=");
        buffer.append(getIdTexto());
        buffer.append(',');
        buffer.append("negrita=");
        buffer.append(getNegrita());
        buffer.append(',');
        buffer.append("texto=");
        buffer.append(getTexto());
        buffer.append(']');
        return buffer.toString();
    }
}
