package cl.bicevida.revelaciones.ejb.entity;

import cl.bicevida.revelaciones.ejb.entity.pk.DatoCaratulaFecuPK;

import java.io.Serializable;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "DatoCaratulaFecu.findAll", query = "select o from DatoCaratulaFecu o") })
@Table(name = "REV_DATO_CARATULA_FECU")
@IdClass(DatoCaratulaFecuPK.class)
public class DatoCaratulaFecu implements Serializable {
    @SuppressWarnings("compatibility:4483185778353268544")
    private static final long serialVersionUID = 2075928588705583134L;
    @Id
    @Column(name = "ID_CARATULA_FECU", nullable = false, insertable = false, updatable = false)
    private BigDecimal idCaratulaFecu;
    @Id
    @Column(name = "ID_NOMBRE_DATO", nullable = false, length = 1024, insertable = false, updatable = false)
    private String idNombreDato;
    @Column(length = 4000)
    private String valor;
    @ManyToOne
    @JoinColumn(name = "ID_NOMBRE_DATO")
    private NombreDatoCaratulaFecu nombreDatoCaratulaFecu;
    @ManyToOne
    @JoinColumn(name = "ID_CARATULA_FECU")
    private CaratulaFecu caratulaFecu;

    public DatoCaratulaFecu() {
    }

    public DatoCaratulaFecu(CaratulaFecu caratulaFecu, NombreDatoCaratulaFecu nombreDatoCaratulaFecu, String valor) {
        this.caratulaFecu = caratulaFecu;
        this.nombreDatoCaratulaFecu = nombreDatoCaratulaFecu;
        this.valor = valor;
    }
    

    public void setIdCaratulaFecu(BigDecimal idCaratulaFecu) {
        this.idCaratulaFecu = idCaratulaFecu;
    }

    public BigDecimal getIdCaratulaFecu() {
        return idCaratulaFecu;
    }

    public void setIdNombreDato(String idNombreDato) {
        this.idNombreDato = idNombreDato;
    }

    public String getIdNombreDato() {
        return idNombreDato;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public void setNombreDatoCaratulaFecu(NombreDatoCaratulaFecu nombreDatoCaratulaFecu) {
        this.nombreDatoCaratulaFecu = nombreDatoCaratulaFecu;
    }

    public NombreDatoCaratulaFecu getNombreDatoCaratulaFecu() {
        return nombreDatoCaratulaFecu;
    }

    public void setCaratulaFecu(CaratulaFecu caratulaFecu) {
        this.caratulaFecu = caratulaFecu;
    }

    public CaratulaFecu getCaratulaFecu() {
        return caratulaFecu;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idCaratulaFecu=");
        buffer.append(getIdCaratulaFecu());
        buffer.append(',');
        buffer.append("idNombreDato=");
        buffer.append(getIdNombreDato());
        buffer.append(',');
        buffer.append("valor=");
        buffer.append(getValor());
        buffer.append(']');
        return buffer.toString();
    }

  
}
