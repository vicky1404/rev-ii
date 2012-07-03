/**
 * 
 */
package cl.mdr.ifrs.modules.aprobacion.mb;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.mb.FiltroBackingBean;
import cl.mdr.ifrs.cross.util.PropertyManager;
import cl.mdr.ifrs.ejb.common.VigenciaEnum;
import cl.mdr.ifrs.ejb.entity.EstadoCuadro;
import cl.mdr.ifrs.ejb.entity.TipoCuadro;
import cl.mdr.ifrs.ejb.entity.Version;
import cl.mdr.ifrs.modules.reporte.mb.ReporteUtilBackingBean;

/**
 * @author rreyes
 * @link http://cl.linkedin.com/in/rreyesc
 *
 */
@ManagedBean
@ViewScoped
public class FlujoAprobacionBackingBean extends AbstractBackingBean implements Serializable {
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private static final long serialVersionUID = -2264520713568048970L;
	
	@ManagedProperty(value="#{reporteUtilBackingBean}")
	private ReporteUtilBackingBean reporteUtilBackingBean;
	
    private EstadoCuadro estadoCuadro;
    private TipoCuadro tipoCuadro;
    private List<Version> catalogoFlujoAprobacion;    
    private String nombreArchivoExport;
    private Version rowSelected;
    private boolean renderFlujo = Boolean.FALSE;
    private transient FiltroBackingBean filtro;
	
    
    public String limpiarAction(){
        FiltroBackingBean filtroPaso = super.getFiltroBackingBean();
        filtroPaso.getPeriodo().setPeriodo(null);
        return null;
    }
	
    /*
    public String buscarAction() {
        logger.info("buscando cuadros para workflow de aprobación");
        try{              
            limpiarAction();
            this.setCatalogoFlujoAprobacion(super.getFacadeService().getPeriodoService().findPeriodoByFiltro(super.getNombreUsuario(), this.getTipoCuadro(), super.getFiltroPeriodo(), this.getEstadoCuadro(), VigenciaEnum.VIGENTE.getKey()));           
            this.setRenderFlujo(Boolean.TRUE);
        }catch(javax.ejb.EJBException e){
            addWarnMessage("El período consultado no existe");
            return null;                    
        } catch(Exception e){
            logger.error(e.getCause(), e);
            addErrorMessage(PropertyManager.getInstance().getMessage("workflow_mensaje_buscar_catalogo_error"));
        }
        return null;
    }
    
    public String guardarAction(){        
        List<HistorialVersionPeriodo> historialVersionPeriodoList = new ArrayList<HistorialVersionPeriodo>();
        HistorialVersionPeriodo historialVersionPeriodo = null;
        try{            
            final List<VersionPeriodo> versionPeriodoValidateInRolList = this.validarCambioEstadoInRol(this.getCatalogoFlujoAprobacion());
            if(!versionPeriodoValidateInRolList.isEmpty()){
                addWarnMessage(PropertyManager.getInstance().getMessage("workflow_mensaje_sin_privilegios_cambiar_estado"));
                this.getMensajeValidacionCuadros(versionPeriodoValidateInRolList);                
                return null;
            }
            boolean versionPeriodoValidateFlujo = this.validarCambioEstadoFlujo(this.getCatalogoFlujoAprobacion(), super.getFacade().getPeriodoService().findPeriodoByFiltro(super.getNombreUsuario(), this.getTipoCuadro(), super.getFiltroPeriodo(), this.getEstadoCuadro(), VigenciaEnum.VIGENTE.getKey()));
            if(!versionPeriodoValidateFlujo){
                sort(this.getCatalogoFlujoAprobacion(), on(VersionPeriodo.class).getVersion().getCatalogo().getOrden());               
                return null;
            }
            else if(this.getCatalogoFlujoAprobacion().isEmpty()){
                agregarWarnMessage(PropertyManager.getInstance().getMessage("workflow_mensaje_seleccione_notas"));
                return null;  
            }
            else{
                
                for(VersionPeriodo versionPeriodo : this.getCatalogoFlujoAprobacion()){
                    String mensaje = "";
                    if(versionPeriodo.getEstado().getIdEstado().equals(EstadoCuadroEnum.CERRADO.getKey())){
                        mensaje = "SE CIERRA VERSIÓN";
                    }else{
                        mensaje = "SE CAMBIA A ESTADO: "+versionPeriodo.getEstado().getNombre();
                    }
                    historialVersionPeriodo = new HistorialVersionPeriodo();
                    historialVersionPeriodo.setVersionPeriodo(versionPeriodo);
                    historialVersionPeriodo.setEstadoCuadro(versionPeriodo.getEstado());
                    historialVersionPeriodo.setFechaProceso(new Date());
                    historialVersionPeriodo.setUsuario(super.getNombreUsuario());
                    historialVersionPeriodo.setComentario(versionPeriodo.getComentario() == null ? mensaje : versionPeriodo.getComentario());
                    historialVersionPeriodoList.add(historialVersionPeriodo);
                }        
                super.getFacade().getPeriodoService().persistFlujoAprobacion(this.getCatalogoFlujoAprobacion(), historialVersionPeriodoList);
                this.setCatalogoFlujoAprobacion(super.getFacade().getPeriodoService().findPeriodoByFiltro(super.getNombreUsuario(), this.getTipoCuadro(), super.getFiltroPeriodo(), this.getEstadoCuadro(), null));
            }
            agregarSuccesMessage(PropertyManager.getInstance().getMessage("workflow_mensaje_guardar_notas_exito"));
            AdfFacesContext.getCurrentInstance().addPartialTarget(this.getTablaCatalogo());
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            agregarErrorMessage(PropertyManager.getInstance().getMessage("workflow_mensaje_guardar_notas_error"));
        }
        return null;
    }
    
    public void reporteFlujoAprobacionAction(ActionEvent event){            
        try{
            final XSSFWorkbook wb = super.getFacadeService().getServiceReporte().createReporteFlujoAprobacion(super.getFacadeService().getPeriodoService().findPeriodoByFiltro(super.getNombreUsuario(), this.getTipoCuadro(), super.getFiltroPeriodo(), this.getEstadoCuadro(), VigenciaEnum.VIGENTE.getKey()));
            this.getReporteUtilBackingBean().setOuputStreamWorkBook(wb, "informe-flujo-aprobacion");
        } catch (Exception e) {
            addErrorMessage("Error al obtener reporte de Flujo de Aprobación");
            logger.error(e.getCause(),e);
        }           
    }
    */

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

}
