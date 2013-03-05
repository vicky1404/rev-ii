package cl.bicevida.revelaciones.common.mb;


import cl.bicevida.revelaciones.common.model.MenuModel;
import cl.bicevida.revelaciones.common.model.TreeItem;
import cl.bicevida.revelaciones.common.util.PropertyManager;
import cl.bicevida.revelaciones.ejb.common.TipoEstructuraEnum;
import cl.bicevida.revelaciones.ejb.common.VigenciaEnum;
import cl.bicevida.revelaciones.ejb.entity.Catalogo;
import cl.bicevida.revelaciones.ejb.entity.Estructura;
import cl.bicevida.revelaciones.ejb.entity.Grilla;
import cl.bicevida.revelaciones.ejb.entity.Grupo;
import cl.bicevida.revelaciones.ejb.entity.Menu;
import cl.bicevida.revelaciones.ejb.entity.MenuGrupo;
import cl.bicevida.revelaciones.ejb.entity.SubGrilla;
import cl.bicevida.revelaciones.ejb.entity.TipoCuadro;
import cl.bicevida.revelaciones.ejb.entity.Version;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.model.ChildPropertyTreeModel;
import org.apache.myfaces.trinidad.model.TreeModel;


public class MenuBackingBean extends SoporteBackingBean implements Serializable{
    private transient Logger logger = Logger.getLogger(MenuBackingBean.class);
    
    @SuppressWarnings("compatibility:8639537057521899220")
    private static final long serialVersionUID = -3886732428225205210L;
    
    private List<Menu> menuList;
    private List<MenuModel> menuModelList;    
        
    //tree catalogo menu
    private List<Catalogo> catalogoList;
    private transient ArrayList<TreeItem> catalogoRoot;
    private transient TreeModel catalogoModel = null;
    private transient Object catalogoInstance = null;
    private List<Catalogo> catalogoSubCuadrosList;
    
    /**
     * Construye la structure List<MenuModel> para despliegue
     * de menu de navegación perfilado por usuario.
     * @return
     */
    public List<MenuModel> getMenuModelList() {
        final List<Menu> menuList = this.getMenuList();
        List<Menu> menuParentList = new ArrayList<Menu>();
        List<Menu> menuChildList = new ArrayList<Menu>();
        MenuModel menuModel = null;
        List<Menu> menuChildModelList = null;
        List<MenuModel> menuModelList = new ArrayList<MenuModel>();
        this.ordenarMenu(menuList);
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

        return menuModelList;
    }
    
    public List<Menu> getMenuList() {
        if (menuList == null) {
            final List<Grupo> grupoList;
            Set<Menu> menus = new LinkedHashSet<Menu>();
            menuList = new ArrayList<Menu>();
            try {
                grupoList = super.getFacade().getSeguridadService().findGruposByUsuario(super.getNombreUsuario());
                for (Grupo grupo : grupoList) {
                    for(MenuGrupo menuGrupo : grupo.getMenuGrupoList()) {
                        menus.add(menuGrupo.getMenu());
                    }
                }
                for(Menu menu : menus) {
                    menuList.add(menu);
                }
            } catch (Exception e) {
                logger.error(e.getCause(), e);
            }
        }
        return menuList;
    }
    
    private static void ordenarMenu(List<Menu> menuList) {
        Collections.sort(menuList, new Comparator<Menu>() {
            public int compare(Menu m1, Menu m2) {
                return m1.getGrupo().compareTo(m2.getGrupo());
            }
        });
    }
    
    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }
    
    public void setMenuModelList(List<MenuModel> menuModelList) {
        this.menuModelList = menuModelList;
    }

    /**
     * genera una structure del tipo TreeModel
     * para menu de catalogo por tipo de cuadro
     */
    public void getCatalogoTreeModel() throws Exception {        
        
        catalogoRoot = new ArrayList<TreeItem>();
        ArrayList<TreeItem> catalogoChildren = null;
        ArrayList<TreeItem> catalogoSubCuadro = null;
        TreeItem nodo = null;
        boolean contieneSubCuadro = Boolean.FALSE;
        boolean bloqueado = super.isSistemaBloqueado();
        
        for(TipoCuadro tipoCuadro : this.getTiposCuadroFromCatalogo(this.getCatalogoList())){
            nodo = new TreeItem();  
            nodo.setParent(Boolean.TRUE);
            nodo.setObject(tipoCuadro);
            
            catalogoRoot.add(nodo);
            catalogoChildren = new ArrayList<TreeItem>();
            
            for(Catalogo catalogo : this.getCatalogoList()){
                if(catalogo.getTipoCuadro().equals(tipoCuadro)){
                    List<Version> versionConSubCuadro = this.contieneSubCuadro(catalogo);                    
                    
                    if (versionConSubCuadro != null && versionConSubCuadro.size() > 0){
                            contieneSubCuadro = Boolean.TRUE;
                    } else {
                            contieneSubCuadro = Boolean.FALSE;
                        }
                    
                    TreeItem children1 = new TreeItem(catalogo, bloqueado, contieneSubCuadro, catalogo.getNombre(), catalogo.getTitulo());
                    catalogoSubCuadro = new ArrayList<TreeItem>();
                    for (Version version : versionConSubCuadro){
                        for (Estructura estructura : version.getEstructuraList()){
                            for (Grilla grilla : estructura.getGrillaList()){
                                for (SubGrilla subGrilla : grilla.getSubGrillaList()){
                                    if (super.existeGrupoEnLista(subGrilla.getGrupo() , super.getGruposByUsuario())){
                                        
                                            String nombreGrupo = null;
                                            if (subGrilla.getGrupo() != null ){
                                                    nombreGrupo = subGrilla.getGrupo().getNombre();
                                                }
                                                   
                                            catalogoSubCuadro.add(new TreeItem(subGrilla, bloqueado, version.getCatalogo().getNombre(), version.getCatalogo().getTitulo(), nombreGrupo));
                                            children1.setContieneSubCuadros(Boolean.TRUE);
                                        }
                                    }
                                    if (estructura.getTipoEstructura().getIdTipoEstructura().equals(TipoEstructuraEnum.GRILLA.getKey()))
                                    break;
                                }
                                if (estructura.getTipoEstructura().getIdTipoEstructura().equals(TipoEstructuraEnum.GRILLA.getKey()))
                                break;
                            }
                            break;
                    } 
                    if (catalogoSubCuadro.size() > 0){
                        children1.setChildren(catalogoSubCuadro);
                    }
                    catalogoChildren.add(children1);
                }
            }
            
            nodo.setChildren(catalogoChildren); 
        }      
            
        this.setListInstance(catalogoRoot);
    }
    
   
    private List<Version> contieneSubCuadro(Catalogo catalogo){
         
         List<Version> versionConSubGrilla =new ArrayList<Version>();
         
        for (Version version : catalogo.getVersionList()){
            if (version.getVigencia().equals(1L) && version.isDesagregado()){
                versionConSubGrilla.add(version);
            } 
         }
            
       return versionConSubGrilla;
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

    /**
     * obtiene el listado de catalogo perfilado por usuario
     * @return
     */
    public List<Catalogo> getCatalogoList() {
        if(catalogoList==null){
            try{
                catalogoList = super.getFacade().getCatalogoService().findCatalogoByFiltro(super.getNombreUsuario(), null, null, VigenciaEnum.VIGENTE.getKey());
            } catch (Exception e) {
                super.agregarErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_error_cargar_menu"));
            }
        }
        return catalogoList;
    }
    
    public void setCatalogoList(List<Catalogo> catalogoList) {
        this.catalogoList = catalogoList;
    }

    public void setCatalogoModel(TreeModel catalogoModel) {
        this.catalogoModel = catalogoModel;
    }

    public TreeModel getCatalogoModel() throws Exception {
        if (catalogoModel == null){
            this.getCatalogoTreeModel();
            catalogoModel = new ChildPropertyTreeModel(catalogoInstance, "children");  
        }            
        return catalogoModel;
    }
    
    public void setListInstance(List instance) {
        this.catalogoInstance = instance;
        catalogoModel = null;
    }

    public void setCatalogoSubCuadrosList(List<Catalogo> catalogoSubCuadrosList) {
        this.catalogoSubCuadrosList = catalogoSubCuadrosList;
    }

    public List<Catalogo> getCatalogoSubCuadrosList() {
        return catalogoSubCuadrosList;
    }
}
