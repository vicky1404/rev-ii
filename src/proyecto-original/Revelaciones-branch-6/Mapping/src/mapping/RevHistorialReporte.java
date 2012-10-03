package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries( { @NamedQuery(name = "RevHistorialReporte.findAll", query = "select o from RevHistorialReporte o") })
@Table(name = "REV_HISTORIAL_REPORTE")
public class RevHistorialReporte implements Serializable {
    @Column(name = "CHECK_SUM_EXPORTACION", length = 512)
    private String checkSumExportacion;
    @Column(length = 1024)
    private String comentario;
    private byte[] documento;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_EXPORTACION")
    private Date fechaExportacion;
    @Id
    @Column(name = "ID_HISTORIAL_REPORTE", nullable = false)
    private BigDecimal idHistorialReporte;
    @Column(name = "IP_USUARIO", length = 256)
    private String ipUsuario;
    @Column(name = "NOMBRE_ARCHIVO", length = 512)
    private String nombreArchivo;
    @Column(name = "USUARIO_OID", length = 256)
    private String usuarioOid;
    @ManyToOne
    @JoinColumn(name = "ID_PERIODO")
    private RevPeriodo revPeriodo;

    public RevHistorialReporte() {
    }

    public RevHistorialReporte(String checkSumExportacion, String comentario, Date fechaExportacion,
                               BigDecimal idHistorialReporte, RevPeriodo revPeriodo, String ipUsuario,
                               String nombreArchivo, String usuarioOid) {
        this.checkSumExportacion = checkSumExportacion;
        this.comentario = comentario;
        this.fechaExportacion = fechaExportacion;
        this.idHistorialReporte = idHistorialReporte;
        this.revPeriodo = revPeriodo;
        this.ipUsuario = ipUsuario;
        this.nombreArchivo = nombreArchivo;
        this.usuarioOid = usuarioOid;
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

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getUsuarioOid() {
        return usuarioOid;
    }

    public void setUsuarioOid(String usuarioOid) {
        this.usuarioOid = usuarioOid;
    }

    public RevPeriodo getRevPeriodo() {
        return revPeriodo;
    }

    public void setRevPeriodo(RevPeriodo revPeriodo) {
        this.revPeriodo = revPeriodo;
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
        buffer.append("nombreArchivo=");
        buffer.append(getNombreArchivo());
        buffer.append(',');
        buffer.append("usuarioOid=");
        buffer.append(getUsuarioOid());
        buffer.append(']');
        return buffer.toString();
    }
}
