package cl.mdr.ifrs.modules.perfilamiento.mb;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import javax.mail.MessagingException;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.inputtext.InputText;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.mb.PropertyManager;
import cl.mdr.ifrs.ejb.cross.Util;
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
	private InputText inputNombreUsuario;
		
			
	@PostConstruct
	void init(){
		rol = new Rol();
		filtroUsuario = new Usuario();				
	}
	
	
	public String buscarAction(){
		try {			
			//this.setUsuarioList(super.getFacadeService().getSeguridadService().findUsuariosByFiltro(this.getFiltroUsuario(), this.getRol()));
			this.setUsuarioList(super.getFacadeService().getSeguridadService().findUsuariosByNombreUsuario(this.getFiltroUsuario()));
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
			this.getNuevoUsuario().setPassword(RandomStringUtils.randomAlphanumeric(16).toUpperCase());
			this.getNuevoUsuario().setCambiarPassword(1L);
			super.getFacadeService().getSeguridadService().crearNuevoUsuario(this.getNuevoUsuario());
			super.addInfoMessage(MessageFormat.format("Se ha creado el Usuario con éxito y se ha enviado un email de confirmación a: {0}", this.getNuevoUsuario().getEmail()));
			this.setNuevoUsuario(null);
			buscarAction();
		} catch (MessagingException e) {
			logger.error(e);
			super.addErrorMessage("Se ha producido un error al enviar el mensaje de confirmación al usuario");
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
	
	public void validarNuevoUsuario(FacesContext context, UIComponent toValidate, Object value) {
		try {			
			final String nombreUsuario = super.getFacadeService().getSeguridadService().validaUsuarioExiste(Util.getString(value, null));
			if(nombreUsuario != null){
				FacesMessage message = new FacesMessage();
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				message.setSummary(MessageFormat.format("El Usuario {0} ya existe en el sistema, intente nuevamente", Util.getString(value, null)));				
				context.addMessage(this.getInputNombreUsuario().getClientId(context), message);
				throw new ValidatorException(message);
			}		
		} catch (Exception e) {
			FacesMessage message = new FacesMessage();
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary("Ocurrio un error al validar al usuario que intenta crear, intente nuevamente.");			
			context.addMessage(this.getInputNombreUsuario().getClientId(context), message);
			throw new ValidatorException(message);
		}		
	}
	
	public void resetUsuarioPassword(ActionEvent event){
		try{
			this.getEditUsuario().setFechaActualizacion(new Date());
			this.getEditUsuario().setPassword(RandomStringUtils.randomAlphanumeric(16).toUpperCase());
			this.getEditUsuario().setCambiarPassword(1L);			
			super.getFacadeService().getSeguridadService().resetearClaveUsuario(this.getEditUsuario());			
			super.addInfoMessage(MessageFormat.format("Se ha regenerado la clave de Usuario éxitosamente y se ha enviado un email de confirmación a: {0}", this.getEditUsuario().getEmail()));
			super.hidePopUp(POP_UP_EDITAR_USUARIO, FORM_EDITAR_USUARIO);
		} catch (Exception e) {
			logger.error(e);
			super.addErrorMessage(PropertyManager.getInstance().getMessage("mensaje_error_generico"));
		}
	}
	
	@SuppressWarnings("unused")
	private String generaClave() throws Exception{
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
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


	public InputText getInputNombreUsuario() {
		return inputNombreUsuario;
	}


	public void setInputNombreUsuario(InputText inputNombreUsuario) {
		this.inputNombreUsuario = inputNombreUsuario;
	}
	
	

}
