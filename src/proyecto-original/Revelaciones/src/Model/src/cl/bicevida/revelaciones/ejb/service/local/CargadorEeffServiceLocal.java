package cl.bicevida.revelaciones.ejb.service.local;


import cl.bicevida.revelaciones.ejb.entity.EstadoFinanciero;
import cl.bicevida.revelaciones.exceptions.EstadoFinancieroException;

import cl.bicevida.revelaciones.eeff.CargadorEeffVO;

import cl.bicevida.revelaciones.eeff.RelacionEeffVO;

import java.io.InputStream;

import java.util.Map;

import javax.ejb.Local;


@Local
public interface CargadorEeffServiceLocal {
    
    Map<Long, EstadoFinanciero> leerEeff(final InputStream loadedExcel)throws EstadoFinancieroException, Exception;
    
    void validarEeffConRelacionEeff(Map<Long, EstadoFinanciero> eeffMap, Long idPeriodo, CargadorEeffVO cargadorVO);
}
