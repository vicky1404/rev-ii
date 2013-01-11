package cl.bicevida.revelaciones.ejb.entity;


import cl.bicevida.revelaciones.ejb.common.Constantes;
import cl.bicevida.revelaciones.ejb.common.TipoCeldaEnum;
import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.revelaciones.ejb.entity.pk.CeldaPK;

import java.io.Serializable;

import java.lang.Long;

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


@Entity
@NamedQueries( { @NamedQuery(name = "Celda.findAll", query = "select o from Celda o"), 
                 @NamedQuery(name = Celda.CELDA_FIND_BY_COLUMNA, query = "select o from Celda o where o.columna = :columna"),
                 /*@NamedQuery(name = Celda.CELDA_FIND_BY_GRUPO, query = "select o from Celda o where o.grupo = :grupo and o.idGrilla = :idGrilla "),
                 @NamedQuery(name = Celda.CELDA_FIND_MAX_GRUPO, query = "select max(o.grupo) from Celda o where o.idGrilla = :idGrilla "),*/
                 @NamedQuery(name = Celda.CELDA_FIND_BY_ID_FILA, query = "select o from Celda o where o.idGrilla = :idGrilla and o.idFila = :idFila order by o.idColumna"),
                 @NamedQuery(name = Celda.FIND_BY_EEFF, query =     "select c from Celda c, RelacionEeff        r where c.idGrilla = r.idGrilla and c.idColumna = r.idColumna and c.idFila = r.idFila and r.periodo.idPeriodo = :idPeriodo and r.idFecu = :idFecu order by c.idColumna , c.idFila"),
                 @NamedQuery(name = Celda.FIND_BY_EEFF_DET, query = "select c from Celda c, RelacionDetalleEeff r where c.idGrilla = r.idGrilla and c.idColumna = r.idColumna and c.idFila = r.idFila and r.periodo.idPeriodo = :idPeriodo and r.idCuenta = :idCuenta and r.idFecu = :idFecu order by c.idColumna , c.idFila")
                 })
@Table(name = Constantes.REV_CELDA)
@IdClass(CeldaPK.class)
public class Celda implements Serializable {
    
    public final static String CELDA_FIND_BY_COLUMNA = "Celda.findCeldaByColumna";
    public final static String CELDA_FIND_BY_GRUPO = "Celda.findCeldaByGrupo";
    public final static String CELDA_FIND_MAX_GRUPO = "Celda.findCeldaMaxGrupo";
    public final static String CELDA_FIND_BY_ID_FILA = "Celda.findCeldaByIdFila";
    public final static String FIND_BY_EEFF = "Celda.findCeldaByEeff";
    public final static String FIND_BY_EEFF_DET = "Celda.findCeldaByEeffDet";
    
    @SuppressWarnings("compatibility:7756436096828081048")
    private static final long serialVersionUID = -3518647074958736787L;

    @Id
    @Column(name = "ID_COLUMNA", nullable = false, insertable = false, updatable = false)
    private Long idColumna;
    
    @Id
    @Column(name = "ID_FILA", nullable = false, insertable = true, updatable = true)
    private Long idFila;
    
    @Id
    @Column(name = "ID_GRILLA", nullable = false, insertable = false, updatable = false)
    private Long idGrilla;
    
    @Column(nullable = false, length = 2048)
    private String valor;
    
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "ID_TIPO_CELDA")
    private TipoCelda tipoCelda;
    
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "ID_TIPO_DATO")
    private TipoDato tipoDato;
    
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "ID_COLUMNA", referencedColumnName = "ID_COLUMNA"),
        @JoinColumn(name = "ID_GRILLA", referencedColumnName = "ID_GRILLA")
    })
    private Columna columna;
    
    @Column(name="CHILD_HORIZONTAL")
    private Long childHorizontal;
    
    @Column(name="PARENT_HORIZONTAL")
    private Long parentHorizontal;
        
    @Column(name="CHILD_VERTICAL")
    private Long childVertical;
    
    @Column(name="PARENT_VERTICAL")
    private Long parentVertical;
    
    @Column(name="FORMULA")
    private String formula;
    
    @OneToMany(mappedBy = "celda2", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<RelacionEeff> relacionEeffList;
    
    @OneToMany(mappedBy = "celda5", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<RelacionDetalleEeff> relacionDetalleEeffList;
    
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
    
    @Transient
    private boolean selectedByMapping;
    
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
        //celdaNew.setRelacionEeffList(celda.getRelacionEeffList());
        //celdaNew.setRelacionDetalleEeffList(celda.getRelacionDetalleEeffList());
        return celdaNew;
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
    
    public int getSizeRelacionEeffList(){
        
        if(relacionEeffList==null)
            return 0;
        else
            return relacionEeffList.size();
    }
    
    public int getSizeRelacionDetalleEeffList(){
        if(getRelacionDetalleEeffList()==null)
            return 0;
        else
            return getRelacionDetalleEeffList().size();
    }

    public void setTieneRelEeff(boolean tieneRelEeff) {
        this.tieneRelEeff = tieneRelEeff;
    }

    public boolean isTieneRelEeff() {
        return tieneRelEeff;
    }

    public void setSumaEeff(BigDecimal sumaEeff) {
        this.sumaEeff = sumaEeff;
    }

    public BigDecimal getSumaEeff() {
        return sumaEeff;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isValid() {
        return valid;
    }
    
    public void setSelectedByMapping(boolean selectedByMapping) {
        this.selectedByMapping = selectedByMapping;
    }

    public boolean isSelectedByMapping() {
        return selectedByMapping;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("columna=");
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
}
