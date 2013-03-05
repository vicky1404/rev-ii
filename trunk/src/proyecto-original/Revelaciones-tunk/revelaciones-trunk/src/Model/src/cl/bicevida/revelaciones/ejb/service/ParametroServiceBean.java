package cl.mdr.ifrs.ejb.service;


import static ch.lambdaj.Lambda.index;
import static ch.lambdaj.Lambda.on;

import static cl.bicevida.revelaciones.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;
import cl.bicevida.revelaciones.ejb.entity.Parametro;
import cl.bicevida.revelaciones.ejb.entity.TipoParametro;
import cl.bicevida.revelaciones.ejb.service.local.ParametroServiceLocal;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Stateless
public class ParametroServiceBean implements ParametroServiceLocal {

    
    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;
	
    private Map<Long, Map<String, Parametro>> parametros;
	
    public ParametroServiceBean() {
        super();
    }
    
    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoParametro> findAll(){
		return em.createNamedQuery(TipoParametro.FIND_ALL).getResultList();    	
    }
    
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Map<Long, Map<String, Parametro>> getParametrosConfigMap() throws Exception{
        parametros = new LinkedHashMap<Long, Map<String,Parametro>>();
        Map<String, Parametro> params;
        for(TipoParametro tipoParametro : this.findAll()){
                params = index(tipoParametro.getParametroList(), on(Parametro.class).getIdParametro());
                parametros.put(tipoParametro.getIdTipoParametro(), params);
        }
        return parametros;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Map<String, Parametro> getParametrosConfigMapByTipoParametro(final Long idTipoParametro) throws Exception{
        
        Map<String, Parametro> paramMap = new HashMap<String, Parametro>();
        
        Query query = em.createNamedQuery(Parametro.FIND_BY_TIPO_PARAMETRO);
        List parametroList = query.setParameter("idTipoParametro", idTipoParametro).getResultList();

        paramMap = index(parametroList, on(Parametro.class).getIdParametro());
        
        return paramMap;
    }

}
