package cl.mdr.ifrs.ejb.service;


import static cl.mdr.ifrs.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import cl.mdr.ifrs.ejb.common.Constantes;
import cl.mdr.ifrs.ejb.entity.CatalogoGrupo;
import cl.mdr.ifrs.ejb.entity.Empresa;
import cl.mdr.ifrs.ejb.entity.Grupo;
import cl.mdr.ifrs.ejb.entity.GrupoEmpresa;
import cl.mdr.ifrs.ejb.entity.Menu;
import cl.mdr.ifrs.ejb.entity.MenuGrupo;
import cl.mdr.ifrs.ejb.entity.Rol;
import cl.mdr.ifrs.ejb.entity.Usuario;
import cl.mdr.ifrs.ejb.entity.UsuarioGrupo;
import cl.mdr.ifrs.ejb.service.local.MailServiceLocal;
import cl.mdr.ifrs.ejb.service.local.SeguridadServiceLocal;


@Stateless
public class SeguridadServiceBean implements SeguridadServiceLocal {
    
    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;
    
    @EJB
    private MailServiceLocal mailService;
    
    public SeguridadServiceBean() {
        super();
    }
    
    /*Mantenedor de Usuaario*/
    
    /* (non-Javadoc)
     * @see cl.mdr.ifrs.ejb.service.local.SeguridadServiceLocal#findUsuariosByFiltro(cl.mdr.ifrs.ejb.entity.Usuario)
     */
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Usuario> findUsuariosByFiltro(final Usuario usuario, final Rol rol) throws Exception{
    	return em.createNamedQuery(Usuario.FIND_BY_FILTRO)
    		.setParameter("nombreUsuario", usuario.getNombreUsuario() != null ? MessageFormat.format("%{0}%", usuario.getNombreUsuario().toUpperCase()) : null)
    		.setParameter("nombre", usuario.getNombre() != null ? MessageFormat.format("%{0}%", usuario.getNombre().toUpperCase()) : null)
    		.setParameter("apellidoPaterno", usuario.getApellidoPaterno() != null ? MessageFormat.format("%{0}%", usuario.getApellidoPaterno().toUpperCase()) : null)
    		.setParameter("apellidoMaterno", usuario.getApellidoMaterno() != null ? MessageFormat.format("%{0}%", usuario.getApellidoMaterno().toUpperCase()) : null)
    		.setParameter("rol", rol.getIdRol())
    		.getResultList();    	
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void crearNuevoUsuario(final Usuario usuario) throws MessagingException, Exception{    	
    	em.persist(usuario);
    	mailService.sendMail("Usuario Creado", 
    						 MessageFormat.format(ResourceBundle.getBundle("exfida-mail-template").getString("exfida_nuevo_usuario_mail_template"), usuario.getNombre(), usuario.getApellidoPaterno(), usuario.getPassword()), 
    						 "rodrigo.reyes@bicevida.cl", 
    						 usuario.getEmail());
    }
    
    /* (non-Javadoc)
     * @see cl.mdr.ifrs.ejb.service.local.SeguridadServiceLocal#persistUsuario(cl.mdr.ifrs.ejb.entity.Usuario)
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistUsuario(final Usuario usuario) throws Exception{
    	em.persist(usuario);
    }
    
    /* (non-Javadoc)
     * @see cl.mdr.ifrs.ejb.service.local.SeguridadServiceLocal#mergeUsuario(cl.mdr.ifrs.ejb.entity.Usuario)
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void mergeUsuario(final Usuario usuario) throws Exception{
    	em.merge(usuario);
    }
    
        
    /* (non-Javadoc)
     * @see cl.mdr.ifrs.ejb.service.local.SeguridadServiceLocal#findGruposAll()
     */
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Grupo> findGruposAll()throws Exception{
        return em.createNamedQuery(Grupo.FIND_ALL).getResultList();
    }
    
    
    /*Usuarios por Grupo*/
    /* (non-Javadoc)
     * @see cl.mdr.ifrs.ejb.service.local.SeguridadServiceLocal#findUsuariosDistinctAll()
     */
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<String> findUsuariosDistinctAll()throws Exception{
        return em.createNamedQuery(UsuarioGrupo.FIND_USUARIOS_DISTINCT_ALL).getResultList();        
    } 
    
    /* (non-Javadoc)
     * @see cl.mdr.ifrs.ejb.service.local.SeguridadServiceLocal#findGruposByUsuario(java.lang.String)
     */
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Grupo> findGruposByUsuario(final String usuario) throws Exception{        
        return em.createNamedQuery(UsuarioGrupo.FIND_GRUPOS_BY_USUARIO)                
        .setParameter("nombreUsuario", usuario)
        .getResultList();
    }
    
    /* (non-Javadoc)
     * @see cl.mdr.ifrs.ejb.service.local.SeguridadServiceLocal#findUsuarioByUserName(java.lang.String)
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Usuario findUsuarioByUserName(final String userName)throws Exception{
    	return (Usuario) em.createQuery("select u from Usuario u " +
    									"left join fetch u.grupos g " +
    									"where u.nombreUsuario =:userName").setParameter("userName", userName).getSingleResult();
    }
    
    /* (non-Javadoc)
     * @see cl.mdr.ifrs.ejb.service.local.SeguridadServiceLocal#persistUsuarioGrupo(java.util.List, cl.mdr.ifrs.ejb.entity.Grupo)
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistUsuarioGrupo(final List<UsuarioGrupo> usuarioGrupoList, final Grupo grupo) throws Exception {
        em.createNamedQuery(UsuarioGrupo.DELETE_BY_GRUPO).setParameter("grupo", grupo).executeUpdate();
        for (UsuarioGrupo usuarioGrupo : usuarioGrupoList) {
            //em.persist(usuarioGrupo);
        	em.createNativeQuery("INSERT INTO "+Constantes.USUARIO_GRUPO+" (NOMBRE_USUARIO, ID_GRUPO_ACCESO ) VALUES(?, ? )")
								 .setParameter(1, usuarioGrupo.getUsuario().getNombreUsuario())
								 .setParameter(2, usuarioGrupo.getGrupo().getIdGrupoAcceso()).executeUpdate();
        }
    }
   
    /* (non-Javadoc)
     * @see cl.mdr.ifrs.ejb.service.local.SeguridadServiceLocal#findUsuarioByGrupoIn(cl.mdr.ifrs.ejb.entity.Grupo)
     */
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Usuario> findUsuarioByGrupoIn(final Grupo grupo) throws Exception {
    	return em.createQuery(" select u from Usuario u "+
							  " where u.nombreUsuario in " +
							  " (select ug.nombreUsuario " +
							  "	from UsuarioGrupo ug " +
							  "	where ug.grupo =:grupo)")
				.setParameter("grupo", grupo)
				.getResultList();  
			    }
    
    /* (non-Javadoc)
     * @see cl.mdr.ifrs.ejb.service.local.SeguridadServiceLocal#findUsuarioByGrupoNotIn(cl.mdr.ifrs.ejb.entity.Grupo)
     */
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Usuario> findUsuarioByGrupoNotIn(final Grupo grupo) throws Exception {
        return em.createQuery(" select u from Usuario u "+
        					  " where u.nombreUsuario not in " +
        					  " (select ug.nombreUsuario " +
        					  "	from UsuarioGrupo ug " +
        					  "	where ug.grupo =:grupo)")
        		.setParameter("grupo", grupo)
        		.getResultList(); 
    }
        
    
    /* (non-Javadoc)
     * @see cl.mdr.ifrs.ejb.service.local.SeguridadServiceLocal#findUsuarioAll()
     */
    @SuppressWarnings("unchecked")
   	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Usuario> findUsuarioAll() throws Exception {
        return em.createQuery("from Usuario u order by u.nombreUsuario").getResultList(); 
    }
    
    /*menu por grupo*/
	/* (non-Javadoc)
	 * @see cl.mdr.ifrs.ejb.service.local.SeguridadServiceLocal#findGrupoById(cl.mdr.ifrs.ejb.entity.Grupo)
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Grupo findGrupoById(final Grupo grupo) throws Exception {
        return (Grupo) em.createQuery("select g from Grupo g left join fetch g.menus where g =:grupo").setParameter("grupo", grupo).getSingleResult();
    }
    
	/* (non-Javadoc)
	 * @see cl.mdr.ifrs.ejb.service.local.SeguridadServiceLocal#findJustGrupoById(cl.mdr.ifrs.ejb.entity.Grupo)
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Grupo findJustGrupoById(final Grupo grupo) throws Exception {
        return (Grupo) em.find(Grupo.class, grupo.getIdGrupoAcceso());
    }
    
    
    /* (non-Javadoc)
     * @see cl.mdr.ifrs.ejb.service.local.SeguridadServiceLocal#findMenuFindAll()
     */
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Menu> findMenuFindAll() throws Exception {
        return em.createNamedQuery(Menu.FIND_ALL).getResultList();
    }
    
    
    /* (non-Javadoc)
     * @see cl.mdr.ifrs.ejb.service.local.SeguridadServiceLocal#persistMenuGrupo(java.util.List, cl.mdr.ifrs.ejb.entity.Grupo)
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistMenuGrupo(final List<MenuGrupo> menuGrupoList, final Grupo grupo) throws Exception {
        em.createNamedQuery(MenuGrupo.DELETE_BY_GRUPO).setParameter("grupo", grupo).executeUpdate();
        for(MenuGrupo menuGrupo : menuGrupoList) {
            em.merge(menuGrupo);
        }
    }
    
    /* (non-Javadoc)
     * @see cl.mdr.ifrs.ejb.service.local.SeguridadServiceLocal#findMenuAccesoByGrupo(cl.mdr.ifrs.ejb.entity.Grupo)
     */
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<MenuGrupo> findMenuAccesoByGrupo(final Grupo grupo) throws Exception {
        return em.createNamedQuery(MenuGrupo.FIND_BY_GRUPO)
                 .setParameter("grupo", grupo).getResultList(); 
    }
    
    /*estructuras por grupo*/
    /* (non-Javadoc)
     * @see cl.mdr.ifrs.ejb.service.local.SeguridadServiceLocal#persistCatalogoGrupo(java.util.List, cl.mdr.ifrs.ejb.entity.Grupo)
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistCatalogoGrupo(final List<CatalogoGrupo> catalogoGrupoList, final Grupo grupo) throws Exception {
        em.createNamedQuery(CatalogoGrupo.DELETE_BY_GRUPO).setParameter("grupo", grupo).executeUpdate();
        for(CatalogoGrupo catalogoGrupo : catalogoGrupoList) {
            em.persist(catalogoGrupo);
        }
    }
        
	/* (non-Javadoc)
	 * @see cl.mdr.ifrs.ejb.service.local.SeguridadServiceLocal#findGrupoAndCatalogoById(cl.mdr.ifrs.ejb.entity.Grupo)
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Grupo findGrupoAndCatalogoById(final Grupo grupo) throws Exception {
        return (Grupo) em.createQuery("select g from Grupo g left join fetch g.catalogos where g =:grupo").setParameter("grupo", grupo).getSingleResult();
    }
    
    
	/* (non-Javadoc)
	 * @see cl.mdr.ifrs.ejb.service.local.SeguridadServiceLocal#findGrupoAndUsuariosById(cl.mdr.ifrs.ejb.entity.Grupo)
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Grupo findGrupoAndUsuariosById(final Grupo grupo) throws Exception {
        return (Grupo) em.createQuery("select g from Grupo g left join fetch g.usuarios where g =:grupo").setParameter("grupo", grupo).getSingleResult();
    }
    
    /*grupo*/
    /* (non-Javadoc)
     * @see cl.mdr.ifrs.ejb.service.local.SeguridadServiceLocal#mergeGrupoList(java.util.List)
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void mergeGrupoList(final List<Grupo> grupoList) throws Exception {        
        for(final Grupo grupo : grupoList) {
            em.merge(grupo);
        }
    }
    
    /* (non-Javadoc)
     * @see cl.mdr.ifrs.ejb.service.local.SeguridadServiceLocal#mergeGrupo(cl.mdr.ifrs.ejb.entity.Grupo)
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void mergeGrupo(final Grupo grupo) throws Exception {               
        em.merge(grupo);        
    }
    
    /*Bloqueo por Grupo*/
    /* (non-Javadoc)
     * @see cl.mdr.ifrs.ejb.service.local.SeguridadServiceLocal#updateBloqueoGrupo(java.util.List)
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void updateBloqueoGrupo(final List<Grupo> grupoList) throws Exception {        
        for(Grupo grupo : grupoList) {
            em.createQuery("UPDATE Grupo g set g.accesoBloqueado = ? WHERE g.idGrupoAcceso = ?")
            				.setParameter(1, grupo.getAccesoBloqueado())
            				.setParameter(2, grupo.getIdGrupoAcceso())
            				.executeUpdate();
        }
    }
    
    /* (non-Javadoc)
     * @see cl.mdr.ifrs.ejb.service.local.SeguridadServiceLocal#authenticateUser(java.lang.String)
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Usuario authenticateUser(final String nombreUsuario) throws Exception, NoResultException{
    	return (Usuario) em.createNamedQuery(Usuario.AUTHENTICATE_USER)
    							  .setParameter("nombreUsuario", nombreUsuario)    							  
    							  .getSingleResult();
    }
    
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String validaUsuarioExiste(final String nombreUsuario) throws Exception{
    	List<String> usuarios = em.createQuery("select nombreUsuario from Usuario where nombreUsuario = :nombreUsuario")
				  .setParameter("nombreUsuario", nombreUsuario)    							  
				  .getResultList();
    	if(!usuarios.isEmpty()){
    		return usuarios.get(0);
    	}
    	return null;
    }
    
    //Grupos por empresas   
    
    /* (non-Javadoc)
     * @see cl.mdr.ifrs.ejb.service.local.SeguridadServiceLocal#findGrupoByEmpresaIn(cl.mdr.ifrs.ejb.entity.Empresa)
     */
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Grupo> findGrupoByEmpresaIn(final Empresa empresa) throws Exception {
    	return em.createQuery(" select g from Grupo g "+
							  " where g.idGrupoAcceso in " +
							  " (select ge.idGrupoAcceso " +
							  "	from GrupoEmpresa ge " +
							  "	where ge.empresa =:empresa) " +
							  " order by g.areaNegocio asc, g.nombre asc")
				.setParameter("empresa", empresa)
				.getResultList();
    }
    
    
    /* (non-Javadoc)
     * @see cl.mdr.ifrs.ejb.service.local.SeguridadServiceLocal#findGrupoByEmpresaNotIn(cl.mdr.ifrs.ejb.entity.Empresa)
     */
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Grupo> findGrupoByEmpresaNotIn(final Empresa empresa) throws Exception {
        return em.createQuery(" select g from Grupo g "+
        					  " where g.idGrupoAcceso not in " +
        					  " (select ge.idGrupoAcceso " +
        					  "	from GrupoEmpresa ge " +
        					  "	where ge.empresa =:empresa) " +
        					  " order by g.areaNegocio asc, g.nombre asc")
        		.setParameter("empresa", empresa)
        		.getResultList(); 
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistGrupoEmpresa(final List<GrupoEmpresa> grupoEmpresaList, final Empresa empresa) throws Exception {
        //em.createQuery("delete from GrupoEmpresa ge where ge.idRut =:rut").setParameter("rut", empresa.getIdRut()).executeUpdate();
    	em.createQuery("delete from GrupoEmpresa ge where ge.empresa.idRut =:rut").setParameter("rut", empresa.getIdRut()).executeUpdate();
        for (GrupoEmpresa grupoEmpresa : grupoEmpresaList) {            
        	em.createNativeQuery("INSERT INTO "+Constantes.GRUPO_EMPRESA+" (ID_GRUPO_ACCESO, ID_RUT) VALUES(?,?)")
								 .setParameter(1, grupoEmpresa.getGrupo().getIdGrupoAcceso())
								 .setParameter(2, grupoEmpresa.getEmpresa().getIdRut()).executeUpdate();
        }
    }
    
       
    /*Retorna los usuario que estan asociados a un catalogo*/
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<UsuarioGrupo> getUsuarioGrupoByCatalogo(Long idCatalogo) throws Exception {        
        Query query = em.createNamedQuery(UsuarioGrupo.FIND_BY_ID_CATALOGO);
        query.setParameter("idCatalogo", idCatalogo);
        return query.getResultList();
    }
    
    public void deleteCatalogoGrupo(Long idCatalogo, String idGrupoAcceso) throws Exception{
    	em.createNativeQuery("DELETE FROM " + Constantes.CATALOGO_GRUPO + " WHERE ID_CATALOGO = ? AND ID_GRUPO_ACCESO = ? ")
    	  .setParameter(1, idCatalogo)
    	  .setParameter(2, idGrupoAcceso)
    	  .executeUpdate();
    }
    
    public void persistCatalogoGrupo(Long idCatalogo, String idGrupoAcceso) throws Exception{
    	
    	BigDecimal result = (BigDecimal)
    	em.createNativeQuery("SELECT COUNT(*) FROM " + Constantes.CATALOGO_GRUPO + " WHERE ID_CATALOGO = ? AND ID_GRUPO_ACCESO = ? ")
    	  .setParameter(1, idCatalogo)
    	  .setParameter(2, idGrupoAcceso)
    	  .getSingleResult();
    	
    	if(result==null || result.equals(new BigDecimal(0))){
	    	em.createNativeQuery("INSERT INTO " + Constantes.CATALOGO_GRUPO + " ( ID_CATALOGO, ID_GRUPO_ACCESO)  VALUES (?,?)")
	    	  .setParameter(1, idCatalogo)
	    	  .setParameter(2, idGrupoAcceso)
	    	  .executeUpdate();
    	}
    }
    
}
