package cl.bicevida.revelaciones.mb;


import cl.bicevida.revelaciones.common.mb.SoporteBackingBean;
import cl.bicevida.revelaciones.common.util.GeneradorDisenoHelper;
import cl.bicevida.revelaciones.common.util.PropertyManager;
import cl.bicevida.revelaciones.ejb.common.TipoEstadoEeffEnum;
import cl.bicevida.revelaciones.ejb.cross.EeffUtil;
import cl.bicevida.revelaciones.ejb.entity.EstadoFinanciero;
import cl.bicevida.revelaciones.ejb.entity.Periodo;
import cl.bicevida.revelaciones.ejb.entity.TipoEstadoEeff;
import cl.bicevida.revelaciones.ejb.entity.VersionEeff;
import cl.bicevida.revelaciones.exceptions.EstadoFinancieroException;

import cl.bicevida.revelaciones.eeff.CargadorEeffVO;

import cl.bicevida.revelaciones.eeff.RelacionEeffVO;

import cl.bicevida.revelaciones.ejb.cross.Util;

import cl.bicevida.revelaciones.ejb.entity.DetalleEeff;
import cl.bicevida.revelaciones.ejb.entity.RelacionDetalleEeff;

import cl.bicevida.revelaciones.ejb.entity.RelacionEeff;

import java.sql.SQLIntegrityConstraintViolationException;

import java.util.ArrayList;
import java.util.List;

import java.util.Map;

import javax.annotation.PostConstruct;

import javax.ejb.TransactionRolledbackLocalException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import oracle.adf.view.rich.component.rich.input.RichInputFile;

import oracle.toplink.exceptions.DatabaseException;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.model.UploadedFile;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class CargadorEeffBackingBean extends SoporteBackingBean {
    
    @SuppressWarnings("compatibility:4153622552299937499")
    private static final long serialVersionUID = 1609945839210513442L;
    
    private transient Logger logger = Logger.getLogger(CargadorEeffBackingBean.class);    
    
    private List<VersionEeff> versionEeffList;
    private transient RichInputFile richInputFile; 
    private transient UploadedFile uploadedFile;
    private Periodo periodo;
    private VersionEeff versionEeff;
    private CargadorEeffVO cargadorVO;
    private boolean renderTableRel = false;

    public CargadorEeffBackingBean() {
    }
    
    @PostConstruct
    public void cargar(){
        try{
            
            periodo = getFacade().getPeriodoService().findMaxPeriodoObj();
            
            versionEeffList = getFacade().getEstadoFinancieroService().getVersionEeffFindByPeriodo(periodo.getIdPeriodo());
            
        }catch(Exception e){
            logger.error(e);
            agregarErrorMessage("Error al buscar los periodos");
        }
    }
    
    private void init(){
        versionEeff = null;
        renderTableRel = false;
        cargadorVO = null;
    }
    
    public String procesarArchivo() {
        try {
            
            if(getUploadedFile() == null){
                init();
                agregarErrorMessage("Seleccione o Actualice el archivo que desea cargar");
                return null;
            }
            if(getUploadedFile().getInputStream() == null){
                init();
                agregarErrorMessage("Seleccione o Actualice el archivo que desea cargar");
                return null;
            }
            
            versionEeff = new VersionEeff();
            
            TipoEstadoEeff tipoEstadoEeff = getFacade().getEstadoFinancieroService().getTipoEstadoEeffById(TipoEstadoEeffEnum.INGRESADO.getKey());
            cargadorVO = getFacade().getCargadorEeffService().leerEeff(getUploadedFile().getInputStream());
            EeffUtil.setVersionEeffToEeffList(cargadorVO.getEeffList(), versionEeff);
            getFacade().getCargadorEeffService().validarNuevoEeff(cargadorVO.getEeffList(), periodo.getIdPeriodo(),cargadorVO);
            
            if(
                Util.esListaValida(cargadorVO.getEeffDescuadreList())||
                Util.esListaValida(cargadorVO.getEeffDetDescuadreList()) ||
                Util.esListaValida(cargadorVO.getEeffBorradoList()) ||
                Util.esListaValida(cargadorVO.getEeffDetBorradoList()) ||            
                Util.esListaValida(cargadorVO.getRelEeffDescuadreList()) ||
                Util.esListaValida(cargadorVO.getRelEeffDetDescuadreList()) ||
                Util.esListaValida(cargadorVO.getRelEeffBorradoList()) ||
                Util.esListaValida(cargadorVO.getRelEeffDetBorradoList())
            ) renderTableRel = true;
            
            versionEeff.setTipoEstadoEeff(tipoEstadoEeff);
            versionEeff.setUsuario(getNombreUsuario());
            versionEeff.setVigencia(1L);
            versionEeff.setPeriodo(periodo);
            versionEeff.setEstadoFinancieroList(cargadorVO.getEeffList());
            
        } catch (EstadoFinancieroException e) {
            
            agregarErrorMessage("El archivo presenta los siguiente errores : ");
            
            for(String str : e.getDetailErrors())
                agregarErrorMessage(str);
        
        } catch (Exception e) {
            logger.error("error al procesar archivo excel ",e);
            agregarErrorMessage("Error al procesar el archivo");
        }
        return null;
    }
    
    
    public void guardarListener(ActionEvent event){
        try{
            
            if(versionEeff!=null && cargadorVO.getEeffList()!=null)
                getFacade().getEstadoFinancieroService().persisVersionEeff(versionEeff);
            else{
                init();
                agregarErrorMessage("Debe cargar informaci�n antes de guardar");
                return;
            }
            
            versionEeffList = getFacade().getEstadoFinancieroService().getVersionEeffFindByPeriodo(periodo.getIdPeriodo());
            
            agregarSuccesMessage("Se ha almacenado correctamente los estados financieros");
            agregarSuccesMessage("Registros Cabecera :" + cargadorVO.getCatidadEeffProcesado());
            agregarSuccesMessage("Registros Detalle  :" + cargadorVO.getCatidadEeffDetProcesado());
            
            init();
            
        }catch(Exception e){
            logger.error("error al guardar eeff", e);
            agregarErrorMessage("Error al guardar informaci�n");
        }
    }
    
    public void archivoEstructuraValidator(FacesContext facesContext, UIComponent uIComponent, Object object){

        try {
            setUploadedFile(GeneradorDisenoHelper.archivoEstructuraValidator(facesContext, (UploadedFile)object, (RichInputFile)uIComponent));
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            agregarErrorMessage(PropertyManager.getInstance().getMessage("Error al procesar archivo"));
        }
    }
    

    public void setVersionEeffList(List<VersionEeff> versionEeffList) {
        this.versionEeffList = versionEeffList;
    }

    public List<VersionEeff> getVersionEeffList() {
        return versionEeffList;
    }

    public void setRichInputFile(RichInputFile richInputFile) {
        this.richInputFile = richInputFile;
    }

    public RichInputFile getRichInputFile() {
        return richInputFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setCargadorVO(CargadorEeffVO cargadorVO) {
        this.cargadorVO = cargadorVO;
    }

    public CargadorEeffVO getCargadorVO() {
        return cargadorVO;
    }

    public boolean isRenderTableRel() {
        return renderTableRel;
    }
}