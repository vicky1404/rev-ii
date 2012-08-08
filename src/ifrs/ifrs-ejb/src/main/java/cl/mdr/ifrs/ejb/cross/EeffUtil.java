package cl.mdr.ifrs.ejb.cross;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.ifrs.ejb.entity.DetalleEeff;
import cl.mdr.ifrs.ejb.entity.EstadoFinanciero;
import cl.mdr.ifrs.ejb.entity.RelacionDetalleEeff;
import cl.mdr.ifrs.ejb.entity.RelacionEeff;
import cl.mdr.ifrs.ejb.entity.VersionEeff;
import cl.mdr.ifrs.ejb.reporte.vo.CargadorEeffVO;


public class EeffUtil {

public static String formatFecu(Long fecu){
        
        StringBuffer output = new StringBuffer("");
        
        if(fecu==null)
            return null;
        
        char[] fecuArray = fecu.toString().toCharArray();
        
        int i = 0;
        int largo = fecuArray.length-1;
        for(char c : fecuArray){
            output.append(c);
            if ((i % 2)==0 && i<largo)
                output.append(".");
            i++;
        }
        return output.toString();
    }    
    
    /*Errores*/
    public static void addErrorFecuNotFound(int col, int row, List<String> errores){
        errores.add("Código fecu no está registrado en base de datos : "+ col + " - línea " + row);
    }
    
    public static void addErrorFecu(int col, int row, List<String> errores){
        errores.add("Código fecu no existe en cabecera columna : "+ col + " - línea " + row);
    }
    
    public static void addErrorFecuNull(int col, int row, List<String> errores){
        errores.add("Código fecu inválido en columna : "+ col + " - línea " + row);
    }
    
    public static void addErrorFecuDu(int col, int row, List<String> errores){
        errores.add("Código fecu repetido : "+ col + " - línea " + row);
    }
    
    public static void addErrorCuentaNull(int col, int row, List<String> errores){
        errores.add("Número de cuenta inválido columna : "+ col + " - línea " + row);
    }
    
    public static void addRowNull(int row, List<String> errores){
        errores.add("No deben ir lineas en blanco - línea :" + row);
    }
    
    public static void addErrorCuantaDu(int col, int row, List<String> errores){
        errores.add("Número de cuenta contable repetido :columna : "+ col + " - línea " + row);
    }
    
    public static boolean esCuentaRepetida(final String cadena, final String nuevoValor){
        
    	if(cadena==null || nuevoValor==null)
            return false;
        
        String newCadena = cadena.replaceAll("[|+|\n]", "");
        
        StringTokenizer tokens = new StringTokenizer(newCadena, ";");
        
        while(tokens.hasMoreElements()){
            
            String str = tokens.nextToken();
            
            if(str.equals("["+nuevoValor+"]"))
                return true;
                                    
        }
        
        return false;
        
    }
    
    public static boolean esCuentaRepetida(final List<RelacionDetalleEeff> relDetEeffList, final DetalleEeff nuevoValor){
        if(!Util.esListaValida(relDetEeffList))
            return false;
        for(RelacionDetalleEeff relDetEeff : relDetEeffList){
            if(relDetEeff.getIdCuenta().equals(nuevoValor.getIdCuenta()) && relDetEeff.getIdFecu().equals(nuevoValor.getIdFecu()))
                return true;
        }
        return false;
    }
    
   
    
    public static Long getLongFromKeyStrFecu(final String key){
        
        if(key==null || key.length()<2)
            return new Long("0");
        
        return Util.getOnlyNumbers(key);
    }
    
    public static  String formatKeyFecu(Long idFecu){
        return "["+ EeffUtil.formatFecu(idFecu)+"]";
    }
    
    public static  String formatKeyCuenta(Long idCuenta){
        return "["+ idCuenta +"]";
    }
    
    public static String formatKeyFecu(final List<RelacionEeff> eeffs){
        
        if(!Util.esListaValida(eeffs))
            return null;
        
        String str = "";
        
        for(RelacionEeff eeff : eeffs){
            str = str + EeffUtil.formatKeyFecu(eeff.getIdFecu()) + ";";
        }
        
        return str;
    }
    
    public static String formatKeyCuenta(final List<RelacionDetalleEeff> detEeffs){
        
        if(!Util.esListaValida(detEeffs))
            return null;
        
        String str = "";
        
        for(RelacionDetalleEeff detalle : detEeffs){
            str = str+ EeffUtil.formatKeyCuenta(detalle.getIdCuenta()) + ";";
        }
        
        return str;
    }
    
    public static CargadorEeffVO convertToCargadoEeffVO(Map<Long, EstadoFinanciero> eeffMap, VersionEeff versionEeff){
        
        CargadorEeffVO cargadorVO = new CargadorEeffVO();
        
        List<EstadoFinanciero> eeffList = new ArrayList<EstadoFinanciero>();
        int cantidadaDet = 0;
        
        for(EstadoFinanciero eeff : eeffMap.values()){
            
            if(eeff.getDetalleEeffList4()!=null)
                cantidadaDet += eeff.getDetalleEeffList4().size();
            
            eeff.setVersionEeff(versionEeff);
            
            eeffList.add(eeff);
        }
        
        cargadorVO.setCatidadEeffProcesado(eeffList.size());
        cargadorVO.setCatidadEeffDetProcesado(cantidadaDet);
        
        return cargadorVO;
    }
    
    public static String getLlaveCeldaToString(Celda celda){
        if(celda==null)
            return null;
        return "["+celda.getIdGrilla()+","+celda.getIdColumna()+","+celda.getIdFila()+"]";
    }
    
    public static Long getLongFromKey(String key){
    	
    	if (key == null || key.length() < 2)
    		return new Long("2");
    	
    	String salida = key.substring(1, key.length()-1);
    	salida = salida.replaceAll("[.]", "");
    	return Util.getLong(salida, new Long("0"));
    }
}
