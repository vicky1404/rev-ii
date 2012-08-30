package cl.mdr.ifrs.ejb.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import cl.mdr.ifrs.ejb.common.Constantes;

@Entity
@NamedQueries({
  @NamedQuery(name = "HistorialReporte.findAll", query = "select o from HistorialReporte o"),
  @NamedQuery(name = HistorialReporte.FIND_ALL_BY_PERIODO, query = "select o from HistorialReporte o where o.periodoEmpresa.idPeriodo = :idPeriodo order by o.fechaExportacion desc")
})
@Table(name = Constantes.HISTORIAL_REPORTE)
public class HistorialReporte implements Serializable {
    private static final long serialVersionUID = 3916741290824425093L;
    
    public static final String FIND_ALL_BY_PERIODO =  "HistorialReporte.findAllByPeriodo";
    
    @Id
    @GeneratedValue(generator="ID_GEN_HISTORIAL_REPORTE")
    @SequenceGenerator(name="ID_GEN_HISTORIAL_REPORTE", sequenceName = "SEQ_HISTORIAL_REPORTE" , allocationSize = 1)
    @Column(name="ID_HISTORIAL_REPORTE", nullable = false)
    private BigDecimal idHistorialReporte;
    
    @Column(name="CHECK_SUM_EXPORTACION", length = 512)
    private String checkSumExportacion;
    
    @Column(length = 1024)
    private String comentario;
    
    private byte[] documento;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="FECHA_EXPORTACION")
    private Date fechaExportacion;
    
    @Column(name="IP_USUARIO", length = 256)
    private String ipUsuario;
            
    @Column(name="NOMBRE_ARCHIVO", length = 512)
    private String nombreArchivo;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns( { @JoinColumn(name = "ID_PERIODO", referencedColumnName = "ID_PERIODO"),
			        @JoinColumn(name = "ID_RUT", referencedColumnName = "ID_RUT")})
    private PeriodoEmpresa periodoEmpresa;

	@ManyToOne
	@JoinColumn(name="NOMBRE_USUARIO")
	private Usuario usuario;
	
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PERIODO" , insertable = false, updatable = false)
    private Periodo periodo;

    public HistorialReporte() {
    }
    
    

    public Usuario getUsuario() {
		return usuario;
	}



	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}



	public String getCheckSumExportacion() {
        return checkSumExportacion;
    }

    public void setCheckSumExportacion(String checkSumExportacion) {
        this.checkSumExportacion = checkSumExportacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public byte[] getDocumento() {
        return documento;
    }

    public void setDocumento(byte[] documento) {
        this.documento = documento;
    }

    public Date getFechaExportacion() {
        return fechaExportacion;
    }

    public void setFechaExportacion(Date fechaExportacion) {
        this.fechaExportacion = fechaExportacion;
    }

    public BigDecimal getIdHistorialReporte() {
        return idHistorialReporte;
    }

    public void setIdHistorialReporte(BigDecimal idHistorialReporte) {
        this.idHistorialReporte = idHistorialReporte;
    }

    public String getIpUsuario() {
        return ipUsuario;
    }

    public void setIpUsuario(String ipUsuario) {
        this.ipUsuario = ipUsuario;
    }
    
    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }
    
    public PeriodoEmpresa getPeriodoEmpresa() {
		return periodoEmpresa;
	}



	public void setPeriodoEmpresa(PeriodoEmpresa periodoEmpresa) {
		this.periodoEmpresa = periodoEmpresa;
	}
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("checkSumExportacion=");
        buffer.append(getCheckSumExportacion());
        buffer.append(',');
        buffer.append("comentario=");
        buffer.append(getComentario());
        buffer.append(',');
        buffer.append("documento=");
        buffer.append(getDocumento());
        buffer.append(',');
        buffer.append("fechaExportacion=");
        buffer.append(getFechaExportacion());
        buffer.append(',');
        buffer.append("idHistorialReporte=");
        buffer.append(getIdHistorialReporte());
        buffer.append(',');
        buffer.append("ipUsuario=");
        buffer.append(getIpUsuario());
        buffer.append(',');       
        buffer.append(']');
        return buffer.toString();
    }



	/**
	 * @return the periodo
	 */
	public Periodo getPeriodo() {
		return periodo;
	}



	/**
	 * @param periodo the periodo to set
	 */
	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}

    
}
