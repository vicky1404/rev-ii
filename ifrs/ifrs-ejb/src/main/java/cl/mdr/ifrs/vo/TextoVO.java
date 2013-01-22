package cl.mdr.ifrs.vo;


import java.io.Serializable;

import cl.mdr.ifrs.ejb.entity.Texto;

public class TextoVO implements Serializable{
	private static final long serialVersionUID = 9088045134553637481L;

	public TextoVO() {
        super();
    }
    
    private Texto texto;

    public TextoVO(Texto texto) {
        super();
        this.texto = texto;
    }

    public void setTexto(Texto texto) {
        this.texto = texto;
    }

    public Texto getTexto() {
        return texto;
    }
}
