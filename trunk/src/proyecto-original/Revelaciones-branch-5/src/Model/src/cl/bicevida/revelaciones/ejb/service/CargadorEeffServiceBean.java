package cl.bicevida.revelaciones.ejb.service;


import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.index;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static ch.lambdaj.Lambda.sort;

import ch.lambdaj.function.compare.ArgumentComparator;

import cl.bicevida.revelaciones.eeff.CargadorEeffVO;
import cl.bicevida.revelaciones.eeff.DependenciaEeffFactory;
import cl.bicevida.revelaciones.eeff.DependenciaVO;
import cl.bicevida.revelaciones.eeff.FilaCeldaVO;
import cl.bicevida.revelaciones.eeff.RelacionEeffVO;
import cl.bicevida.revelaciones.ejb.common.TipoDatoEnum;
import cl.bicevida.revelaciones.ejb.cross.EeffUtil;
import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.revelaciones.ejb.entity.Catalogo;
import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.CodigoFecu;
import cl.bicevida.revelaciones.ejb.entity.Columna;
import cl.bicevida.revelaciones.ejb.entity.CuentaContable;
import cl.bicevida.revelaciones.ejb.entity.DetalleEeff;
import cl.bicevida.revelaciones.ejb.entity.EstadoFinanciero;
import cl.bicevida.revelaciones.ejb.entity.RelacionDetalleEeff;
import cl.bicevida.revelaciones.ejb.entity.RelacionEeff;
import cl.bicevida.revelaciones.ejb.entity.UsuarioGrupo;
import cl.bicevida.revelaciones.ejb.entity.VersionEeff;
import cl.bicevida.revelaciones.ejb.service.local.CargadorEeffServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.CeldaServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.ColumnaServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.EstadoFinancieroServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.FecuServiceLocal;
import cl.bicevida.revelaciones.exceptions.EstadoFinancieroException;

import java.io.InputStream;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
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

import static org.hamcrest.Matchers.equalTo;


@Stateless
public class CargadorEeffServiceBean implements CargadorEeffServiceLocal {
    
    private final Logger logger = Logger.getLogger(CargadorEeffServiceBean.class);
    
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "revelacionesPU")
    private EntityManager em;
    
    @EJB
    private EstadoFinancieroServiceLocal estadoFinancieroService;
    @EJB
    private FecuServiceLocal fecuService;

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
        
         Map<Long, CodigoFecu> fecuMap = fecuService.getCodigoFecusVigentes();
         Map<Long, CuentaContable> cuentaMap = fecuService.getCodigoCuentasVigentes();
        
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
                            EeffUtil.addErrorCuentaNotFound(c+1, r1+1,errores);
                        }
                        
                        if(cuentaCargadaMap.containsKey(idCuenta))
                            EeffUtil.addErrorCuentaDu(c+1, r1+1,errores);
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
                    detalleEeff.setMontoEbs(getValorBigDecimal(cell));
                    break;
                
                case 4:
                    detalleEeff.setMontoReclasificacion(getValorBigDecimal(cell));
                    break;
                
                case 5:
                    detalleEeff.setMontoPesos(getValorBigDecimal(cell));
                    break;
                
                case 6:
                    detalleEeff.setMontoMiles(getValorBigDecimal(cell));
                    break;
                
                case 7:
                    detalleEeff.setMontoPesosMil(getValorBigDecimal(cell));
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
    
    public void validarNuevoEeff(final List<EstadoFinanciero> eeffListNuevo,final Long idPeriodo,final CargadorEeffVO cargadorVO) throws Exception{
        
        VersionEeff versionEeff = null;
        
        try{
            versionEeff = estadoFinancieroService.getVersionEeffVigenteFindByPeriodo(idPeriodo);
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
        List<EstadoFinanciero>  eeffList  = estadoFinancieroService.getEeffByVersion(versionEeff.getIdVersionEeff());
        List<DetalleEeff> eeffDetList = estadoFinancieroService.getDetalleEeffByVersion(versionEeff.getIdVersionEeff());
        
        Map<Long,EstadoFinanciero> eeffMap = index(eeffList, on(EstadoFinanciero.class).getIdFecu());
        Map<String,DetalleEeff> detalleEeffMap = EeffUtil.convertListEeffDetToMap(eeffDetList);
        
        /*Lista con los mapeos de estados financieros vigentes de la bdd*/
        List<RelacionEeff> relEeffList = estadoFinancieroService.getRelacionEeffByPeriodo(idPeriodo);
        List<RelacionDetalleEeff> relDetEeffList = estadoFinancieroService.getRelacionDetalleEeffByPeriodo(idPeriodo);
        
        Map<Long,RelacionEeff> relEeffMap = index(relEeffList, on(RelacionEeff.class).getIdFecu());
        Map<String,RelacionDetalleEeff> relDetalleEeffMap = EeffUtil.convertListRelEeffDetToMap(relDetEeffList);
        
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
        
        relEeffBorradoList.addAll(relEeffMap.values()); // = new ArrayList<RelacionEeff>(relEeffMap.values());
        relEeffDetBorradoList.addAll(relDetalleEeffMap.values()); // = new ArrayList<RelacionDetalleEeff>(relDetalleEeffMap.values());
        
        
        
        cargadorVO.setEeffBorradoList(eeffBorradoList);
        cargadorVO.setEeffDetBorradoList(eeffDetBorradoList);
        cargadorVO.setEeffDescuadreList(eeffDescuadreList);
        cargadorVO.setEeffDetDescuadreList(eeffDetDescuadreList);
        
        cargadorVO.setRelEeffBorradoList(relEeffBorradoList);
        cargadorVO.setRelEeffDetBorradoList(relEeffDetBorradoList);
        cargadorVO.setRelEeffDescuadreList(relEeffDescuadreList);
        cargadorVO.setRelEeffDetDescuadreList(relEeffDetDescuadreList);
        
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
                        
                        if(!eeffDet.getMontoPesos().equals(eeffDetNuevo.getMontoPesos())){
                            
                            eeffDet.setMontoPesosNuevo(eeffDetNuevo.getMontoPesos());
                            eeffDetDescuadreList.add(eeffDet);
                            
                            logger.info("Descuadre en pesos Cuenta : " + eeffDetNuevo.getIdCuenta() + 
                                        " - Monto Nuevo : "  + eeffDet.getMontoPesosNuevo() + 
                                        " - Monto Antiguo " +  eeffDet.getMontoPesos());
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
                                            final Map<Long,RelacionEeff> relEeffMap, 
                                            final Map<String,RelacionDetalleEeff> relDetalleEeffMap,
                                            final List<RelacionEeff>  relEeffDescuadreList,
                                            final List<RelacionDetalleEeff> relEeffDetDescuadreList){
        
        Long idFecu = eeffNuevo.getIdFecu();
        
        /*Validando mapeo monto de FECU relacion EEFF contra monto FECU de EEFF nuevo*/
        if(relEeffMap.containsKey(idFecu)){
            
            RelacionEeff relEeff = relEeffMap.get(idFecu);
            
            if(!relEeff.getMontoTotal().equals(eeffNuevo.getMontoTotal())){
                
                relEeff.setMontoTotalNuevo(eeffNuevo.getMontoTotal());
                relEeffDescuadreList.add(relEeff);
                
                logger.info("Descuadre en Mapeo monto Fecu : " + EeffUtil.formatFecu(idFecu) + 
                            " - Monto Nuevo : "  + relEeff.getMontoTotalNuevo() + 
                            " - Monto Antiguo " + relEeff.getMontoTotal());
            }
            
            /*Validando mapeo monto de CUENTA relacion EEFF contra monto CUENTA de EEFF nuevo*/
            if(Util.esListaValida(eeffNuevo.getDetalleEeffList4())){
                
                for(DetalleEeff eeffDetNuevo : eeffNuevo.getDetalleEeffList4()){
                    
                    String key = EeffUtil.formatKeyFecuCuenta(eeffDetNuevo.getIdFecu(), eeffDetNuevo.getIdCuenta());
                    
                    if(relDetalleEeffMap.containsKey(key)){
                        
                        RelacionDetalleEeff relEeffDet = relDetalleEeffMap.get(key);
                        
                        if(!relEeffDet.getMontoPesos().equals(eeffDetNuevo.getMontoPesos())){
                            
                            relEeffDet.setMontoPesosNuevo(eeffDetNuevo.getMontoPesos());
                            relEeffDetDescuadreList.add(relEeffDet);
                            
                            logger.info("Descuadre en Mapeo pesos Cuenta : " + eeffDetNuevo.getIdCuenta() + 
                                        " - Monto Nuevo : "  + relEeffDet.getMontoPesosNuevo() + 
                                        " - Monto Antiguo " + relEeffDet.getMontoPesos());
                        }
                        
                        relDetalleEeffMap.remove(key);
                        
                    }
                }
            }
            
            relEeffMap.remove(idFecu);
        }
    }
    
    
    public void buildMailList(final CargadorEeffVO cargadorVO){
        
        final StringBuilder mensaje = new StringBuilder();
        
        /*NombreUsuario, UsuarioGrupo*/
        final Map<String, UsuarioGrupo> usuarioMailMap = new HashMap<String, UsuarioGrupo>();
        
        
        if(Util.esListaValida(cargadorVO.getRelEeffBorradoList())){
            
            Catalogo catalogo = cargadorVO.getRelEeffBorradoList().get(0).getCelda2().getColumna().getGrilla().getEstructura1().getVersion().getCatalogo();
            mensaje .append("<table>")
                    .append("<tr>")
                    .append("<td colspan='5'>").append("Se han eliminado los siguientes C�digos FECU : ").append("</td>")
                    .append("</tr>")
                    .append("<tr>")
                    .append("<td colspan='5'>").append("Catalogo : ").append(catalogo.getNombre()).append("</td>")
                    .append("</tr>")
                    .append("</table>");
            
            mensaje .append("<table>")
                    .append(EeffUtil.getTbodyRelEeff());
            
            for(RelacionEeff relEeff : cargadorVO.getRelEeffBorradoList()){
                mensaje
                    .append("<tr>")
                    .append("<td>").append(relEeff.getFecuFormat()).append("</td>")
                    .append("<td>").append(relEeff.getCelda2().getColumna().getTituloColumna()).append("</td>")
                    .append("<td>").append(relEeff.getIdFila()).append("</td>")
                    .append("<td>").append(Util.formatCellKey(relEeff.getCelda2())).append("</td>")
                    .append("<td>").append(relEeff.getCelda2().getValor()).append("</td>")
                    .append("</tr>");

            }
            
            mensaje.append("</table>");

        }
        
        if(Util.esListaValida(cargadorVO.getRelEeffDetBorradoList())){
            
            Catalogo catalogo = cargadorVO.getRelEeffDetBorradoList().get(0).getCelda5().getColumna().getGrilla().getEstructura1().getVersion().getCatalogo();
            mensaje .append("<table>")
                    .append("<tr>")
                    .append("<td colspan='5'>").append("Se han eliminado los siguientes C�digos de Cuenta : ").append("</td>")
                    .append("</tr>")
                    .append("<tr>")
                    .append("<td colspan='5'>").append("Catalogo : ").append(catalogo.getNombre()).append("</td>")
                    .append("</tr>")
                    .append("</table>");
            
            mensaje .append("<table>")
                    .append(EeffUtil.getTbodyRelDetEeff());
            
            for(RelacionDetalleEeff relDetalleEeff : cargadorVO.getRelEeffDetBorradoList()){
                mensaje
                    .append("<tr>")
                    .append("<td>").append(relDetalleEeff.getFecuFormat()).append("</td>")
                    .append("<td>").append(relDetalleEeff.getIdCuenta()).append("</td>")
                    .append("<td>").append(relDetalleEeff.getCelda5().getColumna().getTituloColumna()).append("</td>")
                    .append("<td>").append(relDetalleEeff.getIdFila()).append("</td>")
                    .append("<td>").append(Util.formatCellKey(relDetalleEeff.getCelda5())).append("</td>")
                    .append("<td>").append(relDetalleEeff.getCelda5().getValor()).append("</td>")
                    .append("</tr>");

            }
            
            mensaje.append("</table>");
        }
        
        logger.info(mensaje);
        
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
        
        cargadorVO.setRelEeffDetBorradoList(sort(cargadorVO.getRelEeffDescuadreList(), on(RelacionDetalleEeff.class), cuentaOrderBy));
        cargadorVO.setRelEeffDetDescuadreList(sort(cargadorVO.getRelEeffDescuadreList(), on(RelacionDetalleEeff.class), cuentaOrderBy));
        
    }
    
}