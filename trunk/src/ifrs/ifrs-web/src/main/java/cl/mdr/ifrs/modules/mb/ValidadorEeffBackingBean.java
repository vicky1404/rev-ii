package cl.mdr.ifrs.modules.mb;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.model.TreeFormula;
import cl.mdr.ifrs.ejb.cross.EeffUtil;
import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.Catalogo;
import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.ifrs.ejb.entity.Columna;
import cl.mdr.ifrs.ejb.entity.DetalleEeff;
import cl.mdr.ifrs.ejb.entity.EstadoFinanciero;
import cl.mdr.ifrs.ejb.entity.Estructura;
import cl.mdr.ifrs.ejb.entity.Grilla;
import cl.mdr.ifrs.ejb.entity.Periodo;
import cl.mdr.ifrs.ejb.entity.PeriodoEmpresa;
import cl.mdr.ifrs.ejb.entity.RelacionDetalleEeff;
import cl.mdr.ifrs.ejb.entity.RelacionEeff;
import cl.mdr.ifrs.ejb.entity.TipoCuadro;
import cl.mdr.ifrs.ejb.entity.Version;
import cl.mdr.ifrs.vo.GrillaVO;

/**
 * @author Manuel Gutierrez C.
 * @since 15/07/2012
 * Clase controladora para la pagina de estados financieros.
 *
 */
@ManagedBean(name="eeff")
@ViewScoped
public class ValidadorEeffBackingBean extends AbstractBackingBean{
	
	
	private transient Logger logger = Logger.getLogger(ValidadorEeffBackingBean.class);
	private transient SelectOneMenu tipoCuadroSelect;
    private transient AutoComplete busquedaInputText; 
    private Catalogo catalogo;
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
    private transient SelectOneMenu cuentaChoice;
    private GrillaVO grillaVO;
    private Celda relCelda;
    private List<RelacionEeff> relEeffList;
    private List<RelacionDetalleEeff> relEeffDetList;
    private RelacionEeff relFecuSeleccionada;
    private RelacionDetalleEeff relCuentaSeleccionada;
    private boolean renderTablaCuadro = false;
    private SelectOneMenu fecuAgregadasChoice;
    private SelectOneMenu cuentaAgregadasChoice;
    private TreeNode root;
    private boolean renderTreeTabla;
    private boolean renderEstructuraTabla;
    
    private static final String POPUP_FECU = "popupFecu";
    private static final String POPUP_CUENTA = "popupCuenta";
        
    /*
     * String[0] --> Fecus
     * String[1] --> Cuentas
     */
    private Map<Celda, List[]> relacionMap;
    private transient InputText relCeldaText;
    
    
    public ValidadorEeffBackingBean() {
    }
    
    @SuppressWarnings("rawtypes")
	@PostConstruct
    public void cargarPeriodo(){
        try{
            periodo = getFacadeService().getPeriodoService().findMaxPeriodoObj();
            eeffs = getFacadeService().getEstadoFinancieroService().getEeffVigenteByPeriodo(periodo.getIdPeriodo());
            relacionMap = new LinkedHashMap<Celda, List[]>();
        }catch(Exception e){
            logger.error("Error en metodo cargarPeriodo", e);
            addErrorMessage("Error al cargar página");
        }
    }
    
    public void tipoCuadroChangeListener() {
        try {
            TipoCuadro tipoCuadro =  (TipoCuadro) getTipoCuadroSelect().getValue();
            List<Catalogo> catalogos = getFacadeService().getCatalogoService().findCatalogoByFiltro(getFiltroBackingBean().getEmpresa().getIdRut(), getNombreUsuario(), tipoCuadro, null, 1L);
            setCatalogos(catalogos);
        } catch (Exception e) {
            addErrorMessage("Error al buscar catalogo");
            logger.error(e.getMessage(),e);
        }
    }
    
    public List<Catalogo> catalogosSugeridos(String filtro) {
    	this.getCatalogosFiltrados().clear();
    	for (Catalogo catalogo : this.getCatalogos()){
    		
    		if(catalogo.getNombre().toUpperCase().indexOf(filtro.toUpperCase()) >= 0){
                this.getCatalogosFiltrados().add(catalogo);
            }
    	}
    	
    	return getCatalogosFiltrados();
        
    }
    
    
    public void buscarCatalogo() {
    	
    	initBuscar();
        
        setEstructuras(new ArrayList<Estructura>());
        List<Version> lista = new ArrayList<Version>();
        if(getBusquedaInputText().isValid()){
            Catalogo catalogo = null;
            
            try{
                
                if(getCatalogo() !=null && getCatalogo().getIdCatalogo() !=null){
                    catalogo = getFacadeService().getCatalogoService().findCatalogoByCatalogo(getCatalogo());
               
                }
            }catch(Exception e){
                getBusquedaInputText().setValue(null);
                addWarnMessage("Búsqueda sin resultados");
            }
            
            if(catalogo==null){
                init();
                addWarnMessage("Búsqueda sin resultados");
            }else{
                setCatalogoBusqueda(catalogo);
                getBusquedaInputText().setReadonly(Boolean.TRUE);
                getTipoCuadroSelect().setReadonly(Boolean.TRUE);
                getFiltroBackingBean().setCatalogo(catalogo);
                try{
                    versionVigente = getFacadeService().getVersionService().findUltimaVersionVigente(periodo.getIdPeriodo(),getNombreUsuario(),catalogo.getIdCatalogo());
                    estructuras = getFacadeService().getEstructuraService().findEstructuraByVersion(versionVigente);
                }catch(Exception e){
                    addErrorMessage("Error al buscar versiones");
                    logger.error("Error al buscar versiones",e);
                }
            }
        }else{
            init();
            addWarnMessage("No debe modificar el valor autocompletado");
            getBusquedaInputText().setValue(null);
        }
        lista.add(versionVigente);
        setTreeNode(lista);
        setRenderTreeTabla(Boolean.TRUE);
        System.out.println("*************************");
        System.out.println("lista:" + lista.size());
        System.out.println("*************************");
        
    }
    
    
    public void setTreeNode(List<Version> lista){
    	
    	root = new DefaultTreeNode("root", null);
    	
    	for (Version version : lista){
    		
    		TreeNode nodoVersion = new DefaultTreeNode( new TreeFormula(version.getCatalogo().getTitulo() , version.getVersion().toString() , version.getCatalogo().getNombre(), version.getEstado().getNombre(), Util.getString( version.getFechaCreacion() ), version.getVigencia().toString(), null , null ,null) , root);
    		
    			for (Estructura estructura : version.getEstructuraList()){
    				
    					if (estructura.getTipoEstructura().getNombre().toUpperCase().equals("GRILLA")){
    						
    						new DefaultTreeNode( new TreeFormula("", "", "", "", "", "", "search.png", version.getCatalogo() , estructura) , nodoVersion);
    					} 
    			}
    		
    	}
    	
    	
    	
    }
    
    public void fecuChangeListener(ValueChangeEvent valueChangeEvent) {
        
        EstadoFinanciero eeff = (EstadoFinanciero)valueChangeEvent.getNewValue();
        
        if(eeff==null){
            detalleEeffs = null;
            return;
        }
        
        detalleEeffs = getFacadeService().getEstadoFinancieroService().getDetalleEeffByEeff(eeff);
        
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
        getBusquedaInputText().setValue(null);
        getTipoCuadroSelect().setValue(null);
        getBusquedaInputText().setReadonly(Boolean.FALSE);
        getTipoCuadroSelect().setReadonly(Boolean.FALSE);
    }
    
    public void buscarEstadoFinanciero(ActionEvent event){
            
    }
    
    public void codigoFecuChangeListener() {
    	
        if(this.getEstadoFinanciero() != null){
        	
            detalleEeffs = getFacadeService().getEstadoFinancieroService().getDetalleEeffByEeff(this.getEstadoFinanciero());
            
        }
    }

    public void buscarEstructuraListener(ActionEvent event) {
        try{
            //final Long idEstructura = (Long)event.getComponent().getAttributes().get("idEstructura");
        	final Estructura estructura = (Estructura)event.getComponent().getAttributes().get("estructura");
            Grilla grilla = this.getFacadeService().getGrillaService().findGrillaById(estructura.getIdEstructura());
            grillaVO = this.getFacadeService().getEstructuraService().getGrillaVO(grilla, Boolean.FALSE);
            grillaVO.setGrilla(grilla);
            setRenderEstructuraTabla(Boolean.TRUE);
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
        
        
        
        if(Util.esListaValida(relEeffList) || Util.esListaValida(relEeffDetList))
            aplicarRelacion(relacionMap, relCelda, relEeffList, relEeffDetList);
        
        
    }
    
    private void aplicarRelacion(Map<Celda, List[]> relacionMap, Celda relCelda, List<RelacionEeff> relFecuList, List<RelacionDetalleEeff> relCuentaList) {
        List[] rel = {relFecuList, relCuentaList};
        relacionMap.put(relCelda, rel);
    }
    
   
    
    public void cargarFecuListener(ActionEvent event) {
            
    	final EstadoFinanciero eeff = (EstadoFinanciero)event.getComponent().getAttributes().get("eeff");
        
        if(eeff==null || !validaRelCelda())
            return;
        
        List<RelacionEeff> eeffTempList = new ArrayList<RelacionEeff>();
        RelacionEeff relEeff = new RelacionEeff();
        PeriodoEmpresa periodoEmpresa = new PeriodoEmpresa();
        	periodoEmpresa.setPeriodo(periodo);
        relEeff.copyEstadoFinanciero(eeff, relCelda, periodoEmpresa);
        eeffTempList.add(relEeff);
        relEeffList = eeffTempList;
        
        if(relacionMap.containsKey(relCelda)){
            relacionMap.get(relCelda)[0] = eeffTempList;
        }else{
            aplicarRelacion(relacionMap, relCelda, eeffTempList, new ArrayList<RelacionDetalleEeff>());
        }
        
        relCelda.setRelacionEeffList(eeffTempList);
    }
    
    public void cargarCuentaListener(ActionEvent event) {
        
final DetalleEeff detalleEeff = (DetalleEeff)event.getComponent().getAttributes().get("detalleEeff");
        
        if(detalleEeff==null || !validaRelCelda())
            return;

        if(EeffUtil.esCuentaRepetida(relEeffDetList, detalleEeff)){
            addWarnMessage("Número de cuenta ya esta ingresado para la celda seleccionada " + Util.formatCellKey(relCelda));
            return;
        }
        
        RelacionDetalleEeff relEeffDet = new RelacionDetalleEeff();
        PeriodoEmpresa periodoEmpresa = new PeriodoEmpresa();
        	periodoEmpresa.setPeriodo(periodo);
        relEeffDet.copyDetalleEeff(detalleEeff, relCelda, periodoEmpresa);
    
        if(relacionMap.containsKey(relCelda)){
            relacionMap.get(relCelda)[1].add(relEeffDet);
        }else{
            List<RelacionDetalleEeff> relEeffDetTempList = new ArrayList<RelacionDetalleEeff>();
            relEeffDetTempList.add(relEeffDet);
            aplicarRelacion(relacionMap, relCelda, new ArrayList<RelacionEeff>(), relEeffDetTempList);
        }
        
        getRelEeffDetList().add(relEeffDet);
        
    }
    
    public void guardarRelacionListener(ActionEvent event){
        
    	if(!isValidPeriodoGrilla())
            return;
        
        try{        
            getFacadeService().getEstadoFinancieroService().persistRelaccionEeff(relacionMap, periodo.getIdPeriodo());
            relacionMap.clear();
            Grilla grilla = this.getFacadeService().getGrillaService().findGrillaById(grillaVO.getGrilla().getIdGrilla());
            grillaVO = this.getFacadeService().getEstructuraService().getGrillaVO(grilla, Boolean.FALSE);
            grillaVO.setGrilla(grilla);
            addInfoMessage("Se ha almacenado correctamente la informacón");
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            addErrorMessage("Se ha producido un error al almacenar la relación");
        }
        
    }
    
    public void eliminarRelacionListener(ActionEvent event){
        
        if(!isValidPeriodoGrilla())
            return;
        
        try{
            final Celda celda = (Celda)event.getComponent().getAttributes().get("celda");
            getFacadeService().getEstadoFinancieroService().deleteRelacionAllEeffByCelda(celda);
            if(relacionMap.containsKey(celda)){
                relacionMap.remove(celda);
            }
            celda.setRelacionEeffList(null);
            celda.setRelacionDetalleEeffList(null);
            relEeffList = null;
            relEeffDetList = null;
            
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            addErrorMessage("Se ha producido un error al eliminar la relación");
        }
        
    }
    
    
    public void eliminarTodasRelacionListener(ActionEvent event){
        
        if(!isValidPeriodoGrilla())
            return;
        
        try{
        	super.getFacadeService().getEstadoFinancieroService().deleteAllRelacionByGrillaPeriodo(periodo.getIdPeriodo(), grillaVO.getGrilla().getIdGrilla());
            relacionMap.clear();
            Grilla grilla = super.getFacadeService().getGrillaService().findGrillaById(grillaVO.getGrilla().getIdGrilla());
            grillaVO = super.getFacadeService().getEstructuraService().getGrillaVO(grilla, Boolean.FALSE);
            grillaVO.setGrilla(grilla);
            relEeffList = null;
            relEeffDetList = null;
            
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            addErrorMessage("Se ha producido un error al almacenar la información");
        }
        
    }
    
    public void onChangeFecu(ValueChangeEvent event){
        if(event.getNewValue()!=null){
            relFecuSeleccionada = (RelacionEeff)event.getNewValue();
            FacesContext context = getFacesContext();
            //ExtendedRenderKitService extRenderKitSrvc = Service.getRenderKitService(context, ExtendedRenderKitService.class);
            //extRenderKitSrvc.addScript(context,"AdfPage.PAGE.findComponent('" + POPUP_FECU + "').show();");
        }
    }
    
    public void onChangeCuenta(ValueChangeEvent event){
        if(event.getNewValue()!=null){
            relCuentaSeleccionada = (RelacionDetalleEeff)event.getNewValue();
            FacesContext context = getFacesContext();
            //ExtendedRenderKitService extRenderKitSrvc = Service.getRenderKitService(context, ExtendedRenderKitService.class);
            //extRenderKitSrvc.addScript(context,"AdfPage.PAGE.findComponent('" + POPUP_CUENTA + "').show();");
        }
    }


    public void abrirPaginaRelacion(ActionEvent event){
        
        //final Celda celda = (Celda)event.getComponent().getAttributes().get("celda");
        
        StringBuilder script = new StringBuilder();   
        script = script.append("window.open('relacion.jsf','_blank','menubar=no,toolbar=no,location=no,personalbar=no,directories=no,width=400,height=300,fullscreen=no,titlebar=yes,hotkeys=yes,status=yes,scrollbars=yes,resizable=yes');");   
        //Service.getRenderKitService(getFacesContext(),ExtendedRenderKitService.class).addScript(getFacesContext(),script.toString()); 
    
    }
    
    private String getValorStr(String str){
        if(str==null)
            return "";
        else
            return str;
    }
    
    public boolean validaRelCelda(){
        if(relCelda==null){
            addWarnMessage("Debe seleccionar celda primero");
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

    public List<DetalleEeff> getDetalleEeffs() {
        return detalleEeffs;
    }

	public SelectOneMenu getTipoCuadroSelect() {
		return tipoCuadroSelect;
	}

	public void setTipoCuadroSelect(SelectOneMenu tipoCuadroSelect) {
		this.tipoCuadroSelect = tipoCuadroSelect;
	}

	public SelectOneMenu getCuentaChoice() {
		return cuentaChoice;
	}

	public void setCuentaChoice(SelectOneMenu cuentaChoice) {
		this.cuentaChoice = cuentaChoice;
	}

	public InputText getRelCeldaText() {
		return relCeldaText;
	}

	public void setRelCeldaText(InputText relCeldaText) {
		this.relCeldaText = relCeldaText;
	}

	public AutoComplete getBusquedaInputText() {
		return busquedaInputText;
	}

	public void setBusquedaInputText(AutoComplete busquedaInputText) {
		this.busquedaInputText = busquedaInputText;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public boolean isRenderTreeTabla() {
		return renderTreeTabla;
	}

	public void setRenderTreeTabla(boolean renderTreeTabla) {
		this.renderTreeTabla = renderTreeTabla;
	}

	public boolean isRenderEstructuraTabla() {
		return renderEstructuraTabla;
	}

	public void setRenderEstructuraTabla(boolean renderEstructuraTabla) {
		this.renderEstructuraTabla = renderEstructuraTabla;
	}

	public List<RelacionEeff> getRelEeffList() {
		
		 if(relEeffList==null){
	            relEeffList = new ArrayList<RelacionEeff>();
	        }
		
		return relEeffList;
	}

	public void setRelEeffList(List<RelacionEeff> relEeffList) {
		this.relEeffList = relEeffList;
	}

	public List<RelacionDetalleEeff> getRelEeffDetList() {
		
		 if(relEeffDetList==null){
	            relEeffDetList = new ArrayList<RelacionDetalleEeff>();
	        }
		return relEeffDetList;
	}

	public void setRelEeffDetList(List<RelacionDetalleEeff> relEeffDetList) {
		this.relEeffDetList = relEeffDetList;
	}

	public RelacionEeff getRelFecuSeleccionada() {
		return relFecuSeleccionada;
	}

	public void setRelFecuSeleccionada(RelacionEeff relFecuSeleccionada) {
		this.relFecuSeleccionada = relFecuSeleccionada;
	}

	public RelacionDetalleEeff getRelCuentaSeleccionada() {
		return relCuentaSeleccionada;
	}

	public void setRelCuentaSeleccionada(RelacionDetalleEeff relCuentaSeleccionada) {
		this.relCuentaSeleccionada = relCuentaSeleccionada;
	}

	public boolean isRenderTablaCuadro() {
		return renderTablaCuadro;
	}

	public void setRenderTablaCuadro(boolean renderTablaCuadro) {
		this.renderTablaCuadro = renderTablaCuadro;
	}

	public SelectOneMenu getFecuAgregadasChoice() {
		return fecuAgregadasChoice;
	}

	public void setFecuAgregadasChoice(SelectOneMenu fecuAgregadasChoice) {
		this.fecuAgregadasChoice = fecuAgregadasChoice;
	}

	public SelectOneMenu getCuentaAgregadasChoice() {
		return cuentaAgregadasChoice;
	}

	public void setCuentaAgregadasChoice(SelectOneMenu cuentaAgregadasChoice) {
		this.cuentaAgregadasChoice = cuentaAgregadasChoice;
	}
	
	
    private void init(){
        busqueda = null;
        versionVigente = null;
        estructuras = null;
        detalleEeffs = null;
        estadoFinanciero = null;
        detalleEeff = null;
        numeroCuenta = null;
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
    
    public boolean isValidPeriodoGrilla(){
        
        if(grillaVO==null || grillaVO.getGrilla()==null || grillaVO.getGrilla().getIdGrilla()==null)
            addWarnMessage("Debe seleccionar un cuadro");
        else if(periodo==null || periodo.getIdPeriodo() == null)
            addWarnMessage("Período inválido, debe ingresar a la página nuevamente");
        else
            return true;
        
        return false;
    }

	public Catalogo getCatalogo() {
		return catalogo;
	}

	public void setCatalogo(Catalogo catalogo) {
		this.catalogo = catalogo;
	}

	public Celda getRelCelda() {
		return relCelda;
	}

	public void setRelCelda(Celda relCelda) {
		this.relCelda = relCelda;
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
    
    public int getCountMapping(){
        
        if(relacionMap==null)
            return 0;
        
        return relacionMap.size();
    }
	

}
