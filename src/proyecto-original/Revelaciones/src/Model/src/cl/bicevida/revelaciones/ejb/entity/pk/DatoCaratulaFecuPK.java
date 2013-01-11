package cl.bicevida.revelaciones.ejb.entity.pk;

import java.io.Serializable;

import java.math.BigDecimal;

public class DatoCaratulaFecuPK implements Serializable {
    private BigDecimal idCaratulaFecu;
    private String idNombreDato;

    public DatoCaratulaFecuPK() {
    }

    public DatoCaratulaFecuPK(BigDecimal idCaratulaFecu, String idNombreDato) {
        this.idCaratulaFecu = idCaratulaFecu;
        this.idNombreDato = idNombreDato;
    }

    public boolean equals(Object other) {
        if (other instanceof DatoCaratulaFecuPK) {
            final DatoCaratulaFecuPK otherDatoCaratulaFecuPK = (DatoCaratulaFecuPK)other;
            final boolean areEqual =
                (otherDatoCaratulaFecuPK.idCaratulaFecu.equals(idCaratulaFecu) && otherDatoCaratulaFecuPK.idNombreDato.equals(idNombreDato));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public BigDecimal getIdCaratulaFecu() {
        return idCaratulaFecu;
    }

    public void setIdCaratulaFecu(BigDecimal idCaratulaFecu) {
        this.idCaratulaFecu = idCaratulaFecu;
    }

    public String getIdNombreDato() {
        return idNombreDato;
    }

    public void setIdNombreDato(String idNombreDato) {
        this.idNombreDato = idNombreDato;
    }
}
