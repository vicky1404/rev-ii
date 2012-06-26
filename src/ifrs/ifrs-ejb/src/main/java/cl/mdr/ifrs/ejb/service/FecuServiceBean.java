package cl.mdr.ifrs.ejb.service;

import static ch.lambdaj.Lambda.index;
import static ch.lambdaj.Lambda.on;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cl.mdr.ifrs.ejb.entity.CodigoFecu;
import cl.mdr.ifrs.ejb.service.local.FecuServiceLocal;
import static cl.mdr.ifrs.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;

@Stateless
public class FecuServiceBean implements FecuServiceLocal{
    
    
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;
    
    
    public FecuServiceBean() {
    }
    
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Map<Long, CodigoFecu> getCodigoFecusVigentes(){
        List<CodigoFecu> codigos = em.createNamedQuery(CodigoFecu.FIND_VIGENTE).getResultList();
        return index(codigos, on(CodigoFecu.class).getIdFecu());
    }
}
