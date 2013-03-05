package cl.bicevida.revelaciones.ejb.service.local;


import cl.bicevida.revelaciones.ejb.entity.EstadoCuadro;
import cl.bicevida.revelaciones.ejb.entity.Periodo;
import cl.bicevida.revelaciones.ejb.entity.TipoCelda;
import cl.bicevida.revelaciones.ejb.entity.TipoCuadro;
import cl.bicevida.revelaciones.ejb.entity.TipoDato;
import cl.bicevida.revelaciones.ejb.entity.TipoEstructura;

import java.io.Serializable;

import java.util.List;

import javax.ejb.Remote;


@Remote
public interface MantenedoresTipoServiceLocal extends Serializable {
    
    Object mergeEntity(Object entity);
    
    Object persistEntity(Object entity);
    
    List<Periodo> findAllPeriodo();
    
    Periodo findByPeriodo(Long periodo) throws Exception;
    
    List<TipoEstructura> findAllTipoEstructura();
    
    List<TipoCelda> findAllTipoCelda();
    
    List<TipoDato> findAllTipoDato();
    
    List<TipoCuadro> findAllTipoCuadro();
    
    List<EstadoCuadro> findAllEstadoCuadro();
    
    EstadoCuadro findEstadoCuadroById(Long idEstado);
}
