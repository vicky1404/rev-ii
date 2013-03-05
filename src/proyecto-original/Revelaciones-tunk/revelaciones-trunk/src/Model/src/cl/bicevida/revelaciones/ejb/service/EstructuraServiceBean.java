package cl.bicevida.revelaciones.ejb.service;


import cl.bicevida.revelaciones.ejb.common.TipoEstructuraEnum;
import static cl.bicevida.revelaciones.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;
import cl.bicevida.revelaciones.ejb.cross.SortHelper;
import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.revelaciones.ejb.entity.AgrupacionColumna;
import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.Columna;
import cl.bicevida.revelaciones.ejb.entity.Estructura;
import cl.bicevida.revelaciones.ejb.entity.Grilla;
import cl.bicevida.revelaciones.ejb.entity.HistorialVersionPeriodo;
import cl.bicevida.revelaciones.ejb.entity.Html;
import cl.bicevida.revelaciones.ejb.entity.RelacionCelda;
import cl.bicevida.revelaciones.ejb.entity.RelacionDetalleEeff;
import cl.bicevida.revelaciones.ejb.entity.RelacionEeff;
import cl.bicevida.revelaciones.ejb.entity.SubAgrupacionColumna;
import cl.bicevida.revelaciones.ejb.entity.SubCelda;
import cl.bicevida.revelaciones.ejb.entity.SubColumna;
import cl.bicevida.revelaciones.ejb.entity.SubGrilla;
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
    
    
        public List<Estructura> getSubGrillaByVersion(Version version, boolean applyFormula, SubGrilla subGrilla) throws FormulaException, Exception{
       
        List<Estructura> estructuraList = this.findEstructuraByVersion(version);        
        List<Estructura> estructuraListAux = new ArrayList<Estructura>();
			for(Estructura estructura : estructuraList){
				
							for (Grilla grilla : estructura.getGrillaList()) {
								for (SubGrilla sub  : grilla.getSubGrillaList()){
									if (subGrilla.getAgrupador().intValue() == sub.getAgrupador().intValue()){
											estructura.setGrillaVO(this.getGrillaSubGrillaVO(sub,applyFormula));
											estructuraListAux.add(estructura);
										}
									}
				}
			}
        
        return estructuraListAux;
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
    public GrillaVO getGrillaSubGrillaVO(SubGrilla subGrilla, boolean applyFormula) throws FormulaException, Exception{
    
        if(applyFormula){
            if (subGrilla.getTipoFormula() == null || subGrilla.getTipoFormula().equals(Grilla.TIPO_GRILLA_ESTATICA)){
                facadeService.getFormulaService().processStaticFormulaBySubGrilla(subGrilla);
            } else {
                facadeService.getFormulaService().processDynamicFomulaSubGrilla(subGrilla);
            }
        }

        GrillaVO grillaNotaVO = new GrillaVO();
        List<SubColumna> subColumnaList = subGrilla.getSubColumnaList();
        List<Map<Long,SubCelda>> rows = new ArrayList<Map<Long,SubCelda>>();
        Map<Long,SubCelda> celdaMap = new LinkedHashMap<Long,SubCelda>();
        
        boolean listaVacia = true;
        
        SortHelper.sortSubColumnasByOrden(subColumnaList);

        for(SubColumna columnaNota : subColumnaList){
            
            if(columnaNota.getSubCeldaList()==null)
                continue;
            
            int j=0;
            for(SubCelda subCelda : columnaNota.getSubCeldaList()){
                if(listaVacia){
                    celdaMap = new LinkedHashMap<Long,SubCelda>();
                    celdaMap.put(new Long(columnaNota.getIdSubColumna()), subCelda);
                    rows.add(celdaMap);
                }else{                    
                    if(rows.size()>j)
                        rows.get(j).put(new Long(columnaNota.getIdSubColumna()), subCelda);                    
                }
                j++;
            }
            listaVacia = false;
        }
        
        grillaNotaVO.setSubColumnas(subColumnaList); 
        grillaNotaVO.setSubGrilla(subGrilla);
        grillaNotaVO.setSubRows(rows);
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
    public List<SubAgrupacionColumna> findSubAgrupacionColumnaBySubGrilla(SubGrilla subGrilla) throws Exception {
        
            Query query = em.createNamedQuery(SubAgrupacionColumna.FIND_BY_SUB_GRILLA);
            query.setParameter("idGrilla", subGrilla.getIdGrilla());
            query.setParameter("idSubGrilla", subGrilla.getIdSubGrilla());            
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
                    guardarGrillas(estructura.getGrillaList()); }
                if (estructura.getTipoEstructura().getIdTipoEstructura().equals(TipoEstructuraEnum.HTML.getKey())){
                    mergeHTMLList(estructura.getHtmlList());}            
        }        
        em.merge(versionPeriodo);
        em.merge(historialVersionPeriodo);
    }
    
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistEstructuraSubGrilla(List<Estructura> estructuras, VersionPeriodo versionPeriodo, HistorialVersionPeriodo historialVersionPeriodo, SubGrilla subGrilla) throws Exception {
        
        List<SubGrilla> subGrillaList = new ArrayList<SubGrilla>();
        
        for (Estructura estructura : estructuras){
            for (Grilla grilla : estructura.getGrillaList()){
                for (SubGrilla sub : grilla.getSubGrillaList())
                    if (subGrilla.getAgrupador().intValue() == sub.getAgrupador().intValue()){
                            subGrillaList.add(sub);
                        }
                }
        }        
        
        guardarSubGrillas(subGrillaList); 
        
        //em.merge(versionPeriodo);
        //em.merge(historialVersionPeriodo);
    }

    
    /**
     * Distingue si las grillas tienen formulas estaticas o dinamicas.
     * @author Manuel Gutierrez C.
     * @since 15/02/2012
     * @param grillaList
     * @throws Exception
     */
    public void guardarGrillas(List<Grilla> grillaList) throws Exception {
        
        for (Grilla grilla : grillaList){
            if (grilla.getTipoFormula() != null && grilla.getTipoFormula().equals(Grilla.TIPO_GRILLA_DINAMICA)){
                persistGrillaList(grilla);
            } else {
                facadeService.getGrillaService().mergeEntity(grilla);
            }
        }
        
    }
    
    /**
     * Distingue si las SubGrillas tienen formulas estaticas o dinamicas.
     * @author Manuel Gutierrez C.
     * @since 14/08/2012
     * @param subGrillaList
     * @throws Exception
     */
    public void guardarSubGrillas(List<SubGrilla> subGrillaList) throws Exception {
        
        for (SubGrilla subGrilla : subGrillaList){
            if (subGrilla.getTipoFormula() != null && subGrilla.getTipoFormula().equals(Grilla.TIPO_GRILLA_DINAMICA)){
                persistGrillaList(subGrilla);
            } else {
                facadeService.getGrillaService().mergeSubGrilla(subGrilla);
            }
        }
        
    }
    
    
    public List<Grilla> persistGrillaList(Grilla grilla){

        List<Grilla>  grillaList = new ArrayList<Grilla>();
        List<RelacionEeff> relEeffList = new ArrayList<RelacionEeff>();
        List<RelacionDetalleEeff> relDetEeffList = new ArrayList<RelacionDetalleEeff>();
        List<RelacionCelda> relCeldaList = new ArrayList<RelacionCelda>();
        List<RelacionCelda> celdaList = new ArrayList<RelacionCelda>();
        
        for(Columna columna : grilla.getColumnaList()){
            for(Celda celda : columna.getCeldaList()){
                if(Util.esListaValida(celda.getRelacionEeffList())){
                    for(RelacionEeff rel : celda.getRelacionEeffList()){
                        relEeffList.add(rel);
                    }
                }
                if(Util.esListaValida(celda.getRelacionDetalleEeffList())){
                    for(RelacionDetalleEeff rel : celda.getRelacionDetalleEeffList()){
                        relDetEeffList.add(rel);
                    }
                }
                if (Util.esListaValida(celda.getCeldaRelacionadaList())){
                    
                    for (RelacionCelda rel : celda.getCeldaRelacionadaList()){
                            relCeldaList.add(rel);
                        }
                    
                    }
                
                if (Util.esListaValida(celda.getCeldaList())){
                    
                    for (RelacionCelda rel : celda.getCeldaList()){
                            celdaList.add(rel);
                        }
                    
                    }
            }
        }
        Long idPeriodo;
        try{
            idPeriodo = grilla.getEstructura1().getVersion().getVersionPeriodoList().get(0).getIdPeriodo();
            facadeService.getEstadoFinancieroService().deleteAllRelacionByGrillaPeriodo(idPeriodo, grilla.getEstructura1().getIdEstructura());
            //TODO eliminar relaciones xbrl
        }catch(Exception e){
            idPeriodo = null;
        }
        //Se eliminan las tabla RelacionCelda para que al borrar las celdas no se produzca error de base de datos por registro secundario encontrado. Manuel Gutierrez C. 12/02/2013
        int returnDelete1 =  em
                            .createQuery("delete from RelacionCelda c where c.idGrillaRel = :idGrilla")
                            .setParameter("idGrilla", grilla.getIdGrilla())
                            .executeUpdate();

        int returnDelete3 =  em
                            .createQuery("delete from RelacionCelda c where c.idGrilla = :idGrilla")
                            .setParameter("idGrilla", grilla.getIdGrilla())
                            .executeUpdate();
        
        
        int returnDelete2 =  em
                            .createQuery("delete from Celda c where c.idGrilla = :idGrilla")
                            .setParameter("idGrilla", grilla.getIdGrilla())
                            .executeUpdate();
        
            if(returnDelete1 > 0 && returnDelete2 > 0 && returnDelete3 > 0){
                for(Columna columna : grilla.getColumnaList()){
                    columna.setGrilla(grilla);
                    columna.setIdGrilla(grilla.getEstructura1().getIdEstructura());
                    for(Celda celda : columna.getCeldaList()){
                        celda.setIdColumna(columna.getIdColumna());
                        celda.setIdGrilla(grilla.getIdGrilla());
                        celda.setColumna(columna);
                        em.persist(celda);
                    }
                }
                
                for(RelacionEeff rel : relEeffList){
                    em.persist(rel);
                }
                for(RelacionDetalleEeff rel : relDetEeffList){
                    em.persist(rel);
                }
                for(RelacionCelda rel : relCeldaList){
                    em.persist(rel);
                }
                
                for(RelacionCelda rel : celdaList){
                    em.persist(rel);
                }
                
            }else{
                for(Columna columna : grilla.getColumnaList()){
                    columna.setGrilla(grilla);
                    columna.setIdGrilla(grilla.getIdGrilla());
                    for(Celda celda : columna.getCeldaList()){
                        celda.setIdColumna(columna.getIdColumna());
                        celda.setIdGrilla(grilla.getIdGrilla());
                        //celda.setGrupo(celda.getIdFila());
                        celda.setColumna(columna);                                
                    }
                }
                
                em.merge(grilla);
                grillaList.add(grilla);
                
                for (Columna columna : grilla.getColumnaList()){
                    for (AgrupacionColumna agrupacion : columna.getAgrupacionColumnaList()){
                            agrupacion = em.merge(agrupacion);    
                        }
                }
            }
            
        return grillaList;
    }
    
    
    public List<SubGrilla> persistGrillaList(SubGrilla subGrilla){

            List<SubGrilla>  subGrillaList = new ArrayList<SubGrilla>();
            SubGrilla subGrillaPaso = null;
                
                    subGrillaPaso = new SubGrilla();
                    subGrillaPaso.setIdGrilla(subGrilla.getIdGrilla());
                    subGrillaPaso.setGrilla(subGrilla.getGrilla());
                    subGrillaPaso.setIdSubGrilla(subGrilla.getIdSubGrilla());
                    subGrillaPaso.setTitulo(subGrilla.getTitulo());
                    subGrillaPaso.setSubColumnaList(subGrilla.getSubColumnaList());
                    
                    int returnDelete =  em.createQuery("delete from SubCelda c where c.idGrilla = :idGrilla and c.idSubGrilla = :idSubGrilla")
                                        .setParameter("idGrilla", subGrilla.getIdGrilla())
                                        .setParameter("idSubGrilla", subGrilla.getIdSubGrilla())
                                        .executeUpdate();
                
                        if(returnDelete > 0){
                            for(SubColumna subColumna : subGrillaPaso.getSubColumnaList()){
                                subColumna.setSubGrilla(subGrillaPaso);
                                subColumna.setIdGrilla(subGrillaPaso.getIdGrilla());
                                subColumna.setIdSubGrilla(subGrillaPaso.getIdSubGrilla());                               
                                for(SubCelda subCelda : subColumna.getSubCeldaList()){
                                    subCelda.setIdSubColumna(subColumna.getIdSubColumna());
                                    subCelda.setIdGrilla(subGrillaPaso.getIdGrilla());
                                    subCelda.setIdSubGrilla(subGrillaPaso.getIdSubGrilla());                                    
                                    subCelda.setSubColumna(subColumna);
                                    em.persist(subCelda);
                                }
                            }
                        }else{
                            for(SubColumna subColumna : subGrillaPaso.getSubColumnaList()){
                                subColumna.setSubGrilla(subGrillaPaso);
                                subColumna.setIdGrilla(subGrillaPaso.getIdGrilla());
                                subColumna.setIdSubGrilla(subGrillaPaso.getIdSubGrilla());
                                for(SubCelda subCelda : subColumna.getSubCeldaList()){
                                    subCelda.setIdSubColumna(subColumna.getIdSubColumna());
                                    subCelda.setIdGrilla(subGrillaPaso.getIdGrilla());
                                    subCelda.setIdSubGrilla(subGrillaPaso.getIdSubGrilla());
                                    subCelda.setSubColumna(subColumna);                                
                                }
                            }
                            subGrillaPaso = em.merge(subGrillaPaso);
                            subGrillaList.add(subGrillaPaso);
                            
                            for (SubColumna subColumna : subGrillaPaso.getSubColumnaList()){
                                for (SubAgrupacionColumna subAgrupacion : subColumna.getSubAgrupacionColumnaList()){
                                        subAgrupacion = em.merge(subAgrupacion);    
                                    }
                            }
                        }
                
                    return subGrillaList;
        }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<AgrupacionColumna> findAgrupacionColumnaByGrillaNivel(Long idGrilla, Long idNivel) throws Exception {
        Query query = em.createNamedQuery(AgrupacionColumna.FIND_BY_GRILLA_GRUPO);
        query.setParameter("idGrilla", idGrilla);
        query.setParameter("idNivel", idNivel);
        return query.getResultList();
    }

   
}
