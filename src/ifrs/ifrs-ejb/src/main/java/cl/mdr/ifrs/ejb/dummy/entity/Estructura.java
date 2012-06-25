package cl.mdr.ifrs.ejb.dummy.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 * The persistent class for the IFRS_ESTRUCTURA database table.
 * 
 */
@Entity
@Table(name="IFRS_ESTRUCTURA")
public class Estructura implements Serializable {	
	private static final long serialVersionUID = -6100328419126595627L;

	@Id
	@Column(name="ID_ESTRUCTURA")
	private Long idEstructura;

	private Long orden;

	//bi-directional many-to-one association to TipoEstructura
    @ManyToOne
	@JoinColumn(name="ID_TIPO_ESTRUCTURA")
	private TipoEstructura tipoEstructura;

	//bi-directional many-to-one association to Version
    @ManyToOne
	@JoinColumn(name="ID_VERSION")
	private Version version;

	//bi-directional one-to-one association to Grilla
	@OneToOne(mappedBy="estructura")
	private Grilla grilla;

	//bi-directional one-to-one association to Html
	@OneToOne(mappedBy="estructura")
	private Html html;

	//bi-directional one-to-one association to Texto
	@OneToOne(mappedBy="estructura")
	private Texto texto;

    public Estructura() {
    }

	public Long getIdEstructura() {
		return idEstructura;
	}

	public void setIdEstructura(Long idEstructura) {
		this.idEstructura = idEstructura;
	}

	public Long getOrden() {
		return orden;
	}

	public void setOrden(Long orden) {
		this.orden = orden;
	}

	public TipoEstructura getTipoEstructura() {
		return tipoEstructura;
	}

	public void setTipoEstructura(TipoEstructura tipoEstructura) {
		this.tipoEstructura = tipoEstructura;
	}

	public Version getVersion() {
		return version;
	}

	public void setVersion(Version version) {
		this.version = version;
	}

	public Grilla getGrilla() {
		return grilla;
	}

	public void setGrilla(Grilla grilla) {
		this.grilla = grilla;
	}

	public Html getHtml() {
		return html;
	}

	public void setHtml(Html html) {
		this.html = html;
	}

	public Texto getTexto() {
		return texto;
	}

	public void setTexto(Texto texto) {
		this.texto = texto;
	}
    
    

	
	
}