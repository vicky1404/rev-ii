package cl.mdr.ifrs.modules.seguridad.filter;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import cl.mdr.ifrs.ejb.entity.Usuario;

/**
 * @author rreyes
 */
public class RequestWrapper extends HttpServletRequestWrapper {

	private Principal principal;	
	
	public RequestWrapper(HttpServletRequest request) {
		super(request);		
	}
	
	@Override
	public Principal getUserPrincipal(){
		principal = new Principal() {			
			@Override
			public String getName() {				
				return ((Usuario)getSession().getAttribute(Usuario.class.getName())).getNombreUsuario();
			}
		};
		return principal;		
	}
	
	@Override
	public boolean isUserInRole(String role) {
		Usuario usuario = (Usuario) getSession().getAttribute(Usuario.class.getName());
		return usuario.getRol().getIdRol().equals(role);	
	}

}
