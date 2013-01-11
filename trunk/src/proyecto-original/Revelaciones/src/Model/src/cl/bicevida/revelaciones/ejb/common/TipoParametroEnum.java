package cl.bicevida.revelaciones.ejb.common;

import cl.bicevida.revelaciones.ejb.entity.TipoParametro;

public enum TipoParametroEnum {
    
	MAIL_CONFIG(1L,"MAIL_CONFIG");   
    
    private Long key;
    private String value;
    
    private TipoParametroEnum(Long key, String value){
            this.key = key;
            this.value = value;
    }       

    public Long getKey() {
            return key;
    }

    public String getValue() {
		return value;
	}
	
    public TipoParametro getTipoParametro() {
        return new TipoParametro(this.getKey());
    }

	
}
