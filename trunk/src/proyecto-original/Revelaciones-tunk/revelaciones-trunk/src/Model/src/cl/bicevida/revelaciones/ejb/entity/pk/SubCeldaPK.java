package cl.bicevida.revelaciones.ejb.entity.pk;

import java.io.Serializable;

public class SubCeldaPK implements Serializable {
    private Long idGrilla;
    private Long idSubColumna;
    private Long idSubFila;
    private Long idSubGrilla;

    public SubCeldaPK() {
    }

    public SubCeldaPK(Long idGrilla, Long idSubColumna, Long idSubFila, Long idSubGrilla) {
        this.idGrilla = idGrilla;
        this.idSubColumna = idSubColumna;
        this.idSubFila = idSubFila;
        this.idSubGrilla = idSubGrilla;
    }

    public boolean equals(Object other) {
        if (other instanceof SubCeldaPK) {
            final SubCeldaPK otherRevSubCeldaPK = (SubCeldaPK)other;
            final boolean areEqual =
                (otherRevSubCeldaPK.idGrilla.equals(idGrilla) && otherRevSubCeldaPK.idSubColumna.equals(idSubColumna) &&
                 otherRevSubCeldaPK.idSubFila.equals(idSubFila) && otherRevSubCeldaPK.idSubGrilla.equals(idSubGrilla));
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

    public Long getIdSubFila() {
        return idSubFila;
    }

    public void setIdSubFila(Long idSubFila) {
        this.idSubFila = idSubFila;
    }

    public Long getIdSubGrilla() {
        return idSubGrilla;
    }

    public void setIdSubGrilla(Long idSubGrilla) {
        this.idSubGrilla = idSubGrilla;
    }
}
