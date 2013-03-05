package cl.bicevida.revelaciones.ejb.service;


import static cl.bicevida.revelaciones.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;
import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.Columna;
import cl.bicevida.revelaciones.ejb.entity.Grilla;
import cl.bicevida.revelaciones.ejb.entity.Periodo;
import cl.bicevida.revelaciones.ejb.entity.RelacionCelda;
import cl.bicevida.revelaciones.ejb.entity.pk.CeldaPK;
import cl.bicevida.revelaciones.ejb.service.local.CeldaServiceLocal;

import java.util.List;
import java.util.Map;

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
public class CeldaServiceBean implements CeldaServiceLocal{
    
    @Resource
    SessionContext sessionContext;
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
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Celda> findCeldaByColumna(Columna columna){
        Query query = em.createNamedQuery(Celda.CELDA_FIND_BY_COLUMNA);
        query.setParameter("columna",columna);
        return query.getResultList();
    }
   
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Celda> findCeldaByGrupo(Long grupo, Grilla grilla) throws Exception{
        
            Query query = em.createNamedQuery(Celda.CELDA_FIND_BY_GRUPO);
            query.setHint(QueryHints.MAINTAIN_CACHE, HintValues.FALSE);
            query.setHint(QueryHints.REFRESH, HintValues.TRUE);
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
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Celda> findCeldaByEeff(Long idPeriodo, Long idFecu){
        Query query = em.createNamedQuery(Celda.FIND_BY_EEFF);
        query.setParameter("idPeriodo",idPeriodo);
        query.setParameter("idFecu",idFecu);
        return query.getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Celda> findCeldaByEeffDet(Long idPeriodo, Long idFecu,  Long idCuenta){
        Query query = em.createNamedQuery(Celda.FIND_BY_EEFF_DET);
        query.setParameter("idPeriodo",idPeriodo);        
        query.setParameter("idFecu",idFecu);
        query.setParameter("idCuenta",idCuenta);
        return query.getResultList();
    }  
    
    
    /**
     * @param relacionMap
     * @param idPeriodo
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistRelaccionCelda(Map<Celda, List[]> relacionMap) {

        for (List[] arrayListas : relacionMap.values()) {
            // 0 contiene las RelacionesEeff
            if (Util.esListaValida(arrayListas[0])) {
                List<RelacionCelda> relList = arrayListas[0];
                for (RelacionCelda relCelda : relList) {
                    
                    Query query = em.createNativeQuery("INSERT INTO REV_RELACION_CELDA (ID_COLUMNA, ID_FILA, ID_GRILLA, ID_COLUMNA_REL, ID_FILA_REL, ID_GRILLA_REL, ID_PERIODO) VALUES (?,?,?,?,?,?,?)");
                        int count = 1;
                        query.setParameter(count++, relCelda.getIdColumna());
                        query.setParameter(count++, relCelda.getIdFila());
                        query.setParameter(count++, relCelda.getIdGrilla());
                        query.setParameter(count++, relCelda.getIdColumnaRel());
                        query.setParameter(count++, relCelda.getIdFilaRel());
                        query.setParameter(count++, relCelda.getIdGrillaRel());
                        query.setParameter(count++, relCelda.getIdPeriodo());
                    query.executeUpdate();
                    
                }
            }
        }
    }
    
    
    /**
     * @param relacionMap
     * @param idPeriodo
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteRelacionCelda(Celda celda, Periodo periodo) {
        
            Query query = em.createNativeQuery("DELETE REV_RELACION_CELDA WHERE ID_COLUMNA_REL = ? AND ID_FILA_REL = ? AND ID_GRILLA_REL = ? AND ID_PERIODO = ? ");
            int count = 1;
            query.setParameter(count++, celda.getIdColumna());
            query.setParameter(count++, celda.getIdFila());
            query.setParameter(count++, celda.getIdGrilla());
            query.setParameter(count++, periodo.getIdPeriodo());
            query.executeUpdate();
    }
    
    /**
     * @param relacionMap
     * @param idPeriodo
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteRelacionCeldaByGrilla(Grilla grilla, Periodo periodo) {
        
            Query query = em.createNativeQuery("DELETE REV_RELACION_CELDA WHERE ID_GRILLA_REL = ? AND ID_PERIODO = ? ");
            int count = 1;
            query.setParameter(count++, grilla.getIdGrilla());
            query.setParameter(count++, periodo.getIdPeriodo());
            query.executeUpdate();
    }
    
}
