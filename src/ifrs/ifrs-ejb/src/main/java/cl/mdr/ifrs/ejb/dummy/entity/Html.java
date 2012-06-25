package cl.mdr.ifrs.ejb.dummy.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the IFRS_HTML database table.
 * 
 */
@Entity
@Table(name="IFRS_HTML")
public class Html implements Serializable {
	private static final long serialVersionUID = -8640707929918223892L;

	@Id
	@Column(name="ID_HTML")
	private long idHtml;

    @Lob()
	private byte[] contenido;

	private String titulo;

	//bi-directional one-to-one association to Estructura
	@OneToOne
	@JoinColumn(name="ID_HTML")
	private Estructura estructura;

    public Html() {
    }

	public long getIdHtml() {
		return idHtml;
	}

	public void setIdHtml(long idHtml) {
		this.idHtml = idHtml;
	}

	public byte[] getContenido() {
		return contenido;
	}

	public void setContenido(byte[] contenido) {
		this.contenido = contenido;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Estructura getEstructura() {
		return estructura;
	}

	public void setEstructura(Estructura estructura) {
		this.estructura = estructura;
	}
    
    

	
	
}