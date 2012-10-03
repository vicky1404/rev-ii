package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries( { @NamedQuery(name = "RevHistorialVersion.findAll", query = "select o from RevHistorialVersion o") })
@Table(name = "REV_HISTORIAL_VERSION")
public class RevHistorialVersion implements Serializable {
    @Column(length = 2048)
    private String comentario;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_PROCESO")
    private Date fechaProceso;
    @Id
    @Column(name = "ID_HISTORIAL", nullable = false)
    private BigDecimal idHistorial;
    @Column(length = 256)
    private String usuario;
    @ManyToOne
    @JoinColumn(name = "ID_ESTADO_CUADRO")
    private RevEstadoCuadro revEstadoCuadro;
    @ManyToOne
    @JoinColumns({
    @JoinColumn(name = "ID_VERSION", referencedColumnName = "ID_VERSION"),
    @JoinColumn(name = "ID_PERIODO", referencedColumnName = "ID_PERIODO")
    })
    private RevVersionPeriodo revVersionPeriodo1;

    public RevHistorialVersion() {
    }

    public RevHistorialVersion(String comentario, Date fechaProceso, RevEstadoCuadro revEstadoCuadro, BigDecimal idHistorial, RevVersionPeriodo revVersionPeriodo1, String usuario) {
        this.comentario = comentario;
        this.fechaProceso = fechaProceso;
        this.revEstadoCuadro = revEstadoCuadro;
        this.idHistorial = idHistorial;
        this.revVersionPeriodo1 = revVersionPeriodo1;
        this.usuario = usuario;
    }


    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Date getFechaProceso() {
        return fechaProceso;
    }

    public void setFechaProceso(Date fechaProceso) {
        this.fechaProceso = fechaProceso;
    }


    public BigDecimal getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(BigDecimal idHistorial) {
        this.idHistorial = idHistorial;
    }


    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public RevEstadoCuadro getRevEstadoCuadro() {
        return revEstadoCuadro;
    }

    public void setRevEstadoCuadro(RevEstadoCuadro revEstadoCuadro) {
        this.revEstadoCuadro = revEstadoCuadro;
    }

    public RevVersionPeriodo getRevVersionPeriodo1() {
        return revVersionPeriodo1;
    }

    public void setRevVersionPeriodo1(RevVersionPeriodo revVersionPeriodo1) {
        this.revVersionPeriodo1 = revVersionPeriodo1;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("comentario=");
        buffer.append(getComentario());
        buffer.append(',');
        buffer.append("fechaProceso=");
        buffer.append(getFechaProceso());
        buffer.append(',');
        buffer.append("idHistorial=");
        buffer.append(getIdHistorial());
        buffer.append(',');
        buffer.append("usuario=");
        buffer.append(getUsuario());
        buffer.append(']');
        return buffer.toString();
    }
}
