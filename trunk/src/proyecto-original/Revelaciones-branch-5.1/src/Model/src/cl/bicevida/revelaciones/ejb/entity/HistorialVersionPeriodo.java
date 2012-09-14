package cl.bicevida.revelaciones.ejb.entity;


import cl.bicevida.revelaciones.ejb.common.Constantes;

import java.io.Serializable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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


@Entity
@NamedQueries( { @NamedQuery(name = "HistorialVersionPeriodo.findAll",
                             query = "select o from HistorialVersionPeriodo o") })
@Table(name = Constantes.REV_HISTORIAL_VERSION)
public class HistorialVersionPeriodo implements Serializable {
    @SuppressWarnings("compatibility:-7176178118452758698")
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
    
    @Column(length = 256)
    private String usuario;
    
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "ID_VERSION", referencedColumnName = "ID_VERSION"),
        @JoinColumn(name = "ID_PERIODO", referencedColumnName = "ID_PERIODO")
    })
    private VersionPeriodo versionPeriodo;
        
    @JoinColumn(name = "ID_ESTADO_CUADRO")
    private EstadoCuadro estadoCuadro;


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

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setVersionPeriodo(VersionPeriodo versionPeriodo) {
        this.versionPeriodo = versionPeriodo;
    }

    public VersionPeriodo getVersionPeriodo() {
        return versionPeriodo;
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
        if (!(object instanceof HistorialVersionPeriodo)) {
            return false;
        }
        final HistorialVersionPeriodo other = (HistorialVersionPeriodo)object;
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
}
