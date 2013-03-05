package cl.bicevida.revelaciones.ejb.entity;


import cl.bicevida.revelaciones.ejb.common.Constantes;

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

import org.eclipse.persistence.annotations.Direction;
import org.eclipse.persistence.annotations.NamedStoredProcedureQueries;
import org.eclipse.persistence.annotations.NamedStoredProcedureQuery;
import org.eclipse.persistence.annotations.StoredProcedureParameter;


@Entity
@NamedQueries( { @NamedQuery(name = "Periodo.findAll", query = "select o from Periodo o order by o.periodo"),
                 @NamedQuery(name =Periodo.PERIODO_FIN_BY_PERIODO, query = "select o from Periodo o where o.periodo = :periodo"),
                 @NamedQuery(name =Periodo.FIND_MAX_PERIODO_CERRADO, query = "select MAX(o.periodo) from Periodo o where o.estadoPeriodo.idEstadoPeriodo = 1"),
                 @NamedQuery(name =Periodo.FIND_MAX_PERIODO, query = "select MAX(o.periodo) from Periodo o "),
                 @NamedQuery(name =Periodo.FIND_MAX_PERIODO_OBJ, query =      "select p from Periodo p where p.periodo = (select MAX(o.periodo) from Periodo o)"),
                 @NamedQuery(name =Periodo.FIND_MAX_PERIODO_INICIADO, query = "select p from Periodo p where p.periodo = (select MAX(o.periodo) from Periodo o where o.estadoPeriodo.idEstadoPeriodo in (0 , 2))")
})

@NamedStoredProcedureQueries(
{
    @NamedStoredProcedureQuery(
        name = Periodo.CALL_SP_ABRIR_PERIODO, 
        procedureName = Periodo.SP_ABRIR_PERIODO, 
        returnsResultSet = false,
        parameters = {
                        @StoredProcedureParameter(queryParameter = "usuario", name = "P_USUARIO", direction = Direction.IN, type = String.class),
                        @StoredProcedureParameter(queryParameter = "errno", name = "P_ERRNO", direction = Direction.OUT, type = Integer.class)
                      }),

    @NamedStoredProcedureQuery(
        name = Periodo.CALL_SP_CERRAR_PERIODO, 
        procedureName = Periodo.SP_CERRAR_PERIODO, 
        returnsResultSet = false,
        parameters = {
                        @StoredProcedureParameter(queryParameter = "usuario", name = "P_USUARIO", direction = Direction.IN, type = String.class),
                        @StoredProcedureParameter(queryParameter = "idPeriodo", name = "P_ID_PERIODO", direction = Direction.IN, type = Long.class),
                        @StoredProcedureParameter(queryParameter = "errno", name = "P_ERRNO", direction = Direction.OUT, type = Integer.class)
                      }
    )
})


@Table(name = Constantes.REV_PERIODO)
public class Periodo implements Serializable {
    
    public static final String PERIODO_FIN_BY_PERIODO =  "Periodo.findByPeriodo";
    public static final String FIND_MAX_PERIODO_CERRADO =  "Periodo.findMaxPeriodoCerrado";
    public static final String FIND_MAX_PERIODO_INICIADO =  "Periodo.findMaxPeriodoIniciado";
    public static final String FIND_MAX_PERIODO =  "Periodo.findMaxPeriodo";
    public static final String FIND_MAX_PERIODO_OBJ =  "Periodo.findMaxPeriodoObJ";
    public static final String CALL_SP_ABRIR_PERIODO =  "Periodo.spAbrirPeriodo";
    public static final String CALL_SP_CERRAR_PERIODO =  "Periodo.spCerrarPeriodo";
    public static final String SP_ABRIR_PERIODO = "PKG_REV_PERIODO.PRC_ABRIR_PERIODO";
    public static final String SP_CERRAR_PERIODO = "PKG_REV_PERIODO.PRC_CERRAR_PERIODO";
    
    @SuppressWarnings("compatibility:-1583527207744630034")
    private static final long serialVersionUID = 4755907388348844543L;
    
    @Id
    @Column(name = "ID_PERIODO", nullable = false)
    private Long idPeriodo;
    
    @Column(nullable = false)
    private Long periodo;
    
    @ManyToOne
    @JoinColumn(name = "ID_ESTADO_PERIODO")
    private EstadoPeriodo estadoPeriodo;
    
    @OneToMany(mappedBy = "periodo")
    private List<VersionPeriodo> versionPeriodoList;
    
    @OneToMany(mappedBy = "periodo", fetch = FetchType.LAZY)
    private List<VersionEeff> versionEeffList1;
    
    @OneToMany(mappedBy = "periodo", fetch = FetchType.LAZY)
    private List<RelacionEeff> relacionEeffList;
    
    @OneToMany(mappedBy = "periodo", fetch = FetchType.LAZY)
    private List<RelacionDetalleEeff> relacionDetalleEeffList;
    
    @OneToMany(mappedBy = "periodo")
    private List<RelacionCelda> relacionCeldaList;
    
    @Transient
    private String mesPeriodo;
    
    @Transient
    private String anioPeriodo;

    public Periodo() {
    }

    public Periodo(EstadoPeriodo estadoPeriodo, Long idPeriodo, Long periodo) {
        this.estadoPeriodo = estadoPeriodo;
        this.idPeriodo = idPeriodo;
        this.periodo = periodo;
    }

    public Periodo(Periodo periodo) {
        super();
        this.idPeriodo = periodo.idPeriodo;
        this.periodo = periodo.periodo;
        this.estadoPeriodo = periodo.estadoPeriodo;
        this.versionPeriodoList = periodo.versionPeriodoList;
        this.versionEeffList1 = periodo.versionEeffList1;
    }


    public Long getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(Long idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public Long getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Long periodo) {
        this.periodo = periodo;
    }

    public EstadoPeriodo getEstadoPeriodo() {
        return estadoPeriodo;
    }

    public void setEstadoPeriodo(EstadoPeriodo estadoPeriodo) {
        this.estadoPeriodo = estadoPeriodo;
    }

    public List<VersionPeriodo> getVersionPeriodoList() {
        return versionPeriodoList;
    }

    public void setVersionPeriodoList(List<VersionPeriodo> versionPeriodoList) {
        this.versionPeriodoList = versionPeriodoList;
    }

    public VersionPeriodo addPeriodo(VersionPeriodo versionPeriodo) {
        getVersionPeriodoList().add(versionPeriodo);
        versionPeriodo.setPeriodo(this);
        return versionPeriodo;
    }

    public VersionPeriodo removePeriodo(VersionPeriodo versionPeriodo) {
        getVersionPeriodoList().remove(versionPeriodo);
        versionPeriodo.setPeriodo(null);
        return versionPeriodo;
    }

    public void setMesPeriodo(String mesPeriodo) {
        this.mesPeriodo = mesPeriodo;
    }

    public String getMesPeriodo() {
        if(getPeriodo() != null){
            if(getPeriodo().toString().length()==6)    
                mesPeriodo = getPeriodo().toString().substring(4,6);
        }
        return mesPeriodo;
    }

    public void setAnioPeriodo(String anioPeriodo) {
        this.anioPeriodo = anioPeriodo;
    }

    public String getAnioPeriodo() {
        if(getPeriodo() != null){
            if(getPeriodo().toString().length()==6)    
                anioPeriodo = getPeriodo().toString().substring(0,4);
        }
        return anioPeriodo;
    }
    
    public String getPeriodoFormat() {
        if(periodo != null && periodo.toString().length() == 6){
            return periodo.toString().substring(4,6).concat("-").concat(periodo.toString().substring(0,4));
        }
        return null;
    }

    public void setVersionEeffList1(List<VersionEeff> versionEeffList) {
        this.versionEeffList1 = versionEeffList;
    }

    public List<VersionEeff> getVersionEeffList1() {
        return versionEeffList1;
    }

    public void setRelacionEeffList(List<RelacionEeff> relacionEeffList) {
        this.relacionEeffList = relacionEeffList;
    }

    public List<RelacionEeff> getRelacionEeffList() {
        return relacionEeffList;
    }

    public void RelacionDetalleEeff(List<RelacionDetalleEeff> relacionDetalleEeffList) {
        this.relacionDetalleEeffList = relacionDetalleEeffList;
    }

    public List<RelacionDetalleEeff> getRelacionDetalleEeffList() {
        return relacionDetalleEeffList;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idPeriodo=");
        buffer.append(getIdPeriodo());
        buffer.append(',');
        buffer.append("periodo=");
        buffer.append(getPeriodo());
        buffer.append(',');
        buffer.append(']');
        return buffer.toString();
    }
}
