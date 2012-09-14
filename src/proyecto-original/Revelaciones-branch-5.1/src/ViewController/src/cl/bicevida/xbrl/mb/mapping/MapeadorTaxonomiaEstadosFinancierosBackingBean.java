package cl.bicevida.xbrl.mb.mapping;

import cl.bicevida.revelaciones.common.mb.SoporteBackingBean;

import cl.bicevida.revelaciones.common.util.PropertyManager;
import cl.bicevida.revelaciones.ejb.cross.EeffUtil;
import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.revelaciones.ejb.entity.Catalogo;
import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.DetalleEeff;
import cl.bicevida.revelaciones.ejb.entity.EstadoFinanciero;
import cl.bicevida.revelaciones.ejb.entity.Estructura;

import cl.bicevida.revelaciones.ejb.entity.Periodo;
import cl.bicevida.xbrl.model.TaxonomyTreeItem;

import java.io.Serializable;

import java.text.MessageFormat;

import java.util.ArrayList;
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

import xbrlcore.constants.GeneralConstants;

import xbrlcore.linkbase.LabelLinkbase;
import xbrlcore.linkbase.PresentationLinkbaseElement;

import xbrlcore.taxonomy.Concept;
import xbrlcore.taxonomy.DiscoverableTaxonomySet;

public class MapeadorTaxonomiaEstadosFinancierosBackingBean extends SoporteBackingBean implements Serializable {
    private transient final Logger logger = Logger.getLogger(MapeadorTaxonomiaEstadosFinancierosBackingBean.class);
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = -6638283037052343681L;
    
    //atributos generales
    private boolean renderMappingPanel;
    private Periodo periodoVigente;
    private EstadoFinanciero estadoFinanciero;
    private List<EstadoFinanciero> estadoFinancieroList;
    private List<DetalleEeff> detalleEstadoFinancieroList;
    
    //conceptos XBRL
    private DiscoverableTaxonomySet discoverableTaxonomySet;
    private List<Concept> taxonomyConceptList;
    
    //tree taxonomia
    private transient ArrayList<TaxonomyTreeItem> taxonomyRoot;
    private transient TreeModel taxonomyModel = null;
    private transient Object taxonomyInstance = null;   
    
    //bind de componentes    
    private transient RichDecorativeBox mappingRichDecorativeBox;   
    private transient RichPanelGroupLayout viewMappingPanel;
    
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
        this.setPeriodoVigente(super.getFacade().getPeriodoService().findMaxPeriodoObj());
        this.setEstadoFinancieroList(super.getFacade().getEstadoFinancieroService().getEeffVigenteByPeriodo(this.getPeriodoVigente().getIdPeriodo()));   
        mappingEstadoFinanciero = new LinkedHashMap<Concept, Map<EstadoFinanciero, Boolean>>();
        mappingDetalleEstadoFinanciero = new LinkedHashMap<Concept, Map<DetalleEeff, Boolean>>();
    }
    
    public void buscarTaxonomia(ActionEvent event){        
        try {            
            //taxonomia
            this.setDiscoverableTaxonomySet(super.getFacade().getTaxonomyLoaderService().loadDiscoverableTaxonomySet(super.getFiltro().getXbrlTaxonomia().getUri()));                        
            this.buildTaxonomyTreeModel();                                
            this.setRenderMappingPanel(Boolean.TRUE);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            super.agregarErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_nota_get_catalogo_error"));
        } 
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getMappingRichDecorativeBox());        
    }
    
    public void fijarConceptoForMapping(ActionEvent event){        
        final Concept concept = (Concept)event.getComponent().getAttributes().get("concepto");
        concept.setMapeado(Boolean.TRUE);
        this.setConceptoSelectedForMapping(concept);   
        if(mappingEstadoFinanciero.get(this.getConceptoSelectedForMapping()) == null){
            mappingEstadoFinanciero.put(this.getConceptoSelectedForMapping(), new LinkedHashMap<EstadoFinanciero, Boolean>());
        }
        if(mappingDetalleEstadoFinanciero.get(this.getConceptoSelectedForMapping()) == null){
            mappingDetalleEstadoFinanciero.put(this.getConceptoSelectedForMapping(), new LinkedHashMap<DetalleEeff, Boolean>());
        }
        this.desmarcarCodigosFecu();
        this.desmarcarCuentasContables();
        this.updateMappingDisplay(this.getConceptoSelectedForMapping());
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getViewMappingPanel());
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getMappingRichDecorativeBox());      
    }
    
    public void selectCodigoFecuForMapping(ActionEvent event){        
        if(conceptoSelectedForMapping == null){
            super.agregarWarnMessage("Antes de incluir un Código FECU en el Mapeo. debe seleccionar un Concepto desde la Taxonomía XBRL.");
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
        this.setUnsavedMappingsCount(this.getMappingEstadoFinanciero().size() + this.getMappingDetalleEstadoFinanciero().size()); 
        this.updateMappingDisplay(this.getConceptoSelectedForMapping());
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getViewMappingPanel());
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getMappingRichDecorativeBox());
    }
    
    public void selectCuentaContableForMapping(ActionEvent event){        
        if(conceptoSelectedForMapping == null){
            super.agregarWarnMessage("Antes de incluir una Cuenta Contable en el Mapeo. debe seleccionar un Concepto desde la Taxonomía XBRL.");
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
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getViewMappingPanel());
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getMappingRichDecorativeBox());
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
        cuentasContablesByConcept = new ArrayList<SelectItem>();
        if(this.getMappingEstadoFinanciero().size() > 0){
            for(Map.Entry<EstadoFinanciero, Boolean> entry : this.getMappingEstadoFinanciero().get(key).entrySet()) {
                if(entry.getValue()){
                    final EstadoFinanciero estadoFinanciero = entry.getKey();
                    codigosFecuByConcept.add(new SelectItem( estadoFinanciero, MessageFormat.format("[{0}]", estadoFinanciero.getFecuFormat()), MessageFormat.format("Código FECU:{0}", estadoFinanciero.getFecuFormat()) ));
                }
            }
        }
        
        if(this.getMappingDetalleEstadoFinanciero().size() > 0){
            for(Map.Entry<DetalleEeff, Boolean> entry : this.getMappingDetalleEstadoFinanciero().get(key).entrySet()) {
                if(entry.getValue()){
                    final DetalleEeff detalleEstadoFinanciero = entry.getKey();
                    cuentasContablesByConcept.add(new SelectItem( detalleEstadoFinanciero, MessageFormat.format("[{0}]", detalleEstadoFinanciero.getCuentaContable().getIdCuenta()), MessageFormat.format("Cuenta Contable:{0}", detalleEstadoFinanciero.getCuentaContable().getIdCuenta()) ));
                }
            }
        }
        
    }
    
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
    
    public List<SelectItem> getFecuItems() {
        List<SelectItem> fecuItems = new ArrayList<SelectItem>();
        for (final EstadoFinanciero eeff : this.getEstadoFinancieroList()) {
            fecuItems.add(new SelectItem(eeff, EeffUtil.formatFecu(eeff.getIdFecu())));
        }
        return fecuItems;
    }
    
    public void codigoFecuChangeListener(ValueChangeEvent valueChangeEvent) {
        if(valueChangeEvent.getNewValue() != null){
            this.setDetalleEstadoFinancieroList(super.getFacade().getEstadoFinancieroService().getDetalleEeffByEeff((EstadoFinanciero)valueChangeEvent.getNewValue()));            
        }else{
            this.setDetalleEstadoFinancieroList(null);
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

    public void setTaxonomyModel(TreeModel taxonomyModel) {
        this.taxonomyModel = taxonomyModel;
    }

    public TreeModel getTaxonomyModel() {
        return taxonomyModel;
    }

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

    public void setMappingRichDecorativeBox(RichDecorativeBox mappingRichDecorativeBox) {
        this.mappingRichDecorativeBox = mappingRichDecorativeBox;
    }

    public RichDecorativeBox getMappingRichDecorativeBox() {
        return mappingRichDecorativeBox;
    }

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

    public void setViewMappingPanel(RichPanelGroupLayout viewMappingPanel) {
        this.viewMappingPanel = viewMappingPanel;
    }

    public RichPanelGroupLayout getViewMappingPanel() {
        return viewMappingPanel;
    }
}
