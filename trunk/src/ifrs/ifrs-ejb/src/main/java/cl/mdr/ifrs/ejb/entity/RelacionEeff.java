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
import javax.persistence.Transient;

import cl.mdr.ifrs.ejb.common.Constantes;
import cl.mdr.ifrs.ejb.cross.EeffUtil;
import cl.mdr.ifrs.ejb.entity.pk.RelacionEeffPK;

import com.google.gson.annotations.Expose;


@Entity
@NamedQueries( { @NamedQuery(name = RelacionEeff.FIND_ALL, query = "select o from RelacionEeff o"),
    @NamedQuery(name = RelacionEeff.FIND_BY_PERIODO_EMPRESA, query = "select o from RelacionEeff o where o.periodoEmpresa.idPeriodo = :idPeriodo and o.periodoEmpresa.idRut = :idRut order by o.idFecu"),
    @NamedQuery(name = RelacionEeff.FIND_BY_PERIODO_FECU, query = "select o from RelacionEeff o where o.periodoEmpresa.idPeriodo = :idPeriodo and o.periodoEmpresa.idRut = :idRut and o.idFecu = :idFecu"),
    @NamedQuery(name = RelacionEeff.DELETE_BY_CELDA, query = "delete from RelacionEeff o where o.celda2 = :celda"),
    @NamedQuery(name = RelacionEeff.DELETE_BY_GRILLA_PERIODO_EMPRESA, query = "delete from RelacionEeff o where o.idGrilla = :idGrilla and o.idPeriodo = :idPeriodo and o.idRut = :idRut"),
    @NamedQuery(name = RelacionEeff.FIND_BY_CELDA, query = "select o from RelacionEeff o where o.celda2 = :celda"),
    @NamedQuery(name = RelacionEeff.FIND_BY_GRILLA, query = "select o from RelacionEeff o where o.celda2.columna.grilla = :idGrilla order by o.idColumna, o.idFila")})
@Table(name = Constantes.RELACION_EEFF)
@IdClass(RelacionEeffPK.class)

public class RelacionEeff implements Serializable {
	
    public static final String FIND_ALL = "RelacionEeff.findAll";
    public static final String FIND_BY_PERIODO_EMPRESA = "RelacionEeff.findByPeriodo";
    public static final String FIND_BY_PERIODO_FECU = "RelacionEeff.findByPeriodoFecu";
    public static final String DELETE_BY_CELDA = "RelacionEeff.deleteByCelda";
    public static final String DELETE_BY_GRILLA_PERIODO_EMPRESA = "RelacionEeff.deleteByGrillaPeriodo";
    public static final String FIND_BY_CELDA = "RelacionEeff.findByCelda";
    public static final String FIND_BY_GRILLA = "RelacionEeff.findByGrilla";
    
    @Id
    @Column(name = "ID_FECU", nullable = false, insertable = false, updatable = false)
    @Expose
    private Long idFecu;
    
    @Id
    @Column(name = "ID_PERIODO", nullable = false, insertable = false, updatable = false)
    @Expose
    private Long idPeriodo;
    
    @Id
    @Column(name = "ID_RUT", nullable = false, insertable=false, updatable=false)
    @Expose
    private Long idRut;
    
    @Id
    @Column(name = "ID_GRILLA", nullable = false, insertable = false, updatable = false)
    @Expose
    private Long idGrilla;
    
    @Id
    @Column(name = "ID_COLUMNA", nullable = false, insertable = false, updatable = false)
    @Expose
    private Long idColumna;
    
    @Id
    @Column(name = "ID_FILA", nullable = false, insertable = false, updatable = false)
    @Expose
    private Long idFila;
    
    @Column(name = "MONTO_TOTAL")
    @Expose
    private BigDecimal montoTotal;
    
    @Expose
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns( { @JoinColumn(name = "ID_COLUMNA", referencedColumnName = "ID_COLUMNA"),
                    @JoinColumn(name = "ID_GRILLA", referencedColumnName = "ID_GRILLA"),
                    @JoinColumn(name = "ID_FILA", referencedColumnName = "ID_FILA") })
    
    private Celda celda2;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns( { @JoinColumn(name = "ID_PERIODO", referencedColumnName = "ID_PERIODO"),
			        @JoinColumn(name = "ID_RUT", referencedColumnName = "ID_RUT")})
    @Expose
    private PeriodoEmpresa periodoEmpresa;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_FECU")
    @Expose
    private CodigoFecu codigoFecu;
    
    @Transient
    @Expose
    private BigDecimal montoTotalNuevo;

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
    
    public Long getIdRut() {
		return idRut;
	}

	public void setIdRut(Long idRut) {
		this.idRut = idRut;
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
    
    public void setIdGrilla(Long idGrilla) {
        this.idGrilla = idGrilla;
    }

    public Long getIdGrilla() {
        return idGrilla;
    }

    public void setIdColumna(Long idColumna) {
        this.idColumna = idColumna;
    }

    public Long getIdColumna() {
        return idColumna;
    }

    public void setIdFila(Long idFila) {
        this.idFila = idFila;
    }

    public Long getIdFila() {
        return idFila;
    }
    
    public String getFecuFormat(){
        if(idFecu!=null)
            return EeffUtil.formatFecu(idFecu);
        else
            return "";
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

    public void setMontoTotalNuevo(BigDecimal montoTotalNuevo) {
        this.montoTotalNuevo = montoTotalNuevo;
    }

    public BigDecimal getMontoTotalNuevo() {
        return montoTotalNuevo;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idColumna == null) ? 0 : idColumna.hashCode());
		result = prime * result + ((idFecu == null) ? 0 : idFecu.hashCode());
		result = prime * result + ((idFila == null) ? 0 : idFila.hashCode());
		result = prime * result
				+ ((idGrilla == null) ? 0 : idGrilla.hashCode());
		result = prime * result
				+ ((idPeriodo == null) ? 0 : idPeriodo.hashCode());
		result = prime * result + ((idRut == null) ? 0 : idRut.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RelacionEeff other = (RelacionEeff) obj;
		if (idColumna == null) {
			if (other.idColumna != null)
				return false;
		} else if (!idColumna.equals(other.idColumna))
			return false;
		if (idFecu == null) {
			if (other.idFecu != null)
				return false;
		} else if (!idFecu.equals(other.idFecu))
			return false;
		if (idFila == null) {
			if (other.idFila != null)
				return false;
		} else if (!idFila.equals(other.idFila))
			return false;
		if (idGrilla == null) {
			if (other.idGrilla != null)
				return false;
		} else if (!idGrilla.equals(other.idGrilla))
			return false;
		if (idPeriodo == null) {
			if (other.idPeriodo != null)
				return false;
		} else if (!idPeriodo.equals(other.idPeriodo))
			return false;
		if (idRut == null) {
			if (other.idRut != null)
				return false;
		} else if (!idRut.equals(other.idRut))
			return false;
		return true;
	}

    
   	

}
