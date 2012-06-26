package cl.mdr.ifrs.ejb.service;


import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cl.mdr.ifrs.ejb.cross.EeffUtil;
import cl.mdr.ifrs.ejb.entity.CodigoFecu;
import cl.mdr.ifrs.ejb.entity.DetalleEeff;
import cl.mdr.ifrs.ejb.entity.EstadoFinanciero;
import cl.mdr.ifrs.ejb.service.local.CargadorEeffServiceLocal;
import cl.mdr.ifrs.ejb.service.local.EstadoFinancieroServiceLocal;
import cl.mdr.ifrs.ejb.service.local.FecuServiceLocal;
import cl.mdr.ifrs.exceptions.EstadoFinancieroException;


@Stateless
public class CargadorEeffServiceBean implements CargadorEeffServiceLocal {
    
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "revelacionesPU")
    private EntityManager em;
    
    @EJB
    private EstadoFinancieroServiceLocal estadoFinancieroService;
    @EJB
    private FecuServiceLocal fecuServiceLocal;
    
    private Map<Long, CodigoFecu> fecuMap;

    public CargadorEeffServiceBean() {
    }
    
    @PostConstruct
    private void init(){
        fecuMap = fecuServiceLocal.getCodigoFecusVigentes();
    }
    
    public Map<Long, EstadoFinanciero> leerEeff(final InputStream loadedExcel)throws EstadoFinancieroException, Exception {
        
        XSSFWorkbook workBook = new XSSFWorkbook(loadedExcel);
        
        List<String> errores = new ArrayList<String>();
        
        Map<Long, EstadoFinanciero> eeffMap = new LinkedHashMap<Long, EstadoFinanciero>();
        
        if(workBook.getSheetAt(0) == null || workBook.getSheetAt(1)==null)
            throw new EstadoFinancieroException("El documento Excel no contiene datos");
        
        if(workBook.getSheetAt(0).getPhysicalNumberOfRows() == 0 || 
           workBook.getSheetAt(1).getPhysicalNumberOfRows() == 0)
            throw new EstadoFinancieroException("El documento Excel no contiene filas");     
        
        leerCabeceraEeff(eeffMap, workBook.getSheetAt(0), fecuMap, errores);
        
        leerDetalleEeff(eeffMap, workBook.getSheetAt(1), errores);
        
        if(errores.size()>0)
            throw new EstadoFinancieroException(errores);
        
        return eeffMap;
    }
    
    public void leerCabeceraEeff(Map<Long, EstadoFinanciero> eeffMap, 
                                 XSSFSheet sheet,
                                 Map<Long,CodigoFecu> fecuMap,
                                 List<String> errores){
        
        XSSFRow row;
        XSSFCell cell;
        
        int cols = 4;
        int rows = sheet.getPhysicalNumberOfRows();
        
        for(int r1 = 1; r1 < rows; r1++) {
            
            row = sheet.getRow(r1);
            EstadoFinanciero eeff = new EstadoFinanciero();
            eeff.setDetalleEeffList4(new ArrayList<DetalleEeff>());
            Long idFecu = null;
            
            if(row != null) {
                for(int c = 0; c < cols; c++) {
                    
                    cell = row.getCell(c);

                    switch(c){
                    
                    case 0: //Codigo Fecu Varchar
                        //eeff.setCodigoFecu(cell.getStringCellValue());
                        break;
                    
                    case 1: //Codigo Fecu Long
                        
                        idFecu = getCodigoFecu(cell, c+1, r1+1, errores);
                        
                        if(!fecuMap.containsKey(idFecu))
                            EeffUtil.addErrorFecuNotFound(c+1, r1+1,errores);
                        else
                            eeff.setCodigoFecu(fecuMap.get(idFecu));
                        
                    break;
                    
                    case 2: //Descrici�n fECU
                        //eeff.setDescripcion(cell.getStringCellValue());
                        break;
                    case 3:
                        eeff.setMontoTotal(getValorBigDecimal(cell));
                        break;
                    }
                }
                
                if(idFecu!=null){
                    eeff.setIdFecu(idFecu);
                    
                    if(eeffMap.containsKey(idFecu)){
                        EeffUtil.addErrorFecuDu(1, r1+1,errores);
                    }else{
                        eeffMap.put(idFecu, eeff);
                    }
                }
            }
        }
        
        
    }
    
    public void leerDetalleEeff(Map<Long, EstadoFinanciero> eeffMap, 
                                XSSFSheet sheet, 
                                List<String> errores){
        
        XSSFRow row;
        XSSFCell cell;
        Map<Long,Long> cuentaMap = new LinkedHashMap<Long,Long>();
        
        int cols = 8;
        int rows = sheet.getPhysicalNumberOfRows();
        
        for(int r1 = 1; r1 < rows; r1++) {
            
            row = sheet.getRow(r1);
            DetalleEeff detalleEeff = new DetalleEeff();
            Long idFecu = null;
                        
            if(row == null){
                EeffUtil.addRowNull(r1+1,errores);
                continue;
            }
                
                
            for(int c = 0; c < cols; c++) {
                cell = row.getCell(c);
                switch(c){
                case 0: //Codigo Fecu Long
                    
                    idFecu = getCodigoFecu(cell, c+1, r1+1, errores);
                    
                    break;
                
                case 1: //Descripcion fecu

                    break;
                
                case 2: //Numero cuenta
                    try{
                        Long idCuenta = ((Double)cell.getNumericCellValue()).longValue();
                        
                        if(cuentaMap.containsKey(idCuenta))
                            EeffUtil.addErrorCuantaDu(c+1, r1+1,errores);
                        else
                            cuentaMap.put(idCuenta, idCuenta);
                        
                        detalleEeff.setIdCuenta(idCuenta);
                    }catch(Exception e){
                        EeffUtil.addErrorCuentaNull(c+1, r1+1,errores);
                    }
                    break;
                
                case 3:
                    try{
                        detalleEeff.setDescripcionCuenta(cell.getStringCellValue());
                    }catch(Exception e){
                        detalleEeff.setDescripcionCuenta("");
                    }
                    break;
                
                case 4:
                    detalleEeff.setMontoEbs(getValorBigDecimal(cell));
                    break;
                
                case 5:
                    detalleEeff.setMontoReclasificacion(getValorBigDecimal(cell));
                    break;
                
                case 6:
                    detalleEeff.setMontoPesos(getValorBigDecimal(cell));
                    break;
                
                case 7:
                    detalleEeff.setMontoMiles(getValorBigDecimal(cell));
                    break;
                
                }
            }
            
            if(idFecu!=null){
                if(!eeffMap.containsKey(idFecu)){
                    EeffUtil.addErrorFecu(1+1, r1+1, errores);
                }else{
                    EstadoFinanciero eeff = eeffMap.get(idFecu);
                    detalleEeff.setEstadoFinanciero1(eeff);
                    eeff.getDetalleEeffList4().add(detalleEeff);
                }
            }
            
        }
    }
    
    public Long getCodigoFecu(XSSFCell cell, int col, int row, List<String> errores){        
        Long idFecu = null;

        try{

            idFecu = ((Double)cell.getNumericCellValue()).longValue();
            
        }catch(Exception e){
            EeffUtil.addErrorFecuNull(col+1, row+1,errores);
        }
        
        return idFecu;
    }
    
    public BigDecimal getValorBigDecimal(XSSFCell cell){
        try{
            return new BigDecimal(cell.getNumericCellValue());
        }catch(Exception e){
            return new BigDecimal("0");
        }
    }
}
