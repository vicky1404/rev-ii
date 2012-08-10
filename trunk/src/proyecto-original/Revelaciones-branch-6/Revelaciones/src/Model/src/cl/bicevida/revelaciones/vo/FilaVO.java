package cl.bicevida.revelaciones.vo;

import cl.bicevida.revelaciones.ejb.entity.Celda;

import java.io.Serializable;

import java.util.List;

public class FilaVO implements Serializable {

    @SuppressWarnings("compatibility:5815735569825730323")
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
