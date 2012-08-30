package cl.mdr.ifrs.modules.perfilamiento.mb;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.log4j.Logger;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.model.CommonGridModel;
import cl.mdr.ifrs.ejb.entity.Catalogo;
import cl.mdr.ifrs.ejb.entity.Grupo;
import cl.mdr.ifrs.ejb.entity.TipoCuadro;

/**
* @author rreyes
* @link http://cl.linkedin.com/in/rreyesc
*/
@ManagedBean
@ViewScoped
public class EstructuraPorGrupoBackingBean extends AbstractBackingBean implements Serializable {
	private transient Logger logger = Logger.getLogger(this.getClass().getName());
	private static final long serialVersionUID = 8143886309518282313L;
	
	private String idGrupoSelected;
	private TipoCuadro tipoCuadroSelected;
		
	private transient List<CommonGridModel<Catalogo>> grillaCatalogoList;
	private boolean renderTablaCatalogo;
	
	public EstructuraPorGrupoBackingBean() {
		super();		
	}
	
	public void buscarEstructurasPorGrupoAction(ActionEvent event){    
		
		if(!super.isSelectedEmpresa())
			return;
		
		this.getTipoCuadroSelected();
        try {
            this.setGrillaCatalogoList(this.getCatalogoByGrupoList(super.getFacadeService().getCatalogoService().findCatalogoByFiltro(getFiltroBackingBean().getEmpresa().getIdRut(), null, this.getTipoCuadroSelected(), new Grupo(this.getIdGrupoSelected()), null), 
                                                                   super.getFacadeService().getCatalogoService().findAllVigenteByTipo(getFiltroBackingBean().getEmpresa().getIdRut(), this.getTipoCuadroSelected() )));            
            this.setRenderTablaCatalogo(Boolean.TRUE);
        } catch (Exception e) {
            super.addErrorMessage("Error al obtener el Catalogo");
            logger.error(e.getCause(), e);
        }
    }
	
	public void guardarCatalogoGrupoAction(ActionEvent event) {        
        List<Catalogo> catalogos = new ArrayList<Catalogo>();          
        try {
        	final Grupo grupo = super.getFacadeService().getSeguridadService().findGrupoAndCatalogoById(new Grupo(this.getIdGrupoSelected()));
            for (CommonGridModel<Catalogo> grillaCatalogo : getGrillaCatalogoList()) {
                if (grillaCatalogo.isSelected()) {
                	getFacadeService().getSeguridadService().persistCatalogoGrupo(grillaCatalogo.getEntity().getIdCatalogo(), grupo.getIdGrupoAcceso());
                }else{
                	getFacadeService().getSeguridadService().deleteCatalogoGrupo(grillaCatalogo.getEntity().getIdCatalogo(), grupo.getIdGrupoAcceso());
                }
            }
            grupo.setCatalogos(catalogos);
            //super.getFacadeService().getSeguridadService().mergeGrupo(grupo);
            super.addInfoMessage("", MessageFormat.format("Se actualizó correctamente la lista de accesos a las estructuras para el grupo {0} ", grupo.getNombre() ));
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            this.addErrorMessage("Error al actualizar permisos de Catálogo");
        }        
    }
	
	/**
     * metodo que genera los datos de grilla Catalogo, marcando seleccionados los que ya existen como permisos en la base de datos
     * @param menuAccesos
     * @param menuAll
     * @return List<CommonGridModel<Menu>>
     */
    public List<CommonGridModel<Catalogo>> getCatalogoByGrupoList(List<Catalogo> catalogoByGrupo,  List<Catalogo> catalogoAll) {
        grillaCatalogoList = new ArrayList<CommonGridModel<Catalogo>>();
        CommonGridModel<Catalogo> grillaCatalogo;
        for (Catalogo catalogo : catalogoAll) {
            grillaCatalogo = new CommonGridModel<Catalogo>();
            grillaCatalogo.setEntity(catalogo);
            for (Catalogo catalogo2 : catalogoByGrupo) {
                if (EqualsBuilder.reflectionEquals(catalogo.getIdCatalogo(), catalogo2.getIdCatalogo())) {
                    grillaCatalogo.setSelected(Boolean.TRUE);
                    break;
                }
            }
            grillaCatalogoList.add(grillaCatalogo);
        }
        return grillaCatalogoList;
    }
	
	

	public String getIdGrupoSelected() {
		return idGrupoSelected;
	}

	public void setIdGrupoSelected(String idGrupoSelected) {
		this.idGrupoSelected = idGrupoSelected;
	}

	public List<CommonGridModel<Catalogo>> getGrillaCatalogoList() {
		return grillaCatalogoList;
	}

	public void setGrillaCatalogoList(
			List<CommonGridModel<Catalogo>> grillaCatalogoList) {
		this.grillaCatalogoList = grillaCatalogoList;
	}

	public boolean isRenderTablaCatalogo() {
		return renderTablaCatalogo;
	}

	public void setRenderTablaCatalogo(boolean renderTablaCatalogo) {
		this.renderTablaCatalogo = renderTablaCatalogo;
	}

	public TipoCuadro getTipoCuadroSelected() {
		return tipoCuadroSelected;
	}

	public void setTipoCuadroSelected(TipoCuadro tipoCuadroSelected) {
		this.tipoCuadroSelected = tipoCuadroSelected;
	}

	
		
	
	
}
