package cl.bicevida.revelaciones.mb;


import cl.bicevida.revelaciones.common.mb.SoporteBackingBean;
import cl.bicevida.revelaciones.ejb.entity.EstadoFinanciero;
import cl.bicevida.revelaciones.ejb.entity.GrupoEeff;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;

import javax.faces.event.ActionEvent;


public class MantenedorGrupoEeffBackingBean extends SoporteBackingBean implements Serializable
{
    @SuppressWarnings("compatibility:-5025462716622701724")
    private static final long serialVersionUID = 2593741371267338740L;
    
    GrupoEeff grupoEeff;    
    List<GrupoEeff> grupoEeffList;
    
    List<EstadoFinanciero> estadoFinancieroList;
    boolean renderSinGrupos = false;
    
    @PostConstruct
    void init(){
        
        if(grupoEeffList == null){
            try {
                grupoEeffList = getFacade().getFecuService().getGruposEeff();
                
                Collections.sort(grupoEeffList, new Comparator<GrupoEeff>(){
                 public int compare(GrupoEeff ef1, GrupoEeff ef2) {
                        return  ef1.getOrden().compareTo(ef2.getOrden());
                    }                            
                    });
                
            } catch (Exception e) {
                this.agregarErrorMessage("Error al mostrar los grupos");
            }
        }
    }

    public MantenedorGrupoEeffBackingBean() {
        super();
    }

    public void setGrupoEeff(GrupoEeff grupoEeff) {
        this.grupoEeff = grupoEeff;
    }

    public GrupoEeff getGrupoEeff() {
        return grupoEeff;
    }

    public void setGrupoEeffList(List<GrupoEeff> grupoEeffList) {
        this.grupoEeffList = grupoEeffList;
    }

    public List<GrupoEeff> getGrupoEeffList() {
        return grupoEeffList;
    }
    
    public void guardarGrupoEeff(){
        try {            
            
            super.getFacade().getFecuService().persistGrupoEeff(this.getGrupoEeffList());
            this.setGrupoEeffList(super.getFacade().getFecuService().getGruposEeff());
            super.agregarSuccesMessage("Se han guardado los datos del Grupo con éxito");
            this.init();
        } catch (Exception e) {
            super.agregarErrorMessage("Se ha producido un error al guardar el Grupo de cuadros");
        }
    }
    
    public void agregarFilaGrupoEeff(ActionEvent event){
        List<GrupoEeff> grupoEeffListTemp = new ArrayList<GrupoEeff>();
        grupoEeffListTemp.add(new GrupoEeff());
        grupoEeffListTemp.addAll(grupoEeffList);
        grupoEeffList = grupoEeffListTemp;
    }
    
    public void setEstadoFinancieroList(List<EstadoFinanciero> estadoFinancieroList) {
        this.estadoFinancieroList = estadoFinancieroList;
    }

    public List<EstadoFinanciero> getEstadoFinancieroList() {
        return estadoFinancieroList;
    }

    public void setRenderSinGrupos(boolean renderSinGrupos) {
        this.renderSinGrupos = renderSinGrupos;
    }

    public boolean isRenderSinGrupos() {
        return renderSinGrupos;
    }

}
