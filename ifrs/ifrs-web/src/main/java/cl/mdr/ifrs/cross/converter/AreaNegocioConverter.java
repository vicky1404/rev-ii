package cl.mdr.ifrs.cross.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.log4j.Logger;

import cl.mdr.ifrs.ejb.entity.AreaNegocio;

import com.google.common.base.Strings;

@FacesConverter(value = "areaNegocioConverter")
public class AreaNegocioConverter implements Converter {
	
	private static final Logger LOGGER = Logger.getLogger(AreaNegocioConverter.class);
	
	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String string) {		
		
		AreaNegocio areaNegocio = new AreaNegocio();
		
		if(!Strings.isNullOrEmpty(string)){			
			try {
				areaNegocio = new AreaNegocio(string);
			} catch (final Exception e) {
				LOGGER.error("Error con conversion de valor " + string);
			}
		}
		return areaNegocio;
		
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object object) {		
		String id = "";		
		if(object instanceof AreaNegocio){
			id = ((AreaNegocio) object).getIdAreaNegocio();
		}
		return id;
	}

}
