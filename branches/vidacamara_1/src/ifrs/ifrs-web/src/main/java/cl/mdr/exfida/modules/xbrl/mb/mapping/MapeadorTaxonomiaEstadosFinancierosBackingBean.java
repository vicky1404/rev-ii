package cl.mdr.exfida.modules.xbrl.mb.mapping;


import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;



import cl.mdr.ifrs.ejb.cross.EeffUtil;
import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.DetalleEeff;
import cl.mdr.ifrs.ejb.entity.EstadoFinanciero;
import cl.mdr.ifrs.ejb.entity.Periodo;
import cl.mdr.exfida.modules.xbrl.model.TaxonomyTreeItem;
import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.mb.PropertyManager;

import java.io.Serializable;

import java.text.MessageFormat;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

/*
import oracle.adf.view.rich.component.rich.data.RichTree;
import oracle.adf.view.rich.component.rich.layout.RichDecorativeBox;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
*/
//import oracle.adf.view.rich.context.AdfFacesContext;

import org.apache.log4j.Logger;
//import org.apache.myfaces.trinidad.model.ChildPropertyTreeModel;
//import org.apache.myfaces.trinidad.model.TreeModel;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.*;

import xbrlcore.constants.GeneralConstants;

import xbrlcore.linkbase.LabelLinkbase;
import xbrlcore.linkbase.PresentationLinkbaseElement;

import xbrlcore.taxonomy.Concept;
import xbrlcore.taxonomy.DiscoverableTaxonomySet;

@ManagedBean(name="mapeadorTaxonomiaEstadosFinancierosBackingBean")
@ViewScoped
public class MapeadorTaxonomiaEstadosFinancierosBackingBean extends AbstractBackingBean implements Serializable {
    private transient final Logger logger = Logger.getLogger(MapeadorTaxonomiaEstadosFinancierosBackingBean.class);
   
    private static final long serialVersionUID = -6638283037052343681L;
    
    //atributos generales
    private boolean renderMappingPanel;
    private Periodo periodoVigente;
    private EstadoFinanciero estadoFinanciero;
    private List<EstadoFinanciero> estadoFinancieroList;
    private List<EstadoFinanciero> estadoFinancieroFilteredList;
    private List<DetalleEeff> detalleEstadoFinancieroList;
    private Map<String, Long[]> rangoCodigoFecuMap;
    
    //conceptos XBRL
    private DiscoverableTaxonomySet discoverableTaxonomySet;
    private List<Concept> taxonomyConceptList;
    
    //tree taxonomia
    private String parentTaxonomia;
    private transient ArrayList<TaxonomyTreeItem> taxonomyRoot;
    //private transient TreeModel taxonomyModel = null;
    private transient Object taxonomyInstance = null;   
    
    
    //bind de componentes    
    /*
    private transient RichDecorativeBox mappingRichDecorativeBox;   
    private transient RichPanelGroupLayout viewMappingPanel;
    private transient RichTree conceptoXbrlTree;
    */
    
    //mapping    
    private Concept conceptoSelectedForMapping;
    private Map<Concept, Map<EstadoFinanciero, Boolean>> mappingEstadoFinanciero;
    private Map<Concept, Map<DetalleEeff, Boolean>> mappingDetalleEstadoFinanciero;
    
    //display de elementos contenidos en el mapping
    private List<SelectItem> codigosFecuByConcept;
    private List<SelectItem> cuentasContablesByConcept;
    
    private int unsavedMappingsCount;
    

    public MapeadorTaxonomiaEstadosFinancierosBackingBean() {
        super();
    }
    
    @PostConstruct
    void init(){        
        try {
            this.setPeriodoVigente(super.getFacadeService().getPeriodoService().findMaxPeriodoObj());
            //this.setEstadoFinancieroList(super.getFacadeService().getEstadoFinancieroService().getEeffVigenteByPeriodo(this.getPeriodoVigente().getIdPeriodo(), this.getFiltroPeriodoEmpresa().getIdRut())); //TODO Descomentar y linea de abajo que tiene el rut en duro.
            this.setEstadoFinancieroList(super.getFacadeService().getEstadoFinancieroService().getEeffVigenteByPeriodo(this.getPeriodoVigente().getIdPeriodo(), 15505123L));
            this.setRangoCodigoFecuMap(super.getFacadeService().getTaxonomyMappingEstadoFinancieroService().getRangoCodigoFecuMap());
            mappingEstadoFinanciero = new LinkedHashMap<Concept, Map<EstadoFinanciero, Boolean>>();
            //mappingDetalleEstadoFinanciero = new LinkedHashMap<Concept, Map<DetalleEeff, Boolean>>();            
        } catch (Exception e) {        	
            super.addErrorMessage("Se ha producido un error al inicializar los datos de la aplicación");
            logger.error(e);
        }
        
    }
    
    public void buscarTaxonomia(ActionEvent event){        
        try {            
            //taxonomia
            this.setDiscoverableTaxonomySet(super.getFacadeService().getTaxonomyLoaderService().loadDiscoverableTaxonomySet(super.getFiltroBackingBean().getXbrlTaxonomia().getUri()));                        
            //this.buildTaxonomyTreeModel();
            this.buildTaxonomyConcepts();
            this.setMappingEstadoFinanciero(super.getFacadeService().getTaxonomyMappingEstadoFinancieroService().buildMappingEstadoFinanciero(super.getFiltroBackingBean().getXbrlTaxonomia(), this.getTaxonomyConceptList(), this.getEstadoFinancieroList()));
            this.setRenderMappingPanel(Boolean.TRUE);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            super.addErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_nota_get_catalogo_error"));
        } 
        //AdfFacesContext.getCurrentInstance().addPartialTarget(this.getMappingRichDecorativeBox());        
    }
    
    public void fijarConceptoForMapping(ActionEvent event){        
        final Concept concept = (Concept)event.getComponent().getAttributes().get("concepto");
        concept.setMapeado(Boolean.TRUE);
        this.setConceptoSelectedForMapping(concept);   
        if(mappingEstadoFinanciero.get(this.getConceptoSelectedForMapping()) == null){
            mappingEstadoFinanciero.put(this.getConceptoSelectedForMapping(), new LinkedHashMap<EstadoFinanciero, Boolean>());
        }
        /*if(mappingDetalleEstadoFinanciero.get(this.getConceptoSelectedForMapping()) == null){
            mappingDetalleEstadoFinanciero.put(this.getConceptoSelectedForMapping(), new LinkedHashMap<DetalleEeff, Boolean>());
        }*/
        this.desmarcarCodigosFecu();
        //this.desmarcarCuentasContables();
        this.updateMappingDisplay(this.getConceptoSelectedForMapping());
        //AdfFacesContext.getCurrentInstance().addPartialTarget(this.getViewMappingPanel());
        //AdfFacesContext.getCurrentInstance().addPartialTarget(this.getMappingRichDecorativeBox());      
    }
    
    public void eliminarConceptoForMapping(ActionEvent event){
        final Concept concept = (Concept)event.getComponent().getAttributes().get("concepto");
        concept.setMapeado(Boolean.FALSE);
        try {
            this.getFacadeService().getTaxonomyMappingEstadoFinancieroService().deleteMappingByConceptoAndTaxonomia(super.getFiltroBackingBean().getXbrlTaxonomia(), concept);
            mappingEstadoFinanciero.remove(concept);
            this.desmarcarCodigosFecu();  
            this.setCodigosFecuByConcept(null);
            //AdfFacesContext.getCurrentInstance().addPartialTarget(this.getViewMappingPanel());
            //AdfFacesContext.getCurrentInstance().addPartialTarget(this.getMappingRichDecorativeBox());  
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            super.addErrorMessage(MessageFormat.format("Se ha producido un error al eliminar el Mapeo para el concepto {0}", concept.getLabel()));
        }
            
            
    }
    
    public void guardarMapeo(ActionEvent event){
        try {
            super.getFacadeService().getTaxonomyMappingEstadoFinancieroService().persistMappingTaxonomiaEstadoFinanciero(super.getFiltroBackingBean().getXbrlTaxonomia(), this.getMappingEstadoFinanciero());
            super.addInfoMessage("Se han guardado los Mapeos correctamente");    
            this.setUnsavedMappingsCount(0);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            super.addErrorMessage("Error al guardar los datos de Mapeo");
        }
    }
    
    public void clearPage(ActionEvent event){
        this.setUnsavedMappingsCount(0);
        this.getMappingEstadoFinanciero().clear();
        this.setConceptoSelectedForMapping(null);
        this.setCodigosFecuByConcept(null);
        //AdfFacesContext.getCurrentInstance().addPartialTarget(this.getViewMappingPanel());
        //AdfFacesContext.getCurrentInstance().addPartialTarget(this.getMappingRichDecorativeBox());
    }
    
    public void selectCodigoFecuForMapping(ActionEvent event){        
        if(conceptoSelectedForMapping == null){
            super.addWarnMessage("Antes de incluir un C�digo FECU en el Mapeo. debe seleccionar un Concepto desde la Taxonom�a XBRL.");
            return;
        }
        final EstadoFinanciero estadoFinanciero = (EstadoFinanciero)event.getComponent().getAttributes().get("estadoFinanciero");
        final boolean selection = (Boolean)event.getComponent().getAttributes().get("selected");
        if(selection){                                
            this.getMappingEstadoFinanciero().get(this.getConceptoSelectedForMapping()).put(estadoFinanciero, Boolean.TRUE);            
            estadoFinanciero.setSelectedForMapping(Boolean.TRUE);
        }else{
            this.getMappingEstadoFinanciero().get(this.getConceptoSelectedForMapping()).remove(estadoFinanciero); 
            estadoFinanciero.setSelectedForMapping(Boolean.FALSE);
        }                
        //this.setUnsavedMappingsCount(this.getMappingEstadoFinanciero().size() + this.getMappingDetalleEstadoFinanciero().size()); 
        this.updateMappingDisplay(this.getConceptoSelectedForMapping());
        //AdfFacesContext.getCurrentInstance().addPartialTarget(this.getViewMappingPanel());
        //AdfFacesContext.getCurrentInstance().addPartialTarget(this.getMappingRichDecorativeBox());
    }
    
    @Deprecated
    public void selectCuentaContableForMapping(ActionEvent event){        
        if(conceptoSelectedForMapping == null){
            super.addWarnMessage("Antes de incluir una Cuenta Contable en el Mapeo. debe seleccionar un Concepto desde la Taxonom�a XBRL.");
            return;
        }
        final DetalleEeff detalleEstadoFinanciero = (DetalleEeff)event.getComponent().getAttributes().get("detalleEstadoFinanciero");
        final boolean selection = (Boolean)event.getComponent().getAttributes().get("selected");
        if(selection){                                
            this.getMappingDetalleEstadoFinanciero().get(this.getConceptoSelectedForMapping()).put(detalleEstadoFinanciero, Boolean.TRUE);            
            detalleEstadoFinanciero.setSelectedForMapping(Boolean.TRUE);
        }else{
            this.getMappingDetalleEstadoFinanciero().get(this.getConceptoSelectedForMapping()).remove(detalleEstadoFinanciero); 
            detalleEstadoFinanciero.setSelectedForMapping(Boolean.FALSE);
        }                
        this.setUnsavedMappingsCount(this.getMappingEstadoFinanciero().size() + this.getMappingDetalleEstadoFinanciero().size());  
        this.updateMappingDisplay(this.getConceptoSelectedForMapping());
        //AdfFacesContext.getCurrentInstance().addPartialTarget(this.getViewMappingPanel());
        //AdfFacesContext.getCurrentInstance().addPartialTarget(this.getMappingRichDecorativeBox());
    }

    /**
     */
    public void desmarcarCodigosFecu(){
        for(EstadoFinanciero estadoFinanciero : this.getEstadoFinancieroList()){
            estadoFinanciero.setSelectedForMapping(Boolean.FALSE);
        }
    }

    /**
     */
    public void desmarcarCuentasContables(){
        for(DetalleEeff detalleEstadoFinanciero : this.getDetalleEstadoFinancieroList()){
            detalleEstadoFinanciero.setSelectedForMapping(Boolean.FALSE);
        }
    }
    
    public void updateMappingDisplay(final Concept key) {                
        codigosFecuByConcept = new ArrayList<SelectItem>();
        //cuentasContablesByConcept = new ArrayList<SelectItem>();
        if(this.getMappingEstadoFinanciero().size() > 0){
            for(Map.Entry<EstadoFinanciero, Boolean> entry : this.getMappingEstadoFinanciero().get(key).entrySet()) {
                if(entry.getValue()){
                    EstadoFinanciero estadoFinanciero = entry.getKey();
                    estadoFinanciero.setSelectedForMapping(Boolean.TRUE);                    
                    codigosFecuByConcept.add(new SelectItem( estadoFinanciero, MessageFormat.format("[{0}]", estadoFinanciero.getFecuFormat()), MessageFormat.format("C�digo FECU: {0} - {1}", estadoFinanciero.getFecuFormat(), estadoFinanciero.getCodigoFecu().getDescripcion()) ));
                }
            }
        }
        
        /*
        if(this.getMappingDetalleEstadoFinanciero().size() > 0){
            for(Map.Entry<DetalleEeff, Boolean> entry : this.getMappingDetalleEstadoFinanciero().get(key).entrySet()) {
                if(entry.getValue()){
                    final DetalleEeff detalleEstadoFinanciero = entry.getKey();
                    cuentasContablesByConcept.add(new SelectItem( detalleEstadoFinanciero, MessageFormat.format("[{0}]", detalleEstadoFinanciero.getCuentaContable().getIdCuenta()), MessageFormat.format("Cuenta Contable:{0}", detalleEstadoFinanciero.getCuentaContable().getIdCuenta()) ));
                }
            }
        }
        */
        
    }
    
    /**
     * Metodo que construye una estructura de navegacion tipo Arbol
     * con una DTS obtenida desde los archivos .xsd de la Taxonom�a.
     */
    public void buildTaxonomyTreeModel() throws Exception {         
        taxonomyRoot = new ArrayList<TaxonomyTreeItem>();
        ArrayList<TaxonomyTreeItem> conceptChildren = null;
        ArrayList<TaxonomyTreeItem> subConceptChildren = null;
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
     * con una DTS obtenida desde los archivos .xsd de la Taxonom�a.
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
                    final Concept conceptParent = linkbaseElement.getConcept() == null ? new Concept() : linkbaseElement.getConcept();                
                    final String label = Util.getString(labelLinkbase.getLabel(conceptParent, GeneralConstants.XBRL_ROLE_LABEL), "");                    
                    conceptParent.setLabel(label);
                    TaxonomyTreeItem conceptParentItem = new TaxonomyTreeItem(conceptParent);
                    
                    
                    
                    List<Concept> successorElements = linkbaseElement.getSuccessorElements();
                    if(!successorElements.isEmpty()){
                        List<TaxonomyTreeItem> conceptSuccesorItem = new ArrayList<TaxonomyTreeItem>();
                        for(Concept conceptSuccesor : successorElements){                                        
                            final String labelSuccesor = Util.getString(labelLinkbase.getLabel(conceptSuccesor, GeneralConstants.XBRL_ROLE_LABEL), "");
                            conceptSuccesor.setLabel(labelSuccesor);
                            conceptSuccesorItem.add(new TaxonomyTreeItem(conceptSuccesor));                            
                        }
                        conceptParentItem.setChildren(conceptSuccesorItem);
                    }  
                    
                    conceptChildren.add(conceptParentItem);
                }
                nodo.setChildren(conceptChildren); 
                taxonomyRoot.add(nodo);
            }
        }                                
        this.setListTaxonomyInstance(taxonomyRoot);
    }
    
    private void addNode(TaxonomyTreeItem nodo, List<TaxonomyTreeItem> hojas){
        nodo.setChildren(hojas);
    }
    
    /**
     * @return
     */
    /*
    public TreeModel getTaxonomyTreeModel() {                
        if (taxonomyModel == null){                
            taxonomyModel = new ChildPropertyTreeModel(taxonomyInstance, "children"); 
        }
        return taxonomyModel;
    }
    */

    /**
     * @param instance
     */
    public void setListTaxonomyInstance(List instance) {
        this.taxonomyInstance = instance;
        //taxonomyModel = null;
    }
    
    public List<SelectItem> getFecuItems() {
        List<SelectItem> fecuItems = new ArrayList<SelectItem>();
        for (final EstadoFinanciero eeff : this.getEstadoFinancieroList()) {
            fecuItems.add(new SelectItem(eeff, EeffUtil.formatFecu(eeff.getIdFecu())));
        }
        return fecuItems;
    }
    
    public void codigoFecuChangeListener(ValueChangeEvent valueChangeEvent) {
        if(valueChangeEvent.getNewValue() != null){
            this.setDetalleEstadoFinancieroList(super.getFacadeService().getEstadoFinancieroService().getDetalleEeffByEeff((EstadoFinanciero)valueChangeEvent.getNewValue()));            
        }else{
            this.setDetalleEstadoFinancieroList(null);
        }
    }
    
    public List<SelectItem> getTaxonomyParentItems(){
        List<SelectItem> items = new ArrayList<SelectItem>();
        if (discoverableTaxonomySet != null){
	        for(Map.Entry<String, List<PresentationLinkbaseElement>> entry : discoverableTaxonomySet.getPresentationLinkbase().getLinkRoleToElementList().entrySet()) {
	            if(entry.getKey().contains("eeff"))
	            items.add(new SelectItem( entry.getKey(), super.formatTaxonomyParentName(entry.getKey())));
	        }        
        }
        return items;
    }
    
    public void taxonomyParentChangeListener(ValueChangeEvent valueChangeEvent){
        try {
            if(valueChangeEvent.getNewValue() != null){                
                this.setParentTaxonomia((String)valueChangeEvent.getNewValue());                
                this.buildTaxonomyTreeModel(this.getParentTaxonomia());   
                /*if (conceptoXbrlTree != null && conceptoXbrlTree.getDisclosedRowKeys()!=null ){
                    conceptoXbrlTree.getDisclosedRowKeys().clear();
                    conceptoXbrlTree.setInitiallyExpanded(Boolean.TRUE);                    
                    AdfFacesContext.getCurrentInstance().addPartialTarget(conceptoXbrlTree);
                }*/
                Long[] rango = this.getRangoCodigoFecuMap().get(this.getParentTaxonomia());
                this.setEstadoFinancieroFilteredList(select(this.getEstadoFinancieroList(), having(on(EstadoFinanciero.class).getIdFecu(), greaterThanOrEqualTo(rango[0]))                                                         
                                                     .and(having(on(EstadoFinanciero.class).getIdFecu(), lessThanOrEqualTo(rango[1])))));
            }
        } catch (Exception e) {
            super.addErrorMessage("Se ha producido un error al consultar los conceptos de XBRL.");
            logger.error(e);
            e.printStackTrace();
        }
    }
    
    private void buildTaxonomyConcepts(){        
        taxonomyConceptList = new ArrayList<Concept>();
        for(Concept concept : this.getDiscoverableTaxonomySet().getConcepts()){ 
            taxonomyConceptList.add(concept);            
        }            
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

    public void setTaxonomyRoot(ArrayList<TaxonomyTreeItem> taxonomyRoot) {
        this.taxonomyRoot = taxonomyRoot;
    }

    public ArrayList<TaxonomyTreeItem> getTaxonomyRoot() {
        return taxonomyRoot;
    }

    /*
    public void setTaxonomyModel(TreeModel taxonomyModel) {
        this.taxonomyModel = taxonomyModel;
    }

    public TreeModel getTaxonomyModel() {
        return taxonomyModel;
    }
	*/
    
    public void setTaxonomyInstance(Object taxonomyInstance) {
        this.taxonomyInstance = taxonomyInstance;
    }

    public Object getTaxonomyInstance() {
        return taxonomyInstance;
    }

    public void setRenderMappingPanel(boolean renderMappingPanel) {
        this.renderMappingPanel = renderMappingPanel;
    }

    public boolean isRenderMappingPanel() {
        return renderMappingPanel;
    }

    /*
    public void setMappingRichDecorativeBox(RichDecorativeBox mappingRichDecorativeBox) {
        this.mappingRichDecorativeBox = mappingRichDecorativeBox;
    }

    public RichDecorativeBox getMappingRichDecorativeBox() {
        return mappingRichDecorativeBox;
    }
    */

    public void setEstadoFinancieroList(List<EstadoFinanciero> estadoFinancieroList) {
        this.estadoFinancieroList = estadoFinancieroList;
    }

    public List<EstadoFinanciero> getEstadoFinancieroList() {
        return estadoFinancieroList;
    }

    public void setPeriodoVigente(Periodo periodoVigente) {
        this.periodoVigente = periodoVigente;
    }

    public Periodo getPeriodoVigente() {
        return periodoVigente;
    }

    public void setDetalleEstadoFinancieroList(List<DetalleEeff> detalleEstadoFinancieroList) {
        this.detalleEstadoFinancieroList = detalleEstadoFinancieroList;
    }

    public List<DetalleEeff> getDetalleEstadoFinancieroList() {
        return detalleEstadoFinancieroList;
    }

    public void setEstadoFinanciero(EstadoFinanciero estadoFinanciero) {
        this.estadoFinanciero = estadoFinanciero;
    }

    public EstadoFinanciero getEstadoFinanciero() {
        return estadoFinanciero;
    }

    public void setConceptoSelectedForMapping(Concept conceptoSelectedForMapping) {
        this.conceptoSelectedForMapping = conceptoSelectedForMapping;
    }

    public Concept getConceptoSelectedForMapping() {
        return conceptoSelectedForMapping;
    }

    public void setMappingEstadoFinanciero(Map<Concept, Map<EstadoFinanciero, Boolean>> mappingEstadoFinanciero) {
        this.mappingEstadoFinanciero = mappingEstadoFinanciero;
    }

    public Map<Concept, Map<EstadoFinanciero, Boolean>> getMappingEstadoFinanciero() {
        return mappingEstadoFinanciero;
    }

    public void setMappingDetalleEstadoFinanciero(Map<Concept, Map<DetalleEeff, Boolean>> mappingDetalleEstadoFinanciero) {
        this.mappingDetalleEstadoFinanciero = mappingDetalleEstadoFinanciero;
    }

    public Map<Concept, Map<DetalleEeff, Boolean>> getMappingDetalleEstadoFinanciero() {
        return mappingDetalleEstadoFinanciero;
    }

    public void setUnsavedMappingsCount(int unsavedMappingsCount) {
        this.unsavedMappingsCount = unsavedMappingsCount;
    }

    public int getUnsavedMappingsCount() {
        return unsavedMappingsCount;
    }

    public void setCodigosFecuByConcept(List<SelectItem> codigosFecuByConcept) {
        this.codigosFecuByConcept = codigosFecuByConcept;
    }

    public List<SelectItem> getCodigosFecuByConcept() {
        return codigosFecuByConcept;
    }

    public void setCuentasContablesByConcept(List<SelectItem> cuentasContablesByConcept) {
        this.cuentasContablesByConcept = cuentasContablesByConcept;
    }

    public List<SelectItem> getCuentasContablesByConcept() {
        return cuentasContablesByConcept;
    }

    /*
    public void setViewMappingPanel(RichPanelGroupLayout viewMappingPanel) {
        this.viewMappingPanel = viewMappingPanel;
    }

    public RichPanelGroupLayout getViewMappingPanel() {
        return viewMappingPanel;
    }
    */

    public void setParentTaxonomia(String parentTaxonomia) {
        this.parentTaxonomia = parentTaxonomia;
    }

    public String getParentTaxonomia() {
        return parentTaxonomia;
    }

    public void setRangoCodigoFecuMap(Map<String, Long[]> rangoCodigoFecuMap) {
        this.rangoCodigoFecuMap = rangoCodigoFecuMap;
    }

    public Map<String, Long[]> getRangoCodigoFecuMap() {
        return rangoCodigoFecuMap;
    }

    public void setEstadoFinancieroFilteredList(List<EstadoFinanciero> estadoFinancieroFilteredList) {
        this.estadoFinancieroFilteredList = estadoFinancieroFilteredList;
    }

    public List<EstadoFinanciero> getEstadoFinancieroFilteredList() {
        return estadoFinancieroFilteredList;
    }

    /*
    public void setConceptoXbrlTree(RichTree conceptoXbrlTree) {
        this.conceptoXbrlTree = conceptoXbrlTree;
    }

    public RichTree getConceptoXbrlTree() {
        return conceptoXbrlTree;
    }
    */
}
