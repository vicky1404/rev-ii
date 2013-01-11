package cl.bicevida.revelaciones.ejb.entity;


import cl.bicevida.revelaciones.ejb.common.Constantes;

import java.io.Serializable;

import java.lang.Long;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@NamedQueries( { @NamedQuery(name = EstadoCuadro.FIND_ALL , query = "select o from EstadoCuadro o order by o.nombre"),
                 @NamedQuery(name = EstadoCuadro.FIND_BY_ID, query = "select o from EstadoCuadro o where o.idEstado = :idEstado")})
@Table(name = Constantes.REV_ESTADO_CUADRO)
public class EstadoCuadro implements Serializable {
    public static final String FIND_ALL = "Estado.findAll";
    public static final String FIND_BY_ID = "Estado.findById";
    
    @SuppressWarnings("compatibility:6224906051675648598")
    private static final long serialVersionUID = -1036501892684060831L;
    @Id
    @Column(name = "ID_ESTADO_CUADRO", nullable = false)
    private Long idEstado;
    @Column(nullable = false, length = 128)
    private String nombre;
    @OneToMany(mappedBy = "estado")
    private List<VersionPeriodo> versionPeriodoList;

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

    public List<VersionPeriodo> getVersionPeriodoList() {
        return versionPeriodoList;
    }

    public void setVersionPeriodoList(List<VersionPeriodo> periodoNotaList) {
        this.versionPeriodoList = periodoNotaList;
    }

    public VersionPeriodo addPeriodoNota(VersionPeriodo versionPeriodo) {
        getVersionPeriodoList().add(versionPeriodo);
        versionPeriodo.setEstado(this);
        return versionPeriodo;
    }

    public VersionPeriodo removePeriodoNota(VersionPeriodo versionPeriodo) {
        getVersionPeriodoList().remove(versionPeriodo);
        versionPeriodo.setEstado(null);
        return versionPeriodo;
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
