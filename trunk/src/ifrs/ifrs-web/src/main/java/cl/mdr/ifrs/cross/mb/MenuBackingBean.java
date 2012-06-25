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
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

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
	
	private TreeNode root;
	
	
	
	@SuppressWarnings("unused")
	public MenuBackingBean() {
		super();
		root = new DefaultTreeNode("Proceso", null);  
		TreeNode node0 = new DefaultTreeNode("Notas", root);  
        TreeNode node1 = new DefaultTreeNode("Cuadros Tecnicos", root);   
  
        TreeNode node00 = new DefaultTreeNode("Nota 1", node0);  
        TreeNode node01 = new DefaultTreeNode("Nota 2", node0);  
  
        TreeNode node10 = new DefaultTreeNode("Cuadro 6.1", node1);  
        TreeNode node11 = new DefaultTreeNode("Cuadro 6.2", node1);  

	}
	
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

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	 

}
