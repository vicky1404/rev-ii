package cl.mdr.ifrs.cross.util;

import java.io.Serializable;
import java.util.Map;

import javax.faces.context.FacesContext;

public class GuestPreferences implements Serializable {
	private static final long serialVersionUID = 8606785664671781743L;
	private String theme = "vader"; // default

	public String getTheme() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		if (params.containsKey("theme")) {
			theme = params.get("theme");
		}
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}
}
