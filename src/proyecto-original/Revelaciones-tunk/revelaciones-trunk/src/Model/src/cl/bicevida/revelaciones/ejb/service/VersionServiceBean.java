package cl.bicevida.revelaciones.ejb.service;


import cl.bicevida.revelaciones.ejb.common.EstadoCuadroEnum;
import static cl.bicevida.revelaciones.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;
import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.revelaciones.ejb.entity.AgrupacionColumna;
import cl.bicevida.revelaciones.ejb.entity.Catalogo;
import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.Columna;
import cl.bicevida.revelaciones.ejb.entity.EstadoCuadro;
import cl.bicevida.revelaciones.ejb.entity.EstadoFinanciero;
import cl.bicevida.revelaciones.ejb.entity.Estructura;
import cl.bicevida.revelaciones.ejb.entity.Grilla;
import cl.bicevida.revelaciones.ejb.entity.HistorialVersionPeriodo;
import cl.bicevida.revelaciones.ejb.entity.Html;
import cl.bicevida.revelaciones.ejb.entity.Periodo;
import cl.bicevida.revelaciones.ejb.entity.RelacionDetalleEeff;
import cl.bicevida.revelaciones.ejb.entity.RelacionEeff;
import cl.bicevida.revelaciones.ejb.entity.Texto;
import cl.bicevida.revelaciones.ejb.entity.TipoCuadro;
import cl.bicevida.revelaciones.ejb.entity.TipoEstructura;
import cl.bicevida.revelaciones.ejb.entity.Version;
import cl.bicevida.revelaciones.ejb.entity.VersionPeriodo;
import cl.bicevida.revelaciones.ejb.service.local.CeldaServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.EstadoFinancieroServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.EstructuraServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.GrillaServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.PeriodoServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.VersionServiceLocal;
import cl.bicevida.revelaciones.exceptions.PeriodoException;
import cl.bicevida.revelaciones.vo.GrillaModelVO;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import javax.ejb.EJB;
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
public class VersionServiceBean implements VersionServiceLocal{
    
    @Resource
    SessionContext sessionContext;
    
    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;
    
    @EJB
    private EstructuraServiceLocal estructuraService;
    
    @EJB
    private CeldaServiceLocal celdaService;
    
    @EJB
    private GrillaServiceLocal grillaService;
    
    @EJB
    private PeriodoServiceLocal periodoService;
    
    @EJB
    private EstadoFinancieroServiceLocal eeffSerivce;

    
    public VersionServiceBean() {
    }
    
    public Object mergeEntity(Object entity){
        return em.merge(entity);
    }

    public Object persistEntity(Object entity){
        em.persist(entity);
        return entity;
    }
    
    /*@TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void borrarCeldas(List<Estructura> estructuras, Map<Long, GrillaModelVO> grillaModelMap)throws Exception{

        for(Estructura estructura : estructuras){
            if(grillaModelMap.containsKey(estructura.getOrden())){
                GrillaModelVO grillaModel = grillaModelMap.get(estructura.getOrden());
                if(estructura.getTipoEstructura().getIdTipoEstructura() == TipoEstructura.ESTRUCTURA_TIPO_GRILLA){
                    System.out.println("Borrar");
                    if(estructura.getIdEstructura()!=null){
                        Grilla grillaBorrar = getGrillaService().findGrillaById(estructura.getIdEstructura());
                        if(grillaBorrar!=null)
                            em.remove(grillaBorrar);
                        Estructura estructuraBorrar = getEstructuraService().findEstructuraById(estructura.getIdEstructura());
                        if(estructuraBorrar!=null)
                            em.remove(estructuraBorrar);
                    }
                }
            }
        }

    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistVersion(List<Version> versiones, List<Estructura> estructuras, Map<Long, GrillaModelVO> grillaModelMap) throws Exception{


            for(int i=0; i<versiones.size(); i++){
                Version version; ;
                version = em.merge(versiones.get(i));
                versiones.set(i, version);
                System.out.println(version.getIdVersion());
            }

            if(estructuras==null)
                return;

            int ultimaVersion = versiones.size() -1;

            Version versionVigente = versiones.get(ultimaVersion);

            for(int i=0; i<estructuras.size(); i++){

                Estructura estructura = estructuras.get(i);

                if(grillaModelMap.containsKey(estructuras.get(i).getOrden())){

                    GrillaModelVO grillaModel = grillaModelMap.get(estructura.getOrden());
                    if(estructura.getTipoEstructura().getIdTipoEstructura() == TipoEstructura.ESTRUCTURA_TIPO_GRILLA){


                        estructura.setVersion(versionVigente);
                        Estructura estructuraNew = new Estructura();
                        estructuraNew.setGrillaList(estructura.getGrillaList());
                        estructuraNew.setHtmlList(estructura.getHtmlList());
                        estructuraNew.setOrden(estructura.getOrden());
                        estructuraNew.setTextoList(estructura.getTextoList());
                        estructuraNew.setTipoEstructura(estructura.getTipoEstructura());
                        estructuraNew.setVersion(estructura.getVersion());

                        estructuraNew = em.merge(estructuraNew);

                        estructuras.set(i, estructuraNew);

                        List<Grilla>  grillaList = new ArrayList<Grilla>();
                        Grilla grilla = new Grilla();
                        grilla.setIdGrilla(estructuraNew.getIdEstructura());
                        grilla.setEstructura1(estructuraNew);
                        grilla.setTitulo(grillaModel.getTituloGrilla());
                        for(Columna columna : grillaModel.getColumnas()){
                            columna.setGrilla(grilla);
                            columna.setIdGrilla(estructuraNew.getIdEstructura());
                            for(Celda celda : columna.getCeldaList()){
                                celda.setIdColumna(columna.getIdColumna());
                                celda.setIdGrilla(estructuraNew.getIdEstructura());
                                celda.setGrupo(celda.getIdFila());
                                celda.setColumna(columna);
                            }
                        }
                        grilla.setColumnaList(grillaModel.getColumnas());
                        em.persist(grilla);
                        grillaList.add(grilla);
                        estructura.setGrillaList(grillaList);
                    }else if(estructura.getTipoEstructura().getIdTipoEstructura() == TipoEstructura.ESTRUCTURA_TIPO_TEXTO){
                        em.merge(estructura);
                        List<Texto> textoList = new ArrayList<Texto>();
                        Texto texto = grillaModel.getTexto();
                        texto.setIdTexto(estructura.getIdEstructura());
                        texto.setEstructura2(estructura);
                        texto = em.merge(texto);
                        textoList.add(texto);
                        estructura.setTextoList(textoList);
                    }else{
                        em.merge(estructura);
                        List<Html> htmlList = new ArrayList<Html>();
                        Html html = grillaModel.getHtml();
                        html.setIdHtml(estructura.getIdEstructura());
                        html.setEstructura(estructura);
                        html = em.merge(html);
                        htmlList.add(html);
                        estructura.setHtmlList(htmlList);
                    }
                }
            }
            versionVigente.setEstructuraList(estructuras);

        }
    */

    /*@TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistVersionPeriodoVigente(final Version version, final Periodo periodo, final String usuario) throws PeriodoException {

        if (periodo == null || periodo.getIdPeriodo()==null) {
            throw new PeriodoException("El periodo no puede estar cerrado");
        }

        final EstadoCuadro estadoCuado = em.find(EstadoCuadro.class, EstadoCuadroEnum.INICIADO.getKey());
        
        if (estadoCuado == null) {
            throw new PeriodoException("No existe el estado del cuadro");
        }
        
        VersionPeriodo versionPeriodo = new VersionPeriodo();
        versionPeriodo.setVersion(version);
        versionPeriodo.setPeriodo(periodo);
        versionPeriodo.setUsuario(usuario);
        versionPeriodo.setFechaCreacion(new Date());
        versionPeriodo.setFechaUltimoProceso(new Date());
        versionPeriodo.setEstado(estadoCuado);
        versionPeriodo = em.merge(versionPeriodo);

        HistorialVersionPeriodo historial = new HistorialVersionPeriodo();
        historial.setFechaProceso(new Date());
        historial.setUsuario(usuario);
        historial.setVersionPeriodo(versionPeriodo);
        historial.setEstadoCuadro(estadoCuado);
        historial.setComentario("CREACIÓN INICIAL");
        em.persist(historial);
    }*/    

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistVersion(List<Version> versiones, List<Estructura> estructuras, Map<Long, GrillaModelVO> grillaModelMap, String usuario) throws PeriodoException, Exception{
        
        for(int i=0; i<versiones.size(); i++){
            Version version = versiones.get(i);
            version = em.merge(version);
            versiones.set(i,version);
        }
        
        Version versionVigente = versiones.get(versiones.size() -1);
                                                                    
        Periodo periodo = periodoService.findMaxPeriodoIniciado();
        
        if (periodo == null || periodo.getIdPeriodo()==null) {
            throw new PeriodoException("El periodo no puede estar cerrado");
        }

        final EstadoCuadro estadoCuado = em.find(EstadoCuadro.class, EstadoCuadroEnum.INICIADO.getKey());
        
        if (estadoCuado == null) {
            throw new PeriodoException("No existe el estado del cuadro");
        }
        
        VersionPeriodo versionPeriodo = new VersionPeriodo();
        versionPeriodo.setVersion(versionVigente);
        versionPeriodo.setPeriodo(periodo);
        versionPeriodo.setUsuario(usuario);
        versionPeriodo.setFechaCreacion(new Date());
        versionPeriodo.setFechaUltimoProceso(new Date());
        versionPeriodo.setEstado(estadoCuado);
        versionPeriodo = em.merge(versionPeriodo);

        HistorialVersionPeriodo historial = new HistorialVersionPeriodo();
        historial.setFechaProceso(new Date());
        historial.setUsuario(usuario);
        historial.setVersionPeriodo(versionPeriodo);
        historial.setEstadoCuadro(estadoCuado);
        historial.setComentario("CREACIÓN INICIAL");
        em.persist(historial);
        
        for(int i=0; i<estructuras.size(); i++){
            
            Estructura estructura = estructuras.get(i);
            estructura.setVersion(versionVigente);
            estructura = em.merge(estructura);
            estructuras.set(i,estructura);
            
            if(grillaModelMap.containsKey(estructura.getOrden())){
                GrillaModelVO grillaModel = grillaModelMap.get(estructura.getOrden());
                if(estructura.getTipoEstructura().getIdTipoEstructura() == TipoEstructura.ESTRUCTURA_TIPO_GRILLA){
                    
                    eeffSerivce.deleteAllRelacionByGrillaPeriodo(versionPeriodo.getIdPeriodo(), estructura.getIdEstructura());
                    
                    List<Grilla>  grillaList = new ArrayList<Grilla>();
                    Grilla grilla = new Grilla();
                    grilla.setIdGrilla(estructura.getIdEstructura());
                    grilla.setEstructura1(estructura);
                    grilla.setTitulo(grillaModel.getTituloGrilla());
                    
                    //System.out.println("Borrando Grilla -> " + grilla.getIdGrilla());
                    int returnDelete = em.createQuery("delete from Celda c where c.idGrilla = :idGrilla").setParameter("idGrilla", grilla.getIdGrilla()).executeUpdate();
                    if(returnDelete > 0){
                    
                        em.createQuery("update Grilla g set g.titulo = :titulo where g.idGrilla = :idGrilla")
                        .setParameter("titulo", grilla.getTitulo())
                        .setParameter("idGrilla", grilla.getIdGrilla()).executeUpdate();
                            
                        for(Columna columna : grillaModel.getColumnas()){
                            columna.setGrilla(grilla);
                            //System.out.println("Insertando celda");
                            columna.setIdGrilla(estructura.getIdEstructura());
                            for(Celda celda : columna.getCeldaList()){
                                celda.setIdColumna(columna.getIdColumna());
                                celda.setIdGrilla(estructura.getIdEstructura());
                                celda.setColumna(columna);
                                //System.out.println("A idGrilla -> " + celda.getIdGrilla()  + " idColumna -> " + celda.getIdColumna() + " idFila -> " + celda.getIdFila());
                                if(Util.esListaValida(celda.getRelacionEeffList())){
                                    for(RelacionEeff relEeff : celda.getRelacionEeffList()){
                                        relEeff.setIdGrilla(celda.getIdGrilla());
                                        relEeff.setIdColumna(celda.getIdColumna());
                                        relEeff.setIdFila(celda.getIdFila());
                                    }
                                }
                                if(Util.esListaValida(celda.getRelacionDetalleEeffList())){
                                    for(RelacionDetalleEeff relDetEeff : celda.getRelacionDetalleEeffList()){
                                        relDetEeff.setIdGrilla(celda.getIdGrilla());
                                        relDetEeff.setIdColumna(celda.getIdColumna());
                                        relDetEeff.setIdFila(celda.getIdFila());
                                    }
                                }
                                em.persist(celda);
                            }
                        }
                    }else{
                        for(Columna columna : grillaModel.getColumnas()){
                            columna.setGrilla(grilla);
                            columna.setIdGrilla(estructura.getIdEstructura());
                            columna.setAgrupacionColumnaList(new ArrayList<AgrupacionColumna>());
                            Iterator it = grillaModel.getAgrupacionesMap().entrySet().iterator();
                            while(it.hasNext()){
                                Map.Entry entry = (Map.Entry)it.next();
                                List<AgrupacionColumna> agrupaciones = (List<AgrupacionColumna>) entry.getValue();
                                for(AgrupacionColumna agrupacion : agrupaciones){
                                    if(columna.getIdColumna().equals(agrupacion.getIdColumna())){
                                        agrupacion.setIdGrilla(estructura.getIdEstructura());
                                        agrupacion.setColumna(columna);
                                        columna.getAgrupacionColumnaList().add(agrupacion);
                                    }
                                }
                            }
                            for(Celda celda : columna.getCeldaList()){
                                celda.setIdColumna(columna.getIdColumna());
                                celda.setIdGrilla(estructura.getIdEstructura());
                                celda.setColumna(columna);
                                //System.out.println("B idGrilla -> " + celda.getIdGrilla()  + " idColumna -> " + celda.getIdColumna() + " idFila -> " + celda.getIdFila());
                            }
                        }
                        grilla.setColumnaList(grillaModel.getColumnas());                    
                        grilla = em.merge(grilla);
                        grillaList.add(grilla);
                        estructura.setGrillaList(grillaList);
                    }
                    
                }else if(estructura.getTipoEstructura().getIdTipoEstructura() == TipoEstructura.ESTRUCTURA_TIPO_TEXTO){
                    List<Texto> textoList = new ArrayList<Texto>();
                    Texto texto = grillaModel.getTexto();
                    texto.setIdTexto(estructura.getIdEstructura());
                    texto.setEstructura2(estructura);
                    texto = em.merge(texto);
                    textoList.add(texto);
                    estructura.setTextoList(textoList);
                }else{
                    List<Html> htmlList = new ArrayList<Html>();
                    Html html = grillaModel.getHtml();
                    html.setIdHtml(estructura.getIdEstructura());
                    html.setEstructura(estructura);
                    html = em.merge(html);
                    htmlList.add(html);
                    estructura.setHtmlList(htmlList);
                }
            }
        }
        versionVigente.setEstructuraList(estructuras);
    }
    
    public List<Version> findVersionAll(){
        Query query = em.createNamedQuery(Version.VERSION_FIND_ALL);
        return query.getResultList();
    }
    
    public List<Version> findAllVersionVigente(){
        Query query = em.createNamedQuery(Version.VERSION_FIND_ALL_VIGENTE);
        return query.getResultList();
    }
    
    public List<Version> findAllVersionNoVigente(){
        Query query = em.createNamedQuery(Version.VERSION_FIND_ALL_NO_VIGENTE);
        return query.getResultList();
    }
    
    public Version findVersionVigente(Catalogo catalogo){
        Query query = em.createNamedQuery(Version.VERSION_FIND_VIGENTE);
        query.setParameter("catalogo", catalogo);
        return (Version)query.getSingleResult();
    }
    
    public List<Version> findVersionNoVigente(Catalogo catalogo){
        Query query = em.createNamedQuery(Version.VERSION_FIND_NO_VIGENTE);
        query.setParameter("catalogo",catalogo);
        return query.getResultList();
    }
    
    public List<Version> findVersionAllByCatalogo(Catalogo catalogo){
        Query query = em.createNamedQuery(Version.VERSION_FIND_ALL_BY_CATALOGO);
        query.setParameter("catalogo",catalogo);
        return query.getResultList();
    }
    
    public Version findVersionByVersion(Version version){
        Query query = em.createNamedQuery(Version.VERSION_FIND_BY_VERSION);
        query.setParameter("version",version);
        return (Version)query.getSingleResult();
    }

    public List<Version> findUltimoVersionByPeriodo(final Long periodo, final String usuario, final TipoCuadro tipoCuadro, final Long vigente) throws Exception{
        Query query = em.createNamedQuery(Version.VERSION_FIND_ULTIMO_VERSION_BY_PERIODO);
        query.setParameter("periodo", periodo);
        query.setParameter("usuario", usuario);
        query.setParameter("tipoCuadro", tipoCuadro != null ? tipoCuadro.getIdTipoCuadro() : null ) ; 
        query.setParameter("vigente", vigente);
        query.setHint(QueryHints.REFRESH, HintValues.TRUE);
        return query.getResultList();
    }
    
    public Version findUltimaVersionVigente(final Long idPeriodo, final String usuario, final Long idCatalogo){
        Query query = em.createNamedQuery(Version.FIND_ULTIMA_VERSION_VIGENTE);
        query.setParameter("idPeriodo", idPeriodo);
        query.setParameter("usuario", usuario);
        query.setParameter("idCatalogo", idCatalogo);
        return (Version)query.getSingleResult();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Version findVersionByIdEstructura(Long idEstructura)throws Exception{
        Query query = em.createNamedQuery(Version.VERSION_FIND_BY_ID_ESTRUCTURA);
        query.setParameter("idEstructura", idEstructura);
        return (Version)query.getSingleResult();
    }
    
    
    
    
    public EstructuraServiceLocal getEstructuraService() {
        return estructuraService;
    }

    public CeldaServiceLocal getCeldaService() {
        return celdaService;
    }

    public GrillaServiceLocal getGrillaService() {
        return grillaService;
    }
}
