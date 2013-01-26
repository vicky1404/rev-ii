package xbrlcore.xlink;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a resource within a linkbase. <br/><br/>
 * 
 * @author Daniel Hamm
 */
public class Resource extends ExtendedLinkElement {

    private static final long serialVersionUID = 791825586156371335L;

    private final String lang;

    private final String value;

    private final Map<String, String> hgbRefs = new HashMap<String, String>();
    private final Map<String, String> refs = new HashMap<String, String>();

    /**
     * Constructor.
     * 
     * @param id
     * @param label
     *            Label of this resource.
     * @param linkbaseSource 
     * @param extLinkRole
     * @param role
     * @param title
     * @param lang
     * @param value
     */
    public Resource(String id, String label, String linkbaseSource, String extLinkRole, String role, String title, String lang, String value) {
        super(id, label, linkbaseSource, extLinkRole, role, title);

        this.lang = lang;
        this.value = value;
    }

    /**
     * Checks whether two Resource objects are equal. This is true if and only
     * if: <br/>- both have the same label attribute <br/>- both are located
     * within the same extended link role <br/>- both have the same language
     * <br/>- both have the same value
     * 
     * @param obj
     *            Object the current Resource is checked against.
     * @return True if both Resource objects are the same, false otherwise.
     *  
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Resource))
            return false;
        if (!super.equals(obj))
            return false;

        Resource otherResource = (Resource) obj;

        return getLabel().equals(otherResource.getLabel())
                && (getExtendedLinkRole() == null ? otherResource
                        .getExtendedLinkRole() == null : getExtendedLinkRole()
                        .equals(otherResource.getExtendedLinkRole()))
                && (lang == null ? otherResource.getLang() == null : lang
                        .equals(otherResource.getLang()))
                && (value == null ? otherResource.getValue() == null : value
                        .equals(otherResource.getValue()))
                && (getLinkbaseSource() == null ? otherResource
                        .getLinkbaseSource() == null : getLinkbaseSource()
                        .equals(otherResource.getLinkbaseSource()));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = hash * 31 + getLabel().hashCode();
        hash = hash
                * 31
                + (getExtendedLinkRole() != null ? getExtendedLinkRole()
                        .hashCode() : 0);
        hash = hash * 31 + (lang != null ? lang.hashCode() : 0);
        hash = hash * 31 + (value != null ? value.hashCode() : 0);
        hash = hash
                * 31
                + (getLinkbaseSource() != null ? getLinkbaseSource().hashCode()
                        : 0);
        return hash;
    }

    /**
     * @return false
     */
    @Override
    public boolean isLocator() {
        return false;
    }

    /**
     * @return true
     */
    @Override
    public boolean isResource() {
        return true;
    }

    /**
     * @return xml:lang attribute of this resource.
     */
    public final String getLang() {
        return lang;
    }

    /**
     * @return Value of this resource (value of the according XML element).
     */
    public final String getValue() {
        return value;
    }

    public void addHGBRef(String name, String value) {
        hgbRefs.put(name, value);
    }
    
    public final String getHGBRef(String name) {
        return hgbRefs.get(name);
    }
    

    public final Map<String, String> getHGBRefs() {
        return hgbRefs;
    }

    public void addRef(String name, String value) {
        refs.put(name, value);
    }
    
    public final String getRef(String name) {
        return refs.get(name);
    }
    

    public final Map<String, String> getRefs() {
        return refs;
    }

    public final String getRefName() {
        return refs.get("Name");
    }

    public final String getParagraph() {
        return refs.get("Paragraph");
    }

    public final String getSubParagraph() {
        return refs.get("Subparagraph");
    }

    public final String getClause() {
        return refs.get("Clause");
    }

    public final boolean getLegalFormEU() {
        return Boolean.parseBoolean(hgbRefs.get("legalFormEU"));
    }

    public final boolean getLegalFormKst() {
        return Boolean.parseBoolean(hgbRefs.get("legalFormKSt"));
    }

    public final boolean getLegalFormPg() {
        return Boolean.parseBoolean(hgbRefs.get("legalFormPG"));
    }

    public final String getTypeOperatingResult() {
        return hgbRefs.get("typeOperatingResult");
    }

    public final boolean getFiscalRequirement() {
        String value = hgbRefs.get("fiscalRequirement");
        if (value == null)
            return false;
        
        return (value.trim().length() > 0);
    }

    public final String getFiscalRequirementDetail() {
        String value = hgbRefs.get("fiscalRequirement");
        if (value == null)
            return "";
        
        return value;
    }    
    
    public final String getNotPermittedFor() {
        return hgbRefs.get("notPermittedFor");
    }

    public final String getValidSince() {
        String value = hgbRefs.get("validSince");
        if (value == null)
            return null;
        
        return value.trim();
    }

    public final String getValidThrough() {
        String value = hgbRefs.get("validThrough");
        if (value == null)
            return null;
        
        return value.trim();
    }

    public final String getFiscalValidSince() {
        String value = hgbRefs.get("fiscalValidSince");
        if (value == null)
            return null;
        
        return value.trim();
    }

    public final String getFiscalValidThrough() {
        String value = hgbRefs.get("fiscalValidThrough");
        if (value == null)
            return null;
        
        return value.trim();
    }

    public final boolean isGenericPosition() {
        return (value != null) && value.contains("nicht zuordenbar");
    }
}
