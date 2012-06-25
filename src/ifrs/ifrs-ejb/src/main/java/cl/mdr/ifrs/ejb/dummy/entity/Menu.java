package cl.mdr.ifrs.ejb.dummy.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;


/**
 * The persistent class for the IFRS_MENU database table.
 * 
 */
@Entity
@Table(name="IFRS_MENU")
public class Menu implements Serializable {
	private static final long serialVersionUID = -1500026007374125510L;

	@Id
	@Column(name="ID_MENU")
	private Long idMenu;

	@Column(name="ES_PADRE")
	private Long esPadre;

	private Long estado;

	private Long grupo;

	private String nombre;

	@Column(name="URL_MENU")
	private String urlMenu;

	//bi-directional many-to-many association to Grupo
    @ManyToMany
	@JoinTable(
		name="IFRS_MENU_GRUPO"
		, joinColumns={
			@JoinColumn(name="ID_MENU")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_GRUPO_ACCESO")
			}
		)
	private List<Grupo> grupos;

    public Menu() {
    }

	public Long getIdMenu() {
		return idMenu;
	}

	public void setIdMenu(Long idMenu) {
		this.idMenu = idMenu;
	}

	public Long getEsPadre() {
		return esPadre;
	}

	public void setEsPadre(Long esPadre) {
		this.esPadre = esPadre;
	}

	public Long getEstado() {
		return estado;
	}

	public void setEstado(Long estado) {
		this.estado = estado;
	}

	public Long getGrupo() {
		return grupo;
	}

	public void setGrupo(Long grupo) {
		this.grupo = grupo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getUrlMenu() {
		return urlMenu;
	}

	public void setUrlMenu(String urlMenu) {
		this.urlMenu = urlMenu;
	}

	public List<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}
    
    
		
}