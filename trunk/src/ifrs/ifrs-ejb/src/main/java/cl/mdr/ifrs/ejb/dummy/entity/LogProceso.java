package cl.mdr.ifrs.ejb.dummy.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the IFRS_LOG_PROCESO database table.
 * 
 */
@Entity
@Table(name="IFRS_LOG_PROCESO")
public class LogProceso implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_LOG")
	private long idLog;

    @Temporal( TemporalType.DATE)
	private Date fecha;

	@Column(name="\"LOG\"")
	private String log;

	private String usuario;

    public LogProceso() {
    }

	public long getIdLog() {
		return this.idLog;
	}

	public void setIdLog(long idLog) {
		this.idLog = idLog;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getLog() {
		return this.log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

}