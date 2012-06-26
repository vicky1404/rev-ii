package cl.mdr.ifrs.ejb.common;

import cl.mdr.ifrs.ejb.entity.TipoCelda;



public enum TipoCeldaEnum {
    
    TEXTO(0L,"TEXTO"),
    NUMERO(1L,"NUMERO"),
    TITULO(2L,"TITULO"),
    LINK(3L,"LINK"),
    TOTAL(4L,"TOTAL"),
    SUBTOTAL(5L,"SUBTOTAL"),
    TEXTO_EDITABLE(6L,"TEXTO EDITABLE");

    private Long key;
    private String value;
    
    private TipoCeldaEnum(Long key, String value){
       this.key = key;
       this.value = value;
    }
    
    public void setKey(Long key) {
        this.key = key;
    }

    public Long getKey() {
        return key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
    public TipoCelda getTipoCelda() {
        return new TipoCelda(this.getKey());
    }
}
