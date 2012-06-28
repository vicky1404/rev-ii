package cl.mdr.ifrs.cross.mb;

import java.security.Principal;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import cl.mdr.ifrs.ejb.facade.local.FacadeServiceLocal;

@ManagedBean
@ViewScoped
public abstract class AbstractBackingBean {
	
	@ManagedProperty(value="#{componenteBackingBean}")
	private ComponenteBackingBean componenteBackingBean;
	
	@EJB
	private FacadeServiceLocal facadeService;

	public FacadeServiceLocal getFacadeService() {
		return facadeService;
	}

	public FacesContext getFacesContext(){
		return FacesContext.getCurrentInstance();
	}
	
	public ExternalContext getExternalContext(){
		return this.getFacesContext().getExternalContext();
	}

	public Principal getPrincipal() {
		return this.getExternalContext().getUserPrincipal();
	}
	
	public String getNombreUsuario() {
        final Principal principal = this.getPrincipal();
        if (principal != null) {
            return principal.getName().toUpperCase();
        } else {
            return "usuario.prueba".toUpperCase();
        }
    }

	public void addErrorMessage(String summary, String detail) {
		addMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
	}

	public void addFatalMessage(String summary, String detail) {
		addMessage(FacesMessage.SEVERITY_FATAL, summary, detail);
	}

	public void addInfoMessage(String summary, String detail) {
		addMessage(FacesMessage.SEVERITY_INFO, summary, detail);
	}

	public void addWarnMessage(String summary, String detail) {
		addMessage(FacesMessage.SEVERITY_WARN, summary, detail);
	}

	private void addMessage(Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}
	
	/**
     * @return String
     */    
    public String getIpUsuario() {  
        String ip = this.getExternalContext().getRequestHeaderMap().get("X-Forwarded-For");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = this.getExternalContext().getRequestHeaderMap().get("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = this.getExternalContext().getRequestHeaderMap().get("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = this.getExternalContext().getRequestHeaderMap().get("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = this.getExternalContext().getRequestHeaderMap().get("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = this.getExternalContext().getRequestServerName();  
        }  
        return ip;  
    }  

    /**
     * @return
     */
    public String getViewId(){
        return getFacesContext().getViewRoot().getViewId();
    }
    
    public ComponenteBackingBean getComponenteBackingBean() {
        return componenteBackingBean;
    }

	public void setComponenteBackingBean(ComponenteBackingBean componenteBackingBean) {
		this.componenteBackingBean = componenteBackingBean;
	}
	
}
