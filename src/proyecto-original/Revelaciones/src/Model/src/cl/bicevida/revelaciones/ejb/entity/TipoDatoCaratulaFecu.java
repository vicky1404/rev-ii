package cl.bicevida.revelaciones.ejb.entity;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "TipoDatoCaratulaFecu.findAll",
                             query = "select o from TipoDatoCaratulaFecu o") })
@Table(name = "REV_TIPO_DATO_CARATULA_FECU")
public class TipoDatoCaratulaFecu implements Serializable {
    @Id
    @Column(name = "ID_TIPO_DATO", nullable = false)
    private BigDecimal idTipoDato;
    @Column(name = "NOMBRE_CLASE", length = 256)
    private String nombreClase;
    @Column(name = "NOMBRE_TIPO_DATO", length = 128)
    private String nombreTipoDato;
    @OneToMany(mappedBy = "tipoDatoCaratulaFecu")
    private List<NombreDatoCaratulaFecu> nombreDatoCaratulaFecuList;

    public TipoDatoCaratulaFecu() {
    }

    public TipoDatoCaratulaFecu(BigDecimal idTipoDato, String nombreClase, String nombreTipoDato) {
        this.idTipoDato = idTipoDato;
        this.nombreClase = nombreClase;
        this.nombreTipoDato = nombreTipoDato;
    }

    public void setIdTipoDato(BigDecimal idTipoDato) {
        this.idTipoDato = idTipoDato;
    }

    public BigDecimal getIdTipoDato() {
        return idTipoDato;
    }

    public void setNombreClase(String nombreClase) {
        this.nombreClase = nombreClase;
    }

    public String getNombreClase() {
        return nombreClase;
    }

    public void setNombreTipoDato(String nombreTipoDato) {
        this.nombreTipoDato = nombreTipoDato;
    }

    public String getNombreTipoDato() {
        return nombreTipoDato;
    }

    public void setNombreDatoCaratulaFecuList(List<NombreDatoCaratulaFecu> nombreDatoCaratulaFecuList) {
        this.nombreDatoCaratulaFecuList = nombreDatoCaratulaFecuList;
    }

    public List<NombreDatoCaratulaFecu> getNombreDatoCaratulaFecuList() {
        return nombreDatoCaratulaFecuList;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idTipoDato=");
        buffer.append(getIdTipoDato());
        buffer.append(',');
        buffer.append("nombreClase=");
        buffer.append(getNombreClase());
        buffer.append(',');
        buffer.append("nombreTipoDato=");
        buffer.append(getNombreTipoDato());
        buffer.append(',');
        buffer.append(']');
        return buffer.toString();
    }
}
