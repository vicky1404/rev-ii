package cl.mdr.ifrs.ejb.cross.service;

import static cl.mdr.ifrs.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;

import java.util.List;

import cl.mdr.ifrs.ejb.cross.service.local.TestServiceLocal;
import cl.mdr.ifrs.ejb.entity.Test;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Session Bean implementation class TestServiceBean
 */
@Stateless
public class TestServiceBean implements TestServiceLocal {
	
	@PersistenceContext(name = PERSISTENCE_UNIT_NAME)
	private EntityManager entityManager;

    /**
     * Default constructor. 
     */
    public TestServiceBean() {
        // TODO Auto-generated constructor stub
    }
    
    public List<Test> findAll(){
    	return entityManager.createQuery("from Test").getResultList();
    }

}
