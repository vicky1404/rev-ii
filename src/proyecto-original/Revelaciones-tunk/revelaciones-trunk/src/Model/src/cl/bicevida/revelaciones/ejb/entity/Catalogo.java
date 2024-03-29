package cl.bicevida.revelaciones.ejb.entity;


import cl.bicevida.revelaciones.ejb.common.Constantes;

import java.io.Serializable;

import java.lang.Long;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@NamedQueries( { @NamedQuery(name = Catalogo.CATALOGO_FIND_ALL, 
                             query = "select o from Catalogo o order by o.orden"),
                 
                 @NamedQuery(name = Catalogo.CATALOGO_FIND_ALL_VIGENTE,
                             query = "select o from Catalogo o where o.vigencia = 1 order by o.orden"),
                 
                 @NamedQuery(name = Catalogo.CATALOGO_FIND_ALL_NO_VIGENTE,
                             query = "select o from Catalogo o where o.vigencia = 0 order by o.orden"),
                 
                 @NamedQuery(name = Catalogo.CATALOGO_FIND_ALL_VIGENTE_BY_TIPO,
                             query = "select o from Catalogo o where (:tipoCuadro is null or o.tipoCuadro.idTipoCuadro = :tipoCuadro) and o.vigencia = 1 order by o.orden"),
                 
                 @NamedQuery(name = Catalogo.CATALOGO_FIND_ALL_BY_TIPO,
                             query = "select o from Catalogo o where (:tipoCuadro is null or o.tipoCuadro.idTipoCuadro = :tipoCuadro) and (:vigente is null or o.vigencia = :vigente) order by o.orden"),
                 
                 @NamedQuery(name = Catalogo.CATALOGO_FIND_BY_NOTA,
                             query = "select o from Catalogo o where o = :nota"),
                 
                 @NamedQuery(name = Catalogo.CATALOGO_FIND_BY_FILTRO,
                             query = " select distinct c from Catalogo c , CatalogoGrupo cg, UsuarioGrupo ug where c.idCatalogo = cg.idCatalogo and ug.grupo = cg.grupo and (:usuario is null or ug.usuarioOid = :usuario) and (:tipoCuadro is null or c.tipoCuadro.idTipoCuadro = :tipoCuadro) and (:grupo is null or cg.grupo.idGrupo = :grupo) and (:vigencia is null or c.vigencia = :vigencia) and c.vigencia = 1 order by c.orden asc") 

})
@Table(name = Constantes.REV_CATALOGO)
public class Catalogo implements Serializable {
    
    public static final String CATALOGO_FIND_ALL = "Catalogo.findAll";
    public static final String CATALOGO_FIND_ALL_VIGENTE = "Catalogo.findAllVigente";
    public static final String CATALOGO_FIND_ALL_NO_VIGENTE = "Catalogo.findAllNoVigente";
    public static final String CATALOGO_FIND_ALL_VIGENTE_BY_TIPO = "Catalogo.findAllVigenteByTipo";
    public static final String CATALOGO_FIND_ALL_BY_TIPO = "Catalogo.findAllByTipo";
    public static final String CATALOGO_FIND_BY_NOTA = "Catalogo.findCatalogoByNota";    
    public static final String CATALOGO_FIND_BY_FILTRO = "Catalogo.findCatalogoByFiltro";
    
    @SuppressWarnings("compatibility:-5659885672498942383")
    private static final long serialVersionUID = -3765083808763944426L;


    @Id
    @GeneratedValue(generator="ID_GEN_CATALOGO")
    @SequenceGenerator(name="ID_GEN_CATALOGO", sequenceName = "SEQ_CATALOGO" ,allocationSize = 1)
    @Column(name = "ID_CATALOGO", nullable = false)
    private Long idCatalogo;
    
    @Column(name = "COD_CUADRO", nullable = false)
    private Long codigoCuadro;
    
    @Column(name = "COD_SUBCUADRO", nullable = false)
    private Long codigoSubcuadro;
    
    @Column(name = "NOMBRE", nullable = false, length = 256)
    private String nombre;
    
    @Column(nullable = false)
    private Long orden;
    
    @Column(name = "TITULO", length = 256)
    private String titulo;
    
    @Column(nullable = false)
    private Long vigencia;
    
    @Column(name = "IMPRESION_HORIZONTAL")
    private Long impresionHorizontal;
    
    @Column (name = "VALIDAR_EEFF")
    private Long validarEeff;
    
    @OneToMany(mappedBy = "catalogo")
    private List<Version> versionList;
    
    @OneToMany(mappedBy = "catalogo" , targetEntity = CatalogoGrupo.class, fetch = FetchType.EAGER)
    private List<CatalogoGrupo> catalogoGrupoList;
    
    @ManyToOne
    @JoinColumn(name = "ID_TIPO_CUADRO")
    private TipoCuadro tipoCuadro;

    public Catalogo() {
    }
    
    public Catalogo(Long idCatalogo) {
        this.idCatalogo = idCatalogo;
    }

    public Catalogo(Long codigoNota, Long codigoSubnota, Long idCatalogo, String nombre, Long orden, String titulo,
                    Long vigencia) {
        this.codigoCuadro = codigoNota;
        this.codigoSubcuadro = codigoSubnota;
        this.idCatalogo = idCatalogo;
        this.nombre = nombre;
        this.orden = orden;
        this.titulo = titulo;
        this.vigencia = vigencia;
    }
    

    public Long getCodigoCuadro() {
        return codigoCuadro;
    }

    public void setCodigoCuadro(Long codigoNota) {
        this.codigoCuadro = codigoNota;
    }

    public Long getCodigoSubcuadro() {
        return codigoSubcuadro;
    }

    public void setCodigoSubcuadro(Long codigoSubnota) {
        this.codigoSubcuadro = codigoSubnota;
    }

    public Long getIdCatalogo() {
        return idCatalogo;
    }

    public void setIdCatalogo(Long idNota) {
        this.idCatalogo = idNota;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getOrden() {
        return orden;
    }

    public void setOrden(Long orden) {
        this.orden = orden;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Long getVigencia() {
        return vigencia;
    }

    public void setVigencia(Long vigencia) {
        this.vigencia = vigencia;
    }

    public List<Version> getVersionList() {
        return versionList;
    }

    public void setVersionList(List<Version> versionList) {
        this.versionList = versionList;
    }

    public Version addVersion(Version version) {
        getVersionList().add(version);
        version.setCatalogo(this);
        return version;
    }

    public Version removeVersion(Version version) {
        getVersionList().remove(version);
        version.setCatalogo(null);
        return version;
    }

    public void setCatalogoGrupoList(List<CatalogoGrupo> catalogoGrupoList) {
        this.catalogoGrupoList = catalogoGrupoList;
    }

    public List<CatalogoGrupo> getCatalogoGrupoList() {
        return catalogoGrupoList;
    }

    public void setTipoCuadro(TipoCuadro tipoCuadro) {
        this.tipoCuadro = tipoCuadro;
    }

    public TipoCuadro getTipoCuadro() {
        return tipoCuadro;
    }
    
    public void setImpresionHorizontal(Long impresionHorizontal) {
        this.impresionHorizontal = impresionHorizontal;
    }

    public Long getImpresionHorizontal() {
        return impresionHorizontal;
    }
    
    public void setValidarEeff(Long validarEeff) {
        this.validarEeff = validarEeff;
    }

    public Long getValidarEeff() {
        return validarEeff;
    }
    
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Catalogo)) {
            return false;
        }
        final Catalogo other = (Catalogo)object;
        if (!(idCatalogo == null ? other.idCatalogo == null : idCatalogo.equals(other.idCatalogo))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = 1;
        result = PRIME * result + ((idCatalogo == null) ? 0 : idCatalogo.hashCode());
        return result;
    }
}
