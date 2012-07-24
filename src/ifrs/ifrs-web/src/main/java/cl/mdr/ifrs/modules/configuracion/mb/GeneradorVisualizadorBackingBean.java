package cl.mdr.ifrs.modules.configuracion.mb;

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
import cl.mdr.ifrs.ejb.entity.TipoEstructura;
import cl.mdr.ifrs.exceptions.GrillaIncorrectaException;
import cl.mdr.ifrs.exceptions.PeriodoException;
import cl.mdr.ifrs.model.EstructuraModel;
import cl.mdr.ifrs.vo.AgrupacionColumnaModelVO;
import cl.mdr.ifrs.vo.GrillaVO;
import cl.mdr.ifrs.vo.HtmlVO;
import cl.mdr.ifrs.vo.TextoVO;

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
		this.getEstructuraList();
	}
	
    public void guardarDisenoActionListener(ActionEvent action){
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
            super.getFacadeService().getVersionService().persistVersion(this.getGeneradorVersionBackingBean().getVersionList(), this.getGeneradorVersionBackingBean().getEstructuraList(), this.getConfiguradorDisenoBackingBean().getEstructuraModelMap(), super.getNombreUsuario());   
            super.addInfoMessage("Se ha almacenado correctamente la informacion");
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
		/*estructuraList = getGeneradorVersionBackingBean().getEstructuraList();
        if(Util.esListaValida(estructuraList)){
            for(Estructura estructura : estructuraList){
                if(this.getConfiguradorDisenoBackingBean().getEstructuraModelMap().containsKey(estructura.getOrden())){
                    final EstructuraModel estructuraModel =  this.getConfiguradorDisenoBackingBean().getEstructuraModelMap().get(estructura.getOrden());  
                    if(estructuraModel.getTipoEstructura() == TipoEstructura.ESTRUCTURA_TIPO_GRILLA){
                        if(Util.esListaValida(estructuraModel.getColumnas())){
                            GrillaVO grillaVO = this.createTableModel(estructuraModel.getColumnas(), estructuraModel.getAgrupacionesMap() );                            
                            grillaVO.setCeldaList(GeneradorDisenoHelper.builHtmlGrilla(estructuraModel.getColumnas()));
                            grillaVO.setAgrupaciones(GeneradorDisenoHelper.crearAgrupadorHTMLVO(GeneradorDisenoHelper.getAgrupacionesByColumnaList(estructuraModel.getColumnas())));
                            grillaVO.setTitulo(estructuraModel.getTituloGrilla());
                            estructura.setGrillaVO(grillaVO);                            
                        }
                    }else if(estructuraModel.getTipoEstructura() == TipoEstructura.ESTRUCTURA_TIPO_TEXTO){
                            estructura.setTextoVo(new TextoVO(estructuraModel.getTexto()));
                    }else if(estructuraModel.getTipoEstructura() == TipoEstructura.ESTRUCTURA_TIPO_HTML){
                            estructura.setHtmlVo(new HtmlVO(estructuraModel.getHtml()));
                    }
                }
            }
        }*/
		return estructuraList;
	}

	public void setEstructuraList(List<Estructura> estructuraList) {
		this.estructuraList = estructuraList;
	}
	

}
