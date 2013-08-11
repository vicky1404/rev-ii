package cl.mdr.ifrs.ejb.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


import org.w3c.dom.Document;
import org.w3c.dom.Node;


public class DocumentTest {
    
    public static final String HTML_EMPTY =  " <html xmlns=\"http://www.w3.org/1999/xhtml\">"+
                                             " <head>" + 
                                             " <meta name=\"generator\"" + 
                                             " content=\"HTML Tidy for Java (vers. 2009-12-01), see jtidy.sourceforge.net\" />" + 
                                             " <title></title>" + 
                                             " </head>" + 
                                             " <body> </body>" + 
                                             " </html>";
    
    public DocumentTest() {
        super();
    }

    private static DocumentBuilder documentBuilder;

    static {
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("Unable to get document builder. - " + e.getMessage(), e);
        }
    }

    /**
     * Allow to create a DOM Document instance with the given parameter.
     *
     * @param xml
     * @return
     */
    public static Document buildDocument(String xml) {
        Document doc = null;
        try {
            doc = documentBuilder.parse(new ByteArrayInputStream(xml.getBytes()));
        } catch (Exception e) {
            handleException(e);
        }
        return doc;
    }

    /**
     * Allow to create a DOM Document instance with the given parameter.
     * @param stream
     * @return
     */
    public static Document buildDocument(InputStream stream) {
        Document doc = null;
        try {
            doc = documentBuilder.parse(stream);
        } catch (Exception e) {
            handleException(e);
        }
        return doc;
    }

    public static void handleException(Exception e) {
        if (e instanceof IOException) {
            throw new RuntimeException("Unable to read xml - " + e.getMessage(), e);
        } else if (e instanceof SAXParseException) {
            SAXParseException exception = (SAXParseException)e;
            throw new RuntimeException("Unable to parse xml - Line: " + exception.getLineNumber() + " - " +
                                       e.getMessage(), e);
        } else if (e instanceof SAXException) {
            throw new RuntimeException("Unable to parse xml - " + e.getMessage(), e);
        } else {
            throw new RuntimeException(e);
        }
    }
    
    
    public static void one_node(){
        //String xml = "<data>hello world</data>";
        String xml = 
                     "   <!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\\n" + 
                     "  \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\\n" + 
                     "   <html xmlns=\"http://www.w3.org/1999/xhtml\">\""+   
                     "   <head> "+
                     "   <meta name=\"generator\" content=\"HTML Tidy for Java (vers. 2009-12-01), see jtidy.sourceforge.net\" />"+
                     "   <title></title>"+
                     "   </head>"+
                     "   <body>"+
                     "   <blockquote style=\"MARGIN-RIGHT: 0px\" dir=\"ltr\">"+
                     "   <p>&nbsp;</p>"+
                     "   <p><strong><u>Entidad que reporta.</u></strong></p>"+
                     "   <p>En esta nota ...</p>"+
                     "   </blockquote>"+
                     "   </body>"+
                     "   </html>";  
        
        String xml2 =   "<html xmlns=\"http://www.w3.org/1999/xhtml\">"+
                        "<head>" + 
                        "<meta name=\"generator\"" + 
                        " content=\"HTML Tidy for Java (vers. 2009-12-01), see jtidy.sourceforge.net\" />" + 
                        "<title></title>" + 
                        "</head>" + 
                        "<body> </body>" + 
                        "</html>";
        Document doc = buildDocument(xml2);
        Node dataNode = doc.getFirstChild();
        System.out.println(dataNode.getChildNodes().getLength());
        //System.out.println(dataNode.getTextContent());
    }
 
    
    public static void one_level(){
        String xml = "<data><child>hello world</child></data>";
        Document doc = buildDocument(xml);
        Node dataNode = doc.getFirstChild();
        System.out.println(dataNode.getChildNodes().getLength());
        //System.out.println(dataNode.getFirstChild().getTextContent());
    }
 
    
    public static void test_exception(){
        String xml = "<data>hello world<data>";
        try {
            @SuppressWarnings("unused")
            Document doc = buildDocument(xml);
        } catch (RuntimeException e) {
            System.out.println("Unable to parse xml - Line: 1 - XML document structures must start and end within the same entity."+ e.getMessage());
            return;
        }        
    }
    
    public static void main(String[] args){
        one_node();
        //one_level();
        //test_exception();
    }
}
