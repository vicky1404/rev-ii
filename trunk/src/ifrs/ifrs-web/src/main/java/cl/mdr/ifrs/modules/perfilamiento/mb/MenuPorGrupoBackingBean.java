package cl.mdr.ifrs.modules.perfilamiento.mb;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.log4j.Logger;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.model.CommonGridModel;
import cl.mdr.ifrs.ejb.entity.Grupo;
import cl.mdr.ifrs.ejb.entity.Menu;
import cl.mdr.ifrs.ejb.entity.MenuGrupo;

/**
* @author rreyes
* @link http://cl.linkedin.com/in/rreyesc
*/
@ManagedBean
@ViewScoped
public class MenuPorGrupoBackingBean extends AbstractBackingBean implements Serializable {
	private transient Logger logger = Logger.getLogger(this.getClass().getName());
	private static final long serialVersionUID = -6662762316008127812L;
	
	private Grupo grupoSelected;
    private List<Menu> menuList;
    private transient List<CommonGridModel<Menu>> grillaMenuList;
    private boolean renderTablaMenu;
    private String idGrupoSelected;
    	
	public MenuPorGrupoBackingBean() {
		super();		
	}
	
	/**
	 * @return
	 */
	public void buscarMenuPorGrupoAction(ActionEvent event) {
        try {        	
            this.setGrillaMenuList(this.getMenuByGrupoList(super.getFacadeService().getSeguridadService().findMenuAccesoByGrupo(new Grupo(this.getIdGrupoSelected())),
                                                           super.getFacadeService().getSeguridadService().findMenuFindAll()));
            this.setRenderTablaMenu(Boolean.TRUE);
        } catch (Exception e) {
            super.addErrorMessage("Error", "Error al obtener el listado de Menus");
            logger.error(e.getCause(), e);
        }        
    }
	
	public void grupoChangeListener(){
		logger.info("grupo selected "+this.getGrupoSelected().getIdGrupoAcceso());
	}
	
	/**
     * metodo que genera los datos de grilla menu, marcando seleccionados los que ya existen como permisos en la base de datos
     * @param menuAccesos
     * @param menuAll
     * @return List<CommonGridModel<Menu>>
     */
    public List<CommonGridModel<Menu>> getMenuByGrupoList(List<MenuGrupo> menuGrupoList, List<Menu> menuAll) {
        grillaMenuList = new ArrayList<CommonGridModel<Menu>>();
        CommonGridModel<Menu> grillaMenu;
        for (Menu menu : menuAll) {
            grillaMenu = new CommonGridModel<Menu>();
            grillaMenu.setEntity(menu);            
            for (MenuGrupo menuGrupo : menuGrupoList) {
                if (EqualsBuilder.reflectionEquals(menu.getIdMenu(), menuGrupo.getMenu().getIdMenu())) {
                    grillaMenu.setSelected(Boolean.TRUE);
                    break;
                }
            }
            grillaMenuList.add(grillaMenu);
        }
        return grillaMenuList;
    }
    
    /**
     * procesa una lista con los menus asignados al grupo y los persiste en la base de datos
     * @return
     */
    public void guardarMenuGrupoAction(ActionEvent event) {       
        List<Menu> menus = new ArrayList<Menu>();                  
        try {
        	final Grupo grupo = super.getFacadeService().getSeguridadService().findGrupoById(new Grupo(this.getIdGrupoSelected()));
            for (CommonGridModel<Menu> grillaMenu : getGrillaMenuList()) {
                if (grillaMenu.isSelected()) {
                	menus.add(grillaMenu.getEntity());                    
                }
            }
            grupo.setMenus(menus);            
            super.getFacadeService().getSeguridadService().mergeGrupo(grupo);
            super.addInfoMessage("", MessageFormat.format("Se actualizó correctamente la lista de accesos a los menus para el grupo  {0} ", grupo.getNombre() )  );
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            super.addErrorMessage("Error", "Error al agregar accesos al menu");
        }       
    }
    

	public Grupo getGrupoSelected() {
		return grupoSelected;
	}

	public void setGrupoSelected(Grupo grupoSelected) {
		this.grupoSelected = grupoSelected;
	}

	public List<Menu> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}

	public List<CommonGridModel<Menu>> getGrillaMenuList() {
		return grillaMenuList;
	}

	public void setGrillaMenuList(List<CommonGridModel<Menu>> grillaMenuList) {
		this.grillaMenuList = grillaMenuList;
	}

	public boolean isRenderTablaMenu() {
		return renderTablaMenu;
	}

	public void setRenderTablaMenu(boolean renderTablaMenu) {
		this.renderTablaMenu = renderTablaMenu;
	}

	public String getIdGrupoSelected() {
		return idGrupoSelected;
	}

	public void setIdGrupoSelected(String idGrupoSelected) {
		this.idGrupoSelected = idGrupoSelected;
	}
	
	

}
