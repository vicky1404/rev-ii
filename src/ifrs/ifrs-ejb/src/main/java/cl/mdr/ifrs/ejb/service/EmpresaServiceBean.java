package cl.mdr.ifrs.ejb.service;

import static cl.mdr.ifrs.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.Empresa;
import cl.mdr.ifrs.ejb.service.local.EmpresaServiceLocal;

@Stateless
public class EmpresaServiceBean implements EmpresaServiceLocal {
	
	
	@PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;
	
	
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Empresa> findAll(){
		return em.createNamedQuery(Empresa.EMPRESA_FIND_ALL).getResultList();
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Empresa findById(final Empresa empresa) throws Exception{
		return (Empresa) em.createNamedQuery(Empresa.EMPRESA_FIND_BY_ID).setParameter("rut", Util.getLong(empresa.getRut(), 0L)).getSingleResult();
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Empresa mergeEmpresa(final Empresa empresa) throws Exception{
		return em.merge(empresa);
	}
	
	

}
