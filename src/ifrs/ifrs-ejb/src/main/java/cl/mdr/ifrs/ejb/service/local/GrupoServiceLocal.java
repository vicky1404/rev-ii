package cl.mdr.ifrs.ejb.service.local;

import java.util.List;

import javax.ejb.Local;

import org.hibernate.exception.ConstraintViolationException;

import cl.mdr.ifrs.ejb.entity.AreaNegocio;
import cl.mdr.ifrs.ejb.entity.Empresa;
import cl.mdr.ifrs.ejb.entity.Grupo;

@Local
public interface GrupoServiceLocal {
	
	List<Grupo> findGruposByFiltro(AreaNegocio areaNegocio, Empresa empresa) throws Exception;
	
	void editarGrupo(Grupo grupo) throws Exception;
	
	void editarGrupoList(List<Grupo> grupoList) throws Exception;
	
	void mergeAreaNegocio(Grupo grupo) throws Exception;
	
	void mergeAreaNegocioList(List<Grupo> grupoLis) throws Exception;
	
	void persistAreaNegocio(Grupo grupo) throws ConstraintViolationException, Exception;

}
