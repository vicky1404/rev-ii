package cl.bicevida.revelaciones.ejb.service.local;


import cl.bicevida.revelaciones.ejb.entity.Columna;

import java.io.Serializable;

import java.util.List;

import javax.ejb.Remote;


@Remote
public interface ColumnaServiceLocal extends Serializable {

    Object mergeEntity(Columna entity);

    Object persistEntity(Columna entity);
    
    List<Columna> getColumnaByGrillaFila(Long idGrilla, Long idFila);
    
    List<Columna> getColumnaByGrilla(Long idGrilla);
}
