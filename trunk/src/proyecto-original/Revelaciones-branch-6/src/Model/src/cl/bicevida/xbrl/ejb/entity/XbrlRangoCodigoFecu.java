package cl.bicevida.xbrl.ejb.entity;

import java.io.Serializable;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({ 
                @NamedQuery(name = XbrlRangoCodigoFecu.FIND_ALL,
                            query = "select o from XbrlRangoCodigoFecu o") 
})
@Table(name = "REV_XBRL_RANGO_CODIGO_FECU")
public class XbrlRangoCodigoFecu implements Serializable {
    @SuppressWarnings("compatibility:-2404498182761985933")
    private static final long serialVersionUID = 5041656613325961404L;
    
    public static final String FIND_ALL = "XbrlRangoCodigoFecu.findAll";
    
    @Column(name = "CODIGO_FECU_DESDE")
    private Long codigoFecuDesde;
    @Column(name = "CODIGO_FECU_HASTA")
    private Long codigoFecuHasta;
    @Id
    @Column(name = "ID_INFORME_EEFF", nullable = false, length = 256)
    private String idInformeEeff;

    public XbrlRangoCodigoFecu() {
    }

    public XbrlRangoCodigoFecu(Long codigoFecuDesde, Long codigoFecuHasta, String idInformeEeff) {
        this.codigoFecuDesde = codigoFecuDesde;
        this.codigoFecuHasta = codigoFecuHasta;
        this.idInformeEeff = idInformeEeff;
    }


    public Long getCodigoFecuDesde() {
        return codigoFecuDesde;
    }

    public void setCodigoFecuDesde(Long codigoFecuDesde) {
        this.codigoFecuDesde = codigoFecuDesde;
    }

    public Long getCodigoFecuHasta() {
        return codigoFecuHasta;
    }

    public void setCodigoFecuHasta(Long codigoFecuHasta) {
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
