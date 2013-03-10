package cl.mdr.ifrs.ejb.service;


import static ch.lambdaj.Lambda.index;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;
import static cl.mdr.ifrs.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.commons.collections.ComparatorUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ch.lambdaj.function.compare.ArgumentComparator;
import cl.mdr.ifrs.ejb.cross.EeffUtil;
import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.Catalogo;
import cl.mdr.ifrs.ejb.entity.CodigoFecu;
import cl.mdr.ifrs.ejb.entity.CuentaContable;
import cl.mdr.ifrs.ejb.entity.DetalleEeff;
import cl.mdr.ifrs.ejb.entity.EstadoFinanciero;
import cl.mdr.ifrs.ejb.entity.PeriodoEmpresa;
import cl.mdr.ifrs.ejb.entity.RelacionDetalleEeff;
import cl.mdr.ifrs.ejb.entity.RelacionEeff;
import cl.mdr.ifrs.ejb.entity.UsuarioGrupo;
import cl.mdr.ifrs.ejb.entity.VersionEeff;
import cl.mdr.ifrs.ejb.facade.local.FacadeServiceLocal;
import cl.mdr.ifrs.ejb.service.local.CargadorEeffServiceLocal;
import cl.mdr.ifrs.exceptions.EstadoFinancieroException;
import cl.mdr.ifrs.vo.CargadorEeffVO;


@Stateless
public class CargadorEeffServiceBean implements CargadorEeffServiceLocal {
    
    private final Logger logger = Logger.getLogger(CargadorEeffServiceBean.class);
    
    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;

    @EJB
    private FacadeServiceLocal facadeService;

    public CargadorEeffServiceBean() {
    }
    
    public CargadorEeffVO leerEeff(final InputStream loadedExcel)throws EstadoFinancieroException, Exception {
        
        
        XSSFWorkbook workBook = new XSSFWorkbook(loadedExcel);
        
        List<String> errores = new ArrayList<String>();
        
        Map<Long, EstadoFinanciero> eeffMap = new LinkedHashMap<Long, EstadoFinanciero>();
        
        if(workBook.getSheetAt(0) == null || workBook.getSheetAt(1)==null)
            throw new EstadoFinancieroException("El documento Excel no contiene datos");
        
        if(workBook.getSheetAt(0).getPhysicalNumberOfRows() == 0 || 
           workBook.getSheetAt(1).getPhysicalNumberOfRows() == 0)
            throw new EstadoFinancieroException("El documento Excel no contiene filas");   
        
         Map<Long, CodigoFecu> fecuMap = facadeService.getFecuService().getCodigoFecusVigentes();
         Map<Long, CuentaContable> cuentaMap = facadeService.getFecuService().getCodigoCuentasVigentes();
        
        CargadorEeffVO cargadorVO = new CargadorEeffVO();
        
        int cabecera = leerCabeceraEeff(eeffMap, workBook.getSheetAt(0), fecuMap, errores);
        int detalle = leerDetalleEeff(eeffMap, workBook.getSheetAt(1), cuentaMap, errores);
        
        cargadorVO.setCatidadEeffProcesado(cabecera);
        cargadorVO.setCatidadEeffDetProcesado(detalle);
        cargadorVO.setEeffList(new ArrayList<EstadoFinanciero>(eeffMap.values()));
        
        if(errores.size()>0)
            throw new EstadoFinancieroException(errores);
        
        return cargadorVO;
    }
    
    private int leerCabeceraEeff(Map<Long, EstadoFinanciero> eeffMap, 
                                 XSSFSheet sheet,
                                 Map<Long,CodigoFecu> fecuMap,
                                 List<String> errores){
        
        XSSFRow row;
        XSSFCell cell;
        
        int contador = 0;
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
                            EeffUtil.addErrorFecuNotFound(idFecu,errores);
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
                        EeffUtil.addErrorFecuDu(idFecu,errores);
                    }else{
                        eeffMap.put(idFecu, eeff);
                        contador++;
                    }
                }
            }
        }
        return contador;
    }
    
    public int leerDetalleEeff(Map<Long, EstadoFinanciero> eeffMap, 
                                XSSFSheet sheet,
                                Map<Long, CuentaContable> cuentaMap,
                                List<String> errores){
        
        XSSFRow row;
        XSSFCell cell;
        Map<Long,Long> cuentaCargadaMap = new LinkedHashMap<Long,Long>();
        
        int contador = 0;
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
                
                case 1: //Numero cuenta
                    try{
                        Long idCuenta = ((Double)cell.getNumericCellValue()).longValue();
                        
                        if(cuentaMap.containsKey(idCuenta)){
                            detalleEeff.setCuentaContable(cuentaMap.get(idCuenta));
                        }else{
                            EeffUtil.addErrorCuentaNotFound(idCuenta,errores);
                        }
                        
                        if(cuentaCargadaMap.containsKey(idCuenta))
                            EeffUtil.addErrorCuentaDu(idCuenta,errores);
                        else
                            cuentaCargadaMap.put(idCuenta, idCuenta);
                        
                        detalleEeff.setIdCuenta(idCuenta);
                    }catch(Exception e){
                        EeffUtil.addErrorCuentaNull(c+1, r1+1,errores);
                    }
                    break;
                
                case 2:
                    //descripcion cuenta contable
                    break;
                
                case 3:
                    detalleEeff.setMontoPesos(getValorBigDecimal(cell));
                    break;
                
                case 4:
                    detalleEeff.setMontoMilesValidarMapeo(getValorBigDecimal(cell));
                    break;
                
                case 5:
                    detalleEeff.setMontoXBRL(getValorBigDecimal(cell));
                    break;
                
                }
            }
            
            if(idFecu!=null){
                if(!eeffMap.containsKey(idFecu)){
                    EeffUtil.addErrorFecu(idFecu, errores);
                }else{
                    EstadoFinanciero eeff = eeffMap.get(idFecu);
                    detalleEeff.setEstadoFinanciero1(eeff);
                    eeff.getDetalleEeffList4().add(detalleEeff);
                    contador++;
                }
            }
            
        }
        
        return contador;
    }
    
    private Long getCodigoFecu(XSSFCell cell, int col, int row, List<String> errores){        
        Long idFecu = null;

        try{

            idFecu = ((Double)cell.getNumericCellValue()).longValue();
            
        }catch(Exception e){
            EeffUtil.addErrorFecuNull(col, row,errores);
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
    
    public void validarNuevoEeff(final List<EstadoFinanciero> eeffListNuevo,final PeriodoEmpresa periodoEmpresa,final CargadorEeffVO cargadorVO) throws Exception{
        
        VersionEeff versionEeff = null;
        
        try{
            versionEeff = facadeService.getEstadoFinancieroService().getVersionEeffVigenteFindByPeriodo(periodoEmpresa.getIdPeriodo(), periodoEmpresa.getIdRut());
        }catch(EJBException e){
            if(!(e.getCause() instanceof NoResultException)){
                throw e;
            }
        }
        
        /*No hay ninguna version anterior con la cual validar*/
        if(versionEeff == null)
            return;
        
        /********************** EEFF DE BBDD PARA VALIDACION ******************************************************/
        
        /*Lista con los estados financieros vigentes de la bdd*/
        List<EstadoFinanciero>  eeffList  = facadeService.getEstadoFinancieroService().getEeffByVersion(versionEeff.getIdVersionEeff());
        List<DetalleEeff> eeffDetList = facadeService.getEstadoFinancieroService().getDetalleEeffByVersion(versionEeff.getIdVersionEeff());
        
        Map<Long,EstadoFinanciero> eeffMap = index(eeffList, on(EstadoFinanciero.class).getIdFecu());
        Map<String,DetalleEeff> detalleEeffMap = EeffUtil.convertListEeffDetToMap(eeffDetList);
        
        /*Lista con los mapeos de estados financieros vigentes de la bdd*/
       /* List<RelacionEeff> relEeffList = facadeService.getEstadoFinancieroService().getRelacionEeffByPeriodo(periodoEmpresa.getIdPeriodo(), periodoEmpresa.getIdRut());
        List<RelacionDetalleEeff> relDetEeffList = facadeService.getEstadoFinancieroService().getRelacionDetalleEeffByPeriodo(periodoEmpresa.getIdPeriodo(), periodoEmpresa.getIdRut());
        
        Map<Long,RelacionEeff> relEeffMap = index(relEeffList, on(RelacionEeff.class).getIdFecu());
        Map<String,RelacionDetalleEeff> relDetalleEeffMap = EeffUtil.convertListRelEeffDetToMap(relDetEeffList);
        */
        
        List<RelacionEeff> relEeffList = facadeService.getEstadoFinancieroService().getRelacionEeffByPeriodo(periodoEmpresa.getIdPeriodo(), periodoEmpresa.getIdRut());
        List<RelacionDetalleEeff> relDetEeffList = facadeService.getEstadoFinancieroService().getRelacionDetalleEeffByPeriodo(periodoEmpresa.getIdPeriodo(), periodoEmpresa.getIdRut());
        
        Map<Long,List<RelacionEeff>> relEeffMap = EeffUtil.convertListRelEeffToMap(relEeffList);
        Map<String,List<RelacionDetalleEeff>> relDetalleEeffMap = EeffUtil.convertListRelEeffDetToMap(relDetEeffList);
        
        /********************** LISTAS DE RESULTADO DE VALIDACION **************************************************/
        
        /*Listas representan a las diferencias de montos entre Eeff y  EEff nuevo*/
        List<EstadoFinanciero>  eeffDescuadreList  = new ArrayList<EstadoFinanciero>();
        List<DetalleEeff> eeffDetDescuadreList = new ArrayList<DetalleEeff>();
        
        /*Listas representan a las diferencias de montos entre la celda que esta mapeada y nuevo valor de EEff */
        List<RelacionEeff>  relEeffDescuadreList  = new ArrayList<RelacionEeff>();
        List<RelacionDetalleEeff> relEeffDetDescuadreList = new ArrayList<RelacionDetalleEeff>();
        
        /*Listas representan los FECU y CUENTA que se han eliminado */
        List<EstadoFinanciero>  eeffBorradoList  = new ArrayList<EstadoFinanciero>();
        List<DetalleEeff> eeffDetBorradoList = new ArrayList<DetalleEeff>();
        
        /*Listas representan los FECU y CUENTA mapeados que se han eliminado */
        List<RelacionEeff>  relEeffBorradoList  = new ArrayList<RelacionEeff>();
        List<RelacionDetalleEeff> relEeffDetBorradoList = new ArrayList<RelacionDetalleEeff>();
        

        /*se itera la nueva eeff cargada*/
        for(EstadoFinanciero eeff : eeffListNuevo){
            
            /*Validando montos FECU y CUENTA EEFF contra monto FECU Y CUENTA de EEFF nuevo*/
            validarEeffConEeffNuevo(eeff, 
                                    eeffMap, 
                                    detalleEeffMap,
                                    eeffDescuadreList, 
                                    eeffDetDescuadreList);
            
            /*Validando montos MAPEO FECU y CUENTA EEFF contra monto FECU Y CUENTA de EEFF nuevo*/
            validarRelEeffConEeffNuevo(eeff, 
                                       relEeffMap, 
                                       relDetalleEeffMap, 
                                       relEeffDescuadreList, 
                                       relEeffDetDescuadreList);
            
        }
        
        
        eeffBorradoList.addAll(eeffMap.values());// = new ArrayList<EstadoFinanciero>(eeffMap.values());
        eeffDetBorradoList.addAll(detalleEeffMap.values());  // = new ArrayList<DetalleEeff>(detalleEeffMap.values());
        
        for(List<RelacionEeff> relBList : relEeffMap.values()){
            for(RelacionEeff relB : relBList){
                relEeffBorradoList.add(relB);
            }
        }
        
        //relEeffBorradoList.addAll(relEeffMap.values()); // = new ArrayList<RelacionEeff>(relEeffMap.values());
        //relEeffDetBorradoList.addAll(relDetalleEeffMap.values()); // = new ArrayList<RelacionDetalleEeff>(relDetalleEeffMap.values());
        
        for(List<RelacionDetalleEeff> relDetBList : relDetalleEeffMap.values()){
            for(RelacionDetalleEeff relDetB : relDetBList){
                relEeffDetBorradoList.add(relDetB);
            }
        }
        
        
        

        
        cargadorVO.setEeffBorradoList(eeffBorradoList);
        cargadorVO.setEeffDetBorradoList(eeffDetBorradoList);
        cargadorVO.setEeffDescuadreList(eeffDescuadreList);
        cargadorVO.setEeffDetDescuadreList(eeffDetDescuadreList);
        
        cargadorVO.setRelEeffBorradoList(relEeffBorradoList);
        cargadorVO.setRelEeffDetBorradoList(relEeffDetBorradoList);
        cargadorVO.setRelEeffDescuadreList(relEeffDescuadreList);
        cargadorVO.setRelEeffDetDescuadreList(relEeffDetDescuadreList);
        
        cargarGrillaNoValida(cargadorVO);
        
        sortList(cargadorVO);
        
        buildMailList(cargadorVO);
    
    }
    
    
    
    /**
     *Valida el monto de Eeff contra EEff nuevo
     */
    private void validarEeffConEeffNuevo(   final EstadoFinanciero eeffNuevo, /*EEFF EXCEL*/
                                            final Map<Long,EstadoFinanciero> eeffMap, /*EEFF BDD*/ 
                                            final Map<String,DetalleEeff> detalleEeffMap, /*DET EEFF BDD*/ 
                                            final List<EstadoFinanciero>  eeffDescuadreList,
                                            final List<DetalleEeff> eeffDetDescuadreList){
        
        Long idFecu = eeffNuevo.getIdFecu();
        
        /*Validando mapeo monto de FECU relacion EEFF contra monto FECU de EEFF nuevo*/
        if(eeffMap.containsKey(idFecu)){
            
            EstadoFinanciero eeff = eeffMap.get(idFecu);
            
            if(!eeff.getMontoTotal().equals(eeffNuevo.getMontoTotal())){
                
                eeff.setMontoTotalNuevo(eeffNuevo.getMontoTotal());
                eeffDescuadreList.add(eeff);
                
                logger.info("Descuadre en monto Fecu : " + EeffUtil.formatFecu(idFecu) + 
                            " - Monto Nuevo : "  + eeff.getMontoTotalNuevo() + 
                            " - Monto Antiguo " + eeff.getMontoTotal());
            }
            
            /*Validando mapeo monto de CUENTA  EEFF contra monto CUENTA de EEFF nuevo*/
            if(Util.esListaValida(eeffNuevo.getDetalleEeffList4())){
                
                for(DetalleEeff eeffDetNuevo : eeffNuevo.getDetalleEeffList4()){
                    
                    String key = EeffUtil.formatKeyFecuCuenta(eeffDetNuevo.getIdFecu(), eeffDetNuevo.getIdCuenta());
                    
                    if(detalleEeffMap.containsKey(key)){
                        
                        DetalleEeff eeffDet = detalleEeffMap.get(key);
                        
                        if(!eeffDet.getMontoMilesValidarMapeo().equals(eeffDetNuevo.getMontoMilesValidarMapeo())){
                            
                            eeffDet.setMontoMilesValidarMapeoNuevo(eeffDetNuevo.getMontoMilesValidarMapeo());
                            eeffDetDescuadreList.add(eeffDet);
                            
                            logger.info("Descuadre en pesos Cuenta : " + eeffDetNuevo.getIdCuenta() + 
                                        " - Monto Nuevo : "  + eeffDet.getMontoMilesValidarMapeoNuevo() + 
                                        " - Monto Antiguo " +  eeffDet.getMontoMilesValidarMapeo());
                        }
                        
                        detalleEeffMap.remove(key);
                        
                    }
                }
            }
            eeffMap.remove(idFecu);
        }    
    }
    
    /**
     *Valida el monto del mapeo de Eeff (Relacion EEFF) contra EEff nuevo
     */
    private void validarRelEeffConEeffNuevo(final EstadoFinanciero eeffNuevo,
                                            final Map<Long,List<RelacionEeff>> relEeffMap, 
                                            final Map<String,List<RelacionDetalleEeff>> relDetalleEeffMap,
                                            final List<RelacionEeff>  relEeffDescuadreList,
                                            final List<RelacionDetalleEeff> relEeffDetDescuadreList){
        
        Long idFecu = eeffNuevo.getIdFecu();
        
        /*Validando mapeo monto de FECU relacion EEFF contra monto FECU de EEFF nuevo*/
        if(relEeffMap.containsKey(idFecu)){
            
            List<RelacionEeff> relEeffList = relEeffMap.get(idFecu);
            
            for(RelacionEeff relEeff : relEeffList){
            
            if(!relEeff.getMontoTotal().equals(eeffNuevo.getMontoTotal())){
            	
            	       
                relEeff.setMontoTotalNuevo(eeffNuevo.getMontoTotal());
                relEeffDescuadreList.add(relEeff);
                
                logger.info("Descuadre en Mapeo monto Fecu : " + EeffUtil.formatFecu(idFecu) + 
                            " - Monto Nuevo : "  + relEeff.getMontoTotalNuevo() + 
                            " - Monto Antiguo " + relEeff.getMontoTotal());
            	}
            }
            
            /*Validando mapeo monto de CUENTA relacion EEFF contra monto CUENTA de EEFF nuevo*/
            if(Util.esListaValida(eeffNuevo.getDetalleEeffList4())){
                
                for(DetalleEeff eeffDetNuevo : eeffNuevo.getDetalleEeffList4()){
                    
                    String key = EeffUtil.formatKeyFecuCuenta(eeffDetNuevo.getIdFecu(), eeffDetNuevo.getIdCuenta());
                    
                    if(relDetalleEeffMap.containsKey(key)){
                        
                        List<RelacionDetalleEeff> relEeffDetList = relDetalleEeffMap.get(key);
                        
                        for(RelacionDetalleEeff relEeffDet : relEeffDetList){
                        
                                if(!relEeffDet.getMontoMilesValidarMapeo().equals(eeffDetNuevo.getMontoMilesValidarMapeo())){
                                    
                                    relEeffDet.setMontoMilesValidarMapeoNuevo(eeffDetNuevo.getMontoMilesValidarMapeo());
                                    relEeffDetDescuadreList.add(relEeffDet);
                                    
                                    logger.info("Descuadre en Mapeo pesos Cuenta : " + eeffDetNuevo.getIdCuenta() + 
                                                " - Monto Nuevo : "  + relEeffDet.getMontoMilesValidarMapeoNuevo() + 
                                                " - Monto Antiguo " +  relEeffDet.getMontoMilesValidarMapeo());
                                }
                            
                                relDetalleEeffMap.remove(key);
                        }
                    }
                }
            }
            relEeffMap.remove(idFecu);
        }
    }
    
    
    public void buildMailList(final CargadorEeffVO cargadorVO) throws Exception{
        
        StringBuilder mensaje = null;
        
        /*idCatalogo, htmlMensaje representa todos los mensajes que contiene un catalogo, se debe perfilar el mensaje por grupoCatalogo*/
        final Map<Long,List<StringBuilder>> mensajeMap = new HashMap<Long,List<StringBuilder>>();
        
        if(Util.esListaValida(cargadorVO.getRelEeffBorradoList())){
            
            Map<Long, List<RelacionEeff>> relEeffMap = EeffUtil.convertRelEeffListToMapByGrilla(cargadorVO.getRelEeffBorradoList());
            
            for ( Map.Entry<Long, List<RelacionEeff>> relEeffEntry : relEeffMap.entrySet() ){
                
                mensaje = new StringBuilder();
            
                Catalogo catalogo = relEeffEntry.getValue().get(0).getCelda2().getColumna().getGrilla().getEstructura().getVersion().getCatalogo();
                
                mensaje .append(EeffUtil.getTableTag())
                        .append("<tr>")
                        .append(EeffUtil.getTdFontTag())
                        .append("La revelación : ")
                        .append(catalogo.getNombre()).append(" - ")
                        .append("Ha perdido el mapeo, debido a que se han eliminado los siguientes Códigos FECU : ")
                        .append("</td>")
                        .append("</tr>")
                        .append(EeffUtil.getTableCloseTag());
                
                mensaje .append(EeffUtil.getTableTag())
                        .append(EeffUtil.getTbodyRelEeff());
                
                int count = 0;
                for(RelacionEeff relEeff : relEeffEntry.getValue()){
                    mensaje
                        .append("<tr bgcolor='"+ ((count%2)!=0 ?"#EFF5FB":"") +"'>")
                        .append(EeffUtil.getTdFontTag()).append(relEeff.getFecuFormat()).append(EeffUtil.getTdFontCloseTag())
                        .append(EeffUtil.getTdFontTag()).append(relEeff.getCelda2().getColumna().getTituloColumna()).append(EeffUtil.getTdFontCloseTag())
                        .append(EeffUtil.getTdFontTag()).append(relEeff.getIdFila()).append(EeffUtil.getTdFontCloseTag())
                        .append(EeffUtil.getTdFontTag()).append(Util.formatCellKey(relEeff.getCelda2())).append(EeffUtil.getTdFontCloseTag())
                        .append(EeffUtil.getTdFontTag()).append(relEeff.getCelda2().getValorBigDecimal()).append(EeffUtil.getTdFontCloseTag())
                        .append("</tr>");
                count++;
                }
                mensaje
                        .append(EeffUtil.getTableCloseTag())
                        .append(EeffUtil.getSaltoLineaTag());
                
                if(mensajeMap.containsKey(relEeffEntry.getKey())){
                    mensajeMap.get(relEeffEntry.getKey()).add(mensaje);
                }else{
                    List<StringBuilder> mensajeTemp = new ArrayList<StringBuilder>();
                    mensajeTemp.add(mensaje);
                    mensajeMap.put(relEeffEntry.getKey(), mensajeTemp);
                }
                
            }

        }
        
        if(Util.esListaValida(cargadorVO.getRelEeffDetBorradoList())){
            
            Map<Long, List<RelacionDetalleEeff>> relEeffDetMap = EeffUtil.convertRelDetEeffListToMapByGrilla(cargadorVO.getRelEeffDetBorradoList());
            
            for ( Map.Entry<Long, List<RelacionDetalleEeff>> relDetEeffEntry : relEeffDetMap.entrySet() ){
                
                mensaje = new StringBuilder();
                
                Catalogo catalogo = relDetEeffEntry.getValue().get(0).getCelda5().getColumna().getGrilla().getEstructura().getVersion().getCatalogo();
                int count = 0;
                mensaje .append(EeffUtil.getTableTag())
                        .append("<tr>")
                        .append(EeffUtil.getTdFontTag())
                        .append("La revelación : ")
                        .append(catalogo.getNombre()).append(" - ")
                        .append("Ha perdido el mapeo, debido a que se han eliminado los siguientes Códigos de Cuenta : ")
                        .append(EeffUtil.getTdFontCloseTag())
                        .append("</tr>")
                        .append(EeffUtil.getTableCloseTag());
                
                mensaje .append(EeffUtil.getTableTag())
                        .append(EeffUtil.getTbodyRelDetEeff());
                
                count = 0;
                for(RelacionDetalleEeff relDetalleEeff : relDetEeffEntry.getValue()){
                    
                    mensaje
                        .append("<tr bgcolor='"+ ((count%2)!=0 ?"#EFF5FB":"") +"'>")
                        .append(EeffUtil.getTdFontTag()).append(relDetalleEeff.getFecuFormat()).append(EeffUtil.getTdFontCloseTag())
                        .append(EeffUtil.getTdFontTag()).append(relDetalleEeff.getIdCuenta()).append(EeffUtil.getTdFontCloseTag())
                        .append(EeffUtil.getTdFontTag()).append(relDetalleEeff.getCelda5().getColumna().getTituloColumna()).append(EeffUtil.getTdFontCloseTag())
                        .append(EeffUtil.getTdFontTag()).append(relDetalleEeff.getIdFila()).append(EeffUtil.getTdFontCloseTag())
                        .append(EeffUtil.getTdFontTag()).append(Util.formatCellKey(relDetalleEeff.getCelda5())).append(EeffUtil.getTdFontCloseTag())
                        .append(EeffUtil.getTdFontTag()).append(relDetalleEeff.getCelda5().getValorBigDecimal()).append(EeffUtil.getTdFontCloseTag())
                        .append("</tr>");
                    
                    count++;
                }
            
                mensaje
                        .append(EeffUtil.getTableCloseTag())
                        .append(EeffUtil.getSaltoLineaTag());;
                    
                if(mensajeMap.containsKey(relDetEeffEntry.getKey())){
                    mensajeMap.get(relDetEeffEntry.getKey()).add(mensaje);
                }else{
                    List<StringBuilder> mensajeTemp = new ArrayList<StringBuilder>();
                    mensajeTemp.add(mensaje);
                    mensajeMap.put(relDetEeffEntry.getKey(), mensajeTemp);
                }
            }
        }
        
        if(Util.esListaValida(cargadorVO.getRelEeffDescuadreList())){
            
            Map<Long, List<RelacionEeff>> relEeffMap = EeffUtil.convertRelEeffListToMapByGrilla(cargadorVO.getRelEeffDescuadreList());
            
            for ( Map.Entry<Long, List<RelacionEeff>> relEeffEntry : relEeffMap.entrySet() ){
                
                mensaje = new StringBuilder();
            
                Catalogo catalogo = relEeffEntry.getValue().get(0).getCelda2().getColumna().getGrilla().getEstructura().getVersion().getCatalogo();
                
                mensaje .append(EeffUtil.getTableTag())
                        .append("<tr>")
                        .append(EeffUtil.getTdFontTag())
                        .append("La revelación : ")
                        .append(catalogo.getNombre()).append(" - ")
                        .append("Se ha perdido la validación, debido a que se han modificado los valores de Códigos FECU :  ")
                        .append(EeffUtil.getTdFontCloseTag())
                        .append("</tr>")
                        .append(EeffUtil.getTableCloseTag());
                
                mensaje .append(EeffUtil.getTableTag())
                        .append(EeffUtil.getTbodyRelEeff());
                
                int count = 0;
                for(RelacionEeff relEeff : relEeffEntry.getValue()){
                    mensaje
                        .append("<tr bgcolor='"+ ((count%2)!=0 ?"#EFF5FB":"") +"'>")
                        .append(EeffUtil.getTdFontTag()).append(relEeff.getFecuFormat()).append(EeffUtil.getTdFontCloseTag())
                        .append(EeffUtil.getTdFontTag()).append(relEeff.getCelda2().getColumna().getTituloColumna()).append(EeffUtil.getTdFontCloseTag())
                        .append(EeffUtil.getTdFontTag()).append(relEeff.getIdFila()).append(EeffUtil.getTdFontCloseTag())
                        .append(EeffUtil.getTdFontTag()).append(Util.formatCellKey(relEeff.getCelda2())).append(EeffUtil.getTdFontCloseTag())
                        .append(EeffUtil.getTdFontTag()).append(relEeff.getCelda2().getValorBigDecimal()).append(EeffUtil.getTdFontCloseTag())
                        .append("</tr>");
                count++;
                }
                mensaje
                        .append(EeffUtil.getTableCloseTag())
                        .append(EeffUtil.getSaltoLineaTag());
                
                if(mensajeMap.containsKey(relEeffEntry.getKey())){
                    mensajeMap.get(relEeffEntry.getKey()).add(mensaje);
                }else{
                    List<StringBuilder> mensajeTemp = new ArrayList<StringBuilder>();
                    mensajeTemp.add(mensaje);
                    mensajeMap.put(relEeffEntry.getKey(), mensajeTemp);
                }
                
            }

        }
        
        if(Util.esListaValida(cargadorVO.getRelEeffDetDescuadreList())){
            
            Map<Long, List<RelacionDetalleEeff>> relEeffDetMap = EeffUtil.convertRelDetEeffListToMapByGrilla(cargadorVO.getRelEeffDetDescuadreList());
            
            for ( Map.Entry<Long, List<RelacionDetalleEeff>> relDetEeffEntry : relEeffDetMap.entrySet() ){
                
                mensaje = new StringBuilder();
                
                Catalogo catalogo = relDetEeffEntry.getValue().get(0).getCelda5().getColumna().getGrilla().getEstructura().getVersion().getCatalogo();
                int count = 0;
                mensaje .append(EeffUtil.getTableTag())
                        .append("<tr>")
                        .append(EeffUtil.getTdFontTag())
                        .append("La revelación : ")
                        .append(catalogo.getNombre()).append(" - ")
                        .append("Se ha perdido la validación, debido a que se han modificado los valores de Códigos de Cuenta : ")
                        .append(EeffUtil.getTdFontCloseTag())
                        .append("</tr>")
                        .append(EeffUtil.getTableCloseTag());
                
                mensaje .append(EeffUtil.getTableTag())
                        .append(EeffUtil.getTbodyRelDetEeff());
                
                count = 0;
                for(RelacionDetalleEeff relDetalleEeff : relDetEeffEntry.getValue()){
                    
                    mensaje
                        .append("<tr bgcolor='"+ ((count%2)!=0 ?"#EFF5FB":"") +"'>")
                        .append(EeffUtil.getTdFontTag()).append(relDetalleEeff.getFecuFormat()).append(EeffUtil.getTdFontCloseTag())
                        .append(EeffUtil.getTdFontTag()).append(relDetalleEeff.getIdCuenta()).append(EeffUtil.getTdFontCloseTag())
                        .append(EeffUtil.getTdFontTag()).append(relDetalleEeff.getCelda5().getColumna().getTituloColumna()).append(EeffUtil.getTdFontCloseTag())
                        .append(EeffUtil.getTdFontTag()).append(relDetalleEeff.getIdFila()).append(EeffUtil.getTdFontCloseTag())
                        .append(EeffUtil.getTdFontTag()).append(Util.formatCellKey(relDetalleEeff.getCelda5())).append(EeffUtil.getTdFontCloseTag())
                        .append(EeffUtil.getTdFontTag()).append(relDetalleEeff.getCelda5().getValor()).append(EeffUtil.getTdFontCloseTag())
                        .append("</tr>");
                    
                    count++;
                }
            
                mensaje
                        .append(EeffUtil.getTableCloseTag())
                        .append(EeffUtil.getSaltoLineaTag());;
                    
                if(mensajeMap.containsKey(relDetEeffEntry.getKey())){
                    mensajeMap.get(relDetEeffEntry.getKey()).add(mensaje);
                }else{
                    List<StringBuilder> mensajeTemp = new ArrayList<StringBuilder>();
                    mensajeTemp.add(mensaje);
                    mensajeMap.put(relDetEeffEntry.getKey(), mensajeTemp);
                }
            }
        }
        	logger.info(mensaje);
        	buildUsuarioGrupo(cargadorVO, mensajeMap);
    }
    
    
    private void buildUsuarioGrupo(final CargadorEeffVO cargadorVO, final Map<Long,List<StringBuilder>> mensajeMap) throws Exception{
       
        Map<String,UsuarioGrupo> usuarioMailMap = new LinkedHashMap<String,UsuarioGrupo>();
        
        for ( Map.Entry<Long, List<StringBuilder>> mensajeEntry : mensajeMap.entrySet() ){
            
            StringBuilder contenidoMail = new StringBuilder();
            
            Catalogo catalogo = facadeService.getCatalogoService().findCatalogoByCatalogo(new Catalogo(mensajeEntry.getKey()));
            
            List<UsuarioGrupo> usuarioList =  facadeService.getSeguridadService().getUsuarioGrupoByCatalogoEmailNotNull(mensajeEntry.getKey());
            
            if(!Util.esListaValida(usuarioList))
            	continue;
            
            
            Map<String,UsuarioGrupo> usuarioMap = index(usuarioList, on(UsuarioGrupo.class).getUsuario().getEmail());
            
            for(StringBuilder str : mensajeEntry.getValue()){
                contenidoMail.append(str).append("<br>");
            }
            
            for(UsuarioGrupo usuarioTemp : usuarioMap.values()){
                if(usuarioMailMap.containsKey(usuarioTemp.getUsuario().getEmail())){
                    usuarioMailMap.get(usuarioTemp.getUsuario().getEmail()).getContenidoMail().append(contenidoMail);
                    usuarioMailMap.get(usuarioTemp.getUsuario().getEmail()).getCatalogoAsociadoList().add(catalogo);
                }else{
                    usuarioTemp.setContenidoMail(new StringBuilder(contenidoMail));
                    usuarioTemp.setCatalogoAsociadoList(new ArrayList<Catalogo>());
                    usuarioTemp.getCatalogoAsociadoList().add(catalogo);
                    usuarioMailMap.put(usuarioTemp.getUsuario().getEmail(), usuarioTemp);
                }
            }
        }
        
        cargadorVO.setUsuarioGrupoList(new ArrayList<UsuarioGrupo>(usuarioMailMap.values()));
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void sortList(final CargadorEeffVO cargadorVO){
        
        final Comparator byIdFecu = new ArgumentComparator(on(EstadoFinanciero.class).getIdFecu());
        
        cargadorVO.setEeffBorradoList( sort(cargadorVO.getEeffBorradoList(), on(EstadoFinanciero.class),byIdFecu));
        cargadorVO.setEeffDescuadreList(sort(cargadorVO.getEeffDescuadreList(), on(EstadoFinanciero.class),byIdFecu));
        
        final Comparator byIdCuenta = new ArgumentComparator(on(DetalleEeff.class).getIdCuenta());
        
        cargadorVO.setEeffDetBorradoList(sort(cargadorVO.getEeffDetBorradoList(), on(DetalleEeff.class),byIdCuenta));
        cargadorVO.setEeffDetDescuadreList(sort(cargadorVO.getEeffDetDescuadreList(), on(DetalleEeff.class),byIdCuenta));
        
        final Comparator byFeGrilla = new ArgumentComparator(on(RelacionEeff.class).getIdGrilla());
        final Comparator byFeColumna = new ArgumentComparator(on(RelacionEeff.class).getIdColumna());
        final Comparator byFeFila = new ArgumentComparator(on(RelacionEeff.class).getIdFila());
        final Comparator byFeIdFecu = new ArgumentComparator(on(RelacionEeff.class).getIdFecu());
        
        final Comparator fecuOrderBy = ComparatorUtils.chainedComparator(new Comparator[] {byFeGrilla, byFeColumna,byFeFila,byFeIdFecu});
        
        cargadorVO.setRelEeffBorradoList(sort(cargadorVO.getRelEeffBorradoList(), on(RelacionEeff.class), fecuOrderBy));
        cargadorVO.setRelEeffDescuadreList(sort(cargadorVO.getRelEeffDescuadreList(), on(RelacionEeff.class), fecuOrderBy));
        
        final Comparator byCuGrilla = new ArgumentComparator(on(RelacionDetalleEeff.class).getIdGrilla());
        final Comparator byCuColumna = new ArgumentComparator(on(RelacionDetalleEeff.class).getIdColumna());
        final Comparator byCuFila = new ArgumentComparator(on(RelacionDetalleEeff.class).getIdFila());
        final Comparator byCuIdFecu = new ArgumentComparator(on(RelacionDetalleEeff.class).getIdFecu());
        final Comparator byCuIdCuenta = new ArgumentComparator(on(RelacionDetalleEeff.class).getIdCuenta());
        
        final Comparator cuentaOrderBy = ComparatorUtils.chainedComparator(new Comparator[] {byCuGrilla, byCuColumna,byCuFila,byCuIdFecu,byCuIdCuenta});
        
        cargadorVO.setRelEeffDetBorradoList(sort(cargadorVO.getRelEeffDetBorradoList(), on(RelacionDetalleEeff.class), cuentaOrderBy));
        cargadorVO.setRelEeffDetDescuadreList(sort(cargadorVO.getRelEeffDetDescuadreList(), on(RelacionDetalleEeff.class), cuentaOrderBy));
        
    }
    
    public void sendMailEeff(List<UsuarioGrupo> usuarioGrupoList, String emailFrom, String subject, String host, String usuarioTest, Boolean isTest){
        
        Map<String,UsuarioGrupo> usuarioMap = index(usuarioGrupoList, on(UsuarioGrupo.class).getUsuario().getEmail());
        
        if(isTest!=null && isTest.equals(Boolean.TRUE)){
            for(UsuarioGrupo usuario : usuarioMap.values()){
                if(usuarioTest!=null && usuarioTest.equalsIgnoreCase(usuario.getUsuario().getEmail())){
                	try{
                		facadeService.getMailService().sendMail(subject, usuario.getContenidoMail().toString(), usuario.getUsuario().getEmail());
                	}catch(Exception e){
                		logger.error("Error de dirección de correo" + e.getMessage());
                	}
                		
                }
                    //sendMail(host, emailFrom, usuario.getUsuario().getEmail(), subject, usuario.getContenidoMail().toString(),"text/html");
            }
        }else{
            for(UsuarioGrupo usuario : usuarioMap.values()){
            	try{
            		facadeService.getMailService().sendMail(subject, usuario.getContenidoMail().toString(), usuario.getUsuario().getEmail());
            	}catch(Exception e){
            		logger.error("Error de dirección de correo" + e.getMessage());
            	}
            }
        }
        
    }
    
    /*private void sendMail(String host, String emailFrom, String emailTo, String subject, String mensaje, String type){
        try {
            Properties properties = System.getProperties();
            properties.setProperty("mail.smtp.host", host);
            Session session = Session.getDefaultInstance(properties);
            try{
               MimeMessage message = new MimeMessage(session);
               message.setFrom(new InternetAddress(emailFrom));
               message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
               message.setSubject(subject);
               message.setContent(mensaje, type);
               Transport.send(message);
               logger.info("Sent message successfully....");
            }catch (MessagingException mex) {
               mex.printStackTrace();
            }
        } catch (Exception e) {
            logger.error("Error de dirección de correo" + e.getMessage());
        }
    }*/
    
    
    private void cargarGrillaNoValida(final CargadorEeffVO cargadorVO){
        
        for(RelacionEeff rel : cargadorVO.getRelEeffBorradoList()){
            cargadorVO.getGrillaNoValida().put(rel.getIdGrilla(), rel.getIdGrilla());
        }
        
        for(RelacionEeff rel : cargadorVO.getRelEeffDescuadreList()){
            cargadorVO.getGrillaNoValida().put(rel.getIdGrilla(), rel.getIdGrilla());
        }
        
        for(RelacionDetalleEeff relDet : cargadorVO.getRelEeffDetBorradoList()){
            cargadorVO.getGrillaNoValida().put(relDet.getIdGrilla(), relDet.getIdGrilla());
        }
        
        for(RelacionDetalleEeff relDet : cargadorVO.getRelEeffDetDescuadreList()){
            cargadorVO.getGrillaNoValida().put(relDet.getIdGrilla(), relDet.getIdGrilla());
        }
        
    }
    
    
    
    /*
    private void buildUsuarioGrupo(final CargadorEeffVO cargadorVO, final Map<Long,List<StringBuilder>> mensajeMap) throws Exception{
        
        String dominio;
        
        try{
            dominio = facadeService.getParametroService().getParametrosConfigMapByTipoParametro(TipoParametroEnum.MAIL_CONFIG.getKey()).get(MailConfigEnum.MAIL_DOMINIO.getKey()).getValor();;
        }catch(Exception e){
            dominio = null;
        }
        
        Map<String,UsuarioGrupo> usuarioMailMap = new LinkedHashMap<String,UsuarioGrupo>();
        
        for ( Map.Entry<Long, List<StringBuilder>> mensajeEntry : mensajeMap.entrySet() ){
            
            StringBuilder contenidoMail = new StringBuilder();
            
            Catalogo catalogo = facadeService.getCatalogoService().findCatalogoByCatalogo(new Catalogo(mensajeEntry.getKey()));
            
            Map<String,UsuarioGrupo> usuarioMap = index(facadeService.getSeguridadService().getUsuarioGrupoByCatalogo(mensajeEntry.getKey()), on(UsuarioGrupo.class).getUsuarioOid());
            
            for(StringBuilder str : mensajeEntry.getValue()){
                contenidoMail.append(str).append("<br>");
            }
            
            for(UsuarioGrupo usuarioTemp : usuarioMap.values()){
                
                usuarioTemp.setEmail(EeffUtil.concatUsuarioMail(usuarioTemp.getUsuarioOid(),dominio));
                
                if(usuarioMailMap.containsKey(usuarioTemp.getUsuarioOid())){
                    usuarioMailMap.get(usuarioTemp.getUsuarioOid()).getContenidoMail().append(contenidoMail);
                    usuarioMailMap.get(usuarioTemp.getUsuarioOid()).getCatalogoAsociadoList().add(catalogo);
                }else{
                    usuarioTemp.setContenidoMail(new StringBuilder(contenidoMail));
                    usuarioTemp.setCatalogoAsociadoList(new ArrayList<Catalogo>());
                    usuarioTemp.getCatalogoAsociadoList().add(catalogo);
                    usuarioMailMap.put(usuarioTemp.getUsuarioOid(), usuarioTemp);
                }
            }
        }
        
        cargadorVO.setUsuarioGrupoList(new ArrayList<UsuarioGrupo>(usuarioMailMap.values()));
    }
    
    private void sortList(final CargadorEeffVO cargadorVO){
        
        final Comparator byIdFecu = new ArgumentComparator(on(EstadoFinanciero.class).getIdFecu());
        
        cargadorVO.setEeffBorradoList( sort(cargadorVO.getEeffBorradoList(), on(EstadoFinanciero.class),byIdFecu));
        cargadorVO.setEeffDescuadreList(sort(cargadorVO.getEeffDescuadreList(), on(EstadoFinanciero.class),byIdFecu));
        
        final Comparator byIdCuenta = new ArgumentComparator(on(DetalleEeff.class).getIdCuenta());
        
        cargadorVO.setEeffDetBorradoList(sort(cargadorVO.getEeffDetBorradoList(), on(DetalleEeff.class),byIdCuenta));
        cargadorVO.setEeffDetDescuadreList(sort(cargadorVO.getEeffDetDescuadreList(), on(DetalleEeff.class),byIdCuenta));
        
        final Comparator byFeGrilla = new ArgumentComparator(on(RelacionEeff.class).getIdGrilla());
        final Comparator byFeColumna = new ArgumentComparator(on(RelacionEeff.class).getIdColumna());
        final Comparator byFeFila = new ArgumentComparator(on(RelacionEeff.class).getIdFila());
        final Comparator byFeIdFecu = new ArgumentComparator(on(RelacionEeff.class).getIdFecu());
        
        final Comparator fecuOrderBy = ComparatorUtils.chainedComparator(new Comparator[] {byFeGrilla, byFeColumna,byFeFila,byFeIdFecu});
        
        cargadorVO.setRelEeffBorradoList(sort(cargadorVO.getRelEeffBorradoList(), on(RelacionEeff.class), fecuOrderBy));
        cargadorVO.setRelEeffDescuadreList(sort(cargadorVO.getRelEeffDescuadreList(), on(RelacionEeff.class), fecuOrderBy));
        
        final Comparator byCuGrilla = new ArgumentComparator(on(RelacionDetalleEeff.class).getIdGrilla());
        final Comparator byCuColumna = new ArgumentComparator(on(RelacionDetalleEeff.class).getIdColumna());
        final Comparator byCuFila = new ArgumentComparator(on(RelacionDetalleEeff.class).getIdFila());
        final Comparator byCuIdFecu = new ArgumentComparator(on(RelacionDetalleEeff.class).getIdFecu());
        final Comparator byCuIdCuenta = new ArgumentComparator(on(RelacionDetalleEeff.class).getIdCuenta());
        
        final Comparator cuentaOrderBy = ComparatorUtils.chainedComparator(new Comparator[] {byCuGrilla, byCuColumna,byCuFila,byCuIdFecu,byCuIdCuenta});
        
        cargadorVO.setRelEeffDetBorradoList(sort(cargadorVO.getRelEeffDetBorradoList(), on(RelacionDetalleEeff.class), cuentaOrderBy));
        cargadorVO.setRelEeffDetDescuadreList(sort(cargadorVO.getRelEeffDetDescuadreList(), on(RelacionDetalleEeff.class), cuentaOrderBy));
        
    }
    
    public void sendMailEeff(List<UsuarioGrupo> usuarioGrupoList) throws Exception{
        
        final Map<String, Parametro> mailParams = facadeService.getParametroService().getParametrosConfigMapByTipoParametro(TipoParametroEnum.MAIL_CONFIG.getKey());
        
        String dominio,emailUser,emailPass,emailUserFrom,emailHost,emailPort,subject,enviarMail,usuarioTest = null;
        Boolean autenticar = null;
        try{
            dominio = mailParams.get(MailConfigEnum.MAIL_DOMINIO.getKey()).getValor();
            emailUser =  mailParams.get(MailConfigEnum.CARGADOR_MAIL_FROM.getKey()).getValor();
            emailPass =  mailParams.get(MailConfigEnum.CARGADOR_MAIL_PASS_FROM.getKey()).getValor();
            emailUserFrom =  EeffUtil.concatUsuarioMail(emailUser,dominio);
            emailHost =  mailParams.get(MailConfigEnum.MAIL_HOST.getKey()).getValor();
            emailPort =  mailParams.get(MailConfigEnum.MAIL_PORT.getKey()).getValor();
            subject =    mailParams.get(MailConfigEnum.CARGADOR_SUBJECT.getKey()).getValor();
            enviarMail =     mailParams.get(MailConfigEnum.ENVIAR_EMAIL.getKey()).getValor();
            usuarioTest =  mailParams.get(MailConfigEnum.MAIL_USUARIO_TEST.getKey()).getValor();
            autenticar =  new Boolean(mailParams.get(MailConfigEnum.MAIL_AUTENTUCAR.getKey()).getValor());
        }catch(Exception e){
            logger.error("Error al leer de bdd parámetros de configuración de mail" , e);
            dominio = Constantes.MAIL_DOMINIO;
            emailUser = Constantes.MAIL_USER;
            emailPass = Constantes.MAIL_PASS;
            emailUserFrom = EeffUtil.concatUsuarioMail(emailUser,dominio);
            emailHost = Constantes.MAIL_HOST;
            emailPort = Constantes.MAIL_PORT;
            subject = Constantes.MAIL_SUBJECT;
            enviarMail = Constantes.MAIL_SEND;
            usuarioTest = Constantes.MAIL_USUARIO_TEST;
            autenticar =  Constantes.MAIL_AUTENTICAR;
        }
         
        Map<String,UsuarioGrupo> usuarioMap = index(usuarioGrupoList, on(UsuarioGrupo.class).getUsuarioOid());
        
        if(enviarMail==null || enviarMail.equalsIgnoreCase("false")){
            for(UsuarioGrupo usuario : usuarioMap.values()){
                if(usuarioTest!=null && usuarioTest.equalsIgnoreCase(usuario.getUsuarioOid())){
                    sendMail(emailHost, emailUserFrom, emailUser, emailPass, emailUserFrom, subject, usuario.getContenidoMail().toString(),"text/html",emailPort,autenticar);
				}
            }
        }else{
            for(UsuarioGrupo usuario : usuarioMap.values()){
                sendMail(emailHost, emailUserFrom, emailUser,emailPass, EeffUtil.concatUsuarioMail(usuario.getUsuarioOid(),dominio), subject, usuario.getContenidoMail().toString(),"text/html",emailPort,autenticar);
            }
        }
    }
    
    
    private void sendMail(String host, String emailUserFrom, String emailUser,  String emailPass, String emailTo, String subject, String mensaje, String type, String port, boolean autenticar)throws Exception{
        
        Properties properties = System.getProperties();
        
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.port", port);
		properties.put("mail.smtp.auth", "false");

        

        try{
            Session mailSession = Session.getDefaultInstance(properties, null);
            Transport transport = mailSession.getTransport();
            MimeMessage message = new MimeMessage(mailSession);
           message.setFrom(new InternetAddress(emailUserFrom));
           message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
           message.setSubject(subject);
           message.setContent(mensaje, type);
            if(autenticar){
                transport.connect(emailUser, emailPass);
            }else{
                transport.connect();
            }
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
           transport.close();
        }catch (MessagingException mex) {
           throw mex;
        }
        
    }
     */
}