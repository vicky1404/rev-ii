package cl.bicevida.revelaciones.ejb.cross;

import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.CodigoFecu;

import cl.bicevida.revelaciones.ejb.entity.EstadoFinanciero;
import cl.bicevida.revelaciones.ejb.entity.RelacionDetalleEeff;
import cl.bicevida.revelaciones.ejb.entity.RelacionEeff;

import cl.bicevida.revelaciones.ejb.entity.Version;
import cl.bicevida.revelaciones.ejb.entity.VersionEeff;
import cl.bicevida.revelaciones.eeff.CargadorEeffVO;

import cl.bicevida.revelaciones.ejb.entity.DetalleEeff;

import java.util.ArrayList;
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
    
    public static void addErrorCuentaDu(int col, int row, List<String> errores){
        errores.add("N�mero de cuenta contable repetido :columna : "+ col + " - l�nea " + row);
    }
    
    public static void addErrorCuentaNotFound(int col, int row, List<String> errores){
        errores.add("N�mero de cuenta contable  no est� registrado en base de datos, columna : "+ col + " - l�nea " + row);
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
    
    public static  String formatKeyFecuCuenta(Long idFeuc, Long idCuenta){
        return "["+ idFeuc + "," + idCuenta +"]";
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
    
    public static void setVersionEeffToEeffList(List<EstadoFinanciero> eeffList, VersionEeff versionEeff){
        
        for(EstadoFinanciero eeff : eeffList){
            eeff.setVersionEeff(versionEeff);
        }
    }
    
    public static String getLlaveCeldaToString(Celda celda){
        if(celda==null)
            return null;
        return "["+celda.getIdGrilla()+","+celda.getIdColumna()+","+celda.getIdFila()+"]";
    }
    
    public static Map<String, RelacionDetalleEeff> convertListRelEeffDetToMap(List<RelacionDetalleEeff> eeffDetList){
        
        Map<String, RelacionDetalleEeff> relDetMap = new HashMap<String, RelacionDetalleEeff>();
        
        for(RelacionDetalleEeff relDet : eeffDetList){
            relDetMap.put(formatKeyFecuCuenta(relDet.getIdFecu(), relDet.getIdCuenta()), relDet);
        }
        
        return relDetMap;
    }
    
    public static Map<String, DetalleEeff> convertListEeffDetToMap(List<DetalleEeff> eeffDetList){
        
        Map<String, DetalleEeff> relDetMap = new HashMap<String, DetalleEeff>();
        
        for(DetalleEeff detEeff : eeffDetList){
            relDetMap.put(formatKeyFecuCuenta(detEeff.getIdFecu(), detEeff.getIdCuenta()), detEeff);
        }
        
        return relDetMap;
    }
    
    public static String getTbodyRelEeff(){
        
        StringBuilder tbody = new StringBuilder();
        
        tbody 
            .append("<tbody>")
            .append("<tr>")
            .append("<th>C�digo FECU</th>")
            .append("<th>Columna</th>")
            .append("<th>N�mero Fila</th>")
            .append("<th>Celda</th>")
            .append("<th>Valor</th>")
            .append("</tr>")
            .append("</tbody>");
        
        return tbody.toString();
    }
    
    public static String getTbodyRelDetEeff(){
        
        StringBuilder tbody = new StringBuilder();
        
        tbody 
            .append("<tbody>")
            .append("<tr>")
            .append("<th>C�digo FECU</th>")
            .append("<th>C�digo Cuenta</th>")
            .append("<th>Columna</th>")
            .append("<th>N�mero Fila</th>")
            .append("<th>Celda</th>")
            .append("<th>Valor</th>")
            .append("</tr>")
            .append("</tbody>");
        
        return tbody.toString();
        
    }   
        
}