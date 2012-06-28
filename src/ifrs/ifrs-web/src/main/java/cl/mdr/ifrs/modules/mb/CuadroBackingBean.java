package cl.mdr.ifrs.modules.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.ejb.entity.Estructura;
import cl.mdr.ifrs.ejb.entity.Version;
import cl.mdr.ifrs.exceptions.FormulaException;

public class CuadroBackingBean extends AbstractBackingBean implements Serializable {
	private static final long serialVersionUID = 6443968726750374661L;
	
	private List<Estructura> estructuraList = new ArrayList<Estructura>();
    private Version versionSelected;
   
    private String nombreArchivoExport;
    private boolean renderedPeriodoList = false;
    private boolean renderedInformacion = false;
	
    /*public String buscarCuadro(){
        try{
            setEstructuraList(null);
            
            setVersionPeriodoSelected((VersionPeriodo)getPeriodoCatalogoTable().getRowData());
            setRenderedInformacion(Boolean.TRUE);
            getFiltro().setVersion(this.getVersionPeriodoSelected().getVersion());
            List<Estructura> estructuraList = super.getFacade().getEstructuraService().getEstructuraByVersion(super.getFiltro().getVersion(), true);
            
            setListGrilla(estructuraList);
            
            setEstructuraList(estructuraList);
            
            if(getEstructuraList().isEmpty()){
                agregarWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_version_sin_registros"), 
                                                        getVersionPeriodoSelected().getVersion().getCatalogo().getNombre(),
                                                        getVersionPeriodoSelected().getPeriodo().getAnioPeriodo(), 
                                                        getVersionPeriodoSelected().getPeriodo().getMesPeriodo() , 
                                                        getVersionPeriodoSelected().getVersion().getVersion()));
            }
        } catch (FormulaException e){
            logger.error(e.getCause(), e);
            agregarErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_cuadro_formula_loop_error"));  
            agregarErrorMessage(e.getFormula());  
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            agregarErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_nota_get_catalogo_error"));  
        }
        return null;
    }*/

	public List<Estructura> getEstructuraList() {
		return estructuraList;
	}

	public void setEstructuraList(List<Estructura> estructuraList) {
		this.estructuraList = estructuraList;
	}
}
