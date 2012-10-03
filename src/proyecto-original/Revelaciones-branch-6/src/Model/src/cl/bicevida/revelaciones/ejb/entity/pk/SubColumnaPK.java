package cl.bicevida.revelaciones.ejb.entity.pk;

import java.io.Serializable;

public class SubColumnaPK implements Serializable {
    private Long idGrilla;
    private Long idSubColumna;
    private Long idSubGrilla;

    public SubColumnaPK() {
    }

    public SubColumnaPK(Long idGrilla, Long idSubColumna, Long idSubGrilla) {
        this.idGrilla = idGrilla;
        this.idSubColumna = idSubColumna;
        this.idSubGrilla = idSubGrilla;
    }

    public boolean equals(Object other) {
        if (other instanceof SubColumnaPK) {
            final SubColumnaPK otherRevSubColumnaPK = (SubColumnaPK)other;
            final boolean areEqual =
                (otherRevSubColumnaPK.idGrilla.equals(idGrilla) && otherRevSubColumnaPK.idSubColumna.equals(idSubColumna) &&
                 otherRevSubColumnaPK.idSubGrilla.equals(idSubGrilla));
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

    public Long getIdSubColumna() {
        return idSubColumna;
    }

    public void setIdSubColumna(Long idSubColumna) {
        this.idSubColumna = idSubColumna;
    }

    public Long getIdSubGrilla() {
        return idSubGrilla;
    }

    public void setIdSubGrilla(Long idSubGrilla) {
        this.idSubGrilla = idSubGrilla;
    }
}
