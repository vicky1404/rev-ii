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
@NamedQueries( { @NamedQuery(name = "RevSubGrilla.findAll", query = "select o from RevSubGrilla o") })
@Table(name = "REV_SUB_GRILLA")
@IdClass(RevSubGrillaPK.class)
public class RevSubGrilla implements Serializable {
    private BigDecimal agrupador;
    @Id
    @Column(name = "ID_GRILLA", nullable = false, insertable = false, updatable = false)
    private BigDecimal idGrilla;
    @Column(name = "ID_GRUPO_ACCESO", length = 128)
    private String idGrupoAcceso;
    @Id
    @Column(name = "ID_SUB_GRILLA", nullable = false)
    private BigDecimal idSubGrilla;
    @Column(name = "TIPO_FORMULA")
    private BigDecimal tipoFormula;
    @Column(length = 256)
    private String titulo;
    @ManyToOne
    @JoinColumn(name = "ID_GRILLA")
    private RevGrilla revGrilla1;
    @OneToMany(mappedBy = "revSubGrilla1")
    private List<RevSubColumna> revSubColumnaList1;

    public RevSubGrilla() {
    }

    public RevSubGrilla(BigDecimal agrupador, RevGrilla revGrilla1, String idGrupoAcceso, BigDecimal idSubGrilla,
                        BigDecimal tipoFormula, String titulo) {
        this.agrupador = agrupador;
        this.revGrilla1 = revGrilla1;
        this.idGrupoAcceso = idGrupoAcceso;
        this.idSubGrilla = idSubGrilla;
        this.tipoFormula = tipoFormula;
        this.titulo = titulo;
    }


    public BigDecimal getAgrupador() {
        return agrupador;
    }

    public void setAgrupador(BigDecimal agrupador) {
        this.agrupador = agrupador;
    }

    public BigDecimal getIdGrilla() {
        return idGrilla;
    }

    public void setIdGrilla(BigDecimal idGrilla) {
        this.idGrilla = idGrilla;
    }

    public String getIdGrupoAcceso() {
        return idGrupoAcceso;
    }

    public void setIdGrupoAcceso(String idGrupoAcceso) {
        this.idGrupoAcceso = idGrupoAcceso;
    }

    public BigDecimal getIdSubGrilla() {
        return idSubGrilla;
    }

    public void setIdSubGrilla(BigDecimal idSubGrilla) {
        this.idSubGrilla = idSubGrilla;
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

    public RevGrilla getRevGrilla1() {
        return revGrilla1;
    }

    public void setRevGrilla1(RevGrilla revGrilla1) {
        this.revGrilla1 = revGrilla1;
        if (revGrilla1 != null) {
            this.idGrilla = revGrilla1.getIdGrilla();
        }
    }

    public List<RevSubColumna> getRevSubColumnaList1() {
        return revSubColumnaList1;
    }

    public void setRevSubColumnaList1(List<RevSubColumna> revSubColumnaList1) {
        this.revSubColumnaList1 = revSubColumnaList1;
    }

    public RevSubColumna addRevSubColumna(RevSubColumna revSubColumna) {
        getRevSubColumnaList1().add(revSubColumna);
        revSubColumna.setRevSubGrilla1(this);
        return revSubColumna;
    }

    public RevSubColumna removeRevSubColumna(RevSubColumna revSubColumna) {
        getRevSubColumnaList1().remove(revSubColumna);
        revSubColumna.setRevSubGrilla1(null);
        return revSubColumna;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("agrupador=");
        buffer.append(getAgrupador());
        buffer.append(',');
        buffer.append("idGrilla=");
        buffer.append(getIdGrilla());
        buffer.append(',');
        buffer.append("idGrupoAcceso=");
        buffer.append(getIdGrupoAcceso());
        buffer.append(',');
        buffer.append("idSubGrilla=");
        buffer.append(getIdSubGrilla());
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
