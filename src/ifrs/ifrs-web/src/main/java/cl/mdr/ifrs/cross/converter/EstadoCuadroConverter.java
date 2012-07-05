package cl.mdr.ifrs.cross.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.log4j.Logger;

import cl.mdr.ifrs.ejb.entity.Catalogo;
import cl.mdr.ifrs.ejb.entity.EstadoCuadro;

import com.google.common.base.Strings;
import com.google.gson.Gson;


public class EstadoCuadroConverter implements Converter {
	
	private static final Logger LOGGER = Logger.getLogger(EstadoCuadroConverter.class);
	
	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String string) {		
		
		EstadoCuadro estadoCuadro = new EstadoCuadro();
		
		if(!Strings.isNullOrEmpty(string)){
			final Gson gson = new Gson();
			try {
				estadoCuadro = gson.fromJson(string, EstadoCuadro.class);
			} catch (final Exception e) {
				LOGGER.error("Error con conversion de valor " + string);
			}
		}
		return estadoCuadro;
		
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object object) {
		
		String json = "";
		
		if(object instanceof EstadoCuadro){
			final Gson gson = new Gson();
			json = gson.toJson(object, EstadoCuadro.class);
		}

		return json;
	}

}
