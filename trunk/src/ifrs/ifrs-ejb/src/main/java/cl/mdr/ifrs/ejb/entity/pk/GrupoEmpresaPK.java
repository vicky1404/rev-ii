package cl.mdr.ifrs.ejb.entity.pk;


import java.io.Serializable;


public class GrupoEmpresaPK implements Serializable {
	private static final long serialVersionUID = 165104267434602566L;
	
	public String idGrupoAcceso;      
    public Long rut;
	
    public GrupoEmpresaPK(String idGrupoAcceso, Long rut) {
		super();
		this.idGrupoAcceso = idGrupoAcceso;
		this.rut = rut;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idGrupoAcceso == null) ? 0 : idGrupoAcceso.hashCode());
		result = prime * result + ((rut == null) ? 0 : rut.hashCode());
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
		GrupoEmpresaPK other = (GrupoEmpresaPK) obj;
		if (idGrupoAcceso == null) {
			if (other.idGrupoAcceso != null)
				return false;
		} else if (!idGrupoAcceso.equals(other.idGrupoAcceso))
			return false;
		if (rut == null) {
			if (other.rut != null)
				return false;
		} else if (!rut.equals(other.rut))
			return false;
		return true;
	}
    
    
        
     
    
}
