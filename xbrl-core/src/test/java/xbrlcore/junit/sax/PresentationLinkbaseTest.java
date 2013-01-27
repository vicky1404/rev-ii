package xbrlcore.junit.sax;

import java.util.List;

import xbrlcore.linkbase.PresentationLinkbase;
import xbrlcore.linkbase.PresentationLinkbaseElement;
import xbrlcore.taxonomy.Concept;
import xbrlcore.taxonomy.DiscoverableTaxonomySet;
import junit.framework.TestCase;

public class PresentationLinkbaseTest extends TestCase {

	DiscoverableTaxonomySet p_dts;

	DiscoverableTaxonomySet t_dts;

    @Override
	public void setUp() {
		p_dts = TestHelper.getPrimaryTaxonomy();
		t_dts = TestHelper.getTemplateTaxonomy();
	}

	public void testPresentationLinkbase() {
		assertNotNull(p_dts);

		PresentationLinkbase presentationLinkbase = p_dts
				.getPresentationLinkbase();

		List<PresentationLinkbaseElement> rootElementList = presentationLinkbase
				.getPresentationLinkbaseElementRoot(null);
		assertEquals(1, rootElementList.size());

		PresentationLinkbaseElement presElement = rootElementList.get(0);
		assertNotNull(presElement);
		assertEquals("p-pr_Produkte", presElement.getConcept().getId());

//		List<PresentationLinkbaseElement> presentationList = presentationLinkbase.getPresentationList(presElement.getConcept(), null);
//		assertEquals(11, presentationList.size());
	}

	public void testPresentationList1() {
		assertNotNull(p_dts);

		Concept pConcept1 = p_dts.getConceptByID("p-pr_Essen");
		Concept pConcept2 = p_dts.getConceptByID("p-pr_Wurst");
		Concept pConcept3 = p_dts.getConceptByID("p-pr_Fleisch");
		assertNotNull(pConcept1);
		assertNotNull(pConcept2);
		assertNotNull(pConcept3);

		p_dts
				.getPresentationLinkbase();
	}

	public void testPresentationIterator1() {
		assertNotNull(p_dts);

		p_dts.getConceptByID("p-pr_Produkte");

		p_dts
				.getPresentationLinkbase();
	}

	public void testPresentationList2() {
		try {
			assertNotNull(t_dts);

			Concept pConcept1 = t_dts.getConceptByID("d-ko_Kontinente");
			Concept pConcept2 = t_dts.getConceptByID("d-ko_Europa");
			Concept pConcept3 = t_dts.getConceptByID("d-ko_Asien");
			Concept pConcept4 = t_dts.getConceptByID("d-ko_Amerika");
			assertNotNull(pConcept1);
			assertNotNull(pConcept2);
			assertNotNull(pConcept3);
			assertNotNull(pConcept4);

			PresentationLinkbase presentationLinkbase = t_dts
					.getPresentationLinkbase();
			presentationLinkbase
					.getPresentationList("d-ko-2006-12-31.xsd");

		} catch (Exception ex) {
			System.err.println(ex.toString());
			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}

}