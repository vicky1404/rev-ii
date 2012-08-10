package cl.mdr.ifrs.modules.perfilamiento.mb;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.mb.PropertyManager;
import cl.mdr.ifrs.ejb.entity.Rol;
import cl.mdr.ifrs.ejb.entity.Usuario;

@ManagedBean
@ViewScoped
public class UsuarioBackingBean extends AbstractBackingBean implements Serializable {
	private static final long serialVersionUID = -5520502900256277737L;
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private final static String POP_UP_EDITAR_USUARIO = "dialogEditarUsuario";
	private final static String FORM_EDITAR_USUARIO = "form_editusuario";
	private transient Usuario filtroUsuario;
	private transient Usuario nuevoUsuario;
	private transient Usuario editUsuario;
	private List<Usuario> usuarioList;
	public boolean renderTablaUsuarios;
	public Rol rol;
	private DataTable usuariosTable;
	
			
	@PostConstruct
	void init(){
		rol = new Rol();
		filtroUsuario = new Usuario();				
	}
	
	
	public String buscarAction(){
		try {			
			this.setUsuarioList(super.getFacadeService().getSeguridadService().findUsuariosByFiltro(this.getFiltroUsuario(), this.getRol()));
			this.setRenderTablaUsuarios(Boolean.TRUE);
		} catch (Exception e) {			
			logger.error(e);
			super.addErrorMessage(PropertyManager.getInstance().getMessage("mensaje_error_generico"));
		}
		return null;
	}
	
	public void guardarAction(ActionEvent event){
		try {
			this.getNuevoUsuario().setNombreUsuario(this.getNuevoUsuario().getNombreUsuario().toLowerCase());			
			super.getFacadeService().getSeguridadService().persistUsuario(this.getNuevoUsuario());
			super.addInfoMessage("Se ha Creado el Usuario con éxito");
			buscarAction();
		} catch (Exception e) {
			logger.error(e);
			super.addErrorMessage(PropertyManager.getInstance().getMessage("mensaje_error_generico"));
		}
	}
	
	public void editRowListener(){
		this.setEditUsuario((Usuario) this.getUsuariosTable().getRowData());
		super.displayPopUp(POP_UP_EDITAR_USUARIO, FORM_EDITAR_USUARIO);
	}
	
	public void editarAction(){
		try {
			this.getEditUsuario().setFechaActualizacion(new Date());
			super.getFacadeService().getSeguridadService().mergeUsuario(this.getEditUsuario());
			super.addInfoMessage("Se ha Modificado el Usuario con éxito");
			super.hidePopUp(POP_UP_EDITAR_USUARIO, FORM_EDITAR_USUARIO);
		} catch (Exception e) {
			logger.error(e);
			super.addErrorMessage(PropertyManager.getInstance().getMessage("mensaje_error_generico"));
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
		if(nuevoUsuario == null){
			nuevoUsuario = new Usuario();			
			nuevoUsuario.setRol(new Rol());
			nuevoUsuario.setFechaCreacion(new Date());
		}
		return nuevoUsuario;
	}


	public void setNuevoUsuario(Usuario nuevoUsuario) {
		this.nuevoUsuario = nuevoUsuario;
	}


	public Rol getRol() {
		return rol;
	}


	public void setRol(Rol rol) {
		this.rol = rol;
	}


	public Usuario getEditUsuario() {
		return editUsuario;
	}


	public void setEditUsuario(Usuario editUsuario) {
		this.editUsuario = editUsuario;
	}


	public DataTable getUsuariosTable() {
		return usuariosTable;
	}


	public void setUsuariosTable(DataTable usuariosTable) {
		this.usuariosTable = usuariosTable;
	}
	
	

}
