package cl.bicevida.revelaciones.ejb.service.local;


import cl.bicevida.revelaciones.ejb.entity.Catalogo;
import cl.bicevida.revelaciones.ejb.entity.Estructura;
import cl.bicevida.revelaciones.ejb.entity.TipoCuadro;
import cl.bicevida.revelaciones.ejb.entity.Version;
import cl.bicevida.revelaciones.vo.GrillaModelVO;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;


@Local
public interface VersionServiceLocal {

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
     * @param idEstructura
     * @return
     * @throws Exception
     */
    Version findVersionByIdEstructura(Long idEstructura)throws Exception;
}
