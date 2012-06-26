package cl.bicevida.revelaciones.common.mb;


import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;

import cl.bicevida.revelaciones.common.filtros.Filtro;
import cl.bicevida.revelaciones.common.util.BeanUtil;
import cl.bicevida.revelaciones.common.util.GeneradorDisenoHelper;
import cl.bicevida.revelaciones.common.util.PropertyManager;
import cl.bicevida.revelaciones.ejb.common.EstadoCuadroEnum;
import cl.bicevida.revelaciones.ejb.common.TipoImpresionEnum;
import cl.bicevida.revelaciones.ejb.cross.Constantes;
import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.revelaciones.ejb.entity.AgrupacionColumna;
import cl.bicevida.revelaciones.ejb.entity.Catalogo;
import cl.bicevida.revelaciones.ejb.entity.Columna;
import cl.bicevida.revelaciones.ejb.entity.EstadoCuadro;
import cl.bicevida.revelaciones.ejb.entity.Estructura;
import cl.bicevida.revelaciones.ejb.entity.Grilla;
import cl.bicevida.revelaciones.ejb.entity.Grupo;
import cl.bicevida.revelaciones.ejb.entity.Periodo;
import cl.bicevida.revelaciones.ejb.entity.TipoCelda;
import cl.bicevida.revelaciones.ejb.entity.TipoCuadro;
import cl.bicevida.revelaciones.ejb.entity.TipoDato;
import cl.bicevida.revelaciones.ejb.entity.TipoEstructura;
import cl.bicevida.revelaciones.ejb.entity.VersionPeriodo;
import cl.bicevida.revelaciones.ejb.facade.local.FacadeServiceLocal;
import cl.bicevida.revelaciones.mb.CuadroBackingBean;
import cl.bicevida.revelaciones.mb.GeneradorDisenoBackingBean;
import cl.bicevida.revelaciones.mb.GeneradorVersionBackingBean;
import cl.bicevida.revelaciones.mb.GeneradorVisualizadorBackingBean;
import cl.bicevida.revelaciones.mb.ReporteUtilBackingBean;
import cl.bicevida.revelaciones.vo.AgrupacionColumnaModelVO;
import cl.bicevida.revelaciones.vo.AgrupacionModelVO;
import cl.bicevida.revelaciones.vo.AgrupacionVO;

import java.io.Serializable;

import java.security.Principal;

import java.text.MessageFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import javax.persistence.NoResultException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.context.AdfFacesContext;

import org.apache.log4j.Logger;

import static org.hamcrest.Matchers.equalTo;


public abstract class SoporteBackingBean implements Serializable{   
    private transient Logger logger = Logger.getLogger(SoporteBackingBean.class);
    
    @EJB
    private FacadeServiceLocal facade;

    @SuppressWarnings("compatibility:-4047785155702748500")
    private static final long serialVersionUID = -5390367330811567088L;
    public static final String BEAN_NAME = "soporteBackingBean";    
    
    private Locale localeCL = new Locale("es", "CL");
    
    /*Backing Bean*/
    private GeneradorVersionBackingBean generadorVersion;
    private GeneradorDisenoBackingBean generadorDiseno;
    private GeneradorVisualizadorBackingBean generadorVisualizador;
    private ReporteUtilBackingBean reporteUtil;
    private CuadroBackingBean cuadroBackingBean;
    private ComponenteBackingBean componenteBackingBean;
    private Filtro filtro;
    private AplicacionBackingBean aplicacionBackingBean;
    private boolean sistemaBloqueado;
        

    public SoporteBackingBean() {
    }
    

    public FacadeServiceLocal getFacade() {
        return facade;
    }
    
    /**
     * @return HttpServletRequest
     */
    public HttpServletRequest getRequest(){
        return (HttpServletRequest)getFacesContext().getExternalContext().getRequest();
    }

    /**
     * @return HttpServletResponse
     */
    public HttpServletResponse getResponse(){
        return (HttpServletResponse)getFacesContext().getExternalContext().getResponse();
    }

    /**
     * @return
     */
    public ExternalContext getExternalContext(){
        return getFacesContext().getExternalContext();
    }

    
    /**
     * @return String
     */
    public String getHostUsuario(){
        return getRequest().getRemoteHost();
    }
    
    /**
     * @return String
     */    
    public String getIpUsuario() {  
        String ip = this.getRequest().getHeader("X-Forwarded-For");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = this.getRequest().getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = this.getRequest().getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = this.getRequest().getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = this.getRequest().getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = this.getRequest().getRemoteAddr();  
        }  
        return ip;  
    }  

    /**
     * @return
     */
    public String getViewId(){
        return getFacesContext().getViewRoot().getViewId();
    }
    
    /**
     * FacesContext de JSF
     * @return FacesContext
     */
    public FacesContext getFacesContext(){
        return FacesContext.getCurrentInstance();
    }  
        
    public void setFiltro(Filtro filtro) {
        this.filtro = filtro;
    }

    
    /**
     * Agrega un mensaje de exito
     * @param mensaje
     */
    protected void agregarSuccesMessage(final String mensaje) {
        this.agregarMessage(mensaje, FacesMessage.SEVERITY_INFO, null);
    }

    /**
     * Agrega mensaje de error
     * @param mensaje
     */
    protected void agregarErrorMessage(final String mensaje) {
        agregarMessage(mensaje, FacesMessage.SEVERITY_ERROR, null);
    }

    /**
     * Agrega un mensaje de warning
     * @param mensaje
     */
    protected void agregarWarnMessage(final String mensaje) {
        agregarMessage(mensaje, FacesMessage.SEVERITY_WARN, null);
    }

    private void agregarMessage(final String mensaje, final Severity severity, final String detail) {
        final Iterator<FacesMessage> messages = (Iterator<FacesMessage>)getFacesContext().getMessages();
        while (messages.hasNext()) {
            final FacesMessage message = messages.next();
            if (message.getSummary().equals(mensaje)) {
                return;
            }
        }
        getFacesContext().addMessage(null, new FacesMessage(severity, mensaje, detail));
    }

    public void setLocaleCL(Locale localeCL) {
        this.localeCL = localeCL;
    }

    public Locale getLocaleCL() {
        return localeCL;
    }
    
    public Principal getPrincipal() {
        return getExternalContext().getUserPrincipal();
    }
    
    public String getNombreUsuario() {
        final Principal principal = getPrincipal();
        if (principal != null) {
            return principal.getName().toUpperCase();
        } else {
            return "usuario.prueba".toUpperCase();
        }
    }
    
    public void setADFPartialTarget(Object componente){
            AdfFacesContext context = AdfFacesContext.getCurrentInstance();
            context.addPartialTarget((UIComponent)componente);
    }
    
    public Filtro getFiltro() {
        if (filtro == null) {
            filtro = BeanUtil.findBean(Filtro.FILTRO_BEAN_NAME);
        }
        return filtro;
    }
    
    public CuadroBackingBean getCuadroBackingBean() {
        if (cuadroBackingBean == null) {
            cuadroBackingBean = BeanUtil.findBean(cuadroBackingBean.BEAN_NAME);
        }
        return cuadroBackingBean;
    }
    
    public GeneradorDisenoBackingBean getGeneradorDiseno() {
        if(generadorDiseno == null){
            generadorDiseno = BeanUtil.findBean(GeneradorDisenoBackingBean.BEAN_NAME);
        }
        return generadorDiseno;
    }
    
    public GeneradorVersionBackingBean getGeneradorVersion() {
        if(generadorVersion == null){
            generadorVersion = BeanUtil.findBean(GeneradorVersionBackingBean.BEAN_NAME);
        }
        return generadorVersion;
    }
    
    public GeneradorVisualizadorBackingBean getGeneradorVisualizador() {
        if(generadorVisualizador == null){
            generadorVisualizador = BeanUtil.findBean(GeneradorVisualizadorBackingBean.BEAN_NAME);
        }
        return generadorVisualizador;
    }
    
    public ReporteUtilBackingBean getReporteUtilBackingBean() {
        if(reporteUtil == null){
            reporteUtil = BeanUtil.findBean(ReporteUtilBackingBean.BEAN_NAME);
        }
        return reporteUtil;
    }
    
    public ComponenteBackingBean getComponenteBackingBean() {
        if(componenteBackingBean == null){
            componenteBackingBean = BeanUtil.findBean(ComponenteBackingBean.BEAN_NAME);
        }
        return componenteBackingBean;
    }
    
    public AplicacionBackingBean getAplicacionBackingBean() {
        if(aplicacionBackingBean == null){
            aplicacionBackingBean = BeanUtil.findBean(aplicacionBackingBean.APLICACION_BACKING_BEAN_NAME);
        }
        return aplicacionBackingBean;
    }
    
    public Periodo getFiltroPeriodo() throws Exception{                           
        this.getFiltro().setPeriodo(this.getFacade().getMantenedoresTipoService().findByPeriodo(
                        cl.bicevida.revelaciones.ejb.cross.Util.getLong(getFiltro().getPeriodo().getAnioPeriodo().concat(getFiltro().getPeriodo().getMesPeriodo()), null)));                                         
        
        return this.getFiltro().getPeriodo();        
    }
    
    /**
     * Valida si el usuario logueado tiene los permisos necesarios para 
     * modificar el cuadro en cierto estado
     * @param versionPeriodo
     * @return
     * @throws Exception  
     */
    public boolean validaModificarCuadro(VersionPeriodo versionPeriodo)throws Exception{
        boolean retorno = Boolean.TRUE;
        if(versionPeriodo.getEstado().getIdEstado().equals(EstadoCuadroEnum.CERRADO.getKey())){
            retorno = Boolean.FALSE;
        }
        if(versionPeriodo.getEstado().getIdEstado().equals(EstadoCuadroEnum.INICIADO.getKey())){
            if(!(getRequest().isUserInRole(Constantes.ROL_RESP)) && 
               !(getRequest().isUserInRole(Constantes.ROL_SUP)) &&
               !(getRequest().isUserInRole(Constantes.ROL_ENC)) &&
               !(getRequest().isUserInRole(Constantes.ROL_ADMIN))){
                retorno = Boolean.FALSE;                
            }
        }
        if(versionPeriodo.getEstado().getIdEstado().equals(EstadoCuadroEnum.MODIFICADO.getKey())){
            if(!(getRequest().isUserInRole(Constantes.ROL_RESP)) && 
               !(getRequest().isUserInRole(Constantes.ROL_SUP)) &&
               !(getRequest().isUserInRole(Constantes.ROL_ENC)) &&
               !(getRequest().isUserInRole(Constantes.ROL_ADMIN))){
                retorno = Boolean.FALSE;     
            }
        }
        if(versionPeriodo.getEstado().getIdEstado().equals(EstadoCuadroEnum.POR_APROBAR.getKey())){
            if(!(getRequest().isUserInRole(Constantes.ROL_RESP)) && 
               !(getRequest().isUserInRole(Constantes.ROL_SUP)) &&
               !(getRequest().isUserInRole(Constantes.ROL_ENC)) &&
               !(getRequest().isUserInRole(Constantes.ROL_ADMIN))){
                retorno = Boolean.FALSE;     
            }
        }
        if(versionPeriodo.getEstado().getIdEstado().equals(EstadoCuadroEnum.APROBADO.getKey())){
            if(!(getRequest().isUserInRole(Constantes.ROL_RESP)) && 
               !(getRequest().isUserInRole(Constantes.ROL_SUP)) && 
               !(getRequest().isUserInRole(Constantes.ROL_ADMIN))){
                retorno = Boolean.FALSE;     
            }
        }
        if(versionPeriodo.getEstado().getIdEstado().equals(EstadoCuadroEnum.CERRADO.getKey())){
            if(!(getRequest().isUserInRole(Constantes.ROL_RESP)) && 
               !(getRequest().isUserInRole(Constantes.ROL_ADMIN))){
                retorno = Boolean.FALSE;     
            }
        }
        if(versionPeriodo.getEstado().getIdEstado().equals(EstadoCuadroEnum.CONTINGENCIA.getKey())){
            if(!(getRequest().isUserInRole(Constantes.ROL_RESP)) && 
               !(getRequest().isUserInRole(Constantes.ROL_ADMIN))){
                retorno = Boolean.FALSE;     
            }
        }
        if(retorno == Boolean.FALSE){
            agregarErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_nota_sin_privilegios_modificar"));
        }
        //SI hay alguna validación que generó un mensaje de error, no se puede guardar cambios
        if(getFacesContext().getMessages().hasNext()){
            retorno = Boolean.FALSE;
        }            
        return retorno;
    }
    
    /**
     * Méthod return id of dinamic type grid
     * @return
     * @throws Exception  
     */
    public Long getIdDinamicGrid(){
        return Grilla.TIPO_GRILLA_DINAMICA;
    }
    
    /**
     * Méthod return id of static type grid
     * @return
     * @throws Exception  
     */
    public Long getIdStaticGrid(){
        return Grilla.TIPO_GRILLA_ESTATICA;
    }
    
    
    


    public void rutValidate(FacesContext facesContext, UIComponent uIComponent, Object object) {
    
        RichInputText text = (RichInputText) uIComponent;
        
        if(object==null){
            text.setValid(true);
            return;
        }
        
        String valor = object.toString();
            
        String[] rut_dv = valor.split("-");
        // Las partes del rut (numero y dv) deben tener una longitud positiva
        if ( rut_dv.length == 2   )
        {
            try
            {
                int rut = Integer.parseInt(rut_dv[0]);
                char dv = rut_dv[1].charAt(0);
                boolean resultado = Util.validarRut(rut, dv);
                text.setValid(resultado);
            }catch(Exception e){
                text.setValid(false);
            }
        }else{
            text.setValid(false);
        }
        
        if(!text.isValid()){
            FacesMessage message = new FacesMessage();
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            message.setSummary("Rut no válido");
            message.setDetail("Ingrese un rut válido (xxxxxxxx-x)");
            facesContext.addMessage(text.getClientId(facesContext.getCurrentInstance()), message);  
        }
    }
    
    public boolean isSelectedPeriod(Periodo periodo){
        
        if(periodo.getMesPeriodo()==null){
            agregarWarnMessage("Seleccione Mes");
            return false;
        }
        if(periodo.getAnioPeriodo()==null){
            agregarWarnMessage("Seleccione Año");
            return false;
        }
        
        return true;
            
    }
    
    public void catalogoValidator(FacesContext facesContext, UIComponent uIComponent, Object object) {        
        
        ((RichInputText)uIComponent).setValid(false);
        
        if(object==null)
            return;
        
        try{
            
            String valor = ((String)object).replaceAll(" ","");
            if(valor.contains("ID:")){
                Long id = Long.valueOf(valor.substring(valor.lastIndexOf("ID:")+3,valor.length()));
            }
            ((RichInputText)uIComponent).setValid(true);
        }catch(Exception e){
            agregarWarnMessage("No se debe modificar el valor auto completado");
            logger.error(e.getMessage(),e);
        }
    }
    
    //TODO mover metodo    
    public List<AgrupacionColumnaModelVO> soporteAgrupacionColumna(Long idGrilla, List<Columna> columnas , List<AgrupacionColumna> agrupaciones) {

        Map<Long, Columna> columnaMap = new LinkedHashMap<Long, Columna>();
        Map<Long, AgrupacionColumnaModelVO> nivel1Map = new LinkedHashMap<Long, AgrupacionColumnaModelVO>();
        Map<Long, AgrupacionColumnaModelVO> nivel2Map = new LinkedHashMap<Long, AgrupacionColumnaModelVO>();
        List<AgrupacionColumnaModelVO> nivelesList = new ArrayList<AgrupacionColumnaModelVO>();
        Map<Long,Long> nivel = new HashMap<Long,Long>();
        Long niveles = 0L;
        
        try {
            
            for(Columna columna : columnas){
                columnaMap.put(columna.getIdColumna(), columna);
            }
            
            List<AgrupacionVO> agrupacionesNivel = GeneradorDisenoHelper.crearAgrupadorVO(agrupaciones, 1L);
            
            List<AgrupacionModelVO> agrupacionesModel = new ArrayList<AgrupacionModelVO>();
            
            for(AgrupacionVO agrupacion : agrupacionesNivel){
                
                AgrupacionModelVO agrupacionModel = new AgrupacionModelVO();
                agrupacionModel.setDesde(agrupacion.getDesde());
                agrupacionModel.setHasta(agrupacion.getHasta());
                agrupacionModel.setGrupo(agrupacion.getGrupo());
                agrupacionModel.setAncho(agrupacion.getAncho());
                agrupacionModel.setNivel(agrupacion.getNivel());
                agrupacionModel.setIdGrilla(agrupacion.getIdGrilla());
                agrupacionModel.setTitulo(agrupacion.getTitulo());
                agrupacionesModel.add(agrupacionModel);
            }
            
            
            
            for(AgrupacionModelVO agrupacion : agrupacionesModel){
                
                if(agrupacion.getNivel() == 1L){
                    
                    List<Columna> columnasNew = new ArrayList<Columna>();
                    AgrupacionColumnaModelVO agrupacionN1VO = new AgrupacionColumnaModelVO();
                    agrupacionN1VO.setTituloNivel(agrupacion.getTitulo());
                    agrupacionN1VO.setNivel(1L);
                    agrupacionN1VO.setAgrupacion(agrupacion);
                    for(Long i=agrupacion.getDesde(); i<= agrupacion.getHasta(); i++){
                        if(columnaMap.containsKey(i)){
                            columnasNew.add(columnaMap.get(i));
                            niveles = 1L;
                        }
                    }
                    agrupacionN1VO.setColumnas(columnasNew);
                    nivel1Map.put(agrupacion.getGrupo(), agrupacionN1VO);
                    
                }else  if(agrupacion.getNivel() == 2L){

                    AgrupacionColumnaModelVO agrupacionN2VO = new AgrupacionColumnaModelVO();
                    
                    List<AgrupacionColumnaModelVO> niveles1New = new ArrayList<AgrupacionColumnaModelVO>();
                    
                    for(Long i=agrupacion.getDesde(); i<= agrupacion.getHasta(); i++){
                        
                        //Long grupoNivel1 = GeneradorDisenoHelper.getGrupoNivel1(nivel1Map,i);
                        AgrupacionColumna col = new AgrupacionColumna();
                        col.setIdNivel(1L);
                        col.setIdGrilla(idGrilla);
                        col.setIdColumna(i);
                        col = getFacade().getEstructuraService().findAgrupacionColumnaById(col);
                        Long grupoNivel1 = col.getGrupo();
                        
                        if(grupoNivel1 != null && !nivel.containsKey(grupoNivel1)){
                            nivel.put(grupoNivel1, grupoNivel1);
                            niveles1New.add(nivel1Map.get(grupoNivel1));
                            niveles = 2L;
                        }/*else if(grupoNivel1 != null){                            
                            if(nivel1Map.containsKey(grupoNivel1)){
                                niveles1New.add(nivel1Map.get(grupoNivel1));
                            }
                        }*/
                    }
                    
                    agrupacionN2VO.setTituloNivel(agrupacion.getTitulo());
                    agrupacionN2VO.setNivel(2L);
                    agrupacionN2VO.setNiveles(niveles1New);
                    
                    nivel2Map.put(agrupacion.getGrupo(), agrupacionN2VO);
              }
            }
            
            if (niveles == 0L) {
                return null;   
            }
            
            if (niveles == 1L) {
                Iterator it = nivel1Map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry)it.next();
                    nivelesList.add((AgrupacionColumnaModelVO)entry.getValue());
                    
                }
            }

            if (niveles == 2L) {
                Iterator it = nivel2Map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry)it.next();
                    nivelesList.add((AgrupacionColumnaModelVO)entry.getValue());
                }
            }

        } catch (Exception e) {
            e.getStackTrace();
        }
        
        return nivelesList;
    }
    //TODO mover metodo
    public void setListGrilla(List<Estructura> estructuraList) throws Exception {
            
            for(Estructura estructuras : estructuraList){
                if(estructuras.getTipoEstructura().getIdTipoEstructura().equals(TipoEstructura.ESTRUCTURA_TIPO_GRILLA)){
                    for(Grilla grilla : estructuras.getGrillaList()){
                        List<AgrupacionColumna> agrupaciones = getFacade().getEstructuraService().findAgrupacionColumnaByGrilla(grilla);
                        List<AgrupacionColumnaModelVO> agrupacionColumnaList = soporteAgrupacionColumna(grilla.getIdGrilla(), grilla.getColumnaList(),agrupaciones);                        
                        if(agrupacionColumnaList==null || agrupacionColumnaList.isEmpty()){
                            estructuras.getGrillaVO().setColumnas(grilla.getColumnaList());
                            estructuras.getGrillaVO().setNivel(0L);
                        }else if(agrupacionColumnaList.get(0).getNivel() == 1L){
                            estructuras.getGrillaVO().setNivel(agrupacionColumnaList.get(0).getNivel());
                            estructuras.getGrillaVO().setNivel1List(agrupacionColumnaList);
                        }else if(agrupacionColumnaList.get(0).getNivel() == 2L){
                            estructuras.getGrillaVO().setNivel(agrupacionColumnaList.get(0).getNivel());
                            estructuras.getGrillaVO().setNivel2List(agrupacionColumnaList);    
                        }
                    }
                }
            }
    }
    //TODO mover metodo
    public void setGrillaVO(Estructura estructura) throws Exception {
            
        if(estructura.getTipoEstructura().getIdTipoEstructura().equals(TipoEstructura.ESTRUCTURA_TIPO_GRILLA)){
            for(Grilla grilla : estructura.getGrillaList()){
                List<AgrupacionColumna> agrupaciones = getFacade().getEstructuraService().findAgrupacionColumnaByGrilla(grilla);
                List<AgrupacionColumnaModelVO> agrupacionColumnaList = soporteAgrupacionColumna(grilla.getIdGrilla(), grilla.getColumnaList(),agrupaciones);                        
                if(agrupacionColumnaList==null || agrupacionColumnaList.isEmpty()){
                    estructura.getGrillaVO().setColumnas(grilla.getColumnaList());
                    estructura.getGrillaVO().setNivel(0L);
                }else if(agrupacionColumnaList.get(0).getNivel() == 1L){
                    estructura.getGrillaVO().setNivel(agrupacionColumnaList.get(0).getNivel());
                    estructura.getGrillaVO().setNivel1List(agrupacionColumnaList);
                }else if(agrupacionColumnaList.get(0).getNivel() == 2L){
                    estructura.getGrillaVO().setNivel(agrupacionColumnaList.get(0).getNivel());
                    estructura.getGrillaVO().setNivel2List(agrupacionColumnaList);    
                }
            }
        }
    }
    
    //Verificar metodo RDV
    /*public Object getInformacion(){
        VersionPeriodo periodo = new VersionPeriodo();
        periodo = (VersionPeriodo)periodoCatalogoTable.getRowData();
        renderedInformacion = true;
        getFiltro().setVersion(periodo.getVersion());
        return null;
    }*/
    
    /*@SuppressWarnings("unchecked")
    public static <T> T findBean(String name) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        return (T)ctx.getApplication().evaluateExpressionGet(ctx, "#{" + name + "}", Object.class);
    }*/
    
    public Long getTipoFormulaEstatica(){
        return Grilla.TIPO_GRILLA_ESTATICA;
    }
    
    public Long getTipoFormulaDinamica(){
        return Grilla.TIPO_GRILLA_DINAMICA;
    }
    
    public boolean validateGroupAndRol(final String idGrupo, final String rolOid) throws Exception{
        boolean valid = Boolean.FALSE;                 
        final List<Grupo> grupoList = select(this.getFacade().getSeguridadService().findGruposByUsuario(this.getNombreUsuario()), having(on(Grupo.class).getIdGrupo(), equalTo(idGrupo)));            
        if(grupoList.size() > 0 && getRequest().isUserInRole(rolOid)){
            valid = Boolean.TRUE;                  
        }  
        return valid;    
    }
    
    /*public boolean validateSistemaBloqueadoByGrupo() throws Exception {
        boolean locked = Boolean.FALSE;
        final List<Grupo> grupoList = select(this.getFacade().getSeguridadService().findGruposByUsuario(this.getNombreUsuario()), having(on(Grupo.class).getAccesoBloquedo(), equalTo(1L)));            
        if(grupoList != null && grupoList.size() > 0){
            locked = Boolean.TRUE;
        }
        return locked;
    }*/

    public void setSistemaBloqueado(boolean sistemaBloqueado) {
        this.sistemaBloqueado = sistemaBloqueado;
    }

    public boolean isSistemaBloqueado() throws Exception {
        sistemaBloqueado = Boolean.FALSE;
        final List<Grupo> grupoList = select(this.getFacade().getSeguridadService().findGruposByUsuario(this.getNombreUsuario()), having(on(Grupo.class).getAccesoBloquedo(), equalTo(1L)));            
        if(grupoList != null && grupoList.size() > 0){
            sistemaBloqueado = Boolean.TRUE;
        }
        return sistemaBloqueado;
    }
}
