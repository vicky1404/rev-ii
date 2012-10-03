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
@NamedQueries( { @NamedQuery(name = "RevVersionEeff.findAll", query = "select o from RevVersionEeff o") })
@Table(name = "REV_VERSION_EEFF")
public class RevVersionEeff implements Serializable {
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Id
    @Column(name = "ID_VERSION_EEFF", nullable = false)
    private BigDecimal idVersionEeff;
    @Column(length = 256)
    private String usuario;
    @Column(nullable = false)
    private BigDecimal version;
    @Column(nullable = false)
    private BigDecimal vigencia;
    @OneToMany(mappedBy = "revVersionEeff")
    private List<RevEeff> revEeffList;
    @ManyToOne
    @JoinColumn(name = "ID_ESTADO_EEFF")
    private RevTipoEstadoEeff revTipoEstadoEeff;
    @ManyToOne
    @JoinColumn(name = "ID_PERIODO")
    private RevPeriodo revPeriodo4;

    public RevVersionEeff() {
    }

    public RevVersionEeff(Date fecha, RevTipoEstadoEeff revTipoEstadoEeff, RevPeriodo revPeriodo4, BigDecimal idVersionEeff,
                          String usuario, BigDecimal version, BigDecimal vigencia) {
        this.fecha = fecha;
        this.revTipoEstadoEeff = revTipoEstadoEeff;
        this.revPeriodo4 = revPeriodo4;
        this.idVersionEeff = idVersionEeff;
        this.usuario = usuario;
        this.version = version;
        this.vigencia = vigencia;
    }


    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }


    public BigDecimal getIdVersionEeff() {
        return idVersionEeff;
    }

    public void setIdVersionEeff(BigDecimal idVersionEeff) {
        this.idVersionEeff = idVersionEeff;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
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

    public List<RevEeff> getRevEeffList() {
        return revEeffList;
    }

    public void setRevEeffList(List<RevEeff> revEeffList) {
        this.revEeffList = revEeffList;
    }

    public RevEeff addRevEeff(RevEeff revEeff) {
        getRevEeffList().add(revEeff);
        revEeff.setRevVersionEeff(this);
        return revEeff;
    }

    public RevEeff removeRevEeff(RevEeff revEeff) {
        getRevEeffList().remove(revEeff);
        revEeff.setRevVersionEeff(null);
        return revEeff;
    }

    public RevTipoEstadoEeff getRevTipoEstadoEeff() {
        return revTipoEstadoEeff;
    }

    public void setRevTipoEstadoEeff(RevTipoEstadoEeff revTipoEstadoEeff) {
        this.revTipoEstadoEeff = revTipoEstadoEeff;
    }

    public RevPeriodo getRevPeriodo4() {
        return revPeriodo4;
    }

    public void setRevPeriodo4(RevPeriodo revPeriodo4) {
        this.revPeriodo4 = revPeriodo4;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("fecha=");
        buffer.append(getFecha());
        buffer.append(',');
        buffer.append("idVersionEeff=");
        buffer.append(getIdVersionEeff());
        buffer.append(',');
        buffer.append("usuario=");
        buffer.append(getUsuario());
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
