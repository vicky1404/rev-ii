package cl.mdr.ifrs.modules.perfilamiento.mb;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.index;
import static ch.lambdaj.Lambda.on;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;
import org.primefaces.model.DualListModel;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.ejb.entity.Empresa;
import cl.mdr.ifrs.ejb.entity.Grupo;
import cl.mdr.ifrs.ejb.entity.GrupoEmpresa;

/**
 * @author rreyes
 * @link http://cl.linkedin.com/in/rreyesc
 * @since 10/07/2012
 */
@ManagedBean
@ViewScoped
public class GrupoPorEmpresaBackingBean extends AbstractBackingBean implements Serializable {
	private transient Logger logger = Logger.getLogger(this.getClass().getName());
	private static final long serialVersionUID = -2450554811007183074L;
	
	private Empresa empresa;
	private DualListModel<String> dualListModelGrupos;
	private List<String> source;
	private List<String> target;
	private List<Grupo> grupoListSource;
	private List<Grupo> grupoListTarget;
	private boolean renderDualList;
	private Map<String, Grupo> grupoMap;
	
	@PostConstruct
	void init(){
		empresa = new Empresa();
	}
	
	public void buscarAction(){	
		try {
			this.setGrupoListSource(super.getFacadeService().getSeguridadService().findGrupoByEmpresaNotIn(this.getEmpresa()));
			this.setGrupoListTarget(super.getFacadeService().getSeguridadService().findGrupoByEmpresaIn(this.getEmpresa()));
			this.setSource( extract( this.getGrupoListSource(), on(Grupo.class).getIdGrupoAcceso() ) );
			this.setTarget( extract( this.getGrupoListTarget(), on(Grupo.class).getIdGrupoAcceso() ) );
			this.setDualListModelGrupos(new DualListModel<String>(this.getSource(), this.getTarget()));	
			final Map<String, Grupo> grupoMap = index(  super.getFacadeService().getSeguridadService().findGruposAll(), on(Grupo.class).getIdGrupoAcceso());			
			this.setGrupoMap(grupoMap);	
			this.setRenderDualList(Boolean.TRUE);
		} catch (Exception e) {
			super.addErrorMessage("", "Error al obtener información de Grupos");
			logger.error(e.getMessage(), e);
		}				
	}
	
	public void guardarAction(){
		try {
			List<GrupoEmpresa> grupoEmpresaList = new ArrayList<GrupoEmpresa>();
			GrupoEmpresa grupoEmpresa = null;
			Empresa empresa = super.getFacadeService().getEmpresaService().findById(this.getEmpresa().getIdRut());
			for(String grupoId : this.getDualListModelGrupos().getTarget()){	
				grupoEmpresa = new GrupoEmpresa();
				grupoEmpresa.setGrupo(this.getGrupoMap().get(grupoId));
				grupoEmpresa.setEmpresa(empresa);
				grupoEmpresaList.add(grupoEmpresa);
			}			
			super.getFacadeService().getSeguridadService().persistGrupoEmpresa(grupoEmpresaList, empresa);
			super.addInfoMessage("", MessageFormat.format("Se actualizó correctamente el grupo para la Empresa {0} ", empresa.getNombre() )  );
		} catch (Exception e) {
			logger.error(e.getCause(), e);
			super.addErrorMessage("Error", "Error al actualizar los Grupos por Empresa");		
		}
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public DualListModel<String> getDualListModelGrupos() {
		return dualListModelGrupos;
	}

	public void setDualListModelGrupos(DualListModel<String> dualListModelGrupos) {
		this.dualListModelGrupos = dualListModelGrupos;
	}

	public boolean isRenderDualList() {
		return renderDualList;
	}

	public void setRenderDualList(boolean renderDualList) {
		this.renderDualList = renderDualList;
	}

	public List<String> getSource() {
		return source;
	}

	public void setSource(List<String> source) {
		this.source = source;
	}

	public List<String> getTarget() {
		return target;
	}

	public void setTarget(List<String> target) {
		this.target = target;
	}

	public List<Grupo> getGrupoListSource() {
		return grupoListSource;
	}

	public void setGrupoListSource(List<Grupo> grupoListSource) {
		this.grupoListSource = grupoListSource;
	}

	public List<Grupo> getGrupoListTarget() {
		return grupoListTarget;
	}

	public void setGrupoListTarget(List<Grupo> grupoListTarget) {
		this.grupoListTarget = grupoListTarget;
	}

	public Map<String, Grupo> getGrupoMap() {
		return grupoMap;
	}

	public void setGrupoMap(Map<String, Grupo> grupoMap) {
		this.grupoMap = grupoMap;
	}
	
}
