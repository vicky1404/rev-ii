package cl.mdr.ifrs.cross.mb;

import java.security.Principal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;

import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.Empresa;
import cl.mdr.ifrs.ejb.entity.Grilla;
import cl.mdr.ifrs.ejb.entity.PeriodoEmpresa;
import cl.mdr.ifrs.ejb.entity.Usuario;
import cl.mdr.ifrs.ejb.facade.local.FacadeServiceLocal;

@ManagedBean
@ViewScoped
public abstract class AbstractBackingBean {
	
	@EJB
	private FacadeServiceLocal facadeService;
	
	@ManagedProperty(value="#{componenteBackingBean}")
	private ComponenteBackingBean componenteBackingBean;
	
	@ManagedProperty(value="#{filtroBackingBean}")
    private FiltroBackingBean filtroBackingBean;

	private Locale localeCL = new Locale("es", "CL");
	
	/*Variables que validan las licencias*/
	private final int VERSION_SOFT = 2;
	private final String EMP_SOFT = "1;15505123;";
	private final String EMP_SOFT_DV = "9;K;";
	
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
	
	public boolean isUserInRole(String role){
		return this.getRequest().isUserInRole(role);
	}
	
	/**
	 * @return
	 */
	/**
	 * @return
	 */
	public String getNombreUsuario() {
        final Principal principal = this.getPrincipal();
        if (principal != null) {
            return principal.getName();
        } else {
            return "USUARIO.PRUEBA";
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
    public PeriodoEmpresa getFiltroPeriodoEmpresa() throws Exception{
    	
        this.getFiltroBackingBean().setPeriodoEmpresa(this.getFacadeService().getPeriodoService().getPeriodoEmpresaById(Util.getLong(getFiltroBackingBean().getAnio().concat(getFiltroBackingBean().getMes()),null),getFiltroBackingBean().getEmpresa().getIdRut()));                                                 
        
        return this.getFiltroBackingBean().getPeriodoEmpresa();
        
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
	
	/**
	 * Muestra un componente de tipo pop up definido en la vista
	 * @param idDialog
	 * @param idUpdate
	 */
	public void displayPopUp(final String idDialog, final String idUpdate){
    	RequestContext context = RequestContext.getCurrentInstance();    	
    	context.execute(MessageFormat.format("{0}.show();", idDialog));
        context.update(idUpdate);  
    }
    
    
	/**
	 * Esconde un componente de tipo pop up definido en la vista
	 * @param idDialog
	 * @param idUpdate
	 */
	public void hidePopUp(final String idDialog, final String idUpdate){
    	RequestContext context = RequestContext.getCurrentInstance();    	
    	context.execute(MessageFormat.format("{0}.hide();", idDialog));
        context.update(idUpdate);  
    }
	
	public Usuario getUsuarioSesion(){
		return (Usuario) this.getExternalContext().getSessionMap().get(Usuario.class.getName());
	}
	
	public Long getTipoFormulaEstatica(){
        return Grilla.TIPO_GRILLA_ESTATICA;
    }
    
    public Long getTipoFormulaDinamica(){
        return Grilla.TIPO_GRILLA_DINAMICA;
    }

	public Locale getLocaleCL() {
		return localeCL;
	}

	public void setLocaleCL(Locale localeCL) {
		this.localeCL = localeCL;
	}
	
	/**
	 * Metodo que valida si se ha seleccionado una empresa
	 * Si no se ha seleccionado una empresa, escribe un mensaje de informacion
	 */
	public boolean isSelectedEmpresa(){
		
		if(getFiltroBackingBean().getEmpresa() == null || getFiltroBackingBean().getEmpresa().getIdRut() == null){
			addWarnMessage("Se debe seleccionar una Empresa");
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * Metodo que valida si se ha seleccionado una periodo
	 * Si no se ha seleccionado una periodo, escribe un mensaje de informacion
	 */
	public boolean isSelectedFiltroPeriodo(){
		if(getFiltroBackingBean().getMes()==null || getFiltroBackingBean().getAnio() == null){
			addWarnMessage("Se debe seleccionar una periodo (Mes y Año)");
			return false;
		}
		return true;
	}
	
	protected boolean isValidSoft() throws Exception{
		
		List<Empresa> empresaList = this.getFacadeService().getEmpresaService().findDistEmpresa(getEmpresaRegList());
		if(Util.esListaValida(empresaList)){
			int lic = empresaList.size();
			StringTokenizer strRut = new StringTokenizer(EMP_SOFT, ";");
			StringTokenizer strDv = new StringTokenizer(EMP_SOFT_DV, ";");
			addFatalMessage(new StringBuilder("Cantidad de Empresas Registradas en Base de Datos: ").append(lic).toString());
			addFatalMessage(new StringBuilder("Usted tiene disponible ").append(VERSION_SOFT).append(" Licencias ").append("para las siguientes Empresas:").toString());
			for(int i=0; i<VERSION_SOFT; i++){
				addFatalMessage(new StringBuilder(strRut.nextToken()).append("-").append(strDv.nextElement()).toString());
			}
			return false;
		}
		
		return true;
	}
	
	private List<Long> getEmpresaRegList(){
		StringTokenizer strRut = new StringTokenizer(EMP_SOFT, ";");
		List<Long> empresaRegList = new ArrayList<Long>();
		while(strRut.hasMoreElements()){
			empresaRegList.add(Util.getLong((String)strRut.nextElement(),0L));
		}
		return empresaRegList;
	}

}
