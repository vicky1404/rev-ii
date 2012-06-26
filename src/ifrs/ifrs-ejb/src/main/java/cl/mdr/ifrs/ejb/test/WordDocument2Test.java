package cl.mdr.ifrs.ejb.test;

import org.docx4j.jaxb.Context;
import org.docx4j.model.table.TblFactory;
import org.docx4j.openpackaging.contenttype.ContentType;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.WordprocessingML.AlternativeFormatInputPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.CTAltChunk;
import org.docx4j.wml.Tbl;

import word.w2004.Header2004;
import word.w2004.elements.BreakLine;
import word.w2004.elements.Heading3;
import word.w2004.elements.Paragraph;
import word.w2004.elements.ParagraphPiece;
import word.w2004.elements.TableV2;
import word.w2004.elements.tableElements.TableCell;
import word.w2004.elements.tableElements.TableRow;


public class WordDocument2Test {
    public WordDocument2Test() {
        super();
    }

    public static void main(String[] args) throws Exception {

        System.out.println("Creating package..");
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();

        wordMLPackage.getMainDocumentPart().addStyledParagraphOfText("Title", "Hello world");
        wordMLPackage.getMainDocumentPart().addParagraphOfText("from docx4j!");

        // To get bold text, you must set the run's rPr@w:b,
        // so you can't use the createParagraphOfText convenience method
        //org.docx4j.wml.P p = wordMLPackage.getMainDocumentPart().createParagraphOfText("text");

        org.docx4j.wml.ObjectFactory factory = new org.docx4j.wml.ObjectFactory();
        org.docx4j.wml.P p = factory.createP();
        org.docx4j.wml.Text t = factory.createText();
        t.setValue("text");
        org.docx4j.wml.R run = factory.createR();
        run.getContent().add(t);

        p.getContent().add(run);


        org.docx4j.wml.RPr rpr = factory.createRPr();
        org.docx4j.wml.BooleanDefaultTrue b = new org.docx4j.wml.BooleanDefaultTrue();
        b.setVal(true);
        rpr.setB(b);

        run.setRPr(rpr);

        // Optionally, set pPr/rPr@w:b
        org.docx4j.wml.PPr ppr = factory.createPPr();
        p.setPPr(ppr);
        org.docx4j.wml.ParaRPr paraRpr = factory.createParaRPr();
        ppr.setRPr(paraRpr);
        rpr.setB(b);


        wordMLPackage.getMainDocumentPart().addObject(p);


        // Here is an easier way:
        String str =
            "<w:p xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" ><w:r><w:rPr><w:b /></w:rPr><w:t>Bold, just at w:r level</w:t></w:r></w:p>";

        wordMLPackage.getMainDocumentPart().addObject(org.docx4j.XmlUtils.unmarshalString(str));

        // Let's add a table
        int writableWidthTwips =
            wordMLPackage.getDocumentModel().getSections().get(0).getPageDimensions().getWritableWidthTwips();
        int cols = 3;
        int cellWidthTwips = new Double(Math.floor((writableWidthTwips / cols))).intValue();

        Tbl tbl = TblFactory.createTable(3, 8, cellWidthTwips);
        wordMLPackage.getMainDocumentPart().addObject(tbl);

        System.out.println(new BreakLine(2).create().getContent());

        //wordMLPackage.getMainDocumentPart().addObject(org.docx4j.XmlUtils.unmarshalString(new BreakLine(2).create().getContent()));
        // Add an altChunk
        // .. the part
        String html = "<html><head><title>Import me</title></head><body><p>Hello World!</p></body></html>";
        AlternativeFormatInputPart afiPart = new AlternativeFormatInputPart(new PartName("/hw.html"));
        afiPart.setBinaryData(html_dummy.getBytes());
        afiPart.setContentType(new ContentType("text/html"));
        Relationship altChunkRel = wordMLPackage.getMainDocumentPart().addTargetPart(afiPart);
        // .. the bit in document body
        CTAltChunk ac = Context.getWmlObjectFactory().createCTAltChunk();
        ac.setId(altChunkRel.getId());
        wordMLPackage.getMainDocumentPart().addObject(ac);
        // .. content type
        wordMLPackage.getContentTypeManager().addDefaultContentType("html", "text/html");


        wordMLPackage.getMainDocumentPart().addObject(org.docx4j.XmlUtils.unmarshalString(Heading3.with("Titulo 3").create().getContent()));


        TableV2 tbl1 = new TableV2();

        tbl1.addRow(TableRow.with("Table Header in all Pages", "Usefull for reports").withStyle().bgColor("00FFFF").create().withStyle().repeatTableHeaderOnEveryPage().create());
        tbl1.addRow(TableRow.with("Simple String cell", "Another simple String cell"));
        tbl1.addRow(TableRow.with(TableCell.with(Paragraph.with("TableCell- Style to the whole cell, Par").create()),
                                  "Simple String").withStyle().bold().create());
        tbl1.addRow(TableRow.with("Style to the whole cell, Str", "String").withStyle().bold().create());
        tbl1.addRow(TableRow.with(TableCell.with(Paragraph.with("TableRowV2 with merge").create()).withStyle().gridSpan(2).create()).withStyle().bold().create());
        tbl1.addRow(TableRow.with(TableCell.with(Paragraph.withPieces(ParagraphPiece.with("Paragraph with Style inside TableCell").withStyle().bold().fontSize("20").create()).create()).withStyle().bgColor("00FFFF").create(),
                                  "String"));

        for (int i = 0; i < 8000; i++) {
            tbl1.addRow(TableRow.with("111 ", "222"));
        }

        tbl1.addRow(TableRow.with("LAST", "LAST"));

        wordMLPackage.getMainDocumentPart().addObject(org.docx4j.XmlUtils.unmarshalString(tbl1.getContent()));

        Header2004 hd = new Header2004();
        hd.setHideHeaderAndFooterFirstPage(true);
        hd.addEle(Paragraph.with("Cabecera del Documento").create());
        wordMLPackage.getMainDocumentPart().addObject(org.docx4j.XmlUtils.unmarshalString(hd.getContent()));

        wordMLPackage.save(new java.io.File("c:/DOCUMENT_TEST/doc1.docx"));                
        System.out.println("Done.");
    }
    
        public static String html_dummy =
        "<html " + "xmlns:o='urn:schemas-microsoft-com:office:office' " + "xmlns:w='urn:schemas-microsoft-com:office:word'" +
        "xmlns='http://www.w3.org/TR/REC-html40'>" + "<head><title>Time</title>" + "<!--[if gte mso 9]>" + "<xml>" +
        "<w:WordDocument>" + "<w:View>Print</w:View>" + "<w:Zoom>90</w:Zoom>" + "<w:DoNotOptimizeForBrowser/>" +
        "</w:WordDocument>" + "</xml>" + "<![endif]-->" + "<style>" + "<!-- /* Style Definitions */" +
        "@page Section1" + "   {size:8.5in 11.0in; " + "   margin:1.0in 1.25in 1.0in 1.25in ; " +
        "   mso-header-margin:.5in; " + "   mso-footer-margin:.5in; mso-paper-source:0;}" + " div.Section1" +
        "   {page:Section1;}" + "-->" + "</style></head>" + "<body lang=EN-US style='tab-interval:.5in'>" +
        "<table style=\"WIDTH: 100%; BORDER-COLLAPSE: collapse\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"496\">\n" +
        "<colgroup>\n" +
        "<col style=\"WIDTH: 110pt; mso-width-source: userset; mso-width-alt: 5339\" width=\"146\"/>\n" +
        "<col style=\"WIDTH: 38pt; mso-width-source: userset; mso-width-alt: 1828\" span=\"7\" width=\"50\"/>\n" +
        "<tbody>\n" +
        "<tr style=\"HEIGHT: 15.75pt\" height=\"21\">\n" +
        "<td style=\"BORDER-BOTTOM: black 1pt solid; BORDER-LEFT: windowtext 1pt solid; BACKGROUND-COLOR: transparent; WIDTH: 110pt; HEIGHT: 31.5pt; BORDER-TOP: windowtext 1pt solid; BORDER-RIGHT: windowtext 1pt solid\" class=\"xl98\" height=\"42\" rowspan=\"2\" width=\"146\"><strong><font face=\"Calibri\">EQUIPO DE TRABAJO GUATACA</font></strong></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: transparent; WIDTH: 114pt; BORDER-TOP: windowtext 1pt solid; BORDER-RIGHT: black 1pt solid\" class=\"xl92\" width=\"150\" colspan=\"3\"><strong><font face=\"Calibri\">Diciembre 2011</font></strong></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: transparent; WIDTH: 152pt; BORDER-TOP: windowtext 1pt solid; BORDER-RIGHT: black 1pt solid\" class=\"xl95\" width=\"200\" colspan=\"4\"><strong><font face=\"Calibri\">Enero 2012</font></strong></td></tr>\n" +
        "<tr style=\"HEIGHT: 15.75pt\" height=\"21\">\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: transparent; HEIGHT: 15.75pt; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl72\" height=\"21\"><strong><font face=\"Calibri\">16</font></strong></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: #ffff99; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl79\"><strong><font face=\"Calibri\">23</font></strong></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: #ffff99; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 1pt solid\" class=\"xl80\"><strong><font face=\"Calibri\">30</font></strong></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: transparent; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl72\"><strong><font face=\"Calibri\">06</font></strong></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: transparent; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl73\"><strong><font face=\"Calibri\">13</font></strong></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: transparent; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl73\"><strong><font face=\"Calibri\">20</font></strong></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: transparent; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 1pt solid\" class=\"xl74\"><strong><font face=\"Calibri\">27</font></strong></td></tr>\n" +
        "<tr style=\"HEIGHT: 15pt\" height=\"20\">\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext 1pt solid; BACKGROUND-COLOR: transparent; HEIGHT: 15pt; BORDER-TOP: #f0f0f0; BORDER-RIGHT: windowtext 1pt solid\" class=\"xl68\" height=\"20\"><font face=\"Calibri\">Rodrigo Reyes</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: transparent; BORDER-TOP: #f0f0f0; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl69\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: #ffff99; BORDER-TOP: #f0f0f0; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl81\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: #ffff99; BORDER-TOP: #f0f0f0; BORDER-RIGHT: windowtext 1pt solid\" class=\"xl82\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: #c4d79b; BORDER-TOP: #f0f0f0; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl78\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: transparent; BORDER-TOP: #f0f0f0; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl70\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: transparent; BORDER-TOP: #f0f0f0; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl70\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: transparent; BORDER-TOP: #f0f0f0; BORDER-RIGHT: windowtext 1pt solid\" class=\"xl71\"><font face=\"Calibri\">&nbsp;</font></td></tr>\n" +
        "<tr style=\"HEIGHT: 15pt\" height=\"20\">\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext 1pt solid; BACKGROUND-COLOR: transparent; HEIGHT: 15pt; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 1pt solid\" class=\"xl67\" height=\"20\"><font face=\"Calibri\">Rodrigo Diaz</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: #c4d79b; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl75\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: #ffff99; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl83\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: #ffff99; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 1pt solid\" class=\"xl84\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: transparent; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl65\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: transparent; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl64\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: transparent; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl64\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: transparent; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 1pt solid\" class=\"xl66\"><font face=\"Calibri\">&nbsp;</font></td></tr>\n" +
        "<tr style=\"HEIGHT: 15pt\" height=\"20\">\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext 1pt solid; BACKGROUND-COLOR: transparent; HEIGHT: 15pt; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 1pt solid\" class=\"xl67\" height=\"20\"><font face=\"Calibri\">Manuel Gutierrez</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: transparent; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl65\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: #ffff99; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl83\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: #ffff99; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 1pt solid\" class=\"xl84\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: transparent; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl65\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: #c4d79b; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl76\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: transparent; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl64\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: transparent; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 1pt solid\" class=\"xl66\"><font face=\"Calibri\">&nbsp;</font></td></tr>\n" +
        "<tr style=\"HEIGHT: 15pt\" height=\"20\">\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext 1pt solid; BACKGROUND-COLOR: transparent; HEIGHT: 15pt; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 1pt solid\" class=\"xl67\" height=\"20\"><font face=\"Calibri\">Mauricio Jeria</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: #f0f0f0; BORDER-LEFT: #f0f0f0; BACKGROUND-COLOR: transparent; BORDER-TOP: #f0f0f0; BORDER-RIGHT: #f0f0f0\"><font face=\"Calibri\"></font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext 0.5pt solid; BACKGROUND-COLOR: #ffff99; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl83\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: #ffff99; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 1pt solid\" class=\"xl84\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: transparent; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl65\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: transparent; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl64\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: transparent; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl64\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: #c4d79b; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 1pt solid\" class=\"xl77\"><font face=\"Calibri\">&nbsp;</font></td></tr>\n" +
        "<tr style=\"HEIGHT: 15pt\" height=\"20\">\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext 1pt solid; BACKGROUND-COLOR: transparent; HEIGHT: 15pt; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 1pt solid\" class=\"xl67\" height=\"20\"><font face=\"Calibri\">Julio Benavente</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: transparent; BORDER-TOP: windowtext 0.5pt solid; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl65\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: #ffff99; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl83\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: #ffff99; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 1pt solid\" class=\"xl84\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: transparent; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl65\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: transparent; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl64\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: #c4d79b; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl76\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: transparent; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 1pt solid\" class=\"xl66\"><font face=\"Calibri\">&nbsp;</font></td></tr>\n" +
        "<tr style=\"HEIGHT: 15pt\" height=\"20\">\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext 1pt solid; BACKGROUND-COLOR: #ffff99; HEIGHT: 15pt; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 1pt solid\" class=\"xl87\" height=\"20\"><font face=\"Calibri\">Carlos Reyes</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: #f0f0f0; BORDER-LEFT: #f0f0f0; BACKGROUND-COLOR: #ffff99; BORDER-TOP: #f0f0f0; BORDER-RIGHT: #f0f0f0\" class=\"xl88\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext 0.5pt solid; BACKGROUND-COLOR: #ffff99; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl83\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: #ffff99; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 1pt solid\" class=\"xl84\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: #ffff99; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl89\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: #ffff99; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl83\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: #ffff99; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl83\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 0.5pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: #ffff99; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 1pt solid\" class=\"xl84\"><font face=\"Calibri\">&nbsp;</font></td></tr>\n" +
        "<tr style=\"HEIGHT: 15.75pt\" height=\"21\">\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: windowtext 1pt solid; BACKGROUND-COLOR: #ffff99; HEIGHT: 15.75pt; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 1pt solid\" class=\"xl90\" height=\"21\"><font face=\"Calibri\">Mauricio Barra</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: #ffff99; BORDER-TOP: windowtext 0.5pt solid; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl91\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: #ffff99; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl85\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: #ffff99; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 1pt solid\" class=\"xl86\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: #ffff99; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl91\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: #ffff99; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl85\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: #ffff99; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 0.5pt solid\" class=\"xl85\"><font face=\"Calibri\">&nbsp;</font></td>\n" +
        "<td style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: windowtext; BACKGROUND-COLOR: #ffff99; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 1pt solid\" class=\"xl86\"><font face=\"Calibri\">&nbsp;</font></td></tr></tbody></colgroup></table></body></html>";
        
}
