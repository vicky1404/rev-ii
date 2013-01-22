package cl.mdr.ifrs.cross.mb;

import java.util.ResourceBundle;

public class PropertyManager {


	private static PropertyManager instance;
	static ResourceBundle resourceBundle;
	
	public static PropertyManager getInstance(){
			
		if (instance == null){
			instance = new PropertyManager();
		}
		
		if (resourceBundle == null){
			resourceBundle = ResourceBundle.getBundle("ifrs_es_CL");
		}
		
		return instance;
	}
	
	public String getMessage(final String key){
		return resourceBundle.getString(key);
	}
}
