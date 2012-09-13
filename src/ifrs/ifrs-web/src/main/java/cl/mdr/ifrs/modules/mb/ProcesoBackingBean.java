package cl.mdr.ifrs.modules.mb;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.FacesComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;
import org.primefaces.component.messages.Messages;
import org.primefaces.context.RequestContext;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.mb.ComponenteBackingBean;
import cl.mdr.ifrs.cross.mb.FiltroBackingBean;
import cl.mdr.ifrs.cross.util.GeneradorDisenoHelper;
import cl.mdr.ifrs.cross.util.PropertyManager;
import cl.mdr.ifrs.ejb.common.TipoEstructuraEnum;
import cl.mdr.ifrs.ejb.common.VigenciaEnum;
import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.AgrupacionColumna;
import cl.mdr.ifrs.ejb.entity.Catalogo;
import cl.mdr.ifrs.ejb.entity.Estructura;
import cl.mdr.ifrs.ejb.entity.Grilla;
import cl.mdr.ifrs.ejb.entity.HistorialVersion;
import cl.mdr.ifrs.ejb.entity.PeriodoEmpresa;
import cl.mdr.ifrs.ejb.entity.TipoEstructura;
import cl.mdr.ifrs.ejb.entity.Usuario;
import cl.mdr.ifrs.ejb.entity.Version;
import cl.mdr.ifrs.exceptions.FormulaException;
import cl.mdr.ifrs.vo.AgrupacionModelVO;

@ManagedBean(name="procesoBackingBean")
@ViewScoped
public class ProcesoBackingBean extends AbstractBackingBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6867816117743274288L;
	
	private Long idEmpresa;
	private Logger logger = Logger.getLogger(CuadroBackingBean.class);
	

	private List<Estructura> estructuraList;
	private ComponenteBackingBean componenteBackingBean;
	private List<Catalogo> catalogoList;
	private Catalogo selectedCuadro;
	private Catalogo nuevoCuadro;
	private boolean renderVersionList = false;
	private List<Version> versionList;
	private Version versionSeleccionada;	
	private boolean renderMensajePostConstructor; 
	
	@PostConstruct
	public void cargarCuadro(){		
		init();		
		try {			
			if(!isSelectedEmpresa())
	    		return;								
			try{
				
				PeriodoEmpresa periodoEmpresa = getFacadeService().getPeriodoService().getMaxPeriodoEmpresaByEmpresa(getFiltroBackingBean().getEmpresa().getIdRut());
				getFiltroBackingBean().setPeriodoEmpresa(periodoEmpresa);
				
			}catch(Exception e) {
				logger.error(e.getCause(), e);
	            super.addWarnMessage("El período consultado no existe");
	            return;
			}	
			
			versionList = getFacadeService().getVersionService().findVersionByCatalogoPeriodo(getFiltroBackingBean().getCatalogo().getIdCatalogo(),getFiltroBackingBean().getPeriodoEmpresa());
			versionSeleccionada = getFacadeService().getVersionService().findUltimaVersionVigente(getFiltroBackingBean().getPeriodoEmpresa().getIdPeriodo(), 
																								  getNombreUsuario(), getFiltroBackingBean().getCatalogo().getIdCatalogo());
			if(versionSeleccionada==null){
				addNotFoundMessage();
				return;
			}
			
			estructuraList = getFacadeService().getEstructuraService().getEstructuraByVersion(versionSeleccionada, true);
            
			if(!Util.esListaValida(getEstructuraList())){
                addWarnMessage(PropertyManager.getInstance().getMessage("general_mensaje_version_sin_registros"));
                return;
            }
			
            setListGrilla(estructuraList);
            this.renderVersionList = true;
			
		} catch (FormulaException e){
            logger.error(e.getCause(), e);
            super.addErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_cuadro_formula_loop_error"));  
            super.addErrorMessage(e.getFormula());
            this.setRenderMensajePostConstructor(true);
            return;
        } catch (Exception e) {
			logger.error(e.getMessage(), e);
			this.renderVersionList = false;
            super.addWarnMessage(MessageFormat.format(
            				PropertyManager.getInstance().getMessage("periodo_busqueda_sin_resultado_periodo"), 
		            		getFiltroBackingBean().getPeriodoEmpresa().getPeriodo().getAnioPeriodo(), 
		            		getFiltroBackingBean().getPeriodoEmpresa().getPeriodo().getMesPeriodo()));
		}
	}

    public Object buscarVersion(){
        
    	init();
    	
    	try{
    		if(!isSelectedEmpresa() || getFiltroBackingBean().getCatalogo().getIdCatalogo()==null)
    			return null;
    		super.displayPopUp("statusDialog", "f1");
    		FiltroBackingBean filtro = getFiltroBackingBean();    	
    	
    		Long periodo = Long.valueOf(filtro.getAnio().concat(filtro.getMes()));
    		Long idRut	 = filtro.getEmpresa().getIdRut();
    		
    		try{
                getFiltroBackingBean().setPeriodoEmpresa(getFacadeService().getPeriodoService().getPeriodoEmpresaById(periodo, idRut));
            }catch(Exception e){
            	logger.error(e.getMessage(), e);
            	addNotFoundMessage();
                return null;
            }
    		
    		try{
    			
    			versionList = getFacadeService().getVersionService().findVersionByCatalogoPeriodo(	filtro.getCatalogo().getIdCatalogo(), 
    																								filtro.getPeriodoEmpresa());
    		}catch(Exception e){
                logger.error(e.getCause(), e);
                addErrorMessage("Error al consultar Versiones para el Período");
            }
    		
			if(!Util.esListaValida(versionList)){  
				this.renderVersionList = false;
				addWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("periodo_busqueda_sin_resultado_periodo"), 
																							filtro.getMes(), 
																							filtro.getAnio()));
			}else 
				this.renderVersionList = true;
    	
    	}catch(Exception e){
    		logger.error(e.getMessage(), e);
    		addErrorMessage("Error al seleccionar el período");
    	}
    	
        return null;
    }
    
    public void buscarCuadro(ActionEvent event){
        try{
        	
        	super.displayPopUp("statusDialog", "f1");
        	versionSeleccionada = (Version)event.getComponent().getAttributes().get("version");
            
            estructuraList = getFacadeService().getEstructuraService().getEstructuraByVersion(versionSeleccionada, true);
            
            setListGrilla(estructuraList);
            
            if(getEstructuraList().isEmpty()){
                addWarnMessage(PropertyManager.getInstance().getMessage("general_mensaje_version_sin_registros"));
            }
            
        } catch (FormulaException e){
            logger.error(e.getCause(), e);
            addErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_cuadro_formula_loop_error"));  
            addErrorMessage(e.getFormula());  
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            addErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_nota_get_catalogo_error"));  
        }
    }
    
    public void displayLoaderCuadro(){
    	super.displayPopUp("statusDialog", "f1");
    }
    
    public void addRowListener(ActionEvent event) {
        
        Long idFila = Util.getLong(super.getExternalContext().getRequestParameterMap().get("idFila"), 0L);
        Long idGrilla = Util.getLong(super.getExternalContext().getRequestParameterMap().get("idGrilla"), 0L);

        if (idGrilla == 0L || idFila == 0L)
            return;

        for (int i = 0; i < getEstructuraList().size(); i++) {
            Estructura estructura = getEstructuraList().get(i);

            if (estructura.getTipoEstructura().getIdTipoEstructura().equals(TipoEstructuraEnum.GRILLA.getKey()) &&
                estructura.getGrilla().getIdGrilla().equals(idGrilla)) {
                if(estructura.getGrilla().getTipoFormula()==null || 
                   !estructura.getGrilla().getTipoFormula().equals(Grilla.TIPO_GRILLA_DINAMICA)){
                    addWarnMessage("No se puede agregar fila, primero debe ingresar fórmula dinámica en mantenedor de fórmulas");
                    return;
                }
                GeneradorDisenoHelper.agregarFilaGrillaByFilaSelected(estructura.getGrilla(), idFila);
                try {
                    estructura.setGrillaVO(getFacadeService().getEstructuraService().getGrillaVO(estructura.getGrilla(), true));
                } catch (Exception e) {
                    logger.error(e);
                    addErrorMessage("Se ha producido un error al agregar una fila al cuadro");
                }
            }

        }

        try {
            //Muestra las agrupaciones
            setListGrilla(getEstructuraList());

        } catch (Exception e) {
            logger.error(e);
            addErrorMessage("Se ha producido un error al agregar una fila al cuadro");
        }

    }
    
    public void deleteRowListener(ActionEvent event) {

        Long idFila = Util.getLong(super.getExternalContext().getRequestParameterMap().get("idFila"), 0L);
        Long idGrilla = Util.getLong(super.getExternalContext().getRequestParameterMap().get("idGrilla"), 0L);

        if (idGrilla == 0L || idFila == 0L)
            return;

        for (Estructura estructura : getEstructuraList()) {

            if (estructura.getTipoEstructura().getIdTipoEstructura().equals(TipoEstructuraEnum.GRILLA.getKey()) &&
                estructura.getGrilla().getIdGrilla().equals(idGrilla)) {
                if(estructura.getGrilla().getTipoFormula()==null || 
                   !estructura.getGrilla().getTipoFormula().equals(Grilla.TIPO_GRILLA_DINAMICA)){
                    addWarnMessage("No se puede eliminar fila, primero debe ingresar fórmula dinámica en mantenedor de fórmulas");
                    return;
                }
                if(GeneradorDisenoHelper.deleteRowValidator(estructura.getGrilla(), idFila)){
                    GeneradorDisenoHelper.eliminarUnaFila(estructura.getGrilla(), idFila);
                }
                try {
                    estructura.setGrillaVO(getFacadeService().getEstructuraService().getGrillaVO(estructura.getGrilla(),true));
                } catch (Exception e) {
                    logger.error(e);
                    addErrorMessage("Se ha producido un error al Eliminar una fila del Cuadro");
                }
            }

        }

        try {
            //Muestra las agrupaciones
            setListGrilla(getEstructuraList());

        } catch (Exception e) {
            logger.error(e);
            addErrorMessage("Se ha producido un error al Eliminar una fila del Cuadro");
        }
    }
    
    
    public String guardar() {
        try{
            //if(super.validaModificarCuadro(getVersionSeleccionada())){
                //super.getFacade().getEstructuraService().persistEstructuraList(estructuraList);
                
                //if(!super.validateContent(estructuraList))
                //    return null;
                Version version = this.getVersionSeleccionada() ;
                version.setFechaUltimoProceso(new Date());
                version.setDatosModificados(1L);
                
                HistorialVersion historialVersion = new HistorialVersion();
                historialVersion.setVersion(version);
                historialVersion.setEstadoCuadro(version.getEstado());
                historialVersion.setFechaProceso(new Date());
                historialVersion.setUsuario(new Usuario(super.getNombreUsuario()));
                historialVersion.setComentario(PropertyManager.getInstance().getMessage("general_mensaje_historial_nota_datos_modificados"));   

                super.getFacadeService().getEstructuraService().persistEstructura(estructuraList, version, historialVersion); 
                
                estructuraList = getFacadeService().getEstructuraService().getEstructuraByVersion(versionSeleccionada, true);
                
                setListGrilla(estructuraList);
                
                super.addInfoMessage(PropertyManager.getInstance().getMessage("general_mensaje_nota_guardar_exito"));  
            //}            
        } catch(Exception e){
            logger.error(e.getCause(), e);
            super.addErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_nota_guardar_error"));
        }
        return null;
    }
    
    
	
	//TODO cambiar metodo de posicion
    public void setListGrilla(List<Estructura> estructuraList) throws Exception {
    	
    	System.out.println("entro a metodo list grilla");
        
        for(Estructura estructuras : estructuraList){

            if(estructuras.getTipoEstructura().getIdTipoEstructura().equals(TipoEstructura.ESTRUCTURA_TIPO_GRILLA)){
            	Grilla grilla = estructuras.getGrilla();
            	
            	if(grilla==null)
            		continue;
            	estructuras.getGrillaVO().setTitulo(grilla.getTitulo());
            	estructuras.getGrillaVO().setCeldaList(GeneradorDisenoHelper.builHtmlGrilla(grilla.getColumnaList()));
            		
                List<AgrupacionColumna> agrupaciones = getFacadeService().getEstructuraService().findAgrupacionColumnaByGrilla(grilla);
                
                if(Util.esListaValida(agrupaciones)){
                	List<List<AgrupacionModelVO>> agrupacionesNivel = GeneradorDisenoHelper.crearAgrupadorHTMLVO(agrupaciones);
                	estructuras.getGrillaVO().setAgrupaciones(agrupacionesNivel);
                }
            }
        }
    }
    
    
    private void addNotFoundMessage(){
    	super.addWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("periodo_busqueda_sin_resultado_periodo"), 
    			getFiltroBackingBean().getPeriodoEmpresa().getPeriodo().getAnioPeriodo(), 
    			getFiltroBackingBean().getPeriodoEmpresa().getPeriodo().getMesPeriodo()));
        this.renderVersionList = false;  
    }

	public List<Catalogo> getCatalogoList() {
		return catalogoList;
	}

	public void setCatalogoList(List<Catalogo> catalogoList) {
		this.catalogoList = catalogoList;
	}

	public ComponenteBackingBean getComponenteBackingBean() {
		return componenteBackingBean;
	}

	public void setComponenteBackingBean(ComponenteBackingBean componenteBackingBean) {
		this.componenteBackingBean = componenteBackingBean;
	}
  
	public List<Estructura> getEstructuraList() {
		return estructuraList;
	}

	public void setEstructuraList(List<Estructura> estructuraList) {
		this.estructuraList = estructuraList;
	}

	public Catalogo getSelectedCuadro() {
		return selectedCuadro;
	}


	public void setSelectedCuadro(Catalogo selectedCuadro) {
		this.selectedCuadro = selectedCuadro;
	}


	public Catalogo getNuevoCuadro() {
		if (nuevoCuadro == null){
			nuevoCuadro = new Catalogo();
		}
		
		return nuevoCuadro;
	}


	public void setNuevoCuadro(Catalogo nuevoCuadro) {
		this.nuevoCuadro = nuevoCuadro;
	}


	public Long getIdEmpresa() {
		return idEmpresa;
	}


	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public List<Version> getVersionList() {
		return versionList;
	}

	public void setVersionList(List<Version> versionList) {
		this.versionList = versionList;
	}
    
	
    public boolean isRenderVersionList() {
		return renderVersionList;
	}


	public Version getVersionSeleccionada() {
		return versionSeleccionada;
	}


	public void setVersionSeleccionada(Version versionSeleccionada) {
		this.versionSeleccionada = versionSeleccionada;
	}
	
	public void init(){
		renderVersionList = false;
    	versionList = null;
    	versionSeleccionada = null;
        estructuraList = null;
	}
    
	
	 public void validarEstructuraListener(ActionEvent event){
	        
	        boolean valid = true;
	        this.guardar();
	        
	        for(Estructura estructuras : getEstructuraList()){
	            
	            if(estructuras.getTipoEstructura().getIdTipoEstructura().equals(TipoEstructura.ESTRUCTURA_TIPO_GRILLA)){
	                try {
	                    
	                    boolean result = getFacadeService().getFormulaService().processValidatorEEFF(estructuras.getGrillaVO().getGrilla());
	                    
	                    if(!result)
	                        valid = false;
	                    
	                } catch (Exception e) {
	                    addErrorMessage(PropertyManager.getInstance().getMessage("general_error_validar"));
	                    logger.error(e.getMessage(),e);
	                }
	            }
	        }
	        try{
	            actualizarValidado(valid ? VigenciaEnum.VIGENTE.getKey() : VigenciaEnum.NO_VIGENTE.getKey());
	        } catch (Exception e) {
	        	addErrorMessage(PropertyManager.getInstance().getMessage("general_error_validar_estado"));
	            logger.error(e.getMessage(), e);
	        }
	        
	        if(valid){
	            addInfoMessage(PropertyManager.getInstance().getMessage("general_mensaje_validado_ok"));
	        }else
	            addWarnMessage(PropertyManager.getInstance().getMessage("general_mensaje_validado_mal"));
	        
	    }
	 
	 	private void actualizarValidado(Long valido){
	 		versionSeleccionada.setValidadoEeff(valido);	        
	        getFacadeService().getVersionService().mergeEntity(versionSeleccionada);
	    }
	 
	 	
	 	public boolean isRenderMensajePostConstructor() {
			return renderMensajePostConstructor;
		}

		public void setRenderMensajePostConstructor(boolean renderMensajePostConstructor) {
			this.renderMensajePostConstructor = renderMensajePostConstructor;
		}
}
