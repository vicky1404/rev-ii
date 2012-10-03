package cl.bicevida.revelaciones.eeff;

import cl.bicevida.revelaciones.ejb.entity.Celda;

import java.util.List;

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
