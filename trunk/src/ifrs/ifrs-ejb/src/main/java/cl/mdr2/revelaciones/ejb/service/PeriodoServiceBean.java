package cl.bicevida.revelaciones.ejb.service;


import static cl.bicevida.revelaciones.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;
import cl.bicevida.revelaciones.ejb.entity.Periodo;
import cl.bicevida.revelaciones.ejb.service.local.PeriodoServiceLocal;

import java.util.List;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;

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

    public Periodo findPeriodoByPeriodo(Long periodo){
        Query query = em.createNamedQuery(Periodo.PERIODO_FIN_BY_PERIODO);
        query.setParameter("periodo", periodo);
        query.setHint(QueryHints.REFRESH, HintValues.TRUE);
        return (Periodo)query.getSingleResult();
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
   
}
