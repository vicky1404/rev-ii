package cl.mdr.ifrs.modules.mb;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.model.TreeFormula;
import cl.mdr.ifrs.ejb.cross.EeffUtil;
import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.Catalogo;
import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.ifrs.ejb.entity.DetalleEeff;
import cl.mdr.ifrs.ejb.entity.EstadoFinanciero;
import cl.mdr.ifrs.ejb.entity.Estructura;
import cl.mdr.ifrs.ejb.entity.Grilla;
import cl.mdr.ifrs.ejb.entity.Periodo;
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
    private AutoComplete busquedaInputText;
    private transient SelectOneMenu cuentaChoice;
    private GrillaVO grillaVO;
    private String relCelda;
    private String relFecu;
    private String relCuenta;
    /*
     * String[0] --> Fecus
     * String[1] --> Cuentas
     */
    private Map<String, String[]> relacionMap;
    private transient InputText relCeldaText;
    //private transient InputText relFecuText;
    private transient InputTextarea relFecuText;
    //private transient InputText relCuentaText;
    private transient InputTextarea relCuentaText;
    private TreeNode root;
    private boolean renderTreeTabla;
    private boolean renderEstructuraTabla;
    
    
    public ValidadorEeffBackingBean() {
    }
    
    @PostConstruct
    public void cargarPeriodo(){
        try{
            periodo = getFacadeService().getPeriodoService().findMaxPeriodoObj();
            eeffs = getFacadeService().getEstadoFinancieroService().getEeffVigenteByPeriodo(periodo.getIdPeriodo());
            relacionMap = new LinkedHashMap<String, String[]>();
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
    
    public String buscarCatalogo() {
        
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
                addWarnMessage("Busqueda sin resultados");
            }
            
            if(catalogo==null){
                init();
                addWarnMessage("Busqueda sin resultados");
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
        return null;
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
    
    private void init(){
        versionVigente = null;
        estructuras  = null;
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
        
    	String idColumna = super.getExternalContext().getRequestParameterMap().get("idColumna");
    	String idGrilla = super.getExternalContext().getRequestParameterMap().get("idGrilla");
    	String idFila = super.getExternalContext().getRequestParameterMap().get("idFila");
    	
    	Celda celda = new Celda();
    		celda.setIdColumna(Long.parseLong(idColumna));
    		celda.setIdGrilla(Long.parseLong(idGrilla));
    		celda.setIdFila(Long.parseLong(idFila));
    	
			try {
				
				celda = getFacadeService().getCeldaService().findCeldaById(celda);
				
			} catch (Exception e) {
				logger.error(e);
			}
    	
        
        if(celda==null)
            return;
        
        relCelda = Util.formatCellKey(celda);
        relFecu = EeffUtil.formatKeyFecu(celda.getRelacionEeffList());
        relCuenta = EeffUtil.formatKeyCuenta(celda.getRelacionDetalleEeffList());
        relFecuText.setRows(Util.countToken(relFecu,";"));
        relCuentaText.setRows(Util.countToken(relCuenta,";"));
        
        if(relFecu!=null || relCuenta!=null)
            aplicarRelacion(relacionMap, relCelda, relFecu, relCuenta);
        
        
        
    }
    
    public void cargarFecuListener(ActionEvent event) {
            
            final EstadoFinanciero eeff = (EstadoFinanciero) event.getComponent().getAttributes().get("eeff");
            
            if(eeff==null)
                return;
            
            relFecu = "[" + EeffUtil.formatFecu(eeff.getIdFecu()) + "];";
            
            aplicarRelacion(relacionMap, relCelda, relFecu, relCuenta);
            
            //addPartialText();
    }
    
    public void cargarCuentaListener(ActionEvent event) {
        
        final DetalleEeff detalleEeff = (DetalleEeff)event.getComponent().getAttributes().get("detalleEeff");
        
        if(detalleEeff==null)
            return;
        
        if(validaRelCelda()){
            
            if(EeffUtil.esCuentaRepetida(relCuenta, detalleEeff.getIdCuenta().toString())){
                addWarnMessage("Número de cuenta ya esta ingresado para la celda seleccionada " +  relCelda);
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
        //addPartialText();
    }
    
    public void guardarRelacionListener(ActionEvent event){
        
        try{        
            getFacadeService().getEstadoFinancieroService().persistRelaccionEeff(relacionMap, periodo.getIdPeriodo(),grillaVO.getGrilla());
            relacionMap.clear();
            Grilla grilla = this.getFacadeService().getGrillaService().findGrillaById(grillaVO.getGrilla().getIdGrilla());
            grillaVO = this.getFacadeService().getEstructuraService().getGrillaVO(grilla, Boolean.FALSE);
            grillaVO.setGrilla(grilla);
            addInfoMessage("Se ha almacenado correctamente la informacón");
        }catch(Exception e){
            e.printStackTrace();
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
    
    public void addPartialText(){
        //setADFPartialTarget(relCeldaText);
        //setADFPartialTarget(relFecuText);
        //setADFPartialTarget(relCuentaText);
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

	

	public Catalogo getCatalogo() {
		return catalogo;
	}

	public void setCatalogo(Catalogo catalogo) {
		this.catalogo = catalogo;
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

	public InputTextarea getRelFecuText() {
		return relFecuText;
	}

	public void setRelFecuText(InputTextarea relFecuText) {
		this.relFecuText = relFecuText;
	}

	public InputTextarea getRelCuentaText() {
		return relCuentaText;
	}

	public void setRelCuentaText(InputTextarea relCuentaText) {
		this.relCuentaText = relCuentaText;
	}

}
