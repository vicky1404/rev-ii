package cl.bicevida.revelaciones.ejb.service.local;


import cl.bicevida.revelaciones.ejb.entity.CodigoFecu;
import cl.bicevida.revelaciones.ejb.entity.CuentaContable;
import cl.bicevida.revelaciones.ejb.entity.GrupoEeff;
import cl.bicevida.revelaciones.exceptions.EstadoFinancieroException;

import java.io.InputStream;
import java.io.Serializable;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;


@Remote
public interface FecuServiceLocal extends Serializable {
    
    Map<Long, CodigoFecu> getCodigoFecusVigentes();
    
    Map<Long, CuentaContable> getCodigoCuentasVigentes();
    
    List<CodigoFecu> leerFECU(final InputStream loadedExcel)throws EstadoFinancieroException, Exception;
    
    List<CuentaContable> leerCuenta(final InputStream loadedExcel)throws EstadoFinancieroException, Exception;
    
    void persistFecu(final List<CodigoFecu> fecuList, final String usuario) throws Exception;
     
    void mergeFecu(final List<CodigoFecu> fecuList, final String usuario) throws Exception;
    
    void persistCuenta(final List<CuentaContable> cuentaList,final String usuario) throws Exception;
    
    void mergeCuenta(final List<CuentaContable> cuentaList,final String usuario) throws Exception;
    
    public List<GrupoEeff> getGruposEeff() throws Exception;
    
    void persistGrupoEeff(final List<GrupoEeff> grupoEeffList) throws Exception;
    
    void mergeAsignarGrupoACodigoFecu(final CodigoFecu fecu, GrupoEeff grupoEeff) throws Exception;
    
    void mergeConfiguracionPortadaEEFF(final List<CodigoFecu> fecuList, final String usuario) throws Exception;
    
}
