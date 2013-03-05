package cl.bicevida.revelaciones.common.util;

import javax.faces.context.FacesContext;


public class Util{
    
    public Util(){
        super();
    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    public static Float getFloat(String arg1, Float arg2){
        try{
            return (Float.parseFloat(arg1));
        }catch(NumberFormatException e){
            return arg2; 
        }    
    }
    @SuppressWarnings("unchecked")
    public static <T> T findBean(String name) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        return (T)ctx.getApplication().evaluateExpressionGet(ctx, "#{" + name + "}", Object.class);      
    }
}
