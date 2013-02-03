package cl.mdr.exfida.modules.xbrl.mb.mapping;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import xbrlcore.taxonomy.sax.BuilderStatusCallback;
import xbrlcore.taxonomy.sax.FilterSaxBuilder;
import xbrlcore.taxonomy.sax.SAXBuilder;
import xbrlcore.xlink.Arc;
import xbrlcore.xlink.Locator;
import cl.mdr.exfida.modules.xbrl.model.ConceptTreeNode;
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
	private List<EstadoFinanciero> estadoFinancieroFilteredList;
	private List<DetalleEeff> detalleEstadoFinancieroList;
	private Map<String, Long[]> rangoCodigoFecuMap;

	// conceptos XBRL
	private DiscoverableTaxonomySet discoverableTaxonomySet;
	private List<Concept> taxonomyConceptList;

	// tree taxonomia
	private String parentTaxonomia;

	private TreeNode rootConcept;

	private String statusTaxnomy = "";
	private int progress;

	// bind de componentes
	/*
	 * private transient RichDecorativeBox mappingRichDecorativeBox; private
	 * transient RichPanelGroupLayout viewMappingPanel; private transient
	 * RichTree conceptoXbrlTree;
	 */

	// mapping
	private Concept conceptoSelectedForMapping;
	private Map<Concept, Map<EstadoFinanciero, Boolean>> mappingEstadoFinanciero;
	private Map<Concept, Map<DetalleEeff, Boolean>> mappingDetalleEstadoFinanciero;

	// display de elementos contenidos en el mapping
	private List<SelectItem> codigosFecuByConcept;
	private List<SelectItem> cuentasContablesByConcept;

	private int unsavedMappingsCount;

	public MapeadorTaxonomiaEstadosFinancierosBackingBean() {
	}

	@PostConstruct
	void init() {
		try {
			setPeriodoVigente(super.getFacadeService().getPeriodoService().findMaxPeriodoObj());
			Long idRut = getFiltroBackingBean().getEmpresa().getIdRut();
			EstadoFinancieroServiceLocal estadoFinancieroService = getFacadeService().getEstadoFinancieroService();
			Long idPeriodo = getPeriodoVigente().getIdPeriodo();
			List<EstadoFinanciero> eeffVigenteByPeriodo = estadoFinancieroService.getEeffVigenteByPeriodo(idPeriodo, idRut);
			setEstadoFinancieroList(eeffVigenteByPeriodo);

			TaxonomyMappingEstadoFinancieroServiceLocal taxonomyMappingEstadoFinancieroService = getFacadeService().getTaxonomyMappingEstadoFinancieroService();
			Map<String, Long[]> rangoCodigoFecuMap2 = taxonomyMappingEstadoFinancieroService.getRangoCodigoFecuMap();
			setRangoCodigoFecuMap(rangoCodigoFecuMap2);

			mappingEstadoFinanciero = new LinkedHashMap<Concept, Map<EstadoFinanciero, Boolean>>();
			// mappingDetalleEstadoFinanciero = new LinkedHashMap<Concept,
			// Map<DetalleEeff, Boolean>>();
		} catch (Exception e) {
			super.addErrorMessage("Se ha producido un error al inicializar los datos de la aplicación");
			logger.error(e);
		}

	}

	public void buscarTaxonomia(ActionEvent event) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					String uri = MapeadorTaxonomiaEstadosFinancierosBackingBean.super.getFiltroBackingBean().getXbrlTaxonomia().getUri();
					MapeadorTaxonomiaEstadosFinancierosBackingBean.this.setDiscoverableTaxonomySet(new SAXBuilder(new BuilderStatusCallback() {

						@Override
						public void startBuild() {
							statusTaxnomy = "Cargando la taxonomia top";
							progress = 1;
							logger.info(statusTaxnomy);
						}

						@Override
						public void loadingTaxonomy(String name) {
							statusTaxnomy = "Cargando la taxonomia " + name;
							progress = 40;
							logger.info(statusTaxnomy);
						}

						@Override
						public void loadingLinkBase(int total, int index, String name) {
							statusTaxnomy = String.format("Cargando el linkbase (%s de %s) : %s", index, total, name);
							progress = ((60 * index) / total) + 40;
							logger.info(statusTaxnomy);
						}

						@Override
						public void endBuild() {
							statusTaxnomy = "Fin de carga";
							progress = 100;
							logger.info(statusTaxnomy);
						}
					}, new FilterSaxBuilder() {
						
						@Override
						public boolean isParseable(String name) {
							return name.contains("eeff") || name.contains("cor");
						}
					}).build(new InputSource(uri)));
					
					buildTaxonomyTreeModel(discoverableTaxonomySet.getRoleTypesSorted().iterator().next().getRoleURI());
					buildTaxonomyConcepts();
					TaxonomyMappingEstadoFinancieroServiceLocal taxonomyMappingEstadoFinancieroService = getFacadeService()
							.getTaxonomyMappingEstadoFinancieroService();
					setMappingEstadoFinanciero(taxonomyMappingEstadoFinancieroService.buildMappingEstadoFinanciero(getFiltroBackingBean().getXbrlTaxonomia(),
							getTaxonomyConceptList(), getEstadoFinancieroList()));
					setRenderMappingPanel(true);

				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					MapeadorTaxonomiaEstadosFinancierosBackingBean.super.addErrorMessage(PropertyManager.getInstance().getMessage(
							"general_mensaje_nota_get_catalogo_error"));
				}

			}
		}).start();
	}

	public void fijarConceptoForMapping(ActionEvent event) {
		final Concept concept = (Concept) event.getComponent().getAttributes().get("concepto");
		// concept.setMapeado(Boolean.TRUE);
		setConceptoSelectedForMapping(concept);
		if (mappingEstadoFinanciero.get(getConceptoSelectedForMapping()) == null) {
			mappingEstadoFinanciero.put(getConceptoSelectedForMapping(), new LinkedHashMap<EstadoFinanciero, Boolean>());
		}
		/*
		 * if(mappingDetalleEstadoFinanciero.get(this.getConceptoSelectedForMapping
		 * ()) == null){
		 * mappingDetalleEstadoFinanciero.put(this.getConceptoSelectedForMapping
		 * (), new LinkedHashMap<DetalleEeff, Boolean>()); }
		 */
		desmarcarCodigosFecu();
		// this.desmarcarCuentasContables();
		updateMappingDisplay(getConceptoSelectedForMapping());
		// AdfFacesContext.getCurrentInstance().addPartialTarget(this.getViewMappingPanel());
		// AdfFacesContext.getCurrentInstance().addPartialTarget(this.getMappingRichDecorativeBox());
	}

	public void eliminarConceptoForMapping(ActionEvent event) {
		final Concept concept = (Concept) event.getComponent().getAttributes().get("concepto");
		// concept.setMapeado(Boolean.FALSE);
		try {
			getFacadeService().getTaxonomyMappingEstadoFinancieroService().deleteMappingByConceptoAndTaxonomia(super.getFiltroBackingBean().getXbrlTaxonomia(),
					concept);
			mappingEstadoFinanciero.remove(concept);
			desmarcarCodigosFecu();
			setCodigosFecuByConcept(null);
			// AdfFacesContext.getCurrentInstance().addPartialTarget(this.getViewMappingPanel());
			// AdfFacesContext.getCurrentInstance().addPartialTarget(this.getMappingRichDecorativeBox());
		} catch (Exception e) {
			logger.error(e.getCause(), e);
			// super.addErrorMessage(MessageFormat.format("Se ha producido un error al eliminar el Mapeo para el concepto {0}",
			// concept.getLabel()));
		}

	}

	public void guardarMapeo(ActionEvent event) {
		try {
			super.getFacadeService().getTaxonomyMappingEstadoFinancieroService()
					.persistMappingTaxonomiaEstadoFinanciero(super.getFiltroBackingBean().getXbrlTaxonomia(), getMappingEstadoFinanciero());
			super.addInfoMessage("Se han guardado los Mapeos correctamente");
			setUnsavedMappingsCount(0);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			super.addErrorMessage("Error al guardar los datos de Mapeo");
		}
	}

	public void clearPage(ActionEvent event) {
		setUnsavedMappingsCount(0);
		getMappingEstadoFinanciero().clear();
		setConceptoSelectedForMapping(null);
		setCodigosFecuByConcept(null);
		// AdfFacesContext.getCurrentInstance().addPartialTarget(this.getViewMappingPanel());
		// AdfFacesContext.getCurrentInstance().addPartialTarget(this.getMappingRichDecorativeBox());
	}

	public void selectCodigoFecuForMapping(ActionEvent event) {
		if (conceptoSelectedForMapping == null) {
			super.addWarnMessage("Antes de incluir un C�digo FECU en el Mapeo. debe seleccionar un Concepto desde la Taxonom�a XBRL.");
			return;
		}
		final EstadoFinanciero estadoFinanciero = (EstadoFinanciero) event.getComponent().getAttributes().get("estadoFinanciero");
		final boolean selection = (Boolean) event.getComponent().getAttributes().get("selected");
		if (selection) {
			getMappingEstadoFinanciero().get(getConceptoSelectedForMapping()).put(estadoFinanciero, Boolean.TRUE);
			estadoFinanciero.setSelectedForMapping(Boolean.TRUE);
		} else {
			getMappingEstadoFinanciero().get(getConceptoSelectedForMapping()).remove(estadoFinanciero);
			estadoFinanciero.setSelectedForMapping(Boolean.FALSE);
		}
		// this.setUnsavedMappingsCount(this.getMappingEstadoFinanciero().size()
		// + this.getMappingDetalleEstadoFinanciero().size());
		updateMappingDisplay(getConceptoSelectedForMapping());
		// AdfFacesContext.getCurrentInstance().addPartialTarget(this.getViewMappingPanel());
		// AdfFacesContext.getCurrentInstance().addPartialTarget(this.getMappingRichDecorativeBox());
	}

	@Deprecated
	public void selectCuentaContableForMapping(ActionEvent event) {
		if (conceptoSelectedForMapping == null) {
			super.addWarnMessage("Antes de incluir una Cuenta Contable en el Mapeo. debe seleccionar un Concepto desde la Taxonom�a XBRL.");
			return;
		}
		final DetalleEeff detalleEstadoFinanciero = (DetalleEeff) event.getComponent().getAttributes().get("detalleEstadoFinanciero");
		final boolean selection = (Boolean) event.getComponent().getAttributes().get("selected");
		if (selection) {
			getMappingDetalleEstadoFinanciero().get(getConceptoSelectedForMapping()).put(detalleEstadoFinanciero, Boolean.TRUE);
			detalleEstadoFinanciero.setSelectedForMapping(Boolean.TRUE);
		} else {
			getMappingDetalleEstadoFinanciero().get(getConceptoSelectedForMapping()).remove(detalleEstadoFinanciero);
			detalleEstadoFinanciero.setSelectedForMapping(Boolean.FALSE);
		}
		setUnsavedMappingsCount(getMappingEstadoFinanciero().size() + getMappingDetalleEstadoFinanciero().size());
		updateMappingDisplay(getConceptoSelectedForMapping());
		// AdfFacesContext.getCurrentInstance().addPartialTarget(this.getViewMappingPanel());
		// AdfFacesContext.getCurrentInstance().addPartialTarget(this.getMappingRichDecorativeBox());
	}

	/**
     */
	public void desmarcarCodigosFecu() {
		for (EstadoFinanciero estadoFinanciero : getEstadoFinancieroList()) {
			estadoFinanciero.setSelectedForMapping(Boolean.FALSE);
		}
	}

	/**
     */
	public void desmarcarCuentasContables() {
		for (DetalleEeff detalleEstadoFinanciero : getDetalleEstadoFinancieroList()) {
			detalleEstadoFinanciero.setSelectedForMapping(Boolean.FALSE);
		}
	}

	public void updateMappingDisplay(final Concept key) {
		codigosFecuByConcept = new ArrayList<SelectItem>();
		// cuentasContablesByConcept = new ArrayList<SelectItem>();
		if (getMappingEstadoFinanciero().size() > 0) {
			for (Map.Entry<EstadoFinanciero, Boolean> entry : getMappingEstadoFinanciero().get(key).entrySet()) {
				if (entry.getValue()) {
					EstadoFinanciero estadoFinanciero = entry.getKey();
					estadoFinanciero.setSelectedForMapping(Boolean.TRUE);
					codigosFecuByConcept.add(new SelectItem(estadoFinanciero, MessageFormat.format("[{0}]", estadoFinanciero.getFecuFormat()), MessageFormat
							.format("C�digo FECU: {0} - {1}", estadoFinanciero.getFecuFormat(), estadoFinanciero.getCodigoFecu().getDescripcion())));
				}
			}
		}

	}

	/**
	 * Metodo que construye una estructura de navegacion tipo Arbol con una DTS
	 * obtenida desde los archivos .xsd de la Taxonom�a.
	 */
	public void buildTaxonomyTreeModel(String roleURI) throws Exception {

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

			mapLevels.put(level, new DefaultTreeNode(new ConceptTreeNode(concept, label), parent));

		}

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
			buildTaxonomyTreeModel(getParentTaxonomia());
			/*
			 * if (conceptoXbrlTree != null &&
			 * conceptoXbrlTree.getDisclosedRowKeys()!=null ){
			 * conceptoXbrlTree.getDisclosedRowKeys().clear();
			 * conceptoXbrlTree.setInitiallyExpanded(Boolean.TRUE);
			 * AdfFacesContext
			 * .getCurrentInstance().addPartialTarget(conceptoXbrlTree); }
			 */
			// Long[] rango =
			// this.getRangoCodigoFecuMap().get(this.getParentTaxonomia());
			// this.setEstadoFinancieroFilteredList(select(
			// this.getEstadoFinancieroList(),
			// having(on(EstadoFinanciero.class).getIdFecu(),
			// greaterThanOrEqualTo(rango[0])).and(
			// having(on(EstadoFinanciero.class).getIdFecu(),
			// lessThanOrEqualTo(rango[1])))));

		} catch (Exception e) {
			super.addErrorMessage("Se ha producido un error al consultar los conceptos de XBRL.");
			logger.error(e);
			e.printStackTrace();
		}
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

	public String getStatusTaxnomy() {
		return statusTaxnomy;
	}

	public void setStatusTaxnomy(String statusTaxnomy) {
		this.statusTaxnomy = statusTaxnomy;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public TreeNode getRootConcept() {
		return rootConcept;
	}

	public void setRootConcept(TreeNode rootConcept) {
		this.rootConcept = rootConcept;
	}

}
