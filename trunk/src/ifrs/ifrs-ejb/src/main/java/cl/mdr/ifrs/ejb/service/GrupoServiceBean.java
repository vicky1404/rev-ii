package cl.mdr.ifrs.ejb.service;

import static cl.mdr.ifrs.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.exception.ConstraintViolationException;

import cl.mdr.ifrs.ejb.entity.AreaNegocio;
import cl.mdr.ifrs.ejb.entity.Empresa;
import cl.mdr.ifrs.ejb.entity.Grupo;
import cl.mdr.ifrs.ejb.service.local.GrupoServiceLocal;

/**
 * @author http://www.mdrtech.cl
 * Session Bean implementation class GrupoServiceBean
 */
@Stateless
public class GrupoServiceBean implements GrupoServiceLocal {
	
	@PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public GrupoServiceBean() {
        super();
    }
    
    
    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Grupo> findGruposByFiltro(final AreaNegocio areaNegocio, final Empresa empresa) throws Exception{
    	return em.createNamedQuery(Grupo.FIND_BY_FILTRO).
    				setParameter("areaNegocio", areaNegocio != null ? areaNegocio.getIdAreaNegocio() : null ).
    				setParameter("rutEmpresa", empresa.getIdRut()).
    				getResultList();
    }
    
    public void editarGrupo(final Grupo grupo) throws Exception{
    	
    }
    
    public void editarGrupoList(final List<Grupo> grupoList) throws Exception{
    	
    }
    
    public void mergeAreaNegocio(final Grupo grupo) throws Exception{
    	em.merge(grupo);
    }
    
    public void mergeAreaNegocioList(final List<Grupo> grupoLis) throws Exception{
    	for (final Grupo grupo : grupoLis) {
    		em.merge(grupo);
		}    	    	
    }
    
    public void persistAreaNegocio(final Grupo grupo) throws ConstraintViolationException, Exception{
    	em.persist(grupo);
    }
    
    
    
    
    

}
