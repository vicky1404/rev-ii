/**
 * 
 */
package cl.mdr.ifrs.cross.mb;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static ch.lambdaj.Lambda.index;
import static org.hamcrest.Matchers.equalTo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;

import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import cl.mdr.ifrs.cross.model.MenuModel;
import cl.mdr.ifrs.cross.model.TreeItem;
import cl.mdr.ifrs.cross.util.PropertyManager;
import cl.mdr.ifrs.ejb.common.VigenciaEnum;
import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.Catalogo;
import cl.mdr.ifrs.ejb.entity.Grupo;
import cl.mdr.ifrs.ejb.entity.Menu;
import cl.mdr.ifrs.ejb.entity.TipoCuadro;

/**
 * @author rreyes
 * @link http://cl.linkedin.com/in/rreyesc
 *
 */
public class MenuBackingBean extends AbstractBackingBean implements Serializable {
	private final Logger log = Logger.getLogger(this.getClass().getName());
	private static final long serialVersionUID = 8077481642975216680L;
	
	
	private UIComponent tabActivo;
	private AccordionPanel menuAcordionPanel;
	private String activeTabIndex;
	
	private TreeNode root;
	
	private List<Menu> menuList;
    private List<MenuModel> menuModelList;    
	
	//tree catalogo
    private List<Catalogo> catalogoList;
    
    private boolean sistemaBloqueado;
    private TreeItem treeItem;
    private Map<Long, Catalogo> catalogoMap; 
    private Catalogo catalogoSelected;
    
    private org.primefaces.model.MenuModel model;  
		
	public MenuBackingBean() {
		super();		
		super.getFacesContext().getViewRoot().setLocale(new Locale("es"));
		root = new DefaultTreeNode("Proceso", null);  					 
	}
	
	@PostConstruct
	void init(){	
		try{
			this.buildCuadroTreeMenu(this.getRoot());
			this.buildAccordionPanelMenu();
			this.buildCatalogoMap();
		}catch (Exception e) {
			addErrorMessage("Error", "Se ha producido un error al inicializar el Menú");
			log.severe(""+e);
		}
	}
	
	public void onTabChange(TabChangeEvent event) {  
		this.setTabActivo(event.getTab());   
		this.setActiveTabIndex("1");
    }
	
	public void buildCatalogoMap(){
		final Map<Long, Catalogo> catalogoMap = index(this.getCatalogoList(), on(Catalogo.class).getIdCatalogo());
		this.setCatalogoMap(catalogoMap);
	}
	
	private void buildCuadroTreeMenu(TreeNode root) throws Exception{		 
        boolean bloqueado = this.isSistemaBloqueado();
        for(TipoCuadro tipoCuadro : this.getTiposCuadroFromCatalogo(this.getCatalogoList())){            
            TreeNode node = new DefaultTreeNode(tipoCuadro.getTitulo(), root);              
            for(Catalogo catalogo : this.getCatalogoList()){
                if(catalogo.getTipoCuadro().equals(tipoCuadro)){                    
                	//TreeNode nodeChild =  new DefaultTreeNode(new TreeItem(catalogo, bloqueado), node);
                	new DefaultTreeNode(catalogo.getIdCatalogo(), node);
                }
            }            
        }                
	}
	
	/**
	 * Construye el model de menu acordeon para navegacion
	 */
	private void buildAccordionPanelMenu(){
		final List<Menu> menuList = this.getMenuList();
        List<Menu> menuParentList = new ArrayList<Menu>();
        List<Menu> menuChildList = new ArrayList<Menu>();
        MenuModel menuModel = null;
        List<Menu> menuChildModelList = null;
        List<MenuModel> menuModelList = new ArrayList<MenuModel>();
        ordenarMenu(menuList);
        for (Menu m : menuList) {
            if (m.getUrlMenu().equals("#") && m.getEstado().equals(1L) && m.isPadre()) {
                menuParentList.add(m);
            }
            if (!m.getUrlMenu().equals("#") && m.getEstado().equals(1L) && !m.isPadre()) {
                menuChildList.add(m);
            }
        }

        for(Menu menuParent : menuParentList) {
            menuModel = new MenuModel();
            menuChildModelList = new ArrayList<Menu>();
            menuModel.setMenuParent(menuParent);
            for (Menu menuChild : menuChildList) {
                if (menuParent.getGrupo().compareTo(menuChild.getGrupo()) == 0) {
                    menuChildModelList.add(menuChild);
                }
            }
            menuModel.setMenuChild(menuChildModelList);
            menuModelList.add(menuModel);
        }
        this.setMenuModelList(menuModelList);
	}
	    
    public List<Menu> getMenuList() {
        if (menuList == null) {
            final List<Grupo> grupoList;
            Set<Menu> menus = new LinkedHashSet<Menu>();
            menuList = new ArrayList<Menu>();
            try {                
            	grupoList = super.getFacadeService().getSeguridadService().findUsuarioByUserName(super.getNombreUsuario()).getGrupos();
                for (Grupo grupo : grupoList) {
                    for(final Menu menu1 : super.getFacadeService().getSeguridadService().findGrupoById(grupo).getMenus())  {
                        menus.add(menu1);
                    }
                }
                for(Menu menu : menus) {
                    menuList.add(menu);
                }
            } catch (Exception e) {
                log.severe(""+e);
            }
        }
        return menuList;
    }
    
    public String navigation(){    
    	final String action = super.getExternalContext().getRequestParameterMap().get("urlAction");
    	final String tabNumber = super.getExternalContext().getRequestParameterMap().get("tabNumber");
    	this.setActiveTabIndex(tabNumber);
    	log.info("accion "+action);
    	log.info("tabNumber "+tabNumber);
    	return action;
    }
    
    public String cargarNotaAction(){    	
    	log.info("cargando cuadro ");
    	log.info("idCatalogo "+this.getCatalogoSelected().getNombre());
    	return "cuadro";
    }
    
    public void onSelectNodeMenuCuadro(NodeSelectEvent event) {
    	this.setActiveTabIndex("0");
    	this.setCatalogoSelected(this.getCatalogoMap().get(Util.getLong(event.getTreeNode().toString(), null)));
    	cargarNotaAction();
    	addInfoMessage("", "nodo : "+this.getCatalogoSelected().getNombre() + " "+this.getCatalogoSelected().getTitulo() );
    } 
    
    
    private static void ordenarMenu(List<Menu> menuList) {
        Collections.sort(menuList, new Comparator<Menu>() {
            public int compare(Menu m1, Menu m2) {
                return m1.getGrupo().compareTo(m2.getGrupo());
            }
        });
    }
    
    private List<TipoCuadro> getTiposCuadroFromCatalogo(final List<Catalogo> catalogoList){
        Set<TipoCuadro> tipoCuadroSet = new LinkedHashSet<TipoCuadro>();
        List<TipoCuadro> tipoCuadroList = new ArrayList<TipoCuadro>();
        for(Catalogo catalogo : catalogoList){
            tipoCuadroSet.add(catalogo.getTipoCuadro());
        }
        for(TipoCuadro tipoCuadro : tipoCuadroSet){
            tipoCuadroList.add(tipoCuadro);
        }
        return tipoCuadroList;
    }
    
    public List<Catalogo> getCatalogoList() {
        if(catalogoList==null){
            try{
                catalogoList = super.getFacadeService().getCatalogoService().findCatalogoByFiltro(super.getNombreUsuario(), null, null, VigenciaEnum.VIGENTE.getKey());
            } catch (Exception e) {
                super.addErrorMessage(null, PropertyManager.getInstance().getMessage("general_mensaje_error_cargar_menu"));
            }
        }
        return catalogoList;
    }
    
    public void setSistemaBloqueado(boolean sistemaBloqueado) {
        this.sistemaBloqueado = sistemaBloqueado;
    }

    public boolean isSistemaBloqueado() throws Exception {
        sistemaBloqueado = Boolean.FALSE;
        final List<Grupo> grupoList = select(this.getFacadeService().getSeguridadService().findUsuarioByUserName(super.getNombreUsuario()).getGrupos(), having(on(Grupo.class).getAccesoBloqueado(), equalTo(1L)));            
        if(grupoList != null && grupoList.size() > 0){
            sistemaBloqueado = Boolean.TRUE;
        }
        return sistemaBloqueado;
    }
	
	
	public UIComponent getTabActivo() {
		return tabActivo;
	}

	public void setTabActivo(UIComponent tabActivo) {
		this.tabActivo = tabActivo;
	}


	public AccordionPanel getMenuAcordionPanel() {
		return menuAcordionPanel;
	}


	public void setMenuAcordionPanel(AccordionPanel menuAcordionPanel) {
		this.menuAcordionPanel = menuAcordionPanel;
	}


	public String getActiveTabIndex() {
		return activeTabIndex;
	}


	public void setActiveTabIndex(String activeTabIndex) {
		this.activeTabIndex = activeTabIndex;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}

	public void setCatalogoList(List<Catalogo> catalogoList) {
		this.catalogoList = catalogoList;
	}

	public List<MenuModel> getMenuModelList() {
		return menuModelList;
	}

	public void setMenuModelList(List<MenuModel> menuModelList) {
		this.menuModelList = menuModelList;
	}

	public TreeItem getTreeItem() {
		return treeItem;
	}

	public void setTreeItem(TreeItem treeItem) {
		this.treeItem = treeItem;
	}

	public org.primefaces.model.MenuModel getModel() {
		return model;
	}

	public void setModel(org.primefaces.model.MenuModel model) {
		this.model = model;
	}

	public Map<Long, Catalogo> getCatalogoMap() {
		return catalogoMap;
	}

	public void setCatalogoMap(Map<Long, Catalogo> catalogoMap) {
		this.catalogoMap = catalogoMap;
	}

	public Catalogo getCatalogoSelected() {
		return catalogoSelected;
	}

	public void setCatalogoSelected(Catalogo catalogoSelected) {
		this.catalogoSelected = catalogoSelected;
	}

	 

}
