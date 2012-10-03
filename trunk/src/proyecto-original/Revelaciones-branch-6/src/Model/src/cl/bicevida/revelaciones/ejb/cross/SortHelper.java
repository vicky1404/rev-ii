package cl.bicevida.revelaciones.ejb.cross;


import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.Columna;
import cl.bicevida.revelaciones.ejb.entity.SubColumna;
import cl.bicevida.revelaciones.vo.FilaVO;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


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
    
    /**
     * Metodo que ordena las subcolumnas de una subgrilla
     * @param columnas
     */
    public static void sortSubColumnasByOrden(List<SubColumna> columnas){
        Collections.sort(columnas, new Comparator<SubColumna>(){                        
            public int compare(SubColumna c1, SubColumna c2){                
                return c1.getOrden().compareTo(c2.getOrden());
            }
        });
    }
    
}
