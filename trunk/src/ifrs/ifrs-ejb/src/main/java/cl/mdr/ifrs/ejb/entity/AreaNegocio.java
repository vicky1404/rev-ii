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

import cl.mdr.ifrs.ejb.common.Constantes;


@Entity
@NamedQueries( { 
				@NamedQuery(name = AreaNegocio.FIND_ALL, query = "select o from AreaNegocio o where o.vigente = 1"),
				@NamedQuery(name = AreaNegocio.FIND_ALL_BY_EMPRESA, query = " select o from AreaNegocio o " +
																			" where o.empresa.idRut =:rutEmpresa and " +
																			" (o.vigente = :vigente or :vigente is null)")
				})
@Table(name = Constantes.AREA_NEGOCIO)
public class AreaNegocio implements Serializable {
	private static final long serialVersionUID = -2872761746612674267L;
	
	public static final String FIND_ALL = "AreaNegocio.findAll";
	public static final String FIND_ALL_BY_EMPRESA = "AreaNegocio.findAllByEmpresa";
	
	@Id
    @Column(name = "ID_AREA_NEGOCIO", nullable = false, length = 3)
	private String idAreaNegocio;

	@Column(length = 256)
    private String nombre;
    
	@OneToMany(mappedBy = "areaNegocio")
    private List<Grupo> grupoList1;
	
	@ManyToOne
	@JoinColumn(name="ID_RUT")    
    private Empresa empresa;
	
	@Column(name = "VIGENTE")
    private Long vigente;

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
}
