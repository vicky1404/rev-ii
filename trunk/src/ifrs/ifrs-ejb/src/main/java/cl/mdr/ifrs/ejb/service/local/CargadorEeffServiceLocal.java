package cl.mdr.ifrs.ejb.service.local;


import java.io.InputStream;
import java.util.List;

import javax.ejb.Local;

import cl.mdr.ifrs.ejb.entity.EstadoFinanciero;
import cl.mdr.ifrs.ejb.entity.UsuarioGrupo;
import cl.mdr.ifrs.exceptions.EstadoFinancieroException;
import cl.mdr.ifrs.vo.CargadorEeffVO;


@Local
public interface CargadorEeffServiceLocal {
    
    //Map<Long, EstadoFinanciero> leerEeff(final InputStream loadedExcel)throws EstadoFinancieroException, Exception;
    CargadorEeffVO leerEeff(final InputStream loadedExcel) throws EstadoFinancieroException, Exception;
    void sendMailEeff(List<UsuarioGrupo> usuarioGrupoList, String emailFrom, String subject, String host);
    void validarNuevoEeff(final List<EstadoFinanciero> eeffListNuevo,final Long idPeriodo,final CargadorEeffVO cargadorVO) throws Exception;
}
