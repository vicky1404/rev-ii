package xbrlcore.junit.svs;

import static junit.framework.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.BeforeClass;
import org.xml.sax.SAXException;

import xbrlcore.exception.XBRLException;
import xbrlcore.junit.sax.TestHelper;
import xbrlcore.taxonomy.DiscoverableTaxonomySet;

public abstract class AbstractSVSTests {
	
	
	static DiscoverableTaxonomySet dts;

	@BeforeClass
	public static void setUp() throws URISyntaxException, XBRLException, SAXException, IOException, ParserConfigurationException {
		dts = TestHelper.getDTS("/xbrl/SVS CL-CS 2012-10-05 Modificada el 2012-11-21/cl-cs_shell_2012-10-05.xsd");
	}
	
	protected void testResultado(String printNotaConceptos, String file) {
		
		Scanner scanner = new Scanner(getClass().getResourceAsStream(file));
		Scanner scannerResult = new Scanner(new StringReader(printNotaConceptos));
		while (scanner.hasNext()) {
			assertEquals(scanner.next(), scannerResult.next());
		}
	}
}
