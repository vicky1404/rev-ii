package cl.mdr.ifrs.ejb.reporte.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.AgrupacionColumna;
import cl.mdr.ifrs.vo.AgrupacionModelVO;


public abstract class SoporteReporte {

    @SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(SoporteReporte.class);
    
    public final static String TIPO_STRING = String.class.getName();
    public final static String TIPO_DATE = Date.class.getName();
    public final static String TIPO_INTEGER = Integer.class.getName();
    public final static String TIPO_DOUBLE = Double.class.getName();
    public final static String TIPO_FLOAT = Float.class.getName();
    public final static String TIPO_LONG = Long.class.getName();
    public final static String TIPO_TIME_STAMP = Timestamp.class.getName();
    
    public final static String TIPO_XLS = "xls";
    public final static String TIPO_XLSX = "xlsx";
    public final static String TIPO_PDF = "pdf";
    public final static String TIPO_DOCX = "docx";
    public final static String NOMBRE_REPORTE = "informe_revelaciones";
    
    public final static String CONTENT_TYPE_XLS = "application/vnd.ms-excel";
    public final static String CONTENT_TYPE_PDF = "application/pdf";
    public final static String CONTENT_TYPE_DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    
    public final static String STRING_SIN_DATO = "";
    
    public final static String FOOTER_PAGINA = " - P�gina ";
    
    public final static String LINK_NAME="(+)(-)";

    public SoporteReporte() {

    }

    public static List<AgrupacionModelVO> createAgrupadorVO(List<AgrupacionColumna> agrupaciones){
        
        List<AgrupacionModelVO> agrupacionesVO = new ArrayList<AgrupacionModelVO>();
        
        if(!Util.esListaValida(agrupaciones))
            return agrupacionesVO;
        
        Long min=100L;
        Long max=0L;
        AgrupacionColumna agrupacionPaso =null;
        int contador = 0;
        for(AgrupacionColumna agrupacion : agrupaciones){
            
            contador++;
            
            if(agrupacionPaso !=null && agrupacion.getGrupo()==agrupacionPaso.getGrupo()){
                if(min>agrupacion.getIdColumna()){
                    min=agrupacion.getIdColumna();
                }
                if(max<agrupacion.getIdColumna()){
                    max=agrupacion.getIdColumna();
                }
            }else{
                if(agrupacionPaso!=null){
                    agrupacionesVO.add(new AgrupacionModelVO(min, max, agrupacionPaso.getGrupo(), agrupacionPaso.getAncho(), agrupacionPaso.getTitulo(),agrupacionPaso.getIdGrilla(),agrupacionPaso.getIdNivel()));
                }
                agrupacionPaso = agrupacion;
                min = agrupacion.getIdColumna();
                max = agrupacion.getIdColumna();
            }
            if(contador == agrupaciones.size()){
                agrupacionesVO.add(new AgrupacionModelVO(min, max, agrupacionPaso.getGrupo(), agrupacion.getAncho(), agrupacion.getTitulo(),agrupacionPaso.getIdGrilla(),agrupacionPaso.getIdNivel()));
            }
            
            }
        
        return agrupacionesVO;
    }
      
    public static XSSFFont getFontTitle(XSSFWorkbook wb) {
        XSSFFont font = wb.createFont();
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short)13);
        return font;
    }
    
    public static XSSFFont getFontSubTitle(XSSFWorkbook wb) {
        XSSFFont font = wb.createFont();
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short)11);
        return font;
    }

    public static XSSFFont getFontColumnHeader(XSSFWorkbook wb) {
        XSSFFont font = wb.createFont();
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short)11);
        return font;
    }

    public static XSSFFont getFontTotal(XSSFWorkbook wb) {
        XSSFFont font = wb.createFont();
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short)10);
        return font;
    }

    public static XSSFFont getFontSubTotal(XSSFWorkbook wb) {
        XSSFFont font = wb.createFont();
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short)10);
        return font;
    }

    public static XSSFFont getFontTitulo(XSSFWorkbook wb) {
        XSSFFont font = wb.createFont();
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short)10);
        return font;
    }
    
    public static XSSFFont getFontNormal(XSSFWorkbook wb) {
        XSSFFont font = wb.createFont();
        font.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);        
        font.setFontHeightInPoints((short)10);
        
        return font;
    }

    public static XSSFCellStyle getCellWithBorder(XSSFWorkbook wb) {
        XSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        return style;
    }
    
    public static XSSFCellStyle getCellDecimalStyle(XSSFWorkbook wb, XSSFColor rowColor){
        DataFormat format = wb.createDataFormat();
        XSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setDataFormat(format.getFormat("#,###,###,###.####"));
        style.setFillForegroundColor(rowColor);
        //style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);  
        return style;
    }
    
    public static XSSFCellStyle getCellIntegerStyle(XSSFWorkbook wb, XSSFColor rowColor){
        DataFormat format = wb.createDataFormat();
        XSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setDataFormat(format.getFormat("#,###,###,###"));
        style.setFillForegroundColor(rowColor);
        //style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);  
        return style;
    }
    
    public static XSSFCellStyle getCellDateStyle(XSSFWorkbook wb, XSSFColor rowColor){        
        CreationHelper createHelper = wb.getCreationHelper();
        XSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setDataFormat(createHelper.createDataFormat().getFormat(cl.mdr.ifrs.ejb.cross.Util.DATE_PATTERN_DD_MM_YYYY));
        style.setFillForegroundColor(rowColor);
        //style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);  
        return style;
    }
    
    public static String getNombreReporteDocx(Date date){
        String sufijoNombreReporte = "_".concat(new SimpleDateFormat("dd-MM-yyyy_HH-mm").format(date));
        return SoporteReporte.NOMBRE_REPORTE.concat(sufijoNombreReporte).concat(".").concat(SoporteReporte.TIPO_DOCX);
    }
    
    public static List<List<AgrupacionModelVO>> crearAgrupadorHTMLVO(List<AgrupacionColumna> agrupaciones){
    	
    	List<List<AgrupacionModelVO>> agrupacionesVO = new ArrayList<List<AgrupacionModelVO>>();
    	Map<Long,List<AgrupacionModelVO>> agrupacionMap = new LinkedHashMap<Long,List<AgrupacionModelVO>>();
        
        if(!Util.esListaValida(agrupaciones))
            return agrupacionesVO;
        
        Long min=100L;
        Long max=0L;
        AgrupacionColumna agrupacionPaso =null;
        int contador = 0;
        
        for(AgrupacionColumna agrupacion : agrupaciones){
        	
            contador++;
            
            if(agrupacionPaso !=null && agrupacion.getGrupo()==agrupacionPaso.getGrupo()){
                if(min>agrupacion.getIdColumna()){
                    min=agrupacion.getIdColumna();
                }
                if(max<agrupacion.getIdColumna()){
                    max=agrupacion.getIdColumna();
                }
            }else{
                if(agrupacionPaso!=null){
                	setAgrupadorMap(agrupacionMap, new AgrupacionModelVO(min, max, agrupacionPaso.getGrupo(), agrupacionPaso.getAncho(), agrupacionPaso.getTitulo(),agrupacionPaso.getIdGrilla(),agrupacionPaso.getIdNivel()));
                }
                agrupacionPaso = agrupacion;
                min = agrupacion.getIdColumna();
                max = agrupacion.getIdColumna();
            }
            if(contador == agrupaciones.size()){
            	setAgrupadorMap(agrupacionMap,new AgrupacionModelVO(min, max, agrupacionPaso.getGrupo(), agrupacion.getAncho(), agrupacion.getTitulo(),agrupacionPaso.getIdGrilla(),agrupacionPaso.getIdNivel()));
            }
            
        }
        
        for(List<AgrupacionModelVO> agrupacionesNew : agrupacionMap.values()){
        	agrupacionesVO.add(agrupacionesNew);
        }
        
        return agrupacionesVO;
    	
    }

	private static void setAgrupadorMap(Map<Long,List<AgrupacionModelVO>> agrupacionMap, AgrupacionModelVO agrupacion){
	
		if(agrupacionMap.containsKey(agrupacion.getNivel())){
			agrupacionMap.get(agrupacion.getNivel()).add(agrupacion);
		}else{
			List<AgrupacionModelVO> agrupaciones = new ArrayList<AgrupacionModelVO>();
			agrupaciones.add(agrupacion);
			agrupacionMap.put(agrupacion.getNivel(), agrupaciones);
		}
	}
    
}
