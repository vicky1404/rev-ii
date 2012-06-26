package cl.bicevida.revelaciones.ejb.entity.pk;

import java.io.Serializable;


public class RelacionDetalleEeffPK implements Serializable {
    
    private Long idFecu;
    private Long idPeriodo;
    private Long idCuenta;

    public RelacionDetalleEeffPK() {
    }

    public RelacionDetalleEeffPK(Long idFecu, Long idPeriodo, Long idCuenta) {
        this.idFecu = idFecu;
        this.idPeriodo = idPeriodo;
        this.idCuenta = idCuenta;
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

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof RelacionDetalleEeffPK)) {
            return false;
        }
        final RelacionDetalleEeffPK other = (RelacionDetalleEeffPK) object;
        if (!(idFecu == null ? other.idFecu == null : idFecu.equals(other.idFecu))) {
            return false;
        }
        if (!(idPeriodo == null ? other.idPeriodo == null : idPeriodo.equals(other.idPeriodo))) {
            return false;
        }
        if (!(idCuenta == null ? other.idCuenta == null : idCuenta.equals(other.idCuenta))) {
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
        return result;
    }
}
