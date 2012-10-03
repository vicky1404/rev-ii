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
@NamedQueries( { @NamedQuery(name = "RevAgrupacionColumna.findAll", query = "select o from RevAgrupacionColumna o") })
@Table(name = "REV_AGRUPACION_COLUMNA")
@IdClass(RevAgrupacionColumnaPK.class)
public class RevAgrupacionColumna implements Serializable {
    private BigDecimal ancho;
    private BigDecimal grupo;
    @Id
    @Column(name = "ID_COLUMNA", nullable = false, insertable = false, updatable = false)
    private BigDecimal idColumna;
    @Id
    @Column(name = "ID_GRILLA", nullable = false, insertable = false, updatable = false)
    private BigDecimal idGrilla;
    @Id
    @Column(name = "ID_NIVEL", nullable = false)
    private BigDecimal idNivel;
    @Column(length = 256)
    private String titulo;
    @ManyToOne
    @JoinColumns({
    @JoinColumn(name = "ID_COLUMNA", referencedColumnName = "ID_COLUMNA"),
    @JoinColumn(name = "ID_GRILLA", referencedColumnName = "ID_GRILLA")
    })
    private RevColumna revColumna3;

    public RevAgrupacionColumna() {
    }

    public RevAgrupacionColumna(BigDecimal ancho, BigDecimal grupo, RevColumna revColumna3, BigDecimal idNivel, String titulo) {
        this.ancho = ancho;
        this.grupo = grupo;
        this.revColumna3 = revColumna3;
        this.idNivel = idNivel;
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

    public BigDecimal getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(BigDecimal idNivel) {
        this.idNivel = idNivel;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public RevColumna getRevColumna3() {
        return revColumna3;
    }

    public void setRevColumna3(RevColumna revColumna3) {
        this.revColumna3 = revColumna3;
        if (revColumna3 != null) {
            this.idGrilla = revColumna3.getIdGrilla();
            this.idColumna = revColumna3.getIdColumna();
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
        buffer.append("titulo=");
        buffer.append(getTitulo());
        buffer.append(']');
        return buffer.toString();
    }
}
