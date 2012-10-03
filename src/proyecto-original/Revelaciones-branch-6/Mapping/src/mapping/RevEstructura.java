package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "RevEstructura.findAll", query = "select o from RevEstructura o") })
@Table(name = "REV_ESTRUCTURA")
public class RevEstructura implements Serializable {
    @Id
    @Column(name = "ID_ESTRUCTURA", nullable = false)
    private BigDecimal idEstructura;
    @Column(nullable = false)
    private BigDecimal orden;
    @OneToMany(mappedBy = "revEstructura")
    private List<RevHtml> revHtmlList;
    @OneToMany(mappedBy = "revEstructura1")
    private List<RevTexto> revTextoList;
    @OneToMany(mappedBy = "revEstructura2")
    private List<RevGrilla> revGrillaList;
    @ManyToOne
    @JoinColumn(name = "ID_TIPO_ESTRUCTURA")
    private RevTipoEstructura revTipoEstructura;
    @ManyToOne
    @JoinColumn(name = "ID_VERSION")
    private RevVersion revVersion1;

    public RevEstructura() {
    }

    public RevEstructura(BigDecimal idEstructura, RevTipoEstructura revTipoEstructura, RevVersion revVersion1,
                         BigDecimal orden) {
        this.idEstructura = idEstructura;
        this.revTipoEstructura = revTipoEstructura;
        this.revVersion1 = revVersion1;
        this.orden = orden;
    }


    public BigDecimal getIdEstructura() {
        return idEstructura;
    }

    public void setIdEstructura(BigDecimal idEstructura) {
        this.idEstructura = idEstructura;
    }


    public BigDecimal getOrden() {
        return orden;
    }

    public void setOrden(BigDecimal orden) {
        this.orden = orden;
    }

    public List<RevHtml> getRevHtmlList() {
        return revHtmlList;
    }

    public void setRevHtmlList(List<RevHtml> revHtmlList) {
        this.revHtmlList = revHtmlList;
    }

    public RevHtml addRevHtml(RevHtml revHtml) {
        getRevHtmlList().add(revHtml);
        revHtml.setRevEstructura(this);
        return revHtml;
    }

    public RevHtml removeRevHtml(RevHtml revHtml) {
        getRevHtmlList().remove(revHtml);
        revHtml.setRevEstructura(null);
        return revHtml;
    }

    public List<RevTexto> getRevTextoList() {
        return revTextoList;
    }

    public void setRevTextoList(List<RevTexto> revTextoList) {
        this.revTextoList = revTextoList;
    }

    public RevTexto addRevTexto(RevTexto revTexto) {
        getRevTextoList().add(revTexto);
        revTexto.setRevEstructura1(this);
        return revTexto;
    }

    public RevTexto removeRevTexto(RevTexto revTexto) {
        getRevTextoList().remove(revTexto);
        revTexto.setRevEstructura1(null);
        return revTexto;
    }

    public List<RevGrilla> getRevGrillaList() {
        return revGrillaList;
    }

    public void setRevGrillaList(List<RevGrilla> revGrillaList) {
        this.revGrillaList = revGrillaList;
    }

    public RevGrilla addRevGrilla(RevGrilla revGrilla) {
        getRevGrillaList().add(revGrilla);
        revGrilla.setRevEstructura2(this);
        return revGrilla;
    }

    public RevGrilla removeRevGrilla(RevGrilla revGrilla) {
        getRevGrillaList().remove(revGrilla);
        revGrilla.setRevEstructura2(null);
        return revGrilla;
    }

    public RevTipoEstructura getRevTipoEstructura() {
        return revTipoEstructura;
    }

    public void setRevTipoEstructura(RevTipoEstructura revTipoEstructura) {
        this.revTipoEstructura = revTipoEstructura;
    }

    public RevVersion getRevVersion1() {
        return revVersion1;
    }

    public void setRevVersion1(RevVersion revVersion1) {
        this.revVersion1 = revVersion1;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idEstructura=");
        buffer.append(getIdEstructura());
        buffer.append(',');
        buffer.append("orden=");
        buffer.append(getOrden());
        buffer.append(',');
        buffer.append(']');
        return buffer.toString();
    }
}
