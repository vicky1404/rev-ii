package cl.mdr.ifrs.ejb.entity;

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

import cl.mdr.ifrs.ejb.common.Constantes;

@Entity
@NamedQueries( { @NamedQuery(name = PeriodoEmpresa.FIND_MAX_BY_EMPRESA, 
				query = " select p " +
						" from " +
						" PeriodoEmpresa p " +
						" where " +
						" p.idRut = :idRut and " +
						" p.idPeriodo = (select max(o.idPeriodo) from PeriodoEmpresa o where o.idRut = :idRut) "),
				@NamedQuery(name = PeriodoEmpresa.FIND_BY_ID, 
				query = " select o " +
						" from " +
						" PeriodoEmpresa o" +
						" where " +
						" o.idPeriodo = :idPeriodo and " +
						" o.idRut = :idRut ")
					
})
@Table(name = Constantes.PERIODO_EMPRESA)
public class PeriodoEmpresa implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public static final String FIND_MAX_BY_EMPRESA = "PeriodoEmpresa.findByEmpresa";
	public static final String FIND_BY_ID = "PeriodoEmpresa.findById";

	@Id
	@Column(name="ID_PERIODO")
	private Long idPeriodo;

	@Id
	@Column(name="ID_RUT")
	private Long idRut;

    @ManyToOne
	@JoinColumn(name="ID_RUT")
	private Empresa empresa;

	@ManyToOne
	@JoinColumn(name="ID_ESTADO_PERIODO")
	private EstadoPeriodo estadoPeriodo;

    @ManyToOne
	@JoinColumn(name="ID_PERIODO")
	private Periodo periodo;

    @OneToMany(mappedBy="periodoEmpresa", fetch = FetchType.LAZY)
	private List<RelacionEeff> relacionEeffList;
    
    
	@OneToMany(mappedBy="periodoEmpresa", fetch = FetchType.LAZY)
	private List<RelacionDetalleEeff> relacionDetalleEeffList;
	

	@OneToMany(mappedBy="periodoEmpresa", fetch = FetchType.LAZY)
	private List<Version> versionList;

	@OneToMany(mappedBy="periodoEmpresa", fetch = FetchType.LAZY)
	private List<VersionEeff> VersionEeffList;

	public PeriodoEmpresa() {
    }
    
    public Long getIdPeriodo() {
		return idPeriodo;
	}

	public void setIdPeriodo(Long idPeriodo) {
		this.idPeriodo = idPeriodo;
	}

	public Long getIdRut() {
		return idRut;
	}

	public void setIdRut(Long idRut) {
		this.idRut = idRut;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public EstadoPeriodo getEstadoPeriodo() {
		return estadoPeriodo;
	}

	public void setEstadoPeriodo(EstadoPeriodo estadoPeriodo) {
		this.estadoPeriodo = estadoPeriodo;
	}

	public Periodo getPeriodo() {
		if(periodo==null){
			periodo = new Periodo();
		}
		return periodo;
	}

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}

	public List<RelacionEeff> getRelacionEeffList() {
		return relacionEeffList;
	}

	public void setRelacionEeffList(List<RelacionEeff> relacionEeffList) {
		this.relacionEeffList = relacionEeffList;
	}

	public List<RelacionDetalleEeff> getRelacionDetalleEeffList() {
		return relacionDetalleEeffList;
	}

	public void setRelacionDetalleEeffList(
			List<RelacionDetalleEeff> relacionDetalleEeffList) {
		this.relacionDetalleEeffList = relacionDetalleEeffList;
	}

	public List<Version> getVersionList() {
		return versionList;
	}

	public void setVersionList(List<Version> versionList) {
		this.versionList = versionList;
	}
	
	public List<VersionEeff> getVersionEeffList() {
		return VersionEeffList;
	}

	public void setVersionEeffList(List<VersionEeff> versionEeffList) {
		VersionEeffList = versionEeffList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idPeriodo == null) ? 0 : idPeriodo.hashCode());
		result = prime * result + ((idRut == null) ? 0 : idRut.hashCode());
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
		PeriodoEmpresa other = (PeriodoEmpresa) obj;
		if (idPeriodo == null) {
			if (other.idPeriodo != null)
				return false;
		} else if (!idPeriodo.equals(other.idPeriodo))
			return false;
		if (idRut == null) {
			if (other.idRut != null)
				return false;
		} else if (!idRut.equals(other.idRut))
			return false;
		return true;
	}
	

}
