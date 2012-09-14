package cl.bicevida.revelaciones.ejb.service.local;


import cl.bicevida.revelaciones.ejb.entity.EstadoCuadro;
import cl.bicevida.revelaciones.ejb.entity.Periodo;
import cl.bicevida.revelaciones.ejb.entity.TipoCelda;
import cl.bicevida.revelaciones.ejb.entity.TipoCuadro;
import cl.bicevida.revelaciones.ejb.entity.TipoDato;
import cl.bicevida.revelaciones.ejb.entity.TipoEstructura;

import java.util.List;

import javax.ejb.Local;

import javax.persistence.NoResultException;


@Local
public interface MantenedoresTipoServiceLocal {
    
    Object mergeEntity(Object entity);
    
    Object persistEntity(Object entity);
    
    List<Periodo> findAllPeriodo();
    
    Periodo findByPeriodo(Long periodo) throws Exception;
    
    List<TipoEstructura> findAllTipoEstructura();
    
    List<TipoCelda> findAllTipoCelda();
    
    List<TipoDato> findAllTipoDato();
    
    List<TipoCuadro> findAllTipoCuadro();
    
    List<EstadoCuadro> findAllEstadoCuadro();
}
