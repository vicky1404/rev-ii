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
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ParametroPK id;

	private String valor;

	//bi-directional many-to-one association to TipoParametro
    @ManyToOne
	@JoinColumn(name="ID_TIPO_PARAMETRO" , insertable = false, updatable = false)
	private TipoParametro exfidaTipoParametro;

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

	public TipoParametro getExfidaTipoParametro() {
		return this.exfidaTipoParametro;
	}

	public void setExfidaTipoParametro(TipoParametro exfidaTipoParametro) {
		this.exfidaTipoParametro = exfidaTipoParametro;
	}
	
}