package cl.bicevida.revelaciones.common.model;

import cl.bicevida.revelaciones.ejb.entity.Menu;

import java.io.Serializable;

import java.util.List;

/**
 * Clase que representa un model para construir el menu de la aplicacion.
 * @author rodrigo.reyes@bicevida.cl
 * @since 02/01/2012
 */
public class MenuModel implements Serializable {
    @SuppressWarnings("compatibility:6582069929985144818")
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
