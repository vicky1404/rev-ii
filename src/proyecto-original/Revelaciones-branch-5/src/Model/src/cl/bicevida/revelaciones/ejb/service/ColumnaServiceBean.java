package cl.bicevida.revelaciones.ejb.service;


import static cl.bicevida.revelaciones.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;
import cl.bicevida.revelaciones.ejb.entity.Columna;
import cl.bicevida.revelaciones.ejb.service.local.ColumnaServiceLocal;

import java.util.List;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Stateless
public class ColumnaServiceBean implements ColumnaServiceLocal{
    
    @Resource
    SessionContext sessionContext;
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
    
    public List<Columna> getColumnaByGrillaFila(Long idGrilla, Long idFila){
        Query query = em.createNamedQuery(Columna.FIND_BY_GRILLA_FILA);
        query.setParameter("idGrilla", idGrilla);
        query.setParameter("idFila", idFila);
        return query.getResultList();
    }
    
    
    public List<Columna> getColumnaByGrilla(Long idGrilla){
        Query query = em.createNamedQuery(Columna.FIND_BY_GRILLA);
        query.setParameter("idGrilla", idGrilla);
        return query.getResultList();   
    }
}
