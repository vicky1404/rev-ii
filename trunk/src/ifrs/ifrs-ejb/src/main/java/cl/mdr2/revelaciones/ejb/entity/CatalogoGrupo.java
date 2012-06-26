package cl.bicevida.revelaciones.ejb.entity;


import cl.bicevida.revelaciones.ejb.common.Constantes;
import cl.bicevida.revelaciones.ejb.entity.pk.CatalogoGrupoPK;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@NamedQueries({ 
    @NamedQuery(name = CatalogoGrupo.FIND_ALL , query = "select o from CatalogoGrupo o"),
    @NamedQuery(name = CatalogoGrupo.DELETE_BY_GRUPO , query = "delete from CatalogoGrupo o where o.grupo =:grupo")
})
@Table(name = Constantes.CATALOGO_GRUPO)
@IdClass(CatalogoGrupoPK.class)
public class CatalogoGrupo implements Serializable {
    
    private static final long serialVersionUID = -1223718247712230592L;
    
    public static final String FIND_ALL = "CatalogoGrupo.findAll";
    public static final String DELETE_BY_GRUPO = "CatalogoGrupo.deleteByGrupo";
    
    @Id
    @Column(name = "ID_CATALOGO", nullable = false, insertable = false, updatable = false)
    private Long idCatalogo;
    @Id
    @Column(name = "ID_GRUPO_ACCESO", nullable = false, length = 128, insertable = false, updatable = false)
    private String idGrupoAcceso;
    
    @ManyToOne
    @JoinColumn(name = "ID_CATALOGO")
    private Catalogo catalogo;
    
    @ManyToOne
    @JoinColumn(name = "ID_GRUPO_ACCESO")
    private Grupo grupo;

    public CatalogoGrupo() {
    }

    public Catalogo getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public void setIdCatalogo(Long idCatalogo) {
        this.idCatalogo = idCatalogo;
    }

    public Long getIdCatalogo() {
        return idCatalogo;
    }

    public void setIdGrupoAcceso(String idGrupoAcceso) {
        this.idGrupoAcceso = idGrupoAcceso;
    }

    public String getIdGrupoAcceso() {
        return idGrupoAcceso;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append(']');
        return buffer.toString();
    }
}
