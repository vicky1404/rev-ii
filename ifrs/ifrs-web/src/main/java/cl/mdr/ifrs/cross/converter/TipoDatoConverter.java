package cl.mdr.ifrs.cross.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.log4j.Logger;

import cl.mdr.ifrs.ejb.entity.TipoDato;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@FacesConverter(value = "tipoDatoConverter", forClass= TipoDato.class)
public class TipoDatoConverter implements Converter{
	
	private static final Logger LOGGER = Logger.getLogger(TipoDatoConverter.class);
	
	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String string) {		
		
		TipoDato tipoDato = null;
		
		if(!Strings.isNullOrEmpty(string)){
			final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			try {
				tipoDato= gson.fromJson(string, TipoDato.class);
			} catch (final Exception e) {
				LOGGER.error("Error con conversion de valor " + string);
			}
		}
		return tipoDato;
		
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object object) {
		
		String json = "";
		
		if(object instanceof TipoDato){
			final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			json = gson.toJson(object, TipoDato.class);
		}

		return json;
	}

}
