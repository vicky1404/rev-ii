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
     * @param usuario
     * @throws Exception
     */
    void persistUsuarioOidGrupo(List<UsuarioGrupo> usuarioGrupoList, String usuario) throws Exception;

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
    Grupo findGrupoById(final Grupo grupo) throws Exception;


    /**
     * @param menuGrupoList
     * @param grupo
     * @throws Exception
     */
    //void persistMenuGrupo(List<MenuGrupo> menuGrupoList, Grupo grupo) throws Exception;

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
        
}
