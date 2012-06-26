package cl.bicevida.revelaciones.ejb.cross;

import cl.bicevida.revelaciones.ejb.entity.CodigoFecu;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

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
    
    public static Long getLongFromKey(String key){
        
        if(key==null || key.length()<2)
            return new Long("2");
        
        String salida =  key.substring(1,key.length()-1); 
        salida = salida.replaceAll("[.]", "");
        return Util.getLong(salida, new Long("0"));
    }
}
