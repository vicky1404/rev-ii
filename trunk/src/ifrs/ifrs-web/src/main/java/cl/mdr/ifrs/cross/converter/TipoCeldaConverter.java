package cl.mdr.ifrs.cross.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.log4j.Logger;

import cl.mdr.ifrs.ejb.entity.TipoCelda;

import com.google.common.base.Strings;
import com.google.gson.Gson;

@FacesConverter(value = "tipoCeldaConverter", forClass= TipoCelda.class)
public class TipoCeldaConverter implements Converter{
	
	private static final Logger LOGGER = Logger.getLogger(TipoCeldaConverter.class);
	
	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String string) {		
		
		TipoCelda tipoCelda = null;
		
		if(!Strings.isNullOrEmpty(string)){
			final Gson gson = new Gson();
			try {
				tipoCelda = gson.fromJson(string, TipoCelda.class);
			} catch (final Exception e) {
				LOGGER.error("Error con conversion de valor " + string);
			}
		}
		return tipoCelda;
		
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object object) {
		
		String json = "";
		
		if(object instanceof TipoCelda){
			final Gson gson = new Gson();
			json = gson.toJson(object, TipoCelda.class);
		}

		return json;
	}

}
