package cl.bicevida.revelaciones.ejb.service.local;


import cl.bicevida.revelaciones.ejb.entity.CaratulaFecu;
import cl.bicevida.revelaciones.ejb.entity.DatoCaratulaFecu;
import cl.bicevida.revelaciones.ejb.entity.Periodo;

import java.util.List;

import java.util.Map;

import javax.ejb.Local;


@Local
public interface CaratulaFecuServiceLocal {

    /**
     * @param periodo
     * @return
     * @throws Exception
     */
    CaratulaFecu findByPeriodo(Periodo periodo) throws Exception;

    /**
     * @param datoCaratulaFecuList
     * @throws Exception
     */
    void mergeDatosCaratula(List<DatoCaratulaFecu> datoCaratulaFecuList) throws Exception;

    /**
     * @param periodo
     * @return
     * @throws Exception
     */
    Map<String, DatoCaratulaFecu> getDatoCaratulaFecuMap(Periodo periodo) throws Exception;
}
