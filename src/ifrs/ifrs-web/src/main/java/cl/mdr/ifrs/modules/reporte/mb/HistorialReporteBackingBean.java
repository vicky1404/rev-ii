/**
 * 
 */
package cl.mdr.ifrs.modules.reporte.mb;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.mb.FiltroBackingBean;
import cl.mdr.ifrs.cross.util.PropertyManager;
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
		// TODO Auto-generated constructor stub
	}
	
    private static final String WORD_EXTENSION = "docx";
    /*private transient RichTable historialReporteTable;
    private transient RichInputFile richInputFile;
    private transient UploadedFile uploadedFile;*/
    private transient DataTable historialReporteTable;
    private transient UploadedFile uploadedFile;
    private List<HistorialReporte> historialReporteList;
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
            this.setHistorialReporteList(super.getFacadeService().getReporteDocxService().findHistorialReporteByPeriodo(super.getFiltroBackingBean().getPeriodoEmpresa().getPeriodo()));
            this.setRenderHistorialReportes(Boolean.TRUE);
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            super.addErrorMessage("Se ha producido un error al buscar el Historial de generación de Reportes Word");
        }
        return null;
    }
    
    public void cargaArchivoListener(FileUploadEvent event) { 
    	this.setUploadedFile(event.getFile());
    }
    /**
     * @param context
     * @param out
     * @throws IOException
     */
    public void download(FacesContext context, OutputStream out) throws IOException {   
        final HistorialReporte historialReporte = (HistorialReporte)this.getHistorialReporteTable().getRowData();
        InputStream inputStream;
        byte[] b;
        try {
            inputStream = new ByteArrayInputStream(historialReporte.getDocumento());
            int n;
            while ((n = inputStream.available()) > 0) {
                b = new byte[n];
                int result = inputStream.read(b);
                out.write(b, 0, b.length);
                if (result == -1)
                    break;
            }
        } catch (Exception e) {
        	super.addErrorMessage("Se ha producido un error al descargar el Archivo "+historialReporte.getNombreArchivo());
            logger.error(e.getMessage(), e);
        }
        out.flush();
    }
    
    /**
     * Metodo que valida la suma de verificación md5 del archivo desargado 
     * contra el archivo subido a la aplicacion.
     * @param event
     */
    public void validarArchivo(ActionEvent event){
        HistorialReporte historialReporte = null;
        try {
            historialReporte = (HistorialReporte)event.getComponent().getAttributes().get("historialReporte");
            if(this.getUploadedFile() == null){
            	super.addErrorMessage("Seleccione el archivo a cargar y luego proceda a validar el reporte");
                return;
            }
            if(this.getUploadedFile().getInputstream() == null){
            	super.addErrorMessage("Seleccione el archivo a cargar y luego proceda a validar el reporte");
                return;
            }
            if(historialReporte == null){
            	super.addErrorMessage("Seleccione el reporte que desea validar");
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
	
	

}
