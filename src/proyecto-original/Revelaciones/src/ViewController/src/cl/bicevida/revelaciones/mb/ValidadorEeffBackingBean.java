package cl.bicevida.revelaciones.mb;

import cl.bicevida.revelaciones.common.mb.SoporteBackingBean;
import cl.bicevida.revelaciones.common.util.GeneradorDisenoHelper;
import cl.bicevida.revelaciones.ejb.cross.EeffUtil;
import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.revelaciones.ejb.entity.Catalogo;
import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.CodigoFecu;
import cl.bicevida.revelaciones.ejb.entity.DetalleEeff;
import cl.bicevida.revelaciones.ejb.entity.EstadoFinanciero;
import cl.bicevida.revelaciones.ejb.entity.Estructura;
import cl.bicevida.revelaciones.ejb.entity.Grilla;
import cl.bicevida.revelaciones.ejb.entity.Periodo;
import cl.bicevida.revelaciones.ejb.entity.RelacionEeff;
import cl.bicevida.revelaciones.ejb.entity.TipoCuadro;

import cl.bicevida.revelaciones.ejb.entity.Version;

import cl.bicevida.revelaciones.ejb.entity.pk.DetalleEeffPK;

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

import oracle.adf.view.rich.model.AutoSuggestUIHints;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;

public class ValidadorEeffBackingBean extends SoporteBackingBean {
    
    private transient Logger logger = Logger.getLogger(SoporteBackingBean.class);
    private transient RichSelectOneChoice tipoCuadroSelect;
    private transient RichInputText busquedaInputText;  
    private List<Catalogo> catalogos;
    private String busqueda;
    private Periodo periodo;
    private List<EstadoFinanciero> eeffs;
    private Version versionVigente;
    private List<Estructura> estructuras;
    private List<Catalogo> catalogosFiltrados;
    private List<DetalleEeff> detalleEeffs;
    private Catalogo catalogoBusqueda;
    private EstadoFinanciero estadoFinanciero;
    private DetalleEeff detalleEeff;
    private Long numeroCuenta;
    private transient RichSelectOneChoice cuentaChoice;
    private GrillaVO grillaVO;
    private String relCelda;
    private String relFecu;
    private String relCuenta;
    /*
     * String[0] --> Fecus
     * String[1] --> Cuentas
     */
    private Map<String, String[]> relacionMap;
    private transient RichInputText relCeldaText;
    private transient RichInputText relFecuText;
    private transient RichInputText relCuentaText;

    public ValidadorEeffBackingBean() {
    }
    
    @PostConstruct
    public void cargarPeriodo(){
        try{
            periodo = getFacade().getPeriodoService().findMaxPeriodoObj();
            eeffs = getFacade().getEstadoFinancieroService().getEeffVigenteByPeriodo(periodo.getIdPeriodo());
            relacionMap = new LinkedHashMap<String, String[]>();
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
            for(Catalogo catalogo : getCatalogos()){
                if(catalogo.getNombre().toUpperCase().indexOf(autoSuggestUIHints.getSubmittedValue().toUpperCase()) >= 0){
                    this.getCatalogosFiltrados().add(catalogo);
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
                agregarWarnMessage("Busqueda sin resultados");
            }
            
            if(catalogo==null){
                init();
                agregarWarnMessage("Busqueda sin resultados");
            }else{
                setCatalogoBusqueda(catalogo);
                getBusquedaInputText().setReadOnly(true);
                getTipoCuadroSelect().setReadOnly(true);
                getFiltro().setCatalogo(catalogo);
                try{
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
    
    public void fecuChangeListener(ValueChangeEvent valueChangeEvent) {
        
        EstadoFinanciero eeff = (EstadoFinanciero)valueChangeEvent.getNewValue();
        
        if(eeff==null){
            detalleEeffs = null;
            return;
        }
        
        detalleEeffs = getFacade().getEstadoFinancieroService().getDetalleEeffByEeff(eeff);
        
        cuentaChoice.resetValue();
    }
    
    public List<SelectItem> getFecuItems() {
        List<SelectItem> fecuItems = new ArrayList<SelectItem>();
        for (EstadoFinanciero eeff : eeffs) {
            fecuItems.add(new SelectItem(eeff, EeffUtil.formatFecu(eeff.getIdFecu())));
        }
        return fecuItems;
    }
    
    public void limpiarCatalogolistener(ActionEvent event){
        init();
        getBusquedaInputText().setValue("");
        getBusquedaInputText().setReadOnly(false);
        getTipoCuadroSelect().setReadOnly(false);
    }
    
    public void buscarEstadoFinanciero(ActionEvent event){
            
    }
    
    private void init(){
        versionVigente = null;
        estructuras  = null;
    }


    public void codigoFecuChangeListener(ValueChangeEvent valueChangeEvent) {
        if(valueChangeEvent.getNewValue()!=null){
            detalleEeffs = getFacade().getEstadoFinancieroService().getDetalleEeffByEeff((EstadoFinanciero)valueChangeEvent.getNewValue());
        }
    }

    public void buscarEstructuraListener(ActionEvent event) {
        try{
            final Long idEstructura = (Long)event.getComponent().getAttributes().get("idEstructura");
            Grilla grilla = this.getFacade().getGrillaService().findGrillaById(idEstructura);
            grillaVO = this.getFacade().getEstructuraService().getGrillaVO(grilla, Boolean.FALSE);
            grillaVO.setGrilla(grilla);
        }catch(Exception e){
            logger.error("Error al buscar estructura", e.getCause());
        }
    }
    
    public void cargarRelacionCeldaListener(ActionEvent event) {
        
        final Celda celda = (Celda)event.getComponent().getAttributes().get("celda");
        
        if(celda==null)
            return;
        
        relCelda = Util.formatCellKey(celda);
        relFecu = EeffUtil.formatKeyFecu(celda.getRelacionEeffList());
        relCuenta = EeffUtil.formatKeyCuenta(celda.getRelacionDetalleEeffList());
        relFecuText.setRows(Util.countToken(relFecu,";"));
        relCuentaText.setRows(Util.countToken(relCuenta,";"));
        
        if(relFecu!=null || relCuenta!=null)
            aplicarRelacion(relacionMap, relCelda, relFecu, relCuenta);
        
        addPartialText();
        
    }
    
    public void cargarFecuListener(ActionEvent event) {
            
            final EstadoFinanciero eeff = (EstadoFinanciero)event.getComponent().getAttributes().get("eeff");
            
            if(eeff==null)
                return;
            
            relFecu = "[" + EeffUtil.formatFecu(eeff.getIdFecu()) + "];";
            
            aplicarRelacion(relacionMap, relCelda, relFecu, relCuenta);
            
            addPartialText();
    }
    
    public void cargarCuentaListener(ActionEvent event) {
        
        final DetalleEeff detalleEeff = (DetalleEeff)event.getComponent().getAttributes().get("detalleEeff");
        
        if(detalleEeff==null)
            return;
        
        if(validaRelCelda()){
            
            if(EeffUtil.esCuentaRepetida(relCuenta, detalleEeff.getIdCuenta().toString())){
                agregarWarnMessage("Número de cuenta ya esta ingresado para la celda seleccionada " +  relCelda);
                return;
            }
        
            if(relacionMap.containsKey(relCelda)){
                relCuenta = getValorStr(relacionMap.get(relCelda)[1] ) + "+[" + detalleEeff.getIdCuenta() + "];";
            }else{
                relCuenta = getValorStr(relCuenta) + "[" + detalleEeff.getIdCuenta() + "];";
            }
            
            aplicarRelacion(relacionMap, relCelda, relFecu, relCuenta);
            
        }else{
            relCuentaText.setRows(0);
        }
        
        relCuentaText.setRows(relCuentaText.getRows()+1);
        addPartialText();
    }
    
    public void guardarRelacionListener(ActionEvent event){
        
        try{        
            getFacade().getEstadoFinancieroService().persistRelaccionEeff(relacionMap, periodo.getIdPeriodo(),grillaVO.getGrilla());
            relacionMap.clear();
            Grilla grilla = this.getFacade().getGrillaService().findGrillaById(grillaVO.getGrilla().getIdGrilla());
            grillaVO = this.getFacade().getEstructuraService().getGrillaVO(grilla, Boolean.FALSE);
            grillaVO.setGrilla(grilla);
            agregarSuccesMessage("Se ha almacenado correctamente la informacón");
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void abrirPaginaRelacion(ActionEvent event){
        
        final Celda celda = (Celda)event.getComponent().getAttributes().get("celda");
        
        StringBuilder script = new StringBuilder();   
        script = script.append("window.open('relacion.jsf','_blank','menubar=no,toolbar=no,location=no,personalbar=no,directories=no,width=400,height=300,fullscreen=no,titlebar=yes,hotkeys=yes,status=yes,scrollbars=yes,resizable=yes');");   
        Service.getRenderKitService(getFacesContext(),ExtendedRenderKitService.class).addScript(getFacesContext(),script.toString()); 
    
    }
    
    private String getValorStr(String str){
        if(str==null)
            return "";
        else
            return str;
    }
    
    public void addPartialText(){
        setADFPartialTarget(relCeldaText);
        setADFPartialTarget(relFecuText);
        setADFPartialTarget(relCuentaText);
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

    public void setRelCelda(String relCelda) {
        this.relCelda = relCelda;
    }

    public String getRelCelda() {
        return relCelda;
    }

    public void setRelFecu(String relFecu) {
        this.relFecu = relFecu;
    }

    public String getRelFecu() {
        return relFecu;
    }

    public void setRelCuenta(String relCuenta) {
        this.relCuenta = relCuenta;
    }

    public String getRelCuenta() {
        return relCuenta;
    }

    public void setRelacionMap(Map<String, String[]> relacionMap) {
        this.relacionMap = relacionMap;
    }

    public Map<String, String[]> getRelacionMap() {
        return relacionMap;
    }

    private void aplicarRelacion(Map<String, String[]> relacionMap, String relCelda, String relFecu, String relCuenta) {
        String[] rel = {relFecu, relCuenta};
        relacionMap.put(relCelda, rel);
    }

    public void setRelCeldaText(RichInputText relCeldaText) {
        this.relCeldaText = relCeldaText;
    }

    public RichInputText getRelCeldaText() {
        return relCeldaText;
    }

    public void setRelFecuText(RichInputText relFecuText) {
        this.relFecuText = relFecuText;
    }

    public RichInputText getRelFecuText() {
        return relFecuText;
    }

    public void setRelCuentaText(RichInputText relFecuCuenta) {
        this.relCuentaText = relFecuCuenta;
    }

    public RichInputText getRelCuentaText() {
        return relCuentaText;
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

    public List<EstadoFinanciero> getEeffs() {
        return eeffs;
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

    public List<DetalleEeff> getDetalleEeffs() {
        return detalleEeffs;
    }
}
