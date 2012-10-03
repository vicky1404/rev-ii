package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries( { @NamedQuery(name = "RevLogProceso.findAll", query = "select o from RevLogProceso o") })
@Table(name = "REV_LOG_PROCESO")
public class RevLogProceso implements Serializable {
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Id
    @Column(name = "ID_LOG", nullable = false)
    private BigDecimal idLog;
    @Column(length = 4000)
    private String log;
    @Column(length = 256)
    private String usuario;

    public RevLogProceso() {
    }

    public RevLogProceso(Date fecha, BigDecimal idLog, String log, String usuario) {
        this.fecha = fecha;
        this.idLog = idLog;
        this.log = log;
        this.usuario = usuario;
    }


    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getIdLog() {
        return idLog;
    }

    public void setIdLog(BigDecimal idLog) {
        this.idLog = idLog;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("fecha=");
        buffer.append(getFecha());
        buffer.append(',');
        buffer.append("idLog=");
        buffer.append(getIdLog());
        buffer.append(',');
        buffer.append("log=");
        buffer.append(getLog());
        buffer.append(',');
        buffer.append("usuario=");
        buffer.append(getUsuario());
        buffer.append(']');
        return buffer.toString();
    }
}
