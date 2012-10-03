package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "RevColumna.findAll", query = "select o from RevColumna o") })
@Table(name = "REV_COLUMNA")
@IdClass(RevColumnaPK.class)
public class RevColumna implements Serializable {
    private BigDecimal ancho;
    @Id
    @Column(name = "ID_COLUMNA", nullable = false)
    private BigDecimal idColumna;
    @Id
    @Column(name = "ID_GRILLA", nullable = false, insertable = false, updatable = false)
    private BigDecimal idGrilla;
    @Column(nullable = false)
    private BigDecimal orden;
    @Column(name = "ROW_HEADER")
    private BigDecimal rowHeader;
    @Column(name = "TITULO_COLUMNA", nullable = false, length = 128)
    private String tituloColumna;
    @OneToMany(mappedBy = "revColumna1")
    private List<RevCelda> revCeldaList1;
    @ManyToOne
    @JoinColumn(name = "ID_GRILLA")
    private RevGrilla revGrilla;
    @OneToMany(mappedBy = "revColumna3")
    private List<RevAgrupacionColumna> revAgrupacionColumnaList1;

    public RevColumna() {
    }

    public RevColumna(BigDecimal ancho, BigDecimal idColumna, RevGrilla revGrilla, BigDecimal orden,
                      BigDecimal rowHeader, String tituloColumna) {
        this.ancho = ancho;
        this.idColumna = idColumna;
        this.revGrilla = revGrilla;
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

    public BigDecimal getIdColumna() {
        return idColumna;
    }

    public void setIdColumna(BigDecimal idColumna) {
        this.idColumna = idColumna;
    }

    public BigDecimal getIdGrilla() {
        return idGrilla;
    }

    public void setIdGrilla(BigDecimal idGrilla) {
        this.idGrilla = idGrilla;
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

    public List<RevCelda> getRevCeldaList1() {
        return revCeldaList1;
    }

    public void setRevCeldaList1(List<RevCelda> revCeldaList1) {
        this.revCeldaList1 = revCeldaList1;
    }

    public RevCelda addRevCelda(RevCelda revCelda) {
        getRevCeldaList1().add(revCelda);
        revCelda.setRevColumna1(this);
        return revCelda;
    }

    public RevCelda removeRevCelda(RevCelda revCelda) {
        getRevCeldaList1().remove(revCelda);
        revCelda.setRevColumna1(null);
        return revCelda;
    }

    public RevGrilla getRevGrilla() {
        return revGrilla;
    }

    public void setRevGrilla(RevGrilla revGrilla) {
        this.revGrilla = revGrilla;
        if (revGrilla != null) {
            this.idGrilla = revGrilla.getIdGrilla();
        }
    }

    public List<RevAgrupacionColumna> getRevAgrupacionColumnaList1() {
        return revAgrupacionColumnaList1;
    }

    public void setRevAgrupacionColumnaList1(List<RevAgrupacionColumna> revAgrupacionColumnaList1) {
        this.revAgrupacionColumnaList1 = revAgrupacionColumnaList1;
    }

    public RevAgrupacionColumna addRevAgrupacionColumna(RevAgrupacionColumna revAgrupacionColumna) {
        getRevAgrupacionColumnaList1().add(revAgrupacionColumna);
        revAgrupacionColumna.setRevColumna3(this);
        return revAgrupacionColumna;
    }

    public RevAgrupacionColumna removeRevAgrupacionColumna(RevAgrupacionColumna revAgrupacionColumna) {
        getRevAgrupacionColumnaList1().remove(revAgrupacionColumna);
        revAgrupacionColumna.setRevColumna3(null);
        return revAgrupacionColumna;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("ancho=");
        buffer.append(getAncho());
        buffer.append(',');
        buffer.append("idColumna=");
        buffer.append(getIdColumna());
        buffer.append(',');
        buffer.append("idGrilla=");
        buffer.append(getIdGrilla());
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
