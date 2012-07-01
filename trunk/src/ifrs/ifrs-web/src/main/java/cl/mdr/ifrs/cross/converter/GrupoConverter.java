package cl.mdr.ifrs.cross.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.Grupo;

import com.google.common.base.Strings;

@FacesConverter(value = "grupoConverter")
public class GrupoConverter implements Converter {	
	
	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String string) {		
		Grupo grupo = null;
		if(!Strings.isNullOrEmpty(string)){
			grupo = new Grupo(string);
		}
		return grupo;
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object object) {		
		String id = "";		
		if(object instanceof Grupo){
			id = Util.getString(((Grupo)object).getIdGrupoAcceso(), "");
		}		
		return id;
	}

}
