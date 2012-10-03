package cl.bicevida.revelaciones.eeff;

import cl.bicevida.revelaciones.ejb.cross.EeffUtil;
import cl.bicevida.revelaciones.ejb.entity.Celda;

import cl.bicevida.revelaciones.ejb.entity.Columna;

import cl.bicevida.revelaciones.ejb.entity.EstadoFinanciero;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DependenciaEeffFactory {
    
    //idGrilla, Map<idColumna, listaCeldas>
    private Map<Long, Map<Long,List<Celda>>> grillaFilaCeldaMap;
    
    //Llave, celdas
    private Map<String, Celda> celdasMap;
    
    
    public DependenciaEeffFactory() {
        super();
        celdasMap = new HashMap<String, Celda>();
        grillaFilaCeldaMap = new HashMap<Long, Map<Long,List<Celda>>>();
    }
    
    
    public void putCeldas(final Celda celda){
        
        String llave = EeffUtil.getLlaveCeldaToString(celda);
        
        if(!celdasMap.containsKey(llave)){
            celdasMap.put(llave,celda);
            
            if(!grillaFilaCeldaMap.containsKey(celda.getIdGrilla())){
                Map<Long,List<Celda>> filaMap = new HashMap<Long,List<Celda>>();
                putNewCeldaList(celda, filaMap);
                grillaFilaCeldaMap.put(celda.getIdGrilla(), filaMap);
            }else{
                Map<Long,List<Celda>> filaMap = grillaFilaCeldaMap.get(celda.getIdGrilla());
                if(filaMap.containsKey(celda.getIdFila())){
                    filaMap.get(celda.getIdFila()).add(celda);
                }else{
                    putNewCeldaList(celda, filaMap);
                }
            }
        }
        
    }
    
    public List<DependenciaVO> getDependenciaVOThisInstance(){
        
        List<DependenciaVO> depVOList = new ArrayList<DependenciaVO>();
        Iterator<Map.Entry<Long, Map<Long,List<Celda>>>> itGrilla = grillaFilaCeldaMap.entrySet().iterator();
        
        while(itGrilla.hasNext()){
            
            DependenciaVO depVO = new DependenciaVO();            
            Map.Entry<Long, Map<Long,List<Celda>>> entry =  itGrilla.next();
            
            Long idGrilla = entry.getKey();
            depVO.setIdGrilla(idGrilla);
            
            Map<Long,List<Celda>> mapFila = entry.getValue();
            
            Iterator<Map.Entry<Long,List<Celda>>> itFila = mapFila.entrySet().iterator();
            
            List<FilaCeldaVO> filaCeldaList = new ArrayList<FilaCeldaVO>();
            
            while(itFila.hasNext()){
                
                FilaCeldaVO filaCelda = new FilaCeldaVO();
                
                Map.Entry<Long,List<Celda>> entryFila =  itFila.next();
                
                Long idFila = entryFila.getKey();
                filaCelda.setFila(idFila);
                
                filaCelda.setCeldaList(entryFila.getValue());
                
                filaCeldaList.add(filaCelda);
                
            }
            
            depVO.setFilaCeldaVO(filaCeldaList);
            
            depVOList.add(depVO);
            
        }
        
        return depVOList;

    }
    
    private void putNewCeldaList(final Celda celda, final Map<Long,List<Celda>> filaMap){
        List<Celda> celdaList = new ArrayList<Celda>();
        celdaList.add(celda);
        filaMap.put(celda.getIdFila(), celdaList);
    }


    public Map<Long, Map<Long, List<Celda>>> getGrillaFilaCeldaMap() {
        return grillaFilaCeldaMap;
    }

    public Map<String, Celda> getCeldasMap() {
        return celdasMap;
    }
}
