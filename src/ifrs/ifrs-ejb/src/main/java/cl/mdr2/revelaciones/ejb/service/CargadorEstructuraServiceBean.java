package cl.bicevida.revelaciones.ejb.service;


import cl.bicevida.revelaciones.ejb.common.TipoCeldaEnum;
import cl.bicevida.revelaciones.ejb.common.TipoDatoEnum;
import cl.bicevida.revelaciones.ejb.cross.SortHelper;
import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.Columna;
import cl.bicevida.revelaciones.ejb.entity.Grilla;
import cl.bicevida.revelaciones.ejb.entity.TipoCelda;
import cl.bicevida.revelaciones.ejb.entity.TipoDato;
import cl.bicevida.revelaciones.ejb.facade.local.FacadeServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.CargadorEstructuraServiceLocal;
import cl.bicevida.revelaciones.exceptions.CargaGrillaExcelException;
import cl.bicevida.revelaciones.vo.GrillaVO;

import java.io.InputStream;

import java.math.BigDecimal;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol;


@Stateless
public class CargadorEstructuraServiceBean implements CargadorEstructuraServiceLocal {
    
    @EJB
    FacadeServiceLocal facade;
    
    private Map<Long, TipoCelda> tipoCeldaMap;
    private Map<Long, TipoDato> tipoDatoMap;

    public CargadorEstructuraServiceBean(){
        super();        
    }
        
    @PostConstruct
    void init(){
        this.loadTipoDato();
        this.loadTipoCelda();
    }
    
    /**
     * carga una coleccion map con los tipos desde la base de datos
     * para evitar problema de cascada en las relaciones de tipos
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    private void loadTipoCelda(){
        Map<Long, TipoCelda> tipoCeldaMap = new HashMap<Long, TipoCelda>();
        for(TipoCelda tipoCelda : facade.getMantenedoresTipoService().findAllTipoCelda()){
            tipoCeldaMap.put(tipoCelda.getIdTipoCelda(), tipoCelda);
        }
        this.setTipoCeldaMap(tipoCeldaMap);
    }

     /**
      * carga una coleccion map con los tipos desde la base de datos
      * para evitar problema de cascada en las relaciones de tipos
      */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    private void loadTipoDato(){
        Map<Long, TipoDato> tipoDatoMap = new HashMap<Long, TipoDato>();
        for(TipoDato tipoDato : facade.getMantenedoresTipoService().findAllTipoDato()){
            tipoDatoMap.put(tipoDato.getIdTipoDato(), tipoDato);
        }
        this.setTipoDatoMap(tipoDatoMap);
    }
    
    /**
     * obtiene un listado de las columnas de la hoja excel
     * @param sheet
     * @return
     * @throws Exception
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Columna> getColumnasBySheet(final XSSFSheet sheet)throws Exception{
        XSSFRow row;  
        XSSFCell cell; 
        int rows; 
        rows = sheet.getPhysicalNumberOfRows();         
        int cols = 0;  
        int tmp = 0; 
        List<Columna> columnaList = new ArrayList<Columna>();
        
        for(int i = 0; i < 10 || i < rows; i++) {  
            row = sheet.getRow(i);            
            if(row != null) {  
                tmp = sheet.getRow(i).getPhysicalNumberOfCells();              
                if(tmp > cols) cols = tmp;                  
            }  
        }
        
        for (int r1 = 0; r1 < rows; r1++) {
            row = sheet.getRow(r1);
            if (row != null) {
                for (int c = 0; c < cols; c++) {
                    cell = row.getCell((short)c);
                    if (cell != null) {
                        if (r1 == sheet.getFirstRowNum()) {
                            CTCol ctCol =
                            sheet.getColumnHelper().getColumn(Util.getLong(cell.getColumnIndex(), new Long(0)), true);
                            Columna columna = new Columna();
                            final Long idColumna = (Util.getLong(cell.getColumnIndex(), new Long(0)) + 1L);
                            columna.setIdColumna(idColumna);
                            columna.setOrden(idColumna);                                                                                    
                            if(cell.getCellType() == XSSFCell.CELL_TYPE_STRING){
                                columna.setTituloColumna(cell.getStringCellValue());                                
                            }                            
                            if(cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC){
                                if(DateUtil.isCellDateFormatted(cell)){
                                    columna.setTituloColumna(""+cell.getDateCellValue());                                        
                                }else{
                                    columna.setTituloColumna(""+Math.round(cell.getNumericCellValue()));                                        
                                }                                
                            }          
                            columna.setAncho(ctCol != null ? Math.round(Util.getDouble(ctCol.getWidth(), new Double(50)) * 10) : 200);                           
                            columnaList.add(columna);
                        }
                    }
                }
            }
        } 
        return columnaList;
    }

    /**
     * genera una Grilla con listado de columnas y celdas desde un archivo excel .xlsx
     * @param loadedExcel
     * @return
     * @throws Exception
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Grilla getGrillaByExcel(final InputStream loadedExcel)throws Exception, CargaGrillaExcelException {
        final XSSFWorkbook workBook = new XSSFWorkbook(loadedExcel);
        XSSFSheet sheet = workBook.getSheetAt(0);
        XSSFRow row;  
        XSSFCell cell; 
        Grilla grilla = new Grilla();
        List<Columna> columnaList = new ArrayList<Columna>();
        
        if(sheet == null){            
            throw new CargaGrillaExcelException("El documento Excel no contiene datos");            
        }
        
        int rows; 
        rows = sheet.getPhysicalNumberOfRows();
        
        if(rows == 0){
            throw new CargaGrillaExcelException("El documento Excel no contiene filas");     
        }
        
        int cols = 0;  
        int tmp = 0; 
        
        columnaList = getColumnasBySheet(sheet);
        
        if(columnaList.isEmpty()){
            throw new CargaGrillaExcelException("El documento Excel no contiene columnas");     
        }
        
        for(int i = 0; i < 10 || i < rows; i++) {  
            row = sheet.getRow(i);            
            if(row != null) {  
                tmp = sheet.getRow(i).getPhysicalNumberOfCells();              
                if(tmp > cols) cols = tmp;                  
            }  
        }
        
        for(Columna columna : columnaList){
            List<Celda> celdaList = new ArrayList<Celda>();
            for(int r1 = 0; r1 <= rows+1; r1++) {  
                row = sheet.getRow(r1);  
                if(row != null) {  
                    for(int c = 0; c < cols; c++) {  
                        cell = row.getCell((short)c);
                        if(cell != null){
                            if(columna.getIdColumna().equals(Util.getLong((cell.getColumnIndex()+1), null)) && row.getRowNum() != sheet.getFirstRowNum()){
                                Celda celda = new Celda();
                                celda.setColumna(columna);
                                celda.setIdColumna(columna.getIdColumna());
                                celda.setIdFila(Util.getLong((row.getRowNum()+1), new Long(0)));                            
                                if(cell.getCellType() == XSSFCell.CELL_TYPE_STRING){
                                    celda.setValor(cell.getStringCellValue());  
                                    celda.setTipoDato(this.getTipoDatoMap().get(TipoDatoEnum.TEXTO.getKey()));
                                    celda.setTipoCelda(this.getTipoCeldaMap().get(TipoCeldaEnum.TEXTO.getKey()));
                                }                            
                                if(cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC){
                                    if(DateUtil.isCellDateFormatted(cell)){
                                        celda.setValor(""+cell.getDateCellValue());  
                                        celda.setValorDate(cell.getDateCellValue());
                                        celda.setTipoDato(this.getTipoDatoMap().get(TipoDatoEnum.FECHA.getKey()));
                                        celda.setTipoCelda(this.getTipoCeldaMap().get(TipoCeldaEnum.TEXTO_EDITABLE.getKey()));
                                    }else{
                                        celda.setValor(""+new BigDecimal(cell.getNumericCellValue()));  
                                        celda.setTipoDato(this.getTipoDatoMap().get(TipoDatoEnum.ENTERO.getKey()));
                                        celda.setTipoCelda(this.getTipoCeldaMap().get(TipoCeldaEnum.NUMERO.getKey()));
                                    }                                
                                }                                                                                                                      
                                celdaList.add(celda);
                            }
                        }
                    }
                }
            }
            columna.setCeldaList(celdaList);
        }        
        grilla.setColumnaList(columnaList);        
        return grilla;
        }
    
    /**
     * genera una Grilla con listado de columnas y celdas desde un archivo excel .xlsx
     * @param loadedExcel
     * @return
     * @throws Exception
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Grilla getGrillaByExcelLoader(final Grilla gridE, final InputStream loadedExcel,final Long idGrilla,final Long idFila)throws Exception, CargaGrillaExcelException {
        
        Grilla grid = Grilla.clone(gridE);
        
        final XSSFWorkbook workBook = new XSSFWorkbook(loadedExcel);
        XSSFSheet sheet = workBook.getSheetAt(0);
        XSSFRow row;  
        XSSFCell cell; 
        List<Columna> columnaList = new ArrayList<Columna>();
        
        if(sheet == null){            
            throw new CargaGrillaExcelException("El documento Excel no contiene datos");            
        }
        
        int rows;
        rows = sheet.getPhysicalNumberOfRows();
        
        if(rows == 0){
            throw new CargaGrillaExcelException("El documento Excel no contiene filas");     
        }
        
        int cols = 0;
        int colsSize = 0;
        boolean isDinamic=false;
        
        columnaList = getColumnasBySheet(sheet);
        
        if(columnaList.isEmpty()){
            throw new CargaGrillaExcelException("El documento Excel no contiene columnas");     
        }
        
        row = sheet.getRow(0);            
        if(row != null) {  
            cols = sheet.getRow(0).getPhysicalNumberOfCells();              
        }
        
        //Grilla grid = facade.getGrillaService().findGrillaById(idGrilla);
        SortHelper.sortColumnasByOrden(grid.getColumnaList());
        
        List<Celda> cells = getCellsByFila(grid.getColumnaList(),idFila);
        
        if(grid==null || grid.getColumnaList()==null){
            throw new CargaGrillaExcelException("No existe grilla en base de datos");
        }
        
        List<Columna> columns = grid.getColumnaList();        
        colsSize = columns.size();
        Columna lastColumn = grid.getColumnaList().get(colsSize-1);
        Long idLastColumn = 0L;
        
        if(lastColumn.getTituloColumna().replace(" ", "") .equalsIgnoreCase("(+)(-)")){
            colsSize-=1;
            isDinamic = true;
            idLastColumn = lastColumn.getIdColumna();
        }
        
        if(colsSize != cols){
            throw new CargaGrillaExcelException("La cantidad de columna no coincide con diseño de grilla almacenado");
        }
        
        List<String> detailErrors = new ArrayList<String>();
        detailErrors.add("La grilla no puede tener celdas vacías en :");
        Map<Long, List<Celda>> mapCelda = new LinkedHashMap<Long, List<Celda>>();
        for(int posColumn = 0; posColumn < cols; posColumn++) {
            //System.out.println("posColumn ->"+posColumn);
            List<Celda> cellAdd = new ArrayList<Celda>();
            Celda cellData = cells.get(posColumn);
            for(int posfila=0; posfila<=rows-1; posfila++){
                //System.out.println("posfila ->"+posfila);                
                row = sheet.getRow(posfila);
                cell = row.getCell(posColumn);
                if(cell==null){
                    detailErrors.add("Columa->" + columns.get(posColumn).getTituloColumna() + " Fila->" + posfila);
                }else{
                    String value = "";
                    try{
                        if(cell.getCellType() == XSSFCell.CELL_TYPE_STRING){
                            value = cell.getStringCellValue();  
                        }else if(cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC){
                            if(DateUtil.isCellDateFormatted(cell)){
                                SimpleDateFormat formato = new SimpleDateFormat("yyyyMMdd");
                                value = formato.format(cell.getDateCellValue());
                            }else{
                                if(cellData.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.ENTERO.getKey()))
                                    value = ""+ Math.round(Double.valueOf(cell.getNumericCellValue()));
                                else if(cellData.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.DECIMAL.getKey()))
                                    value = ""+ cell.getNumericCellValue();
                            }
                        }
                    }catch(NullPointerException e){
                        detailErrors.add("Columa->" + columns.get(posColumn).getTituloColumna() + " Fila->" + posfila);
                    }
                    //System.out.println("value ->"+value);
                    Celda celda = Celda.clone(cellData);
                    celda.setValor(value);
                    cellAdd.add(celda);
                }
            }
            mapCelda.put(Integer.valueOf(posColumn+1).longValue(), cellAdd);
        }
        
        if(detailErrors.size()>1){
            throw new CargaGrillaExcelException("La cantidad de columna no coincide con diseño de grilla almacenado",detailErrors);
        }

        List<Celda> cellsNew;
        List<Celda> cellLinkNew = new ArrayList<Celda>();
        Map<Long, List<Celda>> cellNewMap = new HashMap<Long,List<Celda>>();
        for(int i=0; i<colsSize; i++){
            boolean entra = false;
            List<Celda> cellsLoader = mapCelda.get(columns.get(i).getIdColumna());
            int j=0;
            cellsNew = new ArrayList<Celda>();
            for(Celda cellGrid : columns.get(i).getCeldaList()){
                if(cellGrid.getIdFila().equals(idFila)){
                    entra = true;
                    for(Celda cellTemp : cellsLoader){
                        cellsNew.add(cellTemp);
                        if(i==colsSize-1 && isDinamic){
                            Celda cellLink = Celda.clone(cellTemp);
                            cellLink.setIdColumna(idLastColumn);
                            cellLink.setTipoCelda(getTipoCeldaMap().get(TipoCeldaEnum.LINK.getKey()));
                            cellLink.setTipoDato(getTipoDatoMap().get(TipoDatoEnum.TEXTO.getKey()));
                            cellLinkNew.add(cellLink);
                        }
                    }
                }else{
                    if(entra){
                        if(!isLinkRow(columns, cellGrid.getIdFila())){
                            entra = false;
                            cellsNew.add(Celda.clone(cellGrid));
                            if(i==colsSize-1 && isDinamic){
                                cellLinkNew.add(Celda.clone(columns.get(colsSize).getCeldaList().get(j)));
                            }
                        }
                    }else{
                        cellsNew.add(Celda.clone(cellGrid));
                        if(i==colsSize-1 && isDinamic){
                            cellLinkNew.add(Celda.clone(columns.get(colsSize).getCeldaList().get(j)));
                        }
                    }
                }
                j++;
            }
            cellNewMap.put(columns.get(i).getIdColumna(), cellsNew);
            
            if(i==colsSize-1 && isDinamic){
                cellNewMap.put(idLastColumn, cellLinkNew);
            }
            
        }
        
        for(Columna column : grid.getColumnaList()){
            if(cellNewMap.containsKey(column.getIdColumna())){
                List<Celda> cellsTemp = cellNewMap.get(column.getIdColumna());
                //List<Celda> cellsNew = new ArrayList<Celda>();
                Long i=1L;
                for(Celda cellTemp : cellsTemp){
                    //Celda cellStep = Celda.clone(cellTemp);
                    cellTemp.setIdFila(i);
                    i++;
                    //cellsNew.add(cellStep);
                    //System.out.println("COLUMNA->"+cellTemp.getIdColumna()+" FILA->" + cellTemp.getIdFila() + " VALOR->" + cellTemp.getValor() + " DATO->"+cellTemp.getTipoDato().getIdTipoDato()+" CELDA->"+cellTemp.getTipoCelda().getIdTipoCelda());
                }
                column.setCeldaList(cellsTemp);
            }
        }
        return grid;
    }
    
    private List<Celda> getCellsByFila(List<Columna> columns, Long fila){
        List<Celda> cells = new ArrayList<Celda>();
        for(Columna column : columns){
            for(Celda cell : column.getCeldaList()){
                if(cell.getIdFila().equals(fila)){
                    cells.add(cell);
                    break;
                }
            }
        }
        return cells;
    }
    
    private boolean isLinkRow(List<Columna> columns, Long idFila){
        boolean isLink = false;
        for(Celda cellGrid : columns.get(columns.size()-1).getCeldaList()){
            if(cellGrid.getIdFila().equals(idFila)){
                if(cellGrid.getTipoCelda().getIdTipoCelda().equals(TipoCeldaEnum.LINK.getKey())){
                    isLink =  true;
                    break;
                }
            }
        }
        return isLink;
    }
    
    /**
     * @param version
     * @return
     * @throws Exception
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    private Map<String, Map<Long,Celda>> getDataCell(final Grilla grid, final Long idGrilla) throws Exception {
        
        if(grid.getTipoFormula().equals(Grilla.TIPO_GRILLA_DINAMICA)){
            int sequenceChild = 1;
            int sequenceParent = 1;
            /*representa Grupo<idColumna, Celda>*/
            Map<String, Map<Long,Celda>> dataGroupMap = new LinkedHashMap<String, Map<Long,Celda>>();
            Map<Long,Celda> dataCellMap = null;
            String prefix;
            String value;
            /*for(Columna column : grid.getColumnaList()){
                for(Celda cell : column.getCeldaList()){
                    
                    if(cell.getGrupo()==null || cell.getGrupoResultado()==null)
                        continue;
                    
                    if(cell.getGrupoResultado()==null){
                        value = Constantes.PREFIX_CHILD+","+sequenceChild;
                        sequenceChild++;
                    }else if(cell.getGrupo()==null){
                        value = Constantes.PREFIX_PARENT+","+cell.getGrupoResultado();
                        sequenceParent++;
                    }else{
                        value = Constantes.PREFIX_CHILD_PARENT+","+cell.getGrupo()+","+cell.getGrupoResultado();
                        sequenceChild++;
                        sequenceParent++;
                    }
                    if(!dataGroupMap.containsKey(value)){                        
                        dataCellMap = new LinkedHashMap<Long,Celda>();
                        dataCellMap.put(cell.getIdColumna(), cell); 
                        dataGroupMap.put(value, dataCellMap);
                    }else{
                        if(!dataCellMap.containsKey(cell.getIdColumna())){
                            dataCellMap.put(cell.getIdColumna(), cell);
                        }
                    }
                }
            }*/
            return dataGroupMap;
        }else{
            return null;
        }
    }

    /**
     * @param grilla
     * @return
     * @throws Exception
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public GrillaVO getGrillaVOByExcel(final Grilla grilla) throws Exception{
        
        List<Celda> celdas = new ArrayList<Celda>();
        
        celdas = facade.getEstructuraService().getCeldasFromGrilla(grilla);

        GrillaVO grillaNotaVO = new GrillaVO();
        List<Columna> columnaList = grilla.getColumnaList();        
        List<Map<Long,Celda>> rows = new ArrayList<Map<Long,Celda>>();
        Map<Long,Celda> celdaMap = new LinkedHashMap<Long,Celda>();
        
        boolean listaVacia = true;
        
        SortHelper.sortColumnasByOrden(columnaList);

        for(Columna columnaNota : columnaList){
            
            if(columnaNota.getCeldaList()==null)
                continue;
            
            int j=0;
            for(Celda celda : celdas){
                if(celda.getIdColumna().equals(columnaNota.getIdColumna())){
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
            }
            listaVacia = false;
        }
        
        grillaNotaVO.setColumnas(columnaList); 
        grillaNotaVO.setGrilla(grilla);
        grillaNotaVO.setRows(rows);
        return grillaNotaVO;
    }


    public void setTipoCeldaMap(Map<Long, TipoCelda> tipoCeldaMap) {
        this.tipoCeldaMap = tipoCeldaMap;
    }

    public Map<Long, TipoCelda> getTipoCeldaMap() {
        return tipoCeldaMap;
    }

    public void setTipoDatoMap(Map<Long, TipoDato> tipoDatoMap) {
        this.tipoDatoMap = tipoDatoMap;
    }

    public Map<Long, TipoDato> getTipoDatoMap() {
        return tipoDatoMap;
    }
}
