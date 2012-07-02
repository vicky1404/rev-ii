package cl.mdr.ifrs.ejb.service.local;


import java.util.List;

import javax.ejb.Local;

import cl.mdr.ifrs.ejb.entity.Catalogo;
import cl.mdr.ifrs.ejb.entity.EstadoCuadro;
import cl.mdr.ifrs.ejb.entity.Periodo;
import cl.mdr.ifrs.ejb.entity.TipoCelda;
import cl.mdr.ifrs.ejb.entity.TipoCuadro;
import cl.mdr.ifrs.ejb.entity.TipoDato;
import cl.mdr.ifrs.ejb.entity.TipoEstructura;


@Local
public interface MantenedoresTipoServiceLocal {
    
    Object mergeEntity(Object entity);
    
    Object persistEntity(Object entity);
    
    void deleteTipoCuadro(TipoCuadro entity) throws Exception;
    
    void deleteCuadro(Catalogo entity) throws Exception;
    
    List<Periodo> findAllPeriodo();
    
    Periodo findByPeriodo(Long periodo) throws Exception;
    
    List<TipoEstructura> findAllTipoEstructura();
    
    List<TipoCelda> findAllTipoCelda();
    
    List<TipoDato> findAllTipoDato();
    
    List<TipoCuadro> findAllTipoCuadro();
    
    List<EstadoCuadro> findAllEstadoCuadro();
    
    List<TipoCuadro> findByFiltro(TipoCuadro tipoCuadro) ;
    
    TipoCuadro findTipoCuadroById(final Long idTipoCuadro) throws Exception;
}
