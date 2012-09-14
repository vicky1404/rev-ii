package cl.bicevida.revelaciones.ejb.reporte.vo;


import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;

import cl.bicevida.revelaciones.ejb.entity.Catalogo;

import java.awt.Color;

import java.io.Serializable;


public class PropiedadesReporteVO  implements Serializable{

    
    
    private static final long serialVersionUID = 1L;
    
    private String nombreHoja = "hoja";
    
    /*Atributos titulo*/
    private String tituloPrincipal;
    private String tipoLetraReporte = Font._FONT_ARIAL;
    private int    tamanoLetraTitulo = 14;
    private int    tamanoLetraHeader = 12;
    private int    tamanoLetraContenido = 10;    
    private boolean tituloIsNegrita = false;
    private Color colorHeader = Color.LIGHT_GRAY;
    private Color colorBorder = Color.BLACK;
    private Border borderHeader = Border.THIN;
    private Border borderCell = Border.THIN;
    private HorizontalAlign horizontalAlignHeader;
    private VerticalAlign verticalAlingHeader;
    private Catalogo catalogo;
    

    /*Atributos sub titulo*/
    private String subTituloPrincipal;
    
    public PropiedadesReporteVO() {
    }
    

    public String getTituloPrincipal() {
        return tituloPrincipal;
    }

    public void setTituloPrincipal(String tituloPrincipal) {
        this.tituloPrincipal = tituloPrincipal;
    }
    
    public boolean isTituloIsNegrita() {
        return tituloIsNegrita;
    }

    public void setTituloIsNegrita(boolean tituloIsNegrita) {
        this.tituloIsNegrita = tituloIsNegrita;
    }

    public String getSubTituloPrincipal() {
        return subTituloPrincipal;
    }

    public void setSubTituloPrincipal(String subTituloPrincipal) {
        this.subTituloPrincipal = subTituloPrincipal;
    }

    public void setNombreHoja(String nombreHoja) {
        this.nombreHoja = nombreHoja;
    }

    public String getNombreHoja() {
        return nombreHoja;
    }

    public void setColorHeader(Color colorHeader) {
        this.colorHeader = colorHeader;
    }

    public Color getColorHeader() {
        return colorHeader;
    }

    public void setColorBorder(Color colorBorder) {
        this.colorBorder = colorBorder;
    }

    public Color getColorBorder() {
        return colorBorder;
    }

    public void setBorderHeader(Border borderHeader) {
        this.borderHeader = borderHeader;
    }

    public Border getBorderHeader() {
        return borderHeader;
    }

    public void setBorderCell(Border borderCell) {
        this.borderCell = borderCell;
    }

    public Border getBorderCell() {
        return borderCell;
    }

    public void setTamanoLetraTitulo(int tamanoLetraTitulo) {
        this.tamanoLetraTitulo = tamanoLetraTitulo;
    }

    public int getTamanoLetraTitulo() {
        return tamanoLetraTitulo;
    }

    public void setTamanoLetraContenido(int tamanoLetraContenido) {
        this.tamanoLetraContenido = tamanoLetraContenido;
    }

    public int getTamanoLetraContenido() {
        return tamanoLetraContenido;
    }

    public void setHorizontalAlignHeader(HorizontalAlign horizontalAlignHeader) {
        this.horizontalAlignHeader = horizontalAlignHeader;
    }

    public HorizontalAlign getHorizontalAlignHeader() {
        return horizontalAlignHeader;
    }

    public void setVerticalAlingHeader(VerticalAlign verticalAlingHeader) {
        this.verticalAlingHeader = verticalAlingHeader;
    }

    public VerticalAlign getVerticalAlingHeader() {
        return verticalAlingHeader;
    }

    public void setTamanoLetraHeader(int tamanoLetraHeader) {
        this.tamanoLetraHeader = tamanoLetraHeader;
    }

    public int getTamanoLetraHeader() {
        return tamanoLetraHeader;
    }

    public void setTipoLetraReporte(String tipoLetraReporte) {
        this.tipoLetraReporte = tipoLetraReporte;
    }

    public String getTipoLetraReporte() {
        return tipoLetraReporte;
    }

    public void setCatalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
    }

    public Catalogo getCatalogo() {
        return catalogo;
    }
}
