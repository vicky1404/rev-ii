package cl.bicevida.revelaciones.ejb.service.local;


import cl.bicevida.revelaciones.ejb.entity.Catalogo;
import cl.bicevida.revelaciones.ejb.entity.EstadoCuadro;
import cl.bicevida.revelaciones.ejb.entity.HistorialVersionPeriodo;
import cl.bicevida.revelaciones.ejb.entity.Periodo;
import cl.bicevida.revelaciones.ejb.entity.TipoCuadro;
import cl.bicevida.revelaciones.ejb.entity.VersionPeriodo;

import java.util.List;

import javax.ejb.Local;


@Local
public interface PeriodoServiceLocal {
    
    Object mergeEntity(VersionPeriodo entity);

    Object persistEntity(VersionPeriodo entity);
    
    List<VersionPeriodo> findPeriodoAll();
    
    List<VersionPeriodo> findPeriodoAllByPeriodo(Periodo periodo);
    
    Periodo findPeriodoByPeriodo(Long periodo);
    
    List<VersionPeriodo> findPeriodoAllByPeriodoCatalogo(Catalogo catalogo, Periodo periodo);
    
    List<VersionPeriodo> findPeriodoAllByPeriodoCatalogoVigente(Catalogo catalogo, Periodo periodo);
    
    /**
     * retorna un set de datos por filtros opcionales para listar catalogo según:
     * @param usuario
     * @param tipoCuadro
     * @param periodo
     * @param estadoCuadro
     * @param vigente
     * @author rreyes
     * @return List<VersionPeriodo>
     */
    List<VersionPeriodo> findPeriodoByFiltro(String usuario, TipoCuadro tipoCuadro, Periodo periodo, EstadoCuadro estadoCuadro, Long vigente) throws Exception;

    /**
     * @param versionPeriodoList
     * @param historialVersionPeriodoList
     * @throws Exception
     */
    void persistFlujoAprobacion(List<VersionPeriodo> versionPeriodoList, List<HistorialVersionPeriodo> historialVersionPeriodoList)throws Exception;
    
   
    /**
     *  Ejecuta el pl/sql PKG_REV_PERIODO.PRC_ABRIR_PERIODO
     *  para abrir un nuevo periodo. Retorna error desde el procedimiento.
     * @return Integer
     * @author Manuel Gutierrez C.
     * @throws Exception
     */
    Integer abrirPeriodo(String usuario) throws Exception;
    
    /**
     * Retorna el último periodo en estado cerrado.
     * @return
     * @author Manuel Gutierrez C.
     * @throws Exception
     */
    Long findMaxPeriodoCerrado() throws Exception;
    
    /**
     *  Ejecuta el pl/sql PKG_REV_PERIODO.PRC_CERRAR_PERIODO
     *  para cerrar el periodo actual. Retorna error desde el procedimiento.
     * @return Integer
     * @author Manuel Gutierrez C.
     * @throws Exception
     */
    Integer cerrarPeriodo(String usuario, Long idPeriodo) throws Exception;
    
    /**
     * Retorna el último periodo .
     * @return
     * @author Manuel Gutierrez C.
     * @throws Exception
     */
    Long findMaxPeriodo() throws Exception;

    /**
     * @return
     */
    Periodo findMaxPeriodoObj();

    /**
     * retorna el ultimo periodo iniciado y en contingencia
     * @return
     * @throws Exception
     */
    Periodo findMaxPeriodoIniciado() throws Exception;

    /**
     * @param idPeriodo
     * @param idVersion
     * @return
     */
    VersionPeriodo findVersionPeriodoById(Long idPeriodo, Long idVersion);

    /**
     * @param idVersion
     * @return
     * @throws Exception
     */
    List<VersionPeriodo> findVersionPeriodoByIdVersion(Long idVersion) throws Exception;
}
