package cl.bicevida.revelaciones.ejb.reporte.util;


import ar.com.fdvs.dj.core.DJConstants;
import ar.com.fdvs.dj.core.DynamicJasperHelper;
import cl.bicevida.revelaciones.ejb.test.reporte.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.CustomExpression;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import cl.bicevida.revelaciones.ejb.test.reporte.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;

import cl.bicevida.revelaciones.ejb.common.TipoDatoEnum;
import cl.bicevida.revelaciones.ejb.cross.SortHelper;
import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.Columna;
import cl.bicevida.revelaciones.ejb.entity.Estructura;
import cl.bicevida.revelaciones.ejb.entity.Grilla;
import cl.bicevida.revelaciones.ejb.entity.Html;
import cl.bicevida.revelaciones.ejb.entity.Texto;
import cl.bicevida.revelaciones.ejb.entity.TipoDato;
import cl.bicevida.revelaciones.ejb.reporte.vo.GenericColumnVO;
import cl.bicevida.revelaciones.ejb.reporte.vo.PropiedadesReporteVO;
import cl.bicevida.revelaciones.ejb.reporte.vo.ReportePrincipalVO;

import java.math.BigDecimal;

import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.log4j.Logger;


public class ReporteDinamico extends SoporteReporte {
    
    private Logger logger = Logger.getLogger(ReporteDinamico.class);
    String atributo;
    String tipoDato;
    private DecimalFormat decimalFormat;

    public ReporteDinamico(){
        setDecimalFormat(Util.getDecimalFormat());
    }
    
    
    
    public JasperPrint getJasperPrint(ReportePrincipalVO reporte) throws Exception {
        
        DynamicReport dr;
        
        if(reporte==null)
          throw new RuntimeException("Error lista con reportes no puede ir vacia");
        
        Style titleStyle = new Style();
        //titleStyle.setFont(new Font(reporte.getPropiedades().getTamanoLetraTitulo(), reporte.getPropiedades().getTipoLetraReporte(), true));
        titleStyle.setFont(Font.ARIAL_BIG_BOLD);
        
        boolean esListaValida = false;
        
        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = new Integer(20);
        int contador = 0;
        drb.setTitleStyle(titleStyle);                  
        drb.setPageSizeAndOrientation(getPageSizeAndOrientation(true));
        
        if(reporte.getPropiedades().getTituloPrincipal()!=null && reporte.getPropiedades().getTituloPrincipal().trim().length()!=0);
          drb.setTitle(reporte.getPropiedades().getTituloPrincipal());
          
        drb.setDetailHeight(new Integer(15)).setLeftMargin(margin);
        drb.setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin);
        drb.setUseFullPageWidth(true);
        drb.setWhenNoDataAllSectionNoDetail();
        //drb.addAutoText(AutoText.AUTOTEXT_PAGE_X_OF_Y, AutoText.POSITION_FOOTER,AutoText.ALIGNMENT_CENTER);
        drb.setReportName(reporte.getPropiedades().getNombreHoja());
        drb.setIgnorePagination(true);
        
        for(Estructura estructura : reporte.getVersion().getEstructuraList()){
                    
            if(Util.esListaValida(estructura.getGrillaList())){
                
                if(!Util.esListaValida(estructura.getGrillaList()))
                    break;
                
                for(Grilla grilla : estructura.getGrillaList()){
                      esListaValida = true;
                      drb.addConcatenatedReport(crearSubreporteTableNew(grilla.getTitulo(), grilla.getColumnaList(),reporte.getPropiedades()), new ClassicLayoutManager(), "lista_tabla" + estructura.getIdEstructura(), DJConstants.DATA_SOURCE_ORIGIN_PARAMETER, DJConstants.DATA_SOURCE_TYPE_COLLECTION, false);
                      List<GenericColumnVO> generics = createGenericList(grilla.getColumnaList());
                      params.put("lista_tabla" + estructura.getIdEstructura(), generics);
                      contador++;
                }
                
            }
            if(Util.esListaValida(estructura.getHtmlList())){
                
                for(Html html : estructura.getHtmlList()){
                    esListaValida = true;
                    List<GenericColumnVO> generics = new ArrayList<GenericColumnVO>();
                    generics.add(new GenericColumnVO(html.getContenidoStr()));
                    List<Columna> columnas = new ArrayList<Columna>();
                    Columna columna = new Columna();
                    columna.setAncho(300L); //TODO dejar dinamico
                    columna.setTituloColumna(html.getTitulo());
                    columnas.add(columna);
                    drb.addConcatenatedReport(crearSubreporteTableNew(html.getTitulo(), columnas,reporte.getPropiedades()), new ClassicLayoutManager(), "lista_tabla" + estructura.getIdEstructura(), DJConstants.DATA_SOURCE_ORIGIN_PARAMETER, DJConstants.DATA_SOURCE_TYPE_COLLECTION, false);
                    params.put("lista_tabla" + estructura.getIdEstructura(), generics);
                    contador++;
                }
            }
            if(Util.esListaValida(estructura.getTextoList())){
                for(Texto texto : estructura.getTextoList()){
                    esListaValida = true;
                    List<GenericColumnVO> generics = new ArrayList<GenericColumnVO>();
                    generics.add(new GenericColumnVO(texto.getTexto()));
                    List<Columna> columnas = new ArrayList<Columna>();
                    Columna columna = new Columna();
                    columna.setAncho(Integer.valueOf(texto.getTexto()==null?150:texto.getTexto().length()).longValue());
                    columna.setTituloColumna("");
                    columnas.add(columna);
                    drb.addConcatenatedReport(crearSubreporteTableNew("", columnas,reporte.getPropiedades()), new ClassicLayoutManager(), "lista_tabla" + estructura.getIdEstructura(), DJConstants.DATA_SOURCE_ORIGIN_PARAMETER, DJConstants.DATA_SOURCE_TYPE_COLLECTION, false);
                    params.put("lista_tabla" + estructura.getIdEstructura(), generics);
                    contador++;
                }
            }
        }      
        
        if(!esListaValida)
           return null;
        
        dr = drb.build();
        jr = DynamicJasperHelper.generateJasperReport(dr, getLayoutManager(), params);
        jp = JasperFillManager.fillReport(jr, params);
        
        return jp;
    }
    
    
    private String getClassNameByTipoDato(Long tipoDato){
        
        if(TipoDato.TIPO_DATO_ENTERO.equals(tipoDato)){
            return TIPO_STRING;
        }
        if(TipoDato.TIPO_DATO_DECIMAL.equals(tipoDato)){
            return TIPO_DOUBLE;
        }
        if(TipoDato.TIPO_DATO_FECHA.equals(tipoDato)){
            return TIPO_DATE;
        }
        
        return TIPO_STRING;
    }
    
    
    private DynamicReport crearSubreporteTableNew(String titulo, List<Columna> columnas, PropiedadesReporteVO propiedades) throws Exception {
        
            AbstractColumn abstractColumn;
            FastReportBuilder reportBuilder = new FastReportBuilder();
            
            
        try{
            reportBuilder.setUseFullPageWidth(true);
            reportBuilder.setIgnorePagination(true);
            reportBuilder.setOddRowBackgroundStyle(getOddRowBackgroundStyle());
            String TIPO_DATO = TipoDatoEnum.TEXTO.getClase();
            
            int i = GenericColumnVO.INI_ATTR;
            SortHelper.sortColumnasByOrden(columnas);
            
            for(Columna columna : columnas){
                if(Util.esListaValida(columna.getCeldaList())){
                    for(Celda celda : columna.getCeldaList()){
                        if(celda.getIdFila().equals(1L)){
                            if(celda.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.TEXTO.getKey())){
                                TIPO_DATO = TipoDatoEnum.TEXTO.getClase();                           
                            }
                            else if(celda.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.ENTERO.getKey())){
                                TIPO_DATO = TipoDatoEnum.ENTERO.getClase();                          
                            }
                            else if(celda.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.DECIMAL.getKey())){
                                TIPO_DATO = TipoDatoEnum.DECIMAL.getClase();                             
                            }
                            else if(celda.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.FECHA.getKey())){
                                TIPO_DATO = TipoDatoEnum.FECHA.getClase();                           
                            }else{
                                TIPO_DATO = TipoDatoEnum.TEXTO.getClase();
                            }
                        }
                    }
                }
                
                reportBuilder.addField(GenericColumnVO.PREFIJO+i, TIPO_DATO);
                
                ColumnBuilder columnBuilder = ColumnBuilder.getNew();
                
                
                if(TIPO_DATO.equals(TipoDatoEnum.DECIMAL.getClase()) || TIPO_DATO.equals(TipoDatoEnum.ENTERO.getClase())){
                    columnBuilder.setCustomExpression(new GetCustomExpression(GenericColumnVO.PREFIJO+i, TIPO_DATO));
                }
                
                abstractColumn = columnBuilder.setHeaderStyle(getHeaderStyle(propiedades))
                                              .setStyle(TIPO_DATO.equals(TipoDatoEnum.TEXTO.getClase())?getCellStyleLeft(propiedades):getCellStyleRight(propiedades))
                                              .setTitle(columna.getTituloColumna())
                                              .setWidth(columna.getAncho().intValue())
                                              .setFixedWidth(false)                                              
                                              .setColumnProperty(GenericColumnVO.PREFIJO+i, TIPO_DATO)
                                              .build();
                                              
                reportBuilder.addColumn(abstractColumn);
                             
                    
                i++;
                
                if(i > GenericColumnVO.MAX_ATTR)
                    break;
            }            
            
            if(titulo!=null){
                reportBuilder.setTitle(titulo);
            }
            
                                  
            
        }catch(Exception e){
            e.printStackTrace();    
        } 
        
        return reportBuilder.build();
    }
    
    
    public void setDecimalFormat(DecimalFormat decimalFormat){
        this.decimalFormat = decimalFormat;
    }

    public DecimalFormat getDecimalFormat(){
        return decimalFormat;
    }


    private class GetCustomExpression implements CustomExpression{
        @SuppressWarnings("compatibility:6079580851734064440")
        private static final long serialVersionUID = -8423625761192720384L;
        private String atributo;
        private String tipoDato;


        public GetCustomExpression(final String atributo, final String tipoDato){
            this.atributo=atributo;
            this.tipoDato=tipoDato;
        }

        public Object evaluate(Map fields,Map variables,Map parameters){
            try{
                if(tipoDato.equals(TipoDatoEnum.DECIMAL.getClase())){
                    if(fields.get(atributo)==null)
                        return STRING_SIN_DATO;
                    BigDecimal valor = Util.getBigDecimal(fields.get(atributo) , new BigDecimal(0));
                    return getDecimalFormat().format(valor.doubleValue());                                 
                } else if(tipoDato.equals(TipoDatoEnum.ENTERO.getClase())){
                    if(fields.get(atributo)==null)
                        return STRING_SIN_DATO;
                    Long valor = Util.getLong(fields.get(atributo), new Long(0));             
                    return getDecimalFormat().format(valor.longValue());                   
                } else if(tipoDato.equals(TIPO_STRING)){                            
                    return fields.get(atributo);                   
                }else
                    return STRING_SIN_DATO;
            } catch(Exception e){
                logger.error(e.getCause(), e);
                return STRING_SIN_DATO;
            }            
        }

        public String getClassName(){
            return TIPO_STRING;
        }

    }

    public DynamicReport buildReport() {
        return null;
    }
}
