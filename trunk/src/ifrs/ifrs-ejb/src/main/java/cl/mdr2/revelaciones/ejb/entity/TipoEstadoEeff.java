package cl.bicevida.revelaciones.ejb.entity;

import cl.bicevida.revelaciones.ejb.common.Constantes;

import java.io.Serializable;

import java.lang.Long;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@NamedQueries( { @NamedQuery(name = TipoEstadoEeff.FIND_ALL, query = "select o from TipoEstadoEeff o"),
                 @NamedQuery(name = TipoEstadoEeff.FIND_BY_ID, query = "select o from TipoEstadoEeff o where o.idEstadoEeff = :idEstadoEeff")})
@Table(name = Constantes.TIPO_ESTADO_EEFF)

public class TipoEstadoEeff implements Serializable {
    
    public static final String FIND_ALL = "TipoEstadoEeff.findAll";;
    public static final String FIND_BY_ID = "TipoEstadoEeff.findById";
    
    @Id
    @Column(name = "ID_ESTADO_EEFF", nullable = false)
    private Long idEstadoEeff;
    
    @Column(nullable = false, length = 256)
    private String nombre;
    
    @Column(nullable = false)
    private Long vigente;
    
    @OneToMany(mappedBy = "tipoEstadoEeff")
    private List<VersionEeff> versionEeffList;

    public TipoEstadoEeff() {
    }

    public TipoEstadoEeff(Long idEstadoEeff, String nombre, Long vigente) {
        this.idEstadoEeff = idEstadoEeff;
        this.nombre = nombre;
        this.vigente = vigente;
    }

    public Long getIdEstadoEeff() {
        return idEstadoEeff;
    }

    public void setIdEstadoEeff(Long idEstadoEeff) {
        this.idEstadoEeff = idEstadoEeff;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getVigente() {
        return vigente;
    }

    public void setVigente(Long vigente) {
        this.vigente = vigente;
    }

    public List<VersionEeff> getVersionEeffList() {
        return versionEeffList;
    }

    public void setVersionEeffList(List<VersionEeff> versionEeffList) {
        this.versionEeffList = versionEeffList;
    }

    public VersionEeff addVersionEeff(VersionEeff versionEeff) {
        getVersionEeffList().add(versionEeff);
        versionEeff.setTipoEstadoEeff(this);
        return versionEeff;
    }

    public VersionEeff removeVersionEeff(VersionEeff versionEeff) {
        getVersionEeffList().remove(versionEeff);
        versionEeff.setTipoEstadoEeff(null);
        return versionEeff;
    }
}
