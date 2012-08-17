package cl.bicevida.revelaciones.ejb.service;


import static cl.bicevida.revelaciones.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;
import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.Columna;
import cl.bicevida.revelaciones.ejb.entity.Grilla;
import cl.bicevida.revelaciones.ejb.service.local.GrillaServiceLocal;

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
public class GrillaServiceBean implements GrillaServiceLocal{
    
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;
    
    public GrillaServiceBean() {
        super();
    }
    
    public Object mergeEntity(Grilla entity){
        return em.merge(entity);
    }

    public Object persistEntity(Grilla entity){
        em.persist(entity);
        return entity;
    }
    
    public Grilla findGrilla(Long idGrilla) throws Exception{
        Grilla grilla = em.find(Grilla.class, idGrilla);
        return grilla;
    }
    
    public Grilla findGrillaById(Long idGrilla) throws Exception{
        
        Query query = em.createNamedQuery(Grilla.FIND_GRILLA_BY_ID)
                 .setHint(QueryHints.MAINTAIN_CACHE, HintValues.FALSE)
                 .setHint(QueryHints.REFRESH, HintValues.TRUE)
                 .setParameter("idGrilla", idGrilla);
        
        if (query.getResultList().size() > 0){
                
                return (Grilla) query.getSingleResult();
            
        } else {
                return new Grilla();
            }
                 
        
        
    }
    
    /**
     * stored in the database a list of cells.
     * @param grid
     * @author Rodrigo Díaz V.
     * @since 15/03/2011
     * @throws Exception
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistCell(Grilla grid) throws Exception{
        int resultado = em.createQuery("delete from Celda c where c.idGrilla = :idGrilla").setParameter("idGrilla", grid.getIdGrilla()).executeUpdate();
        for(Columna column : grid.getColumnaList()){
            for(Celda cell : column.getCeldaList()){
                
                System.out.println("ID_GRILLA->" + cell.getIdGrilla() + " COLUMNA->"+cell.getIdColumna()+" FILA->" + cell.getIdFila() + " DATO->"+cell.getTipoDato().getIdTipoDato()+" CELDA->"+cell.getTipoCelda().getIdTipoCelda());
                
                em.createNativeQuery(" INSERT INTO rev_celda ( id_grilla, id_Fila, id_Columna, id_tipo_dato, id_tipo_celda, valor, child_vertical, parent_vertical) VALUES ( ?,?,?,?,?,?,?,?) " )
                    .setParameter(1, cell.getIdGrilla())
                    .setParameter(2, cell.getIdFila())
                    .setParameter(3, cell.getIdColumna())
                    .setParameter(4, cell.getTipoDato().getIdTipoDato())
                    .setParameter(5, cell.getTipoCelda().getIdTipoCelda())
                    .setParameter(6, cell.getValor())
                    .setParameter(7, cell.getChildVertical())
                    .setParameter(8, cell.getParentVertical())
                    .executeUpdate();
                //em.persist(cell);
            }
        }
    }
    
    /**
     * Guarda en la base de datos una lista de grillas.
     * @param grillaNotaList
     * @author Manuel Gutierrez C.
     * @since 18/01/2011
     * @throws Exception
     */
    public void mergeGrillaList(List<Grilla> grillaList) throws Exception{
        
        for (Grilla grilla : grillaList){
                em.merge(grilla);
            }
    }
    
    public void desagregarGrilla(final Grilla grilla)throws Exception{
        
    }
    
    public void consolidarGrilla(final Grilla grilla)throws Exception{
        
    }
    
}
