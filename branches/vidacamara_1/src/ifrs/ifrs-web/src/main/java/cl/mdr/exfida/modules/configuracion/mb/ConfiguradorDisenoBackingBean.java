package cl.mdr.exfida.modules.configuracion.mb;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;
import org.hamcrest.Matchers;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.util.GeneradorDisenoHelper;
import cl.mdr.ifrs.ejb.common.TipoCeldaEnum;
import cl.mdr.ifrs.ejb.common.TipoDatoEnum;
import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.ifrs.ejb.entity.Columna;
import cl.mdr.ifrs.ejb.entity.Estructura;
import cl.mdr.ifrs.ejb.entity.Grilla;
import cl.mdr.ifrs.ejb.entity.TipoCelda;
import cl.mdr.ifrs.ejb.entity.TipoDato;
import cl.mdr.ifrs.exceptions.CargaGrillaExcelException;
import cl.mdr.ifrs.model.EstructuraModel;
import cl.mdr.ifrs.vo.GrillaVO;

/**
 * Clase ManagedBean que controla la funcionalidad de la pagina de configuracion de diseño de estructuras.
 * @author rreyes
 * @link http://cl.linkedin.com/in/rreyesc
 * 
 */
@ManagedBean(name = "configuradorDisenoBackingBean")
@ViewScoped
public class ConfiguradorDisenoBackingBean extends AbstractBackingBean implements Serializable {
	private transient Logger logger = Logger.getLogger(this.getClass().getName());  
	private static final long serialVersionUID = -1303743504274782576L;
	public static final String BEAN_NAME = "configuradorDisenoBackingBean"; 
		
    private GrillaVO grillaVO;
    private Grilla grilla;
    private Long idTipoCelda;
    private Long idTipoDato;    
    private TipoCelda tipoCeldaColumna;
    private TipoDato tipoDatoColumna;
    private TipoCelda tipoCeldaCelda;
    private TipoDato tipoDatoCelda;
    private TipoCelda tipoCeldaFila;
    private TipoDato tipoDatoFila;
    private Celda celda;
    private Columna columna;
    private Long fila;
    private String tituloGrilla;
    private String tituloHtml;
    private List<TipoDato> tipoDatoList;
    private List<TipoDato> tipoDatoFilteredList;
    
    /*atributos utilizados para upload de archivo*/
    private transient UploadedFile uploadedFile;
    
    private boolean renderEditarGrilla;
    private boolean renderEditarHtml;
    private boolean renderEditarTexto;
    
    /*seleccionar estructura para edicion*/
    private transient DataTable disenoEstructuraTable;
    private Estructura estructuraSelected;
    
    private Map<Long, EstructuraModel> estructuraModelMap;
    
    
    /**
     * Accion encargada de procesar el archivo 
     * @return
     */
    public void cargaArchivoListener(FileUploadEvent event) {                
        try {
        	this.setUploadedFile(event.getFile());
            if(this.getUploadedFile() == null){
                super.addErrorMessage("Seleccione o Actualice el archivo que desea cargar");
                return;
            }
            if(this.getUploadedFile().getInputstream()  == null){
            	super.addErrorMessage("Seleccione o Actualice el archivo que desea cargar");
                return;
            }            
            this.setGrilla(super.getFacadeService().getCargadorEstructuraService().getGrillaByExcel(this.getUploadedFile().getInputstream()));
            this.setGrillaVO(super.getFacadeService().getEstructuraService().getGrillaVO(this.getGrilla(), Boolean.FALSE));
            //construye la tabla para ser renderizada hacia la vista de edicion.
            this.getGrillaVO().setCeldaList(GeneradorDisenoHelper.builHtmlGrilla(this.getGrilla().getColumnaList()));           
        	this.getGrillaVO().setAgrupaciones(GeneradorDisenoHelper.crearAgrupadorHTMLVO(GeneradorDisenoHelper.getAgrupacionesByColumnaList(this.getGrilla().getColumnaList())));
            //agrego la lista de columnas para su edicion
            this.getEstructuraModelByEstructuraSelected().setColumnas(this.getGrilla().getColumnaList());
            
        } catch (CargaGrillaExcelException e) {
            logger.error("error al procesar archivo excel ",e);
            super.addErrorMessage(e.getMessage());
        
        } catch (Exception e) {
            logger.error("error al procesar archivo excel ",e);
            super.addErrorMessage("Error al procesar el archivo");
        }
       
    }
    
    public void guardarTituloGrillaListener(){
    	this.getEstructuraModelByEstructuraSelected().setTituloGrilla(this.getTituloGrilla());
    }
    
    public void guardarTituloHtmlListener(){
    	this.getEstructuraModelByEstructuraSelected().getHtml().setTitulo(this.getTituloHtml());
    }
    
    /**
     * prepara los datos para editar una columna
     * @param event
     */
    public void prepareEditarColumnaAction(ActionEvent event){
    	final Columna columna = (Columna) event.getComponent().getAttributes().get("columna");
    	this.setTipoDatoFilteredList(null);
    	this.setColumna(columna);
    	this.setTipoCeldaColumna(null);
    	this.setTipoDatoColumna(null);
    	logger.info("editando columna "+columna.getTituloColumna());    	
    	super.displayPopUp("editColumnDialog", "fGV1");
    }
    
    /**
     * procesa la edicion de una columna
     * @param event
     */
    public void editarColumnaAction(ActionEvent event){    	    	
    	logger.info("editando columna "+this.getColumna().getTituloColumna());
    	logger.info("tipo celda "+this.getIdTipoCelda());
    	logger.info("tipo dato "+this.getIdTipoDato());
    	if(this.getTipoCeldaColumna() != null && this.getTipoDatoColumna() != null){
	    	Iterator<List<Celda>> iter = this.getGrillaVO().getCeldaList().iterator();
	    	while(iter.hasNext()){
	    	    Iterator<Celda> celdaIter = iter.next().iterator();
	    	    while(celdaIter.hasNext()){
	    	    	 Celda celda = celdaIter.next();
	    	    	 if(celda.getIdColumna().equals(this.getColumna().getIdColumna())){    	    		
	    	    		 celda.setTipoCelda(this.getTipoCeldaColumna());    	    		   	    		 
	    	    		 celda.setTipoDato(this.getTipoDatoColumna());    		 
	    	    	 }    	         
	    	     }
	    	}
    	}
    }
        
    /**
     * Prepara los datos para editar una celda
     * @param event
     */
    public void prepareEditarCeldaAction(ActionEvent event){
    	final Celda celda = (Celda) event.getComponent().getAttributes().get("celda");
    	logger.info("editando celda "+celda.getValor());
    	this.buildTipoDatoFilteredList(celda.getTipoCelda());
    	this.setCelda(celda);
    	this.setTipoCeldaCelda(celda.getTipoCelda());
    	this.setTipoDatoCelda(celda.getTipoDato());
    	super.displayPopUp("editCellDialog", "fGV1");
    }
    
    /**
     * Procesa la edicion de una Celda
     * @param event
     */
    public void editarCeldaAction(ActionEvent event){
    	Iterator<List<Celda>> iter = this.getGrillaVO().getCeldaList().iterator();
    	while(iter.hasNext()){
    	    Iterator<Celda> celdaIter = iter.next().iterator();
    	    while(celdaIter.hasNext()){
    	    	 Celda celda = celdaIter.next();
    	    	 if( celda.getIdColumna().equals(this.getCelda().getIdColumna()) && celda.getIdFila().equals(this.getCelda().getIdFila()) ){
    	    		 celda.setTipoCelda(this.getTipoCeldaCelda());
    	    		 celda.setTipoDato(this.getTipoDatoCelda());    		 
    	    	 }    	         
    	     }
    	}
    }
        
    /**
     * Prepara los datos para la edición de una fila
     * @param event
     */
    public void prepareEditarFilaAction(ActionEvent event){
    	final Long fila  = (Long) event.getComponent().getAttributes().get("fila");
    	this.setTipoDatoFilteredList(null);
    	logger.info("editando fila "+fila);
    	this.setFila(fila);
    	this.setTipoCeldaFila(null);
    	this.setTipoDatoFila(null);
    	super.displayPopUp("editRowDialog", "fGV1");
    }
    
    /**
     * Procesa la edición de una fila.
     * @param event
     */
    public void editarFilaAction(ActionEvent event){
    	Iterator<List<Celda>> iter = this.getGrillaVO().getCeldaList().iterator();
    	while(iter.hasNext()){
    	    Iterator<Celda> celdaIter = iter.next().iterator();
    	    while(celdaIter.hasNext()){
    	    	 Celda celda = celdaIter.next();
    	    	 if( celda.getIdFila().equals(this.getFila()) ){
    	    		 celda.setTipoCelda(this.getTipoCeldaFila());
    	    		 celda.setTipoDato(this.getTipoDatoFila());    		 
    	    	 }    	         
    	     }
    	}
    }    
    
    /**
     * Habilita la opcion para crear y editar una estructura de tipo grilla
     * @param event
     */
    public void editarGrillaAction(ActionEvent event) {    	
        final Estructura estructura = (Estructura)this.getDisenoEstructuraTable().getRowData();
        if(estructura.getOrden()==null){
            return;
        }
        this.setEstructuraSelected(estructura);
        this.getEstructuraModelByEstructuraSelected().setColumnas(this.getEstructuraSelected().getGrillaVO().getColumnas());
        this.setTituloGrilla(null);
        //construye la tabla para ser renderizada hacia la vista de edicion.
        this.setGrillaVO(this.getEstructuraSelected().getGrillaVO());
        this.getGrillaVO().setCeldaList(GeneradorDisenoHelper.builHtmlGrilla(this.getEstructuraSelected().getGrillaVO().getColumnas()));           
    	this.getGrillaVO().setAgrupaciones(GeneradorDisenoHelper.crearAgrupadorHTMLVO(GeneradorDisenoHelper.getAgrupacionesByColumnaList(this.getEstructuraSelected().getGrillaVO().getColumnas())));
    	this.setRenderEditarGrilla(Boolean.TRUE);
    	this.setRenderEditarHtml(Boolean.FALSE);
    	this.setRenderEditarTexto(Boolean.FALSE);
    	
    }
    
    /**
     * Habilita la opcion para crear y editar una estructura de tipo Html
     * @param event
     */
    public void editarHtmlAction(ActionEvent event) {    	
        final Estructura estructura = (Estructura)this.getDisenoEstructuraTable().getRowData();
        if(estructura.getOrden()==null){
            return;
        }
        this.setEstructuraSelected(estructura);
        this.getEstructuraModelByEstructuraSelected().setHtml(this.getEstructuraSelected().getHtml());
        this.setTituloHtml(null);
        this.setRenderEditarGrilla(Boolean.FALSE);
    	this.setRenderEditarHtml(Boolean.TRUE);
    	this.setRenderEditarTexto(Boolean.FALSE);
    }
    
    /**
     * Habilita la opcion para crear y editar una estructura de tipo Texto
     * @param event
     */
    public void editarTextoAction(ActionEvent event) {    	
        final Estructura estructura = (Estructura)this.getDisenoEstructuraTable().getRowData();
        if(estructura.getOrden()==null){
            return;
        }
        this.setEstructuraSelected(estructura);
        this.getEstructuraModelByEstructuraSelected().setTexto(this.getEstructuraSelected().getTexto());
        this.setRenderEditarGrilla(Boolean.FALSE);
    	this.setRenderEditarHtml(Boolean.FALSE);
    	this.setRenderEditarTexto(Boolean.TRUE);
    }
    
        
    /**
     * Obtiene desde el EstructuraModelMap el objeto EstructuraModel que esta siendo editado.
     * @return EstructuraModel
     */
    public EstructuraModel getEstructuraModelByEstructuraSelected(){
        if(this.getEstructuraModelMap().containsKey(this.getEstructuraSelected().getOrden())){
            return this.getEstructuraModelMap().get(this.getEstructuraSelected().getOrden());    
        }
        return new EstructuraModel();
    }
    
    /**
     * Metodo que filtra los tipos de dato segun el tipo de celda
     * creando un combo anidado para la configuracion de las celdas.
     * @param event
     */
//    public void changeTipoDatoByTipoCeldaListener(ValueChangeEvent event){
//    	try{
//    		final TipoCelda tipoCelda = ((TipoCelda) event.getNewValue());
//    		this.buildTipoDatoFilteredList(tipoCelda);
//    		super.getFacesContext().renderResponse();    
//    	}catch (Exception e) {
//			logger.error(e);
//			super.addErrorMessage("Ha ocurrido un error al filtrar los Tipos de dato");
//		}    	 
//    }
    
    /**
     * Metodo que filtra los tipos de dato segun el tipo de celda
     * creando un combo anidado para la configuracion de las celdas.
     * para cuadro de edicion de celda
     */
    public void changeTipoDatoByTipoCeldaForEditCeldaListener(){
    	try{    
    		Thread.sleep(100);
    		this.buildTipoDatoFilteredList(tipoCeldaCelda);    		  
    	}catch (Exception e) {
			logger.error(e);
			super.addErrorMessage("Ha ocurrido un error al filtrar los Tipos de dato");
		}    	 
    }
    
    /**
     * Metodo que filtra los tipos de dato segun el tipo de celda
     * creando un combo anidado para la configuracion de las celdas.
     * para cuadro de edicion de fila
     */
    public void changeTipoDatoByTipoCeldaForEditFilaListener(){
    	try{    		
    		Thread.sleep(100);
    		this.buildTipoDatoFilteredList(tipoCeldaFila);    		  
    	}catch (Exception e) {
			logger.error(e);
			super.addErrorMessage("Ha ocurrido un error al filtrar los Tipos de dato");
		}    	 
    }
    
    /**
     * Metodo que filtra los tipos de dato segun el tipo de celda
     * creando un combo anidado para la configuracion de las celdas.
     * para cuadro de edicion de columna
     */
    public void changeTipoDatoByTipoCeldaForEditColumnaListener(){
    	try{   
    		Thread.sleep(100);
    		this.buildTipoDatoFilteredList(tipoCeldaColumna);    		  
    	}catch (Exception e) {
			logger.error(e);
			super.addErrorMessage("Ha ocurrido un error al filtrar los Tipos de dato");
		}    	 
    }
                
    /**
     * Metodo que construye una lista de List<TipoDato> filtrada segun el tipo de celda.
     * @param tipoCelda
     * @return
     */
    private List<TipoDato> buildTipoDatoFilteredList(final TipoCelda tipoCelda){
    	tipoDatoFilteredList = new ArrayList<TipoDato>();    	
    	if(tipoCelda.getIdTipoCelda().equals(TipoCeldaEnum.NUMERO.getKey())){
    		tipoDatoFilteredList =  select(this.getTipoDatoList() , 
									having(on(TipoDato.class).getIdTipoDato(),  
											Matchers.isIn(Arrays.asList(new Long[] {TipoDatoEnum.ENTERO.getKey(), TipoDatoEnum.DECIMAL.getKey()}))) );				
    	}
    	else if(tipoCelda.getIdTipoCelda().equals(TipoCeldaEnum.TEXTO.getKey())){
    		tipoDatoFilteredList =  select(this.getTipoDatoList() , 
									having(on(TipoDato.class).getIdTipoDato(),  
											Matchers.isIn(Arrays.asList(new Long[] {TipoDatoEnum.TEXTO.getKey()}))) );    		
    	}
		else if(tipoCelda.getIdTipoCelda().equals(TipoCeldaEnum.TEXTO_EDITABLE.getKey())){
			tipoDatoFilteredList =  select(this.getTipoDatoList() , 
									having(on(TipoDato.class).getIdTipoDato(),  
											Matchers.isIn(Arrays.asList(new Long[] {TipoDatoEnum.TEXTO.getKey(), TipoDatoEnum.FECHA.getKey()}))) );		    		
		}
		else if(tipoCelda.getIdTipoCelda().equals(TipoCeldaEnum.TITULO.getKey())){
			tipoDatoFilteredList =  select(this.getTipoDatoList() , 
									having(on(TipoDato.class).getIdTipoDato(),  
											Matchers.isIn(Arrays.asList(new Long[] {TipoDatoEnum.TEXTO.getKey()}))) );		    		
		}
		else if(tipoCelda.getIdTipoCelda().equals(TipoCeldaEnum.RUT.getKey())){
			tipoDatoFilteredList =  select(this.getTipoDatoList() , 
									having(on(TipoDato.class).getIdTipoDato(),  
											Matchers.isIn(Arrays.asList(new Long[] {TipoDatoEnum.TEXTO.getKey()}))) );		    		
		}
		else if(tipoCelda.getIdTipoCelda().equals(TipoCeldaEnum.TOTAL.getKey())){
			tipoDatoFilteredList =  select(this.getTipoDatoList() , 
									having(on(TipoDato.class).getIdTipoDato(),  
											Matchers.isIn(Arrays.asList(new Long[] {TipoDatoEnum.ENTERO.getKey(), TipoDatoEnum.DECIMAL.getKey()}))) );			
		}
		else if(tipoCelda.getIdTipoCelda().equals(TipoCeldaEnum.SUBTOTAL.getKey())){
			tipoDatoFilteredList =  select(this.getTipoDatoList() , 
									having(on(TipoDato.class).getIdTipoDato(),  
											Matchers.isIn(Arrays.asList(new Long[] {TipoDatoEnum.ENTERO.getKey(), TipoDatoEnum.DECIMAL.getKey()}))) );			
		}
		else if(tipoCelda.getIdTipoCelda().equals(TipoCeldaEnum.LINK.getKey())){
			tipoDatoFilteredList =  select(this.getTipoDatoList() , 
									having(on(TipoDato.class).getIdTipoDato(),  
											Matchers.isIn(Arrays.asList(new Long[] {TipoDatoEnum.TEXTO.getKey()}))) );			
		}
    	return tipoDatoFilteredList;
    }
    
    public void clearTabConfiguracion(){
    	logger.info("limpiando tab de configuración");
    	this.setRenderEditarGrilla(Boolean.FALSE);
    	this.setRenderEditarHtml(Boolean.FALSE);
    	this.setRenderEditarTexto(Boolean.FALSE);	
    }
    
         
	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}


	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}


	public GrillaVO getGrillaVO() {
		return grillaVO;
	}


	public void setGrillaVO(GrillaVO grillaVO) {
		this.grillaVO = grillaVO;
	}
	
	public Grilla getGrilla() {
		return grilla;
	}

	public void setGrilla(Grilla grilla) {
		this.grilla = grilla;
	}

	public Long getIdTipoCelda() {
		return idTipoCelda;
	}

	public void setIdTipoCelda(Long idTipoCelda) {
		this.idTipoCelda = idTipoCelda;
	}

	public Long getIdTipoDato() {
		return idTipoDato;
	}

	public void setIdTipoDato(Long idTipoDato) {
		this.idTipoDato = idTipoDato;
	}
	
	public Celda getCelda() {
		return celda;
	}

	public void setCelda(Celda celda) {
		this.celda = celda;
	}

	public Columna getColumna() {
		return columna;
	}

	public void setColumna(Columna columna) {
		this.columna = columna;
	}

	public TipoCelda getTipoCeldaColumna() {
		return tipoCeldaColumna;
	}

	public void setTipoCeldaColumna(TipoCelda tipoCeldaColumna) {
		this.tipoCeldaColumna = tipoCeldaColumna;
	}

	public TipoDato getTipoDatoColumna() {
		return tipoDatoColumna;
	}

	public void setTipoDatoColumna(TipoDato tipoDatoColumna) {
		this.tipoDatoColumna = tipoDatoColumna;
	}

	public TipoCelda getTipoCeldaCelda() {
		return tipoCeldaCelda;
	}

	public void setTipoCeldaCelda(TipoCelda tipoCeldaCelda) {
		this.tipoCeldaCelda = tipoCeldaCelda;
	}

	public TipoDato getTipoDatoCelda() {
		return tipoDatoCelda;
	}

	public void setTipoDatoCelda(TipoDato tipoDatoCelda) {
		this.tipoDatoCelda = tipoDatoCelda;
	}

	public TipoCelda getTipoCeldaFila() {
		return tipoCeldaFila;
	}

	public void setTipoCeldaFila(TipoCelda tipoCeldaFila) {
		this.tipoCeldaFila = tipoCeldaFila;
	}

	public TipoDato getTipoDatoFila() {
		return tipoDatoFila;
	}

	public void setTipoDatoFila(TipoDato tipoDatoFila) {
		this.tipoDatoFila = tipoDatoFila;
	}

	public Long getFila() {
		return fila;
	}

	public void setFila(Long fila) {
		this.fila = fila;
	}

	public boolean isRenderEditarGrilla() {
		return renderEditarGrilla;
	}

	public void setRenderEditarGrilla(boolean renderEditarGrilla) {
		this.renderEditarGrilla = renderEditarGrilla;
	}

	public boolean isRenderEditarHtml() {
		return renderEditarHtml;
	}

	public void setRenderEditarHtml(boolean renderEditarHtml) {
		this.renderEditarHtml = renderEditarHtml;
	}

	public boolean isRenderEditarTexto() {
		return renderEditarTexto;
	}

	public void setRenderEditarTexto(boolean renderEditarTexto) {
		this.renderEditarTexto = renderEditarTexto;
	}

	public DataTable getDisenoEstructuraTable() {
		return disenoEstructuraTable;
	}

	public void setDisenoEstructuraTable(DataTable disenoEstructuraTable) {
		this.disenoEstructuraTable = disenoEstructuraTable;
	}

	public Estructura getEstructuraSelected() {
		return estructuraSelected;
	}

	public void setEstructuraSelected(Estructura estructuraSelected) {
		this.estructuraSelected = estructuraSelected;
	}
	
	public Map<Long, EstructuraModel> getEstructuraModelMap() {
		if(estructuraModelMap == null){
			estructuraModelMap = new LinkedHashMap<Long, EstructuraModel>();
		}
		return estructuraModelMap;
	}

	public void setEstructuraModelMap(Map<Long, EstructuraModel> estructuraModelMap) {
		this.estructuraModelMap = estructuraModelMap;
	}

	public String getTituloGrilla() {
		return tituloGrilla;
	}

	public void setTituloGrilla(String tituloGrilla) {
		this.tituloGrilla = tituloGrilla;
	}

	public String getTituloHtml() {
		return tituloHtml;
	}

	public void setTituloHtml(String tituloHtml) {
		this.tituloHtml = tituloHtml;
	}

	public List<TipoDato> getTipoDatoList() {
		if(tipoDatoList==null){
            tipoDatoList = super.getFacadeService().getMantenedoresTipoService().findAllTipoDato();
        }
        return tipoDatoList;		
	}

	public void setTipoDatoList(List<TipoDato> tipoDatoList) {
		this.tipoDatoList = tipoDatoList;
	}

	public List<TipoDato> getTipoDatoFilteredList() {
		return tipoDatoFilteredList;
	}

	public void setTipoDatoFilteredList(List<TipoDato> tipoDatoFilteredList) {
		this.tipoDatoFilteredList = tipoDatoFilteredList;
	}

}