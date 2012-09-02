package cl.mdr.ifrs.ejb.service.local;

import java.util.List;

import javax.ejb.Local;

import cl.mdr.ifrs.ejb.entity.AreaNegocio;
import cl.mdr.ifrs.ejb.entity.Empresa;
import cl.mdr.ifrs.ejb.entity.Grupo;

@Local
public interface GrupoServiceLocal {
	
	List<Grupo> findGruposByFiltro(AreaNegocio areaNegocio, Empresa empresa) throws Exception;

}
