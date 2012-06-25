package cl.mdr.ifrs.ejb.dummy.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the IFRS_TIPO_CUADRO database table.
 * 
 */
@Entity
@Table(name="IFRS_TIPO_CUADRO")
public class TipoCuadro implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_TIPO_CUADRO")
	private Long idTipoCuadro;

	private String nombre;

	private String titulo;

	//bi-directional many-to-one association to Catalogo
	@OneToMany(mappedBy="tipoCuadro")
	private List<Catalogo> catalogos;

    public TipoCuadro() {
    }

	public Long getIdTipoCuadro() {
		return idTipoCuadro;
	}

	public void setIdTipoCuadro(Long idTipoCuadro) {
		this.idTipoCuadro = idTipoCuadro;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public List<Catalogo> getCatalogos() {
		return catalogos;
	}

	public void setCatalogos(List<Catalogo> catalogos) {
		this.catalogos = catalogos;
	}
	
    
	
}