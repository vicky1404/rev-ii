package cl.mdr.ifrs.ejb.common;

public enum TipoImpresionEnum {
    
    LANDSCAPE(1L, "HORIZONTAL"),
    PORTRAIT(0L, "VERTICAL");
    
    private Long key;
    private String value;        
    
    private TipoImpresionEnum(Long key, String value) {
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
}
