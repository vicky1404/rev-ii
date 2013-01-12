package cl.mdr.ifrs.ejb.entity.pk;

import java.io.Serializable;

public class MenuGrupoPK implements Serializable {
	private static final long serialVersionUID = 5851436159656134922L;
	
	private String idGrupoAcceso;
    private Long idMenu;

    public MenuGrupoPK() {
    }

    public MenuGrupoPK(String idGrupoAcceso, Long idMenu) {
        this.idGrupoAcceso = idGrupoAcceso;
        this.idMenu = idMenu;
    }

    public boolean equals(Object other) {
        if (other instanceof MenuGrupoPK) {
            final MenuGrupoPK otherMenuGrupoPK = (MenuGrupoPK)other;
            final boolean areEqual =
                (otherMenuGrupoPK.idGrupoAcceso.equals(idGrupoAcceso) && otherMenuGrupoPK.idMenu.equals(idMenu));
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

    public Long getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(Long idMenu) {
        this.idMenu = idMenu;
    }
}
