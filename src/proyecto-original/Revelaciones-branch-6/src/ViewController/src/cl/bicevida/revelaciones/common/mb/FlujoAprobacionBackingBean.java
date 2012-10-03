package cl.bicevida.revelaciones.common.mb;


import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;

import cl.bicevida.revelaciones.common.filtros.Filtro;
import cl.bicevida.revelaciones.common.util.PropertyManager;
import cl.bicevida.revelaciones.ejb.common.EstadoCuadroEnum;
import cl.bicevida.revelaciones.ejb.common.VigenciaEnum;
import cl.bicevida.revelaciones.ejb.cross.Constantes;
import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.revelaciones.ejb.entity.EstadoCuadro;
import cl.bicevida.revelaciones.ejb.entity.HistorialVersionPeriodo;
import cl.bicevida.revelaciones.ejb.entity.TipoCuadro;
import cl.bicevida.revelaciones.ejb.entity.VersionPeriodo;

import java.io.Serializable;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.context.AdfFacesContext;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.event.RowDisclosureEvent;
import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class FlujoAprobacionBackingBean extends SoporteBackingBean implements Serializable {
    private transient Logger logger = Logger.getLogger(FlujoAprobacionBackingBean.class);
    
    @SuppressWarnings("compatibility:2577881934794408957")
    private static final long serialVersionUID = 413186770334880673L;
        
    private EstadoCuadro estadoCuadro;
    private TipoCuadro tipoCuadro;
    private List<VersionPeriodo> catalogoFlujoAprobacion;
    private transient RichSelectOneChoice selectEstadoCuadroHeader;
    private transient RichTable tablaCatalogo;
    private String nombreArchivoExport;
    private VersionPeriodo rowSelected;
    private boolean renderFlujo = Boolean.FALSE;
    private transient Filtro filtro;

    public String limpiarAction(){
        Filtro filtroPaso = super.getFiltro();
        filtroPaso.getPeriodo().setPeriodo(null);
        return null;
    }
    
    public String buscarAction() {
        logger.info("buscando cuadros para workflow de aprobación");
        try{              
            limpiarAction();
            this.setCatalogoFlujoAprobacion(super.getFacade().getPeriodoService().findPeriodoByFiltro(super.getNombreUsuario(), this.getTipoCuadro(), super.getFiltroPeriodo(), this.getEstadoCuadro(), VigenciaEnum.VIGENTE.getKey()));           
            this.setRenderFlujo(Boolean.TRUE);
        }catch(javax.ejb.EJBException e){
            agregarWarnMessage("El período consultado no existe");
            return null;                    
        } catch(Exception e){
            logger.error(e.getCause(), e);
            agregarErrorMessage(PropertyManager.getInstance().getMessage("workflow_mensaje_buscar_catalogo_error"));
        }
        return null;
    }
    
    public String guardarAction(){        
        List<HistorialVersionPeriodo> historialVersionPeriodoList = new ArrayList<HistorialVersionPeriodo>();
        HistorialVersionPeriodo historialVersionPeriodo = null;
        try{
            final List<VersionPeriodo> versionPeriodoValidateInRolList = this.validarCambioEstadoInRol(this.getCatalogoFlujoAprobacion());
            if(!versionPeriodoValidateInRolList.isEmpty()){
                agregarWarnMessage(PropertyManager.getInstance().getMessage("workflow_mensaje_sin_privilegios_cambiar_estado"));
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
    
    public void onChangeEstadoNotaHeader(ValueChangeEvent valueChangeEvent) {
        this.setSelectEstadoCuadroHeader((RichSelectOneChoice)valueChangeEvent.getSource());
        final EstadoCuadro estadoCuadro = (EstadoCuadro)this.getSelectEstadoCuadroHeader().getValue();
        if(estadoCuadro != null){
            List<VersionPeriodo> versionPeriodoList = new ArrayList<VersionPeriodo>();
            for(VersionPeriodo versionPeriodo : this.getCatalogoFlujoAprobacion()){
                versionPeriodo.setEstado(estadoCuadro);
                versionPeriodoList.add(versionPeriodo);
            }
            this.setCatalogoFlujoAprobacion(versionPeriodoList);
            AdfFacesContext.getCurrentInstance().addPartialTarget(this.getTablaCatalogo());
        }
    }
    
    public void onChangeEstadoNota(ValueChangeEvent valueChangeEvent){
        RichSelectOneChoice comboEstadoCuadro = (RichSelectOneChoice)valueChangeEvent.getSource();
        EstadoCuadro estado = (EstadoCuadro)comboEstadoCuadro.getValue();
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getTablaCatalogo());
    }
    
    public void onRowDisclousure(RowDisclosureEvent rowDisclosureEvent) {        
        try {            
            VersionPeriodo versionPeriodo = null;
            RichTable table = (RichTable)rowDisclosureEvent.getSource();
            Iterator it = table.getDisclosedRowKeys().iterator();
            if (it.hasNext()) {                
                Object rowKey = it.next();
                table.setRowIndex(Integer.parseInt(rowKey.toString()));
                versionPeriodo = (VersionPeriodo)table.getRowData();
                if(versionPeriodo != null){            
                    //nota.setHistorial((super.getFacadeNotas().getCatalogoNotaService().findHistorialByNota(nota)));            
                }                
            }
        } catch (Exception e) {            
            agregarErrorMessage("Error al obtener detalle de historial de Flujo");
            logger.error(e.getCause(),e);
        }
    }

    public void onSelectionRow(SelectionEvent selectionEvent) {
        final VersionPeriodo versionPeriodo = (VersionPeriodo)getTablaCatalogo().getSelectedRowData();
        this.setRowSelected(versionPeriodo);
    }
    
    public void reporteFlujoAprobacionAction(ActionEvent event){            
        try{
            final XSSFWorkbook wb = super.getFacade().getServiceReporte().createReporteFlujoAprobacion(super.getFacade().getPeriodoService().findPeriodoByFiltro(super.getNombreUsuario(), this.getTipoCuadro(), super.getFiltroPeriodo(), this.getEstadoCuadro(), VigenciaEnum.VIGENTE.getKey()));
            this.getReporteUtilBackingBean().setOuputStreamWorkBook(wb, "informe-flujo-aprobacion");
        } catch (Exception e) {
            agregarErrorMessage("Error al obtener reporte de Flujo de Aprobación");
            logger.error(e.getCause(),e);
        }           
    }
    
    private boolean validarCambioEstadoFlujo(final List<VersionPeriodo> versionPeriodoListNew, final List<VersionPeriodo> versionPeriodoListActual) throws Exception{
            boolean result = Boolean.TRUE;  
            sort(versionPeriodoListNew, on(VersionPeriodo.class).getIdPeriodo());
            sort(versionPeriodoListActual, on(VersionPeriodo.class).getIdPeriodo());            
            final Iterator<VersionPeriodo> it1 = versionPeriodoListNew.iterator();
            final Iterator<VersionPeriodo> it2 = versionPeriodoListActual.iterator();
            boolean titulo = true;
            while(it1.hasNext() && it2.hasNext()){
                VersionPeriodo versionPeriodoNew = it1.next();
                VersionPeriodo versionPeriodoActual = it2.next();
                
                if( versionPeriodoNew.getVersion().getCatalogo().getValidarEeff() != null &&
                    versionPeriodoNew.getVersion().getCatalogo().getValidarEeff().equals(VigenciaEnum.VIGENTE.getKey()) &&
                    versionPeriodoNew.getVersion().getValidadoEeff() != null &&
                    versionPeriodoNew.getVersion().getValidadoEeff().equals(VigenciaEnum.NO_VIGENTE.getKey())){
                    
                    if(!versionPeriodoNew.getEstado().getIdEstado().equals(EstadoCuadroEnum.INICIADO.getKey()) &&
                       !versionPeriodoNew.getEstado().getIdEstado().equals(EstadoCuadroEnum.MODIFICADO.getKey())){
                        
                        if(titulo){
                            super.agregarWarnMessage(PropertyManager.getInstance().getMessage("workflow_mensaje_prohibido_cambiar_estado"));
                            titulo = false;
                        }
                        
                        super.agregarWarnMessage(MessageFormat.format("{0} del estado: {1} hacia el estado {2} , debido a que no está validado",  
                                                                      versionPeriodoActual.getVersion().getCatalogo().getNombre(), 
                                                                      Util.capitalizar(versionPeriodoActual.getEstado().getNombre()), 
                                                                      Util.capitalizar(versionPeriodoNew.getEstado().getNombre())));                        
                        result = Boolean.FALSE;
                    }
                    
                }
                if(versionPeriodoActual.getEstado().getIdEstado().equals(EstadoCuadroEnum.CERRADO.getKey())){
                    if(!(versionPeriodoNew.getEstado().getIdEstado().equals(EstadoCuadroEnum.CONTINGENCIA.getKey())) &&
                       !(versionPeriodoNew.getEstado().getIdEstado().equals(EstadoCuadroEnum.CERRADO.getKey()))){
                        if(titulo){
                            super.agregarWarnMessage(PropertyManager.getInstance().getMessage("workflow_mensaje_prohibido_cambiar_estado"));
                            titulo = false;
                        }
                        super.agregarWarnMessage(MessageFormat.format("{0} del estado: {1} hacia el estado {2}",  versionPeriodoActual.getVersion().getCatalogo().getNombre(), Util.capitalizar(versionPeriodoActual.getEstado().getNombre()), Util.capitalizar(versionPeriodoNew.getEstado().getNombre())));                        
                        result = Boolean.FALSE;
                    }
                }
            }
            return result;
        }
    
    private List<VersionPeriodo> validarCambioEstadoInRol(List<VersionPeriodo> versionPeriodoList){
        List<VersionPeriodo> versionPeriodoValidateList = new ArrayList<VersionPeriodo>();        
        for(VersionPeriodo versionPeriodo : versionPeriodoList){
            if(versionPeriodo.getEstado().getIdEstado().equals(EstadoCuadroEnum.INICIADO.getKey())){
                if(!(getRequest().isUserInRole(Constantes.ROL_RESP)) &&
                   !(getRequest().isUserInRole(Constantes.ROL_SUP)) &&
                   !(getRequest().isUserInRole(Constantes.ROL_ENC)) &&
                   !(getRequest().isUserInRole(Constantes.ROL_ADMIN))){
                    versionPeriodoValidateList.add(versionPeriodo);
                }
            }
            if(versionPeriodo.getEstado().getIdEstado().equals(EstadoCuadroEnum.MODIFICADO.getKey())){
                if(!(getRequest().isUserInRole(Constantes.ROL_RESP)) && 
                   !(getRequest().isUserInRole(Constantes.ROL_SUP)) &&
                   !(getRequest().isUserInRole(Constantes.ROL_ENC)) &&
                   !(getRequest().isUserInRole(Constantes.ROL_ADMIN))){
                    versionPeriodoValidateList.add(versionPeriodo);
                }
            }
            if(versionPeriodo.getEstado().getIdEstado().equals(EstadoCuadroEnum.POR_APROBAR.getKey())){
                if(!(getRequest().isUserInRole(Constantes.ROL_RESP)) && 
                   !(getRequest().isUserInRole(Constantes.ROL_SUP)) &&
                   !(getRequest().isUserInRole(Constantes.ROL_ENC)) &&
                   !(getRequest().isUserInRole(Constantes.ROL_ADMIN))){
                    versionPeriodoValidateList.add(versionPeriodo);
                }
            }
            if(versionPeriodo.getEstado().getIdEstado().equals(EstadoCuadroEnum.APROBADO.getKey())){
                if(!(getRequest().isUserInRole(Constantes.ROL_RESP)) && 
                   !(getRequest().isUserInRole(Constantes.ROL_SUP)) &&
                   !(getRequest().isUserInRole(Constantes.ROL_ADMIN))){
                    versionPeriodoValidateList.add(versionPeriodo);
                }
            }
            if(versionPeriodo.getEstado().getIdEstado().equals(EstadoCuadroEnum.CERRADO.getKey())){
                if(!(getRequest().isUserInRole(Constantes.ROL_RESP)) && 
                   !(getRequest().isUserInRole(Constantes.ROL_ADMIN))){
                    versionPeriodoValidateList.add(versionPeriodo);
                }
            }
            if(versionPeriodo.getEstado().getIdEstado().equals(EstadoCuadroEnum.CONTINGENCIA.getKey())){
                if(!(getRequest().isUserInRole(Constantes.ROL_RESP)) && 
                   !(getRequest().isUserInRole(Constantes.ROL_ADMIN))){
                    versionPeriodoValidateList.add(versionPeriodo);
                }
            }        
        }
        return versionPeriodoValidateList;
    }
    
    private String getMensajeValidacionCuadros(List<VersionPeriodo> versionPeriodoValidateList){
        StringBuffer mensaje = new StringBuffer();
        for(VersionPeriodo versionPeriodo : versionPeriodoValidateList){
            agregarWarnMessage(versionPeriodo.getVersion().getCatalogo().getNombre().concat(" a estado ").concat(Util.capitalizar(versionPeriodo.getEstado().getNombre())));            
        }
        return mensaje.toString();
    }    
    
    public void setEstadoCuadro(EstadoCuadro estadoCuadro) {
        this.estadoCuadro = estadoCuadro;
    }

    public EstadoCuadro getEstadoCuadro() {
        return estadoCuadro;
    }

    public void setCatalogoFlujoAprobacion(List<VersionPeriodo> catalogoFlujoAprobacion) {
        this.catalogoFlujoAprobacion = catalogoFlujoAprobacion;
    }

    public List<VersionPeriodo> getCatalogoFlujoAprobacion() {
        return catalogoFlujoAprobacion;
    }

    public void setSelectEstadoCuadroHeader(RichSelectOneChoice selectEstadoCuadroHeader) {
        this.selectEstadoCuadroHeader = selectEstadoCuadroHeader;
    }

    public RichSelectOneChoice getSelectEstadoCuadroHeader() {
        return selectEstadoCuadroHeader;
    }

    public void setTablaCatalogo(RichTable tablaCatalogo) {
        this.tablaCatalogo = tablaCatalogo;
    }

    public RichTable getTablaCatalogo() {
        return tablaCatalogo;
    }

    public void setNombreArchivoExport(String nombreArchivoExport) {
        this.nombreArchivoExport = nombreArchivoExport;
    }

    public String getNombreArchivoExport() {
        nombreArchivoExport = "flujo-aprobacion-".concat(new SimpleDateFormat("dd-MM-yyyy_HH-mm").format(new Date())).concat(".xls");
        return nombreArchivoExport;
    }

    public void setRowSelected(VersionPeriodo rowSelected) {
        this.rowSelected = rowSelected;
    }

    public VersionPeriodo getRowSelected() {
        return rowSelected;
    }

    public void setTipoCuadro(TipoCuadro tipoCuadro) {
        this.tipoCuadro = tipoCuadro;
    }

    public TipoCuadro getTipoCuadro() {
        return tipoCuadro;
    }

    public void setRenderFlujo(boolean renderFlujo) {
        this.renderFlujo = renderFlujo;
    }

    public boolean isRenderFlujo() {
        return renderFlujo;
    }
}
