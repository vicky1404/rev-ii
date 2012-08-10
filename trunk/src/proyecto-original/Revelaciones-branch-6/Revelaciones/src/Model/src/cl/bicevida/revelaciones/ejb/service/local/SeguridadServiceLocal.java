package cl.bicevida.revelaciones.ejb.service.local;


import cl.bicevida.revelaciones.ejb.entity.CatalogoGrupo;
import cl.bicevida.revelaciones.ejb.entity.Grupo;
import cl.bicevida.revelaciones.ejb.entity.Menu;
import cl.bicevida.revelaciones.ejb.entity.MenuGrupo;
import cl.bicevida.revelaciones.ejb.entity.UsuarioGrupo;

import java.util.List;

import javax.ejb.Local;


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
    List<MenuGrupo> findMenuAccesoByGrupo(String idGrupo) throws Exception;

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
