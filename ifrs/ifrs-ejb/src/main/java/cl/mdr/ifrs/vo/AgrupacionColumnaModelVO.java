package cl.mdr.ifrs.vo;


import java.io.Serializable;
import java.util.List;

import cl.mdr.ifrs.ejb.entity.Columna;



public class AgrupacionColumnaModelVO implements Serializable{
	private static final long serialVersionUID = 4458790360676830887L;

	public AgrupacionColumnaModelVO() {
        super();
    }
        
    private List<AgrupacionColumnaModelVO> niveles;
    private List<Columna> columnas;
    private String tituloNivel;
    private AgrupacionModelVO agrupacion;
    private Long nivel;

    public void setNiveles(List<AgrupacionColumnaModelVO> nivel) {
        this.niveles = nivel;
    }

    public List<AgrupacionColumnaModelVO> getNiveles() {
        return niveles;
    }

    public void setColumnas(List<Columna> columnas) {
        this.columnas = columnas;
    }

    public List<Columna> getColumnas() {
        return columnas;
    }

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
}
