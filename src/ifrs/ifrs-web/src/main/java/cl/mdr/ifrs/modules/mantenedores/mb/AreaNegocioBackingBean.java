package cl.mdr.ifrs.modules.mantenedores.mb;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;
import org.primefaces.event.RowEditEvent;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.ejb.entity.AreaNegocio;
import cl.mdr.ifrs.exceptions.RegistroNoEditableException;

@ManagedBean
@ViewScoped
public class AreaNegocioBackingBean extends AbstractBackingBean implements Serializable {
	private transient Logger logger = Logger.getLogger(AreaNegocioBackingBean.class);
	private static final long serialVersionUID = 7795748522251877332L;
	private static final Long TODOS = 100L;
	
	private Long vigente;	
	private List<AreaNegocio> areaNegocioList;
	private AreaNegocio areaNegocio;
	private AreaNegocio nuevaAreaNegocio;
	private boolean renderAreaNegocio;
	
	@PostConstruct
	void init(){
		nuevaAreaNegocio = new AreaNegocio();
	}
	
	public String buscarAction(){
		try {			
			this.buildAreaNegocioList();
			this.setRenderAreaNegocio(Boolean.TRUE);
		} catch (Exception e) {
			this.setRenderAreaNegocio(Boolean.FALSE);
			super.addErrorMessage("Se ha producido un error al buscar las Áreas de Negocio.");
			logger.error(e);
		}
		return null;
	}
	
	public void editarAction(RowEditEvent event){
		try {
			super.getFacadeService().getAreaNegocioService().editarAreaNegocio((AreaNegocio) event.getObject());
			super.addInfoMessage("Se ha modificado el Área de Negocio correctamente");
			this.buildAreaNegocioList();
		} catch (RegistroNoEditableException e) {
			super.addErrorMessage(e.getMessage());
			logger.error(e);		
		} catch (Exception e) {
			super.addErrorMessage("Se ha producido un error al editar el Área de Negocio.");
			logger.error(e);
		}		
	}
	
	
	public void editarAllAction(ActionEvent event){
		try {
			super.getFacadeService().getAreaNegocioService().editarAreaNegocioList(this.getAreaNegocioList());
			super.addInfoMessage("Se han modificado las Áreas de Negocio correctamente");
		} catch (RegistroNoEditableException e) {
			super.addErrorMessage(e.getMessage());
			logger.error(e);				
		} catch (Exception e) {
			super.addErrorMessage("Se ha producido un error al editar las Áreas de Negocio.");
			logger.error(e);
		}		
	}
	
	private void buildAreaNegocioList(){
		try{
			if(this.getVigente() == TODOS)
				vigente = null;
			this.setAreaNegocioList(super.getFacadeService().getAreaNegocioService().findAllByEmpresa(super.getFiltroBackingBean().getEmpresa(), this.getVigente()));
		} catch (Exception e) {
			this.setRenderAreaNegocio(Boolean.FALSE);
			super.addErrorMessage("Se ha producido un error al buscar las Areas de Negocio.");
			logger.error(e);
		}		
	}
	
	
	public void agregarAction(ActionEvent event){
		try {
			AreaNegocio nuevaAreaNegocio = this.getNuevaAreaNegocio();
			nuevaAreaNegocio.setIdAreaNegocio(this.getNuevaAreaNegocio().getIdAreaNegocio().toUpperCase());
			nuevaAreaNegocio.setEmpresa(this.getFiltroBackingBean().getEmpresa());
			super.getFacadeService().getAreaNegocioService().persistAreaNegocio(nuevaAreaNegocio);
			super.addInfoMessage("Se ha creado el Área de Negocio correctamente");
			this.buildAreaNegocioList();
			this.setNuevaAreaNegocio(new AreaNegocio());
		}catch (EJBException e) {
			super.addErrorMessage("El Área de Negocio que intenta crear ya existe.");
			this.getNuevaAreaNegocio().setIdAreaNegocio(null);
			logger.error(e);
		}catch (Exception e) {
			super.addErrorMessage("Se ha producido un error al editar el Área de Negocio.");
			logger.error(e);
		}		
	}

	public Long getVigente() {
		return vigente;
	}

	public void setVigente(Long vigente) {
		this.vigente = vigente;
	}

	public List<AreaNegocio> getAreaNegocioList() {
		return areaNegocioList;
	}

	public void setAreaNegocioList(List<AreaNegocio> areaNegocioList) {
		this.areaNegocioList = areaNegocioList;
	}

	public boolean isRenderAreaNegocio() {
		return renderAreaNegocio;
	}

	public void setRenderAreaNegocio(boolean renderAreaNegocio) {
		this.renderAreaNegocio = renderAreaNegocio;
	}

	public AreaNegocio getAreaNegocio() {
		return areaNegocio;
	}

	public void setAreaNegocio(AreaNegocio areaNegocio) {
		this.areaNegocio = areaNegocio;
	}

	public AreaNegocio getNuevaAreaNegocio() {
		return nuevaAreaNegocio;
	}

	public void setNuevaAreaNegocio(AreaNegocio nuevaAreaNegocio) {
		this.nuevaAreaNegocio = nuevaAreaNegocio;
	}

}