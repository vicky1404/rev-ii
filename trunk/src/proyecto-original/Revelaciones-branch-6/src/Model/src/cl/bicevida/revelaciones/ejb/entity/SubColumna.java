package cl.bicevida.revelaciones.ejb.entity;


import cl.bicevida.revelaciones.ejb.common.Constantes;
import cl.bicevida.revelaciones.ejb.entity.pk.SubColumnaPK;

import java.io.Serializable;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@NamedQueries( { @NamedQuery(name = SubColumna.FIND_ALL, query = "select o from SubColumna o"),
                 @NamedQuery(name = SubColumna.FIND_MAX_ID_SUB_COLUMNA, query = "select  coalesce(max( o.idSubColumna), 1) from SubColumna o")})
@Table(name = Constantes.REV_SUB_COLUMNA)
@IdClass(SubColumnaPK.class)

public class SubColumna implements Serializable {
    
    public static final String FIND_ALL = "SubColumna.findAll";
    public static final String FIND_MAX_ID_SUB_COLUMNA = "SubColumna.findMaxIdSubColumna";

    
    private Long ancho;
    @Id
    @Column(name = "ID_GRILLA", nullable = false, insertable = false, updatable = false)
    private Long idGrilla;
    @Id
    @GeneratedValue(generator="ID_GEN_SUB_COLUMNA")
    @SequenceGenerator(name="ID_GEN_SUB_COLUMNA", sequenceName = "SEQ_SUB_COLUMNA" ,allocationSize = 1)
    @Column(name = "ID_SUB_COLUMNA", nullable = false)
    private Long idSubColumna;
    @Id
    @Column(name = "ID_SUB_GRILLA", nullable = false, insertable = false, updatable = false)
    private Long idSubGrilla;
    private Long orden;
    @Column(name = "ROW_HEADER")
    private boolean rowHeader;
    @Column(name = "TITULO_COLUMNA", length = 128)
    private String tituloColumna;
    @ManyToOne
    @JoinColumns( { @JoinColumn(name = "ID_SUB_GRILLA", referencedColumnName = "ID_SUB_GRILLA"),
                    @JoinColumn(name = "ID_GRILLA", referencedColumnName = "ID_GRILLA") })
    private SubGrilla subGrilla;
    @OneToMany(mappedBy = "subColumna", cascade = CascadeType.ALL)
    private List<SubAgrupacionColumna> subAgrupacionColumnaList;
    
    @OneToMany(mappedBy = "subColumna", cascade = CascadeType.ALL)
    private List<SubCelda> subCeldaList;
    
    public SubColumna() {
    }

    public SubColumna(Long ancho, Long idSubColumna, SubGrilla subGrilla, Long orden,
                         boolean rowHeader, String tituloColumna) {
        this.ancho = ancho;
        this.idSubColumna = idSubColumna;
        this.subGrilla = subGrilla;
        this.orden = orden;
        this.rowHeader = rowHeader;
        this.tituloColumna = tituloColumna;
    }

    public Long getAncho() {
        return ancho;
    }

    public void setAncho(Long ancho) {
        this.ancho = ancho;
    }

    public Long getIdGrilla() {
        return idGrilla;
    }

    public void setIdGrilla(Long idGrilla) {
        this.idGrilla = idGrilla;
    }

    public Long getIdSubColumna() {
        
        if (idSubColumna == null){
                idSubColumna = 0L;
            }
        
        return idSubColumna;
    }

    public void setIdSubColumna(Long idSubColumna) {
        this.idSubColumna = idSubColumna;
    }

    public Long getIdSubGrilla() {
        return idSubGrilla;
    }

    public void setIdSubGrilla(Long idSubGrilla) {
        this.idSubGrilla = idSubGrilla;
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

    public SubGrilla getSubGrilla() {
        return subGrilla;
    }

    public void setSubGrilla(SubGrilla subGrilla) {
        this.subGrilla = subGrilla;
        if (subGrilla != null) {
            this.idGrilla = subGrilla.getIdGrilla();
            this.idSubGrilla = subGrilla.getIdSubGrilla();
        }
    }

    public List<SubAgrupacionColumna> getSubAgrupacionColumnaList() {
        return subAgrupacionColumnaList;
    }

    public void setSubAgrupacionColumnaList(List<SubAgrupacionColumna> subAgrupacionColumnaList) {
        this.subAgrupacionColumnaList = subAgrupacionColumnaList;
    }

    public SubAgrupacionColumna addSubAgrupacionColumna(SubAgrupacionColumna subAgrupacionColumna) {
        getSubAgrupacionColumnaList().add(subAgrupacionColumna);
        subAgrupacionColumna.setSubColumna(this);
        return subAgrupacionColumna;
    }

    public SubAgrupacionColumna removeSubAgrupacionColumna(SubAgrupacionColumna subAgrupacionColumna) {
        getSubAgrupacionColumnaList().remove(subAgrupacionColumna);
        subAgrupacionColumna.setSubColumna(null);
        return subAgrupacionColumna;
    }


    public void setSubCeldaList(List<SubCelda> subCeldaList) {
        this.subCeldaList = subCeldaList;
    }

    public List<SubCelda> getSubCeldaList() {
        return subCeldaList;
    }

    public void setRowHeader(boolean rowHeader) {
        this.rowHeader = rowHeader;
    }

    public boolean isRowHeader() {
        return rowHeader;
    }
}
