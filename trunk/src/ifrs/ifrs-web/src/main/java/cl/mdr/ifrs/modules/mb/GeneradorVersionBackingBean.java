package cl.mdr.ifrs.modules.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.cross.model.CommonGridModel;
import cl.mdr.ifrs.cross.util.GeneradorDisenoHelper;
import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.Catalogo;
import cl.mdr.ifrs.ejb.entity.Estructura;
import cl.mdr.ifrs.ejb.entity.TipoCuadro;
import cl.mdr.ifrs.ejb.entity.Version;
import cl.mdr.ifrs.vo.GrillaModelVO;


@ManagedBean
@ViewScoped
public class GeneradorVersionBackingBean extends AbstractBackingBean{
	
	private transient Logger LOG = Logger.getLogger(GeneradorVersionBackingBean.class);
	
	private TipoCuadro tipoCuadro;
	private Long idTipoCuadro;
	

	private Catalogo catalogo;
	private Long idCatalogo;
	private List<Catalogo> catalogoList;
	private List<Version> versionList;
	private List<Estructura> estructuraList;
	private boolean almacenado = false;
	private DataTable estructuraTable;
	private boolean renderBotonEditar;
	private boolean renderEstructura;
	
	

	public void buscarListener(ActionEvent event){
		
		System.out.println(idCatalogo);
		
		try{
			catalogo = getFacadeService().getCatalogoService().findCatalogoByCatalogo(new Catalogo(idCatalogo));
			
			if(catalogo!=null)
				versionList = getFacadeService().getVersionService().findVersionAllByCatalogo(catalogo);
			else
				addInfoMessage("Sin versiones", "No existen versiones para catalogo seleccionado");
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	public Long getIdCatalogo() {
		return idCatalogo;
	}

	public void setIdCatalogo(Long idCatalogo) {
		this.idCatalogo = idCatalogo;
	}

	public List<Catalogo> completeCatalogo(String query) {
		
		List<Catalogo> suggestions = new ArrayList<Catalogo>();  
		
		if(getCatalogoList()==null)
			return suggestions;
		
        
        for(Catalogo p : getCatalogoList()) {  
        	if(p.getNombre().toUpperCase().indexOf(query.toUpperCase()) >= 0)
                suggestions.add(p);  
        }  
          
        return suggestions;  
    }
	
	
	
	public void tipoCuadroChange(){
		try{
			
			System.out.println("Prueba de metodo");
			
			if(tipoCuadro!=null)
				catalogoList = getFacadeService().getCatalogoService().findCatalogoByFiltro(getNombreUsuario(), tipoCuadro, null, 1L);
		}catch(Exception e){
			addErrorMessage("Error al buscar catalogo", "Error al buscar catalogo");
			e.printStackTrace();
		}
		
	}
	
	public void tipoImpresionChange(){
		
		System.out.println("Prueba de metodo 2");
		System.out.println("Prueba de metodo 2");
		System.out.println("Prueba de metodo 2");
		System.out.println("Prueba de metodo 2");
	}
		
	
    public List<Estructura> getEstructuraList() {
        
    	if(estructuraList==null){
        	estructuraList = new ArrayList<Estructura>();
            Estructura estructura = new Estructura();
            estructura.setOrden(1L);
            estructuraList.add(estructura);
        }
    	
        return estructuraList;
    }
    
    public int getSizeEstructrua(){
        return getEstructuraList().size();
    }
    
    public void agregarTipoEstructuraListener(ActionEvent actionEvent) {
        setAlmacenado(false);    
        Estructura estructuraSelected = (Estructura) estructuraTable.getRowData();
        List<Estructura> estructuras = new ArrayList<Estructura>();
        Estructura estructura;
        Long i=0L;
        
        Map<Long, GrillaModelVO> grillaModelMap = getGeneradorDiseno().getGrillaModelMap();
        Map<Long, GrillaModelVO> grillaModelPasoMap = new LinkedHashMap<Long, GrillaModelVO>();
                
        for(Estructura estructuraI : getEstructuraList()){
            i++;
            
            if(getGeneradorDiseno().getGrillaModelMap().containsKey(estructuraI.getOrden()))
                grillaModelPasoMap.put(i, getGeneradorDiseno().getGrillaModelMap().get(estructuraI.getOrden()));
            else
                grillaModelPasoMap.put(i, new GrillaModelVO());
            
            estructuraI.setOrden(i);
            estructuras.add(estructuraI);

            if(estructuraI.getOrden().equals(estructuraSelected.getOrden())){                
                i++;
                grillaModelPasoMap.put(i, new GrillaModelVO());
                estructura = new Estructura();
                estructura.setOrden(i);
                estructuras.add(estructura);
            }

        }
        grillaModelMap.putAll(grillaModelPasoMap);
        grillaModelPasoMap.clear();
        setEstructuraList(estructuras);
    }
    
    /*
     * Metodo que busca las estructura al hacer clink en el icono cargar
     */
    public void buscarEstructuraActionListener(ActionEvent event){
        
    	if(Util.esListaValida(getVersionList()) && getVersionList().get(getVersionList().size()-1).getVigencia().equals(1L)){
            try {
                
                Version version = (Version)event.getComponent().getAttributes().get("celda");
                
                if(version==null)
                    return;

                List<Estructura> estructuras = getFacadeService().getEstructuraService().findEstructuraByVersion(version);
                
                if(estructuras==null || estructuras.size()==0){
                        this.setEstructuraList(null);
                        return;
                }

                getGeneradorDiseno().setGrillaModelMap(GeneradorDisenoHelper.createGrillaModel(estructuras));
                
                setAlmacenado(true);
                setRenderBotonEditar(false);
                this.setEstructuraList(estructuras);
                
            } catch (Exception e) {
                addErrorMessage("Error","Error al obtener información");
                LOG.error(e.getMessage(),e);
            }
            
            setRenderEstructura(true);
        }
    }
    
    public void guardarEstructuraListener(ActionEvent event){
        
        Long version = 1L;
        
        for(int i=0; i<versionList.size()-1; i++){
        	versionList.get(i).setCatalogo(catalogo);
        	versionList.get(i).setVigencia(0L);
        	versionList.get(i).setVersion(version);
            version++;
        }
        versionList.get(versionList.size()-1).setVigencia(1L);
        versionList.get(versionList.size()-1).setCatalogo(catalogo);
        versionList.get(versionList.size()-1).setVersion(version);
        
        /*Se limpia modelo de grillas*/
        getGeneradorDiseno().initBackingBean();
        
        for(Estructura estructura : getEstructuraList()){
            estructura.setVersion(versionList.get(versionList.size()-1));
            
            if(getGeneradorDiseno().getGrillaModelMap().containsKey(estructura.getOrden())){
                getGeneradorDiseno().getGrillaModelMap().get(estructura.getOrden()).setTipoEstructura(estructura.getTipoEstructura().getIdTipoEstructura());
            }else{
                getGeneradorDiseno().getGrillaModelMap().put(estructura.getOrden(), new GrillaModelVO(estructura.getTipoEstructura().getIdTipoEstructura()));
            }
        }
        setAlmacenado(true);
    }
    
    public void agregarVersionListener(ActionEvent actionEvent) {
        
        getGeneradorDiseno().setGrillaModelMap(null);
        
         if(getVersionList().size() > 0){
             
             Version version = getVersionList().get(getVersionList().size()-1);
             
             if(version.getIdVersion()!=null){
                 for(Version versionPaso : getVersionList()){
                     versionPaso.setVigencia(0L);
                 }
                 setEstructuraList(null);
                 getVersionList().add(new Version(version.getVersion()+1,true,new Date(),1L));
                 setRenderEstructura(true);
             }
         }else{
             Version version = new Version(1L, true, new Date(),1L);
             versionList = new ArrayList<Version>();
             versionList.add(version);
             setRenderEstructura(true);
             setEstructuraList(null);
         }
         setAlmacenado(false);
         setRenderBotonEditar(true);
     }
    
    
    public boolean isRenderEstructura() {
		return renderEstructura;
	}

	public void setRenderEstructura(boolean renderEstructura) {
		this.renderEstructura = renderEstructura;
	}

	public boolean isRenderBotonEditar() {
		return renderBotonEditar;
	}

	public void setRenderBotonEditar(boolean renderBotonEditar) {
		this.renderBotonEditar = renderBotonEditar;
	}

	public DataTable getEstructuraTable() {
		return estructuraTable;
	}

	public void setEstructuraTable(DataTable estructuraTable) {
		this.estructuraTable = estructuraTable;
	}

	public boolean isAlmacenado() {
		return almacenado;
	}

	public void setAlmacenado(boolean almacenado) {
		this.almacenado = almacenado;
	}

	public void setEstructuraList(List<Estructura> estructuraList) {
		this.estructuraList = estructuraList;
	}
    
    public TipoCuadro getTipoCuadro() {
		return tipoCuadro;
	}
	public void setTipoCuadro(TipoCuadro tipoCuadro) {
		this.tipoCuadro = tipoCuadro;
	}
	public Catalogo getCatalogo() {
		return catalogo;
	}
	public void setCatalogo(Catalogo catalogo) {
		this.catalogo = catalogo;
	}

	public List<Catalogo> getCatalogoList() {
		return catalogoList;
	}
	

	public void setCatalogoList(List<Catalogo> catalogoList) {

		this.catalogoList = catalogoList;
	}

	public List<Version> getVersionList() {
		return versionList;
	}

	public void setVersionList(List<Version> versionList) {
		this.versionList = versionList;
	}
	
	public Long getIdTipoCuadro() {
		return idTipoCuadro;
	}

	public void setIdTipoCuadro(Long idTipoCuadro) {
		this.idTipoCuadro = idTipoCuadro;
	}
	

}
