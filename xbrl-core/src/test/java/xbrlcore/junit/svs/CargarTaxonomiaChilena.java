package xbrlcore.junit.svs;
import static junit.framework.Assert.assertNotNull;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Test;

import xbrlcore.constants.GeneralConstants;
import xbrlcore.linkbase.LabelLinkbase;
import xbrlcore.linkbase.PresentationLinkbase;
import xbrlcore.linkbase.PresentationLinkbaseElement;
import xbrlcore.taxonomy.Concept;
import xbrlcore.taxonomy.RoleType;
import xbrlcore.xlink.Arc;
import xbrlcore.xlink.Locator;

public class CargarTaxonomiaChilena extends AbstractSVSTests {

	private static final Logger LOGGER = Logger.getLogger(CargarTaxonomiaChilena.class);

		

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
