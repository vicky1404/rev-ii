package cl.bicevida.revelaciones.mb;


import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;

import cl.bicevida.revelaciones.common.mb.SoporteBackingBean;
import cl.bicevida.revelaciones.common.model.TreeItem;
import cl.bicevida.revelaciones.common.util.PropertyManager;
import cl.bicevida.revelaciones.ejb.common.TipoEstructuraEnum;
import cl.bicevida.revelaciones.ejb.entity.Catalogo;
import cl.bicevida.revelaciones.ejb.entity.Estructura;
import cl.bicevida.revelaciones.ejb.entity.Grilla;
import cl.bicevida.revelaciones.ejb.entity.Grupo;
import cl.bicevida.revelaciones.ejb.entity.SubGrilla;
import cl.bicevida.revelaciones.ejb.entity.TipoCuadro;
import cl.bicevida.revelaciones.ejb.entity.Version;
import cl.bicevida.revelaciones.vo.DesagregacionVO;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.output.RichPanelCollection;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.model.ChildPropertyTreeModel;
import org.apache.myfaces.trinidad.model.TreeModel;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;

import static org.hamcrest.Matchers.equalTo;


/**
 * Clase que se encarga de las peticiones Http paara el manejo del mantenedor de desagregacion de notas.
 *
 * @author Manuel Gutierrez C.
 * @since 23/07/2012
 */
public class DesagregacionBackingBean extends SoporteBackingBean implements Serializable{

    @SuppressWarnings("compatibility:6781410199770071197")
    private static final long serialVersionUID = -7959306537569460483L;
    private transient Logger logger = Logger.getLogger(DesagregacionBackingBean.class);
    
    //Variables con las listas de los datos
    private List<Catalogo> catalogoList;
    private List<Catalogo> catalogoVersionVigenteList;
    private List<Catalogo> nota;
    private List<Estructura> estructuraList;
    private List<Version> versionList;
    //Variables para atributos render
    private boolean renderTablaNota;
    private boolean renderTablaVersion;    
    private transient RichSelectOneChoice tipoCuadroBinding;
    private transient RichSelectOneChoice catalogoBinding;
    private transient TreeModel grillasModel = null;
    private transient Object grillasInstance = null;
    private transient ArrayList<TreeItem> catalogoRoot;
    private SubGrilla subGrilla;
    private Grupo grupo;
    private transient RichPanelCollection panelTreeDesagregacion;
    private Integer cantidadGrupos = new Integer(2);
    private List<DesagregacionVO> desagregacionVOList;
   
    
    
    
    

    public DesagregacionBackingBean() {
        super();
    }
    
    /**
     * evento ValueChangeEvent del combo Tipo de Cuadro
     * @param valueChangeEvent
     */
    public void onChangeTipoCuadro(ValueChangeEvent valueChangeEvent) {
        
        final TipoCuadro tipoCuadro = (TipoCuadro)valueChangeEvent.getNewValue();        
        final List<Catalogo> catalogoList = select(super.getComponenteBackingBean().getCatalogoList() ,having(on(Catalogo.class).getTipoCuadro().getIdTipoCuadro(), equalTo(tipoCuadro.getIdTipoCuadro())));                        
        this.setCatalogoList(catalogoList);
    }
    
   /**
     *
     * @param event
     */
    public void buscarVersionVigente(ActionEvent event){     
        
       Catalogo catalogo = super.getFacade().getCatalogoService().findCatalogoByCatalogo( super.getFiltro().getCatalogo() );
       this.getCatalogoVersionVigenteList().clear();
       this.getVersionList().clear();
       this.getCatalogoVersionVigenteList().add(catalogo);
        
       for (Catalogo cat : this.getCatalogoVersionVigenteList()) {
           for ( Version version : cat.getVersionList() ) {
               if (version.getVigencia() != null && version.getVigencia().equals(1L)){
                    this.getVersionList().add(version);
               }
           }
       }

        if (this.getVersionList().size() > 0){
                this.setRenderTablaVersion(Boolean.TRUE);
        } else {
                this.setRenderTablaVersion(Boolean.FALSE);
                super.agregarSuccesMessage("No hay versiones disponibles");
            }
   }
    /**
     *
     * @param event
     */
    public void buscarCuadrosDesagregados(ActionEvent event){
        
        Catalogo catalogo = super.getFacade().getCatalogoService().findCatalogoByCatalogo( super.getFiltro().getCatalogo() );
        
        this.getCatalogoVersionVigenteList().clear();
        this.getVersionList().clear();
        this.getDesagregacionVOList().clear();
        this.getCatalogoVersionVigenteList().add(catalogo);
            
        for (Catalogo cat : this.getCatalogoVersionVigenteList()) {
            for ( Version version : cat.getVersionList() ) {
                if (version.getVigencia() != null && version.getVigencia().equals(1L) && version.isDesagregado()){
                     this.getVersionList().add(version);
                }
            }
        }
        
        for (Version version : this.getVersionList()){
            
            for (Estructura estructura : version.getEstructuraList()){
                
                for (Grilla grilla : estructura.getGrillaList()){
                      
                    for (SubGrilla subGrilla : grilla.getSubGrillaList()){
                        
                            DesagregacionVO desagregacion = new DesagregacionVO();
                                desagregacion.setVersion(version);
                                desagregacion.setGrilla(grilla);
                                desagregacion.setSubGrilla(subGrilla);
                                desagregacion.setCatalogo(catalogo);
                            this.getDesagregacionVOList().add(desagregacion);  
                            
                        }
                        if (estructura.getTipoEstructura().getIdTipoEstructura().equals(TipoEstructuraEnum.GRILLA.getKey()))
                        break;
                    }
                      if (estructura.getTipoEstructura().getIdTipoEstructura().equals(TipoEstructuraEnum.GRILLA.getKey()))
                      break;          
                }
                    break;
            }
        
        
        if (this.getDesagregacionVOList().size() > 0){
                this.setRenderTablaVersion(Boolean.TRUE);
        } else {
                this.setRenderTablaVersion(Boolean.FALSE);
                super.agregarSuccesMessage("Este cuadro no tiene desagregaciones");
            }
        
        
    }
    
    
    /**
     * Construye un List &lt;SelectItem&gt; con elementos de Catalogo para ser 
     * presentados en el componente de ComboBox
     * @return
     */
    public List<SelectItem> getCatalogoSelectItem() {
        List<SelectItem> lista = new ArrayList<SelectItem>();
        if(super.getFiltro().getTipoCuadro().getIdTipoCuadro() != null){
            this.buildCatalogoByTipo();
            for (Catalogo catalogo : this.getCatalogoList()) {
                lista.add(new SelectItem(catalogo, catalogo.getNombre()) );
            }
        }else{
            for (Catalogo catalogo : this.getCatalogoList()) {
                lista.add(new SelectItem(catalogo, catalogo.getNombre()) );
            }
        }
        return lista;
    }    
    
    /**
     * Construye una lista de Catalogo segun el tipo de Cuadro seleccionado
     */
    private void buildCatalogoByTipo(){        
        final List<Catalogo> catalogoList = select(super.getComponenteBackingBean().getCatalogoList() ,having(on(Catalogo.class).getTipoCuadro().getIdTipoCuadro(), equalTo(super.getFiltro().getTipoCuadro().getIdTipoCuadro())));               
        this.setCatalogoList(catalogoList);
    }
    
    public void guardar(ActionEvent event){
        
        try
        {
                super.getFacade().getGrillaService().desagregarGrilla(this.getVersionList());                
                super.agregarSuccesMessage(PropertyManager.getInstance().getMessage("desagregacion_guardar_exito"));
                this.buscarVersionVigente(event);
            
        } catch (Exception ex) {
                logger.error(ex);
                super.agregarErrorMessage(PropertyManager.getInstance().getMessage("desagregacion_guardar_error"));
            }
        
     }


    public void setCatalogoList(List<Catalogo> catalogoList) {
        this.catalogoList = catalogoList;
    }

    public List<Catalogo> getCatalogoList() {
        
        if (catalogoList == null){
                catalogoList = new ArrayList<Catalogo>();
            }
        
        return catalogoList;
    }


    public void setNota(List<Catalogo> nota) {
        this.nota = nota;
    }

    public List<Catalogo> getNota() {
        if (nota == null){
                nota = new ArrayList<Catalogo>();
            }
        return nota;
    }

    public void setRenderTablaNota(boolean renderTablaNota) {
        this.renderTablaNota = renderTablaNota;
    }

    public boolean isRenderTablaNota() {
        return renderTablaNota;
    }


    public void setEstructuraList(List<Estructura> estructuraList) {
        this.estructuraList = estructuraList;
    }

    public List<Estructura> getEstructuraList() {
        
        if (estructuraList == null){
                estructuraList = new ArrayList<Estructura>();
            }
        
        return estructuraList;
    }

    public void setCantidadGrupos(Integer cantidadGrupos) {
        this.cantidadGrupos = cantidadGrupos;
    }

    public Integer getCantidadGrupos() {
        return cantidadGrupos;
    }

    public void setVersionList(List<Version> versionList) {
        this.versionList = versionList;
    }

    public List<Version> getVersionList() {
        if (versionList == null){
                versionList = new ArrayList<Version>();
            }
        return versionList;
    }

    public void setRenderTablaVersion(boolean renderTablaVersion) {
        this.renderTablaVersion = renderTablaVersion;
    }

    public boolean isRenderTablaVersion() {
        return renderTablaVersion;
    }

    public void setTipoCuadroBinding(RichSelectOneChoice tipoCuadroBinding) {
        this.tipoCuadroBinding = tipoCuadroBinding;
    }

    public RichSelectOneChoice getTipoCuadroBinding() {
        return tipoCuadroBinding;
    }


    public void setCatalogoBinding(RichSelectOneChoice catalogoBinding) {
        this.catalogoBinding = catalogoBinding;
    }

    public RichSelectOneChoice getCatalogoBinding() {
        return catalogoBinding;
    }
    
    public TreeModel getGrillasModel() throws Exception {
        //if (grillasModel == null){
            this.getGrillasTreeModel();
            grillasModel = new ChildPropertyTreeModel(grillasInstance, "children");  
        //}            
        return grillasModel;
    }
   
   
    /**
     * genera una structure del tipo TreeModel
     * para menu de catalogo por tipo de cuadro
     */
    public void getGrillasTreeModel() throws Exception {        
        
        catalogoRoot = new ArrayList<TreeItem>();
        ArrayList<TreeItem> catalogoChildren = null;
        TreeItem nodo = null;
        
        nodo = new TreeItem();  
        nodo.setParent(Boolean.TRUE);
        nodo.setObject(super.getFiltro().getCatalogo());
                    
        catalogoRoot.add(nodo);
        
        for(Grilla grilla : this.getGrillasFromCatalogo(super.getFiltro().getCatalogo()) ){                    
     
            catalogoChildren = new ArrayList<TreeItem>();
        
            for(SubGrilla subGrilla: grilla.getSubGrillaList()){
                TreeItem children1 = new TreeItem(subGrilla);
                catalogoChildren.add(children1);
            }
            
            nodo.setChildren(catalogoChildren); 
        }
            
        this.setListInstance(catalogoRoot);
    }
    
    private List<Grilla> getGrillasFromCatalogo(Catalogo catalogo){
        
            List<Grilla> grillaList = new ArrayList<Grilla>();
            
            for (Version version : catalogo.getVersionList()){
                if (version.getVigencia().equals(1L)){
                    for (Estructura estructura : version.getEstructuraList()){
                        for (Grilla grilla : estructura.getGrillaList()){
                                grillaList.add(grilla);
                            }
                        }
                    }
                }
        
                return grillaList;
        }
    
    public void setListInstance(List instance) {
        this.grillasInstance = instance;
        grillasModel = null;
    }
    
    public void abrirPopUp(ActionEvent event){        
        this.setSubGrilla( (SubGrilla) event.getComponent().getAttributes().get("subGrilla") );
        this.setGrupo( this.getSubGrilla().getGrupo() );        
        this.displayPopup();
    }
    
    private void displayPopup(){
        FacesContext context = FacesContext.getCurrentInstance();
        ExtendedRenderKitService extRenderKitSrvc =
        Service.getRenderKitService(context, ExtendedRenderKitService.class);
        extRenderKitSrvc.addScript(context, "AdfPage.PAGE.findComponent('popUp1').show();");
    }
    
    public void displayPopup2(){
        
        if (this.getDesagregacionVOList().size() > 0) {
            
                FacesContext context = FacesContext.getCurrentInstance();
                ExtendedRenderKitService extRenderKitSrvc =
                Service.getRenderKitService(context, ExtendedRenderKitService.class);
                extRenderKitSrvc.addScript(context, "AdfPage.PAGE.findComponent('popUp2').show();");
        } else {
            
                agregarWarnMessage("Debe seleccionar un cuadro antes de eliminarlo");
                
            }
        
    }

    public void setSubGrilla(SubGrilla subGrilla) {
        this.subGrilla = subGrilla;
    }

    public SubGrilla getSubGrilla() {
        return subGrilla;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Grupo getGrupo() {
        return grupo;
    }
    
    public void asociarSubGrillaGrupo(){        
        
        boolean guardar = Boolean.TRUE;
        List<SubGrilla> subGrillaList = this.obtieneSubGrillasList(this.getSubGrilla().getGrilla().getEstructura1().getVersion().getCatalogo().getVersionList());
        
            for (SubGrilla subGrilla : subGrillaList){
                if (subGrilla.getGrupo() != null && subGrilla.getGrupo().getIdGrupo().equals(this.getGrupo().getIdGrupo())){ //Valida que las desagregaciones no tengan este grupo asociado.
                    guardar = Boolean.FALSE;  
                } 
            }
        
        if (guardar){
            
            for (SubGrilla subGrilla : subGrillaList){
                if (subGrilla.getAgrupador().equals(this.getSubGrilla().getAgrupador())){
                        subGrilla.setGrupo( this.getGrupo() );
                        super.getFacade().getGrillaService().mergeSubGrilla( subGrilla );  
                }
            }
       
        } else {
                super.agregarWarnMessage("Grupo ya asignado a esta desagregación.");
            }
        
    }
    
    public void desAsociarSubGrillaGrupo() {
        
        List<SubGrilla> subGrillaList = this.obtieneSubGrillasList(this.getSubGrilla().getGrilla().getEstructura1().getVersion().getCatalogo().getVersionList());
        
            for (SubGrilla subGrilla : subGrillaList){
                if (subGrilla.getAgrupador().equals(this.getSubGrilla().getAgrupador())){
                        subGrilla.setGrupo( null );
                        super.getFacade().getGrillaService().mergeSubGrilla( subGrilla );  
            }
        }
    }

    public void setPanelTreeDesagregacion(RichPanelCollection panelTreeDesagregacion) {
        this.panelTreeDesagregacion = panelTreeDesagregacion;
    }

    public RichPanelCollection getPanelTreeDesagregacion() {
        return panelTreeDesagregacion;
    }
    
    
    
    private List<SubGrilla> obtieneSubGrillasList(List<Version> versionList){
        
            List<SubGrilla> subGrillaList = new ArrayList<SubGrilla>();
        
            for (Version version : versionList){
                if (version.getVigencia().equals(1L)){
                    for (Estructura estructura : version.getEstructuraList()){
                        for (Grilla grilla : estructura.getGrillaList()){
                            for (SubGrilla subGrilla : grilla.getSubGrillaList()){
                                
                                    subGrillaList.add(subGrilla);
                                
                                }
                            }
                        }
                    }
            }
            
            return subGrillaList;
        }
    
    
    public void eliminarSubGrilla(ActionEvent event){
        
            SubGrilla subGrilla = (SubGrilla) event.getComponent().getAttributes().get("subGrilla");
            Version version = (Version) event.getComponent().getAttributes().get("version");
            
        try
        {
           
            if (this.getDesagregacionVOList().size() == 2 ) {

                this.displayPopup2();
                
            } else {
                
                    for (Estructura estructura : version.getEstructuraList()){
                        for (Grilla grilla : estructura.getGrillaList()){
                                for (SubGrilla sub : grilla.getSubGrillaList()){
                                        if (subGrilla.getAgrupador().intValue() == sub.getAgrupador().intValue()){
                                            getFacade().getGrillaService().eliminarSubGrilla(sub);
                                        }
                                    }
                            }
                        }
                
                    super.agregarSuccesMessage("Se eliminó el cuadro desagregado correctamente.");
                
                }
            
            
            this.buscarCuadrosDesagregados(null);
            
            
        } catch (Exception ex){
                logger.error(ex);
                super.agregarErrorMessage("Error al eliminar el cuadro desagregado.");
            }
        }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void eliminarTodasSubGrillasPorGrilla(ActionEvent event){
        Catalogo catalogo = getFiltro().getCatalogo();
        Version version = getFacade().getVersionService().findVersionVigente(catalogo);
        List<SubGrilla> subGrillaList = new ArrayList<SubGrilla>();
        for (Estructura estructura : version.getEstructuraList()){
            for (Grilla grilla : estructura.getGrillaList()){
                for (SubGrilla subGrilla : grilla.getSubGrillaList()){
                        subGrillaList.add(subGrilla);
                    }
                }
            }


        try {
            
            getFacade().getGrillaService().eliminarSubGrillas(subGrillaList);
            this.buscarCuadrosDesagregados(null);
            
        } catch (Exception e) {
            logger.error(e);
            agregarErrorMessage("Hubo un error al eliminar las sub grilla...");
        }
    }


    public void setCatalogoVersionVigenteList(List<Catalogo> catalogoVersionVigenteList) {
        this.catalogoVersionVigenteList = catalogoVersionVigenteList;
    }

    public List<Catalogo> getCatalogoVersionVigenteList() {
        
        if (catalogoVersionVigenteList == null){
                catalogoVersionVigenteList = new ArrayList<Catalogo>();
            }
        
        return catalogoVersionVigenteList;
    }

    public void setDesagregacionVOList(List<DesagregacionVO> desagregacionVOList) {
        this.desagregacionVOList = desagregacionVOList;
    }

    public List<DesagregacionVO> getDesagregacionVOList() {
        if (desagregacionVOList == null){
            
                desagregacionVOList = new ArrayList<DesagregacionVO>();
            }
        return desagregacionVOList;
    }
}
