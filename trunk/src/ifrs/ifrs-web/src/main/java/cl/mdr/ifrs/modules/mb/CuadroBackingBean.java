package cl.mdr.ifrs.modules.mb;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.persistence.NoResultException;

import org.apache.log4j.Logger;
import org.primefaces.event.RowEditEvent;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.mb.ComponenteBackingBean;
import cl.mdr.ifrs.cross.mb.FiltroBackingBean;
import cl.mdr.ifrs.cross.util.GeneradorDisenoHelper;
import cl.mdr.ifrs.cross.util.PropertyManager;
import cl.mdr.ifrs.cross.vo.AgrupacionVO;
import cl.mdr.ifrs.ejb.entity.AgrupacionColumna;
import cl.mdr.ifrs.ejb.entity.Catalogo;
import cl.mdr.ifrs.ejb.entity.Columna;
import cl.mdr.ifrs.ejb.entity.Empresa;
import cl.mdr.ifrs.ejb.entity.Estructura;
import cl.mdr.ifrs.ejb.entity.Grilla;
import cl.mdr.ifrs.ejb.entity.TipoCuadro;
import cl.mdr.ifrs.ejb.entity.TipoEstructura;
import cl.mdr.ifrs.ejb.entity.Version;
import cl.mdr.ifrs.exceptions.FormulaException;
import cl.mdr.ifrs.vo.AgrupacionColumnaModelVO;
import cl.mdr.ifrs.vo.AgrupacionModelVO;


@ManagedBean(name="cuadroBackingBean")
@ViewScoped
public class CuadroBackingBean extends AbstractBackingBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6867816117743274288L;
	
	private Long idEmpresa;
	private Long idCuadro;
	private Logger logger = Logger.getLogger(CuadroBackingBean.class);
	public boolean isRenderVersionList() {
		return renderVersionList;
	}

	private List<Estructura> estructuraList;
	private ComponenteBackingBean componenteBackingBean;
	private List<Catalogo> catalogoList;
	private Catalogo selectedCuadro;
	private Catalogo nuevoCuadro;
	private boolean renderVersionList = false;
	private List<Version> versionList;


	
    public Object buscarVersion(){
        
        FiltroBackingBean filtroPaso = getFiltroBackingBean();
        
        filtroPaso.getPeriodo().setPeriodo(null);

        if(filtroPaso.getCatalogo().getIdCatalogo()!=null){
            try{
                this.renderVersionList = true;
                Long periodo = Long.valueOf(filtroPaso.getPeriodo().getAnioPeriodo().concat(filtroPaso.getPeriodo().getMesPeriodo()));                
                try{
                    getFiltroBackingBean().setPeriodo(getFacadeService().getMantenedoresTipoService().findByPeriodo(periodo));
                }catch(NoResultException e){
                	addNotFoundMessage();
                    return null;                    
                }catch(EJBException e){
                	addNotFoundMessage();
                    return null;
                }
                //CREAR METODO QUE BUSQUE POR PERMISOS - PERIODO - CUADRO
                versionList = getFacadeService().getVersionService().findVersionByCatalogoPeriodo(filtroPaso.getCatalogo().getIdCatalogo(), periodo);
                
                if(versionList == null){  
                    this.renderVersionList = false;
                    addWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("periodo_busqueda_sin_resultado_periodo"), getFiltroBackingBean().getPeriodo().getAnioPeriodo(), getFiltroBackingBean().getPeriodo().getMesPeriodo()));
                }
            }catch(Exception e){
                logger.error(e.getCause(), e);
                addErrorMessage("Error al consultar Versiones para el Período");
            }
            
        }
        
        return null;
    }
    
    public String buscarCuadro(ActionEvent event){
        try{
            final Version version = (Version)event.getComponent().getAttributes().get("version");
            
            estructuraList = getFacadeService().getEstructuraService().getEstructuraByVersion(version, true);
            
            setListGrilla(estructuraList);
            
            setEstructuraList(estructuraList);
            
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
        return null;
    }
    
    private void addNotFoundMessage(){
    	addWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("periodo_busqueda_sin_resultado_periodo"), getFiltroBackingBean().getPeriodo().getAnioPeriodo(), getFiltroBackingBean().getPeriodo().getMesPeriodo()));
        this.renderVersionList = false;  
    }
	
	
	public void editar(RowEditEvent event){
		getFacadeService().getMantenedoresTipoService().mergeEntity((Catalogo) event.getObject());
		setSelectedCuadro(null);
	}
	
	public void eliminar(){
		
		
		try {
			getFacadeService().getMantenedoresTipoService().deleteCuadro(getSelectedCuadro());
			super.addInfoMessage(PropertyManager.getInstance().getMessage("mensaje_tabla_eliminar_registro"), null );
		} catch (Exception e) {
			e.printStackTrace();
			super.addErrorMessage(PropertyManager.getInstance().getMessage("mensaje_error_generico"), null );
		}
		setSelectedCuadro(null);
		
	
	}

	
	public void guardar(ActionEvent event){
        try {            
        	
        	getNuevoCuadro().setEmpresa(new Empresa(getIdEmpresa()));
        	getNuevoCuadro().setTipoCuadro(new TipoCuadro(getIdCuadro()));
        	
        	Catalogo catalogo = new Catalogo();
        	catalogo = getNuevoCuadro();
        	
            super.getFacadeService().getCatalogoService().persistEntity(catalogo);

            setNuevoCuadro(null);
            super.addInfoMessage(PropertyManager.getInstance().getMessage("mensaje_tabla_guardar_registro"), null );
        } catch (Exception e) {
            logger.error(e);
            super.addErrorMessage(PropertyManager.getInstance().getMessage("mensaje_error_generico"), null );
        }
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


	public Long getIdCuadro() {
		if (idCuadro != null && idCuadro == 0){
			return null;
		}
		return idCuadro;
	}


	public void setIdCuadro(Long idCuadro) {
		this.idCuadro = idCuadro;
	}

	public List<Version> getVersionList() {
		return versionList;
	}

	public void setVersionList(List<Version> versionList) {
		this.versionList = versionList;
	}
	
	//TODO cambiar metodo de posicion
    public void setListGrilla(List<Estructura> estructuraList) throws Exception {
        
        for(Estructura estructuras : estructuraList){
        	
        	Grilla grilla = estructuras.getGrilla();
        	
            if(estructuras.getTipoEstructura().getIdTipoEstructura().equals(TipoEstructura.ESTRUCTURA_TIPO_GRILLA)){
                List<AgrupacionColumna> agrupaciones = getFacadeService().getEstructuraService().findAgrupacionColumnaByGrilla(grilla);
                List<AgrupacionColumnaModelVO> agrupacionColumnaList = soporteAgrupacionColumna(grilla.getIdGrilla(), grilla.getColumnaList(),agrupaciones);                        
                if(agrupacionColumnaList==null || agrupacionColumnaList.isEmpty()){
                    estructuras.getGrillaVO().setColumnas(grilla.getColumnaList());
                    estructuras.getGrillaVO().setNivel(0L);
                }else if(agrupacionColumnaList.get(0).getNivel() == 1L){
                    estructuras.getGrillaVO().setNivel(agrupacionColumnaList.get(0).getNivel());
                    estructuras.getGrillaVO().setNivel1List(agrupacionColumnaList);
                }else if(agrupacionColumnaList.get(0).getNivel() == 2L){
                    estructuras.getGrillaVO().setNivel(agrupacionColumnaList.get(0).getNivel());
                    estructuras.getGrillaVO().setNivel2List(agrupacionColumnaList);    
                }
            }
        }
    }
    
    //TODO mover metodo solo para prueba esta aqui    
    private List<AgrupacionColumnaModelVO> soporteAgrupacionColumna(Long idGrilla, List<Columna> columnas , List<AgrupacionColumna> agrupaciones) {

        Map<Long, Columna> columnaMap = new LinkedHashMap<Long, Columna>();
        Map<Long, AgrupacionColumnaModelVO> nivel1Map = new LinkedHashMap<Long, AgrupacionColumnaModelVO>();
        Map<Long, AgrupacionColumnaModelVO> nivel2Map = new LinkedHashMap<Long, AgrupacionColumnaModelVO>();
        List<AgrupacionColumnaModelVO> nivelesList = new ArrayList<AgrupacionColumnaModelVO>();
        Map<Long,Long> nivel = new HashMap<Long,Long>();
        Long niveles = 0L;
        
        try {
            
            for(Columna columna : columnas){
                columnaMap.put(columna.getIdColumna(), columna);
            }
            
            List<AgrupacionVO> agrupacionesNivel = GeneradorDisenoHelper.crearAgrupadorVO(agrupaciones, 1L);
            
            List<AgrupacionModelVO> agrupacionesModel = new ArrayList<AgrupacionModelVO>();
            
            for(AgrupacionVO agrupacion : agrupacionesNivel){
                
                AgrupacionModelVO agrupacionModel = new AgrupacionModelVO();
                agrupacionModel.setDesde(agrupacion.getDesde());
                agrupacionModel.setHasta(agrupacion.getHasta());
                agrupacionModel.setGrupo(agrupacion.getGrupo());
                agrupacionModel.setAncho(agrupacion.getAncho());
                agrupacionModel.setNivel(agrupacion.getNivel());
                agrupacionModel.setIdGrilla(agrupacion.getIdGrilla());
                agrupacionModel.setTitulo(agrupacion.getTitulo());
                agrupacionesModel.add(agrupacionModel);
            }
            
            
            
            for(AgrupacionModelVO agrupacion : agrupacionesModel){
                
                if(agrupacion.getNivel() == 1L){
                    
                    List<Columna> columnasNew = new ArrayList<Columna>();
                    AgrupacionColumnaModelVO agrupacionN1VO = new AgrupacionColumnaModelVO();
                    agrupacionN1VO.setTituloNivel(agrupacion.getTitulo());
                    agrupacionN1VO.setNivel(1L);
                    agrupacionN1VO.setAgrupacion(agrupacion);
                    for(Long i=agrupacion.getDesde(); i<= agrupacion.getHasta(); i++){
                        if(columnaMap.containsKey(i)){
                            columnasNew.add(columnaMap.get(i));
                            niveles = 1L;
                        }
                    }
                    agrupacionN1VO.setColumnas(columnasNew);
                    nivel1Map.put(agrupacion.getGrupo(), agrupacionN1VO);
                    
                }else  if(agrupacion.getNivel() == 2L){

                    AgrupacionColumnaModelVO agrupacionN2VO = new AgrupacionColumnaModelVO();
                    
                    List<AgrupacionColumnaModelVO> niveles1New = new ArrayList<AgrupacionColumnaModelVO>();
                    
                    for(Long i=agrupacion.getDesde(); i<= agrupacion.getHasta(); i++){
                        
                        //Long grupoNivel1 = GeneradorDisenoHelper.getGrupoNivel1(nivel1Map,i);
                        AgrupacionColumna col = new AgrupacionColumna();
                        col.setIdNivel(1L);
                        col.setIdGrilla(idGrilla);
                        col.setIdColumna(i);
                        col = getFacadeService().getEstructuraService().findAgrupacionColumnaById(col);
                        Long grupoNivel1 = col.getGrupo();
                        
                        if(grupoNivel1 != null && !nivel.containsKey(grupoNivel1)){
                            nivel.put(grupoNivel1, grupoNivel1);
                            niveles1New.add(nivel1Map.get(grupoNivel1));
                            niveles = 2L;
                        }/*else if(grupoNivel1 != null){                            
                            if(nivel1Map.containsKey(grupoNivel1)){
                                niveles1New.add(nivel1Map.get(grupoNivel1));
                            }
                        }*/
                    }
                    
                    agrupacionN2VO.setTituloNivel(agrupacion.getTitulo());
                    agrupacionN2VO.setNivel(2L);
                    agrupacionN2VO.setNiveles(niveles1New);
                    
                    nivel2Map.put(agrupacion.getGrupo(), agrupacionN2VO);
              }
            }
            
            if (niveles == 0L) {
                return null;   
            }
            
            if (niveles == 1L) {
                @SuppressWarnings("rawtypes")
				Iterator it = nivel1Map.entrySet().iterator();
                while (it.hasNext()) {
                    @SuppressWarnings("rawtypes")
					Map.Entry entry = (Map.Entry)it.next();
                    nivelesList.add((AgrupacionColumnaModelVO)entry.getValue());
                    
                }
            }

            if (niveles == 2L) {
                @SuppressWarnings("rawtypes")
				Iterator it = nivel2Map.entrySet().iterator();
                while (it.hasNext()) {
                    @SuppressWarnings("rawtypes")
					Map.Entry entry = (Map.Entry)it.next();
                    nivelesList.add((AgrupacionColumnaModelVO)entry.getValue());
                }
            }

        } catch (Exception e) {
            e.getStackTrace();
        }
        
        return nivelesList;
    }
	
	

    
}
