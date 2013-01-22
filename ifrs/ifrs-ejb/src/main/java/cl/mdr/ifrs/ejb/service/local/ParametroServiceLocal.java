package cl.mdr.ifrs.ejb.service.local;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import cl.mdr.ifrs.ejb.entity.Parametro;
import cl.mdr.ifrs.ejb.entity.TipoParametro;

@Local
public interface ParametroServiceLocal {
	
	/**
	 * @return
	 */
	List<TipoParametro> findAll();
	
	/**
     * obtiene una extructura de tipo Map<TipoParametro, Map<String, Parametro>>
     * con todos los parametros de configuracion del sistema.
     * @return
     */
	Map<Long, Map<String, Parametro>> getParametrosConfigMap();

}
