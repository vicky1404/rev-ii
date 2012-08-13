package cl.mdr.ifrs.cross.converter;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.log4j.Logger;



import cl.mdr.ifrs.ejb.entity.EstadoFinanciero;
import cl.mdr.ifrs.ejb.entity.RelacionDetalleEeff;
import cl.mdr.ifrs.ejb.entity.RelacionEeff;



import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@FacesConverter(value = "relacionDetalleEeffConverter")
public class RelacionDetalleEeffConverter implements Converter{

	
private static final Logger LOGGER = Logger.getLogger(RelacionDetalleEeffConverter.class);
	
	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String string) {		
		
		RelacionDetalleEeff relacionDetalleEeff = new RelacionDetalleEeff();
		
		
		if(!Strings.isNullOrEmpty(string)){
			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			
			try {
				relacionDetalleEeff = gson.fromJson(string, RelacionDetalleEeff.class);
						
			} catch (final Exception e) {
				LOGGER.error("Error con conversion de valor " + string);
			}
		}
		
			return relacionDetalleEeff;
			
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object object) {
		
		String json = "";
		
		if(object instanceof RelacionDetalleEeff){
			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			json = gson.toJson(object, RelacionDetalleEeff.class);
		}

		return json;
	}

	
}
