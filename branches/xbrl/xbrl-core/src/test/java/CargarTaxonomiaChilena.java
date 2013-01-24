

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Test;

import xbrlcore.constants.GeneralConstants;
import xbrlcore.junit.sax.TestHelper;
import xbrlcore.linkbase.LabelLinkbase;
import xbrlcore.linkbase.PresentationLinkbase;
import xbrlcore.linkbase.PresentationLinkbaseElement;
import xbrlcore.taxonomy.Concept;
import xbrlcore.taxonomy.DiscoverableTaxonomySet;
import xbrlcore.taxonomy.RoleType;
import xbrlcore.taxonomy.TaxonomySchema;
import xbrlcore.xlink.ExtendedLinkElement;

public class CargarTaxonomiaChilena {
	
	private static final Logger LOGGER = Logger.getLogger(CargarTaxonomiaChilena.class); 
	
	@Test
	public void testCargarTaxonomiaSVSShell() throws Exception{
		DiscoverableTaxonomySet dts = TestHelper.getDTS("xbrl/SVS CL-CS 2012-10-05 Modificada el 2012-11-21/cl-cs_nota-1/cl-cs_nota-1_role-810000.xsd");
		
		Map<String, TaxonomySchema> taxonomyMap = dts.getTaxonomyMap();
		Set<Entry<String, TaxonomySchema>> entrySet = taxonomyMap.entrySet();
		
		for (Entry<String, TaxonomySchema> entry : entrySet) {
			
			
			
			
			LOGGER.info(entry.getKey());
			
			DiscoverableTaxonomySet nota = entry.getValue().getDts();
			
			
			Map<String, List<PresentationLinkbaseElement>> linkRoleToElementList = nota.getPresentationLinkbase().getLinkRoleToElementList();
			Set<Entry<String, List<PresentationLinkbaseElement>>> entrySet2 = linkRoleToElementList.entrySet();
			for (Entry<String, List<PresentationLinkbaseElement>> entry2 : entrySet2) {
				System.out.println(entry2.getKey());
			}
			
			
						
//			final LabelLinkbase labelLinkbase = nota.getLabelLinkbase();
//			
//			for(Map.Entry<String, List<PresentationLinkbaseElement>> entryLabel : presentationLinkbase.getLinkRoleToElementList().entrySet()) {
//	             
//	            for(PresentationLinkbaseElement linkbaseElement : entryLabel.getValue()){  
//	                final Concept concept = linkbaseElement.getConcept() == null ? new Concept() : linkbaseElement.getConcept();                
//	                final String label = labelLinkbase.getLabel(concept, GeneralConstants.XBRL_ROLE_LABEL);                
//	                LOGGER.info(label);
//	            }
//	            
//	        }
			
			return;
		}
		
		
	}

}
