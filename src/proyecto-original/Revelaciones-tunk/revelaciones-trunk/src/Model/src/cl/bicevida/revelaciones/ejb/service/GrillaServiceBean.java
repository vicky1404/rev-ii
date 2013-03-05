package cl.bicevida.revelaciones.ejb.service;


import cl.bicevida.revelaciones.ejb.common.TipoCeldaEnum;
import cl.bicevida.revelaciones.ejb.common.TipoDatoEnum;
import static cl.bicevida.revelaciones.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;
import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.revelaciones.ejb.entity.AgrupacionColumna;
import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.Columna;
import cl.bicevida.revelaciones.ejb.entity.Estructura;
import cl.bicevida.revelaciones.ejb.entity.Grilla;
import cl.bicevida.revelaciones.ejb.entity.SubAgrupacionColumna;
import cl.bicevida.revelaciones.ejb.entity.SubCelda;
import cl.bicevida.revelaciones.ejb.entity.SubColumna;
import cl.bicevida.revelaciones.ejb.entity.SubGrilla;
import cl.bicevida.revelaciones.ejb.entity.Version;
import cl.bicevida.revelaciones.ejb.entity.pk.SubGrillaPK;
import cl.bicevida.revelaciones.ejb.service.local.GrillaServiceLocal;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;


@Stateless
public class GrillaServiceBean implements GrillaServiceLocal{
    
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;
    
    public GrillaServiceBean() {
        super();
    }
    
    public Object mergeEntity(Grilla entity){
        return em.merge(entity);
    }
    
    public Object mergeSubGrilla(SubGrilla entity){
        return em.merge(entity);
    }


    public Object persistEntity(Grilla entity){
        em.persist(entity);
        return entity;
    }
    
    public Grilla findGrilla(Long idGrilla) throws Exception{
        Grilla grilla = em.find(Grilla.class, idGrilla);
        return grilla;
    }
    
    public Grilla findGrillaById(Long idGrilla) throws Exception{
        
        Query query = em.createNamedQuery(Grilla.FIND_GRILLA_BY_ID)
                 .setHint(QueryHints.MAINTAIN_CACHE, HintValues.FALSE)
                 .setHint(QueryHints.REFRESH, HintValues.TRUE)
                 .setParameter("idGrilla", idGrilla);
        
        if (query.getResultList().size() > 0){
                
                return (Grilla) query.getSingleResult();
            
        } else {
                return new Grilla();
            }
                 
        
        
    }
    
    /**
     * stored in the database a list of cells.
     * @param grid
     * @author Rodrigo Díaz V.
     * @since 15/03/2011
     * @throws Exception
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistCell(Grilla grid) throws Exception{
        int resultado = em.createQuery("delete from Celda c where c.idGrilla = :idGrilla").setParameter("idGrilla", grid.getIdGrilla()).executeUpdate();
        for(Columna column : grid.getColumnaList()){
            for(Celda cell : column.getCeldaList()){
                
                System.out.println("ID_GRILLA->" + cell.getIdGrilla() + " COLUMNA->"+cell.getIdColumna()+" FILA->" + cell.getIdFila() + " DATO->"+cell.getTipoDato().getIdTipoDato()+" CELDA->"+cell.getTipoCelda().getIdTipoCelda());
                
                em.createNativeQuery(" INSERT INTO rev_celda ( id_grilla, id_Fila, id_Columna, id_tipo_dato, id_tipo_celda, valor, child_vertical, parent_vertical) VALUES ( ?,?,?,?,?,?,?,?) " )
                    .setParameter(1, cell.getIdGrilla())
                    .setParameter(2, cell.getIdFila())
                    .setParameter(3, cell.getIdColumna())
                    .setParameter(4, cell.getTipoDato().getIdTipoDato())
                    .setParameter(5, cell.getTipoCelda().getIdTipoCelda())
                    .setParameter(6, cell.getValor())
                    .setParameter(7, cell.getChildVertical())
                    .setParameter(8, cell.getParentVertical())
                    .executeUpdate();
                //em.persist(cell);
            }
        }
    }
    
    /**
     * Guarda en la base de datos una lista de grillas.
     * @param grillaNotaList
     * @author Manuel Gutierrez C.
     * @since 18/01/2012
     * @throws Exception
     */
    public void mergeGrillaList(List<Grilla> grillaList) throws Exception{
        
        for (Grilla grilla : grillaList){
                em.merge(grilla);
            }
    }
    
    /**
     * Desagrega las grillas para las notas seleccionadas.
     * @author Manuel Gutierrez C.
     * @since 24/04/2012
     * @param estructuraList
     * @throws Exception
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void desagregarGrilla(final List<Version> versionList) throws Exception{
        
        for (Version version : versionList){
                    for (Estructura estructura : version.getEstructuraList()){
                        for (Grilla grilla : estructura.getGrillaList()){
                            
                            for (Columna columna : grilla.getColumnaList()){
                                for (Celda celda :columna.getCeldaList()){
                                        if (celda.getTipoCelda().getIdTipoCelda().equals(TipoCeldaEnum.NUMERO.getKey())){
                                                celda.setValor("0");
                                        } 
                                    }
                                }
                            
                            this.mergeEntity(grilla); //Guardo la grilla
                            
                            for (int i = 0; i <= version.getCantidadGrupos().intValue()-1 ; i++){
                                        SubGrilla subGrilla = new SubGrilla();
                                            subGrilla.setIdGrilla(grilla.getIdGrilla());                                        
                                            subGrilla.setTitulo(grilla.getTitulo());        
                                            subGrilla.setGrilla(grilla);
                                            subGrilla.setTipoFormula(grilla.getTipoFormula());  
                                            subGrilla.setAgrupador(Integer.valueOf(i)); //Agrupa las subgrillas
                                    List<SubColumna> subColumnaList = new ArrayList<SubColumna>();
                                    for (Columna columna : grilla.getColumnaList()){
                                             SubColumna subColumna = new SubColumna();
                                                 subColumna.setAncho(columna.getAncho());
                                                 subColumna.setIdGrilla(columna.getIdGrilla());
                                                 subColumna.setIdSubColumna(columna.getIdColumna());
                                                 subColumna.setIdSubGrilla(subGrilla.getIdSubGrilla());
                                                 subColumna.setOrden(columna.getOrden());
                                                 subColumna.setRowHeader(columna.isRowHeader());
                                                 subColumna.setTituloColumna(columna.getTituloColumna());
                                                 subColumna.setSubGrilla(subGrilla);    
                                                List<SubCelda> subCeldaList = new ArrayList<SubCelda>();
                                                for (Celda celda : columna.getCeldaList()){
                                                        SubCelda subCelda = new SubCelda();
                                                            subCelda.setChildHorizontal(celda.getChildHorizontal());
                                                            subCelda.setChildVertical(celda.getChildVertical());
                                                            subCelda.setFormula(celda.getFormula());
                                                            subCelda.setIdGrilla(celda.getIdGrilla());
                                                            subCelda.setIdSubColumna(columna.getIdColumna());
                                                            subCelda.setIdSubFila(celda.getIdFila());
                                                            subCelda.setIdSubGrilla(subGrilla.getIdSubGrilla());
                                                            subCelda.setParentHorizontal(celda.getParentHorizontal());
                                                            subCelda.setParentVertical(celda.getParentVertical());
                                                            subCelda.setSubColumna(subColumna);
                                                            subCelda.setTipoCelda(celda.getTipoCelda());
                                                            subCelda.setTipoDato(celda.getTipoDato());
                                                    if (celda.getTipoCelda().getIdTipoCelda().equals(TipoCeldaEnum.NUMERO.getKey())){
                                                            //subCelda.setValor(celda.getValor());
                                                            subCelda.setValor("0");
                                                    } else {
                                                            subCelda.setValor(celda.getValor());
                                                        }
                                                            
                                                            subCeldaList.add(subCelda);
                                                    }
                                       
                                        List<SubAgrupacionColumna> subAgrupacionColumnaList = new ArrayList<SubAgrupacionColumna>();
                                        for (AgrupacionColumna agrupacion : columna.getAgrupacionColumnaList()){
                                               SubAgrupacionColumna subAgrupacion = new SubAgrupacionColumna();
                                                   subAgrupacion.setAncho(agrupacion.getAncho());
                                                   subAgrupacion.setGrupo(agrupacion.getGrupo());
                                                   subAgrupacion.setIdGrilla(grilla.getIdGrilla());
                                                   subAgrupacion.setIdNivel(agrupacion.getIdNivel());                    
                                                   subAgrupacion.setTitulo(agrupacion.getTitulo());                     
                                                   subAgrupacion.setIdSubGrilla(subGrilla.getIdSubGrilla());
                                                   subAgrupacion.setIdSubColumna(columna.getIdColumna());
                                                   subAgrupacion.setSubColumna(subColumna);
                                                   subAgrupacionColumnaList.add(subAgrupacion);
                                                   break;
                                           }  
                                       
                                                subColumna.setSubCeldaList(subCeldaList);
                                                subColumna.setSubAgrupacionColumnaList(subAgrupacionColumnaList);
                                                subColumnaList.add(subColumna);
                                    }
                                            subGrilla.setSubColumnaList(subColumnaList);                                            
                                            em.persist(subGrilla);
                                            em.merge(version);
                                }
                            }
                    }
        }
        
    }
    
    
    private void setSubCeldas(List<SubCelda> subCeldaList){
        
        
        
   }
    
    /**
     * Consolida la informacion entre las grillas desagregadas.
     * @author Manuel Gutierrez C.
     * @param estructuraList
     * @throws Exception
     */
    public void consolidarGrilla(final List<Estructura> estructuraList)throws Exception{
        
                Map<String, String> mapaSubCelda = null;
        
        for (Estructura estructura : estructuraList){
			mapaSubCelda = new HashMap<String, String>();
            for (Grilla grilla : estructura.getGrillaList()){
                for (SubGrilla subGrilla : grilla.getSubGrillaList()){
                    for (SubColumna subColumna : subGrilla.getSubColumnaList()){
                        for (SubCelda subCelda : subColumna.getSubCeldaList()){
                                
                                if (subCelda.getTipoCelda().getIdTipoCelda().equals(TipoCeldaEnum.NUMERO.getKey())){
                                    //System.out.println("subCelda: [fila, columna] = [" + subCelda.getIdSubFila() + "," + subCelda.getIdSubColumna() + "] Grilla = " + subCelda.getIdGrilla() + " SubGrilla = " + subCelda.getIdSubGrilla() + " Valor : "  + subCelda.getValor());
                                    if (subCelda.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.ENTERO.getKey())){
                                             mapaSubCelda.put(subCelda.getIdSubFila() + "-" + subCelda.getIdSubColumna() , String.valueOf( Util.getLong(mapaSubCelda.get(subCelda.getIdSubFila() + "-" + subCelda.getIdSubColumna()), 0L) + subCelda.getValorLong() ) );
                                    } else if (subCelda.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.DECIMAL.getKey())){
                                             mapaSubCelda.put(subCelda.getIdSubFila() + "-" + subCelda.getIdSubColumna(), String.valueOf( Util.getBigDecimal( mapaSubCelda.get(subCelda.getIdSubFila() + "-" + subCelda.getIdSubColumna()), new BigDecimal(0) ).add( subCelda.getValorBigDecimal() )) );
                                        }
                                }
                            }
                        }
                    
                    for (Columna columna : grilla.getColumnaList()){
                        for (Celda celda :columna.getCeldaList()){
                                if (celda.getTipoCelda().getIdTipoCelda().equals(TipoCeldaEnum.NUMERO.getKey())){
                                    celda.setValor(mapaSubCelda.get(celda.getIdFila() + "-" + celda.getIdColumna()));   
                                }
                            }
                        }
                    }
                }
            }
        
        
            for (Estructura estructura : estructuraList){
                for (Grilla grilla : estructura.getGrillaList()){
                       this.mergeEntity(grilla);
                    }
                }
    }
    
    /**
     * Obtiene el Max idSubGrilla.
     * @return
     */
    public Long getMaxIdSubGrilla() throws Exception{
        
        Query query = em.createNamedQuery(SubGrilla.FIND_MAX_ID_SUB_GRILLA);
        Long id = null;
        if (query.getResultList().size()> 0){
                id =  ((BigDecimal) query.getSingleResult()).longValue();
            }
         return id;
    }
    
    /**
     * Obtiene el Max idSubGrilla.
     * @return
     */
    public Long getMaxIdSubColumna() throws Exception{
        
            Query query = em.createNamedQuery(SubColumna.FIND_MAX_ID_SUB_COLUMNA);
            Long id = null;            
            if (query.getResultList().size() > 0){
                    id = ((BigDecimal) query.getSingleResult()).longValue();
                }
        
             return id;
    }

    
    public SubGrilla cloneSubGrilla(Grilla grid) throws Exception {
        SubGrilla gridNew = new SubGrilla();
        gridNew.setIdGrilla(grid.getIdGrilla());
        
        try {
            gridNew.setIdSubGrilla(getMaxIdSubGrilla());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        gridNew.setTitulo(grid.getTitulo());        
        gridNew.setGrilla(grid);
        gridNew.setTipoFormula(grid.getTipoFormula());  
        gridNew.setSubColumnaList(cloneSubColumnaListFromColumnaList(grid.getColumnaList(), gridNew));        
        return gridNew;
    }
    
    private List<SubColumna> cloneSubColumnaListFromColumnaList(List<Columna> columnaList, SubGrilla subGrilla) throws Exception {
        
        List<SubColumna> subColumnaList = new ArrayList<SubColumna>();
        //Sub Columna
        for (Columna columna : columnaList) {
                SubColumna subColumna = new SubColumna();
                subColumna.setAncho(columna.getAncho());
                subColumna.setIdGrilla(columna.getIdGrilla());
                subColumna.setIdSubColumna(this.getMaxIdSubColumna()); 
                subColumna.setIdSubGrilla(subGrilla.getIdSubGrilla());
                subColumna.setOrden(columna.getOrden());
                subColumna.setRowHeader(columna.isRowHeader());
                subColumna.setTituloColumna(columna.getTituloColumna());
                subColumna.setSubGrilla(subGrilla);
            
                List<SubAgrupacionColumna> subAgrupacionColumnaList = new ArrayList<SubAgrupacionColumna>();
                for (AgrupacionColumna agrupacion : columna.getAgrupacionColumnaList()){
                    SubAgrupacionColumna subAgrupacion = new SubAgrupacionColumna();
                    subAgrupacion.setAncho(agrupacion.getAncho());
                    subAgrupacion.setGrupo(agrupacion.getGrupo());
                    subAgrupacion.setIdGrilla(agrupacion.getIdGrilla());
                    subAgrupacion.setIdNivel(agrupacion.getIdNivel());                    
                    subAgrupacion.setTitulo(agrupacion.getTitulo());                     
                    subAgrupacion.setIdSubGrilla(subGrilla.getIdSubGrilla());
                    subAgrupacion.setIdSubColumna(subColumna.getIdSubColumna());
                    subAgrupacionColumnaList.add(subAgrupacion);
                }
                subColumna.setSubAgrupacionColumnaList(subAgrupacionColumnaList);
                subColumnaList.add(subColumna);
            }
        
        return subColumnaList;
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void eliminarSubGrilla(SubGrilla subGrilla) throws Exception{
        
        SubGrillaPK pk = new SubGrillaPK();
            pk.setIdGrilla(subGrilla.getIdGrilla());
            pk.setIdSubGrilla(subGrilla.getIdSubGrilla());
            subGrilla = em.find(SubGrilla.class, pk);
            em.remove(subGrilla);
        }
    
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void eliminarSubGrillas(List<SubGrilla> subGrillaList)throws Exception{
        for (SubGrilla subGrilla : subGrillaList){
                SubGrillaPK pk = new SubGrillaPK();
                    pk.setIdGrilla(subGrilla.getIdGrilla());
                    pk.setIdSubGrilla(subGrilla.getIdSubGrilla());
                subGrilla = em.find(SubGrilla.class, pk);
                em.remove(subGrilla);                
            }
        }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SubGrilla> findSubGrillasFromGrilla(Grilla grilla){
        
        
            return null;
        
        }
    
    
}
