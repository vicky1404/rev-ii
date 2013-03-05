package cl.bicevida.revelaciones.vo;


import cl.bicevida.revelaciones.ejb.entity.SubColumna;

import java.io.Serializable;

import java.util.List;


public class AgrupacionSubColumnaModelVO implements Serializable{
    
    public AgrupacionSubColumnaModelVO() {
        super();
    }
        
    private List<AgrupacionSubColumnaModelVO> niveles;
    private List<SubColumna> subColumnas;
    private String tituloNivel;
    private AgrupacionModelVO agrupacion;
    private Long nivel;

   

    public void setTituloNivel(String tituloNivel) {
        this.tituloNivel = tituloNivel;
    }

    public String getTituloNivel() {
        return tituloNivel;
    }

    public void setAgrupacion(AgrupacionModelVO agrupacion) {
        this.agrupacion = agrupacion;
    }

    public AgrupacionModelVO getAgrupacion() {
        return agrupacion;
    }

    public void setNivel(Long niveles) {
        this.nivel = niveles;
    }

    public Long getNivel() {
        return nivel;
    }

    public void setNiveles(List<AgrupacionSubColumnaModelVO> niveles) {
        this.niveles = niveles;
    }

    public List<AgrupacionSubColumnaModelVO> getNiveles() {
        return niveles;
    }

    public void setSubColumnas(List<SubColumna> subColumnas) {
        this.subColumnas = subColumnas;
    }

    public List<SubColumna> getSubColumnas() {
        return subColumnas;
    }
}
