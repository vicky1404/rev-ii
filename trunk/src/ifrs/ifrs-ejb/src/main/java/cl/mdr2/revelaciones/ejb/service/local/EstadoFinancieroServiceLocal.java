package cl.bicevida.revelaciones.ejb.service.local;


import cl.bicevida.revelaciones.ejb.entity.CodigoFecu;
import cl.bicevida.revelaciones.ejb.entity.DetalleEeff;
import cl.bicevida.revelaciones.ejb.entity.EstadoFinanciero;
import cl.bicevida.revelaciones.ejb.entity.Grilla;
import cl.bicevida.revelaciones.ejb.entity.TipoEstadoEeff;
import cl.bicevida.revelaciones.ejb.entity.VersionEeff;

import java.util.List;

import java.util.Map;

import javax.ejb.Local;


@Local
public interface EstadoFinancieroServiceLocal {
    
    List<TipoEstadoEeff> getEstadoEeffFindAll();
    
    List<VersionEeff> getVersionEeffFindByPeriodo(Long idPeriodo);
    
    void persistEeffMap(Map<Long, EstadoFinanciero> eeffMap);
    
    void persisVersionEeff(VersionEeff version);
    
    TipoEstadoEeff getTipoEstadoEeffById(Long idEstadoEeff);
    
    List<EstadoFinanciero> getEeffVigenteByPeriodo(Long idPeriodo);
    
    List<DetalleEeff> getDetalleEeffByEeff(EstadoFinanciero eeff);
    
    void persistRelaccionEeff(Map<String, String[]> relacionMap, Long idPeriodo, Grilla grilla);
}
