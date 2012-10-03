package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "RevGrilla.findAll", query = "select o from RevGrilla o") })
@Table(name = "REV_GRILLA")
public class RevGrilla implements Serializable {
    @Id
    @Column(name = "ID_GRILLA", nullable = false, insertable = false, updatable = false)
    private BigDecimal idGrilla;
    @Column(name = "TIPO_FORMULA")
    private BigDecimal tipoFormula;
    @Column(length = 256)
    private String titulo;
    @ManyToOne
    @JoinColumn(name = "ID_GRILLA")
    private RevEstructura revEstructura2;
    @OneToMany(mappedBy = "revGrilla")
    private List<RevColumna> revColumnaList;
    @OneToMany(mappedBy = "revGrilla1")
    private List<RevSubGrilla> revSubGrillaList;

    public RevGrilla() {
    }

    public RevGrilla(RevEstructura revEstructura2, BigDecimal tipoFormula, String titulo) {
        this.revEstructura2 = revEstructura2;
        this.tipoFormula = tipoFormula;
        this.titulo = titulo;
    }


    public BigDecimal getIdGrilla() {
        return idGrilla;
    }

    public void setIdGrilla(BigDecimal idGrilla) {
        this.idGrilla = idGrilla;
    }

    public BigDecimal getTipoFormula() {
        return tipoFormula;
    }

    public void setTipoFormula(BigDecimal tipoFormula) {
        this.tipoFormula = tipoFormula;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public RevEstructura getRevEstructura2() {
        return revEstructura2;
    }

    public void setRevEstructura2(RevEstructura revEstructura2) {
        this.revEstructura2 = revEstructura2;
        if (revEstructura2 != null) {
            this.idGrilla = revEstructura2.getIdEstructura();
        }
    }

    public List<RevColumna> getRevColumnaList() {
        return revColumnaList;
    }

    public void setRevColumnaList(List<RevColumna> revColumnaList) {
        this.revColumnaList = revColumnaList;
    }

    public RevColumna addRevColumna(RevColumna revColumna) {
        getRevColumnaList().add(revColumna);
        revColumna.setRevGrilla(this);
        return revColumna;
    }

    public RevColumna removeRevColumna(RevColumna revColumna) {
        getRevColumnaList().remove(revColumna);
        revColumna.setRevGrilla(null);
        return revColumna;
    }

    public List<RevSubGrilla> getRevSubGrillaList() {
        return revSubGrillaList;
    }

    public void setRevSubGrillaList(List<RevSubGrilla> revSubGrillaList) {
        this.revSubGrillaList = revSubGrillaList;
    }

    public RevSubGrilla addRevSubGrilla(RevSubGrilla revSubGrilla) {
        getRevSubGrillaList().add(revSubGrilla);
        revSubGrilla.setRevGrilla1(this);
        return revSubGrilla;
    }

    public RevSubGrilla removeRevSubGrilla(RevSubGrilla revSubGrilla) {
        getRevSubGrillaList().remove(revSubGrilla);
        revSubGrilla.setRevGrilla1(null);
        return revSubGrilla;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idGrilla=");
        buffer.append(getIdGrilla());
        buffer.append(',');
        buffer.append("tipoFormula=");
        buffer.append(getTipoFormula());
        buffer.append(',');
        buffer.append("titulo=");
        buffer.append(getTitulo());
        buffer.append(']');
        return buffer.toString();
    }
}
