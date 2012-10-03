package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries( { @NamedQuery(name = "RevVersion.findAll", query = "select o from RevVersion o") })
@Table(name = "REV_VERSION")
public class RevVersion implements Serializable {
    @Column(name = "CANT_GRUPOS")
    private BigDecimal cantGrupos;
    @Column(length = 256)
    private String comentario;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_CREACION", nullable = false)
    private Date fechaCreacion;
    @Id
    @Column(name = "ID_VERSION", nullable = false)
    private BigDecimal idVersion;
    @Column(name = "VALIDADO_EEFF")
    private BigDecimal validadoEeff;
    @Column(nullable = false)
    private BigDecimal version;
    @Column(nullable = false)
    private BigDecimal vigencia;
    @OneToMany(mappedBy = "revVersion")
    private List<RevVersionPeriodo> revVersionPeriodoList;
    @ManyToOne
    @JoinColumn(name = "ID_CATALOGO")
    private RevCatalogo revCatalogo1;
    @OneToMany(mappedBy = "revVersion1")
    private List<RevEstructura> revEstructuraList1;

    public RevVersion() {
    }

    public RevVersion(BigDecimal cantGrupos, String comentario, Date fechaCreacion, RevCatalogo revCatalogo1,
                      BigDecimal idVersion, BigDecimal validadoEeff, BigDecimal version, BigDecimal vigencia) {
        this.cantGrupos = cantGrupos;
        this.comentario = comentario;
        this.fechaCreacion = fechaCreacion;
        this.revCatalogo1 = revCatalogo1;
        this.idVersion = idVersion;
        this.validadoEeff = validadoEeff;
        this.version = version;
        this.vigencia = vigencia;
    }


    public BigDecimal getCantGrupos() {
        return cantGrupos;
    }

    public void setCantGrupos(BigDecimal cantGrupos) {
        this.cantGrupos = cantGrupos;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }


    public BigDecimal getIdVersion() {
        return idVersion;
    }

    public void setIdVersion(BigDecimal idVersion) {
        this.idVersion = idVersion;
    }

    public BigDecimal getValidadoEeff() {
        return validadoEeff;
    }

    public void setValidadoEeff(BigDecimal validadoEeff) {
        this.validadoEeff = validadoEeff;
    }

    public BigDecimal getVersion() {
        return version;
    }

    public void setVersion(BigDecimal version) {
        this.version = version;
    }

    public BigDecimal getVigencia() {
        return vigencia;
    }

    public void setVigencia(BigDecimal vigencia) {
        this.vigencia = vigencia;
    }

    public List<RevVersionPeriodo> getRevVersionPeriodoList() {
        return revVersionPeriodoList;
    }

    public void setRevVersionPeriodoList(List<RevVersionPeriodo> revVersionPeriodoList) {
        this.revVersionPeriodoList = revVersionPeriodoList;
    }

    public RevVersionPeriodo addRevVersionPeriodo(RevVersionPeriodo revVersionPeriodo) {
        getRevVersionPeriodoList().add(revVersionPeriodo);
        revVersionPeriodo.setRevVersion(this);
        return revVersionPeriodo;
    }

    public RevVersionPeriodo removeRevVersionPeriodo(RevVersionPeriodo revVersionPeriodo) {
        getRevVersionPeriodoList().remove(revVersionPeriodo);
        revVersionPeriodo.setRevVersion(null);
        return revVersionPeriodo;
    }

    public RevCatalogo getRevCatalogo1() {
        return revCatalogo1;
    }

    public void setRevCatalogo1(RevCatalogo revCatalogo1) {
        this.revCatalogo1 = revCatalogo1;
    }

    public List<RevEstructura> getRevEstructuraList1() {
        return revEstructuraList1;
    }

    public void setRevEstructuraList1(List<RevEstructura> revEstructuraList1) {
        this.revEstructuraList1 = revEstructuraList1;
    }

    public RevEstructura addRevEstructura(RevEstructura revEstructura) {
        getRevEstructuraList1().add(revEstructura);
        revEstructura.setRevVersion1(this);
        return revEstructura;
    }

    public RevEstructura removeRevEstructura(RevEstructura revEstructura) {
        getRevEstructuraList1().remove(revEstructura);
        revEstructura.setRevVersion1(null);
        return revEstructura;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("cantGrupos=");
        buffer.append(getCantGrupos());
        buffer.append(',');
        buffer.append("comentario=");
        buffer.append(getComentario());
        buffer.append(',');
        buffer.append("fechaCreacion=");
        buffer.append(getFechaCreacion());
        buffer.append(',');
        buffer.append("idVersion=");
        buffer.append(getIdVersion());
        buffer.append(',');
        buffer.append("validadoEeff=");
        buffer.append(getValidadoEeff());
        buffer.append(',');
        buffer.append("version=");
        buffer.append(getVersion());
        buffer.append(',');
        buffer.append("vigencia=");
        buffer.append(getVigencia());
        buffer.append(']');
        return buffer.toString();
    }
}
