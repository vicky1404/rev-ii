package cl.mdr.ifrs.modules.mb;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.ejb.entity.Catalogo;
import cl.mdr.ifrs.ejb.entity.TipoCuadro;


@ManagedBean
@ViewScoped
public class GeneradorVersionBackingBean extends AbstractBackingBean{
	
	private TipoCuadro tipoCuadro;
	private Catalogo catalogo;
	private List<Catalogo> catalogoList;
	
	
	public List<Catalogo> completeCatalogo(String query) {  
        List<Catalogo> suggestions = new ArrayList<Catalogo>();  
          
        for(Catalogo p : getCatalogoList()) {  
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
		if(catalogoList==null){
			catalogoList = getFacadeService().getCatalogoService().findCatalogoVigenteAll();
		}
		return catalogoList;
	}

	public void setCatalogoList(List<Catalogo> catalogoList) {
		this.catalogoList = catalogoList;
	}
	

}
