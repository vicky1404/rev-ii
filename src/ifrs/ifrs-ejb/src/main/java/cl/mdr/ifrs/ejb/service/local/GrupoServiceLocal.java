package cl.mdr.ifrs.ejb.service.local;

import java.util.List;

import javax.ejb.Local;

import org.hibernate.exception.ConstraintViolationException;

import cl.mdr.ifrs.ejb.entity.AreaNegocio;
import cl.mdr.ifrs.ejb.entity.Empresa;
import cl.mdr.ifrs.ejb.entity.Grupo;
import cl.mdr.ifrs.exceptions.RegistroNoEditableException;

@Local
public interface GrupoServiceLocal {
	
	List<Grupo> findGruposByFiltro(AreaNegocio areaNegocio, Empresa empresa) throws Exception;
	
	void editarGrupo(Grupo grupo) throws RegistroNoEditableException, Exception;
	
	void editarGrupoList(List<Grupo> grupoList) throws RegistroNoEditableException, Exception;
	
	void mergeGrupo(Grupo grupo) throws Exception;
	
	void mergeGrupoList(List<Grupo> grupoLis) throws Exception;
	
	void persistGrupo(Grupo grupo) throws ConstraintViolationException, Exception;

}
