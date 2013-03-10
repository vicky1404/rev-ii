/**
 * 
 */
package cl.mdr.ifrs.modules.aprobacion.mb;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static ch.lambdaj.Lambda.sort;
import static org.hamcrest.Matchers.equalTo;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.persistence.NoResultException;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.chart.PieChartModel;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.mb.FiltroBackingBean;
import cl.mdr.ifrs.cross.util.PropertyManager;
import cl.mdr.ifrs.ejb.common.EstadoCuadroEnum;
import cl.mdr.ifrs.ejb.common.VigenciaEnum;
import cl.mdr.ifrs.ejb.cross.Constantes;
import cl.mdr.ifrs.ejb.cross.SortHelper;
import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.EstadoCuadro;
import cl.mdr.ifrs.ejb.entity.HistorialVersion;
import cl.mdr.ifrs.ejb.entity.TipoCuadro;
import cl.mdr.ifrs.ejb.entity.Version;
import cl.mdr.ifrs.modules.reporte.mb.ReporteUtilBackingBean;

/**
 * @author rreyes
 * @link http://cl.linkedin.com/in/rreyesc
 */
@ManagedBean
@ViewScoped
public class FlujoAprobacionBackingBean extends AbstractBackingBean implements Serializable {
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private static final long serialVersionUID = -2264520713568048970L;
	
	private static final String POPUP_DOWNLOAD_EXCEL = "p_down_excel";
    private static final String FORMULARIO_FLUJO_APROBACION = "form_flujo";
	
	@ManagedProperty(value="#{reporteUtilBackingBean}")
	private ReporteUtilBackingBean reporteUtilBackingBean;
	
    private EstadoCuadro estadoCuadro;
    private TipoCuadro tipoCuadro;
    private List<Version> catalogoFlujoAprobacion; 
    private List<Version> catalogoFlujoAprobacionReporte; 
    private String nombreArchivoExport;
    private Version rowSelected;
    private boolean renderFlujo = Boolean.FALSE;
    private transient FiltroBackingBean filtro;
    private HtmlSelectOneMenu comboEstadoCuadroHeader;
    private DataTable catalogoFlujoTable;
    
    //graficos
    private PieChartModel cuadrosByUsuarioPieModel;  
    private PieChartModel cuadrosAllPieModel; 
    boolean renderChartByUser;
    boolean renderChartAll;
	
    
    public void buscarAction() {
        logger.debug("buscando cuadros para workflow de aprobación");
        
        if(!isSelectedFiltroPeriodo() || !isSelectedEmpresa())
        	return;
        
        try{
            this.setCatalogoFlujoAprobacion(super.getFacadeService().getVersionService().findVersionDetalleByFiltro(super.getNombreUsuario(), super.getFiltroBackingBean().getTipoCuadro(), super.getFiltroPeriodoEmpresa(), this.getEstadoCuadro(), VigenciaEnum.VIGENTE.getKey(), null));            
            SortHelper.sortVersionByOrdenCatalogo(this.getCatalogoFlujoAprobacion());
            this.setRenderFlujo(Boolean.TRUE);
        } catch (NoResultException e) {	
			super.addWarnMessage("No se encontraron resultados para los criterios de búsqueda ingresados.");
			logger.error(e.getCause(), e);		
        }catch(javax.ejb.EJBException e){
            super.addWarnMessage("El período consultado no existe");
            return;                    
        } catch(Exception e){
            logger.error(e.getCause(), e);
            super.addErrorMessage(PropertyManager.getInstance().getMessage("workflow_mensaje_buscar_catalogo_error"));
        }        
    }
    
    
    public String guardarAction(){  
    	logger.debug("guardando cuadros para workflow de aprobación");
    	final List<Version> versionListChanged = select(this.getCatalogoFlujoAprobacion() , having(on(Version.class).isEstadoCambiado(), equalTo(Boolean.TRUE)));
        List<HistorialVersion> historialVersionList = new ArrayList<HistorialVersion>();
        HistorialVersion historialVersion= null;
        try{
        	if(versionListChanged.isEmpty()){
        		addWarnMessage(PropertyManager.getInstance().getMessage("workflow_mensaje_seleccione_notas"));
                return null;  
        	}
            final List<Version> versionValidateInRolList = this.validarCambioEstadoInRol(versionListChanged);
            if(!versionValidateInRolList.isEmpty()){
                addWarnMessage(PropertyManager.getInstance().getMessage("workflow_mensaje_sin_privilegios_cambiar_estado"));
                this.getMensajeValidacionCuadros(versionValidateInRolList);                
                return null;
            }
            boolean versionValidateFlujo = this.validarCambioEstadoFlujo(versionListChanged, super.getFacadeService().getVersionService().findVersionListActualToCompare(versionListChanged));
            if(!versionValidateFlujo){
            	SortHelper.sortVersionByOrdenCatalogo(this.getCatalogoFlujoAprobacion());       
                return null;
            }
            else if(this.getCatalogoFlujoAprobacion().isEmpty()){
                addWarnMessage(PropertyManager.getInstance().getMessage("workflow_mensaje_seleccione_notas"));
                return null;  
            }
            else{
                                                                 
                for(final Version version : versionListChanged){                	
                    String mensaje = "";
                    if(version.getEstado().getIdEstado().equals(EstadoCuadroEnum.CERRADO.getKey())){
                        mensaje = "SE CIERRA VERSIÓN";
                    }else{
                        mensaje = "SE CAMBIA A ESTADO: "+version.getEstado().getNombre();
                    }
                    historialVersion = new HistorialVersion();
                    historialVersion.setVersion(version);
                    historialVersion.setEstadoCuadro(version.getEstado());
                    historialVersion.setFechaProceso(new Date());
                    historialVersion.setUsuario(super.getUsuarioSesion());
                    historialVersion.setComentario(version.getComentario() == null ? mensaje : version.getComentario());
                    historialVersionList.add(historialVersion);
                }        
                super.getFacadeService().getPeriodoService().persistFlujoAprobacion(versionListChanged, historialVersionList);
                this.setCatalogoFlujoAprobacion(super.getFacadeService().getVersionService().findVersionDetalleByFiltro(super.getNombreUsuario(), super.getFiltroBackingBean().getTipoCuadro(), super.getFiltroPeriodoEmpresa(), this.getEstadoCuadro(), VigenciaEnum.VIGENTE.getKey(), null));                
                SortHelper.sortVersionByOrdenCatalogo(this.getCatalogoFlujoAprobacion());
            }
            addInfoMessage(PropertyManager.getInstance().getMessage("workflow_mensaje_guardar_notas_exito"));            
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            addErrorMessage(PropertyManager.getInstance().getMessage("workflow_mensaje_guardar_notas_error"));
        }
        return null;
    }
    
   
 
	public void generarReporteFlujoAprobacionAction(ActionEvent event) {
		logger.debug("generando archivo excel");
		try {			
			  this.setCatalogoFlujoAprobacionReporte(super.getFacadeService()
													.getVersionService()
													.findVersionByFiltro(super.getNombreUsuario(),
																		 super.getFiltroBackingBean().getTipoCuadro(),
																		 super.getFiltroPeriodoEmpresa(), this.getEstadoCuadro(),
																		 VigenciaEnum.VIGENTE.getKey(), null));
			  SortHelper.sortVersionByOrdenCatalogo(this.getCatalogoFlujoAprobacionReporte());
			  this.displayPopUp(POPUP_DOWNLOAD_EXCEL, FORMULARIO_FLUJO_APROBACION);
		} catch (Exception e) {
			addErrorMessage("Error al obtener reporte de Flujo de Aprobación");
			logger.error(e.getCause(), e);
		}
	}
    
    public String descargarReporteFlujoAprobacionAction(){
        logger.debug("descargando archivo excel");
        try{                               
            final XSSFWorkbook wb = super.getFacadeService().getServiceReporte().createReporteFlujoAprobacion(this.getCatalogoFlujoAprobacionReporte());        
            this.getReporteUtilBackingBean().setOuputStreamWorkBook(wb, "informe-flujo-aprobacion");                      
            super.getFacesContext().responseComplete();            
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            addErrorMessage("Error", "Se ha producido un error al exportar a formato MS Excel");
        }
        return null;
    }
    
    public void onchangeEstadoCuadroHeader(){
    	EstadoCuadro estadoCuadro = (EstadoCuadro) this.getComboEstadoCuadroHeader().getValue();
	    if(estadoCuadro != null){
	        List<Version> versionList = new ArrayList<Version>();
	        for(Version version : this.getCatalogoFlujoAprobacion()){
	            version.setEstado(estadoCuadro);
	            version.setEstadoCambiado(Boolean.TRUE);
	            versionList.add(version);	            
	        }
	        this.setCatalogoFlujoAprobacion(versionList);	        
	    }
    }
    
    public void onChangeEstadoNota(ValueChangeEvent valueChangeEvent){        
        final EstadoCuadro estado = (EstadoCuadro)valueChangeEvent.getNewValue();
        Version version = (Version) this.getCatalogoFlujoTable().getRowData();
        version.setEstado(estado);
        version.setEstadoCambiado(Boolean.TRUE);
        super.getFacesContext().renderResponse();     
    }
    
    private boolean validarCambioEstadoFlujo(final List<Version> versionListNew, final List<Version> versionListActual) throws Exception{
        boolean result = Boolean.TRUE;  
        sort(versionListNew, on(Version.class).getIdVersion());
        sort(versionListActual, on(Version.class).getIdVersion());            
        final Iterator<Version> it1 = versionListNew.iterator();
        final Iterator<Version> it2 = versionListActual.iterator();
        int count = 1;
        while(it1.hasNext() && it2.hasNext()){
            Version versionNew = it1.next();
            Version versionActual = it2.next();
            
            if(versionActual.getEstado().getIdEstado().equals(EstadoCuadroEnum.CERRADO.getKey())){
                if(!(versionNew.getEstado().getIdEstado().equals(EstadoCuadroEnum.CONTINGENCIA.getKey())) &&
                   !(versionNew.getEstado().getIdEstado().equals(EstadoCuadroEnum.CERRADO.getKey()))
                    ){
                	
                    if(count == 1){
                        super.addWarnMessage(PropertyManager.getInstance().getMessage("workflow_mensaje_prohibido_cambiar_estado"));
                    }
                    
                    super.addWarnMessage(MessageFormat.format("{0} del estado: {1} hacia el estado {2}",  versionActual.getCatalogo().getNombre(), Util.capitalizar(versionActual.getEstado().getNombre()), Util.capitalizar(versionNew.getEstado().getNombre())));                        
                    result = Boolean.FALSE;
                    count++;
                }
            }
            
            if(((versionNew.getEstado().getIdEstado().equals(EstadoCuadroEnum.CONTINGENCIA.getKey())) ||
               (versionNew.getEstado().getIdEstado().equals(EstadoCuadroEnum.CERRADO.getKey())) ||
               (versionNew.getEstado().getIdEstado().equals(EstadoCuadroEnum.APROBADO.getKey()))) &&
               ((versionNew.getCatalogo().getValidarEeff()!=null && versionNew.getCatalogo().getValidarEeff().equals(VigenciaEnum.VIGENTE.getKey())) &&
               (versionNew.getValidadoEeff() != null && versionNew.getValidadoEeff().equals(VigenciaEnum.NO_VIGENTE.getKey())))
               ){
            	
            	if(count == 1){
                    super.addWarnMessage(PropertyManager.getInstance().getMessage("workflow_mensaje_prohibido_cambiar_estado"));
                }
                
                super.addWarnMessage(MessageFormat.format("{0} del estado: {1} hacia el estado {2} debido a que la revelación no se encuentra Validada",  versionActual.getCatalogo().getNombre(), Util.capitalizar(versionActual.getEstado().getNombre()), Util.capitalizar(versionNew.getEstado().getNombre())));                        
                result = Boolean.FALSE;
                count++;
            	
            }
            
        }
        return result;
    }
    
    private List<Version> validarCambioEstadoInRol(List<Version> versionList){
        List<Version> versionValidateList = new ArrayList<Version>();   
        	
        for(final Version version : versionList){
            if(version.getEstado().getIdEstado().equals(EstadoCuadroEnum.INICIADO.getKey())){
                if(!(super.isUserInRole(Constantes.ROL_RESP)) &&
                   !(super.isUserInRole(Constantes.ROL_SUP)) &&
                   !(super.isUserInRole(Constantes.ROL_ENC)) &&
                   !(super.isUserInRole(Constantes.ROL_ADMIN))){
                	versionValidateList.add(version);
                }
            }
            if(version.getEstado().getIdEstado().equals(EstadoCuadroEnum.MODIFICADO.getKey())){
                if(!(super.isUserInRole(Constantes.ROL_RESP)) && 
                   !(super.isUserInRole(Constantes.ROL_SUP)) &&
                   !(super.isUserInRole(Constantes.ROL_ENC)) &&
                   !(super.isUserInRole(Constantes.ROL_ADMIN))){
                	versionValidateList.add(version);
                }
            }
            if(version.getEstado().getIdEstado().equals(EstadoCuadroEnum.POR_APROBAR.getKey())){
                if(!(super.isUserInRole(Constantes.ROL_RESP)) && 
                   !(super.isUserInRole(Constantes.ROL_SUP)) &&
                   !(super.isUserInRole(Constantes.ROL_ENC)) &&
                   !(super.isUserInRole(Constantes.ROL_ADMIN))){
                	versionValidateList.add(version);
                }
            }
            if(version.getEstado().getIdEstado().equals(EstadoCuadroEnum.APROBADO.getKey())){
                if(!(super.isUserInRole(Constantes.ROL_RESP)) && 
                   !(super.isUserInRole(Constantes.ROL_SUP)) &&
                   !(super.isUserInRole(Constantes.ROL_ADMIN))){
                	versionValidateList.add(version);
                }
            }
            if(version.getEstado().getIdEstado().equals(EstadoCuadroEnum.CERRADO.getKey())){
                if(!(super.isUserInRole(Constantes.ROL_RESP)) && 
                   !(super.isUserInRole(Constantes.ROL_ADMIN))){
                	versionValidateList.add(version);
                }
            }
            if(version.getEstado().getIdEstado().equals(EstadoCuadroEnum.CONTINGENCIA.getKey())){
                if(!(super.isUserInRole(Constantes.ROL_RESP)) && 
                   !(super.isUserInRole(Constantes.ROL_ADMIN))){
                    versionValidateList.add(version);
                }
            }        
        }
        return versionValidateList;
    }
    
    private String getMensajeValidacionCuadros(List<Version> versionValidateList){
        StringBuffer mensaje = new StringBuffer();
        for(final Version version : versionValidateList){
            super.addWarnMessage(version.getCatalogo().getNombre().concat(" a estado ").concat(Util.capitalizar(version.getEstado().getNombre())));            
        }
        return mensaje.toString();
    }
    
    public void graficoCuadrosByUsuarioAction(){
    	try {
    		
    		if(catalogoFlujoAprobacion==null){
    			addWarnMessage("No se ha encontrado información");
    		}
    		
    		//catalogoFlujoAprobacion
			//final List<Version> versiones = super.getFacadeService().getVersionService().findVersionByFiltro(super.getNombreUsuario(), super.getFiltroBackingBean().getTipoCuadro(), super.getFiltroPeriodoEmpresa(), this.getEstadoCuadro(), VigenciaEnum.VIGENTE.getKey(), null);
			cuadrosByUsuarioPieModel = new PieChartModel();  			  
			cuadrosByUsuarioPieModel.set(EstadoCuadroEnum.INICIADO.getValue(), select(catalogoFlujoAprobacion ,having(on(Version.class).getEstado().getIdEstado(), equalTo(EstadoCuadroEnum.INICIADO.getKey()))).size() );  
			cuadrosByUsuarioPieModel.set(EstadoCuadroEnum.MODIFICADO.getValue(), select(catalogoFlujoAprobacion ,having(on(Version.class).getEstado().getIdEstado(), equalTo(EstadoCuadroEnum.MODIFICADO.getKey()))).size());  
			cuadrosByUsuarioPieModel.set(EstadoCuadroEnum.POR_APROBAR.getValue(), select(catalogoFlujoAprobacion ,having(on(Version.class).getEstado().getIdEstado(), equalTo(EstadoCuadroEnum.POR_APROBAR.getKey()))).size());  
			cuadrosByUsuarioPieModel.set(EstadoCuadroEnum.APROBADO.getValue(), select(catalogoFlujoAprobacion ,having(on(Version.class).getEstado().getIdEstado(), equalTo(EstadoCuadroEnum.APROBADO.getKey()))).size());  
			cuadrosByUsuarioPieModel.set(EstadoCuadroEnum.CERRADO.getValue(), select(catalogoFlujoAprobacion ,having(on(Version.class).getEstado().getIdEstado(), equalTo(EstadoCuadroEnum.CERRADO.getKey()))).size());
			cuadrosByUsuarioPieModel.set(EstadoCuadroEnum.CONTINGENCIA.getValue(), select(catalogoFlujoAprobacion ,having(on(Version.class).getEstado().getIdEstado(), equalTo(EstadoCuadroEnum.CONTINGENCIA.getKey()))).size());
			this.setRenderChartByUser(Boolean.TRUE);
			this.displayPopUp("popUpChartByUser", FORMULARIO_FLUJO_APROBACION);
    	} catch (Exception e) {
			 logger.error(e.getCause(), e);
	         addErrorMessage("Error", "Se ha producido un error al Generar el Grafico");
		}
    }
    
    public void graficoCuadrosAllAction(){
    	try {
			final List<Version> versiones = super.getFacadeService().getVersionService().findAllVersionVigente(super.getFiltroBackingBean().getEmpresa().getIdRut());
			cuadrosAllPieModel = new PieChartModel();  			  
			cuadrosAllPieModel.set(EstadoCuadroEnum.INICIADO.getValue(), select(versiones ,having(on(Version.class).getEstado().getIdEstado(), equalTo(EstadoCuadroEnum.INICIADO.getKey()))).size() );  
			cuadrosAllPieModel.set(EstadoCuadroEnum.MODIFICADO.getValue(), select(versiones ,having(on(Version.class).getEstado().getIdEstado(), equalTo(EstadoCuadroEnum.MODIFICADO.getKey()))).size());  
			cuadrosAllPieModel.set(EstadoCuadroEnum.POR_APROBAR.getValue(), select(versiones ,having(on(Version.class).getEstado().getIdEstado(), equalTo(EstadoCuadroEnum.POR_APROBAR.getKey()))).size());  
			cuadrosAllPieModel.set(EstadoCuadroEnum.APROBADO.getValue(), select(versiones ,having(on(Version.class).getEstado().getIdEstado(), equalTo(EstadoCuadroEnum.APROBADO.getKey()))).size());  
			cuadrosAllPieModel.set(EstadoCuadroEnum.CERRADO.getValue(), select(versiones ,having(on(Version.class).getEstado().getIdEstado(), equalTo(EstadoCuadroEnum.CERRADO.getKey()))).size());
			cuadrosAllPieModel.set(EstadoCuadroEnum.CONTINGENCIA.getValue(), select(versiones ,having(on(Version.class).getEstado().getIdEstado(), equalTo(EstadoCuadroEnum.CONTINGENCIA.getKey()))).size());
			this.setRenderChartAll(Boolean.TRUE);
			this.displayPopUp("popUpChartAll", FORMULARIO_FLUJO_APROBACION);
    	} catch (Exception e) {
			 logger.error(e.getCause(), e);
	         addErrorMessage("Error", "Se ha producido un error al Generar el Grafico");
		}
    }

	public ReporteUtilBackingBean getReporteUtilBackingBean() {
		return reporteUtilBackingBean;
	}

	public void setReporteUtilBackingBean(
			ReporteUtilBackingBean reporteUtilBackingBean) {
		this.reporteUtilBackingBean = reporteUtilBackingBean;
	}

	public EstadoCuadro getEstadoCuadro() {
		return estadoCuadro;
	}

	public void setEstadoCuadro(EstadoCuadro estadoCuadro) {
		this.estadoCuadro = estadoCuadro;
	}

	public TipoCuadro getTipoCuadro() {
		return tipoCuadro;
	}

	public void setTipoCuadro(TipoCuadro tipoCuadro) {
		this.tipoCuadro = tipoCuadro;
	}

	public List<Version> getCatalogoFlujoAprobacion() {
		return catalogoFlujoAprobacion;
	}

	public void setCatalogoFlujoAprobacion(List<Version> catalogoFlujoAprobacion) {
		this.catalogoFlujoAprobacion = catalogoFlujoAprobacion;
	}

	public String getNombreArchivoExport() {
		return nombreArchivoExport;
	}

	public void setNombreArchivoExport(String nombreArchivoExport) {
		this.nombreArchivoExport = nombreArchivoExport;
	}

	public Version getRowSelected() {
		return rowSelected;
	}

	public void setRowSelected(Version rowSelected) {
		this.rowSelected = rowSelected;
	}

	public boolean isRenderFlujo() {
		return renderFlujo;
	}

	public void setRenderFlujo(boolean renderFlujo) {
		this.renderFlujo = renderFlujo;
	}

	public FiltroBackingBean getFiltro() {
		return filtro;
	}

	public void setFiltro(FiltroBackingBean filtro) {
		this.filtro = filtro;
	}


	public HtmlSelectOneMenu getComboEstadoCuadroHeader() {
		return comboEstadoCuadroHeader;
	}


	public void setComboEstadoCuadroHeader(HtmlSelectOneMenu comboEstadoCuadroHeader) {
		this.comboEstadoCuadroHeader = comboEstadoCuadroHeader;
	}


	public DataTable getCatalogoFlujoTable() {
		return catalogoFlujoTable;
	}


	public void setCatalogoFlujoTable(DataTable catalogoFlujoTable) {
		this.catalogoFlujoTable = catalogoFlujoTable;
	}


	public List<Version> getCatalogoFlujoAprobacionReporte() {
		return catalogoFlujoAprobacionReporte;
	}


	public void setCatalogoFlujoAprobacionReporte(
			List<Version> catalogoFlujoAprobacionReporte) {
		this.catalogoFlujoAprobacionReporte = catalogoFlujoAprobacionReporte;
	}


	public PieChartModel getCuadrosByUsuarioPieModel() {
		return cuadrosByUsuarioPieModel;
	}


	public void setCuadrosByUsuarioPieModel(PieChartModel cuadrosByUsuarioPieModel) {
		this.cuadrosByUsuarioPieModel = cuadrosByUsuarioPieModel;
	}


	public PieChartModel getCuadrosAllPieModel() {
		return cuadrosAllPieModel;
	}


	public void setCuadrosAllPieModel(PieChartModel cuadrosAllPieModel) {
		this.cuadrosAllPieModel = cuadrosAllPieModel;
	}


	public boolean isRenderChartByUser() {
		return renderChartByUser;
	}


	public void setRenderChartByUser(boolean renderChartByUser) {
		this.renderChartByUser = renderChartByUser;
	}


	public boolean isRenderChartAll() {
		return renderChartAll;
	}


	public void setRenderChartAll(boolean renderChartAll) {
		this.renderChartAll = renderChartAll;
	}

}
