package cl.bicevida.revelaciones.ejb.cross;

import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.CodigoFecu;

import cl.bicevida.revelaciones.ejb.entity.EstadoFinanciero;
import cl.bicevida.revelaciones.ejb.entity.RelacionDetalleEeff;
import cl.bicevida.revelaciones.ejb.entity.RelacionEeff;

import cl.bicevida.revelaciones.ejb.entity.Version;
import cl.bicevida.revelaciones.ejb.entity.VersionEeff;
import cl.bicevida.revelaciones.eeff.CargadorEeffVO;

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
}
