
package cl.bicevida.xbrl.mb.mapping;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static ch.lambdaj.Lambda.index;

import cl.bicevida.revelaciones.common.filtros.Filtro;
import cl.bicevida.revelaciones.common.mb.SoporteBackingBean;

import cl.bicevida.revelaciones.common.model.TreeItem;
import cl.bicevida.revelaciones.common.util.PropertyManager;
import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.revelaciones.ejb.entity.Catalogo;
import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.EstadoFinanciero;
import cl.bicevida.revelaciones.ejb.entity.Estructura;
import cl.bicevida.revelaciones.ejb.entity.Grilla;
import cl.bicevida.revelaciones.ejb.entity.Periodo;
import cl.bicevida.revelaciones.ejb.entity.TipoCuadro;

import cl.bicevida.revelaciones.ejb.entity.VersionPeriodo;

import cl.bicevida.revelaciones.mb.MantenedorFormulaBackingBean;

import cl.bicevida.revelaciones.vo.GrillaVO;

import cl.bicevida.xbrl.model.TaxonomyTreeItem;

import java.io.Serializable;

import java.text.MessageFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

import xbrlcore.constants.GeneralConstants;

import xbrlcore.linkbase.LabelLinkbase;
import xbrlcore.linkbase.PresentationLinkbaseElement;

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
    
    //tree taxonomia
    private transient ArrayList<TaxonomyTreeItem> taxonomyRoot;
    private transient TreeModel taxonomyModel = null;
    private transient Object taxonomyInstance = null;    
    
    //conceptos XBRL
    private DiscoverableTaxonomySet discoverableTaxonomySet;
    private List<Concept> taxonomyConceptList;
    private List<Concept> taxonomyConceptAllList;
    private Concept taxonomyConceptFilter;
    
    //bind de componentes
    private transient RichTreeTable tablaCatalogoTree;
    private transient RichDecorativeBox mappingRichDecorativeBox;
    private transient RichTable taxonomyTable;
    private transient RichTable grillaTable;
    
    //map de taxonomia con celda
    private Concept conceptoSelectedForMapping;
    
    private List<Map<Concept, List<Celda>>> mappingList;
    private transient RichPanelGroupLayout viewMappingPanel;
    
    private Map<Concept, Map<Celda, Boolean>> mapping;
    private Map<Celda, Celda> celdaMap;
    
    private List<SelectItem> celdasByConcept;
    private int unsavedMappingsCount;
    
    private String parentTaxonomia;
    
    public MapeadorTaxonomiaRevelacionesBackingBean() {        
        super();
    }
        
    @PostConstruct    
    void init(){
        taxonomyConceptFilter = new Concept();
        mapping = new LinkedHashMap<Concept, Map<Celda, Boolean>>();
        celdaMap = new LinkedHashMap<Celda, Celda>();
        celdasByConcept = new ArrayList<SelectItem>();
        unsavedMappingsCount = 0;
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
    public void buscarTaxonomyAndGrilla(ActionEvent event){
        final Estructura estructura = (Estructura)event.getComponent().getAttributes().get("estructura");
        final Catalogo catalogo = (Catalogo)event.getComponent().getAttributes().get("catalogo");        
        try {
            //grilla
            this.setCatalogo(catalogo);
            this.setEstructura(estructura);
            this.setGrilla(this.getFacade().getGrillaService().findGrillaById(estructura.getIdEstructura()));
            this.setGrillaVO(this.getFacade().getEstructuraService().getGrillaVO(this.getGrilla(), Boolean.FALSE)); 
            this.buildCeldaMap();
            //taxonomia
            this.setDiscoverableTaxonomySet(super.getFacade().getTaxonomyLoaderService().loadDiscoverableTaxonomySet(super.getFiltro().getXbrlTaxonomia().getUri()));                        
            //this.buildTaxonomyTreeModel();
            this.buildTaxonomyConcepts();
            
            this.setMapping( super.getFacade().getTaxonomyMappingRevelacionService().buildMappingByEstructura(estructura, this.getTaxonomyConceptList()) );
            this.setRenderMappingPanel(Boolean.TRUE);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            super.agregarErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_nota_get_catalogo_error"));
        } 
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getMappingRichDecorativeBox());        
    }
    
    
    public void guardarMapeo(ActionEvent event){
        try {
            super.getFacade().getTaxonomyMappingRevelacionService().persistMappingTaxonomiaRevelacion(super.getFiltro().getXbrlTaxonomia(), this.getMapping());
            super.agregarSuccesMessage("Se han guardado los Mapeos correctamente");   
            this.setUnsavedMappingsCount(0);  
            AdfFacesContext.getCurrentInstance().addPartialTarget(this.getViewMappingPanel()); 
            AdfFacesContext.getCurrentInstance().addPartialTarget(this.getGrillaTable());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            super.agregarErrorMessage("Error al guardar los datos de Mapeo");
        }
    }
    
    public void fijarConceptoForMapping(ActionEvent event){
        this.setCeldasByConcept(null);
        final Concept concept = (Concept)event.getComponent().getAttributes().get("concepto");
        concept.setMapeado(Boolean.TRUE);
        this.setConceptoSelectedForMapping(concept);   
        if(mapping.get(this.getConceptoSelectedForMapping()) == null){
            mapping.put(this.getConceptoSelectedForMapping(), new LinkedHashMap<Celda, Boolean>());
        }
        this.desmarcarCeldasSelecionadasByConcept();
        this.updateCeldaSelectedByConcept(this.getConceptoSelectedForMapping());
        this.updateMappingMap(this.getConceptoSelectedForMapping());
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getViewMappingPanel()); 
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getGrillaTable());
    }

    /**
     * @param event
     */
    public void eliminarConceptoForMapping(ActionEvent event){
        final Concept concept = (Concept)event.getComponent().getAttributes().get("concepto");
        concept.setMapeado(Boolean.FALSE);
        try {
            this.getFacade().getTaxonomyMappingRevelacionService().deleteMappingByConceptoAndTaxonomia(super.getFiltro().getXbrlTaxonomia(), concept);
            mapping.remove(concept);
            this.desmarcarCeldasSelecionadasByConcept();            
            AdfFacesContext.getCurrentInstance().addPartialTarget(this.getViewMappingPanel());
            AdfFacesContext.getCurrentInstance().addPartialTarget(this.getGrillaTable());
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            super.agregarErrorMessage(MessageFormat.format("Se ha producido un error al eliminar el Mapeo para el concepto {0}", concept.getLabel()));
        }                        
    }
    
    private void desmarcarCeldasSelecionadasByConcept() {
        if (this.getGrillaVO().getRows() != null) {
            logger.info("desmarcando celdas");
            for (Map<Long, Celda> row : this.getGrillaVO().getRows()) {
                for (Map.Entry<Long, Celda> entry : row.entrySet()) {
                    entry.getValue().setSelectedByMapping(Boolean.FALSE);
                }
            }
        }
    }
    
    public void selectCeldaForMapping(ActionEvent event){
        if(conceptoSelectedForMapping == null){
            super.agregarWarnMessage("Antes de incluir Celdas en el Mapeo. debe seleccionar un Concepto desde la Taxonomía XBRL.");
            return;
        }
        final Celda celda = (Celda)event.getComponent().getAttributes().get("celda");
        final boolean selection = (Boolean)event.getComponent().getAttributes().get("selected");
        if(selection){                                
            this.getMapping().get(this.getConceptoSelectedForMapping()).put(celda, Boolean.TRUE);            
            celda.setSelectedByMapping(Boolean.TRUE);
        }else{
            this.getMapping().get(this.getConceptoSelectedForMapping()).remove(celda); 
            celda.setSelectedByMapping(Boolean.FALSE);
        }                
        this.setUnsavedMappingsCount(this.getMapping().size());  
        this.updateMappingMap(this.getConceptoSelectedForMapping());
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getViewMappingPanel());
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getGrillaTable());
    }
            
    public void updateMappingMap(final Concept key) {                
        celdasByConcept = new ArrayList<SelectItem>();
        for(Map.Entry<Celda, Boolean> entry : this.getMapping().get(key).entrySet()) {
            if(entry.getValue()){
                final Celda celda = entry.getKey();
                celdasByConcept.add(new SelectItem( celda, MessageFormat.format("[{0},{1}]", celda.getIdColumna(), celda.getIdFila()), MessageFormat.format("Columna:{0}, Fila:{1}", celda.getIdColumna(), celda.getIdFila()) ));
            }
        }
        
    }
            
    private void updateCeldaSelectedByConcept(final Concept key) {        
        for(Map.Entry<Celda, Boolean> entry : this.getMapping().get(key).entrySet()) {
            if(entry.getValue()){
                this.getCeldaMap().get(entry.getKey()).setSelectedByMapping(Boolean.TRUE);                                            
            }
        }        
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
     * Construye un Map con las celdas presentes en la grilla
     * su key esta representado por el String [columna, fila]
     */
    private void buildCeldaMap(){
        this.getCeldaMap().clear();
        for (Map<Long, Celda> row : this.getGrillaVO().getRows()) {
            for (Map.Entry<Long, Celda> entry : row.entrySet()) {                                
                this.getCeldaMap().put(entry.getValue(), entry.getValue());
            }
        }    
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
    
    //Tree Catalogo
    
    /**
     * Metodo que construye una estructura de navegacion tipo Arbol
     * con una DTS obtenida desde los archivos .xsd de la Taxonomía.
     */
    public void buildTaxonomyTreeModel() throws Exception { 
        logger.info("Obteniendo Taxonomía desde file Server:"+super.getFiltro().getXbrlTaxonomia().getUri());
        taxonomyRoot = new ArrayList<TaxonomyTreeItem>();
        ArrayList<TaxonomyTreeItem> conceptChildren = null;
        final DiscoverableTaxonomySet discoverableTaxonomySet = this.getDiscoverableTaxonomySet();
        final LabelLinkbase labelLinkbase = this.getDiscoverableTaxonomySet().getLabelLinkbase();
        TaxonomyTreeItem nodo = null;                    
        for(Map.Entry<String, List<PresentationLinkbaseElement>> entry : discoverableTaxonomySet.getPresentationLinkbase().getLinkRoleToElementList().entrySet()) {
            nodo = new TaxonomyTreeItem();
            nodo.setTaxonomySchema(discoverableTaxonomySet.getTopTaxonomy());
            nodo.setTaxonomyParent(super.formatTaxonomyParentName(entry.getKey()));
            conceptChildren = new ArrayList<TaxonomyTreeItem>(); 
            for(PresentationLinkbaseElement linkbaseElement : entry.getValue()){  
                final Concept concept = linkbaseElement.getConcept() == null ? new Concept() : linkbaseElement.getConcept();                
                final String label = Util.getString(labelLinkbase.getLabel(concept, GeneralConstants.XBRL_ROLE_LABEL), "");                
                concept.setLabel(label);
                conceptChildren.add(new TaxonomyTreeItem(concept));
            }
            nodo.setChildren(conceptChildren); 
            taxonomyRoot.add(nodo);
        }                        
        
        this.setListTaxonomyInstance(taxonomyRoot);
    }
    
    /**
     * Metodo que construye una estructura de navegacion tipo Arbol
     * con una DTS obtenida desde los archivos .xsd de la Taxonomía.
     */
    public void buildTaxonomyTreeModel(String key) throws Exception {         
        taxonomyRoot = new ArrayList<TaxonomyTreeItem>();
        ArrayList<TaxonomyTreeItem> conceptChildren = null;
        final DiscoverableTaxonomySet discoverableTaxonomySet = this.getDiscoverableTaxonomySet();
        final LabelLinkbase labelLinkbase = this.getDiscoverableTaxonomySet().getLabelLinkbase();
        TaxonomyTreeItem nodo = null;                    
        for(Map.Entry<String, List<PresentationLinkbaseElement>> entry : discoverableTaxonomySet.getPresentationLinkbase().getLinkRoleToElementList().entrySet()) {
            if(entry.getKey().equals(key)){
                nodo = new TaxonomyTreeItem();
                nodo.setTaxonomySchema(discoverableTaxonomySet.getTopTaxonomy());
                nodo.setTaxonomyParent(super.formatTaxonomyParentName(entry.getKey()));
                conceptChildren = new ArrayList<TaxonomyTreeItem>(); 
                for(PresentationLinkbaseElement linkbaseElement : entry.getValue()){  
                    final Concept concept = linkbaseElement.getConcept() == null ? new Concept() : linkbaseElement.getConcept();                
                    final String label = Util.getString(labelLinkbase.getLabel(concept, GeneralConstants.XBRL_ROLE_LABEL), "");                
                    concept.setLabel(label);
                    conceptChildren.add(new TaxonomyTreeItem(concept));
                }
                nodo.setChildren(conceptChildren); 
                taxonomyRoot.add(nodo);
            }
        }                                
        this.setListTaxonomyInstance(taxonomyRoot);
    }
    
    /**
     * @return
     */
    public TreeModel getTaxonomyTreeModel() {                
        if (taxonomyModel == null){                
            taxonomyModel = new ChildPropertyTreeModel(taxonomyInstance, "children"); 
        }
        return taxonomyModel;
    }

    /**
     * @param instance
     */
    public void setListTaxonomyInstance(List instance) {
        this.taxonomyInstance = instance;
        taxonomyModel = null;
    }
    
    
    public List<SelectItem> getTaxonomyParentItems(){
        List<SelectItem> items = new ArrayList<SelectItem>();
        for(Map.Entry<String, List<PresentationLinkbaseElement>> entry : discoverableTaxonomySet.getPresentationLinkbase().getLinkRoleToElementList().entrySet()) {
            if(entry.getKey().toUpperCase().contains("NOTA"))
            items.add(new SelectItem( entry.getKey(), super.formatTaxonomyParentName(entry.getKey())));
        }        
        return items;
    }
    
    public void taxonomyParentChangeListener(ValueChangeEvent valueChangeEvent){
        try {
            if(valueChangeEvent.getNewValue() != null){                
                this.setParentTaxonomia((String)valueChangeEvent.getNewValue());                
                this.buildTaxonomyTreeModel(this.getParentTaxonomia());                   
            }
        } catch (Exception e) {
            super.agregarErrorMessage("Se ha producido un error al consultar los conceptos de XBRL.");
            logger.error(e);
            e.printStackTrace();
        }
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
    
    public void setUnsavedMappingsCount(int unsavedMappingsCount) {
        this.unsavedMappingsCount = unsavedMappingsCount;
    }

    public int getUnsavedMappingsCount() {
        return unsavedMappingsCount;
    }

    public void setGrillaTable(RichTable grillaTable) {
        this.grillaTable = grillaTable;
    }

    public RichTable getGrillaTable() {
        return grillaTable;
    }

    public void setCeldasByConcept(List<SelectItem> celdasByConcept) {
        this.celdasByConcept = celdasByConcept;
    }

    public List<SelectItem> getCeldasByConcept() {
        return celdasByConcept;
    }

    public Object getTaxonomyInstance() {
        return taxonomyInstance;
    }

    public void setTaxonomyRoot(ArrayList<TaxonomyTreeItem> taxonomyRoot) {
        this.taxonomyRoot = taxonomyRoot;
    }

    public ArrayList<TaxonomyTreeItem> getTaxonomyRoot() {
        return taxonomyRoot;
    }

    public void setMapping(Map<Concept, Map<Celda, Boolean>> mapping) {
        this.mapping = mapping;
    }

    public Map<Concept, Map<Celda, Boolean>> getMapping() {
        return mapping;
    }

    public void setCeldaMap(Map<Celda, Celda> celdaMap) {
        this.celdaMap = celdaMap;
    }

    public Map<Celda, Celda> getCeldaMap() {
        return celdaMap;
    }

    public void setParentTaxonomia(String parentTaxonomia) {
        this.parentTaxonomia = parentTaxonomia;
    }

    public String getParentTaxonomia() {
        return parentTaxonomia;
    }
}
