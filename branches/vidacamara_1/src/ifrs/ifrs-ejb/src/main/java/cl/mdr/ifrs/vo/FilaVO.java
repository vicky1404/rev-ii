package cl.mdr.ifrs.vo;

import java.io.Serializable;
import java.util.List;

import cl.mdr.ifrs.ejb.entity.Celda;

public class FilaVO implements Serializable {

    private static final long serialVersionUID = 5685296347923924269L;
    
    private Long numeroFila;
    private List<Celda> celdas;
    
    public FilaVO() {
        super();
    }


    public void setNumeroFila(Long numeroFila) {
        this.numeroFila = numeroFila;
    }

    public Long getNumeroFila() {
        return numeroFila;
    }

    public void setCeldas(List<Celda> celdas) {
        this.celdas = celdas;
    }

    public List<Celda> getCeldas() {
        return celdas;
    }
}
