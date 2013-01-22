package cl.mdr.ifrs.cross.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.log4j.Logger;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.ejb.entity.Catalogo;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@FacesConverter(value = "catalogoConverter")
public class CatalogoConverter extends AbstractBackingBean implements Converter {
	
	private static final Logger LOGGER = Logger.getLogger(CatalogoConverter.class);
	
	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String string) {		
		
		Catalogo catalogo = null;
		
		if(!Strings.isNullOrEmpty(string)){
			final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			try {
				catalogo = gson.fromJson(string, Catalogo.class);
				
			} catch (final Exception e) {
				LOGGER.error("Error con conversion de valor " + string);
			}
		}
		return catalogo;
		
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object object) {
		
		String json = "";
		
		if(object instanceof Catalogo){
			final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

			json = gson.toJson(object, Catalogo.class);
		}

		return json;
	}

}
