package cl.mdr.ifrs.ejb.service;


import static ch.lambdaj.Lambda.index;
import static ch.lambdaj.Lambda.on;
import static cl.mdr.ifrs.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import cl.mdr.ifrs.ejb.cross.EeffUtil;
import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.ifrs.ejb.entity.DetalleEeff;
import cl.mdr.ifrs.ejb.entity.EstadoFinanciero;
import cl.mdr.ifrs.ejb.entity.Grilla;
import cl.mdr.ifrs.ejb.entity.RelacionDetalleEeff;
import cl.mdr.ifrs.ejb.entity.RelacionEeff;
import cl.mdr.ifrs.ejb.entity.TipoEstadoEeff;
import cl.mdr.ifrs.ejb.entity.VersionEeff;
import cl.mdr.ifrs.ejb.service.local.EstadoFinancieroServiceLocal;

@Stateless
public class EstadoFinancieroServiceBean implements EstadoFinancieroServiceLocal {
  
    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;

    public EstadoFinancieroServiceBean() {
    }


    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoEstadoEeff> getEstadoEeffFindAll() {
        return em.createNamedQuery("EstadoEeff.findAll").getResultList();
    }
    
    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<VersionEeff> getVersionEeffFindByPeriodo(Long idPeriodo) {
        Query query = em.createNamedQuery(VersionEeff.FIND_BY_PERIOD);
        query.setParameter("idPeriodo", idPeriodo);
        return query.getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
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
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
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
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void persisVersionEeff(VersionEeff version){
        version.setVersion(getMaxVersionByPeriodo(version.getPeriodo().getIdPeriodo()) +1L);
        updateNoVigenteByPeriodo(version.getPeriodo().getIdPeriodo());
        em.persist(version);
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public TipoEstadoEeff getTipoEstadoEeffById(Long idEstadoEeff){
        Query query = em.createNamedQuery(TipoEstadoEeff.FIND_BY_ID);
        query.setParameter("idEstadoEeff", idEstadoEeff);
        return (TipoEstadoEeff)query.getSingleResult();
    }
    
    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EstadoFinanciero> getEeffVigenteByPeriodo(Long idPeriodo){
        Query query = em.createNamedQuery(EstadoFinanciero.FIND_VIGENTE_BY_PERIODO);
        query.setParameter("idPeriodo", idPeriodo);
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EstadoFinanciero> getEeffVigenteByVersion(Long idVersionEeff){
        Query query = em.createNamedQuery(EstadoFinanciero.FIND_BY_VERSION);
        query.setParameter("idVersionEeff", idVersionEeff);
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DetalleEeff> getDetalleEeffByEeff(EstadoFinanciero eeff){
        Query query = em.createNamedQuery(DetalleEeff.FIND_BY_EEFF);
        query.setParameter("eeff", eeff);
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DetalleEeff> getDetalleEeffByVersion(Long idVersionEeff){
        Query query = em.createNamedQuery(DetalleEeff.FIND_BY_VERSION);
        query.setParameter("idVersionEeff", idVersionEeff);
        return query.getResultList();
    }
    
    
    @SuppressWarnings("rawtypes")
	public void persistRelaccionEeff(Map<String, String[]> relacionMap, Long idPeriodo, Grilla grilla){
        
        VersionEeff version =  getVersionEeffVigenteFindByPeriodo(idPeriodo);
        
        List<EstadoFinanciero> eeffs = getEeffVigenteByVersion(version.getIdVersionEeff());
        List<DetalleEeff> detalleEeffs = getDetalleEeffByVersion(version.getIdVersionEeff());
        
        Map<String, Celda> celdaMap = Util.createCeldaMap(grilla);
        Map<Long, EstadoFinanciero>  eeffMap = index(eeffs, on(EstadoFinanciero.class).getIdFecu());
        Map<Long, DetalleEeff>  detalleMap = index(detalleEeffs,on(DetalleEeff.class).getIdCuenta());
        
        List<RelacionEeff> relEeffSaveList = new ArrayList<RelacionEeff>();
        List<RelacionDetalleEeff> relDetSaveList = new ArrayList<RelacionDetalleEeff>();
        
        
        Iterator i = relacionMap.entrySet().iterator();
        while(i.hasNext()){
            
            Map.Entry entry = (Map.Entry)i.next();
            
            String celdaKey = (String) entry.getKey();
            String[] valueKey = (String[] )entry.getValue();
            
            StringTokenizer fecuToken = new StringTokenizer(valueKey[0], ";");
            StringTokenizer cuentaToken = new StringTokenizer(valueKey[1], ";");
            
            Celda celda = celdaMap.get(celdaKey);
            
            if(celda!=null){
                
                while(fecuToken.hasMoreTokens()){
                    String token = fecuToken.nextToken();
                    Long key = EeffUtil.getLongFromKey(token);
                    if(!key.equals(0L)){
                        EstadoFinanciero eeff = eeffMap.get(key);
                        if(eeff != null){
                            RelacionEeff relEeff = new RelacionEeff();
                            relEeff.copyEstadoFinanciero(eeff, celda, version.getPeriodo());
                            em.persist(relEeff);
                        }
                    }
                }
                
                while(cuentaToken.hasMoreTokens()){
                    String token = cuentaToken.nextToken();
                    Long key = EeffUtil.getLongFromKey(token);
                    if(!key.equals(0L)){
                        DetalleEeff detEeff = detalleMap.get(key);
                        if(detEeff != null){
                            RelacionDetalleEeff relDetEeff = new RelacionDetalleEeff();
                            relDetEeff.copyDetalleEeff(detEeff, celda, version.getPeriodo());
                            em.persist(relDetEeff);
                        }
                    }
                }

            }
            
        }
        
    }
}
