package cl.mdr.ifrs.ejb.reporte.vo;


import java.awt.Color;
import java.io.Serializable;

import org.apache.poi.xwpf.usermodel.VerticalAlign;

import cl.mdr.ifrs.ejb.entity.Catalogo;


public class PropiedadesReporteVO  implements Serializable{
	private static final long serialVersionUID = -7290024067588936089L;

	private String nombreHoja = "hoja";
    
    /*Atributos titulo*/
    private String tituloPrincipal;    
    private int    tamanoLetraTitulo = 14;
    private int    tamanoLetraHeader = 12;
    private int    tamanoLetraContenido = 10;    
    private boolean tituloIsNegrita = false;
    private Color colorHeader = Color.LIGHT_GRAY;
    private Color colorBorder = Color.BLACK;
    private VerticalAlign verticalAlingHeader;
    private Catalogo catalogo;
    

    /*Atributos sub titulo*/
    private String subTituloPrincipal;
    
    public PropiedadesReporteVO() {
    }

	public String getNombreHoja() {
		return nombreHoja;
	}

	public void setNombreHoja(String nombreHoja) {
		this.nombreHoja = nombreHoja;
	}

	public String getTituloPrincipal() {
		return tituloPrincipal;
	}

	public void setTituloPrincipal(String tituloPrincipal) {
		this.tituloPrincipal = tituloPrincipal;
	}

	public int getTamanoLetraTitulo() {
		return tamanoLetraTitulo;
	}

	public void setTamanoLetraTitulo(int tamanoLetraTitulo) {
		this.tamanoLetraTitulo = tamanoLetraTitulo;
	}

	public int getTamanoLetraHeader() {
		return tamanoLetraHeader;
	}

	public void setTamanoLetraHeader(int tamanoLetraHeader) {
		this.tamanoLetraHeader = tamanoLetraHeader;
	}

	public int getTamanoLetraContenido() {
		return tamanoLetraContenido;
	}

	public void setTamanoLetraContenido(int tamanoLetraContenido) {
		this.tamanoLetraContenido = tamanoLetraContenido;
	}

	public boolean isTituloIsNegrita() {
		return tituloIsNegrita;
	}

	public void setTituloIsNegrita(boolean tituloIsNegrita) {
		this.tituloIsNegrita = tituloIsNegrita;
	}

	public Color getColorHeader() {
		return colorHeader;
	}

	public void setColorHeader(Color colorHeader) {
		this.colorHeader = colorHeader;
	}

	public Color getColorBorder() {
		return colorBorder;
	}

	public void setColorBorder(Color colorBorder) {
		this.colorBorder = colorBorder;
	}

	public VerticalAlign getVerticalAlingHeader() {
		return verticalAlingHeader;
	}

	public void setVerticalAlingHeader(VerticalAlign verticalAlingHeader) {
		this.verticalAlingHeader = verticalAlingHeader;
	}

	public Catalogo getCatalogo() {
		return catalogo;
	}

	public void setCatalogo(Catalogo catalogo) {
		this.catalogo = catalogo;
	}

	public String getSubTituloPrincipal() {
		return subTituloPrincipal;
	}

	public void setSubTituloPrincipal(String subTituloPrincipal) {
		this.subTituloPrincipal = subTituloPrincipal;
	}
    

    

    
}
