package cl.mdr.ifrs.modules.seguridad.mb;

import java.io.IOException;
import java.io.Serializable;
import java.text.MessageFormat;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.NoResultException;

import org.apache.log4j.Logger;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.ejb.entity.Usuario;

/**
 * @author rreyes
 */
@ManagedBean
@ViewScoped
public class LoginBackingBean extends AbstractBackingBean implements Serializable{
	private transient Logger logger = Logger.getLogger(this.getClass().getName());
	private static final long serialVersionUID = -1985498172517251460L;
	
	private Usuario usuarioLogin;
		
	@PostConstruct
	void init(){	
		usuarioLogin = new Usuario();
	}
	
	/**
	 * @return
	 */
	public String loginAction(){
		logger.info("autentificando usuario");
		try {
			final Usuario usuario = super.getFacadeService().getSeguridadService().authenticateUser(this.getUsuarioLogin().getNombreUsuario());
			if(!usuario.getVigente().equals(1L)){
				super.addErrorMessage("El Usuario se encuentra suspendido, por favor, consulte al Administrador");				
				return null;
			}
			else if(!usuario.getPassword().equals(this.getUsuarioLogin().getPassword())){
				super.addErrorMessage("La Clave es incorrecta");
				this.getUsuarioLogin().setPassword(null);				
				return null;
			}
			
			
			logger.info("Login OK");			
			super.getExternalContext().getSessionMap().put(Usuario.class.getName(), usuario);			
			super.getExternalContext().redirect("pages/home.jsf");
		
		} catch (NoResultException e) {	
			super.addWarnMessage(MessageFormat.format("El Usuario <u>{0}</u> no existe", this.getUsuarioLogin().getNombreUsuario()));
			logger.error(e.getCause(), e);
		} catch (Exception e) {
			super.addErrorMessage("Se ha producido un Error al realizar el proceso de Autenticación");
			logger.error(e.getCause(), e);
		}
		return null;
	}
	
	/**
	 * @return
	 */
	public String logoutAction() {		
		try{
			super.getExternalContext().invalidateSession();
			super.getExternalContext().redirect("login.jsf?faces-redirect=true");
		} catch (IOException e) {		
			super.addErrorMessage("Se ha producido un Error al realizar el proceso de Logout");
			logger.error(e.getCause(), e);
		}
        return null;
    }

		
	public Usuario getUsuarioLogin() {
		return usuarioLogin;
	}

	public void setUsuarioLogin(Usuario usuarioLogin) {
		this.usuarioLogin = usuarioLogin;
	}	

}
