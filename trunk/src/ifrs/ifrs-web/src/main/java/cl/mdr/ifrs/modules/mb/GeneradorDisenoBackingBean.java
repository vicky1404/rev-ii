package cl.mdr.ifrs.modules.mb;

import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.vo.GrillaModelVO;

@ManagedBean
@ViewScoped
public class GeneradorDisenoBackingBean extends AbstractBackingBean {
	
    /*contiene el diseño de la grilla*/
    private Map<Long, GrillaModelVO> grillaModelMap;

	public Map<Long, GrillaModelVO> getGrillaModelMap() {
		return grillaModelMap;
	}

	public void setGrillaModelMap(Map<Long, GrillaModelVO> grillaModelMap) {
		this.grillaModelMap = grillaModelMap;
	}

}
