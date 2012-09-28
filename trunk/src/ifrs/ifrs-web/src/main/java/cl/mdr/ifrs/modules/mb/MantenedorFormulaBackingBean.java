package cl.mdr.ifrs.modules.mb;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.maxFrom;
import static ch.lambdaj.Lambda.minFrom;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;
import static cl.mdr.ifrs.ejb.cross.FormulaHelper.*;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.fieldset.Fieldset;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.model.TreeFormula;
import cl.mdr.ifrs.cross.util.PropertyManager;
import cl.mdr.ifrs.ejb.common.TipoCeldaEnum;
import cl.mdr.ifrs.ejb.common.VigenciaEnum;
import cl.mdr.ifrs.ejb.cross.FormulaHelper;
import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.Catalogo;
import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.ifrs.ejb.entity.Columna;
import cl.mdr.ifrs.ejb.entity.Estructura;
import cl.mdr.ifrs.ejb.entity.Grilla;
import cl.mdr.ifrs.ejb.entity.PeriodoEmpresa;
import cl.mdr.ifrs.ejb.entity.RelacionEeff;
import cl.mdr.ifrs.ejb.entity.TipoCuadro;
import cl.mdr.ifrs.ejb.entity.Version;
import cl.mdr.ifrs.exceptions.FormulaException;
import cl.mdr.ifrs.vo.GrillaVO;




/**
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
	
	private boolean renderTreeNode;
	private Map<String, Celda> celdaMap = new LinkedHashMap<String, Celda>();
	
	
	private String formula = ""; 
	private transient HtmlInputText formulaTargetOutput;
    private transient InputText barraFormulaOutput;
    private String barraFormula;
    private int countFormulasSinGrabar = 0;
    private Map<Celda, Map<Celda, Boolean>> camposFormula = new LinkedHashMap<Celda, Map<Celda, Boolean>>();
    private boolean renderedCatalogoTree;
    private Estructura estructura;
    private boolean renderTablaFormula = Boolean.FALSE;
    private boolean renderBarraFormula = Boolean.FALSE;
    private TreeNode selectedNode;  
    private DataTable dataTableRow;
    private List<Version> versionList;
    private SelectOneMenu comboBuscarIdTipoCuadro;
    private SelectOneMenu comboBuscarIdCatalogo;
    private SelectOneMenu comboBuscarMeses;
    private SelectOneMenu comboBuscarAnio;
    private int largoBarraFormula;
    
	
	private TreeNode root;
	
	private GrillaVO grillaVO = new GrillaVO();
	
	   
    private HtmlSelectOneMenu tipoFormulaCombo;

    
    private Fieldset panelGroupLayoutTablaFormula;
    private Fieldset panelGroupLayoutBarraFormula;
    private InputText contadorFormulasUnsavedOutput;
    
    public final int SUMA = 1;
    public final int RANGO = 1;
    
    /*nuevas modificaciones*/
    private Celda celdaTarget;
    private int tipoOperacion = SUMA; //suma
    private int tipoSeleccion = RANGO; //por rango de celdas
    
    /*Variables solo celda dinamica*/
    private Map<Celda, List<Celda>> celdasTotalMap;
    
    /*Valiables solo celda estatica*/
    private Map<String, Celda> celdasKeyMap;
	/**
	 * 
	 */
	private static final long serialVersionUID = -4726692791898733737L;
	
	
	private void init(){
		this.celdasTotalMap = null;
		this.setBarraFormula(null);
        this.setCeldaTarget(null);
        this.getFormulaMap().clear();
        this.setCountFormulasSinGrabar(0);
        this.setRenderTablaFormula(Boolean.TRUE);
        this.setRenderBarraFormula(Boolean.FALSE);
        this.getTipoFormulaCombo().setValue(null);
	}
	
	
    public void changeTipoCuadro() {
    	
		try {
			this.catalogoList = getFacadeService().getCatalogoService().findCatalogoByFiltro(getFiltroBackingBean().getEmpresa().getIdRut(), getNombreUsuario(), new TipoCuadro(getIdTipoCuadro()) , null, 1L);
		} catch (Exception e) {
			addErrorMessage("Error al buscar Catálogo");
			logger.error("Error al buscar Catálogo",e);
		}
    }
    
    
    /**
     * busca un listado de estructuras para el cuadro seleccionado en los criterios de busqueda.
     * @return
     */
    public void buscar(){ 
    	
    	this.setRenderedCatalogoTree(Boolean.TRUE);
    	this.setRenderTablaFormula(Boolean.FALSE);
    	this.setRenderBarraFormula(Boolean.FALSE);
    	
    	Long periodoLong = Long.parseLong( super.getFiltroBackingBean().getPeriodoEmpresa().getPeriodo().getAnioPeriodo() ) * 100 + Long.parseLong( super.getFiltroBackingBean().getPeriodoEmpresa().getPeriodo().getMesPeriodo() );
        PeriodoEmpresa periodoEmpresa = getFacadeService().getPeriodoService().getPeriodoEmpresaById(periodoLong, super.getFiltroBackingBean().getEmpresa().getIdRut());
        
        try {
        	
        	List<Version> lista = null;
        	
        	if (periodoEmpresa != null){
        		lista = getFacadeService().getVersionService().findVersionByFiltro(null, new TipoCuadro(getIdTipoCuadro()) , periodoEmpresa, null, VigenciaEnum.VIGENTE.getKey(), new Catalogo(getIdCatalogo()));
        		setTreeNode(lista);
        	}
        	
    		if(lista == null || lista.size() == 0){
    			super.addWarnMessage(PropertyManager.getInstance().getMessage("periodo_busqueda_sin_resultado"));
    			this.setRenderedCatalogoTree(Boolean.FALSE);
    		}
		} catch (Exception e) {
			addErrorMessage("Error al Buscar información");
			logger.error(e);
		}  
    }
    
    
    public void setTreeNode(List<Version> lista){
    	
    	root = new DefaultTreeNode("root", null);
    	
    	for (Version version : lista){
    		
    		TreeNode nodoVersion = new DefaultTreeNode( new TreeFormula(version.getCatalogo().getTitulo() , version.getVersion().toString() , version.getCatalogo().getNombre(), version.getEstado().getNombre(), Util.getString( version.getFechaCreacion() ), version.getVigencia().toString(), null , null ,null) , root);
    		
    			for (Estructura estructura : version.getEstructuraList()){
    				
    					if (estructura.getTipoEstructura().getNombre().toUpperCase().equals("GRILLA")){
    						
    						new DefaultTreeNode( new TreeFormula("", "", "", "", "", "", "function_16x16.png", version.getCatalogo() , estructura) , nodoVersion);
    					} 
    			}
    	}
    	
    }
    
    /**
     * ActionListener que ejecuuta la busqueda de una grilla para editar formulas
     * @param event
     */
    public void buscarEstructuraGrilla(ActionEvent event){
    	
    	this.init();
        final Estructura estructura = (Estructura)event.getComponent().getAttributes().get("estructura");
        this.setEstructura(estructura);
        
        try {
        	
			final Grilla grilla = this.getFacadeService().getGrillaService().findGrillaById(estructura.getIdEstructura());
			this.setGrillaVO(this.getFacadeService().getEstructuraService().getGrillaVO(grilla, Boolean.FALSE));
			this.getTipoFormulaCombo().setValue(estructura.getGrilla().getTipoFormula());
			this.setIdTipoFormula(estructura.getGrilla().getTipoFormula());
			this.celdasKeyMap = FormulaHelper.convertCellToMap(this.getGrillaVO().getColumnas());
			
		} catch (Exception e) {
			logger.error(e);
			super.addErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_nota_get_catalogo_error"));
		}
        
    }
    
    /**
     * metodo listener del evento ValueChangeEvent del combo tipo de formula.
     * @param valueChangeEvent
     */    
    public void onChangeTipoFormula(ValueChangeEvent valueChangeEvent) {
       
        Long tipoGrillaNewValue = valueChangeEvent.getNewValue()==null?null:(Long)valueChangeEvent.getNewValue();
        Long tipoGrillaOldValue = valueChangeEvent.getOldValue()==null?null:(Long)valueChangeEvent.getOldValue();
        
        if(tipoGrillaOldValue != null && this.getCountFormulasSinGrabar() > 0){
            
            super.addWarnMessage("Tiene Fórmulas sin guardar, para no almacenar presione cancelar.");
            this.setIdTipoFormula(tipoGrillaOldValue);
            this.getTipoFormulaCombo().setValue(this.getIdTipoFormula());
            return;
            
        }

        this.celdaTarget = null;
        this.setIdTipoFormula(tipoGrillaNewValue);
        this.getTipoFormulaCombo().setValue(this.getIdTipoFormula());
        
    }
    
    
    /**
     * metodo listener que responde al boton de seleccionar la celda de destino para la formula estatica.
     * @param event
     */
    public void fijarCeldaEstaticaTarget(){
    	
    	
    	String idColumna = super.getExternalContext().getRequestParameterMap().get("idColumna");
    	String idGrilla = super.getExternalContext().getRequestParameterMap().get("idGrilla");
    	String idFila = super.getExternalContext().getRequestParameterMap().get("idFila");
    	//Map<String, Celda> cellMap = convertCellToMap(grid.getColumnaList());
	
		final List<Celda> celdaGrillaList =  this.celdaMapToList();	
		
		try {

			this.celdaTarget =  select(celdaGrillaList, having
										   (on(Celda.class).getIdGrilla(), equalTo(Long.parseLong(idGrilla)))
										   .and(having(on(Celda.class).getIdColumna(), equalTo(Long.parseLong(idColumna))))
										   .and(having(on(Celda.class).getIdFila(), equalTo(Long.parseLong(idFila))))).get(0);
			
		} catch (Exception e) {
			addErrorMessage("Error al buscar Celda");
			logger.error(e);
			return;
		}
			
        this.setBarraFormula(celdaTarget.getFormula());
        
        
        this.marcarCeldasSelecionadasByCadenaFormulaEstatica();
        this.setRenderBarraFormula(Boolean.TRUE);
        
        //this.getCamposFormulaByCeldaTarget().clear();
    }
    
    
   
    /**
     * Metodo listener del boton agregar o quitar celda a una formula estatica.
     * @param event
     */
    public void selectCeldaEstatica(){
    	
    	String idColumna = super.getExternalContext().getRequestParameterMap().get("idColumna");
    	String idGrilla = super.getExternalContext().getRequestParameterMap().get("idGrilla");
    	String idFila = super.getExternalContext().getRequestParameterMap().get("idFila");
    	String selected = super.getExternalContext().getRequestParameterMap().get("selected");
    	
    	
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
        
        
        this.setFormula(formulaSaved == null ? this.getCeldaTarget().getFormula() : formulaSaved);     //TODO: Que pasa si this.getCeldaTarget() viene nula?                     
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
            //this.setFormula(this.getFormula().concat(SIGNO_SUMA));
            //this.setFormula(this.getFormula().concat(parOrdenado));            
            //this.updateCamposFormulaByCeldaTargetMap(celda, Boolean.TRUE);            
            celda.setSelectedByFormula(Boolean.TRUE);            
        }else{
            /*if(this.getFormula().contains(SIGNO_SUMA.concat(parOrdenado))){
                this.setFormula(this.getFormula().replace(SIGNO_SUMA.concat(parOrdenado), ""));    
            }
            if(this.getFormula().contains(SIGNO_RESTA.concat(parOrdenado))){
                this.setFormula(this.getFormula().replace(SIGNO_RESTA.concat(parOrdenado), ""));    
            }
            if(this.getFormula().contains(parOrdenado)){
                this.setFormula(this.getFormula().replace(parOrdenado, ""));    
            }
            this.updateCamposFormulaByCeldaTargetMap(celda, Boolean.FALSE);            
            celda.setSelectedByFormula(Boolean.FALSE);*/            
        } 
        this.setBarraFormula(this.getFormula());
        this.updateFormulaMap(this.getCeldaTarget(), this.getFormula()); 
        this.setCountFormulasSinGrabar(this.getFormulaMap().size()); 
        
        //this.updateCamposFormulaMap(this.getCeldaTarget(), this.getCamposFormulaByCeldaTarget());
        this.marcarCeldasSelecionadasByCadenaFormulaEstatica();
       
       
    }
	
	
	
    
    /*RDV*/
    
    /**
     * fija la celda de destino en la cual sera configurada una formula dinamica
     * @param event
     */
    public void fijarCeldaDinamicaTarget(){
    	
    	final String idColumna 	= super.getExternalContext().getRequestParameterMap().get("idColumna");
    	final String idGrilla 	= super.getExternalContext().getRequestParameterMap().get("idGrilla");
    	final String idFila 	= super.getExternalContext().getRequestParameterMap().get("idFila");

    	final List<Celda> celdaGrillaList =  this.celdaMapToList();
    	
		try {

			List<Celda> celdaList =  select(celdaGrillaList, having
										   (on(Celda.class).getIdGrilla(), equalTo(Long.parseLong(idGrilla)))
										   .and(having(on(Celda.class).getIdColumna(), equalTo(Long.parseLong(idColumna))))
										   .and(having(on(Celda.class).getIdFila(), equalTo(Long.parseLong(idFila)))));
			
			this.setCeldaTarget(celdaList.get(0));
			
		} catch (Exception e) {
			addErrorMessage("Error al buscar Celda");
			logger.error(e);
			return;
		}
			
        this.setCeldaTarget(celdaTarget);
        FormulaHelper.marcarCeldasByCeldaTarget(this.getCeldaTarget(), celdaGrillaList, this.getCeldasTotalMap());
        this.setBarraFormula(this.getCeldaTarget().getFormula());
        this.setRenderBarraFormula(Boolean.TRUE);
    }
    
    
    
    /*RDV*/
    public void selectNewCeldaDinamica(){
       
    	if(this.getCeldaTarget() == null){
            logger.info("no se puede aplicar fórmula para la celda de resultado");
            return;
        }
        
    	final String idColumna = super.getExternalContext().getRequestParameterMap().get("idColumna");
    	final String idGrilla = super.getExternalContext().getRequestParameterMap().get("idGrilla");
    	final String idFila = super.getExternalContext().getRequestParameterMap().get("idFila");
    	final String selected 	= super.getExternalContext().getRequestParameterMap().get("selected");
    	
    	final boolean isCheck = Boolean.parseBoolean(selected);
    	
		final List<Celda> celdasGrillaList = this.celdaMapToList();

		try {

			final List<Celda> celdaCeckList =  select(celdasGrillaList, having
										   (on(Celda.class).getIdGrilla(), equalTo(Long.parseLong(idGrilla)))
										   .and(having(on(Celda.class).getIdColumna(), equalTo(Long.parseLong(idColumna))))
										   .and(having(on(Celda.class).getIdFila(), equalTo(Long.parseLong(idFila)))));
			
			final Celda celdaCheck = celdaCeckList.get(0);
			
			if(EqualsBuilder.reflectionEquals(this.getCeldaTarget(), celdaCheck, true)){
				addWarnMessage("No se puede seleccionar la celda total que le está agregando fórmula");
				return;
			}
			
			if(this.getCeldasTotalMap().containsKey(this.getCeldaTarget())){
				
				if(isCheck){
				
					List<Celda> celdaList = this.getCeldasTotalMap().get(this.getCeldaTarget());
					
					int res = 3;
					int size = celdaList.size();
					
					if(size==1){
						res = FormulaHelper.isValidPosCeldaDinamica(this.getCeldaTarget(), celdaList.get(0), celdaCheck);
					}else if(size>1){
						res = FormulaHelper.isValidPosCeldaDinamica(this.getCeldaTarget(), celdaList.get(0),celdaList.get(celdaList.size()-1), celdaCheck);
					}
					
					if(res == 1){
						
						final List<Celda> celdaParaCeckList =  select(celdasGrillaList, having
								   								(on(Celda.class).getIdColumna(), equalTo(this.getCeldaTarget().getIdColumna())));
						
						
						if(Util.esListaValida(celdaParaCeckList)){
						
							for(Celda celda : celdaParaCeckList){
								
								if(Util.isCellNumeric(celda) && 
								   celda.getIdFila().longValue() >= celdaList.get(0).getIdFila().longValue() &&
								   celda.getIdFila().longValue() <= celdaCheck.getIdFila().longValue() &&
								   !celda.getIdFila().equals(this.getCeldaTarget().getIdFila())){
									
									celda.setSelectedByFormula(Boolean.TRUE);
									celda.setChildVertical(this.getCeldaTarget().getParentVertical());
									
									if(celda.getIdFila().longValue() != celdaList.get(0).getIdFila().longValue())
										celdaList.add(celda);
									
									
								}else{
									celda.setSelectedByFormula(Boolean.FALSE);
									celda.setChildVertical(null);
								}
								
							}
							
						}
						
					}else if(res == 2){
						
						final List<Celda> celdaParaCeckList =  select(celdasGrillaList, having
									(on(Celda.class).getIdFila(), equalTo(this.getCeldaTarget().getIdFila())));
						
						for(Celda celda : celdaParaCeckList){
							if(Util.isCellNumeric(celda) &&
							   celda.getIdColumna().longValue() >= celdaList.get(0).getIdColumna().longValue() &&
							   celda.getIdColumna().longValue() <= celdaCheck.getIdColumna().longValue() &&
							   !celda.getIdColumna().equals(this.getCeldaTarget().getIdColumna())){
								   
								celda.setSelectedByFormula(Boolean.TRUE);
								celda.setChildHorizontal(this.getCeldaTarget().getParentHorizontal());
								
								if(celda.getIdColumna().longValue() != celdaList.get(0).getIdColumna().longValue())
									celdaList.add(celda);
								
							}else{
								
								celda.setChildHorizontal(null);
								celda.setSelectedByFormula(Boolean.FALSE);
								
							}
						}
						
					}else{
						addWarnMessage("La celda debe corresponder a la misma fila o columna donde pertenece el total que se encuentra seleccionado");
					}
				
				}else{
					
					if(celdaCheck.getChildVertical() != null){
						final List<Celda> celdaSinCeckList =  select(celdasGrillaList, having (on(Celda.class).getChildVertical(), equalTo(this.getCeldaTarget().getParentVertical())));
						for(Celda celda : celdaSinCeckList){
							celda.setSelectedByFormula(Boolean.FALSE);
							celda.setChildVertical(null);
							celda.setChildHorizontal(null);
						}
						this.getCeldaTarget().setFormula(null);
						this.getCeldaTarget().setParentVertical(null);
						this.getCeldaTarget().setParentHorizontal(null);
						getCeldasTotalMap().remove(this.getCeldaTarget());
					}
					
					if(celdaCheck.getChildHorizontal() != null){
						final List<Celda> celdaSinCeckList =  select(celdasGrillaList, having (on(Celda.class).getChildHorizontal(), equalTo(this.getCeldaTarget().getParentHorizontal())));
						for(Celda celda : celdaSinCeckList){
							celda.setSelectedByFormula(Boolean.FALSE);
							celda.setChildVertical(null);
							celda.setChildHorizontal(null);
						}
						this.getCeldaTarget().setFormula(null);
						this.getCeldaTarget().setParentVertical(null);
						this.getCeldaTarget().setParentHorizontal(null);
						getCeldasTotalMap().remove(this.getCeldaTarget());
					}
					
				}
				
			
			}else{
				
				final int res = FormulaHelper.isValidPrimeraPosCeldaDinamica(this.getCeldaTarget(), celdaCheck);
				
				if(res != 3){
					
					Long idParent = res==1 ? FormulaHelper.getMaxParentVertical(celdaCheck, celdasGrillaList) : FormulaHelper.getMaxParentHorizontal(celdaCheck, celdasGrillaList);
					celdaCheck.setSelectedByFormula(Boolean.TRUE);
					if(res==1){
						celdaCheck.setChildVertical(idParent);
						this.getCeldaTarget().setParentVertical(idParent);
					}else if(res == 2){
						celdaCheck.setChildHorizontal(idParent);
						this.getCeldaTarget().setParentHorizontal(idParent);
					}
					this.getCeldasTotalMap().put(this.getCeldaTarget(), celdaCeckList);
					
				}else{
					addWarnMessage("La celda debe corresponder a la misma fila o columna donde pertenece el total que se encuentra seleccionado");
				}
				
			}
			
			this.setBarraFormula(this.getCeldaTarget().getFormula());
			
		} catch (Exception e) {
			addErrorMessage("Error al buscar Celda");
			logger.error(e);
			return;
		}
	}
    
    
    public Map<Celda, List<Celda>> getCeldasTotalMap(){
    	if(this.celdasTotalMap == null){
    		this.celdasTotalMap = new HashMap<Celda, List<Celda>>();
    	}
    	return this.celdasTotalMap;
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
       /* while (cellKeys.hasMoreTokens()) {
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
        }    */         
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
    	String formulaString = this.getBarraFormulaOutput().getValue().toString();
    	 try {
	        this.validaReferenciaCiclicaFormula();
	        this.validaExpresionFormulaEstatica(formulaString);
	        
	        if(super.getFacesContext().getMessages().hasNext()){
	            return;
	        }
        
       
            List<Celda> celdaList = new ArrayList<Celda>();
            for(Map.Entry<Celda, String> entry : this.getFormulaMap().entrySet()) {
                Celda celda = entry.getKey();
                celda.setFormula(entry.getValue());                
                celdaList.add(celda);
            }  
            this.getGrillaVO().getGrilla().setTipoFormula(Grilla.TIPO_GRILLA_ESTATICA);
            this.getFacadeService().getCeldaService().persistFormulaEstaticaList(this.getGrillaVO().getGrilla(), celdaList);            
            super.addInfoMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_exito_guardar_formula"), this.getFormulaMap().size()));
            this.getFormulaMap().clear();
            this.setCountFormulasSinGrabar(this.getFormulaMap().size());
            
            
        } catch (FormulaException e){
        	super.addErrorMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_error_formato_formula"), formulaString)	);
        	logger.error(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_error_formato_formula"), formulaString));
        } 
        catch (Exception e) {
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
    	String formulaString = this.getBarraFormulaOutput().getValue().toString();
        try {
        	
            if(super.getFacesContext().getMessages().hasNext()){
                return;
            }
            
            this.getGrillaVO().getGrilla().setTipoFormula(Grilla.TIPO_GRILLA_DINAMICA);
            this.getFacadeService().getCeldaService().persistFormulaDinamicaMap(this.getGrillaVO().getGrilla(), this.getCeldasTotalMap());
            super.addInfoMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_exito_guardar_formula"), this.getCeldasTotalMap().size()));
            this.getCeldasTotalMap().clear();
            this.setCountFormulasSinGrabar(0); 
            
        } 
        catch (FormulaException e) {
        	super.addErrorMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_error_formato_formula"), formulaString)	);
        	logger.error(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_error_formato_formula"), formulaString));
        }
        catch (Exception e) {
            super.addErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_error_guardar_formula"));
            logger.error(PropertyManager.getInstance().getMessage("general_mensaje_error_guardar_formula"), e);            
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
  //      this.getCamposFormulaByCeldaTarget().clear();
        this.getFormulaMap().clear();
        this.setCountFormulasSinGrabar(0);
        this.setRenderBarraFormula(Boolean.FALSE);
        return null;
    }
    
    public String limpiarFormulaEstatica(){
        this.celdaTarget = null;
        this.setBarraFormula(null);
//        this.getCamposFormulaByCeldaTarget().clear();
        this.getFormulaMap().clear();
        this.setCountFormulasSinGrabar(0);
        this.setRenderBarraFormula(Boolean.FALSE);
        return null;
    }


	public Estructura getEstructura() {
		return estructura;
	}


	public void setEstructura(Estructura estructura) {
		this.estructura = estructura;
	}

	public void eliminarFormulaEstaticaAll(){
		
        List<Celda> celdaList = new ArrayList<Celda>();
        try{
            for(final Columna columna : this.getGrillaVO().getColumnas()){
                for(final Celda celda : columna.getCeldaList()){
                    celda.setFormula(null);  
                    celda.setSelectedByFormula(Boolean.FALSE);
                    celdaList.add(celda);
                }
            }            
            this.getGrillaVO().getGrilla().setTipoFormula(null);
            //this.getCamposFormulaByCeldaTarget().clear();
            this.setIdTipoFormula(null);
            this.getTipoFormulaCombo().setValue(this.getIdTipoFormula());
            this.setBarraFormula(null); 
            this.getFacadeService().getCeldaService().persistFormulaEstaticaList(this.getGrillaVO().getGrilla(), celdaList); 
            this.setRenderBarraFormula(Boolean.FALSE);
            this.setCountFormulasSinGrabar(0);
            this.setFormulaMap(new HashMap<Celda, String>());
            super.addInfoMessage("Se han eliminado correctamente todas las Fórmulas Estáticas de la Grilla");
            
        } catch (Exception e) {
            super.addErrorMessage("Se ha producido un error al eliminar la fórmula");
            logger.error("Error al eliminar la formula ",e);
        }
    }
	
	public void eliminarFormulaDinamicaAll(){ 
        List<Celda> celdaList = new ArrayList<Celda>();
        try{
            for(final Columna columna : this.getGrillaVO().getColumnas()){
                for(final Celda celda : columna.getCeldaList()){
                    celda.setParentHorizontal(null);                
                    celda.setParentVertical(null);
                    celda.setChildHorizontal(null);
                    celda.setChildVertical(null);
                    celda.setFormula(null);
                    celda.setSelectedByFormula(Boolean.FALSE);
                    celdaList.add(celda);
                }
            }            
            this.getGrillaVO().getGrilla().setTipoFormula(null);
            
            this.setIdTipoFormula(null);
            this.getTipoFormulaCombo().setValue(this.getIdTipoFormula());
            this.setBarraFormula(null); 
            this.getFacadeService().getCeldaService().persistFormulaEstaticaList(this.getGrillaVO().getGrilla(), celdaList);
            this.setRenderBarraFormula(Boolean.FALSE);
            this.setCountFormulasSinGrabar(0);
            this.setFormulaMap(new HashMap<Celda, String>());
            super.addInfoMessage("Se han eliminado correctamente todas las Fórmulas Dinámicas de la Grilla");
        } catch (Exception e) {
            super.addErrorMessage("Se ha producido un error al eliminar la fórmula");
            logger.error("Error al eliminar la formula ",e);
        }
    }


	public boolean isRenderTablaFormula() {
		return renderTablaFormula;
	}


	public void setRenderTablaFormula(boolean renderTablaFormula) {
		this.renderTablaFormula = renderTablaFormula;
	}


	public boolean isRenderBarraFormula() {
		return renderBarraFormula;
	}


	public void setRenderBarraFormula(boolean renderBarraFormula) {
		this.renderBarraFormula = renderBarraFormula;
	}
	
	 
    
    
    
    /**
     * Evalua si una grilla contiene formulas dinamicas
     * @return
     */
    public boolean tieneFormulaDinamica(){
        final List<Celda> celdas = this.celdaMapToList();
        final List<Celda> celdaParentVerticalList = select(celdas ,having(on(Celda.class).getChildVertical(), notNullValue()));
        final List<Celda> celdaParentHorizontalList = select(celdas ,having(on(Celda.class).getChildHorizontal(), notNullValue()));
        if((celdaParentVerticalList != null || celdaParentHorizontalList != null) && (celdaParentVerticalList.size() > 0 || celdaParentHorizontalList.size() > 0)){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }
    
    /**
     * Evalua si una grilla contiene formulas estaticas
     * @return
     */
    public boolean tieneFormulaEstatica(){
        final List<Celda> celdas = this.celdaMapToList();
        final List<Celda> celdaFormulaList = select(celdas ,having(on(Celda.class).getFormula(), notNullValue()));
        if((celdaFormulaList != null) && (celdaFormulaList.size() > 0)){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }


	public HtmlSelectOneMenu getTipoFormulaCombo() {
		return tipoFormulaCombo;
	}


	public void setTipoFormulaCombo(HtmlSelectOneMenu tipoFormulaCombo) {
		this.tipoFormulaCombo = tipoFormulaCombo;
	}
	
	/**
     * Elimina una formula estatica perteneciente a una celda de resultado
     * @param event
     */
    public void eliminarFormulaEstatica(){
        try {
        	
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
    				
    				e.printStackTrace();
    			}
            
            
            celda.setFormula(null);
            this.getFacadeService().getCeldaService().mergeEntity(celda);
            if(!this.tieneFormulaEstatica()){
                this.getGrillaVO().getGrilla().setTipoFormula(null);
                this.getFacadeService().getGrillaService().mergeEntity(this.getGrillaVO().getGrilla());
                
                setIdTipoFormula(null);
                this.getTipoFormulaCombo().setValue(this.getIdTipoFormula());
            }
            this.setCeldaTarget(celda);
            this.setBarraFormula(null);     
            this.setRenderBarraFormula(Boolean.FALSE);
            super.addInfoMessage("Se ha eliminado la Fórmula correctamente");
            
        } catch (Exception e) {
            super.addErrorMessage("Se ha producido un error al eliminar la fórmula");
            logger.error("Error al eliminar la formula ",e);
        }
    }
            
    /**
     * Elimina una formula dinamica vertical
     * @param event
     */
    public void eliminarFormulaDinamica(){        
        
    	try {
        	
        	String idColumna = super.getExternalContext().getRequestParameterMap().get("idColumna");
        	String idGrilla = super.getExternalContext().getRequestParameterMap().get("idGrilla");
        	String idFila = super.getExternalContext().getRequestParameterMap().get("idFila");
        	
        	final List<Celda> celdasGrillaList = this.celdaMapToList();
        	
        	
        	final Celda celdaParentCheck =  select(celdasGrillaList, having
					   (on(Celda.class).getIdGrilla(), equalTo(Long.parseLong(idGrilla)))
					   .and(having(on(Celda.class).getIdColumna(), equalTo(Long.parseLong(idColumna))))
					   .and(having(on(Celda.class).getIdFila(), equalTo(Long.parseLong(idFila))))).get(0);

                             
            final List<Celda> celdaChildVList = select(celdasGrillaList ,having(on(Celda.class).getChildVertical(), equalTo(celdaParentCheck.getParentVertical())));
            final List<Celda> celdaChildHList = select(celdasGrillaList ,having(on(Celda.class).getChildHorizontal(), equalTo(celdaParentCheck.getParentHorizontal())));
            
            celdaParentCheck.setParentVertical(null);
            celdaParentCheck.setParentHorizontal(null);
            celdaParentCheck.setFormula(null);
            
            if(Util.esListaValida(celdaChildVList)){
	            for(Celda celdaChild : celdaChildVList){
	                celdaChild.setChildVertical(null);
	            }
	            this.getFacadeService().getCeldaService().deleteFormulaDinamica(celdaParentCheck, celdaChildVList);
            }
            
            if(Util.esListaValida(celdaChildVList)){
            	this.getFacadeService().getCeldaService().deleteFormulaDinamica(celdaParentCheck, celdaChildHList);
            	for(Celda celdaChild : celdaChildHList){
                    celdaChild.setChildHorizontal(null);
                }
            }
            
            this.getCeldasTotalMap().remove(celdaParentCheck);
            this.setCeldaTarget(celdaParentCheck);
            this.setBarraFormula(null);
            this.setRenderBarraFormula(Boolean.TRUE);
            FormulaHelper.descargarCeldas(celdasGrillaList);
            super.addInfoMessage("Se ha eliminado la fórmula correctamente");
            
        } catch (Exception e) {
            super.addErrorMessage("Se ha producido un error al eliminar la fórmula");
            logger.error("Error al eliminar la formula ",e);
        }
    }


	public TreeNode getSelectedNode() {
		return selectedNode;
	}


	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}


	public DataTable getDataTableRow() {
		return dataTableRow;
	}


	public void setDataTableRow(DataTable dataTableRow) {
		this.dataTableRow = dataTableRow;
	}


	public List<Version> getVersionList() {
		return versionList;
	}


	public void setVersionList(List<Version> versionList) {
		this.versionList = versionList;
	}
    
	public String limpiarResultadoGrilla(){
		
		this.setRenderedCatalogoTree(Boolean.FALSE);
        this.setRenderBarraFormula(Boolean.FALSE);
        this.setRenderTablaFormula(Boolean.FALSE);
        this.getComboBuscarIdTipoCuadro().setValue(null);
        this.getComboBuscarIdCatalogo().setValue(null);
        this.getComboBuscarMeses().setValue(null);
        this.getComboBuscarAnio().setValue(null);
        this.setFormulaMap(new HashMap<Celda, String>());
        return "mantenedor-formula";
    }


	public SelectOneMenu getComboBuscarIdTipoCuadro() {
		return comboBuscarIdTipoCuadro;
	}


	public void setComboBuscarIdTipoCuadro(SelectOneMenu comboBuscarIdTipoCuadro) {
		this.comboBuscarIdTipoCuadro = comboBuscarIdTipoCuadro;
	}


	public SelectOneMenu getComboBuscarIdCatalogo() {
		return comboBuscarIdCatalogo;
	}


	public void setComboBuscarIdCatalogo(SelectOneMenu comboBuscarIdCatalogo) {
		this.comboBuscarIdCatalogo = comboBuscarIdCatalogo;
	}


	public SelectOneMenu getComboBuscarMeses() {
		return comboBuscarMeses;
	}


	public void setComboBuscarMeses(SelectOneMenu comboBuscarMeses) {
		this.comboBuscarMeses = comboBuscarMeses;
	}


	public SelectOneMenu getComboBuscarAnio() {
		return comboBuscarAnio;
	}


	public void setComboBuscarAnio(SelectOneMenu comboBuscarAnio) {
		this.comboBuscarAnio = comboBuscarAnio;
	}


	public HtmlInputText getFormulaTargetOutput() {
		return formulaTargetOutput;
	}


	public void setFormulaTargetOutput(HtmlInputText formulaTargetOutput) {
		this.formulaTargetOutput = formulaTargetOutput;
	}


	public int getLargoBarraFormula() {
		
		if (this.getFormula() != null )
			largoBarraFormula = this.getFormula().length();
		else 
			largoBarraFormula = 2;
		
		return largoBarraFormula;
	}


	public void setLargoBarraFormula(int largoBarraFormula) {
		this.largoBarraFormula = largoBarraFormula;
	}
	
	public void validaExpresionFormulaDinamica(String formula) throws FormulaException{
		
		formula = formula.replaceAll(":", "|");
		String[] arreglo = formula.split("\\|");
		
		for (int i=0; i<arreglo.length;i++ ){
			/*if (!Util.validaParOrdenadoSinSigno(arreglo[i])){
					throw new FormulaException();
			}*/
		}
		
	}
	
	public void validaExpresionFormulaEstatica(String formula) throws FormulaException{
		
		formula = formula.replaceAll(";", "|");
		String[] arreglo = formula.split("\\|");
		
		for (int i=0; i<arreglo.length;i++ ){
			/*if (!Util.validaParOrdenadoConSigno(arreglo[i])){
					throw new FormulaException();
			}*/
		}
		
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

	
	

	public Map<Celda, String> getFormulaMap() {
		return null;
	}


	public void setFormulaMap(Map<Celda, String> formulaMap) {

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

	/*
	public InputText getFormulaTargetOutput() {
		return formulaTargetOutput;
	}


	public void setFormulaTargetOutput(InputText formulaTargetOutput) {
		this.formulaTargetOutput = formulaTargetOutput;
	}*/

	

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
	
	 
}
