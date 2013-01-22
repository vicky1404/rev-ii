package cl.mdr.ifrs.ejb.service;


import static cl.mdr.ifrs.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cl.mdr.ifrs.ejb.entity.Columna;
import cl.mdr.ifrs.ejb.service.local.ColumnaServiceLocal;

@Stateless
public class ColumnaServiceBean implements ColumnaServiceLocal{
    
    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;
    
    public ColumnaServiceBean() {
        super();
    }
    
    public Object mergeEntity(Columna entity){
        return em.merge(entity);
    }

    public Object persistEntity(Columna entity){
        em.persist(entity);
        return entity;
    }
}
