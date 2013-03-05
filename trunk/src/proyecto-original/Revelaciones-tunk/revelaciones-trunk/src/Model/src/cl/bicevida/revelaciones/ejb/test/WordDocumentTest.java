package cl.bicevida.revelaciones.ejb.test;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import word.api.interfaces.IDocument;

import word.w2004.Document2004;
import word.w2004.Footer2004;
import word.w2004.Header2004;
import word.w2004.elements.BreakLine;
import word.w2004.elements.Heading1;
import word.w2004.elements.Image;
import word.w2004.elements.ImageLocation;
import word.w2004.elements.Paragraph;
import word.w2004.elements.ParagraphPiece;
import word.w2004.elements.Table;
import word.w2004.elements.TableV2;
import word.w2004.elements.tableElements.TableCell;
import word.w2004.elements.tableElements.TableEle;
import word.w2004.elements.tableElements.TableRow;

public class WordDocumentTest {
    public WordDocumentTest() {
        super();
    }
    
    public static List<Employee> getResultList(){
            List<Employee> lst = new ArrayList<Employee>();
            
            lst.add(new Employee("Leonardo Correa", "40,000.00"));
            lst.add(new Employee("Romario da Silva", "500,000.00"));
            lst.add(new Employee("Ronaldinho", "850,000.00"));
            lst.add(new Employee("Kaka Ricardo de Oliveira", "1.240,000.00"));
            
            return lst;
    }

    public static void main(String[] args) {
        /*IDocument myDoc = new Document2004();
        
        Header2004 hd = new Header2004();            
        hd.addEle(Paragraph.with("BiceVida - Revelaciones"));
        myDoc.addEle(hd);
        
        Footer2004 footer = new Footer2004();
        footer.addEle(Paragraph.with("BiceVida - Revelaciones" + new Date()));
        footer.showPageNumber(true);
        myDoc.addEle(footer);
        
        myDoc.addEle(Heading1.with("Choro Mota del flow").create());
        myDoc.addEle(Paragraph.with("This document is an example of paragraph").create());
        myDoc.addEle(Paragraph.withPieces(ParagraphPiece.with("New ParagraphPiece styles have been implemented. Here they are:").withStyle().fontSize("10").create()));
        myDoc.getBody().addEle(new BreakLine(2).create().getContent());
        
        /*Table tbl = new Table();        
        tbl.addTableEle(TableEle.TH, "Nombre", "Salario");
        List<Employee> lst = getResultList(); //Filter could be applied here...
        for (Employee person : lst) {
                tbl.addTableEle(TableEle.TD, person.getName(), person.getSalary());
        }
        tbl.addTableEle(TableEle.TF, "Total", "1,100,000.00");

        myDoc.getBody().addEle(tbl);//add my table to the document
        
        myDoc.addEle(new BreakLine(2));
        
        TableV2 tb2 = new TableV2();
        
        tb2.addRow( TableRow.with( TableCell.with("Titulo 1").withStyle().gridSpan(2).create()).withStyle().bold().create());        
        tb2.addRow( TableRow.with("Table Header in all Pages", "Usefull for reports").withStyle().repeatTableHeaderOnEveryPage().create());
        
        tb2.addRow( TableRow.with("Simple String cell", "Another simple String cell") ); 
        tb2.addRow( TableRow.with( TableCell.with(Paragraph.with("TableCell- Style to the whole cell, Par").create()), "Simple String" ).withStyle().bold().create() );
    
        
        tb2.addRow( TableRow.with( TableCell.with(Paragraph.withPieces( ParagraphPiece.with("Paragraph with Style inside TableCell").withStyle().bold().fontSize("11").create() ).create()).withStyle().bgColor("00FFFF").create(), "String"  ));
        
        
        
        for (int i = 0; i < 10; i++) {
            tb2.addRow( TableRow.with("111", "fsdfsf") );            
        }
        
        tb2.addRow( TableRow.with("LAST", "LAST") );            
                
       
        myDoc.addEle(tb2.getContent());
        


        myDoc.addEle(new BreakLine(2)); //two break lines*/
    
        File fileObj = new File("c:/Java2word_allInOne.doc");

        PrintWriter writer = null;
        try {
            writer = new PrintWriter(fileObj);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //String myWord = myDoc.getContent();
        
        StringBuffer word = new StringBuffer();
        word.append(
        "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" + 
        "<?mso-application progid=\"Word.Document\"?>" +
        "<w:document xmlns:ns25=\"http://schemas.openxmlformats.org/drawingml/2006/compatibility\" xmlns:ns26=\"http://schemas.openxmlformats.org/drawingml/2006/lockedCanvas\" xmlns:ns24=\"http://schemas.openxmlformats.org/officeDocument/2006/bibliography\" xmlns:dsp=\"http://schemas.microsoft.com/office/drawing/2008/diagram\" xmlns:w10=\"urn:schemas-microsoft-com:office:word\" xmlns:odx=\"http://opendope.org/xpaths\" xmlns:odgm=\"http://opendope.org/SmartArt/DataHierarchy\" xmlns:dgm=\"http://schemas.openxmlformats.org/drawingml/2006/diagram\" xmlns:ns17=\"urn:schemas-microsoft-com:office:powerpoint\" xmlns:c=\"http://schemas.openxmlformats.org/drawingml/2006/chart\" xmlns:odi=\"http://opendope.org/components\" xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\" xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\" xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" xmlns:ns13=\"urn:schemas-microsoft-com:office:excel\" xmlns:ns6=\"http://schemas.openxmlformats.org/schemaLibrary/2006/main\" xmlns:odq=\"http://opendope.org/questions\" xmlns:ns11=\"http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing\" xmlns:ns8=\"http://schemas.openxmlformats.org/drawingml/2006/chartDrawing\" xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" xmlns:odc=\"http://opendope.org/conditions\">\n" + 
        "    <w:body>\n" + 
        "        <w:sectPr>\n" + 
        "            <w:headerReference w:type=\"default\" r:id=\"rId2\"/>\n" + 
        "        </w:sectPr>\n" + 
        "        <w:p>\n" + 
        "            <w:pPr>\n" + 
        "                <w:pStyle w:val=\"Heading1\"/>\n" + 
        "                <w:jc w:val=\"left\"/>\n" + 
        "            </w:pPr>\n" + 
        "            <w:r>\n" + 
        "                <w:t>Nota 7 - Efectivo y Efectivo Equivalente</w:t>\n" + 
        "            </w:r>\n" + 
        "        </w:p>\n" + 
        "        <w:tbl>\n" + 
        "            <w:tblPr>\n" + 
        "                <w:tblW w:type=\"auto\" w:w=\"0\"/>\n" + 
        "                <w:tblBorders>\n" + 
        "                    <w:top w:sz=\"4\" w:val=\"single\"/>\n" + 
        "                    <w:left w:sz=\"4\" w:val=\"single\"/>\n" + 
        "                    <w:bottom w:sz=\"4\" w:val=\"single\"/>\n" + 
        "                    <w:right w:sz=\"4\" w:val=\"single\"/>\n" + 
        "                    <w:insideH w:sz=\"4\" w:val=\"single\"/>\n" + 
        "                    <w:insideV w:sz=\"4\" w:val=\"single\"/>\n" + 
        "                </w:tblBorders>\n" + 
        "                <w:tblLook w:val=\"00BF\"/>\n" + 
        "            </w:tblPr>\n" + 
        "            <w:tblGrid>\n" + 
        "                <w:gridCol w:w=\"4258\"/>\n" + 
        "                <w:gridCol w:w=\"4258\"/>\n" + 
        "            </w:tblGrid>\n" + 
        "            <w:tr>\n" + 
        "                <w:tc>\n" + 
        "                    <w:tcPr>\n" + 
        "                        <w:shd w:fill=\"8CCB18\" w:color=\"auto\" w:val=\"clear\"/>\n" + 
        "                    </w:tcPr>\n" + 
        "                    <w:p>\n" + 
        "                        <w:pPr>\n" + 
        "                            <w:ind w:left=\"0\"/>\n" + 
        "                            <w:jc w:val=\"left\"/>\n" + 
        "                        </w:pPr>\n" + 
        "                        <w:r>\n" + 
        "                            <w:rPr>\n" + 
        "                                <w:b/>\n" + 
        "                                <w:sz w:val=\"26\"/>\n" + 
        "                            </w:rPr>\n" + 
        "                            <w:t>Efectivo y Efectivo Equivalente</w:t>\n" + 
        "                        </w:r>\n" + 
        "                    </w:p>\n" + 
        "                </w:tc>\n" + 
        "                <w:tc>\n" + 
        "                    <w:tcPr>\n" + 
        "                        <w:shd w:fill=\"8CCB18\" w:color=\"auto\" w:val=\"clear\"/>\n" + 
        "                    </w:tcPr>\n" + 
        "                    <w:p>\n" + 
        "                        <w:pPr>\n" + 
        "                            <w:ind w:left=\"0\"/>\n" + 
        "                            <w:jc w:val=\"left\"/>\n" + 
        "                        </w:pPr>\n" + 
        "                        <w:r>\n" + 
        "                            <w:rPr>\n" + 
        "                                <w:b/>\n" + 
        "                                <w:sz w:val=\"26\"/>\n" + 
        "                            </w:rPr>\n" + 
        "                            <w:t>CLP</w:t>\n" + 
        "                        </w:r>\n" + 
        "                    </w:p>\n" + 
        "                </w:tc>\n" + 
        "                <w:tc>\n" + 
        "                    <w:tcPr>\n" + 
        "                        <w:shd w:fill=\"8CCB18\" w:color=\"auto\" w:val=\"clear\"/>\n" + 
        "                    </w:tcPr>\n" + 
        "                    <w:p>\n" + 
        "                        <w:pPr>\n" + 
        "                            <w:ind w:left=\"0\"/>\n" + 
        "                            <w:jc w:val=\"left\"/>\n" + 
        "                        </w:pPr>\n" + 
        "                        <w:r>\n" + 
        "                            <w:rPr>\n" + 
        "                                <w:b/>\n" + 
        "                                <w:sz w:val=\"26\"/>\n" + 
        "                            </w:rPr>\n" + 
        "                            <w:t>USD</w:t>\n" + 
        "                        </w:r>\n" + 
        "                    </w:p>\n" + 
        "                </w:tc>\n" + 
        "                <w:tc>\n" + 
        "                    <w:tcPr>\n" + 
        "                        <w:shd w:fill=\"8CCB18\" w:color=\"auto\" w:val=\"clear\"/>\n" + 
        "                    </w:tcPr>\n" + 
        "                    <w:p>\n" + 
        "                        <w:pPr>\n" + 
        "                            <w:ind w:left=\"0\"/>\n" + 
        "                            <w:jc w:val=\"left\"/>\n" + 
        "                        </w:pPr>\n" + 
        "                        <w:r>\n" + 
        "                            <w:rPr>\n" + 
        "                                <w:b/>\n" + 
        "                                <w:sz w:val=\"26\"/>\n" + 
        "                            </w:rPr>\n" + 
        "                            <w:t>EUR</w:t>\n" + 
        "                        </w:r>\n" + 
        "                    </w:p>\n" + 
        "                </w:tc>\n" + 
        "                <w:tc>\n" + 
        "                    <w:tcPr>\n" + 
        "                        <w:shd w:fill=\"8CCB18\" w:color=\"auto\" w:val=\"clear\"/>\n" + 
        "                    </w:tcPr>\n" + 
        "                    <w:p>\n" + 
        "                        <w:pPr>\n" + 
        "                            <w:ind w:left=\"0\"/>\n" + 
        "                            <w:jc w:val=\"left\"/>\n" + 
        "                        </w:pPr>\n" + 
        "                        <w:r>\n" + 
        "                            <w:rPr>\n" + 
        "                                <w:b/>\n" + 
        "                                <w:sz w:val=\"26\"/>\n" + 
        "                            </w:rPr>\n" + 
        "                            <w:t>OTRA</w:t>\n" + 
        "                        </w:r>\n" + 
        "                    </w:p>\n" + 
        "                </w:tc>\n" + 
        "                <w:tc>\n" + 
        "                    <w:tcPr>\n" + 
        "                        <w:shd w:fill=\"8CCB18\" w:color=\"auto\" w:val=\"clear\"/>\n" + 
        "                    </w:tcPr>\n" + 
        "                    <w:p>\n" + 
        "                        <w:pPr>\n" + 
        "                            <w:ind w:left=\"0\"/>\n" + 
        "                            <w:jc w:val=\"left\"/>\n" + 
        "                        </w:pPr>\n" + 
        "                        <w:r>\n" + 
        "                            <w:rPr>\n" + 
        "                                <w:b/>\n" + 
        "                                <w:sz w:val=\"26\"/>\n" + 
        "                            </w:rPr>\n" + 
        "                            <w:t>TOTAL</w:t>\n" + 
        "                        </w:r>\n" + 
        "                    </w:p>\n" + 
        "                </w:tc>\n" + 
        "            </w:tr>\n" + 
        "            <w:tr>\n" + 
        "                <w:tc>\n" + 
        "                    <w:p>\n" + 
        "                        <w:pPr>\n" + 
        "                            <w:ind w:left=\"0\"/>\n" + 
        "                            <w:jc w:val=\"left\"/>\n" + 
        "                        </w:pPr>\n" + 
        "                        <w:r>\n" + 
        "                            <w:t>Efectivo en Caja</w:t>\n" + 
        "                        </w:r>\n" + 
        "                    </w:p>\n" + 
        "                </w:tc>\n" + 
        "                <w:tc>\n" + 
        "                    <w:p>\n" + 
        "                        <w:pPr>\n" + 
        "                            <w:ind w:left=\"0\"/>\n" + 
        "                            <w:jc w:val=\"right\"/>\n" + 
        "                        </w:pPr>\n" + 
        "                        <w:r>\n" + 
        "                            <w:t>88.888</w:t>\n" + 
        "                        </w:r>\n" + 
        "                    </w:p>\n" + 
        "                </w:tc>\n" + 
        "                <w:tc>\n" + 
        "                    <w:p>\n" + 
        "                        <w:pPr>\n" + 
        "                            <w:ind w:left=\"0\"/>\n" + 
        "                            <w:jc w:val=\"right\"/>\n" + 
        "                        </w:pPr>\n" + 
        "                        <w:r>\n" + 
        "                            <w:t>20.000</w:t>\n" + 
        "                        </w:r>\n" + 
        "                    </w:p>\n" + 
        "                </w:tc>\n" + 
        "                <w:tc>\n" + 
        "                    <w:p>\n" + 
        "                        <w:pPr>\n" + 
        "                            <w:ind w:left=\"0\"/>\n" + 
        "                            <w:jc w:val=\"right\"/>\n" + 
        "                        </w:pPr>\n" + 
        "                        <w:r>\n" + 
        "                            <w:t>500.000</w:t>\n" + 
        "                        </w:r>\n" + 
        "                    </w:p>\n" + 
        "                </w:tc>\n" + 
        "                <w:tc>\n" + 
        "                    <w:p>\n" + 
        "                        <w:pPr>\n" + 
        "                            <w:ind w:left=\"0\"/>\n" + 
        "                            <w:jc w:val=\"right\"/>\n" + 
        "                        </w:pPr>\n" + 
        "                        <w:r>\n" + 
        "                            <w:t>0</w:t>\n" + 
        "                        </w:r>\n" + 
        "                    </w:p>\n" + 
        "                </w:tc>\n" + 
        "                <w:tc>\n" + 
        "                    <w:p>\n" + 
        "                        <w:pPr>\n" + 
        "                            <w:ind w:left=\"0\"/>\n" + 
        "                            <w:jc w:val=\"right\"/>\n" + 
        "                        </w:pPr>\n" + 
        "                        <w:r>\n" + 
        "                            <w:rPr>\n" + 
        "                                <w:b/>\n" + 
        "                                <w:sz w:val=\"22\"/>\n" + 
        "                            </w:rPr>\n" + 
        "                            <w:t>608.888</w:t>\n" + 
        "                        </w:r>\n" + 
        "                    </w:p>\n" + 
        "                </w:tc>\n" + 
        "            </w:tr>\n" + 
        "            <w:tr>\n" + 
        "                <w:tc>\n" + 
        "                    <w:p>\n" + 
        "                        <w:pPr>\n" + 
        "                            <w:ind w:left=\"0\"/>\n" + 
        "                            <w:jc w:val=\"left\"/>\n" + 
        "                        </w:pPr>\n" + 
        "                        <w:r>\n" + 
        "                            <w:t>Bancos</w:t>\n" + 
        "                        </w:r>\n" + 
        "                    </w:p>\n" + 
        "                </w:tc>\n" + 
        "                <w:tc>\n" + 
        "                    <w:p>\n" + 
        "                        <w:pPr>\n" + 
        "                            <w:ind w:left=\"0\"/>\n" + 
        "                            <w:jc w:val=\"right\"/>\n" + 
        "                        </w:pPr>\n" + 
        "                        <w:r>\n" + 
        "                            <w:t>20.000</w:t>\n" + 
        "                        </w:r>\n" + 
        "                    </w:p>\n" + 
        "                </w:tc>\n" + 
        "                <w:tc>\n" + 
        "                    <w:p>\n" + 
        "                        <w:pPr>\n" + 
        "                            <w:ind w:left=\"0\"/>\n" + 
        "                            <w:jc w:val=\"right\"/>\n" + 
        "                        </w:pPr>\n" + 
        "                        <w:r>\n" + 
        "                            <w:t>8.520.000</w:t>\n" + 
        "                        </w:r>\n" + 
        "                    </w:p>\n" + 
        "                </w:tc>\n" + 
        "                <w:tc>\n" + 
        "                    <w:p>\n" + 
        "                        <w:pPr>\n" + 
        "                            <w:ind w:left=\"0\"/>\n" + 
        "                            <w:jc w:val=\"right\"/>\n" + 
        "                        </w:pPr>\n" + 
        "                        <w:r>\n" + 
        "                            <w:t>0</w:t>\n" + 
        "                        </w:r>\n" + 
        "                    </w:p>\n" + 
        "                </w:tc>\n" + 
        "                <w:tc>\n" + 
        "                    <w:p>\n" + 
        "                        <w:pPr>\n" + 
        "                            <w:ind w:left=\"0\"/>\n" + 
        "                            <w:jc w:val=\"right\"/>\n" + 
        "                        </w:pPr>\n" + 
        "                        <w:r>\n" + 
        "                            <w:t>10.000.000</w:t>\n" + 
        "                        </w:r>\n" + 
        "                    </w:p>\n" + 
        "                </w:tc>\n" + 
        "                <w:tc>\n" + 
        "                    <w:p>\n" + 
        "                        <w:pPr>\n" + 
        "                            <w:ind w:left=\"0\"/>\n" + 
        "                            <w:jc w:val=\"right\"/>\n" + 
        "                        </w:pPr>\n" + 
        "                        <w:r>\n" + 
        "                            <w:rPr>\n" + 
        "                                <w:b/>\n" + 
        "                                <w:sz w:val=\"22\"/>\n" + 
        "                            </w:rPr>\n" + 
        "                            <w:t>18.540.000</w:t>\n" + 
        "                        </w:r>\n" + 
        "                    </w:p>\n" + 
        "                </w:tc>\n" + 
        "            </w:tr>\n" + 
        "            <w:tr>\n" + 
        "                <w:tc>\n" + 
        "                    <w:p>\n" + 
        "                        <w:pPr>\n" + 
        "                            <w:ind w:left=\"0\"/>\n" + 
        "                            <w:jc w:val=\"left\"/>\n" + 
        "                        </w:pPr>\n" + 
        "                        <w:r>\n" + 
        "                            <w:t>Equivalente al Efectivo</w:t>\n" + 
        "                        </w:r>\n" + 
        "                    </w:p>\n" + 
        "                </w:tc>\n" + 
        "                <w:tc>\n" + 
        "                    <w:p>\n" + 
        "                        <w:pPr>\n" + 
        "                            <w:ind w:left=\"0\"/>\n" + 
        "                            <w:jc w:val=\"right\"/>\n" + 
        "                        </w:pPr>\n" + 
        "                        <w:r>\n" + 
        "                            <w:t>0</w:t>\n" + 
        "                        </w:r>\n" + 
        "                    </w:p>\n" + 
        "                </w:tc>\n" + 
        "                <w:tc>\n" + 
        "                    <w:p>\n" + 
        "                        <w:pPr>\n" + 
        "                            <w:ind w:left=\"0\"/>\n" + 
        "                            <w:jc w:val=\"right\"/>\n" + 
        "                        </w:pPr>\n" + 
        "                        <w:r>\n" + 
        "                            <w:t>0</w:t>\n" + 
        "                        </w:r>\n" + 
        "                    </w:p>\n" + 
        "                </w:tc>\n" + 
        "                <w:tc>\n" + 
        "                    <w:p>\n" + 
        "                        <w:pPr>\n" + 
        "                            <w:ind w:left=\"0\"/>\n" + 
        "                            <w:jc w:val=\"right\"/>\n" + 
        "                        </w:pPr>\n" + 
        "                        <w:r>\n" + 
        "                            <w:t>0</w:t>\n" + 
        "                        </w:r>\n" + 
        "                    </w:p>\n" + 
        "                </w:tc>\n" + 
        "                <w:tc>\n" + 
        "                    <w:p>\n" + 
        "                        <w:pPr>\n" + 
        "                            <w:ind w:left=\"0\"/>\n" + 
        "                            <w:jc w:val=\"right\"/>\n" + 
        "                        </w:pPr>\n" + 
        "                        <w:r>\n" + 
        "                            <w:t>0</w:t>\n" + 
        "                        </w:r>\n" + 
        "                    </w:p>\n" + 
        "                </w:tc>\n" + 
        "                <w:tc>\n" + 
        "                    <w:p>\n" + 
        "                        <w:pPr>\n" + 
        "                            <w:ind w:left=\"0\"/>\n" + 
        "                            <w:jc w:val=\"right\"/>\n" + 
        "                        </w:pPr>\n" + 
        "                        <w:r>\n" + 
        "                            <w:rPr>\n" + 
        "                                <w:b/>\n" + 
        "                                <w:sz w:val=\"22\"/>\n" + 
        "                            </w:rPr>\n" + 
        "                            <w:t>0</w:t>\n" + 
        "                        </w:r>\n" + 
        "                    </w:p>\n" + 
        "                </w:tc>\n" + 
        "            </w:tr>\n" + 
        "            <w:tr>\n" + 
        "                <w:tc>\n" + 
        "                    <w:p>\n" + 
        "                        <w:pPr>\n" + 
        "                            <w:ind w:left=\"0\"/>\n" + 
        "                            <w:jc w:val=\"left\"/>\n" + 
        "                        </w:pPr>\n" + 
        "                        <w:r>\n" + 
        "                            <w:rPr>\n" + 
        "                                <w:b/>\n" + 
        "                                <w:sz w:val=\"22\"/>\n" + 
        "                            </w:rPr>\n" + 
        "                            <w:t>Total Efectivo y Efectivo Equivalente</w:t>\n" + 
        "                        </w:r>\n" + 
        "                    </w:p>\n" + 
        "                </w:tc>\n" + 
        "                <w:tc>\n" + 
        "                    <w:p>\n" + 
        "                        <w:pPr>\n" + 
        "                            <w:ind w:left=\"0\"/>\n" + 
        "                            <w:jc w:val=\"right\"/>\n" + 
        "                        </w:pPr>\n" + 
        "                        <w:r>\n" + 
        "                            <w:rPr>\n" + 
        "                                <w:b/>\n" + 
        "                                <w:sz w:val=\"22\"/>\n" + 
        "                            </w:rPr>\n" + 
        "                            <w:t>108.888</w:t>\n" + 
        "                        </w:r>\n" + 
        "                    </w:p>\n" + 
        "                </w:tc>\n" + 
        "                <w:tc>\n" + 
        "                    <w:p>\n" + 
        "                        <w:pPr>\n" + 
        "                            <w:ind w:left=\"0\"/>\n" + 
        "                            <w:jc w:val=\"right\"/>\n" + 
        "                        </w:pPr>\n" + 
        "                        <w:r>\n" + 
        "                            <w:rPr>\n" + 
        "                                <w:b/>\n" + 
        "                                <w:sz w:val=\"22\"/>\n" + 
        "                            </w:rPr>\n" + 
        "                            <w:t>8.540.000</w:t>\n" + 
        "                        </w:r>\n" + 
        "                    </w:p>\n" + 
        "                </w:tc>\n" + 
        "                <w:tc>\n" + 
        "                    <w:p>\n" + 
        "                        <w:pPr>\n" + 
        "                            <w:ind w:left=\"0\"/>\n" + 
        "                            <w:jc w:val=\"right\"/>\n" + 
        "                        </w:pPr>\n" + 
        "                        <w:r>\n" + 
        "                            <w:rPr>\n" + 
        "                                <w:b/>\n" + 
        "                                <w:sz w:val=\"22\"/>\n" + 
        "                            </w:rPr>\n" + 
        "                            <w:t>500.000</w:t>\n" + 
        "                        </w:r>\n" + 
        "                    </w:p>\n" + 
        "                </w:tc>\n" + 
        "                <w:tc>\n" + 
        "                    <w:p>\n" + 
        "                        <w:pPr>\n" + 
        "                            <w:ind w:left=\"0\"/>\n" + 
        "                            <w:jc w:val=\"right\"/>\n" + 
        "                        </w:pPr>\n" + 
        "                        <w:r>\n" + 
        "                            <w:rPr>\n" + 
        "                                <w:b/>\n" + 
        "                                <w:sz w:val=\"22\"/>\n" + 
        "                            </w:rPr>\n" + 
        "                            <w:t>10.000.000</w:t>\n" + 
        "                        </w:r>\n" + 
        "                    </w:p>\n" + 
        "                </w:tc>\n" + 
        "                <w:tc>\n" + 
        "                    <w:p>\n" + 
        "                        <w:pPr>\n" + 
        "                            <w:ind w:left=\"0\"/>\n" + 
        "                            <w:jc w:val=\"right\"/>\n" + 
        "                        </w:pPr>\n" + 
        "                        <w:r>\n" + 
        "                            <w:rPr>\n" + 
        "                                <w:b/>\n" + 
        "                                <w:sz w:val=\"22\"/>\n" + 
        "                            </w:rPr>\n" + 
        "                            <w:t>19.148.888</w:t>\n" + 
        "                        </w:r>\n" + 
        "                    </w:p>\n" + 
        "                </w:tc>\n" + 
        "            </w:tr>\n" + 
        "        </w:tbl>\n" + 
        "        <w:p>\n" + 
        "            <w:r>\n" + 
        "                <w:rPr>\n" + 
        "                    <w:b/>\n" + 
        "                </w:rPr>\n" + 
        "                <w:t> </w:t>\n" + 
        "            </w:r>\n" + 
        "        </w:p>\n" + 
        "        <w:br w:type=\"page\"/>\n" + 
        "        <w:sectPr>\n" + 
        "            <w:pgSz w:code=\"9\" w:h=\"16839\" w:w=\"11907\"/>\n" + 
        "            <w:pgMar w:left=\"1440\" w:bottom=\"1440\" w:right=\"1440\" w:top=\"1440\"/>\n" + 
        "        </w:sectPr>\n" + 
        "    </w:body>\n" + 
        "</w:document>\n");
        
        System.out.println(word);
        
        writer.println(word.toString());
        writer.close();      

    }
}
