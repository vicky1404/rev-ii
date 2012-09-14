package cl.bicevida.revelaciones.ejb.entity.pk;

import java.io.Serializable;


public class RelacionDetalleEeffPK implements Serializable {
    
    private Long idFecu;
    private Long idPeriodo;
    private Long idCuenta;
    private Long idColumna;
    private Long idFila;
    private Long idGrilla;

    public RelacionDetalleEeffPK() {
    }

    public RelacionDetalleEeffPK(Long idFecu, Long idPeriodo, Long idCuenta, Long idColumna, Long idFila,
                                 Long idGrilla) {
        super();
        this.idFecu = idFecu;
        this.idPeriodo = idPeriodo;
        this.idCuenta = idCuenta;
        this.idColumna = idColumna;
        this.idFila = idFila;
        this.idGrilla = idGrilla;
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

    public void setIdCuenta(Long idCuenta) {
        this.idCuenta = idCuenta;
    }

    public Long getIdCuenta() {
        return idCuenta;
    }

    public void setIdColumna(Long idColumna) {
        this.idColumna = idColumna;
    }

    public Long getIdColumna() {
        return idColumna;
    }

    public void setIdFila(Long idFila) {
        this.idFila = idFila;
    }

    public Long getIdFila() {
        return idFila;
    }

    public void setIdGrilla(Long idGrilla) {
        this.idGrilla = idGrilla;
    }

    public Long getIdGrilla() {
        return idGrilla;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof RelacionDetalleEeffPK)) {
            return false;
        }
        final RelacionDetalleEeffPK other = (RelacionDetalleEeffPK)object;
        if (!(idFecu == null ? other.idFecu == null : idFecu.equals(other.idFecu))) {
            return false;
        }
        if (!(idPeriodo == null ? other.idPeriodo == null : idPeriodo.equals(other.idPeriodo))) {
            return false;
        }
        if (!(idCuenta == null ? other.idCuenta == null : idCuenta.equals(other.idCuenta))) {
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

    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = 1;
        result = PRIME * result + ((idFecu == null) ? 0 : idFecu.hashCode());
        result = PRIME * result + ((idPeriodo == null) ? 0 : idPeriodo.hashCode());
        result = PRIME * result + ((idCuenta == null) ? 0 : idCuenta.hashCode());
        result = PRIME * result + ((idColumna == null) ? 0 : idColumna.hashCode());
        result = PRIME * result + ((idFila == null) ? 0 : idFila.hashCode());
        result = PRIME * result + ((idGrilla == null) ? 0 : idGrilla.hashCode());
        return result;
    }
}
