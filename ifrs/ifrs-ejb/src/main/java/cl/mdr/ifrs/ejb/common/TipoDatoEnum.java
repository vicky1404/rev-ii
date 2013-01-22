package cl.mdr.ifrs.ejb.common;

import cl.mdr.ifrs.ejb.entity.TipoDato;

public enum TipoDatoEnum {
    
    TEXTO(0L,"java.lang.String"),
    FECHA(1L,"java.util.Date"),
    ENTERO(2L,"java.lang.Long"),
    DECIMAL(3L,"java.math.BigDecimal");
    
    private Long key;
    private String clase;
    
    private TipoDatoEnum(Long key, String clase){
            this.key = key;
            this.clase = clase;
    }       

    public Long getKey() {
            return key;
    }

    public String getClase() {
            return clase;
    }

    public TipoDato getTipoDato() {
            return new TipoDato(this.getKey());
    }
}
