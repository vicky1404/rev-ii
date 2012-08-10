package cl.bicevida.revelaciones.ejb.entity;


import cl.bicevida.revelaciones.ejb.common.Constantes;

import java.io.Serializable;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@NamedQueries( { 
    @NamedQuery(name = Grupo.FIND_ALL , query = "select o from Grupo o order by o.areaNegocio.nombre asc, o.nombre asc") 
})
@Table(name = Constantes.REV_GRUPO)
public class Grupo implements Serializable {
    private static final long serialVersionUID = -6090604228928052709L;
    
    public static final String FIND_ALL = "Grupo.findAll";
    
    @Id
    @Column(name = "ID_GRUPO_ACCESO", nullable = false, length = 128)
    private String idGrupo;
    
    @Column(length = 512)
    private String nombre;
    
    @ManyToOne
    @JoinColumn(name = "ID_GRUPO_OID")
    private GrupoOid grupoOid;
    
    @ManyToOne
    @JoinColumn(name = "ID_AREA_NEGOCIO")
    private AreaNegocio areaNegocio;
    
    @Column(name = "ACCESO_BLOQUEADO")
    private Long accesoBloquedo;
    
    @OneToMany(mappedBy = "grupo", targetEntity = UsuarioGrupo.class, fetch = FetchType.EAGER)
    private List<UsuarioGrupo> usuarioGrupoList;
    
    @OneToMany(mappedBy = "grupo", targetEntity = CatalogoGrupo.class)
    private List<CatalogoGrupo> catalogoGrupoList;
    
    @OneToMany(mappedBy = "grupo", targetEntity = MenuGrupo.class)    
    private List<MenuGrupo> menuGrupoList;

    public Grupo() {
    }

    public Grupo(AreaNegocio areaNegocio, String idGrupo, GrupoOid grupoOid, String nombre) {
        this.areaNegocio = areaNegocio;
        this.idGrupo = idGrupo;
        this.grupoOid = grupoOid;
        this.nombre = nombre;
    }


    public String getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(String idGrupo) {
        this.idGrupo = idGrupo;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<UsuarioGrupo> getUsuarioGrupoList() {
        return usuarioGrupoList;
    }

    public void setUsuarioGrupoList(List<UsuarioGrupo> usuarioGrupoList) {
        this.usuarioGrupoList = usuarioGrupoList;
    }

    public UsuarioGrupo addUsuarioGrupo(UsuarioGrupo usuarioGrupo) {
        getUsuarioGrupoList().add(usuarioGrupo);
        usuarioGrupo.setGrupo(this);
        return usuarioGrupo;
    }

    public UsuarioGrupo removeUsuarioGrupo(UsuarioGrupo usuarioGrupo) {
        getUsuarioGrupoList().remove(usuarioGrupo);
        usuarioGrupo.setGrupo(null);
        return usuarioGrupo;
    }

    public GrupoOid getGrupoOid() {
        return grupoOid;
    }

    public void setGrupoOid(GrupoOid grupoOid) {
        this.grupoOid = grupoOid;
    }

    public AreaNegocio getAreaNegocio() {
        return areaNegocio;
    }

    public void setAreaNegocio(AreaNegocio areaNegocio) {
        this.areaNegocio = areaNegocio;
    }

    public List<CatalogoGrupo> getCatalogoGrupoList() {
        return catalogoGrupoList;
    }

    public void setCatalogoGrupoList(List<CatalogoGrupo> catalogoGrupoList) {
        this.catalogoGrupoList = catalogoGrupoList;
    }

    public CatalogoGrupo addCatalogoNotaGrupo(CatalogoGrupo catalogoGrupo) {
        getCatalogoGrupoList().add(catalogoGrupo);
        catalogoGrupo.setGrupo(this);
        return catalogoGrupo;
    }

    public CatalogoGrupo removeCatalogoNotaGrupo(CatalogoGrupo catalogoGrupo) {
        getCatalogoGrupoList().remove(catalogoGrupo);
        catalogoGrupo.setGrupo(null);
        return catalogoGrupo;
    }
        

    public void setMenuGrupoList(List<MenuGrupo> menuGrupoList) {
        this.menuGrupoList = menuGrupoList;
    }

    public List<MenuGrupo> getMenuGrupoList() {
        return menuGrupoList;
    }
    
    public void setAccesoBloquedo(Long accesoBloquedo) {
        this.accesoBloquedo = accesoBloquedo;
    }

    public Long getAccesoBloquedo() {
        return accesoBloquedo;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idGrupo=");
        buffer.append(getIdGrupo());
        buffer.append(',');
        buffer.append("nombre=");
        buffer.append(getNombre());
        buffer.append(',');
        buffer.append(']');
        return buffer.toString();
    }


    
}
