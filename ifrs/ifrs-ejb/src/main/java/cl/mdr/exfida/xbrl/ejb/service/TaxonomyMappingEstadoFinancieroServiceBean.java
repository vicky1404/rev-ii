package cl.mdr.exfida.xbrl.ejb.service;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.index;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static cl.mdr.ifrs.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import xbrlcore.taxonomy.Concept;
import cl.mdr.exfida.xbrl.ejb.entity.XbrlConceptoCodigoFecu;
import cl.mdr.exfida.xbrl.ejb.entity.XbrlTaxonomia;
import cl.mdr.exfida.xbrl.ejb.service.local.TaxonomyMappingEstadoFinancieroServiceLocal;
import cl.mdr.ifrs.ejb.entity.EstadoFinanciero;

@Stateless
public class TaxonomyMappingEstadoFinancieroServiceBean implements TaxonomyMappingEstadoFinancieroServiceLocal {

	@PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
	private EntityManager em;

	public TaxonomyMappingEstadoFinancieroServiceBean() {
		super();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void persistMappingTaxonomiaEstadoFinanciero(final XbrlTaxonomia xbrlTaxonomia,
			final Map<EstadoFinanciero, List<Concept>> mappingEstadoFinanciero) throws Exception {
		List<XbrlConceptoCodigoFecu> xbrlConceptoCodigoFecuList = new ArrayList<XbrlConceptoCodigoFecu>();
		for (Map.Entry<EstadoFinanciero, List<Concept>> entry : mappingEstadoFinanciero.entrySet()) {
			for (Concept concept : entry.getValue()) {
				xbrlConceptoCodigoFecuList.add(new XbrlConceptoCodigoFecu(concept.getId(), entry.getKey(), xbrlTaxonomia));
			}
		}

		em.createQuery(" delete from XbrlConceptoCodigoFecu o where o.xbrlTaxonomia.idTaxonomia =:idTaxonomia ")
				.setParameter("idTaxonomia", xbrlTaxonomia.getIdTaxonomia()).executeUpdate();

		Query nativeQuery = em
				.createNativeQuery("insert into IFRS_XBRL_CONCEPTO_CODIGO_FECU (ID_FECU, ID_VERSION_EEFF, ID_TAXONOMIA, ID_CONCEPTO_XBRL) values (?,?,?,?)");
		for (final XbrlConceptoCodigoFecu xbrlConceptoCodigoFecu : xbrlConceptoCodigoFecuList) {
			nativeQuery.setParameter(1, xbrlConceptoCodigoFecu.getIdFecu());
			nativeQuery.setParameter(2, xbrlConceptoCodigoFecu.getIdVersionEeff());
			nativeQuery.setParameter(3, xbrlConceptoCodigoFecu.getXbrlTaxonomia().getIdTaxonomia());
			nativeQuery.setParameter(4, xbrlConceptoCodigoFecu.getIdConceptoXbrl());
			nativeQuery.executeUpdate();
		}
	}

	/**
	 * @param xbrlTaxonomia
	 * @param concept
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deleteMappingByConceptoAndTaxonomia(final XbrlTaxonomia xbrlTaxonomia, final Concept concept) throws Exception {
		em.createQuery(
				" delete from XbrlConceptoCodigoFecu o " + " where o.xbrlTaxonomia.idTaxonomia =:idTaxonomia " + " and o.idConceptoXbrl =:idConceptoXbrl")
				.setParameter("idTaxonomia", xbrlTaxonomia.getIdTaxonomia()).setParameter("idConceptoXbrl", concept.getId()).executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Map<EstadoFinanciero, List<Concept>> buildMappingEstadoFinanciero(final XbrlTaxonomia xbrlTaxonomia, final List<Concept> conceptList,
			final List<EstadoFinanciero> estadoFinancieroList) throws Exception {

		Map<EstadoFinanciero, List<Concept>> mapping = new LinkedHashMap<EstadoFinanciero, List<Concept>>();
		Concept on = on(Concept.class);
		Map<String, Concept> taxonomyConceptMap = index(conceptList, on.getId());
		final List<XbrlConceptoCodigoFecu> xbrlConceptoCodigoFecuList = em
				.createQuery(
						"select o from XbrlConceptoCodigoFecu o " + "left join fetch o.estadoFinanciero e " + "left join fetch o.xbrlTaxonomia x "
								+ "left join fetch e.codigoFecu " + "left join fetch e.versionEeff v " + "left join fetch v.tipoEstadoEeff "
								+ "left join fetch v.periodoEmpresa p " + "left join fetch p.empresa  " + "left join fetch p.estadoPeriodo  "
								+ "left join fetch p.periodo  " + "where x.idTaxonomia =:idTaxonomia")
				.setParameter("idTaxonomia", xbrlTaxonomia.getIdTaxonomia()).getResultList();

		for (EstadoFinanciero eeff : estadoFinancieroList) {
			mapping.put(eeff, new ArrayList<Concept>());
		}

		for (XbrlConceptoCodigoFecu xbrlConceptoCodigoFecu : xbrlConceptoCodigoFecuList) {
			List<EstadoFinanciero> estadoFinancieroListFiltered = select(estadoFinancieroList,
					having(on(EstadoFinanciero.class).getIdFecu(), equalTo(xbrlConceptoCodigoFecu.getEstadoFinanciero().getIdFecu())));
			if (!estadoFinancieroListFiltered.isEmpty()) {
				EstadoFinanciero estadoFinanciero = null;
				estadoFinanciero = estadoFinancieroListFiltered.get(0);

				List<Concept> list = mapping.get(estadoFinanciero);
				list.add(taxonomyConceptMap.get(xbrlConceptoCodigoFecu.getIdConceptoXbrl()));
			}

		}

		return mapping;
	}

}
