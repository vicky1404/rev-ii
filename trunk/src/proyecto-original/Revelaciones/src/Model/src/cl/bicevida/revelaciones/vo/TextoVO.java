package cl.bicevida.revelaciones.vo;


import cl.bicevida.revelaciones.ejb.entity.Texto;

import java.io.Serializable;

public class TextoVO implements Serializable{
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
