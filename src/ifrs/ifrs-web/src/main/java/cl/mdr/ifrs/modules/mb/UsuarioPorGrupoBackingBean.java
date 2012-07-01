package cl.mdr.ifrs.modules.mb;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;
import org.primefaces.model.DualListModel;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.ejb.entity.Grupo;
import cl.mdr.ifrs.ejb.entity.Usuario;

@ManagedBean
@ViewScoped
public class UsuarioPorGrupoBackingBean extends AbstractBackingBean implements Serializable {
	private transient Logger logger = Logger.getLogger(this.getClass().getName());
	private static final long serialVersionUID = 2860517343010041015L;
	
	private String idGrupoSelected;
	private DualListModel<Usuario> dualListModelUsuarios;
	private List<Usuario> source;
	private List<Usuario> target;
	private boolean renderDualList;
	
	public UsuarioPorGrupoBackingBean() {
		super();		
	}
	
	public void buscarUsuarioPorGrupoAction(){
		try {
			this.setSource(super.getFacadeService().getSeguridadService().findUsuarioByGrupoNotIn( new Grupo(this.getIdGrupoSelected()) ));
			this.setTarget(super.getFacadeService().getSeguridadService().findUsuarioByGrupoIn( new Grupo(this.getIdGrupoSelected()) ));
			this.setDualListModelUsuarios(new DualListModel<Usuario>(this.getSource(), this.getTarget()));	
			this.setRenderDualList(Boolean.TRUE);
		} catch (Exception e) {
			super.addErrorMessage("", "Error al obtener información de Usuarios");
			logger.error(e.getMessage(), e);
		}
	}	
	
	public DualListModel<Usuario> getDualListModelUsuarios() {
		return dualListModelUsuarios;
	}
	public void setDualListModelUsuarios(DualListModel<Usuario> dualListModelUsuarios) {
		this.dualListModelUsuarios = dualListModelUsuarios;
	}
	public List<Usuario> getSource() {
		return source;
	}
	public void setSource(List<Usuario> source) {
		this.source = source;
	}
	public List<Usuario> getTarget() {
		return target;
	}
	public void setTarget(List<Usuario> target) {
		this.target = target;
	}
	
	public boolean isRenderDualList() {
		return renderDualList;
	}

	public void setRenderDualList(boolean renderDualList) {
		this.renderDualList = renderDualList;
	}

	public String getIdGrupoSelected() {
		return idGrupoSelected;
	}

	public void setIdGrupoSelected(String idGrupoSelected) {
		this.idGrupoSelected = idGrupoSelected;
	}
	
	

}
