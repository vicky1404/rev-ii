package cl.mdr.ifrs.modules.perfilamiento.mb;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.ejb.entity.Usuario;

@ManagedBean
@ViewScoped
public class UsuarioBackingBean extends AbstractBackingBean implements Serializable {
	private static final long serialVersionUID = -5520502900256277737L;
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	
	private transient Usuario filtroUsuario;
	private transient Usuario nuevoUsuario;
	private List<Usuario> usuarioList;
	public boolean renderTablaUsuarios;
			
	@PostConstruct
	void init(){
		filtroUsuario = new Usuario();
		nuevoUsuario = new Usuario();
	}
	
	
	public String buscarAction(){
		try {
			this.setUsuarioList(super.getFacadeService().getSeguridadService().findUsuariosByFiltro(this.getFiltroUsuario()));
			this.setRenderTablaUsuarios(Boolean.TRUE);
		} catch (Exception e) {			
			logger.error(e);
		}
		return null;
	}
	
	public void guardarAction(ActionEvent event){
		try {
			super.getFacadeService().getSeguridadService().persistUsuario(this.getNuevoUsuario());
			buscarAction();
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	
	

	public Usuario getFiltroUsuario() {
		return filtroUsuario;
	}

	public void setFiltroUsuario(Usuario filtroUsuario) {
		this.filtroUsuario = filtroUsuario;
	}


	public List<Usuario> getUsuarioList() {
		return usuarioList;
	}


	public void setUsuarioList(List<Usuario> usuarioList) {
		this.usuarioList = usuarioList;
	}


	public boolean isRenderTablaUsuarios() {
		return renderTablaUsuarios;
	}


	public void setRenderTablaUsuarios(boolean renderTablaUsuarios) {
		this.renderTablaUsuarios = renderTablaUsuarios;
	}


	public Usuario getNuevoUsuario() {
		return nuevoUsuario;
	}


	public void setNuevoUsuario(Usuario nuevoUsuario) {
		this.nuevoUsuario = nuevoUsuario;
	}
	
	

}
