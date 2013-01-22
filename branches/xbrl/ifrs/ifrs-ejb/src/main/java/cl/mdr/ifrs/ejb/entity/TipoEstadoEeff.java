package cl.mdr.ifrs.ejb.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

import cl.mdr.ifrs.ejb.common.Constantes;


@Entity
@NamedQueries( { @NamedQuery(name = TipoEstadoEeff.FIND_ALL, query = "select o from TipoEstadoEeff o"),
                 @NamedQuery(name = TipoEstadoEeff.FIND_BY_ID, query = "select o from TipoEstadoEeff o where o.idEstadoEeff = :idEstadoEeff")})
@Table(name = Constantes.TIPO_ESTADO_EEFF)

public class TipoEstadoEeff implements Serializable {
	private static final long serialVersionUID = 3689343766503519638L;
	public static final String FIND_ALL = "TipoEstadoEeff.findAll";;
    public static final String FIND_BY_ID = "TipoEstadoEeff.findById";
    
    @Id
    @Column(name = "ID_ESTADO_EEFF", nullable = false)
    @Expose
    private Long idEstadoEeff;
    
    @Column(nullable = false, length = 256)
    @Expose
    private String nombre;
    
    @Column(nullable = false)
    @Expose
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idEstadoEeff == null) ? 0 : idEstadoEeff.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipoEstadoEeff other = (TipoEstadoEeff) obj;
		if (idEstadoEeff == null) {
			if (other.idEstadoEeff != null)
				return false;
		} else if (!idEstadoEeff.equals(other.idEstadoEeff))
			return false;
		return true;
	}
}
