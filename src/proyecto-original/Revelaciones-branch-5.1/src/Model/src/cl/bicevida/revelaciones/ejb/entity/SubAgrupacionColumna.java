package cl.bicevida.revelaciones.ejb.entity;


import cl.bicevida.revelaciones.ejb.common.Constantes;
import cl.bicevida.revelaciones.ejb.entity.pk.SubAgrupacionColumnaPK;

import java.io.Serializable;

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


@Entity
@NamedQueries( { 
                    @NamedQuery(name = "SubAgrupacionColumna.findAll", query = "select o from SubAgrupacionColumna o"),
                    @NamedQuery(name = SubAgrupacionColumna.FIND_BY_SUB_GRILLA, query = "select o from SubAgrupacionColumna o where o.idGrilla = :idGrilla and o.idSubGrilla = :idSubGrilla order by o.idNivel, o.idSubColumna, o.grupo ")
                 }
               )
@Table(name = Constantes.REV_SUB_AGRUPACION_COLUMNA)
@IdClass(SubAgrupacionColumnaPK.class)
public class SubAgrupacionColumna implements Serializable {
    
    public static final String FIND_BY_SUB_GRILLA = "SubAgrupacionColumna.findBySubGrilla";
    
    private Long ancho;
    private Long grupo;
    @Id
    @Column(name = "ID_GRILLA", nullable = false, insertable = false, updatable = false)
    private Long idGrilla;
    @Id
    @Column(name = "ID_NIVEL", nullable = false)
    private Long idNivel;
    @Id
    @Column(name = "ID_SUB_COLUMNA", nullable = false, insertable = false, updatable = false)
    private Long idSubColumna;
    @Id
    @Column(name = "ID_SUB_GRILLA", nullable = false, insertable = false, updatable = false)
    private Long idSubGrilla;
    @Column(length = 256)
    private String titulo;
    @ManyToOne
    @JoinColumns( { @JoinColumn(name = "ID_SUB_COLUMNA", referencedColumnName = "ID_SUB_COLUMNA"),
                    @JoinColumn(name = "ID_SUB_GRILLA", referencedColumnName = "ID_SUB_GRILLA"),
                    @JoinColumn(name = "ID_GRILLA", referencedColumnName = "ID_GRILLA") })
    private SubColumna subColumna;

    public SubAgrupacionColumna() {
    }

    public SubAgrupacionColumna(Long ancho, Long grupo, Long idNivel, SubColumna subColumna,
                                   String titulo) {
        this.ancho = ancho;
        this.grupo = grupo;
        this.idNivel = idNivel;
        this.subColumna = subColumna;
        this.titulo = titulo;
    }

    public Long getAncho() {
        return ancho;
    }

    public void setAncho(Long ancho) {
        this.ancho = ancho;
    }

    public Long getGrupo() {
        return grupo;
    }

    public void setGrupo(Long grupo) {
        this.grupo = grupo;
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

    public Long getIdSubColumna() {
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public SubColumna getSubColumna() {
        return subColumna;
    }

    public void setSubColumna(SubColumna subColumna) {
        this.subColumna = subColumna;
        if (subColumna != null) {
            this.idGrilla = subColumna.getIdGrilla();
            this.idSubGrilla = subColumna.getIdSubGrilla();
            this.idSubColumna = subColumna.getIdSubColumna();
        }
    }
}
