package cl.bicevida.revelaciones.ejb.service.local;

import cl.bicevida.revelaciones.ejb.entity.CodigoFecu;

import cl.bicevida.revelaciones.ejb.entity.CuentaContable;

import java.util.Map;

import javax.ejb.Local;

@Local
public interface FecuServiceLocal {
    
    Map<Long, CodigoFecu> getCodigoFecusVigentes();
    
    Map<Long, CuentaContable> getCodigoCuentasVigentes();
    
}
