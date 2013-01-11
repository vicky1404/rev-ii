package cl.bicevida.revelaciones.ejb.service.local;


import cl.bicevida.revelaciones.ejb.entity.EstadoFinanciero;
import cl.bicevida.revelaciones.exceptions.EstadoFinancieroException;

import cl.bicevida.revelaciones.eeff.CargadorEeffVO;

import cl.bicevida.revelaciones.eeff.RelacionEeffVO;

import cl.bicevida.revelaciones.ejb.entity.UsuarioGrupo;

import java.io.InputStream;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;


@Local
public interface CargadorEeffServiceLocal {
    
    CargadorEeffVO leerEeff(final InputStream loadedExcel)throws EstadoFinancieroException, Exception;
    
    void validarNuevoEeff(List<EstadoFinanciero> eeffListNuevo, Long idPeriodo, CargadorEeffVO cargadorVO) throws Exception;
    
    void sendMailEeff(List<UsuarioGrupo> usuarioGrupoList)  throws Exception;
}
