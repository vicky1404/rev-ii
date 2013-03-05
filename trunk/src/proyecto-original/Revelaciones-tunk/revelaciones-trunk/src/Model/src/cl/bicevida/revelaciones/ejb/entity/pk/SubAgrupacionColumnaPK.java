package cl.bicevida.revelaciones.ejb.entity.pk;

import java.io.Serializable;


public class SubAgrupacionColumnaPK implements Serializable {
    
    private Long idGrilla;
    private Long idNivel;
    private Long idSubColumna;
    private Long idSubGrilla;

    public SubAgrupacionColumnaPK() {
    }

    public SubAgrupacionColumnaPK(Long idGrilla, Long idNivel, Long idSubColumna,
                                     Long idSubGrilla) {
        this.idGrilla = idGrilla;
        this.idNivel = idNivel;
        this.idSubColumna = idSubColumna;
        this.idSubGrilla = idSubGrilla;
    }

    public boolean equals(Object other) {
        if (other instanceof SubAgrupacionColumnaPK) {
            final SubAgrupacionColumnaPK otherRevSubAgrupacionColumnaPK = (SubAgrupacionColumnaPK)other;
            final boolean areEqual =
                (otherRevSubAgrupacionColumnaPK.idGrilla.equals(idGrilla) && otherRevSubAgrupacionColumnaPK.idNivel.equals(idNivel) &&
                 otherRevSubAgrupacionColumnaPK.idSubColumna.equals(idSubColumna) &&
                 otherRevSubAgrupacionColumnaPK.idSubGrilla.equals(idSubGrilla));
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

    public Long getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(Long idNivel) {
        this.idNivel = idNivel;
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
