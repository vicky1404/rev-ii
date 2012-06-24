/**
 * 
 */
package cl.mdr.ifrs.cross.mb;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;

import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.event.TabChangeEvent;

import cl.mdr.ifrs.ejb.cross.service.local.TestServiceLocal;

/**
 * @author rreyes
 * @link http://cl.linkedin.com/in/rreyesc
 *
 */
@ManagedBean
@SessionScoped
public class MenuBackingBean extends AbstractBackingBean implements Serializable {
	private Logger log = Logger.getLogger(this.getClass().getName());
	private static final long serialVersionUID = 8077481642975216680L;
	
	@EJB
	private TestServiceLocal testServiceLocal;
	
	private UIComponent tabActivo;
	private AccordionPanel menuAcordionPanel;
	private String activeTabIndex;
	
	
	public void onTabChange(TabChangeEvent event) {  
		this.setTabActivo(event.getTab());   
		this.setActiveTabIndex("1");
		log.info(""+testServiceLocal.findAll());
    }
	
	
	public UIComponent getTabActivo() {
		return tabActivo;
	}

	public void setTabActivo(UIComponent tabActivo) {
		this.tabActivo = tabActivo;
	}


	public AccordionPanel getMenuAcordionPanel() {
		return menuAcordionPanel;
	}


	public void setMenuAcordionPanel(AccordionPanel menuAcordionPanel) {
		this.menuAcordionPanel = menuAcordionPanel;
	}


	public String getActiveTabIndex() {
		return activeTabIndex;
	}


	public void setActiveTabIndex(String activeTabIndex) {
		this.activeTabIndex = activeTabIndex;
	}

	 

}
