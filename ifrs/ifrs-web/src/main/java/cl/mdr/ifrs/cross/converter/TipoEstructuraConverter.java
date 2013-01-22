package cl.mdr.ifrs.cross.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.log4j.Logger;

import cl.mdr.ifrs.ejb.entity.TipoEstructura;

import com.google.common.base.Strings;
import com.google.gson.Gson;

@FacesConverter(value = "tipoEstructuraConverter", forClass= TipoEstructura.class)
public class TipoEstructuraConverter implements Converter{
	
private static final Logger LOGGER = Logger.getLogger(CatalogoConverter.class);
	
	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String string) {		
		
		TipoEstructura tipoEstructura = null;
		
		if(!Strings.isNullOrEmpty(string)){
			final Gson gson = new Gson();
			try {
				tipoEstructura = gson.fromJson(string, TipoEstructura.class);
			} catch (final Exception e) {
				LOGGER.error("Error con conversion de valor " + string);
			}
		}
		return tipoEstructura;
		
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object object) {
		
		String json = "";
		
		if(object instanceof TipoEstructura){
			final Gson gson = new Gson();
			json = gson.toJson(object, TipoEstructura.class);
		}

		return json;
	}

}
