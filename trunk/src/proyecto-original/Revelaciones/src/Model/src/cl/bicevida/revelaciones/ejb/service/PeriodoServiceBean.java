package cl.bicevida.revelaciones.ejb.service;


import static cl.bicevida.revelaciones.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;
import cl.bicevida.revelaciones.ejb.entity.Catalogo;
import cl.bicevida.revelaciones.ejb.entity.EstadoCuadro;
import cl.bicevida.revelaciones.ejb.entity.HistorialVersionPeriodo;
import cl.bicevida.revelaciones.ejb.entity.Periodo;
import cl.bicevida.revelaciones.ejb.entity.TipoCuadro;
import cl.bicevida.revelaciones.ejb.entity.VersionPeriodo;
import cl.bicevida.revelaciones.ejb.service.local.PeriodoServiceLocal;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;


@Stateless
public class PeriodoServiceBean implements PeriodoServiceLocal{
    
    private Logger logger = Logger.getLogger(PeriodoServiceBean.class);
    
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;
    
    public PeriodoServiceBean() {
        super();
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Object mergeEntity(VersionPeriodo entity){
        return em.merge(entity);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Object persistEntity(VersionPeriodo entity){
        em.persist(entity);
        return entity;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<VersionPeriodo> findPeriodoAll(){
        Query query = em.createNamedQuery(VersionPeriodo.VERSION_PERIODO_FIND_ALL);
        query.setHint(QueryHints.REFRESH, HintValues.TRUE);
        return query.getResultList();
    }
    public List<VersionPeriodo> findPeriodoAllByPeriodo(Periodo periodo){
            Query query = em.createNamedQuery(VersionPeriodo.VERSION_PERIODO_FIND_ALL_BY_PERIODO);
            query.setParameter("periodo", periodo);
            query.setHint(QueryHints.REFRESH, HintValues.TRUE);
            return query.getResultList();
    }
    

    public Periodo findPeriodoByPeriodo(Long periodo){
        Query query = em.createNamedQuery(Periodo.PERIODO_FIN_BY_PERIODO);
        query.setParameter("periodo", periodo);
        query.setHint(QueryHints.REFRESH, HintValues.TRUE);
        return (Periodo)query.getSingleResult();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<VersionPeriodo> findPeriodoAllByPeriodoCatalogo(Catalogo catalogo,Periodo periodo){
        Query query = em.createNamedQuery(VersionPeriodo.VERSION_PERIODO_FIND_ALL_BY_PERIODO_NOTA);
        query.setParameter("catalogo", catalogo);
        query.setParameter("periodo", periodo); 
        query.setHint(QueryHints.REFRESH, HintValues.TRUE);
        return query.getResultList();
    }


    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<VersionPeriodo> findPeriodoByFiltro(final String usuario, final TipoCuadro tipoCuadro, final Periodo periodo, final EstadoCuadro estadoCuadro, final Long vigente) throws Exception{
        return em.createNamedQuery(VersionPeriodo.VERSION_PERIODO_FIND_BY_FILTRO)
        .setParameter("usuario", usuario)
        .setParameter("tipoCuadro", tipoCuadro != null ? tipoCuadro.getIdTipoCuadro() : null )     
        .setParameter("periodo", periodo != null ? periodo.getIdPeriodo() : null)
        .setParameter("estado", estadoCuadro != null ? estadoCuadro.getIdEstado() : null)
        .setParameter("vigente", vigente)
        .setHint(QueryHints.REFRESH, HintValues.TRUE)
        .getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistFlujoAprobacion(final List<VersionPeriodo> versionPeriodoList, final List<HistorialVersionPeriodo> historialVersionPeriodoList)throws Exception{
        for(VersionPeriodo versionPeriodo : versionPeriodoList){
            versionPeriodo.setFechaUltimoProceso(new Date());
            em.merge(versionPeriodo);
        }
        
        for(HistorialVersionPeriodo historialVersionPeriodo : historialVersionPeriodoList){
            em.merge(historialVersionPeriodo);
        }
    }
    
    public Integer abrirPeriodo(String usuario) throws Exception {
        
            Query query = em.createNamedQuery(Periodo.CALL_SP_ABRIR_PERIODO);    
            query.setParameter("usuario", usuario);
            
            return (Integer) query.getSingleResult();  
    }
    
    public Integer cerrarPeriodo(String usuario, Long idPeriodo) throws Exception {
        
            Query query = em.createNamedQuery(Periodo.CALL_SP_CERRAR_PERIODO);    
            query.setParameter("usuario", usuario);
            query.setParameter("idPeriodo", idPeriodo);
            
            return (Integer) query.getSingleResult();  
    }
    
    
    public Long findMaxPeriodoCerrado() throws Exception {
        Long periodo = null;        
            Query query = em.createNamedQuery(Periodo.FIND_MAX_PERIODO_CERRADO);    
            if (query.getResultList().size() > 0){
                    periodo = (Long) query.getSingleResult();
                    if (periodo == null){
                            periodo = 0L;
                        }
                }
            
            return periodo;
        }
    
    
    public Long findMaxPeriodo() throws Exception {
        Long periodo = null;        
            Query query = em.createNamedQuery(Periodo.FIND_MAX_PERIODO);    
            if (query.getResultList().size() > 0){
                    periodo = (Long) query.getSingleResult();
                }
            
            return periodo;
    }
    
    public Periodo findMaxPeriodoObj(){ 
        Query query = em.createNamedQuery(Periodo.FIND_MAX_PERIODO_OBJ);    
        return (Periodo)query.getSingleResult();
    }

    public Periodo findMaxPeriodoIniciado() throws Exception {
        Periodo periodo = new Periodo();
        Query query = em.createNamedQuery(Periodo.FIND_MAX_PERIODO_INICIADO);
        List<Periodo> periodoResult = query.getResultList();
        if (periodoResult.size() > 0 && !periodoResult.isEmpty() && periodoResult != null) {
            periodo = periodoResult.get(0);           
        }
        return periodo;
    }
    
    public VersionPeriodo findVersionPeriodoById(Long idPeriodo, Long idVersion){
        return (VersionPeriodo)em.createNamedQuery(VersionPeriodo.VERSION_PERIODO_FIND_BY_ID)
            .setParameter("idPeriodo", idPeriodo)
            .setParameter("idVersion", idVersion)
            .getSingleResult();
    }
   
}
