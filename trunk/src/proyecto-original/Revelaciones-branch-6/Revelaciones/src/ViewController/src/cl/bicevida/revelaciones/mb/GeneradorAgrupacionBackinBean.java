package cl.bicevida.revelaciones.mb;


import cl.bicevida.revelaciones.common.mb.SoporteBackingBean;
import cl.bicevida.revelaciones.common.util.GeneradorDisenoHelper;
import cl.bicevida.revelaciones.ejb.cross.SortHelper;
import cl.bicevida.revelaciones.ejb.entity.AgrupacionColumna;
import cl.bicevida.revelaciones.ejb.entity.Columna;
import cl.bicevida.revelaciones.ejb.entity.Estructura;
import cl.bicevida.revelaciones.vo.AgrupacionVO;
import cl.bicevida.revelaciones.vo.GrillaModelVO;
import cl.bicevida.revelaciones.vo.TableModelVO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.el.ValueExpression;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import oracle.adf.view.rich.component.rich.data.RichColumn;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;

import org.apache.myfaces.trinidad.event.DisclosureEvent;


public class GeneradorAgrupacionBackinBean extends SoporteBackingBean{

    @SuppressWarnings("compatibility:-3045894201164009527")
    private static final long serialVersionUID = -3861889138954119512L;
    
    private transient RichTable disenoEstructuraTable;
    private transient RichTable editarGrillaTable;    
    private Long grillaSelected = null;    
    private Long widthEditarGrillaTable = 10L;    
    private boolean renderedTableAgrupador = false;        
    private List<SelectItem> columnasDesdeAgregadas;    
    private List<SelectItem> columnasHastaAgregadas;
    private transient TableModelVO tableModel;
    private transient RichSelectOneChoice nivelDesdeChoice;    
    private transient RichSelectOneChoice nivelHastaChoice;            
    public GeneradorAgrupacionBackinBean() {
    }
    
    public String editarGrillaAction() {

        Estructura estructura = (Estructura)getDisenoEstructuraTable().getRowData();
        setGrillaSelected(estructura.getOrden());
        getGeneradorDiseno().setGrillaSelected(estructura.getOrden());
        getGrillaModelMap().get(getGrillaSelected()).setGrillaSelected(estructura.getOrden());
        widthEditarGrillaTable = 8L;
        if(getGrillaModelMap().get(getGrillaSelected()).getColumnas().size()==0){
            return null;
        }
        createDinamicTable();
        return null;
    }
    
    private void createDinamicTable(){
        setInitialEditarGrillaTable();
        addChildrenToEditarGrillaTable(getGrillaModelByEstructuraSelected().getColumnas(), 
                                       getGrillaModelByEstructuraSelected().getAgrupacionesMap(),
                                       getGrillaModelByEstructuraSelected().getNivelesAgregados());
        setTableModel(GeneradorDisenoHelper.createTableModel(getGrillaModelByEstructuraSelected().getColumnas()));
        GeneradorDisenoHelper.procesarColumnas(getGrillaModelMap().get(getGrillaSelected()).getColumnas());
        getEditarGrillaTable().setValue(getTableModel().getRows());
        setRenderedTableAgrupador(true);
        getGrillaModelMap().get(getGrillaSelected()).setWidthEditarGrillaTable(getWidthEditarGrillaTable());
        setADFPartialTarget(getEditarGrillaTable());
    }
    
    private void setInitialEditarGrillaTable(){
        getEditarGrillaTable().clearInitialState();
        getEditarGrillaTable().setAllDetailsEnabled(true);
        getEditarGrillaTable().getChildren().clear();
        getEditarGrillaTable().setId("tb3D01");
        getEditarGrillaTable().setValue(null);        
    }
    
    public GrillaModelVO getGrillaModelByEstructuraSelected(){
        if(getGrillaModelMap().containsKey(getGrillaSelected())){
            return getGrillaModelMap().get(getGrillaSelected());    
        }
        return new GrillaModelVO();
    }
    
    private void addChildrenToEditarGrillaTable(List<Columna> columnas, Map<Long,List<AgrupacionColumna>> agrupacionesMap, List<Long> niveles){ 
        
        Long orden = 0L;
        widthEditarGrillaTable = 10L;
        Map<Long, RichColumn> hijosMap = new LinkedHashMap<Long, RichColumn>();
        Map<Long,AgrupacionVO> hijosAgregadosMap = new LinkedHashMap<Long,AgrupacionVO>();
        Map<Long,AgrupacionVO> nivel1AgregadosMap = new LinkedHashMap<Long,AgrupacionVO>();
        
        SortHelper.sortColumnasByOrden(columnas);
        
        for(Columna columna : columnas){
            orden++;
            widthEditarGrillaTable += columna.getAncho() + 4L;
            columna.setIdColumna(orden);
            columna.setOrden(orden);
            ValueExpression value = getFacesContext().getApplication().getExpressionFactory()
                                    .createValueExpression(getFacesContext().getELContext(), "#{row["+(columna.getIdColumna())+"].valor}", Object.class);
            RichColumn column = GeneradorDisenoHelper.getRichColumn(columna.getTituloColumna(), columna.getAncho(), "center", false, GeneradorDisenoHelper.PREFIX_ID_COLUMN_CHILD +columna.getIdColumna());            
            column.getChildren().add(GeneradorDisenoHelper.getRichOutputText(value));
            hijosMap.put(columna.getIdColumna(), column);
        }
        
        Map<Long, List<AgrupacionVO>> agrupadorVOMap = GeneradorDisenoHelper.crearAgrupadorVO(agrupacionesMap, niveles);
        
        int nivelColumnas = 0;
        
        
        if(agrupadorVOMap.containsKey(1L)){
            for(AgrupacionVO agrupacion : agrupadorVOMap.get(1L)){
                RichColumn column = GeneradorDisenoHelper.getRichColumn(agrupacion.getTitulo(), agrupacion.getAncho(), "center", false, GeneradorDisenoHelper.PREFIX_ID_COLUMN_PARENT+1+agrupacion.getGrupo());
                agrupacion.setRichColumn(column);
                
                for(Long i=agrupacion.getDesde(); i<= agrupacion.getHasta(); i++){
                    if(hijosMap.containsKey(i)){
                        agrupacion.getRichColumn().getChildren().add(hijosMap.get(i));
                        hijosAgregadosMap.put(i, agrupacion);
                        nivelColumnas = 1;
                    }
                }
            }
        }
        
        if(agrupadorVOMap.containsKey(2L)){
            for(AgrupacionVO agrupacion : agrupadorVOMap.get(2L)){
                RichColumn column = GeneradorDisenoHelper.getRichColumn(agrupacion.getTitulo(), agrupacion.getAncho(), "center", false, GeneradorDisenoHelper.PREFIX_ID_COLUMN_PARENT+2+agrupacion.getGrupo());
                agrupacion.setRichColumn(column);
                if(agrupadorVOMap.containsKey(1L)){
                    for(AgrupacionVO subAgrupacion : agrupadorVOMap.get(1L)){
                        if(subAgrupacion.getDesde() <= agrupacion.getHasta() &&  subAgrupacion.getHasta() >= agrupacion.getDesde()){
                            agrupacion.getRichColumn().getChildren().add(subAgrupacion.getRichColumn());
                            nivel1AgregadosMap.put(subAgrupacion.getHasta(), agrupacion);
                            nivelColumnas = 2;
                        }
                    }
                }
            }
        }
        
        if(agrupadorVOMap.containsKey(3L)){
            for(AgrupacionVO agrupacion : agrupadorVOMap.get(3L)){
                RichColumn column = GeneradorDisenoHelper.getRichColumn(agrupacion.getTitulo(), agrupacion.getAncho(), "center", false, GeneradorDisenoHelper.PREFIX_ID_COLUMN_PARENT+3+agrupacion.getGrupo());
                agrupacion.setRichColumn(column);
                for(Long i=agrupacion.getDesde(); i<= agrupacion.getHasta(); i++){
                    if(agrupadorVOMap.containsKey(2L)){
                        for(AgrupacionVO subAgrupacion : agrupadorVOMap.get(2L)){
                            if(agrupacion.getDesde()>= subAgrupacion.getDesde() && agrupacion.getHasta() >= subAgrupacion.getHasta()){
                                agrupacion.getRichColumn().getChildren().add(subAgrupacion.getRichColumn());
                                nivelColumnas = 3;
                            }
                        }
                    }
                }
            }
        }
        
        switch (nivelColumnas){
        
            case 0 : 
            Iterator it0 = hijosMap.entrySet().iterator();
            while(it0.hasNext()){
                Map.Entry entry = (Map.Entry) it0.next();
                getEditarGrillaTable().getChildren().add((RichColumn)entry.getValue());
            }
            break;
            case 1 :
            Iterator it1 = hijosMap.entrySet().iterator();
            while(it1.hasNext()){
                Map.Entry entry = (Map.Entry) it1.next();
                Long idColumna = (Long)entry.getKey();
                if(hijosAgregadosMap.containsKey(idColumna)){
                    AgrupacionVO agru = hijosAgregadosMap.get(idColumna);
                    if(agru.getHasta()==idColumna){
                        getEditarGrillaTable().getChildren().add(agru.getRichColumn());
                    }
                }else{
                    getEditarGrillaTable().getChildren().add((RichColumn)entry.getValue());
                }
            }
            break;
            case 2 :
            Iterator it2 = hijosMap.entrySet().iterator();
            while(it2.hasNext()){
                Map.Entry entry = (Map.Entry) it2.next();
                Long idColumna = (Long)entry.getKey();
                if(hijosAgregadosMap.containsKey(idColumna)){
                    AgrupacionVO agru = hijosAgregadosMap.get(idColumna);
                    if(agru.getHasta()==idColumna){
                        if(nivel1AgregadosMap.containsKey(idColumna)){
                            for(AgrupacionVO  agruN1 : agrupadorVOMap.get(2L)){
                                if(agruN1.getHasta()==idColumna){
                                    getEditarGrillaTable().getChildren().add(agruN1.getRichColumn());
                                }
                            }
                        }else{
                            getEditarGrillaTable().getChildren().add(agru.getRichColumn());
                        }
                    }
                }else{
                    getEditarGrillaTable().getChildren().add((RichColumn)entry.getValue());
                }
            }
            break;
            case 3 :
            for(AgrupacionVO agrupacion : agrupadorVOMap.get(3L)){
                getEditarGrillaTable().getChildren().add(agrupacion.getRichColumn());
            }
            break;
        }
            
        
        getEditarGrillaTable().setWidth(widthEditarGrillaTable + "px;");
    }
    
    public Object eliminarNivelColumnaAction(){
        Long nivel = getGrillaModelByEstructuraSelected().getNivel();
        
        if(nivel==null){
            agregarWarnMessage("El campo nivel es obligatorio");
            return null;
        }
        
        if(nivel == 1L){
            
            if(getGrillaModelByEstructuraSelected().getAgrupacionesMap().containsKey(2L)){
                agregarWarnMessage("Debe eliminar primero nivel 2");
                return null;
            }
            if(getGrillaModelByEstructuraSelected().getAgrupacionesMap().containsKey(nivel)){
                getGrillaModelByEstructuraSelected().getAgrupacionesMap().remove(nivel);
                getGrillaModelByEstructuraSelected().getNivelesAgregados().remove(1L);
            }
            
        }else if(nivel == 2L){
            if(getGrillaModelByEstructuraSelected().getAgrupacionesMap().containsKey(nivel)){
                getGrillaModelByEstructuraSelected().getAgrupacionesMap().remove(nivel);
                getGrillaModelByEstructuraSelected().getNivelesAgregados().remove(2L);
            }
        }
        
        createDinamicTable();
        
        return null;
    }
    
    public Object agregarNivelColumnaAction(){
        
        Columna desde = getGrillaModelByEstructuraSelected().getColumnaDesde();
        Columna hasta = getGrillaModelByEstructuraSelected().getColumnaHasta();
        List<Columna> columnas =  getGrillaModelByEstructuraSelected().getColumnas();
        Long nivel = getGrillaModelByEstructuraSelected().getNivel();
        String titulo = getGrillaModelByEstructuraSelected().getTituloAgrupacion();
        
        if(desde==null || hasta == null || nivel==null){
            agregarWarnMessage("Los campos desde - hasta - nivel son obligatorios");
            return null;
        }
        
        if(desde.getIdColumna()>hasta.getIdColumna()){
            agregarWarnMessage("La columna Hasta no puede ser mayor a la columna desde");
            return null;
        }
        if(nivel==2L && !getGrillaModelByEstructuraSelected().getAgrupacionesMap().containsKey(1L)){
            agregarWarnMessage("Se debe crear primero el nivel 1");
            return null;
        }
        
        if(!getGrillaModelByEstructuraSelected().getAgrupacionesMap().containsKey(nivel)){
            List<AgrupacionColumna> agrupaciones = new ArrayList<AgrupacionColumna>();
            Long ancho=0L;
            Long grupo = 1L;
            for(Long i=desde.getIdColumna(); i <= hasta.getIdColumna(); i++){
                for(Columna col : columnas){
                    if(col.getIdColumna()==i){
                        ancho += col.getAncho();
                        agrupaciones.add(new AgrupacionColumna(ancho,col,nivel,titulo==null?"":titulo,grupo, col.getIdGrilla(),col.getIdColumna()));
                    }
                }
            }
            for(AgrupacionColumna agrupacion : agrupaciones){
                agrupacion.setAncho(ancho);
            }
            getGrillaModelByEstructuraSelected().getAgrupacionesMap().put(nivel,agrupaciones);
            getGrillaModelByEstructuraSelected().getNivelesAgregados().add(nivel);
            
        }else{

            List<AgrupacionColumna> agrupaciones = getGrillaModelByEstructuraSelected().getAgrupacionesMap().get(nivel);
            Collections.sort(agrupaciones);
            for(AgrupacionColumna agrupacion : agrupaciones){
                
                if(desde.getIdColumna().equals(agrupacion.getIdColumna()) || hasta.getIdColumna().equals(agrupacion.getIdColumna())){
                    agregarWarnMessage("No se puede agrupar más de una vez la misma columna en un nivel");
                    return null;
                }
            }
            Long ancho=0L;
            for(Long i=desde.getIdColumna(); i <= hasta.getIdColumna(); i++){
                for(Columna col : columnas){
                    if(col.getIdColumna()==i){
                        ancho += col.getAncho();
                    }
                }
            }
            Long grupo =  Integer.valueOf(agrupaciones.size()).longValue() + 1L;
            for(Long i=desde.getIdColumna(); i <= hasta.getIdColumna(); i++){
                for(Columna col : columnas){
                    
                    if(col.getIdColumna()==i){
                        agrupaciones.add(new AgrupacionColumna(0L,col,nivel,titulo==null?"":titulo, grupo , col.getIdGrilla(),col.getIdColumna()));
                    }
                }
            }        
            getGrillaModelByEstructuraSelected().getAgrupacionesMap().put(nivel,agrupaciones);

        }
        
        createDinamicTable();
        
        return null;
        
    }
    
    public void selectNivelValueListener(ValueChangeEvent valueChangeEvent){
        Long nivel = (Long)valueChangeEvent.getNewValue();
        List<SelectItem> columnasAgregadasDesdeNew = new ArrayList<SelectItem>();
        List<SelectItem> columnasAgregadasHastaNew;
        if(nivel==1L){
            List<Columna> columnas = getGrillaModelByEstructuraSelected().getColumnas();
            for (Columna columna : columnas) {
                columnasAgregadasDesdeNew.add(new SelectItem(columna, columna.getTituloColumna()));
            }
            setColumnasDesdeAgregadaItems(columnasAgregadasDesdeNew);
            setColumnasHastaAgregadaItems(columnasAgregadasDesdeNew);
        }else if(nivel ==2L){
            List<AgrupacionColumna> agrupaciones = getGrillaModelByEstructuraSelected().getAgrupacionesMap().get(1L);
            List<AgrupacionVO> agrupacionMap = GeneradorDisenoHelper.crearAgrupadorVO(agrupaciones, nivel);
            columnasAgregadasHastaNew = new ArrayList<SelectItem>();
            for (AgrupacionVO agrupacion : agrupacionMap) {
                Columna columna = new Columna();
                columna.setIdColumna(agrupacion.getDesde());
                columna.setDesde(agrupacion.getDesde());
                columna.setHasta(agrupacion.getHasta());
                columna.setIdGrilla(agrupacion.getIdGrilla());
                columnasAgregadasDesdeNew.add(new SelectItem(columna, agrupacion.getTitulo()));
            }
            
            for (AgrupacionVO agrupacion : agrupacionMap) {
                Columna columna = new Columna();
                columna.setIdColumna(agrupacion.getHasta());
                columna.setDesde(agrupacion.getDesde());
                columna.setHasta(agrupacion.getHasta());
                columna.setIdGrilla(agrupacion.getIdGrilla());
                columnasAgregadasHastaNew.add(new SelectItem(columna, agrupacion.getTitulo()));
            }
            setColumnasDesdeAgregadaItems(columnasAgregadasDesdeNew);
            setColumnasHastaAgregadaItems(columnasAgregadasHastaNew);
        }
        
        setADFPartialTarget(getNivelDesdeChoice());
        setADFPartialTarget(getNivelHastaChoice());
    }
    
    public void agrupadorDisclosure(DisclosureEvent disclosureEvent) {
        setRenderedTableAgrupador(false);
    }
    
    public List<SelectItem> getNivelColumnasItems(){
        
        List<SelectItem> niveles = new ArrayList<SelectItem>();
        
        for (Long i=1L; i<3; i++) {
            niveles.add(new SelectItem(i, "Nivel "+ i));
        }
        return niveles;
    }
    
    public List<SelectItem> getColumnasDesdeAgregadaItems(){        
        if(columnasDesdeAgregadas==null){
            columnasDesdeAgregadas = new ArrayList<SelectItem>();
        }
        return columnasDesdeAgregadas;
    }
    
    public List<SelectItem> getColumnasHastaAgregadaItems(){        
        if(columnasHastaAgregadas==null){
            columnasHastaAgregadas = new ArrayList<SelectItem>();
        }
        return columnasHastaAgregadas;
    }
    
    
    public void setColumnasDesdeAgregadaItems(List<SelectItem> items){
        this.columnasDesdeAgregadas = items;
    }
    
    public void setColumnasHastaAgregadaItems(List<SelectItem> items){
        this.columnasHastaAgregadas = items;
    }
    
    public Map<Long, GrillaModelVO> getGrillaModelMap() {
        Map<Long, GrillaModelVO> grillaModelMap = getGeneradorDiseno().getGrillaModelMap();
        return grillaModelMap;
    }
    
    public void setDisenoEstructuraTable(RichTable disenoEstructuraTable) {
        this.disenoEstructuraTable = disenoEstructuraTable;
    }

    public RichTable getDisenoEstructuraTable() {
        return disenoEstructuraTable;
    }

    public void setGrillaSelected(Long grillaSelected) {
        this.grillaSelected = grillaSelected;
    }

    public Long getGrillaSelected() {
        return grillaSelected;
    }

    public void setEditarGrillaTable(RichTable editarGrillaTable) {
        this.editarGrillaTable = editarGrillaTable;
    }

    public RichTable getEditarGrillaTable() {
        return editarGrillaTable;
    }

    public Long getWidthEditarGrillaTable() {
        return widthEditarGrillaTable;
    }
    
    public void setTableModel(TableModelVO tableModel) {
        this.tableModel = tableModel;
    }

    public TableModelVO getTableModel() {
        return tableModel;
    }

    public void setNivelDesdeChoice(RichSelectOneChoice nivelDesdeChoice) {
        this.nivelDesdeChoice = nivelDesdeChoice;
    }

    public RichSelectOneChoice getNivelDesdeChoice() {
        return nivelDesdeChoice;
    }

    public void setNivelHastaChoice(RichSelectOneChoice nivelHastaChoice) {
        this.nivelHastaChoice = nivelHastaChoice;
    }

    public RichSelectOneChoice getNivelHastaChoice() {
        return nivelHastaChoice;
    }

    public void setRenderedTableAgrupador(boolean renderedTabbleAgrupador) {
        this.renderedTableAgrupador = renderedTabbleAgrupador;
    }

    public boolean isRenderedTableAgrupador() {
        return renderedTableAgrupador;
    }
}
