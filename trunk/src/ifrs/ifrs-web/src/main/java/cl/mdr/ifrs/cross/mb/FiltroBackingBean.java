package cl.mdr.ifrs.cross.mb;




import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import cl.mdr.ifrs.ejb.entity.Catalogo;
import cl.mdr.ifrs.ejb.entity.Empresa;
import cl.mdr.ifrs.ejb.entity.Periodo;
import cl.mdr.ifrs.ejb.entity.TipoCuadro;
import cl.mdr.ifrs.ejb.entity.Version;

@ManagedBean(name="filtroBackingBean")
@SessionScoped
public class FiltroBackingBean implements Serializable{
	private static final long serialVersionUID = -7043367923658986045L;

	public static final String FILTRO_BEAN_NAME = "filtroBackingBean";
    
	private Empresa empresa;
    private Periodo periodo;
    private Catalogo catalogo;
    private Version version;
    private TipoCuadro tipoCuadro;
    private Long tipoFormula;
    
    //parametro global de debug visual de la aplicacion
    private boolean debug;
          
    public FiltroBackingBean() {
    }


    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Periodo getPeriodo() {
        if (periodo == null){
            periodo = new Periodo();
            }
        return periodo;
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
}
