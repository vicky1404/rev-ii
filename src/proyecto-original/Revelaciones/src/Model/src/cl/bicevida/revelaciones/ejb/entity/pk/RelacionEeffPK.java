package cl.bicevida.revelaciones.ejb.entity.pk;

import java.io.Serializable;

import java.lang.Long;


public class RelacionEeffPK implements Serializable {
    
    private Long idFecu;
    private Long idPeriodo;
    private Long idGrilla;
    private Long idColumna;
    private Long idFila;

    public RelacionEeffPK() {
    }

    public RelacionEeffPK(Long idFecu, Long idPeriodo) {
        this.idFecu = idFecu;
        this.idPeriodo = idPeriodo;
    }


    public Long getIdFecu() {
        return idFecu;
    }

    public void setIdFecu(Long idFecu) {
        this.idFecu = idFecu;
    }

    public Long getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(Long idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof RelacionEeffPK)) {
            return false;
        }
        final RelacionEeffPK other = (RelacionEeffPK)object;
        if (!(idFecu == null ? other.idFecu == null : idFecu.equals(other.idFecu))) {
            return false;
        }
        if (!(idPeriodo == null ? other.idPeriodo == null : idPeriodo.equals(other.idPeriodo))) {
            return false;
        }
        if (!(idColumna == null ? other.idColumna == null : idColumna.equals(other.idColumna))) {
            return false;
        }
        if (!(idFila == null ? other.idFila == null : idFila.equals(other.idFila))) {
            return false;
        }
        if (!(idGrilla == null ? other.idGrilla == null : idGrilla.equals(other.idGrilla))) {
            return false;
        }
        return true;
    }
}
