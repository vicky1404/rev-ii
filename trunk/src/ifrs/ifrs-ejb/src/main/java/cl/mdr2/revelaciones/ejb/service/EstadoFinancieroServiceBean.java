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
    
    public List<EstadoFinanciero> getEeffVigenteByVersion(Long idVersionEeff){
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
