package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

public class RevMenuGrupoPK implements Serializable {
    private String idGrupoAcceso;
    private BigDecimal idMenu;

    public RevMenuGrupoPK() {
    }

    public RevMenuGrupoPK(String idGrupoAcceso, BigDecimal idMenu) {
        this.idGrupoAcceso = idGrupoAcceso;
        this.idMenu = idMenu;
    }

    public boolean equals(Object other) {
        if (other instanceof RevMenuGrupoPK) {
            final RevMenuGrupoPK otherRevMenuGrupoPK = (RevMenuGrupoPK)other;
            final boolean areEqual =
                (otherRevMenuGrupoPK.idGrupoAcceso.equals(idGrupoAcceso) && otherRevMenuGrupoPK.idMenu.equals(idMenu));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
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
}
