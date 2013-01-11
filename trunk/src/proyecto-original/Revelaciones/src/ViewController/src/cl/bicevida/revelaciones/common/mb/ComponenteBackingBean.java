package cl.bicevida.revelaciones.common.mb;

import cl.bicevida.revelaciones.common.util.BeanUtil;
import cl.bicevida.revelaciones.common.util.PropertyManager;
import cl.bicevida.revelaciones.ejb.common.TipoImpresionEnum;
import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.revelaciones.ejb.entity.Catalogo;
import cl.bicevida.revelaciones.ejb.entity.EstadoCuadro;
import cl.bicevida.revelaciones.ejb.entity.Grupo;
import cl.bicevida.revelaciones.ejb.entity.Periodo;

import cl.bicevida.revelaciones.ejb.entity.TipoCelda;
import cl.bicevida.revelaciones.ejb.entity.TipoCuadro;
import cl.bicevida.revelaciones.ejb.entity.TipoDato;
import cl.bicevida.revelaciones.ejb.entity.TipoEstructura;
import cl.bicevida.revelaciones.ejb.entity.VersionPeriodo;
import cl.bicevida.revelaciones.ejb.facade.local.FacadeServiceLocal;

import cl.bicevida.revelaciones.mb.CuadroBackingBean;

import java.io.Serializable;

import java.text.MessageFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;

import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

public class ComponenteBackingBean implements Serializable {
    
    @SuppressWarnings("compatibility:-3213244547428050211")
    private static final long serialVersionUID = -3252284069119110506L;
    
    public static final String BEAN_NAME = "componenteBackingBean";
    
    private transient Logger logger = Logger.getLogger(CuadroBackingBean.class);

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
    private List<TipoDato> tipoDatoList;
    private List<Catalogo> catalogoList;
    private List<VersionPeriodo> periodoCatalogoList;
    
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
        BeanUtil.sortSelectItemMeses(meses);
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
            vigencias.add(new SelectItem(1L, "Sí"));
            vigencias.add(new SelectItem(0L, "No"));
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
    public List<SelectItem> getGrupos(){        
        List<SelectItem> grupos = new ArrayList<SelectItem>();
        try {
            for (Grupo grupo : this.getGrupoList()) {
                grupos.add(new SelectItem(grupo, MessageFormat.format("{0} - {1}", grupo.getIdGrupo(), grupo.getNombre())));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //agregarErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_init_services_error"));
        }
        return grupos;
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
    
    public List<VersionPeriodo> getPeriodoCatalogoList() {
        return periodoCatalogoList;
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

    public void setPeriodoCatalogoList(List<VersionPeriodo> periodoCatalogoList) {
        this.periodoCatalogoList = periodoCatalogoList;
    }


    public void setPeriodoActual(Periodo periodoActual) {
        this.periodoActual = periodoActual;
    }
}
