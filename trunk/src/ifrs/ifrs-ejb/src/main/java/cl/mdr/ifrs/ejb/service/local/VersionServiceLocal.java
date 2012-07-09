package cl.mdr.ifrs.ejb.service.local;


import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import cl.mdr.ifrs.ejb.entity.Catalogo;
import cl.mdr.ifrs.ejb.entity.EstadoCuadro;
import cl.mdr.ifrs.ejb.entity.Estructura;
import cl.mdr.ifrs.ejb.entity.HistorialVersion;
import cl.mdr.ifrs.ejb.entity.Periodo;
import cl.mdr.ifrs.ejb.entity.TipoCuadro;
import cl.mdr.ifrs.ejb.entity.Version;
import cl.mdr.ifrs.vo.GrillaModelVO;


@Local
public interface VersionServiceLocal {


    /**
     * @param versionList
     * @param historialVersionList
     */
    void persistFlujoAprobacion(final List<Version> versionList, final List<HistorialVersion> historialVersionList) throws Exception;
    
    /**
     * @param usuario
     * @param tipoCuadro
     * @param periodo
     * @param estadoCuadro
     * @param vigente
     * @return
     * @throws Exception*/
    List<Version> findVersionByFiltro(final String usuario, final TipoCuadro tipoCuadro, final Periodo periodo, final EstadoCuadro estadoCuadro, final Long vigente, final Catalogo catalogo) throws Exception;     

    /**
     * @param entity
     * @return
     */
    Object mergeEntity(Object entity);

    /**
     * @param entity
     * @return
     */
    Object persistEntity(Object entity);
    
    /**
     * @param version
     * @param estructuras
     * @throws Exception
     */
    void persistVersion(List<Version> versiones, List<Estructura> estructuras, Map<Long, GrillaModelVO> grillaModelMap , String usuario) throws Exception;

    /**
     * @return
     */
    List<Version> findVersionAll();

    /**
     * @return
     */
    List<Version> findAllVersionVigente();

    /**
     * @return
     */
    List<Version> findAllVersionNoVigente();

    /**
     * Retorna la unica version vigencia para un catalogo
     * @param catalogo
     * @return
     */
    Version findVersionVigente(Catalogo catalogo);

    /**
     * Retorna una lista de versiones no vigentes para un catalogo
     * @param catalogo
     * @return
     */
    List<Version> findVersionNoVigente(Catalogo catalogo);

    /**
     * @param catalogo
     * @return
     */
    List<Version> findVersionAllByCatalogo(Catalogo catalogo);
    
    
    /**
     * @param idCatalogo
     * @return
     */
    List<Version> findVersionAllByIdCatalogo(Long idCatalogo);
    
    /**
     * @param version
     * @return
     */
    Version findVersionByVersion(Version version);


    /**
     * @param periodo
     * @param usuario
     * @param tipoCuadro
     * @return
     * @throws Exception
     */
    List<Version> findUltimoVersionByPeriodo(final Long periodo, final String usuario, final TipoCuadro tipoCuadro, final Long vigente) throws Exception;


    /**
     * @param idPeriodo
     * @param usuario
     * @param idCatalogo
     * @return
     */
    Version findUltimaVersionVigente(final Long idPeriodo, final String usuario, final Long idCatalogo);
    
    /**
     * @param versionesModificadas
     * @return
     * @throws Exception
     */
    List<Version> findVersionListActualToCompare(final List<Version> versionesModificadas) throws Exception;
    
    List<Version> findVersionByCatalogoPeriodo(Long idCatalogo, Long idPeriodo) throws Exception;
}

