package cl.bicevida.revelaciones.ejb.entity;

import cl.bicevida.revelaciones.ejb.entity.Periodo;

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
@NamedQueries( { @NamedQuery(name = CaratulaFecu.FIND_ALL, query = "select o from CaratulaFecu o"),
                 @NamedQuery(name = CaratulaFecu.FIND_ALL_BY_PERIODO, query = "select o from CaratulaFecu o where o.periodo.idPeriodo =:idPeriodo")})
@Table(name = "REV_CARATULA_FECU")
public class CaratulaFecu implements Serializable {
    @SuppressWarnings("compatibility:-1133753657982412697")
    private static final long serialVersionUID = 4127999063214819909L;
    
    public static final String FIND_ALL = "CaratulaFecu.findAll";
    public static final String FIND_ALL_BY_PERIODO = "CaratulaFecu.findAllByPeriodo";
    
    @Id
    @Column(name = "ID_CARATULA_FECU", nullable = false)
    private BigDecimal idCaratulaFecu;
    @ManyToOne
    @JoinColumn(name = "ID_PERIODO")
    private Periodo periodo;
    @OneToMany(mappedBy = "caratulaFecu")
    private List<DatoCaratulaFecu> datoCaratulaFecuList;

    public CaratulaFecu() {
    }

    public CaratulaFecu(BigDecimal idCaratulaFecu, Periodo periodo) {
        this.idCaratulaFecu = idCaratulaFecu;
        this.periodo = periodo;
    }

    public void setIdCaratulaFecu(BigDecimal idCaratulaFecu) {
        this.idCaratulaFecu = idCaratulaFecu;
    }

    public BigDecimal getIdCaratulaFecu() {
        return idCaratulaFecu;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setDatoCaratulaFecuList(List<DatoCaratulaFecu> datoCaratulaFecuList) {
        this.datoCaratulaFecuList = datoCaratulaFecuList;
    }

    public List<DatoCaratulaFecu> getDatoCaratulaFecuList() {
        return datoCaratulaFecuList;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idCaratulaFecu=");
        buffer.append(getIdCaratulaFecu());
        buffer.append(',');
        buffer.append(']');
        return buffer.toString();
    }
}
