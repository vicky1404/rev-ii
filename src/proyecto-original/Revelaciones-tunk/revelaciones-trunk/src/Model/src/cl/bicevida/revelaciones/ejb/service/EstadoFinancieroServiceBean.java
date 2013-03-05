package cl.bicevida.revelaciones.ejb.service;


import cl.bicevida.revelaciones.eeff.CargadorEeffVO;
import cl.bicevida.revelaciones.ejb.common.EstadoCuadroEnum;
import cl.bicevida.revelaciones.ejb.common.VigenciaEnum;
import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.CodigoFecu;
import cl.bicevida.revelaciones.ejb.entity.CuentaContable;
import cl.bicevida.revelaciones.ejb.entity.DetalleEeff;
import cl.bicevida.revelaciones.ejb.entity.EstadoCuadro;
import cl.bicevida.revelaciones.ejb.entity.EstadoFinanciero;
import cl.bicevida.revelaciones.ejb.entity.GrupoEeff;
import cl.bicevida.revelaciones.ejb.entity.HistorialVersionPeriodo;
import cl.bicevida.revelaciones.ejb.entity.RelacionDetalleEeff;
import cl.bicevida.revelaciones.ejb.entity.RelacionEeff;
import cl.bicevida.revelaciones.ejb.entity.TipoEstadoEeff;
import cl.bicevida.revelaciones.ejb.entity.Version;
import cl.bicevida.revelaciones.ejb.entity.VersionEeff;
import cl.bicevida.revelaciones.ejb.entity.VersionPeriodo;
import cl.bicevida.revelaciones.ejb.facade.local.FacadeServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.EstadoFinancieroServiceLocal;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Stateless
public class EstadoFinancieroServiceBean implements EstadoFinancieroServiceLocal {
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "revelacionesPU")
    private EntityManager em;
    
    @EJB
    private FacadeServiceLocal facadeService;

    public EstadoFinancieroServiceBean() {
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<TipoEstadoEeff> getEstadoEeffFindAll() {
        return em.createNamedQuery("EstadoEeff.findAll").getResultList();
    }

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

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void persistEeffMap(Map<Long, EstadoFinanciero> eeffMap) {
        for (EstadoFinanciero eeff : eeffMap.values()) {
            em.persist(eeff);
        }
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long getMaxVersionByPeriodo(Long idPeriodo) {
        Long maxVersion =
            (Long)em.createNamedQuery(VersionEeff.FIN_MAX_VERSION_BY_PERIODO).setParameter("idPeriodo", idPeriodo).getSingleResult();
        return maxVersion == null ? 0L : maxVersion;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void updateNoVigenteByPeriodo(Long idPeriodo) {
        em.createNamedQuery(VersionEeff.UPDATE_VIGENCIA_BY_PERIODO).setParameter("idPeriodo",
                                                                                 idPeriodo).executeUpdate();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persisVersionEeff(final VersionEeff versionEeff, final CargadorEeffVO cargadorVO, final String usuario) throws Exception{
        
        versionEeff.setVersion(getMaxVersionByPeriodo(versionEeff.getPeriodo().getIdPeriodo()) + 1L);
        versionEeff.setFecha(new Date());
        updateNoVigenteByPeriodo(versionEeff.getPeriodo().getIdPeriodo());
        em.persist(versionEeff);
        
        Collection<Long> idGrillaColl = cargadorVO.getGrillaNoValida().values();
        
        if(Util.esListaValida(idGrillaColl)){
            
            for(Long idGrilla : idGrillaColl){
               
                Version version = facadeService.getVersionService().findVersionByIdEstructura(idGrilla);
                version.setValidadoEeff(VigenciaEnum.NO_VIGENTE.getKey());
                em.merge(version);
                
                List<VersionPeriodo> versionPeriodoList = facadeService.getPeriodoService().findVersionPeriodoByIdVersion(version.getIdVersion());
                
                if(Util.esListaValida(versionPeriodoList)){
                
                for(VersionPeriodo versionPeriodo : versionPeriodoList){
                    
                    EstadoCuadro estadoCuadro = facadeService.getMantenedoresTipoService().findEstadoCuadroById(EstadoCuadroEnum.MODIFICADO.getKey());
                    versionPeriodo.setEstado(estadoCuadro);
                    
                    em.merge(estadoCuadro);

                    HistorialVersionPeriodo historialVersionPeriodo = new HistorialVersionPeriodo();
                    historialVersionPeriodo.setVersionPeriodo(versionPeriodo);
                    historialVersionPeriodo.setEstadoCuadro(versionPeriodo.getEstado());
                    historialVersionPeriodo.setFechaProceso(new Date());
                    historialVersionPeriodo.setUsuario(usuario);
                    historialVersionPeriodo.setComentario("CAMBIO DE ESTADO AUTOMATICO POR MODIFICACION EN ESTADO FINANCIERO");
                    
                    em.persist(historialVersionPeriodo);
                    
                }
                
                }
            }
        }
        if(Util.esListaValida(versionEeff.getEstadoFinancieroList())){
            for(EstadoFinanciero eeff : versionEeff.getEstadoFinancieroList()){                
                em.createNamedQuery(RelacionEeff.UPDATE_MONTO_BY_FECU_PERIODO)
                    .setParameter("montoTotal", eeff.getMontoTotal())
                    .setParameter("idFecu", eeff.getIdFecu())
                    .setParameter("idPeriodo", versionEeff.getPeriodo().getIdPeriodo()).executeUpdate();
                
                if(Util.esListaValida(eeff.getDetalleEeffList4())){
                    for(DetalleEeff detEeff : eeff.getDetalleEeffList4()){
                        em.createNamedQuery(RelacionDetalleEeff.UPDATE_MONTO_BY_FECU_CUENTA_PERIODO)
                            .setParameter("montoTotal", detEeff.getMontoMilesValidarMapeo())
                            .setParameter("idCuenta", detEeff.getIdCuenta())
                            .setParameter("idFecu", detEeff.getIdFecu())
                            .setParameter("idPeriodo", versionEeff.getPeriodo().getIdPeriodo()).executeUpdate();
                    }
                }
            }
        }
        
        if(Util.esListaValida(cargadorVO.getRelEeffBorradoList())){
            for(RelacionEeff eeff : cargadorVO.getRelEeffBorradoList()){
                em.createNamedQuery(RelacionEeff.DELETE_BY_FECU_PERIODO)
                    .setParameter("idFecu", eeff.getIdFecu())
                    .setParameter("idPeriodo", versionEeff.getPeriodo().getIdPeriodo()).executeUpdate();
            }
        }
        
        if(Util.esListaValida(cargadorVO.getRelEeffDetBorradoList())){
            for(RelacionDetalleEeff eeffDet : cargadorVO.getRelEeffDetBorradoList()){
                em.createNamedQuery(RelacionDetalleEeff.DELETE_BY_FECU_CUENTA_PERIODO)
                    .setParameter("idFecu", eeffDet.getIdFecu())
                    .setParameter("idCuenta", eeffDet.getIdCuenta())
                    .setParameter("idPeriodo", versionEeff.getPeriodo().getIdPeriodo()).executeUpdate();
            }
        }
        
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public TipoEstadoEeff getTipoEstadoEeffById(Long idEstadoEeff) {
        Query query = em.createNamedQuery(TipoEstadoEeff.FIND_BY_ID);
        query.setParameter("idEstadoEeff", idEstadoEeff);
        return (TipoEstadoEeff)query.getSingleResult();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<EstadoFinanciero> getEeffVigenteByPeriodo(Long idPeriodo) {
        Query query = em.createNamedQuery(EstadoFinanciero.FIND_VIGENTE_BY_PERIODO);
        query.setParameter("idPeriodo", idPeriodo);
        return query.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<EstadoFinanciero> getEeffByVersion(Long idVersionEeff) {
        Query query = em.createNamedQuery(EstadoFinanciero.FIND_BY_VERSION);
        query.setParameter("idVersionEeff", idVersionEeff);
        return query.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<DetalleEeff> getDetalleEeffByEeff(EstadoFinanciero eeff) {
        Query query = em.createNamedQuery(DetalleEeff.FIND_BY_EEFF);
        query.setParameter("eeff", eeff);
        return query.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<DetalleEeff> getDetalleEeffByVersion(Long idVersionEeff) {
        Query query = em.createNamedQuery(DetalleEeff.FIND_BY_VERSION);
        query.setParameter("idVersionEeff", idVersionEeff);
        return query.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteRelacionEeffByCelda(Celda celda) {
        Query query = em.createNamedQuery(RelacionEeff.DELETE_BY_CELDA);
        query.setParameter("celda", celda);
        query.executeUpdate();
    }

    
    public void deleteRelacionDetalleEeffByCelda(Celda celda) {
        Query query = em.createNamedQuery(RelacionDetalleEeff.DELETE_BY_CELDA);
        query.setParameter("celda", celda);
        query.executeUpdate();
    }

    
    public void deleteRelacionAllEeffByCelda(Celda celda) {
        deleteRelacionEeffByCelda(celda);
        deleteRelacionDetalleEeffByCelda(celda);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<RelacionEeff> getRelacionEeffByPeriodo(Long idPeriodo) {
        Query query = em.createNamedQuery(RelacionEeff.FIND_BY_PERIODO);
        query.setParameter("idPeriodo", idPeriodo);
        return query.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<RelacionDetalleEeff> getRelacionDetalleEeffByPeriodo(Long idPeriodo) {
        Query query = em.createNamedQuery(RelacionDetalleEeff.FIND_BY_PERIODO);
        query.setParameter("idPeriodo", idPeriodo);
        return query.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<RelacionEeff> getRelacionEeffByPeriodoFecu(Long idPeriodo, Long idFecu) {
        Query query = em.createNamedQuery(RelacionEeff.FIND_BY_PERIODO_FECU);
        query.setParameter("idPeriodo", idPeriodo);
        query.setParameter("idFecu", idFecu);
        return query.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<RelacionDetalleEeff> getRelacionDetalleEeffByPeriodoFecuCuenta(Long idPeriodo, Long idFecu,
                                                                               Long idCuenta) {
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
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistRelaccionEeff(Map<Celda, List[]> relacionMap, Long idPeriodo) {

        for (List[] arrayListas : relacionMap.values()) {
            // 0 contiene las RelacionesEeff
            if (Util.esListaValida(arrayListas[0])) {
                List<RelacionEeff> relList = arrayListas[0];
                for (RelacionEeff relEeff : relList) {
                    em.merge(relEeff);
                }
            }

            // 1 contiene las RelacionesDetalleEeff
            if (Util.esListaValida(arrayListas[1])) {
                List<RelacionDetalleEeff> relList = arrayListas[1];
                for (RelacionDetalleEeff relDetEeff : relList) {
                    em.merge(relDetEeff);
                }
            }
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteAllRelacionByGrillaPeriodo(Long idPeriodo, Long idGrilla) {

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
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<EstadoFinanciero> getEeffByLikeFecu(Long idVersionEeff, Long likeFecu) {
        Query query = em.createNamedQuery(EstadoFinanciero.FIND_BY_LIKE_FECU);
        query.setParameter("idVersionEeff", idVersionEeff);
        query.setParameter("likeFecu", "%" + likeFecu + "%");
        return query.getResultList();
    }

    /**
     * @param idVersionEeff
     * @param likeFecu
     * @return
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<DetalleEeff> getEeffByLikeCuenta(Long idVersionEeff, Long likeCuenta) {
        Query query = em.createNamedQuery(DetalleEeff.FIND_BY_LIKE_CUENTA);
        query.setParameter("idVersionEeff", idVersionEeff);
        query.setParameter("likeCuenta", "%" + likeCuenta + "%");
        return query.getResultList();
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
        final List<Long> mappingCeldaList =
            em.createQuery("select o.idPeriodo from RelacionCelda o where o.idPeriodo =:idPeriodo and o.idGrillaRel =:idGrilla")
            .setParameter("idPeriodo", idPeriodo)
            .setParameter("idGrilla", idGrilla)
            .getResultList();        
        
        if ((!mappingFecuList.isEmpty() && mappingFecuList.size() > 0) || (!mappingCuentaContableList.isEmpty() && mappingCuentaContableList.size() > 0)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
    
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<EstadoFinanciero> getEeffVigenteByPeriodoAndGrupoEeff(Long idPeriodo, GrupoEeff grupoEeff) {
        Query query = em.createNamedQuery(EstadoFinanciero.FIND_VIGENTE_BY_PERIODO_AND_GRUPO_EEFF);
        query.setParameter("idPeriodo", idPeriodo);
        query.setParameter("grupoEeff", grupoEeff);
        return query.getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<EstadoFinanciero> getEeffVigenteByPeriodoSinGrupoEeff(Long idPeriodo) {
        Query query = em.createNamedQuery(EstadoFinanciero.FIND_VIGENTE_BY_PERIODO_SIN_GRUPO_EEFF);
        query.setParameter("idPeriodo", idPeriodo);
        //query.setParameter("grupoEeff", null);
        return query.getResultList();
    }


}
