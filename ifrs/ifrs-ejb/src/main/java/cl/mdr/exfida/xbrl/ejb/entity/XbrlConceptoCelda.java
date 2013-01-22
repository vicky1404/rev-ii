package cl.mdr.exfida.xbrl.ejb.entity;

import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.exfida.xbrl.ejb.entity.pk.XbrlConceptoCeldaPK;

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

@Entity
@NamedQueries( { 
    @NamedQuery(name = XbrlConceptoCelda.FIND_ALL , query = "select o from XbrlConceptoCelda o"),
    @NamedQuery(name = XbrlConceptoCelda.FIND_BY_ESTRUCTURA , query = "select c from XbrlConceptoCelda c where c.idGrilla = :idEstructura")
})
@Table(name = "IFRS_XBRL_CONCEPTO_CELDA")
@IdClass(XbrlConceptoCeldaPK.class)
public class XbrlConceptoCelda implements Serializable {    
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
    @Column(name = "ID_FILA", nullable = false, insertable = false, updatable = false)
    private Long idFila;
    @Id
    @Column(name = "ID_GRILLA", nullable = false, insertable = false, updatable = false)
    private Long idGrilla;
    
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "ID_COLUMNA", referencedColumnName = "ID_COLUMNA"),
        @JoinColumn(name = "ID_GRILLA", referencedColumnName = "ID_GRILLA"),
        @JoinColumn(name = "ID_FILA", referencedColumnName = "ID_FILA")
    })
    private Celda celda;
    
    @ManyToOne
    @JoinColumn(name = "ID_TAXONOMIA")
    private XbrlTaxonomia xbrlTaxonomia;
    
    
    public XbrlConceptoCelda() {
        super();        
    }
    
    public XbrlConceptoCelda(String idConceptoXbrl, Celda celda, XbrlTaxonomia xbrlTaxonomia) {
        super();
        this.idConceptoXbrl = idConceptoXbrl;
        this.celda = celda;
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

    public void setIdFila(Long idFila) {
        this.idFila = idFila;
    }

    public Long getIdFila() {
        return idFila;
    }

    public void setIdGrilla(Long idGrilla) {
        this.idGrilla = idGrilla;
    }

    public Long getIdGrilla() {
        return idGrilla;
    }

    public void setCelda(Celda celda) {
        this.celda = celda;
    }

    public Celda getCelda() {
        return celda;
    }

    public void setXbrlTaxonomia(XbrlTaxonomia xbrlTaxonomia) {
        this.xbrlTaxonomia = xbrlTaxonomia;
    }

    public XbrlTaxonomia getXbrlTaxonomia() {
        return xbrlTaxonomia;
    }
}
