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
    
    //private String titulo;
    private Grilla grilla;
    //private transient List<Map<String, Object>> columns;    
    //private transient List<Map<String, Object>> grupos;
    //private transient List<Map<String, Object>> gruposResultado;
    //private transient List<AgrupacionColumnaModelVO> nivel2List;
    //private transient List<AgrupacionColumnaModelVO> nivel1List;
    //private transient List<Long> registros;
    
    private List<Columna> columnas;
    private List<Map<Long,Celda>> rows;
    private List<List<AgrupacionModelVO>> agrupaciones;
    
    public Long getNivel() {
		return nivel;
	}


	public void setNivel(Long nivel) {
		this.nivel = nivel;
	}


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
/*
    public void setColumns(List<Map<String, Object>> columns) {
        this.columns = columns;
    }

    public List<Map<String, Object>> getColumns() {
        return columns;
    }
    
    public void setGrupos(List<Map<String, Object>> grupos) {
        this.grupos = grupos;
    }

    public List<Map<String, Object>> getGrupos() {
        return grupos;
    }

    public void setGruposResultado(List<Map<String, Object>> gruposResultado) {
        this.gruposResultado = gruposResultado;
    }

    public List<Map<String, Object>> getGruposResultado() {
        return gruposResultado;
    }
*/
    /*public void setRegistros(List<Long> registros) {
        this.registros = registros;
    }

    public List<Long> getRegistros() {
       
        registros = new ArrayList<Long>();
        registros.add(1L);
                  
            return registros;
    }*/

    /*public void setNivel2List(List<AgrupacionColumnaModelVO> nivel2List) {
        this.nivel2List = nivel2List;
    }

    public List<AgrupacionColumnaModelVO> getNivel2List() {
        return nivel2List;
    }

    public void setNivel1List(List<AgrupacionColumnaModelVO> nivel1List) {
        this.nivel1List = nivel1List;
    }

    public List<AgrupacionColumnaModelVO> getNivel1List() {
        return nivel1List;
    }*/

    public void setColumnas(List<Columna> columnas) {
        this.columnas = columnas;
    }

    public List<Columna> getColumnas() {
    	if (columnas == null){
    		columnas = new ArrayList<Columna>();
    	}
        return columnas;
    }
    /*
    public void setNivel(Long nivel) {
        this.nivel = nivel;
    }

    public Long getNivel() {
        return nivel;
    }*/
    
    public void setRows(List<Map<Long, Celda>> rows) {
        this.rows = rows;
    }

    public List<Map<Long, Celda>> getRows() {
    	
    	if (rows == null){
    		rows = new ArrayList<Map<Long,Celda>>();
    	}
    	
        return rows;
    }

    /*public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }*/


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
