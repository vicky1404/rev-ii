package cl.bicevida.revelaciones.common.mb;


import cl.bicevida.revelaciones.common.filtros.Filtro;
import cl.bicevida.revelaciones.common.model.CommonGridModel;
import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.revelaciones.ejb.entity.CodigoFecu;
import cl.bicevida.revelaciones.ejb.entity.EstadoFinanciero;
import cl.bicevida.revelaciones.ejb.entity.GrupoEeff;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;


/**
 * Clase controloladora para el mantenedor de caratula de EEFF
 * @author Manuel Gutierrez C.
 * @since 26/12/2012
 */

public class CaratulaEeffBackingBean extends SoporteBackingBean implements Serializable {    
    private transient Logger logger = Logger.getLogger(CaratulaEeffBackingBean.class);
    @SuppressWarnings("compatibility:-7446856244184267319")
    private static final long serialVersionUID = 7388265230878845608L;
    
    
    private transient List<CommonGridModel<EstadoFinanciero>> grillaEstadoFinancieroList;
    private List<GrupoEeff> grupoEeffList = null;
    private GrupoEeff grupoEeff;
    private boolean renderSinGrupos;
    private GrupoEeff grupoAsignacionEeff;
    
    public CaratulaEeffBackingBean() {
        super();
    }
    
    @PostConstruct
    void init(){
        
        try {
            this.grupoEeffList = new ArrayList<GrupoEeff>();
            this.setGrupoEeffList(getFacade().getFecuService().getGruposEeff());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            this.agregarErrorMessage("Error al cagar la lista de grupos");
        }
    }
    
    public void buscarAction(){
            
        try {
            
            Filtro filtroPaso = getFiltro();
            filtroPaso.getPeriodo().setPeriodo(null);
            
            grillaEstadoFinancieroList = new ArrayList<CommonGridModel<EstadoFinanciero>>();
            CommonGridModel<EstadoFinanciero> grillaEstadoFinanciero;
            List<EstadoFinanciero> estadoFinancieroList = new ArrayList<EstadoFinanciero>();
            
            Long anioMes = Util.getLong(filtroPaso.getPeriodo().getAnioPeriodo().concat(filtroPaso.getPeriodo().getMesPeriodo()), 0L);
            this.getFiltro().setPeriodo(this.getFacade().getPeriodoService().findPeriodoByPeriodo(anioMes));
            
            if (this.getGrupoEeff() != null){
            
                estadoFinancieroList = this.getFacade().getEstadoFinancieroService().getEeffVigenteByPeriodoAndGrupoEeff(this.getFiltro().getPeriodo().getIdPeriodo(), this.getGrupoEeff());
                renderSinGrupos = false;
                
            } else {
                
                estadoFinancieroList = this.getFacade().getEstadoFinancieroService().getEeffVigenteByPeriodoSinGrupoEeff(this.getFiltro().getPeriodo().getIdPeriodo());
                renderSinGrupos = true;
                
                }
            
            for (EstadoFinanciero eeff : estadoFinancieroList) {
                grillaEstadoFinanciero = new CommonGridModel<EstadoFinanciero>();
                grillaEstadoFinanciero.setEntity(eeff);
                grillaEstadoFinanciero.setSelected(Boolean.FALSE);
                grillaEstadoFinancieroList.add(grillaEstadoFinanciero);
            }
            
            Collections.sort(grillaEstadoFinancieroList, new Comparator<CommonGridModel<EstadoFinanciero>>(){
             public int compare(CommonGridModel<EstadoFinanciero> ef1, CommonGridModel<EstadoFinanciero> ef2) {
                 
                    return  ef1.getEntity().getCodigoFecu().getOrden().compareTo(ef2.getEntity().getCodigoFecu().getOrden());
                }                            
                });            
            
        } catch (Exception e) {
            e.printStackTrace();    
            logger.error(e);
            this.agregarErrorMessage("Hay un problema con los periodos");     
        }
            
    }
    
    public void asignarGrupoEeff(){
        
        try{
            if (this.getGrupoAsignacionEeff() != null){            
                if (getGrillaEstadoFinancieroList() != null && getGrillaEstadoFinancieroList().size() > 0){
                    for (CommonGridModel<EstadoFinanciero> grillaEeff : getGrillaEstadoFinancieroList()) {
                        if (grillaEeff.isSelected()) {
                                this.getFacade().getFecuService().mergeAsignarGrupoACodigoFecu(grillaEeff.getEntity().getCodigoFecu(), this.getGrupoAsignacionEeff());
                        }
                        
                    }   
                } else {
                        this.agregarWarnMessage("No hay datos para asignar");     
                    }
            } else {
                    this.agregarWarnMessage("Debe seleccionar un grupo antes de asignar");
                }
        this.buscarAction();
        } catch (Exception ex){
                ex.printStackTrace();    
                logger.error(ex);
                this.agregarErrorMessage("Hay un problema en la asignación");     
            }
    }
    
    public void desasignarGrupoEeff(){
        
        try{
            if (getGrillaEstadoFinancieroList() != null && getGrillaEstadoFinancieroList().size() > 0){
                for (CommonGridModel<EstadoFinanciero> grillaEeff : getGrillaEstadoFinancieroList()) {
                    if (grillaEeff.isSelected()) {
                            this.getFacade().getFecuService().mergeAsignarGrupoACodigoFecu(grillaEeff.getEntity().getCodigoFecu(), null);
                    }
                }   
            } else {
                    this.agregarWarnMessage("No hay datos para desasignar");     
            }
            
        this.buscarAction();
        } catch (Exception ex){
                ex.printStackTrace();    
                logger.error(ex);
                this.agregarErrorMessage("Hay un problema en la desasignación");     
            }
    }
    
    public void guardarAction(){
        
        try{
            List<CodigoFecu> fecuList = new ArrayList<CodigoFecu>();
            if (getGrillaEstadoFinancieroList() != null && getGrillaEstadoFinancieroList().size() > 0){
                for (CommonGridModel<EstadoFinanciero> grillaEeff : getGrillaEstadoFinancieroList()) {
                            fecuList.add(grillaEeff.getEntity().getCodigoFecu());
                } 
            } else {
                    this.agregarWarnMessage("No hay datos para modificar");     
                }
            
            this.getFacade().getFecuService().mergeConfiguracionPortadaEEFF(fecuList, super.getNombreUsuario());
            this.buscarAction();
            this.agregarSuccesMessage("Se guardaron los datos con éxito");
            
        } catch (Exception ex){
                ex.printStackTrace();    
                logger.error(ex);
                this.agregarErrorMessage("Hay un problema al guardar los datos");     
            }
    }


    public void setGrupoEeffList(List<GrupoEeff> grupoEeffList) {
        this.grupoEeffList = grupoEeffList;
    }

    public List<GrupoEeff> getGrupoEeffList() {
        return grupoEeffList;
    }

    public void setGrupoEeff(GrupoEeff grupoEeff) {
        this.grupoEeff = grupoEeff;
    }

    public GrupoEeff getGrupoEeff() {
        return grupoEeff;
    }

    public void setRenderSinGrupos(boolean renderSinGrupos) {
        this.renderSinGrupos = renderSinGrupos;
    }

    public boolean isRenderSinGrupos() {
        return renderSinGrupos;
    }

    public void setGrillaEstadoFinancieroList(List<CommonGridModel<EstadoFinanciero>> grillaEstadoFinancieroList) {
        this.grillaEstadoFinancieroList = grillaEstadoFinancieroList;
    }

    public List<CommonGridModel<EstadoFinanciero>> getGrillaEstadoFinancieroList() {
        return grillaEstadoFinancieroList;
    }

    public void setGrupoAsignacionEeff(GrupoEeff grupoAsignacionEeff) {
        this.grupoAsignacionEeff = grupoAsignacionEeff;
    }

    public GrupoEeff getGrupoAsignacionEeff() {
        return grupoAsignacionEeff;
    }
}
