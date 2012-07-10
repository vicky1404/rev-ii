package cl.mdr.ifrs.modules.mb;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.persistence.NoResultException;

import org.apache.log4j.Logger;
import org.primefaces.event.RowEditEvent;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.mb.ComponenteBackingBean;
import cl.mdr.ifrs.cross.mb.FiltroBackingBean;
import cl.mdr.ifrs.cross.util.GeneradorDisenoHelper;
import cl.mdr.ifrs.cross.util.PropertyManager;
import cl.mdr.ifrs.cross.vo.AgrupacionVO;
import cl.mdr.ifrs.ejb.entity.AgrupacionColumna;
import cl.mdr.ifrs.ejb.entity.Catalogo;
import cl.mdr.ifrs.ejb.entity.Columna;
import cl.mdr.ifrs.ejb.entity.Empresa;
import cl.mdr.ifrs.ejb.entity.Estructura;
import cl.mdr.ifrs.ejb.entity.Grilla;
import cl.mdr.ifrs.ejb.entity.TipoCuadro;
import cl.mdr.ifrs.ejb.entity.TipoEstructura;
import cl.mdr.ifrs.ejb.entity.Version;
import cl.mdr.ifrs.exceptions.FormulaException;
import cl.mdr.ifrs.vo.AgrupacionColumnaModelVO;
import cl.mdr.ifrs.vo.AgrupacionModelVO;

/**
 * @author Manuel Gutierrez C.
 * @since 29/06/2012
 * Maneja la pagina mantenedora de cuadros * 
 */
@ManagedBean(name="cuadroBackingBean")
@ViewScoped
public class CuadroBackingBean extends AbstractBackingBean implements Serializable {
	
public void alfo(){
	
}

    
}
