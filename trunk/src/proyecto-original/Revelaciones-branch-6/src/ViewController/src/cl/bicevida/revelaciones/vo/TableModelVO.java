package cl.bicevida.revelaciones.vo;


import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.Columna;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TableModelVO {
    private List<AgrupacionColumnaModelVO> nivel2List;
    private List<AgrupacionColumnaModelVO> nivel1List;
    private List<Columna> columnas;
    private Long nivelColumna;
    private List<Map<Long,Celda>> rows;
    
    public TableModelVO(){
        
    }

    public TableModelVO(List<Columna> columnas, List<Map<Long, Celda>> rows) {
        this.columnas = columnas;
        this.rows = rows;
    }


    public void setColumnas(List<Columna> columnas) {
        this.columnas = columnas;
    }

    public List<Columna> getColumnas() {
        if(columnas==null)
            columnas = new ArrayList<Columna>();
            
        return columnas;
    }

    public void setRows(List<Map<Long, Celda>> rows) {
        this.rows = rows;
    }

    public List<Map<Long, Celda>> getRows() {
        return rows;
    }

    public void setNivel2List(List<AgrupacionColumnaModelVO> nivel2List) {
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
    }

    public void setNivelColumna(Long nivelDeColumna) {
        this.nivelColumna = nivelDeColumna;
    }

    public Long getNivelColumna() {
        return nivelColumna;
    }
}
