package cl.mdr.ifrs.ejb.service;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;

import cl.mdr.ifrs.ejb.common.TipoCeldaEnum;
import cl.mdr.ifrs.ejb.common.TipoDatoEnum;
import cl.mdr.ifrs.ejb.cross.DocumentFactory;
import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.AgrupacionColumna;
import cl.mdr.ifrs.ejb.entity.CatalogoGrupo;
import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.ifrs.ejb.entity.Columna;
import cl.mdr.ifrs.ejb.entity.Estructura;
import cl.mdr.ifrs.ejb.entity.Grilla;
import cl.mdr.ifrs.ejb.entity.Version;
import cl.mdr.ifrs.ejb.reporte.util.SoporteReporte;
import cl.mdr.ifrs.ejb.reporte.vo.ReportePrincipalVO;
import cl.mdr.ifrs.ejb.service.local.EstructuraServiceLocal;
import cl.mdr.ifrs.ejb.service.local.ReporteServiceLocal;
import cl.mdr.ifrs.vo.AgrupacionModelVO;


@Stateless
public class ReporteServiceBean implements ReporteServiceLocal {
        
    @EJB EstructuraServiceLocal estructuraService;

    public ReporteServiceBean() {
        
    }
    

    @Override
    public XSSFWorkbook createXLSX(List<ReportePrincipalVO> reportes) throws Exception{

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFFont font;
        XSSFCellStyle style = wb.createCellStyle();
        
        DataFormat format = wb.createDataFormat();

        for (ReportePrincipalVO reporte : reportes) {

            XSSFSheet sheet =
                wb.createSheet(reporte.getPropiedades().getNombreHoja() == null ? "" : reporte.getPropiedades().getNombreHoja());

            int posRow = 2;
            int grillaMayor = 1;
            int tituloPrincipal = 1;
            //int contadorColumna = 1;
            
            if (reporte.getPropiedades().getTituloPrincipal() != null) {

                style = wb.createCellStyle();
                
                

                sheet.addMergedRegion(new CellRangeAddress(tituloPrincipal, //first row (0-based)
                            tituloPrincipal, //last row  (0-based)
                            1, //first column (0-based)
                            grillaMayor + 6) //last column  (0-based)
                );
                XSSFRow row = sheet.createRow(1);
                style.setFont(SoporteReporte.getFontTitle(wb));
                XSSFCell cell = row.createCell(1);
                cell.setCellValue(reporte.getPropiedades().getTituloPrincipal());
                cell.setCellStyle(style);
                
                
                

            }

            for (Estructura estructura : reporte.getVersion().getEstructuraList()) {
                
                posRow++;
                
                if (estructura.getGrilla()!=null)
                    continue;
                
                //text part
                if (estructura.getTexto()!=null) {
                    
                }
                
                Grilla grilla = estructura.getGrilla();
                       
                tituloPrincipal = posRow;
                
                if(grilla.getTitulo() != null && !StringUtils.isEmpty(grilla.getTitulo())){
                    
                    posRow++;
                    
                    sheet.addMergedRegion(new CellRangeAddress(posRow, posRow, 1, 7));
                    XSSFRow row2 = sheet.createRow(posRow);
                    style.setFont(SoporteReporte.getFontTitle(wb));
                    XSSFCell cell2 = row2.createCell(1);
                    cell2.setCellValue(grilla.getTitulo());
                    cell2.setCellStyle(style);
                    
                    posRow++;
                    
                }
    
                if (grillaMayor < grilla.getColumnaList().size()) {
                    grillaMayor = grilla.getColumnaList().size();
                }
                
                List<AgrupacionColumna> agrupacionesNivel2 = estructuraService.findAgrupacionColumnaByGrillaNivel(grilla.getIdGrilla(), 2L);
                
                if(Util.esListaValida(agrupacionesNivel2)){
                    List<AgrupacionModelVO> agrupacionesVO = SoporteReporte.createAgrupadorVO(agrupacionesNivel2);
                    posRow++;
                    XSSFRow row = sheet.createRow(posRow);
                    for(AgrupacionModelVO agrupacion : agrupacionesVO){
                        
                        
                        int hasta = agrupacion.getHasta().intValue();
                        
                        if(grilla.getColumnaList().get(grilla.getColumnaList().size()-1).getTituloColumna().replaceAll(" ", "").equals(SoporteReporte.LINK_NAME)){
                            hasta=hasta-1;
                        }
                        
                        XSSFCell cell = row.createCell(agrupacion.getDesde().intValue());
                        style = SoporteReporte.getCellWithBorder(wb);
                        style.setFillForegroundColor(new XSSFColor(new java.awt.Color(230, 233, 238)));
                        style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
                        style.setFont(SoporteReporte.getFontColumnHeader(wb));
                        
                        
                        sheet.addMergedRegion(new CellRangeAddress(posRow,
                                    posRow,
                                    agrupacion.getDesde().intValue(),
                                    hasta)
                        );
                        cell.setCellValue(agrupacion.getTitulo());
                        cell.setCellStyle(style);
                    }
                    posRow++;
                }
                
                List<AgrupacionColumna> agrupacionesNivel1 = estructuraService.findAgrupacionColumnaByGrillaNivel(grilla.getIdGrilla(), 1L);
                
                if(Util.esListaValida(agrupacionesNivel1)){
                    List<AgrupacionModelVO> agrupacionesVO = SoporteReporte.createAgrupadorVO(agrupacionesNivel1);
                    XSSFRow row = sheet.createRow(posRow);
                    int c=0;
                    for(AgrupacionModelVO agrupacion : agrupacionesVO){                                
                        c++;
                        int hasta = agrupacion.getHasta().intValue();
                        if(c==agrupacionesVO.size()){
                            if(grilla.getColumnaList().get(grilla.getColumnaList().size()-1).getTituloColumna().replaceAll(" ", "").equals(SoporteReporte.LINK_NAME)){
                                hasta=hasta-1;
                            }
                        }
                            System.out.println("desde "+agrupacion.getDesde().intValue()+" hasta "+hasta);
                        XSSFCell cell = row.createCell(agrupacion.getDesde().intValue());
                        style = SoporteReporte.getCellWithBorder(wb);
                        style.setFillForegroundColor(new XSSFColor(new java.awt.Color(230, 233, 238)));
                        style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
                        style.setFont(SoporteReporte.getFontColumnHeader(wb));
                        cell.setCellValue(agrupacion.getTitulo());
                        cell.setCellStyle(style);
                        
                        sheet.addMergedRegion(new CellRangeAddress(posRow,
                                    posRow,
                                    agrupacion.getDesde().intValue(),
                                    hasta)
                        );
                    }
                    posRow++;
                }
                
                XSSFRow row = sheet.createRow(posRow);
                XSSFCell cell = null;
                for (Columna columna : grilla.getColumnaList()){
                    if(columna.getTituloColumna().replaceAll(" ", "").equals(SoporteReporte.LINK_NAME)){
                        continue;
                    }
                    sheet.autoSizeColumn(columna.getIdColumna().intValue());
                    style = SoporteReporte.getCellWithBorder(wb);
                    style.setFillForegroundColor(new XSSFColor(new java.awt.Color(230, 233, 238)));
                    style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
                    style.setFont(SoporteReporte.getFontColumnHeader(wb));
                    cell = row.createCell(columna.getIdColumna().intValue());
                    cell.setCellValue(columna.getTituloColumna());
                    cell.setCellStyle(style);
                }
                int numRow = grilla.getColumnaList().get(0).getCeldaList().size();
                
                int i=0;
                
                for(int j=0; j<numRow; j++){
                    
                    posRow++;
                    row = sheet.createRow(posRow);
                    style = SoporteReporte.getCellWithBorder(wb);
                    XSSFColor rowColor = null;
                    if ((j % 2) == 0) {
                        rowColor = new XSSFColor(new java.awt.Color(246, 246, 246));
                        style.setFillForegroundColor(rowColor);
                        style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);                                    
                    }
                    for(Columna columna : grilla.getColumnaList()){
                        
                        if(columna.getTituloColumna().replaceAll(" ", "").equals(SoporteReporte.LINK_NAME)){
                            continue;
                        }
                        
                        Celda celda = columna.getCeldaList().get(i);
                        //System.out.println("row->" + posRow + " celda->"+columna.getIdColumna().intValue()+" valor->"+ celda.getValor());
                        cell = row.createCell(columna.getIdColumna().intValue());
                        
                        if(StringUtils.isEmpty(celda.getValor())){
                            if( celda.getTipoCelda().getIdTipoCelda().equals(TipoCeldaEnum.NUMERO.getKey()) ){                                                                                
                                
                                XSSFCellStyle style2 = wb.createCellStyle();
                                
                                if(rowColor!=null){
                                    style2.setFillForegroundColor(rowColor);
                                    style2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);      
                                }
                                //cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                                cell.setCellValue(new Double("5000"));
                                //cell.setCellStyle(style2);
                            }else{
                                
                                XSSFCellStyle style2 = wb.createCellStyle();
                                
                                if(rowColor!=null){
                                    style2.setFillForegroundColor(rowColor);
                                    style2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);      
                                }
                                cell.setCellType(Cell.CELL_TYPE_STRING);
                                cell.setCellValue(StringUtils.EMPTY);
                                cell.setCellStyle(style2);
                            }
                        }else{                                                                
                            if( celda.getTipoCelda().getIdTipoCelda().equals(TipoCeldaEnum.TOTAL.getKey()) && celda.getTipoCelda().getIdTipoCelda().equals( TipoCeldaEnum.SUBTOTAL.getKey()) ){
                                if ( celda.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.ENTERO.getKey()) ) {                                    
                                    //style.setDataFormat(format.getFormat("#,###,###,###"));
                                    cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                                    cell.setCellValue(celda.getValorLong());
                                    cell.setCellStyle(SoporteReporte.getCellIntegerStyle(wb, rowColor));
                                }                                    
                                else if (celda.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.DECIMAL.getKey()) ) {
                                    //style.setDataFormat(format.getFormat("#,###,###,###.####"));
                                    cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                                    cell.setCellValue(celda.getValorBigDecimal().doubleValue());
                                    cell.setCellStyle(SoporteReporte.getCellDecimalStyle(wb, rowColor));
                                }                                                                
                            }
                            
                            else if( celda.getTipoCelda().getIdTipoCelda().equals(TipoCeldaEnum.TEXTO.getKey()) || celda.getTipoCelda().getIdTipoCelda().equals( TipoCeldaEnum.TEXTO_EDITABLE.getKey()) ){
                                if(celda.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.TEXTO.getKey())){
                                    //style.setFont(SoporteReporte.getFontNormal(wb));
                                    cell.setCellType(Cell.CELL_TYPE_STRING);
                                    cell.setCellValue(celda.getValor());
                                    cell.setCellStyle(style);
                                }
                                else if(celda.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.FECHA.getKey())){
                                    //style.setFont(SoporteReporte.getFontNormal(wb));
                                    cell.setCellType(Cell.CELL_TYPE_STRING);
                                    cell.setCellValue(celda.getValorDate());
                                    cell.setCellStyle(SoporteReporte.getCellDateStyle(wb, rowColor));
                                }                                
                            }
                            
                            else if( celda.getTipoCelda().getIdTipoCelda().equals(TipoCeldaEnum.TITULO.getKey()) ){
                                //style.setFont(SoporteReporte.getFontTitulo(wb));
                                cell.setCellType(Cell.CELL_TYPE_STRING);
                                cell.setCellValue(celda.getValor());
                                cell.setCellStyle(style);
                            }
                            
                            else if( celda.getTipoCelda().getIdTipoCelda().equals(TipoCeldaEnum.NUMERO.getKey())){
                                if(celda.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.ENTERO.getKey())){ 
                                    //style.setDataFormat(format.getFormat("#,###,###,###"));
                                    //cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                                    cell.setCellValue(celda.getValorLong());
                                    cell.setCellStyle(SoporteReporte.getCellIntegerStyle(wb, rowColor));
                                }
                                else if(celda.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.DECIMAL.getKey())){
                                    //style.setDataFormat(format.getFormat("#,###,###,###.####"));
                                    //cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                                    cell.setCellValue(celda.getValorBigDecimal().doubleValue());
                                    cell.setCellStyle(SoporteReporte.getCellDecimalStyle(wb, rowColor));
                                }
                            }                                
                        }
    
                        //cell.setCellStyle(style);
                        
                    }
                    i++;
                }
                posRow++;
            }
        }

        return wb;
    }
    
    
    
    public XSSFWorkbook createInterfaceXBRL(List<ReportePrincipalVO> reportes) throws Exception{
        
        return null;
    }
    
    public XSSFWorkbook createReporteFlujoAprobacion(final List<Version> versionPeriodoList) throws Exception{
        XSSFWorkbook wb = new XSSFWorkbook();    
        XSSFCellStyle style = wb.createCellStyle();
        XSSFCellStyle styleDate = wb.createCellStyle();        
        CreationHelper createHelper = wb.getCreationHelper();
        XSSFSheet sheet = wb.createSheet("Reporte Flujo de Aprobación");
        sheet.setZoom(6,7);
        sheet.setFitToPage(true);
        sheet.createFreezePane(0, 1, 0, 1);
        int rowNum = 0;
        XSSFRow row;
        XSSFCell cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8, cell9;                
        this.createHeaderReporteFlujoAprobacion(wb, sheet, sheet.createRow(0));        
        for(final Version versionPeriodo : versionPeriodoList){
            rowNum++;
            row = sheet.createRow(rowNum);
            style = SoporteReporte.getCellWithBorder(wb);
            styleDate = SoporteReporte.getCellWithBorder(wb);
            if ((rowNum % 2) == 0) {
                style.setFillForegroundColor(new XSSFColor(new java.awt.Color(246, 246, 246)));
                style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);  
                
                styleDate.setFillForegroundColor(new XSSFColor(new java.awt.Color(246, 246, 246)));
                styleDate.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);  
            }
            //definicion de celdas
            cell1 = row.createCell(0);
            cell1.setCellStyle(style);
            cell2 = row.createCell(1);
            cell2.setCellStyle(style);
            cell3 = row.createCell(2);
            cell3.setCellStyle(style);
            cell4 = row.createCell(3);
            cell4.setCellStyle(style);
            cell5 = row.createCell(4);
            cell5.setCellStyle(style);
            cell6 = row.createCell(5);
            cell6.setCellStyle(style);
            cell7 = row.createCell(6);
            cell7.setCellStyle(style);
            cell8 = row.createCell(7);
            cell8.setCellStyle(style);
            cell9 = row.createCell(8);
            cell9.setCellStyle(style);
            
            //datos
            cell1.setCellValue(versionPeriodo.getEstado().getNombre());
            cell2.setCellValue(versionPeriodo.getCatalogo().getTipoCuadro().getNombre());
            cell3.setCellValue(versionPeriodo.getCatalogo().getTitulo());            
            
            for(CatalogoGrupo catalogoGrupo : versionPeriodo.getCatalogo().getCatalogoGrupoList()){
                //TODO se comento ya que no existe en modelo nuevo grupo oid
                /*if(catalogoGrupo.getGrupo().getGrupoOid().getIdGrupoOid().equals(Constantes.ROL_SUP)){
                    for(UsuarioGrupo usuarioGrupo : catalogoGrupo.getGrupo().getUsuarioGrupoList()){
                        cell4.setCellValue(MessageFormat.format("{0} : {1}", catalogoGrupo.getGrupo().getNombre(), usuarioGrupo.getUsuarioOid()));
                    }                    
                }*/
            }
            
            cell5.setCellValue(versionPeriodo.getVersion());
            cell6.setCellValue(versionPeriodo.getComentario() == null ? "" : versionPeriodo.getComentario());
            
            
            if(versionPeriodo.getFechaUltimoProceso() != null){               
                styleDate.setDataFormat(createHelper.createDataFormat().getFormat(Util.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS));
                cell7.setCellValue(versionPeriodo.getFechaUltimoProceso());
                cell7.setCellStyle(styleDate);
            }else{
                cell7.setCellValue(Util.STRING_VACIO);
            }
            
            cell8.setCellValue(MessageFormat.format("{0}-{1}", versionPeriodo.getPeriodo().getAnioPeriodo(), versionPeriodo.getPeriodo().getMesPeriodo()));
            cell9.setCellValue((versionPeriodo.getVigencia().equals(1L) ? "SI" : "NO"));
            
        }
        return wb;
    }
    
    private void createHeaderReporteFlujoAprobacion(XSSFWorkbook wb, XSSFSheet sheet, XSSFRow row){
        XSSFCell cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8, cell9;
        //ancho de columnas
        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 6000);
        sheet.setColumnWidth(2, 23000);
        sheet.setColumnWidth(3, 12000);
        sheet.setColumnWidth(5, 4000);
        sheet.setColumnWidth(6, 6000);
        
        XSSFCellStyle style = wb.createCellStyle();
        style = SoporteReporte.getCellWithBorder(wb);
        style.setFillForegroundColor(new XSSFColor(new java.awt.Color(230, 233, 238)));
        style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style.setFont(SoporteReporte.getFontColumnHeader(wb));
        cell1 = row.createCell(0);
        cell1.setCellStyle(style);
        cell2 = row.createCell(1);
        cell2.setCellStyle(style);
        cell3 = row.createCell(2);
        cell3.setCellStyle(style);
        cell4 = row.createCell(3);
        cell4.setCellStyle(style);
        cell5 = row.createCell(4);
        cell5.setCellStyle(style);
        cell6 = row.createCell(5);
        cell6.setCellStyle(style);
        cell7 = row.createCell(6);
        cell7.setCellStyle(style);
        cell8 = row.createCell(7);
        cell8.setCellStyle(style);
        cell9 = row.createCell(8);
        cell9.setCellStyle(style);
        //titulos
        cell1.setCellValue("ESTADO");
        cell2.setCellValue("TIPO CUADRO");
        cell3.setCellValue("TITULO CUADRO");
        cell4.setCellValue("AREA RESPONSABLE");
        cell5.setCellValue("VERSION");
        cell6.setCellValue("OBSERVACION");
        cell7.setCellValue("FECHA ULTIMO PROCESO");
        cell8.setCellValue("PERIODO");
        cell9.setCellValue("VIGENTE");
        
    }
    
    
}
