package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

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
@NamedQueries( { @NamedQuery(name = "RevSubCelda.findAll", query = "select o from RevSubCelda o") })
@Table(name = "REV_SUB_CELDA")
@IdClass(RevSubCeldaPK.class)
public class RevSubCelda implements Serializable {
    @Column(name = "CHILD_HORIZONTAL")
    private BigDecimal childHorizontal;
    @Column(name = "CHILD_VERTICAL")
    private BigDecimal childVertical;
    @Column(length = 256)
    private String formula;
    @Id
    @Column(name = "ID_GRILLA", nullable = false, insertable = false, updatable = false)
    private BigDecimal idGrilla;
    @Id
    @Column(name = "ID_SUB_COLUMNA", nullable = false, insertable = false, updatable = false)
    private BigDecimal idSubColumna;
    @Id
    @Column(name = "ID_SUB_FILA", nullable = false)
    private BigDecimal idSubFila;
    @Id
    @Column(name = "ID_SUB_GRILLA", nullable = false, insertable = false, updatable = false)
    private BigDecimal idSubGrilla;
    @Column(name = "PARENT_HORIZONTAL")
    private BigDecimal parentHorizontal;
    @Column(name = "PARENT_VERTICAL")
    private BigDecimal parentVertical;
    @Column(length = 2048)
    private String valor;
    @ManyToOne
    @JoinColumn(name = "ID_TIPO_DATO")
    private RevTipoDato revTipoDato1;
    @ManyToOne
    @JoinColumn(name = "ID_TIPO_CELDA")
    private RevTipoCelda revTipoCelda;
    @ManyToOne
    @JoinColumns({
    @JoinColumn(name = "ID_SUB_COLUMNA", referencedColumnName = "ID_SUB_COLUMNA"),
    @JoinColumn(name = "ID_SUB_GRILLA", referencedColumnName = "ID_SUB_GRILLA"),
    @JoinColumn(name = "ID_GRILLA", referencedColumnName = "ID_GRILLA")
    })
    private RevSubColumna revSubColumna5;

    public RevSubCelda() {
    }

    public RevSubCelda(BigDecimal childHorizontal, BigDecimal childVertical, String formula,
                       RevSubColumna revSubColumna5, BigDecimal idSubFila, RevTipoCelda revTipoCelda,
                       RevTipoDato revTipoDato1, BigDecimal parentHorizontal, BigDecimal parentVertical, String valor) {
        this.childHorizontal = childHorizontal;
        this.childVertical = childVertical;
        this.formula = formula;
        this.revSubColumna5 = revSubColumna5;
        this.idSubFila = idSubFila;
        this.revTipoCelda = revTipoCelda;
        this.revTipoDato1 = revTipoDato1;
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

    public BigDecimal getIdGrilla() {
        return idGrilla;
    }

    public void setIdGrilla(BigDecimal idGrilla) {
        this.idGrilla = idGrilla;
    }

    public BigDecimal getIdSubColumna() {
        return idSubColumna;
    }

    public void setIdSubColumna(BigDecimal idSubColumna) {
        this.idSubColumna = idSubColumna;
    }

    public BigDecimal getIdSubFila() {
        return idSubFila;
    }

    public void setIdSubFila(BigDecimal idSubFila) {
        this.idSubFila = idSubFila;
    }

    public BigDecimal getIdSubGrilla() {
        return idSubGrilla;
    }

    public void setIdSubGrilla(BigDecimal idSubGrilla) {
        this.idSubGrilla = idSubGrilla;
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

    public RevTipoDato getRevTipoDato1() {
        return revTipoDato1;
    }

    public void setRevTipoDato1(RevTipoDato revTipoDato1) {
        this.revTipoDato1 = revTipoDato1;
    }

    public RevTipoCelda getRevTipoCelda() {
        return revTipoCelda;
    }

    public void setRevTipoCelda(RevTipoCelda revTipoCelda) {
        this.revTipoCelda = revTipoCelda;
    }

    public RevSubColumna getRevSubColumna5() {
        return revSubColumna5;
    }

    public void setRevSubColumna5(RevSubColumna revSubColumna5) {
        this.revSubColumna5 = revSubColumna5;
        if (revSubColumna5 != null) {
            this.idGrilla = revSubColumna5.getIdGrilla();
            this.idSubGrilla = revSubColumna5.getIdSubGrilla();
            this.idSubColumna = revSubColumna5.getIdSubColumna();
        }
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
        buffer.append("idGrilla=");
        buffer.append(getIdGrilla());
        buffer.append(',');
        buffer.append("idSubColumna=");
        buffer.append(getIdSubColumna());
        buffer.append(',');
        buffer.append("idSubFila=");
        buffer.append(getIdSubFila());
        buffer.append(',');
        buffer.append("idSubGrilla=");
        buffer.append(getIdSubGrilla());
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
