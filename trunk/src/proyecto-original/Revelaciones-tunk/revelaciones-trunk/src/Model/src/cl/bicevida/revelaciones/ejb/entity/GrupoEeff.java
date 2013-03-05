package cl.bicevida.revelaciones.ejb.entity;


import cl.bicevida.revelaciones.ejb.common.Constantes;

import java.io.Serializable;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@NamedQueries( { @NamedQuery(name = GrupoEeff.FIND_ALL, query = "select o from GrupoEeff o order by o.orden asc ") })
@Table(name = Constantes.REV_GRUPO_EEFF)
public class GrupoEeff implements Serializable {
    
    public static final String FIND_ALL = "GrupoEeff.findAll";

    @Id
    @GeneratedValue(generator="ID_GEN_GRUPO_EEFF")
    @SequenceGenerator(name="ID_GEN_GRUPO_EEFF", sequenceName = "SEQ_GRUPO_EEFF" ,allocationSize = 1)
    @Column(name = "ID_GRUPO_EEFF", nullable = false)
    private Long idGrupoEeff;
    
    @Column(nullable = false, length = 256)
    private String descripcion;
    
    @Column(name = "VIGENCIA")
    private boolean vigencia;
    
    @Column(name = "ORDEN")
    private Long orden;
    
    @OneToMany(mappedBy = "GrupoEeff")
    private List<CodigoFecu> codigoFecuList;

    public GrupoEeff() {
    }

    public GrupoEeff(String descripcion, Long idGrupoEeff, boolean vigencia) {
        this.descripcion = descripcion;
        this.idGrupoEeff = idGrupoEeff;
        this.vigencia = vigencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getIdGrupoEeff() {
        return idGrupoEeff;
    }

    public void setIdGrupoEeff(Long idGrupoEeff) {
        this.idGrupoEeff = idGrupoEeff;
    }

   

   

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof GrupoEeff)) {
            return false;
        }
        final GrupoEeff other = (GrupoEeff)object;
        if (!(idGrupoEeff == null ? other.idGrupoEeff == null : idGrupoEeff.equals(other.idGrupoEeff))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = 1;
        result = PRIME * result + ((idGrupoEeff == null) ? 0 : idGrupoEeff.hashCode());
        return result;
    }

    public void setCodigoFecuList(List<CodigoFecu> codigoFecuList) {
        this.codigoFecuList = codigoFecuList;
    }

    public List<CodigoFecu> getCodigoFecuList() {
        return codigoFecuList;
    }

    public void setVigencia(boolean vigencia) {
        this.vigencia = vigencia;
    }

    public boolean isVigencia() {
        return vigencia;
    }

    public void setOrden(Long orden) {
        this.orden = orden;
    }

    public Long getOrden() {
        
        if (orden == null){
                orden = 0L;
            }

        return orden;
    }
}

