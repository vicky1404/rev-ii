/**
 * 
 */
package cl.mdr.ifrs.modules.mb;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;

/**
 * @author rreyes
 * @link http://cl.linkedin.com/in/rreyesc
 *
 */
@ManagedBean
@ViewScoped
public class PerfilamientoBackingBean extends AbstractBackingBean implements Serializable {
	private final Logger log = Logger.getLogger(this.getClass().getName());
	private static final long serialVersionUID = -4236186956272081968L;

}
