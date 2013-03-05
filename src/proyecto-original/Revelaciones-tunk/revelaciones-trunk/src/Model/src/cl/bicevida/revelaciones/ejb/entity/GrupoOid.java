package cl.bicevida.revelaciones.ejb.entity;


import cl.bicevida.revelaciones.ejb.common.Constantes;

import java.io.Serializable;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@NamedQueries( { @NamedQuery(name = "GrupoOid.findAll", query = "select o from GrupoOid o") })
@Table(name = Constantes.REV_GRUPO_OID)
public class GrupoOid implements Serializable {
    @Id
    @Column(name = "ID_GRUPO_OID", nullable = false, length = 128)
    private String idGrupoOid;
    @Column(length = 512)
    private String nombre;
    @OneToMany(mappedBy = "grupoOid")
    private List<Grupo> grupoList;

    public GrupoOid() {
    }

    public GrupoOid(String idGrupoOid, String nombre) {
        this.idGrupoOid = idGrupoOid;
        this.nombre = nombre;
    }


    public String getIdGrupoOid() {
        return idGrupoOid;
    }

    public void setIdGrupoOid(String idGrupoOid) {
        this.idGrupoOid = idGrupoOid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Grupo> getGrupoList() {
        return grupoList;
    }

    public void setGrupoList(List<Grupo> grupoList) {
        this.grupoList = grupoList;
    }

    public Grupo addGrupo(Grupo grupo) {
        getGrupoList().add(grupo);
        grupo.setGrupoOid(this);
        return grupo;
    }

    public Grupo removeGrupo(Grupo grupo) {
        getGrupoList().remove(grupo);
        grupo.setGrupoOid(null);
        return grupo;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idGrupoOid=");
        buffer.append(getIdGrupoOid());
        buffer.append(',');
        buffer.append("nombre=");
        buffer.append(getNombre());
        buffer.append(']');
        return buffer.toString();
    }
}
