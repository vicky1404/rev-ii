package cl.mdr.ifrs.ejb.entity;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;



import com.google.gson.annotations.Expose;

import cl.mdr.ifrs.ejb.common.Constantes;
import cl.mdr.ifrs.ejb.common.TipoCeldaEnum;
import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.pk.CeldaPK;


@Entity
@NamedQueries( { @NamedQuery(name = "Celda.findAll", query = "select o from Celda o"), 
                 @NamedQuery(name = Celda.CELDA_FIND_BY_COLUMNA, query = "select o from Celda o where o.columna = :columna"),
                 /*@NamedQuery(name = Celda.CELDA_FIND_BY_GRUPO, query = "select o from Celda o where o.grupo = :grupo and o.idGrilla = :idGrilla "),
                 @NamedQuery(name = Celda.CELDA_FIND_MAX_GRUPO, query = "select max(o.grupo) from Celda o where o.idGrilla = :idGrilla "),*/
                 @NamedQuery(name = Celda.CELDA_FIND_BY_ID_FILA, query = "select o from Celda o where o.idGrilla = :idGrilla and o.idFila = :idFila order by o.idColumna")                 
                 })
@Table(name = Constantes.CELDA)
@IdClass(CeldaPK.class)
public class Celda implements Serializable {
    
    public final static String CELDA_FIND_BY_COLUMNA = "Celda.findCeldaByColumna";
    public final static String CELDA_FIND_BY_GRUPO = "Celda.findCeldaByGrupo";
    public final static String CELDA_FIND_MAX_GRUPO = "Celda.findCeldaMaxGrupo";
    public final static String CELDA_FIND_BY_ID_FILA = "Celda.findCeldaByIdFila";
    private static final long serialVersionUID = -3518647074958736787L;

    @Id
    @Column(name = "ID_COLUMNA", nullable = false, insertable = false, updatable = false)
    @Expose
    private Long idColumna;
    
    @Id
    @Column(name = "ID_FILA", nullable = false, insertable = true, updatable = true)
    @Expose
    private Long idFila;
    
    @Id
    @Column(name = "ID_GRILLA", nullable = false, insertable = false, updatable = false)
    @Expose
    private Long idGrilla;
    
    @Column(nullable = false, length = 2048)
    @Expose
    private String valor;
    
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "ID_TIPO_CELDA")
    @Expose
    private TipoCelda tipoCelda;
    
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "ID_TIPO_DATO")
    @Expose
    private TipoDato tipoDato;
    
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "ID_COLUMNA", referencedColumnName = "ID_COLUMNA"),
        @JoinColumn(name = "ID_GRILLA", referencedColumnName = "ID_GRILLA")
    })
    @Expose
    private Columna columna;
    
    @Column(name="CHILD_HORIZONTAL")
    @Expose
    private Long childHorizontal;
    
    @Column(name="PARENT_HORIZONTAL")
    @Expose
    private Long parentHorizontal;
        
    @Column(name="CHILD_VERTICAL")
    @Expose
    private Long childVertical;
    
    @Column(name="PARENT_VERTICAL")
    @Expose
    private Long parentVertical;
    
    @Column(name="FORMULA")
    @Expose
    private String formula;
    
    //@Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "celda2", fetch = FetchType.LAZY, orphanRemoval=true)
    private List<RelacionEeff> relacionEeffList;
    
    //@Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "celda5", fetch = FetchType.LAZY, orphanRemoval=true)
    private List<RelacionDetalleEeff> relacionDetalleEeffList;
    
    @Transient
    @Expose
    private boolean esNumero;
    
    @SuppressWarnings("unused")
	@Transient
	@Expose
    private Long valorLong;
    
    @SuppressWarnings("unused")
	@Transient
	@Expose
    private BigDecimal valorBigDecimal;
    
    @SuppressWarnings("unused")
	@Transient
	@Expose
    private Date valorDate;
    
    @Transient
    @Expose
    private boolean selectedByFormula;
    
    @Transient
    private BigDecimal sumaEeff;
    
    @Transient
    private boolean valid = true;
    
   
	public Celda() {
    }

    public Celda(Columna columna, Long idFila, TipoCelda tipoCelda, TipoDato tipoDato, String valor) {
        this.columna = columna;
        this.idFila = idFila;
        this.tipoCelda = tipoCelda;
        this.tipoDato = tipoDato;
        this.valor = valor;
    }
    
    public Celda(Long idColumna, Long idFila) {
        this.idColumna = idColumna;
        this.idFila = idFila;
    }

    public Long getIdColumna() {
        return idColumna;
    }

    public void setIdColumna(Long idColumna) {
        this.idColumna = idColumna;
    }

    public Long getIdFila() {
        return idFila;
    }

    public void setIdFila(Long idFila) {
        this.idFila = idFila;
    }

    public Long getIdGrilla() {
        return idGrilla;
    }

    public void setIdGrilla(Long idGrilla) {
        this.idGrilla = idGrilla;
    }


    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public TipoCelda getTipoCelda() {
        return tipoCelda;
    }

    public void setTipoCelda(TipoCelda tipoCelda) {
        this.tipoCelda = tipoCelda;
    }

    public TipoDato getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(TipoDato tipoDato) {
        this.tipoDato = tipoDato;
    }

    public Columna getColumna() {
        return columna;
    }

    public void setColumna(Columna columna) {
        this.columna = columna;
        if (columna != null) {
            this.idGrilla = columna.getIdGrilla();
            this.idColumna = columna.getIdColumna();
        }
    }

    
    public void setValorLong(Long valorLong) { 
        this.setValor(Util.getString(valorLong, "0"));        
    }

    public Long getValorLong() {
        return Util.getLong(getValor(), 0L);
    }

    public void setValorBigDecimal(BigDecimal valorBigDecimal) {
        this.setValor(Util.getString(valorBigDecimal, "0"));        
    }

    public BigDecimal getValorBigDecimal() {
        return Util.getBigDecimal(getValor(), new BigDecimal(0));
    }

    public void setValorDate(Date valorDate) {
        this.setValor(Util.getString(valorDate));        
    }

    public Date getValorDate() {        
        return Util.getDate(getValor());
    }

    public void setChildHorizontal(Long childHorizontal) {
        this.childHorizontal = childHorizontal;
    }

    public Long getChildHorizontal() {
        return childHorizontal;
    }

    public void setParentHorizontal(Long parentHorizontal) {
        this.parentHorizontal = parentHorizontal;
    }

    public Long getParentHorizontal() {
        return parentHorizontal;
    }

    public void setChildVertical(Long childVertical) {
        this.childVertical = childVertical;
    }

    public Long getChildVertical() {
        return childVertical;
    }

    public void setParentVertical(Long parentVertical) {
        this.parentVertical = parentVertical;
    }

    public Long getParentVertical() {
        return parentVertical;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getFormula() {
        return formula;
    }
    
    
    
    public static Celda clone(final Celda celda){
    
        Celda celdaNew = new Celda();
        celdaNew.setIdColumna(celda.getIdColumna());
        celdaNew.setIdFila(celda.getIdFila());
        celdaNew.setIdGrilla(celda.getIdGrilla());
        celdaNew.setValor(celda.getValor());
        celdaNew.setTipoCelda(celda.getTipoCelda());
        celdaNew.setTipoDato(celda.getTipoDato());
        celdaNew.setChildHorizontal(celda.getChildHorizontal());
        celdaNew.setParentHorizontal(celda.getParentHorizontal());
        celdaNew.setChildVertical(celda.getChildVertical());
        celdaNew.setParentVertical(celda.getParentVertical());
        celdaNew.setFormula(celda.getFormula());
        //celdaNew.setColumna(celda.getColumna());
        return celdaNew;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idGrilla=");
        buffer.append(getIdGrilla());
        buffer.append(',');
        buffer.append("idColumna=");
        buffer.append(getColumna().getIdColumna());
        buffer.append(',');
        buffer.append("idFila=");
        buffer.append(getIdFila());
        buffer.append(',');
        buffer.append("valor=");
        buffer.append(getValor());
        buffer.append(']');
        return buffer.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Celda)) {
            return false;
        }
        final Celda other = (Celda)object;
        if (!(idColumna == null ? other.idColumna == null : idColumna.equals(other.idColumna))) {
            return false;
        }
        if (!(idFila == null ? other.idFila == null : idFila.equals(other.idFila))) {
            return false;
        }
        if (!(idGrilla == null ? other.idGrilla == null : idGrilla.equals(other.idGrilla))) {
            return false;
        }
        return true;
    }

    public void setEsNumero(boolean esNumero) {
        this.esNumero = esNumero;
    }

    public void setSelectedByFormula(boolean selectedByFormula) {
        this.selectedByFormula = selectedByFormula;
    }

    public boolean isSelectedByFormula() {
        return selectedByFormula;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = 1;
        result = PRIME * result + ((idColumna == null) ? 0 : idColumna.hashCode());
        result = PRIME * result + ((idFila == null) ? 0 : idFila.hashCode());
        result = PRIME * result + ((idGrilla == null) ? 0 : idGrilla.hashCode());
        return result;
    }
    
    public boolean isEsNumero() {
        esNumero = false;
        if (getTipoCelda() != null && TipoCeldaEnum.NUMERO.getKey().equals(getTipoCelda().getIdTipoCelda())) {
            esNumero = true;
        } else if (getTipoCelda() != null && TipoCeldaEnum.TOTAL.getKey().equals(getTipoCelda().getIdTipoCelda())) {
            esNumero = true;
        } else if (getTipoCelda() != null && TipoCeldaEnum.SUBTOTAL.getKey().equals(getTipoCelda().getIdTipoCelda())) {
            esNumero = true;
        }
        return esNumero;
    }

    public void setRelacionEeffList(List<RelacionEeff> relacionEeffList) {
        this.relacionEeffList = relacionEeffList;
    }

    public List<RelacionEeff> getRelacionEeffList() {
        return relacionEeffList;
    }

    public void setRelacionDetalleEeffList(List<RelacionDetalleEeff> relacionDetalleEeffList) {
        this.relacionDetalleEeffList = relacionDetalleEeffList;
    }

    public List<RelacionDetalleEeff> getRelacionDetalleEeffList() {
        return relacionDetalleEeffList;
    }
    
    
    @Transient
    public int getSizeRelacionEeffList(){
    	
        if(getRelacionEeffList()==null)
            return 0;
        else
            return getRelacionEeffList().size();
    }
    
    @Transient
    public int getSizeRelacionDetalleEeffList(){
        if(getRelacionDetalleEeffList()==null)
            return 0;
        else
            return getRelacionDetalleEeffList().size();
    }
    
    public BigDecimal getSumaEeff() {
		return sumaEeff;
	}

	public void setSumaEeff(BigDecimal sumaEeff) {
		this.sumaEeff = sumaEeff;
	}
	
	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

}
