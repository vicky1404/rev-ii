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
import javax.persistence.Transient;

import cl.mdr.ifrs.ejb.common.Constantes;


@Entity
@NamedQueries( { @NamedQuery(name = CuentaContable.FIND_ALL, query = "select o from CuentaContable o order by o.idCuenta"),
                 @NamedQuery(name = CuentaContable.FIND_VIGENTE, query = "select o from CuentaContable o where o.vigencia = 1 order by o.idCuenta")})
@Table(name = Constantes.CUENTA_CONTABLE)
public class CuentaContable implements Serializable {
    
    public static final String FIND_ALL = "CuentaContable.findAll";
    public static final String FIND_VIGENTE = "CuentaContable.findVigente";
    
    private static final long serialVersionUID = -5769613251964045031L;

    @Id
    @Column(name = "ID_CUENTA", nullable = false)
    private Long idCuenta;
        
    private String descripcion;
        
   
	public void setVigencia(Long vigencia) {
		this.vigencia = vigencia;
	}

	@OneToMany(mappedBy = "cuentaContable", fetch = FetchType.LAZY)
    private List<DetalleEeff> detalleEeffList;
    
    
    @OneToMany(mappedBy = "cuentaContable", fetch = FetchType.LAZY)
    private List<RelacionDetalleEeff> relacionDetalleEeffList;
    

    @Transient
    private boolean editarId = false;
    
    public boolean isEditarId() {
		return editarId;
	}

	public void setEditarId(boolean editarId) {
		this.editarId = editarId;
	}

	public CuentaContable(){
    }
    
    public CuentaContable(boolean editarId) {
        this.editarId = editarId;
    }
    public void setIdCuenta(Long idCuenta) {
        this.idCuenta = idCuenta;
    }

    public Long getIdCuenta() {
        return idCuenta;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

   

    public void setRelacionDetalleEeffList(List<RelacionDetalleEeff> relacionDetalleEeffList) {
        this.relacionDetalleEeffList = relacionDetalleEeffList;
    }

    public List<RelacionDetalleEeff> getRelacionDetalleEeffList() {
        return relacionDetalleEeffList;
    }

    public void setDetalleEeffList(List<DetalleEeff> detalleEeffList) {
        this.detalleEeffList = detalleEeffList;
    }

    public List<DetalleEeff> getDetalleEeffList() {
        return detalleEeffList;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result
				+ ((detalleEeffList == null) ? 0 : detalleEeffList.hashCode());
		result = prime * result
				+ ((idCuenta == null) ? 0 : idCuenta.hashCode());
		result = prime
				* result
				+ ((relacionDetalleEeffList == null) ? 0
						: relacionDetalleEeffList.hashCode());
		result = prime * result
				+ ((vigencia == null) ? 0 : vigencia.hashCode());
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
		CuentaContable other = (CuentaContable) obj;
		if (descripcion == null) {
			if (other.descripcion != null)
				return false;
		} else if (!descripcion.equals(other.descripcion))
			return false;
		if (detalleEeffList == null) {
			if (other.detalleEeffList != null)
				return false;
		} else if (!detalleEeffList.equals(other.detalleEeffList))
			return false;
		if (idCuenta == null) {
			if (other.idCuenta != null)
				return false;
		} else if (!idCuenta.equals(other.idCuenta))
			return false;
		if (relacionDetalleEeffList == null) {
			if (other.relacionDetalleEeffList != null)
				return false;
		} else if (!relacionDetalleEeffList
				.equals(other.relacionDetalleEeffList))
			return false;
		if (vigencia == null) {
			if (other.vigencia != null)
				return false;
		} else if (!vigencia.equals(other.vigencia))
			return false;
		return true;
	}
	
	 private Long vigencia;
	    
	    public Long getVigencia() {
			return vigencia;
		}

}
