package cl.mdr.ifrs.modules.reporte.mb;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.persistence.NoResultException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.mb.FiltroBackingBean;
import cl.mdr.ifrs.cross.model.CommonGridModel;
import cl.mdr.ifrs.cross.util.PropertyManager;
import cl.mdr.ifrs.cross.util.UtilBean;
import cl.mdr.ifrs.ejb.entity.Catalogo;
import cl.mdr.ifrs.ejb.entity.Estructura;
import cl.mdr.ifrs.ejb.entity.Version;
import cl.mdr.ifrs.ejb.reporte.util.SoporteReporte;
import cl.mdr.ifrs.ejb.reporte.vo.ReportePrincipalVO;

@ManagedBean
@ViewScoped
public class ReporteBackingBean extends AbstractBackingBean implements Serializable {
	private transient Logger logger = Logger.getLogger(this.getClass().getName());
	private static final long serialVersionUID = 8774914129918148714L;
	
	@ManagedProperty(value="#{reporteUtilBackingBean}")
	private ReporteUtilBackingBean reporteUtilBackingBean;
	
	private static final String IMAGEN_HEADER_REPORTE = "logo-bice.jpg";
    private static final String POPUP_DOWNLOAD_WORD = "p_down_word";
    private static final String POPUP_DOWNLOAD_EXCEL = "p_down_excel";
    private static final String FORMULARIO_EXPORTAR_CUADROS = "f_reporte_cuadro";
    private List<Version> versiones;
    private List<CommonGridModel<Version>> catalogoReportes;    
    private boolean renderCatalogoReportes = Boolean.FALSE;
    private boolean renderBotonExportarWord;    
    private List<Version> versionDownloadList;
    private Long tipoImpresionHeader;
	
    
	/**
     * accion encargada de buscar catalogo para generacion de los reportes.
     * @return
     */
    public void buscarAction(){
        logger.info("buscando catalogo para impresión de reportes");     
        FiltroBackingBean filtroPaso = super.getFiltroBackingBean();
        filtroPaso.getPeriodo().setPeriodo(null);
        try { 
            final String fechaPeriodo = super.getFiltroBackingBean().getPeriodo().getAnioPeriodo() + super.getFiltroBackingBean().getPeriodo().getMesPeriodo();  
            try{
                Long periodo = Long.valueOf(filtroPaso.getPeriodo().getAnioPeriodo().concat(filtroPaso.getPeriodo().getMesPeriodo()));
                super.getFiltroBackingBean().setPeriodo(super.getFacadeService().getMantenedoresTipoService().findByPeriodo(periodo));
            }catch(NoResultException e){
            	addWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("periodo_busqueda_sin_resultado_periodo"), super.getFiltroBackingBean().getPeriodo().getAnioPeriodo(), super.getFiltroBackingBean().getPeriodo().getMesPeriodo()));                            
                this.setCatalogoReportes(null); 
                this.setRenderCatalogoReportes(Boolean.FALSE);
                return;                    
            }catch(EJBException e){
                addWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("periodo_busqueda_sin_resultado_periodo"), super.getFiltroBackingBean().getPeriodo().getAnioPeriodo(), super.getFiltroBackingBean().getPeriodo().getMesPeriodo()));                        
                this.setCatalogoReportes(null);                 
                this.setRenderCatalogoReportes(Boolean.FALSE);
                return;
            }
            List<Version> versionList = super.getFacadeService().getVersionService().findUltimoVersionByPeriodo(Long.valueOf(fechaPeriodo), super.getNombreUsuario(), this.getFiltroBackingBean().getTipoCuadro(), null);            
            this.setCatalogoReportes(this.getGrillaReportes(versionList));                
            this.setRenderCatalogoReportes(Boolean.TRUE);
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            super.addErrorMessage("Error", "Se ha producido un error al buscar el catálogo para la generación de reportes");
        }        
    }
    
    /**
     * Contruye una lista con <CommonGridModel<Version>> para desplegar en la vista
     * y obtener el atributo selected de cada item
     * @param versionList
     * @return
     */
    public List<CommonGridModel<Version>> getGrillaReportes(List<Version> versionList){
        catalogoReportes = new ArrayList<CommonGridModel<Version>>();
        for(Version version  : versionList){
            catalogoReportes.add(new CommonGridModel<Version>(version, Boolean.FALSE));
        }
        return catalogoReportes;
    }
    
    public List<Version> getVersionesSelected() throws Exception {
        List<Catalogo> catalogoList = new ArrayList<Catalogo>();
        versiones = new ArrayList<Version>();    
        for (CommonGridModel<Version> grillaReporte : this.getCatalogoReportes()) {
            if (grillaReporte.isSelected()) {                    
                versiones.add(grillaReporte.getEntity()); 
                catalogoList.add(grillaReporte.getEntity().getCatalogo());
            }            
        }
        if(!catalogoList.isEmpty()){
            this.getFacadeService().getCatalogoService().persistEntity(catalogoList);        
        }
        sort(versiones, on(Version.class).getCatalogo().getOrden()); 
        
        return versiones;
    }
    
    public String generarExcel(){        
        try {
            this.setVersionDownloadList(null);
            List<Version> versionList = this.getVersionesSelected();                
            for(Version version : versionList){
                List<Estructura> estructuras = getFacadeService().getEstructuraService().getEstructuraByVersion(version, Boolean.FALSE);
                version.setEstructuraList(estructuras);
            }
            if(versionList.isEmpty() || versionList == null){
                addWarnMessage("Atención", "Seleccione al menos un elemento del Catálogo para exportar a MS Excel");
                return null;
            }
            this.setVersionDownloadList(versionList);                                     
            this.displayPopUp(POPUP_DOWNLOAD_EXCEL, FORMULARIO_EXPORTAR_CUADROS);
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            addErrorMessage("Error", "Se ha producido un error al exportar a formato MS Excel");
        }     
        return null;
    }
    
    public String generarDocx(){            
        try{
            this.setVersionDownloadList(null);
            List<Version> versionList = this.getVersionesSelected();
            for(Version version : versionList){
                List<Estructura> estructuras = getFacadeService().getEstructuraService().getEstructuraByVersion(version, Boolean.FALSE);
                version.setEstructuraList(estructuras);
            }
            if(versionList.isEmpty() || versionList == null){
                addWarnMessage("Seleccione al menos un elemento del Catálogo para exportar a MS Word");
                return null;
            }
            this.setVersionDownloadList(versionList);                                     
            this.displayPopUp(POPUP_DOWNLOAD_WORD, FORMULARIO_EXPORTAR_CUADROS);              
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            addErrorMessage("Error", "Se ha producido un error al exportar a formato MS Word "+e);
        }  
        return null;
    }
    
    public String downloadExcel(){
        logger.info("descargando archivo excel");
        try{
            final List<ReportePrincipalVO> reportes = this.getReporteUtilBackingBean().getGenerarListReporteVO(this.getVersionDownloadList());                            
            XSSFWorkbook wb = super.getFacadeService().getServiceReporte().createXLSX(reportes);        
            this.getReporteUtilBackingBean().setOuputStreamWorkBook(wb);                      
            super.getFacesContext().responseComplete();            
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            addErrorMessage("Error", "Se ha producido un error al exportar a formato MS Excel");
        }
        return null;
    }
    
    public String downloadWord(){
        logger.info("descargando archivo word");
        try{
            final List<ReportePrincipalVO> reportes = this.getReporteUtilBackingBean().getGenerarListReporteVO(this.getVersionDownloadList());
            final String nombreArchivo = SoporteReporte.getNombreReporteDocx(new Date());                        
            WordprocessingMLPackage wordMLPackage = super.getFacadeService().getReporteDocxService().createDOCX(reportes, getLogoReporte(), super.getNombreUsuario(), super.getIpUsuario(), nombreArchivo, super.getFiltroBackingBean().getPeriodo());              
            this.getReporteUtilBackingBean().setOutPutStreamDocx(wordMLPackage, nombreArchivo);
            super.getFacesContext().responseComplete();            
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            addErrorMessage("Error", "Se ha producido un error al exportar a formato MS Word "+e);
        }
        return null;
    }

    /**
     * 
     */
    public void tipoImpresionChange(){
		try{						
			Long tipoImpresion = this.getTipoImpresionHeader();
	        if(tipoImpresion != null){
	            List<CommonGridModel<Version>> grillaList = new ArrayList<CommonGridModel<Version>>();
	            for(CommonGridModel<Version> grillaReporte : this.getCatalogoReportes()) {                                                 
	                grillaReporte.getEntity().getCatalogo().setImpresionHorizontal(tipoImpresion);
	                grillaList.add(grillaReporte);
	            }
	            this.setCatalogoReportes(grillaList);	            
	        }       
		}catch(Exception e){
			addErrorMessage("Error", "Se ha producido un error al Cambiar la Horientación de Impresión");
			logger.error(e.getCause(), e);
		}
		
	}
    
    /**
     * obtiene la imagen a utilizar en la cabecera del reporte.
     * @return byte[]
     * @throws IOException
     */
    private static final byte[] getLogoReporte() throws Exception {        
        InputStream inputStream = UtilBean.class.getResourceAsStream(IMAGEN_HEADER_REPORTE);
        return IOUtils.toByteArray(inputStream);   
    }

	public List<Version> getVersiones() {
		return versiones;
	}

	public void setVersiones(List<Version> versiones) {
		this.versiones = versiones;
	}

	public List<CommonGridModel<Version>> getCatalogoReportes() {
		return catalogoReportes;
	}

	public void setCatalogoReportes(List<CommonGridModel<Version>> catalogoReportes) {
		this.catalogoReportes = catalogoReportes;
	}

	public boolean isRenderCatalogoReportes() {
		return renderCatalogoReportes;
	}

	public void setRenderCatalogoReportes(boolean renderCatalogoReportes) {
		this.renderCatalogoReportes = renderCatalogoReportes;
	}

	public boolean isRenderBotonExportarWord() {
		return renderBotonExportarWord;
	}

	public void setRenderBotonExportarWord(boolean renderBotonExportarWord) {
		this.renderBotonExportarWord = renderBotonExportarWord;
	}

	public List<Version> getVersionDownloadList() {
		return versionDownloadList;
	}

	public void setVersionDownloadList(List<Version> versionDownloadList) {
		this.versionDownloadList = versionDownloadList;
	}

	public ReporteUtilBackingBean getReporteUtilBackingBean() {
		return reporteUtilBackingBean;
	}

	public void setReporteUtilBackingBean(
			ReporteUtilBackingBean reporteUtilBackingBean) {
		this.reporteUtilBackingBean = reporteUtilBackingBean;
	}

	public Long getTipoImpresionHeader() {
		return tipoImpresionHeader;
	}

	public void setTipoImpresionHeader(Long tipoImpresionHeader) {
		this.tipoImpresionHeader = tipoImpresionHeader;
	}
}
