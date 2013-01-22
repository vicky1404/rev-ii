package cl.mdr.ifrs.ejb.entity.pk;

import java.io.Serializable;

import java.lang.Long;


public class AgrupacionColumnaPK implements Serializable {
    private Long idColumna;
    private Long idGrilla;
    private Long idNivel;

    public AgrupacionColumnaPK() {
    }

    public AgrupacionColumnaPK(Long idColumna, Long idGrilla, Long idGrupo) {
        this.idColumna = idColumna;
        this.idGrilla = idGrilla;
        this.idNivel = idGrupo;
    }

    public boolean equals(Object other) {
        if (other instanceof AgrupacionColumnaPK) {
            final AgrupacionColumnaPK otherAgrupacionColumnaPK = (AgrupacionColumnaPK)other;
            final boolean areEqual =
                (otherAgrupacionColumnaPK.idColumna.equals(idColumna) && otherAgrupacionColumnaPK.idGrilla.equals(idGrilla) &&
                 otherAgrupacionColumnaPK.idNivel.equals(idNivel));
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

    public Long getIdGrilla() {
        return idGrilla;
    }

    public void setIdGrilla(Long idGrilla) {
        this.idGrilla = idGrilla;
    }

    public Long getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(Long idGrupo) {
        this.idNivel = idGrupo;
    }
}
