package xbrlcore.junit.sax;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import xbrlcore.exception.XBRLException;
import xbrlcore.taxonomy.DiscoverableTaxonomySet;
import xbrlcore.taxonomy.sax.SAXBuilder;

public class TestHelper {

	public static DiscoverableTaxonomySet getPrimaryTaxonomy() {
		try {
			SAXBuilder saxBuilder = new SAXBuilder();
			return saxBuilder.build(new InputSource(
					TestHelper.class.getResource("/xbrl/unit_tests/taxonomy_original/p-pr-2006-12-31.xsd").toString()));
		} catch (Exception ex) {
			System.err.println(ex.toString());
			ex.printStackTrace();
		}
		return null;
	}

	public static DiscoverableTaxonomySet getTemplateTaxonomy() {
		try {
			SAXBuilder saxBuilder = new SAXBuilder();
			return saxBuilder.build(new InputSource(TestHelper.class.getResource(
					"/xbrl/unit_tests/taxonomy_original/t-st-2006-12-31.xsd").toString()));
		} catch (Exception ex) {
			System.err.println(ex.toString());
			ex.printStackTrace();
		}
		return null;

	}

	public static DiscoverableTaxonomySet getDTS(String source)
			throws SAXException, IOException, ParserConfigurationException, XBRLException {

		SAXBuilder saxBuilder = new SAXBuilder();
		String string = source.startsWith("/") ? "" : "/";
		return saxBuilder.build( new InputSource(TestHelper.class.getResource(string + source).toString()));

	}

}