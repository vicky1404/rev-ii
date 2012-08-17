package cl.bicevida.revelaciones.mb;

import cl.bicevida.revelaciones.common.filtros.Filtro;
import cl.bicevida.revelaciones.common.mb.SoporteBackingBean;
import cl.bicevida.revelaciones.common.util.PropertyManager;
import cl.bicevida.revelaciones.ejb.cross.MD5CheckSum;
import cl.bicevida.revelaciones.ejb.entity.HistorialReporte;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import java.text.MessageFormat;

import java.util.List;

import javax.ejb.EJBException;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import javax.faces.event.ActionEvent;

import javax.persistence.NoResultException;

import oracle.adf.view.rich.component.rich.data.RichTable;

import oracle.adf.view.rich.component.rich.input.RichInputFile;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.model.UploadedFile;

 /**
  * Clase BackingBean que controla las funcionalidades del historial de generacion de reportes docx
  * ademas de la validacion de los reportes generados con checksum MD5
  * @author rodrigo.reyes@bicevida.cl
  * @link http://cl.linkedin.com/in/rreyesc
  */
public class HistorialReporteBackingBean extends SoporteBackingBean implements Serializable {
    private transient Logger logger = Logger.getLogger(HistorialReporteBackingBean.class);

    @SuppressWarnings("compatibility:-9203226846955017799")
    private static final long serialVersionUID = 1L;
    private static final String WORD_EXTENSION = "docx";
    private transient RichTable historialReporteTable;
    private transient RichInputFile richInputFile;
    private transient UploadedFile uploadedFile;
    private List<HistorialReporte> historialReporteList;
    private boolean renderHistorialReportes;

    public HistorialReporteBackingBean() {
    
    }

    /**
     * @return
     */
    public String buscarAction(){
        logger.info("buscando historial de generación de reportes");     
        Filtro filtroPaso = getFiltro();
        filtroPaso.getPeriodo().setPeriodo(null);
        try {             
            try{
                Long periodo = Long.valueOf(filtroPaso.getPeriodo().getAnioPeriodo().concat(filtroPaso.getPeriodo().getMesPeriodo()));
                super.getFiltro().setPeriodo(getFacade().getMantenedoresTipoService().findByPeriodo(periodo));
            }catch(NoResultException e){
                agregarWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("periodo_busqueda_sin_resultado_periodo"), super.getFiltro().getPeriodo().getAnioPeriodo(), super.getFiltro().getPeriodo().getMesPeriodo()));                            
                this.setHistorialReporteList(null); 
                this.setRenderHistorialReportes(Boolean.FALSE);
                return null;                    
            }catch(EJBException e){
                agregarWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("periodo_busqueda_sin_resultado_periodo"), super.getFiltro().getPeriodo().getAnioPeriodo(), super.getFiltro().getPeriodo().getMesPeriodo()));                        
                this.setHistorialReporteList(null); 
                this.setRenderHistorialReportes(Boolean.FALSE);
                return null;
            }
            this.setHistorialReporteList(super.getFacade().getReporteDocxService().findHistorialReporteByPeriodo(super.getFiltro().getPeriodo()));
            this.setRenderHistorialReportes(Boolean.TRUE);
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            agregarErrorMessage("Se ha producido un error al buscar el Historial de generación de Reportes");
        }
        return null;
    }

    /**
     * @param context
     * @param out
     * @throws IOException
     */
    public void download(FacesContext context, OutputStream out) throws IOException {   
        final HistorialReporte historialReporte = (HistorialReporte)this.getHistorialReporteTable().getSelectedRowData();
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
            agregarErrorMessage("Se ha producido un error al descargar el Archivo "+historialReporte.getNombreArchivo());
            logger.error(e.getMessage(), e);
        }
        out.flush();
    }

    /**
     * @param event
     */
    public void validarArchivo(ActionEvent event){
        HistorialReporte historialReporte = null;
        try {
            historialReporte = (HistorialReporte)event.getComponent().getAttributes().get("historialReporte");
            if(this.getUploadedFile() == null){
                agregarErrorMessage("Seleccione el archivo a cargar y luego proceda a validar el reporte");
                return;
            }
            if(this.getUploadedFile().getInputStream() == null){
                agregarErrorMessage("Seleccione el archivo a cargar y luego proceda a validar el reporte");
                return;
            }
            if(historialReporte == null){
                agregarErrorMessage("Seleccione el reporte que desea validar");
                return;
            }
            String checkSumDoc = historialReporte.getCheckSumExportacion();
            String chechSumDocUploaded = MD5CheckSum.getMD5Checksum(this.getUploadedFile().getInputStream());
            if(!checkSumDoc.equals(chechSumDocUploaded)){
                agregarErrorMessage("El contenido original del archivo "+historialReporte.getNombreArchivo()+" ha sido modificado");
            }else{
                agregarSuccesMessage("El contenido original del archivo "+historialReporte.getNombreArchivo()+" no ha sido modificado");
            }
            this.getRichInputFile().resetValue();
        } catch (Exception e) {
            agregarErrorMessage("Se ha producido un error al validar el archivo "+historialReporte.getNombreArchivo());
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * @param facesContext
     * @param uIComponent
     * @param object
     */
    public void uploadFileValidator(FacesContext facesContext, UIComponent uIComponent, Object object) {
        UploadedFile uploadedFile = (UploadedFile)object;
        RichInputFile richInputFile = (RichInputFile)uIComponent;
        if (!uploadedFile.getFilename().endsWith(WORD_EXTENSION)) {   
            FacesMessage message = new FacesMessage();
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            message.setSummary(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_validacion_upload_word_detail"), uploadedFile.getFilename()));
            message.setDetail(PropertyManager.getInstance().getMessage("general_mensaje_validacion_upload_word_mensaje"));        
            facesContext.addMessage(richInputFile.getClientId(facesContext.getCurrentInstance()), message);  
            richInputFile.resetValue();             
            richInputFile.setValid(Boolean.FALSE);            
        }          
    }


    public void setHistorialReporteList(List<HistorialReporte> historialReporteList) {
        this.historialReporteList = historialReporteList;
    }

    public List<HistorialReporte> getHistorialReporteList() {
        return historialReporteList;
    }

    public void setRenderHistorialReportes(boolean renderHistorialReportes) {
        this.renderHistorialReportes = renderHistorialReportes;
    }

    public boolean isRenderHistorialReportes() {
        return renderHistorialReportes;
    }

    public void setHistorialReporteTable(RichTable historialReporteTable) {
        this.historialReporteTable = historialReporteTable;
    }

    public RichTable getHistorialReporteTable() {
        return historialReporteTable;
    }

    public void setRichInputFile(RichInputFile richInputFile) {
        this.richInputFile = richInputFile;
    }

    public RichInputFile getRichInputFile() {
        return richInputFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }
}
