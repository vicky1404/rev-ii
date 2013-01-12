package cl.mdr.ifrs.ejb.entity.pk;


import java.io.Serializable;


public class GrupoEmpresaPK implements Serializable {
	private static final long serialVersionUID = 165104267434602566L;
	
	public String idGrupoAcceso;      
    public Long idRut;
    
    public GrupoEmpresaPK() {
	}
	
    public GrupoEmpresaPK(String idGrupoAcceso, Long idRut) {
		super();
		this.idGrupoAcceso = idGrupoAcceso;
		this.idRut = idRut;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idGrupoAcceso == null) ? 0 : idGrupoAcceso.hashCode());
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
		GrupoEmpresaPK other = (GrupoEmpresaPK) obj;
		if (idGrupoAcceso == null) {
			if (other.idGrupoAcceso != null)
				return false;
		} else if (!idGrupoAcceso.equals(other.idGrupoAcceso))
			return false;
		if (idRut == null) {
			if (other.idRut != null)
				return false;
		} else if (!idRut.equals(other.idRut))
			return false;
		return true;
	}
    
    
        
     
    
}
