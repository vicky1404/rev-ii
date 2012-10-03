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
@NamedQueries( { @NamedQuery(name = "RevCatalogo.findAll", query = "select o from RevCatalogo o") })
@Table(name = "REV_CATALOGO")
public class RevCatalogo implements Serializable {
    @Column(name = "COD_CUADRO", nullable = false)
    private BigDecimal codCuadro;
    @Column(name = "COD_SUBCUADRO", nullable = false)
    private BigDecimal codSubcuadro;
    @Id
    @Column(name = "ID_CATALOGO", nullable = false)
    private BigDecimal idCatalogo;
    @Column(name = "IMPRESION_HORIZONTAL")
    private BigDecimal impresionHorizontal;
    @Column(nullable = false, length = 256)
    private String nombre;
    @Column(nullable = false)
    private BigDecimal orden;
    @Column(length = 256)
    private String titulo;
    @Column(name = "VALIDAR_EEFF")
    private BigDecimal validarEeff;
    @Column(nullable = false)
    private BigDecimal vigencia;
    @OneToMany(mappedBy = "revCatalogo")
    private List<RevCatalogoGrupo> revCatalogoGrupoList;
    @ManyToOne
    @JoinColumn(name = "ID_TIPO_CUADRO")
    private RevTipoCuadro revTipoCuadro;
    @OneToMany(mappedBy = "revCatalogo1")
    private List<RevVersion> revVersionList;

    public RevCatalogo() {
    }

    public RevCatalogo(BigDecimal codCuadro, BigDecimal codSubcuadro, BigDecimal idCatalogo,
                       RevTipoCuadro revTipoCuadro,
                       BigDecimal impresionHorizontal, String nombre, BigDecimal orden, String titulo,
                       BigDecimal validarEeff, BigDecimal vigencia) {
        this.codCuadro = codCuadro;
        this.codSubcuadro = codSubcuadro;
        this.idCatalogo = idCatalogo;
        this.revTipoCuadro = revTipoCuadro;
        this.impresionHorizontal = impresionHorizontal;
        this.nombre = nombre;
        this.orden = orden;
        this.titulo = titulo;
        this.validarEeff = validarEeff;
        this.vigencia = vigencia;
    }


    public BigDecimal getCodCuadro() {
        return codCuadro;
    }

    public void setCodCuadro(BigDecimal codCuadro) {
        this.codCuadro = codCuadro;
    }

    public BigDecimal getCodSubcuadro() {
        return codSubcuadro;
    }

    public void setCodSubcuadro(BigDecimal codSubcuadro) {
        this.codSubcuadro = codSubcuadro;
    }

    public BigDecimal getIdCatalogo() {
        return idCatalogo;
    }

    public void setIdCatalogo(BigDecimal idCatalogo) {
        this.idCatalogo = idCatalogo;
    }


    public BigDecimal getImpresionHorizontal() {
        return impresionHorizontal;
    }

    public void setImpresionHorizontal(BigDecimal impresionHorizontal) {
        this.impresionHorizontal = impresionHorizontal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getOrden() {
        return orden;
    }

    public void setOrden(BigDecimal orden) {
        this.orden = orden;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public BigDecimal getValidarEeff() {
        return validarEeff;
    }

    public void setValidarEeff(BigDecimal validarEeff) {
        this.validarEeff = validarEeff;
    }

    public BigDecimal getVigencia() {
        return vigencia;
    }

    public void setVigencia(BigDecimal vigencia) {
        this.vigencia = vigencia;
    }

    public List<RevCatalogoGrupo> getRevCatalogoGrupoList() {
        return revCatalogoGrupoList;
    }

    public void setRevCatalogoGrupoList(List<RevCatalogoGrupo> revCatalogoGrupoList) {
        this.revCatalogoGrupoList = revCatalogoGrupoList;
    }

    public RevCatalogoGrupo addRevCatalogoGrupo(RevCatalogoGrupo revCatalogoGrupo) {
        getRevCatalogoGrupoList().add(revCatalogoGrupo);
        revCatalogoGrupo.setRevCatalogo(this);
        return revCatalogoGrupo;
    }

    public RevCatalogoGrupo removeRevCatalogoGrupo(RevCatalogoGrupo revCatalogoGrupo) {
        getRevCatalogoGrupoList().remove(revCatalogoGrupo);
        revCatalogoGrupo.setRevCatalogo(null);
        return revCatalogoGrupo;
    }

    public RevTipoCuadro getRevTipoCuadro() {
        return revTipoCuadro;
    }

    public void setRevTipoCuadro(RevTipoCuadro revTipoCuadro) {
        this.revTipoCuadro = revTipoCuadro;
    }

    public List<RevVersion> getRevVersionList() {
        return revVersionList;
    }

    public void setRevVersionList(List<RevVersion> revVersionList) {
        this.revVersionList = revVersionList;
    }

    public RevVersion addRevVersion(RevVersion revVersion) {
        getRevVersionList().add(revVersion);
        revVersion.setRevCatalogo1(this);
        return revVersion;
    }

    public RevVersion removeRevVersion(RevVersion revVersion) {
        getRevVersionList().remove(revVersion);
        revVersion.setRevCatalogo1(null);
        return revVersion;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("codCuadro=");
        buffer.append(getCodCuadro());
        buffer.append(',');
        buffer.append("codSubcuadro=");
        buffer.append(getCodSubcuadro());
        buffer.append(',');
        buffer.append("idCatalogo=");
        buffer.append(getIdCatalogo());
        buffer.append(',');
        buffer.append("impresionHorizontal=");
        buffer.append(getImpresionHorizontal());
        buffer.append(',');
        buffer.append("nombre=");
        buffer.append(getNombre());
        buffer.append(',');
        buffer.append("orden=");
        buffer.append(getOrden());
        buffer.append(',');
        buffer.append("titulo=");
        buffer.append(getTitulo());
        buffer.append(',');
        buffer.append("validarEeff=");
        buffer.append(getValidarEeff());
        buffer.append(',');
        buffer.append("vigencia=");
        buffer.append(getVigencia());
        buffer.append(']');
        return buffer.toString();
    }
}
