package cl.mdr.exfida.modules.xbrl.mb.mapping;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
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
import xbrlcore.taxonomy.sax.SAXBuilder;
import xbrlcore.xlink.Arc;
import xbrlcore.xlink.Locator;
import cl.mdr.exfida.modules.xbrl.model.ConceptTreeNode;
import cl.mdr.exfida.modules.xbrl.model.VersionEstructuraTreeNode;
import cl.mdr.exfida.xbrl.ejb.service.HTML2Texto;
import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.mb.PropertyManager;
import cl.mdr.ifrs.ejb.entity.Catalogo;
import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.ifrs.ejb.entity.Estructura;
import cl.mdr.ifrs.ejb.entity.Grilla;
import cl.mdr.ifrs.ejb.entity.Periodo;
import cl.mdr.ifrs.ejb.entity.TipoCuadro;
import cl.mdr.ifrs.ejb.entity.Version;
import cl.mdr.ifrs.vo.GrillaVO;

@ManagedBean(name = "mapeadorTaxonomiaNotasBackingBean")
@ViewScoped
public class MapeadorTaxonomiaNotasBackingBean extends AbstractBackingBean implements Serializable {
	private transient final Logger logger = Logger.getLogger(MapeadorTaxonomiaNotasBackingBean.class);

	private static final long serialVersionUID = -6638283037052343681L;

	// atributos generales
	private boolean renderMappingPanel;
	private boolean renderTreeEstructuras;

	private Periodo periodoVigente;

	// conceptos XBRL
	private DiscoverableTaxonomySet discoverableTaxonomySet;

	private static Map<String, DiscoverableTaxonomySet> mapDTS = new HashMap<String, DiscoverableTaxonomySet>();

	// tree taxonomia
	private String parentTaxonomia;

	private TreeNode rootConcept;

	private TreeNode root;

	private TreeNode selectedConcept;

	private List<Catalogo> catalogoList;

	private Estructura selectedEstructura;

	private GrillaVO grillaVO;

	private Celda celdaSeleccionda;

	private Map<Celda, List<Concept>> mapping;

	private List<Concept> mappingHTML;

	private boolean renderMappingGrilla = false;

	public MapeadorTaxonomiaNotasBackingBean() {
	}

	@PostConstruct
	void init() {
		try {
			// getFiltroPeriodoEmpresa().setPeriodo(new
			// Periodo(getComponenteBackingBean().getPeriodoActual()));
		} catch (Exception e) {
			super.addErrorMessage("Se ha producido un error al inicializar los datos de la aplicación");
			logger.error(e.toString(), e);
		}

	}

	public void seleccionarCelda() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, String> map = context.getExternalContext().getRequestParameterMap();

		String columna = map.get("columna");

		if ("false".equals(columna)) {
			celdaSeleccionda = new Celda();

			String id1 = map.get("id1");
			celdaSeleccionda.setIdColumna(Long.parseLong(id1));

			String id2 = map.get("id2");
			celdaSeleccionda.setIdGrilla(Long.parseLong(id2));

			String id3 = map.get("id3");
			celdaSeleccionda.setIdFila(Long.parseLong(id3));
		}

	}

	public void deSeleccionarCelda() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, String> map = context.getExternalContext().getRequestParameterMap();
		String id1 = map.get("id1");
		String id2 = map.get("id2");
		String columna = map.get("columna");

		System.out.println(id1 + " " + id2 + " " + columna);

	}

	public String buscarEstructurasAction() {
		try {
			getCatalogoTreeModel();
			renderTreeEstructuras = true;
		} catch (Exception e) {
			logger.error(e.getCause(), e);
			addErrorMessage("Error al consultar datos para realizar el proceso");
		}

		return null;
	}

	/**
	 * Genera una estructura de tipo TreeModel para Tabla de Catalogo con sus
	 * estructuras asociadas
	 */
	public void getCatalogoTreeModel() throws Exception {
		root = new DefaultTreeNode();

		// TODO: (xbrl) Checkear si la obtencion de versión es la correcta
		for (Version version : getFacadeService().getVersionService().findVersionAllByCatalogo(getFiltroBackingBean().getCatalogo())) {
			TreeNode nodo = new DefaultTreeNode(new VersionEstructuraTreeNode(version), root);

			for (Estructura estructura : super.getFacadeService().getEstructuraService().getEstructuraByVersion(version, false)) {
				new DefaultTreeNode(new VersionEstructuraTreeNode(estructura), nodo);
			}
		}

	}

	/**
	 * evento ValueChangeEvent del combo Tipo de Cuadro
	 * 
	 * @param valueChangeEvent
	 */
	public void onChangeTipoCuadro() {
		final TipoCuadro tipoCuadro = getFiltroBackingBean().getTipoCuadro();
		this.catalogoList = (select(super.getComponenteBackingBean().getCatalogoList(),
				having(on(Catalogo.class).getTipoCuadro().getIdTipoCuadro(), equalTo(tipoCuadro.getIdTipoCuadro()))));
	}

	public List<SelectItem> getCatalogoSelectItem() {
		List<SelectItem> lista = new ArrayList<SelectItem>();
		if (this.catalogoList != null && !this.catalogoList.isEmpty()) {
			for (Catalogo catalogo : this.catalogoList) {
				lista.add(new SelectItem(catalogo, catalogo.getNombre()));
			}
		}

		return lista;
	}

	public void buscarTaxonomyAndGrilla(ActionEvent event) {

		selectedEstructura = (Estructura) event.getComponent().getAttributes().get("estructura");
		try {

			renderMappingGrilla = selectedEstructura.getTipoEstructura().getIdTipoEstructura() == 0;

			// taxonomia

			String uri = getFiltroBackingBean().getXbrlTaxonomia().getUri();
			if (!mapDTS.containsKey(uri)) {
				discoverableTaxonomySet = new SAXBuilder().build(new InputSource(uri));
				mapDTS.put(uri, discoverableTaxonomySet);
			} else {
				discoverableTaxonomySet = mapDTS.get(uri);
			}

			parentTaxonomia = seleccionarPosibleTaxonomiaParaNota();
			buildTaxonomyTreeModel(parentTaxonomia);

			ArrayList<Concept> conceptList = new ArrayList<Concept>(discoverableTaxonomySet.getConcepts());

			// grilla
			if (renderMappingGrilla) {
				Grilla grilla = (getFacadeService().getGrillaService().findGrillaById(selectedEstructura.getIdEstructura()));
				grillaVO = (getFacadeService().getEstructuraService().getGrillaVO(grilla, false));

				setMapping((getFacadeService().getTaxonomyMappingRevelacionService().buildMappingByEstructura(selectedEstructura, conceptList)));
			} else {
				// html
				mappingHTML = getFacadeService().getTaxonomyMappingRevelacionService().buildMappingByEstructuraHTML(selectedEstructura, conceptList);

			}

			renderMappingPanel = true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			super.addErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_nota_get_catalogo_error"));
		}
	}

	public String getHtmlContenido() {
		String contenidoStr = selectedEstructura.getHtml().getContenidoStr();

		HTML2Texto html2Texto = new HTML2Texto();
		try {
			html2Texto.parse(new StringReader(contenidoStr));
		} catch (IOException e) {
			logger.error(e.getMessage(), e);

			super.addErrorMessage("Error al mostrar solo texto");
		}
		return html2Texto.getText();
	}

	private String seleccionarPosibleTaxonomiaParaNota() {
		Catalogo catalogo = getFiltroBackingBean().getCatalogo();

		Set<RoleType> roleTypesSorted = discoverableTaxonomySet.getRoleTypesSorted();
		String taxRoleURI = roleTypesSorted.iterator().next().getRoleURI();
		if (catalogo != null) {
			String nombre = catalogo.getNombre();
			if (nombre.contains(".")) {
				nombre = nombre.substring(0, nombre.indexOf("."));
			}

			for (RoleType roleType : roleTypesSorted) {
				if (roleType.getFirstDefinition().contains(nombre)) {
					return roleType.getRoleURI();
				}
			}
		}

		return taxRoleURI;
	}

	public void guardarMapeo(ActionEvent event) {
		try {
			if (renderMappingGrilla) {
				getFacadeService().getTaxonomyMappingRevelacionService().persistMappingTaxonomiaRevelacion(getFiltroBackingBean().getXbrlTaxonomia(), mapping);
			} else {
				getFacadeService().getTaxonomyMappingRevelacionService().persistMappingTaxonomiaRevelacionHTML(getFiltroBackingBean().getXbrlTaxonomia(),
						mappingHTML, selectedEstructura.getHtml());
			}

			addInfoMessage("Se han guardado los Mapeos correctamente");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			addErrorMessage("Error al guardar los datos de Mapeo");
		}
	}

	public void mapearConceptosNota(ActionEvent event) {

		if (selectedConcept == null) {
			super.addWarnMessage("Debe seleccionar unConcepto desde la Taxonomía XBRL");
			return;
		}

		if (renderMappingGrilla) {

			if (celdaSeleccionda == null || selectedConcept == null) {
				super.addWarnMessage("Debe seleccionar una celda");
				return;
			}

			mapearCelda();

		} else {
			if (mappingHTML == null) {
				mappingHTML = new ArrayList<Concept>();
			}
			ConceptTreeNode data = (ConceptTreeNode) selectedConcept.getData();
			if (!mappingHTML.contains(data.getConcept())) {
				mappingHTML.add(data.getConcept());
			}

		}

	}

	private void mapearCelda() {
		ConceptTreeNode data = (ConceptTreeNode) selectedConcept.getData();
		Concept concept = data.getConcept();
		if (concept.isMember()) {

			Concept eje = getEje(selectedConcept.getParent());

			Long idColumna = celdaSeleccionda.getIdColumna();

			List<Map<Long, Celda>> rows = grillaVO.getRows();
			for (Map<Long, Celda> map : rows) {
				Celda celda = map.get(idColumna);
				addConceptCelda(celda, concept);
				addConceptCelda(celda, eje);
			}

		} else if (!concept.isAbstract()) {
			Long idFila = celdaSeleccionda.getIdFila();
			Map<Long, Celda> map = grillaVO.getRows().get(idFila.intValue() - 1);
			Set<Entry<Long, Celda>> entrySet = map.entrySet();
			for (Entry<Long, Celda> entry : entrySet) {
				Celda celda = entry.getValue();
				addConceptCelda(celda, concept);
			}

		} else {
			super.addWarnMessage("El Concepto " + concept.getId() + " no se puede mapear con un valor.");
		}
	}

	private Concept getEje(TreeNode node) {
		ConceptTreeNode data = (ConceptTreeNode) node.getData();
		if (data.getConcept().isExplicitDimension()) {
			return data.getConcept();
		} else if (node.getParent() == null) {
			throw new RuntimeException("Member no tiene eje padre");
		} else {
			return getEje(node.getParent());
		}
	}

	private void addConceptCelda(Celda celda, Concept concept) {

		// TODO (xbrl): Validar que tipo de celda se puede mapear, por ejemplo
		// alguno totales no se mapean.

		List<Concept> list = mapping.get(celda);
		if (list == null) {
			list = new ArrayList<Concept>();
			mapping.put(celda, list);
		}
		if (!list.contains(concept)) {
			list.add(concept);
		}
	}

	public void quitarConceptosNotas(ActionEvent event) {

		if (renderMappingGrilla) {

			if (celdaSeleccionda == null) {
				super.addWarnMessage("Debe seleccionar una Celda");
				return;
			}

			List<Concept> list = mapping.get(celdaSeleccionda);
			if (list != null) {
				list.clear();
			}
		}else{
			if(mappingHTML != null){
				mappingHTML.clear();	
			}
			
		}

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
		nodo.setExpanded(true);

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
			DefaultTreeNode value = new DefaultTreeNode(data, parent);
			value.setExpanded(true);
			mapLevels.put(level, value);

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
		// rangoCodigoFecuMap.put(roleURI, new String[] { min, max });

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
		} catch (Exception e) {
			super.addErrorMessage("Se ha producido un error al consultar los conceptos de XBRL.");
			logger.error(e.toString(), e);
		}
	}

	public void setDiscoverableTaxonomySet(DiscoverableTaxonomySet discoverableTaxonomySet) {
		this.discoverableTaxonomySet = discoverableTaxonomySet;
	}

	public DiscoverableTaxonomySet getDiscoverableTaxonomySet() {
		return discoverableTaxonomySet;
	}

	public void setRenderMappingPanel(boolean renderMappingPanel) {
		this.renderMappingPanel = renderMappingPanel;
	}

	public boolean isRenderMappingPanel() {
		return renderMappingPanel;
	}

	public void setPeriodoVigente(Periodo periodoVigente) {
		this.periodoVigente = periodoVigente;
	}

	public Periodo getPeriodoVigente() {
		return periodoVigente;
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

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public boolean isRenderTreeEstructuras() {
		return renderTreeEstructuras;
	}

	public void setRenderTreeEstructuras(boolean renderTreeEstructuras) {
		this.renderTreeEstructuras = renderTreeEstructuras;
	}

	public Estructura getSelectedEstructura() {
		return selectedEstructura;
	}

	public void setSelectedEstructura(Estructura selectedEstructura) {
		this.selectedEstructura = selectedEstructura;
	}

	public GrillaVO getGrillaVO() {
		return grillaVO;
	}

	public void setGrillaVO(GrillaVO grillaVO) {
		this.grillaVO = grillaVO;
	}

	public TreeNode getSelectedConcept() {
		return selectedConcept;
	}

	public void setSelectedConcept(TreeNode selectedConcept) {
		this.selectedConcept = selectedConcept;
	}

	public Map<Celda, List<Concept>> getMapping() {
		return mapping;
	}

	public void setMapping(Map<Celda, List<Concept>> mapping) {
		this.mapping = mapping;
	}

	public boolean isRenderMappingGrilla() {
		return renderMappingGrilla;
	}

	public List<Concept> getMappingHTML() {
		return mappingHTML;
	}

}
