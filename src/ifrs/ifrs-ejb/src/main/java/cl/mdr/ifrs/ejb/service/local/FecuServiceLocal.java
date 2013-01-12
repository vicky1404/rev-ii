package cl.mdr.ifrs.ejb.service.local;

import cl.mdr.ifrs.ejb.entity.CodigoFecu;

import cl.mdr.ifrs.ejb.entity.CuentaContable;

import java.util.Map;

import javax.ejb.Local;

@Local
public interface FecuServiceLocal {
    
    Map<Long, CodigoFecu> getCodigoFecusVigentes();
    
    Map<Long, CuentaContable> getCodigoCuentasVigentes();
    
}
