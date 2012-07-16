package cl.mdr.ifrs.modules.configuracion.mb;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.util.GeneradorDisenoHelper;
import cl.mdr.ifrs.ejb.entity.Grilla;
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
	
	/*atributos utilizados para la upload de archivo*/
    private transient UploadedFile uploadedFile;
    private GrillaVO grillaVO;
    
    
	/**
	 * Carga mediante Excel
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
            final Grilla grilla = super.getFacadeService().getCargadorEstructuraService().getGrillaByExcel(this.getUploadedFile().getInputstream()); 
            this.setGrillaVO(super.getFacadeService().getEstructuraService().getGrillaVO(grilla, Boolean.FALSE));
            this.getGrillaVO().setCeldaList(GeneradorDisenoHelper.builHtmlGrilla(grilla.getColumnaList()));

        } catch (CargaGrillaExcelException e) {
            logger.error("error al procesar archivo excel ",e);
            addErrorMessage(e.getMessage());
        
        } catch (Exception e) {
            logger.error("error al procesar archivo excel ",e);
            addErrorMessage("Error al procesar el archivo");
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

}
