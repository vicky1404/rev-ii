import static junit.framework.Assert.assertNotNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
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

		Set<RoleType> roleTypesSorted = dts.getRoleTypesSorted();
		assertNotNull(roleTypesSorted);
		for (RoleType roleType : roleTypesSorted) {
			String firstDefinition = roleType.getFirstDefinition();
			assertNotNull(firstDefinition);
			LOGGER.info(roleType.getTaxonomySchema().getName() + " " + firstDefinition);
		}

	}

	@Test
	public void testCargarTituloRolesNotas() throws Exception {
		Set<RoleType> roleTypesSorted = dts.getRoleTypesSorted();
		assertNotNull(roleTypesSorted);
		for (RoleType roleType : roleTypesSorted) {
			String firstDefinition = roleType.getFirstDefinition();
			assertNotNull(firstDefinition);
			LOGGER.info(firstDefinition);
		}

	}

	@Test
	public void testCargarLabelConceptosPorRoleEEFFRole20000() throws Exception {

		String role = "http://www.svs.cl/cl/fr/cs/role/eeff_role-200000";

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
			Arc arcForSourceLocator = presentationLinkbase.getArcForSourceLocator(presentationLinkbaseElement.getLocator());
			if(arcForSourceLocator != null){
				preferredLabel = arcForSourceLocator.getPreferredLabel();
			}
			
			if(preferredLabel == null){
				Arc arcForTargeLocator = presentationLinkbase.getArcForTargetLocator(presentationLinkbaseElement.getLocator());
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
			if(attrib != null && !attrib.isEmpty()){
				LOGGER.info(prefix + label + " (" + attrib + ") ");	
			}
			
		}

	}

}
