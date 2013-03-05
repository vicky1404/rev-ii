package cl.bicevida.revelaciones.ejb.service.local;


import cl.bicevida.revelaciones.eeff.CargadorEeffVO;
import cl.bicevida.revelaciones.ejb.entity.EstadoFinanciero;
import cl.bicevida.revelaciones.ejb.entity.UsuarioGrupo;
import cl.bicevida.revelaciones.exceptions.EstadoFinancieroException;

import java.io.InputStream;
import java.io.Serializable;

import java.util.List;

import javax.ejb.Remote;


@Remote
public interface CargadorEeffServiceLocal extends Serializable{
    
    CargadorEeffVO leerEeff(final InputStream loadedExcel)throws EstadoFinancieroException, Exception;
    
    void validarNuevoEeff(List<EstadoFinanciero> eeffListNuevo, Long idPeriodo, CargadorEeffVO cargadorVO) throws Exception;
    
    void sendMailEeff(List<UsuarioGrupo> usuarioGrupoList)  throws Exception;
}
