package mapping;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "RevGrupo.findAll", query = "select o from RevGrupo o") })
@Table(name = "REV_GRUPO")
public class RevGrupo implements Serializable {
    @Column(name = "ACCESO_BLOQUEADO")
    private BigDecimal accesoBloqueado;
    @Id
    @Column(name = "ID_GRUPO_ACCESO", nullable = false, length = 128)
    private String idGrupoAcceso;
    @Column(length = 512)
    private String nombre;
    @ManyToOne
    @JoinColumn(name = "ID_GRUPO_OID")
    private RevGrupoOid revGrupoOid;
    @OneToMany(mappedBy = "revGrupo")
    private List<RevMenuGrupo> revMenuGrupoList;
    @OneToMany(mappedBy = "revGrupo1")
    private List<RevCatalogoGrupo> revCatalogoGrupoList1;
    @ManyToOne
    @JoinColumn(name = "ID_AREA_NEGOCIO")
    private RevAreaNegocio revAreaNegocio;
    @OneToMany(mappedBy = "revGrupo2")
    private List<RevUsuarioGrupo> revUsuarioGrupoList;

    public RevGrupo() {
    }

    public RevGrupo(BigDecimal accesoBloqueado, RevAreaNegocio revAreaNegocio, String idGrupoAcceso, RevGrupoOid revGrupoOid,
                    String nombre) {
        this.accesoBloqueado = accesoBloqueado;
        this.revAreaNegocio = revAreaNegocio;
        this.idGrupoAcceso = idGrupoAcceso;
        this.revGrupoOid = revGrupoOid;
        this.nombre = nombre;
    }


    public BigDecimal getAccesoBloqueado() {
        return accesoBloqueado;
    }

    public void setAccesoBloqueado(BigDecimal accesoBloqueado) {
        this.accesoBloqueado = accesoBloqueado;
    }


    public String getIdGrupoAcceso() {
        return idGrupoAcceso;
    }

    public void setIdGrupoAcceso(String idGrupoAcceso) {
        this.idGrupoAcceso = idGrupoAcceso;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public RevGrupoOid getRevGrupoOid() {
        return revGrupoOid;
    }

    public void setRevGrupoOid(RevGrupoOid revGrupoOid) {
        this.revGrupoOid = revGrupoOid;
    }

    public List<RevMenuGrupo> getRevMenuGrupoList() {
        return revMenuGrupoList;
    }

    public void setRevMenuGrupoList(List<RevMenuGrupo> revMenuGrupoList) {
        this.revMenuGrupoList = revMenuGrupoList;
    }

    public RevMenuGrupo addRevMenuGrupo(RevMenuGrupo revMenuGrupo) {
        getRevMenuGrupoList().add(revMenuGrupo);
        revMenuGrupo.setRevGrupo(this);
        return revMenuGrupo;
    }

    public RevMenuGrupo removeRevMenuGrupo(RevMenuGrupo revMenuGrupo) {
        getRevMenuGrupoList().remove(revMenuGrupo);
        revMenuGrupo.setRevGrupo(null);
        return revMenuGrupo;
    }

    public List<RevCatalogoGrupo> getRevCatalogoGrupoList1() {
        return revCatalogoGrupoList1;
    }

    public void setRevCatalogoGrupoList1(List<RevCatalogoGrupo> revCatalogoGrupoList1) {
        this.revCatalogoGrupoList1 = revCatalogoGrupoList1;
    }

    public RevCatalogoGrupo addRevCatalogoGrupo(RevCatalogoGrupo revCatalogoGrupo) {
        getRevCatalogoGrupoList1().add(revCatalogoGrupo);
        revCatalogoGrupo.setRevGrupo1(this);
        return revCatalogoGrupo;
    }

    public RevCatalogoGrupo removeRevCatalogoGrupo(RevCatalogoGrupo revCatalogoGrupo) {
        getRevCatalogoGrupoList1().remove(revCatalogoGrupo);
        revCatalogoGrupo.setRevGrupo1(null);
        return revCatalogoGrupo;
    }

    public RevAreaNegocio getRevAreaNegocio() {
        return revAreaNegocio;
    }

    public void setRevAreaNegocio(RevAreaNegocio revAreaNegocio) {
        this.revAreaNegocio = revAreaNegocio;
    }

    public List<RevUsuarioGrupo> getRevUsuarioGrupoList() {
        return revUsuarioGrupoList;
    }

    public void setRevUsuarioGrupoList(List<RevUsuarioGrupo> revUsuarioGrupoList) {
        this.revUsuarioGrupoList = revUsuarioGrupoList;
    }

    public RevUsuarioGrupo addRevUsuarioGrupo(RevUsuarioGrupo revUsuarioGrupo) {
        getRevUsuarioGrupoList().add(revUsuarioGrupo);
        revUsuarioGrupo.setRevGrupo2(this);
        return revUsuarioGrupo;
    }

    public RevUsuarioGrupo removeRevUsuarioGrupo(RevUsuarioGrupo revUsuarioGrupo) {
        getRevUsuarioGrupoList().remove(revUsuarioGrupo);
        revUsuarioGrupo.setRevGrupo2(null);
        return revUsuarioGrupo;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("accesoBloqueado=");
        buffer.append(getAccesoBloqueado());
        buffer.append(',');
        buffer.append("idGrupoAcceso=");
        buffer.append(getIdGrupoAcceso());
        buffer.append(',');
        buffer.append("nombre=");
        buffer.append(getNombre());
        buffer.append(',');
        buffer.append(']');
        return buffer.toString();
    }
}
