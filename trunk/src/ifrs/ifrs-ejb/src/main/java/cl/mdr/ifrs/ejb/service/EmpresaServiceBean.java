package cl.mdr.ifrs.ejb.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cl.mdr.ifrs.ejb.entity.Empresa;
import cl.mdr.ifrs.ejb.service.local.EmpresaServiceLocal;
import static cl.mdr.ifrs.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;

@Stateless
public class EmpresaServiceBean implements EmpresaServiceLocal {
	
	
	@PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;
	
	
	@SuppressWarnings("unchecked")
	public List<Empresa> findAll(){
		return em.createNamedQuery(Empresa.EMPRESA_FIND_ALL).getResultList();
		
	}

}
