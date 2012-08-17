package cl.bicevida.xbrl.ejb.entity;

import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.xbrl.ejb.entity.pk.XbrlConceptoCeldaPK;

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
@NamedQueries( { @NamedQuery(name = "XbrlConceptoCelda.findAll", query = "select o from XbrlConceptoCelda o") })
@Table(name = "REV_XBRL_CONCEPTO_CELDA")
@IdClass(XbrlConceptoCeldaPK.class)
public class XbrlConceptoCelda implements Serializable {    
    private static final long serialVersionUID = 3041768733504005638L;
    
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
}
