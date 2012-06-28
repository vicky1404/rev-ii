package cl.mdr.ifrs.modules.mb;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.ejb.entity.Catalogo;
import cl.mdr.ifrs.ejb.entity.TipoCuadro;
import cl.mdr.ifrs.ejb.entity.Version;


@ManagedBean
@ViewScoped
public class GeneradorVersionBackingBean extends AbstractBackingBean{
	
	private TipoCuadro tipoCuadro;
	private Catalogo catalogo;
	private Long idCatalogo;
	private List<Catalogo> catalogoList;
	private List<Version> versionList;
	
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
		
		if(catalogoList==null)
			return suggestions;
		
        
        for(Catalogo p : catalogoList) {  
        	if(p.getNombre().toUpperCase().indexOf(query.toUpperCase()) >= 0)
                suggestions.add(p);  
        }  
          
        return suggestions;  
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
	
	public void tipoCatalogoChangeListener(){
		try{
			if(tipoCuadro!=null)
				catalogoList = getFacadeService().getCatalogoService().findCatalogoByFiltro(getNombreUsuario(), tipoCuadro, null, 1L);
		}catch(Exception e){
			addErrorMessage("Error al buscar catalogo", "Error al buscar catalogo");
			e.printStackTrace();
		}
		
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
	

}
