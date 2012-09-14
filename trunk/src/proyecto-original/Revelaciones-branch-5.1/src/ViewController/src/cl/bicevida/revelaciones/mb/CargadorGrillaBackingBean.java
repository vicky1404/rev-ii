package cl.bicevida.revelaciones.mb;


import cl.bicevida.revelaciones.common.filtros.Filtro;
import cl.bicevida.revelaciones.common.mb.SoporteBackingBean;
import cl.bicevida.revelaciones.common.util.GeneradorDisenoHelper;
import cl.bicevida.revelaciones.common.util.PropertyManager;
import cl.bicevida.revelaciones.ejb.common.TipoEstructuraEnum;
import cl.bicevida.revelaciones.ejb.entity.Estructura;
import cl.bicevida.revelaciones.ejb.entity.Grilla;
import cl.bicevida.revelaciones.ejb.entity.VersionPeriodo;
import cl.bicevida.revelaciones.exceptions.CargaGrillaExcelException;

import java.text.MessageFormat;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import javax.persistence.NoResultException;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputFile;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.nav.RichCommandLink;
import oracle.adf.view.rich.component.rich.output.RichOutputLabel;
import oracle.adf.view.rich.context.AdfFacesContext;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.model.UploadedFile;


public class CargadorGrillaBackingBean extends SoporteBackingBean{

    @SuppressWarnings("compatibility:580390737427250892")
    private static final long serialVersionUID = -1504322420517463047L;
    private transient Logger logger = Logger.getLogger(CargadorGrillaBackingBean.class);
    private List<Estructura> structureList;
    private List<Estructura> structureListData;
    private List<Grilla> gridList = new ArrayList<Grilla>();
    private Estructura structure;
    private VersionPeriodo versionPeriodSelected;
    private String rowLoad = "";
    private Long idGrid = -1L;
    private transient UploadedFile uploadedFile;
    private transient RichInputFile richInputFile;
    private transient RichTable cuadroTable;
    private transient RichOutputLabel rowLabel;
    private transient RichSelectOneChoice comboCatalogo;
    private transient RichSelectOneChoice comboMesPeriodo;
    private transient RichSelectOneChoice comboAnioPeriodo;
    private transient RichTable periodoCatalogoTable;
    private boolean renderedInformacion = false;
    private boolean renderedPeriodoList = false; 
    private boolean renderedSelectTable = false;
    private boolean renderedGridTable = false;
    
    
    public void limpiar() throws Exception{
        this.setStructure(null);
        this.getGridList().clear();
        this.setUploadedFile(null);
        this.getRichInputFile().resetValue();        
        super.getFiltro().setCatalogo(null);
        super.getFiltro().setPeriodo(null);
        super.getFiltro().setVersion(null);
        getComponenteBackingBean().setPeriodoCatalogoList(null);
        this.resetCombo(this.getComboCatalogo());
        this.resetCombo(this.getComboMesPeriodo());
        this.resetCombo(this.getComboAnioPeriodo());
        this.setRenderedSelectTable(Boolean.FALSE);
        this.setRenderedGridTable(Boolean.FALSE);
        this.setRenderedInformacion(Boolean.FALSE);
        this.setRenderedPeriodoList(Boolean.FALSE);
    }

    public String limpiarCargadorGrilla(){ 
        try{
            limpiar();
        } catch (Exception e) {
            e.printStackTrace();
            agregarErrorMessage("Se ha prodicido un error al inicializar los datos de la página");  
        }
        return null;
    }
    
    private void resetCombo(RichSelectOneChoice combo){
       combo.resetValue();
       AdfFacesContext.getCurrentInstance().addPartialTarget(combo);
    }
    
    public Object buscarVersion(){
        
        Filtro filtroPaso = getFiltro();
        filtroPaso.getPeriodo().setPeriodo(null);
        getComponenteBackingBean().setPeriodoCatalogoList(null);
        getCuadroBackingBean().setEstructuraList(null);

        if(filtroPaso.getCatalogo().getIdCatalogo()!=null){
            try{
                this.renderedPeriodoList = true;
                Long periodo = Long.valueOf(filtroPaso.getPeriodo().getAnioPeriodo().concat(filtroPaso.getPeriodo().getMesPeriodo()));                
                try{
                    getFiltro().setPeriodo(getFacade().getMantenedoresTipoService().findByPeriodo(periodo));
                }catch(NoResultException e){
                    agregarWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("periodo_busqueda_sin_resultado_periodo"), getFiltro().getPeriodo().getAnioPeriodo(), getFiltro().getPeriodo().getMesPeriodo()));
                    getComponenteBackingBean().setPeriodoCatalogoList(null);
                    this.renderedPeriodoList = false;
                    return null;                    
                }catch(EJBException e){
                    agregarWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("periodo_busqueda_sin_resultado_periodo"), getFiltro().getPeriodo().getAnioPeriodo(), getFiltro().getPeriodo().getMesPeriodo()));
                    getComponenteBackingBean().setPeriodoCatalogoList(null);
                    this.renderedPeriodoList = false;
                    return null;
                }
                getComponenteBackingBean().setPeriodoCatalogoList(getFacade().getPeriodoService().findPeriodoAllByPeriodoCatalogo(filtroPaso.getCatalogo(),filtroPaso.getPeriodo()));
                if(getComponenteBackingBean().getPeriodoCatalogoList() == null){  
                    this.renderedPeriodoList = false;
                    agregarWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("periodo_busqueda_sin_resultado_periodo"), getFiltro().getPeriodo().getAnioPeriodo(), getFiltro().getPeriodo().getMesPeriodo()));
                }
            }catch(Exception e){
                logger.error(e.getCause(), e);
                agregarErrorMessage("Error al consultar Versiones para el Período");
            }
            
        }
        
        return null;
    }

    public String search(){
        
        try{
            getGridList().clear();
            setStructureListData(null);
            setRenderedSelectTable(false);
            setRenderedGridTable(false);
            setIdGrid(-1L);
            setRowLoad("");
            this.setVersionPeriodSelected((VersionPeriodo)getPeriodoCatalogoTable().getRowData());
            getFiltro().setVersion(this.getVersionPeriodSelected().getVersion());
            
            if (getVersionPeriodSelected() != null && getVersionPeriodSelected().getVersion() != null){
                    getFiltro().setVersion(getVersionPeriodSelected().getVersion());
            } else {
                    getFiltro().setVersion(super.getFiltro().getVersion());
            }
            List<Estructura> estructuraList = super.getFacade().getEstructuraService().getEstructuraByVersionTipo(super.getFiltro().getVersion(), TipoEstructuraEnum.GRILLA.getKey());
            
            if(estructuraList==null){
                agregarErrorMessage("No hay grillas disponibles para la versión seleccionada");
                return null;
            }
            
            for(Estructura estructura :estructuraList){
                for(Grilla grilla : estructura.getGrillaList()){
                    getGridList().add(grilla);
                }
            }
            setListGrilla(estructuraList);
            this.setStructureListData(estructuraList);
            
            if(getStructureListData().isEmpty()){
                setRenderedInformacion(false);
                agregarWarnMessage(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_version_sin_registros"), 
                                                        getVersionPeriodSelected().getVersion().getCatalogo().getNombre(),
                                                        getVersionPeriodSelected().getPeriodo().getAnioPeriodo(), 
                                                        getVersionPeriodSelected().getPeriodo().getMesPeriodo() , 
                                                        getVersionPeriodSelected().getVersion().getVersion()));
            }else{
                setRenderedInformacion(true);
                if(getGridList().size()==1){
                    setStructure(getStructureByIdGrilla(getGridList().get(0).getIdGrilla(),estructuraList));
                    setRenderedGridTable(false);
                    setRenderedSelectTable(true);
                }else{
                    setRenderedGridTable(true);
                    setRenderedSelectTable(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            agregarErrorMessage(PropertyManager.getInstance().getMessage("general_mensaje_nota_get_catalogo_error"));  
        }
        return null;
    }

    public void selectGrillaListener(ActionEvent actionEvent) {
        RichCommandLink comp = (RichCommandLink)actionEvent.getComponent();
        Long idGrilla = (Long)comp.getAttributes().get("numGrilla");
        setStructure(getStructureByIdGrilla(idGrilla,  getStructureListData()));
    }
    
    private Estructura getStructureByIdGrilla(Long idGrid, List<Estructura> structures){
         
        for(Estructura structure : structures){
            for(Grilla grid : structure.getGrillaList()){
                if(idGrid.equals(grid.getIdGrilla())){
                   return structure;
                }
            }
        }
        
        return null;
    }

    public void selectGroupListener(ActionEvent actionEvent) {
        
        RichCommandLink comp = (RichCommandLink)actionEvent.getComponent();        
        Long fila = (Long)comp.getAttributes().get("numFila");
        Long grilla = (Long)comp.getAttributes().get("numGrilla");
        setRowLoad(String.valueOf(fila));
        setIdGrid(grilla);
        setADFPartialTarget(getRowLabel());
    }
    
    public void fileStructureValidatorXls(FacesContext facesContext, UIComponent uIComponent, Object object){
        try {
            setUploadedFile(GeneradorDisenoHelper.archivoEstructuraValidator(facesContext, (UploadedFile)object, (RichInputFile)uIComponent));
        } catch (Exception e) {
            logger.error(e.getCause(), e);
            agregarErrorMessage(PropertyManager.getInstance().getMessage("Error al procesar archivo"));
        }
    }
    
    public void processFile(ActionEvent event) {                
        try {
            if(this.getUploadedFile() == null){
                agregarErrorMessage("Seleccione o Actualice el archivo que desea cargar");
                return;
            }
            if(this.getUploadedFile().getInputStream() == null){
                agregarErrorMessage("Seleccione o Actualice el archivo que desea cargar");
                return;
            }
            if(getRowLoad()==null || getRowLoad().equals("") || getIdGrid()==null){
                agregarErrorMessage("Seleccione una fila para cargar");
                return;
            }
            
            final Grilla grid = getFacade().getCargadorEstructuraService().getGrillaByExcelLoader(this.getStructure().getGrillaList().get(0), this.getUploadedFile().getInputStream(), getIdGrid(), Long.valueOf(getRowLoad())); 
            
            for(Estructura structure: getStructureListData()){
                if(structure.getIdEstructura().equals(getIdGrid())){
                    List<Grilla> gridList = new ArrayList<Grilla>();
                    gridList.add(grid);
                    structure.setGrillaVO(getFacade().getEstructuraService().getGrillaVO(grid,true));
                    structure.setGrillaList(gridList);
                    setStructure(structure);
                    break;
                }
            }
            setRenderedSelectTable(true);
            setListGrilla(getStructureListData());

        } catch (CargaGrillaExcelException e) {
            logger.error("error al procesar archivo excel ",e);
            agregarErrorMessage(e.getMessage());
        
        } catch (Exception e) {
            logger.error("error al procesar archivo excel ",e);
            agregarErrorMessage("Error al procesar el archivo");
        }
    }
    
    public void saveListener(ActionEvent event){
        
        try{
            for(Grilla grid : getStructure().getGrillaList()){
                if(grid.getIdGrilla().equals(getIdGrid())){
                    getFacade().getGrillaService().persistCell(grid);
                    setGrillaVO(getStructure());
                    break;
                }
            }            
            agregarSuccesMessage("Se ha almacenado correctamente la información");
        }catch(Exception e){
            logger.error("error al procesar archivo excel ",e);
            agregarErrorMessage("Error al guardar el información");
        }
    }

    public void setCuadroTable(RichTable cuadroTable) {
        this.cuadroTable = cuadroTable;
    }

    public RichTable getCuadroTable() {
        return cuadroTable;
    }

    public void setRowLoad(String filaCargar) {
        this.rowLoad = filaCargar;
    }

    public String getRowLoad() {
        return rowLoad;
    }

    public void setRowLabel(RichOutputLabel rowLabel) {
        this.rowLabel = rowLabel;
    }

    public RichOutputLabel getRowLabel() {
        return rowLabel;
    }

    public void setIdGrid(Long idGrilla) {
        this.idGrid = idGrilla;
    }

    public Long getIdGrid() {
        return idGrid;
    }

    public void setRenderedGridTable(boolean renderedGridTable) {
        this.renderedGridTable = renderedGridTable;
    }

    public boolean isRenderedGridTable() {
        return renderedGridTable;
    }

    public void setRenderedSelectTable(boolean renderedSelectTable) {
        this.renderedSelectTable = renderedSelectTable;
    }

    public boolean isRenderedSelectTable() {
        return renderedSelectTable;
    }

    public void setGridList(List<Grilla> grillaList) {
        this.gridList = grillaList;
    }

    public List<Grilla> getGridList() {
        return gridList;
    }

    public void setStructureListData(List<Estructura> estructuraListData) {
        this.structureListData = estructuraListData;
    }

    public List<Estructura> getStructureListData() {
        return structureListData;
    }

    public void setStructure(Estructura estructura) {
        this.structure = estructura;
    }

    public Estructura getStructure() {
        return structure;
    }
    public void setStructureList(List<Estructura> estructuraList) {
        this.structureList = estructuraList;
    }

    public List<Estructura> getStructureList() {
        return structureList;
    }

    public void setVersionPeriodSelected(VersionPeriodo versionPeriodoSelected) {
        this.versionPeriodSelected = versionPeriodoSelected;
    }

    public VersionPeriodo getVersionPeriodSelected() {
        return versionPeriodSelected;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setRichInputFile(RichInputFile richInputFile) {
        this.richInputFile = richInputFile;
    }

    public RichInputFile getRichInputFile() {
        return richInputFile;
    }

    public void setComboCatalogo(RichSelectOneChoice comboCatalogo) {
        this.comboCatalogo = comboCatalogo;
    }

    public RichSelectOneChoice getComboCatalogo() {
        return comboCatalogo;
    }

    public void setComboMesPeriodo(RichSelectOneChoice comboMesPeriodo) {
        this.comboMesPeriodo = comboMesPeriodo;
    }

    public RichSelectOneChoice getComboMesPeriodo() {
        return comboMesPeriodo;
    }

    public void setComboAnioPeriodo(RichSelectOneChoice comboAnioPeriodo) {
        this.comboAnioPeriodo = comboAnioPeriodo;
    }

    public RichSelectOneChoice getComboAnioPeriodo() {
        return comboAnioPeriodo;
    }

    public void setRenderedInformacion(boolean renderedInformacion) {
        this.renderedInformacion = renderedInformacion;
    }

    public boolean isRenderedInformacion() {
        return renderedInformacion;
    }

    public void setRenderedPeriodoList(boolean renderedPeriodoList) {
        this.renderedPeriodoList = renderedPeriodoList;
    }

    public boolean isRenderedPeriodoList() {
        return renderedPeriodoList;
    }

    public void setPeriodoCatalogoTable(RichTable periodoCatalogoTable) {
        this.periodoCatalogoTable = periodoCatalogoTable;
    }

    public RichTable getPeriodoCatalogoTable() {
        return periodoCatalogoTable;
    }
}
