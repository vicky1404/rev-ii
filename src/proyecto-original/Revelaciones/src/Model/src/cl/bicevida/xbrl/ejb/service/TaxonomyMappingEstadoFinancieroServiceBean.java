package cl.bicevida.xbrl.ejb.service;

import static ch.lambdaj.Lambda.*;
import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.index;
import static ch.lambdaj.Lambda.on;
import static org.hamcrest.Matchers.*;

import static cl.bicevida.revelaciones.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;
import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.EstadoFinanciero;
import cl.bicevida.revelaciones.ejb.entity.Estructura;
import cl.bicevida.xbrl.ejb.entity.XbrlConceptoCelda;
import cl.bicevida.xbrl.ejb.entity.XbrlConceptoCodigoFecu;
import cl.bicevida.xbrl.ejb.entity.XbrlRangoCodigoFecu;
import cl.bicevida.xbrl.ejb.entity.XbrlTaxonomia;
import cl.bicevida.xbrl.ejb.service.local.TaxonomyMappingEstadoFinancieroServiceLocal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import java.util.Set;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import xbrlcore.taxonomy.Concept;

@Stateless
public class TaxonomyMappingEstadoFinancieroServiceBean implements TaxonomyMappingEstadoFinancieroServiceLocal {
    
    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;

    public TaxonomyMappingEstadoFinancieroServiceBean() {
        super();
    }
           
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistMappingTaxonomiaEstadoFinanciero(final XbrlTaxonomia xbrlTaxonomia, final Map<Concept, Map<EstadoFinanciero, Boolean>> mappingEstadoFinanciero) throws Exception {
        List<XbrlConceptoCodigoFecu> xbrlConceptoCodigoFecuList = new ArrayList<XbrlConceptoCodigoFecu>();
        for (Map.Entry<Concept, Map<EstadoFinanciero, Boolean>> entry : mappingEstadoFinanciero.entrySet()) {
            for (Map.Entry<EstadoFinanciero, Boolean> entryEstadoFinanciero : entry.getValue().entrySet()) {
                xbrlConceptoCodigoFecuList.add(new XbrlConceptoCodigoFecu(entry.getKey().getId(), entryEstadoFinanciero.getKey(), xbrlTaxonomia));
            }
        }
        
        em.createQuery(" delete from XbrlConceptoCodigoFecu o where o.xbrlTaxonomia.idTaxonomia =:idTaxonomia ")
          .setParameter("idTaxonomia", xbrlTaxonomia.getIdTaxonomia())
          .executeUpdate();
        
        for (final XbrlConceptoCodigoFecu xbrlConceptoCodigoFecu : xbrlConceptoCodigoFecuList) {
            em.persist(xbrlConceptoCodigoFecu);
        }
    }

    /**
     * @param xbrlTaxonomia
     * @param concept
     * @throws Exception
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteMappingByConceptoAndTaxonomia(final XbrlTaxonomia xbrlTaxonomia, final Concept concept) throws Exception{
        em.createQuery(" delete from XbrlConceptoCodigoFecu o " +
                       " where o.xbrlTaxonomia.idTaxonomia =:idTaxonomia " +
                       " and o.idConceptoXbrl =:idConceptoXbrl")
          .setParameter("idTaxonomia", xbrlTaxonomia.getIdTaxonomia())
          .setParameter("idConceptoXbrl", concept.getId())
          .executeUpdate();
    }

    /**
     * @return
     * @throws Exception
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Map<String, Long[]> getRangoCodigoFecuMap() throws Exception{
        Map<String, Long[]> rangoCodigoFecuMap = new HashMap<String, Long[]>();
        List<XbrlRangoCodigoFecu> rangoCodigoFecuList = em.createNamedQuery(XbrlRangoCodigoFecu.FIND_ALL).getResultList();
        for(XbrlRangoCodigoFecu xbrlRangoCodigoFecu : rangoCodigoFecuList){            
            rangoCodigoFecuMap.put(xbrlRangoCodigoFecu.getIdInformeEeff(), new Long[]{xbrlRangoCodigoFecu.getCodigoFecuDesde(), xbrlRangoCodigoFecu.getCodigoFecuHasta()});
        }
        return rangoCodigoFecuMap;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Map<Concept, Map<EstadoFinanciero, Boolean>> buildMappingEstadoFinanciero(final XbrlTaxonomia xbrlTaxonomia, final List<Concept> conceptList, final List<EstadoFinanciero> estadoFinancieroList) throws Exception{
        EstadoFinanciero estadoFinanciero = null;
        Map<Concept, Map<EstadoFinanciero, Boolean>> mapping = new LinkedHashMap<Concept, Map<EstadoFinanciero, Boolean>>();    
        Map<String, Concept> taxonomyConceptMap =  index(conceptList, on(Concept.class).getId());
        final List<XbrlConceptoCodigoFecu> xbrlConceptoCodigoFecuList = 
                                            em.createQuery("select o from XbrlConceptoCodigoFecu o where o.xbrlTaxonomia.idTaxonomia =:idTaxonomia").
                                            setParameter("idTaxonomia", xbrlTaxonomia.getIdTaxonomia()).
                                            getResultList();
        
        final List<String> conceptos = extract(xbrlConceptoCodigoFecuList, on(XbrlConceptoCodigoFecu.class).getIdConceptoXbrl());
        final Set<String> conceptosSet = new LinkedHashSet<String>(conceptos);
        
        for(String concepto : conceptosSet){
            Concept concept = taxonomyConceptMap.get(concepto);
            concept.setMapeado(Boolean.TRUE);
            mapping.put(concept, new LinkedHashMap<EstadoFinanciero, Boolean>());
        }
        
        for(XbrlConceptoCodigoFecu xbrlConceptoCodigoFecu : xbrlConceptoCodigoFecuList){
            List<EstadoFinanciero> estadoFinancieroListFiltered = select(estadoFinancieroList, having(on(EstadoFinanciero.class).getIdFecu(), equalTo(xbrlConceptoCodigoFecu.getEstadoFinanciero().getIdFecu())));
            if(!estadoFinancieroListFiltered.isEmpty()){
               estadoFinanciero = estadoFinancieroListFiltered.get(0);
            }
            mapping.get(taxonomyConceptMap.get(xbrlConceptoCodigoFecu.getIdConceptoXbrl())).put(estadoFinanciero, Boolean.TRUE);            
        }
        
        return mapping;
    }
    
    
}
