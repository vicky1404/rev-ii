package cl.mdr.ifrs.modules.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.RowEditEvent;



import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.mb.PropertyManager;
import cl.mdr.ifrs.ejb.entity.TipoCuadro;
import cl.mdr.ifrs.ejb.facade.local.FacadeServiceLocal;

/**
 * @author Manuel Gutierrez C.
 * @since 27/06/2012
 * Maneja la pagina mantenedora de tipos de cuadro * 
 */

@ManagedBean(name="tipoCuadro")
@ViewScoped
public class TipoCuadroBackingBean extends AbstractBackingBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 406193825104693528L;
	@EJB
	private FacadeServiceLocal servicio; 
	private List<TipoCuadro> lista;
	private TipoCuadro selectedTipoCuadro;
	private TipoCuadro nuevoTipoCuadro;
	
	
	public TipoCuadro getNuevoTipoCuadro() {
		
		if (nuevoTipoCuadro == null){
			nuevoTipoCuadro = new TipoCuadro();
		}
		
		return nuevoTipoCuadro;
	}

	public void setNuevoTipoCuadro(TipoCuadro nuevoTipoCuadro) {
		this.nuevoTipoCuadro = nuevoTipoCuadro;
	}

	private TipoCuadro filtro;
	
	

	public TipoCuadro getFiltro() {
		 if (filtro == null){
			 filtro = new TipoCuadro();
		 }
		
		return filtro;
	}

	public void setFiltro(TipoCuadro filtro) {
		this.filtro = filtro;
	}

	@PostConstruct
	public void init(){
		obtenerLista();
	}
	

	public void guardar(ActionEvent event){
		servicio.getMantenedoresTipoService().mergeEntity(getNuevoTipoCuadro());
		super.addInfoMessage(PropertyManager.getInstance().getMessage("mensaje_tabla_guardar_registro"), null );
		obtenerLista();
		setNuevoTipoCuadro(null);
	}
	
	public void editar(RowEditEvent event){
		servicio.getMantenedoresTipoService().mergeEntity((TipoCuadro) event.getObject());
		obtenerLista();
		setSelectedTipoCuadro(null);
	}
	
	public void eliminar(){
		
			
			try {
				servicio.getMantenedoresTipoService().deleteTipoCuadro(getSelectedTipoCuadro());
				super.addInfoMessage(PropertyManager.getInstance().getMessage("mensaje_tabla_eliminar_registro"), null );
			} catch (Exception e) {
				e.printStackTrace();
				super.addErrorMessage(PropertyManager.getInstance().getMessage("mensaje_error_generico"), null );
			}
			obtenerLista();
			setSelectedTipoCuadro(null);
			
		
	}
	
	public List<TipoCuadro> getLista() {
		if (lista == null){
			lista = new ArrayList<TipoCuadro>();
		}
		return lista;
	}

	public void setLista(List<TipoCuadro> lista) {
		this.lista = lista;
	}

	public TipoCuadro getSelectedTipoCuadro() {
		if (selectedTipoCuadro == null){
			selectedTipoCuadro = new TipoCuadro();
			
		}
		return selectedTipoCuadro;
	}

	public void setSelectedTipoCuadro(TipoCuadro selectedTipoCuadro) {
		this.selectedTipoCuadro = selectedTipoCuadro;
	}
	
	public void limpiar(AjaxBehaviorEvent event){
		setSelectedTipoCuadro(null);
	}
	
	public void obtenerLista(){
		setLista(servicio.getMantenedoresTipoService().findByFiltro(getFiltro()));
	}
}
