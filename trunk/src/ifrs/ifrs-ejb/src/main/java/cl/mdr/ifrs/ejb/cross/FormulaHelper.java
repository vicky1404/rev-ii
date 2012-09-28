package cl.mdr.ifrs.ejb.cross;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.maxFrom;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.ifrs.ejb.entity.Columna;

public class FormulaHelper {
	
	public static final String CORCHETE_IZQUIERDO = "[";
	public static final String CORCHETE_DERECHO = "]";
	public static final char SIGNO_SUMA = '+';
	public static final char SIGNO_RESTA = '-';
	public static final String COMA = ",";
	public static final String PUNTO_COMA = ";";
	public static final String DOS_PUNTOS = ":"; 
	public static final String H_PREFIX = "h-";
	public static final String V_PREFIX = "v-";
	
	
	
	public static void marcarCeldasByCeldaTarget(final Celda celdaTarget,final List<Celda> celdaGrillaList, final Map<Celda, List<Celda>> celdasTotalMap){
		
		
		if(celdaTarget==null || celdaGrillaList==null)
			return;
		
		
		for(Celda celda : celdaGrillaList){
			celda.setSelectedByFormula(Boolean.FALSE);
		}
		
		if(celdaTarget.getParentVertical() != null){
		
			final List<Celda> childVerticalList = select(celdaGrillaList ,having(on(Celda.class).getChildVertical(), equalTo(celdaTarget.getParentVertical())));
			
			if(Util.esListaValida(childVerticalList)){
				
				
				celdaTarget.setFormula(buildFormulaString(childVerticalList));
				List<Celda> celdaTempList = new ArrayList<Celda>();
			
				for(Celda celda : childVerticalList){
					celda.setSelectedByFormula(Boolean.TRUE);
					celdaTempList.add(celda);
				}
				
				celdasTotalMap.put(celdaTarget, celdaTempList);
				
			}
			
		}else if(celdaTarget.getParentHorizontal()!= null){
			
			final List<Celda> childHorizontalList = select(celdaGrillaList ,having(on(Celda.class).getChildHorizontal(), equalTo(celdaTarget.getParentHorizontal())));
			
			if(Util.esListaValida(childHorizontalList)){
				
				celdaTarget.setFormula(buildFormulaString(childHorizontalList));
				List<Celda> celdaTempList = new ArrayList<Celda>();
			
				for(Celda celda : childHorizontalList){
					celda.setSelectedByFormula(Boolean.TRUE);
					celdaTempList.add(celda);
				}
				
				celdasTotalMap.put(celdaTarget, celdaTempList);
			
			}
			
		}
			
	}
	
	
	public static void descargarCeldas(final List<Celda> celdaGrillaList){
		for(Celda celda : celdaGrillaList){
			celda.setSelectedByFormula(Boolean.FALSE);
		}
	}
	
	
	/**
	 * retorna 1 si es correcta y es vertical
	 * retorna 2 si es correcta y es horizontal
	 * retorna 3 si es incorrecta
	 */
	public static int isValidPrimeraPosCeldaDinamica(final Celda celdaTarget, final Celda celdaCheck){
		
		if(celdaTarget.getIdColumna().equals(celdaCheck.getIdColumna())){
			celdaTarget.setFormula(Util.formatCellKey(celdaCheck));
			return 1;
		}
		
		if(celdaTarget.getIdFila().equals(celdaCheck.getIdFila())){
			celdaTarget.setFormula(Util.formatCellKey(celdaCheck));
			return 2;
		}
		
		return 3;
	}
	
	/**
	 * retorna 1 si es correcta y es vertical
	 * retorna 2 si es correcta y es horizontal
	 * retorna 3 si es incorrecta
	 */
	public static int isValidPosCeldaDinamica(final Celda celdaTarget, final Celda celdaPrimera, final Celda celdaCheck){
		
		if(celdaTarget.getIdColumna().equals(celdaPrimera.getIdColumna())){
			if(celdaTarget.getIdColumna().equals(celdaCheck.getIdColumna())){
				celdaTarget.setFormula(buildFormulaString(celdaPrimera, celdaCheck));
				return 1;
			}
		}
		
		if(celdaTarget.getIdFila().equals(celdaPrimera.getIdFila())){
			if(celdaTarget.getIdFila().equals(celdaCheck.getIdFila())){
				celdaTarget.setFormula(buildFormulaString(celdaPrimera, celdaCheck));
				return 2;
			}
		}
			
		
		return 3;
	}
	
	
	/**
	 * retorna 1 si es correcta y es vertical
	 * retorna 2 si es correcta y es horizontal
	 * retorna 3 si es incorrecta
	 */
	public static int isValidPosCeldaDinamica(final Celda celdaTarget, final Celda celdaPrimera,  final Celda celdaUltima, final Celda celdaCheck){
		
		if(celdaPrimera.getIdColumna().equals(celdaUltima.getIdColumna())){
			
			if(celdaTarget.getIdColumna().equals(celdaPrimera.getIdColumna()) &&
			   celdaCheck.getIdColumna().equals(celdaPrimera.getIdColumna())){
				celdaTarget.setFormula(buildFormulaString(celdaPrimera, celdaUltima));
				return 1;
			}
			
		}else if(celdaPrimera.getIdFila().equals(celdaUltima.getIdFila())){
			if( celdaTarget.getIdFila().equals(celdaPrimera.getIdFila()) &&
				celdaPrimera.getIdFila().equals(celdaCheck.getIdFila())){
				celdaTarget.setFormula(buildFormulaString(celdaPrimera, celdaUltima));
				return 2;
			}
		}

		return 3;
	}
	
	
	/**
     * obtiene el maximo indice de parent horizontal desde el conjunto de celdas.
     * @return
     */
    public static Long getMaxParentHorizontal(final Celda celdaTarget, final List<Celda> celdaList){        
        
    	if(celdaTarget.getParentHorizontal() != null){
            return celdaTarget.getParentHorizontal();
        }else{
            return (Util.getLong(maxFrom(celdaList).getParentHorizontal(), 0L)+1L);
        }
    	
    }

    /**
     * obtiene el maximo indice de parent vertical desde el conjunto de celdas.
     * @return
     */
    public static Long getMaxParentVertical(final Celda celdaTarget, final List<Celda> celdaList){   
        if(celdaTarget.getParentVertical() != null){
            return celdaTarget.getParentVertical();
        }else{
            return (Util.getLong(maxFrom(celdaList).getParentVertical(), 0L)+1L);
        }
    }
    
    
    private static String buildFormulaString(List<Celda> celdaList){
    	
		StringBuilder formula = new StringBuilder(Util.formatCellKey(celdaList.get(0)));
		formula.append(':');
		formula.append(Util.formatCellKey(celdaList.get(celdaList.size()-1)));
    	
    	return formula.toString();
    }
	
	private static String buildFormulaString(final Celda celdaPrimera,  final Celda celdaUltima){
    	
		StringBuilder formula = new StringBuilder(Util.formatCellKey(celdaPrimera));
		formula.append(':');
		formula.append(Util.formatCellKey(celdaUltima));
    	
    	return formula.toString();
    }
	
	
	
	/**
	 * Utilidades para formula service bean 
	 */
	
	public static Map<String,List<Celda>> convertCellToMapList(List<Columna> columns){
        
        Map<String,List<Celda>> cellMap = new LinkedHashMap<String,List<Celda>>();
        String key;
        for(Columna column : columns){
            for(Celda cell : column.getCeldaList()){
                if(cell.getChildHorizontal()!=null){
                    key = H_PREFIX+cell.getChildHorizontal();
                    if(cellMap.containsKey(key)){
                        cellMap.get(key).add(cell);
                    }else{
                        List<Celda> cells = new ArrayList<Celda>();
                        cells.add(cell);
                        cellMap.put(key, cells);
                    }
                }
                if(cell.getChildVertical()!=null){
                    key = V_PREFIX+cell.getChildVertical();
                    if(cellMap.containsKey(key)){
                        cellMap.get(key).add(cell);
                    }else{
                        List<Celda> cells = new ArrayList<Celda>();
                        cells.add(cell);
                        cellMap.put(key, cells);
                    }
                }
            }
        }
        
        return cellMap;
    }
	
	
	public static Map<String, Celda> convertCellToMap(final List<Columna> columns){
        
        Map<String, Celda> cellMap = new HashMap<String, Celda>();
        
        for(Columna column : columns){
            for(Celda cell : column.getCeldaList()){
                cellMap.put(Util.formatCellKey(cell), cell);
            }
        }
        
        return cellMap;
    }

    
}
