package cl.mdr.exfida.xbrl.ejb.service;

import static ch.lambdaj.Lambda.index;
import static ch.lambdaj.Lambda.on;
import static cl.mdr.ifrs.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import xbrlcore.constants.GeneralConstants;
import xbrlcore.constants.NamespaceConstants;
import xbrlcore.dimensions.MultipleDimensionType;
import xbrlcore.exception.InstanceException;
import xbrlcore.instance.Fact;
import xbrlcore.instance.Instance;
import xbrlcore.instance.InstanceContext;
import xbrlcore.instance.InstanceUnit;
import xbrlcore.taxonomy.Concept;
import xbrlcore.taxonomy.DiscoverableTaxonomySet;
import cl.mdr.exfida.xbrl.ejb.entity.XbrlConceptoCelda;
import cl.mdr.exfida.xbrl.ejb.entity.XbrlConceptoCodigoFecu;
import cl.mdr.exfida.xbrl.ejb.entity.XbrlConceptoHtml;
import cl.mdr.exfida.xbrl.ejb.entity.XbrlTaxonomia;
import cl.mdr.exfida.xbrl.ejb.service.local.XbrlInstanceGeneratorServiceLocal;
import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.ifrs.ejb.entity.Periodo;

@Stateless
public class XbrlInstanceGeneratorServiceBean implements XbrlInstanceGeneratorServiceLocal {

	private static final Logger LOGGER = Logger.getLogger(XbrlInstanceGeneratorServiceBean.class);

	@PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
	private EntityManager em;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Instance generarInstancia(DiscoverableTaxonomySet dts, XbrlTaxonomia xbrlTaxonomia, String rut, Periodo periodo) {

		HashSet<DiscoverableTaxonomySet> hashSet = new HashSet<DiscoverableTaxonomySet>();
		hashSet.add(dts);

		Map<String, Concept> taxonomyConceptMap = index(dts.getConcepts(), on(Concept.class).getId());

		Instance instance = new Instance(hashSet);

		// TODO (xbrl): estos valores deberian ser configurables?
		instance.addNamespace("cl-cs", "http://www.svs.cl/cl/fr/cs/2012-10-05");
		instance.addNamespace(NamespaceConstants.ISO4217_NAMESPACE);

		try {
			// TODO (xbrl): Se debe implementar la logica de que por periodo se
			// deben informar N contextos definidos por la SVS
			InstanceContext contextoInstant = newContext("CierreUltimoTrimestrePeriodoActual", rut);
			contextoInstant.setPeriodValue(String.format("%s-%s-01", periodo.getAnioPeriodo(), periodo.getMesPeriodo()));
			instance.addContext(contextoInstant);

			// TODO (xbrl): Se debe implementar la logica de que por periodo se
			// deben informar N contextos definidos por la SVS
			InstanceContext contextoDuration = newContext("UltimoTrimestrePeriodoActual", rut);
			contextoDuration.setPeriodStartDate(String.format("%s-%02d-01", periodo.getAnioPeriodo(), Integer.parseInt(periodo.getMesPeriodo()) - 2));
			contextoDuration.setPeriodEndDate(String.format("%s-%s-01", periodo.getAnioPeriodo(), periodo.getMesPeriodo()));
			instance.addContext(contextoDuration);

			// TODO (xbrl): Se debe dejar esta implementacion como configurable?
			InstanceUnit unit = new InstanceUnit("CLP");
			unit.setValue("iso4217:CLP");
			unit.setNamespaceURI(NamespaceConstants.ISO4217_NAMESPACE.toString());
			instance.addUnit(unit);

			addEEFF(xbrlTaxonomia, taxonomyConceptMap, instance, unit, contextoInstant, contextoDuration);
			addNotasCelda(xbrlTaxonomia, taxonomyConceptMap, instance, unit, contextoInstant, contextoDuration, rut);
			addNotasHTML(xbrlTaxonomia, taxonomyConceptMap, instance, unit, contextoInstant, contextoDuration);

		} catch (Exception e) {
			LOGGER.error("Error al tratar de generar el xblr", e);
			throw new RuntimeException(e);
		}

		return instance;
	}

	private void addNotasHTML(XbrlTaxonomia xbrlTaxonomia, Map<String, Concept> taxonomyConceptMap, Instance instance, InstanceUnit unit,
			InstanceContext contextoInstant, InstanceContext contextoDuration) throws Exception {

		@SuppressWarnings("unchecked")
		final List<XbrlConceptoHtml> xbrlConceptoList = em
				.createQuery("select c from XbrlConceptoHtml c left join fetch c.xbrlTaxonomia x left join fetch c.html where x.idTaxonomia =:idTaxonomia")
				.setParameter("idTaxonomia", xbrlTaxonomia.getIdTaxonomia()).getResultList();
		HTML2Texto html2Texto = new HTML2Texto();
		for (XbrlConceptoHtml xbrlConceptoHtml : xbrlConceptoList) {
			Concept concept = taxonomyConceptMap.get(xbrlConceptoHtml.getIdConceptoXbrl());
			String contenidoStr = xbrlConceptoHtml.getHtml().getContenidoStr();
			html2Texto.parse(new StringReader(contenidoStr));
			Fact fact = createFact(unit, contextoInstant, contextoDuration, concept, html2Texto.getText());
			instance.addFact(fact);
		}

	}

	private void addEEFF(XbrlTaxonomia xbrlTaxonomia, Map<String, Concept> taxonomyConceptMap, Instance instance, InstanceUnit unit,
			InstanceContext contextoInstant, InstanceContext contextoDuration) throws InstanceException {
		@SuppressWarnings("unchecked")
		final List<XbrlConceptoCodigoFecu> xbrlConceptoCodigoFecuList = em
				.createQuery(
						"select o from XbrlConceptoCodigoFecu o " + "left join fetch o.estadoFinanciero e " + "left join fetch o.xbrlTaxonomia x "
								+ "left join fetch e.codigoFecu " + "left join fetch e.versionEeff v " + "left join fetch v.tipoEstadoEeff "
								+ "left join fetch v.periodoEmpresa p " + "left join fetch p.empresa  " + "left join fetch p.estadoPeriodo  "
								+ "left join fetch p.periodo  " + "where x.idTaxonomia =:idTaxonomia")
				.setParameter("idTaxonomia", xbrlTaxonomia.getIdTaxonomia()).getResultList();
		for (XbrlConceptoCodigoFecu xbrlConceptoCodigoFecu : xbrlConceptoCodigoFecuList) {

			Concept concept = taxonomyConceptMap.get(xbrlConceptoCodigoFecu.getIdConceptoXbrl());
			Object montoTotal = xbrlConceptoCodigoFecu.getEstadoFinanciero().getMontoTotal();
			Fact fact = createFact(unit, contextoInstant, contextoDuration, concept, montoTotal);

			instance.addFact(fact);

		}
	}

	private Fact createFact(InstanceUnit unit, InstanceContext contextoInstant, InstanceContext contextoDuration, Concept concept, Object montoTotal)
			throws InstanceException {
		Fact fact = new Fact(concept, montoTotal);

		if (GeneralConstants.CONTEXT_DURATION.equals(concept.getPeriodType())) {
			fact.setInstanceContext(contextoDuration);
		} else {
			fact.setInstanceContext(contextoInstant);
		}

		if (concept.isNumericItem()) {
			fact.setDecimals(-3);
			if (!fact.getValue().equals("0")) {
				fact.setValue(fact.getValue() + "000");
			}
			fact.setInstanceUnit(unit);
		}
		return fact;
	}

	@SuppressWarnings("unchecked")
	private void addNotasCelda(XbrlTaxonomia xbrlTaxonomia, Map<String, Concept> taxonomyConceptMap, Instance instance, InstanceUnit unit,
			InstanceContext contextoInstant, InstanceContext contextoDuration, String rut) throws InstanceException {

		Map<Celda, List<Concept>> mapping = new LinkedHashMap<Celda, List<Concept>>();

		final List<XbrlConceptoCelda> xbrlConceptoCeldaList = em
				.createQuery("select c from XbrlConceptoCelda c left join fetch c.xbrlTaxonomia x where x.idTaxonomia =:idTaxonomia")
				.setParameter("idTaxonomia", xbrlTaxonomia.getIdTaxonomia()).getResultList();
		for (XbrlConceptoCelda xbrlConceptoCelda : xbrlConceptoCeldaList) {
			List<Concept> list = mapping.get(xbrlConceptoCelda.getCelda());
			if (list == null) {
				list = new ArrayList<Concept>();
				mapping.put(xbrlConceptoCelda.getCelda(), list);
			}
			list.add(taxonomyConceptMap.get(xbrlConceptoCelda.getIdConceptoXbrl()));
		}

		Set<Entry<Celda, List<Concept>>> set = mapping.entrySet();

		for (Entry<Celda, List<Concept>> entry : set) {
			List<Concept> value = entry.getValue();

			Concept member = null;
			Concept eje = null;
			Concept row = null;
			for (Concept concept : value) {
				if (concept.isMember()) {
					member = concept;
				} else if (concept.isExplicitDimension()) {
					eje = concept;
				} else {
					row = concept;
				}
			}

			String prefix = GeneralConstants.CONTEXT_DURATION.equals(row.getPeriodType()) ? contextoDuration.getId() : contextoInstant.getId();

			if (eje != null && member != null) {
				InstanceContext contextDimension = null;

				contextDimension = newContext(prefix + "_" + eje.getName() + "_" + member.getName(), rut);
				contextDimension.setDimensionalInformation(new MultipleDimensionType(eje, member), GeneralConstants.DIM_SCENARIO);

				if (GeneralConstants.CONTEXT_DURATION.equals(row.getPeriodType())) {
					contextDimension.setPeriodStartDate(contextoDuration.getPeriodStartDate());
					contextDimension.setPeriodEndDate(contextoDuration.getPeriodEndDate());
				} else {
					contextDimension.setPeriodValue(contextoInstant.getPeriodValue());
				}

				Fact fact = new Fact(row, entry.getKey().getValor());
				fact.setInstanceContext(contextDimension);
				if (row.isNumericItem()) {
					fact.setDecimals(-3);
					if (!fact.getValue().equals("0")) {
						fact.setValue(fact.getValue() + "000");
					}
					fact.setInstanceUnit(unit);
				}

				instance.addFact(fact);

			} else {
				Fact fact = createFact(unit, contextoInstant, contextoDuration, row, entry.getKey().getValor());
				instance.addFact(fact);
			}

		}

	}

	private InstanceContext newContext(String idContext, String rut) {
		InstanceContext trimestreAcumulado = new InstanceContext(idContext);
		trimestreAcumulado.setIdentifier(rut);
		trimestreAcumulado.setIdentifierScheme("http://www.svs.cl/rut");
		return trimestreAcumulado;
	}

}
