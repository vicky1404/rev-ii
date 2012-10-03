package cl.bicevida.revelaciones.ejb.cross;

import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.CodigoFecu;

import cl.bicevida.revelaciones.ejb.entity.EstadoFinanciero;
import cl.bicevida.revelaciones.ejb.entity.RelacionDetalleEeff;
import cl.bicevida.revelaciones.ejb.entity.RelacionEeff;

import cl.bicevida.revelaciones.ejb.entity.Version;
import cl.bicevida.revelaciones.ejb.entity.VersionEeff;
import cl.bicevida.revelaciones.eeff.CargadorEeffVO;

import cl.bicevida.revelaciones.ejb.entity.Catalogo;
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
    
    public static void addErrorCuentaDu(int col, int row, List<String> errores){
        errores.add("Número de cuenta contable repetido :columna : "+ col + " - línea " + row);
    }
    
    public static void addErrorCuentaNotFound(int col, int row, List<String> errores){
        errores.add("Número de cuenta contable  no está registrado en base de datos, columna : "+ col + " - línea " + row);
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
            .append("<tr bgcolor='#81BEF7'>")
            .append("<th >").append(getFontTag()).append("Código FECU").append(getFontCloseTag()).append("</th>")
            .append("<th >").append(getFontTag()).append("Columna").append(getFontCloseTag()).append("</th>")
            .append("<th >").append(getFontTag()).append("Número Fila").append(getFontCloseTag()).append("</th>")
            .append("<th >").append(getFontTag()).append("Celda").append(getFontCloseTag()).append("</th>")
            .append("<th >").append(getFontTag()).append("Valor").append(getFontCloseTag()).append("</th>")
            .append("</tr>")
            .append("</tbody>");
        
        return tbody.toString();
    }
    
    public static String getTbodyRelDetEeff(){
        
        StringBuilder tbody = new StringBuilder();
        
        tbody 
            .append("<tbody>")
            .append("<tr bgcolor='#81BEF7'>")
            .append("<th >").append(getFontTag()).append("Código FECU").append(getFontCloseTag()).append("</th>")
            .append("<th >").append(getFontTag()).append("Código Cuenta").append(getFontCloseTag()).append("</th>")
            .append("<th >").append(getFontTag()).append("Columna").append(getFontCloseTag()).append("</th>")
            .append("<th >").append(getFontTag()).append("Número Fila").append(getFontCloseTag()).append("</th>")
            .append("<th >").append(getFontTag()).append("Celda").append(getFontCloseTag()).append("</th>")
            .append("<th >").append(getFontTag()).append("Valor").append(getFontCloseTag()).append("</th>")
            .append("</tr>")
            .append("</tbody>");
        
        return tbody.toString();
        
    }
    
    public static String getTableTag(){
        return "<table cellspacing='0' width='800px'>";
    }
    
    public static String getTableCloseTag(){
        return "</table>";
    }
    
    public static String getSaltoLineaTag(){
        return "<br>";
    }
    
    public static String getFontTag(){
        return "<font face='Arial' size='2'>";
    }
    
    public static String getFontCloseTag(){
        return "</font>";
    }
    
    public static String getTdFontTag(){
        return "<td><font face='Arial' size='2'>";
    }
    
    public static String getTdFontCloseTag(){
        return "</font></td>";
    }
    
    public static Map<Long, List<RelacionEeff>> convertRelEeffListToMapByGrilla(List<RelacionEeff> relEeffList){
        
        Map<Long, List<RelacionEeff>> relEeffMap = new HashMap<Long, List<RelacionEeff>>();
        
        for(RelacionEeff relEeff : relEeffList){
            Long idCatalogo = relEeff.getCelda2().getColumna().getGrilla().getEstructura1().getVersion().getCatalogo().getIdCatalogo();
            if(relEeffMap.containsKey(idCatalogo)){
                relEeffMap.get(idCatalogo).add(relEeff);
            }else{
                List<RelacionEeff> relEeffTempList = new ArrayList<RelacionEeff>();
                relEeffTempList.add(relEeff);
                relEeffMap.put(idCatalogo, relEeffTempList);
            }
        }
        
        return relEeffMap;
    }
    
    public static Map<Long, List<RelacionDetalleEeff>> convertRelDetEeffListToMapByGrilla(List<RelacionDetalleEeff> relDetEeffList){
        
        Map<Long, List<RelacionDetalleEeff>> relDetEeffMap = new HashMap<Long, List<RelacionDetalleEeff>>();
        
        for(RelacionDetalleEeff relDetEeff : relDetEeffList){
            Long idCatalogo = relDetEeff.getCelda5().getColumna().getGrilla().getEstructura1().getVersion().getCatalogo().getIdCatalogo();
            if(relDetEeffMap.containsKey(idCatalogo)){
                relDetEeffMap.get(idCatalogo).add(relDetEeff);
            }else{
                List<RelacionDetalleEeff> relEeffTempList = new ArrayList<RelacionDetalleEeff>();
                relEeffTempList.add(relDetEeff);
                relDetEeffMap.put(idCatalogo, relEeffTempList);
            }
        }
        
        return relDetEeffMap;
    }
    
    public static String concatUsuarioMail(String usuario){
        if(usuario==null)
            return "";
        return usuario.toUpperCase()+"@BICEVIDA.CL";
    }
        
}
