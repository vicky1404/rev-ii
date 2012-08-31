package cl.mdr.ifrs.ejb.service;


import static cl.mdr.ifrs.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.docx4j.XmlUtils;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.model.structure.MarginsWellKnown;
import org.docx4j.model.structure.PageDimensions;
import org.docx4j.model.structure.PageSizePaper;
import org.docx4j.openpackaging.contenttype.ContentType;
import org.docx4j.openpackaging.io.SaveToZipFile;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.Part;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.WordprocessingML.AlternativeFormatInputPart;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.FooterPart;
import org.docx4j.openpackaging.parts.WordprocessingML.HeaderPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.CTAltChunk;
import org.docx4j.wml.FooterReference;
import org.docx4j.wml.Ftr;
import org.docx4j.wml.Hdr;
import org.docx4j.wml.HdrFtrRef;
import org.docx4j.wml.HeaderReference;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.R;
import org.docx4j.wml.RFonts;
import org.docx4j.wml.RPr;
import org.docx4j.wml.SectPr;
import org.docx4j.wml.Style;
import org.docx4j.wml.Styles;

import word.api.interfaces.IElement;
import word.w2004.elements.Heading2;
import word.w2004.elements.Paragraph;
import word.w2004.elements.ParagraphPiece;
import word.w2004.elements.TableV2;
import word.w2004.elements.tableElements.TableCell;
import word.w2004.elements.tableElements.TableRow;
import word.w2004.style.ParagraphStyle;
import cl.mdr.ifrs.ejb.common.TipoCeldaEnum;
import cl.mdr.ifrs.ejb.common.TipoDatoEnum;
import cl.mdr.ifrs.ejb.common.TipoImpresionEnum;
import cl.mdr.ifrs.ejb.cross.MD5CheckSum;
import cl.mdr.ifrs.ejb.cross.SortHelper;
import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.AgrupacionColumna;
import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.ifrs.ejb.entity.Columna;
import cl.mdr.ifrs.ejb.entity.Empresa;
import cl.mdr.ifrs.ejb.entity.Estructura;
import cl.mdr.ifrs.ejb.entity.Grilla;
import cl.mdr.ifrs.ejb.entity.HistorialReporte;
import cl.mdr.ifrs.ejb.entity.Html;
import cl.mdr.ifrs.ejb.entity.Periodo;
import cl.mdr.ifrs.ejb.entity.PeriodoEmpresa;
import cl.mdr.ifrs.ejb.entity.Texto;
import cl.mdr.ifrs.ejb.entity.Usuario;
import cl.mdr.ifrs.ejb.facade.local.FacadeServiceLocal;
import cl.mdr.ifrs.ejb.reporte.util.SoporteReporte;
import cl.mdr.ifrs.ejb.reporte.vo.ReportePrincipalVO;
import cl.mdr.ifrs.ejb.service.local.ReporteDocxServiceLocal;
import cl.mdr.ifrs.vo.AgrupacionModelVO;
import cl.mdr.ifrs.vo.FilaVO;


@Stateless
public class ReporteDocxServiceBean implements ReporteDocxServiceLocal {
    private final Logger logger = Logger.getLogger(ReporteDocxServiceBean.class);
    @SuppressWarnings("unused")
	private static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    
    @EJB 
    private FacadeServiceLocal facadeService;
    
    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;
    
    private ObjectFactory objectFactory;
    private byte[] headerImage;
    
    private static final String GENERAL_FONT_NAME = "Arial";
    private static final String HEADER_FONT_SIZE = "10";
    //private static final String HEADER_FONT_COLOR = "8CCB18";
    private static final String HEADER_FONT_COLOR = "99CC66";    
    private static final String TITULO_FONT_SIZE = "8";
    private static final String TEXTO_FONT_SIZE = "8";
    
    public ReporteDocxServiceBean() {
        super();
    }
    
    @PostConstruct
    void init(){
        objectFactory = new ObjectFactory();
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public WordprocessingMLPackage createDOCX(final List<ReportePrincipalVO> reportes, final byte[] headerImage, final Usuario usuario , final String ipUsuario, final String nombreArchivo, final PeriodoEmpresa periodoEmpresa) throws Exception {  
        PageDimensions pageDimensions;            
        this.setHeaderImage(headerImage);
        int totalReportes = reportes.size();
        int countReportes = 0;
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage(PageSizePaper.LEGAL, false);
        
        Relationship relationshipHeader = createHeaderPart(wordMLPackage);
        Relationship relationshipFooter = createFooterPart(wordMLPackage);
        
        //create the header of the document.
        this.createHeaderReference(wordMLPackage, relationshipHeader);
        //create the footer of the document.
        this.createFooterReference(wordMLPackage, relationshipFooter);
                
        for(ReportePrincipalVO reporte : reportes) { 
            countReportes++;
            
            //page and orientation dimensions
            pageDimensions = new PageDimensions();         
            pageDimensions.setMargins(MarginsWellKnown.NARROW);
            pageDimensions.setPgSize(PageSizePaper.LEGAL,
                                     Util.getLong(reporte.getPropiedades().getCatalogo().getImpresionHorizontal(), new Long(0)).equals(TipoImpresionEnum.LANDSCAPE.getKey()) ?
                                     Boolean.TRUE : Boolean.FALSE);


            //section definition
            SectPr pageSection = Context.getWmlObjectFactory().createSectPr();   
            pageSection.setPgSz(pageDimensions.getPgSz()); 
            pageSection.setPgMar(pageDimensions.getPgMar());
            
            P p = Context.getWmlObjectFactory().createP();
            PPr ppr = Context.getWmlObjectFactory().createPPr();
            p.setPPr(ppr);
            R run =  Context.getWmlObjectFactory().createR();            
                                            
            //titulo del cuadro
            if (reporte.getPropiedades().getTituloPrincipal() != null) {
                this.createTituloUno(run, reporte.getPropiedades().getTituloPrincipal());
                //run.getContent().add(XmlUtils.unmarshalString(Heading1.with(reporte.getPropiedades().getTituloPrincipal()).create().getContent()));                
            }
            
            for (Estructura estructura : reporte.getVersion().getEstructuraList()) {
                //html part
                if (estructura.getHtml()!=null) {
                    Html html = estructura.getHtml();
                        if(html.getTitulo() != null && !StringUtils.isEmpty(html.getTitulo())){
                            run.getContent().add(XmlUtils.unmarshalString(Heading2.with(html.getTitulo()).create().getContent())); 
                        }
                        this.createHtmlReference(run, wordMLPackage, html); 
                        this.createBreakLine(run);
                }
                //text part
                if (estructura.getTexto()!=null) {
                    Texto texto = estructura.getTexto();
                    if(texto.isNegrita()){
                        this.createBoldText(run, texto.getTexto() == null ? "" : texto.getTexto());                            
                    }else{
                        this.createNormalText(run, texto.getTexto() == null ? "" : texto.getTexto());
                        //run.getContent().add(XmlUtils.unmarshalString(ParagraphPiece.with(texto.getTexto()).withStyle().fontSize(TEXTO_FONT_SIZE).create().getContent()));
                    }
                    this.createBreakLine(run);
                }
                //grid part
                if (estructura.getGrilla() != null) {
                    
                    Grilla grilla = estructura.getGrilla();
                                        
                    if(grilla.getTitulo() != null && !StringUtils.isEmpty(grilla.getTitulo())){                            
                        //run.getContent().add(XmlUtils.unmarshalString(Heading2.with(grilla.getTitulo()).create().getContent()));
                        this.createTituloDos(run, grilla.getTitulo());
                    }                        
                    this.createTableWordReference(run, grilla);
                    this.createBreakLine(run);

                }
            }                    
            if(countReportes != totalReportes){
                //agrega un salto de pagina al cambiar de nota o cuadro    
                //run.getContent().add(XmlUtils.unmarshalString(PageBreak.create().getContent())); 
            } 
            
            p.getContent().add(run);
            wordMLPackage.getMainDocumentPart().addObject(p); 
            
            //setea la orientacion respectiva del cuadro
            P paragraph = Context.getWmlObjectFactory().createP();
            PPr paragraphProperties = Context.getWmlObjectFactory().createPPr();
            paragraph.setPPr(paragraphProperties);
            paragraphProperties.setSectPr(pageSection);  
            wordMLPackage.getMainDocumentPart().addObject(paragraph);
                        
        }
        this.setDocumentFont(wordMLPackage);
        
        //guardamos log de descarga del reporte        
        HistorialReporte historialReporte;
        SaveToZipFile saver = new SaveToZipFile(wordMLPackage);  
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        saver.save(byteArrayOutputStream);
        byte[] documentData = byteArrayOutputStream.toByteArray();
        InputStream inputStream = new ByteArrayInputStream(documentData);            
        
        historialReporte = new HistorialReporte();        
        historialReporte.setUsuario(usuario);
        historialReporte.setIpUsuario(ipUsuario);
        historialReporte.setCheckSumExportacion(MD5CheckSum.getMD5Checksum(inputStream));
        historialReporte.setFechaExportacion(new Date());
        historialReporte.setDocumento(documentData);        
        historialReporte.setComentario("REPORTE GENERADO CON "+reportes.size()+" CUADRO(S)");
        historialReporte.setNombreArchivo(nombreArchivo);
        historialReporte.setPeriodoEmpresa(periodoEmpresa);
        em.persist(historialReporte);     
        return wordMLPackage;                
    }
    
    private void setDocumentFont(WordprocessingMLPackage wordMLPackage) {
        Styles styles = wordMLPackage.getMainDocumentPart().getStyleDefinitionsPart().getJaxbElement();
        ObjectFactory factory = Context.getWmlObjectFactory();
        for (Style s : styles.getStyle()) {
            RPr rpr = s.getRPr();        
            if (rpr == null) {
                rpr = factory.createRPr();
                s.setRPr(rpr);
            }
            RFonts rf = rpr.getRFonts();
            if (rf == null) {
                rf = factory.createRFonts();
                rpr.setRFonts(rf);
            }
            // This is where you set your font name.
            rf.setAscii(GENERAL_FONT_NAME);
        }
    }
        
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    private void createTableWordReference(R run, Grilla grilla) throws Exception {
        TableV2 table = null;
        table = new TableV2();
        //agrupacion columnas nivel 2
        List<AgrupacionColumna> agrupacionesNivel2 = facadeService.getEstructuraService().findAgrupacionColumnaByGrillaNivel(grilla.getIdGrilla(), 2L);
        if(Util.esListaValida(agrupacionesNivel2)){
            List<AgrupacionModelVO> agrupacionesVO = SoporteReporte.createAgrupadorVO(agrupacionesNivel2);
            List<IElement> headerNivel2Cells = new ArrayList<IElement>();            
            int columna = 1;
            for(AgrupacionModelVO agrupacion : agrupacionesVO){
                columna++;
                int desde = agrupacion.getDesde().intValue();
                int hasta = agrupacion.getHasta().intValue();
                if(columna == (agrupacionesVO.size())){
                    if(grilla.getColumnaList().get(grilla.getColumnaList().size()-1).getTituloColumna().replaceAll(" ", "").equals(SoporteReporte.LINK_NAME)){
                        hasta=hasta-1;
                    }
                }               
                headerNivel2Cells.add(TableCell.with(Paragraph.withPieces(ParagraphPiece.with(agrupacion.getTitulo()).withStyle().bold().fontSize(HEADER_FONT_SIZE).create()).create()).withStyle().gridSpan(((hasta-desde)+1)).bgColor(HEADER_FONT_COLOR).create());
                
            }            
            table.addRow(TableRow.with(Boolean.TRUE, headerNivel2Cells.toArray()));        
        }
        //agrupacion columnas nivel 1
        List<AgrupacionColumna> agrupacionesNivel1 = facadeService.getEstructuraService().findAgrupacionColumnaByGrillaNivel(grilla.getIdGrilla(), 1L);
        if(Util.esListaValida(agrupacionesNivel1)){
            List<AgrupacionModelVO> agrupacionesVO = SoporteReporte.createAgrupadorVO(agrupacionesNivel1);                        
            List<IElement> headerNivel1Cells = new ArrayList<IElement>(); 
            int columna = 1;
            for(AgrupacionModelVO agrupacion : agrupacionesVO){
                columna++;
                int desde = agrupacion.getDesde().intValue();
                int hasta = agrupacion.getHasta().intValue();
                if(columna == (agrupacionesVO.size())){
                    if(grilla.getColumnaList().get(grilla.getColumnaList().size()-1).getTituloColumna().replaceAll(" ", "").equals(SoporteReporte.LINK_NAME)){
                        hasta=hasta-1;
                    }
                }
                headerNivel1Cells.add(TableCell.with(Paragraph.withPieces(ParagraphPiece.with(agrupacion.getTitulo()).withStyle().bold().fontSize(HEADER_FONT_SIZE).create()).create()).withStyle().gridSpan(((hasta-desde)+1)).bgColor(HEADER_FONT_COLOR).create());
                
            }            
            table.addRow(TableRow.with(Boolean.TRUE, headerNivel1Cells.toArray()));
        }
        
        //agrega los titulos de columna para la grilla
        List<IElement> headerCells = new ArrayList<IElement>();
        for(Columna columna : grilla.getColumnaList()){
            if( !columna.getTituloColumna().replaceAll(" ", "").equals(SoporteReporte.LINK_NAME) && !columna.getTituloColumna().toUpperCase().contains("LINK") ){
                if(StringUtils.isEmpty(columna.getTituloColumna())){
                    headerCells.add(TableCell.with(Paragraph.withPieces(ParagraphPiece.with(StringUtils.EMPTY).withStyle().bold().fontSize(HEADER_FONT_SIZE).create()).create()).withStyle().bgColor(HEADER_FONT_COLOR).create());
                }else{
                    headerCells.add(TableCell.with(Paragraph.withPieces(ParagraphPiece.with(columna.getTituloColumna()).withStyle().bold().fontSize(HEADER_FONT_SIZE).create()).create()).withStyle().bgColor(HEADER_FONT_COLOR).create());                    
                }
            }
        }  
        
        table.addRow(TableRow.with(Boolean.TRUE, headerCells.toArray()));
                        
        for(FilaVO fila : this.buildGrilla(grilla)){
            List<IElement> rowCells = new ArrayList<IElement>();
            for(Celda celda : fila.getCeldas()){
                if( !celda.getColumna().getTituloColumna().replaceAll(" ", "").equals(SoporteReporte.LINK_NAME) && !celda.getColumna().getTituloColumna().toUpperCase().contains("LINK") ){
                    if(StringUtils.isEmpty(celda.getValor())){
                        if( celda.getTipoCelda().getIdTipoCelda().equals(TipoCeldaEnum.NUMERO.getKey()) || celda.getTipoCelda().getIdTipoCelda().equals(TipoCeldaEnum.TOTAL.getKey()) || celda.getTipoCelda().getIdTipoCelda().equals( TipoCeldaEnum.SUBTOTAL.getKey()) ){
                            rowCells.add(TableCell.with(Paragraph.withPieces(ParagraphPiece.with(""+0).withStyle().fontSize(TEXTO_FONT_SIZE).create()).create().withStyle().align(ParagraphStyle.Align.RIGHT).create()).create()); 
                        }else{
                            rowCells.add(TableCell.with(Paragraph.withPieces(ParagraphPiece.with(StringUtils.EMPTY).create()).create()).create()); 
                        }
                                           
                    }else{
                        if( celda.getTipoCelda().getIdTipoCelda().equals(TipoCeldaEnum.TOTAL.getKey()) || celda.getTipoCelda().getIdTipoCelda().equals( TipoCeldaEnum.SUBTOTAL.getKey()) ){
                            if(celda.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.ENTERO.getKey())){ 
                                rowCells.add(TableCell.with(Paragraph.withPieces(ParagraphPiece.with(Util.getIntegerFormat().format(celda.getValorLong())).withStyle().bold().fontSize(TITULO_FONT_SIZE).create()).create().withStyle().align(ParagraphStyle.Align.RIGHT ).create()).create()); 
                            }
                            else if(celda.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.DECIMAL.getKey())){
                                rowCells.add(TableCell.with(Paragraph.withPieces(ParagraphPiece.with(Util.getDecimalFormat().format(celda.getValorBigDecimal())).withStyle().bold().fontSize(TITULO_FONT_SIZE).create()).create().withStyle().align(ParagraphStyle.Align.RIGHT).create()).create());
                            }
                        }
                        else if( celda.getTipoCelda().getIdTipoCelda().equals(TipoCeldaEnum.TEXTO.getKey()) || celda.getTipoCelda().getIdTipoCelda().equals( TipoCeldaEnum.TEXTO_EDITABLE.getKey()) ){
                            if(celda.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.TEXTO.getKey())){
                                rowCells.add(TableCell.with(Paragraph.withPieces(ParagraphPiece.with(Util.cleanForXml(celda.getValor())).withStyle().fontSize(TEXTO_FONT_SIZE).create()).create()).create());                        
                            }
                            else if(celda.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.FECHA.getKey())){
                                rowCells.add(TableCell.with(Paragraph.withPieces(ParagraphPiece.with( celda.getValorDate() == null ? "" : Util.getString(celda.getValorDate()) ).withStyle().fontSize(TEXTO_FONT_SIZE).create()).create()).create());
                            }
                        }
                        else if( celda.getTipoCelda().getIdTipoCelda().equals(TipoCeldaEnum.TITULO.getKey()) ){
                            rowCells.add(TableCell.with(Paragraph.withPieces(ParagraphPiece.with(Util.cleanForXml(celda.getValor())).withStyle().bold().fontSize(TITULO_FONT_SIZE).create()).create()).withStyle().create());
                        }
                        else if( celda.getTipoCelda().getIdTipoCelda().equals(TipoCeldaEnum.NUMERO.getKey()) ){
                            if(celda.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.ENTERO.getKey())){ 
                                rowCells.add(TableCell.with(Paragraph.withPieces(ParagraphPiece.with(Util.getIntegerFormat().format(celda.getValorLong())).withStyle().fontSize(TEXTO_FONT_SIZE).create()).create().withStyle().align(ParagraphStyle.Align.RIGHT).create()).create()); 
                            }
                            else if(celda.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.DECIMAL.getKey())){
                                rowCells.add(TableCell.with(Paragraph.withPieces(ParagraphPiece.with(Util.getDecimalFormat().format(celda.getValorBigDecimal())).withStyle().fontSize(TEXTO_FONT_SIZE).create()).create().withStyle().align(ParagraphStyle.Align.RIGHT).create()).create());
                            }
                        }                                 
                        //values.add(celda.getValor());
                    }
                }
            }//for
            table.addRow(TableRow.with(rowCells.toArray()));
        }
        logger.info("ID Grilla ----------->"+grilla.getIdGrilla());        
        run.getContent().add(XmlUtils.unmarshalString(table.getContent()));
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<FilaVO> buildGrilla(Grilla grilla) throws Exception{        
        List<Celda> celdas = new ArrayList<Celda>();
        celdas = facadeService.getEstructuraService().getCeldasFromGrilla(grilla);                
        final Set<Long> filas =  facadeService.getEstructuraService().getTotalFilasFromGrilla(celdas);        
        List<Celda> celdasByFila = new ArrayList<Celda>();
        List<FilaVO> filasVO = new ArrayList<FilaVO>();        
        FilaVO filaVO = null;
        for(Long fila : filas) {
            filaVO = new FilaVO();
            celdasByFila = new ArrayList<Celda>();
            filaVO.setNumeroFila(fila);
            for (Celda celda : celdas) {
                if (celda.getIdFila().equals(fila)) {
                    if(!celda.getTipoCelda().getIdTipoCelda().equals(TipoCeldaEnum.LINK)){
                        celdasByFila.add(celda);
                    }
                }
            }
            SortHelper.sortCeldasByNumeroColumna(celdasByFila);
            filaVO.setCeldas(celdasByFila);
            filasVO.add(filaVO);
        }
        SortHelper.sortFilasByNumeroFila(filasVO);
        return filasVO;
    }
    
    
    @SuppressWarnings("unused")
	private void addLandScapeOrientation(WordprocessingMLPackage wordMLPackage) throws Exception {
        String tag = "<w:pgSz w:w=\"15840\" w:h=\"12240\" w:orient=\"landscape\" />  ";
        wordMLPackage.getMainDocumentPart().addObject(XmlUtils.unmarshalString(tag));
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    private void createBreakLine(R run) throws Exception {
        String tag = "<w:p xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" ><w:r><w:rPr><w:b /></w:rPr><w:t> </w:t></w:r></w:p>";            
        run.getContent().add(XmlUtils.unmarshalString(tag));
    }
    /**
     * generate a paragraph of bold text 
     * @param wordMLPackage
     * @param text
     * @throws Exception
     */
     @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    private void createBoldText(R run, String text) throws Exception {        
        String tag = "<w:p xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" ><w:r><w:rPr><w:rFonts w:ascii=\"Arial\" w:hAnsi=\"Arial\" w:cs=\"Arial\"/><w:b /></w:rPr><w:t>"+text+"</w:t></w:r></w:p>";            
        run.getContent().add(XmlUtils.unmarshalString(tag));
    }
     
     /**
      * generate a paragraph of text 
      * @param wordMLPackage
      * @param text
      * @throws Exception
      */
     @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
     private void createNormalText(R run, String text) throws Exception {        
         String tag = "<w:p xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" ><w:r><w:rPr><w:rFonts w:ascii=\"Arial\" w:hAnsi=\"Arial\" w:cs=\"Arial\"/></w:rPr><w:t>"+text+"</w:t></w:r></w:p>";            
         run.getContent().add(XmlUtils.unmarshalString(tag));
     } 
    
    private void createTituloUno(R run, String text) throws Exception{
        StringBuffer tag = new StringBuffer();
        tag.append("<w:p xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:rsidR=\"0021554A\" w:rsidRPr=\"00D664FF\" w:rsidRDefault=\"006A2794\">\n");
        tag.append("<w:pPr>\n"); 
        tag.append("   <w:pStyle w:val=\"Heading1\"/>\n");
        tag.append("       <w:rPr>\n");
        tag.append("       <w:rFonts w:ascii=\"Arial\" w:hAnsi=\"Arial\" w:cs=\"Arial\"/>\n");
        tag.append("       <w:color w:val=\"auto\"/>\n");
        tag.append("       </w:rPr>\n");
        tag.append("       </w:pPr>\n"); 
        tag.append("       <w:bookmarkStart w:id=\"0\" w:name=\"_GoBack\"/>\n"); 
        tag.append("       <w:bookmarkEnd w:id=\"0\"/>\n");
        tag.append("       <w:r w:rsidRPr=\"00D664FF\">\n"); 
        tag.append("       <w:rPr>\n");
        tag.append("            <w:rFonts w:ascii=\"Arial\" w:hAnsi=\"Arial\" w:cs=\"Arial\"/>\n");
        tag.append("            <w:color w:val=\"auto\"/>\n");
        tag.append("       </w:rPr>\n");
        tag.append("       <w:t>").append(text).append("</w:t>\n");
        tag.append("    </w:r>");
        tag.append("</w:p>");
        run.getContent().add(XmlUtils.unmarshalString(tag.toString()));
    }
    
    private void createTituloDos(R run, String text) throws Exception{
        StringBuffer tag = new StringBuffer();
        tag.append("<w:p xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:rsidR=\"0021554A\" w:rsidRPr=\"00B60480\" w:rsidRDefault=\"006A2794\">\n");
        tag.append("    <w:pPr>\n");
        tag.append("        <w:pStyle w:val=\"Heading2\"/>\n");
        tag.append("           <w:rPr>\n");
        tag.append("           <w:rFonts w:ascii=\"Arial\" w:hAnsi=\"Arial\" w:cs=\"Arial\"/>\n");
        tag.append("           <w:b w:val=\"0\"/>\n");
        tag.append("           <w:color w:val=\"auto\"/>\n");
        tag.append("           <w:sz w:val=\"20\"/>\n");
        tag.append("        <w:szCs w:val=\"20\"/>\n");
        tag.append("    </w:rPr>\n");
        tag.append("    </w:pPr>\n");
        tag.append("    <w:r w:rsidRPr=\"00B60480\">\n");
        tag.append("         <w:rPr>\n");
        tag.append("             <w:rFonts w:ascii=\"Arial\" w:hAnsi=\"Arial\" w:cs=\"Arial\"/>\n");
        tag.append("             <w:b w:val=\"0\"/>\n");
        tag.append("             <w:color w:val=\"auto\"/>\n");
        tag.append("             <w:sz w:val=\"20\"/>\n");
        tag.append("             <w:szCs w:val=\"20\"/>\n");
        tag.append("          </w:rPr>\n");
        tag.append("        <w:t>").append(text).append("</w:t>\n");
        tag.append("    </w:r>\n");
        tag.append("</w:p>");
        run.getContent().add(XmlUtils.unmarshalString(tag.toString()));
    }

    /**
     * generate the html content for document
     * @param wordMLPackage
     * @param html
     * @throws Exception
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    private void createHtmlReference(R run, WordprocessingMLPackage wordMLPackage, Html html) throws Exception { 
        AlternativeFormatInputPart afiPart = null;
        Relationship altChunkRel = null;
        CTAltChunk ac = null;
        afiPart = new AlternativeFormatInputPart(new PartName("/hw-"+html.getIdHtml()+".html"));
        afiPart.setBinaryData(Util.getBytes(html) == null ? "".getBytes() : Util.getBytes(html));
        afiPart.setContentType(new ContentType("text/html"));
        altChunkRel = wordMLPackage.getMainDocumentPart().addTargetPart(afiPart);        
        ac = Context.getWmlObjectFactory().createCTAltChunk();
        ac.setId(altChunkRel.getId());
        run.getContent().add(ac);                        
        wordMLPackage.getContentTypeManager().addDefaultContentType("html", "text/html");          
    }
    
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    private void createHeaderReference(WordprocessingMLPackage wordprocessingMLPackage, Relationship relationship) throws Exception {
        SectPr sectPr = this.getObjectFactory().createSectPr();
        HeaderReference headerReference = this.getObjectFactory().createHeaderReference();
        headerReference.setId(relationship.getId());
        headerReference.setType(HdrFtrRef.DEFAULT);
        sectPr.getEGHdrFtrReferences().add(headerReference); // add header or
        // header references
        wordprocessingMLPackage.getMainDocumentPart().addObject(sectPr);        
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    private Relationship createHeaderPart(WordprocessingMLPackage wordMLPackage)throws Exception {                
        HeaderPart headerPart = new HeaderPart();        
        headerPart.setPackage(wordMLPackage);
        headerPart.setJaxbElement(this.getHdr(wordMLPackage, headerPart));
        return wordMLPackage.getMainDocumentPart().addTargetPart(headerPart);
    }
    
    @SuppressWarnings("deprecation")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    private Hdr getHdr(WordprocessingMLPackage wordMLPackage, Part sourcePart) throws Exception {
        Hdr hdr = this.getObjectFactory().createHdr();
        hdr.getEGBlockLevelElts().add(this.newImage(wordMLPackage, sourcePart, this.getBytesFormImage(), "filename", "alttext", 1, 2));
        return hdr;
    }
    
    
    //FOOTER
    public void createFooterReference(WordprocessingMLPackage wordprocessingMLPackage, Relationship relationship) throws Exception {
        SectPr sectPr = this.getObjectFactory().createSectPr();
        FooterReference footerReference = this.getObjectFactory().createFooterReference();
        footerReference.setId(relationship.getId());
        footerReference.setType(HdrFtrRef.DEFAULT);
        sectPr.getEGHdrFtrReferences().add(footerReference); // add header or
        // footer references
        wordprocessingMLPackage.getMainDocumentPart().addObject(sectPr);             
    }
    
    public Relationship createFooterPart(WordprocessingMLPackage wordMLPackage) throws Exception {
        FooterPart footerPart = new FooterPart();        
        footerPart.setPackage(wordMLPackage);
        footerPart.setJaxbElement(this.getFtr(wordMLPackage, footerPart));
        return wordMLPackage.getMainDocumentPart().addTargetPart(footerPart);
    }
    
    @SuppressWarnings("deprecation")
	public Ftr getFtr(WordprocessingMLPackage wordMLPackage, Part sourcePart) throws Exception {
        Ftr ftr = this.getObjectFactory().createFtr();
        ftr.getEGBlockLevelElts().add(this.newImage(wordMLPackage, sourcePart, this.getBytesFormImage(), "filename", "alttext", 1, 2));
        return ftr;
    }
    
    
    
    @Deprecated
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    private byte[] getBytesFormImage() throws Exception {
        /*File file = new File("C:\\PROYECTOS\\REVELACIONES\\Revelaciones\\src\\ViewController\\public_html\\images\\logo\\logo-bice.jpg");        
        java.io.InputStream is = new FileInputStream(file);        
        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            System.out.println("File too large!!");
        }
        byte[] bytes = new byte[(int)length];
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        if (offset < bytes.length) {
            System.out.println("Could not completely read file " + file.getName());
        }
        is.close();
        */
        return this.getHeaderImage();
    }
    
    @SuppressWarnings("deprecation")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    private org.docx4j.wml.P newImage(WordprocessingMLPackage wordMLPackage, Part sourcePart, byte[] bytes,
                                            String filenameHint, String altText, int id1, int id2) throws Exception {

        BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, sourcePart, bytes);

        Inline inline = imagePart.createImageInline(filenameHint, altText, id1, id2);        
        org.docx4j.wml.ObjectFactory factory = new org.docx4j.wml.ObjectFactory();
        org.docx4j.wml.P p = factory.createP();
        org.docx4j.wml.R run = factory.createR();
        p.getContent().add(run);
        org.docx4j.wml.Drawing drawing = factory.createDrawing();
        run.getContent().add(drawing);
        drawing.getAnchorOrInline().add(inline);
        return p;
    }
    
    
    
    
    
    
    
    public org.docx4j.wml.P createPageNumber(String text, WordprocessingMLPackage wordMLPackage, Part sourcePart) throws Exception {
        org.docx4j.wml.P p = this.getObjectFactory().createP();
        org.docx4j.wml.R run = this.getObjectFactory().createR();
        String tag = "<w:p xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" ><w:r><w:rPr><w:rFonts w:ascii=\"Arial\" w:hAnsi=\"Arial\" w:cs=\"Arial\"/><w:b /></w:rPr><w:t>"+text+"</w:t></w:r></w:p>";            
        run.getContent().add(XmlUtils.unmarshalString(tag));
        p.getContent().add(run);
        return p;
    }
    
    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<HistorialReporte> findHistorialReporteByPeriodo(final Periodo periodo, final Empresa empresa) throws Exception{
        return em.createNamedQuery(HistorialReporte.FIND_ALL_BY_PERIODO).
        		  setParameter("idPeriodo", periodo.getIdPeriodo()).
        		  setParameter("rutEmpresa", empresa.getIdRut()).
        		  getResultList();
    }
    
    

    public void setObjectFactory(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    public ObjectFactory getObjectFactory() {
        return objectFactory;
    }


    public void setHeaderImage(byte[] headerImage) {
        this.headerImage = headerImage;
    }

    public byte[] getHeaderImage() {
        return headerImage;
    }
}
