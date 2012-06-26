package cl.mdr.ifrs.ejb.service.local;


import java.io.InputStream;
import java.util.Map;

import javax.ejb.Local;

import cl.mdr.ifrs.ejb.entity.EstadoFinanciero;
import cl.mdr.ifrs.exceptions.EstadoFinancieroException;


@Local
public interface CargadorEeffServiceLocal {
    
    Map<Long, EstadoFinanciero> leerEeff(final InputStream loadedExcel)throws EstadoFinancieroException, Exception;
}
