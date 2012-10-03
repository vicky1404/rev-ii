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
@NamedQueries( { @NamedQuery(name = "RevMenu.findAll", query = "select o from RevMenu o") })
@Table(name = "REV_MENU")
public class RevMenu implements Serializable {
    @Column(nullable = false)
    private BigDecimal estado;
    @Column(name = "ES_PADRE")
    private BigDecimal esPadre;
    private BigDecimal grupo;
    @Id
    @Column(name = "ID_MENU", nullable = false)
    private BigDecimal idMenu;
    @Column(nullable = false, length = 512)
    private String nombre;
    @Column(name = "URL_MENU", length = 512)
    private String urlMenu;
    @OneToMany(mappedBy = "revMenu")
    private List<RevMenuGrupo> revMenuGrupoList1;

    public RevMenu() {
    }

    public RevMenu(BigDecimal esPadre, BigDecimal estado, BigDecimal grupo, BigDecimal idMenu, String nombre,
                   String urlMenu) {
        this.esPadre = esPadre;
        this.estado = estado;
        this.grupo = grupo;
        this.idMenu = idMenu;
        this.nombre = nombre;
        this.urlMenu = urlMenu;
    }


    public BigDecimal getEstado() {
        return estado;
    }

    public void setEstado(BigDecimal estado) {
        this.estado = estado;
    }

    public BigDecimal getEsPadre() {
        return esPadre;
    }

    public void setEsPadre(BigDecimal esPadre) {
        this.esPadre = esPadre;
    }

    public BigDecimal getGrupo() {
        return grupo;
    }

    public void setGrupo(BigDecimal grupo) {
        this.grupo = grupo;
    }

    public BigDecimal getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(BigDecimal idMenu) {
        this.idMenu = idMenu;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrlMenu() {
        return urlMenu;
    }

    public void setUrlMenu(String urlMenu) {
        this.urlMenu = urlMenu;
    }

    public List<RevMenuGrupo> getRevMenuGrupoList1() {
        return revMenuGrupoList1;
    }

    public void setRevMenuGrupoList1(List<RevMenuGrupo> revMenuGrupoList1) {
        this.revMenuGrupoList1 = revMenuGrupoList1;
    }

    public RevMenuGrupo addRevMenuGrupo(RevMenuGrupo revMenuGrupo) {
        getRevMenuGrupoList1().add(revMenuGrupo);
        revMenuGrupo.setRevMenu(this);
        return revMenuGrupo;
    }

    public RevMenuGrupo removeRevMenuGrupo(RevMenuGrupo revMenuGrupo) {
        getRevMenuGrupoList1().remove(revMenuGrupo);
        revMenuGrupo.setRevMenu(null);
        return revMenuGrupo;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("esPadre=");
        buffer.append(getEsPadre());
        buffer.append(',');
        buffer.append("estado=");
        buffer.append(getEstado());
        buffer.append(',');
        buffer.append("grupo=");
        buffer.append(getGrupo());
        buffer.append(',');
        buffer.append("idMenu=");
        buffer.append(getIdMenu());
        buffer.append(',');
        buffer.append("nombre=");
        buffer.append(getNombre());
        buffer.append(',');
        buffer.append("urlMenu=");
        buffer.append(getUrlMenu());
        buffer.append(']');
        return buffer.toString();
    }
}
