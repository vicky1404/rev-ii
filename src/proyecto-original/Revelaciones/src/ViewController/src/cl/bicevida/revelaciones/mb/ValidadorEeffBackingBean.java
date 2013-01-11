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
import cl.bicevida.revelaciones.ejb.entity.RelacionDetalleEeff;
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
    
    private transient Logger logger = Logger.getLogger(ValidadorEeffBackingBean.class);
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
    private Celda relCelda;
    private List<RelacionEeff> relEeffList;
    private List<RelacionDetalleEeff> relEeffDetList;
    private RelacionEeff relFecuSeleccionada;
    private RelacionDetalleEeff relCuentaSeleccionada;
    private boolean renderTablaCuadro = false;
    private RichSelectOneChoice fecuAgregadasChoice;
    private RichSelectOneChoice cuentaAgregadasChoice;
    
    private static final String POPUP_FECU = "popupFecu";
    private static final String POPUP_CUENTA = "popupCuenta";
        
    /*
     * String[0] --> Fecus
     * String[1] --> Cuentas
     */
    private Map<Celda, List[]> relacionMap;
    private transient RichInputText relCeldaText;
    

    public ValidadorEeffBackingBean() {
    }
    
    @PostConstruct
    public void cargarPeriodo(){
        try{
            periodo = getFacade().getPeriodoService().findMaxPeriodoObj();
            eeffs = getFacade().getEstadoFinancieroService().getEeffVigenteByPeriodo(periodo.getIdPeriodo());
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

    public void codigoFecuChangeListener(ValueChangeEvent valueChangeEvent) {
        if(valueChangeEvent.getNewValue()!=null){
            detalleEeffs = getFacade().getEstadoFinancieroService().getDetalleEeffByEeff((EstadoFinanciero)valueChangeEvent.getNewValue());
        }else{
            detalleEeffs = null;
        }
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
    
    public void cargarRelacionCeldaListener(ActionEvent event) {
        
        final Celda celda = (Celda)event.getComponent().getAttributes().get("celda");
        
        if(celda==null)
            return;
        
        relCelda = celda;
        relEeffList = celda.getRelacionEeffList();
        relEeffDetList = celda.getRelacionDetalleEeffList();
        partialTargetChoices();
        
        
        if(Util.esListaValida(relEeffList) || Util.esListaValida(relEeffDetList))
            aplicarRelacion(relacionMap, relCelda, relEeffList, relEeffDetList);
        
        addPartialText();
        
    }
    
    private void partialTargetChoices(){
        setADFPartialTarget(fecuAgregadasChoice);
        setADFPartialTarget(cuentaAgregadasChoice);
    }
    
    public void cargarFecuListener(ActionEvent event) {
            
            final EstadoFinanciero eeff = (EstadoFinanciero)event.getComponent().getAttributes().get("eeff");
            
            if(eeff==null || !validaRelCelda())
                return;
            
            
            RelacionEeff relEeff = new RelacionEeff();
            relEeff.copyEstadoFinanciero(eeff, relCelda, periodo);

            
            if(relacionMap.containsKey(relCelda)){  
                if(relacionMap.get(relCelda)[0].contains(relEeff)){
                    super.agregarWarnMessage(MessageFormat.format("El código FECU {0} ya esta relacionado con la celda [{1},{2}]", eeff.getFecuFormat(), relCelda.getIdColumna(), relCelda.getIdFila()));
                    return;
                }
                relacionMap.get(relCelda)[0].add(relEeff);
            }else{
                List<RelacionEeff> eeffTempList = new ArrayList<RelacionEeff>();
                eeffTempList.add(relEeff);
                aplicarRelacion(relacionMap, relCelda, eeffTempList, new ArrayList<RelacionDetalleEeff>());
            }
            
            relCelda.setRelacionEeffList(relacionMap.get(relCelda)[0]);
            setRelEeffList(relacionMap.get(relCelda)[0]);
            
            addPartialText();
    }
    
    public void cargarCuentaListener(ActionEvent event) {
        
        final DetalleEeff detalleEeff = (DetalleEeff)event.getComponent().getAttributes().get("detalleEeff");
        
        if(detalleEeff==null || !validaRelCelda())
            return;

        if(EeffUtil.esCuentaRepetida(relEeffDetList, detalleEeff)){
            agregarWarnMessage("Número de cuenta ya esta ingresado para la celda seleccionada " + Util.formatCellKey(relCelda));
            return;
        }
        
        RelacionDetalleEeff relEeffDet = new RelacionDetalleEeff();
        relEeffDet.copyDetalleEeff(detalleEeff, relCelda, periodo);
    
        if(relacionMap.containsKey(relCelda)){
            relacionMap.get(relCelda)[1].add(relEeffDet);
        }else{
            List<RelacionDetalleEeff> relEeffDetTempList = new ArrayList<RelacionDetalleEeff>();
            relEeffDetTempList.add(relEeffDet);
            aplicarRelacion(relacionMap, relCelda, new ArrayList<RelacionEeff>(), relEeffDetTempList);
        }
        
        getRelEeffDetList().add(relEeffDet);
        
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
            getFacade().getEstadoFinancieroService().persistRelaccionEeff(relacionMap, periodo.getIdPeriodo());
            relacionMap.clear();
            Grilla grilla = this.getFacade().getGrillaService().findGrillaById(grillaVO.getGrilla().getIdGrilla());
            grillaVO = this.getFacade().getEstructuraService().getGrillaVO(grilla, Boolean.FALSE);
            grillaVO.setGrilla(grilla);
            agregarSuccesMessage("Se ha almacenado correctamente la informacón");
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            agregarErrorMessage("Se ha producido un error al almacenar el mapeo");
        }
        
    }
    
    
    public void eliminarRelacionListener(ActionEvent event){
        
        if(!isValidPeriodoGrilla())
            return;
        
        try{
            final Celda celda = (Celda)event.getComponent().getAttributes().get("celda");
            getFacade().getEstadoFinancieroService().deleteRelacionAllEeffByCelda(celda);
            if(relacionMap.containsKey(celda)){
                relacionMap.remove(celda);
            }
            celda.setRelacionEeffList(null);
            celda.setRelacionDetalleEeffList(null);
            relEeffList = null;
            relEeffDetList = null;
            partialTargetChoices();
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            agregarErrorMessage("Se ha producido un error al eliminar la relación");
        }
        
    }
    
    public void eliminarTodasRelacionListener(ActionEvent event){
        
        if(!isValidPeriodoGrilla())
            return;
        
        try{
            getFacade().getEstadoFinancieroService().deleteAllRelacionByGrillaPeriodo(periodo.getIdPeriodo(), grillaVO.getGrilla().getIdGrilla());
            relacionMap.clear();
            Grilla grilla = this.getFacade().getGrillaService().findGrillaById(grillaVO.getGrilla().getIdGrilla());
            grillaVO = this.getFacade().getEstructuraService().getGrillaVO(grilla, Boolean.FALSE);
            grillaVO.setGrilla(grilla);
            relEeffList = null;
            relEeffDetList = null;
            partialTargetChoices();
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            agregarErrorMessage("Se ha producido un error al almacenar la información");
        }
        
    }
    
    public void onChangeFecu(ValueChangeEvent event){
        if(event.getNewValue()!=null){
            relFecuSeleccionada = (RelacionEeff)event.getNewValue();
            FacesContext context = getFacesContext();
            ExtendedRenderKitService extRenderKitSrvc = Service.getRenderKitService(context, ExtendedRenderKitService.class);
            extRenderKitSrvc.addScript(context,"AdfPage.PAGE.findComponent('" + POPUP_FECU + "').show();");
        }
    }
    
    public void onChangeCuenta(ValueChangeEvent event){
        if(event.getNewValue()!=null){
            relCuentaSeleccionada = (RelacionDetalleEeff)event.getNewValue();
            FacesContext context = getFacesContext();
            ExtendedRenderKitService extRenderKitSrvc = Service.getRenderKitService(context, ExtendedRenderKitService.class);
            extRenderKitSrvc.addScript(context,"AdfPage.PAGE.findComponent('" + POPUP_CUENTA + "').show();");
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

    private void aplicarRelacion(Map<Celda, List[]> relacionMap, Celda relCelda, List<RelacionEeff> relFecuList, List<RelacionDetalleEeff> relCuentaList) {
        List[] rel = {relFecuList, relCuentaList};
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

    public void setRelCelda(Celda relCelda) {
        this.relCelda = relCelda;
    }

    public Celda getRelCelda() {
        return relCelda;
    }

    public void setRelFecuSeleccionada(RelacionEeff relFecuSeleccionada) {
        this.relFecuSeleccionada = relFecuSeleccionada;
    }

    public RelacionEeff getRelFecuSeleccionada() {
        return relFecuSeleccionada;
    }

    public void setRelCuentaSeleccionada(RelacionDetalleEeff relCuentaSeleccionada) {
        this.relCuentaSeleccionada = relCuentaSeleccionada;
    }

    public RelacionDetalleEeff getRelCuentaSeleccionada() {
        return relCuentaSeleccionada;
    }

    public void setRelEeffList(List<RelacionEeff> relEeffOneChoineList) {
        this.relEeffList = relEeffOneChoineList;
    }

    public List<RelacionEeff> getRelEeffList() {
        if(relEeffList==null){
            relEeffList = new ArrayList<RelacionEeff>();
        }
        return relEeffList;
    }

    public void setRelEeffDetList(List<RelacionDetalleEeff> relEeffDetOneChoineList) {
        this.relEeffDetList = relEeffDetOneChoineList;
    }

    public List<RelacionDetalleEeff> getRelEeffDetList() {
        if(relEeffDetList==null){
            relEeffDetList = new ArrayList<RelacionDetalleEeff>();
        }
        return relEeffDetList;
    }

    public List<SelectItem> getItemFecuList() {
        List<SelectItem> fecuList = new ArrayList<SelectItem>();
        
        for(RelacionEeff rel : getRelEeffList()){
            fecuList.add(new SelectItem(rel, rel.getFecuFormat()));
        }
        
        return fecuList;
    }

    public List<SelectItem> getItemCuentaList() {
        List<SelectItem> cuentaList = new ArrayList<SelectItem>();
        
        for(RelacionDetalleEeff rel : getRelEeffDetList()){
            cuentaList.add(new SelectItem(rel, rel.getIdCuenta()+""));
        }
        
        return cuentaList;
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
        //periodo = null;
        //eeffs = null;
        versionVigente = null;
        estructuras = null;
        //catalogosFiltrados = null;
        detalleEeffs = null;
        //catalogoBusqueda = null;
        estadoFinanciero = null;
        detalleEeff = null;
        numeroCuenta = null;
        //cuentaChoice = null;
        grillaVO = null;
        relCelda = null;
        relEeffList = null;
        relEeffDetList = null;
        relFecuSeleccionada = null;
        relCuentaSeleccionada = null;
        estructuras  = null;
        renderTablaCuadro = false;
        relacionMap = new LinkedHashMap<Celda, List[]>();
    }
    
    private void initBuscar(){
        grillaVO = null;
        relCelda = null;
        relEeffList = null;
        relEeffDetList = null;
        relFecuSeleccionada = null;
        relCuentaSeleccionada = null;
        renderTablaCuadro = false;
        relacionMap = new LinkedHashMap<Celda, List[]>();
    }

    public void setFecuAgregadasChoice(RichSelectOneChoice fecuAgregadasChoice) {
        this.fecuAgregadasChoice = fecuAgregadasChoice;
    }

    public RichSelectOneChoice getFecuAgregadasChoice() {
        return fecuAgregadasChoice;
    }

    public void setCuentaAgregadasChoice(RichSelectOneChoice cuentaAgregadasChoice) {
        this.cuentaAgregadasChoice = cuentaAgregadasChoice;
    }

    public RichSelectOneChoice getCuentaAgregadasChoice() {
        return cuentaAgregadasChoice;
    }
    
    public int getCountMapping(){
        
        if(relacionMap==null)
            return 0;
        
        return relacionMap.size();
    }
}
