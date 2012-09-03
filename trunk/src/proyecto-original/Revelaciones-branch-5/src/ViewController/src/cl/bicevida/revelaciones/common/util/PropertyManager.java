package cl.bicevida.revelaciones.common.util;

import java.util.ResourceBundle;

public class PropertyManager{

    private static PropertyManager instance;
    static ResourceBundle resourceBundle = null;
    
    
    public static PropertyManager getInstance(){
        if(instance == null){
            instance = new PropertyManager();
        }
        if(resourceBundle == null){
            resourceBundle = ResourceBundle.getBundle("cl.bicevida.revelaciones.messages.revelaciones_es_CL");
        }
        return instance;
    }

    /**
     * Obtiene un mensaje a traves de su key desde el archivo de properties
     * @param key
     * @return
     */
    public String getMessage(final String key){
        return resourceBundle.getString(key);
    }
    
    
    
}