package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "RevCatalogoGrupo.findAll", query = "select o from RevCatalogoGrupo o") })
@Table(name = "REV_CATALOGO_GRUPO")
@IdClass(RevCatalogoGrupoPK.class)
public class RevCatalogoGrupo implements Serializable {
    @Id
    @Column(name = "ID_CATALOGO", nullable = false, insertable = false, updatable = false)
    private BigDecimal idCatalogo;
    @Id
    @Column(name = "ID_GRUPO_ACCESO", nullable = false, length = 128, insertable = false, updatable = false)
    private String idGrupoAcceso;
    @ManyToOne
    @JoinColumn(name = "ID_CATALOGO")
    private RevCatalogo revCatalogo;
    @ManyToOne
    @JoinColumn(name = "ID_GRUPO_ACCESO")
    private RevGrupo revGrupo1;

    public RevCatalogoGrupo() {
    }

    public RevCatalogoGrupo(RevCatalogo revCatalogo, RevGrupo revGrupo1) {
        this.revCatalogo = revCatalogo;
        this.revGrupo1 = revGrupo1;
    }


    public BigDecimal getIdCatalogo() {
        return idCatalogo;
    }

    public void setIdCatalogo(BigDecimal idCatalogo) {
        this.idCatalogo = idCatalogo;
    }

    public String getIdGrupoAcceso() {
        return idGrupoAcceso;
    }

    public void setIdGrupoAcceso(String idGrupoAcceso) {
        this.idGrupoAcceso = idGrupoAcceso;
    }

    public RevCatalogo getRevCatalogo() {
        return revCatalogo;
    }

    public void setRevCatalogo(RevCatalogo revCatalogo) {
        this.revCatalogo = revCatalogo;
        if (revCatalogo != null) {
            this.idCatalogo = revCatalogo.getIdCatalogo();
        }
    }

    public RevGrupo getRevGrupo1() {
        return revGrupo1;
    }

    public void setRevGrupo1(RevGrupo revGrupo1) {
        this.revGrupo1 = revGrupo1;
        if (revGrupo1 != null) {
            this.idGrupoAcceso = revGrupo1.getIdGrupoAcceso();
        }
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idCatalogo=");
        buffer.append(getIdCatalogo());
        buffer.append(',');
        buffer.append("idGrupoAcceso=");
        buffer.append(getIdGrupoAcceso());
        buffer.append(',');
        buffer.append(']');
        return buffer.toString();
    }
}
