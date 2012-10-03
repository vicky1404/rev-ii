package cl.bicevida.revelaciones.converter;


import cl.bicevida.revelaciones.ejb.entity.TipoEstructura;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class TipoEstructuraConverter implements Converter{
    
    @Override        
    public Object getAsObject(final FacesContext arg0, final UIComponent arg1, final String string) {
            
        TipoEstructura tipo = null;
            
        if(string!=null || string.length()!=0){
            try{
                Long id = Long.valueOf(string);
                tipo = new TipoEstructura(id);
            }catch(NumberFormatException e){
                e.printStackTrace(); 
            }catch(Exception e){
                e.printStackTrace();
            }
            
        }
        return tipo;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object object) {
        String idStr = "";
        if(object instanceof TipoEstructura){
            try{
                idStr = ((TipoEstructura)object).getIdTipoEstructura().toString();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return idStr;
    }

}
