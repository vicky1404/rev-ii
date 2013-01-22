package cl.mdr.ifrs.vo;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.ifrs.ejb.entity.Columna;
import cl.mdr.ifrs.ejb.entity.Grilla;


/**
 * Clase no persistente que representa los campos de una grilla
 * calculados y en formato filas por columna
 */
public class GrillaVO implements Serializable {
    private static final long serialVersionUID = -2599361993445052185L;
    
    private String titulo;
    private Grilla grilla;
    
    private List<Columna> columnas;
    private List<Map<Long,Celda>> rows;
    private List<List<AgrupacionModelVO>> agrupaciones;

	private List<List<Celda>> celdaList;
    private Long nivel = 0L;
    
    
    public GrillaVO() {
        super();
    }

    public void setGrilla(Grilla grilla) {
        this.grilla = grilla;
    }

    public Grilla getGrilla() {
        return grilla;
    }
    
    public Long getNivel() {
		return nivel;
	}


	public void setNivel(Long nivel) {
		this.nivel = nivel;
	}


    public void setColumnas(List<Columna> columnas) {
        this.columnas = columnas;
    }

    public List<Columna> getColumnas() {
    	if (columnas == null){
    		columnas = new ArrayList<Columna>();
    	}
        return columnas;
    }
    
    public void setRows(List<Map<Long, Celda>> rows) {
        this.rows = rows;
    }

    public List<Map<Long, Celda>> getRows() {
    	
    	if (rows == null){
    		rows = new ArrayList<Map<Long,Celda>>();
    	}
    	
        return rows;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }


	public List<List<Celda>> getCeldaList() {
		return celdaList;
	}


	public void setCeldaList(List<List<Celda>> celdaList) {
		this.celdaList = celdaList;
	}


	public List<List<AgrupacionModelVO>> getAgrupaciones() {
		return agrupaciones;
	}


	public void setAgrupaciones(List<List<AgrupacionModelVO>> agrupaciones) {
		this.agrupaciones = agrupaciones;
	}
	
	public boolean isRendererAgrupaciones(){
		return Util.esListaValida(this.agrupaciones);
	}
}
