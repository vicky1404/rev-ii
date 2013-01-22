package cl.mdr.ifrs.cross.converter;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.log4j.Logger;



import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.ejb.entity.EstadoFinanciero;
import cl.mdr.ifrs.ejb.reporte.util.SoporteReporte;


import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@FacesConverter(value = "estadoFinancieroConverter")
public class EstadoFinancieroConverter extends AbstractBackingBean implements Converter{

	
private static final Logger LOGGER = Logger.getLogger(EstadoFinancieroConverter.class);
	
	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String string) {		
		
		EstadoFinanciero estadoFinanciero = new EstadoFinanciero();
		
		if(!Strings.isNullOrEmpty(string)){
			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			
			try {
				estadoFinanciero = gson.fromJson(string, EstadoFinanciero.class);
						
			} catch (final Exception e) {
				e.printStackTrace();
				LOGGER.error("Error con conversion de valor " + string);
			}
		}
		
			return estadoFinanciero;
			
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object object) {
		
		String json = "";
		
		if(object instanceof EstadoFinanciero){
			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			json = gson.toJson(object, EstadoFinanciero.class);
		}

		return json;
	}

	
}
