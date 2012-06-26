package cl.bicevida.revelaciones.ejb.service.local;

import cl.bicevida.revelaciones.ejb.entity.CodigoFecu;

import java.util.Map;

public interface FecuServiceLocal {
    
    Map<Long, CodigoFecu> getCodigoFecusVigentes();
    
}
