package cl.bicevida.revelaciones.ejb.service.local;


import cl.bicevida.revelaciones.ejb.entity.Columna;

import java.util.List;

import javax.ejb.Local;

@Local
public interface ColumnaServiceLocal {

    Object mergeEntity(Columna entity);

    Object persistEntity(Columna entity);
    
    List<Columna> getColumnaByGrillaFila(Long idGrilla, Long idFila);
    
    List<Columna> getColumnaByGrilla(Long idGrilla);
}
