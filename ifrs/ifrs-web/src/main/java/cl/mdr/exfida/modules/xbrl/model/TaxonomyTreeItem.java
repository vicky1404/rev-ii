package cl.mdr.exfida.modules.xbrl.model;

import java.util.List;

import xbrlcore.taxonomy.Concept;
import xbrlcore.taxonomy.TaxonomySchema;

public class TaxonomyTreeItem {
      
    private TaxonomySchema taxonomySchema;     
    private String taxonomyParent;
    private Concept concept;
    private List<TaxonomyTreeItem> children;
    
    
    
    public TaxonomyTreeItem() {
        super();
    }

    public TaxonomyTreeItem(Concept concept) {
        super();
        this.concept = concept;
    }


    public void setConcept(Concept concept) {
        this.concept = concept;
    }

    public Concept getConcept() {
        return concept;
    }

    public void setTaxonomySchema(TaxonomySchema taxonomySchema) {
        this.taxonomySchema = taxonomySchema;
    }

    public TaxonomySchema getTaxonomySchema() {
        return taxonomySchema;
    }

    public void setChildren(List<TaxonomyTreeItem> children) {
        this.children = children;
    }

    public List<TaxonomyTreeItem> getChildren() {
        return children;
    }

    public void setTaxonomyParent(String taxonomyParent) {
        this.taxonomyParent = taxonomyParent;
    }

    public String getTaxonomyParent() {
        return taxonomyParent;
    }
}
