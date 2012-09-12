package cl.mdr.ifrs.modules.seguridad.mb;

import java.io.IOException;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.NoResultException;

import org.apache.log4j.Logger;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.Usuario;

/**
 * @author rreyes
 * @link http://cl.linkedin.com/in/rreyesc
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
				super.addErrorMessage("<p>El Usuario se encuentra suspendido del sistema,</p> <p>por favor, consulte al Administrador</p>");				
				return null;
			}
			else if(!usuario.getPassword().equals(this.getUsuarioLogin().getPassword())){
				super.addErrorMessage("La Clave ingresada es incorrecta");
				this.getUsuarioLogin().setPassword(null);				
				return null;
			}
			else if (Util.getLong(usuario.getCambiarPassword(), 0L).equals(1L)){
				super.getExternalContext().getSessionMap().put(Usuario.class.getName(), usuario);			
				super.getExternalContext().redirect("pages/mis-datos.jsf");				
			}else{									
				logger.info("Login OK");
				usuario.setFechaUltimoAcceso(new Date());
				super.getFacadeService().getSeguridadService().mergeUsuario(usuario);
				super.getExternalContext().getSessionMap().put(Usuario.class.getName(), usuario);			
				super.getExternalContext().redirect("pages/home.jsf");
			}
		
		} catch (NoResultException e) {	
			super.addWarnMessage(MessageFormat.format("El Usuario <u>{0}</u> no existe", this.getUsuarioLogin().getNombreUsuario()));
			logger.error(e.getCause(), e);
		} catch (Exception e) {
			super.addErrorMessage("Se ha producido un Error al realizar el proceso de Autentificación");
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
			super.getExternalContext().redirect(super.getExternalContext().getRequestContextPath().concat("/login.jsf?logout=1"));
			super.addInfoMessage("", "Se ha Cerrado correctamente su Sesión en el Sistema");
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
