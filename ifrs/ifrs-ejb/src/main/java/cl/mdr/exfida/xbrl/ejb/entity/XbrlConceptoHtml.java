package cl.mdr.exfida.xbrl.ejb.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import cl.mdr.exfida.xbrl.ejb.entity.pk.XbrlConceptoCeldaPK;
import cl.mdr.ifrs.ejb.entity.Columna;
import cl.mdr.ifrs.ejb.entity.Html;

@Entity
@NamedQueries( { 
    @NamedQuery(name = XbrlConceptoHtml.FIND_ALL , query = "select o from XbrlConceptoHtml o"),
    @NamedQuery(name = XbrlConceptoHtml.FIND_BY_ESTRUCTURA , query = "select c from XbrlConceptoHtml c where c.html.estructura.idEstructura = :idEstructura")
})
@Table(name = "IFRS_XBRL_CONCEPTO_HTML")
public class XbrlConceptoHtml implements Serializable {    
    private static final long serialVersionUID = 3041768733504005638L;
    
    public static final String FIND_ALL = "XbrlConceptoHtml.findAll";
    public static final String FIND_BY_ESTRUCTURA = "XbrlConceptoHtml.findByEstructura";
    
    
    @Id
    @Column(name = "ID_CONCEPTO_XBRL", nullable = false, length = 1024)
    private String idConceptoXbrl;
    
    @ManyToOne
    @JoinColumn(name = "ID_HTML")
    private Html html;
    
    @ManyToOne
    @JoinColumn(name = "ID_TAXONOMIA")
    private XbrlTaxonomia xbrlTaxonomia;
    
    
    public XbrlConceptoHtml() {
    }
    
    public XbrlConceptoHtml(String idConceptoXbrl, Html html, XbrlTaxonomia xbrlTaxonomia) {
        this.idConceptoXbrl = idConceptoXbrl;
        this.setHtml(html);
        this.xbrlTaxonomia = xbrlTaxonomia;
    }


    public void setIdConceptoXbrl(String idConceptoXbrl) {
        this.idConceptoXbrl = idConceptoXbrl;
    }

    public String getIdConceptoXbrl() {
        return idConceptoXbrl;
    }




    public void setXbrlTaxonomia(XbrlTaxonomia xbrlTaxonomia) {
        this.xbrlTaxonomia = xbrlTaxonomia;
    }

    public XbrlTaxonomia getXbrlTaxonomia() {
        return xbrlTaxonomia;
    }

	public Html getHtml() {
		return html;
	}

	public void setHtml(Html html) {
		this.html = html;
	}


}
