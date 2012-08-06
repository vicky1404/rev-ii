package cl.mdr.ifrs.modules.configuracion.mb;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.notNullValue;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.util.GeneradorDisenoHelper;
import cl.mdr.ifrs.ejb.common.VigenciaEnum;
import cl.mdr.ifrs.ejb.cross.SortHelper;
import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.AgrupacionColumna;
import cl.mdr.ifrs.ejb.entity.Catalogo;
import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.ifrs.ejb.entity.Columna;
import cl.mdr.ifrs.ejb.entity.Estructura;
import cl.mdr.ifrs.ejb.entity.Grilla;
import cl.mdr.ifrs.ejb.entity.TipoCuadro;
import cl.mdr.ifrs.ejb.entity.TipoEstructura;
import cl.mdr.ifrs.ejb.entity.Version;
import cl.mdr.ifrs.model.EstructuraModel;
import cl.mdr.ifrs.vo.AgrupacionModelVO;


@ManagedBean(name = "generadorVersionBackingBean")
@ViewScoped
public class GeneradorVersionBackingBean extends AbstractBackingBean{
	
	private transient Logger LOG = Logger.getLogger(GeneradorVersionBackingBean.class);
	public static final String FORM_NAME_PRINCIPAL = "fGV1";
	
	private TipoCuadro tipoCuadro;
	private Long idTipoCuadro;
	

	private Catalogo catalogo;
	private Version versionEditable;
	private Version versionClonable;
	private Estructura estructuraEditable;
	private List<Catalogo> catalogoList;
	private List<Version> versionList;
	private List<Estructura> estructuraList;
	private boolean almacenado = false;
	private DataTable estructuraTable;
	private DataTable versionTable;
	private boolean renderBotonEditar;
	private boolean renderEstructura;
	private boolean renderBotonEditarVersion;	
	
	@ManagedProperty(value="#{configuradorDisenoBackingBean}")
    private ConfiguradorDisenoBackingBean configuradorDisenoBackingBean;
	
	public void buscarListener(ActionEvent event){
		try{			
			if(catalogo!=null){
				this.setVersionList(super.getFacadeService().getVersionService().findVersionAllByIdCatalogo(catalogo.getIdCatalogo()));
				SortHelper.sortVersionDesc(this.getVersionList());
			}else{
				super.addInfoMessage("No existen versiones para Catálogo seleccionado");
			}
		}catch(Exception e){
			super.addErrorMessage("Se ha producido un error al buscar desde el Catálogo para configurar el Cuadro");
			LOG.error(e);
		}
		
	}

	public List<Catalogo> completeCatalogo(String query) {		
		List<Catalogo> suggestions = new ArrayList<Catalogo>();  		
		if(getCatalogoList()==null)
			return suggestions;		        
        for(Catalogo p : getCatalogoList()) {  
        	if(p.getNombre().toUpperCase().indexOf(query.toUpperCase()) >= 0)
                suggestions.add(p);  
        }            
        return suggestions;  
    }
	
	
	public void tipoCuadroChangeValue(ValueChangeEvent event){
		System.out.println("tipoCuadroChangeValue");
	}
	
	
	public void tipoCuadroChange(){
		try{								
			if(tipoCuadro != null)
				catalogoList = getFacadeService().getCatalogoService().findCatalogoByFiltro(getFiltroBackingBean().getEmpresa().getIdRut(), getNombreUsuario(), this.getTipoCuadro() , null, 1L);
		}catch(Exception e){
			addErrorMessage("Error al buscar Catálogo");
			LOG.error(e);
		}
		
	}	
	
    public List<Estructura> getEstructuraList() {        
    	if(estructuraList==null){
        	estructuraList = new ArrayList<Estructura>();
            Estructura estructura = new Estructura();
            estructura.setOrden(1L);
            estructuraList.add(estructura);
        }    	
        return estructuraList;
    }
    
    public int getSizeEstructrua(){
        return this.getEstructuraList().size();
    }
    
    public void agregarTipoEstructuraListener(ActionEvent actionEvent) {
        setAlmacenado(false);
        final Estructura estructuraSelected = (Estructura)actionEvent.getComponent().getAttributes().get("estructura");        
        List<Estructura> estructuras = new ArrayList<Estructura>();
        Estructura estructura;
        Long i=0L;
        
        Map<Long, EstructuraModel> estructuraModelMap = this.getConfiguradorDisenoBackingBean().getEstructuraModelMap();
        Map<Long, EstructuraModel> estructuraModelPasoMap = new LinkedHashMap<Long, EstructuraModel>();
                
        for(Estructura estructuraI : getEstructuraList()){
            i++;
            
            if(this.getConfiguradorDisenoBackingBean().getEstructuraModelMap().containsKey(estructuraI.getOrden())){
            	estructuraModelPasoMap.put(i, this.getConfiguradorDisenoBackingBean().getEstructuraModelMap().get(estructuraI.getOrden()));
            }
            else{
            	estructuraModelPasoMap.put(i, new EstructuraModel());
            }
            
            estructuraI.setOrden(i);
            estructuras.add(estructuraI);

            if(estructuraI.getOrden().equals(estructuraSelected.getOrden())){                
                i++;
                estructuraModelPasoMap.put(i, new EstructuraModel());
                estructura = new Estructura();
                estructura.setOrden(i);
                estructuras.add(estructura);
            }

        }
        estructuraModelMap.putAll(estructuraModelPasoMap);
        estructuraModelPasoMap.clear();
        setEstructuraList(estructuras);
    }
        
    public void eliminarTipoEstructuraListener(ActionEvent actionEvent) {
    	final Estructura estructuraSelected = (Estructura)actionEvent.getComponent().getAttributes().get("estructura");
    	this.setEstructuraEditable(estructuraSelected);
    	if(this.getEstructuraEditable().getIdEstructura() != null){
    		this.displayPopUp("dialogNotDeleteEstr", "gVdt3");  
    		return;
    	}
        setAlmacenado(false);
        if(getEstructuraList().size()<=1){
            return;
        }        
        this.getEstructuraList().remove(this.getEstructuraEditable());
        this.getConfiguradorDisenoBackingBean().getEstructuraModelMap().remove(this.getEstructuraEditable().getOrden());
        Long i=1L;
        for(Estructura estructura : getEstructuraList()){
            EstructuraModel estructuraModel = this.getConfiguradorDisenoBackingBean().getEstructuraModelMap().get(estructura.getOrden());
            this.getConfiguradorDisenoBackingBean().getEstructuraModelMap().put(i, estructuraModel);
            estructura.setOrden(i);
            i++;
        }       
    }
            
    /*
     * Metodo que busca las estructura al hacer clink en el icono cargar
     */
    public void buscarEstructuraActionListener(ActionEvent event){ 
    	this.setEstructuraList(null);    	
    	this.setVersionEditable(((Version)event.getComponent().getAttributes().get("version")));
    	if(this.getVersionEditable() == null){
            return;
        }
    	if(this.getVersionEditable().getVigencia().equals(VigenciaEnum.NO_VIGENTE.getKey())){
    		super.addWarnMessage("Esta Versión no puede ser modificada por se encuentra en un estado no vigente");
    		this.setEstructuraList(null);
    		return;
    	}
    	
    	if(Util.esListaValida(this.getVersionList()) && this.getVersionEditable().getVigencia().equals(VigenciaEnum.VIGENTE.getKey())){
            try {                                                               
                //final List<Estructura> estructuras = super.getFacadeService().getEstructuraService().findEstructuraByVersion(version);   
                final List<Estructura> estructuras = super.getFacadeService().getEstructuraService().getEstructuraByVersion(this.getVersionEditable(), false);
                if(estructuras==null || estructuras.size()==0){
                    this.setEstructuraList(null);
                    return;
                }
                for(Estructura estructura : estructuras){
                    if(estructura.getTipoEstructura().getIdTipoEstructura().equals(TipoEstructura.ESTRUCTURA_TIPO_GRILLA)){
                    	Grilla grilla = estructura.getGrilla();                    	
                    	if(grilla==null){
                    		continue;
                    	}
                    	final List<Celda> celdaList = buildCeldaListByColumnas(grilla.getColumnaList());
                    	if(this.tieneFormulaEstatica(celdaList) || this.tieneFormulaDinamica(celdaList)){
                    		super.addWarnMessage("Esta Versión no puede ser modificada por que tiene Fórmulas Configuradas");
                    		this.setEstructuraList(null);
                    		return;
                    	}
                    	estructura.getGrillaVO().setCeldaList(GeneradorDisenoHelper.builHtmlGrilla(grilla.getColumnaList()));                    		
                        final List<AgrupacionColumna> agrupaciones = getFacadeService().getEstructuraService().findAgrupacionColumnaByGrilla(grilla);                        
                        if(Util.esListaValida(agrupaciones)){
                        	List<List<AgrupacionModelVO>> agrupacionesNivel = GeneradorDisenoHelper.crearAgrupadorHTMLVO(agrupaciones);
                        	estructura.getGrillaVO().setAgrupaciones(agrupacionesNivel);
                        }
                    }
                }
                //this.getConfiguradorDisenoBackingBean().setEstructuraModelMap(GeneradorDisenoHelper.createEstructuraModel(estructuras));                                
                this.setAlmacenado(true);
                this.setRenderBotonEditar(false);
                this.setRenderBotonEditarVersion(Boolean.TRUE);
                this.setEstructuraList(estructuras);                
            } catch (Exception e) {
                super.addErrorMessage("Error al obtener información");
                LOG.error(e.getMessage(),e);
            }            
            this.setRenderEstructura(true);
        }
    }
    
    private List<Celda> buildCeldaListByColumnas(List<Columna> columnaList){
    	List<Celda> celdaList = new ArrayList<Celda>();
    	for(Columna columna : columnaList){
    		for(Celda celda : columna.getCeldaList()){
    			celdaList.add(celda);
    		}
    	}
    	return celdaList;
    }
    
    /**
     * Evalua si una grilla contiene formulas dinamicas
     * @return
     */
    public boolean tieneFormulaDinamica(final List<Celda> celdas){        
        final List<Celda> celdaParentVerticalList = select(celdas ,having(on(Celda.class).getChildVertical(), notNullValue()));
        final List<Celda> celdaParentHorizontalList = select(celdas ,having(on(Celda.class).getChildHorizontal(), notNullValue()));
        if((celdaParentVerticalList != null || celdaParentHorizontalList != null) && (celdaParentVerticalList.size() > 0 || celdaParentHorizontalList.size() > 0)){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }
    
    /**
     * Evalua si una grilla contiene formulas estaticas
     * @return
     */
    public boolean tieneFormulaEstatica(final List<Celda> celdas){        
        final List<Celda> celdaFormulaList = select(celdas ,having(on(Celda.class).getFormula(), notNullValue()));
        if((celdaFormulaList != null) && (celdaFormulaList.size() > 0)){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }

    
    public String guardarEstructura(){                       
        for(int i=versionList.size(); i==1; i--){
        	versionList.get(i).setCatalogo(catalogo);
        	versionList.get(i).setVigencia(0L);        	
        } 
               
        Version nuevaVersion = this.getVersionList().iterator().next();
        nuevaVersion.setVigencia(1L);
        nuevaVersion.setCatalogo(catalogo);        
                                               
        for(Estructura estructura : getEstructuraList()){
            estructura.setVersion(nuevaVersion);            
            if(this.getConfiguradorDisenoBackingBean().getEstructuraModelMap().containsKey(estructura.getOrden())){
            	this.getConfiguradorDisenoBackingBean().getEstructuraModelMap().get(estructura.getOrden()).setTipoEstructura(estructura.getTipoEstructura().getIdTipoEstructura());
            }else{
            	this.getConfiguradorDisenoBackingBean().getEstructuraModelMap().put(estructura.getOrden(), new EstructuraModel(estructura.getTipoEstructura().getIdTipoEstructura()));
            }
        }
        
        this.setAlmacenado(true);        
        return "";
    }
    
    public String editarEstructura(){
    	if(this.getVersionEditable() == null){
    		super.addWarnMessage("Debe seleccionar una Versión para editar");
    		return null;
    	}
    	for(Estructura estructura : getEstructuraList()){
            estructura.setVersion(this.getVersionEditable());            
            if(this.getConfiguradorDisenoBackingBean().getEstructuraModelMap().containsKey(estructura.getOrden())){
            	this.getConfiguradorDisenoBackingBean().getEstructuraModelMap().get(estructura.getOrden()).setTipoEstructura(estructura.getTipoEstructura().getIdTipoEstructura());
            }else{
            	this.getConfiguradorDisenoBackingBean().getEstructuraModelMap().put(estructura.getOrden(), new EstructuraModel(estructura.getTipoEstructura().getIdTipoEstructura()));
            }
        }        
        this.setAlmacenado(true);   
    	return null;
    }
    
    public void agregarVersionListener(ActionEvent actionEvent) {        
    	 this.getConfiguradorDisenoBackingBean().setEstructuraModelMap(null);        
         if(this.getVersionList().size() > 0){             
             //Version version = getVersionList().get(getVersionList().size()-1);             
        	 Version version = this.getVersionList().iterator().next();
             if(version.getIdVersion()!=null){
                 for(Version versionPaso : this.getVersionList()){
                     versionPaso.setVigencia(0L);
                 }
                 this.setEstructuraList(null);
                 this.getVersionList().add(new Version(version.getVersion()+1, true, new Date(), 1L));
                 this.setRenderEstructura(true);
             }
         }else{
             Version version = new Version(1L, true, new Date(), 1L);
             versionList = new ArrayList<Version>();
             versionList.add(version);
             setRenderEstructura(true);
             setEstructuraList(null);
         }
         SortHelper.sortVersionDesc(this.getVersionList());
         setAlmacenado(false);
         setRenderBotonEditar(true);
     }
    
    
    public void subirEstructuraListener(ActionEvent event){        
        List<Estructura> estructurasTemp = new ArrayList<Estructura>();
        Map<Long, EstructuraModel> estructuraModelMap = this.getConfiguradorDisenoBackingBean().getEstructuraModelMap();        
        Map<Long, EstructuraModel> estructuraModelTempMap = new LinkedHashMap<Long, EstructuraModel>();
        final Estructura estructuraSelected = (Estructura)event.getComponent().getAttributes().get("estructura");        
        setAlmacenado(false);
        
        if(!estructuraSelected.getOrden().equals(1L)){
            getEstructuraList().remove(estructuraSelected);            
            Long contador = 0L;
            for(Estructura estructura : getEstructuraList()){
                contador++;
                Long orden = estructuraSelected.getOrden() -1L;                
                if(estructura.getOrden().equals(orden)){                    
                    if(estructuraModelMap.containsKey(estructuraSelected.getOrden())){
                    	estructuraModelTempMap.put(contador, estructuraModelMap.get(estructuraSelected.getOrden()));
                    }else{
                    	estructuraModelTempMap.put(contador, new EstructuraModel());
                    }                   
                    estructuraSelected.setOrden(contador);
                    estructurasTemp.add(estructuraSelected);
                    contador++;
                }                
                if(estructuraModelMap.containsKey(estructura.getOrden())){
                	estructuraModelTempMap.put(contador, estructuraModelMap.get(estructura.getOrden()));
                }
                else{
                	estructuraModelTempMap.put(contador, new EstructuraModel());
                }                
                estructura.setOrden(contador);
                estructurasTemp.add(estructura);
            }
            estructuraModelMap.putAll(estructuraModelTempMap);
            this.setEstructuraList(estructurasTemp);        
            //getGeneradorDiseno().initBackingBean();
        }
    }
    
    public void bajarEstructuraListener(ActionEvent event){        
        List<Estructura> estructurasTemp = new ArrayList<Estructura>();
        final Estructura estructuraSelected = (Estructura)event.getComponent().getAttributes().get("estructura");  
        Map<Long, EstructuraModel> estructuraModelMap = this.getConfiguradorDisenoBackingBean().getEstructuraModelMap();        
        Map<Long, EstructuraModel> estructuraModelTempMap = new LinkedHashMap<Long, EstructuraModel>();
        setAlmacenado(false);
        
        if(estructuraSelected.getOrden().intValue() < getEstructuraList().size()){
            
            Long orden = estructuraSelected.getOrden();
            getEstructuraList().remove(estructuraSelected);
            Long contador = 0L;
            
            for(Estructura estructura : getEstructuraList()){                
                contador++;                
                if(estructuraModelMap.containsKey(estructura.getOrden())){
                	estructuraModelTempMap.put(contador, estructuraModelMap.get(estructura.getOrden()));
                }
                else{
                	estructuraModelTempMap.put(contador, new EstructuraModel());
                }                
                estructura.setOrden(contador);
                estructurasTemp.add(estructura);                
                if(estructura.getOrden().equals(orden)){                    
                    contador++;                    
                    if(estructuraModelMap.containsKey(estructuraSelected.getOrden())){
                    	estructuraModelTempMap.put(contador, estructuraModelMap.get(estructuraSelected.getOrden()));
                    }
                    else{
                    	estructuraModelTempMap.put(contador, new EstructuraModel());
                    }                                        
                    estructuraSelected.setOrden(contador);
                    estructurasTemp.add(estructuraSelected);
                }                
            }
            estructuraModelMap.putAll(estructuraModelTempMap);
            this.setEstructuraList(estructurasTemp);        
            //getGeneradorDiseno().initBackingBean();
        }
    }
    
    /**
     * @param event
     */
    public void prepareCopiarEstructuraActionListener(ActionEvent event){
    	this.setVersionClonable(((Version)event.getComponent().getAttributes().get("version")));
    	super.displayPopUp("dialogCopiarVersion", "fGV1");
    }
    
    /**
     * Metodo encargado de copiar una version con todas sus estrucuturas.
     * @param actionEvent
     */
    public void copiarEstructuraActionListener(ActionEvent event) {      	
    	this.getConfiguradorDisenoBackingBean().setEstructuraModelMap(null);        
        try{            
             if(this.getVersionList().size() > 0){    
                 if(this.getVersionClonable() == null){
                    super.addWarnMessage("Debe seleccionar una Versión para copiar");
                    return;
                 }                                                   
                 if(this.getVersionClonable().getIdVersion()!=null){                     
                     List<Estructura> estructuras = super.getFacadeService().getEstructuraService().findEstructuraByVersion(this.getVersionClonable());
                     List<Estructura> estructurasNew = new ArrayList<Estructura>();
                     for(Estructura estructura : estructuras){
                         Estructura estructuraNew = new Estructura();
                         estructuraNew.setOrden(estructura.getOrden());
                         estructuraNew.setTipoEstructura(estructura.getTipoEstructura());
                         estructuraNew.setVersion(estructura.getVersion());
                         estructuraNew.setHtml(estructura.getHtml());
                         estructuraNew.setGrilla(estructura.getGrilla());
                         estructuraNew.setTexto(estructura.getTexto());
                         estructurasNew.add(estructuraNew);
                     }
                     
                     this.getConfiguradorDisenoBackingBean().setEstructuraModelMap(GeneradorDisenoHelper.cloneEstructuraModel(estructurasNew));
                     
                     for(Version versionPaso : this.getVersionList()){
                         versionPaso.setVigencia(0L);
                     }
                     
                     this.setEstructuraList(estructurasNew);
                     this.getVersionList().add(new Version(this.getVersionClonable().getVersion()+1, true, new Date(), 1L));
                     this.setRenderEstructura(true);
                 }
            }else{
            	 this.getVersionList().add(new Version());
            	 setEstructuraList(null);
            }
            SortHelper.sortVersionDesc(this.getVersionList());
            setRenderBotonEditar(true);            
        }catch(Exception e){
            super.addErrorMessage("Se ha producido un error al copiar la configuración.");
            LOG.error(e.getMessage(),e);
        }
        setAlmacenado(false);
        
    
    }
    
    public boolean isRenderEstructura() {
		return renderEstructura;
	}

	public void setRenderEstructura(boolean renderEstructura) {
		this.renderEstructura = renderEstructura;
	}

	public boolean isRenderBotonEditar() {
		return renderBotonEditar;
	}

	public void setRenderBotonEditar(boolean renderBotonEditar) {
		this.renderBotonEditar = renderBotonEditar;
	}

	public DataTable getEstructuraTable() {
		return estructuraTable;
	}

	public void setEstructuraTable(DataTable estructuraTable) {
		this.estructuraTable = estructuraTable;
	}

	public boolean isAlmacenado() {
		return almacenado;
	}

	public void setAlmacenado(boolean almacenado) {
		this.almacenado = almacenado;
	}

	public void setEstructuraList(List<Estructura> estructuraList) {
		this.estructuraList = estructuraList;
	}
    
    public TipoCuadro getTipoCuadro() {
		return tipoCuadro;
	}
	public void setTipoCuadro(TipoCuadro tipoCuadro) {
		this.tipoCuadro = tipoCuadro;
	}
	public Catalogo getCatalogo() {
		return catalogo;
	}
	public void setCatalogo(Catalogo catalogo) {
		this.catalogo = catalogo;
	}

	public List<Catalogo> getCatalogoList() {
		return catalogoList;
	}
	

	public void setCatalogoList(List<Catalogo> catalogoList) {

		this.catalogoList = catalogoList;
	}

	public List<Version> getVersionList() {		
		return versionList;
	}

	public void setVersionList(List<Version> versionList) {
		this.versionList = versionList;
	}
	
	public Long getIdTipoCuadro() {
		return idTipoCuadro;
	}

	public void setIdTipoCuadro(Long idTipoCuadro) {
		this.idTipoCuadro = idTipoCuadro;
	}

	public ConfiguradorDisenoBackingBean getConfiguradorDisenoBackingBean() {
		return configuradorDisenoBackingBean;
	}

	public void setConfiguradorDisenoBackingBean(
			ConfiguradorDisenoBackingBean configuradorDisenoBackingBean) {
		this.configuradorDisenoBackingBean = configuradorDisenoBackingBean;
	}

	public DataTable getVersionTable() {
		return versionTable;
	}

	public void setVersionTable(DataTable versionTable) {
		this.versionTable = versionTable;
	}

	public boolean isRenderBotonEditarVersion() {
		return renderBotonEditarVersion;
	}

	public void setRenderBotonEditarVersion(boolean renderBotonEditarVersion) {
		this.renderBotonEditarVersion = renderBotonEditarVersion;
	}

	public Version getVersionEditable() {
		return versionEditable;
	}

	public void setVersionEditable(Version versionEditable) {
		this.versionEditable = versionEditable;
	}

	public Estructura getEstructuraEditable() {
		return estructuraEditable;
	}

	public void setEstructuraEditable(Estructura estructuraEditable) {
		this.estructuraEditable = estructuraEditable;
	}

	public Version getVersionClonable() {
		return versionClonable;
	}

	public void setVersionClonable(Version versionClonable) {
		this.versionClonable = versionClonable;
	}
	

}
