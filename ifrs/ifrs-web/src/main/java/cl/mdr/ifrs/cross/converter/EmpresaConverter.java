package cl.mdr.ifrs.cross.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.log4j.Logger;

import cl.mdr.ifrs.ejb.entity.Empresa;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@FacesConverter(value = "empresaConverter")
public class EmpresaConverter implements Converter {
	
	private static final Logger LOGGER = Logger.getLogger(EmpresaConverter.class);
	
	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String string) {		
		
		Empresa empresa = null;
		
		if(!Strings.isNullOrEmpty(string)){
			final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			try {
				empresa = gson.fromJson(string, Empresa.class);
			} catch (final Exception e) {
				LOGGER.error("Error con conversion de valor " + string);
			}
		}
		return empresa;
		
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object object) {
		
		String json = "";
		
		if(object instanceof Empresa){
			final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			json = gson.toJson(object, Empresa.class);
		}

		return json;
	}

}
