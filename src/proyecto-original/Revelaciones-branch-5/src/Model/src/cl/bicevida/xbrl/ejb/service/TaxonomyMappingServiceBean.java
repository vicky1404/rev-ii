package cl.bicevida.xbrl.ejb.service;

import static cl.bicevida.revelaciones.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;
import cl.bicevida.xbrl.ejb.entity.XbrlConceptoCelda;
import cl.bicevida.xbrl.ejb.service.local.TaxonomyMappingServiceLocal;

import java.util.List;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "TaxonomyMappingService", mappedName = "AppRevelaciones-branch-6-Model-TaxonomyMappingService")
public class TaxonomyMappingServiceBean implements TaxonomyMappingServiceLocal {
    
    @Resource
    SessionContext sessionContext;
    
    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;

    public TaxonomyMappingServiceBean() {
        super();
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistMappingTaxonomiaRevelacion(final List<XbrlConceptoCelda> mappingList) throws Exception{
        for(final XbrlConceptoCelda xbrlConceptoCelda : mappingList){
            em.persist(xbrlConceptoCelda);
        }
    }
}
