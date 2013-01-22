package cl.mdr.ifrs.ejb.entity;


import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

import cl.mdr.ifrs.ejb.common.Constantes;


@Entity
@NamedQueries( { 
				@NamedQuery(name = AreaNegocio.FIND_ALL, query = "select o from AreaNegocio o where o.vigente = 1"),
				
				@NamedQuery(name = AreaNegocio.FIND_SIN_EMPRESA, query = " select o from AreaNegocio o " +
																		 " where o.empresa.idRut is null "),

				@NamedQuery(name = AreaNegocio.FIND_ALL_BY_EMPRESA, query = " select o from AreaNegocio o " +
																			" where o.empresa.idRut =:rutEmpresa  and " +
																			" (o.vigente = :vigente or :vigente is null) " +
																			" order by o.nombre asc")
				})
@Table(name = Constantes.AREA_NEGOCIO)
public class AreaNegocio implements Serializable, Comparable<AreaNegocio> {
	private static final long serialVersionUID = -2872761746612674267L;
	
	public static final String FIND_ALL = "AreaNegocio.findAll";
	public static final String FIND_ALL_BY_EMPRESA = "AreaNegocio.findAllByEmpresa";
	public static final String FIND_SIN_EMPRESA = "AreaNegocio.findSinEmpresa";
	
	@Id
    @Column(name = "ID_AREA_NEGOCIO", nullable = false, length = 3)
	@Expose
	private String idAreaNegocio;

	@Column(length = 256)
	@Expose
    private String nombre;
    
	@OneToMany(mappedBy = "areaNegocio")
    private List<Grupo> grupoList1;
	
	@ManyToOne
	@JoinColumn(name="ID_RUT")    
    private Empresa empresa;
	
	@Column(name = "VIGENTE")
	@Expose
    private Long vigente;

    public AreaNegocio() {
    }

    public AreaNegocio(String idAreaNegocio) {
		super();
		this.idAreaNegocio = idAreaNegocio;
	}

	public AreaNegocio(String idAreaNegocio, String nombre, Long vigente) {
		super();
		this.idAreaNegocio = idAreaNegocio;
		this.nombre = nombre;
		this.vigente = vigente;
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

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Long getVigente() {
		return vigente;
	}

	public void setVigente(Long vigente) {
		this.vigente = vigente;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idAreaNegocio == null) ? 0 : idAreaNegocio.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AreaNegocio other = (AreaNegocio) obj;
		if (idAreaNegocio == null) {
			if (other.idAreaNegocio != null)
				return false;
		} else if (!idAreaNegocio.equals(other.idAreaNegocio))
			return false;
		return true;
	}

	@Override
	public int compareTo(AreaNegocio o) {
		return this.idAreaNegocio.compareTo(o.idAreaNegocio);
	}
	
	
	
	
}
