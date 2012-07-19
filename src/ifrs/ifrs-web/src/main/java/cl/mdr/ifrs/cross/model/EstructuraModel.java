package cl.mdr.ifrs.cross.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cl.mdr.ifrs.ejb.entity.AgrupacionColumna;
import cl.mdr.ifrs.ejb.entity.Columna;
import cl.mdr.ifrs.ejb.entity.Html;
import cl.mdr.ifrs.ejb.entity.Texto;

public class EstructuraModel implements Serializable {
	private static final long serialVersionUID = -5070483324266390295L;
	
	private Long idGrilla;    
    private Map<Long,List<AgrupacionColumna>> agrupacionesMap;    
    private List<Columna> columnas;    
    private List<Long> filas;    
    private Long grillaSelected;    
    private Long widthEditarGrillaTable = 15L;    
    private List<Long> cantidadFilasEdiciones;    
    private Html html;    
    private Texto texto;    
    private Long tipoEstructura;     
    private String tituloGrilla;    
    private Columna columnaDesde;    
    private Columna columnaHasta;    
    private String tituloAgrupacion;    
    private List<Long> nivelesAgregados;    
    private Long nivel;
    
    public EstructuraModel(){        
    }
    
    public EstructuraModel(Long tipoEstructura){
        this.tipoEstructura = tipoEstructura;
    }

    public void setColumnas(List<Columna> columnas) {
        this.columnas = columnas;
    }

    public List<Columna> getColumnas() {
        if(columnas == null){
            columnas = new ArrayList<Columna>();
        }
        return columnas;
    }

    public void setFilas(List<Long> filas) {
        this.filas = filas;
    }

    public List<Long> getFilas() {
        if(filas == null){
            filas = new ArrayList<Long>();
        }
        return filas;
    }
    
    public void clearModelTable(){
        getColumnas().clear();
        filas.clear();
    }

    public void setGrillaSelected(Long grillaSelected) {
        this.grillaSelected = grillaSelected;
    }

    public Long getGrillaSelected() {
        return grillaSelected;
    }

    public void setWidthEditarGrillaTable(Long widthEditarGrillaTable) {
        this.widthEditarGrillaTable = widthEditarGrillaTable;
    }

    public Long getWidthEditarGrillaTable() {
        return widthEditarGrillaTable;
    }

    public void setCantidadFilasEdiciones(List<Long> cantidadFilasEdiciones) {
        this.cantidadFilasEdiciones = cantidadFilasEdiciones;
    }

    public List<Long> getCantidadFilasEdiciones() {
        return cantidadFilasEdiciones;
    }
    
    public void setTipoEstructura(Long tipoEstructura) {
        this.tipoEstructura = tipoEstructura;
    }

    public Long getTipoEstructura() {
        return tipoEstructura;
    }

    public void setTexto(Texto texto) {
        this.texto = texto;
    }

    public Texto getTexto() {
        if(texto==null){
            texto = new Texto();
            texto.setNegrita(false);
        }
        return texto;
    }

    public void setHtml(Html html) {
        this.html = html;
    }

    public Html getHtml() {
        if(html==null){
            html = new Html();
        }
        return html;
    }

    public void setTituloGrilla(String tituloGrilla) {
        this.tituloGrilla = tituloGrilla;
    }

    public String getTituloGrilla() {
        return tituloGrilla;
    }

    public void setIdGrilla(Long idGrilla) {
        this.idGrilla = idGrilla;
    }

    public Long getIdGrilla() {
        return idGrilla;
    }

    public void setNivel(Long nivel) {
        this.nivel = nivel;
    }

    public Long getNivel() {
        return nivel;
    }

    public void setAgrupacionesMap(Map<Long, List<AgrupacionColumna>> agrupacionesMap) {
        this.agrupacionesMap = agrupacionesMap;
    }

    public Map<Long, List<AgrupacionColumna>> getAgrupacionesMap() {
        if(agrupacionesMap==null){
            agrupacionesMap = new HashMap<Long, List<AgrupacionColumna>>();
        }
        return agrupacionesMap;
    }

    public void setColumnaDesde(Columna columnaDesde) {
        this.columnaDesde = columnaDesde;
    }

    public Columna getColumnaDesde() {
        return columnaDesde;
    }

    public void setColumnaHasta(Columna columnaHasta) {
        this.columnaHasta = columnaHasta;
    }

    public Columna getColumnaHasta() {
        return columnaHasta;
    }

    public void setTituloAgrupacion(String tituloAgrupacion) {
        this.tituloAgrupacion = tituloAgrupacion;
    }

    public String getTituloAgrupacion() {
        return tituloAgrupacion;
    }

    public void setNivelesAgregados(List<Long> nivelesAgregados) {
        this.nivelesAgregados = nivelesAgregados;
    }

    public List<Long> getNivelesAgregados() {
        if(nivelesAgregados==null){
            nivelesAgregados = new ArrayList<Long>();
        }
        return nivelesAgregados;
    }

}
