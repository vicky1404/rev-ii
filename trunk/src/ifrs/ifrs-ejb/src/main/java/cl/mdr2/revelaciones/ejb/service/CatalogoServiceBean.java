package cl.bicevida.revelaciones.ejb.service;


import static cl.bicevida.revelaciones.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;
import cl.bicevida.revelaciones.ejb.entity.Catalogo;
import cl.bicevida.revelaciones.ejb.entity.Grupo;
import cl.bicevida.revelaciones.ejb.entity.TipoCuadro;
import cl.bicevida.revelaciones.ejb.service.local.CatalogoServiceLocal;

import java.util.List;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;


@Stateless
public class CatalogoServiceBean implements CatalogoServiceLocal{
    
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;
    
    public CatalogoServiceBean() {
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Object mergeEntity(Catalogo entity){
        return em.merge(entity);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Object persistEntity(Catalogo entity){
        em.persist(entity);
        return entity;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Catalogo> findCatalogoAll(){
        Query query = em.createNamedQuery(Catalogo.CATALOGO_FIND_ALL);
        return query.getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Catalogo> findCatalogoVigenteAll(){
        Query query = em.createNamedQuery(Catalogo.CATALOGO_FIND_ALL_VIGENTE);
        return query.getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Catalogo> findCatalogoNoVigente(){
        Query query = em.createNamedQuery(Catalogo.CATALOGO_FIND_ALL_NO_VIGENTE);
        return query.getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Catalogo findCatalogoByCatalogo(Catalogo nota){
        Query query = em.createNamedQuery(Catalogo.CATALOGO_FIND_BY_NOTA);
        query.setParameter("nota", nota);
        return (Catalogo)query.getSingleResult();
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistEntity(List<Catalogo> lista) throws Exception{        
        for(Catalogo nota : lista){
            em.merge(nota);
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Catalogo> findCatalogoByFiltro(final String usuario, final TipoCuadro tipoCuadro, final Grupo grupo, final Long vigencia) throws Exception{
        List<Catalogo> catalogoByFiltro = em.createNamedQuery(Catalogo.CATALOGO_FIND_BY_FILTRO)
                                             .setParameter("usuario", usuario )
                                             .setParameter("tipoCuadro", tipoCuadro != null ? tipoCuadro.getIdTipoCuadro() : null )   
                                             .setParameter("grupo", grupo != null ? grupo.getIdGrupoAcceso() : null)
                                             .setParameter("vigencia", vigencia)   
                                             .setHint(QueryHints.REFRESH, HintValues.TRUE)
                                             .getResultList();
        return catalogoByFiltro;
    }
    
    public List<Catalogo> findAllVigenteByTipo(final TipoCuadro tipoCuadro)throws Exception{
        return em.createNamedQuery(Catalogo.CATALOGO_FIND_ALL_VIGENTE_BY_TIPO)                                             
                                             .setParameter("tipoCuadro", tipoCuadro != null ? tipoCuadro.getIdTipoCuadro() : null )                                                
                                             .setHint(QueryHints.REFRESH, HintValues.TRUE)
                                             .getResultList();
    }
    
    public List<Catalogo> findAllByTipo(final TipoCuadro tipoCuadro, final Long vigente)throws Exception{
        return em.createNamedQuery(Catalogo.CATALOGO_FIND_ALL_BY_TIPO)                                             
                                             .setParameter("tipoCuadro", tipoCuadro != null ? tipoCuadro.getIdTipoCuadro() : null )
                                             .setParameter("vigente", vigente)
                                             .setHint(QueryHints.REFRESH, HintValues.TRUE)
                                             .getResultList();
    }
    
}
