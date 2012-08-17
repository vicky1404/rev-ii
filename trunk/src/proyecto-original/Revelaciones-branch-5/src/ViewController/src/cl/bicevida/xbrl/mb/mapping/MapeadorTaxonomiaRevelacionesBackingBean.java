package cl.bicevida.xbrl.mb.mapping;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;

import cl.bicevida.revelaciones.common.filtros.Filtro;
import cl.bicevida.revelaciones.common.mb.SoporteBackingBean;

import cl.bicevida.revelaciones.common.model.TreeItem;
import cl.bicevida.revelaciones.common.util.PropertyManager;
import cl.bicevida.revelaciones.ejb.entity.Catalogo;
import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.Estructura;
import cl.bicevida.revelaciones.ejb.entity.Grilla;
import cl.bicevida.revelaciones.ejb.entity.Periodo;
import cl.bicevida.revelaciones.ejb.entity.TipoCuadro;

import cl.bicevida.revelaciones.ejb.entity.VersionPeriodo;

import cl.bicevida.revelaciones.mb.MantenedorFormulaBackingBean;

import cl.bicevida.revelaciones.vo.GrillaVO;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

import javax.annotation.PostConstruct;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import javax.faces.model.SelectItem;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.data.RichTreeTable;
import oracle.adf.view.rich.component.rich.layout.RichDecorativeBox;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.context.AdfFacesContext;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.model.ChildPropertyTreeModel;
import org.apache.myfaces.trinidad.model.TreeModel;

import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.equalTo;

import xbrlcore.taxonomy.Concept;
import xbrlcore.taxonomy.DiscoverableTaxonomySet;

/**
 * Clase BackingBean que controla las funcionalidades del Mapeador de Taxonomias v/s Revelaciones.
 * @author rodrigo.reyes@bicevida.cl

 */
public class MapeadorTaxonomiaRevelacionesBackingBean extends SoporteBackingBean implements Serializable {
    private transient final Logger logger = Logger.getLogger(MapeadorTaxonomiaRevelacionesBackingBean.class);
    @SuppressWarnings("compatibility:-7570818953576924844")
    private static final long serialVersionUID = -5712741915754307569L;
    
    private Filtro filtroPaso;
    private Catalogo catalogo;
    private Estructura estructura;
    private Grilla grilla;
    private GrillaVO grillaVO = new GrillaVO();
    private List<Catalogo> catalogoList = new ArrayList<Catalogo>();
    private List<Estructura> estructuraList = new ArrayList<Estructura>();
    private List<VersionPeriodo> versionPeriodoList; 
    private boolean renderedCatalogoTree;
    private boolean renderMappingPanel;
    
    
    //tree catalogo table    
    private transient ArrayList<TreeItem> catalogoRoot;
    private transient TreeModel catalogoModel = null;
    private transient Object catalogoInstance = null;
    
    //conceptos XBRL
    private DiscoverableTaxonomySet discoverableTaxonomySet;
    private List<Concept> taxonomyConceptList;
    private List<Concept> taxonomyConceptAllList;
    private Concept taxonomyConceptFilter;
    
    //bind de componentes
    private transient RichTreeTable tablaCatalogoTree;
    private transient RichDecorativeBox mappingRichDecorativeBox;
    private transient RichTable taxonomyTable;
    
    //map de taxonomia con celda
    private Concept conceptoSelectedForMapping;
    private List<Celda> celdaForMappingList;
    private Map<Concept, List<Celda>> mapping;
    private List<Map<Concept, List<Celda>>> mappingList;
    private transient RichPanelGroupLayout viewMappingPanel;
    
    public MapeadorTaxonomiaRevelacionesBackingBean() {        
        super();
    }
        
    @PostConstruct    
    void init(){
        taxonomyConceptFilter = new Concept();
        mapping = new HashMap<Concept, List<Celda>>();
        super.getFiltro().setPeriodo(new Periodo(getComponenteBackingBean().getPeriodoActual()));
    }
    
    void clearPage(){        
        
    }
    
    public String buscarEstructurasAction(){        
        try {
            this.setVersionPeriodoList(super.getFacade().getPeriodoService().findPeriodoAllByPeriodoCatalogoVigente(super.getFiltro().getCatalogo(), super.getFiltro().getPeriodo()));
            this.getCatalogoTreeModel();
            AdfFacesContext.getCurrentInstance().addPartialTarget(this.getTablaCatalogoTree());
            this.setRenderedCatalogoTree(Boolean.TRUE);
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            agregarErrorMessage("Error al consultar datos para realizar el proceso");
        }
        
        return null;
    }
    
    /**
     * ActionListener que ejecuta la busqueda de una grilla y una taxonomia para establecer 
     * el mapping de datos necesario para la gerneracion del informe XBRL
     * @param event
     */
    public void buscarEstructuraGrilla(ActionEvent event){
        final Estructura estructura = (Estructura)event.getComponent().getAttributes().get("estructura");
        final Catalogo catalogo = (Catalogo)event.getComponent().getAttributes().get("catalogo");        
        try {
            //grilla
            this.setCatalogo(catalogo);
            this.setEstructura(estructura);
            this.setGrilla(this.getFacade().getGrillaService().findGrillaById(estructura.getIdEstructura()));
            this.setGrillaVO(this.getFacade().getEstructuraService().getGrillaVO(this.getGrilla(), Boolean.FALSE)); 
            //taxonomia
            this.setDiscoverableTaxonomySet(super.getFacade().getTaxonomyLoaderService().loadDiscoverableTaxonomySet(super.getFiltro().getXbrlTaxonomia().getUri()));            
            this.buildTaxonomyConcepts();
            this.setRenderMappingPanel(Boolean.TRUE);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            super.agregarErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_nota_get_catalogo_error"));
        } 
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getMappingRichDecorativeBox());        
    }
    
    public void fijarConceptoForMapping(ActionEvent event){
        final Concept concept = (Concept)event.getComponent().getAttributes().get("concepto");
        this.setConceptoSelectedForMapping(concept);
        celdaForMappingList = new ArrayList<Celda>();
        mapping.put(this.getConceptoSelectedForMapping(), celdaForMappingList);
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getViewMappingPanel());               
    }
    
    public void fijarCeldaForMapping(ActionEvent event){
        final Celda celda = (Celda)event.getComponent().getAttributes().get("celda");
        if(celdaForMappingList.contains(celda)){
            super.agregarWarnMessage("Esta celda ya esta contenida en el mapping");
            return;
        }
        celdaForMappingList.add(celda);
        mapping.put(this.getConceptoSelectedForMapping(), celdaForMappingList);
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getViewMappingPanel());  
    }
    
    public void filterTaxonomyTable(ActionEvent event){
        List<Concept> filteredConcepts = null;
        filteredConcepts = select(this.getTaxonomyConceptAllList() ,having(on(Concept.class).getName(),  Matchers.startsWith(this.getTaxonomyConceptFilter().getId()) ));
        if(filteredConcepts == null || filteredConcepts.size() == 0){
            super.agregarWarnMessage("No se encontraron resultados para su criterio de búsqueda.");
            return;
        }
        this.setTaxonomyConceptList(filteredConcepts);
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getTaxonomyTable()); 
    }
    
    public void cleanFilterTaxonomyTable(ActionEvent event){                
        this.setTaxonomyConceptList(this.getTaxonomyConceptAllList());
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getTaxonomyTable()); 
    }
    
    private void buildTaxonomyConcepts(){        
        taxonomyConceptList = new ArrayList<Concept>();
        for(Concept concept : this.getDiscoverableTaxonomySet().getConcepts()){ 
            taxonomyConceptList.add(concept);            
        }
        this.setTaxonomyConceptAllList(taxonomyConceptList);        
    }
    
    /**
     * Genera una estructura de tipo TreeModel
     * para Tabla de Catalogo con sus estructuras asociadas
     */
    public void getCatalogoTreeModel() throws Exception {        
        catalogoRoot = new ArrayList<TreeItem>();
        ArrayList<TreeItem> catalogoChildren = null;
        TreeItem nodo = null;
        for(VersionPeriodo versionPeriodo : this.getVersionPeriodoList()){
            nodo = new TreeItem();
            nodo.setParent(Boolean.TRUE);
            nodo.setObject(versionPeriodo);
            catalogoRoot.add(nodo);
            catalogoChildren = new ArrayList<TreeItem>();
            for(Estructura estructura : super.getFacade().getEstructuraService().getEstructuraByVersion(versionPeriodo.getVersion(), false)){            
                catalogoChildren.add(new TreeItem(estructura));
            }
            nodo.setChildren(catalogoChildren); 
        }                    
        this.setListInstance(catalogoRoot);
    }
    
    public TreeModel getCatalogoModel() throws Exception {        
        //this.getCatalogoTreeModel();
        catalogoModel = new ChildPropertyTreeModel(catalogoInstance, "children");                  
        return catalogoModel;
    }
    
    public void setListInstance(List instance) {
        this.catalogoInstance = instance;
        catalogoModel = null;
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
                lista.add(new SelectItem(catalogo, catalogo.getNombre()));
            }
        }else{
            for (Catalogo catalogo : this.getCatalogoList()) {
                lista.add(new SelectItem(catalogo, catalogo.getNombre()));
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
    
    /**
     * evento ValueChangeEvent del combo Tipo de Cuadro
     * @param valueChangeEvent
     */
    public void onChangeTipoCuadro(ValueChangeEvent valueChangeEvent) {
        final TipoCuadro tipoCuadro = (TipoCuadro)valueChangeEvent.getNewValue();
        final List<Catalogo> catalogoList = select(super.getComponenteBackingBean().getCatalogoList() ,having(on(Catalogo.class).getTipoCuadro().getIdTipoCuadro(), equalTo(tipoCuadro.getIdTipoCuadro())));                
        this.setCatalogoList(catalogoList);
    }


    public void setCatalogoList(List<Catalogo> catalogoList) {
        this.catalogoList = catalogoList;
    }

    public List<Catalogo> getCatalogoList() {
        return catalogoList;
    }

    public void setEstructuraList(List<Estructura> estructuraList) {
        this.estructuraList = estructuraList;
    }

    public List<Estructura> getEstructuraList() {
        return estructuraList;
    }

    public void setCatalogoRoot(ArrayList<TreeItem> catalogoRoot) {
        this.catalogoRoot = catalogoRoot;
    }

    public ArrayList<TreeItem> getCatalogoRoot() {
        return catalogoRoot;
    }

    public void setTablaCatalogoTree(RichTreeTable tablaCatalogoTree) {
        this.tablaCatalogoTree = tablaCatalogoTree;
    }

    public RichTreeTable getTablaCatalogoTree() {
        return tablaCatalogoTree;
    }

    public void setRenderedCatalogoTree(boolean renderedCatalogoTree) {
        this.renderedCatalogoTree = renderedCatalogoTree;
    }

    public boolean isRenderedCatalogoTree() {
        return renderedCatalogoTree;
    }

    public void setVersionPeriodoList(List<VersionPeriodo> versionPeriodoList) {
        this.versionPeriodoList = versionPeriodoList;
    }

    public List<VersionPeriodo> getVersionPeriodoList() {
        return versionPeriodoList;
    }

    public void setRenderMappingPanel(boolean renderMappingPanel) {
        this.renderMappingPanel = renderMappingPanel;
    }

    public boolean isRenderMappingPanel() {
        return renderMappingPanel;
    }

    public void setFiltroPaso(Filtro filtroPaso) {
        this.filtroPaso = filtroPaso;
    }

    public Filtro getFiltroPaso() {
        return filtroPaso;
    }

    public void setCatalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
    }

    public Catalogo getCatalogo() {
        return catalogo;
    }

    public void setEstructura(Estructura estructura) {
        this.estructura = estructura;
    }

    public Estructura getEstructura() {
        return estructura;
    }

    public void setGrilla(Grilla grilla) {
        this.grilla = grilla;
    }

    public Grilla getGrilla() {
        return grilla;
    }

    public void setGrillaVO(GrillaVO grillaVO) {
        this.grillaVO = grillaVO;
    }

    public GrillaVO getGrillaVO() {
        return grillaVO;
    }

    public Object getCatalogoInstance() {
        return catalogoInstance;
    }

    public void setMappingRichDecorativeBox(RichDecorativeBox mappingRichDecorativeBox) {
        this.mappingRichDecorativeBox = mappingRichDecorativeBox;
    }

    public RichDecorativeBox getMappingRichDecorativeBox() {
        return mappingRichDecorativeBox;
    }

    public void setDiscoverableTaxonomySet(DiscoverableTaxonomySet discoverableTaxonomySet) {
        this.discoverableTaxonomySet = discoverableTaxonomySet;
    }

    public DiscoverableTaxonomySet getDiscoverableTaxonomySet() {
        return discoverableTaxonomySet;
    }

    public void setTaxonomyConceptList(List<Concept> taxonomyConceptList) {
        this.taxonomyConceptList = taxonomyConceptList;
    }

    public List<Concept> getTaxonomyConceptList() {
        return taxonomyConceptList;
    }

    public void setTaxonomyConceptAllList(List<Concept> taxonomyConceptAllList) {
        this.taxonomyConceptAllList = taxonomyConceptAllList;
    }

    public List<Concept> getTaxonomyConceptAllList() {
        return taxonomyConceptAllList;
    }

    public void setTaxonomyTable(RichTable taxonomyTable) {
        this.taxonomyTable = taxonomyTable;
    }

    public RichTable getTaxonomyTable() {
        return taxonomyTable;
    }

    public void setTaxonomyConceptFilter(Concept taxonomyConceptFilter) {
        this.taxonomyConceptFilter = taxonomyConceptFilter;
    }

    public Concept getTaxonomyConceptFilter() {
        return taxonomyConceptFilter;
    }

    public void setMapping(Map<Concept, List<Celda>> mapping) {
        this.mapping = mapping;
    }

    public Map<Concept, List<Celda>> getMapping() {
        return mapping;
    }

    public void setMappingList(List<Map<Concept, List<Celda>>> mappingList) {
        this.mappingList = mappingList;
    }

    public List<Map<Concept, List<Celda>>> getMappingList() {
        return mappingList;
    }

    public void setConceptoSelectedForMapping(Concept conceptoSelectedForMapping) {
        this.conceptoSelectedForMapping = conceptoSelectedForMapping;
    }

    public Concept getConceptoSelectedForMapping() {
        return conceptoSelectedForMapping;
    }

    public void setViewMappingPanel(RichPanelGroupLayout viewMappingPanel) {
        this.viewMappingPanel = viewMappingPanel;
    }

    public RichPanelGroupLayout getViewMappingPanel() {
        return viewMappingPanel;
    }

    public void setCeldaForMappingList(List<Celda> celdaForMappingList) {
        this.celdaForMappingList = celdaForMappingList;
    }

    public List<Celda> getCeldaForMappingList() {
        return celdaForMappingList;
    }
}
