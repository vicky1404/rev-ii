package cl.mdr.ifrs.modules.mantenedores.mb;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.event.RowEditEvent;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.ejb.entity.AreaNegocio;
import cl.mdr.ifrs.ejb.entity.Grupo;
import cl.mdr.ifrs.exceptions.RegistroNoEditableException;

@ManagedBean
@ViewScoped
public class GrupoBackingBean extends AbstractBackingBean implements Serializable {
	private transient Logger logger = Logger.getLogger(AreaNegocioBackingBean.class);
	private static final long serialVersionUID = 5161357683025440954L;
	
	private String idAreaNegocio;
	private AreaNegocio areaNegocio;
	private Grupo nuevoGrupo;
	private List<Grupo> grupoList;
	private List<Grupo> grupoByEmpresaList;
	private boolean renderGrupos;	
	
	@PostConstruct
	void init(){
		this.inicializarNuevoGrupo();
	}
	
	private void inicializarNuevoGrupo(){
		nuevoGrupo = new Grupo();
		nuevoGrupo.setAreaNegocio(new AreaNegocio());
	}
	
	/**
	 * busca segun los criterios de busqueda ingresados
	 * @return
	 */
	public String buscarAction(){		
		try {
			this.buildGrupoList();
			this.setRenderGrupos(Boolean.TRUE);
		} catch (Exception e) {
			super.addErrorMessage("Se ha producido un error al buscar los Grupos");
			logger.error(e);
		}
		return null;
	}
	
	/**
	 * edita un elemento seleccionado de la grilla.
	 * @param event
	 */
	public void editarAction(RowEditEvent event){
		try {
			super.getFacadeService().getGrupoService().editarGrupo((Grupo) event.getObject());
			super.addInfoMessage("Se ha modificado el Grupo correctamente");
			this.buildGrupoList();
		} catch (RegistroNoEditableException e) {
			super.addErrorMessage(e.getMessage());
			logger.error(e);
		} catch (Exception e) {
			super.addErrorMessage("Se ha producido un error al editar el Grupo");
			logger.error(e);
		}
	}
	
	/**
	 * edita todos los elementos de la grilla
	 * @param event
	 */
	public void editarAllAction(ActionEvent event){
		try {
			super.getFacadeService().getGrupoService().editarGrupoList(this.getGrupoList());
			super.addInfoMessage("Se ha modificado los Grupos correctamente");
			this.buildGrupoList();
		} catch (RegistroNoEditableException e) {
			super.addErrorMessage(e.getMessage());
			logger.error(e);
		} catch (Exception e) {			
			super.addErrorMessage("Se ha producido un error al editar los Grupos");
			logger.error(e);
		}
	}
	
	/**
	 * @param event
	 */
	public void agregarAction(ActionEvent event){
		Grupo grupo = this.getNuevoGrupo();
		try {
			grupo.setAccesoBloqueado(0L);
			super.getFacadeService().getGrupoService().persistGrupo(grupo);
			super.addInfoMessage("Se ha creado el Grupo correctamente");
			this.buildGrupoList();
			this.inicializarNuevoGrupo();
		} catch (EJBException e) {
			super.addErrorMessage("El Grupo que intenta crear ya existe");
			this.getNuevoGrupo().setIdGrupoAcceso(null);
			logger.error(e);
		} catch (Exception e) {
			super.addErrorMessage("Se ha producido un error al agregar el Grupo");
			logger.error(e);
		}
	}
	
	/**
	 * construlle el listado de grupos segun criterios para ser
	 * desplegados en la grilla.
	 * @throws Exception
	 */
	private void buildGrupoList() throws Exception{		
		if(idAreaNegocio == StringUtils.EMPTY){
			areaNegocio = null;
		}else{
			areaNegocio = new AreaNegocio(idAreaNegocio);
		}
		this.setGrupoList(super.getFacadeService().getGrupoService().findGruposByFiltro(areaNegocio, this.getFiltroBackingBean().getEmpresa()));					
	}
		
	public AreaNegocio getAreaNegocio() {
		return areaNegocio;
	}

	public void setAreaNegocio(AreaNegocio areaNegocio) {
		this.areaNegocio = areaNegocio;
	}

	public List<Grupo> getGrupoList() {
		return grupoList;
	}

	public void setGrupoList(List<Grupo> grupoList) {
		this.grupoList = grupoList;
	}

	public boolean isRenderGrupos() {
		return renderGrupos;
	}

	public void setRenderGrupos(boolean renderGrupos) {
		this.renderGrupos = renderGrupos;
	}

	public String getIdAreaNegocio() {
		return idAreaNegocio;
	}

	public void setIdAreaNegocio(String idAreaNegocio) {
		this.idAreaNegocio = idAreaNegocio;
	}

	

	public Grupo getNuevoGrupo() {
		return nuevoGrupo;
	}

	public void setNuevoGrupo(Grupo nuevoGrupo) {
		this.nuevoGrupo = nuevoGrupo;
	}

	/**
	 * @return the grupoByEmpresaList
	 */
	public List<Grupo> getGrupoByEmpresaList() {
		if(grupoByEmpresaList == null){
			try {
				grupoByEmpresaList = super.getFacadeService().getGrupoService().findGruposByFiltro(null, this.getFiltroBackingBean().getEmpresa());
			} catch (Exception e) {
				super.addErrorMessage("Ocurrio un error al obtener el listado de Grupos de la empresa");
				logger.error(e);
			}
		}
		return grupoByEmpresaList;
	}

	/**
	 * @param grupoByEmpresaList the grupoByEmpresaList to set
	 */
	public void setGrupoByEmpresaList(List<Grupo> grupoByEmpresaList) {
		this.grupoByEmpresaList = grupoByEmpresaList;
	}

}
