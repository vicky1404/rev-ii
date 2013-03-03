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

@Entity
@NamedQueries( { 
    @NamedQuery(name = XbrlConceptoColumna.FIND_ALL , query = "select o from XbrlConceptoColumna o"),
    @NamedQuery(name = XbrlConceptoColumna.FIND_BY_ESTRUCTURA , query = "select c from XbrlConceptoColumna c where c.columna.grilla.estructura.idEstructura = :idEstructura")
})
@Table(name = "IFRS_XBRL_CONCEPTO_COLUMNA")
@IdClass(XbrlConceptoCeldaPK.class)
public class XbrlConceptoColumna implements Serializable {    
    private static final long serialVersionUID = 3041768733504005638L;
    
    public static final String FIND_ALL = "XbrlConceptoCelda.findAll";
    public static final String FIND_BY_ESTRUCTURA = "XbrlConceptoCelda.findByEstructura";
    
    @Id
    @Column(name = "ID_COLUMNA", nullable = false, insertable = false, updatable = false)
    private Long idColumna;
    @Id
    @Column(name = "ID_CONCEPTO_XBRL", nullable = false, length = 1024)
    private String idConceptoXbrl;
    
    @Id
    @Column(name = "ID_GRILLA", nullable = false, insertable = false, updatable = false)
    private Long idGrilla;
    
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "ID_COLUMNA", referencedColumnName = "ID_COLUMNA"),
        @JoinColumn(name = "ID_GRILLA", referencedColumnName = "ID_GRILLA")
    })
    private Columna columna;
    
    @ManyToOne
    @JoinColumn(name = "ID_TAXONOMIA")
    private XbrlTaxonomia xbrlTaxonomia;
    
    
    public XbrlConceptoColumna() {
    }
    
    public XbrlConceptoColumna(String idConceptoXbrl, Columna columna, XbrlTaxonomia xbrlTaxonomia) {
        super();
        this.idConceptoXbrl = idConceptoXbrl;
        this.columna = columna;
        this.xbrlTaxonomia = xbrlTaxonomia;
    }

    public void setIdColumna(Long idColumna) {
        this.idColumna = idColumna;
    }

    public Long getIdColumna() {
        return idColumna;
    }

    public void setIdConceptoXbrl(String idConceptoXbrl) {
        this.idConceptoXbrl = idConceptoXbrl;
    }

    public String getIdConceptoXbrl() {
        return idConceptoXbrl;
    }

    public void setIdGrilla(Long idGrilla) {
        this.idGrilla = idGrilla;
    }

    public Long getIdGrilla() {
        return idGrilla;
    }


    public void setXbrlTaxonomia(XbrlTaxonomia xbrlTaxonomia) {
        this.xbrlTaxonomia = xbrlTaxonomia;
    }

    public XbrlTaxonomia getXbrlTaxonomia() {
        return xbrlTaxonomia;
    }

	public Columna getColumna() {
		return columna;
	}

	public void setColumna(Columna columna) {
		this.columna = columna;
	}
}
