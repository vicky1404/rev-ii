package cl.bicevida.revelaciones.mb;


import cl.bicevida.revelaciones.common.mb.SoporteBackingBean;
import cl.bicevida.revelaciones.common.util.GeneradorDisenoHelper;
import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.revelaciones.ejb.entity.Catalogo;
import cl.bicevida.revelaciones.ejb.entity.Estructura;
import cl.bicevida.revelaciones.ejb.entity.TipoCuadro;
import cl.bicevida.revelaciones.ejb.entity.Version;
import cl.bicevida.revelaciones.vo.GrillaModelVO;

import java.text.MessageFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.model.AutoSuggestUIHints;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.event.DisclosureEvent;


public class GeneradorVersionBackingBean extends SoporteBackingBean{

    @SuppressWarnings("compatibility:-6828780374993653527")
    private static final long serialVersionUID = -3810406642581474883L;
    
    private transient Logger logger = Logger.getLogger(GeneradorVersionBackingBean.class);
    public static final String BEAN_NAME = "generadorVersionBackingBean";
    private String busqueda;
    private Version versionSelected;
    private List<Catalogo> catalogosFiltrados;
    private List<Catalogo> catalogos;
    private List<Version> versiones;
    private List<Estructura> estructuras;
    private boolean renderedPeriodo = false;
    private boolean renderedVersiones = false;
    private boolean renderedEstructura = false;    
    private boolean renderedGrilla = false;    
    private boolean renderedBotonEditar = true;    
    private boolean almacenado = false;
    private Catalogo catalogoBusqueda;
    private Long idEstructura;
    private Long idTipoEstructura;
    private transient RichInputText busquedaInputText;    
    private transient RichTable estruturaTable;    
    private transient RichSelectOneChoice tipoCuadroSelect;
    private transient RichTable versionTable;

    public GeneradorVersionBackingBean() {
    }
    
    public List catalogosSugeridos(FacesContext facesContext, AutoSuggestUIHints autoSuggestUIHints) {
        if(autoSuggestUIHints.getSubmittedValue()!=null){
            getCatalogosFiltrados().clear();
            if(Util.esListaValida(getCatalogos())){
                for(Catalogo catalogo : getCatalogos()){
                    if(catalogo.getNombre().toUpperCase().indexOf(autoSuggestUIHints.getSubmittedValue().toUpperCase()) >= 0){
                        this.getCatalogosFiltrados().add(catalogo);
                    }
                }
            }
        }
        return getCatalogosFiltradosItems();
    }

    public void setCatalogosFiltrados(List<Catalogo> catalogosFiltrados) {
        this.catalogosFiltrados = catalogosFiltrados;
    }
    
    public List<SelectItem> getCatalogosFiltradosItems(){
        List<SelectItem> catalogoItems = new ArrayList<SelectItem>();
        for(Catalogo catalogo : getCatalogosFiltrados()){
            catalogoItems.add(new SelectItem(MessageFormat.format("Nombre: {0} - ID: {1}", catalogo.getNombre(),catalogo.getIdCatalogo()), MessageFormat.format("{0}", catalogo.getNombre())));
        }
        return catalogoItems;
    }

    

    public String buscarCatalogo() {
        setEstructuras(new ArrayList<Estructura>());
        setVersiones(new ArrayList<Version>());
        setRenderedEstructura(false);
        setRenderedVersiones(false);
        setAlmacenado(false);
        setRenderedBotonEditar(true);
        if(getBusquedaInputText().isValid()){
            Catalogo catalogo = null;
            try{
                Long idCatalogo = GeneradorDisenoHelper.getIdCatalogo(busqueda);
                if(idCatalogo!=null){
                    catalogo = getFacade().getCatalogoService().findCatalogoByCatalogo(new Catalogo(idCatalogo));
                }
            }catch(Exception e){
                getBusquedaInputText().setValue("");
                agregarWarnMessage("Búsqueda sin resultados");
            }
            
            if(catalogo==null){
                init();
                agregarWarnMessage("Búsqueda sin resultados");
            }else{
                setCatalogoBusqueda(catalogo);
                getBusquedaInputText().setReadOnly(true);
                getTipoCuadroSelect().setReadOnly(true);
                getFiltro().setCatalogo(catalogo);
                try{
                    versiones = getFacade().getVersionService().findVersionAllByCatalogo(catalogo);
                }catch(Exception e){
                    agregarErrorMessage("Error al buscar versiones");
                    logger.error("Error al buscar versiones",e);
                }
                /*if(versiones==null || versiones.size()==0){
                
                }*/
                setRenderedVersiones(true);
            }
        }else{
            init();
            agregarWarnMessage("No debe modificar el valor autocompletado");
            getBusquedaInputText().setValue("");
        }
        return null;
    }
    
    public void limpiarCatalogolistener(ActionEvent event){
        init();
        getBusquedaInputText().setValue("");
        getBusquedaInputText().setReadOnly(false);
        getTipoCuadroSelect().setReadOnly(false);
    }

    
    
    private Catalogo getCatalogoBusqueda(String parametro){
        
        Catalogo catalogo = null;
            
        try{
            String valor = parametro.replaceAll(" ","");
            if(valor.contains("ID:")){
                Long id = Long.valueOf(valor.substring(valor.lastIndexOf("ID:")+3,valor.length()));
                catalogo = new Catalogo(id);
            }
        }catch(NumberFormatException e){
            logger.error("Error al generar id catalogo");
        }catch(Exception e){
            logger.error("Error al generar id catalogo");
        }
        
        return catalogo;
        
    }

    

    public void agregarVersionListener(ActionEvent actionEvent) {
       
       getGeneradorDiseno().setGrillaModelMap(null);
       
        if(getVersiones().size() > 0){
            
            Version version = getVersiones().get(getVersiones().size()-1);
            
            if(version.getIdVersion()!=null){
                for(Version versionPaso : getVersiones()){
                    versionPaso.setVigencia(0L);
                }
                setEstructuras(null);
                getVersiones().add(new Version(version.getVersion()+1,true,new Date(),1L));
                setRenderedEstructura(true);
            }
        }else{
            Version version = new Version(1L, true, new Date(),1L);
            versiones = new ArrayList<Version>();
            versiones.add(version);
            setRenderedEstructura(true);
            setEstructuras(null);
        }
        setAlmacenado(false);
        setRenderedBotonEditar(true);
    }
    
    public void copiarEstructuraActionListener(ActionEvent actionEvent) {
    
        getGeneradorDiseno().setGrillaModelMap(null);
        
        try{
            
             if(getVersiones().size() > 0){
    
                 if(getVersionSelected()==null){
                    agregarWarnMessage("No se ha podido obtener versión");
                    return;
                 }
                 Version version = getVersionSelected();
                 
                 if(version.getIdVersion()!=null){
                     
                     List<Estructura> estructuras = getFacade().getEstructuraService().findEstructuraByVersion(version);
                     List<Estructura> estructurasNew = new ArrayList<Estructura>();
                     for(Estructura estructura : estructuras){
                         Estructura estructuraNew = new Estructura();
                         estructuraNew.setOrden(estructura.getOrden());
                         estructuraNew.setTipoEstructura(estructura.getTipoEstructura());
                         estructuraNew.setVersion(estructura.getVersion());
                         estructuraNew.setHtmlList(estructura.getHtmlList());
                         estructuraNew.setGrillaList(estructura.getGrillaList());
                         estructuraNew.setTextoList(estructura.getTextoList());
                         estructurasNew.add(estructuraNew);
                     }
                     
                     getGeneradorDiseno().setGrillaModelMap(GeneradorDisenoHelper.cloneGrillaModel(estructurasNew));
                     
                     for(Version versionPaso : getVersiones()){
                         versionPaso.setVigencia(0L);
                     }
                     this.setEstructuras(estructurasNew);
                     getVersiones().add(new Version(version.getVersion()+1,true,new Date(),1L));
                     setRenderedEstructura(true);
                 }
            }else{
                 getVersiones().add(new Version());
                 setEstructuras(null);
            }
            setRenderedBotonEditar(true);
        }catch(Exception e){
            agregarErrorMessage("Error al obtener información");
            logger.error(e.getMessage(),e);
        }
        setAlmacenado(false);
        
    
    }
        
    public void buscarEstructuraActionListener(ActionEvent event){
        if(getVersiones().size()>0 && getVersiones().get(getVersiones().size()-1).getVigencia().equals(1L)){
            try {
                
                Version version = (Version)getVersionTable().getRowData();
                
                if(version==null)
                    return;

                List<Estructura> estructuras = getFacade().getEstructuraService().findEstructuraByVersion(version);
                
                if(estructuras==null || estructuras.size()==0){
                    this.setEstructuras(null);
                    return;
                }

                getGeneradorDiseno().setGrillaModelMap(GeneradorDisenoHelper.createGrillaModel(estructuras));
                
                setAlmacenado(true);
                setRenderedBotonEditar(false);
                this.setEstructuras(estructuras);
                
            } catch (Exception e) {
                agregarErrorMessage("Error al obtener información");
                logger.error(e.getMessage(),e);
            }
            
            setRenderedEstructura(true);
        }
    }
    
    public void agregarTipoEstructuraListener(ActionEvent actionEvent) {
        setAlmacenado(false);    
        Estructura estructuraSelected = (Estructura)estruturaTable.getRowData();
        List<Estructura> estructuras = new ArrayList<Estructura>();
        Estructura estructura;
        Long i=0L;
        
        Map<Long, GrillaModelVO> grillaModelMap = getGeneradorDiseno().getGrillaModelMap();
        Map<Long, GrillaModelVO> grillaModelPasoMap = new LinkedHashMap<Long, GrillaModelVO>();
                
        for(Estructura estructuraI : getEstructuras()){
            i++;
            
            if(getGeneradorDiseno().getGrillaModelMap().containsKey(estructuraI.getOrden()))
                grillaModelPasoMap.put(i, getGeneradorDiseno().getGrillaModelMap().get(estructuraI.getOrden()));
            else
                grillaModelPasoMap.put(i, new GrillaModelVO());
            
            estructuraI.setOrden(i);
            estructuras.add(estructuraI);

            if(estructuraI.getOrden().equals(estructuraSelected.getOrden())){                
                i++;
                grillaModelPasoMap.put(i, new GrillaModelVO());
                estructura = new Estructura();
                estructura.setOrden(i);
                estructuras.add(estructura);
            }

        }
        grillaModelMap.putAll(grillaModelPasoMap);
        grillaModelPasoMap.clear();
        setEstructuras(estructuras);
    }

    public void eliminarTipoEstructuraListener(ActionEvent actionEvent) {        
        setAlmacenado(false);
        if(getEstructuras().size()<=1)
            return;
        Estructura estructuraSelected = (Estructura)estruturaTable.getRowData();
        getEstructuras().remove(estructuraSelected);
        getGeneradorDiseno().getGrillaModelMap().remove(estructuraSelected.getOrden());
        Long i=1L;
        for(Estructura estructura : getEstructuras()){
            GrillaModelVO grillaModel = getGeneradorDiseno().getGrillaModelMap().get(estructura.getOrden());
            getGeneradorDiseno().getGrillaModelMap().put(i, grillaModel);
            estructura.setOrden(i);
            i++;
        }
        getGeneradorDiseno().initBackingBean();
    }
    
    public void subirEstructuraListener(ActionEvent event){
        
        List<Estructura> estructurasTemp = new ArrayList<Estructura>();
        Map<Long, GrillaModelVO> grillaModelMap = getGeneradorDiseno().getGrillaModelMap();
        Map<Long, GrillaModelVO> grillaModelMapTemp = new LinkedHashMap<Long, GrillaModelVO>();
        Estructura estructuraSelected = (Estructura)estruturaTable.getRowData();
        setAlmacenado(false);
        
        if(!estructuraSelected.getOrden().equals(1L)){
            getEstructuras().remove(estructuraSelected);
            
            Long contador = 0L;
            for(Estructura estructura : getEstructuras()){
                contador++;
                Long orden = estructuraSelected.getOrden() -1L;
                
                if(estructura.getOrden().equals(orden)){
                    
                    if(grillaModelMap.containsKey(estructuraSelected.getOrden())){
                        grillaModelMapTemp.put(contador, grillaModelMap.get(estructuraSelected.getOrden()));
                    }else{
                        grillaModelMapTemp.put(contador, new GrillaModelVO());
                    }
                    
                    estructuraSelected.setOrden(contador);
                    estructurasTemp.add(estructuraSelected);
                    contador++;
                }
                
                if(grillaModelMap.containsKey(estructura.getOrden()))
                    grillaModelMapTemp.put(contador, grillaModelMap.get(estructura.getOrden()));
                else
                    grillaModelMapTemp.put(contador, new GrillaModelVO());
                
                estructura.setOrden(contador);
                estructurasTemp.add(estructura);
            }
            grillaModelMap.putAll(grillaModelMapTemp);
            setEstructuras(estructurasTemp);        
            getGeneradorDiseno().initBackingBean();
            AdfFacesContext.getCurrentInstance().addPartialTarget(getEstruturaTable());
        }
    }
    
    public void bajarEstructuraListener(ActionEvent event){
        
        List<Estructura> estructurasTemp = new ArrayList<Estructura>();
        Estructura estructuraSelected = (Estructura)estruturaTable.getRowData();
        Map<Long, GrillaModelVO> grillaModelMap = getGeneradorDiseno().getGrillaModelMap();
        Map<Long, GrillaModelVO> grillaModelMapTemp = new LinkedHashMap<Long, GrillaModelVO>();
        setAlmacenado(false);
        
        if(estructuraSelected.getOrden().intValue() < getEstructuras().size()){
            
            Long orden = estructuraSelected.getOrden();
            getEstructuras().remove(estructuraSelected);
            Long contador = 0L;
            
            for(Estructura estructura : getEstructuras()){
                
                contador++;
                
                if(grillaModelMap.containsKey(estructura.getOrden()))
                    grillaModelMapTemp.put(contador, grillaModelMap.get(estructura.getOrden()));
                else
                    grillaModelMapTemp.put(contador, new GrillaModelVO());
                
                estructura.setOrden(contador);
                estructurasTemp.add(estructura);
                
                if(estructura.getOrden().equals(orden)){
                    
                    contador++;
                    
                    if(grillaModelMap.containsKey(estructuraSelected.getOrden()))
                        grillaModelMapTemp.put(contador, grillaModelMap.get(estructuraSelected.getOrden()));
                    else
                        grillaModelMapTemp.put(contador, new GrillaModelVO());
                    
                    
                    estructuraSelected.setOrden(contador);
                    estructurasTemp.add(estructuraSelected);

                }
                
            }
            grillaModelMap.putAll(grillaModelMapTemp);
            setEstructuras(estructurasTemp);        
            getGeneradorDiseno().initBackingBean();
            AdfFacesContext.getCurrentInstance().addPartialTarget(getEstruturaTable());
        }
    }
    
    public void guardarEstructuraListener(ActionEvent event){
        
        Long version = 1L;
        
        for(int i=0; i<versiones.size()-1; i++){
            versiones.get(i).setCatalogo(catalogoBusqueda);
            versiones.get(i).setVigencia(0L);
            versiones.get(i).setVersion(version);
            version++;
        }
        versiones.get(versiones.size()-1).setVigencia(1L);
        versiones.get(versiones.size()-1).setCatalogo(catalogoBusqueda);
        versiones.get(versiones.size()-1).setVersion(version);
        
        /*Se limpia modelo de grillas*/
        getGeneradorDiseno().initBackingBean();
        
        for(Estructura estructura : getEstructuras()){
            estructura.setVersion(versiones.get(versiones.size()-1));
            
            if(getGeneradorDiseno().getGrillaModelMap().containsKey(estructura.getOrden())){
                getGeneradorDiseno().getGrillaModelMap().get(estructura.getOrden()).setTipoEstructura(estructura.getTipoEstructura().getIdTipoEstructura());
            }else{
                getGeneradorDiseno().getGrillaModelMap().put(estructura.getOrden(), new GrillaModelVO(estructura.getTipoEstructura().getIdTipoEstructura()));
            }
        }
        setAlmacenado(true);
    }

    

    public void tipoCuadroChangeListener(ValueChangeEvent valueChangeEvent) {
        try {
            TipoCuadro tipoCuadro =  (TipoCuadro)getTipoCuadroSelect().getValue();
            List<Catalogo> catalogos = getFacade().getCatalogoService().findCatalogoByFiltro(getNombreUsuario(), tipoCuadro, null, null);
            setCatalogos(catalogos);
        } catch (Exception e) {
            agregarErrorMessage("Error al buscar catalogo");
            logger.error(e.getMessage(),e);
        }
    }
    
    public void versionDisclosure(DisclosureEvent disclosureEvent) {
        //if(!almacenado)
        //    agregarWarnMessage("Recuerde Guardar los cambios para poder Diseñar");
        
        //getGeneradorVisualizador().setRenderedVisualizador(false);
    }
    public void copiarVersionListener(ActionEvent event){
        Version version = (Version)getVersionTable().getRowData();
        setVersionSelected(version);
    }
    
    private void init(){
        renderedPeriodo = false;
        renderedVersiones = false;
        renderedEstructura = false;
        renderedGrilla = false;
        renderedBotonEditar = true;
        versiones = null;
        estructuras  = null;
        almacenado = false;        
    }
    
    public void setRenderedEstructura(boolean renderedEstructura) {
        this.renderedEstructura = renderedEstructura;
    }

    public boolean isRenderedEstructura() {
        return renderedEstructura;
    }

    public void setEstructuras(List<Estructura> estructuras) {
        this.estructuras = estructuras;
    }

    public List<Estructura> getEstructuras() {
        if(estructuras==null){
            estructuras = new ArrayList<Estructura>();
            Estructura estructura = new Estructura();
            estructura.setOrden(1L);
            estructuras.add(estructura);
        }
        return estructuras;
    }
    
    public List<Catalogo> getCatalogosFiltrados() {
        if(catalogosFiltrados==null){
            catalogosFiltrados = new ArrayList<Catalogo>();
        }
        return catalogosFiltrados;
    }

    public void setCatalogos(List<Catalogo> catalogos) {
        this.catalogos = catalogos;
    }

    public List<Catalogo> getCatalogos() {
        if(catalogos==null){
            catalogos =  new ArrayList<Catalogo>();
        }
        return catalogos;
    }
    
    public void setRenderedPeriodo(boolean renderedPeriodo) {
        this.renderedPeriodo = renderedPeriodo;
    }

    public boolean isRenderedPeriodo() {
        return renderedPeriodo;
    }

    public void setVersiones(List<Version> versiones) {
        this.versiones = versiones;
    }

    public List<Version> getVersiones() {
        if(versiones==null){
            versiones = new ArrayList<Version>();
        }
        return versiones;
    }

    public void setRenderedVersiones(boolean renderedVersiones) {
        this.renderedVersiones = renderedVersiones;
    }

    public boolean isRenderedVersiones() {
        return renderedVersiones;
    }
    
    public int getSizeEstructrua(){
        return getEstructuras().size();
    }

    public void setEstruturaTable(RichTable estruturaTable) {
        this.estruturaTable = estruturaTable;
    }

    public RichTable getEstruturaTable() {
        return estruturaTable;
    }
    
    public Catalogo getCatalogo(){
        return getFiltro().getCatalogo();
    }

    public void setRenderedGrilla(boolean renderedGrilla) {
        this.renderedGrilla = renderedGrilla;
    }

    public boolean isRenderedGrilla() {
        return renderedGrilla;
    }

    public void setBusqueda(String busqueda) {
        this.busqueda = busqueda;
    }

    public String getBusqueda() {
        return busqueda;
    }

    public void setBusquedaInputText(RichInputText busquedaButton) {
        this.busquedaInputText = busquedaButton;
    }

    public RichInputText getBusquedaInputText() {
        return busquedaInputText;
    }

    public void setAlmacenado(boolean almacenado) {
        this.almacenado = almacenado;
    }

    public boolean isAlmacenado() {
        return almacenado;
    }

    public void setCatalogoBusqueda(Catalogo catalogoBusqueda) {
        this.catalogoBusqueda = catalogoBusqueda;
    }

    public Catalogo getCatalogoBusqueda() {
        return catalogoBusqueda;
    }

    public void setTipoCuadroSelect(RichSelectOneChoice tipoCuadroSelect) {
        this.tipoCuadroSelect = tipoCuadroSelect;
    }

    public RichSelectOneChoice getTipoCuadroSelect() {
        return tipoCuadroSelect;
    }

    public void setRenderedBotonEditar(boolean renderedBotonEditar) {
        this.renderedBotonEditar = renderedBotonEditar;
    }

    public boolean isRenderedBotonEditar() {
        return renderedBotonEditar;
    }

    public void setVersionTable(RichTable versionTable) {
        this.versionTable = versionTable;
    }

    public RichTable getVersionTable() {
        return versionTable;
    }

    public void setVersionSelected(Version versionSelected) {
        this.versionSelected = versionSelected;
    }

    public Version getVersionSelected() {
        return versionSelected;
    }

    public void setIdEstructura(Long idEstructura) {
        this.idEstructura = idEstructura;
    }

    public Long getIdEstructura() {
        return idEstructura;
    }

    public void setIdTipoEstructura(Long idTipoEstructura) {
        this.idTipoEstructura = idTipoEstructura;
    }

    public Long getIdTipoEstructura() {
        return idTipoEstructura;
    }
}
