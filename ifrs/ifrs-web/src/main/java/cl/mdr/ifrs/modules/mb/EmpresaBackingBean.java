package cl.mdr.ifrs.modules.mb;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;

import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.RowEditEvent;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.mb.PropertyManager;
import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.Empresa;



/**
 * @author Manuel Gutierrez C.
 * @since 27/09/2012
 * Maneja la pagina mantenedora de empresas * 
 */

@ManagedBean(name="empresaBackingBean")
@ViewScoped
public class EmpresaBackingBean extends AbstractBackingBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1997480258179950000L;	
	private static Logger logger = Logger.getLogger(EmpresaBackingBean.class);
	private List<Empresa> lista;
	private Empresa selectedTipoCuadro;
	private Empresa nuevoTipoCuadro;
	private DataTable tabla;
	private Empresa filtro;
	
	
	public Empresa getNuevoTipoCuadro() {
		
		if (nuevoTipoCuadro == null){
			nuevoTipoCuadro = new Empresa();
		}
		return nuevoTipoCuadro;
	}
	
	public void setNuevoTipoCuadro(Empresa nuevoTipoCuadro) {
		this.nuevoTipoCuadro = nuevoTipoCuadro;
	}

	
	
	

	public Empresa getFiltro() {
		 if (filtro == null){
			 filtro = new Empresa();
		 }
		
		return filtro;
	}

	public void setFiltro(Empresa filtro) {
		this.filtro = filtro;
	}

	@PostConstruct
	public void init(){
		obtenerLista();
	}
	

	public void guardar(ActionEvent event){
		try
		{
			if (this.validarRutEmpresa(getNuevoTipoCuadro())){
				
				getFacadeService().getEmpresaService().mergeEmpresa(getNuevoTipoCuadro());
				super.addInfoMessage(PropertyManager.getInstance().getMessage("mensaje_tabla_guardar_registro"), null );
				this.obtenerLista();
				setNuevoTipoCuadro(null);

			} else {
				addWarnMessage(PropertyManager.getInstance().getMessage("general_error_registro_no_guardado_rut_no_valido"));
			}
			

		} catch (Exception ex){
            logger.error(ex);
            super.addErrorMessage(PropertyManager.getInstance().getMessage("mensaje_error_generico"), null );
		}
	}
	
	public void editar(RowEditEvent event){
		
		Empresa empresa = (Empresa) event.getObject();
		
		try {
			
			if (this.validarRutEmpresa(empresa))
				getFacadeService().getEmpresaService().mergeEmpresa(empresa);
			else 
				addWarnMessage(PropertyManager.getInstance().getMessage("general_error_registro_no_guardado_rut_no_valido"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		obtenerLista();
		setSelectedTipoCuadro(null);
	}
	
	public void eliminar(){
		
			
			try {
				if (getSelectedTipoCuadro() != null && getSelectedTipoCuadro().getIdRut() != null){
					getFacadeService().getEmpresaService().deleteEmpresa(getSelectedTipoCuadro());
				}else{
					super.addWarnMessage(PropertyManager.getInstance().getMessage("mensaje_tabla_seleccionar_registro"), null );
					return;
				}
				
					super.addInfoMessage(PropertyManager.getInstance().getMessage("mensaje_tabla_eliminar_registro"), null );
			 				
			} catch (Exception e) {
				super.addWarnMessage(PropertyManager.getInstance().getMessage("mensaje_error_tipo_utilizado_no_borrar"), null );
			}
			obtenerLista();
			setSelectedTipoCuadro(null);
			
		
	}
	
	public List<Empresa> getLista() {
		if (lista == null){
			lista = new ArrayList<Empresa>();
		}
		return lista;
	}

	public void setLista(List<Empresa> lista) {
		this.lista = lista;
	}

	public Empresa getSelectedTipoCuadro() {
		if (selectedTipoCuadro == null){
			selectedTipoCuadro = new Empresa();
			
		}
		return selectedTipoCuadro;
	}

	public void setSelectedTipoCuadro(Empresa selectedTipoCuadro) {
		this.selectedTipoCuadro = selectedTipoCuadro;
	}
	
	public void limpiar(){
		this.getFiltro().setIdRut(null);
		this.getFiltro().setNombre(null);
		this.getFiltro().setRazonSocial(null);
		this.obtenerLista();
	}
	
	public void obtenerLista(){
		try {
			
			setLista(getFacadeService().getEmpresaService().findEmpresaByFiltro(getFiltro()));
			
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public DataTable getTabla() {
		return tabla;
	}

	public void setTabla(DataTable tabla) {
		this.tabla = tabla;
	}
	
	private boolean validarRutEmpresa(Empresa empresa){
		return Util.validarRut( empresa.getIdRut().intValue() , empresa.getDv().charAt(0));
	}

}
