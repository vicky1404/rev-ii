package cl.mdr.ifrs.ejb.service;


import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static cl.mdr.ifrs.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;
import static org.hamcrest.Matchers.equalTo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import cl.mdr.ifrs.ejb.common.Constantes;
import cl.mdr.ifrs.ejb.common.VigenciaEnum;
import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.ifrs.ejb.entity.CodigoFecu;
import cl.mdr.ifrs.ejb.entity.Columna;
import cl.mdr.ifrs.ejb.entity.CuentaContable;
import cl.mdr.ifrs.ejb.entity.DetalleEeff;
import cl.mdr.ifrs.ejb.entity.EstadoFinanciero;
import cl.mdr.ifrs.ejb.entity.Grilla;
import cl.mdr.ifrs.ejb.entity.RelacionDetalleEeff;
import cl.mdr.ifrs.ejb.entity.RelacionEeff;
import cl.mdr.ifrs.ejb.entity.TipoEstadoEeff;
import cl.mdr.ifrs.ejb.entity.VersionEeff;
import cl.mdr.ifrs.ejb.entity.pk.RelacionDetalleEeffPK;
import cl.mdr.ifrs.ejb.service.local.EstadoFinancieroServiceLocal;

@Stateless
public class EstadoFinancieroServiceBean implements EstadoFinancieroServiceLocal {
	
	@PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;

    public EstadoFinancieroServiceBean() {
    }


    @SuppressWarnings("unchecked")
	public List<TipoEstadoEeff> getEstadoEeffFindAll() {
        return em.createNamedQuery("EstadoEeff.findAll").getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<VersionEeff> getVersionEeffFindByPeriodo(Long idPeriodo, Long idRut) {
        Query query = em.createNamedQuery(VersionEeff.FIND_BY_PERIODO_EMPRESA);
        query.setParameter("idPeriodo", idPeriodo);
        query.setParameter("idRut", idRut);
        return query.getResultList();
    }
    
    public VersionEeff getVersionEeffVigenteFindByPeriodo(Long idPeriodo, Long idRut) {
        Query query = em.createNamedQuery(VersionEeff.FIND_VIGENTE_BY_PERIODO_EMPRESA);
        query.setParameter("idPeriodo", idPeriodo);
        query.setParameter("idRut", idRut);
        return (VersionEeff)query.getSingleResult();
    }
    
    public void persistEeffMap(Map<Long, EstadoFinanciero> eeffMap){ 
        for(EstadoFinanciero eeff : eeffMap.values()){
            em.persist(eeff);
        }
    }
    
    public Long getMaxVersionByPeriodo(Long idPeriodo, Long idRut){
        Long maxVersion = (Long) em.createNamedQuery(VersionEeff.FIN_MAX_VERSION_BY_PERIODO_EMPRESA)
            .setParameter("idPeriodo", idPeriodo)
            .setParameter("idRut", idRut)
            .getSingleResult();
        return maxVersion==null?0L:maxVersion;
    }
    
    public void updateNoVigenteByPeriodo(Long idPeriodo, Long idRut, Long vigencia){
    	
    	em.createNativeQuery(" UPDATE IFRS_VERSION_EEFF SET VIGENCIA = ? WHERE ID_PERIODO = ? AND ID_RUT = ? " )
        .setParameter(1, vigencia)
        .setParameter(2, idPeriodo)
        .setParameter(3, idRut)
        .executeUpdate();
    	
    }
    
    public void persisVersionEeff(VersionEeff version){
    	
    	boolean b[] = {false, true,true,true,true,true,true,true}; 
    	
    	
    	
    	if(version.getIdVersionEeff() != null)
    		return;
    	
        version.setVersion(getMaxVersionByPeriodo(version.getPeriodoEmpresa().getIdPeriodo(), version.getPeriodoEmpresa().getIdRut()) +1L);
        updateNoVigenteByPeriodo(version.getPeriodoEmpresa().getIdPeriodo(), version.getPeriodoEmpresa().getIdRut(), VigenciaEnum.NO_VIGENTE.getKey());
        
        //BigDecimal seq =  (BigDecimal) em.createNativeQuery(" SELECT SEQ_VERSION_EEFF.NEXTVAL FROM DUAL ").getSingleResult();
        BigDecimal idVersion = (BigDecimal) em.createNativeQuery("select (MAX(ID_VERSION_EEFF)+1) from "+Constantes.VERSION_EEFF+"").getSingleResult();
        
        if (idVersion == null)
        	idVersion = new BigDecimal(1);
        
        Query query = em.createNativeQuery(" SET IDENTITY_INSERT "+ Constantes.VERSION_EEFF +" ON; Insert into " + Constantes.VERSION_EEFF + " (ID_VERSION_EEFF,ID_PERIODO, ID_RUT ,ID_ESTADO_EEFF,VERSION,VIGENCIA,USUARIO,FECHA) values (?,?,?,?,?,?,?,?); SET IDENTITY_INSERT "+Constantes.VERSION_EEFF+" OFF; " )
        .setParameter(1, idVersion)
        .setParameter(2, version.getPeriodoEmpresa().getIdPeriodo())
        .setParameter(3, version.getPeriodoEmpresa().getIdRut())
        .setParameter(4, version.getTipoEstadoEeff().getIdEstadoEeff())
        .setParameter(5, version.getVersion())
        .setParameter(6, version.getVigencia())
        .setParameter(7, version.getUsuario())
        .setParameter(8, new Date());
        
        query.executeUpdate();
        
        //version.setIdVersionEeff(seq.longValue());
        version.setIdVersionEeff(idVersion.longValue());
        
        if(Util.esListaValida(version.getEstadoFinancieroList())){
        
        	for(EstadoFinanciero eeff : version.getEstadoFinancieroList()) {
		        em.createNativeQuery(" Insert into " + Constantes.EEFF + " (ID_VERSION_EEFF, ID_FECU, MONTO_TOTAL) values (?,?,?) ")
		        .setParameter(1, idVersion)
		        .setParameter(2, eeff.getIdFecu())
		        .setParameter(3, eeff.getMontoTotal())
		        .executeUpdate();
		        eeff.setIdVersionEeff(idVersion.longValue());
		        
		        if(Util.esListaValida(eeff.getDetalleEeffList4())){
		        	
		        	for(DetalleEeff detEeff : eeff.getDetalleEeffList4()){
		        		em.createNativeQuery(" Insert into " + Constantes.DETALLE_EEFF + " (ID_CUENTA,ID_FECU,ID_VERSION_EEFF,MONTO_PESOS,MONTO_MILES,MONTO_PESOS_MIL) " +
		        																		 " values " +
		        																		 " (?,?,?,?,?,?) ")
		        		.setParameter(1, detEeff.getIdCuenta())
		        		.setParameter(2, detEeff.getIdFecu())
		        		.setParameter(3, idVersion)		        		
		        		.setParameter(4, detEeff.getMontoPesos())
		        		.setParameter(5, detEeff.getMontoMilesValidarMapeo())
		        		.setParameter(6, detEeff.getMontoXBRL())
		        		.executeUpdate();
		        	}
		        	
		        }
        	}
        
        }
        
    }
    
    public TipoEstadoEeff getTipoEstadoEeffById(Long idEstadoEeff){
        Query query = em.createNamedQuery(TipoEstadoEeff.FIND_BY_ID);
        query.setParameter("idEstadoEeff", idEstadoEeff);
        return (TipoEstadoEeff)query.getSingleResult();
    }
    
    @SuppressWarnings("unchecked")
    public List<EstadoFinanciero> getEeffVigenteByPeriodo(Long idPeriodo, Long idRut){
        Query query = em.createNamedQuery(EstadoFinanciero.FIND_VIGENTE_BY_PERIODO_EMPRESA);
        query.setParameter("idPeriodo", idPeriodo);
        query.setParameter("idRut", idRut);
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<EstadoFinanciero> getEeffByVersion(Long idVersionEeff){
        Query query = em.createNamedQuery(EstadoFinanciero.FIND_BY_VERSION);
        query.setParameter("idVersionEeff", idVersionEeff);
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<EstadoFinanciero> getEeffEagerByVersion(Long idVersionEeff){
        Query query = em.createNamedQuery(EstadoFinanciero.FIND_EAGER_BY_VERSION);
        query.setParameter("idVersionEeff", idVersionEeff);
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<DetalleEeff> getDetalleEeffByEeff(EstadoFinanciero eeff){
        Query query = em.createNamedQuery(DetalleEeff.FIND_BY_EEFF);
        query.setParameter("eeff", eeff);
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
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
    
    @SuppressWarnings("unchecked")
    public List<RelacionEeff> getRelacionEeffByPeriodo(Long idPeriodo, Long idRut){
        Query query = em.createNamedQuery(RelacionEeff.FIND_BY_PERIODO_EMPRESA);
        query.setParameter("idPeriodo", idPeriodo);
        query.setParameter("idRut", idRut);
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<RelacionDetalleEeff> getRelacionDetalleEeffByPeriodo(Long idPeriodo, Long idRut){
        Query query = em.createNamedQuery(RelacionDetalleEeff.FIND_BY_PERIODO_EMPRESA);
        query.setParameter("idPeriodo", idPeriodo);
        query.setParameter("idRut", idRut);
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<RelacionEeff> getRelacionEeffByPeriodoFecu(Long idPeriodo, Long idRut, Long idFecu){
        Query query = em.createNamedQuery(RelacionEeff.FIND_BY_PERIODO_FECU);
        query.setParameter("idPeriodo", idPeriodo);
        query.setParameter("idRut", idRut);
        query.setParameter("idFecu", idFecu);
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<RelacionDetalleEeff> getRelacionDetalleEeffByPeriodoFecuCuenta(Long idPeriodo, Long idRut, Long idFecu, Long idCuenta){
        Query query = em.createNamedQuery(RelacionDetalleEeff.FIND_BY_PERIODO_FECU_CUENTA);
        query.setParameter("idPeriodo", idPeriodo);
        query.setParameter("idRut", idRut);
        query.setParameter("idFecu", idFecu);
        query.setParameter("idCuenta", idCuenta);
        return query.getResultList();
    }

    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void persistRelaccionEeff(Map<Celda, List[]> relacionMap) throws Exception{
    	
    	Iterator it = relacionMap.entrySet().iterator();
		
    	while (it.hasNext()) {    		
    		Map.Entry e = (Map.Entry)it.next();
    		this.deleteRelacionEeff((Celda) e.getKey());
    		this.deleteRelacionDetalleEeff((Celda) e.getKey());
    	}
    	
    	
        for(List[] arrayListas : relacionMap.values()){
            if(Util.esListaValida(arrayListas[0])){
                List<RelacionEeff> relList = arrayListas[0];
                for(RelacionEeff relEeff : relList){
                	this.insertRelacionEeff(relEeff);
                }
            }
            if(Util.esListaValida(arrayListas[1])){
                List<RelacionDetalleEeff> relList = arrayListas[1];
                for(RelacionDetalleEeff relDetEeff : relList){
                	this.insertRelacionDetalleEeff(relDetEeff);
                }
            }
        }
    }

    public void deleteAllRelacionByGrillaPeriodo(Long idPeriodo, Long idRut, Long idGrilla){
        
        Query query = em.createNamedQuery(RelacionEeff.DELETE_BY_GRILLA_PERIODO_EMPRESA);
        query.setParameter("idPeriodo", idPeriodo);
        query.setParameter("idRut", idRut);
        query.setParameter("idGrilla", idGrilla);
        query.executeUpdate();
        
        query = em.createNamedQuery(RelacionDetalleEeff.DELETE_BY_GRILLA_PERIODO_EMPRESA);
        query.setParameter("idPeriodo", idPeriodo);
        query.setParameter("idRut", idRut);
        query.setParameter("idGrilla", idGrilla);
        query.executeUpdate();

    }

    /**
     * @param idVersionEeff
     * @param likeFecu
     * @return
     */
    @SuppressWarnings("unchecked")
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
    @SuppressWarnings("unchecked")
    public List<DetalleEeff> getEeffByLikeCuenta(Long idVersionEeff, Long likeCuenta){
        Query query = em.createNamedQuery(DetalleEeff.FIND_BY_LIKE_CUENTA);
        query.setParameter("idVersionEeff", idVersionEeff);
        query.setParameter("likeCuenta", "%"+likeCuenta+"%");
        return query.getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public RelacionDetalleEeff getRelacionDetalleEeffByRelacionDetalleEeff(RelacionDetalleEeff relacionDetalleEeff) {
    	
    	RelacionDetalleEeffPK pk = new RelacionDetalleEeffPK();
    		pk.setIdCuenta(relacionDetalleEeff.getIdCuenta());
    		pk.setIdFecu(relacionDetalleEeff.getIdFecu());
    		pk.setIdPeriodo(relacionDetalleEeff.getIdPeriodo());
    		pk.setIdRut(relacionDetalleEeff.getIdRut());
    		pk.setIdGrilla(relacionDetalleEeff.getIdGrilla());
    		pk.setIdColumna(relacionDetalleEeff.getIdColumna());
    		pk.setIdFila(relacionDetalleEeff.getIdFila());
    	
       return (RelacionDetalleEeff) em.find(RelacionDetalleEeff.class, pk);
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
    
    
    private void deleteRelacionEeff(Celda celda) throws Exception{
    	StringBuffer sql = new StringBuffer();
    		sql.append(" DELETE " + Constantes.RELACION_EEFF + " WHERE ID_COLUMNA = ? AND ID_FILA = ? AND ID_GRILLA = ?");
    		Query query = em.createNativeQuery(sql.toString());
        	int contador = 0;
        	query.setParameter(++contador, celda.getIdColumna());
        	query.setParameter(++contador, celda.getIdFila());
        	query.setParameter(++contador, celda.getIdGrilla());
        	query.executeUpdate();
    }
    
    
    private void deleteRelacionDetalleEeff(Celda celda) throws Exception{
    	StringBuffer sql = new StringBuffer();
    		sql.append(" DELETE " + Constantes.RELACION_DETALLE_EEFF + " WHERE ID_COLUMNA = ? AND ID_FILA = ? AND ID_GRILLA = ?");
    		Query query = em.createNativeQuery(sql.toString());
    		
        	int contador = 0;
        	query.setParameter(++contador, celda.getIdColumna());
        	query.setParameter(++contador, celda.getIdFila());
        	query.setParameter(++contador, celda.getIdGrilla());
        	query.executeUpdate();
    }
    
    private void insertRelacionDetalleEeff(RelacionDetalleEeff relDetalleEeff) throws Exception{
    	StringBuffer sql = new StringBuffer();
    	sql.append(" INSERT INTO " + Constantes.RELACION_DETALLE_EEFF )
    	.append(" (ID_COLUMNA,ID_CUENTA,ID_FECU,ID_FILA,ID_GRILLA,ID_PERIODO,ID_RUT,MONTO_MILES,MONTO_PESOS, MONTO_PESOS_MIL) values ")
    	.append("  (?,?,?,?,?,?,?,?,?,?) ");
    		Query query = em.createNativeQuery(sql.toString());
        	int contador = 0;
        	query.setParameter(++contador, relDetalleEeff.getIdColumna());
        	query.setParameter(++contador, relDetalleEeff.getIdCuenta());        	
        	query.setParameter(++contador, relDetalleEeff.getIdFecu());
        	query.setParameter(++contador, relDetalleEeff.getIdFila());
        	query.setParameter(++contador, relDetalleEeff.getIdGrilla());
        	query.setParameter(++contador, relDetalleEeff.getPeriodoEmpresa().getIdPeriodo());        	
        	query.setParameter(++contador, relDetalleEeff.getPeriodoEmpresa().getIdRut());
        	query.setParameter(++contador, relDetalleEeff.getMontoMilesValidarMapeo());
        	query.setParameter(++contador, relDetalleEeff.getMontoPesos());        	
        	query.setParameter(++contador, relDetalleEeff.getMontoXBRL());
        	query.executeUpdate();
    }
    
    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RelacionEeff> getRelacionEeffByCelda(Celda celda){
        Query query = em.createNamedQuery(RelacionEeff.FIND_BY_CELDA);
        query.setParameter("celda", celda);
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<RelacionDetalleEeff> getRelacionDetalleEeffByCelda(Celda celda){
        Query query = em.createNamedQuery(RelacionDetalleEeff.FIND_BY_CELDA);
        query.setParameter("celda", celda);
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RelacionEeff> getRelacionEeffByGrilla(Grilla idGrilla){
        //Query query = em.createNamedQuery(RelacionEeff.FIND_BY_GRILLA);
    	String sqlString = " select distinct * from " + Constantes.RELACION_EEFF + " where id_grilla = " + idGrilla.getIdGrilla() + " order by id_columna, id_fila";
    	Query query = em.createNativeQuery(sqlString, RelacionEeff.class);
        //query.setParameter("idGrilla", idGrilla);
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<RelacionDetalleEeff> getRelacionDetalleEeffByCelda(Long idGrilla){
        Query query = em.createNamedQuery(RelacionDetalleEeff.FIND_BY_GRILLA);
        query.setParameter("idGrilla", idGrilla);
        return query.getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void loadEEFFByGrilla(final Grilla grid){
    	
    	//List<RelacionEeff> relacionEeffList = this.getRelacionEeffByGrilla(grid.getIdGrilla());
    	List<RelacionEeff> relacionEeffList = new ArrayList<RelacionEeff>(); 
    			
    	relacionEeffList = this.getRelacionEeffByGrilla(grid);
    	
    	List<RelacionDetalleEeff> relacionDetEeffList = this.getRelacionDetalleEeffByCelda(grid.getIdGrilla());
    	
    	boolean isRelEeff = Util.esListaValida(relacionEeffList);
    	boolean isRelDetEeff = Util.esListaValida(relacionDetEeffList);
    	
    	for(Columna column : grid.getColumnaList()){
            
            for(Celda cell : column.getCeldaList()){
                
            	if(isRelEeff){
            		List<RelacionEeff> relacionEeffTempList = select(relacionEeffList, having
            												   (on(RelacionEeff.class).getIdGrilla(), equalTo(cell.getIdGrilla()))
            											   .and(having(on(RelacionEeff.class).getIdColumna(), equalTo(cell.getIdColumna())))
            											   .and(having(on(RelacionEeff.class).getIdFila(), equalTo(cell.getIdFila()))));
            		
            		cell.setRelacionEeffList(relacionEeffTempList);
            	}else{
            		cell.setRelacionEeffList(new ArrayList<RelacionEeff>());
            	}

            	
            	if(isRelDetEeff){
            		List<RelacionDetalleEeff> relacionDetEeffTempList = select(relacionDetEeffList, having
						   										(on(RelacionDetalleEeff.class).getIdGrilla(), equalTo(cell.getIdGrilla()))
						   										.and(having(on(RelacionDetalleEeff.class).getIdColumna(), equalTo(cell.getIdColumna())))
						   										.and(having(on(RelacionDetalleEeff.class).getIdFila(), equalTo(cell.getIdFila()))));
            	
            		cell.setRelacionDetalleEeffList(relacionDetEeffTempList);
            	}else{
            		cell.setRelacionDetalleEeffList(new ArrayList<RelacionDetalleEeff>());
            	}
            	

            }
    	}
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void loadEEFFByCelda(final Celda cell){

        cell.setRelacionEeffList(this.getRelacionEeffByCelda(cell));
        cell.setRelacionDetalleEeffList(this.getRelacionDetalleEeffByCelda(cell));
        
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<CodigoFecu> getCodigoFecuAll() {
        Query query = em.createNamedQuery(CodigoFecu.FIND_ALL);
        return query.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<CuentaContable> getCuentaContableAll() {
        Query query = em.createNamedQuery(CuentaContable.FIND_ALL);
        return query.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public boolean validaContieneMapping(final Long idPeriodo, final Long idGrilla) {
        final List<Long> mappingFecuList =
            em.createQuery("select o.idPeriodo from RelacionEeff o where o.idPeriodo =:idPeriodo and o.idGrilla =:idGrilla")
            .setParameter("idPeriodo", idPeriodo)
            .setParameter("idGrilla", idGrilla)
            .getResultList();
        final List<Long> mappingCuentaContableList =
            em.createQuery("select o.idPeriodo from RelacionDetalleEeff o where o.idPeriodo =:idPeriodo and o.idGrilla =:idGrilla")
            .setParameter("idPeriodo", idPeriodo)
            .setParameter("idGrilla", idGrilla)
            .getResultList();
        if ((!mappingFecuList.isEmpty() && mappingFecuList.size() > 0) ||
            (!mappingCuentaContableList.isEmpty() && mappingCuentaContableList.size() > 0)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

}
