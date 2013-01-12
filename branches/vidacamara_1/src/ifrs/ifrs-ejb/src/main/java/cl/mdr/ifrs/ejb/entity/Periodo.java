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
import javax.persistence.Transient;

import com.google.gson.annotations.Expose;

import cl.mdr.ifrs.ejb.common.Constantes;


@Entity
@NamedQueries( { @NamedQuery(name = "Periodo.findAll", query = "select o from Periodo o order by o.idPeriodo"),
                 @NamedQuery(name =Periodo.PERIODO_FIN_BY_PERIODO, query = "select o from Periodo o where o.idPeriodo = :periodo"),
                 @NamedQuery(name =Periodo.FIND_MAX_PERIODO_CERRADO, query = "select MAX(o.idPeriodo) from Periodo o where o.estadoPeriodo.idEstadoPeriodo = 1"),
                 @NamedQuery(name =Periodo.FIND_MAX_PERIODO, query = "select MAX(o.idPeriodo) from Periodo o "),
                 @NamedQuery(name =Periodo.FIND_MAX_PERIODO_OBJ, query =      "select p from Periodo p where p.idPeriodo = (select MAX(o.idPeriodo) from Periodo o)"),
                 @NamedQuery(name =Periodo.FIND_MAX_PERIODO_INICIADO, query = "select p from Periodo p where p.idPeriodo = (select MAX(o.idPeriodo) from Periodo o where o.estadoPeriodo.idEstadoPeriodo in (0 , 2))"),
                 @NamedQuery(name =Periodo.CERRAR_PERIODO_BY_PERIODO, query = "update Periodo p set p.estadoPeriodo.idEstadoPeriodo = 1 where p.idPeriodo =:idPeriodo")
})

@Table(name = Constantes.PERIODO)
public class Periodo implements Serializable {
    
    public static final String PERIODO_FIN_BY_PERIODO =  "Periodo.findByPeriodo";
    public static final String FIND_MAX_PERIODO_CERRADO =  "Periodo.findMaxPeriodoCerrado";
    public static final String FIND_MAX_PERIODO_INICIADO =  "Periodo.findMaxPeriodoIniciado";
    public static final String FIND_MAX_PERIODO =  "Periodo.findMaxPeriodo";
    public static final String FIND_MAX_PERIODO_OBJ =  "Periodo.findMaxPeriodoObJ";
    public static final String CERRAR_PERIODO_BY_PERIODO = "Periodo.CerrarPeriodoByPeriodo";
    public static final int ERROR_ABRIR_PERIODO = -1000;
    
    private static final long serialVersionUID = 4755907388348844543L;
    
    @Id
	@Column(name="ID_PERIODO")
    @Expose
	private Long idPeriodo;

    @ManyToOne
	@JoinColumn(name="ID_ESTADO_PERIODO")
    @Expose
	private EstadoPeriodo estadoPeriodo;

	@OneToMany(mappedBy="periodo", fetch = FetchType.LAZY)
	private List<PeriodoEmpresa> periodoEmpresaList;
    
    @Transient
    @Expose
    private String mesPeriodo;
    
    @Transient
    @Expose
    private String anioPeriodo;

    public Periodo() {
    }
    
    public Periodo(EstadoPeriodo estadoPeriodo, Long idPeriodo, Long periodo) {
        this.estadoPeriodo = estadoPeriodo;
        this.idPeriodo = idPeriodo;
    }

    public Periodo(Periodo periodo) {
        super();
        this.idPeriodo = periodo.idPeriodo;
        this.estadoPeriodo = periodo.estadoPeriodo;
    }

    public Long getIdPeriodo() {
		return idPeriodo;
	}

	public void setIdPeriodo(Long idPeriodo) {
		this.idPeriodo = idPeriodo;
	}



	public EstadoPeriodo getEstadoPeriodo() {
		return estadoPeriodo;
	}



	public void setEstadoPeriodo(EstadoPeriodo estadoPeriodo) {
		this.estadoPeriodo = estadoPeriodo;
	}

	public List<PeriodoEmpresa> getPeriodoEmpresaList() {
		return periodoEmpresaList;
	}



	public void setPeriodoEmpresaList(List<PeriodoEmpresa> periodoEmpresaList) {
		this.periodoEmpresaList = periodoEmpresaList;
	}

    public void setMesPeriodo(String mesPeriodo) {
        this.mesPeriodo = mesPeriodo;
    }

    public String getMesPeriodo() {
        if(getIdPeriodo() != null){
            if(getIdPeriodo().toString().length()==6)    
                mesPeriodo = getIdPeriodo().toString().substring(4,6);
        }
        return mesPeriodo;
    }

    public void setAnioPeriodo(String anioPeriodo) {
        this.anioPeriodo = anioPeriodo;
    }

    public String getAnioPeriodo() {
        if(getIdPeriodo() != null){
            if(getIdPeriodo().toString().length()==6)    
                anioPeriodo = getIdPeriodo().toString().substring(0,4);
        }
        return anioPeriodo;
    }
    
    public String getPeriodoFormat() {
        if(getIdPeriodo() != null && getIdPeriodo().toString().length() == 6){
            return getIdPeriodo().toString().substring(4,6).concat("-").concat(getIdPeriodo().toString().substring(0,4));
        }
        return null;
    }


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idPeriodo == null) ? 0 : idPeriodo.hashCode());
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
		Periodo other = (Periodo) obj;
		if (idPeriodo == null) {
			if (other.idPeriodo != null)
				return false;
		} else if (!idPeriodo.equals(other.idPeriodo))
			return false;
		return true;
	}
    
    
	
    
}
