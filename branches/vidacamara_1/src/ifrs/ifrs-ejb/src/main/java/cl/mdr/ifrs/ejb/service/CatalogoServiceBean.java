package cl.mdr.ifrs.ejb.service;


import static cl.mdr.ifrs.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import cl.mdr.ifrs.ejb.entity.Catalogo;
import cl.mdr.ifrs.ejb.entity.Grupo;
import cl.mdr.ifrs.ejb.entity.TipoCuadro;
import cl.mdr.ifrs.ejb.service.local.CatalogoServiceLocal;


@Stateless
public class CatalogoServiceBean implements CatalogoServiceLocal{
    
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
    	
       Catalogo catalogo = new Catalogo();
	    		catalogo.setVigencia(entity.getVigencia());
	    		catalogo.setImpresionHorizontal(entity.getImpresionHorizontal());
	    		catalogo.setNombre(entity.getNombre());
	    		catalogo.setOrden(entity.getOrden());
	    		catalogo.setTipoCuadro(entity.getTipoCuadro());
	    		catalogo.setTitulo(entity.getTitulo());
	    		catalogo.setValidarEeff(entity.getValidarEeff());
	    		catalogo.setEmpresa(entity.getEmpresa());
	    		catalogo.setValidarEeff(0L);    		
        em.persist(catalogo);
        return null;
    }
    
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Catalogo> findCatalogoAll(){
        Query query = em.createNamedQuery(Catalogo.CATALOGO_FIND_ALL);
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Catalogo> findCatalogoVigenteAll(){
        Query query = em.createNamedQuery(Catalogo.CATALOGO_FIND_ALL_VIGENTE);
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Catalogo> findCatalogoNoVigente(){
        Query query = em.createNamedQuery(Catalogo.CATALOGO_FIND_ALL_NO_VIGENTE);
        return query.getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Catalogo findCatalogoByCatalogo(Catalogo catalogo){
        Query query = em.createNamedQuery(Catalogo.CATALOGO_FIND_BY_NOTA);
        query.setParameter("nota", catalogo);
        return (Catalogo)query.getSingleResult();
    }     
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistEntity(List<Catalogo> lista) throws Exception{        
        for(Catalogo nota : lista){
            em.merge(nota);
        }
    }
    
    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Catalogo> findCatalogoByFiltro(final Long rutEmpresa, final String usuario, final TipoCuadro tipoCuadro, final Grupo grupo, final Long vigencia) throws Exception{        
		List<Catalogo> catalogoByFiltro = em.createNamedQuery(Catalogo.CATALOGO_FIND_BY_FILTRO)
											 .setParameter("rut", rutEmpresa )
											 .setParameter("usuario", usuario )
                                             .setParameter("tipoCuadro", tipoCuadro)   
                                             .setParameter("grupo", grupo)
                                             .setParameter("vigencia", vigencia)                                                
                                             .getResultList();
        return catalogoByFiltro;
    }
    
    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Catalogo> findCatalogoByGrupo(final Long rutEmpresa, final TipoCuadro tipoCuadro, final Grupo grupo, final Long vigencia) throws Exception{        
		return em.createNamedQuery(Catalogo.CATALOGO_FIND_BY_GRUPO)
											 .setParameter("rut", rutEmpresa )											 
                                             .setParameter("tipoCuadro", tipoCuadro)   
                                             .setParameter("grupo", grupo)
                                             .setParameter("vigencia", vigencia)                                                
                                             .getResultList();
        
    }
    
    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Catalogo> findAllVigenteByTipo(final Long rutEmpresa, final TipoCuadro tipoCuadro)throws Exception{
        return em.createNamedQuery(Catalogo.CATALOGO_FIND_ALL_VIGENTE_BY_TIPO)
								        	 .setParameter("rutEmpresa",rutEmpresa)
								        	 .setParameter("tipoCuadro", tipoCuadro)                                                                                             
                                             .getResultList();
    }
    
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Catalogo> findAllByTipo(final Long tipoCuadro, final Long vigente, final Long idRut)throws Exception{
    	Query query = em.createNamedQuery(Catalogo.CATALOGO_FIND_ALL_BY_TIPO)                                             
                                             .setParameter("tipoCuadro",  tipoCuadro)
                                             .setParameter("idRut",  idRut)
                                             .setParameter("vigente", vigente);                                           
         return query.getResultList();
    }
    
}
