package cl.mdr.ifrs.cross.mb;

import java.security.Principal;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import cl.mdr.ifrs.cross.util.UtilBean;

public abstract class AbstractBackingBean {
	
	/*Backing Bean*/
	private ComponenteBackingBean componenteBackingBean;

	public FacesContext getFacesContext(){
		return FacesContext.getCurrentInstance();
	}
	
	public ExternalContext getExternalContext(){
		return this.getFacesContext().getExternalContext();
	}

	public Principal getPrincipal() {
		return this.getExternalContext().getUserPrincipal();
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
	
    public ComponenteBackingBean getComponenteBackingBean() {
        if(componenteBackingBean == null){
            componenteBackingBean = UtilBean.findBean(ComponenteBackingBean.BEAN_NAME);
        }
        return componenteBackingBean;
    }
}
