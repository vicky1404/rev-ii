package cl.mdr.ifrs.modules.mb;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.ejb.entity.Grupo;

@ManagedBean
@ViewScoped
public class MenuPorGrupoBackingBean extends AbstractBackingBean implements Serializable {
	private static final long serialVersionUID = -6662762316008127812L;
	
	private Grupo grupoSelected;
		
	public MenuPorGrupoBackingBean() {
		super();		
	}

	public Grupo getGrupoSelected() {
		return grupoSelected;
	}

	public void setGrupoSelected(Grupo grupoSelected) {
		this.grupoSelected = grupoSelected;
	}
	
	

}
