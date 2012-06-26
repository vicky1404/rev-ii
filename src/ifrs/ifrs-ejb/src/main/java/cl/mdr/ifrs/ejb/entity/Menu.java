package cl.mdr.ifrs.ejb.entity;


import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import cl.mdr.ifrs.ejb.common.Constantes;


@Entity
@NamedQueries( { 
    @NamedQuery(name = Menu.FIND_ALL, query = "select o from Menu o order by o.grupo asc , o.padre desc") 
})
@Table(name = Constantes.MENU)
public class Menu implements Serializable {    
    private static final long serialVersionUID = -880292692665795840L;
    
    public static final String FIND_ALL = "Menu.findAll";
    
    @Column(nullable = false)
    private Long estado;
    
    private Long grupo;
    
    @Id
    @Column(name = "ID_MENU", nullable = false)
    private Long idMenu;
    
    @Column(nullable = false, length = 512)
    private String nombre;
    
    @Column(name = "URL_MENU", length = 512)
    private String urlMenu;
    
    @Column(name = "ES_PADRE")
    private boolean padre;
    
    @OneToMany(mappedBy = "menu")
    private List<MenuGrupo> menuGrupoList;
    
  //bi-directional many-to-many association to Grupo
    @ManyToMany
	@JoinTable(
		name="IFRS_MENU_GRUPO"
		, joinColumns={
			@JoinColumn(name="ID_MENU")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_GRUPO_ACCESO")
			}
		)
	private List<Grupo> grupos;

    public List<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}

	public Menu() {
    }

    public Menu(Long estado, Long grupo, Long idMenu, String nombre, String urlMenu) {
        this.estado = estado;
        this.grupo = grupo;
        this.idMenu = idMenu;
        this.nombre = nombre;
        this.urlMenu = urlMenu;
    }


    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }

    public Long getGrupo() {
        return grupo;
    }

    public void setGrupo(Long grupo) {
        this.grupo = grupo;
    }

    public Long getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(Long idMenu) {
        this.idMenu = idMenu;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrlMenu() {
        return urlMenu;
    }

    public void setUrlMenu(String urlMenu) {
        this.urlMenu = urlMenu;
    }

    public List<MenuGrupo> getMenuGrupoList() {
        return menuGrupoList;
    }

    public void setMenuGrupoList(List<MenuGrupo> menuGrupoList) {
        this.menuGrupoList = menuGrupoList;
    }

    public MenuGrupo addMenuGrupo(MenuGrupo menuGrupo) {
        getMenuGrupoList().add(menuGrupo);
        menuGrupo.setMenu(this);
        return menuGrupo;
    }

    public MenuGrupo removeMenuGrupo(MenuGrupo menuGrupo) {
        getMenuGrupoList().remove(menuGrupo);
        menuGrupo.setMenu(null);
        return menuGrupo;
    }
    
    public void setPadre(boolean padre) {
        this.padre = padre;
    }

    public boolean isPadre() {
        return padre;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("estado=");
        buffer.append(getEstado());
        buffer.append(',');
        buffer.append("grupo=");
        buffer.append(getGrupo());
        buffer.append(',');
        buffer.append("idMenu=");
        buffer.append(getIdMenu());
        buffer.append(',');
        buffer.append("nombre=");
        buffer.append(getNombre());
        buffer.append(',');
        buffer.append("urlMenu=");
        buffer.append(getUrlMenu());
        buffer.append(']');
        return buffer.toString();
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Menu)) {
            return false;
        }
        final Menu other = (Menu)object;
        if (!(idMenu == null ? other.idMenu == null : idMenu.equals(other.idMenu))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = 1;
        result = PRIME * result + ((idMenu == null) ? 0 : idMenu.hashCode());
        return result;
    }

    
}
