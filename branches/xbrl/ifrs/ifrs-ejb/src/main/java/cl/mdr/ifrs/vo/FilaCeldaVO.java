package cl.mdr.ifrs.vo;



import java.util.List;

import cl.mdr.ifrs.ejb.entity.Celda;

public class FilaCeldaVO {
    
    public Long fila;
    public List<Celda> celdaList;
    
    public FilaCeldaVO() {
        super();
    }

    public void setFila(Long fila) {
        this.fila = fila;
    }

    public Long getFila() {
        return fila;
    }

    public void setCeldaList(List<Celda> celdaList) {
        this.celdaList = celdaList;
    }

    public List<Celda> getCeldaList() {
        return celdaList;
    }
}
