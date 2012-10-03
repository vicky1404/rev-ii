package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

public class RevCatalogoGrupoPK implements Serializable {
    private BigDecimal idCatalogo;
    private String idGrupoAcceso;

    public RevCatalogoGrupoPK() {
    }

    public RevCatalogoGrupoPK(BigDecimal idCatalogo, String idGrupoAcceso) {
        this.idCatalogo = idCatalogo;
        this.idGrupoAcceso = idGrupoAcceso;
    }

    public boolean equals(Object other) {
        if (other instanceof RevCatalogoGrupoPK) {
            final RevCatalogoGrupoPK otherRevCatalogoGrupoPK = (RevCatalogoGrupoPK)other;
            final boolean areEqual =
                (otherRevCatalogoGrupoPK.idCatalogo.equals(idCatalogo) && otherRevCatalogoGrupoPK.idGrupoAcceso.equals(idGrupoAcceso));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
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
}
