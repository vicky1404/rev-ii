package cl.mdr.ifrs.modules.perfilamiento.mb;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.index;
import static ch.lambdaj.Lambda.on;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;
import org.primefaces.model.DualListModel;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.ejb.entity.Grupo;
import cl.mdr.ifrs.ejb.entity.Usuario;
import cl.mdr.ifrs.ejb.entity.UsuarioGrupo;

/**
* @author rreyes
* @link http://cl.linkedin.com/in/rreyesc
*/
@ManagedBean
@ViewScoped
public class UsuarioPorGrupoBackingBean extends AbstractBackingBean implements Serializable {
	private transient Logger logger = Logger.getLogger(this.getClass().getName());
	private static final long serialVersionUID = 2860517343010041015L;
	
	private String idGrupoSelected;
	private DualListModel<String> dualListModelUsuarios;
	private List<String> source;
	private List<String> target;
	private List<Usuario> usuarioListSource;
	private List<Usuario> usuarioListTarget;
	private Map<String, Usuario> usuarioMap;
	
	private boolean renderDualList;
	
	public UsuarioPorGrupoBackingBean() {
		super();		
	}
	
	/**
	 * Accion que busca los usuarios que pertenecen y no pertenecen al grupo seleccionado en el filtro
	 * para su posterior asociacion.
	 * 
	 */
	public void buscarUsuarioPorGrupoAction(){
		try {
			this.setUsuarioListSource(super.getFacadeService().getSeguridadService().findUsuarioByGrupoNotIn( new Grupo(this.getIdGrupoSelected()) ));
			this.setUsuarioListTarget(super.getFacadeService().getSeguridadService().findUsuarioByGrupoIn( new Grupo(this.getIdGrupoSelected()) ));
			final Map<String, Usuario> usuarioMap = index(  super.getFacadeService().getSeguridadService().findUsuarioAll(), on(Usuario.class).getNombreUsuario() );			
			this.setUsuarioMap(usuarioMap);			
			this.setSource( extract(this.getUsuarioListSource(), on(Usuario.class).getNombreUsuario()) );
			this.setTarget( extract(this.getUsuarioListTarget(), on(Usuario.class).getNombreUsuario()) );
			this.setDualListModelUsuarios(new DualListModel<String>(this.getSource(), this.getTarget()));	
			this.setRenderDualList(Boolean.TRUE);
		} catch (Exception e) {
			super.addErrorMessage("", "Error al obtener información de Usuarios");
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * accion encargada de guardar la asociacion de usuarios con el grupo respectivo
	 * 
	 */
	public void guardarUsuarioPorGrupoAction(){
		try {			
			final Grupo grupo = super.getFacadeService().getSeguridadService().findGrupoAndUsuariosById(new Grupo(this.getIdGrupoSelected()));
			List<UsuarioGrupo> usuarioGrupos = new ArrayList<UsuarioGrupo>();
			UsuarioGrupo usuarioGrupo = null;
			for(String u : this.getDualListModelUsuarios().getTarget()){	
				usuarioGrupo = new UsuarioGrupo();
				usuarioGrupo.setGrupo(grupo);
				usuarioGrupo.setUsuario(this.getUsuarioMap().get(u));
				usuarioGrupos.add(usuarioGrupo);
			}			
			super.getFacadeService().getSeguridadService().persistUsuarioGrupo(usuarioGrupos, grupo);							
            super.addInfoMessage("", MessageFormat.format("Se actualizó correctamente el grupo de Usuarios {0} ", grupo.getNombre() )  );
		} catch (Exception e) {
			logger.error(e.getCause(), e);
			super.addErrorMessage("Error", "Error al actualizar el grupo de Usuarios");			
		}
	}
	
	public DualListModel<String> getDualListModelUsuarios() {
		return dualListModelUsuarios;
	}
	public void setDualListModelUsuarios(DualListModel<String> dualListModelUsuarios) {
		this.dualListModelUsuarios = dualListModelUsuarios;
	}
	public List<String> getSource() {
		return source;
	}
	public void setSource(List<String> source) {
		this.source = source;
	}
	public List<String> getTarget() {
		return target;
	}
	public void setTarget(List<String> target) {
		this.target = target;
	}
	
	public boolean isRenderDualList() {
		return renderDualList;
	}

	public void setRenderDualList(boolean renderDualList) {
		this.renderDualList = renderDualList;
	}

	public String getIdGrupoSelected() {
		return idGrupoSelected;
	}

	public void setIdGrupoSelected(String idGrupoSelected) {
		this.idGrupoSelected = idGrupoSelected;
	}

	public Map<String, Usuario> getUsuarioMap() {
		return usuarioMap;
	}

	public void setUsuarioMap(Map<String, Usuario> usuarioMap) {
		this.usuarioMap = usuarioMap;
	}

	public List<Usuario> getUsuarioListSource() {
		return usuarioListSource;
	}

	public void setUsuarioListSource(List<Usuario> usuarioListSource) {
		this.usuarioListSource = usuarioListSource;
	}

	public List<Usuario> getUsuarioListTarget() {
		return usuarioListTarget;
	}

	public void setUsuarioListTarget(List<Usuario> usuarioListTarget) {
		this.usuarioListTarget = usuarioListTarget;
	}
	

}
