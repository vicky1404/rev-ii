package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Date;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries( { @NamedQuery(name = "RevVersionPeriodo.findAll", query = "select o from RevVersionPeriodo o") })
@Table(name = "REV_VERSION_PERIODO")
@IdClass(RevVersionPeriodoPK.class)
public class RevVersionPeriodo implements Serializable {
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_CREACION", nullable = false)
    private Date fechaCreacion;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_ULTIMO_PROCESO")
    private Date fechaUltimoProceso;
    @Id
    @Column(name = "ID_PERIODO", nullable = false, insertable = false, updatable = false)
    private BigDecimal idPeriodo;
    @Id
    @Column(name = "ID_VERSION", nullable = false, insertable = false, updatable = false)
    private BigDecimal idVersion;
    @Column(nullable = false, length = 256)
    private String usuario;
    @ManyToOne
    @JoinColumn(name = "ID_VERSION")
    private RevVersion revVersion;
    @OneToMany(mappedBy = "revVersionPeriodo1")
    private List<RevHistorialVersion> revHistorialVersionList2;
    @ManyToOne
    @JoinColumn(name = "ID_ESTADO_CUADRO")
    private RevEstadoCuadro revEstadoCuadro1;
    @ManyToOne
    @JoinColumn(name = "ID_PERIODO")
    private RevPeriodo revPeriodo2;

    public RevVersionPeriodo() {
    }

    public RevVersionPeriodo(Date fechaCreacion, Date fechaUltimoProceso, RevEstadoCuadro revEstadoCuadro1,
                             RevPeriodo revPeriodo2, RevVersion revVersion, String usuario) {
        this.fechaCreacion = fechaCreacion;
        this.fechaUltimoProceso = fechaUltimoProceso;
        this.revEstadoCuadro1 = revEstadoCuadro1;
        this.revPeriodo2 = revPeriodo2;
        this.revVersion = revVersion;
        this.usuario = usuario;
    }


    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaUltimoProceso() {
        return fechaUltimoProceso;
    }

    public void setFechaUltimoProceso(Date fechaUltimoProceso) {
        this.fechaUltimoProceso = fechaUltimoProceso;
    }


    public BigDecimal getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(BigDecimal idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public BigDecimal getIdVersion() {
        return idVersion;
    }

    public void setIdVersion(BigDecimal idVersion) {
        this.idVersion = idVersion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public RevVersion getRevVersion() {
        return revVersion;
    }

    public void setRevVersion(RevVersion revVersion) {
        this.revVersion = revVersion;
        if (revVersion != null) {
            this.idVersion = revVersion.getIdVersion();
        }
    }

    public List<RevHistorialVersion> getRevHistorialVersionList2() {
        return revHistorialVersionList2;
    }

    public void setRevHistorialVersionList2(List<RevHistorialVersion> revHistorialVersionList2) {
        this.revHistorialVersionList2 = revHistorialVersionList2;
    }

    public RevHistorialVersion addRevHistorialVersion(RevHistorialVersion revHistorialVersion) {
        getRevHistorialVersionList2().add(revHistorialVersion);
        revHistorialVersion.setRevVersionPeriodo1(this);
        return revHistorialVersion;
    }

    public RevHistorialVersion removeRevHistorialVersion(RevHistorialVersion revHistorialVersion) {
        getRevHistorialVersionList2().remove(revHistorialVersion);
        revHistorialVersion.setRevVersionPeriodo1(null);
        return revHistorialVersion;
    }

    public RevEstadoCuadro getRevEstadoCuadro1() {
        return revEstadoCuadro1;
    }

    public void setRevEstadoCuadro1(RevEstadoCuadro revEstadoCuadro1) {
        this.revEstadoCuadro1 = revEstadoCuadro1;
    }

    public RevPeriodo getRevPeriodo2() {
        return revPeriodo2;
    }

    public void setRevPeriodo2(RevPeriodo revPeriodo2) {
        this.revPeriodo2 = revPeriodo2;
        if (revPeriodo2 != null) {
            this.idPeriodo = revPeriodo2.getIdPeriodo();
        }
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("fechaCreacion=");
        buffer.append(getFechaCreacion());
        buffer.append(',');
        buffer.append("fechaUltimoProceso=");
        buffer.append(getFechaUltimoProceso());
        buffer.append(',');
        buffer.append("idPeriodo=");
        buffer.append(getIdPeriodo());
        buffer.append(',');
        buffer.append("idVersion=");
        buffer.append(getIdVersion());
        buffer.append(',');
        buffer.append("usuario=");
        buffer.append(getUsuario());
        buffer.append(']');
        return buffer.toString();
    }
}
