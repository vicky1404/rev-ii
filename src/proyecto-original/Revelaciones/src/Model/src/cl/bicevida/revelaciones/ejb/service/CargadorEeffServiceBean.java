package cl.bicevida.revelaciones.ejb.service;


import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.index;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;

import cl.bicevida.revelaciones.eeff.CargadorEeffVO;
import cl.bicevida.revelaciones.eeff.DependenciaEeffFactory;
import cl.bicevida.revelaciones.eeff.DependenciaVO;
import cl.bicevida.revelaciones.eeff.FilaCeldaVO;
import cl.bicevida.revelaciones.eeff.RelacionEeffVO;
import cl.bicevida.revelaciones.ejb.cross.EeffUtil;
import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.CodigoFecu;
import cl.bicevida.revelaciones.ejb.entity.Columna;
import cl.bicevida.revelaciones.ejb.entity.DetalleEeff;
import cl.bicevida.revelaciones.ejb.entity.EstadoFinanciero;
import cl.bicevida.revelaciones.ejb.entity.RelacionDetalleEeff;
import cl.bicevida.revelaciones.ejb.entity.RelacionEeff;
import cl.bicevida.revelaciones.ejb.service.local.CargadorEeffServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.CeldaServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.ColumnaServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.EstadoFinancieroServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.FecuServiceLocal;
import cl.bicevida.revelaciones.exceptions.EstadoFinancieroException;

import java.io.InputStream;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

import static org.hamcrest.Matchers.equalTo;


@Stateless
public class CargadorEeffServiceBean implements CargadorEeffServiceLocal {
    
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "revelacionesPU")
    private EntityManager em;
    
    @EJB
    private EstadoFinancieroServiceLocal estadoFinancieroService;
    @EJB
    private FecuServiceLocal fecuService;
    @EJB
    private ColumnaServiceLocal columnaService;
    @EJB
    private CeldaServiceLocal celdaService;
    
    private Map<Long, CodigoFecu> fecuMap;

    public CargadorEeffServiceBean() {
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
        
        fecuMap = fecuService.getCodigoFecusVigentes();
        
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
                    
                    case 2: //Descrición fECU
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
    
    private Long getCodigoFecu(XSSFCell cell, int col, int row, List<String> errores){        
        Long idFecu = null;

        try{

            idFecu = ((Double)cell.getNumericCellValue()).longValue();
            
        }catch(Exception e){
            EeffUtil.addErrorFecuNull(col+1, row+1,errores);
        }
        
        return idFecu;
    }
    
    private BigDecimal getValorBigDecimal(XSSFCell cell){
        try{
            return new BigDecimal(cell.getNumericCellValue());
        }catch(Exception e){
            return new BigDecimal("0");
        }
    }
    
    public void validarEeffConRelacionEeff(Map<Long, EstadoFinanciero> eeffMap, Long idPeriodo,CargadorEeffVO cargadorVO){
        
        
        List<RelacionEeff> eeffList = estadoFinancieroService.getRelacionEeffByPeriodo(idPeriodo);
        List<RelacionDetalleEeff> relacionEeffList = estadoFinancieroService.getRelacionDetalleEeffByPeriodo(idPeriodo);
        
        Map<Long,RelacionEeff> relEeffMap = index(eeffList, on(RelacionEeff.class).getIdFecu());
        Map<Long,RelacionDetalleEeff> relDetalleEeffMap = index(relacionEeffList, on(RelacionDetalleEeff.class).getIdCuenta());
        
        Iterator<Map.Entry<Long, EstadoFinanciero>> it = eeffMap.entrySet().iterator();
        
        while(it.hasNext()){
            
            Map.Entry<Long, EstadoFinanciero> entry =  it.next();
            Long idFecu = entry.getKey();
            EstadoFinanciero eeffNew = entry.getValue();
            
            System.out.println("Iterando Fecu : " + idFecu);
            
            if(relEeffMap.containsKey(idFecu)){
                
                RelacionEeff relEeff = relEeffMap.get(idFecu);
                
                if(!relEeff.getMontoTotal().equals(eeffNew.getMontoTotal())){
                    System.out.println("Descuadre en monto Fecu : " + EeffUtil.formatFecu(idFecu) + 
                                       " - Monto Nuevo : "  + eeffNew.getMontoTotal() + " - Monto Antiguo " + relEeff.getMontoTotal());
                }
                
                if(Util.esListaValida(eeffNew.getDetalleEeffList4())){
                    
                    for(DetalleEeff detEeffNew : eeffNew.getDetalleEeffList4()){
                        
                        if(relDetalleEeffMap.containsKey(detEeffNew.getIdCuenta())){
                            
                            RelacionDetalleEeff relDetEeff = relDetalleEeffMap.get(detEeffNew.getIdCuenta());
                            
                            if(!relDetEeff.getMontoPesos().equals(detEeffNew.getMontoPesos())){
                                System.out.println("Descuadre en pesos Cuenta : " + detEeffNew.getIdCuenta() + 
                                                   " - Monto Nuevo : "  + detEeffNew.getMontoPesos() + " - Monto Antiguo " + relDetEeff.getMontoPesos());
                            }
                            
                            relDetalleEeffMap.remove(detEeffNew.getIdCuenta());
                            
                        }else{
                            System.out.println("Cuenta nueva : " + detEeffNew.getIdCuenta());
                        }
                    }
                }

                relEeffMap.remove(idFecu);

            }else{
                System.out.println("Fecu nueva : " + idFecu);
            }
        }
        
        DependenciaEeffFactory df = new DependenciaEeffFactory();
        
        if(!relEeffMap.isEmpty()){
            
            Iterator<Map.Entry<Long, RelacionEeff>> itRel = relEeffMap.entrySet().iterator();
            cargadorVO.setEeffBorrarList(new ArrayList<RelacionEeff>());
            
            while(itRel.hasNext()){
                
                Map.Entry<Long, RelacionEeff> entry =  itRel.next();
                Long idFecu = entry.getKey();
                RelacionEeff relEeff = entry.getValue();
                cargadorVO.getEeffBorrarList().add(relEeff);
                
                List<Celda> celdaList = celdaService.findCeldaByEeff(idPeriodo, idFecu);
                
                System.out.println("Se perderá la relacion para todas las grillas que estan asociadas al Fecu : " + idFecu);
                
                for(Celda celda : celdaList){
                    df.putCeldas(celda);
                }
                
            }
        }
        
        if(!relDetalleEeffMap.isEmpty()){
            
            Iterator<Map.Entry<Long, RelacionDetalleEeff>> itRelDet = relDetalleEeffMap.entrySet().iterator();
            cargadorVO.setEeffBorrarDetList(new ArrayList<RelacionDetalleEeff>());
            
            while(itRelDet.hasNext()){
                
                Map.Entry<Long, RelacionDetalleEeff> entry =  itRelDet.next();
                Long idCuenta = entry.getKey();
                RelacionDetalleEeff relDetEeff = entry.getValue();
                cargadorVO.getEeffBorrarDetList().add(relDetEeff);
                System.out.println("Se perderá la relacion para todas las grillas que estan asociadas a la Cuenta : " + idCuenta);
                
                List<Celda> celdaList = celdaService.findCeldaByEeffDet(idPeriodo, idCuenta);
                
                for(Celda celda : celdaList){
                    df.putCeldas(celda);
                }
                
            }
        }
        
        List<RelacionEeffVO> relacionVOList = new ArrayList<RelacionEeffVO>();
        List<DependenciaVO> dependenciaList = df.getDependenciaVOThisInstance();
        
        for(DependenciaVO dep : dependenciaList){
            
            List<Columna> columnaList = columnaService.getColumnaByGrilla(dep.getIdGrilla());
            
            RelacionEeffVO relacionVO = new RelacionEeffVO();
            
            if(Util.esListaValida(columnaList)){
                relacionVO.setTitulo(columnaList.get(0).getGrilla().getEstructura1().getVersion().getCatalogo().getTitulo());
            }
            
            int anchoTabla = 0;
            
            for(Columna columna : columnaList){
                
                List<Celda> celdaListTemp = new ArrayList<Celda>();
                
                for(FilaCeldaVO filaCeldaVO : dep.getFilaCeldaVO()){
                    List<Celda> celdaListPaso = select(columna.getCeldaList() ,having(on(Celda.class).getIdFila(), equalTo(filaCeldaVO.getFila())));
                    
                    for(Celda cellTemp : filaCeldaVO.getCeldaList()){
                        for(Celda celda : celdaListPaso){
                            if(cellTemp.equals(celda)){
                                celda.setTieneRelEeff(true);
                                break;
                            }
                        }
                    }
                    
                    if(celdaListPaso != null)
                        celdaListTemp.addAll(celdaListPaso);
                }
                
                columna.setCeldaList(celdaListTemp);
                
                anchoTabla += columna.getAncho();
            }
            relacionVO.setAnchoTabla(anchoTabla);
            relacionVO.setColumnaList(columnaList);
            relacionVO.setCeldaList(Util.builHtmlGrilla(columnaList));

            relacionVOList.add(relacionVO);
        }
        
        cargadorVO.setGrillaRelacionList(relacionVOList);
    }
    
    
}
