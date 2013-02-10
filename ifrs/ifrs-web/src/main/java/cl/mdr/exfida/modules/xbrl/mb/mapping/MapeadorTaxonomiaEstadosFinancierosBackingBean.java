package cl.mdr.exfida.modules.xbrl.mb.mapping;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.xml.sax.InputSource;

import xbrlcore.constants.GeneralConstants;
import xbrlcore.linkbase.LabelLinkbase;
import xbrlcore.linkbase.PresentationLinkbase;
import xbrlcore.linkbase.PresentationLinkbaseElement;
import xbrlcore.taxonomy.Concept;
import xbrlcore.taxonomy.DiscoverableTaxonomySet;
import xbrlcore.taxonomy.RoleType;
import xbrlcore.taxonomy.sax.DefaultStatusCallback;
import xbrlcore.taxonomy.sax.FilterSaxBuilder;
import xbrlcore.taxonomy.sax.SAXBuilder;
import xbrlcore.xlink.Arc;
import xbrlcore.xlink.Locator;
import cl.mdr.exfida.modules.xbrl.model.ConceptTreeNode;
import cl.mdr.exfida.modules.xbrl.model.EEFFConceptMapping;
import cl.mdr.exfida.xbrl.ejb.service.local.TaxonomyMappingEstadoFinancieroServiceLocal;
import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.mb.PropertyManager;
import cl.mdr.ifrs.ejb.cross.EeffUtil;
import cl.mdr.ifrs.ejb.entity.DetalleEeff;
import cl.mdr.ifrs.ejb.entity.EstadoFinanciero;
import cl.mdr.ifrs.ejb.entity.Periodo;
import cl.mdr.ifrs.ejb.service.local.EstadoFinancieroServiceLocal;

@ManagedBean(name = "mapeadorTaxonomiaEstadosFinancierosBackingBean")
@ViewScoped
public class MapeadorTaxonomiaEstadosFinancierosBackingBean extends AbstractBackingBean implements Serializable {
	private transient final Logger logger = Logger.getLogger(MapeadorTaxonomiaEstadosFinancierosBackingBean.class);

	private static final long serialVersionUID = -6638283037052343681L;

	// atributos generales
	private boolean renderMappingPanel;
	private Periodo periodoVigente;
	private EstadoFinanciero estadoFinanciero;
	private List<EstadoFinanciero> estadoFinancieroList;
	
	private List<DetalleEeff> detalleEstadoFinancieroList;
	private Map<String, String[]> rangoCodigoFecuMap = new HashMap<String, String[]>();

	// conceptos XBRL
	private DiscoverableTaxonomySet discoverableTaxonomySet;
	private List<Concept> taxonomyConceptList;

	// tree taxonomia
	private String parentTaxonomia;

	private TreeNode rootConcept;

	private Map<EstadoFinanciero, List<Concept>> mappingEstadoFinanciero;

	// display de elementos contenidos en el mapping
	private List<SelectItem> codigosFecuByConcept;
	private List<SelectItem> cuentasContablesByConcept;

	private int unsavedMappingsCount;
	
	private List<EEFFConceptMapping> mappings;
	private List<EEFFConceptMapping> mappingFilter;
	
	private EEFFConceptMapping selectedconceptMapping;
	
	private TreeNode[] selectedConcepts; 

	public MapeadorTaxonomiaEstadosFinancierosBackingBean() {
	}

	@PostConstruct
	void init() {
		try {
			setPeriodoVigente(super.getFacadeService().getPeriodoService().findMaxPeriodoObj());
			Long idRut = getFiltroBackingBean().getEmpresa().getIdRut();
			EstadoFinancieroServiceLocal estadoFinancieroService = getFacadeService().getEstadoFinancieroService();
			Long idPeriodo = getPeriodoVigente().getIdPeriodo();
			List<EstadoFinanciero> eeffVigenteByPeriodo = estadoFinancieroService.getEeffVigenteXBRLMappingByPeriodo(idPeriodo, idRut);
			estadoFinancieroList = eeffVigenteByPeriodo;
			mappingEstadoFinanciero = new LinkedHashMap<EstadoFinanciero, List<Concept>>();

		} catch (Exception e) {
			super.addErrorMessage("Se ha producido un error al inicializar los datos de la aplicación");
			logger.error(e.toString(), e);
		}

	}

	public void generarMappingAutomatico() {

		Set<RoleType> roleTypesSorted = discoverableTaxonomySet.getRoleTypesSorted();
		PresentationLinkbase presentationLinkbase = discoverableTaxonomySet.getPresentationLinkbase();
		for (RoleType roleType : roleTypesSorted) {
			List<PresentationLinkbaseElement> presentationListByLinkRole = presentationLinkbase.getPresentationListByLinkRole(roleType.getRoleURI());

			for (EstadoFinanciero estadoFinanciero : estadoFinancieroList) {

				FOR_CONCEP: for (PresentationLinkbaseElement presentationLinkbaseElement : presentationListByLinkRole) {
					Concept concept = presentationLinkbaseElement.getConcept();
					String codigo = concept.getAttrib("codigo");
					if (codigo != null && !codigo.isEmpty()) {

						if (codigo.equals(estadoFinanciero.getFecuFormat())) {
							List<Concept> list = mappingEstadoFinanciero.get(estadoFinanciero);
							if (list == null) {
								list = new ArrayList<Concept>();
							}
							list.add(concept);
							estadoFinanciero.setSelectedForMapping(true);
							mappingEstadoFinanciero.put(estadoFinanciero, list);
							break FOR_CONCEP;
						}
					}

				}
			}

		}
	}

	public void buscarTaxonomia(ActionEvent event) {

		try {

			String uri = getFiltroBackingBean().getXbrlTaxonomia().getUri();
			setDiscoverableTaxonomySet(new SAXBuilder(new DefaultStatusCallback(), new FilterSaxBuilder() {
				@Override
				public boolean isParseable(String name) {
					return name.contains("eeff") || name.contains("cor");
				}
			}).build(new InputSource(uri)));

			String taxRoleURI = discoverableTaxonomySet.getRoleTypesSorted().iterator().next().getRoleURI();
			buildTaxonomyTreeModel(taxRoleURI);
			buildTaxonomyConcepts();
			TaxonomyMappingEstadoFinancieroServiceLocal taxonomyMappingEstadoFinancieroService = getFacadeService().getTaxonomyMappingEstadoFinancieroService();
			this.mappingEstadoFinanciero = taxonomyMappingEstadoFinancieroService.buildMappingEstadoFinanciero(getFiltroBackingBean().getXbrlTaxonomia(),
					getTaxonomyConceptList(), getEstadoFinancieroList());
			setRenderMappingPanel(true);
			
			creaMapping(mappingEstadoFinanciero);
			
			filtrarEstadosFinancieros(taxRoleURI);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			MapeadorTaxonomiaEstadosFinancierosBackingBean.super.addErrorMessage(PropertyManager.getInstance().getMessage(
					"general_mensaje_nota_get_catalogo_error"));
		}

	}

	private void creaMapping(Map<EstadoFinanciero, List<Concept>> mappingEstadoFinanciero2) {
		this.mappings = new ArrayList<EEFFConceptMapping>();
		for (Entry<EstadoFinanciero, List<Concept>> mapp : mappingEstadoFinanciero2.entrySet()) {
			EEFFConceptMapping e = new EEFFConceptMapping();
			e.setConcepts(mapp.getValue());
			e.setEstadoFinanciero(mapp.getKey());
			mappings.add(e);
		}
		
	}

	

	public void eliminarConceptoForMapping(ActionEvent event) {
		final Concept concept = (Concept) event.getComponent().getAttributes().get("concepto");
		try {
			getFacadeService().getTaxonomyMappingEstadoFinancieroService().deleteMappingByConceptoAndTaxonomia(super.getFiltroBackingBean().getXbrlTaxonomia(),
					concept);
			mappingEstadoFinanciero.remove(concept);
		} catch (Exception e) {
			logger.error(e.getCause(), e);
			super.addErrorMessage("Se ha producido un error al eliminar el Mapeo para el concepto");
		}

	}

	public void guardarMapeo(ActionEvent event) {
		try {
			super.getFacadeService().getTaxonomyMappingEstadoFinancieroService().persistMappingTaxonomiaEstadoFinanciero(super.getFiltroBackingBean().getXbrlTaxonomia(), this.mappingEstadoFinanciero);
			super.addInfoMessage("Se han guardado los Mapeos correctamente");
			setUnsavedMappingsCount(0);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			super.addErrorMessage("Error al guardar los datos de Mapeo");
		}
	}

	

	public void mapearConceptosEEFF(ActionEvent event) {
		if (selectedconceptMapping == null || selectedConcepts == null || selectedConcepts.length == 0) {
			super.addWarnMessage("Debe seleccionar un o varios Concepto desde la Taxonomía XBRL y un Codigo FECU");
			return;
		}
		
		List<Concept> list = mappingEstadoFinanciero.get(selectedconceptMapping.getEstadoFinanciero());
		
		for (TreeNode node : selectedConcepts) {
			
			ConceptTreeNode data = (ConceptTreeNode) node.getData();
			if(!data.getConcept().isAbstract() || data.getConcept().getTypeString().equals("nonnum:domainItemType")){
				if(!list.contains(data.getConcept())){
					list.add(data.getConcept());	
				}
			}else{
				super.addWarnMessage("El Concepto " + data.getConcept().getId() +  " no se puede mapear con un valor.");
				return;	
			}
				
		}
		
		filtrarEstadosFinancieros(parentTaxonomia);

	}
	
	public void quitarConceptosEEFF(ActionEvent event) {
		if (selectedconceptMapping == null ) {
			super.addWarnMessage("Debe seleccionar un o varios Concepto desde la Taxonomía XBRL y un Codigo FECU");
			return;
		}
		
		List<Concept> list = mappingEstadoFinanciero.get(selectedconceptMapping.getEstadoFinanciero());
		
		if(selectedConcepts == null || selectedConcepts.length == 0){
			list.clear();
		}else{
			for (TreeNode node : selectedConcepts) {
				
				ConceptTreeNode data = (ConceptTreeNode) node.getData();
				if(list.contains(data.getConcept())){
					list.remove(data.getConcept());	
				}
					
			}	
		}
		
		
		
		filtrarEstadosFinancieros(parentTaxonomia);

	}



	

	/**
	 * Metodo que construye una estructura de navegacion tipo Arbol con una DTS
	 * obtenida desde los archivos .xsd de la Taxonom�a.
	 */
	public void buildTaxonomyTreeModel(String roleURI) throws Exception {

		String max = "";
		String min = "";

		final DiscoverableTaxonomySet dts = getDiscoverableTaxonomySet();
		final LabelLinkbase labelLinkbase = getDiscoverableTaxonomySet().getLabelLinkbase();
		final PresentationLinkbase presentationLinkbase = getDiscoverableTaxonomySet().getPresentationLinkbase();

		rootConcept = new DefaultTreeNode("root", null);

		RoleType roleType = dts.getRoleTypes().get(roleURI);

		TreeNode nodo = new DefaultTreeNode(new ConceptTreeNode(roleType, roleType.getFirstDefinition()), rootConcept);

		Map<Integer, TreeNode> mapLevels = new HashMap<Integer, TreeNode>();

		List<PresentationLinkbaseElement> presentationListByLinkRole = presentationLinkbase.getPresentationListByLinkRole(roleType.getRoleURI());
		for (PresentationLinkbaseElement presentationLinkbaseElement : presentationListByLinkRole) {
			Concept concept = presentationLinkbaseElement.getConcept();
			String label = null;
			String preferredLabel = null;
			Locator locator = presentationLinkbaseElement.getLocator();
			Arc arcForSourceLocator = presentationLinkbase.getArcForSourceLocator(locator);
			if (arcForSourceLocator != null) {
				preferredLabel = arcForSourceLocator.getPreferredLabel();
			}

			if (preferredLabel == null) {
				Arc arcForTargeLocator = presentationLinkbase.getArcForTargetLocator(locator);
				if (arcForTargeLocator != null) {
					preferredLabel = arcForTargeLocator.getPreferredLabel();
				}
			}

			if (preferredLabel != null && !preferredLabel.isEmpty()) {
				label = labelLinkbase.getLabel(concept, preferredLabel);
			}

			if (label == null || label.isEmpty()) {
				label = labelLinkbase.getLabel(concept, GeneralConstants.XBRL_ROLE_LABEL);
			}

			int level = presentationLinkbaseElement.getLevel();
			TreeNode parent = null;
			if (level == 1) {
				parent = nodo;
			} else {
				parent = mapLevels.get(level - 1);
			}

			ConceptTreeNode data = new ConceptTreeNode(concept, label);
			mapLevels.put(level, new DefaultTreeNode(data, parent));

			String codigo = data.getCodigo();
			if (codigo != null) {
				if (min.equals("")) {
					min = codigo;
				} else {
					min = codigo.compareTo(min) < 0 ? codigo : min;
				}

				max = codigo.compareTo(max) > 0 ? codigo : max;
			}

		}
		logger.info("Max" + max);
		logger.info("Min" + min);
		rangoCodigoFecuMap.put(roleURI, new String[] { min, max });

	}

	public List<SelectItem> getFecuItems() {
		List<SelectItem> fecuItems = new ArrayList<SelectItem>();
		for (final EstadoFinanciero eeff : getEstadoFinancieroList()) {
			fecuItems.add(new SelectItem(eeff, EeffUtil.formatFecu(eeff.getIdFecu())));
		}
		return fecuItems;
	}

	public void codigoFecuChangeListener(ValueChangeEvent valueChangeEvent) {
		if (valueChangeEvent.getNewValue() != null) {
			setDetalleEstadoFinancieroList(super.getFacadeService().getEstadoFinancieroService()
					.getDetalleEeffByEeff((EstadoFinanciero) valueChangeEvent.getNewValue()));
		} else {
			setDetalleEstadoFinancieroList(null);
		}
	}

	public List<SelectItem> getTaxonomyParentItems() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		if (discoverableTaxonomySet != null) {
			Set<RoleType> roleTypesSorted = discoverableTaxonomySet.getRoleTypesSorted();
			for (RoleType roleType : roleTypesSorted) {
				items.add(new SelectItem(roleType.getRoleURI(), roleType.getFirstDefinition()));
			}
		}
		return items;
	}

	public void taxonomyParentChangeListener() {
		try {
			String selectedTaxonomia = getParentTaxonomia();
			buildTaxonomyTreeModel(selectedTaxonomia);
			filtrarEstadosFinancieros(selectedTaxonomia);

		} catch (Exception e) {
			super.addErrorMessage("Se ha producido un error al consultar los conceptos de XBRL.");
			logger.error(e.toString(), e);
		}
	}

	private void filtrarEstadosFinancieros(String selectedTaxonomia) {
		String[] rango = rangoCodigoFecuMap.get(selectedTaxonomia);
		
		mappingFilter = select(this.mappings, having(on(EEFFConceptMapping.class).getFecuFormat(), greaterThanOrEqualTo(rango[0]))
				.and(having(on(EEFFConceptMapping.class).getFecuFormat(), lessThanOrEqualTo(rango[1]))));
		
		
	}
	
	

	private void buildTaxonomyConcepts() {
		taxonomyConceptList = new ArrayList<Concept>();
		for (Concept concept : getDiscoverableTaxonomySet().getConcepts()) {
			taxonomyConceptList.add(concept);
		}
	}

	public void setDiscoverableTaxonomySet(DiscoverableTaxonomySet discoverableTaxonomySet) {
		this.discoverableTaxonomySet = discoverableTaxonomySet;
	}

	public List<SelectItem> getCodigosFecuByConcept() {
		return codigosFecuByConcept;
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

	public void setRenderMappingPanel(boolean renderMappingPanel) {
		this.renderMappingPanel = renderMappingPanel;
	}

	public boolean isRenderMappingPanel() {
		return renderMappingPanel;
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


	public void setUnsavedMappingsCount(int unsavedMappingsCount) {
		this.unsavedMappingsCount = unsavedMappingsCount;
	}

	public int getUnsavedMappingsCount() {
		return unsavedMappingsCount;
	}

	public void setCuentasContablesByConcept(List<SelectItem> cuentasContablesByConcept) {
		this.cuentasContablesByConcept = cuentasContablesByConcept;
	}

	public List<SelectItem> getCuentasContablesByConcept() {
		return cuentasContablesByConcept;
	}

	public void setParentTaxonomia(String parentTaxonomia) {
		this.parentTaxonomia = parentTaxonomia;
	}

	public String getParentTaxonomia() {
		return parentTaxonomia;
	}

	public TreeNode getRootConcept() {
		return rootConcept;
	}

	public void setRootConcept(TreeNode rootConcept) {
		this.rootConcept = rootConcept;
	}

	public List<EEFFConceptMapping> getMappingFilter() {
		return mappingFilter;
	}

	public TreeNode[] getSelectedConcepts() {
		return selectedConcepts;
	}

	public void setSelectedConcepts(TreeNode[] selectedConcepts) {
		this.selectedConcepts = selectedConcepts;
	}

	public EEFFConceptMapping getSelectedconceptMapping() {
		return selectedconceptMapping;
	}

	public void setSelectedconceptMapping(EEFFConceptMapping selectedconceptMapping) {
		this.selectedconceptMapping = selectedconceptMapping;
	}

	

	

}
