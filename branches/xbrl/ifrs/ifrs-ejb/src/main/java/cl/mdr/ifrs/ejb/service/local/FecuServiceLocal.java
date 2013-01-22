package cl.mdr.ifrs.ejb.service.local;

import cl.mdr.ifrs.ejb.entity.CodigoFecu;

import cl.mdr.ifrs.ejb.entity.CuentaContable;
import cl.mdr.ifrs.exceptions.EstadoFinancieroException;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

@Local
public interface FecuServiceLocal {
    
    Map<Long, CodigoFecu> getCodigoFecusVigentes();
    
    Map<Long, CuentaContable> getCodigoCuentasVigentes();
    
    List<CodigoFecu> leerFECU(final InputStream loadedExcel)throws EstadoFinancieroException, Exception;
    
    List<CuentaContable> leerCuenta(final InputStream loadedExcel)throws EstadoFinancieroException, Exception;
    
    void persistFecu(final List<CodigoFecu> fecuList) throws Exception;
     
    void mergeFecu(final List<CodigoFecu> fecuList) throws Exception;
    
    void persistCuenta(final List<CuentaContable> cuentaList) throws Exception;
    
    void mergeCuenta(final List<CuentaContable> cuentaList) throws Exception;
    
}
