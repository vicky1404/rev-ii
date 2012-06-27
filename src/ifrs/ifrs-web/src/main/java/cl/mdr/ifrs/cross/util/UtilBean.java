package cl.mdr.ifrs.cross.util;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class UtilBean {
	
    @SuppressWarnings({"unchecked"})
    public static <T> T findBean(String name) {
        FacesContext ctx = FacesContext.getCurrentInstance();               
        return (T)ctx.getApplication().evaluateExpressionGet(ctx, MessageFormat.format("#{{0}}", name) , Object.class);      
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
