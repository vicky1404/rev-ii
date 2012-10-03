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
@NamedQueries( { @NamedQuery(name = "RevSubColumna.findAll", query = "select o from RevSubColumna o") })
@Table(name = "REV_SUB_COLUMNA")
@IdClass(RevSubColumnaPK.class)
public class RevSubColumna implements Serializable {
    private BigDecimal ancho;
    @Id
    @Column(name = "ID_GRILLA", nullable = false, insertable = false, updatable = false)
    private BigDecimal idGrilla;
    @Id
    @Column(name = "ID_SUB_COLUMNA", nullable = false)
    private BigDecimal idSubColumna;
    @Id
    @Column(name = "ID_SUB_GRILLA", nullable = false, insertable = false, updatable = false)
    private BigDecimal idSubGrilla;
    private BigDecimal orden;
    @Column(name = "ROW_HEADER")
    private BigDecimal rowHeader;
    @Column(name = "TITULO_COLUMNA", length = 128)
    private String tituloColumna;
    @ManyToOne
    @JoinColumns({
    @JoinColumn(name = "ID_SUB_GRILLA", referencedColumnName = "ID_SUB_GRILLA"),
    @JoinColumn(name = "ID_GRILLA", referencedColumnName = "ID_GRILLA")
    })
    private RevSubGrilla revSubGrilla1;
    @OneToMany(mappedBy = "revSubColumna2")
    private List<RevSubAgrupacionColumna> revSubAgrupacionColumnaList2;
    @OneToMany(mappedBy = "revSubColumna5")
    private List<RevSubCelda> revSubCeldaList4;

    public RevSubColumna() {
    }

    public RevSubColumna(BigDecimal ancho, BigDecimal idSubColumna, RevSubGrilla revSubGrilla1,
                         BigDecimal orden, BigDecimal rowHeader, String tituloColumna) {
        this.ancho = ancho;
        this.idSubColumna = idSubColumna;
        this.revSubGrilla1 = revSubGrilla1;
        this.orden = orden;
        this.rowHeader = rowHeader;
        this.tituloColumna = tituloColumna;
    }


    public BigDecimal getAncho() {
        return ancho;
    }

    public void setAncho(BigDecimal ancho) {
        this.ancho = ancho;
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

    public BigDecimal getIdSubGrilla() {
        return idSubGrilla;
    }

    public void setIdSubGrilla(BigDecimal idSubGrilla) {
        this.idSubGrilla = idSubGrilla;
    }

    public BigDecimal getOrden() {
        return orden;
    }

    public void setOrden(BigDecimal orden) {
        this.orden = orden;
    }

    public BigDecimal getRowHeader() {
        return rowHeader;
    }

    public void setRowHeader(BigDecimal rowHeader) {
        this.rowHeader = rowHeader;
    }

    public String getTituloColumna() {
        return tituloColumna;
    }

    public void setTituloColumna(String tituloColumna) {
        this.tituloColumna = tituloColumna;
    }

    public RevSubGrilla getRevSubGrilla1() {
        return revSubGrilla1;
    }

    public void setRevSubGrilla1(RevSubGrilla revSubGrilla1) {
        this.revSubGrilla1 = revSubGrilla1;
        if (revSubGrilla1 != null) {
            this.idGrilla = revSubGrilla1.getIdGrilla();
            this.idSubGrilla = revSubGrilla1.getIdSubGrilla();
        }
    }

    public List<RevSubAgrupacionColumna> getRevSubAgrupacionColumnaList2() {
        return revSubAgrupacionColumnaList2;
    }

    public void setRevSubAgrupacionColumnaList2(List<RevSubAgrupacionColumna> revSubAgrupacionColumnaList2) {
        this.revSubAgrupacionColumnaList2 = revSubAgrupacionColumnaList2;
    }

    public RevSubAgrupacionColumna addRevSubAgrupacionColumna(RevSubAgrupacionColumna revSubAgrupacionColumna) {
        getRevSubAgrupacionColumnaList2().add(revSubAgrupacionColumna);
        revSubAgrupacionColumna.setRevSubColumna2(this);
        return revSubAgrupacionColumna;
    }

    public RevSubAgrupacionColumna removeRevSubAgrupacionColumna(RevSubAgrupacionColumna revSubAgrupacionColumna) {
        getRevSubAgrupacionColumnaList2().remove(revSubAgrupacionColumna);
        revSubAgrupacionColumna.setRevSubColumna2(null);
        return revSubAgrupacionColumna;
    }

    public List<RevSubCelda> getRevSubCeldaList4() {
        return revSubCeldaList4;
    }

    public void setRevSubCeldaList4(List<RevSubCelda> revSubCeldaList4) {
        this.revSubCeldaList4 = revSubCeldaList4;
    }

    public RevSubCelda addRevSubCelda(RevSubCelda revSubCelda) {
        getRevSubCeldaList4().add(revSubCelda);
        revSubCelda.setRevSubColumna5(this);
        return revSubCelda;
    }

    public RevSubCelda removeRevSubCelda(RevSubCelda revSubCelda) {
        getRevSubCeldaList4().remove(revSubCelda);
        revSubCelda.setRevSubColumna5(null);
        return revSubCelda;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("ancho=");
        buffer.append(getAncho());
        buffer.append(',');
        buffer.append("idGrilla=");
        buffer.append(getIdGrilla());
        buffer.append(',');
        buffer.append("idSubColumna=");
        buffer.append(getIdSubColumna());
        buffer.append(',');
        buffer.append("idSubGrilla=");
        buffer.append(getIdSubGrilla());
        buffer.append(',');
        buffer.append("orden=");
        buffer.append(getOrden());
        buffer.append(',');
        buffer.append("rowHeader=");
        buffer.append(getRowHeader());
        buffer.append(',');
        buffer.append("tituloColumna=");
        buffer.append(getTituloColumna());
        buffer.append(']');
        return buffer.toString();
    }
}
