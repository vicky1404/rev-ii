package cl.mdr.ifrs.ejb.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import cl.mdr.ifrs.ejb.common.Constantes;


/**
 * @author http://www.mdrtech.cl
 * The persistent class for the EXFIDA_TIPO_PARAMETRO database table.
 * 
 */
@Entity
@NamedQueries( { 
    @NamedQuery(name = TipoParametro.FIND_ALL , query = " select tp from TipoParametro tp left join fetch tp.parametros")
})
@Table(name= Constantes.TIPO_PARAMETRO)
public class TipoParametro implements Serializable {	
	private static final long serialVersionUID = 7554058215498947457L;

	public static final String FIND_ALL = "TipoParametro.findAll";

	@Id
	@Column(name="ID_TIPO_PARAMETRO")
	private long idTipoParametro;

	private String nombre;
	
	@OneToMany(mappedBy="tipoParametro" , fetch = FetchType.LAZY)
	private List<Parametro> parametros;

    public TipoParametro() {
    }
    
	public TipoParametro(long idTipoParametro) {
		super();
		this.idTipoParametro = idTipoParametro;
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

	public List<Parametro> getParametros() {
		return parametros;
	}

	public void setParametros(List<Parametro> parametros) {
		this.parametros = parametros;
	}

	
	
}