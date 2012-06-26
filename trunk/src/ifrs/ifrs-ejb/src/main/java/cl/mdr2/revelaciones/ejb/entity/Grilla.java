package cl.bicevida.revelaciones.ejb.entity;


import cl.bicevida.revelaciones.ejb.common.Constantes;

import java.io.Serializable;

import java.lang.Long;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;


@Entity
@NamedQueries( { @NamedQuery(name = Grilla.FIND_ALL, query = "select o from Grilla o"),
                 @NamedQuery(name = Grilla.FIND_GRILLA_BY_ID , query = "select o from Grilla o where o.idGrilla = :idGrilla ")
                 
                 })
@Table(name = Constantes.GRILLA)
public class Grilla implements Serializable {
    
    public static final String FIND_ALL = "Grilla.findAll";
    public static final String FIND_GRILLA_BY_ID = "Grilla.findGrillaById";
    
    public static final Long TIPO_GRILLA_ESTATICA = 2L;
    public static final Long TIPO_GRILLA_DINAMICA = 1L;
    
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = -6769823371555303401L;
    @Id
    @Column(name = "ID_GRILLA",insertable = true)
    private Long idGrilla;
    @Column(length = 256)
    private String titulo;
    @OneToMany(mappedBy = "grilla",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy("idColumna asc")            
    private List<Columna> columnaList;
    @ManyToOne(targetEntity = Estructura.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_GRILLA", insertable = false, updatable = false)
    private Estructura estructura1;
    @Column(name="TIPO_FORMULA")
    private Long tipoFormula;
   
    

    public Grilla() {
    }

    public Grilla(Estructura estructura, Long idGrilla, String titulo) {
        this.estructura1 = estructura;
        this.idGrilla = idGrilla;
        this.titulo = titulo;
    }


    public Long getIdGrilla() {
        /*if(estructura1!=null){
            return estructura1.getIdEstructura();
        }*/
        return idGrilla;
    }

    public void setIdGrilla(Long idGrilla) {
        this.idGrilla = idGrilla;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Columna> getColumnaList() {
        return columnaList;
    }

    public void setColumnaList(List<Columna> columnaList) {
        this.columnaList = columnaList;
    }

    public Columna addColumna(Columna columna) {
        getColumnaList().add(columna);
        columna.setGrilla(this);
        return columna;
    }

    public Columna removeColumna(Columna columna) {
        getColumnaList().remove(columna);
        columna.setGrilla(null);
        return columna;
    }

    public Estructura getEstructura1() {
        return estructura1;
    }

    public void setEstructura1(Estructura estructuraNota) {
        this.estructura1 = estructuraNota;
    }
    
    

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idGrilla=");
        buffer.append(getIdGrilla());
        buffer.append(',');
        buffer.append("titulo=");
        buffer.append(getTitulo());
        buffer.append(']');
        return buffer.toString();
    }

    public void setTipoFormula(Long tipoFormula) {
        this.tipoFormula = tipoFormula;
    }

    public Long getTipoFormula() {
        return tipoFormula;
    }
    
    public static Grilla clone(Grilla grid){
        Grilla gridNew = new Grilla();
        gridNew.setIdGrilla(grid.getIdGrilla());
        gridNew.setTitulo(grid.getTitulo());
        gridNew.setColumnaList(grid.getColumnaList());
        gridNew.setEstructura1(grid.getEstructura1());
        gridNew.setTipoFormula(grid.getTipoFormula());
        return gridNew;
    }
}
