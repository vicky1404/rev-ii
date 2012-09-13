package cl.mdr.ifrs.ejb.service;


import static ch.lambdaj.Lambda.index;
import static ch.lambdaj.Lambda.on;
import static cl.mdr.ifrs.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;

import java.util.Date;
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

import cl.mdr.ifrs.ejb.common.Constantes;
import cl.mdr.ifrs.ejb.cross.EeffUtil;
import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.ifrs.ejb.entity.DetalleEeff;
import cl.mdr.ifrs.ejb.entity.EstadoFinanciero;
import cl.mdr.ifrs.ejb.entity.Grilla;
import cl.mdr.ifrs.ejb.entity.PeriodoEmpresa;
import cl.mdr.ifrs.ejb.entity.RelacionDetalleEeff;
import cl.mdr.ifrs.ejb.entity.RelacionEeff;
import cl.mdr.ifrs.ejb.entity.TipoEstadoEeff;
import cl.mdr.ifrs.ejb.entity.Version;
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
	public EstadoFinanciero getEstadoFinancieroByEstadoFinanciero(EstadoFinanciero estadoFinanciero) {
    	EstadoFinanciero objeto = new EstadoFinanciero();
    	
    	try
    	{
    		objeto = (EstadoFinanciero) em.find(EstadoFinanciero.class, estadoFinanciero);
    		
    	} catch (Exception ex){
    		
    		ex.printStackTrace();
    	}
    	
    	
       return objeto;
    }   
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public RelacionDetalleEeff getRelacionDetalleEeffByRelacionDetalleEeff(RelacionDetalleEeff relacionDetalleEeff) {
    	RelacionDetalleEeff objeto = new RelacionDetalleEeff();
    	
    	try
    	{
    		objeto = (RelacionDetalleEeff) em.find(RelacionDetalleEeff.class, relacionDetalleEeff);
    		
    	} catch (Exception ex){
    		
    		ex.printStackTrace();
    	}
    	
    	
       return objeto;
    }   
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public VersionEeff getVersionEeffVigenteFindByPeriodo(Long idPeriodo) {
        Query query = em.createNamedQuery(VersionEeff.FIND_VIGENTE_BY_PERIOD);
        query.setParameter("idPeriodo", idPeriodo);
        
        if (query.getResultList().size() > 0){
        	return (VersionEeff)query.getSingleResult();
        }
        
        return null;
    }
    
    public void persistEeffMap(Map<Long, EstadoFinanciero> eeffMap){ 
        for(EstadoFinanciero eeff : eeffMap.values()){
            em.persist(eeff);
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long getMaxVersionByPeriodoEmpresa(PeriodoEmpresa periodoEmpresa){
        Long maxVersion = (Long) em.createNamedQuery(VersionEeff.FIN_MAX_VERSION_BY_PERIODO_EMPRESA)
            .setParameter("idPeriodo", periodoEmpresa.getIdPeriodo())
            .setParameter("idRut", periodoEmpresa.getIdRut())
            .getSingleResult();
        return maxVersion==null?0L:maxVersion;
    }
    
    public void updateNoVigenteByPeriodoEmpresa(PeriodoEmpresa periodoEmpresa){
    	
    	em.createNativeQuery("update " + Constantes.VERSION_EEFF + " o set o.VIGENCIA = :valor where o.ID_PERIODO = :idPeriodo and o.ID_RUT = :idRut")
    	.setParameter("idPeriodo", periodoEmpresa.getIdPeriodo())
        .setParameter("idRut", periodoEmpresa.getIdRut())
        .setParameter("valor", 0L)
        .executeUpdate();
    	
    	/*
        em.createNamedQuery(VersionEeff.UPDATE_VIGENCIA_BY_PERIODO_EMPRESA)
            .setParameter("idPeriodo", periodoEmpresa.getIdPeriodo())
            .setParameter("idRut", periodoEmpresa.getIdRut())
            .setParameter("valor", 0L)
            .executeUpdate();
       */
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persisVersionEeff(VersionEeff version){
    	Long versionNueva = getMaxVersionByPeriodoEmpresa(version.getPeriodoEmpresa()) + 1L;
        
        if (versionNueva.compareTo(1L) > 0){
        	updateNoVigenteByPeriodoEmpresa(version.getPeriodoEmpresa());
        }
        
        version.setVersion(versionNueva);
        //em.persist(version);
        
        int indice = 0;
        StringBuffer insertVersion = new StringBuffer();
        insertVersion.append(" insert into " + Constantes.VERSION_EEFF + " (FECHA, ID_PERIODO, ID_RUT, ID_ESTADO_EEFF, usuario, version, vigencia, ID_VERSION_EEFF) ")
        .append(" values (?,?,?,?,?,?,?,?) ");
        
        em.createNativeQuery(insertVersion.toString())
        .setParameter(++indice, new Date())
        .setParameter(++indice, version.getPeriodoEmpresa().getIdPeriodo())
        .setParameter(++indice, version.getPeriodoEmpresa().getIdRut())
        .setParameter(++indice, version.getTipoEstadoEeff().getIdEstadoEeff())
        .setParameter(++indice, version.getUsuario())
        .setParameter(++indice, version.getVersion())
        .setParameter(++indice, version.getVigencia())
        .setParameter(++indice, version.getVersion()).executeUpdate();
        

        StringBuffer insertEeff = new StringBuffer();
        insertEeff.append("insert into IFRS_EEFF (ID_FECU, MONTO_TOTAL, ID_VERSION_EEFF) values (?, ?, ?) ");

        
        for (EstadoFinanciero estadoFinanciero : version.getEstadoFinancieroList()){
            indice = 0;            
            em.createNativeQuery(insertEeff.toString())
            .setParameter(++indice, estadoFinanciero.getIdFecu())
            .setParameter(++indice, estadoFinanciero.getMontoTotal())
            .setParameter(++indice, version.getVersion()).executeUpdate();
        }
        
        

    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public TipoEstadoEeff getTipoEstadoEeffById(Long idEstadoEeff){
        Query query = em.createNamedQuery(TipoEstadoEeff.FIND_BY_ID);
        query.setParameter("idEstadoEeff", idEstadoEeff);
        
        if (query.getResultList().size() > 0){
        	return (TipoEstadoEeff)query.getSingleResult();	
        }
        
        return null;
        
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
                            relEeff.copyEstadoFinanciero(eeff, celda, version.getPeriodoEmpresa());
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
                            relDetEeff.copyDetalleEeff(detEeff, celda, version.getPeriodoEmpresa());
                            em.persist(relDetEeff);
                        }
                    }
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
     * @param relacionMap
     * @param idPeriodo
     * @throws Exception 
     */
    public void persistRelaccionEeff(Map<Celda, List[]> relacionMap, Long idPeriodo) throws Exception{

        for(List[] arrayListas : relacionMap.values()){
            // 0 contiene las RelacionesEeff
            if(Util.esListaValida(arrayListas[0])){
                List<RelacionEeff> relList = arrayListas[0];
                for(RelacionEeff relEeff : relList){
                    //em.merge(relEeff);
                	this.insertRelacionEeff(relEeff);
                }
            }
            
            // 1 contiene las RelacionesDetalleEeff
            if(Util.esListaValida(arrayListas[1])){
                List<RelacionDetalleEeff> relList = arrayListas[1];
                for(RelacionDetalleEeff relDetEeff : relList){
                    //em.merge(relDetEeff);
                	this.insertRelacionDetalleEeff(relDetEeff);
                }
            }
        }
    }
    
    private void insertRelacionEeff(RelacionEeff relEeff) throws Exception{
    	
    	StringBuffer sql = new StringBuffer();
    	
    	
    	sql.append(" INSERT INTO " + Constantes.RELACION_EEFF + " ( ID_COLUMNA,ID_FECU,ID_FILA,ID_GRILLA,ID_PERIODO,ID_RUT,MONTO_TOTAL) values ");
    	sql.append("  (?,?,?,?,?,?,?) ");
    	
    	
    		
    		Query query = em.createNativeQuery(sql.toString());
        	int contador = 0;
        	query.setParameter(++contador, relEeff.getIdColumna());
        	query.setParameter(++contador, relEeff.getIdFecu());
        	query.setParameter(++contador, relEeff.getIdFila());
        	query.setParameter(++contador, relEeff.getIdGrilla());
        	query.setParameter(++contador, relEeff.getPeriodoEmpresa().getIdPeriodo());        	
        	query.setParameter(++contador, relEeff.getPeriodoEmpresa().getIdRut());
        	query.setParameter(++contador, relEeff.getMontoTotal());
        	
        	query.executeUpdate();
    		
    		
    	
    }
    
    
    private void insertRelacionDetalleEeff(RelacionDetalleEeff relDetalleEeff) throws Exception{
    	
    	StringBuffer sql = new StringBuffer();
    	
    	
    	sql.append(" INSERT INTO " + Constantes.RELACION_DETALLE_EEFF )
    	.append(" (ID_COLUMNA,ID_CUENTA,ID_FECU,ID_FILA,ID_GRILLA,ID_PERIODO,ID_RUT,MONTO_EBS,MONTO_MILES,MONTO_PESOS,MONTO_RECLASIFICACION) values ")
    	.append("  (?,?,?,?,?,?,?,?,?,?,?,?) ");
    	
    	
    		
    		Query query = em.createNativeQuery(sql.toString());
        	int contador = 0;
        	query.setParameter(++contador, relDetalleEeff.getIdColumna());
        	query.setParameter(++contador, relDetalleEeff.getIdCuenta());        	
        	query.setParameter(++contador, relDetalleEeff.getIdFecu());
        	query.setParameter(++contador, relDetalleEeff.getIdFila());
        	query.setParameter(++contador, relDetalleEeff.getIdGrilla());
        	query.setParameter(++contador, relDetalleEeff.getPeriodoEmpresa().getIdPeriodo());        	
        	query.setParameter(++contador, relDetalleEeff.getPeriodoEmpresa().getIdRut());
        	query.setParameter(++contador, relDetalleEeff.getMontoEbs());
        	query.setParameter(++contador, relDetalleEeff.getMontoMiles());
        	query.setParameter(++contador, relDetalleEeff.getMontoPesos());
        	query.setParameter(++contador, relDetalleEeff.getMontoReclasificacion());
        	query.executeUpdate();
    		
    		
    	
    }
    
    public void deleteRelacionAllEeffByCelda(Celda celda){
        deleteRelacionEeffByCelda(celda);
        deleteRelacionDetalleEeffByCelda(celda);
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
    
    /**
     * @param idVersionEeff
     * @param likeFecu
     * @return
     */
    public List<EstadoFinanciero> getEeffByLikeFecu(Long idVersionEeff, Long likeFecu){
        Query query = em.createNamedQuery(EstadoFinanciero.FIND_BY_LIKE_FECU);
        query.setParameter("idVersionEeff", idVersionEeff);
        query.setParameter("likeFecu","%"+likeFecu+"%");
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
    
    public List<EstadoFinanciero> getEeffByVersion(Long idVersionEeff){
        Query query = em.createNamedQuery(EstadoFinanciero.FIND_BY_VERSION);
        query.setParameter("idVersionEeff", idVersionEeff);
        return query.getResultList();
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
   
}
