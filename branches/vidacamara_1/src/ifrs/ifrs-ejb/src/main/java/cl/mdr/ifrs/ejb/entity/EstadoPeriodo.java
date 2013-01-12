package cl.mdr.ifrs.ejb.entity;


import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

import cl.mdr.ifrs.ejb.common.Constantes;


@Entity
@NamedQueries( { @NamedQuery(name = "EstadoPeriodo.findAll", query = "select o from EstadoPeriodo o order by o.nombre asc") })
@Table(name = Constantes.ESTADO_PERIODO)
public class EstadoPeriodo implements Serializable {
    private static final long serialVersionUID = -2621130761052193959L;
    
    public static final Long ESTADO_INICIADO = 0L;
    public static final Long ESTADO_CERRADO = 1L;
    public static final Long ESTADO_CONTINGENCIA = 2L;
    
    @Id
    @Column(name = "ID_ESTADO_PERIODO", nullable = false)    
    @Expose
    private Long idEstadoPeriodo;
    
    @Column(nullable = false, length = 128)
    @Expose
    private String nombre;
    
    @OneToMany(mappedBy = "estadoPeriodo")
    private List<Periodo> periodoList;

    public EstadoPeriodo() {
    }

    public EstadoPeriodo(Long idEstadoPeriodo, String nombre) {
        this.idEstadoPeriodo = idEstadoPeriodo;
        this.nombre = nombre;
    }


    public Long getIdEstadoPeriodo() {
        return idEstadoPeriodo;
    }

    public void setIdEstadoPeriodo(Long idEstadoPeriodo) {
        this.idEstadoPeriodo = idEstadoPeriodo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Periodo> getPeriodoList() {
        return periodoList;
    }

    public void setPeriodoList(List<Periodo> periodoList) {
        this.periodoList = periodoList;
    }

    public Periodo addPeriodo(Periodo periodo) {
        getPeriodoList().add(periodo);
        periodo.setEstadoPeriodo(this);
        return periodo;
    }

    public Periodo removePeriodo(Periodo periodo) {
        getPeriodoList().remove(periodo);
        periodo.setEstadoPeriodo(null);
        return periodo;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idEstadoPeriodo=");
        buffer.append(getIdEstadoPeriodo());
        buffer.append(',');
        buffer.append("nombre=");
        buffer.append(getNombre());
        buffer.append(',');
        buffer.append(']');
        return buffer.toString();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idEstadoPeriodo == null) ? 0 : idEstadoPeriodo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EstadoPeriodo other = (EstadoPeriodo) obj;
		if (idEstadoPeriodo == null) {
			if (other.idEstadoPeriodo != null)
				return false;
		} else if (!idEstadoPeriodo.equals(other.idEstadoPeriodo))
			return false;
		return true;
	}
}
