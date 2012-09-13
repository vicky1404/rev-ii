package cl.mdr.ifrs.ejb.common;

public enum TipoEstadoEeffEnum {

    INGRESADO(1L,"INGRESADO"),
    APROVADO(2L,"APROVADO");
    

    private Long key;
    private String value;

    private TipoEstadoEeffEnum(Long key, String value) {
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
