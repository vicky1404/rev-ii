package cl.mdr.ifrs.ejb.entity.pk;

import java.io.Serializable;

import java.lang.Long;



public class DetalleEeffPK implements Serializable {
    
    private Long idCuenta;

    private Long idFecu;
    
    private Long idVersionEeff;

    public DetalleEeffPK(Long idCuenta, Long idFecu, Long idVersionEeff) {
        super();
        this.idCuenta = idCuenta;
        this.idFecu = idFecu;
        this.idVersionEeff = idVersionEeff;
    }

    public DetalleEeffPK() {
    }

    public void setIdCuenta(Long idCuenta) {
        this.idCuenta = idCuenta;
    }

    public Long getIdCuenta() {
        return idCuenta;
    }

    public void setIdFecu(Long idFecu) {
        this.idFecu = idFecu;
    }

    public Long getIdFecu() {
        return idFecu;
    }

    public void setIdVersionEeff(Long idVersionEeff) {
        this.idVersionEeff = idVersionEeff;
    }

    public Long getIdVersionEeff() {
        return idVersionEeff;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof DetalleEeffPK)) {
            return false;
        }
        final DetalleEeffPK other = (DetalleEeffPK)object;
        if (!(idCuenta == null ? other.idCuenta == null : idCuenta.equals(other.idCuenta))) {
            return false;
        }
        if (!(idFecu == null ? other.idFecu == null : idFecu.equals(other.idFecu))) {
            return false;
        }
        if (!(idVersionEeff == null ? other.idVersionEeff == null : idVersionEeff.equals(other.idVersionEeff))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = 1;
        result = PRIME * result + ((idCuenta == null) ? 0 : idCuenta.hashCode());
        result = PRIME * result + ((idFecu == null) ? 0 : idFecu.hashCode());
        result = PRIME * result + ((idVersionEeff == null) ? 0 : idVersionEeff.hashCode());
        return result;
    }
}
