package cl.bicevida.revelaciones.ejb.entity;


import cl.bicevida.revelaciones.ejb.common.Constantes;
import cl.bicevida.revelaciones.ejb.entity.pk.SubGrillaPK;

import java.io.Serializable;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@NamedQueries( { @NamedQuery(name = SubGrilla.FIND_ALL, query = "select o from SubGrilla o"),
                 @NamedQuery(name = SubGrilla.FIND_MAX_ID_SUB_GRILLA, query = "select  coalesce (max( o.idSubGrilla ), 1) from SubGrilla o")
                 })
@Table(name = Constantes.REV_SUB_GRILLA)
@IdClass(SubGrillaPK.class)
public class SubGrilla implements Serializable {
    
    public static final String FIND_ALL = "SubGrilla.findAll";
    public static final String FIND_MAX_ID_SUB_GRILLA = "SubGrilla.findMaxIdSubGrilla";
    
    @Id
    @Column(name = "ID_GRILLA", nullable = false, insertable = false, updatable = false)
    private Long idGrilla;
    @Id
    @GeneratedValue(generator="ID_GEN_SUB_GRILLA")
    @SequenceGenerator(name="ID_GEN_SUB_GRILLA", sequenceName = "SEQ_SUB_GRILLA" ,allocationSize = 1)
    @Column(name = "ID_SUB_GRILLA", nullable = false)
    private Long idSubGrilla;
    @Column(name = "TIPO_FORMULA")
    private Long tipoFormula;
    @Column(length = 256)
    private String titulo;
    @ManyToOne
    @JoinColumn(name = "ID_GRILLA")
    private Grilla grilla;
    @OneToMany(mappedBy = "subGrilla", cascade = CascadeType.ALL)
    private List<SubColumna> subColumnaList;
    
    @ManyToOne
    @JoinColumn(name = "ID_GRUPO_ACCESO")
    private Grupo grupo;
    
    @Column(name = "AGRUPADOR")
    private Integer agrupador;

    public SubGrilla() {
    }

    public SubGrilla(Grilla grilla, Long idSubGrilla, Long tipoFormula, String titulo) {
        this.grilla = grilla;
        this.idSubGrilla = idSubGrilla;
        this.tipoFormula = tipoFormula;
        this.titulo = titulo;
    }

    public Long getIdGrilla() {
        return idGrilla;
    }

    public void setIdGrilla(Long idGrilla) {
        this.idGrilla = idGrilla;
    }

    public Long getIdSubGrilla() {
        
        if (idSubGrilla == null){
                idSubGrilla = 0L;
            }
        
        return idSubGrilla;
    }

    public void setIdSubGrilla(Long idSubGrilla) {
        this.idSubGrilla = idSubGrilla;
    }

    public Long getTipoFormula() {
        return tipoFormula;
    }

    public void setTipoFormula(Long tipoFormula) {
        this.tipoFormula = tipoFormula;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Grilla getGrilla() {
        return grilla;
    }

    public void setGrilla(Grilla grilla) {
        this.grilla = grilla;
        if (grilla != null) {
            this.idGrilla = grilla.getIdGrilla();
        }
    }

    public List<SubColumna> getSubColumnaList() {
        return subColumnaList;
    }

    public void setSubColumnaList(List<SubColumna> subColumnaList) {
        this.subColumnaList = subColumnaList;
    }

    public SubColumna addSubColumna(SubColumna subColumna) {
        getSubColumnaList().add(subColumna);
        subColumna.setSubGrilla(this);
        return subColumna;
    }

    public SubColumna removeSubColumna(SubColumna subColumna) {
        getSubColumnaList().remove(subColumna);
        subColumna.setSubGrilla(null);
        return subColumna;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setAgrupador(Integer agrupador) {
        this.agrupador = agrupador;
    }

    public Integer getAgrupador() {
        return agrupador;
    }
}
