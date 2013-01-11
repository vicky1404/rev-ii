package cl.bicevida.revelaciones.ejb.service.local;

import cl.bicevida.revelaciones.ejb.entity.Parametro;
import cl.bicevida.revelaciones.ejb.entity.TipoParametro;

import java.util.List;
import java.util.Map;

public interface ParametroServiceLocal {
    
    
    /**
     * @return
     */
    List<TipoParametro> findAll();
    
    /**
    * @return
    */
    Map<Long, Map<String, Parametro>> getParametrosConfigMap()  throws Exception;
    
    Map<String, Parametro> getParametrosConfigMapByTipoParametro(final Long idTipoParametro) throws Exception;
    
}
