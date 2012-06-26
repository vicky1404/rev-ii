package cl.bicevida.revelaciones.ejb.entity;


import cl.bicevida.revelaciones.ejb.common.Constantes;
import cl.bicevida.revelaciones.ejb.entity.pk.RelacionEeffPK;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@NamedQueries( { @NamedQuery(name = "RelacionEeff.findAll", query = "select o from RelacionEeff o") })
@Table(name = Constantes.RELACION_EEFF)
@IdClass(RelacionEeffPK.class)
public class RelacionEeff implements Serializable {
        
    @SuppressWarnings("compatibility:4191810355259797898")
    private static final long serialVersionUID = -6886308246258165195L;
    
    @Id
    @Column(name = "ID_FECU", nullable = false, insertable = false, updatable = false)
    private Long idFecu;
    
    @Id
    @Column(name = "ID_PERIODO", nullable = false, insertable = false, updatable = false)
    private Long idPeriodo;
    
    @Column(name = "MONTO_TOTAL")
    private BigDecimal montoTotal;
    
    @ManyToOne
    @JoinColumns( { @JoinColumn(name = "ID_COLUMNA", referencedColumnName = "ID_COLUMNA"),
                    @JoinColumn(name = "ID_GRILLA", referencedColumnName = "ID_GRILLA"),
                    @JoinColumn(name = "ID_FILA", referencedColumnName = "ID_FILA") })
    private Celda celda2;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PERIODO")
    private Periodo periodo;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_FECU")
    private CodigoFecu codigoFecu;

    public RelacionEeff() {
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

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public Celda getCelda2() {
        return celda2;
    }

    public void setCelda2(Celda celda2) {
        this.celda2 = celda2;
    }
    
    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
        if (periodo != null) {
            this.idPeriodo = periodo.getIdPeriodo();
        }
    }

    public void setCodigoFecu(CodigoFecu codigoFecu) {
        this.codigoFecu = codigoFecu;
    }

    public CodigoFecu getCodigoFecu() {
        return codigoFecu;
    }
    
    public void copyEstadoFinanciero(EstadoFinanciero eeff,Celda celda, Periodo periodo){
        this.idFecu = eeff.getIdFecu();
        this.idPeriodo = periodo.getIdPeriodo();
        this.montoTotal = eeff.getMontoTotal();
        this.celda2 = celda;
        this.periodo = periodo;
        this.codigoFecu = eeff.getCodigoFecu();
    }
}
