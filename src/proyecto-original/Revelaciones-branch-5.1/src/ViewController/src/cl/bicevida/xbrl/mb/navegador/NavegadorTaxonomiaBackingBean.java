package cl.bicevida.xbrl.mb.navegador;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;

import cl.bicevida.revelaciones.common.mb.SoporteBackingBean;
import cl.bicevida.revelaciones.ejb.common.VigenciaEnum;
import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.revelaciones.ejb.entity.Catalogo;
import cl.bicevida.revelaciones.ejb.entity.Periodo;
import cl.bicevida.revelaciones.ejb.entity.TipoCuadro;
import cl.bicevida.xbrl.ejb.entity.XbrlTaxonomia;
import cl.bicevida.xbrl.model.TaxonomyTreeItem;

import java.io.IOException;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import javax.xml.parsers.ParserConfigurationException;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.data.RichTreeTable;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.QueryEvent;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.model.ChildPropertyTreeModel;
import org.apache.myfaces.trinidad.model.TreeModel;

import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.equalTo;

import org.hamcrest.text.StringContains;

import org.xml.sax.SAXException;

import xbrlcore.constants.GeneralConstants;

import xbrlcore.linkbase.LabelLinkbase;
import xbrlcore.linkbase.PresentationLinkbaseElement;

import xbrlcore.taxonomy.Concept;
import xbrlcore.taxonomy.DiscoverableTaxonomySet;
import xbrlcore.taxonomy.TaxonomySchema;

public class NavegadorTaxonomiaBackingBean extends SoporteBackingBean implements Serializable {
    private final transient Logger logger = Logger.getLogger(this.getClass().getName());
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    private transient ArrayList<TaxonomyTreeItem> taxonomyRoot;
    private transient TreeModel taxonomyModel = null;
    private transient Object taxonomyInstance = null;    
    private DiscoverableTaxonomySet discoverableTaxonomySet;
    private List<XbrlTaxonomia> xbrlTaxonomias;    
    private List<SelectItem> xbrlTaxonomiasSelectItem;
    private boolean renderTaxonomia = false;    
    private List<String> importedTaxonomies;
    private List<Concept> taxonomyConceptList;
    private List<Concept> taxonomyConceptAllList;
    private Concept taxonomyConceptFilter;
    private transient RichTable taxonomyTable;
    private List<String> taxonomyConceptTypeList;
    
    private transient RichTreeTable taxonomyTreeTable;

    public NavegadorTaxonomiaBackingBean() {
        super();
    }
    
    @PostConstruct
    void init(){
        taxonomyConceptFilter = new Concept();
    }
    

    /**
     * Metodo Action que ejecuta la visualizacion de una Taxonom�a
     * segun los parametros obtenidos de la tabla REV_XBRL_TAXONOMIA
     * @return String
     */
    public String visualizarTaxonomiaAction(){
        try {
            if(taxonomyTreeTable != null && this.isRenderTaxonomia()){
                this.getTaxonomyTreeTable().getDisclosedRowKeys().clear();
            }
            this.setDiscoverableTaxonomySet(super.getFacade().getTaxonomyLoaderService().loadDiscoverableTaxonomySet(super.getFiltro().getXbrlTaxonomia().getUri()));
            this.buildTaxonomyTreeModel();
            //this.buildTaxonomyConcepts();
            this.buildImportedTaxonomies();
            this.setRenderTaxonomia(Boolean.TRUE);
        } catch (ParserConfigurationException e) {
            super.agregarErrorMessage("Se ha producido un error al Analizar los archivos de la Taxonom�a");
            logger.error(e);
            e.printStackTrace();
        } catch (SAXException e) {
            super.agregarErrorMessage("Se ha producido un error al Analizar los archivos de la Taxonom�a");
            logger.error(e);
            e.printStackTrace();
        } catch (IOException e) {
            super.agregarErrorMessage("No se ha encontrado el Archivo de la Taxonom�a Correspondiente");
            logger.error(e);
            e.printStackTrace();
        } catch (Exception e) {
            super.agregarErrorMessage("Se ha producido un error al Analizar los archivos de la Taxonom�a");
            logger.error(e);
            e.printStackTrace();
        }
        return null;
    }
    
    public void filterTaxonomyTable(ActionEvent event){
        List<Concept> filteredConcepts = null;        
        //filteredConcepts = select(this.getTaxonomyConceptAllList() ,having(on(Concept.class).getName(),  StringContains.containsString( (this.getTaxonomyConceptFilter().getId()) ) ));
        filteredConcepts = select(this.getTaxonomyConceptAllList() ,having(on(Concept.class).getName(),  Matchers.startsWith(this.getTaxonomyConceptFilter().getId()) ));
        if(filteredConcepts == null || filteredConcepts.size() == 0){
            super.agregarWarnMessage("No se encontraron resultados para su criterio de b�squeda.");
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
     * Metodo que construye una estructura de navegacion tipo Arbol
     * con una DTS obtenida desde los archivos .xsd de la Taxonom�a.
     */
    public void buildTaxonomyTreeModel() throws Exception { 
        logger.info("Obteniendo Taxonom�a desde file Server:"+super.getFiltro().getXbrlTaxonomia().getUri());
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
        
        this.setListInstance(taxonomyRoot);
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
    public void setListInstance(List instance) {
        this.taxonomyInstance = instance;
        taxonomyModel = null;
    }

    /**
     * @return
     * @throws Exception
     */
    public List<SelectItem> getXbrlTaxonomiasSelectItem() throws Exception {
        if(xbrlTaxonomiasSelectItem == null){
            xbrlTaxonomiasSelectItem = new ArrayList<SelectItem>();
            for(XbrlTaxonomia taxonomia : this.getXbrlTaxonomias()){
                xbrlTaxonomiasSelectItem.add(new SelectItem(taxonomia, taxonomia.getNombre()));
            }
        }
        return xbrlTaxonomiasSelectItem;
    }

    /**
     * @return
     * @throws Exception
     */
    public List<XbrlTaxonomia> getXbrlTaxonomias() throws Exception {
        if(xbrlTaxonomias == null){
            xbrlTaxonomias = super.getFacade().getTaxonomyLoaderService().findTaxonomiasByFiltro(null, null, VigenciaEnum.VIGENTE.getKey());
        }
        return xbrlTaxonomias;
    }
    
    public void buildImportedTaxonomies(){
        List<String> importedTaxonomies = new ArrayList<String>();
        for(String name : this.getDiscoverableTaxonomySet().getTopTaxonomy().getImportedTaxonomyNames()){
            importedTaxonomies.add(name);
        }
        Collections.sort(importedTaxonomies);
        this.setImportedTaxonomies(importedTaxonomies);
    }
    
    public void setDiscoverableTaxonomySet(DiscoverableTaxonomySet discoverableTaxonomySet) {
        this.discoverableTaxonomySet = discoverableTaxonomySet;
    }
    
    public DiscoverableTaxonomySet getDiscoverableTaxonomySet(){        
        return discoverableTaxonomySet;
    }

    public void setXbrlTaxonomias(List<XbrlTaxonomia> xbrlTaxonomias) {
        this.xbrlTaxonomias = xbrlTaxonomias;
    }
    
    public void setXbrlTaxonomiasSelectItem(List<SelectItem> xbrlTaxonomiasSelectItem){        
        this.xbrlTaxonomiasSelectItem = xbrlTaxonomiasSelectItem;
    }
    
    public void setRenderTaxonomia(boolean renderTaxonomia) {
        this.renderTaxonomia = renderTaxonomia;
    }

    public boolean isRenderTaxonomia() {
        return renderTaxonomia;
    }

    public void setImportedTaxonomies(List<String> importedTaxonomies) {
        this.importedTaxonomies = importedTaxonomies;
    }

    public List<String> getImportedTaxonomies() {
        return importedTaxonomies;
    }

    public void setTaxonomyConceptList(List<Concept> taxonomyConceptList) {
        this.taxonomyConceptList = taxonomyConceptList;
    }

    public List<Concept> getTaxonomyConceptList() {
        return taxonomyConceptList;
    }

    public void filterTaxonomyConcepts(QueryEvent queryEvent) {
        // Add event code here...
    }

    public void setTaxonomyConceptFilter(Concept taxonomyConceptFilter) {
        this.taxonomyConceptFilter = taxonomyConceptFilter;
    }

    public Concept getTaxonomyConceptFilter() {
        return taxonomyConceptFilter;
    }

    public void setTaxonomyTable(RichTable taxonomyTable) {
        this.taxonomyTable = taxonomyTable;
    }

    public RichTable getTaxonomyTable() {
        return taxonomyTable;
    }

    public void setTaxonomyConceptAllList(List<Concept> taxonomyConceptAllList) {
        this.taxonomyConceptAllList = taxonomyConceptAllList;
    }

    public List<Concept> getTaxonomyConceptAllList() {
        return taxonomyConceptAllList;
    }

    public void setTaxonomyConceptTypeList(List<String> taxonomyConceptTypeList) {
        this.taxonomyConceptTypeList = taxonomyConceptTypeList;
    }

    public List<String> getTaxonomyConceptTypeList() {
        return taxonomyConceptTypeList;
    }

    public void setTaxonomyTreeTable(RichTreeTable taxonomyTreeTable) {
        this.taxonomyTreeTable = taxonomyTreeTable;
    }

    public RichTreeTable getTaxonomyTreeTable() {
        return taxonomyTreeTable;
    }
}
