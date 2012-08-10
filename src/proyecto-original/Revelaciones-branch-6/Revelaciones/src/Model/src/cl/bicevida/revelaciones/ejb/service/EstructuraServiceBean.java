package cl.bicevida.revelaciones.ejb.service;


import cl.bicevida.revelaciones.ejb.common.TipoEstructuraEnum;
import static cl.bicevida.revelaciones.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;
import cl.bicevida.revelaciones.ejb.cross.SortHelper;
import cl.bicevida.revelaciones.ejb.entity.AgrupacionColumna;
import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.Columna;
import cl.bicevida.revelaciones.ejb.entity.Estructura;
import cl.bicevida.revelaciones.ejb.entity.Grilla;
import cl.bicevida.revelaciones.ejb.entity.HistorialVersionPeriodo;
import cl.bicevida.revelaciones.ejb.entity.Html;
import cl.bicevida.revelaciones.ejb.entity.Texto;
import cl.bicevida.revelaciones.ejb.entity.Version;
import cl.bicevida.revelaciones.ejb.entity.VersionPeriodo;
import cl.bicevida.revelaciones.ejb.facade.local.FacadeServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.EstructuraServiceLocal;
import cl.bicevida.revelaciones.exceptions.FormulaException;
import cl.bicevida.revelaciones.vo.GrillaVO;
import cl.bicevida.revelaciones.vo.HtmlVO;
import cl.bicevida.revelaciones.vo.TextoVO;

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

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;


@Stateless(name = "EstructuraNotaService" , mappedName = "AppRevelaciones-Model-EstructuraNotaService")
public class EstructuraServiceBean implements EstructuraServiceLocal {
    
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
     * Guarda en la base de datos una lista de HTML.
     * @param htmlList
     * @author Manuel Gutierrez C.
     * @since 18/01/2011
     * @throws Exception
     */
    public void mergeHTMLList(List<Html> htmlList) throws Exception{
        
        for (Html html : htmlList){
                em.merge(html);
            }
    }

    /**
     * @param grilla
     * @return
     * @throws Exception
     */
    public List<Celda> getCeldasFromGrilla(Grilla grilla) throws Exception{
        final List<Celda> celdas = new ArrayList<Celda>(); 
        for(Columna columna : grilla.getColumnaList()){
            for(Celda celda : columna.getCeldaList()){
                celdas.add(celda);
            }                
        }
        return celdas;    
    }

    /**
     * @param version
     * @return
     * @throws Exception
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Estructura> findEstructuraByVersion(Version version) throws Exception{
        return em.createNamedQuery(Estructura.FIND_ESTRUCTURA_BY_VERSION)
                 .setHint(QueryHints.MAINTAIN_CACHE, HintValues.FALSE)
                 .setHint(QueryHints.REFRESH, HintValues.TRUE)
                 .setParameter("version", version)                 
                 .getResultList();
    }
    
    /**
     * @param version, idTipoEstructura
     * @return
     * @throws Exception
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Estructura> findEstructuraByVersionTipo(Version version, Long idTipoEstructura) throws Exception{
        return em.createNamedQuery(Estructura.FIND_ESTRUCTURA_BY_VERSION_TIPO)
                 .setHint(QueryHints.MAINTAIN_CACHE, HintValues.FALSE)
                 .setHint(QueryHints.REFRESH, HintValues.TRUE)
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
                for (Grilla grilla : estructura.getGrillaList()) {
                    estructura.setGrillaVO(this.getGrillaVO(grilla,applyFormula));
                }
                for (Texto texto : estructura.getTextoList()) {
                    estructura.setTextoVo(new TextoVO(texto));
                }
                for (Html html : estructura.getHtmlList()) {
                    estructura.setHtmlVo(new HtmlVO(html));
                }
        }
        
        
        return estructuraList;
    }
    
    public List<Estructura> getEstructuraByVersionTipo(Version version, Long idTipoEstructura) throws Exception{
       
        List<Estructura> estructuraList = this.findEstructuraByVersionTipo(version,idTipoEstructura);        
        
        for(Estructura estructura : estructuraList){
                for (Grilla grilla : estructura.getGrillaList()) {
                    estructura.setGrillaVO(this.getGrillaVO(grilla,true));
                }
                for (Texto texto : estructura.getTextoList()) {
                    estructura.setTextoVo(new TextoVO(texto));
                }
                for (Html html : estructura.getHtmlList()) {
                    estructura.setHtmlVo(new HtmlVO(html));
                }
        }
        
        
        return estructuraList;
    }
    
    public List<Estructura> getEstructuraTipoGrillaByVersion(Version version) throws Exception{
       
        List<Estructura> estructuraList = this.findEstructuraByVersion(version);        
       
        for(Estructura estructura : estructuraList){
            
            for (Grilla grilla : estructura.getGrillaList()){
                estructura.setGrillaVO(this.getGrillaVO(grilla, true));                            
            }
        }
        
        
        return estructuraList;
    }
    
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public GrillaVO getGrillaVO(Grilla grilla, boolean applyFormula) throws FormulaException, Exception{
    
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
                facadeService.getGrillaService().mergeGrillaList(estructura.getGrillaList());
            } else if (estructura.getTipoEstructura().getIdTipoEstructura().equals(TipoEstructuraEnum.HTML.getKey())) {                
               this.mergeHTMLList(estructura.getHtmlList());
            }
        }
        
    }
    
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
    
    
    public List<Object> sql(String sql) throws Exception {
            Query query = em.createNativeQuery(sql);
            return query.getResultList();
     }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistEstructura(List<Estructura> estructuras, VersionPeriodo versionPeriodo, HistorialVersionPeriodo historialVersionPeriodo) throws Exception {
        for (Estructura estructura : estructuras){
                if (estructura.getTipoEstructura().getIdTipoEstructura().equals(TipoEstructuraEnum.GRILLA.getKey())){
                    guardarGrillas(estructura); }
                if (estructura.getTipoEstructura().getIdTipoEstructura().equals(TipoEstructuraEnum.HTML.getKey())){
                    mergeHTMLList(estructura.getHtmlList());}            
        }
        em.merge(versionPeriodo);
        em.merge(historialVersionPeriodo);
    }
    
    /**
     * Distingue si las grillas tienen formulas estaticas o dinamicas.
     * @author Manuel Gutierrez C.
     * @since 15/02/2012
     * @param grillaList
     * @throws Exception
     */
     public void guardarGrillas(Estructura estructura) throws Exception {

             for (Grilla grilla : estructura.getGrillaList()){
                 
                 estructura.setGrillaVO(this.getGrillaVO(grilla,true));
                 
                 if (grilla.getTipoFormula() != null && grilla.getTipoFormula().equals(Grilla.TIPO_GRILLA_DINAMICA)){
                     persistGrillaList(grilla);
                 } else {
                     facadeService.getGrillaService().mergeEntity(grilla);
                 }
             }
             
    }
    
    
    public List<Grilla> persistGrillaList(Grilla grilla){

            List<Grilla>  grillaList = new ArrayList<Grilla>();
            Grilla grillaPaso = null;
        
            //for (Grilla grilla : grillas){
                
                    grillaPaso = new Grilla();
                    grillaPaso.setIdGrilla(grilla.getEstructura1().getIdEstructura());
                    grillaPaso.setEstructura1(grilla.getEstructura1());
                    grillaPaso.setTitulo(grilla.getTitulo());
                    grillaPaso.setColumnaList(grilla.getColumnaList());
                
                    int returnDelete =  em
                                        .createQuery("delete from Celda c where c.idGrilla = :idGrilla")
                                        .setParameter("idGrilla", grilla.getIdGrilla())
                                        .executeUpdate();
                
                
                        if(returnDelete > 0){
                            for(Columna columna : grillaPaso.getColumnaList()){
                                columna.setGrilla(grillaPaso);
                                columna.setIdGrilla(grillaPaso.getEstructura1().getIdEstructura());
                                for(Celda celda : columna.getCeldaList()){
                                    celda.setIdColumna(columna.getIdColumna());
                                    celda.setIdGrilla(grillaPaso.getIdGrilla());
                                    celda.setColumna(columna);
                                    em.persist(celda);
                                }
                            }
                        }else{
                            for(Columna columna : grillaPaso.getColumnaList()){
                                columna.setGrilla(grillaPaso);
                                columna.setIdGrilla(grillaPaso.getIdGrilla());
                                for(Celda celda : columna.getCeldaList()){
                                    celda.setIdColumna(columna.getIdColumna());
                                    celda.setIdGrilla(grillaPaso.getIdGrilla());
                                    //celda.setGrupo(celda.getIdFila());
                                    celda.setColumna(columna);                                
                                }
                            }
                            
                            grillaPaso = em.merge(grillaPaso);
                            grillaList.add(grillaPaso);
                            
                            for (Columna columna : grillaPaso.getColumnaList()){
                                for (AgrupacionColumna agrupacion : columna.getAgrupacionColumnaList()){
                                        agrupacion = em.merge(agrupacion);    
                                    }
                            }
                        }
            //}
        
                    return grillaList;        
            
        }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<AgrupacionColumna> findAgrupacionColumnaByGrillaNivel(Long idGrilla, Long idNivel) throws Exception {
        Query query = em.createNamedQuery(AgrupacionColumna.FIND_BY_GRILLA_GRUPO);
        query.setParameter("idGrilla", idGrilla);
        query.setParameter("idNivel", idNivel);
        return query.getResultList();
    }

   
}
