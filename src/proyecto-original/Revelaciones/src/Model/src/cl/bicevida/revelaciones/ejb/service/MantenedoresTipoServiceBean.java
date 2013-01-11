package cl.bicevida.revelaciones.ejb.service;


import static cl.bicevida.revelaciones.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;
import cl.bicevida.revelaciones.ejb.entity.EstadoCuadro;
import cl.bicevida.revelaciones.ejb.entity.Periodo;
import cl.bicevida.revelaciones.ejb.entity.TipoCelda;
import cl.bicevida.revelaciones.ejb.entity.TipoCuadro;
import cl.bicevida.revelaciones.ejb.entity.TipoDato;
import cl.bicevida.revelaciones.ejb.entity.TipoEstructura;
import cl.bicevida.revelaciones.ejb.service.local.MantenedoresTipoServiceLocal;

import java.util.List;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Stateless(name = "MantenedoresTipoService")
public class MantenedoresTipoServiceBean implements MantenedoresTipoServiceLocal {
    
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;
    
    
    public MantenedoresTipoServiceBean() {
        super();
    }
    
    public Object mergeEntity(Object entity){
        return em.merge(entity);
    }

    public Object persistEntity(Object entity){
        em.persist(entity);
        return entity;
    }
    
    /**
     * @return
     */
    public List<Periodo> findAllPeriodo(){
        Query query = em.createNamedQuery("Periodo.findAll");
        return query.getResultList();
    }
    
    /**
     * @return
     */
    public Periodo findByPeriodo(Long periodo) throws Exception{
        Query query = em.createNamedQuery("Periodo.findByPeriodo");
        query.setParameter("periodo", periodo);
        Periodo periodoResult = (Periodo)query.getSingleResult();        
        return periodoResult;
        
    }
    
    /**
     * @return
     */
    public List<TipoEstructura> findAllTipoEstructura(){
        Query query = em.createNamedQuery(TipoEstructura.QUERY_FIND_TIPO_ESTRUCTURA_ALL);
        return query.getResultList();
    }
    
    /**
     * @return
     */
    public List<TipoCelda> findAllTipoCelda(){
        Query query = em.createNamedQuery(TipoCelda.QUERY_FIND_CELDA_ALL);
        return query.getResultList();
    }
    
    /**
     * @return
     */
    public List<TipoDato> findAllTipoDato(){
       Query query = em.createNamedQuery(TipoDato.QUERY_FIND_TIPO_DATO_ALL);
       return query.getResultList();
    }

    /**
     * @return
     */
    public List<TipoCuadro> findAllTipoCuadro(){
       return em.createNamedQuery(TipoCuadro.FIND_ALL).getResultList();       
    }

    /*Metodos para EstadoCuadro*/
    public List<EstadoCuadro> findAllEstadoCuadro(){
        return em.createNamedQuery(EstadoCuadro.FIND_ALL).getResultList();       
    }
    
    /*Metodos para EstadoCuadro*/
    public EstadoCuadro findEstadoCuadroById(Long idEstado){
        return (EstadoCuadro)em.createNamedQuery(EstadoCuadro.FIND_BY_ID).setParameter("idEstado", idEstado).getSingleResult();
    }
}
