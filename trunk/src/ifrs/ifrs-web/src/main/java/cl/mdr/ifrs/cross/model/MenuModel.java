package cl.mdr.ifrs.cross.model;

import java.io.Serializable;
import java.util.List;

import cl.mdr.ifrs.ejb.entity.Menu;

/**
 * Clase que representa un model para construir el menu de la aplicacion.
 * @author rodrigo.reyes@bicevida.cl
 * @since 02/01/2012
 */
public class MenuModel implements Serializable {
    private static final long serialVersionUID = 6224750768107133881L;
    
    private Menu menuParent;
    private List<Menu> menuChild;
    
    public MenuModel() {
        super();
    }

    public void setMenuParent(Menu menuParent) {
        this.menuParent = menuParent;
    }

    public Menu getMenuParent() {
        return menuParent;
    }

    public void setMenuChild(List<Menu> menuChild) {
        this.menuChild = menuChild;
    }

    public List<Menu> getMenuChild() {
        return menuChild;
    }
}
