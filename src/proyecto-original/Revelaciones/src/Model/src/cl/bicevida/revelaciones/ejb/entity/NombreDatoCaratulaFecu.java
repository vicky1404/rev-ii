package cl.bicevida.revelaciones.ejb.entity;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "NombreDatoCaratulaFecu.findAll",
                             query = "select o from NombreDatoCaratulaFecu o") })
@Table(name = "REV_NOMBRE_DATO_CARATULA_FECU")
public class NombreDatoCaratulaFecu implements Serializable {
    @SuppressWarnings("compatibility:571313091835209609")
    private static final long serialVersionUID = 6659577175789830504L;
    @Column(name = "CODIGO_FECU")
    private BigDecimal codigoFecu;
    @Id
    @Column(name = "ID_NOMBRE_DATO", nullable = false, length = 1024)
    private String idNombreDato;
    @ManyToOne
    @JoinColumn(name = "ID_TIPO_DATO")
    private TipoDatoCaratulaFecu tipoDatoCaratulaFecu;
    @ManyToOne
    @JoinColumn(name = "ID_NOMBRE_DATO_PADRE")
    private NombreDatoCaratulaFecu nombreDatoCaratulaFecuPadre;
    
    @OneToMany(mappedBy = "nombreDatoCaratulaFecu")
    private List<DatoCaratulaFecu> datoCaratulaFecuList;
    
    @Column(name = "GLOSA_DATO")
    private String glosaDato;

    public NombreDatoCaratulaFecu() {
    }
    
    public void setCodigoFecu(BigDecimal codigoFecu) {
        this.codigoFecu = codigoFecu;
    }

    public BigDecimal getCodigoFecu() {
        return codigoFecu;
    }

    public void setIdNombreDato(String idNombreDato) {
        this.idNombreDato = idNombreDato;
    }

    public String getIdNombreDato() {
        return idNombreDato;
    }

    public void setTipoDatoCaratulaFecu(TipoDatoCaratulaFecu tipoDatoCaratulaFecu) {
        this.tipoDatoCaratulaFecu = tipoDatoCaratulaFecu;
    }

    public TipoDatoCaratulaFecu getTipoDatoCaratulaFecu() {
        return tipoDatoCaratulaFecu;
    }

    public void setDatoCaratulaFecuList(List<DatoCaratulaFecu> datoCaratulaFecuList) {
        this.datoCaratulaFecuList = datoCaratulaFecuList;
    }

    public List<DatoCaratulaFecu> getDatoCaratulaFecuList() {
        return datoCaratulaFecuList;
    }
    
    public void setGlosaDato(String glosaDato) {
        this.glosaDato = glosaDato;
    }

    public String getGlosaDato() {
        return glosaDato;
    }
    
    public void setNombreDatoCaratulaFecuPadre(NombreDatoCaratulaFecu nombreDatoCaratulaFecuPadre) {
        this.nombreDatoCaratulaFecuPadre = nombreDatoCaratulaFecuPadre;
    }

    public NombreDatoCaratulaFecu getNombreDatoCaratulaFecuPadre() {
        return nombreDatoCaratulaFecuPadre;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("codigoFecu=");
        buffer.append(getCodigoFecu());
        buffer.append(',');
        buffer.append("idNombreDato=");
        buffer.append(getIdNombreDato());
        buffer.append(',');
        buffer.append(']');
        return buffer.toString();
    }


    
}
