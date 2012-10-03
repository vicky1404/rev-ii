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
@NamedQueries( { @NamedQuery(name = "RevTipoCuadro.findAll", query = "select o from RevTipoCuadro o") })
@Table(name = "REV_TIPO_CUADRO")
public class RevTipoCuadro implements Serializable {
    @Id
    @Column(name = "ID_TIPO_CUADRO", nullable = false)
    private BigDecimal idTipoCuadro;
    @Column(nullable = false, length = 256)
    private String nombre;
    @Column(length = 512)
    private String titulo;
    @OneToMany(mappedBy = "revTipoCuadro")
    private List<RevCatalogo> revCatalogoList;

    public RevTipoCuadro() {
    }

    public RevTipoCuadro(BigDecimal idTipoCuadro, String nombre, String titulo) {
        this.idTipoCuadro = idTipoCuadro;
        this.nombre = nombre;
        this.titulo = titulo;
    }


    public BigDecimal getIdTipoCuadro() {
        return idTipoCuadro;
    }

    public void setIdTipoCuadro(BigDecimal idTipoCuadro) {
        this.idTipoCuadro = idTipoCuadro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<RevCatalogo> getRevCatalogoList() {
        return revCatalogoList;
    }

    public void setRevCatalogoList(List<RevCatalogo> revCatalogoList) {
        this.revCatalogoList = revCatalogoList;
    }

    public RevCatalogo addRevCatalogo(RevCatalogo revCatalogo) {
        getRevCatalogoList().add(revCatalogo);
        revCatalogo.setRevTipoCuadro(this);
        return revCatalogo;
    }

    public RevCatalogo removeRevCatalogo(RevCatalogo revCatalogo) {
        getRevCatalogoList().remove(revCatalogo);
        revCatalogo.setRevTipoCuadro(null);
        return revCatalogo;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idTipoCuadro=");
        buffer.append(getIdTipoCuadro());
        buffer.append(',');
        buffer.append("nombre=");
        buffer.append(getNombre());
        buffer.append(',');
        buffer.append("titulo=");
        buffer.append(getTitulo());
        buffer.append(']');
        return buffer.toString();
    }
}
