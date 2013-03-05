package cl.bicevida.revelaciones.ejb.reporte.util;


import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.core.layout.LayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.Page;
import ar.com.fdvs.dj.domain.constants.Transparency;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;

import cl.bicevida.revelaciones.ejb.common.TipoDatoEnum;
import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.revelaciones.ejb.entity.AgrupacionColumna;
import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.Columna;
import cl.bicevida.revelaciones.ejb.reporte.vo.GenericColumnVO;
import cl.bicevida.revelaciones.ejb.reporte.vo.PropiedadesReporteVO;
import cl.bicevida.revelaciones.vo.AgrupacionModelVO;

import java.awt.Color;

import java.lang.reflect.Field;

import java.sql.Timestamp;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public abstract class SoporteReporte {

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
    
    public final static String FOOTER_PAGINA = " - Página ";
    
    public final static String LINK_NAME="(+)(-)";

    public SoporteReporte() {

    }

    public Map getParams() {
        return params;
    }
    
    public Style getOddRowBackgroundStyle(){
        Style oddRowStyle = new Style("OddRowStyle");
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);
        return oddRowStyle;
    }
    
    public Style getHeaderStyle(PropiedadesReporteVO propiedades){
        Style headerStyle = new Style("HeaderStyle");        
        headerStyle.setBorder(propiedades.getBorderHeader());
        headerStyle.setBorderColor(propiedades.getColorBorder());
        headerStyle.setBackgroundColor(propiedades.getColorHeader());        
        headerStyle.setTransparency(Transparency.OPAQUE);
        headerStyle.setBorderBottom(Border.PEN_2_POINT);
        headerStyle.setBorderBottom(propiedades.getBorderHeader());
        headerStyle.setHorizontalAlign(propiedades.getHorizontalAlignHeader());
        headerStyle.setVerticalAlign(propiedades.getVerticalAlingHeader());
        //headerStyle.setFont(new Font(propiedades.getTamanoLetraHeader(), propiedades.getTipoLetraReporte(), true));
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        return headerStyle;
    }
    
    
    public Style getCellStyle(final String tipoDato){
        Style cellStyle = new Style("BorderStyle");        
        cellStyle.setBorder(Border.THIN);
        cellStyle.setBorderColor(Color.BLACK);        
        return cellStyle;
    }
    
    public Style getCellStyleRight(PropiedadesReporteVO propiedades){
        Style cellStyle = new Style("BorderStyleRight");        
        cellStyle.setBorder(propiedades.getBorderCell());
        cellStyle.setBorderColor(propiedades.getColorBorder());
        cellStyle.setHorizontalAlign(HorizontalAlign.RIGHT);
        //cellStyle.setFont(new Font(propiedades.getTamanoLetraContenido(), propiedades.getTipoLetraReporte(), true));
        cellStyle.setFont(Font.ARIAL_SMALL);
        return cellStyle;
    }
    
    public Style getCellStyleLeft(PropiedadesReporteVO propiedades){
        Style cellStyle = new Style("BorderStyleLeft");        
        cellStyle.setBorder(propiedades.getBorderCell());
        cellStyle.setBorderColor(propiedades.getColorBorder());
        cellStyle.setHorizontalAlign(HorizontalAlign.LEFT);
        //cellStyle.setFont(new Font(propiedades.getTamanoLetraContenido(), propiedades.getTipoLetraReporte(), true));
        cellStyle.setFont(Font.ARIAL_SMALL);
        return cellStyle;
    }
    
    public Page getPageSizeAndOrientation(boolean portrait){
        int w = 1100;
        int h = 300;
        return new Page(h, w, portrait);
    }

    protected JasperPrint jp;
    protected JasperReport jr;
    protected Map params = new HashMap();
    protected DynamicReport dr;

    public abstract DynamicReport buildReport() throws Exception;

    /**
     * Metodo que recive una lista de columnas y retorna
     * una lista de GenericColumnVO que representa los registros
     * de la lista de columnas
     * @param columnas
     * @return
     */
    public List<GenericColumnVO> createGenericList(List<Columna> columnas) {    
        int contadorColumna = GenericColumnVO.INI_ATTR;
        int contadorMaxCelda = 0;
        List<GenericColumnVO> generics = new ArrayList<GenericColumnVO>();
        GenericColumnVO generic;            
        sort(columnas, on(Columna.class).getOrden());
        
        for(Columna columna : columnas) {
            String attr = formatSetMethod(GenericColumnVO.PREFIJO + contadorColumna);
            int contadorCelda = 0;                                    
            for (Celda celda : columna.getCeldaList()) {                
                if (contadorColumna == GenericColumnVO.INI_ATTR) {
                    generic = new GenericColumnVO();
                    try{
                        if(celda.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.TEXTO.getKey())){
                            MethodUtils.invokeMethod(generic, attr, celda.getValor());                            
                        }
                        else if(celda.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.ENTERO.getKey())){
                            MethodUtils.invokeMethod(generic, attr, celda.getValorLong());                            
                        }
                        else if(celda.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.DECIMAL.getKey())){
                            MethodUtils.invokeMethod(generic, attr, celda.getValorBigDecimal());                            
                        }
                        else if(celda.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.FECHA.getKey())){
                            MethodUtils.invokeMethod(generic, attr, celda.getValorDate());                            
                        }else{
                            MethodUtils.invokeMethod(generic, attr, celda.getValor());   
                        }
                    } catch (Exception e) {
                        try {
                            MethodUtils.invokeMethod(generic, attr, "");
                        } catch (Exception f) {
                            logger.error(f);
                        }
                    }
                    contadorMaxCelda = columna.getCeldaList().size();
                    generics.add(generic);
                } else {
                    if (contadorCelda < contadorMaxCelda) {
                        try{                                                        
                            if(celda.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.TEXTO.getKey())){
                                MethodUtils.invokeMethod(generics.get(contadorCelda), attr, celda.getValor());
                            }
                            else if(celda.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.ENTERO.getKey())){
                                MethodUtils.invokeMethod(generics.get(contadorCelda), attr, celda.getValorLong());
                            }
                            else if(celda.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.DECIMAL.getKey())){
                                MethodUtils.invokeMethod(generics.get(contadorCelda), attr, celda.getValorBigDecimal());
                            }
                            else if(celda.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.FECHA.getKey())){
                                MethodUtils.invokeMethod(generics.get(contadorCelda), attr, celda.getValorDate());
                            }else{
                                MethodUtils.invokeMethod(generics.get(contadorCelda), attr, celda.getValor());   
                            }                                                        
                        } catch (Exception e) {
                            try {
                                MethodUtils.invokeMethod(generics.get(contadorCelda), attr, "");
                            } catch (Exception f) {
                                logger.error(f);
                            }
                        }
                    }
                }
                contadorCelda++;

                if (contadorColumna == GenericColumnVO.MAX_ATTR)
                    break;
            }
            contadorColumna++;
        }
        return generics;
    }
    
    private String formatGetMethod(Field field){
        return "get".concat(field.getName().substring(0,1).toUpperCase()).concat(field.getName().substring(1));
    }
    
    private String formatSetMethod(String field){
        return "set".concat(field.substring(0,1).toUpperCase() + field.substring(1));
    }
    
    

    protected LayoutManager getLayoutManager() {
        return new ClassicLayoutManager();
    }

    public DynamicReport getDynamicReport() {
        return dr;
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
        font.setFontName(HSSFFont.FONT_ARIAL);        
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short)13);
        return font;
    }
    
    public static XSSFFont getFontSubTitle(XSSFWorkbook wb) {
        XSSFFont font = wb.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short)11);
        return font;
    }

    public static XSSFFont getFontColumnHeader(XSSFWorkbook wb) {
        XSSFFont font = wb.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short)11);
        return font;
    }

    public static XSSFFont getFontTotal(XSSFWorkbook wb) {
        XSSFFont font = wb.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short)10);
        return font;
    }

    public static XSSFFont getFontSubTotal(XSSFWorkbook wb) {
        XSSFFont font = wb.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short)10);
        return font;
    }

    public static XSSFFont getFontTitulo(XSSFWorkbook wb) {
        XSSFFont font = wb.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short)10);
        return font;
    }
    
    public static XSSFFont getFontNormal(XSSFWorkbook wb) {
        XSSFFont font = wb.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL);
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
        style.setFont(getFontNormal(wb));
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
        style.setFont(getFontNormal(wb));
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
        style.setFont(getFontNormal(wb));
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
        style.setDataFormat(createHelper.createDataFormat().getFormat(Util.DATE_PATTERN_DD_MM_YYYY));
        style.setFillForegroundColor(rowColor);
        style.setFont(getFontNormal(wb));
        //style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);  
        return style;
    }
    
    public static XSSFCellStyle getCellTituloStyle(XSSFWorkbook wb, XSSFColor rowColor){        
        CreationHelper createHelper = wb.getCreationHelper();
        XSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);        
        style.setFillForegroundColor(rowColor);
        //style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);  
        return style;
    }
    
    public static String getNombreReporteDocx(Date date){
        String sufijoNombreReporte = "_".concat(new SimpleDateFormat("dd-MM-yyyy_HH-mm").format(date));
        return SoporteReporte.NOMBRE_REPORTE.concat(sufijoNombreReporte).concat(".").concat(SoporteReporte.TIPO_DOCX);
    }
    
}
