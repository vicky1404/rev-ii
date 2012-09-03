package cl.mdr.ifrs.ejb.service;

import static cl.mdr.ifrs.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;

import java.text.MessageFormat;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.exception.ConstraintViolationException;

import cl.mdr.ifrs.ejb.common.VigenciaEnum;
import cl.mdr.ifrs.ejb.entity.AreaNegocio;
import cl.mdr.ifrs.ejb.entity.Empresa;
import cl.mdr.ifrs.ejb.entity.Grupo;
import cl.mdr.ifrs.ejb.service.local.AreaNegocioServiceLocal;
import cl.mdr.ifrs.exceptions.RegistroNoEditableException;

/**
 * @author http://www.mdrtech.cl
 * Session Bean implementation class AreaNegocioServiceBean
 */
@Stateless
public class AreaNegocioServiceBean implements AreaNegocioServiceLocal {

	@PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;
	
	/**
     * Default constructor. 
     */
    public AreaNegocioServiceBean() {
        super();
    }
    
    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AreaNegocio> findAllByEmpresa(final Empresa empresa, final Long vigente) throws Exception{    
    	return em.createNamedQuery(AreaNegocio.FIND_ALL_BY_EMPRESA).
    			setParameter("rutEmpresa", empresa.getIdRut()).
    			setParameter("vigente", vigente).    			    			    			
    			getResultList();
    }
    
    public void editarAreaNegocio(final AreaNegocio areaNegocio) throws RegistroNoEditableException, Exception{
		if (areaNegocio.getVigente().equals(VigenciaEnum.NO_VIGENTE.getKey())
				&& em.createNamedQuery(Grupo.FIND_BY_AREA_NEGOCIO)
						.setParameter("idAreaNegocio", areaNegocio.getIdAreaNegocio()).getResultList()
						.size() > 0) {
			throw new RegistroNoEditableException(MessageFormat.format("El Área de Negocio {0} no puede ser deshabilitado ya que tiene Grupos asociados.", areaNegocio.getNombre()));
		}
		this.mergeAreaNegocio(areaNegocio);
    }
    
    public void editarAreaNegocioList(final List<AreaNegocio> areaNegocioList) throws RegistroNoEditableException, Exception{
    	for (final AreaNegocio areaNegocio : areaNegocioList) {
    		if (areaNegocio.getVigente().equals(VigenciaEnum.NO_VIGENTE.getKey())
    				&& em.createNamedQuery(Grupo.FIND_BY_AREA_NEGOCIO)
    						.setParameter("idAreaNegocio", areaNegocio.getIdAreaNegocio()).getResultList()
    						.size() > 0) {
    			throw new RegistroNoEditableException(MessageFormat.format("El Área de Negocio {0} no puede ser deshabilitado ya que tiene Grupos asociados.", areaNegocio.getNombre()));
    		}
		}
    	this.mergeAreaNegocioList(areaNegocioList);
    }
    
    public void mergeAreaNegocio(final AreaNegocio areaNegocio) throws Exception{
    	em.merge(areaNegocio);
    }
    
    public void mergeAreaNegocioList(final List<AreaNegocio> areaNegocioList) throws Exception{
    	for (final AreaNegocio areaNegocio : areaNegocioList) {
    		em.merge(areaNegocio);
		}    	    	
    }
    
    public void persistAreaNegocio(final AreaNegocio areaNegocio) throws ConstraintViolationException, Exception{
    	em.persist(areaNegocio);
    }

}
