package cl.mdr.ifrs.cross.vo;

import java.util.List;

public class CampoModelVO {
    
    public static final short TIPO_COLUMNA = 1;
    public static final short TIPO_CELDA = 2;
    public static final short TIPO_DATO = 3;
    
    private List<EdicionVO> columnas;    
    private short tipo;
    
    public CampoModelVO() {
    }
    
    public CampoModelVO(List<EdicionVO> columnas,short tipo) {
        this.columnas = columnas;
        this.tipo = tipo;
    }
    

    public void setTipo(short tipo) {
        this.tipo = tipo;
    }

    public short getTipo() {
        return tipo;
    }

    public void setColumnas(List<EdicionVO> columnas) {
        this.columnas = columnas;
    }

    public List<EdicionVO> getColumnas() {
        return columnas;
    }
    
}