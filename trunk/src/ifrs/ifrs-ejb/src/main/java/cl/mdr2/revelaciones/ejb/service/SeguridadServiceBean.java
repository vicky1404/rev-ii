package cl.bicevida.revelaciones.ejb.service;


import static cl.bicevida.revelaciones.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;
import cl.bicevida.revelaciones.ejb.entity.CatalogoGrupo;
import cl.bicevida.revelaciones.ejb.entity.Grupo;
import cl.bicevida.revelaciones.ejb.entity.Menu;
import cl.bicevida.revelaciones.ejb.entity.MenuGrupo;
import cl.bicevida.revelaciones.ejb.entity.UsuarioGrupo;
import cl.bicevida.revelaciones.ejb.service.local.SeguridadServiceLocal;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;


@Stateless
public class SeguridadServiceBean implements SeguridadServiceLocal {
    
    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;
    
    public SeguridadServiceBean() {
        super();
    }
    
        
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Grupo> findGruposAll()throws Exception{
        return em.createNamedQuery(Grupo.FIND_ALL).getResultList();
    }
    
    
    /*usuario por grupo*/
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<String> findUsuariosDistinctAll()throws Exception{
        return em.createNamedQuery(UsuarioGrupo.FIND_USUARIOS_DISTINCT_ALL).getResultList();        
    } 
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Grupo> findGruposByUsuario(final String usuario) throws Exception{        
        return em.createNamedQuery(UsuarioGrupo.FIND_GRUPOS_BY_USUARIO)        
        .setHint(QueryHints.REFRESH, HintValues.TRUE)
        .setParameter("usuarioOid", usuario)
        .getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistUsuarioOidGrupo(final List<UsuarioGrupo> usuarioGrupoList, final String usuario) throws Exception {
        em.createNamedQuery(UsuarioGrupo.DELETE_BY_USUARIO).setParameter("usuarioOid", usuario).executeUpdate();
        for (UsuarioGrupo usuarioGrupo : usuarioGrupoList) {
            em.persist(usuarioGrupo);
        }
    }
    
    /*menu por grupo*/    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Menu> findMenuFindAll() throws Exception {
        return em.createNamedQuery(Menu.FIND_ALL).getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistMenuGrupo(final List<MenuGrupo> menuGrupoList, final Grupo grupo) throws Exception {
        em.createNamedQuery(MenuGrupo.DELETE_BY_GRUPO).setParameter("grupo", grupo).executeUpdate();
        for(MenuGrupo menuGrupo : menuGrupoList) {
            em.persist(menuGrupo);
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<MenuGrupo> findMenuAccesoByGrupo(final String idGrupo) throws Exception {
        return em.createNamedQuery(MenuGrupo.FIND_BY_GRUPO)
                 .setParameter("idGrupo", idGrupo).getResultList();
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
