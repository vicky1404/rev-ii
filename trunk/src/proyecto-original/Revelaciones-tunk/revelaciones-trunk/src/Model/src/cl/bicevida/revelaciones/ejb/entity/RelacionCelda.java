package cl.bicevida.revelaciones.ejb.entity;


import cl.bicevida.revelaciones.ejb.common.Constantes;
import cl.bicevida.revelaciones.ejb.entity.pk.RelacionCeldaPK;

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
@NamedQueries( { @NamedQuery(name = "RelacionCelda.findAll", query = "select o from RelacionCelda o") })
@Table(name = Constantes.REV_RELACION_CELDA)
@IdClass(RelacionCeldaPK.class)
public class RelacionCelda implements Serializable {
    @Id
    @Column(name = "ID_COLUMNA", nullable = false, insertable = false, updatable = false)
    private Long idColumna;
    @Id
    @Column(name = "ID_COLUMNA_REL", nullable = false, insertable = false, updatable = false)
    private Long idColumnaRel;
    @Id
    @Column(name = "ID_FILA", nullable = false, insertable = false, updatable = false)
    private Long idFila;
    @Id
    @Column(name = "ID_FILA_REL", nullable = false, insertable = false, updatable = false)
    private Long idFilaRel;
    @Id
    @Column(name = "ID_GRILLA", nullable = false, insertable = false, updatable = false)
    private Long idGrilla;
    @Id
    @Column(name = "ID_GRILLA_REL", nullable = false, insertable = false, updatable = false)
    private Long idGrillaRel;
    @ManyToOne
    @JoinColumns( { @JoinColumn(name = "ID_COLUMNA_REL", referencedColumnName = "ID_COLUMNA"),
                    @JoinColumn(name = "ID_GRILLA_REL", referencedColumnName = "ID_GRILLA"),
                    @JoinColumn(name = "ID_FILA_REL", referencedColumnName = "ID_FILA") })
    private Celda celdaRelacionada;
    @ManyToOne
    @JoinColumns( { @JoinColumn(name = "ID_COLUMNA", referencedColumnName = "ID_COLUMNA"),
                    @JoinColumn(name = "ID_GRILLA", referencedColumnName = "ID_GRILLA"),
                    @JoinColumn(name = "ID_FILA", referencedColumnName = "ID_FILA") })
    private Celda celda;
    @Id
    @Column(name = "ID_PERIODO", nullable = false, insertable = false, updatable = false)
    private Long idPeriodo;
    @ManyToOne
    @JoinColumn(name = "ID_PERIODO")
    private Periodo periodo;



    public RelacionCelda() {
        
    }


    public RelacionCelda(Celda celda, Celda celdaRelacionada) {
        this.celda = celda;
        this.celdaRelacionada = celdaRelacionada;
    }

    public Long getIdColumna() {
        return idColumna;
    }

    public void setIdColumna(Long idColumna) {
        this.idColumna = idColumna;
    }

    public Long getIdColumnaRel() {
        return idColumnaRel;
    }

    public void setIdColumnaRel(Long idColumnaRel) {
        this.idColumnaRel = idColumnaRel;
    }

    public Long getIdFila() {
        return idFila;
    }

    public void setIdFila(Long idFila) {
        this.idFila = idFila;
    }

    public Long getIdFilaRel() {
        return idFilaRel;
    }

    public void setIdFilaRel(Long idFilaRel) {
        this.idFilaRel = idFilaRel;
    }

    public Long getIdGrilla() {
        return idGrilla;
    }

    public void setIdGrilla(Long idGrilla) {
        this.idGrilla = idGrilla;
    }

    public Long getIdGrillaRel() {
        return idGrillaRel;
    }

    public void setIdGrillaRel(Long idGrillaRel) {
        this.idGrillaRel = idGrillaRel;
    }

    public Celda getCeldaRelacionada() {
        return celdaRelacionada;
    }

    public void setCeldaRelacionada(Celda celdaRelacionada) {
        this.celdaRelacionada = celdaRelacionada;
        if (celdaRelacionada != null) {
            this.idFilaRel = celdaRelacionada.getIdFila();
            this.idGrillaRel = celdaRelacionada.getIdGrilla();
            this.idColumnaRel = celdaRelacionada.getIdColumna();
        }
    }

    public Celda getCelda() {
        return celda;
    }

    public void setCelda(Celda celda) {
        this.celda = celda;
        if (celda != null) {
            this.idFila = celda.getIdFila();
            this.idGrilla = celda.getIdGrilla();
            this.idColumna = celda.getIdColumna();
        }
    }
    
    public void copyCelda(Celda celda, Celda celdaRelacionada, Periodo periodo){
        
        this.setIdColumna(celda.getIdColumna());
        this.setIdFila(celda.getIdFila());
        this.setIdGrilla(celda.getIdGrilla());
        
        this.setIdGrillaRel(celdaRelacionada.getIdGrilla());
        this.setIdColumnaRel(celdaRelacionada.getIdColumna());
        this.setIdFilaRel(celdaRelacionada.getIdFila());
        
        this.setIdPeriodo(periodo.getIdPeriodo());
   }

    public void setIdPeriodo(Long idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public Long getIdPeriodo() {
        return idPeriodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Periodo getPeriodo() {
        return periodo;
    }
}
