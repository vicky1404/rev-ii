package cl.bicevida.revelaciones.ejb.service;

import static cl.bicevida.revelaciones.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;
import cl.bicevida.revelaciones.ejb.entity.CodigoFecu;
import cl.bicevida.revelaciones.ejb.service.local.FecuServiceLocal;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.index;

import cl.bicevida.revelaciones.ejb.entity.CuentaContable;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;


import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class FecuServiceBean implements FecuServiceLocal{
    
    
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;
    
    
    public FecuServiceBean() {
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Map<Long, CodigoFecu> getCodigoFecusVigentes(){
        List<CodigoFecu> codigos = em.createNamedQuery(CodigoFecu.FIND_VIGENTE).getResultList();
        return index(codigos, on(CodigoFecu.class).getIdFecu());
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Map<Long, CuentaContable> getCodigoCuentasVigentes(){
        List<CuentaContable> codigos = em.createNamedQuery(CuentaContable.FIND_VIGENTE).getResultList();
        return index(codigos, on(CuentaContable.class).getIdCuenta());
    }
}
