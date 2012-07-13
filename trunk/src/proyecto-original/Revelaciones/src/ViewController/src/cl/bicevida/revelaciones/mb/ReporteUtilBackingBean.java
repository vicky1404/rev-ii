package cl.bicevida.revelaciones.mb;


import cl.bicevida.com.lowagie.text.Document;
import cl.bicevida.com.lowagie.text.FontFactory;
import cl.bicevida.com.lowagie.text.PageSize;
import cl.bicevida.com.lowagie.text.Paragraph;
import cl.bicevida.com.lowagie.text.pdf.PdfContentByte;
import cl.bicevida.com.lowagie.text.pdf.PdfCopyFields;
import cl.bicevida.com.lowagie.text.pdf.PdfImportedPage;
import cl.bicevida.com.lowagie.text.pdf.PdfReader;
import cl.bicevida.com.lowagie.text.pdf.PdfWriter;
import cl.bicevida.revelaciones.common.mb.SoporteBackingBean;
import cl.bicevida.revelaciones.common.util.PropertyManager;
import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.revelaciones.ejb.entity.Catalogo;
import cl.bicevida.revelaciones.ejb.entity.Estructura;
import cl.bicevida.revelaciones.ejb.entity.Grilla;
import cl.bicevida.revelaciones.ejb.entity.Html;
import cl.bicevida.revelaciones.ejb.entity.Texto;
import cl.bicevida.revelaciones.ejb.entity.Version;
import cl.bicevida.revelaciones.ejb.reporte.util.SoporteReporte;
import cl.bicevida.revelaciones.ejb.reporte.vo.PropiedadesReporteVO;
import cl.bicevida.revelaciones.ejb.reporte.vo.ReportePrincipalVO;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.io.SaveToZipFile;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;


public class ReporteUtilBackingBean extends SoporteBackingBean{

    public static final String BEAN_NAME = "reporteUtilBackingBean";

    @SuppressWarnings("compatibility:-1082356846306732394")
    private static final long serialVersionUID = 1L;
    private String sufijoNombreReporte = "_".concat(new SimpleDateFormat("dd-MM-yyyy_HH-mm").format(new Date()));

    //private ConfiguracionServiceRemote configuracionRemote;
    
    private Logger logger = Logger.getLogger(ReporteUtilBackingBean.class);
    
    
    public ReporteUtilBackingBean() {        
    }
    
    
    /*public void generarExcel(ActionEvent event){
        List<ReportePrincipalVO> reportes;
        List<Version> versionList = this.getVersionesSelected();        
        try {
            for(Version version : versionList){
                List<Estructura> estructuras = getFacade().getEstructuraService().getEstructuraByVersion(version);
                version.setEstructuraList(estructuras);
            }
            if(versionList.isEmpty() || versionList == null){
                agregarWarnMessage("Seleccione al menos un elemento del Catalogo para exportar a MS Excel");
                return;
            }
            reportes = getReporteUtilBackingBean().getGenerarListReporteVO(versionList);
            XSSFWorkbook wb = getFacade().getServiceReporte().createXLSX(reportes);
            //List<JasperPrint> jasperPrints = getReporteUtilBackingBean().getListJasperPrint(reportes);
            getReporteUtilBackingBean().setOuputStreamWorkbook(wb);
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            agregarErrorMessage("Se ha producido un error al exportar a formato MS Excel");
        }
    }*/
    
    private byte[] getBytesPdfHtml(byte[] html) throws Exception{
         byte[] bytePDF = getFacade().getServiceReporte().getBytesHtmlToPdf(html);
         return bytePDF;
    }
    
   public void setStreamConcatenarReportePDF(List<ReportePrincipalVO> reportesVO) throws Exception {

        HttpServletResponse response = getResponse();
        boolean esReporteValido = false;

        PdfCopyFields pdfCopy = null;

        if (!Util.esListaValida(reportesVO))
            return;

        int j = 1;

        for (ReportePrincipalVO reporteVO : reportesVO) {
            if (j == 1) {
                response.reset();
                pdfCopy = new PdfCopyFields(response.getOutputStream());
            }
            for (Estructura estructura : reporteVO.getVersion().getEstructuraList()) {

                if (Util.esListaValida(estructura.getGrillaList())) {

                    for (Grilla grilla : estructura.getGrillaList()) {
                        //addPdfCopyFromJasper(reporteVO, pdfCopy);
                    }

                }
                if (Util.esListaValida(estructura.getHtmlList())) {

                    for (Html html : estructura.getHtmlList()) {
                        addPdfCopyReaderFromHtml(html.getContenidoStr(), pdfCopy, reporteVO.getPropiedades());
                    }

                }
                if (Util.esListaValida(estructura.getTextoList())) {
                    for (Texto html : estructura.getTextoList()) {
                        //addPdfCopyFromJasper(reporteVO, pdfCopy);
                    }
                }
            }
            j++;
        }
        
        response.addHeader("content-disposition","attachment; filename=" + SoporteReporte.NOMBRE_REPORTE.concat(this.getSufijoNombreReporte()) + "." + SoporteReporte.TIPO_PDF);
        response.setContentType(SoporteReporte.CONTENT_TYPE_PDF);
        pdfCopy.close();
    }
     
    /*private void addPdfCopyFromJasper(ReportePrincipalVO reporteVO,PdfCopyFields pdfCopy) throws Exception{
        byte[] bytePDF = getBytesPdfJasper(reporteVO);
        if(bytePDF!=null){
            PdfReader pdfReader = new PdfReader(bytePDF);
            pdfCopy.addDocument(pdfReader);
        }
    }*/
    
    private void addPdfCopyReaderFromHtml(String html, PdfCopyFields pdfCopy, PropiedadesReporteVO propiedades) throws Exception{

                byte[] bytePDF = getBytesPdfHtml(html.getBytes());
                PdfReader pdfReader = new PdfReader(bytePDF);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                
                Document document = new Document(PageSize.A4,0,0,0,10);
                PdfWriter writer = PdfWriter.getInstance(document, outputStream);
                document.open();
                PdfContentByte cb = writer.getDirectContent();

                
                /*HeaderFooter footer = new HeaderFooter(
                                        new Phrase(propiedades.getSubTituloPrincipal().concat(FOOTER_PAGINA)), true);
                            footer.setBorder(Rectangle.NO_BORDER);   
                            footer.setAlignment(Element.ALIGN_CENTER);*/
                            
                int paginas = pdfReader.getNumberOfPages();
                
                for(int i=1; i<=paginas; i++){
                    
                    document.newPage();
                    if(i==1){
                        document.add(new Paragraph("       "+propiedades.getTituloPrincipal(),
                                     FontFactory.getFont(propiedades.getTipoLetraReporte(), 13))); 
                        document.add(new Paragraph(""));
                    }
                    /*document.setFooter(footer);*/
                    PdfImportedPage page = writer.getImportedPage(pdfReader, i);
                    cb.addTemplate(page, 0, 0);
                    
                }                
                document.close();
                
                pdfReader = new PdfReader(outputStream.toByteArray());

                pdfCopy.addDocument(pdfReader);
    }
    
    /**
     * @param versiones
     * @return 
     */
    public List<ReportePrincipalVO> getGenerarListReporteVO(final List<Version> versiones) throws Exception{
 
        if(versiones==null || versiones.size()==0)
            throw new RuntimeException("Error: la lista de reportes no puede ser null");
        
        List<ReportePrincipalVO> reportes = new ArrayList<ReportePrincipalVO>();
        ReportePrincipalVO reporte = null;
        for(Version version : versiones){
            reporte = new ReportePrincipalVO(version, getPropiedades(version.getCatalogo()));
            reportes.add(reporte);
        }
        
        return reportes;
    }
    
    /*public List<JasperPrint> getListJasperPrint(List<ReportePrincipalVO> reportesVO) throws Exception{
    
        List<JasperPrint> jasperPrints = new ArrayList<JasperPrint>();
        int contador = 0;
        for(ReportePrincipalVO reporteVO : reportesVO){
            JasperPrint  jasperPrint = getFacade().getServiceReporte().getJasperPrint(reporteVO);
            if(jasperPrint!=null)
                jasperPrints.add(jasperPrint);
        }
        
        if(jasperPrints.size()==0)
            throw new ReporteSinDatosException();
            
        return jasperPrints;
    }*/
    
    private PropiedadesReporteVO getPropiedades(final Catalogo catalogo){
        PropiedadesReporteVO propiedades = new PropiedadesReporteVO();
        /*try {
            propiedades = this.getConfiguracionRemote().getPropiedadesReporte();
        } catch (Exception e) {
            propiedades = new PropiedadesReporteVO();
        }*/
        propiedades.setNombreHoja(catalogo.getNombre());
        propiedades.setTituloPrincipal(catalogo.getTitulo());
        propiedades.setCatalogo(catalogo);
        //propiedades.setSubTituloPrincipal(catalogo.getNombre());
        
        return propiedades;        
    }
    

    private String getPattern(String tipoDato){

        if(tipoDato.equals(SoporteReporte.TIPO_DATE) || tipoDato.equals(SoporteReporte.TIPO_TIME_STAMP)){
            return PropertyManager.getInstance().getMessage("date_es_pattern");
        }else{
            return null;
        }
    }
    
    public void setOuputStreamJasper(List<JasperPrint> jaspers) throws Exception{
    
        HttpServletResponse response = getResponse();
                
        OutputStream ouputStream = response.getOutputStream();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JRXlsExporter exporter = new JRXlsExporter();        
        
        exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jaspers);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
        exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
        exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.FALSE);
        exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);
        /*
        exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);        
        exporter.setParameter(JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE, Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.SHEET_NAMES, sheetNames );
        */
        exporter.setParameter(JExcelApiExporterParameter.IS_DETECT_CELL_TYPE, Boolean.FALSE);
        exporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);    
       
        exporter.exportReport();
        
        response.reset();
        response.addHeader("content-disposition","attachment; filename=" + SoporteReporte.NOMBRE_REPORTE.concat(this.getSufijoNombreReporte()) + "." + SoporteReporte.TIPO_XLS);
        response.setContentType(SoporteReporte.CONTENT_TYPE_XLS);
        byteArrayOutputStream.writeTo(ouputStream);
        ouputStream.write(byteArrayOutputStream.toByteArray());
        ouputStream.flush();
        byteArrayOutputStream.close();

    }
    
    
    /**
     * @param List<ReportePrincipalVO>
     * Genera una descarga de PDF concatenando los reportes segun la lista de ReportePrincipalVO
     */
    /* public void setStreamConcatenarReportePDF(List<ReportePrincipalVO> reportesVO) throws Exception{
    
       HttpServletResponse response = getResponse();
       boolean esReporteValido = false;
       
       PdfCopyFields pdfCopy = null;
       
       if(Util.esListaValida(reportesVO)){

       int j=1;
                
       for(ReportePrincipalVO reporteVO : reportesVO){

           if(Util.esListaValida(reporteVO.getSubReportes())){
               esReporteValido = true;
           int i=1;                   
           for(SubReporteVO subReporteVO : reporteVO.getSubReportes()){
           
               if(j==1){
                   response.reset();
                   pdfCopy = new PdfCopyFields(response.getOutputStream());
               }
               PdfReader pdfReader = null;
               
                       if(subReporteVO.getEsFilaPrimero().equals(Boolean.FALSE)){
                           
                           addPdfCopyReaderFromHtml(subReporteVO.getTextosHtml() , pdfCopy, reporteVO.getPropiedades());
                           
                           if(i==reporteVO.getSubReportes().size()){
                               addPdfCopyFromJasper(reporteVO, pdfCopy);
                           }
                           
                       }else{

                           if(i==1){
                               addPdfCopyFromJasper(reporteVO, pdfCopy);
                           }
                           
                           addPdfCopyReaderFromHtml(subReporteVO.getTextosHtml() , pdfCopy, reporteVO.getPropiedades());
                       }
               i++;
               j++;
           }
           }
           
       }

       }
       if(!esReporteValido){
           throw new ReporteSinDatosException("El reporte no contiene información");
       }
       else{
           response.addHeader("content-disposition","attachment; filename=" + NOMBRE_REPORTE + "." + TIPO_PDF);
           response.setContentType(CONTENT_TYPE_PDF);
           pdfCopy.close();
       }
    }*/
    
    /*public void setOuputStreamWorkbook(Object wb) throws Exception{        
        String fileName = "";        
        HttpServletResponse response = getResponse();
        response.reset();        
        OutputStream ouputStream = response.getOutputStream();
        if(wb instanceof XSSFWorkbook){
            ((XSSFWorkbook)wb).write(ouputStream);
            response.setContentType(SoporteReporte.CONTENT_TYPE_XLS);
            fileName = "filename= "+SoporteReporte.NOMBRE_REPORTE.concat(this.getSufijoNombreReporte()).concat(".").concat(SoporteReporte.TIPO_XLSX);
        }else if(wb instanceof XWPFDocument){
            ((XWPFDocument)wb).write(ouputStream);
            fileName = "filename=reporteWord.doc";
        }        
        response.addHeader("content-disposition","attachment; "+fileName);
        ouputStream.flush();
        ouputStream.close();
        if (!getFacesContext().getResponseComplete())
        getFacesContext().responseComplete();
    }*/
    
    /**
     * Force the download of the generated Excel file
     * @param xSSFWorkbook
     * @throws Exception
     */
    public void setOuputStreamWorkBook(final XSSFWorkbook xSSFWorkbook) throws Exception {    
        super.getResponse().setContentType(SoporteReporte.CONTENT_TYPE_XLS);
        final String fileName = "filename= "+SoporteReporte.NOMBRE_REPORTE.concat(this.getSufijoNombreReporte()).concat(".").concat(SoporteReporte.TIPO_XLSX);
        super.getResponse().addHeader("content-disposition","attachment; "+fileName);
        xSSFWorkbook.write(super.getResponse().getOutputStream());              
        super.getResponse().getOutputStream().flush();
        super.getResponse().getOutputStream().close();
        if (!getFacesContext().getResponseComplete())
        getFacesContext().responseComplete();
    }
    
    /**
     * Force the download of the generated Excel file
     * @param xSSFWorkbook
     * @throws Exception
     */
    public void setOuputStreamWorkBook(final XSSFWorkbook xSSFWorkbook, final String fileNameExport) throws Exception {    
        super.getResponse().setContentType(SoporteReporte.CONTENT_TYPE_XLS);
        final String fileName = "filename= "+fileNameExport.concat(this.getSufijoNombreReporte()).concat(".").concat(SoporteReporte.TIPO_XLSX);
        super.getResponse().addHeader("content-disposition","attachment; "+fileName);
        xSSFWorkbook.write(super.getResponse().getOutputStream());              
        super.getResponse().getOutputStream().flush();
        super.getResponse().getOutputStream().close();
        if (!getFacesContext().getResponseComplete())
        getFacesContext().responseComplete();
    }

    /**
     * Force the download of the generated Word file
     * @param wordMLPackage
     * @throws Exception
     */
    public void setOutPutStreamDocx(final WordprocessingMLPackage wordMLPackage, String nombreReporte) throws Exception {    
        super.getResponse().setContentType(SoporteReporte.CONTENT_TYPE_DOCX);
        final String fileName = "filename= "+nombreReporte;
        super.getResponse().addHeader("content-disposition","attachment; "+fileName);
        SaveToZipFile saver = new SaveToZipFile(wordMLPackage);        
        saver.save(super.getResponse().getOutputStream());
        super.getResponse().getOutputStream().flush();
        super.getResponse().getOutputStream().close();
        if (!getFacesContext().getResponseComplete())
        getFacesContext().responseComplete();
    }

    public String guardarAction() {
        return null;
    }

    public String editarAction() {
        return null;
    }

    public String buscarAction() {
        return null;
    }


    public void setSufijoNombreReporte(String sufijoNombreReporte) {
        this.sufijoNombreReporte = sufijoNombreReporte;
    }

    public String getSufijoNombreReporte() {
        return sufijoNombreReporte;
    }
}
