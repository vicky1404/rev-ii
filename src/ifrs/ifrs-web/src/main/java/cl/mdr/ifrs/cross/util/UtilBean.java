package cl.mdr.ifrs.cross.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class UtilBean {
	
    @SuppressWarnings({"unchecked"})
    public static <T> T findBean(String beanName) {
        FacesContext ctx = FacesContext.getCurrentInstance();               
        StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("#{");
		stringBuilder.append(beanName);
		stringBuilder.append("}");
		return (T)ctx.getApplication().evaluateExpressionGet(ctx, stringBuilder.toString() , Object.class);      
    }
    
    public static void sortSelectItemMeses(List<SelectItem> meses){
        Collections.sort(meses, new Comparator<SelectItem>(){                        
            public int compare(SelectItem s1, SelectItem s2){ 
                Integer i1 = Integer.parseInt(s1.getLabel());
                Integer i2 = Integer.parseInt(s2.getLabel());
                return i2.compareTo(i1);
            }
        });
    }
    
    public static void sortSelectItemAnios(List<SelectItem> anios){
        Collections.sort(anios, new Comparator<SelectItem>(){                        
            public int compare(SelectItem s1, SelectItem s2){ 
                Integer i1 = Integer.parseInt(s1.getLabel());
                Integer i2 = Integer.parseInt(s2.getLabel());
                return i2.compareTo(i1);
            }
        });
    }

}
