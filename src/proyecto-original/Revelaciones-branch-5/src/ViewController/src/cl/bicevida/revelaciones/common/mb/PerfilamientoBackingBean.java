package cl.bicevida.revelaciones.common.mb;


import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;

import cl.bicevida.revelaciones.common.model.CommonGridModel;
import cl.bicevida.revelaciones.common.util.Navegacion;
import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.revelaciones.ejb.entity.Catalogo;
import cl.bicevida.revelaciones.ejb.entity.CatalogoGrupo;
import cl.bicevida.revelaciones.ejb.entity.Grupo;
import cl.bicevida.revelaciones.ejb.entity.Menu;
import cl.bicevida.revelaciones.ejb.entity.MenuGrupo;
import cl.bicevida.revelaciones.ejb.entity.TipoCuadro;
import cl.bicevida.revelaciones.ejb.entity.UsuarioGrupo;

import java.io.Serializable;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.model.AutoSuggestUIHints;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.log4j.Logger;

import static org.hamcrest.Matchers.equalTo;


public class PerfilamientoBackingBean extends SoporteBackingBean implements Serializable {
    private transient Logger logger = Logger.getLogger(PerfilamientoBackingBean.class);
    
    @SuppressWarnings("compatibility:5360572622017533832")
    private static final long serialVersionUID = 8893168287119399771L;
    
    private String nombreArchivoExport;
    private Grupo grupo;
    private TipoCuadro tipoCuadro;

    /*usuario por grupo*/
    private String user;
    private List<String> listaUsuariosAll;
    private List<String> usuariosFiltrados = new ArrayList<String>();
    private transient List<CommonGridModel<Grupo>> grillaGrupoList;
    private List<SelectItem> listaUsuariosSuggested;
    private boolean renderTablaGrupos;
    private transient RichTable tablaGrupos;
    
    /*menu por grupo*/
    private List<Menu> menuList;
    private transient List<CommonGridModel<Menu>> grillaMenuList;
    private boolean renderTablaMenu;
    private transient RichTable tablaMenu;
    
    /*estructura por grupo*/
    private transient List<CommonGridModel<Catalogo>> grillaCatalogoList;
    private boolean renderTablaCatalogo;
    private transient RichTable tablaCatalogo;
    private transient RichSelectOneChoice comboGrupos;
    private transient RichSelectOneChoice comboTiposCuadro;
    
    /*bloqueo por grupo*/
    private transient List<CommonGridModel<Grupo>> grillaBloqueoGrupoList;
    private transient RichTable tablaGruposBloqueados;
    private boolean renderTablaGruposBloqueados;
    private Long accesoBloqueado;

    public PerfilamientoBackingBean(){
        super();
    }
    
    private void resetCombo(RichSelectOneChoice combo){
       combo.resetValue();
       AdfFacesContext.getCurrentInstance().addPartialTarget(combo);
    }
    
    public String limpiarGrupoPorUsuarioAction(){        
        try{            
            this.getUsuariosFiltrados().clear();
            this.setListaUsuariosSuggested(null);
            this.setRenderTablaGrupos(Boolean.FALSE);
            this.setListaUsuariosAll(null);
            AdfFacesContext.getCurrentInstance().addPartialTarget(this.getTablaGrupos());            
        } catch (Exception e) {
            super.agregarErrorMessage("Error al limpiar datos");
            logger.error(e.getCause(), e);
        }
        return Navegacion.USUARIO_GRUPO;
    }

    public String limpiarCatalogoPorGrupoAction(){
        this.grillaCatalogoList = null;
        this.setRenderTablaCatalogo(Boolean.FALSE);
        this.resetCombo(this.getComboGrupos());        
        this.tipoCuadro = null;
        return Navegacion.ESTRUCTURA_GRUPO;
    }
    
    public String limpiarEstructuraPorGrupoAction(){
        this.setGrillaCatalogoList(null);
        this.setRenderTablaCatalogo(Boolean.FALSE);
        this.setGrupo(null);
        this.setTipoCuadro(null);
        this.resetCombo(this.getComboGrupos());
        this.resetCombo(this.getComboTiposCuadro());
        return Navegacion.ESTRUCTURA_GRUPO;
    }
    
    public String limpiarMenuPorGrupoAction(){
        this.setGrillaMenuList(null);
        this.setRenderTablaMenu(Boolean.FALSE);
        this.setGrupo(null);        
        this.resetCombo(this.getComboGrupos());        
        return Navegacion.MENU_GRUPO;
    }
            
    public String buscarGrupoPorUsuarioAction() {
        try{                   
            setGrillaGrupoList(this.getGruposByUsuarioList(super.getFacade().getSeguridadService().findGruposByUsuario(this.getUser()),
                                                           super.getFacade().getSeguridadService().findGruposAll()));
            setRenderTablaGrupos(Boolean.TRUE);           
            if (this.getListaUsuariosSuggested().isEmpty()) {
                super.agregarSuccesMessage(MessageFormat.format("No se encontraron datos para el usuario {0} por lo que se ingresaran los nuevos permisos para el usuario nuevo", this.getUser()));
            }            
        } catch (Exception e) {
            super.agregarErrorMessage("Error al obtener el listado de Grupos");
            logger.error(e.getCause(), e);
        }
        return null;
    }
        
    public String buscarMenuPorGrupoAction() {
        try {
            setGrillaMenuList(this.getMenuByGrupoList(super.getFacade().getSeguridadService().findMenuAccesoByGrupo(this.getGrupo().getIdGrupo()),
                                                      super.getFacade().getSeguridadService().findMenuFindAll()));
            setRenderTablaMenu(Boolean.TRUE);
        } catch (Exception e) {
            super.agregarErrorMessage("Error al obtener el listado de Menus");
            logger.error(e.getCause(), e);
        }
        return null;
    }
    
    public String buscarEstructurasPorGrupoAction(){        
        try {
            this.setGrillaCatalogoList(this.getCatalogoByGrupoList(super.getFacade().getCatalogoService().findCatalogoByFiltro(null, this.getTipoCuadro(), this.getGrupo(), null), 
                                                                   super.getFacade().getCatalogoService().findAllVigenteByTipo(this.getTipoCuadro())));            
            setRenderTablaCatalogo(Boolean.TRUE);
        } catch (Exception e) {
            super.agregarErrorMessage("Error al obtener el Catalogo");
            logger.error(e.getCause(), e);
        }
        return null;
    }
    
    public String buscarBloqueoPorGrupo(){
        try {            
            this.setGrillaBloqueoGrupoList(this.getGruposBloqueados(this.getGrupoListByFiltroAcceso()));    
            this.setRenderTablaGruposBloqueados(Boolean.TRUE);
        } catch (Exception e) {
            super.agregarErrorMessage("Error al obtener los grupos para bloquear ingreso al Sistema");
            logger.error(e.getCause(), e);
        }
        return null;
    }
    
    private List<Grupo> getGrupoListByFiltroAcceso() throws Exception {
        List<Grupo> grupoList;
        if(this.getAccesoBloqueado() != null){
            grupoList = select(super.getFacade().getSeguridadService().findGruposAll() ,having(on(Grupo.class).getAccesoBloquedo(), equalTo(this.getAccesoBloqueado())));
        }else{
            grupoList = super.getFacade().getSeguridadService().findGruposAll();
        }
        return grupoList;
    }

    /**
     * procesa la list de grupos a los que se indica pertenencia del usuario
     * y los persiste en la base de datos.
     * @return
     */
    public String guardarUsuarioGrupoAction() {
        List<UsuarioGrupo> usuarioGrupoList = new ArrayList<UsuarioGrupo>();
        UsuarioGrupo usuarioGrupo = null;
        try {
            for (CommonGridModel<Grupo> grillaGrupo : getGrillaGrupoList()) {
                if (grillaGrupo.isSelected()) {                   
                    usuarioGrupo = new UsuarioGrupo();
                    usuarioGrupo.setUsuarioOid(this.getUser().toUpperCase());
                    usuarioGrupo.setGrupo(grillaGrupo.getEntity());
                    usuarioGrupoList.add(usuarioGrupo);
                }
            }
            super.getFacade().getSeguridadService().persistUsuarioOidGrupo(usuarioGrupoList, this.getUser().toUpperCase());            
            super.agregarSuccesMessage("Se actualizó correctamente la lista de permisos para el usuario " + this.getUser().toUpperCase());            
            this.setListaUsuariosAll(null);
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            agregarErrorMessage("Error al agregar grupos al usuario");
        }
        return null;
    }


    /**
     * procesa una lista con los menus asignados al grupo y los persiste en la base de datos
     * @return
     */
    public String guardarMenuGrupoAction() {
        List<MenuGrupo> menuGrupoList = new ArrayList<MenuGrupo>();
        MenuGrupo menuGrupo = null;
        try {
            for (CommonGridModel<Menu> grillaMenu : getGrillaMenuList()) {
                if (grillaMenu.isSelected()) {                    
                    menuGrupo = new MenuGrupo();
                    menuGrupo.setMenu(grillaMenu.getEntity());
                    menuGrupo.setGrupo(this.getGrupo());
                    menuGrupoList.add(menuGrupo);
                }
            }
            super.getFacade().getSeguridadService().persistMenuGrupo(menuGrupoList, this.getGrupo());            
            super.agregarSuccesMessage(MessageFormat.format("Se actualizó correctamente la lista de accesos a los menus para el grupo  {0}-{1} ", this.getGrupo().getIdGrupo(), this.getGrupo().getNombre()));
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            agregarErrorMessage("Error al agregar accesos al menu");
        }
        return null;
    }
    
    public String guardarCatalogoGrupoAction() {
        List<CatalogoGrupo> catalogoGrupoList = new ArrayList<CatalogoGrupo>();
        CatalogoGrupo catalogoGrupo = null;
        try {
            for (CommonGridModel<Catalogo> grillaCatalogo : getGrillaCatalogoList()) {
                if (grillaCatalogo.isSelected()) {                    
                    catalogoGrupo = new CatalogoGrupo();
                    catalogoGrupo.setCatalogo(grillaCatalogo.getEntity());
                    catalogoGrupo.setGrupo(this.getGrupo());
                    catalogoGrupoList.add(catalogoGrupo);
                }
            }
            super.getFacade().getSeguridadService().persistCatalogoGrupo(catalogoGrupoList, this.getGrupo());
            super.agregarSuccesMessage(MessageFormat.format("Se actualizó correctamente la lista de accesos a las estructuras para el grupo {0}-{1} ", this.getGrupo().getIdGrupo(), this.getGrupo().getNombre()));
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            agregarErrorMessage("Error al agregar accesos al menu");
        }
        return null;
    }
    
    /**
     * procesa la list de grupos a los que se indica pertenencia del usuario
     * y los persiste en la base de datos.
     * @return
     */
    public String guardarBloqueoGrupoAction() {        
        try {
            List<Grupo> grupoBloqueadoList = new ArrayList<Grupo>();
            for (CommonGridModel<Grupo> grillaGrupo : getGrillaBloqueoGrupoList()){                
                grillaGrupo.getEntity().setAccesoBloquedo(grillaGrupo.isSelected() ? 1L : 0L);   
                grupoBloqueadoList.add(grillaGrupo.getEntity());            
            }
            super.getFacade().getSeguridadService().mergeGrupoList(grupoBloqueadoList);
            this.setGrillaBloqueoGrupoList(this.getGruposBloqueados(this.getGrupoListByFiltroAcceso()));
            AdfFacesContext.getCurrentInstance().addPartialTarget(this.getTablaGruposBloqueados()); 
            super.agregarSuccesMessage("Se actualizó correctamente el bloqueo del Sistema");                        
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            agregarErrorMessage("Error al bloquear el sistema");
        }
        return null;
    }
    
    
    private String formatGrupos(List<Grupo> grupoBloqueadoList){
        StringBuffer msg = new StringBuffer();
        for(Grupo grupo : grupoBloqueadoList){
            msg.append(grupo.getIdGrupo()).append("\n");
        }
        return msg.toString();
    }
    
    
    

    /**
     * metodo que genera los datos de grilla grupos, marcando seleccionados los que ya existen como permisos en la base de datos
     * @param menuAccesos
     * @param menuAll
     * @return List<CommonGridModel<Menu>>
     */
    public List<CommonGridModel<Grupo>> getGruposByUsuarioList(List<Grupo> grupoAccesosByUsuario, List<Grupo> grupoAccesosAll) {
        grillaGrupoList = new ArrayList<CommonGridModel<Grupo>>();
        CommonGridModel<Grupo> grillaGrupo;
        for (Grupo grupo : grupoAccesosAll) {
            grillaGrupo = new CommonGridModel<Grupo>();
            grillaGrupo.setEntity(grupo);
            for (Grupo grupoUsuario : grupoAccesosByUsuario) {
                if (EqualsBuilder.reflectionEquals(grupo.getIdGrupo(), grupoUsuario.getIdGrupo())) {
                    grillaGrupo.setSelected(Boolean.TRUE);
                    break;
                }
            }
            grillaGrupoList.add(grillaGrupo);
        }
        return grillaGrupoList;
    }
    
    public List<CommonGridModel<Grupo>> getGruposBloqueados(List<Grupo> grupoAccesosAll){
        CommonGridModel<Grupo> grillaBloqueoGrupo;
        grillaBloqueoGrupoList = new ArrayList<CommonGridModel<Grupo>>();
        for(Grupo grupo : grupoAccesosAll) {
            grillaBloqueoGrupo = new CommonGridModel<Grupo>();
            grillaBloqueoGrupo.setEntity(grupo);
            if(Util.getLong(grupo.getAccesoBloquedo(), 0L).equals(1L)){
                grillaBloqueoGrupo.setSelected(Boolean.TRUE);
            }
            grillaBloqueoGrupoList.add(grillaBloqueoGrupo);            
        }
        return grillaBloqueoGrupoList;
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
     * metodo que genera los datos de grilla Catalogo, marcando seleccionados los que ya existen como permisos en la base de datos
     * @param menuAccesos
     * @param menuAll
     * @return List<CommonGridModel<Menu>>
     */
    public List<CommonGridModel<Catalogo>> getCatalogoByGrupoList(List<Catalogo> catalogoByGrupo,  List<Catalogo> catalogoAll) {
        grillaCatalogoList = new ArrayList<CommonGridModel<Catalogo>>();
        CommonGridModel<Catalogo> grillaCatalogo;
        for (Catalogo catalogo : catalogoAll) {
            grillaCatalogo = new CommonGridModel<Catalogo>();
            grillaCatalogo.setEntity(catalogo);
            for (Catalogo catalogo2 : catalogoByGrupo) {
                if (EqualsBuilder.reflectionEquals(catalogo.getIdCatalogo(), catalogo2.getIdCatalogo())) {
                    grillaCatalogo.setSelected(Boolean.TRUE);
                    break;
                }
            }
            grillaCatalogoList.add(grillaCatalogo);
        }
        return grillaCatalogoList;
    }
    
    
    

    /**
     * lista para componente de autocompletar de nombres de usuario
     * @param facesContext
     * @param autoSuggestUIHints
     * @return
     */
    public List usuariosSuggestList(FacesContext facesContext, AutoSuggestUIHints autoSuggestUIHints) {  
        try{
            if (autoSuggestUIHints.getSubmittedValue() != null) {
                this.getUsuariosFiltrados().clear();
                for (String usuario : this.getListaUsuariosAll()) {
                    if ((usuario).toUpperCase().indexOf(autoSuggestUIHints.getSubmittedValue().toUpperCase()) >= 0){
                        this.getUsuariosFiltrados().add(usuario);
                    }
                }
            }
        } catch(Exception e){
            agregarErrorMessage("Error al cargar lista sugerida de usuarios");
            logger.error(e.getMessage(), e);
        }    
        return this.getListaUsuariosSuggested();
    }
    
    public List<SelectItem> getListaUsuariosSuggested() {
       listaUsuariosSuggested = getListaUsuarios();
       return listaUsuariosSuggested;
    }
    
    public List<SelectItem> getListaUsuarios(){        
        List<SelectItem> users = new ArrayList<SelectItem>();  
        try{                                    
            for(String usuario : this.getUsuariosFiltrados()){
                users.add(new SelectItem(usuario, MessageFormat.format("{0}", usuario.toUpperCase())));
            }          
        } catch(Exception e){
            agregarErrorMessage("Error al lista de usuarios");
            logger.error(e.getMessage(), e);
        }
        return users;
    }

    public void setListaUsuariosAll(List<String> listaUsuariosAll) {
        this.listaUsuariosAll = listaUsuariosAll;
    }

    public List<String> getListaUsuariosAll() {
        if (listaUsuariosAll == null) {
            try {
                listaUsuariosAll = super.getFacade().getSeguridadService().findUsuariosDistinctAll();
            } catch (Exception e) {
                logger.error(e.getCause(), e);
            }
        }
        return listaUsuariosAll;
    }

    public void setUsuariosFiltrados(List<String> usuariosFiltrados) {
        this.usuariosFiltrados = usuariosFiltrados;
    }

    public List<String> getUsuariosFiltrados() {
        return usuariosFiltrados;
    }

    public void setGrillaGrupoList(List<CommonGridModel<Grupo>> grillaGrupoList) {
        this.grillaGrupoList = grillaGrupoList;
    }

    public List<CommonGridModel<Grupo>> getGrillaGrupoList() {
        return grillaGrupoList;
    }

    public void setRenderTablaGrupos(boolean renderTablaGrupos) {
        this.renderTablaGrupos = renderTablaGrupos;
    }

    public boolean isRenderTablaGrupos() {
        return renderTablaGrupos;
    }

    public void setListaUsuariosSuggested(List<SelectItem> listaUsuariosSuggested) {
        this.listaUsuariosSuggested = listaUsuariosSuggested;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setTablaGrupos(RichTable tablaGrupos) {
        this.tablaGrupos = tablaGrupos;
    }

    public RichTable getTablaGrupos() {
        return tablaGrupos;
    }
    
    public void setNombreArchivoExport(String nombreArchivoExport) {
        this.nombreArchivoExport = nombreArchivoExport;
    }

    public String getNombreArchivoExport() {
        nombreArchivoExport = "informe-".concat(new SimpleDateFormat("dd-MM-yyyy_HH-mm").format(new Date())).concat(".xls");
        return nombreArchivoExport;
    }


    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setGrillaMenuList(List<CommonGridModel<Menu>> grillaMenuList) {
        this.grillaMenuList = grillaMenuList;
    }

    public List<CommonGridModel<Menu>> getGrillaMenuList() {
        return grillaMenuList;
    }

    public void setRenderTablaMenu(boolean renderTablaMenu) {
        this.renderTablaMenu = renderTablaMenu;
    }

    public boolean isRenderTablaMenu() {
        return renderTablaMenu;
    }

    public void setTablaMenu(RichTable tablaMenu) {
        this.tablaMenu = tablaMenu;
    }

    public RichTable getTablaMenu() {
        return tablaMenu;
    }

    public void setTipoCuadro(TipoCuadro tipoCuadro) {
        this.tipoCuadro = tipoCuadro;
    }

    public TipoCuadro getTipoCuadro() {
        return tipoCuadro;
    }

    public void setGrillaCatalogoList(List<CommonGridModel<Catalogo>> grillaCatalogoList) {
        this.grillaCatalogoList = grillaCatalogoList;
    }

    public List<CommonGridModel<Catalogo>> getGrillaCatalogoList() {
        return grillaCatalogoList;
    }

    public void setRenderTablaCatalogo(boolean renderTablaCatalogo) {
        this.renderTablaCatalogo = renderTablaCatalogo;
    }

    public boolean isRenderTablaCatalogo() {
        return renderTablaCatalogo;
    }

    public void setTablaCatalogo(RichTable tablaCatalogo) {
        this.tablaCatalogo = tablaCatalogo;
    }

    public RichTable getTablaCatalogo() {
        return tablaCatalogo;
    }

    public void setComboGrupos(RichSelectOneChoice comboGrupos) {
        this.comboGrupos = comboGrupos;
    }

    public RichSelectOneChoice getComboGrupos() {
        return comboGrupos;
    }

    public void setComboTiposCuadro(RichSelectOneChoice comboTiposCuadro) {
        this.comboTiposCuadro = comboTiposCuadro;
    }

    public RichSelectOneChoice getComboTiposCuadro() {
        return comboTiposCuadro;
    }

    public void setGrillaBloqueoGrupoList(List<CommonGridModel<Grupo>> grillaBloqueoGrupoList) {
        this.grillaBloqueoGrupoList = grillaBloqueoGrupoList;
    }

    public List<CommonGridModel<Grupo>> getGrillaBloqueoGrupoList() throws Exception {        
        return grillaBloqueoGrupoList;
    }

    public void setTablaGruposBloqueados(RichTable tablaGruposBloqueados) {
        this.tablaGruposBloqueados = tablaGruposBloqueados;
    }

    public RichTable getTablaGruposBloqueados() {
        return tablaGruposBloqueados;
    }

    public void setRenderTablaGruposBloqueados(boolean renderTablaGruposBloqueados) {
        this.renderTablaGruposBloqueados = renderTablaGruposBloqueados;
    }

    public boolean isRenderTablaGruposBloqueados() {
        return renderTablaGruposBloqueados;
    }
    
    public List<SelectItem> getFiltroBloqueoSelectItem() {
        List<SelectItem> bloqueoItems = new ArrayList<SelectItem>();
        bloqueoItems.add(new SelectItem(0L, "Habilitados"));
        bloqueoItems.add(new SelectItem(1L, "Bloqueados"));
        return bloqueoItems;
    }

    public void setAccesoBloqueado(Long accesoBloqueado) {
        this.accesoBloqueado = accesoBloqueado;
    }

    public Long getAccesoBloqueado() {
        return accesoBloqueado;
    }
}
