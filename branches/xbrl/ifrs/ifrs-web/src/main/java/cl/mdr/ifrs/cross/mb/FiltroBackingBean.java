package cl.mdr.ifrs.cross.mb;




import java.io.Serializable;

import javax.annotation.PostConstruct;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;


import cl.mdr.exfida.xbrl.ejb.entity.XbrlTaxonomia;
import cl.mdr.ifrs.ejb.entity.Catalogo;
import cl.mdr.ifrs.ejb.entity.Empresa;

import cl.mdr.ifrs.ejb.entity.PeriodoEmpresa;
import cl.mdr.ifrs.ejb.entity.TipoCuadro;
import cl.mdr.ifrs.ejb.entity.Version;



@ManagedBean(name="filtroBackingBean")
@SessionScoped
public class FiltroBackingBean implements Serializable{
	private static final long serialVersionUID = -7043367923658986045L;

	public static final String FILTRO_BEAN_NAME = "filtroBackingBean";
    
	private Empresa empresa;
    private PeriodoEmpresa periodoEmpresa;
    private Catalogo catalogo;
    private Version version;
    private TipoCuadro tipoCuadro;
    private Long tipoFormula;
    private XbrlTaxonomia xbrlTaxonomia;
    /*Mes y año que se selecciona en los filtros*/
    private String mes;
    private String anio;

	//parametro global de debug visual de la aplicacion
    private boolean debug;
    
    @PostConstruct
    public void init(){
    	empresa = null;
    	periodoEmpresa = null;
        catalogo = null;
        version = null;
        tipoCuadro = null;
        tipoFormula = null;
    }
          
    public XbrlTaxonomia getXbrlTaxonomia() {
		return xbrlTaxonomia;
	}

	public void setXbrlTaxonomia(XbrlTaxonomia xbrlTaxonomia) {
		this.xbrlTaxonomia = xbrlTaxonomia;
	}

	public FiltroBackingBean() {
    }


    public void setPeriodoEmpresa(PeriodoEmpresa periodoEmpresa) {        
        if(periodoEmpresa!=null && periodoEmpresa.getPeriodo()!=null){
        	mes = periodoEmpresa.getPeriodo().getMesPeriodo();
        	anio = periodoEmpresa.getPeriodo().getAnioPeriodo();
        }
        this.periodoEmpresa = periodoEmpresa;
    }

    public PeriodoEmpresa getPeriodoEmpresa() {
        if (periodoEmpresa == null){
        	periodoEmpresa = new PeriodoEmpresa();
            }
        return periodoEmpresa;
    }

    public void setCatalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
    }

    public Catalogo getCatalogo() {
        if(catalogo==null){
            catalogo = new Catalogo();
        }
        return catalogo;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public Version getVersion() {
        if(version==null){
            version = new Version();
        }
        return version;
    }

    public void setTipoCuadro(TipoCuadro tipoCuadro) {
        this.tipoCuadro = tipoCuadro;
    }

    public TipoCuadro getTipoCuadro() {
        if(tipoCuadro==null){
            tipoCuadro = new TipoCuadro();
        }
        return tipoCuadro;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setTipoFormula(Long tipoFormula) {
        this.tipoFormula = tipoFormula;
    }

    public Long getTipoFormula() {
        return tipoFormula;
    }


	public Empresa getEmpresa() {
		return empresa;
	}


	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public String getAnio() {
		return anio;
	}

	public void setAnio(String anio) {
		this.anio = anio;
	}
	
	
	
}
