package cl.mdr.ifrs.cross.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.log4j.Logger;

import cl.mdr.ifrs.ejb.entity.Grupo;

import com.google.common.base.Strings;
import com.google.gson.Gson;

public class GrupoConverter implements Converter {
	private transient Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String string) {
		
		Grupo grupo = null;
		
		if(!Strings.isNullOrEmpty(string)){
			final Gson gson = new Gson();
			try {
				grupo = gson.fromJson(string, Grupo.class);
			} catch (final Exception e) {
				logger.error("Error con conversion de valor " + string, e);
			}
		}
		return grupo;
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object object) {
		
		String json = "";
		
		if(object instanceof Grupo){
			final Gson gson = new Gson();
			json = gson.toJson(object, Grupo.class);
		}
		return json;
	}

}
