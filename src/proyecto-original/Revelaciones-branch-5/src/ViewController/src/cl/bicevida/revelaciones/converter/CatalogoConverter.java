package cl.bicevida.revelaciones.converter;


import cl.bicevida.revelaciones.ejb.entity.Catalogo;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class CatalogoConverter implements Converter{
        
    @Override        
    public Object getAsObject(final FacesContext arg0, final UIComponent arg1, final String string) {
            
        Catalogo nota = null;
            
        if(string!=null || string.length()!=0){
            String valor = string.trim();
            try{
                if(valor.contains("ID:")){
                    Long id = Long.valueOf(valor.substring(valor.indexOf("ID:")));
                    nota = new Catalogo(id);
                }
            }catch(NumberFormatException e){
                e.printStackTrace(); 
            }catch(Exception e){
                e.printStackTrace();
            }
            
        }
        return nota;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object object) {
        if(object!=null)
            return ((Catalogo)object).getNombre();
        else
            return "";
    }
}
