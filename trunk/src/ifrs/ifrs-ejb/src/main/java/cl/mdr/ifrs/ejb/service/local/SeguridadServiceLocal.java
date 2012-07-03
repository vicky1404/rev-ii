package cl.mdr.ifrs.ejb.service.local;


import java.util.List;

import javax.ejb.Local;

import cl.mdr.ifrs.ejb.entity.CatalogoGrupo;
import cl.mdr.ifrs.ejb.entity.Grupo;
import cl.mdr.ifrs.ejb.entity.Menu;
import cl.mdr.ifrs.ejb.entity.MenuGrupo;
import cl.mdr.ifrs.ejb.entity.Usuario;
import cl.mdr.ifrs.ejb.entity.UsuarioGrupo;


@Local
public interface SeguridadServiceLocal {

    /**
     * @return
     * @throws Exception
     */
    List<Grupo> findGruposAll()throws Exception;

    /**
     * @return
     * @throws Exception
     */
    List<String> findUsuariosDistinctAll()throws Exception; 

    /**
     * @param usuario
     * @return
     * @throws Exception
     */
    List<Grupo> findGruposByUsuario(String usuario) throws Exception;
    
    /**
     * @param userName
     * @return
     * @throws Exception
     */
    Usuario findUsuarioByUserName(final String userName)throws Exception;

    
    /**
     * @param usuarioGrupoList
     * @param grupo
     * @throws Exception
     */
    void persistUsuarioGrupo(List<UsuarioGrupo> usuarioGrupoList, Grupo grupo) throws Exception;

    /**
     * @return
     * @throws Exception
     */
    List<Menu> findMenuFindAll() throws Exception;
    
    
    /**
     * @param grupo
     * @return
     * @throws Exception
     */
    Grupo findGrupoById(Grupo grupo) throws Exception;
    
    
    /**
     * @param grupo
     * @return
     * @throws Exception
     */
    Grupo findJustGrupoById(Grupo grupo) throws Exception;


    /**
     * @param menuGrupoList
     * @param grupo
     * @throws Exception
     */
    void persistMenuGrupo(List<MenuGrupo> menuGrupoList, Grupo grupo) throws Exception;

    /**
     * @param idGrupo
     * @return
     * @throws Exception
     */
    List<MenuGrupo> findMenuAccesoByGrupo(Grupo grupo) throws Exception;

    /**
     * @param catalogoGrupoList
     * @param grupo
     * @throws Exception
     */
    void persistCatalogoGrupo(List<CatalogoGrupo> catalogoGrupoList, Grupo grupo) throws Exception;

    /**
     * @param grupoList
     * @throws Exception
     */
    void mergeGrupoList(List<Grupo> grupoList) throws Exception;
    
    
    /**
     * @param grupo
     * @throws Exception
     */
    void mergeGrupo(final Grupo grupo) throws Exception;
    
    
    /**
     * @param grupo
     * @return
     * @throws Exception
     */
    Grupo findGrupoAndCatalogoById(Grupo grupo) throws Exception;
    
    /**
     * @param grupo
     * @return
     * @throws Exception
     */
    Grupo findGrupoAndUsuariosById(Grupo grupo) throws Exception;
    
    /**
     * @param grupo
     * @return
     * @throws Exception
     */
    List<Usuario> findUsuarioByGrupoIn(Grupo grupo) throws Exception;
    
    /**
     * @param grupo
     * @return
     * @throws Exception
     */
    List<Usuario> findUsuarioByGrupoNotIn(Grupo grupo) throws Exception;
    
    /**
     * @return
     * @throws Exception
     */
    List<Usuario> findUsuarioAll() throws Exception;
    
    /**
     * @param grupoList
     * @throws Exception
     */
    void updateBloqueoGrupo(List<Grupo> grupoList) throws Exception;
        
}
