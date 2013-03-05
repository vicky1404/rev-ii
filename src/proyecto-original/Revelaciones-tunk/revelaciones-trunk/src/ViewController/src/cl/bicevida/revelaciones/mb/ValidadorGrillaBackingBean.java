package cl.bicevida.revelaciones.mb;


import cl.bicevida.revelaciones.common.mb.SoporteBackingBean;
import cl.bicevida.revelaciones.common.util.GeneradorDisenoHelper;
import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.revelaciones.ejb.entity.Catalogo;
import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.DetalleEeff;
import cl.bicevida.revelaciones.ejb.entity.EstadoFinanciero;
import cl.bicevida.revelaciones.ejb.entity.Estructura;
import cl.bicevida.revelaciones.ejb.entity.Grilla;
import cl.bicevida.revelaciones.ejb.entity.Periodo;
import cl.bicevida.revelaciones.ejb.entity.RelacionCelda;
import cl.bicevida.revelaciones.ejb.entity.TipoCuadro;
import cl.bicevida.revelaciones.ejb.entity.Version;
import cl.bicevida.revelaciones.vo.GrillaVO;

import java.text.MessageFormat;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.output.RichOutputText;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.model.AutoSuggestUIHints;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


/**
 * @author Manuel Gutierrez C.
 * @since 12/02/2013
 * Clase controladora para el mantenedor de relaciones (mapeo) entre grillas.
 */
public class ValidadorGrillaBackingBean extends SoporteBackingBean {

    @SuppressWarnings("compatibility:1742060691891788935")
    private static final long serialVersionUID = -4035055750647494664L;
    private transient Logger logger = Logger.getLogger(ValidadorGrillaBackingBean.class);
    private transient RichSelectOneChoice tipoCuadroSelect;    
    private transient RichSelectOneChoice tipoCuadroSelect2;
    private transient RichInputText busquedaInputText;  
    private transient RichInputText busquedaInputText2;  
    private transient RichOutputText contadorMapeos;  
    private List<Catalogo> catalogos;
    private String busqueda;
    private String busqueda2;
    private Periodo periodo;
    private Version versionVigente;
    private Version versionVigente2;
    private List<Estructura> estructuras;
    private List<Estructura> estructuras2;
    private List<Catalogo> catalogosFiltrados;
    private Catalogo catalogoBusqueda;
    private EstadoFinanciero estadoFinanciero;
    private DetalleEeff detalleEeff;
    private Long numeroCuenta;
    private transient RichSelectOneChoice cuentaChoice;
    private GrillaVO grillaVO;
    private GrillaVO grillaVO2;
    private Celda relCelda;
    private boolean renderTablaCuadro = false;
    private boolean renderTablaCuadro2 = false;
    private transient RichSelectOneChoice celdasAgregadasChoice;    
    private List<RelacionCelda> relCeldaList;
    private RelacionCelda relCeldaSeleccionada;
    
        
    /*
     * String[0] --> Fecus
     * String[1] --> Cuentas
     */
    private Map<Celda, List[]> relacionMap;
    private transient RichInputText relCeldaText; 

   
    
    @PostConstruct
    public void cargarPeriodo(){
        try{
            periodo = getFacade().getPeriodoService().findMaxPeriodoObj();            
            relacionMap = new LinkedHashMap<Celda, List[]>();
        }catch(Exception e){
            logger.error("Error en metodo cargarPeriodo", e);
            agregarErrorMessage("Error al cargar página");
        }
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
    
    public List<SelectItem> getCatalogosFiltradosItems(){
        List<SelectItem> catalogoItems = new ArrayList<SelectItem>();
        for(Catalogo catalogo : getCatalogosFiltrados()){
            catalogoItems.add(new SelectItem(MessageFormat.format("Nombre: {0} - ID: {1}", catalogo.getNombre(),catalogo.getIdCatalogo()), MessageFormat.format("{0}", catalogo.getNombre())));
        }
        return catalogoItems;
    }
    
    public String buscarCatalogo() {
        
        initBuscar();
        
        setEstructuras(new ArrayList<Estructura>());

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
                    if (periodo == null)
                    periodo = getFacade().getPeriodoService().findMaxPeriodoObj(); 
                    
                    versionVigente = getFacade().getVersionService().findUltimaVersionVigente(periodo.getIdPeriodo(),getNombreUsuario(),catalogo.getIdCatalogo());
                    estructuras = getFacade().getEstructuraService().findEstructuraByVersion(versionVigente);
                }catch(Exception e){
                    agregarErrorMessage("Error al buscar versiones");
                    logger.error("Error al buscar versiones",e);
                }
            }
        }else{
            init();
            agregarWarnMessage("No debe modificar el valor autocompletado");
            getBusquedaInputText().setValue("");
        }
        
        return null;
    }
    
    
    public String buscarCatalogo2() {
        
        initBuscar2();
        
        getTipoCuadroSelect2().setRequired(Boolean.TRUE);
        getBusquedaInputText2().setRequired(Boolean.TRUE);
        
        setEstructuras2(new ArrayList<Estructura>());

        if(getBusquedaInputText2().isValid()){
            Catalogo catalogo = null;
            try{
                Long idCatalogo = GeneradorDisenoHelper.getIdCatalogo(busqueda2);
                if(idCatalogo!=null){
                    catalogo = getFacade().getCatalogoService().findCatalogoByCatalogo(new Catalogo(idCatalogo));
                }
            }catch(Exception e){
                getBusquedaInputText2().setValue("");
                agregarWarnMessage("Búsqueda sin resultados");
            }
            
            if(catalogo==null){
                init2();
                agregarWarnMessage("Búsqueda sin resultados");
            }else{
                setCatalogoBusqueda(catalogo);
                getBusquedaInputText2().setReadOnly(true);
                getTipoCuadroSelect2().setReadOnly(true);
                getFiltro().setCatalogo(catalogo);
                try{
                    if (periodo == null)
                    periodo = getFacade().getPeriodoService().findMaxPeriodoObj(); 
                    
                    versionVigente2 = getFacade().getVersionService().findUltimaVersionVigente(periodo.getIdPeriodo(),getNombreUsuario(),catalogo.getIdCatalogo());
                    estructuras2 = getFacade().getEstructuraService().findEstructuraByVersion(versionVigente2);
                }catch(Exception e){
                    agregarErrorMessage("Error al buscar versiones");
                    logger.error("Error al buscar versiones",e);
                }
            }
        }else{
            init2();
            agregarWarnMessage("No debe modificar el valor autocompletado");
            getBusquedaInputText2().setValue("");
        }
        
        getBusquedaInputText2().setRequired(Boolean.FALSE);
        getTipoCuadroSelect2().setRequired(Boolean.FALSE);
        
        return null;
    }
    
    public void limpiarCatalogolistener(ActionEvent event){
       

        init();
        
        getBusquedaInputText().setValue("");
        getBusquedaInputText().setReadOnly(false);
        getTipoCuadroSelect().setReadOnly(false);
    }
    
    
    public void limpiarCatalogolistener2(ActionEvent event){
        
        
        init2();        
        
        getBusquedaInputText2().setValue("");
        getBusquedaInputText2().setReadOnly(false);
        getTipoCuadroSelect2().setReadOnly(false);
    }    
    
    public void buscarEstructuraListener(ActionEvent event) {
        try{
            final Long idEstructura = (Long)event.getComponent().getAttributes().get("idEstructura");
            Grilla grilla = this.getFacade().getGrillaService().findGrillaById(idEstructura);
            grillaVO = this.getFacade().getEstructuraService().getGrillaVO(grilla, Boolean.FALSE);
            grillaVO.setGrilla(grilla);
            renderTablaCuadro = true;
        }catch(Exception e){
            logger.error("Error al buscar estructura", e.getCause());
        }
    }
    
    public void buscarEstructuraListener2(ActionEvent event) {
        try{
            final Long idEstructura = (Long)event.getComponent().getAttributes().get("idEstructura");
            Grilla grilla = this.getFacade().getGrillaService().findGrillaById(idEstructura);
            grillaVO2 = this.getFacade().getEstructuraService().getGrillaVO(grilla, Boolean.FALSE);
            grillaVO2.setGrilla(grilla);
            renderTablaCuadro2 = true;
        }catch(Exception e){
            logger.error("Error al buscar estructura", e.getCause());
        }
    }
    
    
    public void cargarRelacionCeldaListener(ActionEvent event) {
        
        final Celda celda = (Celda)event.getComponent().getAttributes().get("celda");
        
        if(celda==null)
            return;
        
        relCelda = celda;
        relCeldaList = celda.getCeldaRelacionadaList();
        
        partialTargetChoices();
        
        
        if(Util.esListaValida(relCeldaList) || Util.esListaValida(relCeldaList))
        aplicarRelacionCelda(relacionMap, relCelda, relCeldaList);
        
        addPartialText();
        
    }
    
    private void partialTargetChoices(){
        setADFPartialTarget(celdasAgregadasChoice);
    }
    
    
    public void cargarCeldaListener(ActionEvent event) {
        
        final Celda celdaRelacionada = (Celda) event.getComponent().getAttributes().get("celda");
        
        if(celdaRelacionada==null || !validaRelCelda())
            return;
        
        RelacionCelda relacionCelda = new RelacionCelda();
        
        if (periodo == null)
        periodo = getFacade().getPeriodoService().findMaxPeriodoObj(); 
        
        relacionCelda.copyCelda(celdaRelacionada, relCelda, periodo);
        
        if(relacionMap.containsKey(relCelda)){  
        
            if (relacionMap != null && relacionMap.size() > 0 && relacionMap.get(relCelda)[0] != null && relacionMap.get(relCelda)[0].size() > 0){
                
                List<RelacionCelda>   listaRelacionCelda = relacionMap.get(relCelda)[0];                
                
                for (RelacionCelda rc : listaRelacionCelda){
                        
                    if ( relacionCelda.getIdFila().equals(rc.getIdFila()) && relacionCelda.getIdColumna().equals(rc.getIdColumna()) && relacionCelda.getIdGrilla().equals(rc.getIdGrilla()) ){
                            super.agregarWarnMessage("La celda [" + relacionCelda.getIdColumna() + "," + relacionCelda.getIdFila() +  "] ya está relacionada a la celda " + "[" + relCelda.getIdColumna() + "," + relCelda.getIdFila() +  "]. No se volverá a cargar esta relación.");
                            return;
                    }
                 }
            }        
            relacionMap.get(relCelda)[0].add(relacionCelda);
        }else{
            List<RelacionCelda> relacionCeldaTempList = new ArrayList<RelacionCelda>();
            relacionCeldaTempList.add(relacionCelda);
            aplicarRelacionCelda(relacionMap, relCelda, relacionCeldaTempList);
        }
        
        relCelda.setCeldaRelacionadaList(relacionMap.get(relCelda)[0]);
        
        setRelacionCeldaList(relacionMap.get(relCelda)[0]);
        
        AdfFacesContext.getCurrentInstance().addPartialTarget(celdasAgregadasChoice); 
        AdfFacesContext.getCurrentInstance().addPartialTarget(contadorMapeos); 
        
        addPartialText();

    }
    
    
    public void guardarRelacionListener(ActionEvent event){
        
        if(!isValidPeriodoGrilla())
            return;
        
        if(relacionMap==null || relacionMap.isEmpty()){
            agregarWarnMessage("No hay información para guardar");
            return;
        }
        
        try{        
            getFacade().getCeldaService().persistRelaccionCelda(relacionMap);
            relacionMap.clear();
            Grilla grilla = this.getFacade().getGrillaService().findGrillaById(grillaVO.getGrilla().getIdGrilla());
            grillaVO = this.getFacade().getEstructuraService().getGrillaVO(grilla, Boolean.FALSE);
            grillaVO.setGrilla(grilla);
            agregarSuccesMessage("Se ha almacenado correctamente la informacón");
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            agregarErrorMessage("Se ha producido un error al almacenar la relación");
        }
        
    }
    
    
    public void eliminarRelacionListener(ActionEvent event){
        
        if(!isValidPeriodoGrilla())
            return;
        
        try{
            
            final Celda celda = (Celda)event.getComponent().getAttributes().get("celda");
            
            if (periodo == null)
            periodo = getFacade().getPeriodoService().findMaxPeriodoObj(); 
            
            getFacade().getCeldaService().deleteRelacionCelda(celda, periodo);
            
            if(relacionMap.containsKey(celda)){
                relacionMap.remove(celda);
            }
            celda.setCeldaRelacionadaList(null);
            relCeldaList = null;
            partialTargetChoices();
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            agregarErrorMessage("Se ha producido un error al eliminar la relación");
        }
        
    }
    
    
    public void onChangeCelda(ValueChangeEvent event){
        if(event.getNewValue()!=null){
            relCeldaSeleccionada = (RelacionCelda) event.getNewValue();     
            
            Celda celda = new Celda();

            try {
                celda.setIdColumna(relCeldaSeleccionada.getIdColumna());
                celda.setIdFila(relCeldaSeleccionada.getIdFila());
                celda.setIdGrilla(relCeldaSeleccionada.getIdGrilla());
                celda = getFacade().getCeldaService().findCeldaById(celda);
                relCeldaSeleccionada.setCelda(celda);
            } catch (Exception e) {
                 e.printStackTrace();   
                 logger.error(e);
            }
            
            FacesContext context = getFacesContext();
            ExtendedRenderKitService extRenderKitSrvc = Service.getRenderKitService(context, ExtendedRenderKitService.class);
            extRenderKitSrvc.addScript(context,"AdfPage.PAGE.findComponent('popupCelda').show();");
        }
    }

    
    public void eliminarTodasRelacionListener(ActionEvent event){
        
        if(!isValidPeriodoGrilla())
            return;
        
        try{
            if (periodo == null)
            periodo = getFacade().getPeriodoService().findMaxPeriodoObj(); 
            
            getFacade().getCeldaService().deleteRelacionCeldaByGrilla(grillaVO.getGrilla(), periodo);
            relacionMap.clear();
            Grilla grilla = this.getFacade().getGrillaService().findGrillaById(grillaVO.getGrilla().getIdGrilla());
            grillaVO = this.getFacade().getEstructuraService().getGrillaVO(grilla, Boolean.FALSE);
            grillaVO.setGrilla(grilla);
            relCeldaList = null;            
            partialTargetChoices();
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            agregarErrorMessage("Se ha producido un error al almacenar la información");
        }
        
    }
    
    public void abrirPaginaRelacion(ActionEvent event){
        
        final Celda celda = (Celda)event.getComponent().getAttributes().get("celda");
        
        StringBuilder script = new StringBuilder();   
        script = script.append("window.open('relacion.jsf','_blank','menubar=no,toolbar=no,location=no,personalbar=no,directories=no,width=400,height=300,fullscreen=no,titlebar=yes,hotkeys=yes,status=yes,scrollbars=yes,resizable=yes');");   
        Service.getRenderKitService(getFacesContext(),ExtendedRenderKitService.class).addScript(getFacesContext(),script.toString()); 
    
    }
    
    public List<SelectItem> getItemCeldaList() {
        List<SelectItem> celdaList = new ArrayList<SelectItem>();
        
        for(RelacionCelda rel : getRelacionCeldaList()){
            
            Celda celdaRelacionda = new Celda();
                celdaRelacionda.setIdGrilla(rel.getIdGrilla());
                celdaRelacionda.setIdColumna(rel.getIdColumna());
                celdaRelacionda.setIdFila(rel.getIdFila());


            try {
                celdaRelacionda = super.getFacade().getCeldaService().findCeldaById(celdaRelacionda);
            } catch (Exception e) {
                   e.printStackTrace();
                   logger.error(e);
            }
            
            String nombreCatalogo = celdaRelacionda.getColumna().getGrilla().getEstructura1().getVersion().getCatalogo().getNombre();
            Long orden = celdaRelacionda.getColumna().getGrilla().getEstructura1().getOrden();
            String celdaFormatoGrilla = Util.formatCellKey( celdaRelacionda );
            celdaList.add(new SelectItem(rel, celdaFormatoGrilla  + " (" + nombreCatalogo   + ", orden : " + orden + ")" ));
        }
        
        return celdaList;
    }

    
    public void addPartialText(){
        setADFPartialTarget(relCeldaText);
    }
    
    public boolean validaRelCelda(){
        if(relCelda==null){
            agregarWarnMessage("Debe seleccionar celda primero");
            return false;
        }
        return true;
    }
    
    public void setGrillaVO(GrillaVO grillaVO) {
        this.grillaVO = grillaVO;
    }

    public GrillaVO getGrillaVO() {
        return grillaVO;
    }


    public void setRelacionMap(Map<Celda, List[]> relacionMap) {
        this.relacionMap = relacionMap;
    }

    public Map<Celda, List[]> getRelacionMap() {
        return relacionMap;
    }

    
    private void aplicarRelacionCelda(Map<Celda, List[]> relacionMap, Celda relCelda, List<RelacionCelda> relacionCeldaList) {
        List[] rel = {relacionCeldaList};
        relacionMap.put(relCelda, rel);
    }


    public void setRelCeldaText(RichInputText relCeldaText) {
        this.relCeldaText = relCeldaText;
    }

    public RichInputText getRelCeldaText() {
        return relCeldaText;
    }
    
    public void setTipoCuadroSelect(RichSelectOneChoice tipoCuadroSelect) {
        this.tipoCuadroSelect = tipoCuadroSelect;
    }

    public RichSelectOneChoice getTipoCuadroSelect() {
        return tipoCuadroSelect;
    }

    public void setCatalogos(List<Catalogo> catalogos) {
        this.catalogos = catalogos;
    }

    public List<Catalogo> getCatalogos() {
        return catalogos;
    }

    public void setBusqueda(String busqueda) {
        this.busqueda = busqueda;
    }

    public String getBusqueda() {
        return busqueda;
    }

    public void setBusquedaInputText(RichInputText busquedaInputText) {
        this.busquedaInputText = busquedaInputText;
    }

    public RichInputText getBusquedaInputText() {
        return busquedaInputText;
    }
    
    public List<Catalogo> getCatalogosFiltrados() {
        if(catalogosFiltrados==null){
            catalogosFiltrados = new ArrayList<Catalogo>();
        }
        return catalogosFiltrados;
    }

    public void setCatalogosFiltrados(List<Catalogo> catalogosFiltrados) {
        this.catalogosFiltrados = catalogosFiltrados;
    }

    public void setEstructuras(List<Estructura> estructuras) {
        this.estructuras = estructuras;
    }

    public List<Estructura> getEstructuras() {
        return estructuras;
    }

    public void setCatalogoBusqueda(Catalogo catalogoBusqueda) {
        this.catalogoBusqueda = catalogoBusqueda;
    }

    public Catalogo getCatalogoBusqueda() {
        return catalogoBusqueda;
    }

    public void setVersionVigente(Version versionVigente) {
        this.versionVigente = versionVigente;
    }

    public Version getVersionVigente() {
        return versionVigente;
    }
    
    public void setNumeroCuenta(Long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public Long getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setEstadoFinanciero(EstadoFinanciero estadoFinanciero) {
        this.estadoFinanciero = estadoFinanciero;
    }

    public EstadoFinanciero getEstadoFinanciero() {
        return estadoFinanciero;
    }

    public void setDetalleEeff(DetalleEeff detalleEeff) {
        this.detalleEeff = detalleEeff;
    }

    public DetalleEeff getDetalleEeff() {
        return detalleEeff;
    }

    public void setCuentaChoice(RichSelectOneChoice cuentaChoice) {
        this.cuentaChoice = cuentaChoice;
    }

    public RichSelectOneChoice getCuentaChoice() {
        return cuentaChoice;
    }

    public void setRelCelda(Celda relCelda) {
        this.relCelda = relCelda;
    }

    public Celda getRelCelda() {
        return relCelda;
    }

    
    public void setRelacionCeldaList(List<RelacionCelda> relacionCeldaOneChoineList) {
        this.relCeldaList = relacionCeldaOneChoineList;
    }
    
    public List<RelacionCelda> getRelacionCeldaList() {
        if(relCeldaList==null){
            relCeldaList = new ArrayList<RelacionCelda>();
        }
        return relCeldaList;
    }

    public boolean isRenderTablaCuadro() {
        return renderTablaCuadro;
    }
    
    public boolean isValidPeriodoGrilla(){
        
        if(grillaVO==null || grillaVO.getGrilla()==null || grillaVO.getGrilla().getIdGrilla()==null)
            agregarWarnMessage("Debe seleccionar un cuadro");
        else if(periodo==null || periodo.getIdPeriodo() == null)
            agregarWarnMessage("Período inválido, debe ingresar a la página nuevamente");
        else
            return true;
        
        return false;
    }
    
    private void init(){
        busqueda = null;
        periodo = null;
        versionVigente = null;
        estructuras = null;
        estadoFinanciero = null;
        detalleEeff = null;
        numeroCuenta = null;
        grillaVO = null;
        relCelda = null;
        estructuras  = null;
        renderTablaCuadro = false;
        relacionMap = new LinkedHashMap<Celda, List[]>();
    }
    
    private void init2(){
        busqueda2 = null;
        periodo = null;
        versionVigente2 = null;
        estructuras2 = null;
        catalogosFiltrados = null;
        catalogoBusqueda = null;
        estadoFinanciero = null;
        detalleEeff = null;
        numeroCuenta = null;
        grillaVO2 = null;        
        estructuras2  = null;
        renderTablaCuadro2 = false;
        relacionMap = new LinkedHashMap<Celda, List[]>();
    }

    
    
    private void initBuscar(){
        grillaVO = null;
        relCelda = null;
        renderTablaCuadro = false;
        relacionMap = new LinkedHashMap<Celda, List[]>();
    }
    
    private void initBuscar2(){
        grillaVO2 = null;        
        renderTablaCuadro2 = false;
        relacionMap = new LinkedHashMap<Celda, List[]>();
    }
    
    public int getCountMapping(){
        
        if(relacionMap==null)
            return 0;
        
        return relacionMap.size();
    }


    public void setTipoCuadroSelect2(RichSelectOneChoice tipoCuadroSelect2) {
        this.tipoCuadroSelect2 = tipoCuadroSelect2;
    }

    public RichSelectOneChoice getTipoCuadroSelect2() {
        return tipoCuadroSelect2;
    }

    public void setBusquedaInputText2(RichInputText busquedaInputText2) {
        this.busquedaInputText2 = busquedaInputText2;
    }

    public RichInputText getBusquedaInputText2() {
        return busquedaInputText2;
    }

    public void setBusqueda2(String busqueda2) {
        this.busqueda2 = busqueda2;
    }

    public String getBusqueda2() {
        return busqueda2;
    }

    public void setVersionVigente2(Version versionVigente2) {
        this.versionVigente2 = versionVigente2;
    }

    public Version getVersionVigente2() {
        return versionVigente2;
    }

    public void setEstructuras2(List<Estructura> estructuras2) {
        this.estructuras2 = estructuras2;
    }

    public List<Estructura> getEstructuras2() {
        return estructuras2;
    }

    public void setGrillaVO2(GrillaVO grillaVO2) {
        this.grillaVO2 = grillaVO2;
    }

    public GrillaVO getGrillaVO2() {
        return grillaVO2;
    }

    public boolean isRenderTablaCuadro2() {
        return renderTablaCuadro2;
    }

    public void setRelCeldaList(List<RelacionCelda> relCeldaList) {
        this.relCeldaList = relCeldaList;
    }

    public List<RelacionCelda> getRelCeldaList() {
        return relCeldaList;
    }

    public void setCeldasAgregadasChoice(RichSelectOneChoice celdasAgregadasChoice) {
        this.celdasAgregadasChoice = celdasAgregadasChoice;
    }

    public RichSelectOneChoice getCeldasAgregadasChoice() {
        return celdasAgregadasChoice;
    }

    public void setRelCeldaSeleccionada(RelacionCelda relCeldaSeleccionada) {
        this.relCeldaSeleccionada = relCeldaSeleccionada;
    }

    public RelacionCelda getRelCeldaSeleccionada() {
        return relCeldaSeleccionada;
    }

    public void setContadorMapeos(RichOutputText contadorMapeos) {
        this.contadorMapeos = contadorMapeos;
    }

    public RichOutputText getContadorMapeos() {
        return contadorMapeos;
    }
}
