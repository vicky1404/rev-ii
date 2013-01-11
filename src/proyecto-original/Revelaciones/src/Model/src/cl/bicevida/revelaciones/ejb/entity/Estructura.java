package cl.bicevida.revelaciones.ejb.entity;


import cl.bicevida.revelaciones.ejb.common.Constantes;
import cl.bicevida.revelaciones.vo.GrillaVO;
import cl.bicevida.revelaciones.vo.HtmlVO;
import cl.bicevida.revelaciones.vo.TextoVO;

import java.io.Serializable;

import java.lang.Long;

import java.util.ArrayList;
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
import javax.persistence.Transient;


@Entity
@NamedQueries({
    @NamedQuery(name = Estructura.FIND_ESTRUCTURA_ALL, query = "select o from Estructura o"),    
    @NamedQuery(name = Estructura.FIND_ESTRUCTURA_BY_VERSION,  query = "select o from Estructura o where o.version = :version order by o.orden"),
    @NamedQuery(name = Estructura.FIND_ESTRUCTURA_BY_VERSION_TIPO,  query = "select o from Estructura o where o.version = :version and o.tipoEstructura.idTipoEstructura = :idTipoEstructura order by o.orden"),
    @NamedQuery(name = Estructura.FIND_ESTRUCTURA_BY_ID,  query = "select o from Estructura o where o.idEstructura = :id")
    /*@NamedQuery(name = Estructura.FIND_ESTRUCTURA_BY_VERSION_CASCADE,  query = "select o from Estructura o join fetch o.grillaList where o.version = :version order by o.orden")
    ,
    @NamedQuery(name = Estructura.FIND_ESTRUCTURA_VERSION, 
                query = "select o from Estructura o left join fetch o.htmlList left join fetch o.grillaList left join fetch o.textoList  where o.version = :version ")*/
    
})

//@NamedQuery(name = Version.VERSION_FIND_VIGENTE, query = "select o from Version o join fetch o.versionPeriodoList where o.vigencia = 1 and o.catalogo = :catalogo "),
@Table(name = Constantes.REV_ESTRUCTURA)
public class Estructura implements Serializable {
    
    public static final String FIND_ESTRUCTURA_BY_VERSION = "Estructura.findEstructuraByVersion";
    public static final String FIND_ESTRUCTURA_BY_VERSION_TIPO = "Estructura.findEstructuraByVersionTipo";    
    public static final String FIND_ESTRUCTURA_ALL = "Estructura.findAll";
    public static final String FIND_ESTRUCTURA_BY_ID = "Estructura.findById";
    
    
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = -2123468030703890538L;
    
    @Id
    @GeneratedValue(generator="ID_GEN_ESTRUCTURA")
    @SequenceGenerator(name="ID_GEN_ESTRUCTURA", sequenceName = "SEQ_ESTRUCTURA" ,allocationSize = 1)
    @Column(name = "ID_ESTRUCTURA", nullable = false)
    private Long idEstructura;
    
    @Column(nullable = false)
    private Long orden;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_TIPO_ESTRUCTURA")
    private TipoEstructura tipoEstructura;
    
    @ManyToOne    
    @JoinColumn(name = "ID_VERSION")
        
    private Version version;
    
    //@OneToMany(mappedBy = "estructura", targetEntity = Html.class, fetch = FetchType.EAGER)                
    @OneToMany(fetch = FetchType.EAGER)        
    @JoinColumn(name = "ID_HTML", insertable = false, updatable = false)
    private List<Html> htmlList;
    
    //@OneToMany(mappedBy = "estructura1", targetEntity = Grilla.class, fetch = FetchType.EAGER)        
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_GRILLA", insertable = false, updatable = false)
    private List<Grilla> grillaList;
        
    //@OneToMany(mappedBy = "estructura2", targetEntity = Texto.class, fetch = FetchType.EAGER)        
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_TEXTO", insertable = false, updatable = false)
    private List<Texto> textoList;
       
    @Transient
    private GrillaVO grillaVO;
    
    @Transient
    private TextoVO textoVo;
    
    
    @Transient
    private HtmlVO htmlVo;
    
    
    
    


    public Estructura() {
    }
    
    public Estructura(Long idEstructura) {
        this.idEstructura = idEstructura;
    }


    public Estructura(Long idEstructura, TipoEstructura tipoEstructura, Version version, Long orden) {
        this.idEstructura = idEstructura;
        this.tipoEstructura = tipoEstructura;
        this.version = version;
        this.orden = orden;
    }

    public Long getIdEstructura() {
        return idEstructura;
    }

    public void setIdEstructura(Long idEstructura) {
        this.idEstructura = idEstructura;
    }

    public Long getOrden() {
        return orden;
    }

    public void setOrden(Long orden) {
        this.orden = orden;
    }

    public TipoEstructura getTipoEstructura() {
        return tipoEstructura;
    }

    public void setTipoEstructura(TipoEstructura tipoEstructura) {
        this.tipoEstructura = tipoEstructura;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }
    
   

    public void setGrillaVO(GrillaVO grillaVO) {
        this.grillaVO = grillaVO;
    }

    public GrillaVO getGrillaVO() {
        return grillaVO;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idEstructura=");
        buffer.append(getIdEstructura());
        buffer.append(',');
        buffer.append("orden=");
        buffer.append(getOrden());
        buffer.append(',');
        buffer.append(']');
        return buffer.toString();
    }

    public void setHtmlList(List<Html> htmlList) {
        this.htmlList = htmlList;
    }

    public List<Html> getHtmlList() {
        return htmlList;
    }

    public void setGrillaList(List<Grilla> grillaList) {
        this.grillaList = grillaList;
    }

    public List<Grilla> getGrillaList() {
        if(grillaList==null){
            grillaList = new ArrayList<Grilla>();
        }
        return grillaList;
    }

    public void setTextoList(List<Texto> textoList) {
        this.textoList = textoList;
    }

    public List<Texto> getTextoList() {
        return textoList;
    }

    public void setTextoVo(TextoVO textoVo) {
        this.textoVo = textoVo;
    }

    public TextoVO getTextoVo() {
        return textoVo;
    }

    public void setHtmlVo(HtmlVO htmlVo) {
        this.htmlVo = htmlVo;
    }

    public HtmlVO getHtmlVo() {
        return htmlVo;
    }
    
  
    public Grilla addGrilla(Grilla grilla) {
        getGrillaList().add(grilla);
        grilla.setEstructura1(this);
        return grilla;
    }

    public Grilla removeGrilla(Grilla grilla) {
        getGrillaList().remove(grilla);
        grilla.setEstructura1(null);
        return grilla;
    }

    public Texto addTexto(Texto texto) {
        getTextoList().add(texto);
        texto.setEstructura2(this);
        return texto;
    }

    public Texto removeTexto(Texto texto) {
        getTextoList().remove(texto);
        texto.setEstructura2(null);
        return texto;
    }


    public Html addHtml(Html html) {
        getHtmlList().add(html);
        html.setEstructura(this);
        return html;
    }

    public Html removeHtml(Html Html) {
        getHtmlList().remove(Html);
        Html.setEstructura(null);
        return Html;
    }

}
