package cl.mdr.ifrs.cross.vo;

import java.io.Serializable;

public class TipoFormulaVO implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2024602080171693415L;
	
	private Long valor;
	private String label;
	
	
	
	public TipoFormulaVO(Long valor, String label) {
		super();
		this.valor = valor;
		this.label = label;
	}
	public Long getValor() {
		return valor;
	}
	public void setValor(Long valor) {
		this.valor = valor;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	
	
	

}
