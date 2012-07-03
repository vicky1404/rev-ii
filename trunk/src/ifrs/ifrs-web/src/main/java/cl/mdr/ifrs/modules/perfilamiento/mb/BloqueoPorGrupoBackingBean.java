/**
 * 
 */
package cl.mdr.ifrs.modules.perfilamiento.mb;

import static org.hamcrest.Matchers.equalTo;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.model.CommonGridModel;
import cl.mdr.ifrs.ejb.entity.Grupo;

/**
 * @author rreyes
 * @link http://cl.linkedin.com/in/rreyesc
 */
@ManagedBean
@ViewScoped
public class BloqueoPorGrupoBackingBean extends AbstractBackingBean implements Serializable {
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private static final long serialVersionUID = -4236186956272081968L;
	
	 private transient List<CommonGridModel<Grupo>> grillaBloqueoGrupoList;
	 private boolean renderTablaGruposBloqueados;
	 private Long accesoBloqueado = 100L;
	 
	 @PostConstruct
	 void init(){
		 inicializarPagina();
	 }
	 
	 
	 private void inicializarPagina(){
		try{
			this.setGrillaBloqueoGrupoList(this.getGruposBloqueados(this.getGrupoListByFiltroAcceso()));
			this.setRenderTablaGruposBloqueados(Boolean.TRUE);
		} catch (Exception e) {			
			super.addErrorMessage("", "Error al obtener los grupos para bloquear ingreso al Sistema");
            logger.error(e.getCause(), e);
		}
	 }
	 
	 public void buscarBloqueoPorGrupo(){
        try {            
            this.setGrillaBloqueoGrupoList(this.getGruposBloqueados(this.getGrupoListByFiltroAcceso()));    
            this.setRenderTablaGruposBloqueados(Boolean.TRUE);
        } catch (Exception e) {
            super.addErrorMessage("", "Error al obtener los grupos para bloquear ingreso al Sistema");
            logger.error(e.getCause(), e);
        }        
    }
	 
	 
	 public List<CommonGridModel<Grupo>> getGruposBloqueados(List<Grupo> grupoAccesosAll){
        CommonGridModel<Grupo> grillaBloqueoGrupo;
        grillaBloqueoGrupoList = new ArrayList<CommonGridModel<Grupo>>();
        for(Grupo grupo : grupoAccesosAll) {
            grillaBloqueoGrupo = new CommonGridModel<Grupo>();
            grillaBloqueoGrupo.setEntity(grupo);
            if(cl.mdr.ifrs.ejb.cross.Util.getLong(grupo.getAccesoBloqueado(), 0L).equals(1L)){
                grillaBloqueoGrupo.setSelected(Boolean.TRUE);
            }
            grillaBloqueoGrupoList.add(grillaBloqueoGrupo);            
        }
        return grillaBloqueoGrupoList;
    }
	 
	 private List<Grupo> getGrupoListByFiltroAcceso() throws Exception {
        List<Grupo> grupoList;
        if(this.getAccesoBloqueado() != null && !this.getAccesoBloqueado().equals(100L)){
            grupoList = select(super.getFacadeService().getSeguridadService().findGruposAll() ,having(on(Grupo.class).getAccesoBloqueado(), equalTo(this.getAccesoBloqueado())));
        }
        else if (this.getAccesoBloqueado().equals(100L)){
        	grupoList = super.getFacadeService().getSeguridadService().findGruposAll();
        }
        else{
            grupoList = super.getFacadeService().getSeguridadService().findGruposAll();
        }
        return grupoList;
    }
	 
	 
	 /**
     * procesa la list de grupos para actualizar flag de bloqueo de ingreso de información en el sistema
     * @return
     */
    public void guardarBloqueoGrupoAction() {        
        try {
            List<Grupo> grupoBloqueadoList = new ArrayList<Grupo>();
            for (CommonGridModel<Grupo> grillaGrupo : getGrillaBloqueoGrupoList()){                
                grillaGrupo.getEntity().setAccesoBloqueado(grillaGrupo.isSelected() ? 1L : 0L);   
                grupoBloqueadoList.add(grillaGrupo.getEntity());            
            }
            super.getFacadeService().getSeguridadService().updateBloqueoGrupo(grupoBloqueadoList);                       
            super.addInfoMessage("Éxito", "Se actualizó correctamente el bloqueo del Sistema");                        
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            super.addErrorMessage("Error", "Error al bloquear el sistema");
        }       
    } 


	public List<CommonGridModel<Grupo>> getGrillaBloqueoGrupoList() {
		return grillaBloqueoGrupoList;
	}


	public void setGrillaBloqueoGrupoList(
			List<CommonGridModel<Grupo>> grillaBloqueoGrupoList) {
		this.grillaBloqueoGrupoList = grillaBloqueoGrupoList;
	}


	public boolean isRenderTablaGruposBloqueados() {
		return renderTablaGruposBloqueados;
	}


	public void setRenderTablaGruposBloqueados(boolean renderTablaGruposBloqueados) {
		this.renderTablaGruposBloqueados = renderTablaGruposBloqueados;
	}


	public Long getAccesoBloqueado() {
		return accesoBloqueado;
	}


	public void setAccesoBloqueado(Long accesoBloqueado) {
		this.accesoBloqueado = accesoBloqueado;
	}

}
