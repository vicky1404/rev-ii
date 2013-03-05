package cl.bicevida.revelaciones.common.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una grilla con seleccion multiple mediante checkbox
 * @param <T>
 */
public class CommonGridModel<T> implements Serializable{
    @SuppressWarnings("compatibility:3881129147947685689")
    private static final long serialVersionUID = 3794274026026173438L;
    
    private transient T entity;    
    private boolean selected;
    
    public CommonGridModel(){    
    }
    
    public CommonGridModel(T entity, boolean selected){
        this.entity = entity;
        this.selected = selected;
    }
    
    public void setEntity(T entity){
        this.entity = entity;
    }

    public T getEntity(){
        return entity;
    }
    
    public void setSelected(boolean selected){
        this.selected = selected;
    }

    public boolean isSelected(){
        return selected;
    }
    
}
