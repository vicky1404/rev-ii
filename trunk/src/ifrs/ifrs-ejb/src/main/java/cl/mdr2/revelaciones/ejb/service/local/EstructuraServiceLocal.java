package cl.bicevida.revelaciones.ejb.service.local;


import cl.bicevida.revelaciones.ejb.entity.AgrupacionColumna;
import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.Estructura;
import cl.bicevida.revelaciones.ejb.entity.Grilla;
import cl.bicevida.revelaciones.ejb.entity.HistorialVersion;
import cl.bicevida.revelaciones.ejb.entity.Version;
import cl.bicevida.revelaciones.exceptions.FormulaException;
import cl.bicevida.revelaciones.vo.GrillaVO;

import java.util.List;
import java.util.Set;

import javax.ejb.Local;


@Local
public interface EstructuraServiceLocal {
    
    Object queryByRange(String jpqlStmt, int firstResult, int maxResults);
    
    List<Celda> getCeldasFromGrilla(Grilla grilla) throws Exception;
    
    Set<Long> getTotalFilasFromGrilla(List<Celda> celdas) throws Exception;
    
    List<Estructura> findEstructuraByVersion(Version version) throws Exception;
    
    public List<Estructura> findEstructuraByVersionTipo(Version version, Long idTipoEstructura) throws Exception;
    
    List<Estructura> getEstructuraByVersion(Version version, boolean applyFormula) throws FormulaException, Exception;
    
    public List<Estructura> getEstructuraByVersionTipo(Version version, Long idTipoEstructura) throws Exception;
    
    //List<FilaVO> buildGrilla(Grilla grilla)throws Exception;
    
    Estructura mergeEstructura(Estructura estructura) throws Exception;
    
    void mergeEstructuraList(List<Estructura> estructuraList) throws Exception;
    
    void persistEstructuraList(List<Estructura> estructuraList) throws Exception;
    
    List<Estructura> getEstructuraTipoGrillaByVersion(Version version) throws Exception;
    
    GrillaVO getGrillaVO(Grilla grilla, boolean applyFormula) throws FormulaException, Exception;
    
    Estructura findEstructuraById(Long id) throws Exception;
    
    List<AgrupacionColumna> findAgrupacionColumnaByGrilla(Grilla grilla) throws Exception;
    
    AgrupacionColumna findAgrupacionColumnaById(AgrupacionColumna agrupacion) throws Exception;
    
    List<Object> sql(String sql) throws Exception;
    
    void persistEstructura(List<Estructura> estructuras,  HistorialVersion historialVersionPeriodo) throws Exception;
    
    public List<AgrupacionColumna> findAgrupacionColumnaByGrillaNivel(Long idGrilla, Long idNivel) throws Exception;
    
}
