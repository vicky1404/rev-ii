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
@NamedQueries( { @NamedQuery(name = "RevSubAgrupacionColumna.findAll",
                             query = "select o from RevSubAgrupacionColumna o") })
@Table(name = "REV_SUB_AGRUPACION_COLUMNA")
@IdClass(RevSubAgrupacionColumnaPK.class)
public class RevSubAgrupacionColumna implements Serializable {
    private BigDecimal ancho;
    private BigDecimal grupo;
    @Id
    @Column(name = "ID_GRILLA", nullable = false, insertable = false, updatable = false)
    private BigDecimal idGrilla;
    @Id
    @Column(name = "ID_NIVEL", nullable = false)
    private BigDecimal idNivel;
    @Id
    @Column(name = "ID_SUB_COLUMNA", nullable = false, insertable = false, updatable = false)
    private BigDecimal idSubColumna;
    @Id
    @Column(name = "ID_SUB_GRILLA", nullable = false, insertable = false, updatable = false)
    private BigDecimal idSubGrilla;
    @Column(length = 256)
    private String titulo;
    @ManyToOne
    @JoinColumns({
    @JoinColumn(name = "ID_SUB_COLUMNA", referencedColumnName = "ID_SUB_COLUMNA"),
    @JoinColumn(name = "ID_SUB_GRILLA", referencedColumnName = "ID_SUB_GRILLA"),
    @JoinColumn(name = "ID_GRILLA", referencedColumnName = "ID_GRILLA")
    })
    private RevSubColumna revSubColumna2;

    public RevSubAgrupacionColumna() {
    }

    public RevSubAgrupacionColumna(BigDecimal ancho, BigDecimal grupo, BigDecimal idNivel,
                                   RevSubColumna revSubColumna2, String titulo) {
        this.ancho = ancho;
        this.grupo = grupo;
        this.idNivel = idNivel;
        this.revSubColumna2 = revSubColumna2;
        this.titulo = titulo;
    }


    public BigDecimal getAncho() {
        return ancho;
    }

    public void setAncho(BigDecimal ancho) {
        this.ancho = ancho;
    }

    public BigDecimal getGrupo() {
        return grupo;
    }

    public void setGrupo(BigDecimal grupo) {
        this.grupo = grupo;
    }

    public BigDecimal getIdGrilla() {
        return idGrilla;
    }

    public void setIdGrilla(BigDecimal idGrilla) {
        this.idGrilla = idGrilla;
    }

    public BigDecimal getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(BigDecimal idNivel) {
        this.idNivel = idNivel;
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public RevSubColumna getRevSubColumna2() {
        return revSubColumna2;
    }

    public void setRevSubColumna2(RevSubColumna revSubColumna2) {
        this.revSubColumna2 = revSubColumna2;
        if (revSubColumna2 != null) {
            this.idGrilla = revSubColumna2.getIdGrilla();
            this.idSubGrilla = revSubColumna2.getIdSubGrilla();
            this.idSubColumna = revSubColumna2.getIdSubColumna();
        }
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("ancho=");
        buffer.append(getAncho());
        buffer.append(',');
        buffer.append("grupo=");
        buffer.append(getGrupo());
        buffer.append(',');
        buffer.append("idGrilla=");
        buffer.append(getIdGrilla());
        buffer.append(',');
        buffer.append("idNivel=");
        buffer.append(getIdNivel());
        buffer.append(',');
        buffer.append("idSubColumna=");
        buffer.append(getIdSubColumna());
        buffer.append(',');
        buffer.append("idSubGrilla=");
        buffer.append(getIdSubGrilla());
        buffer.append(',');
        buffer.append("titulo=");
        buffer.append(getTitulo());
        buffer.append(']');
        return buffer.toString();
    }
}
