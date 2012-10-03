package cl.bicevida.revelaciones.ejb.service.local;


import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.CodigoFecu;
import cl.bicevida.revelaciones.ejb.entity.DetalleEeff;
import cl.bicevida.revelaciones.ejb.entity.EstadoFinanciero;
import cl.bicevida.revelaciones.ejb.entity.Grilla;
import cl.bicevida.revelaciones.ejb.entity.RelacionDetalleEeff;
import cl.bicevida.revelaciones.ejb.entity.RelacionEeff;
import cl.bicevida.revelaciones.ejb.entity.TipoEstadoEeff;
import cl.bicevida.revelaciones.ejb.entity.VersionEeff;

import java.util.List;

import java.util.Map;

import javax.ejb.Local;

import javax.persistence.Query;


@Local
public interface EstadoFinancieroServiceLocal {
    
    List<TipoEstadoEeff> getEstadoEeffFindAll();
    
    List<VersionEeff> getVersionEeffFindByPeriodo(Long idPeriodo);
    
    VersionEeff getVersionEeffVigenteFindByPeriodo(Long idPeriodo);
    
    void persistEeffMap(Map<Long, EstadoFinanciero> eeffMap);
    
    void persisVersionEeff(VersionEeff version);
    
    TipoEstadoEeff getTipoEstadoEeffById(Long idEstadoEeff);
    
    List<EstadoFinanciero> getEeffVigenteByPeriodo(Long idPeriodo);
    
    public List<EstadoFinanciero> getEeffByVersion(Long idVersionEeff);
    
    List<DetalleEeff> getDetalleEeffByEeff(EstadoFinanciero eeff);
    
    List<DetalleEeff> getDetalleEeffByVersion(Long idVersionEeff);
    
    void persistRelaccionEeff(Map<Celda, List[]> relacionMap, Long idPeriodo);
    
    void deleteAllRelacionByGrillaPeriodo(Long idPeriodo, Long idGrilla);
    
    void deleteRelacionEeffByCelda(Celda celda);
    
    void deleteRelacionDetalleEeffByCelda(Celda celda);
    
    void deleteRelacionAllEeffByCelda(Celda celda);
    
    List<RelacionEeff> getRelacionEeffByPeriodo(Long idPeriodo);
    
    List<RelacionDetalleEeff> getRelacionDetalleEeffByPeriodo(Long idPeriodo);
    
    List<RelacionEeff> getRelacionEeffByPeriodoFecu(Long idPeriodo, Long idFecu);
    
    List<RelacionDetalleEeff> getRelacionDetalleEeffByPeriodoFecuCuenta(Long idPeriodo, Long idFecu, Long idCuenta);
    
    List<EstadoFinanciero> getEeffByLikeFecu(Long idVersionEeff, Long likeFecu);
    
    List<DetalleEeff> getEeffByLikeCuenta(Long idVersionEeff, Long likeCuenta);
    
}
