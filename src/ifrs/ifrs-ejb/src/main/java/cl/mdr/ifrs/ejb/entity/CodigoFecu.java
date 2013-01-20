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

import com.google.gson.annotations.Expose;

import cl.mdr.ifrs.ejb.common.Constantes;


@Entity
@NamedQueries( { @NamedQuery(name = CodigoFecu.FIND_ALL, query = "select o from CodigoFecu o"),
                 @NamedQuery(name = CodigoFecu.FIND_VIGENTE, query = "select o from CodigoFecu o where o.vigencia = 1 order by o.idFecu")})
@Table(name = Constantes.CODIGO_FECU)

public class CodigoFecu implements Serializable {
    
    private static final long serialVersionUID = 3464663425042226085L;
    
    public static final String FIND_ALL = "CodigoFecu.findAll";
    public static final String FIND_VIGENTE = "CodigoFecu.findVigente";

    @Id
    @Expose
    @Column(name = "ID_FECU", nullable = false)
    private Long idFecu;
    
    @Expose
    private String descripcion;
    
    @Expose
    private Long vigencia;
    
    public Long getVigencia() {
		return vigencia;
	}

	public void setVigencia(Long vigencia) {
		this.vigencia = vigencia;
	}

	@OneToMany(mappedBy = "codigoFecu", fetch = FetchType.LAZY)
    private List<EstadoFinanciero> eeffList;
    
    @OneToMany(mappedBy = "codigoFecu", fetch = FetchType.LAZY)
    private List<RelacionEeff> relacionEeffList;
    
    @Transient
    private boolean editarId = false;   
   
    
    public boolean isEditarId() {
		return editarId;
	}

	public void setEditarId(boolean editarId) {
		this.editarId = editarId;
	}

	public CodigoFecu(boolean editarId) {
        this.editarId = editarId;
    }
    
    public CodigoFecu(){
    }

    public CodigoFecu(Long idFecu, String descripcion, Long vigente) {
        this.idFecu = idFecu;
        this.descripcion = descripcion;
        this.vigencia = vigente;
    }

    public void setIdFecu(Long idFecu) {
        this.idFecu = idFecu;
    }

    public Long getIdFecu() {
        return idFecu;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

   

    public void setEeffList(List<EstadoFinanciero> eeffList) {
        this.eeffList = eeffList;
    }

    public List<EstadoFinanciero> getEeffList() {
        return eeffList;
    }

    public void setRelacionEeffList(List<RelacionEeff> relacionEeffList) {
        this.relacionEeffList = relacionEeffList;
    }

    public List<RelacionEeff> getRelacionEeffList() {
        return relacionEeffList;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof CodigoFecu)) {
            return false;
        }
        final CodigoFecu other = (CodigoFecu)object;
        if (!(idFecu == null ? other.idFecu == null : idFecu.equals(other.idFecu))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = 1;
        result = PRIME * result + ((idFecu == null) ? 0 : idFecu.hashCode());
        return result;
    }
}
