package cl.mdr.ifrs.modules.mb;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.apache.log4j.Logger;
import org.primefaces.event.RowEditEvent;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.mb.ComponenteBackingBean;
import cl.mdr.ifrs.cross.mb.PropertyManager;
import cl.mdr.ifrs.ejb.entity.Catalogo;
import cl.mdr.ifrs.ejb.entity.Empresa;
import cl.mdr.ifrs.ejb.entity.Estructura;
import cl.mdr.ifrs.ejb.entity.TipoCuadro;


@ManagedBean(name="cuadroBackingBean")
@ViewScoped
public class CuadroBackingBean extends AbstractBackingBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6867816117743274288L;
	
	private Long idCuadro;
	private Logger logger = Logger.getLogger(CuadroBackingBean.class);
	private TipoCuadro tipoCuadro;
	private List<Estructura> lista;
	private ComponenteBackingBean componenteBackingBean;
	private List<Catalogo> catalogoList;
	private Catalogo selectedCuadro;
	private Catalogo nuevoCuadro;

	


	@PostConstruct	
	public void init(){		
		obtenerLista();
	}
	
	
	public void obtenerLista(){
		
		try {
			
			this.setCatalogoList(super.getFacadeService().getCatalogoService().findAllByTipo(getIdCuadro(), null, getFiltroBackingBean().getEmpresa().getIdRut()));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void editar(RowEditEvent event){
		getFacadeService().getMantenedoresTipoService().mergeEntity((Catalogo) event.getObject());
		obtenerLista();
		setSelectedCuadro(null);
	}
	
	public void eliminar(){
		
		
		try {
			if (getSelectedCuadro() != null && getSelectedCuadro().getIdCatalogo() != null){
				getFacadeService().getMantenedoresTipoService().deleteCuadro(getSelectedCuadro());
			} else {
				super.addWarnMessage(PropertyManager.getInstance().getMessage("mensaje_tabla_seleccionar_registro"), null );
				return;
			}
			
			super.addInfoMessage(PropertyManager.getInstance().getMessage("mensaje_tabla_eliminar_registro"), null );
		} catch (Exception e) {
			e.printStackTrace();
			super.addErrorMessage(PropertyManager.getInstance().getMessage("mensaje_error_generico"), null );
		}
		obtenerLista();
		setSelectedCuadro(null);
		
	
	}
	
	public void actualizaLista(){
					obtenerLista();
	}
	
	public void guardar(ActionEvent event){
        try {            
        	
        	getNuevoCuadro().setEmpresa(getFiltroBackingBean().getEmpresa());
        	getNuevoCuadro().setTipoCuadro(new TipoCuadro(getNuevoCuadro().getTipoCuadro().getIdTipoCuadro()));        	
            super.getFacadeService().getCatalogoService().persistEntity(getNuevoCuadro());
            obtenerLista();
            setNuevoCuadro(null);
            super.addInfoMessage(PropertyManager.getInstance().getMessage("mensaje_tabla_guardar_registro"), null );
        } catch (Exception e) {
            logger.error(e);
            super.addErrorMessage(PropertyManager.getInstance().getMessage("mensaje_error_generico"), null );
        }
    }

	public List<Catalogo> getCatalogoList() {
		return catalogoList;
	}

	public void setCatalogoList(List<Catalogo> catalogoList) {
		this.catalogoList = catalogoList;
	}

	public ComponenteBackingBean getComponenteBackingBean() {
		return componenteBackingBean;
	}

	public void setComponenteBackingBean(ComponenteBackingBean componenteBackingBean) {
		this.componenteBackingBean = componenteBackingBean;
	}

	public List<Estructura> getLista() {
		return lista;
	}


	public TipoCuadro getTipoCuadro() {
		return tipoCuadro;
	}


	public void setTipoCuadro(TipoCuadro tipoCuadro) {
		this.tipoCuadro = tipoCuadro;
	}


	public void setLista(List<Estructura> lista) {
		this.lista = lista;
	}
  
	public Catalogo getSelectedCuadro() {
		return selectedCuadro;
	}


	public void setSelectedCuadro(Catalogo selectedCuadro) {
		this.selectedCuadro = selectedCuadro;
	}


	public Catalogo getNuevoCuadro() {
		if (nuevoCuadro == null){
			nuevoCuadro = new Catalogo();
		}
		
		return nuevoCuadro;
	}


	public void setNuevoCuadro(Catalogo nuevoCuadro) {
		this.nuevoCuadro = nuevoCuadro;
	}


	public Long getIdCuadro() {
		if (idCuadro != null && idCuadro == 0){
			return null;
		}
		return idCuadro;
	}


	public void setIdCuadro(Long idCuadro) {
		this.idCuadro = idCuadro;
	}
	
	
	

    
}