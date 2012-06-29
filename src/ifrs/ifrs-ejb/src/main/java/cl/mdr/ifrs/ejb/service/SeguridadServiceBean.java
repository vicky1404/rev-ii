package cl.mdr.ifrs.ejb.service;


import static cl.mdr.ifrs.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cl.mdr.ifrs.ejb.entity.CatalogoGrupo;
import cl.mdr.ifrs.ejb.entity.Grupo;
import cl.mdr.ifrs.ejb.entity.Menu;
import cl.mdr.ifrs.ejb.entity.MenuGrupo;
import cl.mdr.ifrs.ejb.entity.Usuario;
import cl.mdr.ifrs.ejb.entity.UsuarioGrupo;
import cl.mdr.ifrs.ejb.service.local.SeguridadServiceLocal;


@Stateless
public class SeguridadServiceBean implements SeguridadServiceLocal {
    
    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;
    
    public SeguridadServiceBean() {
        super();
    }
    
        
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Grupo> findGruposAll()throws Exception{
        return em.createNamedQuery(Grupo.FIND_ALL).getResultList();
    }
    
    
    /*usuario por grupo*/
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<String> findUsuariosDistinctAll()throws Exception{
        return em.createNamedQuery(UsuarioGrupo.FIND_USUARIOS_DISTINCT_ALL).getResultList();        
    } 
    
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Grupo> findGruposByUsuario(final String usuario) throws Exception{        
        return em.createNamedQuery(UsuarioGrupo.FIND_GRUPOS_BY_USUARIO)                
        .setParameter("usuarioOid", usuario)
        .getResultList();
    }
    
    public Usuario findUsuarioByUserName(final String userName)throws Exception{
    	return (Usuario) em.createQuery("select u from Usuario u " +
    									"left join fetch u.grupos g " +
    									"where u.nombreUsuario =:userName").setParameter("userName", userName).getSingleResult();
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistUsuarioOidGrupo(final List<UsuarioGrupo> usuarioGrupoList, final String usuario) throws Exception {
        em.createNamedQuery(UsuarioGrupo.DELETE_BY_USUARIO).setParameter("usuarioOid", usuario).executeUpdate();
        for (UsuarioGrupo usuarioGrupo : usuarioGrupoList) {
            em.persist(usuarioGrupo);
        }
    }
    
    /*menu por grupo*/
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Grupo findGrupoById(final Grupo grupo) throws Exception {
        return (Grupo) em.createQuery("select g from Grupo g left join fetch g.menus where g =:grupo").setParameter("grupo", grupo).getSingleResult();
    }
    
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Grupo findJustGrupoById(final Grupo grupo) throws Exception {
        return (Grupo) em.find(Grupo.class, grupo.getIdGrupoAcceso());
    }
    
    
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Menu> findMenuFindAll() throws Exception {
        return em.createNamedQuery(Menu.FIND_ALL).getResultList();
    }
    
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistMenuGrupo(final List<MenuGrupo> menuGrupoList, final Grupo grupo) throws Exception {
        em.createNamedQuery(MenuGrupo.DELETE_BY_GRUPO).setParameter("grupo", grupo).executeUpdate();
        for(MenuGrupo menuGrupo : menuGrupoList) {
            em.merge(menuGrupo);
        }
    }
    
    @SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<MenuGrupo> findMenuAccesoByGrupo(final Grupo grupo) throws Exception {
        return em.createNamedQuery(MenuGrupo.FIND_BY_GRUPO)
                 .setParameter("grupo", grupo).getResultList(); 
    }
    
    /*estructuras por grupo*/
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistCatalogoGrupo(final List<CatalogoGrupo> catalogoGrupoList, final Grupo grupo) throws Exception {
        em.createNamedQuery(CatalogoGrupo.DELETE_BY_GRUPO).setParameter("grupo", grupo).executeUpdate();
        for(CatalogoGrupo catalogoGrupo : catalogoGrupoList) {
            em.persist(catalogoGrupo);
        }
    }
    
    /*grupo*/
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void mergeGrupoList(final List<Grupo> grupoList) throws Exception {        
        for(Grupo grupo : grupoList) {
            em.merge(grupo);
        }
    }
    
        
    
    
    


}
