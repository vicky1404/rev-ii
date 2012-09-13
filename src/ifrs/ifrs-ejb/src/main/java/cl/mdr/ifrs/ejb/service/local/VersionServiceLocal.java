package cl.mdr.ifrs.ejb.service.local;


import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import cl.mdr.ifrs.ejb.entity.Catalogo;
import cl.mdr.ifrs.ejb.entity.Empresa;
import cl.mdr.ifrs.ejb.entity.EstadoCuadro;
import cl.mdr.ifrs.ejb.entity.Estructura;
import cl.mdr.ifrs.ejb.entity.HistorialVersion;
import cl.mdr.ifrs.ejb.entity.Periodo;
import cl.mdr.ifrs.ejb.entity.PeriodoEmpresa;
import cl.mdr.ifrs.ejb.entity.TipoCuadro;
import cl.mdr.ifrs.ejb.entity.Version;
import cl.mdr.ifrs.exceptions.PeriodoException;
import cl.mdr.ifrs.model.EstructuraModel;


@Local
public interface VersionServiceLocal {


    /**
     * @param versionList
     * @param historialVersionList
     */
    void persistFlujoAprobacion( List<Version> versionList,  List<HistorialVersion> historialVersionList) throws Exception;
    
    /**
     * @param usuario
     * @param tipoCuadro
     * @param periodo
     * @param estadoCuadro
     * @param vigente
     * @return
     * @throws Exception*/
    List<Version> findVersionByFiltro(String usuario,  TipoCuadro tipoCuadro,  PeriodoEmpresa periodoEmpresa,  EstadoCuadro estadoCuadro,  Long vigente,  Catalogo catalogo) throws Exception;     

    
    /**
     * @param usuario
     * @param tipoCuadro
     * @param periodo
     * @param estadoCuadro
     * @param vigente
     * @return
     * @throws Exception*/
    List<Version> findVersionDetalleByFiltro(String usuario,  TipoCuadro tipoCuadro,  PeriodoEmpresa periodoEmpresa,  EstadoCuadro estadoCuadro,  Long vigente,  Catalogo catalogo) throws Exception;
    
    
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
     * @param versiones
     * @param estructuras
     * @param estructuraModelMap
     * @param usuario
     * @throws Exception
     */
    Version persistVersion(List<Version> versiones, List<Estructura> estructuras, Map<Long, EstructuraModel> estructuraModelMap , String usuario, PeriodoEmpresa periodoEmpresa) throws Exception;
    
    /**
     * @param version
     * @param estructuras
     * @param estructuraModelMap
     * @param usuario
     * @throws PeriodoException
     * @throws Exception
     */
    void editarVersion(Version version, List<Estructura> estructuras, Map<Long, EstructuraModel> estructuraModelMap, String usuario) throws PeriodoException, Exception;

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
    List<Version> findVersionVigenteSinCerrar(PeriodoEmpresa periodoEmpresa);

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
    List<Version> findUltimoVersionByPeriodo( Long periodo,  String usuario,  TipoCuadro tipoCuadro,  Long vigente, Empresa empresa ) throws Exception;


    /**
     * @param idPeriodo
     * @param usuario
     * @param idCatalogo
     * @return
     */
    Version findUltimaVersionVigente( Long idPeriodo, Long idRut,  String usuario,  Long idCatalogo);
    
    /**
     * @param versionesModificadas
     * @return
     * @throws Exception
     */
    List<Version> findVersionListActualToCompare( List<Version> versionesModificadas) throws Exception;
    
    List<Version> findVersionByCatalogoPeriodo(Long idCatalogo, PeriodoEmpresa periodoEmpresa) throws Exception;
}

