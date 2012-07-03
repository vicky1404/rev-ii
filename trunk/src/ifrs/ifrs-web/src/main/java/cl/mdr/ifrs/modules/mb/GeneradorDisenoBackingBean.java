package cl.mdr.ifrs.modules.mb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.model.UploadedFile;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.mb.PropertyManager;
import cl.mdr.ifrs.cross.util.GeneradorDisenoHelper;
import cl.mdr.ifrs.cross.vo.CampoModelVO;
import cl.mdr.ifrs.cross.vo.EdicionVO;
import cl.mdr.ifrs.ejb.cross.SortHelper;
import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.AgrupacionColumna;
import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.ifrs.ejb.entity.Columna;
import cl.mdr.ifrs.ejb.entity.Estructura;
import cl.mdr.ifrs.ejb.entity.Grilla;
import cl.mdr.ifrs.ejb.entity.TipoCelda;
import cl.mdr.ifrs.ejb.entity.TipoDato;
import cl.mdr.ifrs.exceptions.CargaGrillaExcelException;
import cl.mdr.ifrs.vo.AgrupacionColumnaModelVO;
import cl.mdr.ifrs.vo.GrillaModelVO;
import cl.mdr.ifrs.vo.GrillaVO;

@ManagedBean
@ViewScoped
public class GeneradorDisenoBackingBean extends AbstractBackingBean {
	
    private transient Logger logger = Logger.getLogger(GeneradorDisenoBackingBean.class);    
    
    public static final String BEAN_NAME = "generadorDisenoBackingBean";    
    private boolean renderedEditarGrilla = false;
    private boolean renderdEditarGrillaTable = false;
    private boolean renderedTextArea = false;
    private boolean renderedPopUp = false;
    private boolean renderedTextHtml = false;
    private Columna columnaAgregada;
    private Long filaSelected = 1L;
    private TipoDato tipoDatoSelected;
    private TipoCelda tipoCeldaSelected;
    private transient DataTable editarGrillaTable;
    private transient DataTable disenoEstructuraTable;
    private transient DataTable agregarColumnaTable;
    private transient DataTable editorTable1;
    private transient DataTable editorTable2;
    private transient DataTable editorTable3;
    private transient SelectOneMenu agregarFilaChoice;
    /*contiene el diseño de la grilla*/
    private Map<Long, GrillaModelVO> grillaModelMap;
    /*Contien la estructura para la edicion*/
    private List<CampoModelVO> campoEdicionGrillas;
    private Long grillaSelected = null;
    private Long widthEditarGrillaTable = 10L;
    private GrillaVO tableModel;
    private boolean negrita = false;
    private String tituloGrilla;
    private String tituloTexto;
    private String tituloHtml;
    
    /*atributos utilizados para la upload de archivo*/
    private transient UploadedFile uploadedFile;
    //private transient RichInputFile richInputFile; 

    public GeneradorDisenoBackingBean() {
        super();
    }
    
    public String editarGrillaAction() {
                
        setRenderedPopUp(true);
        setRenderedEditarGrilla(true);
        setRenderdEditarGrillaTable(false);
        setRenderedTextArea(false);
        setRenderedTextHtml(false);
        setColumnaAgregada(null);
        getAgregarFilaChoice().clearInitialState();
        /*Registro seleccionado de tabla*/
        Estructura estructura = (Estructura)getDisenoEstructuraTable().getRowData();
        /*Se setea la grilla que se selecciono de la tabla*/
        if(estructura.getOrden()==null)
            return null;
        setGrillaSelected(estructura.getOrden());
        /*Se setea la codigo de grilla seleccionado*/
        GrillaModelVO grillaModel = getGrillaModelByEstructuraSelected();
        grillaModel.setGrillaSelected(estructura.getOrden());        
        setWidthEditarGrillaTable(15L);
        setFilaSelected(null);
        setCampoEdicionGrillas(null);
        if(grillaModel.getColumnas().size()==0){
            return null;
        }        
        getCampoEdicionGrillas();
        clearTitulos();
        setInitialEditarGrillaTable();        
        addChildrenToEditarGrillaTable(grillaModel.getColumnas());
        setTableModel(createTableModel(grillaModel.getColumnas(),grillaModel.getAgrupacionesMap()));
        procesarColumnas(grillaModel.getColumnas());
        getEditarGrillaTable().setValue(getTableModel(grillaModel).getRows());
        
        /*
         //TODO a
        setADFPartialTarget(getAgregarFilaChoice());
        setADFPartialTarget(getEditorTable1());
        setADFPartialTarget(getEditorTable2());
        setADFPartialTarget(getEditorTable3());  
        getGrillaModelByEstructuraSelected().setWidthEditarGrillaTable(getWidthEditarGrillaTable());
        */
        getFilasAgregadas();
        
        return null;
    }
    
    private void setInitialEditarGrillaTable(){
        getEditarGrillaTable().clearInitialState();
        getEditarGrillaTable().getChildren().clear();
        getEditarGrillaTable().setId("tblD01");
        getEditarGrillaTable().setValue(null);
        setRenderdEditarGrillaTable(true);
    }
    
    private void addChildrenToEditarGrillaTable(List<Columna> columnas){
        
        Long orden = 0L;
        widthEditarGrillaTable = 10L;
        
        for(Columna columna : columnas){
            orden++;  
            if(orden.equals(1L)){
                widthEditarGrillaTable += 53L;
                ValueExpression value = getFacesContext().getApplication().getExpressionFactory()
                                        .createValueExpression(getFacesContext().getELContext(), "#{row["+(columna.getIdColumna())+"].idFila}", Object.class);
                Column column = GeneradorDisenoHelper.getRichColumn("#", 50L, "center", false, GeneradorDisenoHelper.PREFIX_ID_COLUMN_CHILD+"N"+(columna.getIdColumna()-1L));
                //column.setRowHeader(false);
                column.getChildren().add(GeneradorDisenoHelper.getRichOutputText(value));
                getEditarGrillaTable().getChildren().add(column);
            }
            widthEditarGrillaTable += columna.getAncho() + 4L;
            columna.setIdColumna(orden);
            columna.setOrden(orden);
            ValueExpression value = getFacesContext().getApplication().getExpressionFactory()
                                    .createValueExpression(getFacesContext().getELContext(), "#{row["+(columna.getIdColumna())+"].valor}", Object.class);
            Column column = getRichColumn(columna.getTituloColumna(), columna.getAncho(), "left", false, GeneradorDisenoHelper.PREFIX_ID_COLUMN_CHILD+columna.getIdColumna());
            column.getChildren().add(getRichOutputText(value));
            getEditarGrillaTable().getChildren().add(column);
            
        }
        //getEditarGrillaTable().setWidth(widthEditarGrillaTable + "px;");
    }
    
    public String editarHtmlAction(){
        setGrillaSelectedByTable();
        setRenderdEditarGrillaTable(false);
        setRenderedEditarGrilla(false);
        setRenderedPopUp(false);
        setRenderedTextArea(false);
        setRenderedTextHtml(true);
        setTituloHtml(getGrillaModelByEstructuraSelected().getHtml().getTitulo());
        return null;
    }
    
    private void setGrillaSelectedByTable(){
        /*Registro seleccionado de tabla*/
        Estructura estructura = (Estructura)getDisenoEstructuraTable().getRowData();
        
        /*Se setea la grilla que se selecciono de la tabla*/
        setGrillaSelected(estructura.getOrden());
    }
    
    public String editarTextoAction() {
        setGrillaSelectedByTable();
        setRenderdEditarGrillaTable(false);
        setRenderedEditarGrilla(false);
        setRenderedPopUp(false);
        setRenderedTextHtml(false);
        setRenderedTextArea(true);
        setTituloTexto(getGrillaModelByEstructuraSelected().getTexto().getTexto());
        setNegrita(getGrillaModelByEstructuraSelected().getTexto().isNegrita());
        return null;
    }
    
    private Column getRichColumn(String titulo, Long ancho, String alineacion, boolean rowHeader, String id){
        Column column = new Column();
        column.setHeaderText(titulo);
        //column.setAlign(alineacion);
        //column.setRowHeader(rowHeader);
        column.setRendered(true);
        column.setWidth(ancho.intValue());
        column.setId(id);
        return column;
    }
    
    private HtmlOutputText getRichOutputText(ValueExpression value){
    	HtmlOutputText oText = new HtmlOutputText();
        oText.setValueExpression("value", value);
        oText.setRendered(true);
        return oText;
    }
    
    public Object editarColumnaAction() {
        /*TODO a
         * Column column = getRichColumnSelected();
        if(column!=null){
            
            if(column.getHeaderText() != null && column.getHeaderText().equals("#"))
                return null;
            Long valor =null;
            try{
                valor = Long.valueOf(column.getWidth().replaceAll("px;", ""));
                widthEditarGrillaTable -= valor;
            }catch(NumberFormatException e){
                valor =null;
            }
            column.setHeaderText(getColumnaAgregada().getTituloColumna());
            column.setWidth(getColumnaAgregada().getAncho()+"px;");
            column.setRowHeader(getColumnaAgregada().isRowHeader());
            Integer posicion = getPosicionColumnaByColumna(column);
            if(posicion!=null){
                widthEditarGrillaTable += getColumnaAgregada().getAncho();
                Columna columna = getGrillaModelByEstructuraSelected().getColumnas().get(posicion.intValue());
                columna.setTituloColumna(getColumnaAgregada().getTituloColumna());
                columna.setAncho(getColumnaAgregada().getAncho());
                columna.setRowHeader(getColumnaAgregada().isRowHeader());
                setCampoEdicionGrillas(createCampoModel(getFilaSelected()));
            }
            
            setAdfPartialTargetTableEditor();
        }*/
        return null;
    }
    
    public Object eliminarColumnaAction() {
        setRenderdEditarGrillaTable(false);
        Column column = getColumnSelected();        
        if(column!=null){
            Integer posicion = getPosicionColumnaByColumna(column);
            if(posicion!=null){
                getGrillaModelByEstructuraSelected().getAgrupacionesMap().clear();
                getGrillaModelByEstructuraSelected().getNivelesAgregados().clear();
                widthEditarGrillaTable -= (getGrillaModelByEstructuraSelected().getColumnas().get(posicion.intValue()).getAncho()+10);
                getGrillaModelByEstructuraSelected().getColumnas().remove(posicion.intValue());
                setInitialEditarGrillaTable();
                addChildrenToEditarGrillaTable(getGrillaModelByEstructuraSelected().getColumnas());
                getEditarGrillaTable().setValue(createTableModel(getGrillaModelByEstructuraSelected().getColumnas(),getGrillaModelByEstructuraSelected().getAgrupacionesMap()).getRows());
                setCampoEdicionGrillas(createCampoModel(getFilaSelected()));
            }
            setAdfPartialTargetTableEditor();
        }
        return null;
    }
    
    private void setAdfPartialTargetTableEditor(){
        
    	/*TODO a
    	 * setADFPartialTarget(getEditarGrillaTable());
        setADFPartialTarget(getEditorTable1());
        setADFPartialTarget(getEditorTable2());
        setADFPartialTarget(getEditorTable2());*/
    }
    
    private Integer getPosicionColumnaByColumna(Column column){
        
        /*La primera rich column es un correlativo*/
        Integer contador = -1;
        
        for(int i=0; i<getEditarGrillaTable().getChildren().size(); i++){
            if(getEditarGrillaTable().getChildren().get(i) instanceof Column){
                if(getEditarGrillaTable().getChildren().get(i).equals(column)) {
                    return contador;
                }
            }
            contador++;
        }
        
        return null;
    }
    

    public Object agregarColumnaAction() {
        
        if(getGrillaSelected()==null || !getGrillaModelMap().containsKey(getGrillaSelected()))
            return null;
        
        Columna columnaPaso = (Columna)getAgregarColumnaTable().getRowData();
        
        if(columnaPaso==null || columnaPaso.getTituloColumna()==null ||
           columnaPaso.getAncho()==null){
           addWarnMessage("Debe ingresar el Título de la columna y el ancho"); 
            return null;
        }
        
        GrillaModelVO grillaModel = getGrillaModelByEstructuraSelected();
        
        Long idColumna = Integer.valueOf(grillaModel.getColumnas().size()).longValue() + 1L; 
        
        if(grillaModel.getColumnas().size()==0){
            setFilaSelected(1L);
            setInitialEditarGrillaTable();
            widthEditarGrillaTable += 53L;
            ValueExpression value = getFacesContext().getApplication().getExpressionFactory()
                                    .createValueExpression(getFacesContext().getELContext(), "#{row["+(idColumna)+"].idFila}", Object.class);
            Column column = GeneradorDisenoHelper.getRichColumn("#", 50L, "center", false, GeneradorDisenoHelper.PREFIX_ID_COLUMN_CHILD+"N"+(idColumna-1L));
            //column.setRowHeader(true);
            column.getChildren().add(GeneradorDisenoHelper.getRichOutputText(value));
            getEditarGrillaTable().getChildren().add(column);
        }

        Columna columna = new Columna();
        
        columna.setAncho(getColumnaAgregada().getAncho());
        columna.setOrden(getColumnaAgregada().getOrden());
        columna.setTituloColumna(getColumnaAgregada().getTituloColumna());
        
        columna.setIdColumna(idColumna);
        widthEditarGrillaTable += columna.getAncho() + 4L;
        grillaModel.setWidthEditarGrillaTable(widthEditarGrillaTable);
        grillaModel.getColumnas().add(columna); 
        
        
        List<Celda> celdaList = new ArrayList<Celda>();
        celdaList.add(new Celda(idColumna, 1L));
        columna.setCeldaList(celdaList);

        if(grillaModel.getFilas().size()==0){
            grillaModel.getFilas().add(1L);
        }
        if(getFilaSelected()==null){
            setFilaSelected(1L);
        }
        
        grillaModel.getAgrupacionesMap().clear();
        grillaModel.getNivelesAgregados().clear();
        Column column = getRichColumn(columna.getTituloColumna(), columna.getAncho(), 
                                          "center", false, GeneradorDisenoHelper.PREFIX_ID_COLUMN_CHILD+getGrillaSelected()+grillaModel.getColumnas().size());
        columna.setOrden(Integer.valueOf(grillaModel.getColumnas().size()).longValue());
        ValueExpression value = getFacesContext().getApplication().getExpressionFactory()
                                .createValueExpression(getFacesContext().getELContext(), "#{row["+(columna.getIdColumna())+"].valor}", Object.class);
        column.getChildren().add(getRichOutputText(value));
        getEditarGrillaTable().getChildren().add(column);
        //getEditarGrillaTable().setWidth(widthEditarGrillaTable.intValue());
        procesarColumnas(grillaModel.getColumnas());
        setTableModel(createTableModel(grillaModel.getColumnas(),grillaModel.getAgrupacionesMap()));
        getEditarGrillaTable().setValue(getTableModel(grillaModel).getRows());
        setRenderdEditarGrillaTable(true);
        getAgregarFilaChoice().resetValue();
        //TODO
        //setADFPartialTarget(getAgregarFilaChoice());
        //setAdfPartialTargetTableEditor();
        setCampoEdicionGrillas(createCampoModel(getFilaSelected()));
        getCampoEdicionGrillas();
        return null;
    }
    
    public List<Long> getCantidadFilasEdiciones(){
        
        if(getGrillaSelected()==null)
            return new ArrayList<Long>();
        
        return getGrillaModelByEstructuraSelected().getCantidadFilasEdiciones();
    }

    /*TODO a
     * public void editarGrillaTableColumnSelection(ColumnSelectionEvent columnSelectionEvent) {
        Column column = getColumnSelected();
        if(column!=null){
            Columna columna = new Columna();
            columna.setAncho(Long.valueOf(column.getWidth().substring(0,column.getWidth().length()-3)));
            columna.setTituloColumna(column.getHeaderText());
            setColumnaAgregada(columna);
            //TODO a
            //setADFPartialTarget(getAgregarColumnaTable());
        }
    }*/
    
    private Column getColumnSelected(){
        //TODO a
        Column column = null;
        Collection col = null;
        /*Collection col = getEditarGrillaTable().getS
        if(col==null || col.size()==0)
            return null;*/
        Iterator it = col.iterator();
        String columnSelected = (String)it.next();
        columnSelected = columnSelected.substring(columnSelected.indexOf(GeneradorDisenoHelper.PREFIX_ID_COLUMN_CHILD),columnSelected.length());
        for(int i=0; i<getEditarGrillaTable().getChildren().size(); i++){
            if(getEditarGrillaTable().getChildren().get(i) instanceof Column){
            	Column columnPaso = (Column)getEditarGrillaTable().getChildren().get(i);
                if(columnPaso.getId()!=null && columnPaso.getId().equalsIgnoreCase(columnSelected)){
                    column = columnPaso;
                }
            }
        }
        
        return column;
    }
    
    public List<Columna> getColumnasAgregadasSelected(){
        List<Columna> columnas = new ArrayList<Columna>();
        columnas.add(getColumnaAgregada());
        return columnas;
    }

    public List<Columna> getColumnasAgregadas(GrillaModelVO grillaModel){
        
        List<Columna> colums = new ArrayList<Columna>();
        
        if(grillaModel.getColumnas()==null)   
            return colums;
        
        colums.addAll(grillaModel.getColumnas());
        
        return colums;
    }
    
    public List<SelectItem> getFilasAgregadas(){
        
        List<SelectItem> filas = new ArrayList<SelectItem>();

        if(grillaSelected==null)   
            return filas;
        
        if(!getGrillaModelMap().containsKey(getGrillaSelected())){            
            sumarUnaFilaTableModelMap(getGrillaModelByEstructuraSelected(), -1L);
        }
        
        for(Long value : getGrillaModelByEstructuraSelected().getFilas()){
            filas.add(new SelectItem(value,"Fila N° - " + value));
        }
        
        return filas;
    }

    public Object agregarFilaAction() {
        
        if(getFilaSelected()==null)
            addWarnMessage("Debe seleccionar una fila");
        
        if(getGrillaSelected()!=null && getFilaSelected()!=null)   
            sumarUnaFilaTableModelMap(getGrillaModelByEstructuraSelected(), getFilaSelected());
        
        filaSelected++;
        
        setCampoEdicionGrillas(createCampoModel(getFilaSelected()));
        
        createTableModel(getGrillaModelMap().get(grillaSelected).getColumnas(),getGrillaModelByEstructuraSelected().getAgrupacionesMap());
        
        getEditarGrillaTable().setValue(getTableModel(getGrillaModelByEstructuraSelected()).getRows());
        
        //TODO a
        //addPartialTarget(getEditarGrillaTable());
        
        return null;
    }
    
    public Object eliminarFilaAction() {
        
        if(getFilaSelected()==null)
            addWarnMessage("Debe seleccionar una fila");
        
        GrillaModelVO grillaModel = getGrillaModelByEstructuraSelected();
            
        eliminarUnaFilaTableModelMap(grillaModel, getFilaSelected());
        
        if(!getFilaSelected().equals(1L))
            filaSelected--;
        
        setCampoEdicionGrillas(createCampoModel(getFilaSelected()));
        
        createTableModel(grillaModel.getColumnas(),grillaModel.getAgrupacionesMap());
        
        getEditarGrillaTable().setValue(getTableModel(grillaModel).getRows());
        
        //TODO a
        //addPartialTarget(getEditarGrillaTable());
        
        return null;
    }
    
    private void eliminarUnaFilaTableModelMap(GrillaModelVO grillaModelVO, Long filaSelected){
        
        if(grillaModelVO.getFilas()==null || grillaModelVO.getFilas().size()==0 
           || grillaModelVO.getFilas().size()==1 || getFilaSelected()==null)
            return;
        
        grillaModelVO.getFilas().remove(grillaModelVO.getFilas().size()-1);
        
        for(Columna columna : grillaModelVO.getColumnas()){
            if(columna.getCeldaList()!=null && columna.getCeldaList().size() >= filaSelected.intValue()-1){
                columna.getCeldaList().remove(filaSelected.intValue()-1);        
                    Long contador=1L;
                    for(Celda celda : columna.getCeldaList()){
                        celda.setIdFila(contador);
                        contador++;
                    }
            }
            
        }
        
        
    }
    
    private void sumarUnaFilaTableModelMap(GrillaModelVO grillaModelVO, Long filaSelected){
        
        List<Long> filas;
        Celda celdaNueva;
        
        filas = grillaModelVO.getFilas();
        if(filas==null){
            filas = new ArrayList<Long>();
            filas.add(1L);
            
        }else{
            Long numeroFila = Integer.valueOf(filas.size()).longValue();
            filas.add(numeroFila+1L);                
        }
                
        for(Columna columna : grillaModelVO.getColumnas()){
            
            Long contador =1L;            
            
            if(columna.getCeldaList()==null){
                
                columna.setCeldaList(new ArrayList<Celda>());
                celdaNueva = new Celda(columna.getIdColumna(),1L);
                
                if(getTipoDatoSelected()!=null)
                    celdaNueva.setTipoDato(getTipoDatoSelected());
                else
                    celdaNueva.setTipoDato(new TipoDato(TipoDato.TIPO_DATO_TEXTO));
                
                columna.getCeldaList().add(celdaNueva);
                
            }else{
                List<Celda> celdaPasoList = new ArrayList<Celda>();
                for(Celda celda : columna.getCeldaList()){
                    celda.setIdFila(contador);
                    celdaPasoList.add(celda);
                    if(celda.getIdFila().equals(filaSelected)){
                        contador++;
                        celdaNueva = new Celda(columna.getIdColumna(),contador);
                        if(getTipoCeldaSelected()==null)
                            celdaNueva.setTipoCelda(celda.getTipoCelda());
                        else
                            celdaNueva.setTipoCelda(getTipoCeldaSelected());
                        if(getTipoDatoSelected()==null)
                            celdaNueva.setTipoDato(celda.getTipoDato());
                        else
                            celdaNueva.setTipoDato(getTipoDatoSelected());
                        
                        celdaPasoList.add(celdaNueva);
                    }
                    contador++;
                }
                columna.setCeldaList(celdaPasoList);
            }
        }
        
        grillaModelVO.setFilas(filas);
    }

    public Object editarFilaAction() {
        setRenderedPopUp(true);
        getFilasAgregadas();
        getCampoEdicionGrillas();
        //setADFPartialTarget(getEditarFilaPopUp());
        return null;
    }
    
    public void selectFilaChangeListener(ValueChangeEvent valueChangeEvent) {
        
        if(valueChangeEvent.getNewValue()==null)
            return;
        setFilaSelected((Long)valueChangeEvent.getNewValue());
        setCampoEdicionGrillas(createCampoModel(getFilaSelected()));
    }
    
    private List<CampoModelVO> createCampoModel(Long filaSelected){
        
        List<CampoModelVO> campoEdicionGrillas = new ArrayList<CampoModelVO>();
        List<EdicionVO> valorList = new ArrayList<EdicionVO>();
        List<EdicionVO> tipoDatoList = new ArrayList<EdicionVO>();
        List<EdicionVO> tipoCeldaList = new ArrayList<EdicionVO>();
        GrillaModelVO grillaModel = getGrillaModelByEstructuraSelected();
        
        if ( !Util.getLong(filaSelected, -1L).equals(-1L) ) {
        
            GrillaVO grillaVO = getTableModel(grillaModel);
            for (Columna columna : getColumnasAgregadas(grillaModel)) {
                Celda celdaValor = GeneradorDisenoHelper.cloneCelda(getCeldaByColumnaFilaSelected(columna.getIdColumna(), filaSelected,grillaVO));
                valorList.add(new EdicionVO(columna.getTituloColumna(), celdaValor.getValor()==null?"":celdaValor.getValor(), columna.getAncho()));
                tipoDatoList.add(new EdicionVO(columna.getTituloColumna(), celdaValor.getTipoDato(),columna.getAncho()));
                tipoCeldaList.add(new EdicionVO(columna.getTituloColumna(), celdaValor.getTipoCelda(),columna.getAncho()));
            }
            
        } else {
            for (Columna columna : getColumnasAgregadas(grillaModel)) {
                valorList.add(new EdicionVO(columna.getTituloColumna(), "",columna.getAncho()));
                tipoDatoList.add(new EdicionVO(columna.getTituloColumna(), null,columna.getAncho()));
                tipoCeldaList.add(new EdicionVO(columna.getTituloColumna(), null,columna.getAncho()));
            }
        }
        
        campoEdicionGrillas.add(new CampoModelVO(valorList, CampoModelVO.TIPO_COLUMNA));        
        campoEdicionGrillas.add(new CampoModelVO(tipoCeldaList, CampoModelVO.TIPO_CELDA));
        campoEdicionGrillas.add(new CampoModelVO(tipoDatoList, CampoModelVO.TIPO_DATO));
        return campoEdicionGrillas;
    }

    public void setRenderedPopUp(boolean renderedPopUp) {
        this.renderedPopUp = renderedPopUp;
    }

    public boolean isRenderedPopUp() {
        return renderedPopUp;
    }

    public void setCampoEdicionGrillas(List<CampoModelVO> modelGrillas) {
        this.campoEdicionGrillas = modelGrillas;
    }

    public List<CampoModelVO> getCampoEdicionGrillas() {
        
        List<EdicionVO> valorList = new ArrayList<EdicionVO>();
        List<EdicionVO> tipoDatoList = new ArrayList<EdicionVO>();
        List<EdicionVO> tipoCeldaList = new ArrayList<EdicionVO>();
        
        if(campoEdicionGrillas==null){
            campoEdicionGrillas = new ArrayList<CampoModelVO>();
            for (Columna columna : getGrillaModelByEstructuraSelected().getColumnas()) {
                Celda celdaValor = new Celda();
                valorList.add(new EdicionVO(columna.getTituloColumna(), celdaValor.getValor(),columna.getAncho()));
                tipoDatoList.add(new EdicionVO(columna.getTituloColumna(), celdaValor.getTipoDato(),columna.getAncho()));
                tipoCeldaList.add(new EdicionVO(columna.getTituloColumna(), celdaValor.getTipoCelda(),columna.getAncho()));
            }
            campoEdicionGrillas.add(new CampoModelVO(valorList, CampoModelVO.TIPO_COLUMNA));        
            campoEdicionGrillas.add(new CampoModelVO(tipoCeldaList, CampoModelVO.TIPO_CELDA));
            campoEdicionGrillas.add(new CampoModelVO(tipoDatoList, CampoModelVO.TIPO_DATO));
        }
            
        return campoEdicionGrillas;
    }
    
    
    public List<Long> getUnRegistroList(){
        List<Long> lista = new ArrayList<Long>();
        lista.add(1L);
        return lista;
    }

    public void setFilaSelected(Long filaSelected) {
        this.filaSelected = filaSelected;
    }

    public Long getFilaSelected() {
        return filaSelected;
    }

    public void guardarEdicionCeldaByFilaAction(ActionEvent event) {
        
        if(getFilaSelected()==null)
            return;
        
        setRenderedPopUp(false);
        
        /*Recupero el valor de las celda por lista de columnas*/
        CampoModelVO valorGrilla = campoEdicionGrillas.get(0);
        CampoModelVO tipoCeldaGrilla = campoEdicionGrillas.get(1);
        CampoModelVO tipoDatoGrilla = campoEdicionGrillas.get(2);
        
        GrillaModelVO grillaModel = getGrillaModelByEstructuraSelected();
        try{
            for(int i=0; i<valorGrilla.getColumnas().size(); i++){
                
                EdicionVO edicionValor = valorGrilla.getColumnas().get(i);
                EdicionVO edicionDato  = tipoDatoGrilla.getColumnas().get(i);
                EdicionVO edicionCelda = tipoCeldaGrilla.getColumnas().get(i);
                
                Columna columna =  grillaModel.getColumnas().get(i);
                Celda celda = columna.getCeldaList().get((getFilaSelected().intValue()-1));
                
                if(edicionValor.getValor()!=null)
                    celda.setValor((String)edicionValor.getValor());
                if(edicionDato.getValor()!=null)
                    celda.setTipoDato((TipoDato)edicionDato.getValor());
                if(edicionCelda.getValor()!=null)
                    celda.setTipoCelda((TipoCelda)edicionCelda.getValor());
            }
            
            //TODO a
            //setADFPartialTarget(getEditarGrillaTable());
            
        }catch(Exception e){
            logger.error(e);
        }
        
        return;
    }

    public void setGrillaModelMap(Map<Long, GrillaModelVO> tableModelMap) {
        this.grillaModelMap = tableModelMap;
    }

    public Map<Long, GrillaModelVO> getGrillaModelMap() {
        if(grillaModelMap==null){
            grillaModelMap = new LinkedHashMap<Long, GrillaModelVO>();
        }
        return grillaModelMap;
    }
    
    public GrillaVO getTableModel(GrillaModelVO grillaModel){
        
        this.tableModel = createTableModel(grillaModel.getColumnas(),grillaModel.getAgrupacionesMap());
            
        return this.tableModel;
    }
    
    public void setTableModel(GrillaVO tableModel){
        this.tableModel = tableModel;    
    }

    public void setGrillaSelected(Long grillaSelected) {
        this.grillaSelected = grillaSelected;
    }

    public Long getGrillaSelected() {
        return grillaSelected;
    }
    
    public GrillaVO createTableModel(List<Columna> columnasGrilla, Map<Long,List<AgrupacionColumna>> agrupacionesMap) {
        
        List<AgrupacionColumna> agrupacionColumnas = new ArrayList<AgrupacionColumna>();
        SortHelper.sortColumnasByOrden(columnasGrilla);
        boolean listaVacia = true;
        List<Columna> columnas = new ArrayList<Columna>();
        List<Map<Long,Celda>> rows = new ArrayList<Map<Long,Celda>>();
        Map<Long,Celda> celdaMap = new LinkedHashMap<Long,Celda>();
        List<AgrupacionColumnaModelVO> agrupacionesModelVO = new ArrayList<AgrupacionColumnaModelVO>();
        GrillaVO grillaVO = new GrillaVO();
        
        if(agrupacionesMap !=null && !agrupacionesMap.isEmpty()){
            
            Iterator it = agrupacionesMap.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry entry = (Map.Entry)it.next();
                List<AgrupacionColumna> lista = (List<AgrupacionColumna>) entry.getValue();
                if(Util.esListaValida(lista)){
                    agrupacionColumnas.addAll(lista);
                }
            }
        }
            
        agrupacionesModelVO = GeneradorDisenoHelper.soporteAgrupacionColumna(columnasGrilla,agrupacionColumnas);        

        for(Columna columnaNota : columnasGrilla){
            columnas.add(columnaNota);
            if(columnaNota.getCeldaList()==null)
                continue;
            
            int j=0;
            for(Celda celda : columnaNota.getCeldaList()){
                celda.setIdColumna(Integer.valueOf(j).longValue()+1L);
                if(listaVacia){
                    celdaMap = new LinkedHashMap<Long,Celda>();
                    celdaMap.put(new Long(columnaNota.getIdColumna()), celda);
                    rows.add(celdaMap);
                }else{
                    if(rows.size()>j){
                        rows.get(j).put(new Long(columnaNota.getIdColumna()), celda);
                    }
                }
                j++;                    
            }
            listaVacia = false;
        }
        GeneradorDisenoHelper.setListaPorNivelUsado(agrupacionesModelVO, grillaVO);
                
        grillaVO.setColumnas(columnas);
        grillaVO.setRows(rows);
        
        
            
        return grillaVO;
    }

    public void setEditarGrillaTable(DataTable editarGrillaTable) {
        this.editarGrillaTable = editarGrillaTable;
    }

    public DataTable getEditarGrillaTable() {
        return editarGrillaTable;
    }
    
    public Celda getCeldaByColumnaFilaSelected(Long idColuma, Long idFila, GrillaVO grillaVO){
        
        Celda celda = null;
        
        try{
            Map<Long, Celda> celdaMap  =  grillaVO.getRows().get(Long.valueOf(idFila-1).intValue());
            celda = celdaMap.get(idColuma);
        }catch(Exception e){
            celda = new Celda();
        }
        
        return celda;
    }
    
    public void guardarTextoListener(ActionEvent event){
        getGrillaModelByEstructuraSelected().getTexto().setTexto(getTituloTexto());
        getGrillaModelByEstructuraSelected().getTexto().setNegrita(isNegrita());
    }
    
    public void guardarGrilaListener(ActionEvent event){
        getGrillaModelByEstructuraSelected().setTituloGrilla(getTituloGrilla());
    }
    
    public void guardarTextoHtmlListener(ActionEvent event){
        getGrillaModelByEstructuraSelected().getHtml().setTitulo(getTituloHtml());
    }
    
    public GrillaModelVO getGrillaModelByEstructuraSelected(){
        if(getGrillaModelMap().containsKey(getGrillaSelected())){
            return getGrillaModelMap().get(getGrillaSelected());    
        }
        return new GrillaModelVO();
    }
    
    private void procesarColumnas(List<Columna> columnas){
        
        int numCelda=0;
        
        if(columnas==null)
            return;
        
        for(Columna columna : columnas){
            if(columna.getCeldaList()!=null)
                numCelda = columna.getCeldaList().size() > numCelda ? columna.getCeldaList().size() : numCelda;
        }
        
        for(Columna columna : columnas){
            if(columna.getCeldaList()==null){
                columna.setCeldaList(new ArrayList<Celda>());
            }
            if(columna.getCeldaList().size() < numCelda){
                int celdas = numCelda - columna.getCeldaList().size();
                Long idCelda = 0L;
                for(int i=0; i<celdas; i++){
                    idCelda = columna.getCeldaList().size() +1L;
                    columna.getCeldaList().add(new Celda(columna.getIdColumna(),idCelda));
                }
            }
        }
        
    }
    
    private void clearTitulos(){
        setTituloGrilla(getGrillaModelByEstructuraSelected().getTituloGrilla());
        setTituloHtml(getGrillaModelByEstructuraSelected().getHtml().getTitulo());
        setTituloTexto(getGrillaModelByEstructuraSelected().getTexto().getTexto());
        setNegrita(getGrillaModelByEstructuraSelected().getTexto().isNegrita());
    }

    public void setColumnaAgregada(Columna columnaAgregada) {
        this.columnaAgregada = columnaAgregada;
    }

    public Columna getColumnaAgregada() {
        if(columnaAgregada==null){
            columnaAgregada = new Columna();
        }
        return columnaAgregada;
    }

    public void setRenderedTextArea(boolean renderedTextArea) {
        this.renderedTextArea = renderedTextArea;
    }

    public boolean isRenderedTextArea() {
        return renderedTextArea;
    }

    public void setAgregarColumnaTable(DataTable agregarColumnaTable) {
        this.agregarColumnaTable = agregarColumnaTable;
    }

    public DataTable getAgregarColumnaTable() {
        return agregarColumnaTable;
    }
    
    public void setRenderedTextHtml(boolean renderedTextHtml) {
        this.renderedTextHtml = renderedTextHtml;
    }

    public boolean isRenderedTextHtml() {
        return renderedTextHtml;
    }

    //TODO evento
    /*public void desenadorDisclosure(DisclosureEvent disclosureEvent) {
        if(!getGeneradorVersion().isAlmacenado())
             agregarWarnMessage("Recuerde Guardar los cambios para poder Diseñar");
        
        getGeneradorVisualizador().setRenderedVisualizador(false);
        setRenderdEditarGrillaTable(false);
    }*/

    public void setNegrita(boolean negrita) {
        this.negrita = negrita;
    }

    public boolean isNegrita() {
        return negrita;
    }

    public void setTituloGrilla(String tituloGrilla) {
        this.tituloGrilla = tituloGrilla;
    }

    public String getTituloGrilla() {
        if(tituloGrilla==null)
            tituloGrilla = "";
        return tituloGrilla;
    }

    public void setTituloTexto(String tituloTexto) {
        this.tituloTexto = tituloTexto;
    }

    public String getTituloTexto() {
        return tituloTexto;
    }

    public void setTituloHtml(String tituloHtml) {
        this.tituloHtml = tituloHtml;
    }

    public String getTituloHtml() {
        return tituloHtml;
    }


    public void setTipoDatoSelected(TipoDato tipoDatoSelected) {
        this.tipoDatoSelected = tipoDatoSelected;
    }

    public TipoDato getTipoDatoSelected() {
        return tipoDatoSelected;
    }

    public void setTipoCeldaSelected(TipoCelda tipoCeldaSelected) {
        this.tipoCeldaSelected = tipoCeldaSelected;
    }

    public TipoCelda getTipoCeldaSelected() {
        return tipoCeldaSelected;
    }
    
    public void initBackingBean(){
        this.renderedEditarGrilla = false;
        this.renderdEditarGrillaTable = false;
        this.renderedPopUp = false;
        this.columnaAgregada = null;
        this.filaSelected = 1L;
        this.grillaSelected = null;
        this.widthEditarGrillaTable = 10L;
        this.renderedTextArea = false;
        this.columnaAgregada = null;
        this.tituloGrilla = null;
    }

    public void setAgregarFilaChoice(SelectOneMenu agregarFilaChoice) {
        this.agregarFilaChoice = agregarFilaChoice;
    }

    public SelectOneMenu getAgregarFilaChoice() {
        return agregarFilaChoice;
    }

    public void setEditorTable1(DataTable editorTable1) {
        this.editorTable1 = editorTable1;
    }

    public DataTable getEditorTable1() {
        return editorTable1;
    }

    public void setEditorTable2(DataTable editorTable2) {
        this.editorTable2 = editorTable2;
    }

    public DataTable getEditorTable2() {
        return editorTable2;
    }

    public void setEditorTable3(DataTable editorTable3) {
        this.editorTable3 = editorTable3;
    }

    public DataTable getEditorTable3() {
        return editorTable3;
    }
	
	/*carga mediante excel*/

    /**
     * accion encargada de procesar el archivo 
     * @return
     */
    public String procesarArchivo() {                
        try {
            if(this.getUploadedFile() == null){
                addErrorMessage("Seleccione o Actualice el archivo que desea cargar");
                return null;
            }
            if(this.getUploadedFile().getInputstream()  == null){
            	addErrorMessage("Seleccione o Actualice el archivo que desea cargar");
                return null;
            }
            final Grilla grilla = super.getFacadeService().getCargadorEstructuraService().getGrillaByExcel(this.getUploadedFile().getInputstream()); 
            GrillaModelVO grillaModel = getGrillaModelByEstructuraSelected();
            grillaModel.setColumnas(grilla.getColumnaList());
            
            this.clearTitulos();
            this.setInitialEditarGrillaTable();        
            this.addChildrenToEditarGrillaTable(grilla.getColumnaList());
            this.setTableModel(createTableModel(grillaModel.getColumnas(), grillaModel.getAgrupacionesMap()));
            this.procesarColumnas(grilla.getColumnaList());
            this.getEditarGrillaTable().setValue(getTableModel(grillaModel).getRows());
            this.getGrillaModelByEstructuraSelected().setWidthEditarGrillaTable(getWidthEditarGrillaTable());
            this.getGrillaModelByEstructuraSelected().setColumnas(grilla.getColumnaList()); 
            this.getGrillaModelByEstructuraSelected().setFilas(this.getFilasByArchivo(getTableModel(grillaModel).getRows()));
            
            //TODO
            /*this. setADFPartialTarget(getEditarGrillaTable());
            setADFPartialTarget(getAgregarFilaChoice());
            setADFPartialTarget(getEditorTable1());
            setADFPartialTarget(getEditorTable2());
            setADFPartialTarget(getEditorTable3());*/  
            this.getFilasAgregadas();
            this.getCampoEdicionGrillas();
        } catch (CargaGrillaExcelException e) {
            logger.error("error al procesar archivo excel ",e);
            addErrorMessage(e.getMessage());
        
        } catch (Exception e) {
            logger.error("error al procesar archivo excel ",e);
            addErrorMessage("Error al procesar el archivo");
        }
        return null;
    }
    
    private List<Long> getFilasByArchivo(final List<Map<Long, Celda>> rows){
        List<Long> filas = new ArrayList<Long>();
        for(long i = 1 ; i <= rows.size(); i++){
            filas.add(i);
        }
        return filas;
    }
    
    public void archivoEstructuraValidator(FacesContext facesContext, UIComponent uIComponent, Object object){

        try {
            setUploadedFile(GeneradorDisenoHelper.archivoEstructuraValidator(facesContext, (UploadedFile)object));
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            addErrorMessage(PropertyManager.getInstance().getMessage("Error al procesar archivo"));
        }
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }
    
    public void setWidthEditarGrillaTable(Long widthEditarGrillaTable) {
        this.widthEditarGrillaTable = widthEditarGrillaTable;
    }

    public Long getWidthEditarGrillaTable() {
        return widthEditarGrillaTable;
    }
    
    public void setDisenoEstructuraTable(DataTable disenoEstructuraTable) {
        this.disenoEstructuraTable = disenoEstructuraTable;
    }

    public DataTable getDisenoEstructuraTable() {
        return disenoEstructuraTable;
    }
    
    public void setRenderedEditarGrilla(boolean renderedEditarGrilla) {
        this.renderedEditarGrilla = renderedEditarGrilla;
    }

    public boolean isRenderedEditarGrilla() {
        return renderedEditarGrilla;
    }

    public void setRenderdEditarGrillaTable(boolean renderdEditarGrillaTable) {
        this.renderdEditarGrillaTable = renderdEditarGrillaTable;
    }

    public boolean isRenderdEditarGrillaTable() {
        return renderdEditarGrillaTable;
    }
}

