package cl.mdr.ifrs.ejb.dummy.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the IFRS_ESTADO_PERIODO database table.
 * 
 */
@Entity
@Table(name="IFRS_ESTADO_PERIODO")
public class EstadoPeriodo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_ESTADO_PERIODO")
	private Long idEstadoPeriodo;

	private String nombre;

	//bi-directional many-to-one association to Periodo
	@OneToMany(mappedBy="estadoPeriodo")
	private List<Periodo> periodos;

    public EstadoPeriodo() {
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

	public List<Periodo> getPeriodos() {
		return periodos;
	}

	public void setPeriodos(List<Periodo> periodos) {
		this.periodos = periodos;
	}
    
    
	
	
}