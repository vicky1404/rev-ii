package cl.mdr.ifrs.ejb.entity;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import cl.mdr.ifrs.ejb.common.Constantes;
import cl.mdr.ifrs.vo.GrillaVO;
import cl.mdr.ifrs.vo.HtmlVO;
import cl.mdr.ifrs.vo.TextoVO;

import com.google.gson.annotations.Expose;


@Entity
@NamedQueries({
    @NamedQuery(name = Estructura.FIND_ESTRUCTURA_ALL, query = "select o from Estructura o"),    
    @NamedQuery(name = Estructura.FIND_ESTRUCTURA_BY_VERSION,  query = "select o from Estructura o where o.version.idVersion = :version order by o.orden"),
    @NamedQuery(name = Estructura.FIND_ESTRUCTURA_BY_VERSION_TIPO,  query = "select o from Estructura o where o.version = :version and o.tipoEstructura.idTipoEstructura = :idTipoEstructura order by o.orden"),
    @NamedQuery(name = Estructura.FIND_ESTRUCTURA_BY_ID,  query = "select o from Estructura o where o.idEstructura = :id")
    /*@NamedQuery(name = Estructura.FIND_ESTRUCTURA_BY_VERSION_CASCADE,  query = "select o from Estructura o join fetch o.grillaList where o.version = :version order by o.orden")
    ,
    @NamedQuery(name = Estructura.FIND_ESTRUCTURA_VERSION, 
                query = "select o from Estructura o left join fetch o.htmlList left join fetch o.grillaList left join fetch o.textoList  where o.version = :version ")*/
    
})

//@NamedQuery(name = Version.VERSION_FIND_VIGENTE, query = "select o from Version o join fetch o.versionPeriodoList where o.vigencia = 1 and o.catalogo = :catalogo "),
@Table(name = Constantes.ESTRUCTURA)
public class Estructura implements Serializable {
    
    public static final String FIND_ESTRUCTURA_BY_VERSION = "Estructura.findEstructuraByVersion";
    public static final String FIND_ESTRUCTURA_BY_VERSION_TIPO = "Estructura.findEstructuraByVersionTipo";    
    public static final String FIND_ESTRUCTURA_ALL = "Estructura.findAll";
    public static final String FIND_ESTRUCTURA_BY_ID = "Estructura.findById";
    
    
    private static final long serialVersionUID = -2123468030703890538L;
    
    @Id
    //@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID_ESTRUCTURA", nullable = false)
    @Expose
    private Long idEstructura;
    
    @Column(nullable = false)
    @Expose
    private Long orden;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_TIPO_ESTRUCTURA")
    @Expose
    private TipoEstructura tipoEstructura;
    
    @ManyToOne    
    @JoinColumn(name = "ID_VERSION")
    @Expose
    private Version version;
    
    
    //@OneToMany(mappedBy = "estructura", targetEntity = Html.class, fetch = FetchType.EAGER)                
    @OneToOne(mappedBy = "estructura", fetch = FetchType.EAGER, targetEntity = Html.class, orphanRemoval=true)        
    //@JoinColumn(name = "ID_HTML", referencedColumnName="ID_ESTRUCTURA", insertable = false, updatable = false)
    private Html html;
    
    
    //@OneToMany(mappedBy = "estructura1", targetEntity = Grilla.class, fetch = FetchType.EAGER)        
    @OneToOne(mappedBy = "estructura", fetch = FetchType.EAGER, orphanRemoval=true)
    @JoinColumn(name = "ID_GRILLA", referencedColumnName="ID_ESTRUCTURA",  insertable = false, updatable = false)
    private Grilla grilla;
       
    
    //@OneToMany(mappedBy = "estructura2", targetEntity = Texto.class, fetch = FetchType.EAGER)        
    @OneToOne(mappedBy = "estructura", fetch = FetchType.EAGER, orphanRemoval=true)
    //@JoinColumn(name = "ID_TEXTO", referencedColumnName="ID_ESTRUCTURA", insertable = false, updatable = false)
    private Texto texto;
             
    @Transient    
    private GrillaVO grillaVO;
    
    @Transient    
    private TextoVO textoVo;
    
    
    @Transient    
    private HtmlVO htmlVo;
    
    @Transient
    private boolean cambiaTipo;


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
    	if(grillaVO == null){
    		grillaVO = new GrillaVO();
    	}
        return grillaVO;
    }
        
    public void setTextoVo(TextoVO textoVo) {
        this.textoVo = textoVo;
    }

    public TextoVO getTextoVo() {
    	if(textoVo == null){
    		textoVo = new TextoVO();
    		textoVo.setTexto(new Texto());
    	}
        return textoVo;
    }

    public void setHtmlVo(HtmlVO htmlVo) {
        this.htmlVo = htmlVo;
    }

    public HtmlVO getHtmlVo() {
    	if(htmlVo == null){
    		htmlVo = new HtmlVO();
    		htmlVo.setHtml(new Html());
    	}
        return htmlVo;
    }

    public void setHtml(Html html) {
        this.html = html;
    }

    public Html getHtml() {
        return html;
    }

    public void setGrilla(Grilla grilla) {
        this.grilla = grilla;
    }

    public Grilla getGrilla() {
        return grilla;
    }

    public void setTexto(Texto texto) {
        this.texto = texto;
    }

    public Texto getTexto() {
        return texto;
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

	/**
	 * @return the cambiaTipo
	 */
	public boolean isCambiaTipo() {
		return cambiaTipo;
	}

	/**
	 * @param cambiaTipo the cambiaTipo to set
	 */
	public void setCambiaTipo(boolean cambiaTipo) {
		this.cambiaTipo = cambiaTipo;
	}
	
	
}
