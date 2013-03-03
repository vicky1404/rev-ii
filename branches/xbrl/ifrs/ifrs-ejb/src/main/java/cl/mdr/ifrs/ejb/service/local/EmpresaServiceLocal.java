package cl.mdr.ifrs.ejb.service.local;

import java.util.List;

import javax.ejb.Local;

import cl.mdr.ifrs.ejb.entity.Empresa;

@Local
public interface EmpresaServiceLocal {
	
	List<Empresa> findAll();
	
	Empresa findById(Long rut) throws Exception;
	
	Empresa mergeEmpresa(Empresa empresa) throws Exception;
	
	List<Empresa> findDistEmpresa(List<Long> empresaList) throws Exception;
	
	List<Empresa> findEmpresaByFiltro(Empresa empresa) throws Exception;
	
	void deleteEmpresa(Empresa empresa) throws Exception;
	
	List<Empresa> findInEmpresa(List<Long> empresaList) throws Exception;
}