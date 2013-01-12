package cl.mdr.ifrs.ejb.service.local;


import java.util.List;

import javax.ejb.Local;
import javax.mail.MessagingException;
import javax.persistence.NoResultException;

import cl.mdr.ifrs.ejb.entity.CatalogoGrupo;
import cl.mdr.ifrs.ejb.entity.Empresa;
import cl.mdr.ifrs.ejb.entity.Grupo;
import cl.mdr.ifrs.ejb.entity.GrupoEmpresa;
import cl.mdr.ifrs.ejb.entity.Menu;
import cl.mdr.ifrs.ejb.entity.MenuGrupo;
import cl.mdr.ifrs.ejb.entity.Rol;
import cl.mdr.ifrs.ejb.entity.Usuario;
import cl.mdr.ifrs.ejb.entity.UsuarioGrupo;


@Local
public interface SeguridadServiceLocal {

	/**
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	List<Usuario> findUsuariosByFiltro(Usuario usuario , Rol rol) throws Exception;
	
	/**
	 * @param usuario
	 * @throws MessagingException
	 * @throws Exception
	 */
	void crearNuevoUsuario(Usuario usuario) throws MessagingException, Exception;
	
    /**
     * @param usuario
     * @throws Exception
     */
    void persistUsuario(Usuario usuario) throws Exception;
    
    
    /**
     * @param usuario
     * @throws Exception
     */
    void mergeUsuario(Usuario usuario) throws Exception;
	
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
    
    /**
     * @param nombreUsuario
     * @return
     * @throws Exception
     */
    Usuario authenticateUser(String nombreUsuario) throws Exception, NoResultException;
    
    /**
     * @param nombreUsuario
     * @return
     * @throws Exception
     */
    String validaUsuarioExiste(String nombreUsuario) throws Exception;
    
    /**
     * @param empresa
     * @return
     * @throws Exception
     */
    List<Grupo> findGrupoByEmpresaIn(Empresa empresa) throws Exception;
    
    /**
     * @param empresa
     * @return
     * @throws Exception
     */
    List<Grupo> findGrupoByEmpresaNotIn(Empresa empresa) throws Exception;
    
    /**
     * @param grupoEmpresaList
     * @param empresa
     * @throws Exception
     */
    void persistGrupoEmpresa(List<GrupoEmpresa> grupoEmpresaList, Empresa empresa) throws Exception;
    
    /**
     * 
     * @param idCatalogo
     * @return
     * @throws Exception
     */
    List<UsuarioGrupo> getUsuarioGrupoByCatalogo(Long idCatalogo) throws Exception;
    
    
    /**
     * 
     * @param idCatalogo
     * @return
     * @throws Exception
     */
    public List<UsuarioGrupo> getUsuarioGrupoByCatalogoEmailNotNull(Long idCatalogo) throws Exception;
    
    /**
     * 
     * @param idCatalogo, idGrupoAcceso
     * @return
     * @throws Exception
     */
    void deleteCatalogoGrupo(Long idCatalogo, String idGrupoAcceso) throws Exception;
    
    
    /**
     * 
     * @param idCatalogo, idGrupoAcceso
     * @return
     * @throws Exception
     */
    void persistCatalogoGrupo(Long idCatalogo, String idGrupoAcceso) throws Exception;
        
}
