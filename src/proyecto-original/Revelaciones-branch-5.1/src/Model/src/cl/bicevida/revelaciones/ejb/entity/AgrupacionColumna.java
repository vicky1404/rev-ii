package cl.bicevida.revelaciones.ejb.entity;


import cl.bicevida.revelaciones.ejb.common.Constantes;
import cl.bicevida.revelaciones.ejb.entity.pk.AgrupacionColumnaPK;

import java.io.Serializable;

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


@Entity
@NamedQueries( { @NamedQuery(name = AgrupacionColumna.FIND_ALL, query = "select o from AgrupacionColumna o") ,
                 @NamedQuery(name = AgrupacionColumna.FIND_BY_GRILLA, query = "select o from AgrupacionColumna o where o.idGrilla = :idGrilla order by o.idNivel, o.idColumna, o.grupo "),
                 @NamedQuery(name = AgrupacionColumna.FIND_BY_ID, query = "select o from AgrupacionColumna o where o.idColumna =:idColumna and o.idGrilla =:idGrilla and o.idNivel =:idNivel"),
                 @NamedQuery(name = AgrupacionColumna.FIND_BY_GRILLA_GRUPO, query = "select o from AgrupacionColumna o where o.idGrilla = :idGrilla and o.idNivel = :idNivel order by o.idNivel, o.idColumna, o.grupo ")
                 })
@Table(name = Constantes.REV_AGRUPACION_COLUMNA)
@IdClass(AgrupacionColumnaPK.class)
public class  AgrupacionColumna implements Serializable, Comparable<AgrupacionColumna> {
    
    public static final String FIND_ALL = "AgrupacionColumna.findAll";
    public static final String FIND_BY_GRILLA = "AgrupacionColumna.findByGrilla";
    public static final String FIND_BY_ID = "AgrupacionColumna.findByID";  
    public static final String FIND_BY_GRILLA_GRUPO = "AgrupacionColumna.findByGrillaGrupo"; 
    
    @Column(nullable = false)
    private Long ancho;
    @Id
    @Column(name = "ID_COLUMNA", nullable = false, insertable = false, updatable = false)
    private Long idColumna;
    @Id
    @Column(name = "ID_GRILLA", nullable = false, insertable = false, updatable = false)
    private Long idGrilla;
    @Id
    @Column(name = "ID_NIVEL", nullable = false)
    private Long idNivel;
    @Column(length = 256)
    private String titulo;
    
    @Column
    private Long grupo;
    
    @Transient
    private boolean renderAbrirColumna;
    @Transient
    private boolean renderCerrarColumna;

    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns( { @JoinColumn(name = "ID_COLUMNA", referencedColumnName = "ID_COLUMNA"),
                    @JoinColumn(name = "ID_GRILLA", referencedColumnName = "ID_GRILLA") })
    private Columna columna;

    public AgrupacionColumna() {
    }

    public AgrupacionColumna(Long ancho, Columna columna, Long idNivel, String titulo, Long grupo, Long idGrilla, Long idColumna) {
        this.ancho = ancho;
        this.columna = columna;
        this.idNivel = idNivel;
        this.titulo = titulo;
        this.grupo = grupo;
        this.idGrilla = idGrilla;
        this.idColumna = idColumna;
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

    public Long getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(Long idNivel) {
        this.idNivel = idNivel;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
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
    
    @Override        
    public int compareTo(AgrupacionColumna agrupacion){
        
        if(getIdNivel() < agrupacion.getIdNivel()){
            return -1;
        }else if(getIdNivel() == agrupacion.getIdNivel()){
            return 0;
        }else{
            return 1;
        }
        
    }

    public void setGrupo(Long grupo) {
        this.grupo = grupo;
    }

    public Long getGrupo() {
        return grupo;
    }

    public void setRenderAbrirColumna(boolean renderAbrirColumna) {
        this.renderAbrirColumna = renderAbrirColumna;
    }

    public boolean isRenderAbrirColumna() {
        return renderAbrirColumna;
    }

    public void setRenderCerrarColumna(boolean renderCerrarColumna) {
        this.renderCerrarColumna = renderCerrarColumna;
    }

    public boolean isRenderCerrarColumna() {
        return renderCerrarColumna;
    }
    
    public static AgrupacionColumna clone(AgrupacionColumna agrupacion){
        AgrupacionColumna agrupacionNew = new AgrupacionColumna();
        agrupacionNew.setAncho(agrupacion.getAncho());
        agrupacionNew.setColumna(agrupacion.getColumna());
        agrupacionNew.setGrupo(agrupacion.getGrupo());
        agrupacionNew.setIdColumna(agrupacion.getIdColumna());
        agrupacionNew.setIdNivel(agrupacion.getIdNivel());
        agrupacionNew.setTitulo(agrupacion.getTitulo());
        return agrupacionNew;
    }
}
