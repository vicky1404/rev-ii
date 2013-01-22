package cl.mdr.ifrs.cross.mb;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;

import cl.mdr.ifrs.cross.util.GuestPreferences;

@ManagedBean
@SessionScoped
public class ThemeBackingBean implements Serializable {
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private static final long serialVersionUID = 6071533611276140673L;
	
	//theme
    private Map<String, String> themes;
    private String theme = "bluesky";
    private GuestPreferences gp = new GuestPreferences();
    
	@PostConstruct  
    public void init() {                   
        themes = new TreeMap<String, String>();  
        themes.put("Aristo", "aristo");          
        themes.put("Bluesky", "bluesky");        
    }

	public Map<String, String> getThemes() {
		return themes;
	}

	public void setThemes(Map<String, String> themes) {
		this.themes = themes;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	} 
	
	public void saveTheme() {
        logger.info("theme: " + theme);
        gp.setTheme(theme);
    }

	public GuestPreferences getGp() {
		return gp;
	}

	public void setGp(GuestPreferences gp) {
		this.gp = gp;
	}

}
