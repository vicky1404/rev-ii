package cl.mdr.ifrs.ejb.entity;

import java.io.Serializable;
import javax.persistence.*;

import cl.mdr.ifrs.ejb.common.Constantes;


/**
 * The persistent class for the EXFIDA_PARAMETRO database table.
 * 
 */
@Entity
@Table(name=Constantes.PARAMETRO)
public class Parametro implements Serializable {	
	private static final long serialVersionUID = 4250392142307076068L;

	@EmbeddedId
	private ParametroPK id;

	private String valor;
	
    @ManyToOne
	@JoinColumn(name="ID_TIPO_PARAMETRO" , insertable = false, updatable = false)
	private TipoParametro tipoParametro;

    public Parametro() {
    }

	public ParametroPK getId() {
		return this.id;
	}

	public void setId(ParametroPK id) {
		this.id = id;
	}
	
	public String getValor() {
		return this.valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public TipoParametro getTipoParametro() {
		return tipoParametro;
	}

	public void setTipoParametro(TipoParametro tipoParametro) {
		this.tipoParametro = tipoParametro;
	}
	
	
	
}