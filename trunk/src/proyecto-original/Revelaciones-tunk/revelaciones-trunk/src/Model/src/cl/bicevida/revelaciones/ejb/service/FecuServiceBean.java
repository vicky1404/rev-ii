package cl.bicevida.revelaciones.ejb.service;


import static ch.lambdaj.Lambda.index;
import static ch.lambdaj.Lambda.on;

import cl.bicevida.revelaciones.ejb.common.VigenciaEnum;
import static cl.bicevida.revelaciones.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;
import cl.bicevida.revelaciones.ejb.cross.EeffUtil;
import cl.bicevida.revelaciones.ejb.entity.CodigoFecu;
import cl.bicevida.revelaciones.ejb.entity.CuentaContable;
import cl.bicevida.revelaciones.ejb.entity.GrupoEeff;
import cl.bicevida.revelaciones.ejb.service.local.EstadoFinancieroServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.FecuServiceLocal;
import cl.bicevida.revelaciones.exceptions.EstadoFinancieroException;

import java.io.InputStream;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;


@Stateless
public class FecuServiceBean implements FecuServiceLocal{
    
    
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;
    
    @EJB
    EstadoFinancieroServiceLocal estadoFinancieroService;
    
    
    public FecuServiceBean() {
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Map<Long, CodigoFecu> getCodigoFecusVigentes(){
        List<CodigoFecu> codigos = em.createNamedQuery(CodigoFecu.FIND_VIGENTE).getResultList();
        return index(codigos, on(CodigoFecu.class).getIdFecu());
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Map<Long, CuentaContable> getCodigoCuentasVigentes(){
        List<CuentaContable> codigos = em.createNamedQuery(CuentaContable.FIND_VIGENTE).getResultList();
        return index(codigos, on(CuentaContable.class).getIdCuenta());
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Map<Long, CodigoFecu> getCodigoFecusAll(){
        List<CodigoFecu> codigos = em.createNamedQuery(CodigoFecu.FIND_ALL).getResultList();
        return index(codigos, on(CodigoFecu.class).getIdFecu());
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Map<Long, CuentaContable> getCuentaContableAll(){
        List<CuentaContable> cuentas = em.createNamedQuery(CuentaContable.FIND_ALL).getResultList();
        return index(cuentas, on(CuentaContable.class).getIdCuenta());
    }
    
    
    public List<CodigoFecu> leerFECU(final InputStream loadedExcel)throws EstadoFinancieroException, Exception {
        
        XSSFWorkbook workBook = new XSSFWorkbook(loadedExcel);
        List<String> errores = new ArrayList<String>();
        
        if(workBook.getSheetAt(0) == null)
            throw new EstadoFinancieroException("El documento Excel no contiene datos");
        
        if(workBook.getSheetAt(0).getPhysicalNumberOfRows() == 0)
            throw new EstadoFinancieroException("El documento Excel no contiene filas");   
        
        Map<Long, CodigoFecu> fecuMap = getCodigoFecusAll();
        
        List<CodigoFecu> nuevoFecuList = new ArrayList<CodigoFecu>();
        
        XSSFRow row;
        XSSFCell cell;
        XSSFSheet sheet = workBook.getSheetAt(0);
        
        int cols = 2;
        int rows = sheet.getPhysicalNumberOfRows();
        
        for(int r1 = 1; r1 < rows; r1++) {
            
            row = sheet.getRow(r1);
            CodigoFecu fecu = new CodigoFecu();
            Long idFecu = null;
            
            if(row != null) {
                for(int c = 0; c < cols; c++) {
                    
                    cell = row.getCell(c);

                    switch(c){
                        case 0: //Codigo Fecu Long
                            try{
                                idFecu = ((Double)cell.getNumericCellValue()).longValue();
                                if(!fecuMap.containsKey(idFecu)){
                                    fecu.setIdFecu(idFecu);
                                    fecu.setVigencia(VigenciaEnum.VIGENTE.getKey());
                                }else{
                                    idFecu = null;
                                }
                            }catch(Exception e){
                                idFecu = null;
                                EeffUtil.addErrorFecuNull(c+1, r1+1,errores);
                            }
                        break;
                        
                        case 1: //Descrición fECU
                            try{
                                if(cell.getStringCellValue()==null || cell.getStringCellValue().trim().equals("")){
                                    idFecu = null;
                                    EeffUtil.addErrorFecuDescripcionNull(c+1, r1+1,errores);
                                }else{
                                    fecu.setDescripcion(cell.getStringCellValue());
                                }
                            }catch(Exception e){
                                idFecu = null;
                                EeffUtil.addErrorFecuDescripcionNull(c+1, r1+1,errores);
                            }
                        break;
                    }
                }
                
                if(idFecu!=null){
                    
                    if(!nuevoFecuList.contains(fecu)){
                        nuevoFecuList.add(fecu);    
                    }
                }
            }
        }
        
        if(errores.size()>0)
            throw new EstadoFinancieroException(errores);
        
        return nuevoFecuList;
        
    }
    
    
    public List<CuentaContable> leerCuenta(final InputStream loadedExcel)throws EstadoFinancieroException, Exception {
        
        XSSFWorkbook workBook = new XSSFWorkbook(loadedExcel);
        List<String> errores = new ArrayList<String>();
        
        if(workBook.getSheetAt(0) == null)
            throw new EstadoFinancieroException("El documento Excel no contiene datos");
        
        if(workBook.getSheetAt(0).getPhysicalNumberOfRows() == 0)
            throw new EstadoFinancieroException("El documento Excel no contiene filas");   
        
        Map<Long, CuentaContable> cuentaMap = getCuentaContableAll();
        
        List<CuentaContable> nuevaCuentaList = new ArrayList<CuentaContable>();
        
        XSSFRow row;
        XSSFCell cell;
        XSSFSheet sheet = workBook.getSheetAt(0);
        
        int cols = 2;
        int rows = sheet.getPhysicalNumberOfRows();
        
        for(int r1 = 1; r1 < rows; r1++) {
            
            row = sheet.getRow(r1);
            CuentaContable cuenta = new CuentaContable();
            Long idCuenta = null;
            
            if(row != null) {
                for(int c = 0; c < cols; c++) {
                    
                    cell = row.getCell(c);

                    switch(c){
                        case 0: //Codigo Fecu Long
                            try{
                                idCuenta = ((Double)cell.getNumericCellValue()).longValue();
                                if(!cuentaMap.containsKey(idCuenta)){
                                    cuenta.setIdCuenta(idCuenta);
                                    cuenta.setVigencia(VigenciaEnum.VIGENTE.getKey());
                                }else{
                                    idCuenta = null;
                                }
                            }catch(Exception e){
                                idCuenta = null;
                                EeffUtil.addErrorFecuNull(c+1, r1+1,errores);
                            }
                        break;
                        
                        case 1: //Descrición fECU
                            try{
                                if(cell.getStringCellValue()==null || cell.getStringCellValue().trim().equals("")){
                                    idCuenta = null;
                                    EeffUtil.addErrorCuentaDescripcionNull(c+1, r1+1,errores);
                                }else{
                                    cuenta.setDescripcion(cell.getStringCellValue());
                                }
                            }catch(Exception e){
                                idCuenta = null;
                                EeffUtil.addErrorCuentaDescripcionNull(c+1, r1+1,errores);
                            }
                        break;
                    }
                }
                
                if(idCuenta!=null){
                    
                    if(!nuevaCuentaList.contains(cuenta)){
                        nuevaCuentaList.add(cuenta);    
                    }
                }
            }
        }
        
        if(errores.size()>0)
            throw new EstadoFinancieroException(errores);
        
        return nuevaCuentaList;
        
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistFecu(final List<CodigoFecu> fecuList, final String usuario) throws Exception {

        for(CodigoFecu fecu : fecuList){
            fecu.setEditarId(false);
            fecu.setUsuarioCreacion(usuario);
            fecu.setFechaCreacion(new Date());
            em.persist(fecu);
        }
        
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void mergeFecu(final List<CodigoFecu> fecuList, final String usuario) throws Exception {

        for(CodigoFecu fecu : fecuList){
            
            CodigoFecu fecuBD = em.find(CodigoFecu.class, fecu.getIdFecu());
            fecu.setEditarId(false);
            
            if(fecuBD !=null && fecuBD.getIdFecu()!= null && fecuBD.getDescripcion()!=null){
                if(!fecuBD.getIdFecu().equals(fecu.getIdFecu()) || !fecuBD.getDescripcion().equals(fecu.getDescripcion())){
                    fecu.setUsuarioModificacion(usuario);
                    fecu.setFechaModificacion(new Date());
                    em.merge(fecu);
                }
            }else{
                fecu.setUsuarioCreacion(usuario);
                fecu.setFechaCreacion(new Date());
                em.merge(fecu);   
            }
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void mergeConfiguracionPortadaEEFF(final List<CodigoFecu> fecuList, final String usuario) throws Exception {

        for(CodigoFecu fecu : fecuList){
                fecu.setUsuarioModificacion(usuario);                
                fecu.setFechaModificacion(new Date());
                em.merge(fecu);   
        }
    }

    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistCuenta(final List<CuentaContable> cuentaList,final String usuario) throws Exception {

        for(CuentaContable cuenta : cuentaList){
            cuenta.setEditarId(false);
            cuenta.setFechaCreacion(new Date());
            cuenta.setUsuarioCreacion(usuario);
            em.persist(cuenta);
        }
        
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void mergeCuenta(final List<CuentaContable> cuentaList,final String usuario) throws Exception {

        for(CuentaContable cuenta : cuentaList){
            
            CuentaContable cuentaBD = em.find(CuentaContable.class, cuenta.getIdCuenta());
            cuenta.setEditarId(false);
            
            if(cuentaBD !=null && cuentaBD.getIdCuenta()!= null && cuentaBD.getDescripcion()!=null){
                if(!cuentaBD.getIdCuenta().equals(cuenta.getIdCuenta()) || !cuentaBD.getDescripcion().equals(cuenta.getDescripcion())){
                    cuenta.setUsuarioModificacion(usuario);
                    cuenta.setFechaModificacion(new Date());
                    em.merge(cuenta);
                }
            }else{
                cuenta.setUsuarioCreacion(usuario);
                cuenta.setFechaCreacion(new Date());
                em.merge(cuenta);   
            }
        }
        
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<GrupoEeff> getGruposEeff() throws Exception {
        
             Query query = em.createNamedQuery(GrupoEeff.FIND_ALL)                
                                    .setHint(QueryHints.MAINTAIN_CACHE, HintValues.FALSE)
                                    .setHint(QueryHints.REFRESH, HintValues.TRUE);
            return query.getResultList();
        }
    
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistGrupoEeff(final List<GrupoEeff> grupoEeffList) throws Exception {

        for(GrupoEeff grupo : grupoEeffList){
            em.merge(grupo);
        }
        
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void mergeAsignarGrupoACodigoFecu(final CodigoFecu fecu, GrupoEeff grupoEeff) throws Exception {        
        fecu.setGrupoEeff(grupoEeff);
        if (grupoEeff == null){
            fecu.setBold(Boolean.FALSE);
            fecu.setSubGrupo(Boolean.FALSE);
        }
        em.merge(fecu);            
    }    

    
}
