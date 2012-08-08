package cl.mdr.exfida.modules.configuracion.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.util.GeneradorDisenoHelper;
import cl.mdr.ifrs.ejb.cross.SortHelper;
import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.AgrupacionColumna;
import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.ifrs.ejb.entity.Columna;
import cl.mdr.ifrs.ejb.entity.Estructura;
import cl.mdr.ifrs.ejb.entity.Grilla;
import cl.mdr.ifrs.ejb.entity.PeriodoEmpresa;
import cl.mdr.ifrs.ejb.entity.TipoEstructura;
import cl.mdr.ifrs.ejb.entity.Version;
import cl.mdr.ifrs.exceptions.GrillaIncorrectaException;
import cl.mdr.ifrs.exceptions.PeriodoException;
import cl.mdr.ifrs.model.EstructuraModel;
import cl.mdr.ifrs.vo.AgrupacionColumnaModelVO;
import cl.mdr.ifrs.vo.AgrupacionModelVO;
import cl.mdr.ifrs.vo.GrillaVO;

@ManagedBean
@ViewScoped
public class GeneradorVisualizadorBackingBean extends AbstractBackingBean implements Serializable {
	private transient Logger logger = Logger.getLogger(this.getClass().getName());  
	private static final long serialVersionUID = 5163999002484028864L;
	
	@ManagedProperty(value="#{configuradorDisenoBackingBean}")
    private ConfiguradorDisenoBackingBean configuradorDisenoBackingBean;
	
	@ManagedProperty(value="#{generadorVersionBackingBean}")
	private GeneradorVersionBackingBean generadorVersionBackingBean;
	
	private List<Estructura> estructuraList;
			
	public void cargarEstructuraAction(ActionEvent event){
		if(!this.generadorVersionBackingBean.isAlmacenado()){
			super.displayPopUp("dialogNoDataSaved", GeneradorVersionBackingBean.FORM_NAME_PRINCIPAL);
			return;
		}
		this.getEstructuraList();
	}
	
    public void guardarDisenoActionListener(ActionEvent action){
    	Version versionResult = null;
        try {
        	
        	if(!isSelectedEmpresa())
        		return;
        	
            GeneradorDisenoHelper.validarContenidoCelda(this.getConfiguradorDisenoBackingBean().getEstructuraModelMap());
        } catch (GrillaIncorrectaException e) {
            if(e.getErrores()==null)
                super.addWarnMessage(e.getMessage());
            else
                for(String error : e.getErrores()){
                	super.addWarnMessage(error);
                }
            return;          
        } catch (Exception e) {            
            super.addErrorMessage("Error al procesar la información");
            return;
        }
        try{
        	final PeriodoEmpresa periodoEmpresa = super.getFacadeService().getPeriodoService().getMaxPeriodoEmpresaByEmpresa(super.getFiltroBackingBean().getEmpresa().getIdRut());
        	versionResult = super.getFacadeService().getVersionService().persistVersion(this.getGeneradorVersionBackingBean().getVersionList(), this.getGeneradorVersionBackingBean().getEstructuraList(), this.getConfiguradorDisenoBackingBean().getEstructuraModelMap(), super.getNombreUsuario(), periodoEmpresa);   
            if(versionResult != null){
            	this.updateViewContent(versionResult);
            	super.addInfoMessage("Se ha configurado correctamente la Revelación");
            }
            
        }catch(PeriodoException e){
        	super.addErrorMessage("Error, no se ha almacenado la información");
        	super.addErrorMessage(e.getMessage());
            logger.error(e);
        }catch(Exception e){
        	super.addErrorMessage("Error, no se ha almacenado la información");
            logger.error(e);
        }
    }
    
    private void updateViewContent(final Version versionResult) throws Exception{
    	 if(versionResult == null){
    		 throw new Exception();
    	 }
    	 final List<Estructura> estructuras = super.getFacadeService().getEstructuraService().getEstructuraByVersion(versionResult, false);
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
             	
             	estructura.getGrillaVO().setCeldaList(GeneradorDisenoHelper.builHtmlGrilla(grilla.getColumnaList()));                    		
                 final List<AgrupacionColumna> agrupaciones = super.getFacadeService().getEstructuraService().findAgrupacionColumnaByGrilla(grilla);                        
                 if(Util.esListaValida(agrupaciones)){
                 	List<List<AgrupacionModelVO>> agrupacionesNivel = GeneradorDisenoHelper.crearAgrupadorHTMLVO(agrupaciones);
                 	estructura.getGrillaVO().setAgrupaciones(agrupacionesNivel);
                 }
             }
         }                                     
         this.getGeneradorVersionBackingBean().setAlmacenado(true);
         this.getGeneradorVersionBackingBean().setRenderBotonEditar(false);
         this.getGeneradorVersionBackingBean().setRenderBotonEditarVersion(Boolean.TRUE);
         this.getGeneradorVersionBackingBean().setEstructuraList(estructuras);
         this.getGeneradorVersionBackingBean().setRenderEstructura(true);
    }
    
    public void editarDisenoActionListener(ActionEvent action){
        try {
            GeneradorDisenoHelper.validarContenidoCelda(this.getConfiguradorDisenoBackingBean().getEstructuraModelMap());
        } catch (GrillaIncorrectaException e) {
            if(e.getErrores()==null)
                super.addWarnMessage(e.getMessage());
            else
                for(String error : e.getErrores()){
                	super.addWarnMessage(error);
                }
            return;          
        } catch (Exception e) {            
            super.addErrorMessage("Error al procesar la información");
            return;
        }
        try{
            super.getFacadeService().getVersionService().editarVersion(this.getGeneradorVersionBackingBean().getVersionEditable(), this.getGeneradorVersionBackingBean().getEstructuraList(), this.getConfiguradorDisenoBackingBean().getEstructuraModelMap(), super.getNombreUsuario());   
            super.addInfoMessage("Se ha configurado correctamente la Revelación");
        }catch(PeriodoException e){
        	super.addErrorMessage("Error, no se ha almacenado la información");
        	super.addErrorMessage(e.getMessage());
            logger.error(e);
        }catch(Exception e){
        	super.addErrorMessage("Error, no se ha almacenado la información");
            logger.error(e);
        }
    }
				
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public GrillaVO createTableModel(List<Columna> columnasGrilla, Map<Long,List<AgrupacionColumna>> agrupacionesMap) {        
        List<AgrupacionColumna> agrupacionColumnas = new ArrayList<AgrupacionColumna>();
        SortHelper.sortColumnasByOrden(columnasGrilla);
        boolean listaVacia = true;
        List<Columna> columnas = new ArrayList<Columna>();
        List<Map<Long,Celda>> rows = new ArrayList<Map<Long,Celda>>();
        Map<Long,Celda> celdaMap = new LinkedHashMap<Long,Celda>();
        List<AgrupacionColumnaModelVO> agrupacionesModelVO = new ArrayList<AgrupacionColumnaModelVO>();
        GrillaVO grillaVO = new GrillaVO();
        
        if(agrupacionesMap !=null && !agrupacionesMap.isEmpty()){
            
            Iterator it = agrupacionesMap.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry entry = (Map.Entry)it.next();
                List<AgrupacionColumna> lista = (List<AgrupacionColumna>) entry.getValue();
                if(Util.esListaValida(lista)){
                    agrupacionColumnas.addAll(lista);
                }
            }
        }
            
        agrupacionesModelVO = GeneradorDisenoHelper.soporteAgrupacionColumna(columnasGrilla,agrupacionColumnas);        

        for(Columna columnaNota : columnasGrilla){
            columnas.add(columnaNota);
            if(columnaNota.getCeldaList()==null)
                continue;
            
            int j=0;
            for(Celda celda : columnaNota.getCeldaList()){
                celda.setIdColumna(Integer.valueOf(j).longValue()+1L);
                if(listaVacia){
                    celdaMap = new LinkedHashMap<Long,Celda>();
                    celdaMap.put(new Long(columnaNota.getIdColumna()), celda);
                    rows.add(celdaMap);
                }else{
                    if(rows.size()>j){
                        rows.get(j).put(new Long(columnaNota.getIdColumna()), celda);
                    }
                }
                j++;                    
            }
            listaVacia = false;
        }
        GeneradorDisenoHelper.setListaPorNivelUsado(agrupacionesModelVO, grillaVO);
                
        grillaVO.setColumnas(columnas);
        grillaVO.setRows(rows);
        
        
            
        return grillaVO;
    }

	public ConfiguradorDisenoBackingBean getConfiguradorDisenoBackingBean() {
		return configuradorDisenoBackingBean;
	}

	public void setConfiguradorDisenoBackingBean(
			ConfiguradorDisenoBackingBean configuradorDisenoBackingBean) {
		this.configuradorDisenoBackingBean = configuradorDisenoBackingBean;
	}

	public GeneradorVersionBackingBean getGeneradorVersionBackingBean() {
		return generadorVersionBackingBean;
	}

	public void setGeneradorVersionBackingBean(
			GeneradorVersionBackingBean generadorVersionBackingBean) {
		this.generadorVersionBackingBean = generadorVersionBackingBean;
	}

	public List<Estructura> getEstructuraList() {
		estructuraList = getGeneradorVersionBackingBean().getEstructuraList();
        if(Util.esListaValida(estructuraList)){
            for(Estructura estructura : estructuraList){
                if(this.getConfiguradorDisenoBackingBean().getEstructuraModelMap().containsKey(estructura.getOrden())){
                    final EstructuraModel estructuraModel =  this.getConfiguradorDisenoBackingBean().getEstructuraModelMap().get(estructura.getOrden());  
                    if(estructuraModel.getTipoEstructura() == TipoEstructura.ESTRUCTURA_TIPO_GRILLA){
                        if(Util.esListaValida(estructuraModel.getColumnas())){
                            GrillaVO grillaVO = new GrillaVO();  
                            grillaVO.setColumnas(estructuraModel.getColumnas());
                            grillaVO.setCeldaList(GeneradorDisenoHelper.builHtmlGrilla(estructuraModel.getColumnas()));
                            grillaVO.setAgrupaciones(GeneradorDisenoHelper.crearAgrupadorHTMLVO(GeneradorDisenoHelper.getAgrupacionesByColumnaList(estructuraModel.getColumnas())));
                            grillaVO.setTitulo(estructuraModel.getTituloGrilla());
                            estructura.setGrillaVO(grillaVO);                            
                        }
                    }else if(estructuraModel.getTipoEstructura() == TipoEstructura.ESTRUCTURA_TIPO_TEXTO){
                            estructura.setTexto(estructuraModel.getTexto());
                    }else if(estructuraModel.getTipoEstructura() == TipoEstructura.ESTRUCTURA_TIPO_HTML){
                            estructura.setHtml(estructuraModel.getHtml());
                    }
                }
            }
        }
		return estructuraList;
	}

	public void setEstructuraList(List<Estructura> estructuraList) {
		this.estructuraList = estructuraList;
	}
	

}
