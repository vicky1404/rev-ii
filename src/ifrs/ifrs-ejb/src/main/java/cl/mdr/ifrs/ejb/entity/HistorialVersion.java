package cl.mdr.ifrs.ejb.entity;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import cl.mdr.ifrs.ejb.common.Constantes;


@Entity
/*@NamedQueries( { @NamedQuery(name = "HistorialVersionPeriodo.findAll",
                             query = "select o from HistorialVersionPeriodo o") })*/
@Table(name = Constantes.HISTORIAL_VERSION)
public class HistorialVersion implements Serializable {
    private static final long serialVersionUID = -115717143071849171L;
    
    @Id
    @GeneratedValue(generator="ID_GEN_HISTORIAL_VERSION")
    @SequenceGenerator(name="ID_GEN_HISTORIAL_VERSION", sequenceName = "SEQ_HISTORIAL_VERSION" ,allocationSize = 1) 
    @Column(name = "ID_HISTORIAL", nullable = false)
    private Long idHistorial;
    
    @Column(length = 2048)
    private String comentario;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_PROCESO")
    private Date fechaProceso;
        
    @ManyToOne
    @JoinColumn(name = "ID_PERIODO", referencedColumnName = "ID_PERIODO")
    private Version version;
        
    @JoinColumn(name = "ID_ESTADO_CUADRO")
    private EstadoCuadro estadoCuadro;
    
    @ManyToOne
	@JoinColumn(name="NOMBRE_USUARIO")
	private Usuario usuario;


    public void setIdHistorial(Long idHistorial) {
        this.idHistorial = idHistorial;
    }

    public Long getIdHistorial() {
        return idHistorial;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setFechaProceso(Date fechaProceso) {
        this.fechaProceso = fechaProceso;
    }

    public Date getFechaProceso() {
        return fechaProceso;
    }
    
    public void setEstadoCuadro(EstadoCuadro estadoCuadro) {
        this.estadoCuadro = estadoCuadro;
    }

    public EstadoCuadro getEstadoCuadro() {
        return estadoCuadro;
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof HistorialVersion)) {
            return false;
        }
        final HistorialVersion other = (HistorialVersion)object;
        if (!(idHistorial == null ? other.idHistorial == null : idHistorial.equals(other.idHistorial))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = 1;
        result = PRIME * result + ((idHistorial == null) ? 0 : idHistorial.hashCode());
        return result;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public Version getVersion() {
        return version;
    }

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
