package cl.bicevida.revelaciones.ejb.entity;


import cl.bicevida.revelaciones.ejb.common.Constantes;
import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.revelaciones.ejb.entity.pk.SubCeldaPK;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@NamedQueries( { @NamedQuery(name = "SubCelda.findAll", query = "select o from SubCelda o")})
@Table(name = Constantes.REV_SUB_CELDA)
@IdClass(SubCeldaPK.class)
public class SubCelda implements Serializable {
    @Column(name = "CHILD_HORIZONTAL")
    private Long childHorizontal;
    @Column(name = "CHILD_VERTICAL")
    private Long childVertical;
    @Column(length = 256)
    private String formula;
    @Id
    @Column(name = "ID_GRILLA", nullable = false, insertable = false, updatable = false)
    private Long idGrilla;
    @Id
    @Column(name = "ID_SUB_COLUMNA", nullable = false, insertable = false, updatable = false)
    private Long idSubColumna;
    @Id
    @Column(name = "ID_SUB_FILA", nullable = false)
    private Long idSubFila;
    @Id
    @Column(name = "ID_SUB_GRILLA", nullable = false, insertable = false, updatable = false)
    private Long idSubGrilla;
    @Column(name = "PARENT_HORIZONTAL")
    private Long parentHorizontal;
    @Column(name = "PARENT_VERTICAL")
    private Long parentVertical;
    @Column(length = 2048)
    private String valor;
    @ManyToOne
    @JoinColumn(name = "ID_TIPO_DATO")
    private TipoDato tipoDato;
    @ManyToOne
    @JoinColumn(name = "ID_TIPO_CELDA")
    private TipoCelda tipoCelda;
    
    @ManyToOne
    @JoinColumns( { @JoinColumn(name = "ID_SUB_COLUMNA", referencedColumnName = "ID_SUB_COLUMNA"),
                    @JoinColumn(name = "ID_SUB_GRILLA", referencedColumnName = "ID_SUB_GRILLA"),
                    @JoinColumn(name = "ID_GRILLA", referencedColumnName = "ID_GRILLA") })
    private SubColumna subColumna;
    
    
    @Transient
    private boolean esNumero;
    
    @Transient
    private Long valorLong;
    
    @Transient
    private BigDecimal valorBigDecimal;
    
    @Transient
    private Date valorDate;
    
    @Transient
    private boolean selectedByFormula;
    
    @Transient
    private boolean tieneRelEeff = false;
    
    @Transient
    private BigDecimal sumaEeff;
    
    @Transient
    private boolean valid = true;
    
    public SubCelda() {
    }

    public SubCelda(Long childHorizontal, Long childVertical, String formula, Long idGrilla,
                       Long idSubColumna, Long idSubFila, Long idSubGrilla,
                       TipoCelda tipoCelda, TipoDato tipoDato, Long parentHorizontal,
                       Long parentVertical, String valor, SubColumna subColumna) {
        this.childHorizontal = childHorizontal;
        this.childVertical = childVertical;
        this.formula = formula;
        this.idGrilla = idGrilla;
        this.idSubColumna = idSubColumna;
        this.idSubFila = idSubFila;
        this.idSubGrilla = idSubGrilla;
        this.tipoCelda = tipoCelda;
        this.tipoDato = tipoDato;
        this.parentHorizontal = parentHorizontal;
        this.parentVertical = parentVertical;
        this.valor = valor;
        this.subColumna = subColumna;
    }

    public Long getChildHorizontal() {
        return childHorizontal;
    }

    public void setChildHorizontal(Long childHorizontal) {
        this.childHorizontal = childHorizontal;
    }

    public Long getChildVertical() {
        return childVertical;
    }

    public void setChildVertical(Long childVertical) {
        this.childVertical = childVertical;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public Long getIdGrilla() {
        return idGrilla;
    }

    public void setIdGrilla(Long idGrilla) {
        this.idGrilla = idGrilla;
    }

    public Long getIdSubColumna() {
        return idSubColumna;
    }

    public void setIdSubColumna(Long idSubColumna) {
        this.idSubColumna = idSubColumna;
    }

    public Long getIdSubFila() {
        return idSubFila;
    }

    public void setIdSubFila(Long idSubFila) {
        this.idSubFila = idSubFila;
    }

    public Long getIdSubGrilla() {
        return idSubGrilla;
    }

    public void setIdSubGrilla(Long idSubGrilla) {
        this.idSubGrilla = idSubGrilla;
    }


    public Long getParentHorizontal() {
        return parentHorizontal;
    }

    public void setParentHorizontal(Long parentHorizontal) {
        this.parentHorizontal = parentHorizontal;
    }

    public Long getParentVertical() {
        return parentVertical;
    }

    public void setParentVertical(Long parentVertical) {
        this.parentVertical = parentVertical;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public TipoDato getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(TipoDato tipoDato) {
        this.tipoDato = tipoDato;
    }

    public TipoCelda getTipoCelda() {
        return tipoCelda;
    }

    public void setTipoCelda(TipoCelda tipoCelda) {
        this.tipoCelda = tipoCelda;
    }

    public void setSubColumna(SubColumna subColumna) {
        this.subColumna = subColumna;
    }

    public SubColumna getSubColumna() {
        return subColumna;
    }
    
    public void setValorLong(Long valorLong) {         
        if(valorLong == null || valorLong.equals(0L)){
            this.setValor("0");
        }else{
            this.setValor(Util.getString(valorLong, "0"));        
        }
    }

    public Long getValorLong() {
        return Util.getLong(getValor(), 0L);
    }

    public void setValorBigDecimal(BigDecimal valorBigDecimal) {        
        if(valorBigDecimal == null || valorBigDecimal.equals(new BigDecimal(0))){
            this.setValor("0");
        }else{
            this.setValor(Util.getString(valorBigDecimal, "0"));        
        }
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
    
    public SubCelda(Long idSubColumna, Long idSubFila) {
        this.idSubColumna = idSubColumna;
        this.idSubFila = idSubFila;
    }
}
