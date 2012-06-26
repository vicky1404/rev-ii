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
@NamedQueries( { @NamedQuery(name = "EstadoPeriodo.findAll", query = "select o from EstadoPeriodo o order by o.nombre asc") })
@Table(name = Constantes.REV_ESTADO_PERIODO)
public class EstadoPeriodo implements Serializable {
    @SuppressWarnings("compatibility:-3565816229293650930")
    private static final long serialVersionUID = -2621130761052193959L;
    
    public static final Long ESTADO_INICIADO = 0L;
    public static final Long ESTADO_CERRADO = 1L;
    public static final Long ESTADO_CONTINGENCIA = 2L;
    
    @Id
    @Column(name = "ID_ESTADO_PERIODO", nullable = false)
    private Long idEstadoPeriodo;
    @Column(nullable = false, length = 128)
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
}
