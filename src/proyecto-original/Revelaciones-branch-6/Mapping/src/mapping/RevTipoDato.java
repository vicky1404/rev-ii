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
@NamedQueries( { @NamedQuery(name = "RevTipoDato.findAll", query = "select o from RevTipoDato o") })
@Table(name = "REV_TIPO_DATO")
public class RevTipoDato implements Serializable {
    @Id
    @Column(name = "ID_TIPO_DATO", nullable = false)
    private BigDecimal idTipoDato;
    @Column(nullable = false, length = 64)
    private String nombre;
    @Column(name = "NOMBRE_CLASE", length = 256)
    private String nombreClase;
    @OneToMany(mappedBy = "revTipoDato")
    private List<RevCelda> revCeldaList2;
    @OneToMany(mappedBy = "revTipoDato1")
    private List<RevSubCelda> revSubCeldaList;

    public RevTipoDato() {
    }

    public RevTipoDato(BigDecimal idTipoDato, String nombre, String nombreClase) {
        this.idTipoDato = idTipoDato;
        this.nombre = nombre;
        this.nombreClase = nombreClase;
    }


    public BigDecimal getIdTipoDato() {
        return idTipoDato;
    }

    public void setIdTipoDato(BigDecimal idTipoDato) {
        this.idTipoDato = idTipoDato;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreClase() {
        return nombreClase;
    }

    public void setNombreClase(String nombreClase) {
        this.nombreClase = nombreClase;
    }

    public List<RevCelda> getRevCeldaList2() {
        return revCeldaList2;
    }

    public void setRevCeldaList2(List<RevCelda> revCeldaList2) {
        this.revCeldaList2 = revCeldaList2;
    }

    public RevCelda addRevCelda(RevCelda revCelda) {
        getRevCeldaList2().add(revCelda);
        revCelda.setRevTipoDato(this);
        return revCelda;
    }

    public RevCelda removeRevCelda(RevCelda revCelda) {
        getRevCeldaList2().remove(revCelda);
        revCelda.setRevTipoDato(null);
        return revCelda;
    }

    public List<RevSubCelda> getRevSubCeldaList() {
        return revSubCeldaList;
    }

    public void setRevSubCeldaList(List<RevSubCelda> revSubCeldaList) {
        this.revSubCeldaList = revSubCeldaList;
    }

    public RevSubCelda addRevSubCelda(RevSubCelda revSubCelda) {
        getRevSubCeldaList().add(revSubCelda);
        revSubCelda.setRevTipoDato1(this);
        return revSubCelda;
    }

    public RevSubCelda removeRevSubCelda(RevSubCelda revSubCelda) {
        getRevSubCeldaList().remove(revSubCelda);
        revSubCelda.setRevTipoDato1(null);
        return revSubCelda;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idTipoDato=");
        buffer.append(getIdTipoDato());
        buffer.append(',');
        buffer.append("nombre=");
        buffer.append(getNombre());
        buffer.append(',');
        buffer.append("nombreClase=");
        buffer.append(getNombreClase());
        buffer.append(',');
        buffer.append(']');
        return buffer.toString();
    }
}
