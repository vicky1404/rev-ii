package xbrlcore.taxonomy;

import java.io.Serializable;
import java.util.Map;

import xbrlcore.constants.GeneralConstants;

/**
 * This class represents a single XBRL concept; which actually is a schema
 * element of the taxonomySchemaName. <br/><br/>
 * 
 * @author Daniel Hamm
 */
public class Concept implements Serializable, Cloneable {

    private static final long serialVersionUID = 3237499348079052902L;

    private final String name;
    private final String id;

    private final String typeString;
    private final ConceptTypes type;
    private final ConceptTypeRestriction typeRestriction;

    private final TaxonomySchema taxonomySchema;

    private final String substitutionGroup;

    private final boolean tuple;
    private final boolean item;
    private final boolean fractionItem;
    private final boolean numericItem;
    private final boolean isAbstract;
    private final boolean nillable;

    private final String periodType;

    private final String typedDomainRef;

    private boolean isTupleItem = false;

    private transient Object userObject = null;
    
    private Map<String, String> attributes;
    
    public Concept(String name, String id, String type, ConceptTypeRestriction typeRestriction,
            TaxonomySchema taxonomySchema,
            String substitutionGroup, boolean isAbstract, boolean nillable, String periodType, String typedDomainRef) {
    	this(name, id, type, typeRestriction, taxonomySchema, substitutionGroup, isAbstract, nillable, periodType, typedDomainRef, null);
    }

    /**
     * 
     * @param name The name of the element.
     * @param id
     * @param type
     * @param typeRestriction
     * @param taxonomySchema
     *            Taxonomy name this element belongs to.
     * @param substitutionGroup
     *            Substitution group of the element.
     * @param isAbstract
     *            True if element is abstract, otherwise false.
     * @param nillable
     *            True if the element is nillable, otherwise false.
     * @param periodType
     *            Period type of the element.
     * @param typedDomainRef
     *            typedDomainRef attribute.
     */
    public Concept(String name, String id, String type, ConceptTypeRestriction typeRestriction,
                   TaxonomySchema taxonomySchema,
                   String substitutionGroup, boolean isAbstract, boolean nillable, String periodType, String typedDomainRef, Map<String, String> attributes) {
        this.name = name;
        this.id = id;
        this.typeString = type;
        if (type == null && typeRestriction != null)
            this.type = typeRestriction.getType();
        else
            this.type = ConceptTypes.getFromString(type);
        this.typeRestriction = typeRestriction;
        this.taxonomySchema = taxonomySchema;

        this.substitutionGroup = substitutionGroup;
        boolean tuple = false;
        boolean item = false;
        boolean fractionItem = false;
        boolean numericItem = true;
        if (substitutionGroup != null) {
            String sglc = substitutionGroup.toLowerCase();
            if (sglc.equals("xbrli:tuple")) {
                numericItem = false;
                tuple = true;
                item = false;
            } else if (sglc.equals("xbrli:item")) {
                numericItem = true;
                tuple = false;
                item = true;
            }
        }
        this.tuple = tuple;
        this.item = item;
        this.fractionItem = fractionItem;
        this.numericItem = numericItem;
        this.isAbstract = isAbstract;
        this.nillable = nillable;

        if ((periodType != null)
        && (periodType.equals(GeneralConstants.CONTEXT_INSTANT) ||
            periodType.equals(GeneralConstants.CONTEXT_DURATION))) {
            this.periodType = periodType;
        } else {
            this.periodType = null;
        }

        this.typedDomainRef = typedDomainRef;
        
        this.attributes = attributes;
    }

    /**
     * @return Taxonomy this element belongs to.
     */
    public final TaxonomySchema getTaxonomySchema() {
        return taxonomySchema;
    }

	/**
	 * 
	 * @return ID of the element.
	 */
	@Override
	public String toString() {
		if (id != null)
			return "ID: " + id + ", name: " + name;

        return "ID: missing, name: " + name;
	}

    /**
     * Tests whether this object is equal to another one.
     * 
     * @param obj
     *            Object the current Concept is tested against.
     * @return True if and only if obj is an Concept and the name of obj is the
     *         same as the name of the current element, both IDs are the same
     *         and both belong to the same schema file.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Concept))
            return false;
        Concept otherConcept = (Concept) obj;
        return ((name == null) ? otherConcept.getName() == null : name
                .equals(otherConcept.getName()))
                && ((id == null) ? otherConcept.getId() == null : id.equals(otherConcept.getId()))
                && taxonomySchema.equals(otherConcept.getTaxonomySchema());
    }

    /**
     * 
     * @return Returns a hash code of this object.
     */
    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + (id != null ? id.hashCode() : 0);
        hash = hash * 31 + (name != null ? name.hashCode() : 0);
        hash = hash * 31 + taxonomySchema.hashCode();
        return hash;
    }

    /**
     * Checks whether this concept represents a typed dimension.
     * 
     * @return True if this concept is a typed dimension, false otherwise.
     */
    public boolean isTypedDimension() {
        if (substitutionGroup != null
                && substitutionGroup
                        .equals(GeneralConstants.XBRL_SUBST_GROUP_DIMENSION_ITEM)
                && typedDomainRef != null) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether this concept represents an explicit dimension.
     * 
     * @return True if this concept is an explicit dimension, false otherwise.
     */
    public boolean isExplicitDimension() {
        if (substitutionGroup
                .equals(GeneralConstants.XBRL_SUBST_GROUP_DIMENSION_ITEM)
                && typedDomainRef == null) {
            return true;
        }
        return false;
    }

    /**
     * @return True if the element is abstract, otherwise false.
     */
    public final boolean isAbstract() {
        return isAbstract;
    }

    /**
     * @return ID of the element.
     */
    public final String getId() {
        return id;
    }

    /**
     * @return Name of the element.
     */
    public final String getName() {
        return name;
    }

    /**
     * @return True if the element is nillable, otherwise false.
     */
    public final boolean isNillable() {
        return nillable;
    }

    /**
     * @return Period type of the element.
     */
    public final String getPeriodType() {
        return periodType;
    }

    /**
     * @return Substitution group of the element.
     */
    public final String getSubstitutionGroup() {
        return substitutionGroup;
    }

    public final boolean isTuple() {
        return tuple;
    }

    public final boolean isItem() {
        return item;
    }
    
    /**
     * @return Type of the element.
     */
    public final String getTypeString() {
        return typeString;
    }

    /**
     * @return Type of the element.
     */
    public final ConceptTypes getType() {
        return type;
    }

    /**
     * @return the complex type simple type restriction (if any).
     */
    public final ConceptTypeRestriction getTypeRestriction() {
        return typeRestriction;
    }

    /**
     * 
     * @return typedDomainRef attribute.
     */
    public final String getTypedDomainRef() {
        return typedDomainRef;
    }

    /**
     * @return A clone of this Concept object.
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * @return Returns the fractionItem.
     */
    public final boolean isFractionItem() {
        return fractionItem && getType().isFractional();
    }

    /**
     * @return Returns the numericItem.
     */
    public final boolean isNumericItem() {
        return numericItem && getType().isNumeric();
    }

    void setIsTupleItem(boolean isTupleItem) {
        this.isTupleItem = isTupleItem;
    }

    public final boolean isTupleItem() {
        return isTupleItem;
    }

    public final Object getUserObject() {
        return userObject;
    }

    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }

	public String getAttrib(String key) {
		if(attributes != null){
			return attributes.get(key);	
		}else{
			return "";
		}
		
	}
	
	

	
}
