/**
 * 
 */
package cl.mdr.ifrs.cross.mb;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.index;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static ch.lambdaj.Lambda.sort;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.component.UIComponent;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ValueChangeEvent;

import org.apache.log4j.Logger;
import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.component.tree.Tree;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import cl.mdr.ifrs.cross.model.MenuModel;
import cl.mdr.ifrs.cross.model.TreeItem;
import cl.mdr.ifrs.cross.util.PropertyManager;
import cl.mdr.ifrs.cross.util.UtilBean;
import cl.mdr.ifrs.ejb.common.VigenciaEnum;
import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.Catalogo;
import cl.mdr.ifrs.ejb.entity.Empresa;
import cl.mdr.ifrs.ejb.entity.Grupo;
import cl.mdr.ifrs.ejb.entity.Menu;
import cl.mdr.ifrs.ejb.entity.TipoCuadro;

/**
 * @author rreyes
 * @link http://cl.linkedin.com/in/rreyesc
 *
 */
public class MenuBackingBean extends AbstractBackingBean implements Serializable {
	
	public static final String BEAN_NAME = "menuBackingBean";
	
	private final Logger log = Logger.getLogger(this.getClass().getName());
	private static final long serialVersionUID = 8077481642975216680L;
	private static final String PROCESO_VIEW_ID = "/pages/proceso/proceso.jsf";
	private static final String BLANK_VIEW_ID = "/pages/blank.jsf";
	private static final String SEGURIDAD_VIEW_ID = "/pages/seguridad/seguridad.jsf";
	
	private FiltroBackingBean filtroBackingBean;
	
	private UIComponent tabActivo;
	private AccordionPanel menuAcordionPanel;
	private String activeTabIndex;
	
	private TreeNode root;
	
	private List<Menu> menuList;
    private List<MenuModel> menuModelList;    
	
	//tree catalogo
    private Long idRut;
    private Empresa empresa;
    private Tree treeCatalogo;
    private List<Catalogo> catalogoList;    
    private boolean sistemaBloqueado;
    private TreeItem treeItem;
    private Map<Long, Catalogo> catalogoMap; 
    private Catalogo catalogoSelected;
    private boolean renderSelectorEmpresa;
    private boolean redireccionado;
    private boolean valid = true;
    private String breadcrumb;
          
    public String getBreadcrumb() {
		return breadcrumb;
	}

	public void setBreadcrumb(String breadcrumb) {
		this.breadcrumb = breadcrumb;
	}


	private org.primefaces.model.MenuModel model; 
                  		
	public MenuBackingBean() {
		super();				
		root = new DefaultTreeNode("Proceso", null);  	
		
	}
	
	@PostConstruct
	public void load(){
		
		
	try {
		valid = isValidSoft();
		if(!valid){
			getExternalContext().redirect(super.getExternalContext().getRequestContextPath().concat(SEGURIDAD_VIEW_ID));
		}
	} catch (Exception e) {
		log.error(e.getMessage(),e);
	}
		
	}
	
	
	public void menuBackingBeanParaUnaEmpresa(ComponentSystemEvent event){
		
		if(!valid)
			return;
		
		List<Empresa> empresaList = super.getFacadeService().getEmpresaService().findAll();
		
		if (empresaList.size() == 1){
			for (Empresa empresa : empresaList){
				try{
					this.setEmpresa(empresa);
					getFiltroBackingBean().setEmpresa(empresa);
					if(root.getChildCount() > 0)
						root.getChildren().clear();
					buildCuadroTreeMenu(this.getRoot());
					buildAccordionPanelMenu();
					buildCatalogoMap();
					this.setRenderSelectorEmpresa(Boolean.FALSE);
					if (!isRedireccionado()){
						this.setRedireccionado(Boolean.TRUE);
					getExternalContext().redirect(super.getExternalContext().getRequestContextPath().concat(BLANK_VIEW_ID));
					}
					return;
									
				}catch(Exception e){
					e.printStackTrace();
				}
				
				
			}
		} else if (!this.isRenderSelectorEmpresa()) {
			
			getFiltroBackingBean().init();
			init();
			this.setRenderSelectorEmpresa(Boolean.TRUE);
		}
		
	}
	
	/**
	 * 
	
	@throws IOException 
	 * @PostConstruct
	void init(){	
		try{
			this.buildCuadroTreeMenu(this.getRoot());
			this.buildAccordionPanelMenu();
			this.buildCatalogoMap();
		}catch (Exception e) {
			addErrorMessage("Error", "Se ha producido un error al inicializar el Menú");
			log.error(e.getCause(), e);
		}
	}*/

	
	public void empresaChangeValue(ValueChangeEvent event) throws IOException{
		
		init();
		if(!valid)
			return;
		
		if(event.getNewValue() == null){
			getFiltroBackingBean().init();
			addWarnMessage(PropertyManager.getInstance().getMessage("general_seleccionar_empresa"));
			getExternalContext().redirect(super.getExternalContext().getRequestContextPath().concat(BLANK_VIEW_ID));
			return;
		}
		
		try{
			idRut = (Long)event.getNewValue();
			empresa = getFacadeService().getEmpresaService().findById(idRut);
			getFiltroBackingBean().setEmpresa(empresa);
			buildCuadroTreeMenu(this.getRoot());
			buildAccordionPanelMenu();
			buildCatalogoMap();
			getExternalContext().redirect(super.getExternalContext().getRequestContextPath().concat(BLANK_VIEW_ID));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * @param event
	 */
	public void onTabChange(TabChangeEvent event) {  
		this.setTabActivo(event.getTab());   
		this.setActiveTabIndex("1");
    }
	
	/**
	 * 
	 */
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
        ordenarMenuByGrupo(menuList);
        for (Menu m : menuList) {
            if (m.getUrlMenu().equals("#") && m.getEstado().equals(1L) && m.isPadre()) {
                menuParentList.add(m);
            }
            if (!m.getUrlMenu().equals("#") && m.getEstado().equals(1L) && !m.isPadre()) {
                menuChildList.add(m);
            }
        }
                
        ordenarMenuByOrden(menuParentList);
        for(Menu menuParent : menuParentList) {
            menuModel = new MenuModel();
            menuChildModelList = new ArrayList<Menu>();
            menuModel.setMenuParent(menuParent);
            for (Menu menuChild : menuChildList) {
                if (menuParent.getGrupo().compareTo(menuChild.getGrupo()) == 0) {
                    menuChildModelList.add(menuChild);
                }
            }
            ordenarMenuByOrden(menuChildModelList);
            menuModel.setMenuChild(menuChildModelList);
            menuModelList.add(menuModel);
        }
        this.setMenuModelList(menuModelList);
	}
	    
    /**
     * @return
     */
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
            	log.error(e.getCause(), e);
            }
        }
        return menuList;
    }
    
    /**
     * @return
     */
    public String navigation(){    
    	final String action = super.getExternalContext().getRequestParameterMap().get("urlAction");
    	final String tabNumber = super.getExternalContext().getRequestParameterMap().get("tabNumber");
    	final String breadcrumb = super.getExternalContext().getRequestParameterMap().get("breadcrumb");
    	this.setBreadcrumb(null);
    	this.setBreadcrumb(breadcrumb);
    	this.setActiveTabIndex(tabNumber);
    	log.info("accion "+action);
    	log.info("tabNumber "+tabNumber);
    	log.info("breadcrumb "+breadcrumb);
    	return action;
    }
    
    /**
     * @return
     */
    @Deprecated
    public String cargarNotaAction(){    	
    	log.info("cargando cuadro ");
    	log.info("idCatalogo "+this.getCatalogoSelected().getNombre());    	
    	return "/pages/cuadro/cuadroBackingBean.jsf";
    }
    
    /**
     * @param event
     * @throws Exception
     */
    public void onSelectNodeMenuCuadro(NodeSelectEvent event) throws Exception {
    	this.setActiveTabIndex("0");
    	event.getTreeNode().getParent().setExpanded(Boolean.TRUE);
    	event.getTreeNode().getParent().setSelected(Boolean.TRUE);
    	this.setCatalogoSelected(this.getCatalogoMap().get(Util.getLong(event.getTreeNode().toString(), null)));
    	if(super.getPrincipal() == null){
    		super.addFatalMessage("Debido a su inactividad, la Sesión ha Caducado. por favor ingrese nuevamente.");
    	}
    	if(this.getCatalogoSelected() != null){
    		this.getFiltroBackingBean().setCatalogo(this.getCatalogoSelected());    	    
    		super.getExternalContext().redirect(super.getExternalContext().getRequestContextPath().concat(PROCESO_VIEW_ID));
    	}
    } 
    
    
    /**
     * @param menuList
     */
    private static void ordenarMenuByGrupo(List<Menu> menuList) {
        Collections.sort(menuList, new Comparator<Menu>() {
            public int compare(Menu m1, Menu m2) {
                return m1.getGrupo().compareTo(m2.getGrupo());
            }
        });
    }
    
    /**
     * @param menuList
     */
    private static void ordenarMenuByNombre(List<Menu> menuList) {
        Collections.sort(menuList, new Comparator<Menu>() {
            public int compare(Menu m1, Menu m2) {
                return m1.getNombre().compareTo(m2.getNombre());
            }
        });
    }
    
    private static void ordenarMenuByOrden(List<Menu> menuList) {
        Collections.sort(menuList, new Comparator<Menu>() {
            public int compare(Menu m1, Menu m2) {
                return m1.getOrden().compareTo(m2.getOrden());
            }
        });
    }
    
    /**
     * @param catalogoList
     * @return
     */
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
    
    /**
     * @return
     * */
    public List<Catalogo> getCatalogoList() {
        if(catalogoList==null){
            try{
                catalogoList = super.getFacadeService().getCatalogoService().findCatalogoByFiltro(empresa.getIdRut(), getNombreUsuario(), null, null, VigenciaEnum.VIGENTE.getKey());
            } catch (Exception e) {
            	log.error(e.getMessage());
                super.addErrorMessage(null, PropertyManager.getInstance().getMessage("general_mensaje_error_cargar_menu"));
            }
        }
        return catalogoList;
    }
    
    public void setSistemaBloqueado(boolean sistemaBloqueado) {
        this.sistemaBloqueado = sistemaBloqueado;
    }

    /**
     * @return
     * @throws Exception
     */
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

	public FiltroBackingBean getFiltroBackingBean() {
		if(filtroBackingBean == null){
			filtroBackingBean = UtilBean.findBean(FiltroBackingBean.FILTRO_BEAN_NAME);
		}
		return filtroBackingBean;
	}

	public void setFiltroBackingBean(FiltroBackingBean filtroBackingBean) {
		this.filtroBackingBean = filtroBackingBean;
	}

	public Tree getTreeCatalogo() {
		return treeCatalogo;
	}

	public void setTreeCatalogo(Tree treeCatalogo) {
		this.treeCatalogo = treeCatalogo;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Long getIdRut() {
		return idRut;
	}

	public void setIdRut(Long idRut) {
		this.idRut = idRut;
	}
	
	private void init(){
		
		if(root !=null)
			root.getChildren().clear();
		
		tabActivo = null;
		menuAcordionPanel = null;
		activeTabIndex = null;		
		menuList = null;
	    menuModelList = null;
	    idRut = null;
	    empresa = null;
	    treeCatalogo = null;
	    catalogoList = null;    
	    sistemaBloqueado = false;
	    treeItem = null;
	    catalogoMap = null; 
	    catalogoSelected = null;
	}


	public boolean isRenderSelectorEmpresa() {
		return renderSelectorEmpresa;
	}


	public void setRenderSelectorEmpresa(boolean renderSelectorEmpresa) {
		this.renderSelectorEmpresa = renderSelectorEmpresa;
	}


	public boolean isRedireccionado() {
		return redireccionado;
	}


	public void setRedireccionado(boolean redireccionado) {
		this.redireccionado = redireccionado;
	}

}
