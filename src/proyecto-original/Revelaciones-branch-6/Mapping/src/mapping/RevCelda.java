package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "RevCelda.findAll", query = "select o from RevCelda o") })
@Table(name = "REV_CELDA")
@IdClass(RevCeldaPK.class)
public class RevCelda implements Serializable {
    @Column(name = "CHILD_HORIZONTAL")
    private BigDecimal childHorizontal;
    @Column(name = "CHILD_VERTICAL")
    private BigDecimal childVertical;
    @Column(length = 128)
    private String formula;
    @Id
    @Column(name = "ID_COLUMNA", nullable = false, insertable = false, updatable = false)
    private BigDecimal idColumna;
    @Id
    @Column(name = "ID_FILA", nullable = false)
    private BigDecimal idFila;
    @Id
    @Column(name = "ID_GRILLA", nullable = false, insertable = false, updatable = false)
    private BigDecimal idGrilla;
    @Column(name = "PARENT_HORIZONTAL")
    private BigDecimal parentHorizontal;
    @Column(name = "PARENT_VERTICAL")
    private BigDecimal parentVertical;
    @Column(length = 2048)
    private String valor;
    @ManyToOne
    @JoinColumns({
    @JoinColumn(name = "ID_COLUMNA", referencedColumnName = "ID_COLUMNA"),
    @JoinColumn(name = "ID_GRILLA", referencedColumnName = "ID_GRILLA")
    })
    private RevColumna revColumna1;
    @ManyToOne
    @JoinColumn(name = "ID_TIPO_DATO")
    private RevTipoDato revTipoDato;
    @OneToMany(mappedBy = "revCelda2")
    private List<RevRelacionDetalleEeff> revRelacionDetalleEeffList2;
    @OneToMany(mappedBy = "revCelda5")
    private List<RevXbrlConceptoCelda> revXbrlConceptoCeldaList2;
    @OneToMany(mappedBy = "revCelda8")
    private List<RevRelacionEeff> revRelacionEeffList3;
    @ManyToOne
    @JoinColumn(name = "ID_TIPO_CELDA")
    private RevTipoCelda revTipoCelda1;

    public RevCelda() {
    }

    public RevCelda(BigDecimal childHorizontal, BigDecimal childVertical, String formula, RevColumna revColumna1,
                    BigDecimal idFila, RevTipoCelda revTipoCelda1, RevTipoDato revTipoDato,
                    BigDecimal parentHorizontal, BigDecimal parentVertical, String valor) {
        this.childHorizontal = childHorizontal;
        this.childVertical = childVertical;
        this.formula = formula;
        this.revColumna1 = revColumna1;
        this.idFila = idFila;
        this.revTipoCelda1 = revTipoCelda1;
        this.revTipoDato = revTipoDato;
        this.parentHorizontal = parentHorizontal;
        this.parentVertical = parentVertical;
        this.valor = valor;
    }


    public BigDecimal getChildHorizontal() {
        return childHorizontal;
    }

    public void setChildHorizontal(BigDecimal childHorizontal) {
        this.childHorizontal = childHorizontal;
    }

    public BigDecimal getChildVertical() {
        return childVertical;
    }

    public void setChildVertical(BigDecimal childVertical) {
        this.childVertical = childVertical;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public BigDecimal getIdColumna() {
        return idColumna;
    }

    public void setIdColumna(BigDecimal idColumna) {
        this.idColumna = idColumna;
    }

    public BigDecimal getIdFila() {
        return idFila;
    }

    public void setIdFila(BigDecimal idFila) {
        this.idFila = idFila;
    }

    public BigDecimal getIdGrilla() {
        return idGrilla;
    }

    public void setIdGrilla(BigDecimal idGrilla) {
        this.idGrilla = idGrilla;
    }


    public BigDecimal getParentHorizontal() {
        return parentHorizontal;
    }

    public void setParentHorizontal(BigDecimal parentHorizontal) {
        this.parentHorizontal = parentHorizontal;
    }

    public BigDecimal getParentVertical() {
        return parentVertical;
    }

    public void setParentVertical(BigDecimal parentVertical) {
        this.parentVertical = parentVertical;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public RevColumna getRevColumna1() {
        return revColumna1;
    }

    public void setRevColumna1(RevColumna revColumna1) {
        this.revColumna1 = revColumna1;
        if (revColumna1 != null) {
            this.idGrilla = revColumna1.getIdGrilla();
            this.idColumna = revColumna1.getIdColumna();
        }
    }

    public RevTipoDato getRevTipoDato() {
        return revTipoDato;
    }

    public void setRevTipoDato(RevTipoDato revTipoDato) {
        this.revTipoDato = revTipoDato;
    }

    public List<RevRelacionDetalleEeff> getRevRelacionDetalleEeffList2() {
        return revRelacionDetalleEeffList2;
    }

    public void setRevRelacionDetalleEeffList2(List<RevRelacionDetalleEeff> revRelacionDetalleEeffList2) {
        this.revRelacionDetalleEeffList2 = revRelacionDetalleEeffList2;
    }

    public RevRelacionDetalleEeff addRevRelacionDetalleEeff(RevRelacionDetalleEeff revRelacionDetalleEeff) {
        getRevRelacionDetalleEeffList2().add(revRelacionDetalleEeff);
        revRelacionDetalleEeff.setRevCelda2(this);
        return revRelacionDetalleEeff;
    }

    public RevRelacionDetalleEeff removeRevRelacionDetalleEeff(RevRelacionDetalleEeff revRelacionDetalleEeff) {
        getRevRelacionDetalleEeffList2().remove(revRelacionDetalleEeff);
        revRelacionDetalleEeff.setRevCelda2(null);
        return revRelacionDetalleEeff;
    }

    public List<RevXbrlConceptoCelda> getRevXbrlConceptoCeldaList2() {
        return revXbrlConceptoCeldaList2;
    }

    public void setRevXbrlConceptoCeldaList2(List<RevXbrlConceptoCelda> revXbrlConceptoCeldaList2) {
        this.revXbrlConceptoCeldaList2 = revXbrlConceptoCeldaList2;
    }

    public RevXbrlConceptoCelda addRevXbrlConceptoCelda(RevXbrlConceptoCelda revXbrlConceptoCelda) {
        getRevXbrlConceptoCeldaList2().add(revXbrlConceptoCelda);
        revXbrlConceptoCelda.setRevCelda5(this);
        return revXbrlConceptoCelda;
    }

    public RevXbrlConceptoCelda removeRevXbrlConceptoCelda(RevXbrlConceptoCelda revXbrlConceptoCelda) {
        getRevXbrlConceptoCeldaList2().remove(revXbrlConceptoCelda);
        revXbrlConceptoCelda.setRevCelda5(null);
        return revXbrlConceptoCelda;
    }

    public List<RevRelacionEeff> getRevRelacionEeffList3() {
        return revRelacionEeffList3;
    }

    public void setRevRelacionEeffList3(List<RevRelacionEeff> revRelacionEeffList3) {
        this.revRelacionEeffList3 = revRelacionEeffList3;
    }

    public RevRelacionEeff addRevRelacionEeff(RevRelacionEeff revRelacionEeff) {
        getRevRelacionEeffList3().add(revRelacionEeff);
        revRelacionEeff.setRevCelda8(this);
        return revRelacionEeff;
    }

    public RevRelacionEeff removeRevRelacionEeff(RevRelacionEeff revRelacionEeff) {
        getRevRelacionEeffList3().remove(revRelacionEeff);
        revRelacionEeff.setRevCelda8(null);
        return revRelacionEeff;
    }

    public RevTipoCelda getRevTipoCelda1() {
        return revTipoCelda1;
    }

    public void setRevTipoCelda1(RevTipoCelda revTipoCelda1) {
        this.revTipoCelda1 = revTipoCelda1;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("childHorizontal=");
        buffer.append(getChildHorizontal());
        buffer.append(',');
        buffer.append("childVertical=");
        buffer.append(getChildVertical());
        buffer.append(',');
        buffer.append("formula=");
        buffer.append(getFormula());
        buffer.append(',');
        buffer.append("idColumna=");
        buffer.append(getIdColumna());
        buffer.append(',');
        buffer.append("idFila=");
        buffer.append(getIdFila());
        buffer.append(',');
        buffer.append("idGrilla=");
        buffer.append(getIdGrilla());
        buffer.append(',');
        buffer.append("parentHorizontal=");
        buffer.append(getParentHorizontal());
        buffer.append(',');
        buffer.append("parentVertical=");
        buffer.append(getParentVertical());
        buffer.append(',');
        buffer.append("valor=");
        buffer.append(getValor());
        buffer.append(']');
        return buffer.toString();
    }
}
