package cl.bicevida.revelaciones.ejb.service;


import static ch.lambdaj.Lambda.index;
import static ch.lambdaj.Lambda.on;

import cl.bicevida.revelaciones.ejb.cross.EeffUtil;
import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.DetalleEeff;
import cl.bicevida.revelaciones.ejb.entity.EstadoFinanciero;
import cl.bicevida.revelaciones.ejb.entity.Grilla;
import cl.bicevida.revelaciones.ejb.entity.RelacionDetalleEeff;
import cl.bicevida.revelaciones.ejb.entity.RelacionEeff;
import cl.bicevida.revelaciones.ejb.entity.TipoEstadoEeff;
import cl.bicevida.revelaciones.ejb.entity.VersionEeff;
import cl.bicevida.revelaciones.ejb.service.local.EstadoFinancieroServiceLocal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Stateless
public class EstadoFinancieroServiceBean implements EstadoFinancieroServiceLocal {
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "revelacionesPU")
    private EntityManager em;

    public EstadoFinancieroServiceBean() {
    }


    public List<TipoEstadoEeff> getEstadoEeffFindAll() {
        return em.createNamedQuery("EstadoEeff.findAll").getResultList();
    }
    
    public List<VersionEeff> getVersionEeffFindByPeriodo(Long idPeriodo) {
        Query query = em.createNamedQuery(VersionEeff.FIND_BY_PERIOD);
        query.setParameter("idPeriodo", idPeriodo);
        return query.getResultList();
    }
    
    public VersionEeff getVersionEeffVigenteFindByPeriodo(Long idPeriodo) {
        Query query = em.createNamedQuery(VersionEeff.FIND_VIGENTE_BY_PERIOD);
        query.setParameter("idPeriodo", idPeriodo);
        return (VersionEeff)query.getSingleResult();
    }
    
    public void persistEeffMap(Map<Long, EstadoFinanciero> eeffMap){ 
        for(EstadoFinanciero eeff : eeffMap.values()){
            em.persist(eeff);
        }
    }
    
    public Long getMaxVersionByPeriodo(Long idPeriodo){
        Long maxVersion = (Long) em.createNamedQuery(VersionEeff.FIN_MAX_VERSION_BY_PERIODO)
            .setParameter("idPeriodo", idPeriodo)
            .getSingleResult();
        return maxVersion==null?0L:maxVersion;
    }
    
    public void updateNoVigenteByPeriodo(Long idPeriodo){
        em.createNamedQuery(VersionEeff.UPDATE_VIGENCIA_BY_PERIODO)
            .setParameter("idPeriodo", idPeriodo)
            .executeUpdate();
    }
    
    public void persisVersionEeff(VersionEeff version){
        version.setVersion(getMaxVersionByPeriodo(version.getPeriodo().getIdPeriodo()) +1L);
        updateNoVigenteByPeriodo(version.getPeriodo().getIdPeriodo());
        em.persist(version);
    }
    
    public TipoEstadoEeff getTipoEstadoEeffById(Long idEstadoEeff){
        Query query = em.createNamedQuery(TipoEstadoEeff.FIND_BY_ID);
        query.setParameter("idEstadoEeff", idEstadoEeff);
        return (TipoEstadoEeff)query.getSingleResult();
    }
    
    public List<EstadoFinanciero> getEeffVigenteByPeriodo(Long idPeriodo){
        Query query = em.createNamedQuery(EstadoFinanciero.FIND_VIGENTE_BY_PERIODO);
        query.setParameter("idPeriodo", idPeriodo);
        return query.getResultList();
    }
    
    public List<EstadoFinanciero> getEeffByVersion(Long idVersionEeff){
        Query query = em.createNamedQuery(EstadoFinanciero.FIND_BY_VERSION);
        query.setParameter("idVersionEeff", idVersionEeff);
        return query.getResultList();
    }
    
    public List<DetalleEeff> getDetalleEeffByEeff(EstadoFinanciero eeff){
        Query query = em.createNamedQuery(DetalleEeff.FIND_BY_EEFF);
        query.setParameter("eeff", eeff);
        return query.getResultList();
    }
    
    public List<DetalleEeff> getDetalleEeffByVersion(Long idVersionEeff){
        Query query = em.createNamedQuery(DetalleEeff.FIND_BY_VERSION);
        query.setParameter("idVersionEeff", idVersionEeff);
        return query.getResultList();
    }
    
    public void deleteRelacionEeffByCelda(Celda celda){
        Query query = em.createNamedQuery(RelacionEeff.DELETE_BY_CELDA);
        query.setParameter("celda", celda);
        query.executeUpdate();
    }
    
    public void deleteRelacionDetalleEeffByCelda(Celda celda){
        Query query = em.createNamedQuery(RelacionDetalleEeff.DELETE_BY_CELDA);
        query.setParameter("celda", celda);
        query.executeUpdate();
    }
    
    public void deleteRelacionAllEeffByCelda(Celda celda){
        deleteRelacionEeffByCelda(celda);
        deleteRelacionDetalleEeffByCelda(celda);
    }
    
    public List<RelacionEeff> getRelacionEeffByPeriodo(Long idPeriodo){
        Query query = em.createNamedQuery(RelacionEeff.FIND_BY_PERIODO);
        query.setParameter("idPeriodo", idPeriodo);
        return query.getResultList();
    }
    
    public List<RelacionDetalleEeff> getRelacionDetalleEeffByPeriodo(Long idPeriodo){
        Query query = em.createNamedQuery(RelacionDetalleEeff.FIND_BY_PERIODO);
        query.setParameter("idPeriodo", idPeriodo);
        return query.getResultList();
    }
    
    public List<RelacionEeff> getRelacionEeffByPeriodoFecu(Long idPeriodo, Long idFecu){
        Query query = em.createNamedQuery(RelacionEeff.FIND_BY_PERIODO_FECU);
        query.setParameter("idPeriodo", idPeriodo);
        query.setParameter("idFecu", idFecu);
        return query.getResultList();
    }
    
    public List<RelacionDetalleEeff> getRelacionDetalleEeffByPeriodoFecuCuenta(Long idPeriodo, Long idFecu, Long idCuenta){
        Query query = em.createNamedQuery(RelacionDetalleEeff.FIND_BY_PERIODO_FECU_CUENTA);
        query.setParameter("idPeriodo", idPeriodo);
        query.setParameter("idFecu", idFecu);
        query.setParameter("idCuenta", idCuenta);
        return query.getResultList();
    }

    /**
     * @param relacionMap
     * @param idPeriodo
     */
    public void persistRelaccionEeff(Map<Celda, List[]> relacionMap, Long idPeriodo){

        for(List[] arrayListas : relacionMap.values()){
            // 0 contiene las RelacionesEeff
            if(Util.esListaValida(arrayListas[0])){
                List<RelacionEeff> relList = arrayListas[0];
                for(RelacionEeff relEeff : relList){
                    em.merge(relEeff);
                }
            }
            
            // 1 contiene las RelacionesDetalleEeff
            if(Util.esListaValida(arrayListas[1])){
                List<RelacionDetalleEeff> relList = arrayListas[1];
                for(RelacionDetalleEeff relDetEeff : relList){
                    em.merge(relDetEeff);
                }
            }
        }
    }

    public void deleteAllRelacionByGrillaPeriodo(Long idPeriodo, Long idGrilla){
        
        Query query = em.createNamedQuery(RelacionEeff.DELETE_BY_GRILLA_PERIODO);
        query.setParameter("idPeriodo", idPeriodo);
        query.setParameter("idGrilla", idGrilla);
        query.executeUpdate();
        
        query = em.createNamedQuery(RelacionDetalleEeff.DELETE_BY_GRILLA_PERIODO);
        query.setParameter("idPeriodo", idPeriodo);
        query.setParameter("idGrilla", idGrilla);
        query.executeUpdate();

    }

    /**
     * @param idVersionEeff
     * @param likeFecu
     * @return
     */
    public List<EstadoFinanciero> getEeffByLikeFecu(Long idVersionEeff, Long likeFecu){
        Query query = em.createNamedQuery(EstadoFinanciero.FIND_BY_LIKE_FECU);
        query.setParameter("idVersionEeff", idVersionEeff);
        query.setParameter("likeFecu", "%"+likeFecu+"%");
        return query.getResultList();
    }
    
    /**
     * @param idVersionEeff
     * @param likeFecu
     * @return
     */
    public List<DetalleEeff> getEeffByLikeCuenta(Long idVersionEeff, Long likeCuenta){
        Query query = em.createNamedQuery(DetalleEeff.FIND_BY_LIKE_CUENTA);
        query.setParameter("idVersionEeff", idVersionEeff);
        query.setParameter("likeCuenta", "%"+likeCuenta+"%");
        return query.getResultList();
    }
    
}
