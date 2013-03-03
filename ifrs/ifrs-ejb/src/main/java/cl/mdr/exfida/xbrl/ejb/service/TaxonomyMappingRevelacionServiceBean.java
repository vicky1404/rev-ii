package cl.mdr.exfida.xbrl.ejb.service;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.index;
import static ch.lambdaj.Lambda.on;
import static cl.mdr.ifrs.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;

import xbrlcore.taxonomy.Concept;
import cl.mdr.exfida.xbrl.ejb.entity.XbrlConceptoCelda;
import cl.mdr.exfida.xbrl.ejb.entity.XbrlConceptoCodigoFecu;
import cl.mdr.exfida.xbrl.ejb.entity.XbrlTaxonomia;
import cl.mdr.exfida.xbrl.ejb.entity.pk.XbrlConceptoCeldaPK;
import cl.mdr.exfida.xbrl.ejb.service.local.TaxonomyMappingRevelacionServiceLocal;
import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.ifrs.ejb.entity.Columna;
import cl.mdr.ifrs.ejb.entity.Estructura;

@Stateless
public class TaxonomyMappingRevelacionServiceBean implements TaxonomyMappingRevelacionServiceLocal{

    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;

    public TaxonomyMappingRevelacionServiceBean() {
        super();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistMappingTaxonomiaRevelacion(final XbrlTaxonomia xbrlTaxonomia, final Map<Celda, List<Concept>> mapping) throws Exception {
        List<XbrlConceptoCelda> xbrlConceptoCeldaList = new ArrayList<XbrlConceptoCelda>();
        for (Entry<Celda, List<Concept>> entry : mapping.entrySet()) {
            for (Concept concept : entry.getValue()) {
                xbrlConceptoCeldaList.add(new XbrlConceptoCelda(concept.getId(), entry.getKey(), xbrlTaxonomia));
            }
        }
        
        em.createQuery(" delete from XbrlConceptoCelda o where o.xbrlTaxonomia.idTaxonomia =:idTaxonomia ")
          .setParameter("idTaxonomia", xbrlTaxonomia.getIdTaxonomia())
          .executeUpdate();
        
        Query nativeQuery = em
				.createNativeQuery("insert into IFRS_XBRL_CONCEPTO_CELDA (ID_COLUMNA, ID_CONCEPTO_XBRL, ID_FILA, ID_GRILLA, ID_TAXONOMIA) values (?,?,?,?,?)");
		for (final XbrlConceptoCelda xbrlConcepto : xbrlConceptoCeldaList) {
			nativeQuery.setParameter(1, xbrlConcepto.getCelda().getIdColumna());
			nativeQuery.setParameter(2, xbrlConcepto.getIdConceptoXbrl());
			nativeQuery.setParameter(3, xbrlConcepto.getCelda().getIdFila());
			nativeQuery.setParameter(4, xbrlConcepto.getCelda().getIdGrilla());
			nativeQuery.setParameter(5, xbrlConcepto.getXbrlTaxonomia().getIdTaxonomia());
			nativeQuery.executeUpdate();
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
    
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<XbrlConceptoCelda> findMappingByEstructura(final Estructura estructura) throws Exception {        
        return em.createNamedQuery(XbrlConceptoCelda.FIND_BY_ESTRUCTURA)
                                .setParameter("idEstructura", estructura.getIdEstructura())
                                .getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Map<Celda, List<Concept>> buildMappingByEstructura(final Estructura estructura, final List<Concept> conceptList) throws Exception{
    	Map<Celda, List<Concept>> mapping = new LinkedHashMap<Celda, List<Concept>>();
    	
        Map<String, Concept> taxonomyConceptMap =  index(conceptList, on(Concept.class).getId());
        
        final List<XbrlConceptoCelda> xbrlConceptoCeldaList = findMappingByEstructura(estructura) ;
        
        for (XbrlConceptoCelda xbrlConceptoCelda : xbrlConceptoCeldaList) {
			List<Concept> list = mapping.get(xbrlConceptoCelda.getCelda());
			if(list == null){
				list = new ArrayList<Concept>();
				mapping.put(xbrlConceptoCelda.getCelda(), list);
			}
			list.add(taxonomyConceptMap.get(xbrlConceptoCelda.getIdConceptoXbrl()));
		}
        
        return mapping;
    }

	@Override
	public void persistMappingTaxonomiaRevelacionColumnas(XbrlTaxonomia xbrlTaxonomia, Map<Columna, List<Concept>> mapping) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<Columna, List<Concept>> buildMappingByEstructuraColumna(Estructura estructura, List<Concept> conceptList) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
    
    
    


}
