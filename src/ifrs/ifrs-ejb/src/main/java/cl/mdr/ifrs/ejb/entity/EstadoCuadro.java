package cl.mdr.ifrs.ejb.entity;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

import cl.mdr.ifrs.ejb.common.Constantes;


@Entity
@NamedQueries( { @NamedQuery(name = EstadoCuadro.FIND_ALL , query = "select o from EstadoCuadro o order by o.nombre") })
@Table(name = Constantes.ESTADO_CUADRO)
public class EstadoCuadro implements Serializable {
    public static final String FIND_ALL = "Estado.findAll";
    
    private static final long serialVersionUID = -1036501892684060831L;
    
    @Id
    @Column(name = "ID_ESTADO_CUADRO", nullable = false)
    @Expose
    private Long idEstado;
    
    @Column(nullable = false, length = 128)
    @Expose
    private String nombre;
    
    
    public EstadoCuadro() {
    }
    
    public EstadoCuadro(Long idEstado) {
        this.idEstado = idEstado;        
    }

    public EstadoCuadro(Long idEstado, String nombre) {
        this.idEstado = idEstado;
        this.nombre = nombre;
    }


    public Long getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Long idEstado) {
        this.idEstado = idEstado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof EstadoCuadro)) {
            return false;
        }
        final EstadoCuadro other = (EstadoCuadro)object;
        if (!(idEstado == null ? other.idEstado == null : idEstado.equals(other.idEstado))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = 1;
        result = PRIME * result + ((idEstado == null) ? 0 : idEstado.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idEstadoNota=");
        buffer.append(getIdEstado());
        buffer.append(',');
        buffer.append("nombre=");
        buffer.append(getNombre());
        buffer.append(',');
        buffer.append(']');
        return buffer.toString();
    }
    
}
