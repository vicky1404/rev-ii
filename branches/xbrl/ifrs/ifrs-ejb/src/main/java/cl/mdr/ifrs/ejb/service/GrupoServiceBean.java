package cl.mdr.ifrs.ejb.service;

import static cl.mdr.ifrs.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;

import java.text.MessageFormat;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.exception.ConstraintViolationException;

import cl.mdr.ifrs.ejb.common.Constantes;
import cl.mdr.ifrs.ejb.entity.AreaNegocio;
import cl.mdr.ifrs.ejb.entity.Empresa;
import cl.mdr.ifrs.ejb.entity.Grupo;
import cl.mdr.ifrs.ejb.service.local.GrupoServiceLocal;
import cl.mdr.ifrs.exceptions.RegistroNoEditableException;

/**
 * @author http://www.mdrtech.cl
 * Session Bean implementation class GrupoServiceBean
 */
@Stateless
public class GrupoServiceBean implements GrupoServiceLocal {
	
	@PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public GrupoServiceBean() {
        super();
    }
    
    
    @SuppressWarnings("unchecked")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Grupo> findGruposByFiltro(final AreaNegocio areaNegocio, final Empresa empresa) throws Exception{
    	return em.createNamedQuery(Grupo.FIND_BY_FILTRO).
    				setParameter("areaNegocio", areaNegocio != null ? areaNegocio.getIdAreaNegocio() : null ).
    				setParameter("rutEmpresa", empresa.getIdRut()).    				
    				getResultList();
    }
    
    public void editarGrupo(final Grupo grupo) throws RegistroNoEditableException, Exception{
    	final int usuariosEnGrupo = this.validateUsuariosEnGrupo(grupo);
    	final int menuEnGrupo = this.validateMenuEnGrupo(grupo);
    	final int catalogoEnGrupo = this.validateCatalogoEnGrupo(grupo);
    	if(usuariosEnGrupo > 0){
    		throw new RegistroNoEditableException(MessageFormat.format("No es posible editar el Grupo {0} ya que posee {1} Usuario(s) asociados", grupo.getNombre(), usuariosEnGrupo));
    	}
    	if(menuEnGrupo > 0){
    		throw new RegistroNoEditableException(MessageFormat.format("No es posible editar el Grupo {0} ya que posee opciones de Menú asociadas", grupo.getNombre()));
    	}
    	if(catalogoEnGrupo > 0){
    		throw new RegistroNoEditableException(MessageFormat.format("No es posible editar el Grupo {0} ya que posee {1} Revelacion(es) asociadas", grupo.getNombre(), catalogoEnGrupo));
    	}
    	this.mergeGrupo(grupo);
    }
    
    public void eliminarGrupo(final Grupo grupo) throws RegistroNoEditableException, Exception{
    	final int usuariosEnGrupo = this.validateUsuariosEnGrupo(grupo);
    	final int menuEnGrupo = this.validateMenuEnGrupo(grupo);
    	final int catalogoEnGrupo = this.validateCatalogoEnGrupo(grupo);
    	if(usuariosEnGrupo > 0){
    		throw new RegistroNoEditableException(MessageFormat.format("No es posible eliminar el Grupo {0} ya que posee {1} Usuario(s) asociados", grupo.getNombre(), usuariosEnGrupo));
    	}
    	if(menuEnGrupo > 0){
    		throw new RegistroNoEditableException(MessageFormat.format("No es posible eliminar el Grupo {0} ya que posee opciones de Menú asociadas", grupo.getNombre()));
    	}
    	if(catalogoEnGrupo > 0){
    		throw new RegistroNoEditableException(MessageFormat.format("No es posible eliminar el Grupo {0} ya que posee {1} Revelacion(es) asociadas", grupo.getNombre(), catalogoEnGrupo));
    	}
    	this.deleteGrupo(grupo);
    }
    
    public void editarGrupoList(final List<Grupo> grupoList) throws RegistroNoEditableException, Exception{
    	for (Grupo grupo : grupoList) {
			this.editarGrupo(grupo);
		}
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private int validateUsuariosEnGrupo(final Grupo grupo) throws Exception{
		return em
				.createNativeQuery("select nombre_usuario from "+Constantes.USUARIO_GRUPO+" where id_grupo_acceso = ? ")
				.setParameter(1, grupo.getIdGrupoAcceso())
				.getResultList().size();
	}
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private int validateMenuEnGrupo(final Grupo grupo) throws Exception{
		return em
				.createNativeQuery("select id_menu from "+Constantes.MENU_GRUPO+" where id_grupo_acceso = ? ")
				.setParameter(1, grupo.getIdGrupoAcceso())
				.getResultList().size();
	}
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private int validateCatalogoEnGrupo(final Grupo grupo) throws Exception{
		return em
				.createNativeQuery("select id_catalogo from "+Constantes.CATALOGO_GRUPO+" where id_grupo_acceso = ? ")
				.setParameter(1, grupo.getIdGrupoAcceso())
				.getResultList().size();
	}
    
    public void mergeGrupo(final Grupo grupo) throws Exception{
    	em.merge(grupo);
    }
    
    public void mergeGrupoList(final List<Grupo> grupoLis) throws Exception{
    	for (final Grupo grupo : grupoLis) {
    		em.merge(grupo);
		}    	    	
    }
    
    public void persistGrupo(final Grupo grupo) throws ConstraintViolationException, Exception{
    	em.persist(grupo);
    }
    
    public void deleteGrupo(final Grupo grupo) throws Exception{
    	Grupo grupoDel = em.find(Grupo.class, grupo.getIdGrupoAcceso());
    	em.remove(grupoDel);
    }
    
    
    
    
    

}
