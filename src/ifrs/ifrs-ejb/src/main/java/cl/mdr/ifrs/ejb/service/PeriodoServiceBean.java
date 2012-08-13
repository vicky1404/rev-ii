package cl.mdr.ifrs.ejb.service;


import static cl.mdr.ifrs.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;

import java.sql.CallableStatement;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.internal.SessionImpl;

import cl.mdr.ifrs.ejb.entity.HistorialVersion;
import cl.mdr.ifrs.ejb.entity.Periodo;
import cl.mdr.ifrs.ejb.entity.PeriodoEmpresa;
import cl.mdr.ifrs.ejb.entity.Version;
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
        
        if (query != null && query.getResultList().size() > 0){
        	return (Periodo)query.getSingleResult();
        }
        
        return null;
    }
    
   	
    public Integer abrirPeriodo(String usuario) throws Exception {
    	
    	CallableStatement callableStatement = ((SessionImpl) em.getDelegate()).connection().prepareCall("{call PKG_IFRS_PERIODO.PRC_NUEVO_PERIODO(?,?)}");

    	callableStatement.setString(1, usuario);
    	callableStatement.registerOutParameter(2, Types.INTEGER);
    	callableStatement.execute();
	
    	Integer outputValue = callableStatement.getInt(2);

    	return outputValue;
    	
    }
    
    public Integer cerrarPeriodo(PeriodoEmpresa periodoEmpresa, String usuario) throws Exception {
    	
    	CallableStatement callableStatement = ((SessionImpl) em.getDelegate()).connection().prepareCall("{call PKG_IFRS_PERIODO.PRC_CERRAR_PERIODO(?,?,?,?)}");

    	callableStatement.setLong(1, periodoEmpresa.getIdPeriodo());
    	callableStatement.setLong(2, periodoEmpresa.getIdRut());
    	callableStatement.setString(3, usuario);
    	callableStatement.registerOutParameter(4, Types.INTEGER);
    	callableStatement.execute();
	
    	Integer outputValue = callableStatement.getInt(4);

    	return outputValue;
    	
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
   	
   	@TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistFlujoAprobacion(final List<Version> versionList, final List<HistorialVersion> historialVersionList)throws Exception{
        for(Version version : versionList){
            version.setFechaUltimoProceso(new Date());
            em.merge(version);
        }
        
        for(HistorialVersion historialVersion : historialVersionList){
            em.merge(historialVersion);
        }
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
   	
   	/*METODOS CORRESPONDIENTES AL PERIODO_EMPRESA*/
   	
   	public PeriodoEmpresa getMaxPeriodoEmpresaByEmpresa(Long idRut){
   		Query query = em.createNamedQuery(PeriodoEmpresa.FIND_MAX_BY_EMPRESA);
   		query.setParameter("idRut", idRut);
   		return (PeriodoEmpresa) query.getSingleResult();
   	}
   	
   	@SuppressWarnings("unchecked")
	public List<PeriodoEmpresa> getMaxPeriodoEmpresaNoCerrado(){
   		Query query = em.createNamedQuery(PeriodoEmpresa.FIND_MAX_NO_CERRADO);
   		return query.getResultList();
   	}
   	
   	public PeriodoEmpresa getPeriodoEmpresaById(Long idPeriodo, Long idRut){
   		Query query = em.createNamedQuery(PeriodoEmpresa.FIND_BY_ID);
   		query.setParameter("idPeriodo", idPeriodo);
   		query.setParameter("idRut", idRut);
   		if (query.getResultList().size() > 0){
   			return (PeriodoEmpresa) query.getSingleResult();
   		}
   		return null;
   	}
   
} 
