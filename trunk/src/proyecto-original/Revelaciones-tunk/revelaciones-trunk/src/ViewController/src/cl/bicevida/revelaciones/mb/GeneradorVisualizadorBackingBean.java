package cl.bicevida.revelaciones.mb;


import cl.bicevida.revelaciones.common.mb.SoporteBackingBean;
import cl.bicevida.revelaciones.common.util.GeneradorDisenoHelper;
import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.revelaciones.ejb.entity.Estructura;
import cl.bicevida.revelaciones.ejb.entity.TipoEstructura;
import cl.bicevida.revelaciones.exceptions.GrillaIncorrectaException;
import cl.bicevida.revelaciones.exceptions.PeriodoException;
import cl.bicevida.revelaciones.vo.GrillaModelVO;
import cl.bicevida.revelaciones.vo.GrillaVO;
import cl.bicevida.revelaciones.vo.HtmlVO;
import cl.bicevida.revelaciones.vo.TextoVO;

import java.util.List;
import java.util.Map;

import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.event.DisclosureEvent;


public class GeneradorVisualizadorBackingBean extends SoporteBackingBean{

    @SuppressWarnings("compatibility:4710751984467567343")
    private static final long serialVersionUID = 4260277309391061083L;
    
    private transient Logger logger = Logger.getLogger(CuadroBackingBean.class);
    public static final String BEAN_NAME = "generadorVisualizadorBackingBean";
    
    private boolean renderedVisualizador = false;
    
    public GeneradorVisualizadorBackingBean() {
    }
    
    public void guardarDisenoListener(ActionEvent action){
        try {
            GeneradorDisenoHelper.validarContenidoCelda(getGeneradorDiseno().getGrillaModelMap());
        } catch (GrillaIncorrectaException e) {
            if(e.getErrores()==null)
                agregarWarnMessage(e.getMessage());
            else
                for(String error : e.getErrores()){
                    agregarWarnMessage(error);
                }
            return;          
        } catch (Exception e) {            
            agregarErrorMessage("Error al procesar la información");
            return;
        }
        try{
            getFacade().getVersionService().persistVersion(getGeneradorVersion().getVersiones(), getGeneradorVersion().getEstructuras(), getGeneradorDiseno().getGrillaModelMap(), super.getNombreUsuario());   
            getGeneradorVersion().setRenderedBotonEditar(false);
            agregarSuccesMessage("Se ha almacenado correctamente la información");
        }catch(PeriodoException e){
            agregarErrorMessage("Error, no se ha almacenado la información");
            agregarErrorMessage(e.getMessage());
            logger.error(e);
        }catch(Exception e){
            agregarErrorMessage("Error, no se ha almacenado la información");
            logger.error(e);
        }
    }
    
    public List<Estructura> getEstructuras(){
        List<Estructura> estructuras = getGeneradorVersion().getEstructuras();
        if(Util.esListaValida(estructuras)){
            for(Estructura estructura : estructuras){
                if(getGrillaModelMap().containsKey(estructura.getOrden())){
                    GrillaModelVO grillaModel =  getGrillaModelMap().get(estructura.getOrden());  
                    if(grillaModel.getTipoEstructura() == TipoEstructura.ESTRUCTURA_TIPO_GRILLA){
                        if(Util.esListaValida(grillaModel.getColumnas())){
                            GrillaVO grillaVO = getGeneradorDiseno().createTableModel(grillaModel.getColumnas(),grillaModel.getAgrupacionesMap() );
                            grillaVO.setTitulo(grillaModel.getTituloGrilla());
                            estructura.setGrillaVO(grillaVO);
                        }
                    }else if(grillaModel.getTipoEstructura() == TipoEstructura.ESTRUCTURA_TIPO_TEXTO){
                            estructura.setTextoVo(new TextoVO(grillaModel.getTexto()));
                    }else if(grillaModel.getTipoEstructura() == TipoEstructura.ESTRUCTURA_TIPO_HTML){
                            estructura.setHtmlVo(new HtmlVO(grillaModel.getHtml()));
                    }
                }
            }
        }
        return estructuras;
    }
    
    public Map<Long, GrillaModelVO> getGrillaModelMap(){
        Map<Long, GrillaModelVO> grillaModelMap = getGeneradorDiseno().getGrillaModelMap();
        return grillaModelMap;
    }
    
    public void setRenderedVisualizador(boolean renderedVisualizador) {
        this.renderedVisualizador = renderedVisualizador;
    }

    public boolean isRenderedVisualizador() {
        return renderedVisualizador;
    }

    public void visualizadorDisclosure(DisclosureEvent disclosureEvent) {
        setRenderedVisualizador(true);
    }
}
