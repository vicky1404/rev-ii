package cl.mdr.ifrs.modules.perfilamiento.mb;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.ejb.entity.Usuario;

@ManagedBean
@ViewScoped
public class MisDatosBackingBean extends AbstractBackingBean implements Serializable  {
	private static final long serialVersionUID = 4818380140733330076L;
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	
	private Usuario usuario;
	
	@PostConstruct
	void init(){
		this.setUsuario(super.getUsuarioSesion());		
	}
	
	
	/**
	 * @return the usuario
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
