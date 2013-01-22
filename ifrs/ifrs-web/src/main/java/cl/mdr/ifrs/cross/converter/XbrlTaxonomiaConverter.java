package cl.mdr.ifrs.cross.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.log4j.Logger;

import cl.mdr.exfida.xbrl.ejb.entity.XbrlTaxonomia;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@FacesConverter(value = "xbrlTaxonomiaConverter")
public class XbrlTaxonomiaConverter implements Converter {
	private static final Logger LOGGER = Logger.getLogger(XbrlTaxonomiaConverter.class);

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,String string) {
		XbrlTaxonomia taxonomia = null;
				
		if(!Strings.isNullOrEmpty(string)){
			//final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			final Gson gson = new GsonBuilder().create();
			try {
				taxonomia = gson.fromJson(string, XbrlTaxonomia.class);
			} catch (final Exception e) {
				LOGGER.error("Error con conversion de valor " + string);
			}
		}
		return taxonomia;		
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object object) {
		String json = "";
		
		if(object instanceof XbrlTaxonomia){
			final Gson gson = new GsonBuilder().create();

			json = gson.toJson(object, XbrlTaxonomia.class);
		}

		return json;		
	}

}
