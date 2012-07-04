package cl.mdr.ifrs.modules.seguridad.mb;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;

import cl.mdr.ifrs.ejb.entity.Usuario;

@ManagedBean
@ViewScoped
public class LoginBackingBean implements Serializable{
	private transient Logger logger = Logger.getLogger(this.getClass().getName());
	private static final long serialVersionUID = -1985498172517251460L;
	
	private Usuario usuario;
	
	@PostConstruct
	void init(){
		usuario = new Usuario("usuario.prueba");
	}
	
	public String loginAction(){
		logger.info("autentificando usuario");
		return null;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
