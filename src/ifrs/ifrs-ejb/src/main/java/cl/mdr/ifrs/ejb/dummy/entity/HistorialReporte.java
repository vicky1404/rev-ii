package cl.mdr.ifrs.ejb.dummy.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the IFRS_HISTORIAL_REPORTE database table.
 * 
 */
@Entity
@Table(name="IFRS_HISTORIAL_REPORTE")
public class HistorialReporte implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_HISTORIAL_REPORTE")
	private long idHistorialReporte;

	@Column(name="CHECK_SUM_EXPORTACION")
	private String checkSumExportacion;

	private String comentario;

    @Lob()
	private byte[] documento;

    @Temporal( TemporalType.DATE)
	@Column(name="FECHA_EXPORTACION")
	private Date fechaExportacion;

	@Column(name="IP_USUARIO")
	private String ipUsuario;

	@Column(name="NOMBRE_ARCHIVO")
	private String nombreArchivo;

	//bi-directional many-to-one association to Periodo
    @ManyToOne
	@JoinColumn(name="ID_PERIODO")
	private Periodo ifrsPeriodo;

	//bi-directional many-to-one association to Usuario
    @ManyToOne
	@JoinColumn(name="NOMBRE_USUARIO")
	private Usuario ifrsUsuario;

    public HistorialReporte() {
    }

	public long getIdHistorialReporte() {
		return this.idHistorialReporte;
	}

	public void setIdHistorialReporte(long idHistorialReporte) {
		this.idHistorialReporte = idHistorialReporte;
	}

	public String getCheckSumExportacion() {
		return this.checkSumExportacion;
	}

	public void setCheckSumExportacion(String checkSumExportacion) {
		this.checkSumExportacion = checkSumExportacion;
	}

	public String getComentario() {
		return this.comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public byte[] getDocumento() {
		return this.documento;
	}

	public void setDocumento(byte[] documento) {
		this.documento = documento;
	}

	public Date getFechaExportacion() {
		return this.fechaExportacion;
	}

	public void setFechaExportacion(Date fechaExportacion) {
		this.fechaExportacion = fechaExportacion;
	}

	public String getIpUsuario() {
		return this.ipUsuario;
	}

	public void setIpUsuario(String ipUsuario) {
		this.ipUsuario = ipUsuario;
	}

	public String getNombreArchivo() {
		return this.nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	public Periodo getIfrsPeriodo() {
		return this.ifrsPeriodo;
	}

	public void setIfrsPeriodo(Periodo ifrsPeriodo) {
		this.ifrsPeriodo = ifrsPeriodo;
	}
	
	public Usuario getIfrsUsuario() {
		return this.ifrsUsuario;
	}

	public void setIfrsUsuario(Usuario ifrsUsuario) {
		this.ifrsUsuario = ifrsUsuario;
	}
	
}