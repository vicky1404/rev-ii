package cl.mdr.ifrs.modules.mantenedores.mb;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.event.RowEditEvent;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.ejb.common.VigenciaEnum;
import cl.mdr.ifrs.ejb.entity.AreaNegocio;
import cl.mdr.ifrs.ejb.entity.Grupo;

@ManagedBean
@ViewScoped
public class GrupoBackingBean extends AbstractBackingBean implements Serializable {
	private transient Logger logger = Logger.getLogger(AreaNegocioBackingBean.class);
	private static final long serialVersionUID = 5161357683025440954L;
	
	private String idAreaNegocio;
	private AreaNegocio areaNegocio;
	private Grupo nuevoGrupo;
	private List<Grupo> grupoList;
	private boolean renderGrupos;
	private List<AreaNegocio> areaNegocioByEmpresaList;
	
	@PostConstruct
	void init(){
		nuevoGrupo = new Grupo();
	}
	
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
	
	public void editarAction(RowEditEvent event){
		
	}
	
	public void editarAllAction(ActionEvent event){
		
	}
	
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

	public List<AreaNegocio> getAreaNegocioByEmpresaList() throws Exception {
		if(areaNegocioByEmpresaList == null){
			areaNegocioByEmpresaList = this.getFacadeService().getAreaNegocioService().findAllByEmpresa(this.getFiltroBackingBean().getEmpresa(), VigenciaEnum.VIGENTE.getKey());
    	}
		return areaNegocioByEmpresaList;
	}

	public void setAreaNegocioByEmpresaList(List<AreaNegocio> areaNegocioByEmpresaList) {
		this.areaNegocioByEmpresaList = areaNegocioByEmpresaList;
	}

	public Grupo getNuevoGrupo() {
		return nuevoGrupo;
	}

	public void setNuevoGrupo(Grupo nuevoGrupo) {
		this.nuevoGrupo = nuevoGrupo;
	}

}
