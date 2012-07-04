package cl.mdr.ifrs.cross.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.log4j.Logger;

import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.Catalogo;
import cl.mdr.ifrs.ejb.entity.TipoCuadro;

import com.google.common.base.Strings;
import com.google.gson.Gson;

@FacesConverter(value = "tipoCuadroConverter", forClass= TipoCuadro.class)
public class TipoCuadroConverter implements Converter{
	
	private static final Logger LOGGER = Logger.getLogger(TipoCuadroConverter.class);
	
	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String string) {		
		
		TipoCuadro tipoCuadro = null;
		
		if(!Strings.isNullOrEmpty(string)){
			final Gson gson = new Gson();
			try {
				tipoCuadro = gson.fromJson(string, TipoCuadro.class);
			} catch (final Exception e) {
				LOGGER.error("Error con conversion de valor " + string);
			}
		}
		return tipoCuadro;
		
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object object) {
		
		String json = "";
		
		if(object instanceof TipoCuadro){
			final Gson gson = new Gson();
			json = gson.toJson(object, TipoCuadro.class);
		}

		return json;
	}

}
