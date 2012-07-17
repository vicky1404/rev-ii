package cl.mdr.ifrs.modules.configuracion.mb;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.util.GeneradorDisenoHelper;
import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.ifrs.ejb.entity.Columna;
import cl.mdr.ifrs.ejb.entity.Grilla;
import cl.mdr.ifrs.ejb.entity.TipoCelda;
import cl.mdr.ifrs.ejb.entity.TipoDato;
import cl.mdr.ifrs.exceptions.CargaGrillaExcelException;
import cl.mdr.ifrs.vo.GrillaVO;

/**
 * Clase ManagedBean que controla la funcionalidad de la pagina de configuracion de diseño de estructuras.
 * @author rdiaz & rreyes 
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
    
    /*atributos utilizados para la upload de archivo*/
    private transient UploadedFile uploadedFile;
    
    
    /**
	 * Carga mediante Libro 
	 * Excel
	 * 
	 **/

    /**
     * accion encargada de procesar el archivo 
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
            this.getGrillaVO().setCeldaList(GeneradorDisenoHelper.builHtmlGrilla(this.getGrilla().getColumnaList()));

        } catch (CargaGrillaExcelException e) {
            logger.error("error al procesar archivo excel ",e);
            addErrorMessage(e.getMessage());
        
        } catch (Exception e) {
            logger.error("error al procesar archivo excel ",e);
            addErrorMessage("Error al procesar el archivo");
        }
       
    }
    
    /**
     * @param event
     */
    public void editarColumnaAction(ActionEvent event){    	
    	Columna columna = (Columna) event.getComponent().getAttributes().get("columna");
    	logger.info("editando columna "+columna.getTituloColumna());
    	logger.info("tipo celda "+this.getIdTipoCelda());
    	logger.info("tipo dato "+this.getIdTipoDato());
    	Iterator<List<Celda>> iter = this.getGrillaVO().getCeldaList().iterator();
    	while(iter.hasNext()){
    	    Iterator<Celda> celdaIter = iter.next().iterator();
    	    while(celdaIter.hasNext()){
    	    	 Celda celda = celdaIter.next();
    	    	 if(celda.getIdColumna().equals(columna.getIdColumna())){
    	    		 celda.setTipoCelda(new TipoCelda(this.getIdTipoCelda()));
    	    		 celda.setTipoDato(new TipoDato(this.getIdTipoDato()));    		 
    	    	 }    	         
    	     }
    	}
    }
    
    public void onChangeTipoCeldaListener(ValueChangeEvent valueChangeEvent){
    	if(valueChangeEvent.getNewValue() != null){
    		this.setIdTipoCelda(null);    	
    		this.setIdTipoCelda((Long) valueChangeEvent.getNewValue());
    		logger.info("change tipo celda "+this.getIdTipoCelda());
    	}
    }
    
    public void onChangeTipoDatoListener(ValueChangeEvent valueChangeEvent){
    	if(valueChangeEvent.getNewValue() != null){
    		this.setIdTipoDato(null);	
    		this.setIdTipoDato((Long) valueChangeEvent.getNewValue());    		
        	logger.info("change tipo dato "+this.getIdTipoDato());
    	}
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

}
