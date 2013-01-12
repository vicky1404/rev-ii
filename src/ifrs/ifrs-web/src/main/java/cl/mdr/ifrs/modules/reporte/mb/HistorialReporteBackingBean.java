/**
 * 
 */
package cl.mdr.ifrs.modules.reporte.mb;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.mb.FiltroBackingBean;
import cl.mdr.ifrs.ejb.cross.MD5CheckSum;
import cl.mdr.ifrs.ejb.entity.HistorialReporte;
import cl.mdr.ifrs.ejb.entity.PeriodoEmpresa;

/**
 * @author rodrigo.reyes
 *
 */
@ManagedBean
@ViewScoped
public class HistorialReporteBackingBean extends AbstractBackingBean implements Serializable {
	private static final long serialVersionUID = 8302112088102881721L;
	private transient Logger logger = Logger.getLogger(HistorialReporteBackingBean.class);
	
	public HistorialReporteBackingBean() {
		super();
	}
	    
    private transient DataTable historialReporteTable;
    private transient UploadedFile uploadedFile;
    private StreamedContent downloadedFile;  
    private List<HistorialReporte> historialReporteList;
    private HistorialReporte historialReporte;
    private boolean renderHistorialReportes;
    
    /**
     * @return
     */
    public String buscarAction(){
        logger.info("buscando historial de generación de reportes");  
        if(!isSelectedEmpresa()){
    		return null;
    	}
        FiltroBackingBean filtroPaso = super.getFiltroBackingBean();
        filtroPaso.getPeriodoEmpresa().setPeriodo(null);
        try {                         
        	Long periodo = null;
            periodo = Long.valueOf(super.getFiltroBackingBean().getAnio() + super.getFiltroBackingBean().getMes());
            PeriodoEmpresa periodoEmpresa = super.getFacadeService().getPeriodoService().getPeriodoEmpresaById(periodo, getFiltroBackingBean().getEmpresa().getIdRut());
            if(periodoEmpresa == null){
            	super.addWarnMessage(MessageFormat.format("No se encontro información referente al período {0}-{1}",super.getFiltroBackingBean().getAnio(), super.getFiltroBackingBean().getMes()));                        
            	this.setHistorialReporteList(null); 
                this.setRenderHistorialReportes(Boolean.FALSE);
                return null;
            }
            super.getFiltroBackingBean().setPeriodoEmpresa(periodoEmpresa);                                         
            this.setHistorialReporteList(super.getFacadeService().getReporteDocxService().findHistorialReporteByPeriodo(super.getFiltroBackingBean().getPeriodoEmpresa().getPeriodo(), super.getFiltroBackingBean().getEmpresa()));
            this.setRenderHistorialReportes(Boolean.TRUE);
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            super.addErrorMessage("Se ha producido un error al buscar el Historial de generación de Reportes Word");
        }
        return null;
    }
    
    public void cargaArchivoListener(FileUploadEvent event) { 
    	this.setUploadedFile(event.getFile());
    	this.validarArchivo();
    }
        
    /**
     * Action Listener encargado de la descarga del archivo
     */
    public void fileDownloadListener() { 
    	try {
	    	final HistorialReporte historialReporte = (HistorialReporte)this.getHistorialReporteTable().getRowData();
	        InputStream stream = new ByteArrayInputStream(historialReporte.getDocumento());
	        downloadedFile = new DefaultStreamedContent(stream, "application/vnd.openxmlformats-officedocument.wordprocessingml.document", historialReporte.getNombreArchivo());
    	} catch (Exception e) {
        	super.addErrorMessage("Se ha producido un error al descargar el Archivo "+historialReporte.getNombreArchivo());
            logger.error(e.getMessage(), e);
        }
        
    }  
  
    
    public void prepareValidarArchivo(ActionEvent event){
    	this.setHistorialReporte((HistorialReporte)event.getComponent().getAttributes().get("historialReporte"));
    	super.displayPopUp("dialogValidaReporte", "tabla_historial_reporte");
    }
    
    /**
     * Metodo que valida la suma de verificación md5 del archivo desargado 
     * contra el archivo subido a la aplicacion.
     * @param event
     */
    public void validarArchivo(){        
        try {
            if(this.historialReporte == null){
            	super.addErrorMessage("Seleccione el archivo a validar desde el Historial de Reportes");
            	return;
            }
            if(this.getUploadedFile() == null){
            	super.addErrorMessage("Seleccione el archivo a cargar y luego proceda a validar el reporte");
                return;
            }
            if(this.getUploadedFile().getInputstream() == null){
            	super.addErrorMessage("Seleccione el archivo a cargar y luego proceda a validar el reporte");
                return;
            }            
            String checkSumDoc = historialReporte.getCheckSumExportacion();
            String chechSumDocUploaded = MD5CheckSum.getMD5Checksum(this.getUploadedFile().getInputstream());
            if(!checkSumDoc.equals(chechSumDocUploaded)){
            	super.addErrorMessage("El contenido original del archivo "+historialReporte.getNombreArchivo()+" ha sido modificado");
            }else{
                super.addInfoMessage("El contenido original del archivo "+historialReporte.getNombreArchivo()+" no ha sido modificado");
            }
            //this.getRichInputFile().resetValue();
        } catch (Exception e) {
        	super.addErrorMessage("Se ha producido un error al validar el archivo "+historialReporte.getNombreArchivo());
            logger.error(e.getMessage(), e);
        }
    }

	/**
	 * @return the historialReporteList
	 */
	public List<HistorialReporte> getHistorialReporteList() {
		return historialReporteList;
	}

	/**
	 * @param historialReporteList the historialReporteList to set
	 */
	public void setHistorialReporteList(List<HistorialReporte> historialReporteList) {
		this.historialReporteList = historialReporteList;
	}

	/**
	 * @return the renderHistorialReportes
	 */
	public boolean isRenderHistorialReportes() {
		return renderHistorialReportes;
	}

	/**
	 * @param renderHistorialReportes the renderHistorialReportes to set
	 */
	public void setRenderHistorialReportes(boolean renderHistorialReportes) {
		this.renderHistorialReportes = renderHistorialReportes;
	}

	/**
	 * @return the uploadedFile
	 */
	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	/**
	 * @param uploadedFile the uploadedFile to set
	 */
	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	/**
	 * @return the historialReporteTable
	 */
	public DataTable getHistorialReporteTable() {
		return historialReporteTable;
	}

	/**
	 * @param historialReporteTable the historialReporteTable to set
	 */
	public void setHistorialReporteTable(DataTable historialReporteTable) {
		this.historialReporteTable = historialReporteTable;
	}

	public HistorialReporte getHistorialReporte() {
		return historialReporte;
	}

	public void setHistorialReporte(HistorialReporte historialReporte) {
		this.historialReporte = historialReporte;
	}

	public StreamedContent getDownloadedFile() {
		return downloadedFile;
	}

	public void setDownloadedFile(StreamedContent downloadedFile) {
		this.downloadedFile = downloadedFile;
	}
	
	

}
