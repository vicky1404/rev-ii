package cl.mdr.ifrs.ejb.entity;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

import cl.mdr.ifrs.ejb.common.Constantes;


@Entity
@NamedQueries( { @NamedQuery(name = "TipoEstructura.findAll", query = "select o from TipoEstructura o order by o.nombre asc") })
@Table(name = Constantes.TIPO_ESTRUCTURA)
public class TipoEstructura implements Serializable {
    
    public static final String QUERY_FIND_TIPO_ESTRUCTURA_ALL = "TipoEstructura.findAll";
    public static final Long ESTRUCTURA_TIPO_GRILLA = 0L;
    public static final Long ESTRUCTURA_TIPO_HTML = 1L;
    public static final Long ESTRUCTURA_TIPO_TEXTO = 2L;
    
    private static final long serialVersionUID = 4553286198690512390L;
    
    @Id
    @Column(name = "ID_TIPO_ESTRUCTURA", nullable = false)
    @Expose
    private Long idTipoEstructura;
    

	@Column(nullable = false, length = 64)
	@Expose
    private String nombre;
    
	/*@OneToMany(mappedBy = "tipoEstructura")
    private List<Estructura> estructuraList;*/

    public TipoEstructura() {
    }

    public TipoEstructura(Long idTipoEstructura, String nombre) {
        this.idTipoEstructura = idTipoEstructura;
        this.nombre = nombre;
    }

    public TipoEstructura(Long idTipoEstructura) {
        this.idTipoEstructura = idTipoEstructura;
    }

    public Long getIdTipoEstructura() {
        return idTipoEstructura;
    }

    public void setIdTipoEstructura(Long idTipoEstructura) {
        this.idTipoEstructura = idTipoEstructura;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /*public List<Estructura> getEstructuraList() {
        return estructuraList;
    }

    public void setEstructuraList(List<Estructura> estructuraList) {
        this.estructuraList = estructuraList;
    }*/

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idTipoEstructura=");
        buffer.append(getIdTipoEstructura());
        buffer.append(',');
        buffer.append("nombre=");
        buffer.append(getNombre());
        buffer.append(']');
        return buffer.toString();
    }
    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((idTipoEstructura == null) ? 0 : idTipoEstructura.hashCode());
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
		TipoEstructura other = (TipoEstructura) obj;
		if (idTipoEstructura == null) {
			if (other.idTipoEstructura != null)
				return false;
		} else if (!idTipoEstructura.equals(other.idTipoEstructura))
			return false;
		return true;
	}
}
