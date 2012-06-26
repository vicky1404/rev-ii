package cl.mdr.ifrs.ejb.entity;


import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import cl.mdr.ifrs.ejb.common.Constantes;
import cl.mdr.ifrs.ejb.entity.pk.ColumnaPK;


@Entity
@NamedQueries( { @NamedQuery(name = "Columna.findAll", query = "select o from Columna o") })
@Table(name = Constantes.COLUMNA)
@IdClass(ColumnaPK.class)
public class Columna implements Serializable {

    private static final long serialVersionUID = 8480508449730276846L;
    private Long ancho;
    
    @Id
    @Column(name = "ID_COLUMNA", nullable = false)
    private Long idColumna;
    
    @Id
    @Column(name = "ID_GRILLA", nullable = false, insertable = false, updatable = false)
    private Long idGrilla;
    
    @Column(nullable = false)
    private Long orden;
    
    @Column(name = "TITULO_COLUMNA", nullable = false, length = 128)
    private String tituloColumna;
    
    @OneToMany(mappedBy = "columna" , fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy("idColumna asc ,idFila asc")
    private List<Celda> celdaList;
    
    @ManyToOne
    @JoinColumn(name = "ID_GRILLA")
    private Grilla grilla;
    
    @Column(name = "ROW_HEADER")
    private boolean rowHeader;
    
    
    
    @OneToMany(mappedBy = "columna", fetch = FetchType.EAGER)
    private List<AgrupacionColumna> agrupacionColumnaList;
    
    @Transient
    private Celda celdaModel;    
    
    @Transient
    private Long desde;
    
    @Transient
    private Long hasta;
    
    
    public Columna() {
    }

    public Columna(Long ancho, Long idColumna, Grilla grilla, Long orden,
                       String tituloColumna) {
        this.ancho = ancho;
        this.idColumna = idColumna;
        this.grilla = grilla;
        this.orden = orden;
        this.tituloColumna = tituloColumna;
    }


    public Long getAncho() {
        return ancho;
    }

    public void setAncho(Long ancho) {
        this.ancho = ancho;
    }

    public Long getIdColumna() {
        return idColumna;
    }

    public void setIdColumna(Long idColumna) {
        this.idColumna = idColumna;
    }

    public Long getIdGrilla() {
        return idGrilla;
    }

    public void setIdGrilla(Long idGrilla) {
        this.idGrilla = idGrilla;
    }

    public Long getOrden() {
        return orden;
    }

    public void setOrden(Long orden) {
        this.orden = orden;
    }

    public String getTituloColumna() {
        return tituloColumna;
    }

    public void setTituloColumna(String tituloColumna) {
        this.tituloColumna = tituloColumna;
    }

    public List<Celda> getCeldaList() {
        return celdaList;
    }

    public void setCeldaList(List<Celda> celdaNotaList) {
        this.celdaList = celdaNotaList;
    }

    public Celda addCeldaNota(Celda celdaNota) {
        getCeldaList().add(celdaNota);
        celdaNota.setColumna(this);
        return celdaNota;
    }

    public Celda removeCeldaNota(Celda celdaNota) {
        getCeldaList().remove(celdaNota);
        celdaNota.setColumna(null);
        return celdaNota;
    }

    public Grilla getGrilla() {
        return grilla;
    }

    public void setGrilla(Grilla grillaNota) {
        this.grilla = grillaNota;
        if (grillaNota != null) {
            this.idGrilla = grillaNota.getIdGrilla();
        }
    }

    public void setRowHeader(boolean rowHeader) {
        this.rowHeader = rowHeader;
    }

    public boolean isRowHeader() {
        return rowHeader;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("ancho=");
        buffer.append(getAncho());
        buffer.append(',');
        buffer.append("idColumna=");
        buffer.append(getIdColumna());
        buffer.append(',');
        buffer.append("orden=");
        buffer.append(getOrden());
        buffer.append(',');
        buffer.append("tituloColumna=");
        buffer.append(getTituloColumna());
        buffer.append(']');
        return buffer.toString();
    }

    public void setCeldaModel(Celda celdaModel) {
        this.celdaModel = celdaModel;
    }

    public Celda getCeldaModel() {
        if(celdaModel==null){
            celdaModel = new Celda();       
        }
        return celdaModel;
    }
    public void setDesde(Long desde) {
        this.desde = desde;
    }

    public Long getDesde() {
        return desde;
    }

    public void setHasta(Long hasta) {
        this.hasta = hasta;
    }

    public Long getHasta() {
        return hasta;
    }

    public void setAgrupacionColumnaList(List<AgrupacionColumna> agrupacionColumnaList) {
        this.agrupacionColumnaList = agrupacionColumnaList;
    }

    public List<AgrupacionColumna> getAgrupacionColumnaList() {
        return agrupacionColumnaList;
    }

  
}
