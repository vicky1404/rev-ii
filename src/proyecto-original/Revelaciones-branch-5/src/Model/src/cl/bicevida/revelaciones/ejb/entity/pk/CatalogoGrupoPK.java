package cl.bicevida.revelaciones.ejb.entity.pk;

import java.io.Serializable;


public class CatalogoGrupoPK implements Serializable {

    @SuppressWarnings("compatibility:3772586196785191487")
    private static final long serialVersionUID = -724729999541823154L;
    private Long idCatalogo;
    private String idGrupoAcceso;

    public CatalogoGrupoPK() {
    }

    public CatalogoGrupoPK(Long idCatalogo, String idGrupoAcceso) {
        this.idCatalogo = idCatalogo;
        this.idGrupoAcceso = idGrupoAcceso;
    }

    public boolean equals(Object other) {
        if (other instanceof CatalogoGrupoPK) {
            final CatalogoGrupoPK otherCatalogoGrupoPK = (CatalogoGrupoPK)other;
            final boolean areEqual =
                (otherCatalogoGrupoPK.idCatalogo.equals(idCatalogo) && otherCatalogoGrupoPK.idGrupoAcceso.equals(idGrupoAcceso));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public Long getIdCatalogo() {
        return idCatalogo;
    }

    public void setIdCatalogo(Long idCatalogo) {
        this.idCatalogo = idCatalogo;
    }

    public String getIdGrupoAcceso() {
        return idGrupoAcceso;
    }

    public void setIdGrupoAcceso(String idGrupoAcceso) {
        this.idGrupoAcceso = idGrupoAcceso;
    }
}
