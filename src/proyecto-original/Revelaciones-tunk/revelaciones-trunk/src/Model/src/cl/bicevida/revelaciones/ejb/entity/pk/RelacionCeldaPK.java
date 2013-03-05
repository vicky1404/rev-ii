package cl.bicevida.revelaciones.ejb.entity.pk;

import java.io.Serializable;


public class RelacionCeldaPK implements Serializable {
    
    private Long idColumna;
    private Long idColumnaRel;
    private Long idFila;
    private Long idFilaRel;
    private Long idGrilla;
    private Long idGrillaRel;
    private Long idPeriodo;

    public RelacionCeldaPK() {
    }

    public RelacionCeldaPK(Long idColumna, Long idColumnaRel, Long idFila, Long idFilaRel,
                              Long idGrilla, Long idGrillaRel, Long idPeriodo) {
        this.idColumna = idColumna;
        this.idColumnaRel = idColumnaRel;
        this.idFila = idFila;
        this.idFilaRel = idFilaRel;
        this.idGrilla = idGrilla;
        this.idGrillaRel = idGrillaRel;
        this.idPeriodo = idPeriodo;
    }

    public boolean equals(Object other) {
        if (other instanceof RelacionCeldaPK) {
            final RelacionCeldaPK otherRevRelacionCeldaPK = (RelacionCeldaPK)other;
            final boolean areEqual =
                (otherRevRelacionCeldaPK.idColumna.equals(idColumna) && otherRevRelacionCeldaPK.idColumnaRel.equals(idColumnaRel) &&
                 otherRevRelacionCeldaPK.idFila.equals(idFila) &&
                 otherRevRelacionCeldaPK.idFilaRel.equals(idFilaRel) &&
                 otherRevRelacionCeldaPK.idGrilla.equals(idGrilla) &&
                 otherRevRelacionCeldaPK.idGrillaRel.equals(idGrillaRel) &&
                 otherRevRelacionCeldaPK.idPeriodo.equals(idPeriodo));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public Long getIdColumna() {
        return idColumna;
    }

    public void setIdColumna(Long idColumna) {
        this.idColumna = idColumna;
    }

    public Long getIdColumnaRel() {
        return idColumnaRel;
    }

    public void setIdColumnaRel(Long idColumnaRel) {
        this.idColumnaRel = idColumnaRel;
    }

    public Long getIdFila() {
        return idFila;
    }

    public void setIdFila(Long idFila) {
        this.idFila = idFila;
    }

    public Long getIdFilaRel() {
        return idFilaRel;
    }

    public void setIdFilaRel(Long idFilaRel) {
        this.idFilaRel = idFilaRel;
    }

    public Long getIdGrilla() {
        return idGrilla;
    }

    public void setIdGrilla(Long idGrilla) {
        this.idGrilla = idGrilla;
    }

    public Long getIdGrillaRel() {
        return idGrillaRel;
    }

    public void setIdGrillaRel(Long idGrillaRel) {
        this.idGrillaRel = idGrillaRel;
    }

    public Long getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(Long idPeriodo) {
        this.idPeriodo = idPeriodo;
    }
}
