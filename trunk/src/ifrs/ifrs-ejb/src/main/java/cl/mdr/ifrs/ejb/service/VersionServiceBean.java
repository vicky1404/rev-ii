package cl.mdr.ifrs.ejb.service;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
import static cl.mdr.ifrs.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import cl.mdr.ifrs.ejb.common.EstadoCuadroEnum;
import cl.mdr.ifrs.ejb.entity.AgrupacionColumna;
import cl.mdr.ifrs.ejb.entity.Catalogo;
import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.ifrs.ejb.entity.Columna;
import cl.mdr.ifrs.ejb.entity.EstadoCuadro;
import cl.mdr.ifrs.ejb.entity.Estructura;
import cl.mdr.ifrs.ejb.entity.Grilla;
import cl.mdr.ifrs.ejb.entity.HistorialVersion;
import cl.mdr.ifrs.ejb.entity.Html;
import cl.mdr.ifrs.ejb.entity.Periodo;
import cl.mdr.ifrs.ejb.entity.Texto;
import cl.mdr.ifrs.ejb.entity.TipoCuadro;
import cl.mdr.ifrs.ejb.entity.TipoEstructura;
import cl.mdr.ifrs.ejb.entity.Usuario;
import cl.mdr.ifrs.ejb.entity.Version;
import cl.mdr.ifrs.ejb.service.local.CeldaServiceLocal;
import cl.mdr.ifrs.ejb.service.local.EstructuraServiceLocal;
import cl.mdr.ifrs.ejb.service.local.GrillaServiceLocal;
import cl.mdr.ifrs.ejb.service.local.PeriodoServiceLocal;
import cl.mdr.ifrs.ejb.service.local.VersionServiceLocal;
import cl.mdr.ifrs.exceptions.PeriodoException;
import cl.mdr.ifrs.model.EstructuraModel;


@Stateless
public class VersionServiceBean implements VersionServiceLocal{
	private static final Logger logger = Logger.getLogger(VersionServiceBean.class);   
    
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
        historial.setComentario("CREACI�N INICIAL");
        em.persist(historial);
    }*/    
    
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistFlujoAprobacion(final List<Version> versionList, final List<HistorialVersion> historialVersionList) throws Exception{
        
        for(Version version : versionList){
            version.setFechaUltimoProceso(new Date());
            em.merge(version);
        }
        
        for(HistorialVersion historialVersion : historialVersionList){
            em.merge(historialVersion);
        }
    }
    
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Version> findVersionByFiltro(final String usuario, final TipoCuadro tipoCuadro, final Periodo periodo, final EstadoCuadro estadoCuadro, final Long vigente, final Catalogo catalogo) throws Exception{
        return em.createNamedQuery(Version.VERSION_FIND_BY_FILTRO)
        .setParameter("usuario", usuario)
        .setParameter("tipoCuadro", tipoCuadro.getIdTipoCuadro() != null ? tipoCuadro.getIdTipoCuadro() : null )     
        .setParameter("catalogo", catalogo != null ? catalogo.getIdCatalogo() : null )
        .setParameter("periodo", periodo.getIdPeriodo() != null ? periodo.getIdPeriodo() : null)
        .setParameter("estado", estadoCuadro != null ? estadoCuadro.getIdEstado() : null)
        .setParameter("vigente", vigente != null ? vigente : null)        
        .getResultList();
    }
    
    @SuppressWarnings("unchecked")
   	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Version> findVersionListActualToCompare(final List<Version> versionesModificadas){
    	final List<Long> idVersionList = extract(versionesModificadas, on(Version.class).getIdVersion());
    	return em.createQuery("select v from Version v where v.idVersion in (:idVersionList)")
    			.setParameter("idVersionList", idVersionList)
    			.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistVersion(final List<Version> versiones, final List<Estructura> estructuras, final Map<Long, EstructuraModel> estructuraModelMap, final String usuario) throws PeriodoException, Exception{
        
    	
    	Periodo periodo = periodoService.findMaxPeriodoIniciado();
        
        if (periodo == null || periodo.getIdPeriodo()==null) {
            throw new PeriodoException("El período no puede estar cerrado");
        }

        final EstadoCuadro estadoCuado = em.find(EstadoCuadro.class, EstadoCuadroEnum.INICIADO.getKey());
        
        if (estadoCuado == null) {
            throw new PeriodoException("No existe el estado del cuadro");
        }
        
    	Version versionVigente = versiones.get(versiones.size() -1);
    	
    	versionVigente.setPeriodo(periodo);
        versionVigente.setUsuario(usuario);
        versionVigente.setFechaCreacion(new Date());
        versionVigente.setFechaUltimoProceso(new Date());
        versionVigente.setEstado(estadoCuado);
        
        em.persist(versionVigente);
    	
    	for(int i=0; i<versiones.size(); i++){
            Version version = versiones.get(i);
            version = em.merge(version);
            versiones.set(i,version);
        }

        HistorialVersion historial = new HistorialVersion();
        historial.setFechaProceso(new Date());
        historial.setUsuario(new Usuario(usuario));
        historial.setVersion(versionVigente);
        historial.setEstadoCuadro(estadoCuado);
        historial.setComentario("CREACIÓN INICIAL");
        em.persist(historial);
        
        for(int i=0; i<estructuras.size(); i++){
            
            Estructura estructura = estructuras.get(i);
            estructura.setVersion(versionVigente);
            estructura = em.merge(estructura);
            estructuras.set(i,estructura);
            
            if(estructuraModelMap.containsKey(estructura.getOrden())){
                EstructuraModel estructuraModel = estructuraModelMap.get(estructura.getOrden());
                List<Columna> columnas = new ArrayList<Columna>();
                if(estructura.getTipoEstructura().getIdTipoEstructura() == TipoEstructura.ESTRUCTURA_TIPO_GRILLA){   
                	
                    Grilla grilla = new Grilla();
                    grilla.setIdGrilla(estructura.getIdEstructura());
                    grilla.setEstructura(estructura);
                    grilla.setTitulo(estructuraModel.getTituloGrilla());                                        
                    em.persist(grilla);                                                                                                      
                    for(Columna columna : estructuraModel.getColumnas()){
                        columna.setGrilla(grilla);                        
                        em.persist(columna);
                        logger.info("Insertando Columna ->" + columna.getIdColumna());
                        for(Celda celda : columna.getCeldaList()){
                        	celda.setIdGrilla(grilla.getIdGrilla());
                        	celda.setIdColumna(columna.getIdColumna());
                        	logger.info("Insertando celda col->" + celda.getIdColumna() + " fila->" + celda.getIdFila() + " grilla->" + celda.getIdGrilla());
                        	//em.persist(celda);
                        }
                        for(AgrupacionColumna agrupacionColumna : columna.getAgrupacionColumnaList()){
                        	//em.persist(agrupacionColumna);
                        }
                    }
                    
                    
                }else if(estructura.getTipoEstructura().getIdTipoEstructura() == TipoEstructura.ESTRUCTURA_TIPO_TEXTO){
                    Texto texto = estructuraModel.getTexto();
                    texto.setIdTexto(estructura.getIdEstructura());
                    texto.setEstructura(estructura);
                    texto = em.merge(texto);
                    estructura.setTexto(texto);
                }else{
                    List<Html> htmlList = new ArrayList<Html>();
                    Html html = estructuraModel.getHtml();
                    html.setIdHtml(estructura.getIdEstructura());
                    html.setEstructura(estructura);
                    html = em.merge(html);
                    htmlList.add(html);
                    estructura.setHtml(html);
                }
            }
        }
        versionVigente.setEstructuraList(estructuras);
    }
    
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Version> findVersionAll(){
        Query query = em.createNamedQuery(Version.VERSION_FIND_ALL);
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Version> findAllVersionVigente(){
        Query query = em.createNamedQuery(Version.VERSION_FIND_ALL_VIGENTE);
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Version> findVersionVigenteSinCerrar(Long idPeriodo){
        Query query = em.createNamedQuery(Version.FIND_VIGENTE_SIN_CERRAR);
        query.setParameter("idPeriodo", idPeriodo);
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Version> findAllVersionNoVigente(){
        Query query = em.createNamedQuery(Version.VERSION_FIND_ALL_NO_VIGENTE);
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Version findVersionVigente(Catalogo catalogo){
        Query query = em.createNamedQuery(Version.VERSION_FIND_NO_VIGENTE);
        query.setParameter("catalogo", catalogo);
        return (Version)query.getSingleResult();
    }
    
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Version> findVersionNoVigente(Catalogo catalogo){
        Query query = em.createNamedQuery(Version.VERSION_FIND_VIGENTE);
        query.setParameter("catalogo",catalogo);
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Version> findVersionAllByCatalogo(Catalogo catalogo){
        Query query = em.createNamedQuery(Version.VERSION_FIND_ALL_BY_CATALOGO);
        query.setParameter("catalogo",catalogo);
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Version findVersionByVersion(Version version){
        Query query = em.createNamedQuery(Version.VERSION_FIND_BY_VERSION);
        query.setParameter("version",version);
        return (Version)query.getSingleResult();
    }

    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Version> findUltimoVersionByPeriodo(final Long periodo, final String usuario, final TipoCuadro tipoCuadro, final Long vigente) throws Exception{
        Query query = em.createNamedQuery(Version.VERSION_FIND_ULTIMO_VERSION_BY_PERIODO);
        query.setParameter("periodo", periodo != null ? periodo.longValue() : "");
        query.setParameter("usuario", usuario);
        query.setParameter("tipoCuadro", tipoCuadro.getIdTipoCuadro() != null ? tipoCuadro.getIdTipoCuadro().longValue() : "" ) ; 
        query.setParameter("vigente", vigente != null ? vigente.longValue() : "" );        
        return query.getResultList();
    }
        
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Version findUltimaVersionVigente(final Long idPeriodo, final String usuario, final Long idCatalogo){
        Query query = em.createNamedQuery(Version.FIND_ULTIMA_VERSION_VIGENTE);
        query.setParameter("idPeriodo", idPeriodo);
        query.setParameter("usuario", usuario);
        query.setParameter("idCatalogo", idCatalogo);
        return (Version)query.getSingleResult();
    }
    
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Version> findVersionByCatalogoPeriodo(Long idCatalogo, Long idPeriodo){
	    Query query = em.createNamedQuery(Version.VERSION_FIND_BY_ID_CATALOGO_ID_PERIODO);
	    query.setParameter("idCatalogo",idCatalogo);
	    query.setParameter("idPeriodo",idPeriodo);
	    return query.getResultList();
    }

    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Version> findVersionAllByIdCatalogo(Long idCatalogo) {
		 Query query = em.createNamedQuery(Version.VERSION_FIND_ALL_BY_ID_CATALOGO);
	     query.setParameter("idCatalogo",idCatalogo);
	     return query.getResultList();
	}
}
