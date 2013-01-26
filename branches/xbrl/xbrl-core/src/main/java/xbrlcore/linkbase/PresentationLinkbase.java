package xbrlcore.linkbase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import xbrlcore.constants.GeneralConstants;
import xbrlcore.taxonomy.Concept;
import xbrlcore.taxonomy.DiscoverableTaxonomySet;
import xbrlcore.taxonomy.TaxonomySchema;
import xbrlcore.xlink.ExtendedLinkElement;
import xbrlcore.xlink.Locator;

/**
 * This class represents a presentation linkbase of a DTS. The purpose of this
 * linkbase is to structure the elements in a hierarchical order. <br/>
 * <br/>
 * 
 * @author Daniel Hamm
 */
public class PresentationLinkbase extends Linkbase {

	private static final long serialVersionUID = 2459167242500244987L;

	public static final String NAME = "presentation";

	/*
	 * maps each extended link role to the corresponding
	 * presentationLinkbaseElementList
	 */
	private final Map<String, List<PresentationLinkbaseElement>> linkRoleToElementLists = new HashMap<String, List<PresentationLinkbaseElement>>();

	

	private boolean linkbaseBuilt = false;

	private int positionDeepestLevel;

	/**
	 * Constructor.
	 * 
	 * @param dts
	 *            Taxonomy which the presentation linkbase refers to.
	 */
	public PresentationLinkbase(DiscoverableTaxonomySet dts) {
		super(dts, NAME);
	}

	/**
	 * This method builds the presentation linkbase. More detailed, the
	 * hierarchical structure of the elements is built. This structure is built
	 * in various PresentationLinkbaseElement objects.
	 */
	@Override
	public void buildLinkbase() {

		if (linkbaseBuilt)
			return;

		// build the presentationLinkbaseElementList for each extended link role
		for (String currExtendedLinkRole : getExtendedLinkRoles()) {
			List<PresentationLinkbaseElement> presentationLinkbaseElementList = new ArrayList<PresentationLinkbaseElement>();

			List<ExtendedLinkElement> extendedLinkElementList = getExtendedLinkElementsFromBaseSet(currExtendedLinkRole);

			for (ExtendedLinkElement currExtendedLinkElement : extendedLinkElementList) {
				Concept currXBRLElement = ((Locator) currExtendedLinkElement).getConcept();
				
				

				PresentationLinkbaseElement currPresentationLinkbaseElement = new PresentationLinkbaseElement(currExtendedLinkRole,
						(Locator) currExtendedLinkElement);

				// set successor elements
				List<ExtendedLinkElement> xLinkElementsSuccessor = getTargetExtendedLinkElements(currXBRLElement, currExtendedLinkRole);
				List<Locator> xbrlElementSuccessor = new ArrayList<Locator>();
				for (ExtendedLinkElement currXLinkElement : xLinkElementsSuccessor) {
					if (currXLinkElement.isLocator()) {
						xbrlElementSuccessor.add(((Locator) currXLinkElement));
					}
				}
				currPresentationLinkbaseElement.setSuccessorElements(xbrlElementSuccessor);

				// set parent element (there can be only one)
				List<ExtendedLinkElement> xLinkList = getSourceExtendedLinkElements(currXBRLElement, currExtendedLinkRole);
				if (xLinkList.size() > 0) {
					ExtendedLinkElement xLinkElementParent = xLinkList.get(0);
					if (xLinkElementParent != null && xLinkElementParent.isLocator()) {
						currPresentationLinkbaseElement.setParentElement(((Locator) xLinkElementParent));
					}
				}

				// set level
				int level = determineLevel(0, currXBRLElement, currExtendedLinkRole);
				currPresentationLinkbaseElement.setLevel(level);

				// set number of successor at deepest level
				if (xbrlElementSuccessor.size() == 0) {
					currPresentationLinkbaseElement.setNumSuccessorAtDeepestLevel(0);
				} else {
					int numberOfSuccessorAtDeepestLevel = determineNumberOfSuccessorAtDeepestLevel(0, currXBRLElement, currExtendedLinkRole);
					currPresentationLinkbaseElement.setNumSuccessorAtDeepestLevel(numberOfSuccessorAtDeepestLevel);
				}

				presentationLinkbaseElementList.add(currPresentationLinkbaseElement);
			}

			linkRoleToElementLists.put(currExtendedLinkRole, presentationLinkbaseElementList);
		}

		linkbaseBuilt = true;
	}

	/**
	 * Returns a list of PresentationLinkbaseElement objects according to the
	 * presentation linkbase. The list already is in correct order.
	 * 
	 * @param taxonomyName
	 *            The name of the taxonomy of which the presentation shall be
	 *            obtained (if NULL, the whole DTS is taken).
	 * @param extendedLinkRole
	 *            Extended link role from which the presentation shall be
	 *            obtained (if NULL, the default link role is taken).
	 * @return List of xbrlcore.linkbase.PresentationLinkbaseElement objects.
	 */
	public List<PresentationLinkbaseElement> getPresentationList(String taxonomyName, String extendedLinkRole)
	/* throws XBRLCoreException */{
		if (extendedLinkRole == null) {
			extendedLinkRole = GeneralConstants.XBRL_LINKBASE_DEFAULT_LINKROLE;
		}
		List<PresentationLinkbaseElement> resultList = new ArrayList<PresentationLinkbaseElement>();
		List<PresentationLinkbaseElement> finalResultList = new ArrayList<PresentationLinkbaseElement>();

		positionDeepestLevel = 0;

		List<PresentationLinkbaseElement> rootElementList = getPresentationLinkbaseElementRoot(extendedLinkRole);

		for (int i = 0; i < rootElementList.size(); i++) {
			PresentationLinkbaseElement currRootElement = rootElementList.get(i);

			List<PresentationLinkbaseElement> tmpResultList = new ArrayList<PresentationLinkbaseElement>();
			tmpResultList = collectPresentationLinkbaseElementList(currRootElement, extendedLinkRole, tmpResultList);
			resultList.addAll(tmpResultList);
		}

		for (int i = 0; i < resultList.size(); i++) {
			PresentationLinkbaseElement currElement = resultList.get(i);
			if(currElement == null || currElement.getConcept() == null){
				continue;
			}
			TaxonomySchema taxonomySchema = currElement.getConcept().getTaxonomySchema();
			if ((taxonomyName != null && taxonomySchema.getName().equals(taxonomyName)) || taxonomyName == null) {
				finalResultList.add(currElement);
			}
		}

		return finalResultList;
	}
	
	public List<PresentationLinkbaseElement> getPresentationListByLinkRole(String extendedLinkRole)
	/* throws XBRLCoreException */{
		
		List<PresentationLinkbaseElement> resultList = new ArrayList<PresentationLinkbaseElement>();
		List<PresentationLinkbaseElement> finalResultList = new ArrayList<PresentationLinkbaseElement>();

		positionDeepestLevel = 0;

		List<PresentationLinkbaseElement> rootElementList = getPresentationLinkbaseElementRoot(extendedLinkRole);

		for (int i = 0; i < rootElementList.size(); i++) {
			PresentationLinkbaseElement currRootElement = rootElementList.get(i);

			List<PresentationLinkbaseElement> tmpResultList = new ArrayList<PresentationLinkbaseElement>();
			tmpResultList = collectPresentationLinkbaseElementList(currRootElement, extendedLinkRole, tmpResultList);
			resultList.addAll(tmpResultList);
		}

		for (int i = 0; i < resultList.size(); i++) {
			PresentationLinkbaseElement currElement = resultList.get(i);
			if(currElement == null || currElement.getConcept() == null){
				continue;
			}
			finalResultList.add(currElement);
		}

		return finalResultList;
	}

	/**
	 * Returns an iterator which contains all the elements in the presentation
	 * linkbase for the given parameters in correct order.
	 * 
	 * @param taxonomyName
	 *            The name of the taxonomy of which the presentation shall be
	 *            obtained (if NULL, the whole DTS is taken).
	 * @param extendedLinkRole
	 *            Extended link role from which the presentation shall be
	 *            obtained (if NULL, the default link role is taken).
	 * @return Iterator of xbrlcore.linkbase.PresentationLinkbaseElement
	 *         objects.
	 */
	public Iterator<PresentationLinkbaseElement> iterator(String taxonomyName, String extendedLinkRole)/*
																										 * throws
																										 * XBRLCoreException
																										 */{
		return getPresentationList(taxonomyName, extendedLinkRole).iterator();
	}




	/**
	 * Returns a list of PresentationLinkbaseElement objects according to the
	 * default link role (http://www.xbrl.org/2003/role/link) of the
	 * presentation linkbase which belong to a certain taxonomy within the DTS.
	 * The list already is in correct order.
	 * 
	 * @param taxonomyName
	 *            Name of the taxonomy within the DTS the elements have to
	 *            belong to.
	 * @return List of xbrlcore.linkbase.PresentationLinkbaseElement objects.
	 */
	public List<PresentationLinkbaseElement> getPresentationList(String taxonomyName)
	/* throws XBRLCoreException */{
		return getPresentationList(taxonomyName, GeneralConstants.XBRL_LINKBASE_DEFAULT_LINKROLE);
	}

	/**
	 * Returns an iterator of PresentationLinkbaseElement objects according to
	 * the default link role (http://www.xbrl.org/2003/role/link) of the
	 * presentation linkbase which belong to a certain taxonomy within the DTS.
	 * The elements within the iterator already is in correct order.
	 * 
	 * @param taxonomyName
	 *            Name of the taxonomy within the DTS the elements have to
	 *            belong to.
	 * @return Iterator of xbrlcore.linkbase.PresentationLinkbaseElement
	 *         objects.
	 */
	public Iterator<PresentationLinkbaseElement> iterator(String taxonomyName)
	/* throws XBRLCoreException */{
		return getPresentationList(taxonomyName).iterator();
	}

	/**
	 * Returns a certain PresentationLinkbaseElement object.
	 * 
	 * @param tmpElement
	 *            XBRL element the PresentationLinkbaseElement object refers to.
	 * @param extendedLinkRole
	 *            Extended link role from which the PresentationLinkbaseElement
	 *            shall be obtained (if NULL, the default link role is taken).
	 * @return The xbrlcore.linkbase.PresentationLinkbaseElement object which
	 *         matches to the given parameters.
	 */
	public PresentationLinkbaseElement getPresentationLinkbaseElement(Locator tmpElement, String extendedLinkRole) {
		buildLinkbase();
		

		if (extendedLinkRole == null) {
			extendedLinkRole = GeneralConstants.XBRL_LINKBASE_DEFAULT_LINKROLE;
		}
		
		if(tmpElement == null){
			return null;
		}
		
		List<PresentationLinkbaseElement> presentationLinkbaseElementList = linkRoleToElementLists.get(extendedLinkRole);
		if (presentationLinkbaseElementList != null) {
			for (PresentationLinkbaseElement currElement : presentationLinkbaseElementList) {
				if (currElement != null && currElement.getConcept() != null 
						&& currElement.getConcept().getId().equals(tmpElement.getConcept().getId())
						&& currElement.getLocator().getLabel().equals(tmpElement.getLabel())) {
					return currElement;
				}
			}
		}
		return null;
	}

	

	/**
	 * Helping method to build the hierarchical structure.
	 */
	private List<PresentationLinkbaseElement> collectPresentationLinkbaseElementList(PresentationLinkbaseElement currElement, String extendedLinkRole,
			List<PresentationLinkbaseElement> currList) {
		if(currElement == null){
			return currList;
		}
		
		currList.add(currList.size(), currElement);

		if (currElement.getNumDirectSuccessor() == 0 || !currElement.getConcept().isAbstract()) {
			currElement.setPositionDeepestLevel(positionDeepestLevel++);
		}

		List<Locator> successorElements = currElement.getSuccessorElements();
		for (Locator currXBRLElement : successorElements) {
			PresentationLinkbaseElement nextElement = getPresentationLinkbaseElement(currXBRLElement, extendedLinkRole);
			collectPresentationLinkbaseElementList(nextElement, extendedLinkRole, currList);
		}
		return currList;
	}

	/**
	 * Helping method to get the level of a certain element within the
	 * presentation linkbase.
	 */
	private int determineLevel(int i, Concept currXBRLElement, String extendedLinkRole) {
		i++;
		List<ExtendedLinkElement> xLinkList = getSourceExtendedLinkElements(currXBRLElement, extendedLinkRole);
		if (xLinkList.size() > 0) {
			ExtendedLinkElement xLinkElementParent = xLinkList.get(0);
			if (xLinkElementParent != null && xLinkElementParent.isLocator()) {
				i = determineLevel(i, ((Locator) xLinkElementParent).getConcept(), extendedLinkRole);
			}
		}
		return i;
	}

	/**
	 * Helping method to get the number of successors at the deepest level (this
	 * is needed later to render the template).
	 */
	private int determineNumberOfSuccessorAtDeepestLevel(int i, Concept currXBRLElement, String extendedLinkRole) {
		List<ExtendedLinkElement> currXLinkElement = getTargetExtendedLinkElements(currXBRLElement, extendedLinkRole);
		/*
		 * if element is not the source of any other element, it is at the
		 * deepest level
		 */
		if (currXLinkElement.size() == 0) {
			i++;
		} else {
			for (ExtendedLinkElement xLinkElementChild : currXLinkElement) {
				if (xLinkElementChild.isLocator()) {
					i = determineNumberOfSuccessorAtDeepestLevel(i, ((Locator) xLinkElementChild).getConcept(), extendedLinkRole);
				}
			}
		}
		return i;
	}

	/**
	 * Returns root elements of the presentation linkbase.
	 * 
	 * @param extendedLinkRole
	 *            Extended link role of the presentation linkbase.
	 * @return List A list with all the elements which are root elements in the
	 *         presentation linkbase (that means, which have no parent element)
	 *         in a specific extended link role.
	 */
	public List<PresentationLinkbaseElement> getPresentationLinkbaseElementRoot(String extendedLinkRole) {

		buildLinkbase();

		List<PresentationLinkbaseElement> resultList = new ArrayList<PresentationLinkbaseElement>();
		if (extendedLinkRole == null) {
			extendedLinkRole = GeneralConstants.XBRL_LINKBASE_DEFAULT_LINKROLE;
		}
		List<PresentationLinkbaseElement> presentationLinkbaseElementList = linkRoleToElementLists.get(extendedLinkRole);
		if (presentationLinkbaseElementList != null) {
			for (PresentationLinkbaseElement currElement : presentationLinkbaseElementList) {
				if (currElement.getParentElement() == null) {
					resultList.add(currElement);
				}
			}
		}
		return resultList;
	}
}
