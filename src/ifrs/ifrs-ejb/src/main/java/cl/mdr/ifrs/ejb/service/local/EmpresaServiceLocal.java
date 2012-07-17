package cl.mdr.ifrs.ejb.service.local;

import java.util.List;

import javax.ejb.Local;

import cl.mdr.ifrs.ejb.entity.Empresa;

@Local
public interface EmpresaServiceLocal {
	
	List<Empresa> findAll();
	
	Empresa findById(Long rut) throws Exception;
	
	Empresa mergeEmpresa(Empresa empresa) throws Exception;
}
