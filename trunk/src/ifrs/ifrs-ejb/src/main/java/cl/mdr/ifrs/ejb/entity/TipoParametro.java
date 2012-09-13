package cl.mdr.ifrs.ejb.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import cl.mdr.ifrs.ejb.common.Constantes;


/**
 * The persistent class for the EXFIDA_TIPO_PARAMETRO database table.
 * 
 */
@Entity
@Table(name= Constantes.TIPO_PARAMETRO)
public class TipoParametro implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_TIPO_PARAMETRO")
	private long idTipoParametro;

	private String nombre;

	//bi-directional many-to-one association to Parametro
	@OneToMany(mappedBy="exfidaTipoParametro")
	private List<Parametro> exfidaParametros;

    public TipoParametro() {
    }

	public long getIdTipoParametro() {
		return this.idTipoParametro;
	}

	public void setIdTipoParametro(long idTipoParametro) {
		this.idTipoParametro = idTipoParametro;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Parametro> getExfidaParametros() {
		return this.exfidaParametros;
	}

	public void setExfidaParametros(List<Parametro> exfidaParametros) {
		this.exfidaParametros = exfidaParametros;
	}
	
}