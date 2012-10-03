package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "RevTipoEstadoEeff.findAll", query = "select o from RevTipoEstadoEeff o") })
@Table(name = "REV_TIPO_ESTADO_EEFF")
public class RevTipoEstadoEeff implements Serializable {
    @Id
    @Column(name = "ID_ESTADO_EEFF", nullable = false)
    private BigDecimal idEstadoEeff;
    @Column(nullable = false, length = 256)
    private String nombre;
    @Column(nullable = false)
    private BigDecimal vigente;
    @OneToMany(mappedBy = "revTipoEstadoEeff")
    private List<RevVersionEeff> revVersionEeffList;

    public RevTipoEstadoEeff() {
    }

    public RevTipoEstadoEeff(BigDecimal idEstadoEeff, String nombre, BigDecimal vigente) {
        this.idEstadoEeff = idEstadoEeff;
        this.nombre = nombre;
        this.vigente = vigente;
    }


    public BigDecimal getIdEstadoEeff() {
        return idEstadoEeff;
    }

    public void setIdEstadoEeff(BigDecimal idEstadoEeff) {
        this.idEstadoEeff = idEstadoEeff;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getVigente() {
        return vigente;
    }

    public void setVigente(BigDecimal vigente) {
        this.vigente = vigente;
    }

    public List<RevVersionEeff> getRevVersionEeffList() {
        return revVersionEeffList;
    }

    public void setRevVersionEeffList(List<RevVersionEeff> revVersionEeffList) {
        this.revVersionEeffList = revVersionEeffList;
    }

    public RevVersionEeff addRevVersionEeff(RevVersionEeff revVersionEeff) {
        getRevVersionEeffList().add(revVersionEeff);
        revVersionEeff.setRevTipoEstadoEeff(this);
        return revVersionEeff;
    }

    public RevVersionEeff removeRevVersionEeff(RevVersionEeff revVersionEeff) {
        getRevVersionEeffList().remove(revVersionEeff);
        revVersionEeff.setRevTipoEstadoEeff(null);
        return revVersionEeff;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idEstadoEeff=");
        buffer.append(getIdEstadoEeff());
        buffer.append(',');
        buffer.append("nombre=");
        buffer.append(getNombre());
        buffer.append(',');
        buffer.append("vigente=");
        buffer.append(getVigente());
        buffer.append(']');
        return buffer.toString();
    }
}
