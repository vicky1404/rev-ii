package cl.mdr.ifrs.ejb.entity;


import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import cl.mdr.ifrs.ejb.common.Constantes;


@Entity
@NamedQueries( { @NamedQuery(name = "AreaNegocio.findAll", query = "select o from AreaNegocio o") })
@Table(name = Constantes.AREA_NEGOCIO)
public class AreaNegocio implements Serializable {
	private static final long serialVersionUID = -2872761746612674267L;
	@Id
    @Column(name = "ID_AREA_NEGOCIO", nullable = false, length = 3)
    private String idAreaNegocio;
    @Column(length = 256)
    private String nombre;
    @OneToMany(mappedBy = "areaNegocio")
    private List<Grupo> grupoList1;

    public AreaNegocio() {
    }

    public AreaNegocio(String idAreaNegocio, String nombre) {
        this.idAreaNegocio = idAreaNegocio;
        this.nombre = nombre;
    }


    public String getIdAreaNegocio() {
        return idAreaNegocio;
    }

    public void setIdAreaNegocio(String idAreaNegocio) {
        this.idAreaNegocio = idAreaNegocio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Grupo> getGrupoList1() {
        return grupoList1;
    }

    public void setGrupoList1(List<Grupo> grupoList1) {
        this.grupoList1 = grupoList1;
    }

    public Grupo addGrupo(Grupo grupo) {
        getGrupoList1().add(grupo);
        grupo.setAreaNegocio(this);
        return grupo;
    }

    public Grupo removeGrupo(Grupo grupo) {
        getGrupoList1().remove(grupo);
        grupo.setAreaNegocio(null);
        return grupo;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idAreaNegocio=");
        buffer.append(getIdAreaNegocio());
        buffer.append(',');
        buffer.append("nombre=");
        buffer.append(getNombre());
        buffer.append(']');
        return buffer.toString();
    }
}
