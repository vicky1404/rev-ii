package cl.mdr.ifrs.ejb.dummy.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the IFRS_CATALOGO database table.
 * 
 */
@Entity
@Table(name="IFRS_CATALOGO")
public class Catalogo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_CATALOGO")
	private Long idCatalogo;

	@Column(name="COD_CUADRO")
	private Long codCuadro;

	@Column(name="COD_SUBCUADRO")
	private Long codSubcuadro;

	@Column(name="IMPRESION_HORIZONTAL")
	private Long impresionHorizontal;

	private String nombre;

	private BigDecimal orden;

	private String titulo;

	private BigDecimal vigencia;

	//bi-directional many-to-one association to Empresa
    @ManyToOne
	@JoinColumn(name="RUT")
	private Empresa empresa;

	//bi-directional many-to-one association to TipoCuadro
    @ManyToOne
	@JoinColumn(name="ID_TIPO_CUADRO")
	private TipoCuadro tipoCuadro;

	//bi-directional many-to-many association to Grupo
	@ManyToMany(mappedBy="catalogos")
	private List<Grupo> grupos;

	//bi-directional many-to-one association to Version
	@OneToMany(mappedBy="catalogo")
	private List<Version> versiones;

    public Catalogo() {
    }

	public Long getIdCatalogo() {
		return idCatalogo;
	}

	public void setIdCatalogo(Long idCatalogo) {
		this.idCatalogo = idCatalogo;
	}

	public Long getCodCuadro() {
		return codCuadro;
	}

	public void setCodCuadro(Long codCuadro) {
		this.codCuadro = codCuadro;
	}

	public Long getCodSubcuadro() {
		return codSubcuadro;
	}

	public void setCodSubcuadro(Long codSubcuadro) {
		this.codSubcuadro = codSubcuadro;
	}

	public Long getImpresionHorizontal() {
		return impresionHorizontal;
	}

	public void setImpresionHorizontal(Long impresionHorizontal) {
		this.impresionHorizontal = impresionHorizontal;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public BigDecimal getOrden() {
		return orden;
	}

	public void setOrden(BigDecimal orden) {
		this.orden = orden;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public BigDecimal getVigencia() {
		return vigencia;
	}

	public void setVigencia(BigDecimal vigencia) {
		this.vigencia = vigencia;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public TipoCuadro getTipoCuadro() {
		return tipoCuadro;
	}

	public void setTipoCuadro(TipoCuadro tipoCuadro) {
		this.tipoCuadro = tipoCuadro;
	}

	public List<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}

	public List<Version> getVersiones() {
		return versiones;
	}

	public void setVersiones(List<Version> versiones) {
		this.versiones = versiones;
	}
	
    
	
}