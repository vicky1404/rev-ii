package cl.mdr.ifrs.ejb.service.local;


import java.util.List;

import javax.ejb.Local;

import cl.mdr.ifrs.ejb.entity.HistorialVersion;
import cl.mdr.ifrs.ejb.entity.Periodo;
import cl.mdr.ifrs.ejb.entity.Version;


@Local
public interface PeriodoServiceLocal {
       
    Periodo findPeriodoByPeriodo(Long periodo);
    
   
    /**
     *  Ejecuta el pl/sql PKG_REV_PERIODO.PRC_ABRIR_PERIODO
     *  para abrir un nuevo periodo. Retorna error desde el procedimiento.
     * @return Integer
     * @author Manuel Gutierrez C.
     * @throws Exception
     */
    Integer abrirPeriodo(String usuario) throws Exception;
    
    /**
     * Retorna el �ltimo periodo en estado cerrado.
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
    int cerrarPeriodo(String usuario, Long idPeriodo) throws Exception;
    
    /**
     * Retorna el �ltimo periodo .
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
     * Persiste un set de versiones con su estado modificado
     * y un registro en el historial para cada una.
     * @param versionList
     * @param historialVersionList
     * @throws Exception
     */
    void persistFlujoAprobacion(List<Version> versionList, List<HistorialVersion> historialVersionList) throws Exception ;

}
