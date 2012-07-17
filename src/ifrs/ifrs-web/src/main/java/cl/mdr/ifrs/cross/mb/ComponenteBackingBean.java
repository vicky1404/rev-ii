package cl.mdr.ifrs.cross.mb;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import cl.mdr.ifrs.cross.util.UtilBean;
import cl.mdr.ifrs.cross.vo.TipoFormulaVO;
import cl.mdr.ifrs.ejb.common.TipoImpresionEnum;
import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.Catalogo;
import cl.mdr.ifrs.ejb.entity.Empresa;
import cl.mdr.ifrs.ejb.entity.EstadoCuadro;
import cl.mdr.ifrs.ejb.entity.Grilla;
import cl.mdr.ifrs.ejb.entity.Grupo;
import cl.mdr.ifrs.ejb.entity.Periodo;
import cl.mdr.ifrs.ejb.entity.Rol;
import cl.mdr.ifrs.ejb.entity.TipoCelda;
import cl.mdr.ifrs.ejb.entity.TipoCuadro;
import cl.mdr.ifrs.ejb.entity.TipoDato;
import cl.mdr.ifrs.ejb.entity.TipoEstructura;
import cl.mdr.ifrs.ejb.facade.local.FacadeServiceLocal;


@ManagedBean(name="componenteBackingBean")
@SessionScoped
public class ComponenteBackingBean implements Serializable {
	private static final long serialVersionUID = -4793929287251572773L;

	public static final String BEAN_NAME = "componenteBackingBean";
    
    private transient Logger logger = Logger.getLogger(ComponenteBackingBean.class);

    @EJB
    private FacadeServiceLocal facade;
    
    /*variables generales*/
    private Periodo periodoActual;
    
    
    /*Listas para llenar combos*/
    private List<Periodo> periodoList;
    private List<TipoEstructura> tipoEstructuraList;
    private List<TipoCelda> tipoCeldaList;
    private List<TipoCuadro> tipoCuadroList;
    private List<EstadoCuadro> estadoCuadroList;
    private List<Grupo> grupoList;
    private List<Empresa> empresaList;
    private List<TipoDato> tipoDatoList;
    private List<Catalogo> catalogoList;
    private List<Rol> rolList;
    
    
    public ComponenteBackingBean() {
    }
    
    /*Metodos utiles para aplicacion*/
    
    
    /**Metodo retorna el periodo actual vigente de la aplicacion
     * @return
     */
    public Periodo getPeriodoActual() {
        if(periodoActual==null)
            periodoActual = getFacade().getPeriodoService().findMaxPeriodoObj();
        return periodoActual;
    }
    
    /*Select items de la aplicacion*/
    
    /**
      * Construye un SelectItem con los meses del Año presentes en la tabla periodo
      * @return
      */    
    public List<SelectItem> getMeses(){
        
        List<SelectItem> meses = new ArrayList<SelectItem>();
        Map<String, String> mapMeses = new HashMap<String,String>();
        
        for(Periodo periodo : getPeriodoList()){
            if(!mapMeses.containsKey(periodo.getMesPeriodo())){
                mapMeses.put(periodo.getMesPeriodo(), periodo.getMesPeriodo());
                meses.add(new SelectItem(periodo.getMesPeriodo(), periodo.getMesPeriodo()));
            }
        }
        UtilBean.sortSelectItemMeses(meses);
        return meses;
    }
        
    public List<SelectItem> getPeriodFormat(){
        List<SelectItem> periods = new ArrayList<SelectItem>();
        for(Periodo periodo : getPeriodoList()){
            periods.add(new SelectItem(periodo.getIdPeriodo(), periodo.getPeriodoFormat()));
        }
        return periods;
    }   

    /**
      * Construye un SelectItem con los meses del Año presentes en la tabla periodo
      * @return
      */    
    public List<SelectItem> getAnios(){

        List<SelectItem> anios = new ArrayList<SelectItem>();
        Map<String, String> mapAnios = new HashMap<String,String>();

        for(Periodo periodo : getPeriodoList()){
            if(!mapAnios.containsKey(periodo.getAnioPeriodo())){
                mapAnios.put(periodo.getAnioPeriodo(), periodo.getAnioPeriodo());
                anios.add(new SelectItem(periodo.getAnioPeriodo(), periodo.getAnioPeriodo()));
            }
        }
        UtilBean.sortSelectItemAnios(anios);
        return anios;
    }

    public List<SelectItem> getTipoCeldas(){
        
        List<SelectItem> celdas = new ArrayList<SelectItem>();
        
        for(TipoCelda celda : getTipoCeldaList()){
            celdas.add(new SelectItem(celda, celda.getNombre()));
        }
        
        return celdas;
    }

    public List<SelectItem> getTipoDatos(){
            
            List<SelectItem> tipoDatos = new ArrayList<SelectItem>();
            
            for(TipoDato tipoDato : getTipoDatoList()){
                tipoDatos.add(new SelectItem(tipoDato, tipoDato.getNombre()));
            }
            
            return tipoDatos;
    }

    public List<SelectItem> getTipoEstructuras(){
        
        List<SelectItem> tipoEstructuras = new ArrayList<SelectItem>();
        
        for(TipoEstructura tipoEstructura : getTipoEstructuraList()){
            tipoEstructuras.add(new SelectItem(tipoEstructura, tipoEstructura.getNombre()));
        }
        
        return tipoEstructuras;
    }

    public List<SelectItem> getVigencias(){        
            List<SelectItem> vigencias = new ArrayList<SelectItem>();
            vigencias.add(new SelectItem(1L, "SI"));
            vigencias.add(new SelectItem(0L, "NO"));
            return vigencias;
    }

    /**
     * lista de select items de tipo cuadro para combo
     * @return
     */
    public List<SelectItem> getTipoCuadroItems() {
        List<SelectItem> tipoCuadroItems = new ArrayList<SelectItem>();
        for (TipoCuadro tipoCuadro : getTipoCuadroList()) {
            tipoCuadroItems.add(new SelectItem(tipoCuadro, Util.capitalizar(tipoCuadro.getNombre()))); 
            
        }
        return tipoCuadroItems;
    }

    /**
     * lista de select items de estado cuadro para combo
     * @return
     */
    public List<SelectItem> getEstadoCuadroItems() {
        List<SelectItem> estadoCuadroItems = new ArrayList<SelectItem>();
        for (EstadoCuadro estadoCuadro : getEstadoCuadroList()) {
            estadoCuadroItems.add(new SelectItem(estadoCuadro, estadoCuadro.getNombre()));
        }
        return estadoCuadroItems;
    }

    public List<SelectItem> getCatalogoSelectItem(){
        
        List<SelectItem> lista = new ArrayList<SelectItem>();
        
        for(Catalogo catalogo : getCatalogoList()){
            lista.add(new SelectItem(catalogo.getIdCatalogo(), catalogo.getNombre()));
        }
        
        return lista;
    }
    
    public SelectItem[] getTipoImpresionItems() {
        SelectItem[] items = new SelectItem[TipoImpresionEnum.values().length];
        int i = 0;
        for (TipoImpresionEnum t : TipoImpresionEnum.values()) {
            items[i++] = new SelectItem(t.getKey(), t.getValue());
        }
        return items;
    }

    /**
     * genera una lista de objetos selectItem para llenar combo de grupos.
     * @return
     */
    public List<SelectItem> getGrupoItems(){        
        List<SelectItem> grupos = new ArrayList<SelectItem>();
        try {
            for (Grupo grupo : this.getGrupoList()) {
                grupos.add(new SelectItem(grupo, MessageFormat.format("{0} - {1}", grupo.getIdGrupoAcceso(), grupo.getNombre())));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);            
        }
        return grupos;
    }  
        
    
    
    public List<SelectItem> getEmpresasItems(){
    	 List<SelectItem> empresas = new ArrayList<SelectItem>();
         try {
             for (Empresa empresa : this.getEmpresaList()) {
            	 empresas.add(new SelectItem(empresa, empresa.getRazonSocial()));
             }
         } catch (Exception e) {
             logger.error(e.getMessage(), e);             
         }
         return empresas;
    }
    
    public List<SelectItem> getFiltroBloqueoSelectItem() {
        List<SelectItem> bloqueoItems = new ArrayList<SelectItem>();
        bloqueoItems.add(new SelectItem(0L, "Habilitados"));
        bloqueoItems.add(new SelectItem(1L, "Bloqueados"));
        return bloqueoItems;
    }
    
    /**
     * genera una lista de objetos selectItem para llenar combo de roles.
     * @return
     */
    public List<SelectItem> getRolItems(){        
        List<SelectItem> rolItems = new ArrayList<SelectItem>();
        try {
            for (Rol rol : this.getRolList()) {
            	rolItems.add(new SelectItem(rol, rol.getIdRol()));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);            
        }
        return rolItems;
    } 

      
    /*Metodos accesadores para mantenedores*/
    
    public List<TipoDato> getTipoDatoList() {
            if(tipoDatoList==null){
                tipoDatoList = getFacade().getMantenedoresTipoService().findAllTipoDato();
            }
            return tipoDatoList;
    }

    public List<Periodo> getPeriodoList() {
        if(periodoList==null){
            periodoList = getFacade().getMantenedoresTipoService().findAllPeriodo();
        }
        return periodoList;
    }
    
    public List<TipoEstructura> getTipoEstructuraList() {
        if(tipoEstructuraList==null){
            tipoEstructuraList = facade.getMantenedoresTipoService().findAllTipoEstructura();
        }
        return tipoEstructuraList;
    }

    public List<TipoCelda> getTipoCeldaList() {
        if(tipoCeldaList==null){
            tipoCeldaList = getFacade().getMantenedoresTipoService().findAllTipoCelda();
        }
        return tipoCeldaList;
    }
    
    public List<Catalogo> getCatalogoList() {
        if(catalogoList==null){
            catalogoList = getFacade().getCatalogoService().findCatalogoAll();
        }
        return catalogoList;
    }

    public List<TipoCuadro> getTipoCuadroList() {
        if(tipoCuadroList == null){
           tipoCuadroList = this.getFacade().getMantenedoresTipoService().findAllTipoCuadro();
        }
        return tipoCuadroList;
    }
    
    public List<EstadoCuadro> getEstadoCuadroList() {
        if(estadoCuadroList == null){
            estadoCuadroList = this.getFacade().getMantenedoresTipoService().findAllEstadoCuadro(); 
        }
        return estadoCuadroList;
    }
    
    public List<Grupo> getGrupoList() throws Exception {
        if(grupoList == null){
            grupoList = getFacade().getSeguridadService().findGruposAll();
        }
        return grupoList;
    }
    
    public List<Empresa> getEmpresaList(){
    	if (empresaList == null){
    		empresaList = this.getFacade().getEmpresaService().findAll();
    	}    	
    	return empresaList;    	
    }

    public List<SelectItem> getEmpresaItems(){        
        List<SelectItem> empresaItems = new ArrayList<SelectItem>();
        try {
            for (Empresa empresa : this.getEmpresaList()) {
            	empresaItems.add(new SelectItem(empresa, empresa.getRazonSocial()));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);            
        }
        return empresaItems;
    } 
    
    
    public List<Rol> getRolList() throws Exception {
    	if(rolList == null){
    		rolList = this.getFacade().getMantenedoresTipoService().findAllRol();
    	}
		return rolList;
	}
    
    
    public FacadeServiceLocal getFacade() {
        return facade;
    }
        
    /*Metodos Mutadores*/
    
    public void setTipoDatoList(List<TipoDato> tipoDatoList) {
            this.tipoDatoList = tipoDatoList;
    }
    
    public void setPeriodoList(List<Periodo> periodoList) {
        this.periodoList = periodoList;
    }
    
    public void setTipoCeldaList(List<TipoCelda> tipoCeldaList) {
        this.tipoCeldaList = tipoCeldaList;
    }
    
    public void setCatalogoList(List<Catalogo> catalogoList) {
        this.catalogoList = catalogoList;
    }
    
    public void setTipoCuadroList(List<TipoCuadro> tipoCuadroList) {
        this.tipoCuadroList = tipoCuadroList;
    }
    
    
    public void setEstadoCuadroList(List<EstadoCuadro> estadoCuadroList) {
        this.estadoCuadroList = estadoCuadroList;
    }

    
    public void setGrupoList(List<Grupo> grupoList) {
        this.grupoList = grupoList;
    }
    
 
    
    public List<TipoFormulaVO> getTipoFormulaList() {
    	
        final List<TipoFormulaVO> lista = new ArrayList<TipoFormulaVO>();        
        lista.add(new TipoFormulaVO(Grilla.TIPO_GRILLA_ESTATICA, "Fórmula Estática"));
        lista.add(new TipoFormulaVO(Grilla.TIPO_GRILLA_DINAMICA, "Fórmula Dinámica"));    
       
        return lista;
    }

	public void setRolList(List<Rol> rolList) {
		this.rolList = rolList;
	}


}
