package cl.bicevida.revelaciones.mb;


import cl.bicevida.revelaciones.common.filtros.Filtro;
import cl.bicevida.revelaciones.common.mb.SoporteBackingBean;
import cl.bicevida.revelaciones.common.util.GeneradorDisenoHelper;
import cl.bicevida.revelaciones.common.util.PropertyManager;
import cl.bicevida.revelaciones.ejb.common.TipoEstructuraEnum;
import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.revelaciones.ejb.entity.Estructura;
import cl.bicevida.revelaciones.ejb.entity.Grilla;
import cl.bicevida.revelaciones.ejb.entity.HistorialVersionPeriodo;
import cl.bicevida.revelaciones.ejb.entity.Periodo;
import cl.bicevida.revelaciones.ejb.entity.SubGrilla;
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

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import javax.persistence.NoResultException;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;

import org.apache.log4j.Logger;


public class CuadroDesagregadoBackingBean extends SoporteBackingBean implements Serializable{
    private transient Logger logger = Logger.getLogger(CuadroDesagregadoBackingBean.class);
    @SuppressWarnings("compatibility:-7656952128317697995")
    private static final long serialVersionUID = -8524005303834461862L;
    
    public static final String BEAN_NAME = "cuadroDesagregadoBackingBean";
    
    private List<Estructura> estructuraList = new ArrayList<Estructura>(); 
    private Version versionSelected;
    private SoporteBackingBean soporteBackingBean;
    private VersionPeriodo versionPeriodoSelected;
    private String nombreArchivoExport;
    private boolean renderedPeriodoList = false;
    private boolean renderedInformacion = false;
    private transient RichTable periodoCatalogoTable;
    private List<SubGrilla> subGrillaList = new ArrayList<SubGrilla>();
   
    
    public CuadroDesagregadoBackingBean() {
        super();  
    }
    
    @PostConstruct
    public void cargarUltimaVersion(){
        try{
            getFiltro().setPeriodo(new Periodo(getComponenteBackingBean().getPeriodoActual()));
            versionSelected = getFacade().getVersionService().findUltimaVersionVigente(getFiltro().getPeriodo().getIdPeriodo(), getNombreUsuario(), getFiltro().getCatalogo().getIdCatalogo());
            getFiltro().setCatalogo(versionSelected.getCatalogo());
            getFiltro().setVersion(versionSelected);
            getComponenteBackingBean().setPeriodoCatalogoList(getFacade().getPeriodoService().findPeriodoAllByPeriodoCatalogo(versionSelected.getCatalogo(),getComponenteBackingBean().getPeriodoActual()));
            versionPeriodoSelected = getFacade().getPeriodoService().findVersionPeriodoById(getFiltro().getPeriodo().getIdPeriodo(), versionSelected.getIdVersion());
            estructuraList = getFacade().getEstructuraService().getSubGrillaByVersion(versionSelected, true, super.getFiltro().getSubGrilla());
            setListSubGrilla(estructuraList);
            renderedPeriodoList = true;
            renderedInformacion = true;
        }catch (FormulaException e){
            logger.error(e.getCause(), e);
            agregarErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_cuadro_formula_loop_error"));  
            agregarErrorMessage(e.getFormula());  
        }catch(Exception e){
            agregarErrorMessage("Error al cargar información de página");
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
                agregarErrorMessage("Error al consultar Versiones para el Período");
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
                System.out.println("Max Long -> " + Long.MAX_VALUE);
                VersionPeriodo versionPeriodo = this.getVersionPeriodoSelected() ;
                versionPeriodo.setFechaUltimoProceso(new Date());
                
                HistorialVersionPeriodo historialVersionPeriodo = new HistorialVersionPeriodo();
                historialVersionPeriodo.setVersionPeriodo(versionPeriodo);
                historialVersionPeriodo.setEstadoCuadro(versionPeriodo.getEstado());
                historialVersionPeriodo.setFechaProceso(new Date());
                historialVersionPeriodo.setUsuario(super.getNombreUsuario());
                historialVersionPeriodo.setComentario(PropertyManager.getInstance().getMessage("general_mensaje_historial_nota_datos_modificados"));
                
                super.getFacade().getEstructuraService().persistEstructuraSubGrilla(estructuraList, versionPeriodo, historialVersionPeriodo, this.getFiltro().getSubGrilla());
                List<Estructura> estructuraList = super.getFacade().getEstructuraService().getSubGrillaByVersion(super.getFiltro().getVersion(), true, super.getFiltro().getSubGrilla());
                super.getFacade().getGrillaService().consolidarGrilla(estructuraList);  
                setListSubGrilla(estructuraList);
                this.setEstructuraList(estructuraList);
                
                super.agregarSuccesMessage(PropertyManager.getInstance().getMessage("general_mensaje_nota_guardar_exito"));  
            }            
        } catch(Exception e){
            e.printStackTrace();
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

    public void validateHtml(FacesContext facesContext, UIComponent uIComponent, Object object) {
        final Integer MAX_TEXT_BYTES = Integer.parseInt(PropertyManager.getInstance().getMessage("constantes_max_text_bytes"));
        String html = (String) object;         
        if(cl.bicevida.revelaciones.ejb.cross.Util.getBytes(html).length >= MAX_TEXT_BYTES) {
            FacesMessage message = new FacesMessage();
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            message.setSummary(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_texto_maximo_permitido"), MAX_TEXT_BYTES));
            message.setDetail(null);        
            facesContext.addMessage(null, message);
            ((RichInputText)uIComponent).setValid(Boolean.FALSE);            
        }
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
        
        for(Estructura estructuras : getEstructuraList()){
            
            boolean valid = true;
            
            if(estructuras.getTipoEstructura().getIdTipoEstructura().equals(TipoEstructura.ESTRUCTURA_TIPO_GRILLA)){
                try {
                    
                    boolean result = getFacade().getFormulaService().processValidatorEEFF(estructuras.getGrillaVO().getGrilla());
                    
                    if(!result)
                        valid = false;
                    
                    if(valid)
                        agregarSuccesMessage("El cuadro ha sido correctamente validado, se ha cambiado el estado del cuadro");
                    else
                        agregarWarnMessage("El cuadro presenta diferencias con respecto del valor del estado financiero");
                        
                } catch (Exception e) {
                    agregarErrorMessage("Se ha producido un error al validar la celda");
                    logger.error(e.getMessage(),e);
                }
            }
        }
        
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


    public void setSubGrillaList(List<SubGrilla> subGrillaList) {
        this.subGrillaList = subGrillaList;
    }

    public List<SubGrilla> getSubGrillaList() {
        return subGrillaList;
    }

    
}
