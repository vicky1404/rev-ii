package cl.bicevida.revelaciones.vo;

import java.io.Serializable;

import oracle.adf.view.rich.component.rich.data.RichColumn;


public class AgrupacionVO implements Serializable{

    @SuppressWarnings("compatibility:-2408084214031641591")
    private static final long serialVersionUID = -5961676982770644458L;
    private Long desde;
    private Long hasta;
    private Long grupo;
    private Long ancho;
    private Long nivel;
    private Long idGrilla;
    private String titulo;   
    private RichColumn richColumn;
    
    
    public AgrupacionVO() {
        super();
    }

    public AgrupacionVO(Long desde, Long hasta, Long grupo, Long ancho, String titulo, Long idGrilla) {
        super();
        this.desde = desde;
        this.hasta = hasta;
        this.grupo = grupo;
        this.ancho = ancho;
        this.titulo = titulo;
        this.idGrilla = idGrilla;
    }
    
    public AgrupacionVO(Long desde, Long hasta, Long grupo, Long ancho, String titulo, Long idGrilla, Long nivel) {
        super();
        this.desde = desde;
        this.hasta = hasta;
        this.grupo = grupo;
        this.ancho = ancho;
        this.titulo = titulo;
        this.idGrilla = idGrilla;
        this.nivel = nivel;
    }

    public void setDesde(Long desde) {
        this.desde = desde;
    }

    public Long getDesde() {
        return desde;
    }

    public void setHasta(Long hasta) {
        this.hasta = hasta;
    }

    public Long getHasta() {
        return hasta;
    }

    public void setGrupo(Long grupo) {
        this.grupo = grupo;
    }

    public Long getGrupo() {
        return grupo;
    }

    public void setAncho(Long ancho) {
        this.ancho = ancho;
    }

    public Long getAncho() {
        return ancho;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setRichColumn(RichColumn richColumn) {
        this.richColumn = richColumn;
    }

    public RichColumn getRichColumn() {
        return richColumn;
    }

    public void setNivel(Long nivel) {
        this.nivel = nivel;
    }

    public Long getNivel() {
        return nivel;
    }

    public void setIdGrilla(Long idGrilla) {
        this.idGrilla = idGrilla;
    }

    public Long getIdGrilla() {
        return idGrilla;
    }
}
