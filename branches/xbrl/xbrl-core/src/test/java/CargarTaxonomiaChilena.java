import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import xbrlcore.constants.GeneralConstants;
import xbrlcore.exception.XBRLException;
import xbrlcore.junit.sax.TestHelper;
import xbrlcore.linkbase.LabelLinkbase;
import xbrlcore.linkbase.PresentationLinkbase;
import xbrlcore.linkbase.PresentationLinkbaseElement;
import xbrlcore.taxonomy.Concept;
import xbrlcore.taxonomy.DiscoverableTaxonomySet;
import xbrlcore.taxonomy.RoleType;
import xbrlcore.xlink.Arc;
import xbrlcore.xlink.Locator;

public class CargarTaxonomiaChilena {

	private static final Logger LOGGER = Logger.getLogger(CargarTaxonomiaChilena.class);

	static DiscoverableTaxonomySet dts;

	@BeforeClass
	public static void setUp() throws URISyntaxException, XBRLException, SAXException, IOException, ParserConfigurationException {
		URI uri = CargarTaxonomiaChilena.class.getResource("/xbrl/SVS CL-CS 2012-10-05 Modificada el 2012-11-21/cl-cs_shell_2012-10-05.xsd").toURI();
		dts = TestHelper.getDTS(uri.toString());
	}

	@Test
	public void testCargarDefinicionTaxonomiasSVS() throws Exception {

		StringBuilder builder = new StringBuilder();
		Set<RoleType> roleTypesSorted = dts.getRoleTypesSorted();
		assertNotNull(roleTypesSorted);
		for (RoleType roleType : roleTypesSorted) {
			String firstDefinition = roleType.getFirstDefinition();
			assertNotNull(firstDefinition);
			String name = roleType.getTaxonomySchema().getName() + " " + firstDefinition;
			LOGGER.info(name);
			builder.append(name);
			builder.append(String.format("%n"));
		}
		
		testResultado(builder.toString(), "/expected/taxonomy/TitulosNotas.txt");

	}



	@Test
	public void testCargarLabelConceptosPorRoleEEFFRole20000() throws Exception {

		String role = "http://www.svs.cl/cl/fr/cs/role/eeff_role-200000";

		testResultado(printNotaConceptos(role), "/expected/taxonomy/LabelsEEFF20000.txt");

	}
	
	@Test
	public void testCargarLabelConceptosPorNota25() throws Exception {

		String role = "http://www.svs.cl/cl/fr/cs/role/nota-25_role-838100";

		testResultado(printNotaConceptos(role), "/expected/taxonomy/LabelsNota25.txt");

	}

	private void testResultado(String printNotaConceptos, String file) {
		
		Scanner scanner = new Scanner(getClass().getResourceAsStream(file));
		Scanner scannerResult = new Scanner(new StringReader(printNotaConceptos));
		while (scanner.hasNext()) {
			assertEquals(scanner.next(), scannerResult.next());
		}
	}

	private String printNotaConceptos(String role) {
		StringBuilder builder = new StringBuilder();
		PresentationLinkbase presentationLinkbase = dts.getPresentationLinkbase();
		LabelLinkbase labelLinkbase = dts.getLabelLinkbase();

		List<PresentationLinkbaseElement> list = presentationLinkbase.getPresentationListByLinkRole(role);

		
		for (PresentationLinkbaseElement presentationLinkbaseElement : list) {
			String prefix = "";
			for (int i = 0; i < presentationLinkbaseElement.getLevel() * 2 ; i++) {
				prefix += "-";
			}
			
			Concept concept = presentationLinkbaseElement.getConcept();
			String label = null;
			String preferredLabel = null;
			Locator locator = presentationLinkbaseElement.getLocator();
			Arc arcForSourceLocator = presentationLinkbase.getArcForSourceLocator(locator);
			if(arcForSourceLocator != null){
				preferredLabel = arcForSourceLocator.getPreferredLabel();
			}
			
			if(preferredLabel == null){
				Arc arcForTargeLocator = presentationLinkbase.getArcForTargetLocator(locator);
				if(arcForTargeLocator != null){
					preferredLabel = arcForTargeLocator.getPreferredLabel();	
				}
			}
			
			
			if(preferredLabel != null && !preferredLabel.isEmpty()){
				label = labelLinkbase.getLabel(concept, preferredLabel);		
			}
			
			if(label == null || label.isEmpty()){
				label = labelLinkbase.getLabel(concept, GeneralConstants.XBRL_ROLE_LABEL);
			}
					
			String attrib = concept.getAttrib("codigo");
			String string = null;
			if(attrib != null && !attrib.isEmpty()){
				string = prefix + label + " (" + attrib + ") ";
					
			}else{
				string = prefix + label;
				
			}
			LOGGER.info(string);
			builder.append(string);
			builder.append(String.format("%n"));
			
		}
		return builder.toString();
	}

}
