package cl.mdr.ifrs.ejb.entity;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import cl.mdr.ifrs.ejb.common.Constantes;
import cl.mdr.ifrs.ejb.entity.pk.MenuGrupoPK;


@Entity
@NamedQueries({
        @NamedQuery(name = MenuGrupo.FIND_ALL, query = "select o from MenuGrupo o"),        
        @NamedQuery(name = MenuGrupo.FIND_BY_GRUPO, query = "select o from MenuGrupo o where o.grupo =:grupo"),
        @NamedQuery(name = MenuGrupo.DELETE_BY_GRUPO, query = "delete from MenuGrupo o where o.grupo =:grupo")
})
@Table(name = Constantes.MENU_GRUPO)
@IdClass(MenuGrupoPK.class)
public class MenuGrupo implements Serializable {    
    private static final long serialVersionUID = 1355801743738463444L;
        
    public static final String FIND_ALL = "MenuGrupo.findAll";
    public static final String FIND_BY_GRUPO = "MenuGrupo.findByGrupo";
    public static final String DELETE_BY_GRUPO = "MenuGrupo.deleteByGrupo";

    @Id
    @Column(name="ID_GRUPO_ACCESO", nullable = false, insertable = false, updatable = false)
    private String idGrupoAcceso;
    
    @Id
    @Column(name="ID_MENU", nullable = false, insertable = false, updatable = false)
    private Long idMenu;
    
    @ManyToOne
    @JoinColumn(name = "ID_MENU")
    private Menu menu;
    
    @ManyToOne
    @JoinColumn(name = "ID_GRUPO_ACCESO")
    private Grupo grupo;

    public MenuGrupo() {
    }
    
    public String getIdGrupoAcceso() {
        return idGrupoAcceso;
    }

    public void setIdGrupoAcceso(String idGrupoAcceso) {
        this.idGrupoAcceso = idGrupoAcceso;
    }

    public Long getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(Long idMenu) {
        this.idMenu = idMenu;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
        if (menu != null) {
            this.idMenu = menu.getIdMenu();
        }
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Grupo getGrupo() {
        return grupo;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idGrupoAcceso=");
        buffer.append(getIdGrupoAcceso());
        buffer.append(',');
        buffer.append(']');
        return buffer.toString();
    }
}
