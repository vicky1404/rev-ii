package cl.mdr.ifrs.ejb.test;

import java.io.FileWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

import javax.xml.transform.stream.StreamSource;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.DocumentSource;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class HtmlToWordTest {
    public HtmlToWordTest() {
        super();
    }
    
    public static void main(String[] args) {
        try {
            Document document = styleDocument(getDocument(), "file:///C:\\BASIC_IP_To_Word.xslt?".replace("?", ""));            
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter( System.out, format );
            writer.write( document );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Document styleDocument(Document document, String stylesheet) throws Exception {

        // load the transformer using JAXP
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(new StreamSource(stylesheet));

        // now lets style the given document
        DocumentSource source = new DocumentSource(document);
        DocumentResult result = new DocumentResult();
        transformer.transform(source, result);

        // return the transformed document
        Document transformedDoc = result.getDocument();
        return transformedDoc;
    }
    
    private static Document getDocument() throws DocumentException {
        String doc = "<h2 xmlns=\"http://www.w3.org/1999/xhtml\">Rodrigo Reyes</h2>";
        return DocumentHelper.parseText(doc);
    }
}
