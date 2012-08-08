package cl.mdr.ifrs.ejb.entity;


import java.io.Serializable;
import java.math.BigDecimal;

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
import javax.persistence.Table;

import cl.mdr.ifrs.ejb.common.Constantes;
import cl.mdr.ifrs.ejb.cross.EeffUtil;
import cl.mdr.ifrs.ejb.entity.pk.RelacionEeffPK;


@Entity
@NamedQueries( { @NamedQuery(name = RelacionEeff.FIND_ALL, query = "select o from RelacionEeff o"),
    @NamedQuery(name = RelacionEeff.FIND_BY_PERIODO, query = "select o from RelacionEeff o where o.periodoEmpresa.periodo.idPeriodo = :idPeriodo order by o.idFecu"),
    @NamedQuery(name = RelacionEeff.FIND_BY_PERIODO_FECU, query = "select o from RelacionEeff o where o.periodoEmpresa.periodo.idPeriodo = :idPeriodo and o.idFecu = :idFecu"),
    @NamedQuery(name = RelacionEeff.DELETE_BY_CELDA, query = "delete from RelacionEeff o where o.celda2 = :celda"),
    @NamedQuery(name = RelacionEeff.DELETE_BY_GRILLA_PERIODO, query = "delete from RelacionEeff o where o.idGrilla = :idGrilla and o.idPeriodo = :idPeriodo")})
@Table(name = Constantes.RELACION_EEFF)
@IdClass(RelacionEeffPK.class)

public class RelacionEeff implements Serializable {
	
	public static final String FIND_ALL = "RelacionEeff.findAll";
    public static final String FIND_BY_PERIODO = "RelacionEeff.findByPeriodo";
    public static final String FIND_BY_PERIODO_FECU = "RelacionEeff.findByPeriodoFecu";
    public static final String DELETE_BY_CELDA = "RelacionEeff.deleteByCelda";
    public static final String DELETE_BY_GRILLA_PERIODO = "RelacionEeff.deleteByGrillaPeriodo";
        
    private static final long serialVersionUID = -6886308246258165195L;
    
    @Id
    @Column(name = "ID_FECU", nullable = false, insertable = false, updatable = false)
    private Long idFecu;
    
    @Id
    @Column(name = "ID_PERIODO", nullable = false, insertable = false, updatable = false)
    private Long idPeriodo;
    
    @Id
    @Column(name = "ID_RUT", nullable = false, insertable = false, updatable = false)
    private Long idRut;
    
	@Column(name = "ID_GRILLA", insertable = false, updatable = false)
    private Long idGrilla;


	@Column(name = "MONTO_TOTAL")
    private BigDecimal montoTotal;
	
    @ManyToOne
    @JoinColumns( { @JoinColumn(name = "ID_COLUMNA", referencedColumnName = "ID_COLUMNA"),
                    @JoinColumn(name = "ID_GRILLA", referencedColumnName = "ID_GRILLA"),
                    @JoinColumn(name = "ID_FILA", referencedColumnName = "ID_FILA") })
    private Celda celda2;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns( { @JoinColumn(name = "ID_PERIODO", referencedColumnName = "ID_PERIODO"),
			        @JoinColumn(name = "ID_RUT", referencedColumnName = "ID_RUT")})
    private PeriodoEmpresa periodoEmpresa;
    
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
    
    public Long getIdRut() {
		return idRut;
	}

	public void setIdRut(Long idRut) {
		this.idRut = idRut;
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
    
    public PeriodoEmpresa getPeriodoEmpresa() {
        return periodoEmpresa;
    }

    public void setPeriodoEmpresa(PeriodoEmpresa periodoEmpresa) {
        this.periodoEmpresa = periodoEmpresa;
        if (periodoEmpresa != null) {
            this.idPeriodo = periodoEmpresa.getIdPeriodo();
            this.idRut = periodoEmpresa.getIdRut();
        }
    }

    public void setCodigoFecu(CodigoFecu codigoFecu) {
        this.codigoFecu = codigoFecu;
    }

    public CodigoFecu getCodigoFecu() {
        return codigoFecu;
    }
    
    public void copyEstadoFinanciero(EstadoFinanciero eeff,Celda celda, PeriodoEmpresa periodoEmpresa){
        this.idFecu = eeff.getIdFecu();
        if(periodoEmpresa!=null){
        	this.idPeriodo = periodoEmpresa.getIdPeriodo();
        	this.idRut = periodoEmpresa.getIdRut();
        }
        this.montoTotal = eeff.getMontoTotal();
        this.celda2 = celda;
        this.periodoEmpresa = periodoEmpresa;
        this.codigoFecu = eeff.getCodigoFecu();
    }

	public Long getIdGrilla() {
		return idGrilla;
	}

	public void setIdGrilla(Long idGrilla) {
		this.idGrilla = idGrilla;
	}
	
	public String getFecuFormat(){
        if(idFecu!=null)
            return EeffUtil.formatFecu(idFecu);
        else
            return "";
    }

}
