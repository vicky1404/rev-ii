package cl.mdr.ifrs.ejb.entity;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cl.mdr.ifrs.ejb.common.Constantes;
import cl.mdr.ifrs.ejb.entity.pk.GrupoEmpresaPK;


@Entity
@Table(name = Constantes.GRUPO_EMPRESA)
@IdClass(GrupoEmpresaPK.class)
public class GrupoEmpresa implements Serializable {
	private static final long serialVersionUID = -3055275699901024198L;

	@Id
    @Column(name="ID_GRUPO_ACCESO", nullable = false, insertable = false,updatable = false)
    private String idGrupoAcceso;
    
    @Id
    @Column(name="RUT", nullable = false, insertable = false,updatable = false)
    private Long idRut;
    
    @ManyToOne
    @JoinColumn(name = "ID_GRUPO_ACCESO")
    private Grupo grupo; 
    
    @ManyToOne
    @JoinColumn(name = "ID_RUT")
    private Empresa empresa;

	public String getIdGrupoAcceso() {
		return idGrupoAcceso;
	}

	public void setIdGrupoAcceso(String idGrupoAcceso) {
		this.idGrupoAcceso = idGrupoAcceso;
	}
	
	public Long getIdRut() {
		return idRut;
	}

	public void setIdRut(Long idRut) {
		this.idRut = idRut;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	} 
 
     
    
}
