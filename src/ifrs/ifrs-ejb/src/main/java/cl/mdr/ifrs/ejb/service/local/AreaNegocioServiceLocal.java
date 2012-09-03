package cl.mdr.ifrs.ejb.service.local;

import java.util.List;

import javax.ejb.Local;

import org.hibernate.exception.ConstraintViolationException;

import cl.mdr.ifrs.ejb.entity.AreaNegocio;
import cl.mdr.ifrs.ejb.entity.Empresa;
import cl.mdr.ifrs.exceptions.RegistroNoEditableException;

@Local
public interface AreaNegocioServiceLocal {
	
	List<AreaNegocio> findAllByEmpresa(Empresa empresa, Long vigente) throws Exception;
	
	void editarAreaNegocio(AreaNegocio areaNegocio) throws RegistroNoEditableException, Exception;
	
	void editarAreaNegocioList(List<AreaNegocio> areaNegocioList) throws RegistroNoEditableException, Exception;
	
	void mergeAreaNegocio(AreaNegocio areaNegocio) throws Exception;
	
	void mergeAreaNegocioList(List<AreaNegocio> areaNegocioList) throws Exception;
	
	void persistAreaNegocio(AreaNegocio areaNegocio) throws ConstraintViolationException, Exception;

}
