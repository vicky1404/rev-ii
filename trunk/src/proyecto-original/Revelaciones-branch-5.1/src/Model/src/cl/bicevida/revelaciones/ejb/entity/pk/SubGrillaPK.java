package cl.bicevida.revelaciones.ejb.entity.pk;

import java.io.Serializable;


public class SubGrillaPK implements Serializable {
    private Long idGrilla;
    private Long idSubGrilla;

    public SubGrillaPK() {
    }

    public SubGrillaPK(Long idGrilla, Long idSubGrilla) {
        this.idGrilla = idGrilla;
        this.idSubGrilla = idSubGrilla;
    }

    public boolean equals(Object other) {
        if (other instanceof SubGrillaPK) {
            final SubGrillaPK otherRevSubGrillaPK = (SubGrillaPK)other;
            final boolean areEqual =
                (otherRevSubGrillaPK.idGrilla.equals(idGrilla) && otherRevSubGrillaPK.idSubGrilla.equals(idSubGrilla));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public Long getIdGrilla() {
        return idGrilla;
    }

    public void setIdGrilla(Long idGrilla) {
        this.idGrilla = idGrilla;
    }

    public Long getIdSubGrilla() {
        return idSubGrilla;
    }

    public void setIdSubGrilla(Long idSubGrilla) {
        this.idSubGrilla = idSubGrilla;
    }
}
