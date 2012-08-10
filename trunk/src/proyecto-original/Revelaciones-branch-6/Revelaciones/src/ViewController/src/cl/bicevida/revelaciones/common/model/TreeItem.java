package cl.bicevida.revelaciones.common.model;

import java.io.Serializable;

import java.util.List;

public class TreeItem implements Serializable {
    @SuppressWarnings("compatibility:5511077314584747336")
    private static final long serialVersionUID = 1340055417692770735L;
    
    private boolean parent;
    private transient Object object;
    private List<TreeItem> children;
    private boolean bloqueado;
    
    public TreeItem() {
        super();
    }   
    
    public TreeItem(boolean parent, Object object){
      super();
      this.parent = parent;      
      this.object = object;
    }
    
    public TreeItem(Object object){
      super();      
      this.object = object;
    }
    
    public TreeItem(Object object, boolean bloqueado){
      super();      
      this.object = object;
      this.bloqueado = bloqueado;
    }
    
    public void setChildren(List<TreeItem> children) {
        this.children = children;
    }
 
    public List<TreeItem> getChildren() {
        return children;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public void setParent(boolean parent) {
        this.parent = parent;
    }

    public boolean isParent() {
        return parent;
    }

    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }
}
