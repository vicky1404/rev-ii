package cl.mdr.ifrs.ejb.cross;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class DocumentFactory {
    
    public static final String HTML_EMPTY =  " <html xmlns=\"http://www.w3.org/1999/xhtml\">"+
                                             " <head>" + 
                                             " <meta name=\"generator\"" + 
                                             " content=\"HTML Tidy for Java (vers. 2009-12-01), see jtidy.sourceforge.net\" />" + 
                                             " <title></title>" + 
                                             " </head>" + 
                                             " <body> </body>" + 
                                             " </html>";
    
    public DocumentFactory() {
        super();
    }
    
    private static DocumentBuilder documentBuilder;

    static {
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("No se puede obtener el DocumentBuilder. - " + e.getMessage(), e);
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
}
