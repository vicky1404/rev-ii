package cl.mdr.ifrs.cross.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.TipoCuadro;

import com.google.common.base.Strings;

@FacesConverter(value = "tipoCuadroConverter")
public class TipoCuadroConverter implements Converter{
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String string) {
		TipoCuadro tipoCuadro = null;
		if(!Strings.isNullOrEmpty(string)){
			tipoCuadro = new TipoCuadro(Util.getLong(string, 0L));
		}
		return tipoCuadro;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object object) {			
		String id = "";		
		if(object instanceof TipoCuadro){
			id = Util.getString(((TipoCuadro)object).getIdTipoCuadro(), "");
		}		
		return id;
	}

}
