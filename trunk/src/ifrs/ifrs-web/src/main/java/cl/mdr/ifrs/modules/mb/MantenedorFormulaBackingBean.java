package cl.mdr.ifrs.modules.mb;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static ch.lambdaj.Lambda.maxFrom;
import static ch.lambdaj.Lambda.minFrom;


import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.enterprise.inject.Default;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.log4j.Logger;
import org.primefaces.component.commandlink.CommandLink;
import org.primefaces.component.fieldset.Fieldset;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.model.TreeFormula;
import cl.mdr.ifrs.cross.util.PropertyManager;
import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.Catalogo;
import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.ifrs.ejb.entity.Columna;
import cl.mdr.ifrs.ejb.entity.Estructura;
import cl.mdr.ifrs.ejb.entity.Grilla;
import cl.mdr.ifrs.ejb.entity.Periodo;
import cl.mdr.ifrs.ejb.entity.TipoCuadro;
import cl.mdr.ifrs.ejb.entity.Version;
import cl.mdr.ifrs.ejb.entity.pk.ColumnaPK;
import cl.mdr.ifrs.vo.GrillaVO;




/**
 * @author Manuel Gutierrez C.
 * @since 03/07/2012
 * Maneja la pagina mantenedora de formulas * 
 */
@ManagedBean(name ="formula")
@ViewScoped
public class MantenedorFormulaBackingBean extends AbstractBackingBean implements Serializable{
	
	
	Logger logger = Logger.getLogger(MantenedorFormulaBackingBean.class);
	
	private Long idCatalogo;
	private Long idTipoCuadro;
	private Long idTipoFormula;
	private List<Catalogo> catalogoList;
	private String mesPeriodo;
	private String anioPeriodo;
	private boolean renderTreeNode;
	private Map<String, Celda> celdaMap = new LinkedHashMap<String, Celda>();
	private Map<Celda, String> formulaMap = new LinkedHashMap<Celda, String>();	
	private Celda celdaTarget;
	private String formula = "";
	private Map<Celda, Boolean> camposFormulaByCeldaTarget = new LinkedHashMap<Celda, Boolean>();
	private Map<Celda, Boolean> camposFormulaByCeldaDisplay = new LinkedHashMap<Celda, Boolean>(); 
	private transient InputText formulaTargetOutput;
    private transient InputText barraFormulaOutput;
    private String barraFormula;
    private int countFormulasSinGrabar = 0;
    private Map<Celda, Map<Celda, Boolean>> camposFormula = new LinkedHashMap<Celda, Map<Celda, Boolean>>();
    private boolean renderedCatalogoTree;
	
	private TreeNode root;
	
	private Grilla grilla;
	private GrillaVO grillaVO = new GrillaVO();
	
	private static final String CORCHETE_IZQUIERDO = "[";
    private static final String CORCHETE_DERECHO = "]";
    private static final String SIGNO_SUMA = "+";
    private static final String SIGNO_RESTA = "-";
    private static final String COMA = ",";
    private static final String PUNTO_COMA = ";";
    private static final String DOS_PUNTOS = ":";
    private SelectOneMenu tipoFormulaCombo;

    
    private Fieldset panelGroupLayoutTablaFormula;
    private Fieldset panelGroupLayoutBarraFormula;
    private InputText contadorFormulasUnsavedOutput;
	/**
	 * 
	 */
	private static final long serialVersionUID = -4726692791898733737L;
	
	
    public void changeTipoCuadro() {
    	
    	final TipoCuadro tipoCuadro = new TipoCuadro(getIdTipoCuadro());      
        final List<Catalogo> catalogoList = select(super.getComponenteBackingBean().getCatalogoList() ,having(on(Catalogo.class).getTipoCuadro().getIdTipoCuadro(), equalTo(tipoCuadro.getIdTipoCuadro())));                
        this.setCatalogoList(catalogoList);
    }
    
    
    /**
     * busca un listado de estructuras para el cuadro seleccionado en los criterios de busqueda.
     * @return
     */
    public void buscar(){ 
    	 this.setRenderedCatalogoTree(Boolean.TRUE);
    	Long periodoLong = Long.parseLong( getAnioPeriodo() ) * 100 + Long.parseLong( getMesPeriodo() );
        Periodo periodo = getFacadeService().getPeriodoService().findPeriodoByPeriodo(periodoLong);
        try {
			setTreeNode( getFacadeService().getVersionService().findVersionByFiltro(null, new TipoCuadro(getIdTipoCuadro()) , periodo, null, null, new Catalogo(getIdCatalogo())) );
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}  
    }
    
    
    public void setTreeNode(List<Version> lista){
    	
    	root = new DefaultTreeNode("root", null);
    	
    	for (Version version : lista){
    		
    		TreeNode nodoVersion = new DefaultTreeNode( new TreeFormula(version.getCatalogo().getTitulo() , version.getVersion().toString() , version.getCatalogo().getNombre(), version.getEstado().getNombre(), Util.getString( version.getFechaCreacion() ), version.getVigencia().toString(), null , null ,null) , root);
    		
    			for (Estructura estructura : version.getEstructuraList()){
    				
    					if (estructura.getTipoEstructura().getNombre().toUpperCase().equals("GRILLA")){
    						
    						new DefaultTreeNode( new TreeFormula(null, null, null, null, null, null, "function_16x16.png", version.getCatalogo() , estructura) , nodoVersion);
    					} 
    			}
    		
    	}
    	
    	
    	
    }
    
    
    
    /**
     * ActionListener que ejecuuta la busqueda de una grilla para editar formulas
     * @param event
     */
    public void buscarEstructuraGrilla(ActionEvent event){
    	
        final Estructura estructura = (Estructura)event.getComponent().getAttributes().get("estructura");
        final Catalogo catalogo = (Catalogo)event.getComponent().getAttributes().get("catalogo");
        
        this.getFormulaMap().clear();
        
        try {
        	
			this.setGrilla(this.getFacadeService().getGrillaService().findGrillaById(estructura.getIdEstructura()));
			this.setGrillaVO(this.getFacadeService().getEstructuraService().getGrillaVO(this.getGrilla(), Boolean.FALSE));
			buildCeldaMap();			
		    getTipoFormulaCombo().setValue(estructura.getGrilla().getTipoFormula());
		    setIdTipoFormula(estructura.getGrilla().getTipoFormula());
		    
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
        
        
        /*
        this.setBarraFormula(null);
        this.setCeldaTarget(null);
        super.getFiltro().setTipoFormula(null);
        this.getCamposFormulaByCeldaTarget().clear();
        this.getFormulaMap().clear();
        this.setCountFormulasSinGrabar(this.getFormulaMap().size());
        this.desMarcarCeldasSelecionadasByTarget();
        this.updateTipoFormulaCombo(this.getTipoFormulaCombo(), super.getFiltro().getTipoFormula());  
        try {
            this.setCatalogo(catalogo);
            this.setEstructura(estructura);
            this.setGrilla(this.getFacade().getGrillaService().findGrillaById(estructura.getIdEstructura()));
            
            super.getFiltro().setTipoFormula(this.getGrilla().getTipoFormula());
            this.buildCeldaMap();
            this.setRenderGrilla(Boolean.TRUE);
            this.updateTipoFormulaCombo(this.getTipoFormulaCombo(), super.getFiltro().getTipoFormula());              
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            super.agregarErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_nota_get_catalogo_error"));
        } 
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getBarraFormulaOutput());
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPanelGroupLayoutBarraFormula()); 
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPanelGroupLayoutTablaFormula());
        */
    }
    
    /**
     * Actualiza el valor del combo tipo de formula en la vista
     * @param combo
     * @param tipoFormula
     */
    private void updateTipoFormulaCombo(SelectOneMenu combo, Long tipoFormula){
        //combo.setValue(tipoFormula);
        //AdfFacesContext.getCurrentInstance().addPartialTarget(combo);
        //RequestContext.getCurrentInstance().update("somTipoFormula");
    }
    
    /**
     * Metodo listener del boton agregar o quitar celda a una formula estatica.
     * @param event
     */
    public void selectCeldaEstatica(){
    	
    	String idColumna = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idColumna");
    	String idGrilla = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idGrilla");
    	String idFila = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idFila");
    	String selected = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("selected");
    	
    	Celda celda = new Celda();
    		celda.setIdColumna(Long.parseLong(idColumna));
    		celda.setIdGrilla(Long.parseLong(idGrilla));
    		celda.setIdFila(Long.parseLong(idFila));
    	
    	
			try {
				
				celda = getFacadeService().getCeldaService().findCeldaById(celda);
				
			} catch (Exception e) {
				
				e.printStackTrace();
			}

		
        
        final boolean celdaSelected = Boolean.parseBoolean(selected );
        
        final String parOrdenado = CORCHETE_IZQUIERDO.concat(""+celda.getIdColumna()).concat(COMA).concat(""+celda.getIdFila()).concat(CORCHETE_DERECHO).concat(PUNTO_COMA); 
        final String formulaSaved = this.getFormulaMap().get(this.getCeldaTarget());
        this.setFormula(formulaSaved == null ? this.getCeldaTarget().getFormula() : formulaSaved);                         
        if(EqualsBuilder.reflectionEquals(this.getCeldaTarget(), celda, true)){
            super.addWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_celda_destino_existe_en_formula"), parOrdenado));             
            return;
        }
        if(this.getCeldaTarget() == null && celdaSelected){
            super.addWarnMessage(PropertyManager.getInstance().getMessage("general_mensaje_formula_sin_celda_destino"));                        
            RequestContext.getCurrentInstance().update(this.getPanelGroupLayoutTablaFormula().getId());
            return;
        }
        if(this.getFormula() == null){
            this.setFormula("");
        }
        if(celdaSelected){
            if(this.getFormula().contains(parOrdenado)){
                super.addWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_celda_existe_en_formula"), parOrdenado));
                return;
            }
            this.setFormula(this.getFormula().concat(SIGNO_SUMA));
            this.setFormula(this.getFormula().concat(parOrdenado));            
            this.updateCamposFormulaByCeldaTargetMap(celda, Boolean.TRUE);            
            celda.setSelectedByFormula(Boolean.TRUE);            
        }else{
            if(this.getFormula().contains(SIGNO_SUMA.concat(parOrdenado))){
                this.setFormula(this.getFormula().replace(SIGNO_SUMA.concat(parOrdenado), ""));    
            }
            if(this.getFormula().contains(SIGNO_RESTA.concat(parOrdenado))){
                this.setFormula(this.getFormula().replace(SIGNO_RESTA.concat(parOrdenado), ""));    
            }
            if(this.getFormula().contains(parOrdenado)){
                this.setFormula(this.getFormula().replace(parOrdenado, ""));    
            }
            this.updateCamposFormulaByCeldaTargetMap(celda, Boolean.FALSE);            
            celda.setSelectedByFormula(Boolean.FALSE);            
        } 
        this.setBarraFormula(this.getFormula());
        this.updateFormulaMap(this.getCeldaTarget(), this.getFormula()); 
        this.setCountFormulasSinGrabar(this.getFormulaMap().size()); 
        
        this.updateCamposFormulaMap(this.getCeldaTarget(), this.getCamposFormulaByCeldaTarget());                
        
       
        //RequestContext.getCurrentInstance().update(this.getContadorFormulasUnsavedOutput().getId());
        RequestContext.getCurrentInstance().update(this.getPanelGroupLayoutBarraFormula().getId());        
        RequestContext.getCurrentInstance().update(this.getPanelGroupLayoutTablaFormula().getId());
    }
	

	public Long getIdCatalogo() {
		return idCatalogo;
	}

	public void setIdCatalogo(Long idCatalogo) {
		this.idCatalogo = idCatalogo;
	}


	public List<Catalogo> getCatalogoList() {
		return catalogoList;
	}


	public void setCatalogoList(List<Catalogo> catalogoList) {
		this.catalogoList = catalogoList;
	}


	public Long getIdTipoCuadro() {
		return idTipoCuadro;
	}


	public void setIdTipoCuadro(Long idTipoCuadro) {
		this.idTipoCuadro = idTipoCuadro;
	}


	public String getMesPeriodo() {
		return mesPeriodo;
	}


	public void setMesPeriodo(String mesPeriodo) {
		this.mesPeriodo = mesPeriodo;
	}


	public String getAnioPeriodo() {
		return anioPeriodo;
	}


	public void setAnioPeriodo(String anioPeriodo) {
		this.anioPeriodo = anioPeriodo;
	}


	public TreeNode getRoot() {
		return root;
	}


	public void setRoot(TreeNode root) {
		this.root = root;
	}


	public boolean isRenderTreeNode() {
		return renderTreeNode;
	}


	public void setRenderTreeNode(boolean renderTreeNode) {
		this.renderTreeNode = renderTreeNode;
	}


	public Long getIdTipoFormula() {
		return idTipoFormula;
	}


	public void setIdTipoFormula(Long idTipoFormula) {
		this.idTipoFormula = idTipoFormula;
	}


	public Grilla getGrilla() {
		return grilla;
	}


	public void setGrilla(Grilla grilla) {
		this.grilla = grilla;
	}


	public GrillaVO getGrillaVO() {
		return grillaVO;
	}


	public void setGrillaVO(GrillaVO grillaVO) {
		this.grillaVO = grillaVO;
	}


	/**
     * Construye un Map con las celdas presentes en la grilla
     * su key esta representado por el String [columna, fila]
     */
    private void buildCeldaMap(){
        this.getCeldaMap().clear();
        for (Map<Long, Celda> row : this.getGrillaVO().getRows()) {
            for (Map.Entry<Long, Celda> entry : row.entrySet()) {                                
                this.getCeldaMap().put(this.formatCellKey(entry.getValue()), entry.getValue());
            }
        }    
    }
    
    /**
     * devuelve un string formateado con el key utilizado para representar una celda dentro de la formula estatica.
     * ejemplo: [2,1]
     * @param cell
     * @return
     */
    private String formatCellKey(final Celda cell){
        return  CORCHETE_IZQUIERDO
                .concat(cell.getIdColumna().toString())
                .concat(COMA)
                .concat(cell.getIdFila().toString())
                .concat(CORCHETE_DERECHO);
    }


	public Map<String, Celda> getCeldaMap() {
		return celdaMap;
	}


	public void setCeldaMap(Map<String, Celda> celdaMap) {
		this.celdaMap = celdaMap;
	}


	public SelectOneMenu getTipoFormulaCombo() {
		return tipoFormulaCombo;
	}


	public void setTipoFormulaCombo(SelectOneMenu tipoFormulaCombo) {
		this.tipoFormulaCombo = tipoFormulaCombo;
	}


	public Map<Celda, String> getFormulaMap() {
		return formulaMap;
	}


	public void setFormulaMap(Map<Celda, String> formulaMap) {
		this.formulaMap = formulaMap;
	}


	public Celda getCeldaTarget() {
		return celdaTarget;
	}


	public void setCeldaTarget(Celda celdaTarget) {
		this.celdaTarget = celdaTarget;
	}


	public String getFormula() {
		return formula;
	}


	public void setFormula(String formula) {
		this.formula = formula;
	}


	public Fieldset getPanelGroupLayoutTablaFormula() {
		return panelGroupLayoutTablaFormula;
	}


	public void setPanelGroupLayoutTablaFormula(
			Fieldset panelGroupLayoutTablaFormula) {
		this.panelGroupLayoutTablaFormula = panelGroupLayoutTablaFormula;
	}

	public void updateCamposFormulaByCeldaTargetMap(Celda key, Boolean value) {
        if(!this.getCamposFormulaByCeldaTarget().containsKey(key)){
            this.getCamposFormulaByCeldaTarget().put(key, value);
        }else{         
            for(Map.Entry<Celda, Boolean> entry : this.getCamposFormulaByCeldaTarget().entrySet()) {
                if(entry.getKey().equals(key)){
                    entry.setValue(value);
                    break;
                }
            }
        }
    }


	public Map<Celda, Boolean> getCamposFormulaByCeldaTarget() {
		return camposFormulaByCeldaTarget;
	}


	public void setCamposFormulaByCeldaTarget(
			Map<Celda, Boolean> camposFormulaByCeldaTarget) {
		this.camposFormulaByCeldaTarget = camposFormulaByCeldaTarget;
	}


	public Map<Celda, Boolean> getCamposFormulaByCeldaDisplay() {
		return camposFormulaByCeldaDisplay;
	}


	public void setCamposFormulaByCeldaDisplay(
			Map<Celda, Boolean> camposFormulaByCeldaDisplay) {
		this.camposFormulaByCeldaDisplay = camposFormulaByCeldaDisplay;
	}


	public InputText getFormulaTargetOutput() {
		return formulaTargetOutput;
	}


	public void setFormulaTargetOutput(InputText formulaTargetOutput) {
		this.formulaTargetOutput = formulaTargetOutput;
	}


	public InputText getBarraFormulaOutput() {
		return barraFormulaOutput;
	}


	public void setBarraFormulaOutput(InputText barraFormulaOutput) {
		this.barraFormulaOutput = barraFormulaOutput;
	}


	public String getBarraFormula() {
		return barraFormula;
	}


	public void setBarraFormula(String barraFormula) {
		this.barraFormula = barraFormula;
	}
	
	/**
     * Actializa la coleccion de tipo Map que contiene las formulas 
     * estaticas que se encuentran en modo de edicion.
     * @param key
     * @param value
     */
    public void updateFormulaMap(Celda key, String value) {
        if(!this.getFormulaMap().containsKey(key)){
            this.getFormulaMap().put(key, value);
        }else{         
            for(Map.Entry<Celda, String> entry : this.getFormulaMap().entrySet()) {
                if(entry.getKey().equals(key)){
                    entry.setValue(value);
                    break;
                }
            }
        }
    }
    
    public void updateCamposFormulaMap(Celda key, Map<Celda, Boolean> value) {
        if(!this.getCamposFormula().containsKey(key)){
            this.getCamposFormula().put(key, value);
        }else{         
            for(Map.Entry<Celda, Map<Celda, Boolean>> entry : this.getCamposFormula().entrySet()) {
                if(entry.getKey().equals(key)){
                    entry.setValue(value);
                    break;
                }
            }
        }
    }


	public int getCountFormulasSinGrabar() {
		return countFormulasSinGrabar;
	}


	public void setCountFormulasSinGrabar(int countFormulasSinGrabar) {
		this.countFormulasSinGrabar = countFormulasSinGrabar;
	}


	public Map<Celda, Map<Celda, Boolean>> getCamposFormula() {
		return camposFormula;
	}


	public void setCamposFormula(Map<Celda, Map<Celda, Boolean>> camposFormula) {
		this.camposFormula = camposFormula;
	}


	public Fieldset getPanelGroupLayoutBarraFormula() {
		return panelGroupLayoutBarraFormula;
	}


	public void setPanelGroupLayoutBarraFormula(
			Fieldset panelGroupLayoutBarraFormula) {
		this.panelGroupLayoutBarraFormula = panelGroupLayoutBarraFormula;
	}


	public InputText getContadorFormulasUnsavedOutput() {
		return contadorFormulasUnsavedOutput;
	}


	public void setContadorFormulasUnsavedOutput(
			InputText contadorFormulasUnsavedOutput) {
		this.contadorFormulasUnsavedOutput = contadorFormulasUnsavedOutput;
	}
	
	 
	
	
	/**
     * metodo listener que responde al boton de seleccionar la celda de destino para la formula estatica.
     * @param event
     */
    public void fijarCeldaEstaticaTarget(){  
    	
    	
    	String idColumna = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idColumna");
    	String idGrilla = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idGrilla");
    	String idFila = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idFila");
    	
    	Celda celdaTarget = new Celda();
    		celdaTarget.setIdColumna(Long.parseLong(idColumna));
    		celdaTarget.setIdGrilla(Long.parseLong(idGrilla));
    		celdaTarget.setIdFila(Long.parseLong(idFila));
    	
    	
			try {
				
				celdaTarget = getFacadeService().getCeldaService().findCeldaById(celdaTarget);
				
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
			
        this.setCeldaTarget(celdaTarget);
        final String formulaSaved = this.getFormulaMap().get(this.getCeldaTarget());
        this.setFormula(formulaSaved == null ? this.getCeldaTarget().getFormula() : formulaSaved);             
        if(this.getFormula() == null){
            this.setFormula("");
        }        
        this.setBarraFormula(null);
        this.setBarraFormula(this.getFormula());         
        this.desMarcarCeldasSelecionadasByTarget();        
        this.marcarCeldasSelecionadasByCadenaFormulaEstatica();
        
        RequestContext.getCurrentInstance().update(this.getPanelGroupLayoutBarraFormula().getId());
        RequestContext.getCurrentInstance().update(this.getPanelGroupLayoutTablaFormula().getId());
        this.getCamposFormulaByCeldaTarget().clear();
    }
    
    
   
    
    /**
     * desmarca todas las celdas seleccionadas para una celda target
     */
    private void desMarcarCeldasSelecionadasByTarget() {
        if (this.getGrillaVO().getRows() != null) {
            logger.info("desmarcando celdas");
            for (Map<Long, Celda> row : this.getGrillaVO().getRows()) {
                for (Map.Entry<Long, Celda> entry : row.entrySet()) {
                    entry.getValue().setSelectedByFormula(Boolean.FALSE);
                }
            }
        }
    }
    
    
    /**
     * Marca como seleccionado en la grilla de formula estatica 
     * las celdas seleccionadas en la formula de destino
     */
    private void marcarCeldasSelecionadasByCadenaFormulaEstatica(){
        logger.info("marcando celdas para formula");
        if(this.getFormulaMap().get(this.getCeldaTarget()) == null && this.getCeldaTarget().getFormula() == null){
            logger.info("no existe formula para la celda de resultado");
            return;
        }
        final String formulaSaved = this.getFormulaMap().get(this.getCeldaTarget());        
        StringTokenizer cellKeys = new StringTokenizer(formulaSaved == null ? this.getCeldaTarget().getFormula() : formulaSaved, ";");   
        while (cellKeys.hasMoreTokens()) {
            String cellKey = cellKeys.nextToken()
                             .replace(SIGNO_SUMA, "")
                             .replace(SIGNO_RESTA, "");
            logger.info(cellKey);   
            if (this.getCeldaMap().containsKey(cellKey)) {
                Celda celda = this.getCeldaMap().get(cellKey);
                if(EqualsBuilder.reflectionEquals(this.getCeldaTarget(), celda, true)){
                    super.addWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_celda_destino_existe_en_formula"), cellKey));             
                    return;
                }
                celda.setSelectedByFormula(Boolean.TRUE);                            
            }else{
                super.addErrorMessage("La celda "+cellKey+" no existe en el cuadro");
                if(this.getFormula().contains(SIGNO_SUMA.concat(cellKey))){
                    this.setFormula(this.getFormula().replace(SIGNO_SUMA.concat(cellKey.concat(PUNTO_COMA)), ""));    
                }
                if(this.getFormula().contains(SIGNO_RESTA.concat(cellKey))){
                    this.setFormula(this.getFormula().replace(SIGNO_RESTA.concat(cellKey.concat(PUNTO_COMA)), ""));    
                }
                if(this.getFormula().contains(cellKey)){
                    this.setFormula(this.getFormula().replace(cellKey.concat(PUNTO_COMA), ""));    
                }
                this.setBarraFormula(this.getFormula());                            
            }
        }             
    }


	public boolean isRenderedCatalogoTree() {
		return renderedCatalogoTree;
	}


	public void setRenderedCatalogoTree(boolean renderedCatalogoTree) {
		this.renderedCatalogoTree = renderedCatalogoTree;
	}
	
	 /**
     * ActionListener que guarda las configuraciones efectuadas en las formulas estaticas
     * para la grilla.
     * @param event
     */
    public void guardarFormulaEstatica(ActionEvent event){
        this.validaReferenciaCiclicaFormula();
        if(super.getFacesContext().getMessages().hasNext()){
            return;
        } 
        try {
            List<Celda> celdaList = new ArrayList<Celda>();
            for(Map.Entry<Celda, String> entry : this.getFormulaMap().entrySet()) {
                Celda celda = entry.getKey();
                celda.setFormula(entry.getValue());
                celdaList.add(celda);
            }  
            this.getGrilla().setTipoFormula(Grilla.TIPO_GRILLA_ESTATICA);
            this.getFacadeService().getCeldaService().persistFormulaEstaticaList(this.getGrilla(), celdaList);            
            super.addInfoMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_exito_guardar_formula"), this.getFormulaMap().size()));
            this.getFormulaMap().clear();
            this.setCountFormulasSinGrabar(this.getFormulaMap().size());
            
            RequestContext.getCurrentInstance().update(this.getContadorFormulasUnsavedOutput().getId());
            RequestContext.getCurrentInstance().update(this.getPanelGroupLayoutTablaFormula().getId());
            
            
        } catch (Exception e) {
            super.addErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_error_guardar_formula"));
            logger.error(PropertyManager.getInstance().getMessage("general_mensaje_error_guardar_formula"), e);            
        }
    }
    
    
    private void validaReferenciaCiclicaFormula(){
        for(Map.Entry<Celda, String> entry : this.getFormulaMap().entrySet()) {
            final String cellKey = this.formatCellKey(entry.getKey());
            if(entry.getValue().contains(cellKey)){
                super.addWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_celda_destino_existe_en_formula"), cellKey));             
                break;                
            }
        }
    }
	
    
    /**
     * ActionListener que guarda las configuraciones efectuadas en las formulas Dinamicas
     * para la grilla.
     * @param event
     */
    public void guardarFormulaDinamica(ActionEvent event){
        this.validaReferenciaCiclicaFormula();
        if(super.getFacesContext().getMessages().hasNext()){
            return;
        }    
        try {
            Map<Celda, List<Celda>> formulaDinamicaMap = new HashMap<Celda, List<Celda>>();
            
            for(Map.Entry<Celda, String> entry : this.getFormulaMap().entrySet()) {
                Celda celda = entry.getKey();
                celda.setFormula(entry.getValue());
            
                if(celda.getFormula().contains(SIGNO_SUMA) || celda.getFormula().contains(PUNTO_COMA)){
                    formulaDinamicaMap.put(celda, this.getCeldaTotalListByTarget(celda));
                }else{
                    formulaDinamicaMap.put(celda, this.getCeldaListByTarget(celda));
                }
                celdaTarget.setFormula(null);
            }
              
            logger.info(formulaDinamicaMap);            
            this.getGrilla().setTipoFormula(Grilla.TIPO_GRILLA_DINAMICA);            
            this.getFacadeService().getCeldaService().persistFormulaDinamicaMap(this.getGrilla(), formulaDinamicaMap);
            super.addInfoMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_exito_guardar_formula"), this.getFormulaMap().size()));
            this.getFormulaMap().clear();
            this.setCountFormulasSinGrabar(this.getFormulaMap().size()); 
            
            RequestContext.getCurrentInstance().update(this.getContadorFormulasUnsavedOutput().getId());
            RequestContext.getCurrentInstance().update(this.getPanelGroupLayoutTablaFormula().getId());
            
            
        } catch (Exception e) {
            super.addErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_error_guardar_formula"));
            logger.error(PropertyManager.getInstance().getMessage("general_mensaje_error_guardar_formula"), e);            
        }
    }
    
    /**
     * genera una lista de celdas totales y subtotales 
     * incluidas en la formula dinamica a partir de la celda parent y del string de formula
     * @param celdaTarget
     * @return
     */
    private List<Celda> getCeldaTotalListByTarget(Celda celdaTarget) {
        List<Celda> celdaListByTarget = new ArrayList<Celda>();
        final String formulaSaved = this.getFormulaMap().get(celdaTarget);
        Long parentVertical = null;
        Long parentHorizontal = null;        
        StringTokenizer cellKeys = new StringTokenizer(formulaSaved == null ? this.getCeldaTarget().getFormula() : formulaSaved, PUNTO_COMA);
        while (cellKeys.hasMoreTokens()) {
            String cellKey = cellKeys.nextToken()
                             .replace(SIGNO_SUMA, "")
                             .replace(SIGNO_RESTA, "");
            if (this.getCeldaMap().containsKey(cellKey)) {
                celdaListByTarget.add(this.getCeldaMap().get(cellKey));
            }
            for(Celda celda : celdaListByTarget){
                if(!celdaTarget.getIdColumna().equals(celda.getIdColumna())){
                    parentHorizontal = this.getMaxParentHorizontal(celdaTarget);
                    break;
                }
                else if (!celdaTarget.getIdFila().equals(celda.getIdFila())){
                    parentVertical = this.getMaxParentVertical(celdaTarget);
                    break;
                }
            }
            for(Celda celda : celdaListByTarget){                
                if(parentVertical != null){
                    celdaTarget.setParentVertical(parentVertical);                
                    celda.setChildVertical(parentVertical);
                }
                else if(parentHorizontal != null){
                    celdaTarget.setParentHorizontal(parentHorizontal);
                    celda.setChildHorizontal(parentHorizontal);
                }
            }
        }
        celdaTarget.setFormula(null);
        return celdaListByTarget;
    }
    
    /**
     * genera una lista de celdas incluidas en la formula dinamica a partir de la celda parent y del string de formula
     * @param celdaTarget
     * @return
     */
    private List<Celda> getCeldaListByTarget(Celda celdaTarget) {
        List<Celda> celdaListByTarget = new ArrayList<Celda>();
        List<Celda> celdaRangoList = new ArrayList<Celda>();
        final String formulaSaved = this.getFormulaMap().get(celdaTarget);
        StringTokenizer cellKeys = new StringTokenizer(formulaSaved == null ? this.getCeldaTarget().getFormula() : formulaSaved, DOS_PUNTOS);
        while (cellKeys.hasMoreTokens()) {
            String cellKey = cellKeys.nextToken().replace(DOS_PUNTOS, "");
            if (this.getCeldaMap().containsKey(cellKey)) {
                celdaRangoList.add(this.getCeldaMap().get(cellKey));
            }
        }

        if(celdaRangoList.size()==1){
            
            if(celdaTarget.getIdColumna().equals(celdaRangoList.get(0).getIdColumna())){
                
                final Long parentVertical = this.getMaxParentVertical(celdaTarget);
                celdaTarget.setParentVertical(parentVertical);
                celdaRangoList.get(0).setChildVertical(parentVertical);
                celdaListByTarget.add(celdaRangoList.get(0));
                
            }else if(celdaTarget.getIdFila().equals(celdaRangoList.get(0).getIdFila())){
                final Long parentHorizontal = this.getMaxParentHorizontal(celdaTarget);
                celdaTarget.setParentHorizontal(parentHorizontal);
                celdaRangoList.get(0).setChildHorizontal(parentHorizontal);
                celdaListByTarget.add(celdaRangoList.get(0));
            }

        }else if (!celdaRangoList.isEmpty() && celdaRangoList.size()>1) {
            List<Celda> celdaList = this.celdaMapToList();
            List<Celda> celdasByColumna = new ArrayList<Celda>();
            List<Celda> celdasByFila = new ArrayList<Celda>();
            if (celdaRangoList.get(0).getIdColumna().equals(celdaRangoList.get(1).getIdColumna())) {
                //seleccion vertical
                final Long parentVertical = this.getMaxParentVertical(celdaTarget);
                celdaTarget.setParentVertical(parentVertical);
                celdasByColumna = select(celdaList, having(on(Celda.class).getIdColumna(), equalTo(celdaRangoList.get(0).getIdColumna()))
                                         .and(having(on(Celda.class).getIdFila(), greaterThanOrEqualTo(celdaRangoList.get(0).getIdFila())))
                                         .and(having(on(Celda.class).getIdFila(), lessThanOrEqualTo(celdaRangoList.get(1).getIdFila()))));
                if (celdasByColumna.isEmpty()) {
                    celdasByColumna = select(celdaList, having(on(Celda.class).getIdColumna(), equalTo(celdaRangoList.get(0).getIdColumna()))
                                             .and(having(on(Celda.class).getIdFila(),lessThanOrEqualTo(celdaRangoList.get(0).getIdFila())))
                                             .and(having(on(Celda.class).getIdFila(), greaterThanOrEqualTo(celdaRangoList.get(1).getIdFila()))));
                }
                for (Celda celda : celdasByColumna) {                    
                    celda.setChildVertical(parentVertical);
                    celdaListByTarget.add(celda);
                }
            } else if (celdaRangoList.get(0).getIdFila().equals(celdaRangoList.get(1).getIdFila())) {
                //seleccion horizontal
                final Long parentHorizontal = this.getMaxParentHorizontal(celdaTarget);
                celdaTarget.setParentHorizontal(parentHorizontal);                
                celdasByFila = select(celdaList ,having(on(Celda.class).getIdFila(), equalTo(celdaRangoList.get(0).getIdFila()))
                                      .and(having(on(Celda.class).getIdColumna(), greaterThanOrEqualTo(celdaRangoList.get(0).getIdColumna())))
                                      .and(having(on(Celda.class).getIdColumna(), lessThanOrEqualTo(celdaRangoList.get(1).getIdColumna()))));
                if(celdasByFila.isEmpty()){
                    celdasByFila = select(celdaList ,having(on(Celda.class).getIdFila(), equalTo(celdaRangoList.get(0).getIdFila()))
                                          .and(having(on(Celda.class).getIdColumna(), lessThanOrEqualTo(celdaRangoList.get(0).getIdColumna())))
                                          .and(having(on(Celda.class).getIdColumna(), greaterThanOrEqualTo(celdaRangoList.get(1).getIdColumna()))));
                }
                for (Celda celda : celdasByFila) {
                    celda.setChildHorizontal(parentHorizontal);
                    celdaListByTarget.add(celda);
                }
            }
        }
        return celdaListByTarget;
    }
    
    /**
     * obtiene el maximo indice de parent horizontal desde el conjunto de celdas.
     * @return
     */
    private Long getMaxParentHorizontal(final Celda celdaTarget){        
        if(celdaTarget.getParentHorizontal() != null){
            return celdaTarget.getParentHorizontal();
        }else{
            final List<Celda> celdas = this.celdaMapToList();
            return (Util.getLong(maxFrom(celdas).getParentHorizontal(), 0L)+1L);
        }
    }

    /**
     * obtiene el maximo indice de parent vertical desde el conjunto de celdas.
     * @return
     */
    private Long getMaxParentVertical(final Celda celdaTarget){ 
        if(celdaTarget.getParentVertical() != null){
            return celdaTarget.getParentVertical();
        }else{
            final List<Celda> celdas = this.celdaMapToList();
            return (Util.getLong(maxFrom(celdas).getParentVertical(), 0L)+1L);
        }
    }
    
    /**
     * Convierte un Map de celda a List
     * @return
     */
    private List<Celda> celdaMapToList(){
        List<Celda> celdaList = new ArrayList<Celda>();
        for (Map<Long, Celda> row : this.getGrillaVO().getRows()) {
            for (Map.Entry<Long, Celda> entry : row.entrySet()) {                                
                celdaList.add(entry.getValue());
            }
        }
        return celdaList;
    }
    
    public String limpiarFormulaDinamica(){
        this.celdaTarget = null;
        this.setBarraFormula(null);
        this.getCamposFormulaByCeldaTarget().clear();
        this.getFormulaMap().clear();
        this.setCountFormulasSinGrabar(0);
        this.desMarcarCeldasSelecionadasByTarget();
        return null;
    }
    
    public String limpiarFormulaEstatica(){
        this.celdaTarget = null;
        this.setBarraFormula(null);
        this.getCamposFormulaByCeldaTarget().clear();
        this.getFormulaMap().clear();
        this.setCountFormulasSinGrabar(0);
        this.desMarcarCeldasSelecionadasByTarget();
        return null;
    }
}
