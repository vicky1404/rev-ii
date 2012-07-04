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
import javax.servlet.http.HttpServletRequest;

import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.Periodo;
import cl.mdr.ifrs.ejb.facade.local.FacadeServiceLocal;
import cl.mdr.ifrs.modules.mb.GeneradorDisenoBackingBean;
import cl.mdr.ifrs.modules.mb.GeneradorVersionBackingBean;
import cl.mdr.ifrs.modules.mb.GeneradorVisualizadorBackingBean;

@ManagedBean
@ViewScoped
public abstract class AbstractBackingBean {
	
	@EJB
	private FacadeServiceLocal facadeService;
	
	@ManagedProperty(value="#{componenteBackingBean}")
	private ComponenteBackingBean componenteBackingBean;
	
	@ManagedProperty(value="#{generadorVersion}")
    private GeneradorVersionBackingBean generadorVersion;
    
	@ManagedProperty(value="#{generadorDiseno}")
    private GeneradorDisenoBackingBean generadorDiseno;
    
	@ManagedProperty(value="#{generadorVisualizador}")
    private GeneradorVisualizadorBackingBean generadorVisualizador;
	
	@ManagedProperty(value="#{filtroBackingBean}")
    private FiltroBackingBean filtroBackingBean;
	
	
	public GeneradorVersionBackingBean getGeneradorVersion() {
		return generadorVersion;
	}

	public void setGeneradorVersion(GeneradorVersionBackingBean generadorVersion) {
		this.generadorVersion = generadorVersion;
	}

	public GeneradorDisenoBackingBean getGeneradorDiseno() {
		return generadorDiseno;
	}

	public void setGeneradorDiseno(GeneradorDisenoBackingBean generadorDiseno) {
		this.generadorDiseno = generadorDiseno;
	}

	public GeneradorVisualizadorBackingBean getGeneradorVisualizador() {
		return generadorVisualizador;
	}

	public void setGeneradorVisualizador(
			GeneradorVisualizadorBackingBean generadorVisualizador) {
		this.generadorVisualizador = generadorVisualizador;
	}

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
		//return this.getExternalContext().getUserPrincipal();
		return this.getRequest().getUserPrincipal();
	}
	
	/**
	 * @return
	 */
	public String getNombreUsuario() {
        final Principal principal = this.getPrincipal();
        if (principal != null) {
            return principal.getName().toUpperCase();
        } else {
            return "usuario.prueba".toUpperCase();
        }
    }
	
	public HttpServletRequest getRequest(){
		return (HttpServletRequest) this.getExternalContext().getRequest();
	}
	
	/**
     * Obtiene un objeto desde la persistencia a partir del filtro de periodo en sesion.
     * @return
     * @throws Exception
     */
    public Periodo getFiltroPeriodo() throws Exception{                           
        this.getFiltroBackingBean().setPeriodo(this.getFacadeService().getMantenedoresTipoService().findByPeriodo(Util.getLong(getFiltroBackingBean().getPeriodo().getAnioPeriodo().concat(getFiltroBackingBean().getPeriodo().getMesPeriodo()), null)));                                                 
        return this.getFiltroBackingBean().getPeriodo();        
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
	
	public void addErrorMessage(String summary) {
		addMessage(FacesMessage.SEVERITY_ERROR, summary, null);
	}

	public void addFatalMessage(String summary) {
		addMessage(FacesMessage.SEVERITY_FATAL, summary, null);
	}

	public void addInfoMessage(String summary) {
		addMessage(FacesMessage.SEVERITY_INFO, summary, null);
	}

	public void addWarnMessage(String summary) {
		addMessage(FacesMessage.SEVERITY_WARN, summary, null);
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

	public FiltroBackingBean getFiltroBackingBean() {
		return filtroBackingBean;
	}

	public void setFiltroBackingBean(FiltroBackingBean filtroBackingBean) {
		this.filtroBackingBean = filtroBackingBean;
	}
	
    
	
}
