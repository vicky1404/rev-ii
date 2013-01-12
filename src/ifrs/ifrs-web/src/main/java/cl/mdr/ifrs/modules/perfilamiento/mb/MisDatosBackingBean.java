package cl.mdr.ifrs.modules.perfilamiento.mb;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.mb.PropertyManager;
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
	
	public void editAction(ActionEvent actionEvent){
		try {
			Usuario usuario = this.getUsuario();
			final Usuario usuarioPersisted = super.getFacadeService().getSeguridadService().authenticateUser(usuario.getNombreUsuario());
			if(usuario.getCambiarPassword().equals(1L) && usuario.getPassword().equals(usuarioPersisted.getPassword())){
				super.addWarnMessage("La nueva clave no debe ser igual a la generda por el Administrador del Sistema");
				return;
			}					
			usuario.setCambiarPassword(0L);
			this.getFacadeService().getSeguridadService().mergeUsuario(usuario);
			super.addInfoMessage("Estimado Usuario, sus datos se han actualizado correctamente.");
		} catch (Exception e) {
			logger.error(e);
			super.addErrorMessage(PropertyManager.getInstance().getMessage("mensaje_error_generico"));
		}
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
