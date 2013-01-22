package cl.mdr.ifrs.cross.converter;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.log4j.Logger;



import cl.mdr.ifrs.ejb.entity.EstadoFinanciero;
import cl.mdr.ifrs.ejb.entity.RelacionEeff;


import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@FacesConverter(value = "relacionEeffConverter")
public class RelacionEeffConverter implements Converter{

	
private static final Logger LOGGER = Logger.getLogger(RelacionEeffConverter.class);
	
	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String string) {		
		
		RelacionEeff relacionEeff = new RelacionEeff();
		
		if(!Strings.isNullOrEmpty(string)){
			final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			try {
				relacionEeff = gson.fromJson(string, RelacionEeff.class);
						
			} catch (final Exception e) {
				LOGGER.error("Error con conversion de valor " + string);
			}
		}
		
			return relacionEeff;
			
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object object) {
		
		String json = "";
		
		if(object instanceof RelacionEeff){
			final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			json = gson.toJson(object, RelacionEeff.class);
		}

		return json;
	}

	
}
