package cl.bicevida.revelaciones.ejb.service.local;


import cl.bicevida.revelaciones.ejb.entity.EstadoFinanciero;
import cl.bicevida.revelaciones.exceptions.EstadoFinancieroException;

import java.io.InputStream;

import java.util.Map;

import javax.ejb.Local;


@Local
public interface CargadorEeffServiceLocal {
    
    Map<Long, EstadoFinanciero> leerEeff(final InputStream loadedExcel)throws EstadoFinancieroException, Exception;
}
