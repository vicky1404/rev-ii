package cl.bicevida.revelaciones.common.mb;


import cl.bicevida.revelaciones.ejb.entity.Catalogo;
import cl.bicevida.revelaciones.ejb.entity.Grilla;

import cl.bicevida.revelaciones.ejb.entity.TipoCuadro;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;

import oracle.adf.view.rich.component.rich.data.RichTable;

import org.apache.log4j.Logger;


public class MantenedorBackingBean extends SoporteBackingBean implements Serializable{
    private static final long serialVersionUID = -1279280109468668253L;
    
    private transient Logger logger = Logger.getLogger(MantenedorBackingBean.class);
    
    private TipoCuadro tipoCuadro;   
    private transient RichTable tablaCatalogo;    
    private List<Catalogo> catalogoList; 
    private boolean renderTablaCatalogo = false;

    public String buscarCatalogoAction(){
        try {
            this.setCatalogoList(super.getFacade().getCatalogoService().findAllByTipo(this.getTipoCuadro(), null));
            this.setRenderTablaCatalogo(Boolean.TRUE);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            agregarErrorMessage("Se ha producido un error al buscar catalogo de cuadros");
        }
        return null;
    }
    
    public void guardarCatalogo(){
        try {            
            super.getFacade().getCatalogoService().persistEntity(this.getCatalogoList());
            this.setCatalogoList(super.getFacade().getCatalogoService().findAllByTipo(this.getTipoCuadro(), null));
            super.agregarSuccesMessage("Se han guardado los datos del Catálogo con éxito");
        } catch (Exception e) {
            logger.error(e);
            super.agregarErrorMessage("Se ha producido un error al guardar el Catálogo de cuadros");
        }
    }
    public void agregarFilaCatalogoNota(ActionEvent event){
        List<Catalogo> catalogoListTemp = new ArrayList<Catalogo>();
        catalogoListTemp.add(new Catalogo());
        catalogoListTemp.addAll(catalogoList);
        catalogoList = catalogoListTemp;
        //catalogoList.add(new Catalogo());
    }
    
    public void eliminarFilaCatalogoNota(ActionEvent event){
        
        if(validaListaCatalogo()){
            Catalogo nota = catalogoList.get(catalogoList.size()-1);
            if(nota.getIdCatalogo()==null){
                catalogoList.remove(catalogoList.size()-1);
            }
        }
    }

    private boolean validaListaCatalogo(){

        if(catalogoList!=null || catalogoList.size()>0)
            return true;

        return false;
    }

    public void setTipoCuadro(TipoCuadro tipoCuadro) {
        this.tipoCuadro = tipoCuadro;
    }

    public TipoCuadro getTipoCuadro() {
        return tipoCuadro;
    }
    
    public void setCatalogoList(List<Catalogo> catalogoList) {
        this.catalogoList = catalogoList;
    }

    public List<Catalogo> getCatalogoList() {                    
        return catalogoList;
    }
    
    public void setTablaCatalogo(RichTable tablaCatalogo) {
        this.tablaCatalogo = tablaCatalogo;
    }

    public RichTable getTablaCatalogo() {
        return tablaCatalogo;
    }

    public void setRenderTablaCatalogo(boolean renderTablaCatalogo) {
        this.renderTablaCatalogo = renderTablaCatalogo;
    }

    public boolean isRenderTablaCatalogo() {
        return renderTablaCatalogo;
    }
}
