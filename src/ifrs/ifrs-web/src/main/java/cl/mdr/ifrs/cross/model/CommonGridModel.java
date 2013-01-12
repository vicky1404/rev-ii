package cl.mdr.ifrs.cross.model;

import java.io.Serializable;

/**
 * Clase que representa una grilla con seleccion multiple mediante checkbox
 * @param <T>
 */
public class CommonGridModel<T> implements Serializable{
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
