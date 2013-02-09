package cl.bicevida.revelaciones.ejb.entity;


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


@Entity
@NamedQueries( { @NamedQuery(name = CuentaContable.FIND_ALL, query = "select o from CuentaContable o order by o.idCuenta"),
                 @NamedQuery(name = CuentaContable.FIND_VIGENTE, query = "select o from CuentaContable o where o.vigencia = 1 order by o.idCuenta")})
@Table(name = "REV_CUENTA_CONTABLE")
public class CuentaContable implements Serializable {
    
    public static final String FIND_ALL = "CuentaContable.findAll";
    public static final String FIND_VIGENTE = "CuentaContable.findVigente";
    
    @SuppressWarnings("compatibility:8617578974005001601")
    private static final long serialVersionUID = -5769613251964045031L;

    @Id
    @Column(name = "ID_CUENTA", nullable = false)
    private Long idCuenta;
        
    private String descripcion;
        
    private Long vigencia;
    
    @OneToMany(mappedBy = "cuentaContable", fetch = FetchType.LAZY)
    private List<DetalleEeff> detalleEeffList;
    
    
    @OneToMany(mappedBy = "cuentaContable", fetch = FetchType.LAZY)
    private List<RelacionDetalleEeff> relacionDetalleEeffList;
    
    @Transient
    private boolean editarId = false;
    
    
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

    public void setVigencia(Long vigencia) {
        this.vigencia = vigencia;
    }

    public Long getVigencia() {
        return vigencia;
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

    public void setEditarId(boolean editarId) {
        this.editarId = editarId;
    }

    public boolean isEditarId() {
        return editarId;
    }
}