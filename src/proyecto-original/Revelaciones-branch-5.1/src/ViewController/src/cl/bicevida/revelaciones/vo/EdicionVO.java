package cl.bicevida.revelaciones.vo;

import java.util.ArrayList;
import java.util.List;

public class EdicionVO {
    
    private String titulo;
    private Object valor;
    private Long ancho;
    
    public EdicionVO() {
        super();
    }

    public EdicionVO(String titulo, Object valor, Long ancho) {
        super();
        this.titulo = titulo;
        this.valor = valor;
        this.ancho = ancho;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }

    public Object getValor() {
        return valor;
    }

    public void setAncho(Long ancho) {
        this.ancho = ancho;
    }

    public Long getAncho() {
        return ancho;
    }
}
