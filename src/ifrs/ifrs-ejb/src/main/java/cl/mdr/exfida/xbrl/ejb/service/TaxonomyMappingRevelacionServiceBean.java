package cl.mdr.exfida.xbrl.ejb.service;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.index;
import static ch.lambdaj.Lambda.on;
import static cl.mdr.ifrs.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cl.mdr.exfida.xbrl.ejb.entity.XbrlConceptoCelda;
import cl.mdr.exfida.xbrl.ejb.entity.XbrlTaxonomia;
import cl.mdr.exfida.xbrl.ejb.service.local.TaxonomyMappingRevelacionServiceLocal;
import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.ifrs.ejb.entity.Estructura;

import xbrlcore.taxonomy.Concept;

@Stateless
public class TaxonomyMappingRevelacionServiceBean implements TaxonomyMappingRevelacionServiceLocal{

    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;

    public TaxonomyMappingRevelacionServiceBean() {
        super();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistMappingTaxonomiaRevelacion(final XbrlTaxonomia xbrlTaxonomia, final Map<Concept, Map<Celda, Boolean>> mapping) throws Exception {
        List<XbrlConceptoCelda> xbrlConceptoCeldaList = new ArrayList<XbrlConceptoCelda>();
        for (Map.Entry<Concept, Map<Celda, Boolean>> entry : mapping.entrySet()) {
            for (Map.Entry<Celda, Boolean> entryCelda : entry.getValue().entrySet()) {
                xbrlConceptoCeldaList.add(new XbrlConceptoCelda(entry.getKey().getId(), entryCelda.getKey(), xbrlTaxonomia));
            }
        }
        
        em.createQuery(" delete from XbrlConceptoCelda o where o.xbrlTaxonomia.idTaxonomia =:idTaxonomia ")
          .setParameter("idTaxonomia", xbrlTaxonomia.getIdTaxonomia())
          .executeUpdate();
        
        for (final XbrlConceptoCelda xbrlConceptoCelda : xbrlConceptoCeldaList) {
            em.persist(xbrlConceptoCelda);
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteMappingByConceptoAndTaxonomia(final XbrlTaxonomia xbrlTaxonomia, final Concept concept){
        em.createQuery(" delete from XbrlConceptoCelda o " +
                       " where o.xbrlTaxonomia.idTaxonomia =:idTaxonomia " +
                       " and o.idConceptoXbrl =:idConceptoXbrl")
          .setParameter("idTaxonomia", xbrlTaxonomia.getIdTaxonomia())
          .setParameter("idConceptoXbrl", concept.getId())
          .executeUpdate();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<XbrlConceptoCelda> findMappingByEstructura(final Estructura estructura) throws Exception {        
        return em.createNamedQuery(XbrlConceptoCelda.FIND_BY_ESTRUCTURA)
                                .setParameter("idEstructura", estructura.getIdEstructura())
                                .getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Map<Concept, Map<Celda, Boolean>> buildMappingByEstructura(final Estructura estructura, final List<Concept> conceptList) throws Exception{
        Map<Concept, Map<Celda, Boolean>> mapping = new LinkedHashMap<Concept, Map<Celda, Boolean>>();    
        Map<String, Concept> taxonomyConceptMap =  index(conceptList, on(Concept.class).getId());
        final List<XbrlConceptoCelda> xbrlConceptoCeldaList = this.findMappingByEstructura(estructura) ;
        
        final List<String> conceptos = extract(xbrlConceptoCeldaList, on(XbrlConceptoCelda.class).getIdConceptoXbrl());
        final Set<String> conceptosSet = new LinkedHashSet<String>(conceptos);
        
        for(String concepto : conceptosSet){
            Concept concept = taxonomyConceptMap.get(concepto);
            concept.setMapeado(Boolean.TRUE);
            mapping.put(concept, new LinkedHashMap<Celda, Boolean>());
        }
        
        for(XbrlConceptoCelda xbrlConceptoCelda : xbrlConceptoCeldaList){
            mapping.get(taxonomyConceptMap.get(xbrlConceptoCelda.getIdConceptoXbrl())).put(xbrlConceptoCelda.getCelda(), Boolean.TRUE);            
        }
        
        return mapping;
    }
    
    
    


}
