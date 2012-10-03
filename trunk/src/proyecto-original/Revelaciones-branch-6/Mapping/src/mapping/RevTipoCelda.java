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
@NamedQueries( { @NamedQuery(name = "RevTipoCelda.findAll", query = "select o from RevTipoCelda o") })
@Table(name = "REV_TIPO_CELDA")
public class RevTipoCelda implements Serializable {
    @Id
    @Column(name = "ID_TIPO_CELDA", nullable = false)
    private BigDecimal idTipoCelda;
    @Column(nullable = false, length = 128)
    private String nombre;
    @OneToMany(mappedBy = "revTipoCelda")
    private List<RevSubCelda> revSubCeldaList1;
    @OneToMany(mappedBy = "revTipoCelda1")
    private List<RevCelda> revCeldaList3;

    public RevTipoCelda() {
    }

    public RevTipoCelda(BigDecimal idTipoCelda, String nombre) {
        this.idTipoCelda = idTipoCelda;
        this.nombre = nombre;
    }


    public BigDecimal getIdTipoCelda() {
        return idTipoCelda;
    }

    public void setIdTipoCelda(BigDecimal idTipoCelda) {
        this.idTipoCelda = idTipoCelda;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<RevSubCelda> getRevSubCeldaList1() {
        return revSubCeldaList1;
    }

    public void setRevSubCeldaList1(List<RevSubCelda> revSubCeldaList1) {
        this.revSubCeldaList1 = revSubCeldaList1;
    }

    public RevSubCelda addRevSubCelda(RevSubCelda revSubCelda) {
        getRevSubCeldaList1().add(revSubCelda);
        revSubCelda.setRevTipoCelda(this);
        return revSubCelda;
    }

    public RevSubCelda removeRevSubCelda(RevSubCelda revSubCelda) {
        getRevSubCeldaList1().remove(revSubCelda);
        revSubCelda.setRevTipoCelda(null);
        return revSubCelda;
    }

    public List<RevCelda> getRevCeldaList3() {
        return revCeldaList3;
    }

    public void setRevCeldaList3(List<RevCelda> revCeldaList3) {
        this.revCeldaList3 = revCeldaList3;
    }

    public RevCelda addRevCelda(RevCelda revCelda) {
        getRevCeldaList3().add(revCelda);
        revCelda.setRevTipoCelda1(this);
        return revCelda;
    }

    public RevCelda removeRevCelda(RevCelda revCelda) {
        getRevCeldaList3().remove(revCelda);
        revCelda.setRevTipoCelda1(null);
        return revCelda;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idTipoCelda=");
        buffer.append(getIdTipoCelda());
        buffer.append(',');
        buffer.append("nombre=");
        buffer.append(getNombre());
        buffer.append(',');
        buffer.append(']');
        return buffer.toString();
    }
}
