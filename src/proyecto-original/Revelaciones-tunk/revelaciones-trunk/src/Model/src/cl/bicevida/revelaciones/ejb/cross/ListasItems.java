package cl.bicevida.revelaciones.ejb.cross;


import cl.bicevida.revelaciones.ejb.entity.Periodo;
import cl.bicevida.revelaciones.ejb.entity.TipoEstructura;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import javax.faces.model.SelectItem;


public class ListasItems{
    
    //@EJB FacadeServiceLocal facade;
    
    private List<SelectItem> vigencias;
    private List<SelectItem> meses;
    private List<SelectItem> anios;
    private List<SelectItem> tipoEstructuras;
    private List<TipoEstructura> tipoEstructuraList;
    private List<Periodo> periodoList;
    
    
    private static ListasItems INSTANCE = null;
     
    private ListasItems() {
    }
    
    @PostConstruct
    public void init(){
        vigencias = new ArrayList<SelectItem>();
        vigencias.add(new SelectItem(1L, "Sí"));
        vigencias.add(new SelectItem(0L, "No")); 
        
        //periodoList = facade.getMantenedoresTipoService().findAllPeriodo();
        
        anios = new ArrayList<SelectItem>();
                
        for(Periodo periodo : periodoList){
            anios.add(new SelectItem(periodo.getAnioPeriodo(), periodo.getAnioPeriodo()));
        }
        
        meses = new ArrayList<SelectItem>();
                
        for(Periodo periodo : periodoList){
            meses.add(new SelectItem(periodo.getMesPeriodo(), periodo.getMesPeriodo()));
        }
        
        //tipoEstructuraList = facade.getMantenedoresTipoService().findAllTipoEstructura();
        
        for(TipoEstructura tipoEstructura : tipoEstructuraList){
            tipoEstructuras.add(new SelectItem(tipoEstructura.getIdTipoEstructura(), tipoEstructura.getNombre()));
        }
    }
 
    private synchronized static void createInstance() {
        if (INSTANCE == null) { 
            INSTANCE = new ListasItems();
        }
    }
 
    public static ListasItems getInstance() {
        if (INSTANCE == null) createInstance();
        return INSTANCE;
    }


    public void setVigencias(List<SelectItem> vigencias) {
        this.vigencias = vigencias;
    }

    public List<SelectItem> getVigencias() {
        return vigencias;
    }

    public void setMeses(List<SelectItem> meses) {
        this.meses = meses;
    }

    public List<SelectItem> getMeses() {
        return meses;
    }

    public void setAnios(List<SelectItem> anios) {
        this.anios = anios;
    }

    public List<SelectItem> getAnios() {
        return anios;
    }

    public void setTipoEstructuras(List<SelectItem> tipoEstructuras) {
        this.tipoEstructuras = tipoEstructuras;
    }

    public List<SelectItem> getTipoEstructuras() {
        return tipoEstructuras;
    }

    public void setPeriodoList(List<Periodo> periodoList) {
        this.periodoList = periodoList;
    }

    public List<Periodo> getPeriodoList() {
        return periodoList;
    }

    public void setTipoEstructuraList(List<TipoEstructura> tipoEstructuraList) {
        this.tipoEstructuraList = tipoEstructuraList;
    }

    public List<TipoEstructura> getTipoEstructuraList() {
        return tipoEstructuraList;
    }
}
