package cl.mdr.ifrs.ejb.service.test;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.index;
import static ch.lambdaj.Lambda.max;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.equalTo;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol;

import cl.mdr.ifrs.ejb.common.TipoCeldaEnum;
import cl.mdr.ifrs.ejb.common.TipoDatoEnum;
import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.AgrupacionColumna;
import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.ifrs.ejb.entity.Columna;
import cl.mdr.ifrs.ejb.entity.Grilla;
import cl.mdr.ifrs.ejb.entity.TipoCelda;
import cl.mdr.ifrs.ejb.entity.TipoDato;
import cl.mdr.ifrs.exceptions.CargaGrillaExcelException;

public class CargadorEstructuraServiceTest {
	
	private static InputStream loadedExcel;
	private static XSSFWorkbook workBook;
	
	@Before
	public void init(){
		try {
			loadedExcel = CargadorEstructuraServiceTest.class.getResourceAsStream("/load-excel/config-nota.xlsx");						
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}
	
	@After
	public void tearDown(){
		loadedExcel = null;
		workBook = null;
	}
	
	@Test
	public void testCargadorEstructura(){
		try{		
			Grilla grilla = this.getGrillaByExcel(loadedExcel);
			System.out.println(grilla.getColumnaList());
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testFilasCabecera() throws Exception{
		workBook = new XSSFWorkbook(loadedExcel);
		for(Long fila : this.getFilasCabecera(workBook.getSheetAt(0))){
			System.out.println(fila);
		}
	}
	
	@Test
	public void testMergedArea() throws Exception{
		workBook = new XSSFWorkbook(loadedExcel);
		XSSFSheet sheet = workBook.getSheetAt(0); 
		for (int i = 0; i < sheet.getNumMergedRegions(); i++) {           
	        CellRangeAddress mRegion = sheet.getMergedRegion(i); 
	        int fromRow = mRegion.getFirstRow();
	        int toRow = mRegion.getLastRow();
	        int fromCol = mRegion.getFirstColumn();
	        int toCol = mRegion.getLastColumn();
	        System.out.println(fromRow + " "+ toRow + " " + fromCol + " "+ toCol + " celdas: "+mRegion.getNumberOfCells());
		}
	}
	
	@Test
	public void testColumnasAnidadas() throws Exception{
		workBook = new XSSFWorkbook(loadedExcel);
		XSSFSheet sheet = workBook.getSheetAt(0); 
		List<Columna> columnas = this.getColumnasBySheet(sheet);
		final Map<Long, Columna> columnaMap = index(columnas, on(Columna.class).getIdColumna());
		/*List<AgrupacionColumna> ac =*/ this.getAgrupacionByColumna(sheet, columnaMap);
		//System.out.println(ac);		
		for(Columna columna : columnas){
			System.out.println(columna.getAgrupacionColumnaList());
		}
 	}
	
	/**
	 * @param sheet
	 * @param cellRangeAddress
	 * @return Cell
	 */
	public Cell getCellByReference(final XSSFSheet sheet, final CellRangeAddress cellRangeAddress){		
		final XSSFRow row;  
        XSSFCell cell = null;                  					
        row = sheet.getRow(cellRangeAddress.getFirstRow());
        cell = row.getCell((short)cellRangeAddress.getFirstColumn());                    
        if (cell != null) {
        	return cell;                  	                    	                   	                   
        }                					
		return null;
	}
	
	
	/**
	 * Obtiene un listado de las celdas combinadas de la hoja.
	 * @param sheet
	 * @return List<CellRangeAddress>
	 */
	public List<CellRangeAddress> getCellRangeAddresses(final XSSFSheet sheet){
		List<CellRangeAddress> cellRangeAddresses = new ArrayList<CellRangeAddress>();
		for (int i = 0; i < sheet.getNumMergedRegions(); i++) {           
	        cellRangeAddresses.add(sheet.getMergedRegion(i));
		}
		return cellRangeAddresses;
	}
	
	/**
	 * Metodo que obtiene el listado de agrupaciones correspondientes a la Columna
	 * @param columna
	 * @return
	 */
	public List<AgrupacionColumna> getAgrupacionByColumna(final XSSFSheet sheet, final Map<Long, Columna> columnaMap){
		List<AgrupacionColumna> agrupacionColumnas = new ArrayList<AgrupacionColumna>();
		AgrupacionColumna agrupacionColumna = null;						
		Long grupo = 1L;		
		for(CellRangeAddress cellRangeAddress : this.getCellRangeAddresses(sheet)){			
			for(int i=cellRangeAddress.getFirstColumn()+1; i<=cellRangeAddress.getLastColumn()+1; i++){
				Columna columna = columnaMap.get(new Long(i));
				agrupacionColumna = new AgrupacionColumna();
				agrupacionColumna.setIdNivel(new Long(cellRangeAddress.getFirstRow()+1));
				agrupacionColumna.setColumna(columna);
				agrupacionColumna.setTitulo(this.getCellByReference(sheet, cellRangeAddress).getStringCellValue());
				agrupacionColumna.setIdColumna(columna.getIdColumna());
				agrupacionColumna.setIdGrilla(columna.getIdGrilla());
				agrupacionColumna.setGrupo(grupo);
				agrupacionColumnas.add(agrupacionColumna);					
			}			
			grupo++;						
		}		
		return agrupacionColumnas;
	}
			
	/**
	 * Metodo que obtiene un listado con los numeros de fila correspondientes a las cabeceras de la tabla.
	 * @param sheet
	 * @return
	 */
	public Set<Long> getFilasCabecera(final XSSFSheet sheet){
		Set<Long> headerRows = new HashSet<Long>();
		XSSFRow row;  
        XSSFCell cell; 
        int rows; 
        rows = sheet.getPhysicalNumberOfRows();         
        int cols = 0;  
        int tmp = 0; 
        
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
                    	if(cell.getCellStyle().getFillBackgroundColorColor() != null){                    		
                    		headerRows.add(new Long(cell.getRowIndex()));
                    	}                    	                    	                   	                   
                    }
                }
            }
        }		
		return headerRows;
	}
	
	/**
     * Obtiene un listado de las columnas de la hoja excel
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
        Long columnRow = max(this.getFilasCabecera(sheet));

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
                        if (columnRow != null && r1 == columnRow.intValue()) {
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
        List<AgrupacionColumna> agrupacionColumnasByColumna = new ArrayList<AgrupacionColumna>();
        
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
        
        columnaList = this.getColumnasBySheet(sheet);
        
        if(columnaList.isEmpty()){
            throw new CargaGrillaExcelException("El documento Excel no contiene columnas");     
        }
        
        final Map<Long, Columna> columnaMap = index(columnaList, on(Columna.class).getIdColumna());
		
        final List<AgrupacionColumna> agrupacionColumnas = this.getAgrupacionByColumna(sheet, columnaMap);
        
        for(int i = 0; i < 10 || i < rows; i++) {  
            row = sheet.getRow(i);            
            if(row != null) {  
                tmp = sheet.getRow(i).getPhysicalNumberOfCells();              
                if(tmp > cols) cols = tmp;                  
            }  
        }
        
        for(Columna columna : columnaList){
            List<Celda> celdaList = new ArrayList<Celda>();
            agrupacionColumnasByColumna = select(agrupacionColumnas, having(on(AgrupacionColumna.class).getIdColumna(), equalTo(columna.getIdColumna())));
            columna.setAgrupacionColumnaList(agrupacionColumnasByColumna);
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
                                    celda.setTipoDato(new TipoDato(TipoDatoEnum.TEXTO.getKey()));
                                    celda.setTipoCelda(new TipoCelda(TipoCeldaEnum.TEXTO.getKey()));
                                }                            
                                if(cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC){
                                    if(DateUtil.isCellDateFormatted(cell)){
                                        celda.setValor(""+cell.getDateCellValue());  
                                        celda.setValorDate(cell.getDateCellValue());
                                        celda.setTipoDato(new TipoDato(TipoDatoEnum.FECHA.getKey()));
                                        celda.setTipoCelda(new TipoCelda(TipoCeldaEnum.TEXTO_EDITABLE.getKey()));
                                    }else{
                                        celda.setValor(""+new BigDecimal(cell.getNumericCellValue()));  
                                        celda.setTipoDato(new TipoDato(TipoDatoEnum.ENTERO.getKey()));
                                        celda.setTipoCelda(new TipoCelda(TipoCeldaEnum.NUMERO.getKey()));
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

}
