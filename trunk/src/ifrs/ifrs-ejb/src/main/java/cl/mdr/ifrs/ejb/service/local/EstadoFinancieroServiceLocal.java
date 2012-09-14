package cl.mdr.ifrs.ejb.service.local;


import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.persistence.Query;

import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.ifrs.ejb.entity.DetalleEeff;
import cl.mdr.ifrs.ejb.entity.EstadoFinanciero;
import cl.mdr.ifrs.ejb.entity.RelacionDetalleEeff;
import cl.mdr.ifrs.ejb.entity.RelacionEeff;
import cl.mdr.ifrs.ejb.entity.TipoEstadoEeff;
import cl.mdr.ifrs.ejb.entity.VersionEeff;


@Local
public interface EstadoFinancieroServiceLocal {
	
	List<TipoEstadoEeff> getEstadoEeffFindAll();
    
    List<VersionEeff> getVersionEeffFindByPeriodo(Long idPeriodo, Long idRut);
    
    VersionEeff getVersionEeffVigenteFindByPeriodo(Long idPeriodo, Long idRut);
    
    void persistEeffMap(Map<Long, EstadoFinanciero> eeffMap);
    
    Long getMaxVersionByPeriodo(Long idPeriodo, Long idRut);
    
    void updateNoVigenteByPeriodo(Long idPeriodo, Long idRut, Long vigencia);
    
    void persisVersionEeff(VersionEeff version);
    
    TipoEstadoEeff getTipoEstadoEeffById(Long idEstadoEeff);
    
    List<EstadoFinanciero> getEeffVigenteByPeriodo(Long idPeriodo, Long idRut);
    
    List<EstadoFinanciero> getEeffByVersion(Long idVersionEeff);
    
    List<EstadoFinanciero> getEeffEagerByVersion(Long idVersionEeff);
    
    List<DetalleEeff> getDetalleEeffByEeff(EstadoFinanciero eeff);
    
    List<DetalleEeff> getDetalleEeffByVersion(Long idVersionEeff);
    
    void deleteRelacionEeffByCelda(Celda celda);
    
    void deleteRelacionDetalleEeffByCelda(Celda celda);
    
    void deleteRelacionAllEeffByCelda(Celda celda);
    
    List<RelacionEeff> getRelacionEeffByPeriodo(Long idPeriodo, Long idRut);
    
    List<RelacionDetalleEeff> getRelacionDetalleEeffByPeriodo(Long idPeriodo, Long idRut);
    
    List<RelacionEeff> getRelacionEeffByPeriodoFecu(Long idPeriodo, Long idRut, Long idFecu);
    
    List<RelacionDetalleEeff> getRelacionDetalleEeffByPeriodoFecuCuenta(Long idPeriodo, Long idRut, Long idFecu, Long idCuenta);

    void persistRelaccionEeff(Map<Celda, List[]> relacionMap)  throws Exception;

    void deleteAllRelacionByGrillaPeriodo(Long idPeriodo, Long idRut, Long idGrilla);

    List<EstadoFinanciero> getEeffByLikeFecu(Long idVersionEeff, Long likeFecu);
    
    List<DetalleEeff> getEeffByLikeCuenta(Long idVersionEeff, Long likeCuenta);
    
    RelacionDetalleEeff getRelacionDetalleEeffByRelacionDetalleEeff(RelacionDetalleEeff relacionDetalleEeff);
    
    List<RelacionEeff> getRelacionEeffByCelda(Celda celda);

    List<RelacionDetalleEeff> getRelacionDetalleEeffByCelda(Celda celda);
    
}
