package cl.mdr.ifrs.cross.mb;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.Principal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.primefaces.context.RequestContext;


import cl.mdr.ifrs.ejb.cross.Constantes;
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
	
	private Integer restaLic = new Integer(0);
	private boolean renderMensajeExpiracionLicencia;
	
	

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
		
		Calendar fechaArchivo = GregorianCalendar.getInstance();
		Calendar fechaActual = GregorianCalendar.getInstance();
		
		//fechaArchivo.set(2012, 0, 20);
		fechaArchivo.set(Integer.parseInt(this.flic().substring(4, 8)) , Integer.parseInt(this.flic().substring(2, 4)) - 1 , Integer.parseInt( this.flic().substring(0, 2) ) );
		fechaActual.setTime(new Date());
		
		List<Empresa> empresaList = null;
		
		File archivo = new File(this.getlicFile());
				
		if (!archivo.exists()){
			return false;
		}	
		
		this.setRestaLic(new Integer(Util.restaLic(fechaArchivo, fechaActual)));
		
		if (this.licType().equalsIgnoreCase(Constantes.TYPE_INST_CL)){
			if ((Constantes.D_LIC.intValue() - this.getRestaLic().intValue()) <= 0 ){
				return false;
			}
			
			empresaList = this.getFacadeService().getEmpresaService().findAll();
			if(empresaList != null){
				
				if (empresaList.size() > getEmpresaRegList().size()){
					return false;	
				}
				
				for (Long rut : getEmpresaRegList()){
					boolean encontrado = false;
					for (Empresa empresa : empresaList){
						if (empresa.getIdRut().intValue() == rut.intValue()){
							encontrado = true;
							break;
						}
					}
					
					if (!encontrado){
						return false;
					}
				}
				
			}
		} else if (this.licType().equalsIgnoreCase(Constantes.TYPE_INST_OP)){
			if ((Constantes.D_LIC.intValue() - this.getRestaLic().intValue()) <= 0 ){
				return false;
			}
			empresaList = this.getFacadeService().getEmpresaService().findAll();
			if (empresaList != null && empresaList.size() > 0 && empresaList.size() != this.numRuts().intValue()){
				return false;	
			}
		}
		else if (this.licType().equalsIgnoreCase(Constantes.TYPE_INST_FR)){
			return true;
		}
		else {
				return false;
		}
		
		
		return true;
	}
	
	private List<Long> getEmpresaRegList(){
		StringTokenizer strRut = new StringTokenizer(this.licRuts(), ";");
		List<Long> empresaRegList = new ArrayList<Long>();
		while(strRut.hasMoreElements()){
			empresaRegList.add(Util.getLong((String)strRut.nextElement(),0L));
		}
		return empresaRegList;
	}
	
	public String licType(){
		
	 String type="";
	 String key="";
	 
	 try{
		  FileInputStream fstream = new FileInputStream(this.getlicFile());
		  DataInputStream in = new DataInputStream(fstream);
		  BufferedReader br = new BufferedReader(new InputStreamReader(in));
		  String strLine;
		  
		  	while ((strLine = br.readLine()) != null)   {
		  			strLine = decrypt(strLine);
		  		    key = strLine.substring(0, strLine.indexOf("="));
		  		    
		  		    if (key.equalsIgnoreCase(Constantes.TYPE_INST)){
		  		    	type = strLine.substring(strLine.indexOf("=")+1, strLine.length());	
		  		    	break;
		  		    }
		  			
		  	}
		  	
		  	in.close();
		  	
	 }catch (Exception e){
		 System.err.println("Error: " + e.getMessage());
		 e.printStackTrace();
	 }
	 
	 return type;
	}
	
	public String flic(){
		
		 String type="";
		 String key="";
		 
		 try{
			  FileInputStream fstream = new FileInputStream(this.getlicFile());
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  
			  	while ((strLine = br.readLine()) != null)   {
			  			strLine = decrypt(strLine);
			  		    key = strLine.substring(0, strLine.indexOf("="));
			  		    
			  		    if (key.equalsIgnoreCase(Constantes.FLIC)){
			  		    	type = strLine.substring(strLine.indexOf("=")+1, strLine.length());	
			  		    	break;
			  		    }
			  			
			  	}
			  	
			  	in.close();
			  	
		 }catch (Exception e){
			 System.err.println("Error: " + e.getMessage());
			 e.printStackTrace();
		 }
		 
		 return type;
		}
	
	
	public String licRuts(){
		
		 String type="";
		 String key="";
		 
		 try{
			  FileInputStream fstream = new FileInputStream(this.getlicFile());
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  
			  	while ((strLine = br.readLine()) != null)   {
			  			strLine = decrypt(strLine);
			  		    key = strLine.substring(0, strLine.indexOf("="));
			  		    
			  		    if (key.equalsIgnoreCase(Constantes.KEYS)){
			  		    	type = strLine.substring(strLine.indexOf("=")+1, strLine.length());	
			  		    	break;
			  		    }
			  			
			  	}
			  	
			  	in.close();
			  	
		 }catch (Exception e){
			 System.err.println("Error: " + e.getMessage());
			 e.printStackTrace();
		 }
		 
		 return type;
		}
	
	
	public Integer numRuts(){
		
		 String type="";
		 String key="";
		 
		 try{
			  FileInputStream fstream = new FileInputStream(this.getlicFile());
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  
			  	while ((strLine = br.readLine()) != null)   {
			  			strLine = decrypt(strLine);
			  		    key = strLine.substring(0, strLine.indexOf("="));
			  		    
			  		    if (key.equalsIgnoreCase(Constantes.NUM)){
			  		    	type = strLine.substring(strLine.indexOf("=")+1, strLine.length());	
			  		    	break;
			  		    }
			  			
			  	}
			  	
			  	in.close();
			  	
		 }catch (Exception e){
			 System.err.println("Error: " + e.getMessage());
			 e.printStackTrace();
		 }
		 
		 if (type == null){
			 type = "0";
		 }
		 
		 return Integer.parseInt(type);
		}
	
	private String getlicFile(){
		
		return getExternalContext().getRealPath(File.separator) .concat( "WEB-INF" ).concat(File.separator)  .concat( Constantes.LIC_FILE_NAME);
		
	}
	
	public String decrypt(String cadena) {
		StandardPBEStringEncryptor s = new StandardPBEStringEncryptor();
		s.setPassword("uniquekey");
		String devuelve = "";
		try {
			
			devuelve = s.decrypt(cadena);
			
		} catch (Exception e) {
			
		}
		return devuelve;
	} 
	
	public String formatTaxonomyParentName(String name){
        name = name.replaceAll("http://www.svs.cl/cl/fr/cs/role/", "");
        if(name.contains("nota")){
            name = Util.capitalizar(name.replaceAll("_role", ""));
        }
        return name;
    }
	
	public Integer getRestaLic() {
		return restaLic;
	}

	public void setRestaLic(Integer restaLic) {
		this.restaLic = restaLic;
	}

	public boolean isRenderMensajeExpiracionLicencia() {
		
		if (this.getRestaLic() != null && (Constantes.D_LIC.intValue() - this.restaLic.intValue()) <= Constantes.D_EX.intValue()){
			
			renderMensajeExpiracionLicencia = true;
			
			if (this.licType().equalsIgnoreCase(Constantes.TYPE_INST_FR)){
				renderMensajeExpiracionLicencia = false;
			}
			
			
		} else {
			
			renderMensajeExpiracionLicencia = false;
			
		}
		
		return renderMensajeExpiracionLicencia;
	}

	public void setRenderMensajeExpiracionLicencia(
			boolean renderMensajeExpiracionLicencia) {
		this.renderMensajeExpiracionLicencia = renderMensajeExpiracionLicencia;
	}
	
	public int getDiasRestantes(){
		return (Constantes.D_LIC.intValue() - this.getRestaLic().intValue());
	}
	
	public boolean isRenderLicenciaExpiro(){
		
		if (this.getRestaLic() != null && (Constantes.D_LIC.intValue() - this.getRestaLic().intValue()) <= 0){
			renderMensajeExpiracionLicencia = true;
			if (this.licType().equalsIgnoreCase(Constantes.TYPE_INST_FR)){
				renderMensajeExpiracionLicencia = false;
			}
			
		} else {
			renderMensajeExpiracionLicencia = false;
		}
		return renderMensajeExpiracionLicencia;
	}

}