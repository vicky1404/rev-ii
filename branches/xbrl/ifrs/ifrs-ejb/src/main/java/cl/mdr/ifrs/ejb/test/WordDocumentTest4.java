package cl.mdr.ifrs.ejb.test;

import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.poi.hdf.extractor.WordDocument;

import org.docx4j.XmlUtils;
import org.docx4j.jaxb.Context;
import org.docx4j.model.structure.PageDimensions;
import org.docx4j.model.structure.PageSizePaper;
import org.docx4j.openpackaging.contenttype.ContentType;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.WordprocessingML.AlternativeFormatInputPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.BooleanDefaultTrue;
import org.docx4j.wml.CTAltChunk;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.SectPr;

import word.w2004.elements.Heading3;
import word.w2004.elements.Paragraph;
import word.w2004.elements.ParagraphPiece;
import word.w2004.elements.TableV2;
import word.w2004.elements.tableElements.TableCell;
import word.w2004.elements.tableElements.TableRow;

public class WordDocumentTest4 {
    public WordDocumentTest4() {
        super();
    }

    public static void main(String[] args) throws InvalidFormatException, Docx4JException, JAXBException {
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
        
        //parte 1
        PageDimensions page1 = new PageDimensions();
        page1.setPgSize(PageSizePaper.A4, true);
        
        SectPr yourSectPr = Context.getWmlObjectFactory().createSectPr();   
        yourSectPr.setPgSz(page1.getPgSz());
        P newP = Context.getWmlObjectFactory().createP();
        PPr pPr = Context.getWmlObjectFactory().createPPr();
        /*BooleanDefaultTrue def = new BooleanDefaultTrue();
        def.setVal(true);       
        pPr.setAutoSpaceDE(def);
        pPr.setAutoSpaceDN(def);*/
        
        
        newP.setPPr(pPr);
        pPr.setSectPr(yourSectPr);     
        
        
        org.docx4j.wml.Text t =  Context.getWmlObjectFactory().createText();
        t.setValue("text!!!!!!!");
        org.docx4j.wml.R run =  Context.getWmlObjectFactory().createR();
        run.setParent(new org.docx4j.wml.R.LastRenderedPageBreak().getParent());
        run.getContent().add(t);
        
        String html = "<html><head><title>Import me</title></head><body><p>Hello World!</p>wenaaaaa</body></html>";
        AlternativeFormatInputPart afiPart = new AlternativeFormatInputPart(new PartName("/hw.html"));
        afiPart.setBinaryData(html.getBytes());
        afiPart.setContentType(new ContentType("text/html"));
        Relationship altChunkRel = wordMLPackage.getMainDocumentPart().addTargetPart(afiPart);
        // .. the bit in document body
        CTAltChunk ac = Context.getWmlObjectFactory().createCTAltChunk();
        ac.setId(altChunkRel.getId());
        run.getContent().add(ac);
        
        
        TableV2 tbl1 = new TableV2();

        tbl1.addRow(TableRow.with("Table Header in all Pages", "Usefull for reports").withStyle().bgColor("00FFFF").create().withStyle().repeatTableHeaderOnEveryPage().create());
        tbl1.addRow(TableRow.with("Simple String cell", "Another simple String cell"));
        tbl1.addRow(TableRow.with(TableCell.with(Paragraph.with("TableCell- Style to the whole cell, Par").create()),
                                  "Simple String").withStyle().bold().create());
        tbl1.addRow(TableRow.with("Style to the whole cell, Str", "String").withStyle().bold().create());
        tbl1.addRow(TableRow.with(TableCell.with(Paragraph.with("TableRowV2 with merge").create()).withStyle().gridSpan(2).create()).withStyle().bold().create());
        tbl1.addRow(TableRow.with(TableCell.with(Paragraph.withPieces(ParagraphPiece.with("Paragraph with Style inside TableCell").withStyle().bold().fontSize("20").create()).create()).withStyle().bgColor("00FFFF").create(),
                                  "String"));

        for (int i = 0; i < 100; i++) {
            tbl1.addRow(TableRow.with("111 ", "222"));
        }

        tbl1.addRow(TableRow.with("LAST", "LAST"));        
        run.getContent().add(org.docx4j.XmlUtils.unmarshalString(tbl1.getContent()));
        
        newP.getContent().add(run);
        wordMLPackage.getMainDocumentPart().addObject(newP); 
        
        
        PageDimensions page2 = new PageDimensions();
        page2.setPgSize(PageSizePaper.A4, false);
        
        SectPr yourSectPr2 = Context.getWmlObjectFactory().createSectPr();   
        yourSectPr2.setPgSz(page2.getPgSz());
        P newP2 = Context.getWmlObjectFactory().createP();
        PPr pPr2 = Context.getWmlObjectFactory().createPPr();
        newP2.setPPr(pPr2);
        pPr2.setSectPr(yourSectPr2);        
        wordMLPackage.getMainDocumentPart().addObject(newP2);
        org.docx4j.wml.R run2 =  Context.getWmlObjectFactory().createR();
        
        AlternativeFormatInputPart afiPart2 = new AlternativeFormatInputPart(new PartName("/hw1.html"));
        //afiPart2.setBinaryData(WordDocument2Test.html_dummy.getBytes());
        afiPart2.setContentType(new ContentType("text/html"));
        Relationship altChunkRel2 = wordMLPackage.getMainDocumentPart().addTargetPart(afiPart2);
        // .. the bit in document body
        CTAltChunk ac2 = Context.getWmlObjectFactory().createCTAltChunk();
        ac2.setId(altChunkRel2.getId());
        run2.getContent().add(ac2);
        
        
        //newP2.getContent().add(run2);
        
        
        
        wordMLPackage.save(new java.io.File("c:/doc-test-2.docx"));
        // next, add newP to your document.xml

    }
}
