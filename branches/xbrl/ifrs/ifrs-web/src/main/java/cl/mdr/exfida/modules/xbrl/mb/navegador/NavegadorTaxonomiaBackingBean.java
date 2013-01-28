package cl.mdr.exfida.modules.xbrl.mb.navegador;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.index;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static ch.lambdaj.Lambda.sort;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.hamcrest.Matchers;
import org.xml.sax.SAXException;

import xbrlcore.taxonomy.Concept;
import xbrlcore.taxonomy.ConceptTypes;
import xbrlcore.taxonomy.DiscoverableTaxonomySet;
import cl.mdr.exfida.xbrl.ejb.entity.XbrlTaxonomia;
import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.ejb.common.VigenciaEnum;


/**
 * @author Rodrigo Reyes C.
 * @link http://cl.linkedin.com/in/rreyesc
 * @since 10-08-2012
 */
@ManagedBean
@ViewScoped
public class NavegadorTaxonomiaBackingBean extends AbstractBackingBean implements Serializable  {
	private static final long serialVersionUID = 1185322549078766462L;	
	private final transient Logger logger = Logger.getLogger(this.getClass().getName());
                
    private DiscoverableTaxonomySet discoverableTaxonomySet;
    private List<XbrlTaxonomia> xbrlTaxonomias;
    private XbrlTaxonomia xbrlTaxonomiaSelected;
    private Long idXbrlTaxonomiaSelected;
    private Map<Long, XbrlTaxonomia> xbrlTaxonomiaMap;
    private List<SelectItem> xbrlTaxonomiasSelectItem;
    private boolean renderTaxonomia = false;    
    private List<String> importedTaxonomies;
    private List<Concept> taxonomyConceptList;
    private List<Concept> taxonomyConceptAllList;
    private Concept taxonomyConceptFilter;    
    private List<SelectItem> taxonomyConceptTypeItems = new ArrayList<SelectItem>();
   

    public NavegadorTaxonomiaBackingBean() {
        super();
    }
    
    @PostConstruct
    void init(){
    	try {
    		xbrlTaxonomiaSelected = new XbrlTaxonomia();        
			this.buildXbrlTaxonomiaMap();
		} catch (Exception e) {
			super.addErrorMessage("Se ha producido un error al Analizar los archivos de la Taxonomía");
            logger.error(e);
		}
    }
    

    /**
     * Metodo Action que ejecuta la visualizacion de una Taxonomía
     * segun los parametros obtenidos de la tabla REV_XBRL_TAXONOMIA
     * @return String
     */
    public void visualizarTaxonomiaAction(ActionEvent event){
        try {
        	this.setXbrlTaxonomiaSelected(this.getXbrlTaxonomiaMap().get(this.getIdXbrlTaxonomiaSelected()));
            this.setDiscoverableTaxonomySet(super.getFacadeService().getTaxonomyLoaderService().loadDiscoverableTaxonomySet(this.getXbrlTaxonomiaSelected().getUri()));            
            this.buildTaxonomyConcepts();
            this.buildImportedTaxonomies();
            this.buildTypeConceptsItem();
            this.setRenderTaxonomia(Boolean.TRUE);
        } catch (ParserConfigurationException e) {
            super.addErrorMessage("Se ha producido un error al Analizar los archivos de la Taxonomía");
            logger.error(e);
        } catch (SAXException e) {
            super.addErrorMessage("Se ha producido un error al Analizar los archivos de la Taxonomía");
            logger.error(e);
        } catch (IOException e) {
            super.addErrorMessage("No se ha encontrado el Archivo de la Taxonomía Correspondiente");
            logger.error(e);
        } catch (Exception e) {
            super.addErrorMessage("Se ha producido un error al Analizar los archivos de la Taxonomía");
            logger.error(e);
        }        
    }
    
    public void filterTaxonomyTable(ActionEvent event){
        List<Concept> filteredConcepts = null;
        filteredConcepts = select(this.getTaxonomyConceptAllList() ,having(on(Concept.class).getName(),  Matchers.startsWith(this.getTaxonomyConceptFilter().getId()) ));
        if(filteredConcepts == null || filteredConcepts.size() == 0){
            super.addWarnMessage("No se encontraron resultados para su criterio de búsqueda.");
            return;
        }
        this.setTaxonomyConceptList(filteredConcepts);        
    }
    
    public void cleanFilterTaxonomyTable(ActionEvent event){                
        this.setTaxonomyConceptList(this.getTaxonomyConceptAllList());        
    }
    
    private void buildTaxonomyConcepts(){        
        taxonomyConceptList = new ArrayList<Concept>();
        taxonomyConceptList.addAll(this.getDiscoverableTaxonomySet().getConcepts());
        Collections.sort(taxonomyConceptList, new Comparator<Concept>() {
        	
        	@Override
        	public int compare(Concept o1, Concept o2) {
        		return o1.getId().compareTo(o2.getId());
        	}
		});
        this.setTaxonomyConceptAllList(taxonomyConceptList);        
    }
    
    private void buildXbrlTaxonomiaMap() throws Exception{
    	xbrlTaxonomiaMap = index(this.getXbrlTaxonomias(), on(XbrlTaxonomia.class).getIdTaxonomia());
    }
    
    private void buildTypeConceptsItem(){
    	    	
    	ConceptTypes[] values = ConceptTypes.values();
    	
    	Arrays.sort(values,new  Comparator<ConceptTypes>(){
    		 @Override
    		public int compare(ConceptTypes o1, ConceptTypes o2) {
    			return o1.toString().compareTo(o2.toString());
    		}
    		
    	});
    	
    	
    	taxonomyConceptTypeItems = new ArrayList<SelectItem>();
    	taxonomyConceptTypeItems.add(new SelectItem("", "Seleccione"));
    	for(ConceptTypes type : values){
    		taxonomyConceptTypeItems.add(new SelectItem(type.toString(), type.toString()));
    	}
    }
               

    /**
     * @return
     * @throws Exception
     */
    public List<SelectItem> getXbrlTaxonomiasSelectItem() throws Exception {
        if(xbrlTaxonomiasSelectItem == null){
        	try{
	            xbrlTaxonomiasSelectItem = new ArrayList<SelectItem>();
	            for(XbrlTaxonomia taxonomia : this.getXbrlTaxonomias()){
	                xbrlTaxonomiasSelectItem.add(new SelectItem(taxonomia, taxonomia.getNombre()));
	            }
        	}catch (Exception e) {
				logger.error(e);
				super.addErrorMessage("Se ha producido un error al obtener el listado de Taxonomías disponibles.");
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
            xbrlTaxonomias = super.getFacadeService().getTaxonomyLoaderService().findTaxonomiasByFiltro(null, null, VigenciaEnum.VIGENTE.getKey());
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

    public void setXbrlTaxonomiaSelected(XbrlTaxonomia xbrlTaxonomiaSelected) {
        this.xbrlTaxonomiaSelected = xbrlTaxonomiaSelected;
    }

    public XbrlTaxonomia getXbrlTaxonomiaSelected() {
        return xbrlTaxonomiaSelected;
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

    public void setTaxonomyConceptFilter(Concept taxonomyConceptFilter) {
        this.taxonomyConceptFilter = taxonomyConceptFilter;
    }

    public Concept getTaxonomyConceptFilter() {
        return taxonomyConceptFilter;
    }
    
    public void setTaxonomyConceptAllList(List<Concept> taxonomyConceptAllList) {
        this.taxonomyConceptAllList = taxonomyConceptAllList;
    }

    public List<Concept> getTaxonomyConceptAllList() {
        return taxonomyConceptAllList;
    }
    
	public Long getIdXbrlTaxonomiaSelected() {
		return idXbrlTaxonomiaSelected;
	}

	public void setIdXbrlTaxonomiaSelected(Long idXbrlTaxonomiaSelected) {
		this.idXbrlTaxonomiaSelected = idXbrlTaxonomiaSelected;
	}

	public Map<Long, XbrlTaxonomia> getXbrlTaxonomiaMap() {
		return xbrlTaxonomiaMap;
	}

	public void setXbrlTaxonomiaMap(Map<Long, XbrlTaxonomia> xbrlTaxonomiaMap) {
		this.xbrlTaxonomiaMap = xbrlTaxonomiaMap;
	}

	public List<SelectItem> getTaxonomyConceptTypeItems() {
		return taxonomyConceptTypeItems;
	}

	public void setTaxonomyConceptTypeItems(List<SelectItem> taxonomyConceptTypeItems) {
		this.taxonomyConceptTypeItems = taxonomyConceptTypeItems;
	}
	
	
	

}
