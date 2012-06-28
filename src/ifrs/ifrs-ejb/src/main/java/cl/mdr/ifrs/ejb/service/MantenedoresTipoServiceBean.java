package cl.mdr.ifrs.ejb.service;


import static cl.mdr.ifrs.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import cl.mdr.ifrs.ejb.entity.EstadoCuadro;
import cl.mdr.ifrs.ejb.entity.Periodo;
import cl.mdr.ifrs.ejb.entity.TipoCelda;
import cl.mdr.ifrs.ejb.entity.TipoCuadro;
import cl.mdr.ifrs.ejb.entity.TipoDato;
import cl.mdr.ifrs.ejb.entity.TipoEstructura;
import cl.mdr.ifrs.ejb.service.local.MantenedoresTipoServiceLocal;


@Stateless(name = "MantenedoresTipoService")
public class MantenedoresTipoServiceBean implements MantenedoresTipoServiceLocal {
       
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
    
    public void deleteTipoCuadro(TipoCuadro entity) throws Exception {
    	TipoCuadro tipoCuadro = em.find(TipoCuadro.class, entity.getIdTipoCuadro());
    	em.remove(tipoCuadro);
    }
    
    /**
     * @return
     */
    @SuppressWarnings("unchecked")
   	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Periodo> findAllPeriodo(){
        Query query = em.createNamedQuery("Periodo.findAll");
        return query.getResultList();
    }
    
    /**
     * @return
     */    
   	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Periodo findByPeriodo(Long periodo) throws Exception{
        Query query = em.createNamedQuery("Periodo.findByPeriodo");
        query.setParameter("periodo", periodo);
        Periodo periodoResult = (Periodo)query.getSingleResult();        
        return periodoResult;
        
    }
    
    /**
     * @return
     */
    @SuppressWarnings("unchecked")
   	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<TipoEstructura> findAllTipoEstructura(){
        Query query = em.createNamedQuery(TipoEstructura.QUERY_FIND_TIPO_ESTRUCTURA_ALL);
        return query.getResultList();
    }
    
    /**
     * @return
     */
    @SuppressWarnings("unchecked")
   	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<TipoCelda> findAllTipoCelda(){
        Query query = em.createNamedQuery(TipoCelda.QUERY_FIND_CELDA_ALL);
        return query.getResultList();
    }
    
    /**
     * @return
     */
    @SuppressWarnings("unchecked")
   	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<TipoDato> findAllTipoDato(){
       Query query = em.createNamedQuery(TipoDato.QUERY_FIND_TIPO_DATO_ALL);
       return query.getResultList();
    }

    /**
     * @return
     */
    @SuppressWarnings("unchecked")
   	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<TipoCuadro> findAllTipoCuadro(){
       return em.createNamedQuery(TipoCuadro.FIND_ALL).getResultList();       
    }
    
    /**
     * @return
     */
    @SuppressWarnings("unchecked")
   	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<TipoCuadro> findByFiltro(TipoCuadro tipoCuadro){
       return em.createNamedQuery(TipoCuadro.FIND_BY_FILTRO)
    		   .setParameter("nombre", tipoCuadro.getNombre() != null ? "%" + tipoCuadro.getNombre().toLowerCase() + "%" : null) 
    		   .setParameter("titulo", tipoCuadro.getTitulo() != null ?  "%" + tipoCuadro.getTitulo().toLowerCase() + "%": null) .getResultList();       
    }

    /*Metodos para EstadoCuadro*/
    @SuppressWarnings("unchecked")
   	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<EstadoCuadro> findAllEstadoCuadro(){
        return em.createNamedQuery(EstadoCuadro.FIND_ALL).getResultList();       
    }
    
	    
}
