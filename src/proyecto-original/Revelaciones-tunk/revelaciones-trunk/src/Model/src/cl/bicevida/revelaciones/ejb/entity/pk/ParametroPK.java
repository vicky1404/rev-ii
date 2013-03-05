package cl.bicevida.revelaciones.ejb.entity.pk;

import java.io.Serializable;


public class ParametroPK implements Serializable {
    private String idParametro;
    private Long idTipoParametro;

    public ParametroPK() {
    }

    public ParametroPK(String idParametro, Long idTipoParametro) {
        this.idParametro = idParametro;
        this.idTipoParametro = idTipoParametro;
    }

    public boolean equals(Object other) {
        if (other instanceof ParametroPK) {
            final ParametroPK otherParametroPK = (ParametroPK)other;
            final boolean areEqual =
                (otherParametroPK.idParametro.equals(idParametro) && otherParametroPK.idTipoParametro.equals(idTipoParametro));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String getIdParametro() {
        return idParametro;
    }

    public void setIdParametro(String idParametro) {
        this.idParametro = idParametro;
    }

    public Long getIdTipoParametro() {
        return idTipoParametro;
    }

    public void setIdTipoParametro(Long idTipoParametro) {
        this.idTipoParametro = idTipoParametro;
    }
}
