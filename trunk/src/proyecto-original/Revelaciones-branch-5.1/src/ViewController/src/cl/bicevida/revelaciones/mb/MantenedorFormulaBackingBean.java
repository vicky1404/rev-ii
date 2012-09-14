package cl.bicevida.revelaciones.mb;


import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.maxFrom;
import static ch.lambdaj.Lambda.minFrom;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;

import cl.bicevida.revelaciones.common.filtros.Filtro;
import cl.bicevida.revelaciones.common.mb.SoporteBackingBean;
import cl.bicevida.revelaciones.common.model.TreeItem;
import cl.bicevida.revelaciones.common.util.BeanUtil;
import cl.bicevida.revelaciones.common.util.PropertyManager;
import cl.bicevida.revelaciones.ejb.common.TipoCeldaEnum;
import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.revelaciones.ejb.entity.Catalogo;
import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.Columna;
import cl.bicevida.revelaciones.ejb.entity.Estructura;
import cl.bicevida.revelaciones.ejb.entity.Grilla;
import cl.bicevida.revelaciones.ejb.entity.TipoCuadro;
import cl.bicevida.revelaciones.ejb.entity.Version;
import cl.bicevida.revelaciones.ejb.entity.VersionPeriodo;
import cl.bicevida.revelaciones.vo.GrillaVO;

import java.io.Serializable;

import java.text.MessageFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.ejb.EJBException;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import javax.persistence.NoResultException;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.data.RichTreeTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.component.rich.nav.RichCommandImageLink;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.DialogEvent;
import oracle.adf.view.rich.event.ItemEvent;
import oracle.adf.view.rich.render.ClientEvent;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.model.ChildPropertyTreeModel;
import org.apache.myfaces.trinidad.model.TreeModel;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;


/**
 * Clase BackingBean que controla las funcionalidades del mantenedor de Formulas
 * @author rodrigo.reyes@bicevida.cl
 * @link http://cl.linkedin.com/in/rreyesc
 */
public class MantenedorFormulaBackingBean extends SoporteBackingBean implements Serializable {
    private transient final Logger logger = Logger.getLogger(MantenedorFormulaBackingBean.class);
    
    @SuppressWarnings("compatibility:6397602712897679830")
    private static final long serialVersionUID = -4533460546592377457L;
    private SoporteBackingBean soporteBackingBean;
    private List<VersionPeriodo> periodoCatalogoList;
    private List<Catalogo> catalogoList = new ArrayList<Catalogo>();
    private List<Estructura> estructuraList = new ArrayList<Estructura>();
    private Catalogo catalogo;
    private Estructura estructura;
    private Grilla grilla;
    private GrillaVO grillaVO = new GrillaVO();
    
    
    //tree catalogo table    
    private transient ArrayList<TreeItem> catalogoRoot;
    private transient TreeModel catalogoModel = null;
    private transient Object catalogoInstance = null;
    
    private boolean renderGrilla;
    private boolean renderedCatalogoTree;
    private transient RichTreeTable tablaCatalogoTree;
    
    //atributos para editor de formula
    private static final String CORCHETE_IZQUIERDO = "[";
    private static final String CORCHETE_DERECHO = "]";
    private static final String SIGNO_SUMA = "+";
    private static final String SIGNO_RESTA = "-";
    private static final String COMA = ",";
    private static final String PUNTO_COMA = ";";
    private static final String DOS_PUNTOS = ":";
    private Celda celdaTarget;
    private Celda celdaDinamicaTarget;
    private transient RichInputText formulaTargetOutput;
    private transient RichInputText barraFormulaOutput;
    private String formula = "";
    private String barraFormula;
    private StringBuilder cadenaFormula = new StringBuilder();    
    private transient RichTable grillaFormulasTable;
    private Map<Celda, String> formulaMap = new LinkedHashMap<Celda, String>();
    private int countFormulasSinGrabar = 0;
    private transient RichInputText contadorFormulasUnsavedOutput;
    private Map<Celda, Map<Celda, Boolean>> camposFormula = new LinkedHashMap<Celda, Map<Celda, Boolean>>();
    private Map<Celda, Map<Celda, Boolean>> camposFormulaPersistent = new LinkedHashMap<Celda, Map<Celda, Boolean>>();
    private Map<Celda, Boolean> camposFormulaByCeldaTarget = new LinkedHashMap<Celda, Boolean>();
    private Map<Celda, Boolean> camposFormulaByCeldaDisplay = new LinkedHashMap<Celda, Boolean>();    
    private transient RichPanelGroupLayout panelGroupLayoutTablaFormula;
    private transient RichPanelGroupLayout panelGroupLayoutBarraFormula;
    private Map<String, Celda> celdaMap = new LinkedHashMap<String, Celda>();
    private List<Celda> celdaList = new ArrayList<Celda>();
    private transient RichSelectOneChoice tipoFormulaCombo;


    /**
     * Constructor default
     */
    public MantenedorFormulaBackingBean() {
        super();
    }
    
    /**
     * Construye un List &lt;SelectItem&gt; con elementos de Tipo de Formula para ser 
     * presentados en el componente de ComboBox
     * @return
     */
    public List<SelectItem> getTipoFormulaSelectItem() {
        final List<SelectItem> lista = new ArrayList<SelectItem>();
        lista.add(new SelectItem(Grilla.TIPO_GRILLA_ESTATICA, "Fórmula Estática"));
        lista.add(new SelectItem(Grilla.TIPO_GRILLA_DINAMICA, "Fórmula Dinámica"));
        return lista;
    }

    /**
     * Construye un List &lt;SelectItem&gt; con elementos de Catalogo para ser 
     * presentados en el componente de ComboBox
     * @return
     */
    public List<SelectItem> getCatalogoSelectItem() {
        List<SelectItem> lista = new ArrayList<SelectItem>();
        if(super.getFiltro().getTipoCuadro().getIdTipoCuadro() != null){
            this.buildCatalogoByTipo();
            for (Catalogo catalogo : this.getCatalogoList()) {
                lista.add(new SelectItem(catalogo, catalogo.getNombre()));
            }
        }else{
            for (Catalogo catalogo : this.getCatalogoList()) {
                lista.add(new SelectItem(catalogo, catalogo.getNombre()));
            }
        }
        return lista;
    }

    /**
     * Construye una lista de Catalogo segun el tipo de Cuadro seleccionado
     */
    private void buildCatalogoByTipo(){        
        final List<Catalogo> catalogoList = select(super.getComponenteBackingBean().getCatalogoList() ,having(on(Catalogo.class).getTipoCuadro().getIdTipoCuadro(), equalTo(super.getFiltro().getTipoCuadro().getIdTipoCuadro())));               
        this.setCatalogoList(catalogoList);
    }


    /**
     * evento ValueChangeEvent del combo Tipo de Cuadro
     * @param valueChangeEvent
     */
    public void onChangeTipoCuadro(ValueChangeEvent valueChangeEvent) {
        final TipoCuadro tipoCuadro = (TipoCuadro)valueChangeEvent.getNewValue();
        final List<Catalogo> catalogoList = select(super.getComponenteBackingBean().getCatalogoList() ,having(on(Catalogo.class).getTipoCuadro().getIdTipoCuadro(), equalTo(tipoCuadro.getIdTipoCuadro())));                
        this.setCatalogoList(catalogoList);
    }
    

    public String limpiarResultadoGrilla(){
        this.grilla = null;
        this.catalogoModel = null;
        this.setRenderGrilla(Boolean.FALSE);
        this.setRenderedCatalogoTree(Boolean.FALSE);
        return "mantenedor-formula";
    }

    /**
     * busca un listado de estructuras para el cuadro seleccionado en los criterios de busqueda.
     * @return
     */
    public Object buscar(){
        this.limpiarResultadoGrilla();
        Filtro filtroPaso = getFiltro();
        filtroPaso.getPeriodo().setPeriodo(null);
        catalogoInstance = null;
        catalogoModel = null; 
        if (tablaCatalogoTree != null && tablaCatalogoTree.getDisclosedRowKeys()!=null ){
                tablaCatalogoTree.getDisclosedRowKeys().clear();//esta linea fue agregada para resolver NoRowAvailableException. MGC
        }
        //this.setPeriodoCatalogoList(null);
        //getCuadroBackingBean().setEstructuraList(null);

        if(filtroPaso.getCatalogo().getIdCatalogo()!=null){
            try{
                this.setRenderedCatalogoTree(Boolean.TRUE);
                Long periodo = Long.valueOf(filtroPaso.getPeriodo().getAnioPeriodo().concat(filtroPaso.getPeriodo().getMesPeriodo()));                
                try{
                    getFiltro().setPeriodo(getFacade().getMantenedoresTipoService().findByPeriodo(periodo));
                }catch(NoResultException e){
                    agregarWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("periodo_busqueda_sin_resultado_periodo"), super.getFiltro().getPeriodo().getAnioPeriodo(), super.getFiltro().getPeriodo().getMesPeriodo()));
                    getComponenteBackingBean().setPeriodoCatalogoList(null);
                    this.setRenderedCatalogoTree(Boolean.FALSE);                    
                    return null;                    
                }catch(EJBException e){
                    agregarWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("periodo_busqueda_sin_resultado_periodo"), super.getFiltro().getPeriodo().getAnioPeriodo(), super.getFiltro().getPeriodo().getMesPeriodo()));
                    getComponenteBackingBean().setPeriodoCatalogoList(null);
                    this.setRenderedCatalogoTree(Boolean.FALSE);
                    return null;
                }
                getComponenteBackingBean().setPeriodoCatalogoList(super.getFacade().getPeriodoService().findPeriodoAllByPeriodoCatalogo(filtroPaso.getCatalogo(),filtroPaso.getPeriodo()));
                this.getCatalogoTreeModel();
                AdfFacesContext.getCurrentInstance().addPartialTarget(this.getTablaCatalogoTree());
                if(getComponenteBackingBean().getPeriodoCatalogoList() == null){  
                    this.setRenderedCatalogoTree(Boolean.FALSE);
                    agregarWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("periodo_busqueda_sin_resultado_periodo"), super.getFiltro().getPeriodo().getAnioPeriodo(), super.getFiltro().getPeriodo().getMesPeriodo()));
                }
            }catch(Exception e){
                logger.error(e.getCause(), e);
                agregarErrorMessage("Error al consultar Versiones para el Período");
            }
            
        }
        
        return null;
    }

    /**
     * Elimina una formula estatica perteneciente a una celda de resultado
     * @param event
     */
    public void eliminarFormulaEstatica(ActionEvent event){        
        try {
            final Celda celda = (Celda)event.getComponent().getAttributes().get("celda");
            celda.setFormula(null);
            this.getFacade().getCeldaService().mergeEntity(celda);
            this.desMarcarCeldasSelecionadasByTarget();
            if(!this.tieneFormulaEstatica()){
                this.getGrilla().setTipoFormula(null);
                this.getFacade().getGrillaService().mergeEntity(this.getGrilla());
                this.getCamposFormulaByCeldaTarget().clear();
                super.getFiltro().setTipoFormula(null);
                this.updateTipoFormulaCombo(this.getTipoFormulaCombo(), super.getFiltro().getTipoFormula()); 
            }
            this.setCeldaTarget(celda);
            this.setBarraFormula(null);            
            super.agregarSuccesMessage("Se ha eliminado la Fórmula correctamente");
            AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPanelGroupLayoutTablaFormula()); 
            AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPanelGroupLayoutBarraFormula());
        } catch (Exception e) {
            super.agregarErrorMessage("Se ha producido un error al eliminar la fórmula");
            logger.error("Error al eliminar la formula ",e);
        }
    }
    
    public void confirmEliminarFormulaEstaticaAll(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.ok)) {
            this.eliminarFormulaEstaticaAll(null);
            this.celdaTarget = null;
        }        
    }
    
    public void eliminarFormulaEstaticaAll(ActionEvent event){
        List<Celda> celdaList = new ArrayList<Celda>();
        try{
            for(final Columna columna : this.getGrillaVO().getColumnas()){
                for(final Celda celda : columna.getCeldaList()){
                    celda.setFormula(null);                
                    celdaList.add(celda);
                }
            }            
            this.getGrilla().setTipoFormula(null);
            this.getCamposFormulaByCeldaTarget().clear();
            super.getFiltro().setTipoFormula(null);
            this.updateTipoFormulaCombo(this.getTipoFormulaCombo(), super.getFiltro().getTipoFormula());
            this.setBarraFormula(null); 
            this.getFacade().getCeldaService().persistFormulaEstaticaList(this.getGrilla(), celdaList);
            AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPanelGroupLayoutTablaFormula()); 
            AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPanelGroupLayoutBarraFormula());
            super.agregarSuccesMessage("Se han eliminado correctamente todas las Fórmulas Estáticas de la Grilla");
        } catch (Exception e) {
            super.agregarErrorMessage("Se ha producido un error al eliminar la fórmula");
            logger.error("Error al eliminar la formula ",e);
        }
    }
            
    /**
     * Elimina una formula dinamica vertical
     * @param event
     */
    public void eliminarFormulaDinamicaVertical(ActionEvent event){        
        try {
            final Celda celdaParent = (Celda)event.getComponent().getAttributes().get("celda");            
            //obtiene la lista de celdas pertenecientes al parent vertical
            final List<Celda> celdaChildList = select(this.celdaMapToList() ,having(on(Celda.class).getChildVertical(), equalTo(celdaParent.getParentVertical())));
            //eliminamos la referencia del parent.            
            celdaParent.setParentVertical(null);
            for(Celda celdaChild : celdaChildList){
                celdaChild.setChildVertical(null);
            }                        
            this.getFacade().getCeldaService().deleteFormulaDinamica(celdaParent, celdaChildList);
            this.desMarcarCeldasSelecionadasByTarget();
            if(!this.tieneFormulaDinamica()){
                this.getGrilla().setTipoFormula(null);
                this.getFacade().getGrillaService().mergeEntity(this.getGrilla());                
                this.getCamposFormulaByCeldaTarget().clear();    
                super.getFiltro().setTipoFormula(null);
                this.updateTipoFormulaCombo(this.getTipoFormulaCombo(), super.getFiltro().getTipoFormula()); 
            }
            celdaParent.setFormula(null);
            this.setCeldaTarget(celdaParent);
            this.setBarraFormula(null);            
            super.agregarSuccesMessage("Se ha eliminado la Fórmula Vertical correctamente");
            AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPanelGroupLayoutTablaFormula()); 
            AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPanelGroupLayoutBarraFormula());
        } catch (Exception e) {
            super.agregarErrorMessage("Se ha producido un error al eliminar la Fórmula");
            logger.error("Error al eliminar la fórmula ",e);
        }
    }
            
    /**
     * Elimina una formula dinamica vertical
     * @param event
     */
    public void eliminarFormulaDinamicaHorizontal(ActionEvent event){        
        try {
            final Celda celdaParent = (Celda)event.getComponent().getAttributes().get("celda");            
            //obtiene la lista de celdas pertenecientes al parent Horizontal
            final List<Celda> celdaChildList = select(this.celdaMapToList() ,having(on(Celda.class).getChildHorizontal(), equalTo(celdaParent.getParentHorizontal())));
            //eliminamos la referencia del parent.            
            celdaParent.setParentHorizontal(null);
            for(Celda celdaChild : celdaChildList){
                celdaChild.setChildHorizontal(null);
            }                        
            this.getFacade().getCeldaService().deleteFormulaDinamica(celdaParent, celdaChildList);
            this.desMarcarCeldasSelecionadasByTarget();
            if(!this.tieneFormulaDinamica()){
                this.getGrilla().setTipoFormula(null);
                this.getFacade().getGrillaService().mergeEntity(this.getGrilla());
                this.getCamposFormulaByCeldaTarget().clear();               
                super.getFiltro().setTipoFormula(null);
                this.updateTipoFormulaCombo(this.getTipoFormulaCombo(), super.getFiltro().getTipoFormula());            
            }
            this.setCeldaTarget(celdaParent);
            this.setBarraFormula(null);            
            super.agregarSuccesMessage("Se ha eliminado la Fórmula Horizontal correctamente");
            AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPanelGroupLayoutTablaFormula()); 
            AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPanelGroupLayoutBarraFormula());
        } catch (Exception e) {
            super.agregarErrorMessage("Se ha producido un error al eliminar la fórmula");
            logger.error("Error al eliminar la formula ",e);
        }
    }
    
    public void confirmEliminarFormulaDinamicaAll(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.ok)) {
            this.eliminarFormulaDinamicaAll(null);
            this.celdaTarget = null;
        }        
    }
    
    public void eliminarFormulaDinamicaAll(ActionEvent event){ 
        List<Celda> celdaList = new ArrayList<Celda>();
        try{
            for(final Columna columna : this.getGrillaVO().getColumnas()){
                for(final Celda celda : columna.getCeldaList()){
                    celda.setParentHorizontal(null);                
                    celda.setParentVertical(null);
                    celda.setChildHorizontal(null);
                    celda.setChildVertical(null);
                    celdaList.add(celda);
                }
            }            
            this.getGrilla().setTipoFormula(null);
            this.getCamposFormulaByCeldaTarget().clear();
            super.getFiltro().setTipoFormula(null);
            this.updateTipoFormulaCombo(this.getTipoFormulaCombo(), super.getFiltro().getTipoFormula());
            this.setBarraFormula(null); 
            this.getFacade().getCeldaService().persistFormulaEstaticaList(this.getGrilla(), celdaList);
            AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPanelGroupLayoutTablaFormula()); 
            AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPanelGroupLayoutBarraFormula());
            super.agregarSuccesMessage("Se han eliminado correctamente todas las Fórmulas Dinámicas de la Grilla");
        } catch (Exception e) {
            super.agregarErrorMessage("Se ha producido un error al eliminar la fórmula");
            logger.error("Error al eliminar la formula ",e);
        }
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
            this.getFacade().getCeldaService().persistFormulaEstaticaList(this.getGrilla(), celdaList);            
            super.agregarSuccesMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_exito_guardar_formula"), this.getFormulaMap().size()));
            this.getFormulaMap().clear();
            this.setCountFormulasSinGrabar(this.getFormulaMap().size()); 
            AdfFacesContext.getCurrentInstance().addPartialTarget(this.getContadorFormulasUnsavedOutput());
            AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPanelGroupLayoutTablaFormula());                        
        } catch (Exception e) {
            super.agregarErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_error_guardar_formula"));
            logger.error(PropertyManager.getInstance().getMessage("general_mensaje_error_guardar_formula"), e);            
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
            //List<Celda> celdaTargetList = new ArrayList<Celda>();
            for(Map.Entry<Celda, String> entry : this.getFormulaMap().entrySet()) {
                Celda celda = entry.getKey();
                celda.setFormula(entry.getValue());
                //celdaTargetList.add(celda);
                if(celda.getFormula().contains(SIGNO_SUMA) || celda.getFormula().contains(PUNTO_COMA)){
                    formulaDinamicaMap.put(celda, this.getCeldaTotalListByTarget(celda));
                }else{
                    formulaDinamicaMap.put(celda, this.getCeldaListByTarget(celda));
                }
                celdaTarget.setFormula(null);
            }
            /*for(Celda celdaTarget : celdaTargetList){   
                if(celdaTarget.getFormula().contains(SIGNO_SUMA) || celdaTarget.getFormula().contains(PUNTO_COMA)){
                    formulaDinamicaMap.put(celdaTarget, this.getCeldaTotalListByTarget(celdaTarget));
                }else{
                    formulaDinamicaMap.put(celdaTarget, this.getCeldaListByTarget(celdaTarget));
                }
                celdaTarget.setFormula(null);
            }*/   
            logger.info(formulaDinamicaMap);            
            this.getGrilla().setTipoFormula(Grilla.TIPO_GRILLA_DINAMICA);            
            this.getFacade().getCeldaService().persistFormulaDinamicaMap(this.getGrilla(), formulaDinamicaMap);
            super.agregarSuccesMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_exito_guardar_formula"), this.getFormulaMap().size()));
            this.getFormulaMap().clear();
            this.setCountFormulasSinGrabar(this.getFormulaMap().size()); 
            AdfFacesContext.getCurrentInstance().addPartialTarget(this.getContadorFormulasUnsavedOutput());
            AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPanelGroupLayoutTablaFormula());                        
        } catch (Exception e) {
            super.agregarErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_error_guardar_formula"));
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
    
    private void validaReferenciaCiclicaFormula(){
        for(Map.Entry<Celda, String> entry : this.getFormulaMap().entrySet()) {
            final String cellKey = this.formatCellKey(entry.getKey());
            if(entry.getValue().contains(cellKey)){
                super.agregarWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_celda_destino_existe_en_formula"), cellKey));             
                break;                
            }
        }
    }
    
    
    
    
    

    /**
     * Actualiza el componente de barra de formula desde un evento onkeyup
     * @param event
     */
    public void updateBarraFormula(ClientEvent event){        
        String formula = Util.getString(event.getParameters().get("barraFormula"), "");
        logger.info("---> "+formula);        
        this.getFormulaMap().put(this.getCeldaTarget(), formula);
        this.setFormula(formula);
        this.desMarcarCeldasSelecionadasByTarget();
        if(super.getFiltro().getTipoFormula().equals(Grilla.TIPO_GRILLA_ESTATICA)){
            this.marcarCeldasSelecionadasByCadenaFormulaEstatica();
        }
        else if(super.getFiltro().getTipoFormula().equals(Grilla.TIPO_GRILLA_DINAMICA)){
            this.marcarCeldasSelecionadasByCadenaFormulaDinamica();
        }
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getBarraFormulaOutput());
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPanelGroupLayoutBarraFormula()); 
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPanelGroupLayoutTablaFormula());
    }

    /**
     * ActionListener que ejecuuta la busqueda de una grilla para editar formulas
     * @param event
     */
    public void buscarEstructuraGrilla(ActionEvent event){
        
        final Estructura estructura = (Estructura)event.getComponent().getAttributes().get("estructura");
        final Catalogo catalogo = (Catalogo)event.getComponent().getAttributes().get("catalogo");
        final Version version = (Version)event.getComponent().getAttributes().get("version");
        this.setBarraFormula(null);
        this.setCeldaTarget(null);
        super.getFiltro().setTipoFormula(null);
        this.getCamposFormulaByCeldaTarget().clear();
        this.getFormulaMap().clear();
        this.setCountFormulasSinGrabar(this.getFormulaMap().size());
        this.desMarcarCeldasSelecionadasByTarget();
        this.updateTipoFormulaCombo(this.getTipoFormulaCombo(), super.getFiltro().getTipoFormula());  
        
        
        if (tablaCatalogoTree != null && tablaCatalogoTree.getDisclosedRowKeys()!=null ){
                tablaCatalogoTree.getDisclosedRowKeys().clear();////esta linea fue agregada para resolver NoRowAvailableException. MGC
        }
        
        try {
                    if (!version.isDesagregado()){
                    
                        
                            this.setCatalogo(catalogo);
                            this.setEstructura(estructura);
                            this.setGrilla(this.getFacade().getGrillaService().findGrillaById(estructura.getIdEstructura()));
                            this.setGrillaVO(this.getFacade().getEstructuraService().getGrillaVO(this.getGrilla(), Boolean.FALSE));
                            super.getFiltro().setTipoFormula(this.getGrilla().getTipoFormula());
                            this.buildCeldaMap();
                            this.setRenderGrilla(Boolean.TRUE);
                            this.updateTipoFormulaCombo(this.getTipoFormulaCombo(), super.getFiltro().getTipoFormula());              
                    } else {
                            this.setRenderGrilla(Boolean.FALSE);
                            super.agregarWarnMessage(PropertyManager.getInstance().getMessage("general_mensaje_cuadro_formula_version_desagregada"));
                        }
                        
                    
                        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getBarraFormulaOutput());
                        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPanelGroupLayoutBarraFormula()); 
                        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPanelGroupLayoutTablaFormula());
            
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            super.agregarErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_nota_get_catalogo_error"));
        } 
     
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

    /**
     * metodo listener que responde al boton de seleccionar la celda de destino para la formula estatica.
     * @param event
     */
    public void fijarCeldaEstaticaTarget(ActionEvent event){        
        final Celda celdaTarget = (Celda)event.getComponent().getAttributes().get("celdaTarget");
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
        System.out.println(this.getGrilla());
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPanelGroupLayoutBarraFormula());
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPanelGroupLayoutTablaFormula());
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
                    super.agregarWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_celda_destino_existe_en_formula"), cellKey));             
                    return;
                }
                celda.setSelectedByFormula(Boolean.TRUE);                            
            }else{
                super.agregarErrorMessage("La celda "+cellKey+" no existe en el cuadro");
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

    /**
     * Metodo listener del boton agregar o quitar celda a una formula estatica.
     * @param event
     */
    public void selectCeldaEstatica(ActionEvent event){
        RichCommandImageLink check = null;       
        check = (RichCommandImageLink)event.getComponent();
        
        final Celda celda = (Celda)event.getComponent().getAttributes().get("celda");
        final boolean celdaSelected = (Boolean)event.getComponent().getAttributes().get("selected");
        final String parOrdenado = CORCHETE_IZQUIERDO.concat(""+celda.getIdColumna()).concat(COMA).concat(""+celda.getIdFila()).concat(CORCHETE_DERECHO).concat(PUNTO_COMA); 
        final String formulaSaved = this.getFormulaMap().get(this.getCeldaTarget());
        this.setFormula(formulaSaved == null ? this.getCeldaTarget().getFormula() : formulaSaved);                         
        if(EqualsBuilder.reflectionEquals(this.getCeldaTarget(), celda, true)){
            super.agregarWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_celda_destino_existe_en_formula"), parOrdenado));             
            return;
        }
        if(this.getCeldaTarget() == null && celdaSelected){
            super.agregarWarnMessage(PropertyManager.getInstance().getMessage("general_mensaje_formula_sin_celda_destino"));                        
            AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPanelGroupLayoutTablaFormula());
            return;
        }
        if(this.getFormula() == null){
            this.setFormula("");
        }
        if(celdaSelected){
            if(this.getFormula().contains(parOrdenado)){
                super.agregarWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_celda_existe_en_formula"), parOrdenado));
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
        
        //AdfFacesContext.getCurrentInstance().addPartialTarget(this.getBarraFormulaOutput());
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getContadorFormulasUnsavedOutput());
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPanelGroupLayoutBarraFormula());
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPanelGroupLayoutTablaFormula());
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
    
    /************************************************
     * FORMULA DINAMICA                             *
     ************************************************/
    
    /**
     * metodo listener del evento ValueChangeEvent del combo tipo de formula.
     * @param valueChangeEvent
     */    
    public void onChangeTipoFormula(ValueChangeEvent valueChangeEvent) {
       
        RichSelectOneChoice combo = (RichSelectOneChoice)valueChangeEvent.getSource();
        
        Long tipoGrillaNewValue = valueChangeEvent.getNewValue()==null?null:(Long)valueChangeEvent.getNewValue();
        Long tipoGrillaOldValue = valueChangeEvent.getOldValue()==null?null:(Long)valueChangeEvent.getOldValue();
        
        if(tipoGrillaOldValue != null && this.getCountFormulasSinGrabar() > 0){
            
            super.agregarWarnMessage("Tiene Fórmulas sin guardar, para no almacenar presione cancelar.");
            this.updateTipoFormulaCombo(combo,tipoGrillaOldValue);
            return;
            
        }
        
        if(tipoGrillaNewValue != null && tipoGrillaNewValue.equals(Grilla.TIPO_GRILLA_ESTATICA)){
            
            if(tieneFormulaDinamica()){
                
                super.agregarWarnMessage("El cuadro posee Fórmulas Dinámicas configuradas, antes de configurar una Fórmula Estática debe eliminar las otras Fórmulas.");
                this.updateTipoFormulaCombo(combo,tipoGrillaOldValue);                
                return;
            }
                
        }else if(tipoGrillaNewValue != null && tipoGrillaNewValue.equals(Grilla.TIPO_GRILLA_DINAMICA)){
            
            if(tieneFormulaEstatica()){
                
                super.agregarWarnMessage("El cuadro posee Fórmulas Estáticas configuradas, antes de configurar una Fórmula Dinámica debe eliminar las otras Fórmulas.");
                super.getFiltro().setTipoFormula(tipoGrillaOldValue);
                this.updateTipoFormulaCombo(combo, tipoGrillaOldValue);
                return;
                
            }
        }
        
        this.celdaTarget = null;
        
        super.getFiltro().setTipoFormula(tipoGrillaNewValue);
        
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPanelGroupLayoutTablaFormula());
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPanelGroupLayoutBarraFormula());
    }
    

    /**
     * Actualiza el valor del combo tipo de formula en la vista
     * @param combo
     * @param tipoFormula
     */
    private void updateTipoFormulaCombo(RichSelectOneChoice combo, Long tipoFormula){
        combo.setValue(tipoFormula);
        AdfFacesContext.getCurrentInstance().addPartialTarget(combo);
    }


    /**
     * fija la celda de destino en la cual sera configurada una formula dinamica
     * @param event
     */
    public void fijarCeldaDinamicaTarget(ActionEvent event){        
        final Celda celdaTarget = (Celda)event.getComponent().getAttributes().get("celdaTarget");                            
        this.setCeldaTarget(celdaTarget);
        final String formulaSaved = this.getFormulaMap().get(this.getCeldaTarget());        
        this.setFormula(formulaSaved == null ? this.getCeldaTarget().getFormula() : formulaSaved);             
        if(this.getFormula() == null){
            this.setFormula("");
        }        
        this.setBarraFormula(null);
        this.setBarraFormula(this.getFormula());         
        this.desMarcarCeldasSelecionadasByTarget();
        if(celdaTarget.getParentVertical() != null || celdaTarget.getParentHorizontal() != null){
            this.marcarCeldasSelecionadasByCeldaTarget();
        }else{
            this.marcarCeldasSelecionadasByCadenaFormulaDinamica();
        }
        System.out.println(super.getFiltro());
        this.getCamposFormulaByCeldaTarget().clear();       
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPanelGroupLayoutBarraFormula());
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPanelGroupLayoutTablaFormula());        
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
    
    private boolean isCeldaTotal(final Celda celda) {
        if (celda.isEsNumero() &&
            (celda.getTipoCelda().getIdTipoCelda().equals(TipoCeldaEnum.SUBTOTAL.getKey()) || celda.getTipoCelda().getIdTipoCelda().equals(TipoCeldaEnum.TOTAL.getKey()))) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
    
    /**
     * Metodo listener del boton agregar o quitar celda a una formula estatica.
     * @param event
     */
    public void selectCeldaDinamica(ActionEvent event){
        RichCommandImageLink check = null;       
        check = (RichCommandImageLink)event.getComponent();
        
        final Celda celda = (Celda)event.getComponent().getAttributes().get("celda");
        final boolean celdaSelected = (Boolean)event.getComponent().getAttributes().get("selected");
        final String parOrdenado = CORCHETE_IZQUIERDO.concat(""+celda.getIdColumna()).concat(COMA).concat(""+celda.getIdFila()).concat(CORCHETE_DERECHO); 
        final String formulaSaved = this.getFormulaMap().get(this.getCeldaTarget());        
        this.setFormula(formulaSaved == null ? this.getCeldaTarget().getFormula() : formulaSaved);                         
        if(EqualsBuilder.reflectionEquals(this.getCeldaTarget(), celda, true)){
            super.agregarWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_celda_destino_existe_en_formula"), parOrdenado));             
            return;
        }        
        if(this.getCeldaTarget() == null && celdaSelected){
            super.agregarWarnMessage(PropertyManager.getInstance().getMessage("general_mensaje_formula_sin_celda_destino"));                        
            AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPanelGroupLayoutTablaFormula());
            return;
        }
        if(this.getFormula() == null){
            this.setFormula("");
        }
        if(celdaSelected){            
            if(!this.isCeldaTotal(celda)){
                if(this.getCamposFormulaByCeldaTarget().size() >= 2){               
                    super.agregarWarnMessage(PropertyManager.getInstance().getMessage("general_mensaje_rango_formado_formula"));
                    return;
                }
                if (this.getFormula().contains(parOrdenado)) {
                    super.agregarWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_celda_existe_en_formula"), parOrdenado));
                    return;
                }
                //RDV Valida que se seleccione en misma columan
                if(!validarOperacionDinamica(this.getCeldaTarget(), celda)){
                    return; 
                }
                if(!this.getFormula().equals("")){
                    this.setFormula(this.getFormula().concat(DOS_PUNTOS)); 
                }
                this.setFormula(this.getFormula().concat(parOrdenado));                        
                this.getCamposFormulaByCeldaTarget().put(celda, Boolean.TRUE);
                celda.setSelectedByFormula(Boolean.TRUE);
                this.setBarraFormula(this.getFormula());
                this.updateFormulaMap(this.getCeldaTarget(), this.getFormula());  
            } else if(this.isCeldaTotal(celda)){
                
                final String parOrdenado2 = CORCHETE_IZQUIERDO.concat(""+celda.getIdColumna()).concat(COMA).concat(""+celda.getIdFila()).concat(CORCHETE_DERECHO).concat(PUNTO_COMA);
                
                if(!validarOperacionDinamica(this.getCeldaTarget(), celda)){
                    return; 
                }
                if(this.getFormula().contains(parOrdenado2)){
                    super.agregarWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_celda_existe_en_formula"), parOrdenado));
                    return;
                }               
                this.setFormula(this.getFormula().concat(SIGNO_SUMA));
                this.setFormula(this.getFormula().concat(parOrdenado2));            
                this.updateCamposFormulaByCeldaTargetMap(celda, Boolean.TRUE);            
                celda.setSelectedByFormula(Boolean.TRUE);                            
                this.setBarraFormula(this.getFormula());
                this.updateFormulaMap(this.getCeldaTarget(), this.getFormula());  
                
            }
            
        } else {   
            if(!this.isCeldaTotal(celda)){
                if (this.getFormula().contains(DOS_PUNTOS.concat(parOrdenado))) {
                    this.setFormula(this.getFormula().replace(DOS_PUNTOS.concat(parOrdenado), ""));
                }            
                if (this.getFormula().contains(parOrdenado)) {
                    this.setFormula(this.getFormula().replace(parOrdenado, ""));
                }            
                this.getCamposFormulaByCeldaTarget().remove(celda);
                celda.setSelectedByFormula(Boolean.FALSE);
                this.setBarraFormula(this.getFormula());
                this.updateFormulaMap(this.getCeldaTarget(), this.getFormula());
                            
                if(this.getCamposFormulaByCeldaTarget().size() == 2 || this.getCamposFormulaByCeldaTarget().size() == 1){
                    this.desMarcarCeldasSelecionadasByTarget();
                    this.getCamposFormulaByCeldaTarget().clear();
                    this.setFormula("");
                    this.setBarraFormula(this.getFormula());
                    this.getFormulaMap().remove(this.getCeldaTarget());                 
                }
            }
            else if (this.isCeldaTotal(celda)) {
                final String parOrdenado2 = CORCHETE_IZQUIERDO.concat("" + celda.getIdColumna()).concat(COMA).concat("" +celda.getIdFila()).concat(CORCHETE_DERECHO).concat(PUNTO_COMA);
                if (this.getFormula().contains(SIGNO_SUMA.concat(parOrdenado2))) {
                    this.setFormula(this.getFormula().replace(SIGNO_SUMA.concat(parOrdenado2), ""));
                }
                if (this.getFormula().contains(SIGNO_RESTA.concat(parOrdenado2))) {
                    this.setFormula(this.getFormula().replace(SIGNO_RESTA.concat(parOrdenado2), ""));
                }
                if (this.getFormula().contains(parOrdenado2)) {
                    this.setFormula(this.getFormula().replace(parOrdenado2, ""));
                }
                this.updateCamposFormulaByCeldaTargetMap(celda, Boolean.FALSE);
                celda.setSelectedByFormula(Boolean.FALSE);
                this.setBarraFormula(this.getFormula());
                this.updateFormulaMap(this.getCeldaTarget(), this.getFormula());
            }

        }


        this.setCountFormulasSinGrabar(this.getFormulaMap().size());
        this.updateCamposFormulaMap(this.getCeldaTarget(), this.getCamposFormulaByCeldaTarget());
        
        if((this.getCamposFormulaByCeldaTarget().size() == 2) && (!this.isCeldaTotal(celda))){
            this.marcarCeldasSelecionadasByCadenaFormulaDinamica();
        }
        
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getContadorFormulasUnsavedOutput());
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPanelGroupLayoutBarraFormula());
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPanelGroupLayoutTablaFormula());
    }
    
    private boolean validarOperacionDinamica(final Celda celdaParent, final Celda celdaChild){
        //RDV Valida que se seleccione en misma columan
        if(!celdaParent.getIdColumna().equals(celdaChild.getIdColumna()) && !celdaParent.getIdFila().equals(celdaChild.getIdFila())){
            super.agregarWarnMessage("El rango debe ser en la misma columna o fila");
            return false; 
        }
        
        return true;
    }
    
    /**
     * Marca como seleccionado en la grilla de formula dinamica 
     * las celdas seleccionadas en la formula de destino
     */
    private void marcarCeldasSelecionadasByCadenaFormulaDinamica(){
        logger.info("marcando celdas para formula dinamica");
        List<Celda> celdaRangoList = new ArrayList<Celda>();
        if(this.getFormulaMap().get(this.getCeldaTarget()) == null && this.getCeldaTarget().getFormula() == null){
            logger.info("no existe formula para la celda de resultado");
            return;
        }
        final String formulaSaved = this.getFormulaMap().get(this.getCeldaTarget());        
        StringTokenizer cellKeys = new StringTokenizer(formulaSaved == null ? this.getCeldaTarget().getFormula() : formulaSaved, DOS_PUNTOS);   
        while (cellKeys.hasMoreTokens()) {
            String cellKey = cellKeys.nextToken()
                             .replace(DOS_PUNTOS, "");
                             
            logger.info(cellKey);   
            if (this.getCeldaMap().containsKey(cellKey)) {
                Celda celda = this.getCeldaMap().get(cellKey);
                if(EqualsBuilder.reflectionEquals(this.getCeldaTarget(), celda, true)){
                    super.agregarWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_celda_destino_existe_en_formula"), cellKey));             
                    return;
                }
                celdaRangoList.add(celda);
            }else{
                super.agregarErrorMessage("La celda "+cellKey+" no existe en el cuadro");
                return;                            
            }
        }
    
        if(!celdaRangoList.isEmpty()){
            List<Celda> celdaList = this.celdaMapToList(); 
            List<Celda> celdasByColumna = new ArrayList<Celda>();
            List<Celda> celdasByFila = new ArrayList<Celda>();
            if(celdaRangoList.get(0).getIdColumna().equals(celdaRangoList.get(1).getIdColumna())){
                //seleccion vertical
                celdasByColumna = select(celdaList ,having(on(Celda.class).getIdColumna(), equalTo(celdaRangoList.get(0).getIdColumna()))
                                         .and(having(on(Celda.class).getIdFila(), greaterThanOrEqualTo(celdaRangoList.get(0).getIdFila())))
                                         .and(having(on(Celda.class).getIdFila(), lessThanOrEqualTo(celdaRangoList.get(1).getIdFila()))));
                if(celdasByColumna.isEmpty()){
                    celdasByColumna = select(celdaList ,having(on(Celda.class).getIdColumna(), equalTo(celdaRangoList.get(0).getIdColumna()))
                                             .and(having(on(Celda.class).getIdFila(), lessThanOrEqualTo(celdaRangoList.get(0).getIdFila())))
                                             .and(having(on(Celda.class).getIdFila(), greaterThanOrEqualTo(celdaRangoList.get(1).getIdFila()))));    
                }
                for(Celda celda : celdasByColumna){                                        
                    celda.setSelectedByFormula(Boolean.TRUE);  
                }
            }
            else if(celdaRangoList.get(0).getIdFila().equals(celdaRangoList.get(1).getIdFila())){
                //seleccion horizontal 
                celdasByFila = select(celdaList ,having(on(Celda.class).getIdFila(), equalTo(celdaRangoList.get(0).getIdFila()))
                                      .and(having(on(Celda.class).getIdColumna(), greaterThanOrEqualTo(celdaRangoList.get(0).getIdColumna())))
                                      .and(having(on(Celda.class).getIdColumna(), lessThanOrEqualTo(celdaRangoList.get(1).getIdColumna()))));
                if(celdasByFila.isEmpty()){
                    celdasByFila = select(celdaList ,having(on(Celda.class).getIdFila(), equalTo(celdaRangoList.get(0).getIdFila()))
                                          .and(having(on(Celda.class).getIdColumna(), lessThanOrEqualTo(celdaRangoList.get(0).getIdColumna())))
                                          .and(having(on(Celda.class).getIdColumna(), greaterThanOrEqualTo(celdaRangoList.get(1).getIdColumna()))));
                }
                for (Celda celda : celdasByFila) {
                    celda.setSelectedByFormula(Boolean.TRUE);
                }
            }
        }
    }
    
    /**
     * Marca como seleccionado en la grilla de formula dinamica 
     * las celdas seleccionadas en la formula de destino
     */
    private void marcarCeldasSelecionadasByCeldaTarget(){
        logger.info("marcando celdas para formula dinamica desde el parent");
        List<Celda> celdas = null;
        StringBuffer formula = new StringBuffer();
        if(this.getCeldaTarget().getParentVertical() != null){
            celdas = select(this.celdaMapToList() ,having(on(Celda.class).getChildVertical(), equalTo(this.getCeldaTarget().getParentVertical())));
            final Long columna = maxFrom(celdas).getIdColumna();
            final Long filaMin = minFrom(celdas).getIdFila();
            final Long filaMax = maxFrom(celdas).getIdFila();
            formula.append(CORCHETE_IZQUIERDO)
                   .append(columna).append(COMA).append(filaMin).append(CORCHETE_DERECHO)
                   .append(DOS_PUNTOS)
                   .append(CORCHETE_IZQUIERDO)
                   .append(columna).append(COMA).append(filaMax).append(CORCHETE_DERECHO);
            this.setFormula(formula.toString());
            this.setBarraFormula(this.getFormula());
            AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPanelGroupLayoutBarraFormula());
        }
        else if(this.getCeldaTarget().getParentHorizontal() != null){
            celdas = select(this.celdaMapToList() ,having(on(Celda.class).getChildHorizontal(), equalTo(this.getCeldaTarget().getParentHorizontal())));
            final Long fila = maxFrom(celdas).getIdFila();
            final Long columnaMin = minFrom(celdas).getIdColumna();
            final Long columnaMax = maxFrom(celdas).getIdColumna();
            formula.append(CORCHETE_IZQUIERDO)
                   .append(columnaMin).append(COMA).append(fila).append(CORCHETE_DERECHO)
                   .append(DOS_PUNTOS)
                   .append(CORCHETE_IZQUIERDO)
                   .append(columnaMax).append(COMA).append(fila).append(CORCHETE_DERECHO);
            this.setFormula(formula.toString());
            this.setBarraFormula(this.getFormula());
            AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPanelGroupLayoutBarraFormula());
        }
        
        if(celdas != null && celdas.size() > 0){
            for(Celda celda : celdas){
                celda.setSelectedByFormula(Boolean.TRUE);
            }
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
    
    /**
     * renderisa en la pagina un componente popup segun el parametro popupId 
     * @param popupId
     */
    public static void showPopup(String popupId) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExtendedRenderKitService extendedRenderKitService = Service.getRenderKitService(context, ExtendedRenderKitService.class);
        extendedRenderKitService.addScript(context, "AdfPage.PAGE.findComponent('" + popupId + "').show();");
    }
    
    /**
     * Obtiene una referencia del managed bean
     * SoporteBackingBean
     * @return
     */
    public SoporteBackingBean getSoporteBackingBean() {
        if(soporteBackingBean == null){
            soporteBackingBean = BeanUtil.findBean(SoporteBackingBean.BEAN_NAME);
        }
        return soporteBackingBean;
    }
    
    //catalogo TreeModel
    
    /**
     * Genera una estructura de tipo TreeModel
     * para Tabla de Catalogo con sus estructuras asociadas
     */
    public void getCatalogoTreeModel() throws Exception {        
        catalogoRoot = new ArrayList<TreeItem>();
        ArrayList<TreeItem> catalogoChildren = null;
        TreeItem nodo = null;
        for(VersionPeriodo versionPeriodo : getComponenteBackingBean().getPeriodoCatalogoList()){
            nodo = new TreeItem();
            nodo.setParent(Boolean.TRUE);
            nodo.setObject(versionPeriodo);
            catalogoRoot.add(nodo);
            catalogoChildren = new ArrayList<TreeItem>();
            for(Estructura estructura : super.getFacade().getEstructuraService().getEstructuraByVersion(versionPeriodo.getVersion(), false)){            
                catalogoChildren.add(new TreeItem(estructura));
            }
            nodo.setChildren(catalogoChildren); 
        }                    
        this.setListInstance(catalogoRoot);
    }
        
    public void setCatalogoModel(TreeModel catalogoModel) {
        this.catalogoModel = catalogoModel;
    }

    public TreeModel getCatalogoModel() throws Exception {        
        //this.getCatalogoTreeModel();
        catalogoModel = new ChildPropertyTreeModel(catalogoInstance, "children");                  
        return catalogoModel;
    }
    
    public void setListInstance(List instance) {
        this.catalogoInstance = instance;
        catalogoModel = null;
    }
    
    //Fin metodos para TreeModel
    
    public void onChangeTipoGrillaListener(ItemEvent itemEvent) {
       logger.info(itemEvent.getComponent());
    }
    
    public void setCatalogoList(List<Catalogo> catalogoList) {
        this.catalogoList = catalogoList;
    }

    public List<Catalogo> getCatalogoList() {
        return catalogoList;
    }

    public void setCatalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
    }

    public Catalogo getCatalogo() {
        return catalogo;
    }

    public void setEstructuraList(List<Estructura> estructuraList) {
        this.estructuraList = estructuraList;
    }

    public List<Estructura> getEstructuraList() {
        return estructuraList;
    }

    public void setGrilla(Grilla grilla) {
        this.grilla = grilla;
    }

    public Grilla getGrilla() {
        return grilla;
    }

    public void setGrillaVO(GrillaVO grillaVO) {
        this.grillaVO = grillaVO;
    }

    public GrillaVO getGrillaVO() {
        return grillaVO;
    }

    public void setEstructura(Estructura estructura) {
        this.estructura = estructura;
    }

    public Estructura getEstructura() {
        return estructura;
    }

    public void setRenderGrilla(boolean renderGrilla) {
        this.renderGrilla = renderGrilla;
    }

    public boolean isRenderGrilla() {
        return renderGrilla;
    }

    public void setTablaCatalogoTree(RichTreeTable tablaCatalogoTree) {
        this.tablaCatalogoTree = tablaCatalogoTree;
    }

    public RichTreeTable getTablaCatalogoTree() {
        return tablaCatalogoTree;
    }

    public void setRenderedCatalogoTree(boolean renderedCatalogoTree) {
        this.renderedCatalogoTree = renderedCatalogoTree;
    }

    public boolean isRenderedCatalogoTree() {
        return renderedCatalogoTree;
    }

    public void setCeldaTarget(Celda celdaTarget) {
        this.celdaTarget = celdaTarget;
    }

    public Celda getCeldaTarget() {
        return celdaTarget;
    }


    public void setFormulaTargetOutput(RichInputText formulaTargetOutput) {
        this.formulaTargetOutput = formulaTargetOutput;
    }

    public RichInputText getFormulaTargetOutput() {
        return formulaTargetOutput;
    }
    
    public void setBarraFormulaOutput(RichInputText barraFormulaOutput) {
        this.barraFormulaOutput = barraFormulaOutput;
    }

    public RichInputText getBarraFormulaOutput() {
        return barraFormulaOutput;
    }

    public void setBarraFormula(String barraFormula) {
        this.barraFormula = barraFormula;
    }

    public String getBarraFormula() {
        return barraFormula;
    }

    public void setCadenaFormula(StringBuilder cadenaFormula) {
        this.cadenaFormula = cadenaFormula;
    }

    public StringBuilder getCadenaFormula() {
        return cadenaFormula;
    }

    public void setGrillaFormulasTable(RichTable grillaFormulasTable) {
        this.grillaFormulasTable = grillaFormulasTable;
    }

    public RichTable getGrillaFormulasTable() {
        return grillaFormulasTable;
    }

    public void setFormulaMap(Map<Celda, String> formulaMap) {
        this.formulaMap = formulaMap;
    }

    public Map<Celda, String> getFormulaMap() {
        return formulaMap;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getFormula() {
        return formula;
    }

    public void setCountFormulasSinGrabar(int countFormulasSinGrabar) {
        this.countFormulasSinGrabar = countFormulasSinGrabar;
    }

    public int getCountFormulasSinGrabar() {
        return countFormulasSinGrabar;
    }

    public void setContadorFormulasUnsavedOutput(RichInputText contadorFormulasUnsavedOutput) {
        this.contadorFormulasUnsavedOutput = contadorFormulasUnsavedOutput;
    }

    public RichInputText getContadorFormulasUnsavedOutput() {
        return contadorFormulasUnsavedOutput;
    }

    public void setCamposFormula(Map<Celda, Map<Celda, Boolean>> camposFormula) {
        this.camposFormula = camposFormula;
    }

    public Map<Celda, Map<Celda, Boolean>> getCamposFormula() {
        return camposFormula;
    }

    public void setCamposFormulaByCeldaTarget(Map<Celda, Boolean> camposFormulaByCeldaTarget) {
        this.camposFormulaByCeldaTarget = camposFormulaByCeldaTarget;
    }

    public Map<Celda, Boolean> getCamposFormulaByCeldaTarget() {
        return camposFormulaByCeldaTarget;
    }

    public void setCamposFormulaByCeldaDisplay(Map<Celda, Boolean> camposFormulaByCeldaDisplay) {
        this.camposFormulaByCeldaDisplay = camposFormulaByCeldaDisplay;
    }

    public Map<Celda, Boolean> getCamposFormulaByCeldaDisplay() {
        return camposFormulaByCeldaDisplay;
    }
    
    public void setPanelGroupLayoutTablaFormula(RichPanelGroupLayout panelGroupLayoutTablaFormula) {
        this.panelGroupLayoutTablaFormula = panelGroupLayoutTablaFormula;
    }

    public RichPanelGroupLayout getPanelGroupLayoutTablaFormula() {
        return panelGroupLayoutTablaFormula;
    }

    public void setCamposFormulaPersistent(Map<Celda, Map<Celda, Boolean>> camposFormulaPersistent) {
        this.camposFormulaPersistent = camposFormulaPersistent;
    }

    public Map<Celda, Map<Celda, Boolean>> getCamposFormulaPersistent() {
        return camposFormulaPersistent;
    }

    public void setCeldaMap(Map<String, Celda> celdaMap) {
        this.celdaMap = celdaMap;
    }

    public Map<String, Celda> getCeldaMap() {
        return celdaMap;
    }

    public void setPanelGroupLayoutBarraFormula(RichPanelGroupLayout panelGroupLayoutBarraFormula) {
        this.panelGroupLayoutBarraFormula = panelGroupLayoutBarraFormula;
    }

    public RichPanelGroupLayout getPanelGroupLayoutBarraFormula() {
        return panelGroupLayoutBarraFormula;
    }

    public void setCeldaList(List<Celda> celdaList) {
        this.celdaList = celdaList;
    }

    public List<Celda> getCeldaList() {
        return celdaList;
    }

    public void setTipoFormulaCombo(RichSelectOneChoice tipoFormulaCombo) {
        this.tipoFormulaCombo = tipoFormulaCombo;
    }

    public RichSelectOneChoice getTipoFormulaCombo() {
        return tipoFormulaCombo;
    }

    public void setCeldaDinamicaTarget(Celda celdaDinamicaTarget) {
        this.celdaDinamicaTarget = celdaDinamicaTarget;
    }

    public Celda getCeldaDinamicaTarget() {
        return celdaDinamicaTarget;
    }
}
