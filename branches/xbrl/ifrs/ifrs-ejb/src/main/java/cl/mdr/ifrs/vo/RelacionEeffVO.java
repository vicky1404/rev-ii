package cl.mdr.ifrs.vo;



import java.io.Serializable;

import java.util.List;

import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.ifrs.ejb.entity.Columna;


public class RelacionEeffVO implements Serializable{

    @SuppressWarnings("compatibility:-398243565381817960")
    private static final long serialVersionUID = -6091613450896477255L;
        
    private String titulo;
    private int anchoTabla = 600;
    private List<Columna> columnaList;
    private List<List<Celda>> celdaList;
    
    
    public RelacionEeffVO() {
        super();
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setColumnaList(List<Columna> columnaList) {
        this.columnaList = columnaList;
    }

    public List<Columna> getColumnaList() {
        return columnaList;
    }

    public void setCeldaList(List<List<Celda>> celdaList) {
        this.celdaList = celdaList;
    }

    public List<List<Celda>> getCeldaList() {
        return celdaList;
    }
    
    public int sizeColumnaList(){
        
        if(columnaList!=null)
            return columnaList.size();
        
        return 0;
    }

    public void setAnchoTabla(int anchoTabla) {
        this.anchoTabla = anchoTabla;
    }

    public int getAnchoTabla() {
        return anchoTabla;
    }
}
