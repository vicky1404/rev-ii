package cl.mdr.ifrs.ejb.common;

public enum EstadoCuadroEnum {
    
    INICIADO(0L, "Iniciado", true),
    MODIFICADO(1L, "Modificado", true),
    POR_APROBAR(2L, "Por Aprobar", true),
    APROBADO(3L, "Aprobado", true),
    CERRADO(4L, "Cerrado", true),
    CONTINGENCIA(5L, "Contingencia", true);
    
    private Long key;
    private String value;
    private boolean vigente;

    private EstadoCuadroEnum(final Long key, final String value, final boolean vigente){
        this.key = key;
        this.value = value;
        this.vigente = vigente;                    
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

    public void setVigente(boolean vigente) {
        this.vigente = vigente;
    }

    public boolean isVigente() {
        return vigente;
    }
}
