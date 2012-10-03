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
@NamedQueries( { @NamedQuery(name = "RevMenuGrupo.findAll", query = "select o from RevMenuGrupo o") })
@Table(name = "REV_MENU_GRUPO")
@IdClass(RevMenuGrupoPK.class)
public class RevMenuGrupo implements Serializable {
    @Id
    @Column(name = "ID_GRUPO_ACCESO", nullable = false, length = 128, insertable = false, updatable = false)
    private String idGrupoAcceso;
    @Id
    @Column(name = "ID_MENU", nullable = false, insertable = false, updatable = false)
    private BigDecimal idMenu;
    @ManyToOne
    @JoinColumn(name = "ID_GRUPO_ACCESO")
    private RevGrupo revGrupo;
    @ManyToOne
    @JoinColumn(name = "ID_MENU")
    private RevMenu revMenu;

    public RevMenuGrupo() {
    }

    public RevMenuGrupo(RevGrupo revGrupo, RevMenu revMenu) {
        this.revGrupo = revGrupo;
        this.revMenu = revMenu;
    }


    public String getIdGrupoAcceso() {
        return idGrupoAcceso;
    }

    public void setIdGrupoAcceso(String idGrupoAcceso) {
        this.idGrupoAcceso = idGrupoAcceso;
    }

    public BigDecimal getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(BigDecimal idMenu) {
        this.idMenu = idMenu;
    }

    public RevGrupo getRevGrupo() {
        return revGrupo;
    }

    public void setRevGrupo(RevGrupo revGrupo) {
        this.revGrupo = revGrupo;
        if (revGrupo != null) {
            this.idGrupoAcceso = revGrupo.getIdGrupoAcceso();
        }
    }

    public RevMenu getRevMenu() {
        return revMenu;
    }

    public void setRevMenu(RevMenu revMenu) {
        this.revMenu = revMenu;
        if (revMenu != null) {
            this.idMenu = revMenu.getIdMenu();
        }
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idGrupoAcceso=");
        buffer.append(getIdGrupoAcceso());
        buffer.append(',');
        buffer.append("idMenu=");
        buffer.append(getIdMenu());
        buffer.append(',');
        buffer.append(']');
        return buffer.toString();
    }
}
