package cl.bicevida.revelaciones.vo;


import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.Columna;
import cl.bicevida.revelaciones.ejb.entity.Grilla;
import cl.bicevida.revelaciones.ejb.entity.SubCelda;
import cl.bicevida.revelaciones.ejb.entity.SubColumna;
import cl.bicevida.revelaciones.ejb.entity.SubGrilla;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Clase no persistente que representa los campos de una grilla
 * calculados y en formato filas por columna
 */
public class GrillaVO implements Serializable {
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = -2599361993445052185L;
    
    private String titulo;
    private Grilla grilla;
    private transient List<Map<String, Object>> columns;    
    private transient List<Map<String, Object>> grupos;
    private transient List<Map<String, Object>> gruposResultado;
    private transient List<AgrupacionColumnaModelVO> nivel2List;
    private transient List<AgrupacionColumnaModelVO> nivel1List;
    
    private transient List<AgrupacionSubColumnaModelVO> nivel2DesagregadoList;
    private transient List<AgrupacionSubColumnaModelVO> nivel1DesagregadoList;

    
    private List<Columna> columnas;    
    private transient List<Map<Long,Celda>> rows;
    private transient Long nivel;    
    private transient List<Long> registros;
    
    
    private SubGrilla subGrilla;
    private List<SubColumna> subColumnas;    
    private transient List<Map<Long,SubCelda>> subRows;
    

    public GrillaVO() {
        super();
    }


    public void setGrilla(Grilla grilla) {
        this.grilla = grilla;
    }

    public Grilla getGrilla() {
        return grilla;
    }

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

    public void setRegistros(List<Long> registros) {
        this.registros = registros;
    }

    public List<Long> getRegistros() {
       
        registros = new ArrayList<Long>();
        registros.add(1L);
                  
            return registros;
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

    public void setColumnas(List<Columna> columnas) {
        this.columnas = columnas;
    }

    public List<Columna> getColumnas() {
        return columnas;
    }

    public void setNivel(Long nivel) {
        this.nivel = nivel;
    }

    public Long getNivel() {
        return nivel;
    }

    public void setRows(List<Map<Long, Celda>> rows) {
        this.rows = rows;
    }

    public List<Map<Long, Celda>> getRows() {
        return rows;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setSubGrilla(SubGrilla subGrilla) {
        this.subGrilla = subGrilla;
    }

    public SubGrilla getSubGrilla() {
        return subGrilla;
    }

    public void setSubColumnas(List<SubColumna> subColumnas) {
        this.subColumnas = subColumnas;
    }

    public List<SubColumna> getSubColumnas() {
        return subColumnas;
    }

    public void setSubRows(List<Map<Long, SubCelda>> subRows) {
        this.subRows = subRows;
    }

    public List<Map<Long, SubCelda>> getSubRows() {
        return subRows;
    }


    public void setNivel2DesagregadoList(List<AgrupacionSubColumnaModelVO> nivel2DesagregadoList) {
        this.nivel2DesagregadoList = nivel2DesagregadoList;
    }

    public List<AgrupacionSubColumnaModelVO> getNivel2DesagregadoList() {
        return nivel2DesagregadoList;
    }

    public void setNivel1DesagregadoList(List<AgrupacionSubColumnaModelVO> nivel1DesagregadoList) {
        this.nivel1DesagregadoList = nivel1DesagregadoList;
    }

    public List<AgrupacionSubColumnaModelVO> getNivel1DesagregadoList() {
        return nivel1DesagregadoList;
    }
}
