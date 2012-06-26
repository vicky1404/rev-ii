package cl.mdr.ifrs.ejb.service.local;

import java.util.Map;

import cl.mdr.ifrs.ejb.entity.CodigoFecu;

public interface FecuServiceLocal {
    
    Map<Long, CodigoFecu> getCodigoFecusVigentes();
    
}
