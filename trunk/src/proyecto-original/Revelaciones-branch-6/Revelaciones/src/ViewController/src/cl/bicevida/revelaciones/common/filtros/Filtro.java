package cl.bicevida.revelaciones.common.filtros;


import cl.bicevida.revelaciones.ejb.entity.Catalogo;
import cl.bicevida.revelaciones.ejb.entity.Periodo;
import cl.bicevida.revelaciones.ejb.entity.TipoCuadro;
import cl.bicevida.revelaciones.ejb.entity.Version;

import java.io.Serializable;

public class Filtro implements Serializable{
    
    public static final String FILTRO_BEAN_NAME = "filtro";
    
    private Periodo periodo;
    private Catalogo catalogo;
    private Version version;
    private TipoCuadro tipoCuadro;
    private Long tipoFormula;
    
    //parametro global de debug visual de la aplicacion
    private boolean debug;
          
    public Filtro() {
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
}
