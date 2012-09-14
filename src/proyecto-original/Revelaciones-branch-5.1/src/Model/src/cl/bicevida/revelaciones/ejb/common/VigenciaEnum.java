package cl.bicevida.revelaciones.ejb.common;

public enum VigenciaEnum {

    VIGENTE(1L, "Vigente"),
    NO_VIGENTE(0L, "No Vigente");

    private Long key;
    private String value;

    private VigenciaEnum(Long key, String value) {
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
