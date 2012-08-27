package cl.mdr.ifrs.ejb.service.local;


import java.io.InputStream;

import javax.ejb.Local;

import cl.mdr.ifrs.exceptions.EstadoFinancieroException;
import cl.mdr.ifrs.vo.CargadorEeffVO;


@Local
public interface CargadorEeffServiceLocal {
    
    //Map<Long, EstadoFinanciero> leerEeff(final InputStream loadedExcel)throws EstadoFinancieroException, Exception;
    CargadorEeffVO leerEeff(final InputStream loadedExcel) throws EstadoFinancieroException, Exception;
}
