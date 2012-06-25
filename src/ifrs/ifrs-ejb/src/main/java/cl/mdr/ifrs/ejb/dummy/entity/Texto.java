package cl.mdr.ifrs.ejb.dummy.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the IFRS_TEXTO database table.
 * 
 */
@Entity
@Table(name="IFRS_TEXTO")
public class Texto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_TEXTO")
	private Long idTexto;

	private BigDecimal negrita;

	private String texto;

	//bi-directional one-to-one association to Estructura
	@OneToOne
	@JoinColumn(name="ID_TEXTO")
	private Estructura estructura;

    public Texto() {
    }

	public Long getIdTexto() {
		return idTexto;
	}

	public void setIdTexto(Long idTexto) {
		this.idTexto = idTexto;
	}

	public BigDecimal getNegrita() {
		return negrita;
	}

	public void setNegrita(BigDecimal negrita) {
		this.negrita = negrita;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Estructura getEstructura() {
		return estructura;
	}

	public void setEstructura(Estructura estructura) {
		this.estructura = estructura;
	}

    
	
}