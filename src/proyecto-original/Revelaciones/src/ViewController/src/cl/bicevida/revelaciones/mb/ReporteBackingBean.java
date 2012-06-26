package cl.bicevida.revelaciones.mb;


import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;

import cl.bicevida.revelaciones.common.filtros.Filtro;
import cl.bicevida.revelaciones.common.mb.SoporteBackingBean;
import cl.bicevida.revelaciones.common.model.CommonGridModel;
import cl.bicevida.revelaciones.common.util.BeanUtil;
import cl.bicevida.revelaciones.common.util.PropertyManager;
import cl.bicevida.revelaciones.ejb.cross.Constantes;
import cl.bicevida.revelaciones.ejb.entity.Catalogo;
import cl.bicevida.revelaciones.ejb.entity.Estructura;
import cl.bicevida.revelaciones.ejb.entity.Version;
import cl.bicevida.revelaciones.ejb.reporte.util.SoporteReporte;
import cl.bicevida.revelaciones.ejb.reporte.vo.ReportePrincipalVO;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import java.net.URL;
import java.net.URLConnection;

import java.text.MessageFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBException;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import javax.persistence.NoResultException;

import javax.servlet.http.HttpServletRequest;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.context.AdfFacesContext;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;


public class ReporteBackingBean extends SoporteBackingBean implements Serializable{
    private transient Logger logger = Logger.getLogger(ReporteBackingBean.class);
    
    @SuppressWarnings("compatibility:7208346911976114320")
    private static final long serialVersionUID = -2280200512986803632L;
    private static final String IMAGEN_HEADER_REPORTE = "logo-bice.jpg";
    private static final String POPUP_DOWNLOAD_WORD = "p_down_word";
    private static final String POPUP_DOWNLOAD_EXCEL = "p_down_excel";
    private List<Version> versiones;
    //private List<CommonGridModel<VersionPeriodo>> catalogoReportes;
    private List<CommonGridModel<Version>> catalogoReportes;
    
    private boolean renderCatalogoReportes = Boolean.FALSE;
    private transient RichTable tablaCatalogo;
    private boolean renderBotonExportarWord;
    
    private List<Version> versionDownloadList;

    /**
     * accion encargada de buscar catalogo para generacion de los reportes.
     * @return
     */
    public String buscar(){
        logger.info("buscando catalogo para impresión de reportes");     
        Filtro filtroPaso = getFiltro();
        filtroPaso.getPeriodo().setPeriodo(null);
        try { 
            final String fechaPeriodo = super.getFiltro().getPeriodo().getAnioPeriodo() + super.getFiltro().getPeriodo().getMesPeriodo();  
            try{
                Long periodo = Long.valueOf(filtroPaso.getPeriodo().getAnioPeriodo().concat(filtroPaso.getPeriodo().getMesPeriodo()));
                super.getFiltro().setPeriodo(getFacade().getMantenedoresTipoService().findByPeriodo(periodo));
            }catch(NoResultException e){
                agregarWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("periodo_busqueda_sin_resultado_periodo"), super.getFiltro().getPeriodo().getAnioPeriodo(), super.getFiltro().getPeriodo().getMesPeriodo()));                            
                this.setCatalogoReportes(null); 
                this.setRenderCatalogoReportes(Boolean.FALSE);
                return null;                    
            }catch(EJBException e){
                agregarWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("periodo_busqueda_sin_resultado_periodo"), super.getFiltro().getPeriodo().getAnioPeriodo(), super.getFiltro().getPeriodo().getMesPeriodo()));                        
                this.setCatalogoReportes(null);                 
                this.setRenderCatalogoReportes(Boolean.FALSE);
                return null;
            }
            List<Version> versionList = super.getFacade().getVersionService().findUltimoVersionByPeriodo(Long.valueOf(fechaPeriodo), super.getNombreUsuario(), this.getFiltro().getTipoCuadro(), null);            
            this.setCatalogoReportes(this.getGrillaReportes(versionList));                
            this.setRenderCatalogoReportes(Boolean.TRUE);
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            agregarErrorMessage("Se ha producido un error al buscar el catálogo para la generación de reportes");
        }
        return null;
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
            catalogoReportes.add(new CommonGridModel(version, Boolean.FALSE));
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
            this.getFacade().getCatalogoService().persistEntity(catalogoList);        
        }
        sort(versiones, on(Version.class).getCatalogo().getOrden()); 
        
        return versiones;
    }
      
    public String generarExcel(){        
        try {
            this.setVersionDownloadList(null);
            List<Version> versionList = this.getVersionesSelected();                
            for(Version version : versionList){
                List<Estructura> estructuras = getFacade().getEstructuraService().getEstructuraByVersion(version, Boolean.FALSE);
                version.setEstructuraList(estructuras);
            }
            if(versionList.isEmpty() || versionList == null){
                agregarWarnMessage("Seleccione al menos un elemento del Catálogo para exportar a MS Excel");
                return null;
            }
            this.setVersionDownloadList(versionList);                                     
            this.displayPopup(POPUP_DOWNLOAD_EXCEL);
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            agregarErrorMessage("Se ha producido un error al exportar a formato MS Excel");
        }     
        return null;
    }
    
    public String generarDocx(){            
        try{
            this.setVersionDownloadList(null);
            List<Version> versionList = this.getVersionesSelected();
            for(Version version : versionList){
                List<Estructura> estructuras = getFacade().getEstructuraService().getEstructuraByVersion(version, Boolean.FALSE);
                version.setEstructuraList(estructuras);
            }
            if(versionList.isEmpty() || versionList == null){
                agregarWarnMessage("Seleccione al menos un elemento del Catálogo para exportar a MS Word");
                return null;
            }
            this.setVersionDownloadList(versionList);                                     
            this.displayPopup(POPUP_DOWNLOAD_WORD);              
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            agregarErrorMessage("Se ha producido un error al exportar a formato MS Word "+e);
        }  
        return null;
    }
    
    public void downloadExcel(ActionEvent event){
        logger.info("descargando archivo excel");
        try{
            List<ReportePrincipalVO> reportes = getReporteUtilBackingBean().getGenerarListReporteVO(this.getVersionDownloadList());                            
            XSSFWorkbook wb = super.getFacade().getServiceReporte().createXLSX(reportes);        
            this.getReporteUtilBackingBean().setOuputStreamWorkBook(wb);
            super.getFacesContext().responseComplete();
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            agregarErrorMessage("Se ha producido un error al exportar a formato MS Excel");
        }     
    }
    
    public void downloadWord(ActionEvent event){
        logger.info("descargando archivo word");
        try{
            List<ReportePrincipalVO> reportes = getReporteUtilBackingBean().getGenerarListReporteVO(this.getVersionDownloadList());
            final String nombreArchivo = SoporteReporte.getNombreReporteDocx(new Date());                        
            WordprocessingMLPackage wordMLPackage = super.getFacade().getReporteDocxService().createDOCX(reportes, this.getLogoReporte(), super.getNombreUsuario(), super.getIpUsuario(), nombreArchivo, super.getFiltro().getPeriodo());              
            this.getReporteUtilBackingBean().setOutPutStreamDocx(wordMLPackage, nombreArchivo);
            super.getFacesContext().responseComplete();
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            agregarErrorMessage("Se ha producido un error al exportar a formato MS Word "+e);
        }  
    }
    
    private void displayPopup(String popupId){
        FacesContext context = FacesContext.getCurrentInstance();
        ExtendedRenderKitService extRenderKitSrvc =
        Service.getRenderKitService(context, ExtendedRenderKitService.class);
        extRenderKitSrvc.addScript(context, "AdfPage.PAGE.findComponent('" + popupId + "').show();");
    }
    
    @Deprecated
    public void generarPdf(ActionEvent event){
        List<ReportePrincipalVO> reportes;        
        try {
            List<Version> versionList = this.getVersionesSelected();
            if(versionList.isEmpty() || versionList == null){
                agregarWarnMessage("Seleccione al menos un elemento del Catálogo para exportar a PDF");
                return;
            }
            reportes = getReporteUtilBackingBean().getGenerarListReporteVO(versionList);            
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            agregarErrorMessage("Se ha producido un error al exportar a formato PDF");
        }
    }
    
    @Deprecated
    private final byte[] getLogoReporteFromUrl() throws Exception { 
        HttpServletRequest request = super.getRequest();
        StringBuffer sUrl = new StringBuffer();       
        sUrl.append(request.getScheme());
        sUrl.append("://");
        sUrl.append(request.getServerName());
        sUrl.append(":");
        sUrl.append(request.getServerPort());
        sUrl.append(request.getContextPath());
        sUrl.append("/images/logo/logo-bice.jpg");        
        // Get the image             
        final URL url = new URL(sUrl.toString());        
        URLConnection urlConnection = url.openConnection();            
        InputStream inputStream = urlConnection.getInputStream();        
        return IOUtils.toByteArray(inputStream);         
    }


    /**
     * obtiene la imagen a utilizar en la cabecera del reporte.
     * @return byte[]
     * @throws IOException
     */
    private static final byte[] getLogoReporte() throws Exception {        
        InputStream inputStream = BeanUtil.class.getResourceAsStream(IMAGEN_HEADER_REPORTE);
        return IOUtils.toByteArray(inputStream);   
    }
    
    private boolean busquedaValida(){
        
        boolean salida = true;
        
        if(getFiltro().getTipoCuadro().getIdTipoCuadro()==null){
            agregarWarnMessage("Debe seleccionar Tipo Cuadro");
            salida = false;
        }if(getFiltro().getPeriodo().getMesPeriodo()==null){
            agregarWarnMessage("Debe seleccionar Mes Periodo");
            salida = false;
        }if(getFiltro().getPeriodo().getAnioPeriodo()==null){
            agregarWarnMessage("Debe seleccionar Año Periodo");
            salida = false;
        }

        return salida;
        
    }
    
    public void onChangeTipoImpresion(ValueChangeEvent valueChangeEvent) {
        RichSelectOneChoice comboTipo = (RichSelectOneChoice)valueChangeEvent.getSource();
        final Long impresionHorizontal =  cl.bicevida.revelaciones.ejb.cross.Util.getLong(comboTipo.getValue(), null);        
        if(impresionHorizontal != null){
            List<CommonGridModel<Version>> grillaList = new ArrayList<CommonGridModel<Version>>();
            for(CommonGridModel<Version> grillaReporte : this.getCatalogoReportes()) {                                                 
                grillaReporte.getEntity().getCatalogo().setImpresionHorizontal(impresionHorizontal);
                grillaList.add(grillaReporte);
            }
            this.setCatalogoReportes(grillaList);
            AdfFacesContext.getCurrentInstance().addPartialTarget(this.getTablaCatalogo()); 
        }        
    }
   
    
    public void setVersiones(List<Version> versiones) {
        this.versiones = versiones;
    }

    public List<Version> getVersiones() {
        return versiones;
    }


    public void setCatalogoReportes(List<CommonGridModel<Version>> catalogoReportes) {
        this.catalogoReportes = catalogoReportes;
    }

    public List<CommonGridModel<Version>> getCatalogoReportes() {
        return catalogoReportes;
    }

    public void setRenderCatalogoReportes(boolean renderCatalogoReportes) {
        this.renderCatalogoReportes = renderCatalogoReportes;
    }

    public boolean isRenderCatalogoReportes() {
        return renderCatalogoReportes;
    }

    public void setTablaCatalogo(RichTable tablaCatalogo) {
        this.tablaCatalogo = tablaCatalogo;
    }

    public RichTable getTablaCatalogo() {
        return tablaCatalogo;
    }


    public void setRenderBotonExportarWord(boolean renderBotonExportarWord) {
        this.renderBotonExportarWord = renderBotonExportarWord;
    }

    public boolean isRenderBotonExportarWord() {
        try {
            renderBotonExportarWord = super.validateGroupAndRol(Constantes.GRUPO_ADMIN, Constantes.ROL_ADMIN);
        } catch (Exception e) {
            super.agregarErrorMessage("Se ha producido un error al evaluar perfil de Usuario en componente de exportar a MS Word");
            logger.error(e.getMessage(), e);
        }
        return renderBotonExportarWord;
    }

    public void setVersionDownloadList(List<Version> versionDownloadList) {
        this.versionDownloadList = versionDownloadList;
    }

    public List<Version> getVersionDownloadList() {
        return versionDownloadList;
    }
}


