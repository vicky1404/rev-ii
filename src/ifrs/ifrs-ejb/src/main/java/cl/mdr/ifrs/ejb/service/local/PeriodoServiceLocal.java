package cl.mdr.ifrs.ejb.service.local;


import javax.ejb.Local;

import cl.mdr.ifrs.ejb.entity.Periodo;


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
    Integer cerrarPeriodo(String usuario, Long idPeriodo) throws Exception;
    
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

}
