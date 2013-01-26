package xbrlcore.linkbase;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import xbrlcore.constants.GeneralConstants;
import xbrlcore.taxonomy.Concept;
import xbrlcore.taxonomy.DiscoverableTaxonomySet;
import xbrlcore.xlink.ExtendedLinkElement;
import xbrlcore.xlink.Locator;
import xbrlcore.xlink.Resource;

/**
 * This class represents a label linkbase of a DTS. This contains one or more
 * labels for concepts. <br/><br/>
 * 
 * @author Daniel Hamm
 */
public class LabelLinkbase extends Linkbase {

	private static final long serialVersionUID = 1794952740633234300L;

    public static final String NAME = "label";

	private Set<String> languageSet = null;

	/**
	 * Constructor.
	 * 
	 * @param dts
	 *            The taxonomy this label linkbase refers to.
	 */
	public LabelLinkbase(DiscoverableTaxonomySet dts) {
		super(dts, NAME);
	}

    /**
     * Returns a certain label map of a certain role contained in the label
     * linkbase. The map key is the "xlink:role" attribute;  The map value 
     * is the label value.
     * 
     * @param concept
     *            The element the label refers to.
     * @param lang
     *            The language of the label (if NULL, the language is not taken
     *            into account).
     * @param xbrlExtendedLinkRole
     *            The extended link role of the label. If null, the default link
     *            role (http://www.xbrl.org/2003/role/link) is taken.
     * @return The label matching the parameters. If no label is found, null is
     *         returned.
     */
    public Map<String, String> getLabelMap(Concept concept, String lang, String xbrlExtendedLinkRole) {
        if (concept== null || lang == null || xbrlExtendedLinkRole == null ) {
            return null;
        }
        Map<String, String> labelMap = new HashMap<String, String>();
        List<ExtendedLinkElement> possibleResults = getTargetExtendedLinkElements(concept, xbrlExtendedLinkRole);
        for (ExtendedLinkElement tmpElement : possibleResults) {
            if (tmpElement.isResource()) {
                Resource currResource = (Resource) tmpElement;
                if (currResource.getLang().equals(lang)) {
                    labelMap.put(tmpElement.getRole(), currResource.getValue());
                }
            }
        }
        return labelMap;
    }

	@Override
	public void buildLinkbase() {
	}

	/**
	 * Returns a certain label contained in this label linkbase. The label is
	 * determined by the role, which in this case is
	 * http://www.xbrl.org/2003/role/label (the default extended link role).
	 * 
	 * @param concept
	 *            The element the label refers to.
	 * @param lang
	 *            The language of the label.
	 * @param xbrlExtendedLinkRole
	 *            The extended link role of the label. If null, the default link
	 *            role (http://www.xbrl.org/2003/role/link) is taken.
	 * @return The label matching the parameters. If no label is found, null is
	 *         returned.
	 */
	public String getLabel(Concept concept, String lang,
			String xbrlExtendedLinkRole) {
		if (xbrlExtendedLinkRole == null) {
			xbrlExtendedLinkRole = GeneralConstants.XBRL_LINKBASE_DEFAULT_LINKROLE;
		}
		List<ExtendedLinkElement> possibleResults = getTargetExtendedLinkElements(concept,
				xbrlExtendedLinkRole);
		for (ExtendedLinkElement tmpElement : possibleResults) {
			if (tmpElement.isResource()) {
				Resource currResource = (Resource) tmpElement;
				if (currResource.getLang().equals(lang)
						&& tmpElement.getRole().equals(
								GeneralConstants.XBRL_ROLE_LABEL)) {
					return currResource.getValue();
				}
			} else if (tmpElement.isLocator()) {
				Locator currLoc = (Locator) tmpElement;
				if (currLoc.getResource() != null) {
					if (currLoc.getResource().getLang().equals(lang)
							&& tmpElement.getRole().equals(
									GeneralConstants.XBRL_ROLE_LABEL)) {
						return currLoc.getResource().getValue();
					}
				}
			}
		}
		return null;
	}

	/**
	 * Returns a certain label of a certain role contained in the label
	 * linkbase.
	 * 
	 * @param concept
	 *            The element the label refers to.
	 * @param lang
	 *            The language of the label (if NULL, the language is not taken
	 *            into account).
	 * @param role
	 *            The role of the label.
	 * @param xbrlExtendedLinkRole
	 *            The extended link role of the label. If null, the default link
	 *            role (http://www.xbrl.org/2003/role/link) is taken.
	 * @return The label matching the parameters. If no label is found, null is
	 *         returned.
	 */
	public String getLabel(Concept concept, String lang, String role,
			String xbrlExtendedLinkRole) {
		if (xbrlExtendedLinkRole == null) {
			xbrlExtendedLinkRole = GeneralConstants.XBRL_LINKBASE_DEFAULT_LINKROLE;
		}
		List<ExtendedLinkElement> possibleResults = getTargetExtendedLinkElements(concept,
				xbrlExtendedLinkRole);
		for (ExtendedLinkElement tmpElement : possibleResults) {
			if (tmpElement.isResource() && tmpElement.getRole().equals(role)) {
				Resource currResource = (Resource) tmpElement;
				if ((lang == null) || currResource.getLang().equals(lang)) {
					return currResource.getValue();
				}
			}
		}
		return null;
	}

	/**
	 * Returns a certain label of a certain role contained in the label
	 * linkbase, but does neither take the language nor the extended link role
	 * into account.
	 * 
	 * @param concept
	 *            The element the label refers to.
	 * @param role
	 *            The role of the label.
	 * @return The label matching the parameters. If no label is found, null is
	 *         returned.
	 */
	public String getLabel(Concept concept, String role) {
	    for (String currExtendedLinkRole : getExtendedLinkRoles()) {
			String tmpLabel = getLabel(concept, null, role,
					currExtendedLinkRole);
			if (tmpLabel != null)
				return tmpLabel;
		}
		return null;
	}

    /**
     * Returns a certain label of a certain role contained in the label
     * linkbase, but does neither take the language nor the extended link role
     * into account.
     * 
     * @param concept
     *            The element the label refers to.
     * @param lang
     * @param role
     *            The role of the label.
     * @return The label matching the parameters. If no label is found, null is
     *         returned.
     */
    public String getLabel2(Concept concept, String lang, String role) {
        for (String currExtendedLinkRole : getExtendedLinkRoles()) {
            String tmpLabel = getLabel(concept, lang, role,
                    currExtendedLinkRole);
            if (tmpLabel != null)
                return tmpLabel;
        }
        return null;
    }

	/**
	 * Returns whether this label linkbase contains labels of a specific
	 * language.
	 * 
	 * @param lang
	 *            Language which is checked whether it is contained in this
	 *            label linkbase.
	 * @return True if there is at least one label of the specified language,
	 *         false otherwise.
	 */
	public boolean containsLanguage(String lang) {
		return getLanguageSet().contains(lang);
	}

	/**
	 * @return Set of languages of labels used in this label linkbase.
	 */
	public Set<String> getLanguageSet() {
		if (languageSet == null) {
			/* build language set */
			languageSet = new HashSet<String>();
			for (ExtendedLinkElement currExLinkElement : getExtendedLinkElements()) {
				if (currExLinkElement.isResource()) {
					Resource currResource = (Resource) currExLinkElement;
					languageSet.add(currResource.getLang());
				}
			}
		}
		return languageSet;
	}

}
