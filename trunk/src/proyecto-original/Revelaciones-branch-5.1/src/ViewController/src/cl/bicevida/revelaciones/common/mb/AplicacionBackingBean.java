package cl.bicevida.revelaciones.common.mb;


import cl.bicevida.revelaciones.ejb.cross.Constantes;
import cl.bicevida.revelaciones.ejb.entity.Catalogo;
import cl.bicevida.revelaciones.ejb.entity.Grupo;
import cl.bicevida.revelaciones.ejb.entity.SubGrilla;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import oracle.adf.view.rich.component.rich.output.RichInlineFrame;

import org.apache.log4j.Logger;


public class AplicacionBackingBean extends SoporteBackingBean implements Serializable{


    @SuppressWarnings("compatibility:7539758807977102735")
    private static final long serialVersionUID = -8917803707999960807L;
    
    public static final String APLICACION_BACKING_BEAN_NAME = "aplicacionBackingBean"; 
    private transient Logger logger = Logger.getLogger(AplicacionBackingBean.class);
    private transient RichInlineFrame iframeContenidoCentral;
    public static final String FACES_CONTEXT = "/faces"; 
    
    private boolean renderComboDebug = false;
    private List<Grupo> grupoByUsuario;
    
    public AplicacionBackingBean() {
    }
    
    @PostConstruct
    private void init(){
        try{
            this.setRenderComboDebug(super.validateGroupAndRol(Constantes.GRUPO_ROOT, Constantes.ROL_ADMIN)); 
        } catch (Exception e) {
            agregarErrorMessage("Error al inicializar combo de debug ");
        }        
    }
    
    public RichInlineFrame getIframeContenidoCentral() {
            return iframeContenidoCentral;
        }
    
    /**
     * accion encargada de asignar la vista seleccionada desde el menu en el
     * RichInlineFrame central.
     * @return
     */
    public String navigationHandler(ActionEvent event) {
        
        String urlRedirect = null;
        String urlMenu = super.getExternalContext().getRequestParameterMap().get("urlMenu");
        final Object param = event.getComponent().getAttributes().get("id_catalogo_param");
        
        try{          
            
            if (urlMenu == null){
            
                if(param != null && param instanceof Catalogo){
                    
                        super.getFiltro().setCatalogo((Catalogo) param);
                        urlRedirect = super.getExternalContext().getRequestParameterMap().get("urlTreeCatalogo");
                }
                else if (param != null && param instanceof SubGrilla){
                    
                        super.getFiltro().setCatalogo( ((SubGrilla)param).getGrilla().getEstructura1().getVersion().getCatalogo() );
                        super.getFiltro().setSubGrilla((SubGrilla)param);
                        urlRedirect = super.getExternalContext().getRequestParameterMap().get("urlTreeCatalogoDesagregado");
                        
                }
                
            } else {
                    urlRedirect = urlMenu;
            }
            
            if(urlRedirect != null){
                this.getIframeContenidoCentral().setSource(FACES_CONTEXT.concat(urlRedirect));        
            }
        }catch (Exception e) {
            this.getIframeContenidoCentral().setSource(FACES_CONTEXT.concat("/pages/error/not-found.jsf"));
            logger.error(e.getCause(), e);
        }                    
        return null;
    }

    public void setIframeContenidoCentral(RichInlineFrame iframeContenidoCentral) {
        this.iframeContenidoCentral = iframeContenidoCentral;
    }
    
    public void setRenderComboDebug(boolean renderComboDebug) {
        this.renderComboDebug = renderComboDebug;
    }

    public boolean isRenderComboDebug() {
        return renderComboDebug;
    }

    public void setGrupoByUsuario(List<Grupo> grupoByUsuario) {
        this.grupoByUsuario = grupoByUsuario;
    }

    public List<Grupo> getGrupoByUsuario() throws Exception {        
        grupoByUsuario = this.getFacade().getSeguridadService().findGruposByUsuario(this.getNombreUsuario());        
        return grupoByUsuario;
    }
    
    public List<SelectItem> getDebugOptions() {
        List<SelectItem> debugOptions = new ArrayList<SelectItem>();
        debugOptions.add(new SelectItem(Boolean.TRUE, "SI"));
        debugOptions.add(new SelectItem(Boolean.FALSE, "NO"));
        return debugOptions;
    }
}
