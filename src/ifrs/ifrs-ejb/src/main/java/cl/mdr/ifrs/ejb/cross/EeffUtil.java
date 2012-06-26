package cl.mdr.ifrs.ejb.cross;

import java.util.List;
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
        errores.add("C�digo fecu no est� registrado en base de datos : "+ col + " - l�nea " + row);
    }
    
    public static void addErrorFecu(int col, int row, List<String> errores){
        errores.add("C�digo fecu no existe en cabecera columna : "+ col + " - l�nea " + row);
    }
    
    public static void addErrorFecuNull(int col, int row, List<String> errores){
        errores.add("C�digo fecu inv�lido en columna : "+ col + " - l�nea " + row);
    }
    
    public static void addErrorFecuDu(int col, int row, List<String> errores){
        errores.add("C�digo fecu repetido : "+ col + " - l�nea " + row);
    }
    
    public static void addErrorCuentaNull(int col, int row, List<String> errores){
        errores.add("N�mero de cuenta inv�lido columna : "+ col + " - l�nea " + row);
    }
    
    public static void addRowNull(int row, List<String> errores){
        errores.add("No deben ir lineas en blanco - l�nea :" + row);
    }
    
    public static void addErrorCuantaDu(int col, int row, List<String> errores){
        errores.add("N�mero de cuenta contable repetido :columna : "+ col + " - l�nea " + row);
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
