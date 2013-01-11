package cl.bicevida.revelaciones.ejb.service.local;


import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.Columna;
import cl.bicevida.revelaciones.ejb.entity.Grilla;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.persistence.Query;


@Local
public interface CeldaServiceLocal {
    
    Object mergeEntity(Celda entity) throws Exception;

    Object persistEntity(Celda entity);
    
    Celda findCeldaById(Celda celda) throws Exception;
    
    List<Celda> findCeldaByColumna(Columna columna);
    
    List<Celda> findCeldaByGrupo(Long grupo, Grilla grilla) throws Exception;
    
    Long findMaxGrupo(Grilla grilla) throws Exception;
    
    public List<Celda> findCeldaByIdFila(Long idGrilla, Long idFila) throws Exception;
    
    void persistCeldaList(List<Celda> celdaList) throws Exception;
    
    void persistFormulaEstaticaList(Grilla grilla, List<Celda> celdaList) throws Exception;
    
    void persistFormulaDinamicaMap(Grilla grilla, Map<Celda, List<Celda>> formulaDinamicaMap) throws Exception;
    
    void deleteFormulaDinamica(Celda celdaParent, List<Celda> celdaChildList) throws Exception;
    
    List<Celda> findCeldaByEeff(Long idPeriodo, Long idFecu);
    
    List<Celda> findCeldaByEeffDet(Long idPeriodo, Long idFecu,  Long idCuenta);
}
