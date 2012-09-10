package cl.mdr.ifrs.modules.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.persistence.RollbackException;

import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.RowEditEvent;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.mb.PropertyManager;
import cl.mdr.ifrs.ejb.entity.TipoCuadro;


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
	private static Logger logger = Logger.getLogger(TipoCuadroBackingBean.class);
	private static final long serialVersionUID = 406193825104693528L;
	private List<TipoCuadro> lista;
	private TipoCuadro selectedTipoCuadro;
	private TipoCuadro nuevoTipoCuadro;
	private DataTable tabla;
	
	
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
		try
		{
			getFacadeService().getMantenedoresTipoService().mergeEntity(getNuevoTipoCuadro());
			super.addInfoMessage(PropertyManager.getInstance().getMessage("mensaje_tabla_guardar_registro"), null );
			obtenerLista();
			setNuevoTipoCuadro(null);

		} catch (Exception ex){
            logger.error(ex);
            super.addErrorMessage(PropertyManager.getInstance().getMessage("mensaje_error_generico"), null );
		}
	}
	
	public void editar(RowEditEvent event){
		getFacadeService().getMantenedoresTipoService().mergeEntity((TipoCuadro) event.getObject());
		obtenerLista();
		setSelectedTipoCuadro(null);
	}
	
	public void eliminar(){
		
			
			try {
				if (getSelectedTipoCuadro() != null && getSelectedTipoCuadro().getIdTipoCuadro() != null){
					getFacadeService().getMantenedoresTipoService().deleteTipoCuadro(getSelectedTipoCuadro());
				}else{
					super.addWarnMessage(PropertyManager.getInstance().getMessage("mensaje_tabla_seleccionar_registro"), null );
					return;
				}
				
					super.addInfoMessage(PropertyManager.getInstance().getMessage("mensaje_tabla_eliminar_registro"), null );
			 				
			} catch (Exception e) {
				
				super.addWarnMessage(PropertyManager.getInstance().getMessage("mensaje_error_tipo_utilizado_no_borrar"), null );
				//super.addErrorMessage(PropertyManager.getInstance().getMessage("mensaje_error_generico"), null );
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
		setLista(getFacadeService().getMantenedoresTipoService().findByFiltro(getFiltro()));
	}

	public DataTable getTabla() {
		return tabla;
	}

	public void setTabla(DataTable tabla) {
		this.tabla = tabla;
	}
}
