package cl.mdr.ifrs.ejb.service.local;


import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.persistence.Query;

import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.ifrs.ejb.entity.DetalleEeff;
import cl.mdr.ifrs.ejb.entity.EstadoFinanciero;
import cl.mdr.ifrs.ejb.entity.Grilla;
import cl.mdr.ifrs.ejb.entity.RelacionDetalleEeff;
import cl.mdr.ifrs.ejb.entity.RelacionEeff;
import cl.mdr.ifrs.ejb.entity.TipoEstadoEeff;
import cl.mdr.ifrs.ejb.entity.VersionEeff;


@Local
public interface EstadoFinancieroServiceLocal {
    
List<TipoEstadoEeff> getEstadoEeffFindAll();
    
    List<VersionEeff> getVersionEeffFindByPeriodo(Long idPeriodo);
    
    void persistEeffMap(Map<Long, EstadoFinanciero> eeffMap);
    
    void persisVersionEeff(VersionEeff version);
    void persistRelaccionEeff(Map<Celda, List[]> relacionMap, Long idPeriodo) throws Exception;
    
    TipoEstadoEeff getTipoEstadoEeffById(Long idEstadoEeff);
    
    List<EstadoFinanciero> getEeffVigenteByPeriodo(Long idPeriodo);
    
    List<DetalleEeff> getDetalleEeffByEeff(EstadoFinanciero eeff);
    
    void persistRelaccionEeff(Map<String, String[]> relacionMap, Long idPeriodo, Grilla grilla);
    
    void deleteAllRelacionByGrillaPeriodo(Long idPeriodo, Long idGrilla);
    
    VersionEeff getVersionEeffVigenteFindByPeriodo(Long idPeriodo);
    
    void deleteRelacionAllEeffByCelda(Celda celda);
    
    EstadoFinanciero getEstadoFinancieroByEstadoFinanciero(EstadoFinanciero estadoFinanciero);
    
    RelacionDetalleEeff getRelacionDetalleEeffByRelacionDetalleEeff(RelacionDetalleEeff relacionDetalleEeff);
    
    List<EstadoFinanciero> getEeffByLikeFecu(Long idVersionEeff, Long likeFecu);
    
    List<DetalleEeff> getEeffByLikeCuenta(Long idVersionEeff, Long likeCuenta);
    
    List<EstadoFinanciero> getEeffByVersion(Long idVersionEeff);
    
    List<DetalleEeff> getDetalleEeffByVersion(Long idVersionEeff);
    
    List<RelacionEeff> getRelacionEeffByPeriodo(Long idPeriodo);
    
    List<RelacionDetalleEeff> getRelacionDetalleEeffByPeriodo(Long idPeriodo);
}
