package cl.bicevida.revelaciones.common.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;


public class BeanUtil{
    
    public BeanUtil(){
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
    
    public static void sortSelectItemMeses(List<SelectItem> meses){
        Collections.sort(meses, new Comparator<SelectItem>(){                        
            public int compare(SelectItem s1, SelectItem s2){ 
                Integer i1 = Integer.parseInt(s1.getLabel());
                Integer i2 = Integer.parseInt(s2.getLabel());
                return i1.compareTo(i2);
            }
        });
    }
    
}
