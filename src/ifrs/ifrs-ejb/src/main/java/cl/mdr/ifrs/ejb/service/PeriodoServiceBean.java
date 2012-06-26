package cl.mdr.ifrs.ejb.service;


import static cl.mdr.ifrs.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import cl.mdr.ifrs.ejb.entity.Periodo;
import cl.mdr.ifrs.ejb.service.local.PeriodoServiceLocal;

@Stateless
public class PeriodoServiceBean implements PeriodoServiceLocal{
    
    @SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(PeriodoServiceBean.class);
    
    
    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;
    
    public PeriodoServiceBean() {
        super();
    }
    
   
   	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Periodo findPeriodoByPeriodo(Long periodo){
        Query query = em.createNamedQuery(Periodo.PERIODO_FIN_BY_PERIODO);
        query.setParameter("periodo", periodo);        
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
    
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
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
    
    
   	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long findMaxPeriodo() throws Exception {
        Long periodo = null;        
            Query query = em.createNamedQuery(Periodo.FIND_MAX_PERIODO);    
            if (query.getResultList().size() > 0){
                    periodo = (Long) query.getSingleResult();
                }
            
            return periodo;
    }
    
   	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Periodo findMaxPeriodoObj(){ 
        Query query = em.createNamedQuery(Periodo.FIND_MAX_PERIODO_OBJ);    
        return (Periodo)query.getSingleResult();
    }

   	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
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
