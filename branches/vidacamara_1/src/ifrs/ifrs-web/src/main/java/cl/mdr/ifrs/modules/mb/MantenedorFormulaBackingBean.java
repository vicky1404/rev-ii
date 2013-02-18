package cl.mdr.ifrs.modules.mb;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static cl.mdr.ifrs.ejb.cross.FormulaHelper.PUNTO_COMA;
import static cl.mdr.ifrs.ejb.cross.FormulaHelper.SIGNO_RESTA;
import static cl.mdr.ifrs.ejb.cross.FormulaHelper.SIGNO_SUMA;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.log4j.Logger;
import org.primefaces.component.fieldset.Fieldset;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.model.TreeFormula;
import cl.mdr.ifrs.cross.util.PropertyManager;
import cl.mdr.ifrs.ejb.common.VigenciaEnum;
import cl.mdr.ifrs.ejb.cross.FormulaHelper;
import cl.mdr.ifrs.ejb.cross.SortHelper;
import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.Catalogo;
import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.ifrs.ejb.entity.Estructura;
import cl.mdr.ifrs.ejb.entity.Grilla;
import cl.mdr.ifrs.ejb.entity.PeriodoEmpresa;
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
    
    /*Variables comunes*/
    private Celda celdaTarget;
    private List<Version> versionList;
    private int countFormulasSinGrabar = 0;
    private String barraFormula;
    private Long idTipoFormula;
    private Long idTipoCuadro;
	private List<Catalogo> catalogoList;
	private int largoBarraFormula;
	private GrillaVO grillaVO;
	private Estructura estructura;
	private Integer tipoOperacion = FormulaHelper.SUMAR;

	
    /*Variables solo celda dinamica*/
    private Map<Celda, List<Celda>> celdasTotalMap;
    
    /*Valiables solo celda estatica*/
    private List<Celda> celdaGrillaList;
    private Map<String, Celda> celdasKeyMap;
    private Map<Celda, Celda> celdasSaveMap;
    
	/*Render*/        
    private boolean renderedCatalogoTree;
    private boolean renderTreeNode;
    private boolean renderTablaFormula = Boolean.FALSE;
    private boolean renderBarraFormula = Boolean.FALSE;
    
    /*componentes de la pagina*/
    private transient TreeNode selectedNode;  
    private transient TreeNode root;
    private transient SelectOneMenu comboBuscarIdTipoCuadro;
    private transient SelectOneMenu comboBuscarIdCatalogo;
    private transient SelectOneMenu comboBuscarMeses;
    private transient SelectOneMenu comboBuscarAnio;
    private transient HtmlSelectOneMenu tipoFormulaCombo;    
    private transient Fieldset panelGroupLayoutTablaFormula;
    //private transient Fieldset panelGroupLayoutBarraFormula;
    private transient InputText contadorFormulasUnsavedOutput;
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -4726692791898733737L;
	
	
	private void init(){
		this.grillaVO = null;
		this.celdaGrillaList = null;
		this.celdasTotalMap = null;
		this.celdasKeyMap = null;
		this.setBarraFormula(null);
        this.celdaTarget = null;
        this.setCountFormulasSinGrabar(0);
        this.setRenderTablaFormula(Boolean.FALSE);
        this.setRenderBarraFormula(Boolean.FALSE);
        this.setRenderedCatalogoTree(Boolean.TRUE);
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
    	
    	init();
    	
    	Long periodoLong = Long.parseLong( getFiltroBackingBean().getAnio()) * 100 + Long.parseLong( getFiltroBackingBean().getMes() );
        PeriodoEmpresa periodoEmpresa = getFacadeService().getPeriodoService().getPeriodoEmpresaById(periodoLong, getFiltroBackingBean().getEmpresa().getIdRut());
        
        try {
        	
        	List<Version> lista = null;
        	
        	if (periodoEmpresa != null){
        		lista = getFacadeService().getVersionService().findVersionByFiltro(null, getFiltroBackingBean().getTipoCuadro(), periodoEmpresa, null, VigenciaEnum.VIGENTE.getKey(), getFiltroBackingBean().getCatalogo());
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
    
    public void cancelarBuscar(){
	    init();
	    this.setRenderedCatalogoTree(Boolean.FALSE);
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
			SortHelper.sortGrilla(grilla);
			this.setGrillaVO(this.getFacadeService().getEstructuraService().getGrillaVO(grilla, Boolean.FALSE));
			this.getTipoFormulaCombo().setValue(estructura.getGrilla().getTipoFormula());
			this.setIdTipoFormula(estructura.getGrilla().getTipoFormula());
			this.celdasKeyMap = FormulaHelper.buildCeldaMap(this.getGrillaVO());
			this.celdaGrillaList =  FormulaHelper.celdaMapToList(this.getGrillaVO());	
			this.setRenderTablaFormula(Boolean.TRUE);
			
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
        
        if(tipoGrillaOldValue != null && this.getCeldasSaveMap().size()>0 || this.getCeldasTotalMap().size()>0){
            
            super.addWarnMessage("Tiene Fórmulas sin guardar, para no almacenar presione cancelar.");
            this.setIdTipoFormula(tipoGrillaOldValue);
            this.getTipoFormulaCombo().setValue(tipoGrillaOldValue);
            return;
            
        }
        
        if(getGrillaVO().getGrilla().getTipoFormula()!=null && !getGrillaVO().getGrilla().getTipoFormula().equals(tipoGrillaNewValue)){
        	super.addWarnMessage("Debe presionar el boton Eliminar Todas, para poder cambiar el tipo de Fórmula.");
            this.setIdTipoFormula(getGrillaVO().getGrilla().getTipoFormula());
            this.getTipoFormulaCombo().setValue(getGrillaVO().getGrilla().getTipoFormula());
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
        FormulaHelper.desmacarCeldas(celdaGrillaList);
        this.marcarCeldasSelecionadasByCadenaFormulaEstatica(celdaTarget);
        this.setRenderBarraFormula(Boolean.TRUE);
    }
    
    
   
    /**
     * Metodo listener del boton agregar o quitar celda a una formula estatica.
     * @param event
     */
    public void selectCeldaEstatica(){
    	
    	if(this.getCeldaTarget() == null){
            super.addWarnMessage(PropertyManager.getInstance().getMessage("general_mensaje_formula_sin_celda_destino"));
            return;
        }
    	
    	String idColumna = super.getExternalContext().getRequestParameterMap().get("idColumna");
    	String idGrilla = super.getExternalContext().getRequestParameterMap().get("idGrilla");
    	String idFila = super.getExternalContext().getRequestParameterMap().get("idFila");
    	String selected = super.getExternalContext().getRequestParameterMap().get("selected");
	
		try {

			final Celda celdaCheck =  select(celdaGrillaList, having
										   (on(Celda.class).getIdGrilla(), equalTo(Long.parseLong(idGrilla)))
										   .and(having(on(Celda.class).getIdColumna(), equalTo(Long.parseLong(idColumna))))
										   .and(having(on(Celda.class).getIdFila(), equalTo(Long.parseLong(idFila))))).get(0);
			
			final boolean isCheck = Boolean.parseBoolean(selected);
	        final String celdaCheckKey = Util.formatCellKey(celdaCheck).concat(PUNTO_COMA);
	        String formula  = this.getCeldaTarget().getFormula() == null ? "" : this.getCeldaTarget().getFormula();
	        
	        
	        
	        if(EqualsBuilder.reflectionEquals(this.getCeldaTarget(), celdaCheck, true)){
	            super.addWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_celda_destino_existe_en_formula"), celdaCheckKey));             
	            return;
	        }
	        
	        if(!isCheck){
	        	int pos = formula.indexOf(celdaCheckKey);
	        	/*if(pos==0){
	        		formula = formula.substring(pos,formula.length());
	        	}else */
	        	//La celda esta al final de la formula String
	        	
	        	if(pos == formula.length() - (celdaCheckKey).length()){
	        		
	        		formula = formula.substring(0,pos-1);
	        	
	        	}else{ //La celda esta entre medio de la formula String
	        		
	        		String formulaPaso = formula.substring(0,pos-1);
	        		formulaPaso += formula.substring( pos +(celdaCheckKey).length() ,formula.length());
	        		formula = formulaPaso;
	        		
	        	}

	        	this.getCeldaTarget().setFormula(formula);
	        	this.setBarraFormula(formula);
	        	getCeldasSaveMap().put(this.getCeldaTarget(),this.getCeldaTarget());

	        }else{
	        
	        	if(!formula.contains(celdaCheckKey)){
	        		
	        		String simbolo = getTipoOperacion()==null ? String.valueOf(FormulaHelper.SIGNO_SUMA) :
	        						 getTipoOperacion().equals(FormulaHelper.SUMAR) ? String.valueOf(FormulaHelper.SIGNO_SUMA) : 
	        						 String.valueOf(FormulaHelper.SIGNO_RESTA);
	        		
	        		formula = formula.concat(simbolo).concat(celdaCheckKey);
	        		this.getCeldaTarget().setFormula(formula);
		        	this.setBarraFormula(formula);
		        	getCeldasSaveMap().put(this.getCeldaTarget(),this.getCeldaTarget());
	        	}
	        }
	        
	        celdaCheck.setSelectedByFormula(isCheck);
	        this.setCountFormulasSinGrabar(this.getCeldasSaveMap().size()); 
        
		} catch (Exception e) {
			addErrorMessage("Error al buscar procesar fórmula");
			logger.error(e);
			return;
		}

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
		
        FormulaHelper.marcarCeldasDinamicasByCeldaTarget(this.getCeldaTarget(), celdaGrillaList, this.getCeldasTotalMap());
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
    	
		final List<Celda> celdasGrillaList = FormulaHelper.celdaMapToList(this.getGrillaVO());

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
							
							Celda primeraCelda = celdaList.get(0);
							
							if(size==1){
								if(celdaCheck.getIdFila().longValue() < primeraCelda.getIdFila().longValue()){
									addWarnMessage("La fila debe ser mayor a la fila seleccionada");
									return;
								}
								
								if(celdaCheck.getIdFila().equals(primeraCelda.getIdFila())){
									primeraCelda.setSelectedByFormula(Boolean.FALSE);
									return;
								}
							}
						
							for(Celda celda : celdaParaCeckList){
								
								if(Util.isCellNumeric(celda) && 
								   celda.getIdFila().longValue() >= primeraCelda.getIdFila().longValue() &&
								   celda.getIdFila().longValue() <= celdaCheck.getIdFila().longValue() &&
								   !celda.getIdFila().equals(this.getCeldaTarget().getIdFila())){
									
									celda.setSelectedByFormula(Boolean.TRUE);
									celda.setChildVertical(this.getCeldaTarget().getParentVertical());
									
									if(celda.getIdFila().longValue() != primeraCelda.getIdFila().longValue())
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
						
						if(Util.esListaValida(celdaParaCeckList)){
							
							
							Celda primeraCelda = celdaList.get(0);
							
							if(size==1){
								if(celdaCheck.getIdColumna().longValue() < primeraCelda.getIdColumna().longValue()){
									addWarnMessage("La fila debe ser mayor a la columna seleccionada");
									return;
								}
								
								if(celdaCheck.getIdColumna().equals(primeraCelda.getIdColumna())){
									primeraCelda.setSelectedByFormula(Boolean.FALSE);
									return;
								}
							}
							
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
			
			this.setCountFormulasSinGrabar(this.getCeldasTotalMap().size());
			this.setBarraFormula(this.getCeldaTarget().getFormula());
			
		} catch (Exception e) {
			addErrorMessage("Error al buscar Celda");
			logger.error(e);
			return;
		}
	}
    
    /**
     * Marca como seleccionado en la grilla de formula estatica 
     * las celdas seleccionadas en la formula de destino
     */
    private void marcarCeldasSelecionadasByCadenaFormulaEstatica(final Celda celdaTarget){
        logger.info("marcando celdas para formula");
        if(celdaTarget.getFormula() == null || celdaTarget.getFormula().equals("")){
            return;
        }
        StringTokenizer cellKeys = new StringTokenizer(celdaTarget.getFormula(), ";");
        while (cellKeys.hasMoreTokens()) {
        	String cellKey = cellKeys.nextToken()
                    .replace(String.valueOf(FormulaHelper.SIGNO_SUMA), "")
                    .replace(String.valueOf(SIGNO_RESTA), "");
        	
        	if (this.getCeldasKeyMap().containsKey(cellKey)) {
                Celda celda = this.getCeldasKeyMap().get(cellKey);
                if(EqualsBuilder.reflectionEquals(celdaTarget, celda, true)){
                    super.addWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_celda_destino_existe_en_formula"), cellKey));             
                    return;
                }
                celda.setSelectedByFormula(Boolean.TRUE);                            
            }else{
                super.addErrorMessage("La celda "+cellKey+" no existe en el cuadro");
                if(celdaTarget.getFormula().contains(String.valueOf(FormulaHelper.SIGNO_SUMA).concat(cellKey))){
                	celdaTarget.setFormula(celdaTarget.getFormula().replace(String.valueOf(SIGNO_SUMA).concat(cellKey.concat(PUNTO_COMA)), ""));    
                }
                if(celdaTarget.getFormula().contains(String.valueOf(SIGNO_RESTA).concat(cellKey))){
                	celdaTarget.setFormula(celdaTarget.getFormula().replace(String.valueOf(SIGNO_RESTA).concat(cellKey.concat(PUNTO_COMA)), ""));    
                }
                if(celdaTarget.getFormula().contains(cellKey)){
                	celdaTarget.setFormula(celdaTarget.getFormula().replace(cellKey.concat(PUNTO_COMA), ""));    
                }
                this.setBarraFormula(celdaTarget.getFormula());                            
            }

        }         
    }
	
	 /**
     * ActionListener que guarda las configuraciones efectuadas en las formulas estaticas
     * para la grilla.
     * @param event
     */
    public void guardarFormulaEstatica(ActionEvent event){
    	 try {
    		 
    		 for(Celda celda : celdasSaveMap.values()){
    		 
    			 if(celda.getFormula()!=null)
    				 this.validaExpresionFormulaEstatica(celda.getFormula());
    			 
    		 }
	        
	        if(super.getFacesContext().getMessages().hasNext()){
	            return;
	        }
        
            this.getGrillaVO().getGrilla().setTipoFormula(Grilla.TIPO_GRILLA_ESTATICA);
            
            this.getFacadeService().getCeldaService().persistFormulaEstaticaList(this.getGrillaVO().getGrilla(), this.getCeldasSaveMap().values());            
            super.addInfoMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_exito_guardar_formula"), this.getCeldasSaveMap().size()));
            this.getCeldasSaveMap().clear();
            this.getCeldasTotalMap().clear();
            this.setCountFormulasSinGrabar(0);
            
            
        } catch (FormulaException e){
        	super.addErrorMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_error_formato_formula"), "")	);
        	logger.error(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_error_formato_formula"), ""));
        } 
        catch (Exception e) {
            super.addErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_error_guardar_formula"));
            logger.error(PropertyManager.getInstance().getMessage("general_mensaje_error_guardar_formula"), e);            
        }
    }
    
    /**
     * ActionListener que guarda las configuraciones efectuadas en las formulas Dinamicas
     * para la grilla.
     * @param event
     */
    public void guardarFormulaDinamica(ActionEvent event){
    	
        try {
        	
            if(super.getFacesContext().getMessages().hasNext()){
                return;
            }
            
            this.getGrillaVO().getGrilla().setTipoFormula(Grilla.TIPO_GRILLA_DINAMICA);
            this.getFacadeService().getCeldaService().persistFormulaDinamicaMap(this.getGrillaVO().getGrilla(), this.getCeldasTotalMap());
            super.addInfoMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_exito_guardar_formula"), this.getCeldasTotalMap().size()));
            this.getCeldasTotalMap().clear();
            this.getCeldasSaveMap().clear();
            this.setCountFormulasSinGrabar(0); 
            
        } 
        catch (FormulaException e) {
        	super.addErrorMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_error_formato_formula"), "re")	); //TODO REVISAR
        	logger.error(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_error_formato_formula"), "re"));
        }
        catch (Exception e) {
            super.addErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_error_guardar_formula"));
            logger.error(PropertyManager.getInstance().getMessage("general_mensaje_error_guardar_formula"), e);            
        }
    }

	

	public void eliminarFormulaEstaticaAll(){

        try{
            for(final Celda celda : this.getCeldaGrillaList()){
                celda.setFormula(null);  
                celda.setSelectedByFormula(Boolean.FALSE);
            }
            this.getGrillaVO().getGrilla().setTipoFormula(null);
            this.getCeldasSaveMap().clear();
            this.setIdTipoFormula(null);
            this.getTipoFormulaCombo().setValue(null);
            this.setBarraFormula(null); 
            this.getFacadeService().getCeldaService().persistFormulaEstaticaList(this.getGrillaVO().getGrilla(), this.getCeldaGrillaList()); 
            this.setRenderBarraFormula(Boolean.FALSE);
            this.getCeldasTotalMap().clear();
            this.getCeldasSaveMap().clear();
            this.setCountFormulasSinGrabar(0);
            super.addInfoMessage("Se han eliminado correctamente todas las Fórmulas Estáticas de la Grilla");
            
        } catch (Exception e) {
            super.addErrorMessage("Se ha producido un error al eliminar la fórmula");
            logger.error("Error al eliminar la formula ",e);
        }
    }
	
	public void eliminarFormulaDinamicaAll(){ 
        try{
            for(final Celda celda : this.getCeldaGrillaList()){
                celda.setParentHorizontal(null);                
                celda.setParentVertical(null);
                celda.setChildHorizontal(null);
                celda.setChildVertical(null);
                celda.setFormula(null);
                celda.setSelectedByFormula(Boolean.FALSE);
            }        
            this.getGrillaVO().getGrilla().setTipoFormula(null);
            this.setIdTipoFormula(null);
            this.getTipoFormulaCombo().setValue(null);
            this.setBarraFormula(null); 
            this.getFacadeService().getCeldaService().persistFormulaEstaticaList(this.getGrillaVO().getGrilla(), this.getCeldaGrillaList());
            this.setRenderBarraFormula(Boolean.FALSE);
            this.getCeldasTotalMap().clear();
            this.getCeldasSaveMap().clear();
            this.setCountFormulasSinGrabar(0);
            super.addInfoMessage("Se han eliminado correctamente todas las Fórmulas Dinámicas de la Grilla");
        } catch (Exception e) {
            super.addErrorMessage("Se ha producido un error al eliminar la fórmula");
            logger.error("Error al eliminar la formula ",e);
        }
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
        	
        	final List<Celda> celdasGrillaList = this.getCeldaGrillaList();
        	
        	final Celda celdaCheck =  select(celdasGrillaList, having
					   (on(Celda.class).getIdGrilla(), equalTo(Long.parseLong(idGrilla)))
					   .and(having(on(Celda.class).getIdColumna(), equalTo(Long.parseLong(idColumna))))
					   .and(having(on(Celda.class).getIdFila(), equalTo(Long.parseLong(idFila))))).get(0);
            
            
        	celdaCheck.setFormula(null);
            this.getFacadeService().getCeldaService().mergeEntity(celdaCheck);
            this.setCeldaTarget(celdaCheck);
            this.setBarraFormula(null);
            FormulaHelper.desmacarCeldas(this.getCeldaGrillaList());
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
        	
        	final List<Celda> celdasGrillaList = this.getCeldaGrillaList();
        	
        	
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
            FormulaHelper.desmacarCeldas(celdasGrillaList);
            super.addInfoMessage("Se ha eliminado la fórmula correctamente");
            
        } catch (Exception e) {
            super.addErrorMessage("Se ha producido un error al eliminar la fórmula");
            logger.error("Error al eliminar la formula ",e);
        }
    }
	
	
	
    public void cancelarFormulaEstatica(){
    	cancelarEdicion();
    }
    
    public void cancelarFormulaDinamica(){
    	cancelarEdicion();
    }
    
    private void cancelarEdicion(){
    	this.celdasTotalMap = null;
		this.celdasKeyMap = null;
		this.setBarraFormula(null);
        this.setCeldaTarget(null);
        this.setCountFormulasSinGrabar(0);
    }
    
    
	public Map<Celda, List<Celda>> getCeldasTotalMap(){
    	if(this.celdasTotalMap == null){
    		this.celdasTotalMap = new HashMap<Celda, List<Celda>>();
    	}
    	return this.celdasTotalMap;
    }


	public boolean isRenderedCatalogoTree() {
		return renderedCatalogoTree;
	}


	public void setRenderedCatalogoTree(boolean renderedCatalogoTree) {
		this.renderedCatalogoTree = renderedCatalogoTree;
	}
	
	public Estructura getEstructura() {
		return estructura;
	}


	public void setEstructura(Estructura estructura) {
		this.estructura = estructura;
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
        final List<Celda> celdas = FormulaHelper.celdaMapToList(this.getGrillaVO());
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
        final List<Celda> celdas = FormulaHelper.celdaMapToList(this.getGrillaVO());
        final List<Celda> celdaFormulaList = select(celdas ,having(on(Celda.class).getFormula(), notNullValue()));
        if((celdaFormulaList != null) && (celdaFormulaList.size() > 0)){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
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
			if (!Util.validaParOrdenadoConSigno(arreglo[i])){
					throw new FormulaException();
			}
		}
		
	}


	public HtmlSelectOneMenu getTipoFormulaCombo() {
		return tipoFormulaCombo;
	}


	public void setTipoFormulaCombo(HtmlSelectOneMenu tipoFormulaCombo) {
		this.tipoFormulaCombo = tipoFormulaCombo;
	}


	public TreeNode getSelectedNode() {
		return selectedNode;
	}


	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}


	public List<Version> getVersionList() {
		return versionList;
	}


	public void setVersionList(List<Version> versionList) {
		this.versionList = versionList;
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

	public int getLargoBarraFormula() {
		
		if (this.getCeldaTarget() != null && this.getCeldaTarget().getFormula() != null)
			largoBarraFormula = this.getCeldaTarget().getFormula().length();
		else 
			largoBarraFormula = 2;
		
		return largoBarraFormula;
	}


	public void setLargoBarraFormula(int largoBarraFormula) {
		this.largoBarraFormula = largoBarraFormula;
	}
	
	public List<Catalogo> getCatalogoList() {
		return catalogoList;
	}


	public void setCatalogoList(List<Catalogo> catalogoList) {
		this.catalogoList = catalogoList;
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
		
		if(grillaVO==null){
			grillaVO = new GrillaVO();
		}

		return grillaVO;
	}


	public void setGrillaVO(GrillaVO grillaVO) {
		this.grillaVO = grillaVO;
	}

	public Celda getCeldaTarget() {
		return celdaTarget;
	}


	public void setCeldaTarget(Celda celdaTarget) {
		this.celdaTarget = celdaTarget;
	}

	public Fieldset getPanelGroupLayoutTablaFormula() {
		return panelGroupLayoutTablaFormula;
	}


	public void setPanelGroupLayoutTablaFormula(
			Fieldset panelGroupLayoutTablaFormula) {
		this.panelGroupLayoutTablaFormula = panelGroupLayoutTablaFormula;
	}

	public String getBarraFormula() {
		return barraFormula;
	}


	public void setBarraFormula(String barraFormula) {
		this.barraFormula = barraFormula;
	}

	public int getCountFormulasSinGrabar() {
		return countFormulasSinGrabar;
	}


	public void setCountFormulasSinGrabar(int countFormulasSinGrabar) {
		this.countFormulasSinGrabar = countFormulasSinGrabar;
	}

	public InputText getContadorFormulasUnsavedOutput() {
		return contadorFormulasUnsavedOutput;
	}


	public void setContadorFormulasUnsavedOutput(
			InputText contadorFormulasUnsavedOutput) {
		this.contadorFormulasUnsavedOutput = contadorFormulasUnsavedOutput;
	}


	public Map<String, Celda> getCeldasKeyMap() {
		if(celdasKeyMap == null){
			celdasKeyMap = new HashMap<String,Celda>();
		}
		return celdasKeyMap;
	}


	public void setCeldasKeyMap(Map<String, Celda> celdasKeyMap) {
		this.celdasKeyMap = celdasKeyMap;
	}


	public List<Celda> getCeldaGrillaList() {
		if(celdaGrillaList == null){
			celdaGrillaList = new ArrayList<Celda>();
		}
		return celdaGrillaList;
	}


	public void setCeldaGrillaList(List<Celda> celdaGrillaList) {
		this.celdaGrillaList = celdaGrillaList;
	}


	public Map<Celda, Celda> getCeldasSaveMap() {
		if(celdasSaveMap==null){
			celdasSaveMap = new HashMap<Celda,Celda>();
		}
		return celdasSaveMap;
	}


	public void setCeldasSaveMap(Map<Celda, Celda> celdasSaveMap) {
		this.celdasSaveMap = celdasSaveMap;
	}


	public Long getIdTipoCuadro() {
		return idTipoCuadro;
	}


	public void setIdTipoCuadro(Long idTipoCuadro) {
		this.idTipoCuadro = idTipoCuadro;
	}


	public Integer getTipoOperacion() {
		return tipoOperacion;
	}


	public void setTipoOperacion(Integer tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}


	public void setCeldasTotalMap(Map<Celda, List<Celda>> celdasTotalMap) {
		this.celdasTotalMap = celdasTotalMap;
	}
	
	 
}
