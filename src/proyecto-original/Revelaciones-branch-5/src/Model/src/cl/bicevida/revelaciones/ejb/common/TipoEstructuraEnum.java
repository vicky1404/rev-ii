package cl.bicevida.revelaciones.ejb.common;

import cl.bicevida.revelaciones.ejb.entity.TipoEstructura;

public enum TipoEstructuraEnum {
    
    GRILLA(0L,"Grilla"),
    HTML(1L,"HTML"),
    TEXTO(2L, "Texto");
    
    private Long key;
    private String clase;
    
    private TipoEstructuraEnum(Long key, String clase){
            this.key = key;
            this.clase = clase;
    }       

    public Long getKey() {
            return key;
    }

    public String getClase() {
            return clase;
    }

    public TipoEstructura getTipoEstructura() {
            return new TipoEstructura(this.getKey());
    }
}
