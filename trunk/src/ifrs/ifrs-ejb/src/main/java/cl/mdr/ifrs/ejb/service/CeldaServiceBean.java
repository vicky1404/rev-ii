package cl.mdr.ifrs.ejb.service;


import static cl.mdr.ifrs.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.ifrs.ejb.entity.Columna;
import cl.mdr.ifrs.ejb.entity.Grilla;
import cl.mdr.ifrs.ejb.entity.pk.CeldaPK;
import cl.mdr.ifrs.ejb.service.local.CeldaServiceLocal;

@Stateless
public class CeldaServiceBean implements CeldaServiceLocal{
    
    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;
    
    public CeldaServiceBean() {
        super();
    }
    
    public Object mergeEntity(Celda entity) throws Exception{
        return em.merge(entity);
    }

    public Object persistEntity(Celda entity){
        em.persist(entity);
        return entity;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Celda findCeldaById(Celda celda) throws Exception{
        
            CeldaPK pk = new CeldaPK();
                pk.setIdColumna(celda.getIdColumna());
                pk.setIdFila(celda.getIdFila());
                pk.setIdGrilla(celda.getIdGrilla());
            return (Celda) em.find(Celda.class, pk);
        
    }
    
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Celda> findCeldaByColumna(Columna columna){
        Query query = em.createNamedQuery(Celda.CELDA_FIND_BY_COLUMNA);
        query.setParameter("columna",columna);
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Celda findCeldaByColumnaGrilla(Columna columna){
        Query query = em.createNamedQuery(Celda.CELDA_FIND_BY_COLUMNA);
        query.setParameter("columna",columna);
        
        if (query.getResultList().size() > 0){
        	
        	return (Celda) query.getSingleResult();
        }
        
        return null;
    }
   
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Celda> findCeldaByGrupo(Long grupo, Grilla grilla) throws Exception{
        
            Query query = em.createNamedQuery(Celda.CELDA_FIND_BY_GRUPO);            
            query.setParameter("grupo", grupo);
            query.setParameter("idGrilla", grilla.getIdGrilla());
            return query.getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long findMaxGrupo(Grilla grilla) throws Exception{
            Query query = em.createNamedQuery(Celda.CELDA_FIND_MAX_GRUPO);
            query.setParameter("idGrilla", grilla.getIdGrilla());
            if (query.getResultList() != null && query.getResultList().size() > 0){
            
                    Long resultado = (Long) query.getSingleResult();
                    
                    if (resultado == null){
                            resultado = 0L;
                        }
            
                return resultado;
                
            } else {
                    return 0L;    
                }
    }
    
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Celda> findCeldaByIdFila(Long idGrilla, Long idFila) throws Exception{
        Query query = em.createNamedQuery(Celda.CELDA_FIND_BY_ID_FILA);
        query.setParameter("idGrilla",idGrilla);
        query.setParameter("idFila",idFila);
        return query.getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistCeldaList(final List<Celda> celdaList) throws Exception{
        for(final Celda celda : celdaList){
            em.merge(celda);
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistFormulaEstaticaList(final Grilla grilla, final List<Celda> celdaList) throws Exception{
        //actualiza o guarda tipo de grilla
        em.merge(grilla);
        //actualiza o guerda formulas en el listado de celdas correspondiente
        for(final Celda celda : celdaList){
            em.merge(celda);
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistFormulaDinamicaMap(final Grilla grilla, final Map<Celda, List<Celda>> formulaDinamicaMap) throws Exception{
        //actualiza o guarda tipo de grilla
        em.merge(grilla);
        for(final Map.Entry<Celda, List<Celda>> entry : formulaDinamicaMap.entrySet()){
            //actualiza celda parent
            em.merge(entry.getKey());
            //actualiza celdas child
            for(final Celda celda : entry.getValue()){                
                em.merge(celda);
            }
        }        
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteFormulaDinamica(Celda celdaParent, List<Celda> celdaChildList) throws Exception{
        em.merge(celdaParent);
        for(Celda celdaChild : celdaChildList){
            em.merge(celdaChild);
        }
    }
    
    
    
    
}
