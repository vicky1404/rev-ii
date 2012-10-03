package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "RevXbrlRangoCodigoFecu.findAll",
                             query = "select o from RevXbrlRangoCodigoFecu o") })
@Table(name = "REV_XBRL_RANGO_CODIGO_FECU")
public class RevXbrlRangoCodigoFecu implements Serializable {
    @Column(name = "CODIGO_FECU_DESDE")
    private BigDecimal codigoFecuDesde;
    @Column(name = "CODIGO_FECU_HASTA")
    private BigDecimal codigoFecuHasta;
    @Id
    @Column(name = "ID_INFORME_EEFF", nullable = false, length = 256)
    private String idInformeEeff;

    public RevXbrlRangoCodigoFecu() {
    }

    public RevXbrlRangoCodigoFecu(BigDecimal codigoFecuDesde, BigDecimal codigoFecuHasta, String idInformeEeff) {
        this.codigoFecuDesde = codigoFecuDesde;
        this.codigoFecuHasta = codigoFecuHasta;
        this.idInformeEeff = idInformeEeff;
    }


    public BigDecimal getCodigoFecuDesde() {
        return codigoFecuDesde;
    }

    public void setCodigoFecuDesde(BigDecimal codigoFecuDesde) {
        this.codigoFecuDesde = codigoFecuDesde;
    }

    public BigDecimal getCodigoFecuHasta() {
        return codigoFecuHasta;
    }

    public void setCodigoFecuHasta(BigDecimal codigoFecuHasta) {
        this.codigoFecuHasta = codigoFecuHasta;
    }

    public String getIdInformeEeff() {
        return idInformeEeff;
    }

    public void setIdInformeEeff(String idInformeEeff) {
        this.idInformeEeff = idInformeEeff;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("codigoFecuDesde=");
        buffer.append(getCodigoFecuDesde());
        buffer.append(',');
        buffer.append("codigoFecuHasta=");
        buffer.append(getCodigoFecuHasta());
        buffer.append(',');
        buffer.append("idInformeEeff=");
        buffer.append(getIdInformeEeff());
        buffer.append(']');
        return buffer.toString();
    }
}
