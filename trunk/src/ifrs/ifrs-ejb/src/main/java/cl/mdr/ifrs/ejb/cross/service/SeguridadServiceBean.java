package cl.mdr.ifrs.ejb.cross.service;

import static cl.mdr.ifrs.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cl.mdr.ifrs.ejb.cross.service.local.SeguridadServiceLocal;
import cl.mdr.ifrs.ejb.dummy.entity.Grupo;

/**
 * Session Bean implementation class SeguridadServiceBean
 */
@Stateless
public class SeguridadServiceBean implements SeguridadServiceLocal {

	@PersistenceContext(name = PERSISTENCE_UNIT_NAME)
	private EntityManager em;
	
	/**
     * Default constructor. 
     */
    public SeguridadServiceBean() {
        // TODO Auto-generated constructor stub
    }
    
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Grupo> findGruposAll()throws Exception{
        return em.createNamedQuery(Grupo.FIND_ALL).getResultList();
    }
    
           
    /*grupo*/
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void mergeGrupoList(final List<Grupo> grupoList) throws Exception {        
        for(Grupo grupo : grupoList) {
            em.merge(grupo);
        }
    }

}
