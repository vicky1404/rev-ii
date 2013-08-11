package cl.mdr.ifrs.ejb.service;


import static cl.mdr.ifrs.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import cl.mdr.ifrs.ejb.common.Constantes;
import cl.mdr.ifrs.ejb.common.TipoEstructuraEnum;
import cl.mdr.ifrs.ejb.cross.SortHelper;
import cl.mdr.ifrs.ejb.entity.AgrupacionColumna;
import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.ifrs.ejb.entity.Columna;
import cl.mdr.ifrs.ejb.entity.Estructura;
import cl.mdr.ifrs.ejb.entity.Grilla;
import cl.mdr.ifrs.ejb.entity.HistorialVersion;
import cl.mdr.ifrs.ejb.entity.Version;
import cl.mdr.ifrs.ejb.facade.local.FacadeServiceLocal;
import cl.mdr.ifrs.ejb.service.local.EstructuraServiceLocal;
import cl.mdr.ifrs.exceptions.FormulaException;
import cl.mdr.ifrs.vo.GrillaVO;
import cl.mdr.ifrs.vo.HtmlVO;
import cl.mdr.ifrs.vo.TextoVO;


@Stateless(name = "EstructuraNotaService" , mappedName = "AppRevelaciones-Model-EstructuraNotaService")
public class EstructuraServiceBean implements EstructuraServiceLocal {
    @SuppressWarnings("unused")
	private final Logger logger = Logger.getLogger(EstructuraServiceBean.class);
    @EJB FacadeServiceLocal facadeService;
    
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;

    public EstructuraServiceBean() {
    }

    public Object queryByRange(String jpqlStmt, int firstResult, int maxResults)  {
        Query query = em.createQuery(jpqlStmt);
        if (firstResult > 0) {
            query = query.setFirstResult(firstResult);
        }
        if (maxResults > 0) {
            query = query.setMaxResults(maxResults);
        }
        return query.getResultList();
    }
    
    public Estructura mergeEstructura(Estructura estructura) throws Exception{
        return em.merge(estructura);
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void mergeEstructuraList(List<Estructura> estructuraList) throws Exception{
        for(Estructura estructura : estructuraList){
            em.merge(estructura);
        } 
    }
    

    /**
     * @param grilla
     * @return
     * @throws Exception
     */
    public List<Celda> getCeldasFromGrilla(Grilla grilla) throws Exception{
        final List<Celda> celdas = facadeService.getCeldaService().findCeldaByGrilla(grilla);                 
        return celdas;    
    }

    /**
     * @param version
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Estructura> findEstructuraByVersion(Version version) throws Exception{
        return em.createNamedQuery(Estructura.FIND_ESTRUCTURA_BY_VERSION)                 
                 .setParameter("version", version.getIdVersion())                 
                 .getResultList();
    }
    
    /**
     * @param version, idTipoEstructura
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Estructura> findEstructuraByVersionTipo(Version version, Long idTipoEstructura) throws Exception{
        return em.createNamedQuery(Estructura.FIND_ESTRUCTURA_BY_VERSION_TIPO)                 
                 .setParameter("version", version)                 
                 .setParameter("idTipoEstructura", idTipoEstructura)
                 .getResultList();
    }    
    
    /**
     * @param version
     * @return
     * @throws Exception
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Estructura findEstructuraById(Long id) throws Exception{
        return (Estructura)em.createNamedQuery(Estructura.FIND_ESTRUCTURA_BY_ID).setParameter("id", id).getSingleResult();
    }
    
    public List<Estructura> getEstructuraByVersion(Version version, boolean applyFormula) throws FormulaException, Exception{
       
        List<Estructura> estructuraList = this.findEstructuraByVersion(version);        
        
        for(Estructura estructura : estructuraList){
                if (estructura.getGrilla() != null) {
                    estructura.setGrillaVO(this.getGrillaVO(estructura.getGrilla(),applyFormula));
                }
                if (estructura.getTexto() != null) {
                    estructura.setTextoVo(new TextoVO(estructura.getTexto()));
                }
                if (estructura.getHtml() != null) {
                    estructura.setHtmlVo(new HtmlVO(estructura.getHtml()));
                }
        }
        
        
        return estructuraList;
    }
    
    public List<Estructura> getEstructuraByVersionTipo(Version version, Long idTipoEstructura) throws Exception{
       
        List<Estructura> estructuraList = this.findEstructuraByVersionTipo(version,idTipoEstructura);        
        
        for(Estructura estructura : estructuraList){
                if (estructura.getGrilla() != null) {
                    estructura.setGrillaVO(this.getGrillaVO(estructura.getGrilla() ,true));
                }
                if (estructura.getTexto()  != null) {
                    estructura.setTextoVo(new TextoVO(estructura.getTexto()));
                }
                if (estructura.getHtml()  != null) {
                    estructura.setHtmlVo(new HtmlVO(estructura.getHtml()));
                }
        }
        
        
        return estructuraList;
    }
    
    public List<Estructura> getEstructuraTipoGrillaByVersion(Version version) throws Exception{
       
        List<Estructura> estructuraList = this.findEstructuraByVersion(version);        
       
        for(Estructura estructura : estructuraList){
            
            if (estructura.getGrilla() != null){
                estructura.setGrillaVO(this.getGrillaVO(estructura.getGrilla(), true));                            
            }
        }
        
        
        return estructuraList;
    }
    
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public GrillaVO getGrillaVO(Grilla grilla, boolean applyFormula) throws FormulaException, Exception{
    
    	//final Estructura estructura = facadeService.getEstructuraService().findEstructuraById(grillaE.getIdGrilla());
    	//final Grilla grilla = estructura.getGrilla();
    	
        if(applyFormula){
            if (grilla.getTipoFormula() == null || grilla.getTipoFormula().equals(Grilla.TIPO_GRILLA_ESTATICA)){
                facadeService.getFormulaService().processStaticFormula(grilla);
            } else {
                //TODO probar formulas dinamicas
                facadeService.getFormulaService().processDynamicFomula(grilla);
            }
        }

        GrillaVO grillaNotaVO = new GrillaVO();
        List<Columna> columnaList = grilla.getColumnaList();        
        List<Map<Long,Celda>> rows = new ArrayList<Map<Long,Celda>>();
        Map<Long,Celda> celdaMap = new LinkedHashMap<Long,Celda>();
        
        boolean listaVacia = true;
        
        SortHelper.sortColumnasByOrden(columnaList);

        for(Columna columnaNota : columnaList){
            
            if(columnaNota.getCeldaList()==null)
                continue;
            
            int j=0;
            for(Celda celda : columnaNota.getCeldaList()){
                if(listaVacia){
                    celdaMap = new LinkedHashMap<Long,Celda>();
                    celdaMap.put(new Long(columnaNota.getIdColumna()), celda);
                    rows.add(celdaMap);
                }else{                    
                    if(rows.size()>j)
                        rows.get(j).put(new Long(columnaNota.getIdColumna()), celda);                    
                }
                j++;
            }
            listaVacia = false;
        }
        
        grillaNotaVO.setColumnas(columnaList); 
        grillaNotaVO.setGrilla(grilla);
        grillaNotaVO.setRows(rows);
        return grillaNotaVO;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Set<Long> getTotalFilasFromGrilla(List<Celda> celdas) throws Exception{
        final Set<Long> filas = new LinkedHashSet<Long>();         
        for(Celda celda : celdas){
            filas.add(celda.getIdFila());
        }
        return filas;
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistEstructuraList(List<Estructura> estructuraList) throws Exception {
        
        for (Estructura estructura : estructuraList){            
            if (estructura.getTipoEstructura().getIdTipoEstructura().equals(TipoEstructuraEnum.GRILLA.getKey())){
                em.merge(estructura.getGrilla());
            } else if (estructura.getTipoEstructura().getIdTipoEstructura().equals(TipoEstructuraEnum.HTML.getKey())) {                
               em.merge(estructura.getHtml());
            }
        }
        
    }
    
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)    
    public List<AgrupacionColumna> findAgrupacionColumnaByGrilla(Grilla grilla) throws Exception {
        
            Query query = em.createNamedQuery(AgrupacionColumna.FIND_BY_GRILLA);
            query.setParameter("idGrilla", grilla.getIdGrilla());
            return query.getResultList();   
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)    
    public AgrupacionColumna findAgrupacionColumnaById(AgrupacionColumna agrupacion) throws Exception {
        Query query = em.createNamedQuery(AgrupacionColumna.FIND_BY_ID);
        query.setParameter("idColumna", agrupacion.getIdColumna());
        query.setParameter("idGrilla", agrupacion.getIdGrilla());
        query.setParameter("idNivel", agrupacion.getIdNivel());
        return (AgrupacionColumna)query.getSingleResult();   
    }
    
    
    @SuppressWarnings("unchecked")
	public List<Object> sql(String sql) throws Exception {
            Query query = em.createNativeQuery(sql);
            return query.getResultList();
     }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistEstructura(List<Estructura> estructuras, HistorialVersion historialVersionPeriodo) throws Exception {
        for (Estructura estructura : estructuras){
                if (estructura.getTipoEstructura().getIdTipoEstructura().equals(TipoEstructuraEnum.GRILLA.getKey())){
                    guardarGrillas(estructura.getGrilla()); }
                if (estructura.getTipoEstructura().getIdTipoEstructura().equals(TipoEstructuraEnum.HTML.getKey())){                   
                   em.merge(estructura.getHtml());}            
        }        
        //em.merge(versionPeriodo);
        em.merge(historialVersionPeriodo);
    }
    
    /**
     * Distingue si las grillas tienen formulas estaticas o dinamicas.
     * @author MDR Technology
     * @since 15/02/2012
     * @param grillaList
     * @throws Exception
     */
    public void guardarGrillas(Grilla grilla) throws Exception {
        
        if (grilla.getTipoFormula() != null && grilla.getTipoFormula().equals(Grilla.TIPO_GRILLA_DINAMICA)){
            persistGrillaList(grilla);
        } else {
            facadeService.getGrillaService().mergeEntity(grilla);
        }
    }
    
    
    public void persistGrillaList(Grilla grilla){

        /*int returnDelete =  */
        em.createQuery("delete from Celda c where c.idGrilla = :idGrilla").setParameter("idGrilla", grilla.getIdGrilla()).executeUpdate();
    
        for(Columna columna : grilla.getColumnaList()){

            for(Celda celda : columna.getCeldaList()){
                em.createNativeQuery(" Insert into " + Constantes.CELDA + " (ID_COLUMNA,ID_FILA,ID_GRILLA,ID_TIPO_CELDA,ID_TIPO_DATO,VALOR," +
                															" CHILD_HORIZONTAL,PARENT_HORIZONTAL,CHILD_VERTICAL,PARENT_VERTICAL,FORMULA)" +
                															" values (?,?,?,?,?,?,?,?,?,?,?) ")
                .setParameter(1, celda.getIdColumna())
                .setParameter(2, celda.getIdFila())
                .setParameter(3, celda.getIdGrilla())
                .setParameter(4, celda.getTipoCelda().getIdTipoCelda())
                .setParameter(5, celda.getTipoDato().getIdTipoDato())
                .setParameter(6, celda.getValor())
                .setParameter(7, celda.getChildHorizontal())
                .setParameter(8, celda.getParentHorizontal())
                .setParameter(9, celda.getChildVertical())
                .setParameter(10, celda.getParentVertical())
                .setParameter(11, celda.getFormula())
                .executeUpdate();
            }
        }
    
    }
    
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<AgrupacionColumna> findAgrupacionColumnaByGrillaNivel(Long idGrilla, Long idNivel) throws Exception {
        Query query = em.createNamedQuery(AgrupacionColumna.FIND_BY_GRILLA_GRUPO);
        query.setParameter("idGrilla", idGrilla);
        query.setParameter("idNivel", idNivel);
        return query.getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistEstructura(List<Estructura> estructuras, Version version, HistorialVersion historialVersion) throws Exception {
    	em.merge(version);
    	
        for (Estructura estructura : estructuras){
                
        	if (estructura.getTipoEstructura().getIdTipoEstructura().equals(TipoEstructuraEnum.GRILLA.getKey())){
        		
        		estructura.setGrillaVO(this.getGrillaVO(estructura.getGrilla(),true));
                
        		if (estructura.getGrilla().getTipoFormula() != null && estructura.getGrilla().getTipoFormula().equals(Grilla.TIPO_GRILLA_DINAMICA)){
                    persistGrillaList(estructura.getGrilla());
                } else {
                    for(Columna columna : estructura.getGrilla().getColumnaList()){
                    	for(Celda celda : columna.getCeldaList()){
                    		em.merge(celda);
                    	}
                    }
                }
        		
        	}
                
            if (estructura.getTipoEstructura().getIdTipoEstructura().equals(TipoEstructuraEnum.HTML.getKey()))
                em.merge(estructura.getHtml());            
        }
        em.merge(historialVersion);
    }
   
}
