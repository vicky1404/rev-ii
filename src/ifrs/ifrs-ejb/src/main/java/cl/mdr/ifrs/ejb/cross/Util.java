package cl.mdr.ifrs.ejb.cross;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.xml.utils.XMLChar;
import org.w3c.tidy.Tidy;

import cl.mdr.ifrs.ejb.common.TipoCeldaEnum;
import cl.mdr.ifrs.ejb.common.TipoDatoEnum;
import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.ifrs.ejb.entity.Columna;
import cl.mdr.ifrs.ejb.entity.Grilla;
import cl.mdr.ifrs.ejb.entity.Html;


public class Util{
    public final static String NUMBER_DECIMAL_PATTERN = "#,###,###.####";
    public final static String NUMBER_INTEGER_PATTERN = "#,###,###.####";
    public final static String DATE_PATTERN_DD_MM_YYYY = "dd-MM-yyyy";
    public final static String DATE_PATTERN_DD_MM_YYYY_HH_MM_SS = "dd-MM-yyyy HH:mm:ss";
    public final static String STRING_VACIO = "";
    
    public Util(){
        super();
    }
    
    public static DecimalFormat getDecimalFormat(){                    
        Locale locale = new Locale("es", "CL");
        NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
        DecimalFormat decimalFormat = (DecimalFormat)numberFormat;
        decimalFormat.applyPattern(NUMBER_DECIMAL_PATTERN);
        return decimalFormat;
    }
    
    public static DecimalFormat getIntegerFormat(){                    
        Locale locale = new Locale("es", "CL");
        NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
        DecimalFormat decimalFormat = (DecimalFormat)numberFormat;
        decimalFormat.applyPattern(NUMBER_INTEGER_PATTERN);
        return decimalFormat;
    }
    
    /**
     * @param arg1
     * @param arg2
     * @return
     */
    public static Float getFloat(String arg1, Float arg2){
        try{
            return (Float.parseFloat(arg1));
        }catch(Exception e){
            return arg2; 
        }    
    }
    
    /**
     * @param arg1
     * @param arg2
     * @return
     */
    public static Float getFloat(Float arg1, Float arg2){
        try{
            return arg1.floatValue();
        }catch(Exception e){
            return arg2; 
        }    
    }
    
    /**
     * @param arg1
     * @param arg2
     * @return
     */
    public static Double getDouble(Double arg1, Double arg2){
        try{
            return arg1.doubleValue();
        }catch(Exception e){
            return arg2; 
        }    
    }
    
    public static Double getDouble(Object arg1, Double arg2){
        try{
        	return (Double)arg1;
        }catch(Exception e){
            //logger.error("Error al convertir Double ", e);
            return arg2; 
        }    
    }
    
    public static Double getDouble(BigDecimal arg1, Double arg2){
        try{
        	return arg1.doubleValue();
        }catch(Exception e){
            //logger.error("Error al convertir Double ", e);
            return arg2; 
        }    
    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    public static Integer getInteger(String arg1, Integer arg2){
        try{
            return (Integer.parseInt(arg1));
        }catch(Exception e){
            return arg2; 
        }    
    }
    
    public static Integer getInteger(Object arg1, Integer arg2){
        try{
            return (Integer)arg1;
        }catch(Exception e){
            //logger.error("Error al convertir Integer ", e);
            return arg2; 
        }    
    }
    
    public static BigDecimal getBigDecimal(Object arg1, BigDecimal arg2){
        try{
            return new BigDecimal(arg1.toString());
        }catch(Exception e){
            //logger.error("Error al convertir BigDecimal ", e);
            return arg2; 
        }    
    }
    
    public static BigDecimal getBigDecimal(String arg1, BigDecimal arg2){
        try{
            return new BigDecimal(arg1);
        }catch(Exception e){
            //logger.error("Error al convertir BigDecimal ", e);
            return arg2; 
        }    
    }
    
    
    public static int getInteger(int arg1, int arg2){
        try{
            return arg1+0;
        }catch(Exception e){
            return arg2; 
        }    
    }
    
    public static int getInteger(Integer arg1, Integer arg2){
        try{
            return arg1+0;
        }catch(Exception e){
            return arg2; 
        }    
    }
    
    public static Date getDate(final String arg1){        
        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN_DD_MM_YYYY);
        try {
            return format.parse(arg1);
        } catch (Exception e) {
            //logger.error(MessageFormat.format("Error al convertir la fecha {0} a Date ", arg1) ,e);
            return null;
        }
    }
    
    public static String getString(final Long arg1, String arg2){
        try {
            return arg1.toString();
        }catch(Exception e){
            //logger.error("error al convertir long to string "+arg1);
            return arg2; 
        }      
    }
    
    public static String getString(final BigDecimal arg1, String arg2){
        try {
            return arg1.toString();
        }catch(Exception e){
            return arg2; 
        }      
    }
    
    public static String getString(final Object arg1, String arg2){
        try {
            return (String)arg1;
        }catch(Exception e){
            return arg2; 
        }      
    }
    
    public static String getString(final Date arg1){        
        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN_DD_MM_YYYY);
        try {
            return format.format(arg1);
        } catch (Exception e) {
            //logger.error(MessageFormat.format("Error al convertir la fecha {0} a String ", arg1) ,e);
            return STRING_VACIO;
        }
    }
    
    
    
    public static String getMesPeriodo(Integer mes){
        if(mes < 10){
            return "0"+mes;
        }else{
            return ""+mes;
        }        
    }
    
    /**
     * @param arg1
     * @param arg2
     * @return
     */
    public static Long getLong(Long arg1, Long arg2){
        try{
            return arg1.longValue();
        }catch(Exception e){
            return arg2; 
        }    
    }
    
    public static Long getLong(Object arg1, Long arg2){
        try{
            return (Long)arg1;
        }catch(Exception e){
            //logger.error("Error al convertir Long ", e);
            return arg2; 
        }    
    }
    
    public static Long getLong(String arg1, Long arg2){
        try{
            return new Long(arg1);
        }catch(Exception e){
            //logger.error("Error al convertir Long ", e);
            return arg2; 
        }    
    }
    public static Long getLong(Integer arg1, Long arg2){
        try{
            return new Long(arg1);
        }catch(Exception e){            
            return arg2; 
        }    
    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    public static String bytesToString(byte[] arg1, String arg2 ){
        try{
            return new String(arg1);
        }catch(Exception e){
            return arg2;
        }
    }
    
    public static byte[] getBytes(String arg1){
        try{
            return arg1.getBytes();
        }catch(Exception e){
            return new byte[4];
        }
    }

    /**
     * @param lista
     * @return
     */
    @SuppressWarnings("rawtypes")
	public static boolean esListaValida(List lista){
        if(lista==null || lista.size()==0)
            return false;
        else
            return true;
    }
    
    /**
     * @param lista
     * @return
     */
    @SuppressWarnings("rawtypes")
	public static boolean esListaValida(Collection lista){
        if(lista==null || lista.size()==0)
            return false;
        else
            return true;
    }
    
    public static String htmlBytesToString(byte[] html){
        if(html != null){
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(html);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Tidy tidy = new Tidy();
            tidy.setXHTML(true);
            tidy.setDocType("omit");
            tidy.parse(byteArrayInputStream, byteArrayOutputStream);
            byte[] byteData = byteArrayOutputStream.toByteArray();
            return Util.bytesToString(byteData,"").replaceAll("<p>&nbsp;</p>\n", "");
        }else{
            return "";
        }
    }
    
    public static String clearHtml(String html){
        if(html != null && !html.equals("")){            
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Tidy tidy = new Tidy();
            tidy.setXHTML(true);
            tidy.setDocType("omit");
            tidy.parse(new StringReader(html), byteArrayOutputStream);
            byte[] byteData = byteArrayOutputStream.toByteArray();
            return Util.bytesToString(byteData,"");
        }else{
            return "";
        }
    }
    
        
    public static byte[] getBytes(Html html){
        try{
            return html.getContenido();
        }catch(Exception e){
            return new byte[4];
        }
    }

    /**
     * metodo paa capitalizar texto 
     * @param arg1
     * @return
     */
    public static String capitalizar(String arg1){
    	if(arg1 != null){
    		return WordUtils.capitalize(arg1.toLowerCase());
    	}else{
    		return null;
    	}
         
    }
    
    public static String stripInvalidXmlCharacters(String input) {        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (XMLChar.isValid(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    
    public static String cleanForXml(String text){
        String retorno = text.replace("\"", "")
                             .replace("#", "")
                             .replace("$", "")
                             .replace("%", "")
                             .replace("/", "")
                             .replace("\\", "")
                             .replace("&", "")
                             .replace("(", "")
                             .replace(")", "")
                             .replace("<", "")
                             .replace(">", "")
                             .replace("*", "");        
        Pattern INVALID_XML_CHARS = Pattern.compile("[^\\u0009\\u000A\\u000D\\u0020-\\uD7FF\\uE000-\\uFFFD\uD800\uDC00-\uDBFF\uDFFF]");       
        INVALID_XML_CHARS.matcher(retorno).replaceAll("");    
        return retorno;
    }
    
    public static boolean validarRut(int rut, char dv)
    {
        int m = 0, s = 1;
        for (; rut != 0; rut /= 10)
        {
            s = (s + rut % 10 * (9 - m++ % 6)) % 11;
        }
        return dv == (char) (s != 0 ? s + 47 : 75);
    }
    
    public static int countToken(String valor, String token){
        
        if(valor==null || token == null)
            return 0;
        
        StringTokenizer str = new StringTokenizer(valor, token);
        
        return str.countTokens();
    }

    /**
     * La clave del map es del formato [idColumna,idFila]
     * @param grilla
     * @return
     */
    public static Map<String, Celda> createCeldaMap(Grilla grilla){
        
        Map<String,Celda> celdaMap = new HashMap<String,Celda>();
        
        for(Columna columna : grilla.getColumnaList()){
            for(Celda celda : columna.getCeldaList()){
                celdaMap.put("["+ celda.getIdColumna() + "," + celda.getIdFila() + "]", celda);
            }
        }
        
        return celdaMap;
    }
    
    public static String formatCellKey(final Celda cell){
        
    	return  new StringBuilder()
    			.append("[")
    			.append(cell.getIdColumna())
    			.append(",")
    			.append(cell.getIdFila())
    			.append("]").toString();
    }
    
    public static String formatCeldaGrillaKey(final Celda cell){
        return  new StringBuilder()
        		.append("[")
        		.append(cell.getIdGrilla())
        		.append(",")
        		.append(cell.getIdColumna())
        		.append(",")
        		.append(cell.getIdFila())
        		.append("]").toString();
    }
    
    public static boolean isTotalorSubTotalNumeric(Celda cell){
        
        if( (cell.getTipoCelda().getIdTipoCelda().equals(TipoCeldaEnum.SUBTOTAL.getKey()) ||
             cell.getTipoCelda().getIdTipoCelda().equals(TipoCeldaEnum.TOTAL.getKey()) ) &&
            
            (cell.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.ENTERO.getKey()) ||
             cell.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.DECIMAL.getKey())) ){
            
            return true;
        }
        
        return false;
        
    }
    
    
    public static boolean isCellNumeric(Celda cell){
        
        if( (cell.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.ENTERO.getKey()) ||
             cell.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.DECIMAL.getKey())) ){
            
            return true;
        }
        
        return false;
        
    }
    
    public static Long getOnlyNumbers(final String str) {
    
        String numeros = "";

        if (str == null || str.length() == 0)
            return 0L;

        for (int i = 0; i < str.length(); i++) {

            if (Character.isDigit(str.charAt(i)))
                numeros = numeros + str.charAt(i);
        
        }
        
        return Util.getLong(numeros, 0L);
    }
    
    public static List<List<Celda>> builHtmlGrilla(List<Columna> columnasE){
                    
            List<List<Celda>> celdaDobleList = new ArrayList<List<Celda>>();
            
            int i=0;
            for(Columna columna : columnasE){
                    
                for(Celda celda : columna.getCeldaList()){
                        
                        try{
                                celdaDobleList.get(i).add(celda);
                                
                        }catch(IndexOutOfBoundsException e){
                                List<Celda> celdas = new ArrayList<Celda>();
                                celdas.add(celda);
                                celdaDobleList.add(celdas);
                        }
                        
                        i++;
                }
                
                i=0;
            }
            
            return celdaDobleList;
            
    }
    
    /**
     * Evalua una expresion regular para un par ordenado representado como un string con 
     * la forma +/-[n,m]. Por ejemplo +[2,3]
     * @param parOrdenado
     */
    public static boolean validaParOrdenadoConSigno(String parOrdenado){
    	String patron = "^(^(\\+|-){1}\\[[0-9],[0-9]\\])$";
    	Pattern p = Pattern.compile(patron);
    	Matcher m = p.matcher(parOrdenado);
      return m.find();
    }
    
    /**
     * Evalua una expresion regular para un par ordenado representado como un string con 
     * la forma [n,m]. Por ejemplo [2,3]
     * @param parOrdenado
     */
    public static boolean validaParOrdenadoSinSigno(String parOrdenado){
    	String patron = "^(\\[[0-9],[0-9]\\])$";
    	Pattern p = Pattern.compile(patron);
    	Matcher m = p.matcher(parOrdenado);
      return m.find();
    }
    
    
    public static int restaLic(Calendar fechaInicio, Calendar fechaFin){
    	
    	int respuesta = 0;
    	int anios = 0;
    	
    	if (fechaInicio != null && fechaFin != null){
    		
    		if (fechaFin.get(Calendar.YEAR) == fechaInicio.get(Calendar.YEAR)){
    			respuesta = fechaFin.get(Calendar.DAY_OF_YEAR) - fechaInicio.get(Calendar.DAY_OF_YEAR);
    		} else if (fechaFin.get(Calendar.YEAR) > fechaInicio.get(Calendar.YEAR)){
    			anios = fechaFin.get(Calendar.YEAR) - fechaInicio.get(Calendar.YEAR);
    			respuesta = (fechaFin.get(Calendar.DAY_OF_YEAR) + (((150*2)+(15*4)+5) * anios)) - fechaInicio.get(Calendar.DAY_OF_YEAR);
    		}
    	}
    	
    	return respuesta;
    }
}

