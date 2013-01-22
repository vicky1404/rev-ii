package cl.mdr.ifrs.ejb.service;

import static cl.mdr.ifrs.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;
import static ch.lambdaj.Lambda.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cl.mdr.ifrs.ejb.entity.Parametro;
import cl.mdr.ifrs.ejb.entity.TipoParametro;
import cl.mdr.ifrs.ejb.service.local.ParametroServiceLocal;

/**
 * @author http://www.mdrtech.cl
 * Session Bean implementation class ParametroServiceBean
 */
@Stateless
public class ParametroServiceBean implements ParametroServiceLocal {

    
	@PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;
	
	private Map<Long, Map<String, Parametro>> parametros;
	
	/**
     * Default constructor. 
     */
    public ParametroServiceBean() {
        super();
    }
    
    /* (non-Javadoc)
     * @see cl.mdr.ifrs.ejb.service.local.ParametroServiceLocal#findAll()
     */
    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoParametro> findAll(){
		return em.createNamedQuery(TipoParametro.FIND_ALL).getResultList();    	
    }
    
    
    /* (non-Javadoc)
     * @see cl.mdr.ifrs.ejb.service.local.ParametroServiceLocal#getParametrosConfigMap()
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Map<Long, Map<String, Parametro>> getParametrosConfigMap(){
    	parametros = new LinkedHashMap<Long, Map<String,Parametro>>();
    	Map<String, Parametro> params;
    	for(TipoParametro tipoParametro : this.findAll()){
    		params = index(tipoParametro.getParametros(), on(Parametro.class).getId().getNombre());
    		parametros.put(tipoParametro.getIdTipoParametro(), params);
    	}
    	return parametros;
    }
    
    

}
