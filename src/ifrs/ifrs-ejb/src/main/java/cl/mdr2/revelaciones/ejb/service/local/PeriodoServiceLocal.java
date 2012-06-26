package cl.bicevida.revelaciones.ejb.service.local;


import cl.bicevida.revelaciones.ejb.entity.Catalogo;
import cl.bicevida.revelaciones.ejb.entity.EstadoCuadro;
import cl.bicevida.revelaciones.ejb.entity.HistorialVersion;
import cl.bicevida.revelaciones.ejb.entity.Periodo;
import cl.bicevida.revelaciones.ejb.entity.TipoCuadro;

import java.util.List;

import javax.ejb.Local;


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

}
