package cl.mdr.ifrs.ejb.cross;


import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.ComparatorUtils;

import ch.lambdaj.function.compare.ArgumentComparator;
import cl.mdr.ifrs.ejb.entity.AgrupacionColumna;
import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.ifrs.ejb.entity.Columna;
import cl.mdr.ifrs.ejb.entity.Grilla;
import cl.mdr.ifrs.ejb.entity.Version;
import cl.mdr.ifrs.vo.FilaVO;


public class SortHelper {
    
    public SortHelper() {
        super();
    }

    /**
     * ordena un listado de filasNotaVO 
     * segun su numero de fila.
     * @param filasNotaVO
     */
    public static void sortFilasByNumeroFila(List<FilaVO> filasNotaVO){
        Collections.sort(filasNotaVO, new Comparator<FilaVO>(){                        
            public int compare(FilaVO f1, FilaVO f2){                
                return f1.getNumeroFila().compareTo(f2.getNumeroFila());
            }
        });
    }

    /**
     * ordena un listado de celdas en funcion de su numero de columna
     * @param celdas
     */
    public static void sortCeldasByNumeroColumna(List<Celda> celdas){
        Collections.sort(celdas, new Comparator<Celda>(){                        
            public int compare(Celda c1, Celda c2){                
                return c1.getIdColumna().compareTo(c2.getIdColumna());
            }
        });
    }
    
    public static void sortColumnasByOrden(List<Columna> columnas){
        Collections.sort(columnas, new Comparator<Columna>(){                        
            public int compare(Columna c1, Columna c2){                
                return c1.getOrden().compareTo(c2.getOrden());
            }
        });
    }
    
    public static void sortVersionByOrdenCatalogo(List<Version> versiones){
        Collections.sort(versiones, new Comparator<Version>(){                        
            public int compare(Version v1, Version v2){                
                return v1.getCatalogo().getOrden().compareTo(v2.getCatalogo().getOrden());
            }
        });
    }
    
    public static void sortVersionDesc(List<Version> versiones){
        Collections.sort(versiones, new Comparator<Version>(){                        
            public int compare(Version v1, Version v2){                
                return v2.getVersion().compareTo(v1.getVersion());
            }
        });
    }
    
    public static void sortVersionAsc(List<Version> versiones){
        Collections.sort(versiones, new Comparator<Version>(){                        
            public int compare(Version v1, Version v2){                
                return v1.getVersion().compareTo(v2.getVersion());
            }
        });
    }
    
    public static void sortAgrupacionColumnaByNivel(List<AgrupacionColumna> agrupacionColumnas){
        Collections.sort(agrupacionColumnas, new Comparator<AgrupacionColumna>(){                        
            public int compare(AgrupacionColumna a1, AgrupacionColumna a2){                
                return a1.getIdNivel().compareTo(a2.getIdNivel());
            }
        });
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void sortGrilla(Grilla grilla){
    	
    	if(grilla.getColumnaList()==null)
    		return;
    	
    	List<Columna> sortedColumnas = sort(grilla.getColumnaList(), on(Columna.class).getIdColumna());
    	
    	
		final Comparator byCelColumna = new ArgumentComparator(on(Celda.class).getIdColumna());
        final Comparator byCelFila = new ArgumentComparator(on(Celda.class).getIdFila());
        
        final Comparator celdaOrdeBy = ComparatorUtils.chainedComparator(new Comparator[] {byCelColumna,byCelFila});
        
    	for(Columna columna : sortedColumnas){
    		
    		if(columna.getCeldaList()==null)
    			continue;
    		
    		columna.setCeldaList(sort(columna.getCeldaList(), on(Celda.class), celdaOrdeBy));
    	}
    	
    	grilla.setColumnaList(sortedColumnas);
    	
    }
    
    
}