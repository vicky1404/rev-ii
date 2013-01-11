package cl.bicevida.revelaciones.mb;


import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;

import cl.bicevida.revelaciones.common.filtros.Filtro;
import cl.bicevida.revelaciones.common.mb.SoporteBackingBean;
import cl.bicevida.revelaciones.common.util.GeneradorDisenoHelper;
import cl.bicevida.revelaciones.common.util.PropertyManager;
import cl.bicevida.revelaciones.ejb.common.TipoEstructuraEnum;
import cl.bicevida.revelaciones.ejb.common.VigenciaEnum;
import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.revelaciones.ejb.entity.Catalogo;
import cl.bicevida.revelaciones.ejb.entity.Estructura;
import cl.bicevida.revelaciones.ejb.entity.Grilla;
import cl.bicevida.revelaciones.ejb.entity.HistorialVersionPeriodo;
import cl.bicevida.revelaciones.ejb.entity.Periodo;
import cl.bicevida.revelaciones.ejb.entity.RelacionDetalleEeff;
import cl.bicevida.revelaciones.ejb.entity.RelacionEeff;
import cl.bicevida.revelaciones.ejb.entity.TipoEstructura;
import cl.bicevida.revelaciones.ejb.entity.Version;
import cl.bicevida.revelaciones.ejb.entity.VersionPeriodo;
import cl.bicevida.revelaciones.exceptions.FormulaException;

import java.io.Serializable;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import javax.ejb.EJBException;

import javax.faces.event.ActionEvent;

import javax.persistence.NoResultException;

import oracle.adf.view.rich.component.rich.data.RichTable;

import org.apache.log4j.Logger;

import static org.hamcrest.Matchers.equalTo;


public class CuadroBackingBean extends SoporteBackingBean implements Serializable{
    private transient Logger logger = Logger.getLogger(CuadroBackingBean.class);
    @SuppressWarnings("compatibility:-7656952128317697995")
    private static final long serialVersionUID = -8524005303834461862L;
    
    public static final String BEAN_NAME = "cuadroBackingBean";
    
    private List<Estructura> estructuraList = new ArrayList<Estructura>(); 
    private Version versionSelected;
    private SoporteBackingBean soporteBackingBean;
    private VersionPeriodo versionPeriodoSelected;
    private String nombreArchivoExport;
    private boolean renderedPeriodoList = false;
    private boolean renderedInformacion = false;
    private transient RichTable periodoCatalogoTable;
    private List<RelacionEeff> relEeffList;
    private List<RelacionDetalleEeff> relDetEeffList;
    
    public CuadroBackingBean() {
        super();  
    }
    
    @PostConstruct
    public void cargarUltimaVersion(){
        try{
            getFiltro().setPeriodo(new Periodo(getComponenteBackingBean().getPeriodoActual()));
            if(getFiltro().getCatalogo()==null || getFiltro().getCatalogo().getIdCatalogo()==null)
                return;
            versionSelected = getFacade().getVersionService().findUltimaVersionVigente(getFiltro().getPeriodo().getIdPeriodo(), getNombreUsuario(), getFiltro().getCatalogo().getIdCatalogo());
            getFiltro().setCatalogo(versionSelected.getCatalogo());
            getFiltro().setVersion(versionSelected);
            getComponenteBackingBean().setPeriodoCatalogoList(getFacade().getPeriodoService().findPeriodoAllByPeriodoCatalogo(versionSelected.getCatalogo(),getComponenteBackingBean().getPeriodoActual()));
            versionPeriodoSelected = getFacade().getPeriodoService().findVersionPeriodoById(getFiltro().getPeriodo().getIdPeriodo(), versionSelected.getIdVersion());
            estructuraList = getFacade().getEstructuraService().getEstructuraByVersion(versionSelected, true);
            setListGrilla(estructuraList);
            renderedPeriodoList = true;
            renderedInformacion = true;
        }catch (FormulaException e){
            logger.error(e.getCause(), e);
            agregarErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_cuadro_formula_loop_error"));  
            agregarErrorMessage(e.getFormula());  
        }catch(Exception e){
            agregarErrorMessage(PropertyManager.getInstance().getMessage("general_error_cargar_pagina"));
            logger.error(e.getMessage());
        }
    }
    
    public Object buscarVersion(){
        
        Filtro filtroPaso = getFiltro();
        filtroPaso.getPeriodo().setPeriodo(null);
        getComponenteBackingBean().setPeriodoCatalogoList(null);
        getCuadroBackingBean().setEstructuraList(null);

        if(filtroPaso.getCatalogo().getIdCatalogo()!=null){
            try{
                this.renderedPeriodoList = true;
                Long periodo = Long.valueOf(filtroPaso.getPeriodo().getAnioPeriodo().concat(filtroPaso.getPeriodo().getMesPeriodo()));                
                try{
                    getFiltro().setPeriodo(getFacade().getMantenedoresTipoService().findByPeriodo(periodo));
                }catch(NoResultException e){
                    agregarWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("periodo_busqueda_sin_resultado_periodo"), getFiltro().getPeriodo().getAnioPeriodo(), getFiltro().getPeriodo().getMesPeriodo()));
                    getComponenteBackingBean().setPeriodoCatalogoList(null);
                    this.renderedPeriodoList = false;
                    return null;                    
                }catch(EJBException e){
                    agregarWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("periodo_busqueda_sin_resultado_periodo"), getFiltro().getPeriodo().getAnioPeriodo(), getFiltro().getPeriodo().getMesPeriodo()));
                    getComponenteBackingBean().setPeriodoCatalogoList(null);
                    this.renderedPeriodoList = false;
                    return null;
                }
                getComponenteBackingBean().setPeriodoCatalogoList(getFacade().getPeriodoService().findPeriodoAllByPeriodoCatalogo(filtroPaso.getCatalogo(),filtroPaso.getPeriodo()));
                if(getComponenteBackingBean().getPeriodoCatalogoList() == null){  
                    this.renderedPeriodoList = false;
                    agregarWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("periodo_busqueda_sin_resultado_periodo"), getFiltro().getPeriodo().getAnioPeriodo(), getFiltro().getPeriodo().getMesPeriodo()));
                }
            }catch(Exception e){
                logger.error(e.getCause(), e);
                agregarErrorMessage(PropertyManager.getInstance().getMessage("general_error_cargar_versiones"));
            }
            
        }
        
        return null;
    }
    
    public String buscarCuadro(){
        try{
            setEstructuraList(null);
            
            setVersionPeriodoSelected((VersionPeriodo)getPeriodoCatalogoTable().getRowData());
            setRenderedInformacion(Boolean.TRUE);
            getFiltro().setVersion(this.getVersionPeriodoSelected().getVersion());
            List<Estructura> estructuraList = super.getFacade().getEstructuraService().getEstructuraByVersion(super.getFiltro().getVersion(), true);
            
            setListGrilla(estructuraList);
            
            setEstructuraList(estructuraList);
            
            if(getEstructuraList().isEmpty()){
                agregarWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_version_sin_registros"), 
                                                        getVersionPeriodoSelected().getVersion().getCatalogo().getNombre(),
                                                        getVersionPeriodoSelected().getPeriodo().getAnioPeriodo(), 
                                                        getVersionPeriodoSelected().getPeriodo().getMesPeriodo() , 
                                                        getVersionPeriodoSelected().getVersion().getVersion()));
            }
        } catch (FormulaException e){
            logger.error(e.getCause(), e);
            agregarErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_cuadro_formula_loop_error"));  
            agregarErrorMessage(e.getFormula());  
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            agregarErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_nota_get_catalogo_error"));  
        }
        return null;
    }
    
    /**
     * Guarda en la BDD una catalogos
     * @return
     */
    public String guardar() {
        try{
            if(super.validaModificarCuadro(this.getVersionPeriodoSelected())){
                
                VersionPeriodo versionPeriodo = this.getVersionPeriodoSelected() ;
                versionPeriodo.setFechaUltimoProceso(new Date());
                
                HistorialVersionPeriodo historialVersionPeriodo = new HistorialVersionPeriodo();
                historialVersionPeriodo.setVersionPeriodo(versionPeriodo);
                historialVersionPeriodo.setEstadoCuadro(versionPeriodo.getEstado());
                historialVersionPeriodo.setFechaProceso(new Date());
                historialVersionPeriodo.setUsuario(super.getNombreUsuario());
                historialVersionPeriodo.setComentario(PropertyManager.getInstance().getMessage("general_mensaje_historial_nota_datos_modificados"));
                
                super.getFacade().getEstructuraService().persistEstructura(estructuraList, versionPeriodo, historialVersionPeriodo);
                List<Estructura> estructuraList = super.getFacade().getEstructuraService().getEstructuraByVersion(super.getFiltro().getVersion(), true);
                setListGrilla(estructuraList);
                this.setEstructuraList(estructuraList);
                if(versionSelected.getValidadoEeff()!=null && versionSelected.getValidadoEeff().equals(VigenciaEnum.VIGENTE.getKey())){
                    actualizarValidado(VigenciaEnum.NO_VIGENTE.getKey());
                }
                super.agregarSuccesMessage(PropertyManager.getInstance().getMessage("general_mensaje_nota_guardar_exito"));
            }            
        } catch(Exception e){
            logger.error(e.getCause(), e);
            super.agregarErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_nota_guardar_error"));
        }
        return null;
    }
    
    public void agregarFilaAction(ActionEvent event){
        logger.info(event.getSource());  
    }
    
    public void eliminarFilaAction(ActionEvent event){
        logger.info(event.getSource());
    }
    
    public void setEstructuraList(List<Estructura> estructuraList) {
        this.estructuraList = estructuraList;
    }

    public List<Estructura> getEstructuraList(){        
        return estructuraList;
    }

    public void setVersionSelected(Version versionSelected) {
        this.versionSelected = versionSelected;
    }

    public Version getVersionSelected() {        
        return super.getFiltro().getVersion();
    }

    public void setSoporteBackingBean(SoporteBackingBean soporteBackingBean) {
        this.soporteBackingBean = soporteBackingBean;
    }

    public void setVersionPeriodoSelected(VersionPeriodo versionPeriodoSelected) {
        this.versionPeriodoSelected = versionPeriodoSelected;
    }

    public VersionPeriodo getVersionPeriodoSelected() {
        return versionPeriodoSelected;
    }

    public void setNombreArchivoExport(String nombreArchivoExport) {
        this.nombreArchivoExport = nombreArchivoExport;
    }

    public String getNombreArchivoExport() {
        nombreArchivoExport = "informe-revelaciones-".concat(new SimpleDateFormat("dd-MM-yyyy_HH-mm").format(new Date())).concat(".xls");
        return nombreArchivoExport;
    }

    public void addRowListener(ActionEvent event) {
        
        Long idFila = Util.getLong(super.getExternalContext().getRequestParameterMap().get("idFila"), 0L);
        Long idGrilla = Util.getLong(super.getExternalContext().getRequestParameterMap().get("idGrilla"), 0L);

        if (idGrilla == 0L || idFila == 0L)
            return;

        for (int i = 0; i < getEstructuraList().size(); i++) {
            Estructura estructura = getEstructuraList().get(i);

            if (estructura.getTipoEstructura().getIdTipoEstructura().equals(TipoEstructuraEnum.GRILLA.getKey()) &&
                estructura.getGrillaVO().getGrilla().getIdGrilla().equals(idGrilla)) {
                if(estructura.getGrillaVO().getGrilla().getTipoFormula()==null || 
                   !estructura.getGrillaVO().getGrilla().getTipoFormula().equals(Grilla.TIPO_GRILLA_DINAMICA)){
                    agregarWarnMessage("No se puede agregar fila, primero debe ingresar formula dinámica en mantenedor de formulas");
                    return;
                }
                
                GeneradorDisenoHelper.agregarFilaGrillaByFilaSelected(estructura.getGrillaVO().getGrilla(), idFila);
                
                try {
                    estructura.setGrillaVO(getFacade().getEstructuraService().getGrillaVO(estructura.getGrillaVO().getGrilla(), true));
                } catch (Exception e) {
                    logger.error(e);
                    agregarErrorMessage("Se ha producido un error al agregar una fila al cuadro");
                }
            }

        }
        
        try {
            //Muestra las agrupaciones
            setListGrilla(getEstructuraList());

        } catch (Exception e) {
            logger.error(e);
            super.agregarErrorMessage("Se ha producido un error al agregar una fila al cuadro");
        }

    }
    
    public void deleteRowListener(ActionEvent event) {

        Long idFila = Util.getLong(super.getExternalContext().getRequestParameterMap().get("idFila"), 0L);
        Long idGrilla = Util.getLong(super.getExternalContext().getRequestParameterMap().get("idGrilla"), 0L);

        if (idGrilla == 0L || idFila == 0L)
            return;

        for (Estructura estructura : getEstructuraList()) {

            if (estructura.getTipoEstructura().getIdTipoEstructura().equals(TipoEstructuraEnum.GRILLA.getKey()) &&
                estructura.getGrillaVO().getGrilla().getIdGrilla().equals(idGrilla)) {
                if(estructura.getGrillaVO().getGrilla().getTipoFormula()==null || 
                   !estructura.getGrillaVO().getGrilla().getTipoFormula().equals(Grilla.TIPO_GRILLA_DINAMICA)){
                    agregarWarnMessage("No se puede eliminar fila, primero debe ingresar formula dinámica en mantenedor de formulas");
                    return;
                }
                if(GeneradorDisenoHelper.deleteRowValidator(estructura.getGrillaVO().getGrilla(), idFila)){
                    GeneradorDisenoHelper.eliminarUnaFila(estructura.getGrillaVO().getGrilla(), idFila);
                }
                try {
                    estructura.setGrillaVO(getFacade().getEstructuraService().getGrillaVO(estructura.getGrillaVO().getGrilla(),true));
                } catch (Exception e) {
                    logger.error(e);
                    super.agregarErrorMessage("Se ha producido un error al Eliminar una fila del Cuadro");
                }
            }

        }

        try {
            //Muestra las agrupaciones
            setListGrilla(getEstructuraList());

        } catch (Exception e) {
            logger.error(e);
            super.agregarErrorMessage("Se ha producido un error al Eliminar una fila del Cuadro");
        }
    }
    
    
    public void validarEstructuraListener(ActionEvent event){        
        boolean valid = true; 
        this.guardar();
        int validaMapping = this.validaMapping(); 
        int countGrillas = select(this.getEstructuraList() ,having(on(Estructura.class).getTipoEstructura().getIdTipoEstructura(), equalTo(TipoEstructura.ESTRUCTURA_TIPO_GRILLA))).size();
        if(validaMapping == countGrillas){
            return;
        }            
        for(Estructura estructuras : this.getEstructuraList()){            
            if(estructuras.getTipoEstructura().getIdTipoEstructura().equals(TipoEstructura.ESTRUCTURA_TIPO_GRILLA)){
                try {                    
                    boolean result = getFacade().getFormulaService().processValidatorEEFF(estructuras.getGrillaVO().getGrilla());                    
                    if(!result)
                        valid = false;
                    
                } catch (Exception e) {
                    agregarErrorMessage(PropertyManager.getInstance().getMessage("general_error_validar"));
                    logger.error(e.getMessage(),e);
                }
            }
        }
        try{            
            actualizarValidado(valid ? VigenciaEnum.VIGENTE.getKey() : VigenciaEnum.NO_VIGENTE.getKey());
        } catch (Exception e) {
            agregarErrorMessage(PropertyManager.getInstance().getMessage("general_error_validar_estado"));
            logger.error(e.getMessage(), e);
        }
        
        if(valid){
            agregarSuccesMessage(PropertyManager.getInstance().getMessage("general_mensaje_validado_ok"));
        }else{
            agregarWarnMessage(PropertyManager.getInstance().getMessage("general_mensaje_validado_mal"));
        }
    }
    
    private int validaMapping(){
        int count = 0;
        for(Estructura estructura : this.getEstructuraList()){   
            if(estructura.getTipoEstructura().getIdTipoEstructura().equals(TipoEstructura.ESTRUCTURA_TIPO_GRILLA)){
                if(!super.getFacade().getEstadoFinancieroService().validaContieneMapping(this.getFiltro().getPeriodo().getIdPeriodo(), estructura.getIdEstructura())){                    
                        super.agregarWarnMessage(MessageFormat.format("El cuadro {0} de la Revelación {1}, no tiene configurada las asociaciones necesarias para validar contra los Estados Financieros.", 
                                                                       estructura.getOrden(), estructura.getVersion().getCatalogo().getNombre()));
                        count++;
                }
            }            
        }
        return count;
    }
    
    private void actualizarValidado(Long valido){
        versionSelected.setValidadoEeff(valido);
        versionPeriodoSelected.setVersion(versionSelected);
        getFacade().getVersionService().mergeEntity(versionSelected);
    }
    
    public void setRenderedPeriodoList(boolean renderedPeriodoNotaList) {
        this.renderedPeriodoList = renderedPeriodoNotaList;
    }

    public boolean isRenderedPeriodoList() {
        return renderedPeriodoList;
    }
    
    public void setRenderedInformacion(boolean renderedInformacionNota) {
        this.renderedInformacion = renderedInformacionNota;
    }

    public boolean isRenderedInformacion() {
        return renderedInformacion;
    }
    
    public void setPeriodoCatalogoTable(RichTable periodoNotaTable) {
        this.periodoCatalogoTable = periodoNotaTable;
    }

    public RichTable getPeriodoCatalogoTable() {
        return periodoCatalogoTable;
    }


    public void setRelEeffList(List<RelacionEeff> relEeffList) {
        this.relEeffList = relEeffList;
    }

    public List<RelacionEeff> getRelEeffList() {
        return relEeffList;
    }

    public void setRelDetEeffList(List<RelacionDetalleEeff> relDetEeffList) {
        this.relDetEeffList = relDetEeffList;
    }

    public List<RelacionDetalleEeff> getRelDetEeffList() {
        return relDetEeffList;
    }
}
